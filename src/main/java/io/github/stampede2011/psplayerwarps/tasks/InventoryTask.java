package io.github.stampede2011.psplayerwarps.tasks;

import com.codehusky.huskyui.StateContainer;
import com.codehusky.huskyui.states.Page;
import com.codehusky.huskyui.states.action.ActionType;
import com.codehusky.huskyui.states.action.runnable.RunnableAction;
import com.codehusky.huskyui.states.element.ActionableElement;
import io.github.stampede2011.psplayerwarps.PSPlayerWarps;
import io.github.stampede2011.psplayerwarps.config.MainConfig;
import io.github.stampede2011.psplayerwarps.config.WarpType;
import io.github.stampede2011.psplayerwarps.utils.Utilities;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InventoryTask {
    private static PSPlayerWarps plugin = PSPlayerWarps.getInstance();

    public static void openWarps(Player player) throws ObjectMappingException {

        StateContainer container = new StateContainer();
        Page.PageBuilder main = Page.builder()
                .setAutoPaging(true)
                .setInventoryDimension(InventoryDimension.of(9, 6))
                .setTitle(Utilities.toText("&b&lPlayer Warps"));
        AtomicInteger currSlot = new AtomicInteger();

        Map<String,WarpType> sortedNewMap = plugin.storage.get().warps.entrySet().stream().sorted((e1,e2)->
        {
            Integer e1Size = e1.getValue().likes.size();
            Integer e2Size = e2.getValue().likes.size();
            return e2Size.compareTo(e1Size);
        })
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (e1, e2) -> e1, LinkedHashMap::new));

        sortedNewMap.forEach((key,val)->{
            main.putElement(currSlot.get(), new ActionableElement(
                    new RunnableAction(container, ActionType.CLOSE, "", context -> {
                        TeleportTask.teleport(player, val, val.displayName);
                    }),
                    getWarpItem(val)
            ));

            currSlot.getAndIncrement();
        });

