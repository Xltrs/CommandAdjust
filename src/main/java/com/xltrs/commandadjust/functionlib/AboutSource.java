package com.xltrs.commandadjust.functionlib;
//Version 20260925

import net.minecraft.commands.CommandSourceStack;

import java.util.Objects;

public class AboutSource {
    //可以直接快速拿玩家语言
    public static String Language(CommandSourceStack context) {
        if (context.isPlayer()) {
            return Objects.requireNonNull(context.getPlayer()).getLanguage();
        } else {
            return "en_us";
        }
    }

    public static boolean IsServer(CommandSourceStack context) {
        return context.getEntity() == null &&
                context.getTextName().equals("Server") &&
                context.hasPermission(4);
    }

}
