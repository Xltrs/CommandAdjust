package com.xltrs.commandadjust.command;
//Version 20260924

import com.xltrs.commandadjust.Config;
import com.xltrs.commandadjust.functionlib.GetDispatcher;
import com.xltrs.commandadjust.functionlib.AboutSource;
import com.xltrs.commandadjust.Logshow;
import com.xltrs.multilinguallib.MultilingualService;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class checkcmdconfiglist {
    public static void register() {
        GetDispatcher.Get().register(
                Commands.literal("checkcmdconfiglist") //注册名称
                        .requires(source -> source.hasPermission(4)) //还是只有4级才可以用
                        .executes(context -> {

                                    String Language = AboutSource.Language(context.getSource());

                                    String Output = MultilingualService.GetKey("commandadjust.command.checklist", Language) + "\n";
                                    for (String entry : Config.CommandModifyList.get()) {
                                        String[] parts = entry.split(":");
                                        if (parts.length == 2) {
                                            Output += "- " + parts[0] + " : " + parts[1] + "\n";
                                        }
                                    }

                                    final String FinalOutput = Output; //没办法，不中转一下idea会报错，别看它是Final类型的变量，实际上这种局部变量用完就丢了，无所谓(*^_^*)
                                    context.getSource().sendSuccess(
                                            () -> Component.literal(FinalOutput),
                                            false
                                    );
                                    Logshow.debug("[Command Use] Checked list once");
                                    return 1;

                                }
                        )
        );
        Logshow.debug("[Command Register] Register checkcmdconfiglist commands success");
    }
}
