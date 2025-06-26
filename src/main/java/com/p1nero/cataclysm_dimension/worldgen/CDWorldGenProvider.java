package com.p1nero.cataclysm_dimension.worldgen;

import com.p1nero.cataclysm_dimension.CataclysmDimension;
import com.p1nero.cataclysm_dimension.worldgen.dimension.CataclysmDimensions;
import com.p1nero.cataclysm_dimension.worldgen.noise.CDNoiseSettings;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CDWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, CataclysmDimensions::bootstrapType)
            .add(Registries.NOISE_SETTINGS, CDNoiseSettings::bootstrap)
            .add(Registries.LEVEL_STEM, CataclysmDimensions::bootstrapStem);;

    public CDWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(CataclysmDimension.MOD_ID));
    }
}