package com.p1nero.cataclysm_dimension;

import com.mojang.logging.LogUtils;
import com.p1nero.cataclysm_dimension.worldgen.placements.CDPlacementTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(CataclysmDimensionMod.MOD_ID)
public class CataclysmDimensionMod {
    public static final String MOD_ID = "cataclysm_dimension";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CataclysmDimensionMod(IEventBus bus, ModContainer modContainer) {
        CDPlacementTypes.STRUCTURE_PLACEMENT_TYPES.register(bus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

}
