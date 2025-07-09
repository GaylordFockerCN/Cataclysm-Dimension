package com.p1nero.cataclysm_dimension.worldgen.placements;

import com.p1nero.cataclysm_dimension.CataclysmDimensionMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CDPlacementTypes {
    public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, CataclysmDimensionMod.MOD_ID);
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<SpawnPosPlacement>> SPAWN_POS_PLACEMENT =
            STRUCTURE_PLACEMENT_TYPES.register("spawn_pos_placement", () -> () -> SpawnPosPlacement.CODEC);
}
