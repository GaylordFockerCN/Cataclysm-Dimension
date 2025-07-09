package com.p1nero.cataclysm_dimension.worldgen.placements;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SpawnPosPlacement extends StructurePlacement {

    public static final MapCodec<SpawnPosPlacement> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.stable(new SpawnPosPlacement()));
    protected SpawnPosPlacement() {
        super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1, 114514, Optional.empty());
    }

    @Override
    protected boolean isPlacementChunk(@NotNull ChunkGeneratorStructureState state, int chunkX, int chunkZ) {
        return chunkX == 0 && chunkZ == 0;
    }

    @Override
    public boolean isStructureChunk(@NotNull ChunkGeneratorStructureState structureState, int x, int z) {
        return isPlacementChunk(structureState, x, z);
    }

    @Override
    public @NotNull StructurePlacementType<?> type() {
        return CDPlacementTypes.SPAWN_POS_PLACEMENT.get();
    }
}
