package com.xltrs.commandadjust.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.xltrs.commandadjust.functionlib.AboutSource;
import com.xltrs.commandadjust.functionlib.CheckCommand;
import com.xltrs.commandadjust.functionlib.CommandRunAPI;
import com.xltrs.commandadjust.functionlib.GetDispatcher;
import com.xltrs.multilinguallib.MultilingualService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class slrcommand {
    public static void register() {
        GetDispatcher.Get().register(
                Commands.literal("slrcommand") //注册名称
                        .requires(source -> source.hasPermission(1)) //随便用，你有这个实力就行
                        .then(Commands.argument("PermissionLevel", IntegerArgumentType.integer(0, 4))
                                .then(Commands.argument("Command", StringArgumentType.greedyString())
                                        .executes(context -> {

                                                    CommandSourceStack Source = context.getSource();
                                                    String Command = StringArgumentType.getString(context, "Command"); //定义变量，方便后续使用
                                                    int Level = IntegerArgumentType.getInteger(context, "PermissionLevel");
                                                    int RawLevel = CheckCommand.RawLevel(Command.split(" ")[0]);
                                                    int ConfigLevel = CheckCommand.ConfigLevel(Command.split(" ")[0]);
                                                    String Language = AboutSource.Language(context.getSource());

                                                    if (RawLevel == -1) {
                                                        Source.sendFailure(
                                                                Component.literal(
                                                                        String.format(
                                                                                MultilingualService.GetKey("commandadjust.command.not.exist", Language)
                                                                                , Command
                                                                        )
                                                                )
                                                        );
                                                        return 0;
                                                    }

                                                    if (ConfigLevel != -1 && Source.hasPermission(ConfigLevel)) {
                                                        CommandRunAPI.SetLevel(Source, Level, Command);
                                                        return 1;
                                                    }

                                                    if (ConfigLevel != -1 && !Source.hasPermission(ConfigLevel)) {
                                                        return 0;
                                                    }

                                                    if (Source.hasPermission(RawLevel)) {
                                                        CommandRunAPI.SetLevel(Source, Level, Command);
                                                        return 1;
                                                    }

                                                    return 0;

                                                }
                                        )
                                )
                        )
        );
    }
}
