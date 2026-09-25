package com.xltrs.commandadjust.functionlib;
//Version 20260925

import com.mojang.brigadier.CommandDispatcher;
import com.xltrs.commandadjust.CommandAdjustMAIN;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = CommandAdjustMAIN.MODID)
public class GetDispatcher {
    //主要是得绕一下neoforge，不然怎么传入RegisterCommandsEvent event参数啊
    private static final Map<String, CommandDispatcher<CommandSourceStack>> Dispatcher = new HashMap<>();

    @SubscribeEvent
    private static void CatchDispatcher(RegisterCommandsEvent event) {
        Dispatcher.put("API", event.getDispatcher());
    }

    //拿Hashmap包装真不是多此一举，因为不包装会IllegalArgumentException
    public static CommandDispatcher<CommandSourceStack> Get() {
        return Dispatcher.get("API");
    }

}
