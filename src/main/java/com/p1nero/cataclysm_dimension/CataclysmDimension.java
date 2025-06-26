package com.p1nero.cataclysm_dimension;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(CataclysmDimension.MOD_ID)
public class CataclysmDimension {
    public static final String MOD_ID = "cataclysm_dimension";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CataclysmDimension(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

}
