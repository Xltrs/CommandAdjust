package com.xltrs.commandadjust;

import java.util.MissingFormatArgumentException;

public class Logshow {

    public static void debug(String ShowInfo, Object... args) {
        try {
            if (Config.CommandAdjustDebug.get() && Config.CommandAdjustShowDebug.get()) {
                CommandAdjustMAIN.LOGGER.info(String.format(ShowInfo, args));
                return;
            }

            if (Config.CommandAdjustDebug.get()) {
                CommandAdjustMAIN.LOGGER.debug(String.format(ShowInfo, args));
            }

        } catch (MissingFormatArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void info(String ShowInfo, Object... args) {
        try {
            CommandAdjustMAIN.LOGGER.info(String.format(ShowInfo, args));
        } catch (MissingFormatArgumentException e) {
            e.printStackTrace();
        }
    }

}
