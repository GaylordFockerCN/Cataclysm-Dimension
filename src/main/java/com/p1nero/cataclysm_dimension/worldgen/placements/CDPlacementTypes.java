package com.p1nero.cataclysm_dimension.worldgen.placements;

import com.p1nero.cataclysm_dimension.CataclysmDimensionMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CDPlacementTypes {
    public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, CataclysmDimensionMod.MOD_ID);
    public static final RegistryObject<StructurePlacementType<SpawnPosPlacement>> SPAWN_POS_PLACEMENT =
            STRUCTURE_PLACEMENT_TYPES.register("spawn_pos_placement", () -> SpawnPosPlacement.CODEC::codec);
}
