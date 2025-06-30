package com.p1nero.cataclysm_dimension.worldgen;

import com.p1nero.cataclysm_dimension.CataclysmDimension;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalLong;

public class CataclysmDimensions {
    //远古工厂 (Ancient Factory) 恶地
    public static final ResourceKey<LevelStem> CATACLYSM_FORGE_OF_AEONS_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_forge_of_aeons"));
    public static final ResourceKey<Level> CATACLYSM_FORGE_OF_AEONS_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_forge_of_aeons"));
    public static final ResourceKey<DimensionType> CATACLYSM_FORGE_OF_AEONS_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_forge_of_aeons_type"));

    //沉沦之城 (Sunken City)海洋
    public static final ResourceKey<LevelStem> CATACLYSM_ABYSSAL_DEPTHS_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_abyssal_depths"));
    public static final ResourceKey<Level> CATACLYSM_ABYSSAL_DEPTHS_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_abyssal_depths"));
    public static final ResourceKey<DimensionType> CATACLYSM_ABYSSAL_DEPTHS_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_abyssal_depths_type"));

    //被诅咒的金字塔 (Cursed Pyramid)沙漠
    public static final ResourceKey<LevelStem> CATACLYSM_PHARAOHS_BANE_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_pharaohs_bane"));
    public static final ResourceKey<Level> CATACLYSM_PHARAOHS_BANE_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_pharaohs_bane"));
    public static final ResourceKey<DimensionType> CATACLYSM_PHARAOHS_BANE_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_pharaohs_bane_type"));

    //苦寒监牢 (Frosted Prison)雪原
    public static final ResourceKey<LevelStem> CATACLYSM_ETERNAL_FROSTHOLD_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_eternal_frosthold"));
    public static final ResourceKey<Level> CATACLYSM_ETERNAL_FROSTHOLD_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_eternal_frosthold"));
    public static final ResourceKey<DimensionType> CATACLYSM_ETERNAL_FROSTHOLD_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_eternal_frosthold_type"));

    //卫城 (Acropolis)温水海洋
    public static final ResourceKey<LevelStem> CATACLYSM_SANCTUM_FALLEN_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_sanctum_fallen"));
    public static final ResourceKey<Level> CATACLYSM_SANCTUM_FALLEN_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_sanctum_fallen"));
    public static final ResourceKey<DimensionType> CATACLYSM_SANCTUM_FALLEN_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_sanctum_fallen_type"));

    //灵魂锻造厂 (Soul Black Smith)灵魂沙峡谷
    public static final ResourceKey<LevelStem> CATACLYSM_SOULS_ANVIL_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_souls_anvil"));
    public static final ResourceKey<Level> CATACLYSM_SOULS_ANVIL_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_souls_anvil"));
    public static final ResourceKey<DimensionType> CATACLYSM_SOULS_ANVIL_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_souls_anvil_type"));

    //熔岩竞技场 (Burning Arena)地狱
    public static final ResourceKey<LevelStem> CATACLYSM_INFERNOS_MAW_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_infernos_maw"));
    public static final ResourceKey<Level> CATACLYSM_INFERNOS_MAW_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_infernos_maw"));
    public static final ResourceKey<DimensionType> CATACLYSM_INFERNOS_MAW_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_infernos_maw_type"));

