package io.github.stampede2011.psplayerwarps.config;

import com.google.common.collect.Maps;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Map;

@ConfigSerializable
public class Storage {

    @Setting
    public Map<String, WarpType> warps = Maps.newHashMap();



}
