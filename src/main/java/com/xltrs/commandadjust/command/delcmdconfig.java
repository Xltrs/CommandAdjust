package com.xltrs.commandadjust.command;
//Version 20260924

import com.mojang.brigadier.arguments.StringArgumentType;
import com.xltrs.commandadjust.*;
import com.xltrs.commandadjust.functionlib.CheckCommand;
import com.xltrs.commandadjust.functionlib.GetDispatcher;
import com.xltrs.commandadjust.functionlib.AboutSource;
import com.xltrs.multilinguallib.MultilingualService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class delcmdconfig {
    public static void register() {
        GetDispatcher.Get().register(
                Commands.literal("delcmdconfig") //注册名称
                        .requires(source -> source.hasPermission(4)) //也是只有4级才可以用
                        .then(
                                Commands.argument("Command", StringArgumentType.string()) //输入要删除的命令
                                        .executes(context -> {
                                                    String CommandName = StringArgumentType.getString(context, "Command"); //提前准备变量，方便使用
                                                    CommandSourceStack Source = context.getSource();
                                                    int RawLevel = CheckCommand.RawLevel(CommandName);

                                                    String Language = AboutSource.Language(context.getSource());

                                                    if (CheckCommand.ConfigLevel(CommandName) == -1) { //配置里不存在这个指令就报错，万一配置里包含游戏内没有的指令呢，所以不阻止删除游戏内没有的指令的配置，方便清理无效项
                                                        Source.sendFailure(
                                                                Component.literal(
                                                                        String.format(
                                                                                MultilingualService.GetKey("commandadjust.command.not.exist", Language)
                                                                                , CommandName
                                                                        )
                                                                )
                                                        );
                                                        return 0;
                                                    }

                                                    if (Config.CannotDeleteKeyCommandConfig.get() && RawLevel >= 4) { //检查是不是4级指令，虽然其实不用大于等于的(世界上最废物的大于等于号q(≧▽≦q))
                                                        Logshow.debug("[Command Use] Intercept delete ''%s'' command config,because it is level 4 command", CommandName);
                                                        Source.sendFailure(
                                                                Component.literal(
                                                                        String.format(
                                                                                MultilingualService.GetKey("commandadjust.command.cannot.del.level4command.config", Language),
                                                                                CommandName
                                                                        )
                                                                )
                                                        );
                                                        return 0;
                                                    }
                                                    DataToConfig.Delete(CommandName); //如果全部过关就删除此指令的配置
                                                    Source.sendSuccess(
                                                            () -> Component.literal(
                                                                    String.format(
                                                                            MultilingualService.GetKey("commandadjust.command.del.config.success", Language),
                                                                            CommandName
                                                                    )
                                                            ), false
                                                    );
                                                    return 1;
                                                }
                                        )
                        )
        );
        Logshow.debug("[Command Register] Register delcmdconfig commands success");
    }
}
