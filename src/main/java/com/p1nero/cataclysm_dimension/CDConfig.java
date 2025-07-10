package com.p1nero.cataclysm_dimension;

import net.minecraftforge.common.ForgeConfigSpec;

public class CDConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_TELEPORT_EYE = BUILDER
                .comment("是否启用传送眼？启用后shift时右键即可传送。或者您可以用您自己的传送逻辑")
                .comment("Use default teleport? Right click when pressing shift to teleport. Or you can design your own enter way.")
                .define("enable_teleport_eye", true);
    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
