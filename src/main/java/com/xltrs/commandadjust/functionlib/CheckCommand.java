package com.xltrs.commandadjust.functionlib;
//Version 20260925

import com.mojang.brigadier.tree.CommandNode;
import com.xltrs.commandadjust.HMCacheKernel;
import com.xltrs.commandadjust.Logshow;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

//这个类其实一个API，可以用来查询指令是否存在与等级，但都快被自己人调用114514次了ヾ(^▽^*)))，平均一个指令用100次checkcommand (不是)
public class CheckCommand {
    //创建一堆假身份来试
    private static final CommandSourceStack TestSource_0 = new CommandSourceStack(CommandSource.NULL, Vec3.ZERO, Vec2.ZERO, null, 4, "TestSource", Component.literal("TestSource"), null, null);
    private static final CommandSourceStack TestSource_1 = TestSource_0.withPermission(1);
    private static final CommandSourceStack TestSource_2 = TestSource_0.withPermission(2);
    private static final CommandSourceStack TestSource_3 = TestSource_0.withPermission(3);
    private static final CommandSourceStack TestSource_4 = TestSource_0.withPermission(4);

    //=====================检查原始等级，直接调用canuse方法，没有此指令则返回-1，也就是可以充当指令是否存在的检测器\^o^/====================
    public static int RawLevel(String CommandName) {
//使用Hashmap加快检测速度
        Integer CommandRawLevelInCache = HMCacheKernel.GetCRLData(CommandName);

        if (CommandRawLevelInCache != null) {
            Logshow.debug("[CheckCommand API] Cache hit,Check %s command raw level is %s", CommandName, CommandRawLevelInCache);
            return CommandRawLevelInCache;
        }

        CommandNode<CommandSourceStack> Node = GetDispatcher.Get().getRoot().getChild(CommandName);

        if (Node == null) {
            Logshow.debug("[CheckCommand API] %s Command does not exist in game", CommandName);
            return -1;
        }

        if (Node.canUse(TestSource_0)) {
            Logshow.debug("[CheckCommand API] Cache no hit,Check %s command raw level is 0", CommandName);
            HMCacheKernel.AddCRLData(CommandName, 0);
            return 0;
        }

        if (Node.canUse(TestSource_1)) {
            Logshow.debug("[CheckCommand API] Cache no hit,Check %s command raw level is 1", CommandName);
            HMCacheKernel.AddCRLData(CommandName, 1);
            return 1;
        }

        if (Node.canUse(TestSource_2)) {
            Logshow.debug("[CheckCommand API] Cache no hit,Check %s command raw level is 2", CommandName);
            HMCacheKernel.AddCRLData(CommandName, 2);
            return 2;
        }

        if (Node.canUse(TestSource_3)) {
            Logshow.debug("[CheckCommand API] Cache no hit,Check %s command raw level is 3", CommandName);
            HMCacheKernel.AddCRLData(CommandName, 3);
            return 3;
        }

        if (Node.canUse(TestSource_4)) {
            Logshow.debug("[CheckCommand API] Cache no hit,Check %s command raw level is 4", CommandName);
            HMCacheKernel.AddCRLData(CommandName, 4);
            return 4;
        }

        Logshow.debug("[CheckCommand API] Cache no hit,Check %s command raw level failed", CommandName);
        return -1;

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
