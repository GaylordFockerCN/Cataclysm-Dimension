package com.p1nero.cataclysm_dimension;


import net.neoforged.neoforge.common.ModConfigSpec;

public class CDConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_TELEPORT_EYE = BUILDER
                .comment("是否启用传送眼？启用后shift时右键即可传送。或者您可以用您自己的传送逻辑")
                .comment("Use default teleport? Right click when pressing shift to teleport. Or you can design your own enter way.")
                .define("enable_teleport_eye", true);
    public static final ModConfigSpec SPEC = BUILDER.build();
}
