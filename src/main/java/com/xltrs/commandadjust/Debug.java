package com.xltrs.commandadjust;
//万能debug类，一个debug.show()直接判断有没有开启debug选项和多个占位符支持还是太超纲了(≧∇≦)
public class Debug {
    public static void show(String ShowInfo, Object... args) {
        if (Config.CommandAdjustDebug.get().equals(true)) {
            if (Config.CommandAdjustShowDebug.get().equals(true)) {
                CommandAdjustMAIN.LOGGER.info(String.format(ShowInfo, args));
                return;
            }
        }
        if (Config.CommandAdjustDebug.get().equals(true)) {
            CommandAdjustMAIN.LOGGER.debug(String.format(ShowInfo, args));
        }
    }
}
