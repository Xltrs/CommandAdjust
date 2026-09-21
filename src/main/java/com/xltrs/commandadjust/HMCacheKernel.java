package com.xltrs.commandadjust;
//HashMap Cache Kernel，用于处理本模组的基础hashmap缓存

import java.util.HashMap;
import java.util.Map;

public class HMCacheKernel {
    private static final Map<String, Integer> CommandRawLevel = new HashMap<>();
    private static final Map<String, Integer> CommandConfigLevel = new HashMap<>();

    //这个是刷新所有数据用的，不然呢？awa
    public static void ReloadCache() {
        Logshow.debug("[HMCacheKernel] Reload Cache");
        CommandRawLevel.clear();
        CommandConfigLevel.clear();
        BuildCCLData();
    }

    //==================================原始指令等级部分逻辑=========================================
    public static void AddCRLData(String CommandName, Integer CommandLevel) {
        CommandRawLevel.put(CommandName, CommandLevel);
    }

    public static Integer GetCRLData(String CommandName) {
        return CommandRawLevel.get(CommandName);
    }

    //================================配置指令等级部分逻辑=========================================
    public static void AddCCLData(String CommandName, Integer CommandLevel) {
        CommandConfigLevel.put(CommandName, CommandLevel);
    }

    public static void DeleteCCLData(String CommandName) {
        CommandConfigLevel.remove(CommandName);
    }

    public static Integer GetCCLData(String CommandName) {
        return CommandConfigLevel.get(CommandName);
    }

    public static void BuildCCLData() {
        Logshow.debug("[HMCacheKernel] Build command config level cache");
        for (String entry : Config.CommandModifyList.get()) {
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                try {
                    CommandConfigLevel.put(parts[0], Integer.parseInt(parts[1]));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


