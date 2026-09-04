package com.xltrs.commandadjust;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.xltrs.multilinguallib.MultilingualService;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

//初始化监听器，不然怎么注册指令?
@EventBusSubscriber(modid = CommandAdjustMAIN.MODID)
public class Command {
    //=====================================================================设置指令等级==============================================================
    @SubscribeEvent
    public static void setcmdlevelCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("setcmdlevel") //注册名称
                        .requires(source -> source.hasPermission(4)) //只有4级才可以用
                        .then(
                                Commands.argument("Command", StringArgumentType.string()) //输入要修改的命令
                                        .then(
                                                Commands.argument("PermissionLevel", IntegerArgumentType.integer(0, 4)) //检测输入的等级是否在0~4之间
                                                        .executes(context -> {
                                                                    try {
                                                                        String CommandName = StringArgumentType.getString(context, "Command"); //定义变量，方便后续使用
                                                                        int CommandLevel = IntegerArgumentType.getInteger(context, "PermissionLevel");
                                                                        CommandSourceStack source = context.getSource();


                                                                        ServerPlayer player = context.getSource().getPlayer(); //获取语言
                                                                        String Language;
                                                                        if (player != null) {
                                                                            Language = player.getLanguage();
                                                                        } else {
                                                                            Language = "en_us";
                                                                        }


                                                                        if (CheckCommand.RawLevel(CommandName) == -1) { //检查指令是否存在于游戏中，返回-1就是不存在，不存在就不给改
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

                                                                        if (Config.KeyCommandGuard.get()) { //检查4级指令保护是否开启
                                                                            if (CheckCommand.RawLevel(CommandName) >= 4) { //检查即将修改的指令的原始等级是不是4级
                                                                                Debug.show("[Command Use] Intercept modify ''%s'' command,because it is level 4 command", CommandName);
                                                                                source.sendFailure(
                                                                                        Component.literal(
                                                                                                String.format(
                                                                                                        MultilingualService.GetKey("commandadjust.command.cannot.del.level4command.config", Language),
                                                                                                        CommandName
                                                                                                )
                                                                                        )
                                                                                );
                                                                                return 0;
                                                                            }
                                                                        }

                                                                        if (Config.CannotModifyCommandConfigLevelToRawLevel.get()) { //检查是否将指令的配置等级设为原始等级，长难句这一块
                                                                            if (CheckCommand.RawLevel(CommandName) == CommandLevel) {
                                                                                Debug.show("[Command Use] Intercept modify ''%s'' command config level(%s) to raw level(%s)",
                                                                                        CommandName,
                                                                                        CommandLevel,
                                                                                        CheckCommand.RawLevel(CommandName)
                                                                                );
                                                                                source.sendFailure(
                                                                                        Component.literal(
                                                                                                String.format(
                                                                                                        MultilingualService.GetKey("commandadjust.command.cannot.configleveltorawlevel", Language),
                                                                                                        CommandName,
                                                                                                        CommandLevel,
                                                                                                        CheckCommand.RawLevel(CommandName)
                                                                                                )
                                                                                        )
                                                                                );
                                                                                return 0;
                                                                            }
                                                                        }

                                                                        DataToConfig.Save(CommandName, CommandLevel); //如果全部通过，那么就调用保存函数
                                                                        source.sendSuccess(
                                                                                () -> Component.literal(
                                                                                        String.format(
                                                                                                MultilingualService.GetKey("commandadjust.command.modify.level.success", Language),
                                                                                                CommandName,
                                                                                                CommandLevel
                                                                                        )
                                                                                ), false
                                                                        );
                                                                        if (CommandName.equals("execute")) {
                                                                            if (CommandLevel < 2) {//别急，还有最后一关
                                                                                source.sendSuccess(
                                                                                        () -> Component.literal(
                                                                                                        MultilingualService.GetKey("commandadjust.command.modify.execute.warning", Language)
                                                                                                )
                                                                                                .withStyle(style -> style.withColor(ChatFormatting.YELLOW))
                                                                                        , false
                                                                                );
                                                                            }
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
    }

    //=====================================================================删除指令配置==============================================================
    @SubscribeEvent
    public static void delcmdconfigCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("delcmdconfig") //注册名称
                        .requires(source -> source.hasPermission(4)) //也是只有4级才可以用
                        .then(
                                Commands.argument("Command", StringArgumentType.string()) //输入要删除的命令
                                        .executes(context -> {
                                                    String CommandName = StringArgumentType.getString(context, "Command"); //提前准备变量，方便使用
                                                    CommandSourceStack source = context.getSource();

                                                    ServerPlayer player = context.getSource().getPlayer(); //获取语言
                                                    String Language;
                                                    if (player != null) {
                                                        Language = player.getLanguage();
                                                    } else {
                                                        Language = "en_us";
                                                    }

                                                    if (CheckCommand.ConfigLevel(CommandName) == -1) { //配置里不存在这个指令就报错，万一配置里包含游戏内没有的指令呢，方便清理无效项
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
                                                    if (Config.CannotDeleteKeyCommandConfig.get()) { //检查开没开不准删除4级指令配置项的开关，而且同时打开了4级指令保护的配置项
                                                        if (Config.KeyCommandGuard.get()) {
                                                            if (CheckCommand.RawLevel(CommandName) >= 4) { //检查是不是4级指令，虽然其实不用大于等于的(世界上最废物的大于等于号q(≧▽≦q))
                                                                Debug.show("[Command Use] Intercept delete ''%s'' command config,because it is level 4 command", CommandName);
                                                                source.sendFailure(
                                                                        Component.literal(
                                                                                String.format(
                                                                                        MultilingualService.GetKey("commandadjust.command.cannot.del.level4command.config", Language),
                                                                                        CommandName
                                                                                )
                                                                        )
                                                                );
                                                                return 0;
                                                            }
                                                        }
                                                    }
                                                    DataToConfig.Delete(CommandName); //如果全部过关就删除此指令的配置
                                                    source.sendSuccess(
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
    }

    //=====================================================================检查指令原始/配置等级==============================================================
    @SubscribeEvent
    public static void checkcmdlevelCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("checkcmdlevel") //注册名称
                        .requires(source -> source.hasPermission(0)) //谁都可以能用
                        //===================================原始指令等级监测分支===============================
                        .then(Commands.literal("raw")
                                .then(Commands.argument("CommandName", StringArgumentType.string())
                                        .executes(context -> {
                                                    String CommandName = StringArgumentType.getString(context, "CommandName"); //定义变量，方便后续使用
                                                    int RawLevel = CheckCommand.RawLevel(CommandName);

                                                    ServerPlayer player = context.getSource().getPlayer(); //获取语言
                                                    String Language;
                                                    if (player != null) {
                                                        Language = player.getLanguage();
                                                    } else {
                                                        Language = "en_us";
                                                    }

                                                    if (RawLevel == -1) { //这里是检查指令在游戏中是否存在，不然检查不存在的指令干什么，不过其实checkcommand类报-1那其实就是不存在( •̀ ω •́ )✧
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

                                                    ServerPlayer player = context.getSource().getPlayer(); //获取语言
                                                    String Language;
                                                    if (player != null) {
                                                        Language = player.getLanguage();
                                                    } else {
                                                        Language = "en_us";
                                                    }

                                                    if (RawLevel == -1) { //这里是检查指令在游戏中是否存在，不然检查不存在的指令干什么，尽管这里是检查配置文件的:)
                                                        if (ConfigLevel != -1) { //为了妥协，所以特意添加这里的if语句，方便检查有没有加入游戏内不存在的指令的配置
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

                                                    if (RawLevel == -1) { //这里还是检查指令在游戏中是否存在
                                                        if (ConfigLevel == -1) { //如果连配置文件里也没有这个指令，那么就真的就得狠狠报错了φ(゜▽゜*)♪
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
    }

    //=====================================================================查看指令配置列表==============================================================
    @SubscribeEvent
    public static void checklistCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("checkcmdconfiglist") //注册名称
                        .requires(source -> source.hasPermission(4)) //还是只有4级才可以用
                        .executes(context -> {

                                    ServerPlayer player = context.getSource().getPlayer(); //获取语言
                                    String Language = "en_us";
                                    if (player != null) {
                                        Language = player.getLanguage();
                                    }

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
                                    Debug.show("[Command Use] Checked list once");
                                    return 1;
                                }
                        )
        );
    }
}