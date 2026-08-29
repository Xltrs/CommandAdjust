package com.xltrs.commandadjust;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

//只是主类，主类通常是不放实际性代码的，说白了就是个入口(*^_^*)
@Mod(CommandAdjustMAIN.MODID)
public class CommandAdjustMAIN {
    public static final String MODID = "commandadjust";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CommandAdjustMAIN(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Command Adjust Mod has been loaded");
    }
}
