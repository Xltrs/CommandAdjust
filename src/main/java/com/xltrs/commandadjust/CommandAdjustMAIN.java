package com.xltrs.commandadjust;

import com.xltrs.multilinguallib.MultilingualService;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
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
//别信上面那行字，他乱说的( ﹁ ﹁ )
@Mod(CommandAdjustMAIN.MODID)
public class CommandAdjustMAIN {
    public static final String MODID = "commandadjust";
    public static final Logger LOGGER = LogUtils.getLogger();

    //注册基本信息
    public CommandAdjustMAIN(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        MultilingualService.Register(MODID);
    }

    //注册服务器启动时要做的事情
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        HMCacheKernel.BuildCCLData();
        if (Config.CommandAdjustLogoPrint.get()) {
            LOGGER.info("   ______                                          __   ");
            LOGGER.info("  / ____/___  ____ ___  ____ ___  ____ _____  ____/ /   ");
            LOGGER.info(" / /   / __ \\/ __ `__ \\/ __ `__ \\/ __ `/ __ \\/ __  /   ");
            LOGGER.info("/ /___/ /_/ / / / / / / / / / / / /_/ / / / / /_/ /    ");
            LOGGER.info("\\____/\\____/_/_/_/_/_/_/ /_/ /_/\\__,_/_/ /_/\\__,_/      ");
            LOGGER.info("   /   | ____/ / (_)_  _______/ /_      ");
            LOGGER.info("  / /| |/ __  / / / / / / ___/ __/      ");
            LOGGER.info(" / ___ / /_/ / / / /_/ (__  ) /_      ");
            LOGGER.info("/_/  |_\\__,_/_/ /\\__,_/____/\\__/     ");
            LOGGER.info("           /___/               ");
        }
        LOGGER.info("Command Adjust v1.0.9"); //记得更新模组时修改这里的版本号啊！！！！！！！！！！！！！！！！！！！！！！
        LOGGER.info("Copyright (c) 2026 Xltrs");
    }

    //注册/reload时要做的事情
    @SubscribeEvent
    public void ReBuildCache(AddReloadListenerEvent event) {
        HMCacheKernel.ReloadCache();
    }

}
