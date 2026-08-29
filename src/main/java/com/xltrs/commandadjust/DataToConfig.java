package com.xltrs.commandadjust;

import java.util.ArrayList;
import java.util.List;
//这里就不用注释了，就24行代码awa
public class DataToConfig {
    public static void Save(String CommandName, int CommandLevel) {
        List<String> CommandList = new ArrayList<>(Config.CommandModifyList.get());
        CommandList.removeIf(e -> e.startsWith(CommandName + ":"));
        CommandList.add(CommandName + ":" + CommandLevel);
        Config.CommandModifyList.set(CommandList);
        Config.SPEC.save();
        Debug.show("[DataToConfig] Modify %s command at level %s Success", CommandName, CommandLevel);
    }

    public static void Delete(String CommandName) {
        List<String> CommandList = new ArrayList<>(Config.CommandModifyList.get());
        CommandList.removeIf(e -> e.startsWith(CommandName + ":"));
        Config.CommandModifyList.set(CommandList);
        Config.SPEC.save();
        Debug.show("[DataToConfig] Delete %s command config Success", CommandName);
    }
}
