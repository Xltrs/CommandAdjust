package com.xltrs.commandadjust.mixin;

import com.mojang.brigadier.ParseResults;
import com.xltrs.commandadjust.CheckCommand;
import com.xltrs.commandadjust.Debug;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public abstract class CommandsMixin {
    private static boolean isElevating = false; //注意，这里的提权检查等会要考

    @Inject(method = "performCommand", at = @At("HEAD"), cancellable = true, remap = false)
    private void OnPerformCommand(ParseResults<CommandSourceStack> parseResults, String command, CallbackInfo ci) {
        if (isElevating == true) { //如果检查到是提权状态，就丢给原版代码处理q(≧▽≦q)
            isElevating = false;
            return; //开玩笑的，原方法先别滚(╯▽╰ )
        }

        //设一堆变量方便后续使用(*^_^*)
        String CommandName = command.split(" ")[0];
        CommandSourceStack source = parseResults.getContext().getSource();
        int ConfigLevel = CheckCommand.ConfigLevel(CommandName);
        int RawLevel = CheckCommand.RawLevel(CommandName);

        //严重bug修复:execute指令，它可以用run参数来执行任何2级及以下的指令，必须严肃处理(* ￣︿￣ )
        if (CommandName.equals("execute")) {
            int LastRunIndex = command.lastIndexOf(" run "); //找到最后一个run的位置
            if (LastRunIndex != -1) { //-1就是不存在run，可以跳过
                String AfterRun = command.substring(LastRunIndex + 5).trim(); //为什么+5？因为上面设的" run "用了5格，加5就可以定位到真正要运行的指令
                String InnerCommand = AfterRun.split(" ")[0]; //把run后面的内容按空格分开，获取指令名
                int InnerConfigLevel = CheckCommand.ConfigLevel(InnerCommand);
                int InnerRawLevel = CheckCommand.RawLevel(InnerCommand);
                if (InnerRawLevel == -1) { //不存在的指令检查
                    Debug.show("[Command Run] Intercepted a execute command run not exist command");
                    ci.cancel();
                    return;
                }
                if (InnerConfigLevel != -1) { //是否存在于配置文件的指令检查
                    if (!source.hasPermission(InnerConfigLevel)) {
                        {
                            Debug.show("[Command Run] Intercepted a execute command privilege escalation");
                            ci.cancel();
                            return;
                        }
                    }
                }
                if (InnerConfigLevel == -1) { //无配置的指令检查
                    if (!source.hasPermission(InnerRawLevel)) {
                        {
                            Debug.show("[Command Run] Intercepted a execute command privilege escalation");
                            ci.cancel();
                            return;
                        }
                    }
                }
            }
        }

        if (RawLevel == -1) { //没这个指令? 那mojang你自己看着办吧 q(≧▽≦q)
            Debug.show("[Command Run] Run %s command,but it is not exist in game", CommandName);
            return;
        }

        if (ConfigLevel == -1) { //没配置就丢给原版代码处理 q(≧▽≦q)
            Debug.show("[Command Run] Run %s command,but it is not exist in config", CommandName);
            return;
        }

        if (source.hasPermission(ConfigLevel)) {//大于等于配置等级且大于原版等级也丢回给原版代码处理 q(≧▽≦q)
            if (source.hasPermission(RawLevel)) {
            Debug.show("[Command Run] Run %s command", CommandName);
            return;
        }
    }

        if (source.hasPermission(ConfigLevel)) { //这是执行者权限高于或等级配置，但低于原版权限的处理方法，但不能放到检查执行者和配置等级的下面，不然idea会啸`(*>﹏<*)′
            if (!source.hasPermission(RawLevel)) {
                CommandSourceStack HighLevelSource = source.withPermission(RawLevel); //临时提权，但不能提太高，不然可能有bug φ(゜▽゜*)♪
                try {
                    isElevating = true; //我只是在提权T_T
                    ParseResults<CommandSourceStack> newResults = source
                            .getServer()
                            .getCommands()
                            .getDispatcher()
                            .parse(command, HighLevelSource);
                    source
                            .getServer()
                            .getCommands()
                            .performCommand(newResults, command);
                    ci.cancel(); //原方法可以滚了(*^_^*)
                    Debug.show("[Command Run] %s command config level is %s,but executor level below raw level %s,so elevate privileges to %s to run %s command",
                            CommandName,
                            ConfigLevel,
                            RawLevel,
                            RawLevel,
                            CommandName
                    );
                    return;
                } catch (Exception e) {
                    isElevating = false;
                    e.printStackTrace();
                }
            }
        }

        if (!source.hasPermission(ConfigLevel)) { //检查执行者是不是不大于等于配置里的等级，虽然略绕awa
            Debug.show("[Command Run] Intercept %s command run",CommandName);
            ci.cancel();
        }

    }
}

