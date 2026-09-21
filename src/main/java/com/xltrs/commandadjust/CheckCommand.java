package com.xltrs.commandadjust;

import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

//这个类其实一个API，可以用来查询指令是否存在与等级，但都快被自己人调用114514次了ヾ(^▽^*)))，平均一个指令用100次checkcommand (不是)
public class CheckCommand {
    //=====================检查原始等级，直接调用canuse方法，没有此指令则返回-1，也就是可以充当指令是否存在的检测器\^o^/====================
    public static int RawLevel(String CommandName) {

        Integer CommandRawLevelInCache = HMCacheKernel.GetCRLData(CommandName);
//使用HashMap缓存来快速获取指令等级结果ˋ( ° ▽、° )
        if (CommandRawLevelInCache != null) {
            Logshow.debug("[CheckCommand API] Cache hit,Check %s command raw level is %s", CommandName, CommandRawLevelInCache);
            return CommandRawLevelInCache;
        }

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        if (server == null) { //单人模式下ServerLifecycleHooks.getCurrentServer()会抽风，故添加说明T_T
            Logshow.debug("[CheckCommand API] ServerLifecycleHooks.getCurrentServer() return null,can not continue check %s command raw level", CommandName);
            return -1;
        }

        CommandNode<CommandSourceStack> node = server.getCommands().getDispatcher().getRoot().getChild(CommandName);

        if (node == null) {
            Logshow.debug("[CheckCommand API] Cache no hit,%s Command does not exist in game", CommandName);
            return -1;
        }

        for (int level = 0; level <= 4; level++) {
            CommandSourceStack FakeSource = server.createCommandSourceStack().withPermission(level);
            if (node.canUse(FakeSource)) {
                Logshow.debug("[CheckCommand API] Cache no hit,Check %s command raw level is %s", CommandName, level);
                HMCacheKernel.AddCRLData(CommandName, level);
                return level;
            }
        }
//下面这是保底，不是多余
        Logshow.debug("[CheckCommand API] Cache no hit,Check %s command raw level is 4", CommandName);
        return 4;
    }

    //========================检查配置文件等级，检查配置文件里的等级，没有设定此指令则返回-1，也可以充当指令是否存在的检测器========================
    public static int ConfigLevel(String CommandName) {
        Integer ConfigLevel = HMCacheKernel.GetCCLData(CommandName);
        if (ConfigLevel != null) {
            Logshow.debug("[CheckCommand API] Check %s command config level is %s", CommandName, ConfigLevel);
            return ConfigLevel;
        } else {
            Logshow.debug("[CheckCommand API] %s command is not exist in config", CommandName);
            return -1;
        }
    }
}
