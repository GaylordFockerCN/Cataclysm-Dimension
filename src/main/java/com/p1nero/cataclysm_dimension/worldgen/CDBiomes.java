package com.p1nero.cataclysm_dimension.worldgen;

import com.p1nero.cataclysm_dimension.CataclysmDimensionMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;

public class CDBiomes {

    public static final ResourceKey<Biome> BADLANDS = register("badlands");
    public static final ResourceKey<Biome> DEEP_OCEAN = register("deep_ocean");
    public static final ResourceKey<Biome> DESERT = register("desert");
    public static final ResourceKey<Biome> NETHER_WASTES = register("nether_wastes");
    public static final ResourceKey<Biome> SOUL_SAND_VALLEY = register("soul_sand_valley");
    public static final ResourceKey<Biome> THE_END = register("the_end");
    public static final ResourceKey<Biome> WARM_OCEAN = register("warm_ocean");
    public static final ResourceKey<Biome> SNOWY_PLAINS = register("snowy_plains");
    public static ResourceKey<Biome> register(String name){
        return ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(CataclysmDimensionMod.MOD_ID, name));
    }

    /**
     * 就那么几个群系，复制粘贴改现成的数据包更方便= =
     */
    public static void boostrap(BootstrapContext<Biome> context) {
    }

}
