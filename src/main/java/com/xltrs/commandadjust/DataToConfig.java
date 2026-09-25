package com.xltrs.commandadjust;

import java.util.ArrayList;
import java.util.List;

//Version 20260920
//这里就不用注释了，就这么点代码awa
public class DataToConfig {

    public static void Save(String CommandName, int CommandLevel) {
        HMCacheKernel.AddCCLData(CommandName, CommandLevel);
        List<String> CommandList = new ArrayList<>(Config.CommandModifyList.get());
        CommandList.removeIf(e -> e.startsWith(CommandName + ":"));
        CommandList.add(CommandName + ":" + CommandLevel);
        Config.CommandModifyList.set(CommandList);
        Config.SPEC.save();
        Logshow.debug("[DataToConfig] Modify %s command at level %s Success", CommandName, CommandLevel);
    }

    public static void Delete(String CommandName) {
        HMCacheKernel.DeleteCCLData(CommandName);
        List<String> CommandList = new ArrayList<>(Config.CommandModifyList.get());
        CommandList.removeIf(e -> e.startsWith(CommandName + ":"));
        Config.CommandModifyList.set(CommandList);
        Config.SPEC.save();
        Logshow.debug("[DataToConfig] Delete %s command config Success", CommandName);
    }

}
