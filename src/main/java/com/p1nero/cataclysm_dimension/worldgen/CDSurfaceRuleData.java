package com.p1nero.cataclysm_dimension.worldgen;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class CDSurfaceRuleData {
    private static final SurfaceRules.RuleSource AIR;
    private static final SurfaceRules.RuleSource BEDROCK;
    private static final SurfaceRules.RuleSource WHITE_TERRACOTTA;
    private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA;
    private static final SurfaceRules.RuleSource TERRACOTTA;
    private static final SurfaceRules.RuleSource RED_SAND;
    private static final SurfaceRules.RuleSource RED_SANDSTONE;
    private static final SurfaceRules.RuleSource STONE;
    private static final SurfaceRules.RuleSource DEEPSLATE;
    private static final SurfaceRules.RuleSource DIRT;
    private static final SurfaceRules.RuleSource PODZOL;
    private static final SurfaceRules.RuleSource COARSE_DIRT;
    private static final SurfaceRules.RuleSource MYCELIUM;
    private static final SurfaceRules.RuleSource GRASS_BLOCK;
    private static final SurfaceRules.RuleSource CALCITE;
    private static final SurfaceRules.RuleSource GRAVEL;
    private static final SurfaceRules.RuleSource SAND;
    private static final SurfaceRules.RuleSource SANDSTONE;
    private static final SurfaceRules.RuleSource PACKED_ICE;
    private static final SurfaceRules.RuleSource SNOW_BLOCK;
    private static final SurfaceRules.RuleSource MUD;
    private static final SurfaceRules.RuleSource POWDER_SNOW;
    private static final SurfaceRules.RuleSource ICE;
    private static final SurfaceRules.RuleSource WATER;
    private static final SurfaceRules.RuleSource LAVA;
    private static final SurfaceRules.RuleSource NETHERRACK;
    private static final SurfaceRules.RuleSource SOUL_SAND;
    private static final SurfaceRules.RuleSource SOUL_SOIL;
    private static final SurfaceRules.RuleSource BASALT;
    private static final SurfaceRules.RuleSource BLACKSTONE;
    private static final SurfaceRules.RuleSource WARPED_WART_BLOCK;
    private static final SurfaceRules.RuleSource WARPED_NYLIUM;
    private static final SurfaceRules.RuleSource NETHER_WART_BLOCK;
    private static final SurfaceRules.RuleSource CRIMSON_NYLIUM;
    private static final SurfaceRules.RuleSource ENDSTONE;

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }

    public static SurfaceRules.RuleSource overworld() {
        return overworldLike(true, false, true);
    }

    public static SurfaceRules.RuleSource overworldLike(boolean p_198381_, boolean p_198382_, boolean p_198383_) {
        SurfaceRules.ConditionSource surfacerules$conditionsource = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(97), 2);
        SurfaceRules.ConditionSource surfacerules$conditionsource1 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource2 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(63), -1);
        SurfaceRules.ConditionSource surfacerules$conditionsource3 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(74), 1);
        SurfaceRules.ConditionSource surfacerules$conditionsource4 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(60), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource5 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource6 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource7 = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource8 = SurfaceRules.waterBlockCheck(0, 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource9 = SurfaceRules.waterStartCheck(-6, -1);
        SurfaceRules.ConditionSource surfacerules$conditionsource10 = SurfaceRules.hole();
        SurfaceRules.ConditionSource surfacerules$conditionsource11 = SurfaceRules.isBiome(Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN, CDBiomes.DEEP_OCEAN);
        SurfaceRules.ConditionSource surfacerules$conditionsource12 = SurfaceRules.steep();
        SurfaceRules.RuleSource surfacerules$rulesource = SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource8, GRASS_BLOCK), DIRT);
        SurfaceRules.RuleSource surfacerules$rulesource1 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND);
        SurfaceRules.RuleSource surfacerules$rulesource2 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL);
        SurfaceRules.ConditionSource surfacerules$conditionsource13 = SurfaceRules.isBiome(CDBiomes.WARM_OCEAN, Biomes.WARM_OCEAN, Biomes.BEACH, Biomes.SNOWY_BEACH);
        SurfaceRules.ConditionSource surfacerules$conditionsource14 = SurfaceRules.isBiome(Biomes.DESERT, CDBiomes.DESERT);
        SurfaceRules.RuleSource surfacerules$rulesource3 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.STONY_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.CALCITE, -0.0125D, 0.0125D), CALCITE), STONE)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.STONY_SHORE), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.GRAVEL, -0.05D, 0.05D), surfacerules$rulesource2), STONE)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_HILLS), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE)), SurfaceRules.ifTrue(surfacerules$conditionsource13, surfacerules$rulesource1), SurfaceRules.ifTrue(surfacerules$conditionsource14, surfacerules$rulesource1), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.DRIPSTONE_CAVES), STONE));
        SurfaceRules.RuleSource surfacerules$rulesource4 = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45D, 0.58D), SurfaceRules.ifTrue(surfacerules$conditionsource8, POWDER_SNOW));
        SurfaceRules.RuleSource surfacerules$rulesource5 = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.35D, 0.6D), SurfaceRules.ifTrue(surfacerules$conditionsource8, POWDER_SNOW));
        SurfaceRules.RuleSource surfacerules$rulesource6 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource12, PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, -0.5D, 0.2D), PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, -0.0625D, 0.025D), ICE), SurfaceRules.ifTrue(surfacerules$conditionsource8, SNOW_BLOCK))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SNOWY_SLOPES), SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource12, STONE), surfacerules$rulesource4, SurfaceRules.ifTrue(surfacerules$conditionsource8, SNOW_BLOCK))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.JAGGED_PEAKS), STONE), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.GROVE), SurfaceRules.sequence(surfacerules$rulesource4, DIRT)), surfacerules$rulesource3, SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA), SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS), SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), surfacerules$rulesource2), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE), SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), DIRT), surfacerules$rulesource2)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MANGROVE_SWAMP), MUD), DIRT);
        SurfaceRules.RuleSource surfacerules$rulesource7 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource12, PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, 0.0D, 0.2D), PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, 0.0D, 0.025D), ICE), SurfaceRules.ifTrue(surfacerules$conditionsource8, SNOW_BLOCK))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SNOWY_SLOPES), SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource12, STONE), surfacerules$rulesource5, SurfaceRules.ifTrue(surfacerules$conditionsource8, SNOW_BLOCK))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.JAGGED_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource12, STONE), SurfaceRules.ifTrue(surfacerules$conditionsource8, SNOW_BLOCK))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.GROVE), SurfaceRules.sequence(surfacerules$rulesource5, SurfaceRules.ifTrue(surfacerules$conditionsource8, SNOW_BLOCK))), surfacerules$rulesource3, SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA), SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE), SurfaceRules.ifTrue(surfaceNoiseAbove(-0.5D), COARSE_DIRT))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS), SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), surfacerules$rulesource2), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE), SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), surfacerules$rulesource), surfacerules$rulesource2)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA), SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), COARSE_DIRT), SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95D), PODZOL))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.ICE_SPIKES), SurfaceRules.ifTrue(surfacerules$conditionsource8, SNOW_BLOCK)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MANGROVE_SWAMP), MUD), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MUSHROOM_FIELDS), MYCELIUM), surfacerules$rulesource);
        SurfaceRules.ConditionSource surfacerules$conditionsource15 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.909D, -0.5454D);
        SurfaceRules.ConditionSource surfacerules$conditionsource16 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.1818D, 0.1818D);
        SurfaceRules.ConditionSource surfacerules$conditionsource17 = SurfaceRules.noiseCondition(Noises.SURFACE, 0.5454D, 0.909D);
        SurfaceRules.RuleSource surfacerules$rulesource8 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WOODED_BADLANDS, CDBiomes.BADLANDS), SurfaceRules.ifTrue(surfacerules$conditionsource, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource15, COARSE_DIRT), SurfaceRules.ifTrue(surfacerules$conditionsource16, COARSE_DIRT), SurfaceRules.ifTrue(surfacerules$conditionsource17, COARSE_DIRT), surfacerules$rulesource))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SWAMP), SurfaceRules.ifTrue(surfacerules$conditionsource5, SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource6), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER)))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MANGROVE_SWAMP), SurfaceRules.ifTrue(surfacerules$conditionsource4, SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource6), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER)))))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS, CDBiomes.BADLANDS), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource1, ORANGE_TERRACOTTA), SurfaceRules.ifTrue(surfacerules$conditionsource3, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource15, TERRACOTTA), SurfaceRules.ifTrue(surfacerules$conditionsource16, TERRACOTTA), SurfaceRules.ifTrue(surfacerules$conditionsource17, TERRACOTTA), SurfaceRules.bandlands())), SurfaceRules.ifTrue(surfacerules$conditionsource7, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, RED_SANDSTONE), RED_SAND)), SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource10), ORANGE_TERRACOTTA), SurfaceRules.ifTrue(surfacerules$conditionsource9, WHITE_TERRACOTTA), surfacerules$rulesource2)), SurfaceRules.ifTrue(surfacerules$conditionsource2, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource6, SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource3), ORANGE_TERRACOTTA)), SurfaceRules.bandlands())), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(surfacerules$conditionsource9, WHITE_TERRACOTTA)))), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(surfacerules$conditionsource7, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource11, SurfaceRules.ifTrue(surfacerules$conditionsource10, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource8, AIR), SurfaceRules.ifTrue(SurfaceRules.temperature(), ICE), WATER))), surfacerules$rulesource7))), SurfaceRules.ifTrue(surfacerules$conditionsource9, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(surfacerules$conditionsource11, SurfaceRules.ifTrue(surfacerules$conditionsource10, WATER))), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, surfacerules$rulesource6), SurfaceRules.ifTrue(surfacerules$conditionsource13, SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, SANDSTONE)), SurfaceRules.ifTrue(surfacerules$conditionsource14, SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, SANDSTONE)))), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS), STONE), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, CDBiomes.WARM_OCEAN), surfacerules$rulesource1), surfacerules$rulesource2)));
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        if (p_198382_) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK));
        }

        if (p_198383_) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
        }

        SurfaceRules.RuleSource surfacerules$rulesource9 = SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), surfacerules$rulesource8);
        builder.add(p_198381_ ? surfacerules$rulesource9 : surfacerules$rulesource8);
        builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }

    public static SurfaceRules.RuleSource nether() {
        SurfaceRules.ConditionSource surfacerules$conditionsource = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(31), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource1 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(32), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource2 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(30), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource3 = SurfaceRules.not(SurfaceRules.yStartCheck(VerticalAnchor.absolute(35), 0));
        SurfaceRules.ConditionSource surfacerules$conditionsource4 = SurfaceRules.yBlockCheck(VerticalAnchor.belowTop(5), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource5 = SurfaceRules.hole();
        SurfaceRules.ConditionSource surfacerules$conditionsource6 = SurfaceRules.noiseCondition(Noises.SOUL_SAND_LAYER, -0.012);
        SurfaceRules.ConditionSource surfacerules$conditionsource7 = SurfaceRules.noiseCondition(Noises.GRAVEL_LAYER, -0.012);
        SurfaceRules.ConditionSource surfacerules$conditionsource8 = SurfaceRules.noiseCondition(Noises.PATCH, -0.012);
        SurfaceRules.ConditionSource surfacerules$conditionsource9 = SurfaceRules.noiseCondition(Noises.NETHERRACK, 0.54);
        SurfaceRules.ConditionSource surfacerules$conditionsource10 = SurfaceRules.noiseCondition(Noises.NETHER_WART, 1.17);
        SurfaceRules.ConditionSource surfacerules$conditionsource11 = SurfaceRules.noiseCondition(Noises.NETHER_STATE_SELECTOR, (double)0.0F);
        SurfaceRules.RuleSource surfacerules$rulesource = SurfaceRules.ifTrue(surfacerules$conditionsource8, SurfaceRules.ifTrue(surfacerules$conditionsource2, SurfaceRules.ifTrue(surfacerules$conditionsource3, GRAVEL)));
        return SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK), SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK), SurfaceRules.ifTrue(surfacerules$conditionsource4, NETHERRACK), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.BASALT_DELTAS), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, BASALT), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[]{surfacerules$rulesource, SurfaceRules.ifTrue(surfacerules$conditionsource11, BASALT), BLACKSTONE})))), SurfaceRules.ifTrue(SurfaceRules.isBiome(CDBiomes.SOUL_SAND_VALLEY, Biomes.SOUL_SAND_VALLEY), SurfaceRules.sequence(new SurfaceRules.RuleSource[]{SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, SurfaceRules.sequence(new SurfaceRules.RuleSource[]{SurfaceRules.ifTrue(surfacerules$conditionsource11, SOUL_SAND), SOUL_SOIL})), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[]{surfacerules$rulesource, SurfaceRules.ifTrue(surfacerules$conditionsource11, SOUL_SAND), SOUL_SOIL}))})), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[]{SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource1), SurfaceRules.ifTrue(surfacerules$conditionsource5, LAVA)), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[]{Biomes.WARPED_FOREST}), SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource9), SurfaceRules.ifTrue(surfacerules$conditionsource, SurfaceRules.sequence(new SurfaceRules.RuleSource[]{SurfaceRules.ifTrue(surfacerules$conditionsource10, WARPED_WART_BLOCK), WARPED_NYLIUM})))), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[]{Biomes.CRIMSON_FOREST}), SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource9), SurfaceRules.ifTrue(surfacerules$conditionsource, SurfaceRules.sequence(new SurfaceRules.RuleSource[]{SurfaceRules.ifTrue(surfacerules$conditionsource10, NETHER_WART_BLOCK), CRIMSON_NYLIUM}))))})), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[]{CDBiomes.NETHER_WASTES, Biomes.NETHER_WASTES}), SurfaceRules.sequence(new SurfaceRules.RuleSource[]{SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(surfacerules$conditionsource6, SurfaceRules.sequence(new SurfaceRules.RuleSource[]{SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource5), SurfaceRules.ifTrue(surfacerules$conditionsource2, SurfaceRules.ifTrue(surfacerules$conditionsource3, SOUL_SAND))), NETHERRACK}))), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(surfacerules$conditionsource, SurfaceRules.ifTrue(surfacerules$conditionsource3, SurfaceRules.ifTrue(surfacerules$conditionsource7, SurfaceRules.sequence(new SurfaceRules.RuleSource[]{SurfaceRules.ifTrue(surfacerules$conditionsource1, GRAVEL), SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource5), GRAVEL)})))))})), NETHERRACK);
    }

    public static SurfaceRules.RuleSource end() {
        return ENDSTONE;
    }

    public static SurfaceRules.RuleSource air() {
        return AIR;
    }

    private static SurfaceRules.ConditionSource surfaceNoiseAbove(double value) {
        return SurfaceRules.noiseCondition(Noises.SURFACE, value / (double)8.25F, Double.MAX_VALUE);
    }

    static {
        AIR = makeStateRule(Blocks.AIR);
        BEDROCK = makeStateRule(Blocks.BEDROCK);
        WHITE_TERRACOTTA = makeStateRule(Blocks.WHITE_TERRACOTTA);
        ORANGE_TERRACOTTA = makeStateRule(Blocks.ORANGE_TERRACOTTA);
        TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
        RED_SAND = makeStateRule(Blocks.RED_SAND);
        RED_SANDSTONE = makeStateRule(Blocks.RED_SANDSTONE);
        STONE = makeStateRule(Blocks.STONE);
        DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);
        DIRT = makeStateRule(Blocks.DIRT);
        PODZOL = makeStateRule(Blocks.PODZOL);
        COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
        MYCELIUM = makeStateRule(Blocks.MYCELIUM);
        GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
        CALCITE = makeStateRule(Blocks.CALCITE);
        GRAVEL = makeStateRule(Blocks.GRAVEL);
        SAND = makeStateRule(Blocks.SAND);
        SANDSTONE = makeStateRule(Blocks.SANDSTONE);
        PACKED_ICE = makeStateRule(Blocks.PACKED_ICE);
        SNOW_BLOCK = makeStateRule(Blocks.SNOW_BLOCK);
        MUD = makeStateRule(Blocks.MUD);
        POWDER_SNOW = makeStateRule(Blocks.POWDER_SNOW);
        ICE = makeStateRule(Blocks.ICE);
        WATER = makeStateRule(Blocks.WATER);
        LAVA = makeStateRule(Blocks.LAVA);
        NETHERRACK = makeStateRule(Blocks.NETHERRACK);
        SOUL_SAND = makeStateRule(Blocks.SOUL_SAND);
        SOUL_SOIL = makeStateRule(Blocks.SOUL_SOIL);
        BASALT = makeStateRule(Blocks.BASALT);
        BLACKSTONE = makeStateRule(Blocks.BLACKSTONE);
        WARPED_WART_BLOCK = makeStateRule(Blocks.WARPED_WART_BLOCK);
        WARPED_NYLIUM = makeStateRule(Blocks.WARPED_NYLIUM);
        NETHER_WART_BLOCK = makeStateRule(Blocks.NETHER_WART_BLOCK);
        CRIMSON_NYLIUM = makeStateRule(Blocks.CRIMSON_NYLIUM);
        ENDSTONE = makeStateRule(Blocks.END_STONE);
    }
}
