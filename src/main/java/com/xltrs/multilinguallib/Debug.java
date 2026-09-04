package com.xltrs.multilinguallib;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

//你猜debug类哪来的？ ヾ(•ω•`)o
public class Debug {
    public static void show(String ShowInfo, Object... args) {
        final Logger LOGGER = LogUtils.getLogger();
        LOGGER.debug(String.format(ShowInfo, args));
    }
}