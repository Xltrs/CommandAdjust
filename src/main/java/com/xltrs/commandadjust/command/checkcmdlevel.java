package com.xltrs.commandadjust.command;
//Version 20260924

import com.mojang.brigadier.arguments.StringArgumentType;
import com.xltrs.commandadjust.functionlib.CheckCommand;
import com.xltrs.commandadjust.functionlib.GetDispatcher;
import com.xltrs.commandadjust.functionlib.AboutSource;
import com.xltrs.commandadjust.Logshow;
import com.xltrs.multilinguallib.MultilingualService;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class checkcmdlevel {
    public static void register() {
        GetDispatcher.Get().register(
                Commands.literal("checkcmdlevel") //注册名称
                        .requires(source -> source.hasPermission(0)) //这次倒谁都能用了哈，我认为玩家有对服务器内指令设置的知情权，不过可以自己设置等级就是了awa
                        //===================================原始指令等级监测分支===============================
                        .then(Commands.literal("raw")
                                .then(Commands.argument("CommandName", StringArgumentType.string())
                                        .executes(context -> {

                                                    String CommandName = StringArgumentType.getString(context, "CommandName"); //定义变量，方便后续使用
                                                    int RawLevel = CheckCommand.RawLevel(CommandName);

                                                    String Language = AboutSource.Language(context.getSource());

                                                    if (RawLevel == -1) { //这里是检查指令在游戏中是否存在，不然检查不存在的指令干什么，不过其实checkcommand类报-1那其实就是不存在
                                                        context.getSource().sendFailure(
                                                                Component.literal(
                                                                        String.format(
                                                                                MultilingualService.GetKey("commandadjust.command.not.exist", Language)
                                                                                , CommandName
                                                                        )
                                                                )
                                                        );
                                                        return 0;
                                                    }

                                                    context.getSource().sendSuccess(
                                                            () -> Component.literal(
                                                                    String.format(
                                                                            MultilingualService.GetKey("commandadjust.command.check.rawlevel", Language),
                                                                            CommandName,
                                                                            RawLevel
                                                                    )
                                                            ),
                                                            false
                                                    );
                                                    return 1;

                                                }
                                        )
                                )

                        )
                        //=============================================配置中指令等级监测分支==========================================
                        .then(Commands.literal("config")
                                .then(Commands.argument("CommandName", StringArgumentType.string())
                                        .executes(context -> {

                                                    String CommandName = StringArgumentType.getString(context, "CommandName"); //定义变量，方便后续使用
                                                    int RawLevel = CheckCommand.RawLevel(CommandName);
                                                    int ConfigLevel = CheckCommand.ConfigLevel(CommandName);

                                                    String Language = AboutSource.Language(context.getSource());

                                                    if (RawLevel == -1 && ConfigLevel != -1) { //这里是检查指令在游戏中是否存在，不然检查不存在的指令干什么，尽管这里是检查配置文件的，不过为了妥协，所以特意添加这里的if语句，方便检查有没有加入游戏内不存在的指令的配置
                                                        context.getSource().sendSuccess(
                                                                () ->
                                                                        Component.literal(
                                                                                String.format(
                                                                                        MultilingualService.GetKey("commandadjust.command.check.configlevel.cmdnotexistingame", Language),
                                                                                        CommandName,
                                                                                        ConfigLevel,
                                                                                        CommandName
                                                                                )
                                                                        ),
                                                                false
                                                        );
                                                        return 1;
                                                    }

                                                    if (ConfigLevel == -1) { //检查配置文件里有没有这个指令
                                                        context.getSource().sendFailure(
                                                                Component.literal(
                                                                        String.format(
                                                                                MultilingualService.GetKey("commandadjust.command.not.existinconfig", Language),
                                                                                CommandName
                                                                        )
                                                                )
                                                        );
                                                        return 0;
                                                    }

                                                    if (RawLevel == -1 && ConfigLevel == -1) { //这里还是检查指令在游戏中是否存在，但如果连配置文件里也没有这个指令，那么就真的就得狠狠报错了φ(゜▽゜*)♪
                                                        context.getSource().sendFailure(
                                                                Component.literal(
                                                                        String.format(
                                                                                MultilingualService.GetKey("commandadjust.command.not.existinconfig", Language),
                                                                                CommandName
                                                                        )
                                                                )
                                                        );
                                                        return 0;
                                                    }

                                                    context.getSource().sendSuccess(
                                                            () ->
                                                                    Component.literal(
                                                                            String.format(
                                                                                    MultilingualService.GetKey("commandadjust.command.check.configlevel", Language),
                                                                                    CommandName,
                                                                                    ConfigLevel
                                                                            )
                                                                    ),
                                                            false
                                                    );
                                                    return 1;

                                                }
                                        )
                                )
                        )
        );
        Logshow.debug("[Command Register] Register checkcmdlevel commands success");
    }
}
