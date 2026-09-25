package com.xltrs.commandadjust.mixin;
//Version 20260925

import com.mojang.brigadier.ParseResults;
import com.xltrs.commandadjust.Config;
import com.xltrs.commandadjust.functionlib.CheckCommand;
import com.xltrs.commandadjust.Logshow;
import com.xltrs.commandadjust.functionlib.AboutSource;
import com.xltrs.commandadjust.functionlib.CommandRunAPI;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public abstract class CommandsMixin {

    @Inject(method = "performCommand", at = @At("HEAD"), cancellable = true, remap = false)
    private void OnPerformCommand(ParseResults<CommandSourceStack> ParseResults, String Command, CallbackInfo CI) {

        //如果是服务器执行的话，就开个后门放行
        if (AboutSource.IsServer(ParseResults.getContext().getSource()) && Config.ServerCanRunAnyCommand.get()) {
            Logshow.debug("[Command Run] Server run %s command", Command.split(" ")[0]);
            return;
        }

        //设一堆变量方便后续使用(*^_^*)
        String CommandName = Command.split(" ")[0];
        CommandSourceStack Source = ParseResults.getContext().getSource();
        int ConfigLevel = CheckCommand.ConfigLevel(CommandName);
        int RawLevel = CheckCommand.RawLevel(CommandName);

        Logshow.debug("[Command Run] %s try to run %s command,start to check", Source.getTextName(), CommandName);

        //严重bug修复:execute指令，它可以用run参数来执行任何2级及以下的指令，必须严肃处理(* ￣︿￣ )
        if (CommandName.equals("execute")) {

            Logshow.debug("[Command Run] Discover %s try to use execute command,start to special check", Source.getTextName());

            int LastRunIndex = Command.lastIndexOf(" run "); //找到最后一个run的位置
            if (LastRunIndex != -1) { //-1就是不存在run，可以跳过

                String InnerCommand = Command
                        .substring(LastRunIndex + 5)
                        .trim()
                        .split(" ")[0]; //为什么+5？因为上面设的" run "用了5格，加5就可以定位到真正要运行的指令,然后以空格为分界线拆解指令获取指令名

                int InnerConfigLevel = CheckCommand.ConfigLevel(InnerCommand);
                int InnerRawLevel = CheckCommand.RawLevel(InnerCommand);

                if (InnerRawLevel == -1) { //不存在的指令检查
                    Logshow.debug("[Command Run] Intercepted %s use execute command to run unknown command(%s)", Source.getTextName(), InnerCommand);
                    CI.cancel();
                    return;
                }

                if (InnerConfigLevel != -1 && !Source.hasPermission(InnerConfigLevel)) { //是否存在于配置文件的指令检查
                    Logshow.debug("[Command Run] Intercepted %s use execute command to privilege escalation run %s command(Level %s,has config)", Source.getTextName(), InnerCommand, InnerConfigLevel);
                    CI.cancel();
                    return;
                }

                if (InnerConfigLevel == -1 && !Source.hasPermission(InnerRawLevel)) { //无配置的指令检查
                    Logshow.debug("[Command Run] Intercepted %s use execute command to privilege escalation run %s command(Level %s,no config)", Source.getTextName(), InnerCommand, InnerRawLevel);
                    CI.cancel();
                    return;
                }
            }
        }

        if (RawLevel == -1) { //没这个指令? 那mojang你自己看着办吧 q(≧▽≦q)
            Logshow.debug("[Command Run] %s run %s command,but it does not exist in game", Source.getTextName(), CommandName);
            return;
        }

        if (ConfigLevel == -1) { //没配置就丢给原版代码处理 q(≧▽≦q)zs
            Logshow.debug("[Command Run] %s run %s command,but command does not exist in config", Source.getTextName(), CommandName);
            return;
        }

        if (Source.hasPermission(ConfigLevel) && Source.hasPermission(RawLevel)) {//大于等于配置等级且大于原版等级也丢回给原版代码处理 q(≧▽≦q)
            Logshow.debug("[Command Run] %s run %s command,command does exist in config", Source.getTextName(), CommandName);
            return;
        }

        if (!Source.hasPermission(ConfigLevel)) { //检查执行者是不是不大于等于配置里的等级，虽然略绕awa
            Logshow.debug("[Command Run] Intercept %s run %s command", Source.getTextName(), CommandName);
            CI.cancel();
        }
        //这是执行者权限高于或等级配置，但低于原版权限的处理方法
        if (Source.hasPermission(ConfigLevel) && !Source.hasPermission(RawLevel)) {
            CommandRunAPI.SetLevel(Source, RawLevel, Command);
            CI.cancel(); //原方法可以滚了(*^_^*)
            Logshow.debug("[Command Run] %s command config level is %s,but %s level below raw level %s,so elevate privileges to %s to run %s command",
                    CommandName,
                    ConfigLevel,
                    Source.getTextName(),
                    RawLevel,
                    RawLevel,
                    CommandName
            );
        }
    }
}

