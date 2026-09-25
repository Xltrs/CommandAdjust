package com.xltrs.commandadjust.command;
//Version 20260924

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.xltrs.commandadjust.*;
import com.xltrs.commandadjust.functionlib.CheckCommand;
import com.xltrs.commandadjust.functionlib.GetDispatcher;
import com.xltrs.commandadjust.functionlib.AboutSource;
import com.xltrs.multilinguallib.MultilingualService;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class setcmdlevel {
    public static void register() {
        GetDispatcher.Get().register(
                Commands.literal("setcmdlevel") //注册名称
                        .requires(source -> source.hasPermission(4)) //只有4级才可以用
                        .then(
                                Commands.argument("Command", StringArgumentType.string()) //输入要修改的命令
                                        .then(
                                                Commands.argument("PermissionLevel", IntegerArgumentType.integer(0, 5)) //检测输入的等级是否在0~4之间
                                                        .executes(context -> {
                                                                    try {

                                                                        String CommandName = StringArgumentType.getString(context, "Command"); //定义变量，方便后续使用
                                                                        int CommandLevel = IntegerArgumentType.getInteger(context, "PermissionLevel");
                                                                        CommandSourceStack Source = context.getSource();
                                                                        int RawLevel = CheckCommand.RawLevel(CommandName);

                                                                        String Language = AboutSource.Language(context.getSource());

                                                                        if (RawLevel == -1) { //检查指令是否存在于游戏中，返回-1就是不存在，不存在就不给改
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

                                                                        if (Config.KeyCommandGuard.get() && RawLevel >= 4) { //检查4级指令保护是否开启，检查即将修改的指令的原始等级是不是4级
                                                                            Logshow.debug("[Command Use] Intercept modify ''%s'' command,because it is level 4 command", CommandName);
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

                                                                        if (CommandName.equals("setcmdlevel")) { //防止作死把唯一的通道给禁用了
                                                                            Logshow.debug("[Command Use] Intercept disable ''setcmdlevel'' command");
                                                                            Source.sendFailure(
                                                                                    Component.literal(
                                                                                            String.format(
                                                                                                    MultilingualService.GetKey("commandadjust.command.prevent.disable.setcmdlevel", Language)
                                                                                            )
                                                                                    )
                                                                            );
                                                                            return 0;
                                                                        }

                                                                        if (Config.CannotModifyCommandConfigLevelToRawLevel.get() && RawLevel == CommandLevel && CheckCommand.ConfigLevel(CommandName) == -1) {
                                                                            Source.sendFailure( //如果在修改配置等级为原等级就当作删除此指令的配置时，配置没有此指令，则提示不存在此指令
                                                                                    Component.literal(
                                                                                            String.format(
                                                                                                    MultilingualService.GetKey("commandadjust.command.not.exist", Language)
                                                                                                    , CommandName
                                                                                            )
                                                                                    )
                                                                            );
                                                                            return 0;
                                                                        }

                                                                        if (Config.CannotModifyCommandConfigLevelToRawLevel.get() && RawLevel == CommandLevel) { //检查是否将指令的配置等级设为原始等级，长难句这一块
                                                                            DataToConfig.Delete(CommandName); //现在是修改配置等级为原等级就当作删除此指令的配置
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

                                                                        DataToConfig.Save(CommandName, CommandLevel); //如果全部通过，那么就调用保存函数

                                                                        if (CommandLevel <= 4) {
                                                                            Source.sendSuccess(
                                                                                    () -> Component.literal(
                                                                                            String.format(
                                                                                                    MultilingualService.GetKey("commandadjust.command.modify.level.success", Language),
                                                                                                    CommandName,
                                                                                                    CommandLevel
                                                                                            )
                                                                                    ), false
                                                                            );
                                                                        }
                                                                        //修改指令等级和禁用指令的不同提示
                                                                        if (CommandLevel > 4) {
                                                                            Source.sendSuccess(
                                                                                    () -> Component.literal(
                                                                                            String.format(
                                                                                                    MultilingualService.GetKey("commandadjust.command.disable.success", Language),
                                                                                                    CommandName,
                                                                                                    CommandLevel
                                                                                            )
                                                                                    ), false
                                                                            );
                                                                        }

                                                                        if (CommandName.equals("execute") && CommandLevel < 2) { //别急，还有最后一关
                                                                            Source.sendSuccess(
                                                                                    () -> Component.literal(
                                                                                                    MultilingualService.GetKey("commandadjust.command.modify.execute.warning", Language)
                                                                                            )
                                                                                            .withStyle(style -> style.withColor(ChatFormatting.YELLOW))
                                                                                    , false
                                                                            );
                                                                        }

                                                                        return 1;

                                                                    } catch (
                                                                            Exception e) { //这里本来是为了看看单人模式为什么不能用而写的，但这是玄学bug！ 一加这个报错就打印堆栈的代码进去就一秒老实的，错误不报了，配置文件可以保存了ヾ(≧▽≦*)o (面向玄学编程这一块)
                                                                        e.printStackTrace();
                                                                        return 0;
                                                                    }
                                                                }
                                                        )
                                        )
                        )
        );
        Logshow.debug("[Command Register] Register setcmdlevel commands success");
    }
}
