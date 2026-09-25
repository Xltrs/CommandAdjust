package com.xltrs.commandadjust;
//Version 20260925

import com.mojang.brigadier.CommandDispatcher;
import com.xltrs.commandadjust.command.*;
import com.xltrs.commandadjust.functionlib.GetDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = CommandAdjustMAIN.MODID)
public class Command {

    @SubscribeEvent
    public static void Register(RegisterCommandsEvent event) {
        setcmdlevel.register();
        delcmdconfig.register();
        checkcmdlevel.register();
        checkcmdconfiglist.register();
        slrcommand.register();
        Logshow.debug("[Command Register] Register all commands success");
    }

}
