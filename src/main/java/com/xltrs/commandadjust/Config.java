package com.xltrs.commandadjust;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    //是否不可以将指令的配置等级设为原始等级，长难句这一块
    public static final ModConfigSpec.ConfigValue<Boolean> CannotModifyCommandConfigLevelToRawLevel = BUILDER
            .comment("Forbidden modify command config level back to raw level")
            .define("CannotModifyCommandConfigLevelToRawLevel", true);
    //Debug选项，字面意思
    public static final ModConfigSpec.ConfigValue<Boolean> CommandAdjustDebug = BUILDER
            .comment("Show debug info.")
            .define("Debug", true);
    //显示Debug信息，其实就是把debug级别改成info级别了
    public static final ModConfigSpec.ConfigValue<Boolean> CommandAdjustShowDebug = BUILDER
            .comment("Make debug info visible")
            .define("ShowDebug", false);
    //是否不能删除4级指令的配置项，需要和4级指令保护一起开(难道有需要单独开这玩意的场景？有了再删个if语句也不迟)
    public static final ModConfigSpec.ConfigValue<Boolean> CannotDeleteKeyCommandConfig = BUILDER
            .comment("Forbidden delete level 4 command config,it needs open KeyCommandProtection to work")
            .define("CannotDeleteKeyCommandConfig", false);
    //关键指令保护，也就是不能修改4级指令的等级
    public static final ModConfigSpec.ConfigValue<Boolean> KeyCommandGuard = BUILDER
            .comment("Forbidden modify level 4 command")
            .define("KeyCommandProtection", true);
    //本模组的核心！指令等级修改的配置列表
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CommandModifyList = BUILDER
            .comment("This is modify command permission level list")
            .defineList("ModifyCommandLevelList", List.of("setcmdlevel:4", "checkcmdlevel:0", "delcmdconfig:4"),
                    obj -> {
                        if (!(obj instanceof String entry)) return false;
                        String[] parts = entry.split(":");
                        if (parts.length != 2) return false;
                        if (parts[0].isBlank()) return false;
                        try {
                            int level = Integer.parseInt(parts[1]);
                            return level >= 0 && level <= 4;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
            );
    static final ModConfigSpec SPEC = BUILDER.build();
}