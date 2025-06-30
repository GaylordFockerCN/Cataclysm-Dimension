package com.p1nero.cataclysm_dimension.worldgen;

import com.p1nero.cataclysm_dimension.CataclysmDimension;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;

/**
 * 就那么几个群系，复制粘贴改现成的数据包更方便= =
 */
public class CDBiomes {

    public static ResourceKey<Biome> createBiomeKey(String name){
        return ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, name));
    }

    public static void boostrap(BootstrapContext<Biome> context) {
    }

}
