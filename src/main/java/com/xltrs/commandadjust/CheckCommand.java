package com.xltrs.commandadjust;

import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

//这个类其实一个API，可以用来查询指令是否存在与等级，但都快被自己人调用114514次了ヾ(^▽^*)))，平均一个指令用100次checkcommand (不是)
public class CheckCommand {
    //检查原始等级，直接调用canuse方法，没有此指令则返回-1，也就是可以充当指令是否存在的检测器\^o^/
    public static int RawLevel(String CommandName) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) { //单人模式下ServerLifecycleHooks.getCurrentServer()会抽风，故添加说明T_T
            Debug.show("ServerLifecycleHooks.getCurrentServer() return null,can not continue check %s command raw level", CommandName);
            return -1;
        }
        CommandNode<CommandSourceStack> node = server.getCommands().getDispatcher().getRoot().getChild(CommandName);
        if (node == null) {
            Debug.show("[CheckCommand API] %s Command is not exist in game", CommandName);
            return -1;
        }
        for (int level = 0; level <= 4; level++) {
            CommandSourceStack fakeSource = server.createCommandSourceStack().withPermission(level);
            if (node.canUse(fakeSource)) {
                Debug.show("[CheckCommand API] Check %s command raw level is %s", CommandName, level);
                return level;
            }
        }
        Debug.show("[CheckCommand API] Check %s command raw level is 4", CommandName);
        return 4;
    }

    //检查配置文件等级，检查配置文件里的等级，没有设定此指令则返回-1，也可以充当指令是否存在的检测器
    public static int ConfigLevel(String CommandName) {
        for (String entry : Config.CommandModifyList.get()) {
            String[] parts = entry.split(":");
            if (parts.length != 2) continue;
            if (parts[0].equals(CommandName)) {
                try {
                    Debug.show("[CheckCommand API] Check %s command config level is %s", CommandName, Integer.parseInt(parts[1]));
                    return Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    Debug.show("[CheckCommand API] %s command is not exist in config", CommandName);
                    return -1;
                }
            }
        }
        Debug.show("[CheckCommand API] %s command is not exist in config", CommandName);
        return -1;
    }
}