    //废弃堡垒 (Ruined Citadel)末地
    public static final ResourceKey<LevelStem> CATACLYSM_BASTION_LOST_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_bastion_lost"));
    public static final ResourceKey<Level> CATACLYSM_BASTION_LOST_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_bastion_lost"));
    public static final ResourceKey<DimensionType> CATACLYSM_BASTION_LOST_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(CataclysmDimension.MOD_ID, "cataclysm_bastion_lost_type"));

    public static void bootstrapType(BootstrapContext<DimensionType> context) {
        // 远古工厂 - 恶地维度 (类似主世界)
        context.register(CATACLYSM_FORGE_OF_AEONS_DIM_TYPE, new DimensionType(
                OptionalLong.empty(),       // 跟随主世界时间
                true,                       // 有天空光照
                false,                      // 无天花板
                false,                      // 非超高温
                true,                       // 自然生成结构
                1.0,                        // 正常坐标缩放
                true,                       // 床安全
                false,                      // 重生锚无效
                -64,                        // 最低Y层
                384,                        // 总高度
                256,                        // 逻辑高度
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                0.5f,                       // 稍暗的环境光
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));

        // 沉沦之城 - 海洋维度 (提高海平面)
        context.register(CATACLYSM_ABYSSAL_DEPTHS_DIM_TYPE, new DimensionType(
                OptionalLong.of(6000),      // 固定中午时间
                true,                       // 有天空光照
                false,                      // 无天花板
                false,                      // 非超高温
                true,                       // 自然生成结构
                1.0,                        // 正常坐标缩放
                false,                      // 床在水中无效
                false,                      // 重生锚无效
                -64,                        // 最低Y层
                384,                        // 总高度
                128,                        // 降低逻辑高度(高压环境)
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                1.0f,
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));

        // 被诅咒的金字塔 - 沙漠维度 (降低海平面)
        context.register(CATACLYSM_PHARAOHS_BANE_DIM_TYPE, new DimensionType(
                OptionalLong.of(6000),     // 固定中午时间
                true,                       // 有天空光照
                false,                      // 无天花板
                true,                       // 超高温(沙漠)
                true,                       // 自然生成结构
                1.0,                        // 正常坐标缩放
                true,                       // 床安全
                false,                      // 重生锚无效
                32,                         // 提高最低Y层(降低海平面)
                256,                        // 总高度
                256,                        // 逻辑高度
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                1.0f,                       // 明亮的沙漠环境光
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));

        // 苦寒监牢 - 雪原维度 (极寒环境)(降低海平面)
        context.register(CATACLYSM_ETERNAL_FROSTHOLD_DIM_TYPE, new DimensionType(
                OptionalLong.of(18000),     // 永久夜晚
                false,                      // 无天空光照(暴风雪)
                true,                       // 有天花板(冰层)
                false,                      // 非超高温
                false,                      // 无自然生成
                1.0,                        // 正常坐标缩放
                false,                      // 床会冻结
                false,                      // 重生锚无效
                32,                          // 最低Y层
                256,                        // 总高度
                128,                        // 降低逻辑高度
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                0.2f,          // 昏暗的极地环境光
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));

        // 卫城 - 温水海洋维度
        context.register(CATACLYSM_SANCTUM_FALLEN_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000),     // 固定黄昏时间
                true,                      // 有天空光照
                false,                     // 无天花板
                false,                     // 非超高温
                true,                      // 自然生成结构
                1.0,                       // 正常坐标缩放
                false,                     // 床在水中无效
                false,                     // 重生锚无效
                -64,                       // 最低Y层
                384,                       // 总高度
                192,                       // 中等逻辑高度
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                0.6f,                      // 温暖水域环境光
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));

        // 灵魂锻造厂 - 灵魂沙峡谷 (地狱变种)
        context.register(CATACLYSM_SOULS_ANVIL_DIM_TYPE, new DimensionType(
                OptionalLong.empty(),      // 无固定时间
                false,                     // 无天空光照
                true,                      // 有天花板(灵魂岩层)
                true,                      // 超高温
                false,                     // 无自然生成
                8.0,                       // 地狱坐标缩放
                false,                     // 床会爆炸
                true,                      // 重生锚有效
                0,                         // 最低Y层
                384,                       // 总高度
                384,                       // 地狱逻辑高度
                BlockTags.INFINIBURN_NETHER,
                BuiltinDimensionTypes.NETHER_EFFECTS,
                0.1f,                     // 极暗的灵魂峡谷光
                new DimensionType.MonsterSettings(true, false, ConstantInt.of(0), 0)));

        // 熔岩竞技场 - 地狱维度
        context.register(CATACLYSM_INFERNOS_MAW_DIM_TYPE, new DimensionType(
                OptionalLong.empty(),    // 无固定时间
                false,                     // 无天空光照
                true,                      // 有天花板(基岩层)
                true,                      // 超高温(水蒸发)
                false,                     // 无自然生成
                8.0,                       // 地狱坐标缩放
                false,                     // 床会爆炸
                true,                      // 重生锚有效
                0,                         // 最低Y层
                384,                       // 总高度
                384,                       // 地狱逻辑高度
                BlockTags.INFINIBURN_NETHER,
                BuiltinDimensionTypes.NETHER_EFFECTS,
                0.5f,                     // 熔岩环境光
                new DimensionType.MonsterSettings(true, false, ConstantInt.of(0), 0)));

        // 废弃堡垒 - 末地维度
        context.register(CATACLYSM_BASTION_LOST_DIM_TYPE, new DimensionType(
                OptionalLong.empty(),     // 无固定时间
                true,                      // 有天空光照(特殊)
                false,                     // 无天花板
                false,                     // 非超高温
                false,                     // 无自然生成
                1.0,                       // 正常坐标缩放
                false,                     // 床会爆炸
                false,                     // 重生锚无效
                0,                         // 最低Y层
                256,                       // 总高度
                256,                       // 逻辑高度
                BlockTags.INFINIBURN_END,
                BuiltinDimensionTypes.END_EFFECTS,
                0.0f,                     // 完全黑暗
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
    }

    public static void bootstrapStem(BootstrapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        // 远古工厂 - 恶地群系 (BADLANDS)
        NoiseBasedChunkGenerator FORGE_OF_AEONS_ChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.BADLANDS)),
                noiseGenSettings.getOrThrow(CDNoiseSettings.PLAIN));
        LevelStem FORGE_OF_AEONS_stem = new LevelStem(dimTypes.getOrThrow(CataclysmDimensions.CATACLYSM_FORGE_OF_AEONS_DIM_TYPE), FORGE_OF_AEONS_ChunkGenerator);
        context.register(CATACLYSM_FORGE_OF_AEONS_KEY, FORGE_OF_AEONS_stem);

        // 沉沦之城 - 海洋群系 (DEEP_OCEAN)
        NoiseBasedChunkGenerator ABYSSAL_DEPTHS_ChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.DEEP_OCEAN)),
                noiseGenSettings.getOrThrow(CDNoiseSettings.SEA));
        LevelStem ABYSSAL_DEPTHS_stem = new LevelStem(dimTypes.getOrThrow(CataclysmDimensions.CATACLYSM_ABYSSAL_DEPTHS_DIM_TYPE), ABYSSAL_DEPTHS_ChunkGenerator);
        context.register(CATACLYSM_ABYSSAL_DEPTHS_KEY, ABYSSAL_DEPTHS_stem);

        // 被诅咒的金字塔 - 沙漠群系 (DESERT)
        NoiseBasedChunkGenerator PHARAOHS_BANE_ChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.DESERT)),
                noiseGenSettings.getOrThrow(CDNoiseSettings.DESERT));
        LevelStem PHARAOHS_BANE_stem = new LevelStem(dimTypes.getOrThrow(CataclysmDimensions.CATACLYSM_PHARAOHS_BANE_DIM_TYPE), PHARAOHS_BANE_ChunkGenerator);
        context.register(CATACLYSM_PHARAOHS_BANE_KEY, PHARAOHS_BANE_stem);

        // 苦寒监牢 - 雪原群系 (SNOWY_PLAINS)
        NoiseBasedChunkGenerator ETERNAL_FROSTHOLD_ChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.SNOWY_SLOPES)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));
        LevelStem ETERNAL_FROSTHOLD_stem = new LevelStem(dimTypes.getOrThrow(CataclysmDimensions.CATACLYSM_ETERNAL_FROSTHOLD_DIM_TYPE), ETERNAL_FROSTHOLD_ChunkGenerator);
        context.register(CATACLYSM_ETERNAL_FROSTHOLD_KEY, ETERNAL_FROSTHOLD_stem);

        // 卫城 - 温水海洋群系 (LUKEWARM_OCEAN)
        NoiseBasedChunkGenerator SANCTUM_FALLEN_ChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.LUKEWARM_OCEAN)),
                noiseGenSettings.getOrThrow(CDNoiseSettings.SEA));
        LevelStem SANCTUM_FALLEN_stem = new LevelStem(dimTypes.getOrThrow(CataclysmDimensions.CATACLYSM_SANCTUM_FALLEN_DIM_TYPE), SANCTUM_FALLEN_ChunkGenerator);
        context.register(CATACLYSM_SANCTUM_FALLEN_KEY, SANCTUM_FALLEN_stem);

        // 灵魂锻造厂 - 灵魂沙峡谷群系 (SOUL_SAND_VALLEY)
        NoiseBasedChunkGenerator SOULS_ANVIL_ChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.SOUL_SAND_VALLEY)),
                noiseGenSettings.getOrThrow(CDNoiseSettings.BIG_NETHER)) {
            @Override
            public int getGenDepth() {
                return 128; // 降低生成高度适应灵魂峡谷特性
            }
        };
        LevelStem SOULS_ANVIL_stem = new LevelStem(dimTypes.getOrThrow(CataclysmDimensions.CATACLYSM_SOULS_ANVIL_DIM_TYPE), SOULS_ANVIL_ChunkGenerator);
        context.register(CATACLYSM_SOULS_ANVIL_KEY, SOULS_ANVIL_stem);

        // 熔岩竞技场 - 地狱荒地群系 (NETHER_WASTES)
        NoiseBasedChunkGenerator INFERNOS_MAW_ChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.NETHER_WASTES)),
                noiseGenSettings.getOrThrow(CDNoiseSettings.BIG_NETHER));
        LevelStem INFERNOS_MAW_stem = new LevelStem(dimTypes.getOrThrow(CataclysmDimensions.CATACLYSM_INFERNOS_MAW_DIM_TYPE), INFERNOS_MAW_ChunkGenerator);
        context.register(CATACLYSM_INFERNOS_MAW_KEY, INFERNOS_MAW_stem);

        // 废弃堡垒 - 末地荒地群系 (END_BARRENS)
        NoiseBasedChunkGenerator BASTION_LOST_ChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.THE_END)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.END)) {
            @Override
            public int getSpawnHeight(@NotNull LevelHeightAccessor world) {
                return 65; // 调整末地堡垒生成高度
            }
        };
        LevelStem BASTION_LOST_stem = new LevelStem(dimTypes.getOrThrow(CataclysmDimensions.CATACLYSM_BASTION_LOST_DIM_TYPE), BASTION_LOST_ChunkGenerator);
        context.register(CATACLYSM_BASTION_LOST_KEY, BASTION_LOST_stem);

    }
}