//        for (Map.Entry<String, WarpType> entry : plugin.storage.get().warps.entrySet().stream().sorted()) {
//            WarpType warp = entry.getValue();
//            main.putElement(currSlot, new ActionableElement(
//                    new RunnableAction(container, ActionType.CLOSE, "", context -> {
//                        TeleportTask.teleport(player, warp, warp.displayName);
//                    }),
//                    getWarpItem(warp)
//            ));
//
//            currSlot++;
//        }

        container.setInitialState(main.build("main"));
        container.launchFor(player);
    }

    public static void openIcons(Player player, WarpType warp, String warpName) throws ObjectMappingException {

        StateContainer container = new StateContainer();
        Page.PageBuilder main = Page.builder()
                .setAutoPaging(true)
                .setInventoryDimension(InventoryDimension.of(9, 6))
                .setTitle(Utilities.toText("&8Icon selector"));
        for (Map.Entry<String, MainConfig.Settings.Icon> entry : plugin.config.get().settings.icons.entrySet()) {
            MainConfig.Settings.Icon icon = entry.getValue();
            main.putElement(icon.position - 1, new ActionableElement(
                    new RunnableAction(container, ActionType.CLOSE, "", context -> {
                        if (player.hasPermission(entry.getValue().permission)) {
                            warp.icon = entry.getKey();
                            plugin.storage.save();
                            player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ICON_SUCCESS.replace("%warp%", warpName), true));
                        } else {
                            player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_ICON_NO_PERMS, true));
                        }
                    }),
                    getIconItem(player, icon)
            ));
        }

        container.setInitialState(main.build("icons"));
        container.launchFor(player);
    }

    public static ItemStack getWarpItem(WarpType warp) {

        List<Text> loreList = getWarpDescription(warp);
        loreList.add(0, Text.EMPTY);
        loreList.add(Text.EMPTY);
        loreList.add(Utilities.toText("&f&l>> &bOwner: &f" + Utilities.getNameForUuid(warp.owner)));
        loreList.add(Utilities.toText("&f&l>> &bLocation: &f" + warp.location.world + ", " + warp.location.x + ", " + warp.location.y + ", " + warp.location.z));
        loreList.add(Utilities.toText("&f&l>> &bLikes: &f" + warp.likes.size()));
        loreList.add(Text.EMPTY);
        loreList.add(Utilities.toText("&b&l>> &bClick to teleport!"));

        ItemType itemType = getItemType(warp.icon);

        ItemStack item = ItemStack.builder()
                .itemType(itemType)
                .add(Keys.DISPLAY_NAME, Utilities.toText("&b&l" + warp.displayName))
                .add(Keys.ITEM_LORE, loreList)
                .build();

        DataContainer cntr = item.toContainer();

        cntr.set(DataQuery.of("UnsafeDamage"), (plugin.config.get().settings.icons.containsKey(warp.icon) ? plugin.config.get().settings.icons.get(warp.icon).unsafeDamage : 0));

        if (plugin.config.get().settings.icons.containsKey(warp.icon))
            if (!plugin.config.get().settings.icons.get(warp.icon).nbt.equals(""))
                cntr.set(DataQuery.of("UnsafeData", "SpriteName"), plugin.config.get().settings.icons.get(warp.icon).nbt);

        item.setRawData(cntr);

        if (itemType.equals(ItemTypes.SKULL)) {
            item.offer(Keys.SKULL_TYPE, SkullTypes.PLAYER);
            if (Utilities.getUserForUuid(warp.owner).isPresent()) {
                item.offer(Keys.REPRESENTED_PLAYER, Utilities.getUserForUuid(warp.owner).get().getProfile());
            }
        }

        return item;
    }

    public static ItemType getItemType(String icon) {
        if (plugin.config.get().settings.icons.containsKey(icon)) {
            return plugin.config.get().settings.icons.get(icon).id;
        } else {
            return ItemTypes.SKULL;
        }
    }

    public static ItemStack getIconItem(Player player, MainConfig.Settings.Icon icon) {

        ItemStack item = ItemStack.builder()
                .itemType(icon.id)
                .add(Keys.DISPLAY_NAME, Utilities.toText(icon.name))
                .add(Keys.ITEM_LORE, icon.lore.stream().map(Utilities::toText).collect(Collectors.toList()))
                .build();

        DataContainer cntr = item.toContainer();

        cntr.set(DataQuery.of("UnsafeDamage"), icon.unsafeDamage);

        if (!icon.nbt.equals(""))
            cntr.set(DataQuery.of("UnsafeData", "SpriteName"), icon.nbt);

        item.setRawData(cntr);

        if (icon.id.equals(ItemTypes.SKULL)) {
            item.offer(Keys.SKULL_TYPE, SkullTypes.PLAYER);
            if (Utilities.getUserForUuid(player.getUniqueId()).isPresent()) {
                item.offer(Keys.REPRESENTED_PLAYER, Utilities.getUserForUuid(player.getUniqueId()).get().getProfile());
            }
        }

        return item;
    }

    public static List<Text> getWarpDescription(WarpType warp) {

        List<Text> output = new ArrayList();
        StringBuilder temp = new StringBuilder(warp.description.length());
        String splitArray[] = warp.description.split(" ");
        int currLine = 0;
        for (int i = 0; i < splitArray.length; i++) {
            String word = splitArray[i] + " ";
            temp.append(word);
            currLine += word.length();
            if (currLine > 35) {
                output.add(Text.builder()
                        .append(new Text[]{Utilities.toText("&f" + temp.toString())})
                        .build());
                temp = new StringBuilder(warp.description.length());
                currLine = 0;
            }
        }
        output.add(Text.builder()
                .append(new Text[]{Utilities.toText("&f" + temp.toString())})
                .build());
        return output;
    }
}
