package com.p1nero.cataclysm_dimension.worldgen;

import com.p1nero.cataclysm_dimension.CataclysmDimensionMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;


public class CDNoiseSettings {
    public static final ResourceKey<NoiseGeneratorSettings> PLAIN = createNoiseGeneratorKey("plain_noise_gen");
    public static final ResourceKey<NoiseGeneratorSettings> SEA = createNoiseGeneratorKey("sea");
    public static final ResourceKey<NoiseGeneratorSettings> DEEP_SEA = createNoiseGeneratorKey("deep_sea");
    public static final ResourceKey<NoiseGeneratorSettings> DESERT = createNoiseGeneratorKey("no_sea");
    public static final ResourceKey<NoiseGeneratorSettings> ARENA = createNoiseGeneratorKey("arena");
    public static final ResourceKey<NoiseGeneratorSettings> SOUL = createNoiseGeneratorKey("soul");
    public static final ResourceKey<NoiseGeneratorSettings> AIR = createNoiseGeneratorKey("air");

    private static ResourceKey<NoiseGeneratorSettings> createNoiseGeneratorKey(String name) {
        return ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(CataclysmDimensionMod.MOD_ID, name));
    }

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noise = context.lookup(Registries.NOISE);
        context.register(PLAIN, plainNoise(densityFunctions, noise));
        context.register(SEA, overworldWithSeaLevel(context, false, false, 128));
//        context.register(DEEP_SEA, sunkenCity(densityFunctions, noise));
        context.register(DESERT, plainDesert(densityFunctions, noise));
        context.register(ARENA, arena(densityFunctions, noise));
        context.register(SOUL, arena(densityFunctions, noise));
        context.register(AIR, new NoiseGeneratorSettings(new NoiseSettings(0, 128, 2, 1),
                Blocks.AIR.defaultBlockState(),
                Blocks.AIR.defaultBlockState(),
                new NoiseRouter(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero()),
                CDSurfaceRuleData.air(),
                List.of(),
                0,
                true,
                false,
                false,
                true));
    }

    public static NoiseGeneratorSettings plainNoise(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
        return new NoiseGeneratorSettings(
                new NoiseSettings(32, 256, 1, 2),
                Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(),
                overworldZIP(densityFunctions, noiseParameters),
                CDSurfaceRuleData.overworld(), (new OverworldBiomeBuilder()).spawnTarget(),
                63,
                false,
                true,
                true,
                false);
    }


    public static NoiseGeneratorSettings plainDesert(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
        return new NoiseGeneratorSettings(
                new NoiseSettings(32, 256, 1, 2),
                Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(),
                overworldZIP(densityFunctions, noiseParameters),
                CDSurfaceRuleData.overworld(),
                (new OverworldBiomeBuilder()).spawnTarget(),
                -128,
                false,
                true,
                true,
                false);
    }

    public static NoiseGeneratorSettings arena(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
        return new NoiseGeneratorSettings(new NoiseSettings(0, 128, 1, 1),
                Blocks.NETHERRACK.defaultBlockState(),
                Blocks.LAVA.defaultBlockState(),
                nether(densityFunctions, noiseParameters),
                CDSurfaceRuleData.nether(),
                List.of(),
                32,
                false,
                false,
                false,
                true);
    }



    public static NoiseGeneratorSettings soul(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
        return new NoiseGeneratorSettings(new NoiseSettings(-128, 384, 1, 1),
                Blocks.NETHERRACK.defaultBlockState(),
                Blocks.LAVA.defaultBlockState(),
                soulNoiseRouter(densityFunctions, noiseParameters),
                CDSurfaceRuleData.nether(),
                List.of(),
                64,
                false,
                false,
                false,
                true);
    }

    public static NoiseGeneratorSettings sunkenCity(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
        return new NoiseGeneratorSettings(
                new NoiseSettings(-128, 384, 1, 1),
                Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(),
                overworldZIP2(densityFunctions, noiseParameters),
                CDSurfaceRuleData.overworld(),
                (new OverworldBiomeBuilder()).spawnTarget(),
                63,
                false,
                true,
                true,
                false);
    }

    public static NoiseGeneratorSettings overworldWithSeaLevel(BootstrapContext<?> context, boolean large, boolean amplified, int seaLevel) {
        return new NoiseGeneratorSettings(
                new NoiseSettings(-64, 384, 1, 2),
                Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(),
                NoiseRouterData.overworld(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE), amplified, large),
                CDSurfaceRuleData.overworld(),
                new OverworldBiomeBuilder().spawnTarget(),
                seaLevel,
                false,
                true,
                true,
                false);
    }

    protected static NoiseRouter overworldZIP(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
        DensityFunction $$4 = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_BARRIER), 0.5);
        DensityFunction $$5 = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        DensityFunction $$6 = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143);
        DensityFunction $$7 = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_LAVA));
        DensityFunction $$8 = getFunction(densityFunctions, SHIFT_X);
        DensityFunction $$9 = getFunction(densityFunctions, SHIFT_Z);
        DensityFunction $$10 = DensityFunctions.shiftedNoise2d($$8, $$9, 0.25, noiseParameters.getOrThrow(Noises.TEMPERATURE));
        DensityFunction $$11 = DensityFunctions.shiftedNoise2d($$8, $$9, 0.25, noiseParameters.getOrThrow(Noises.VEGETATION));
        DensityFunction $$12 = getFunction(densityFunctions, FACTOR);
        DensityFunction $$13 = getFunction(densityFunctions, DEPTH);
        DensityFunction $$14 = noiseGradientDensity(DensityFunctions.cache2d($$12), $$13);

        DensityFunction $$15 = getFunction(densityFunctions, SLOPED_CHEESE);
        DensityFunction function0 = DensityFunctions.interpolated($$15);
        DensityFunction function =  DensityFunctions.add(DensityFunctions.yClampedGradient(-32, 256, 100, -100), function0);//缩放统一为平原

        return new NoiseRouter($$4, $$5, $$6, $$7, $$10, $$11, getFunction(densityFunctions, CONTINENTS), getFunction(densityFunctions, EROSION), $$13, getFunction(densityFunctions, RIDGES), slideOverworld(false, DensityFunctions.add($$14, DensityFunctions.constant(-0.703125)).clamp(-64.0, 64.0)), function, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
    }

    protected static NoiseRouter overworldZIP2(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
        DensityFunction $$4 = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_BARRIER), 0.2);
        DensityFunction $$5 = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.37);
        DensityFunction $$6 = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143);
        DensityFunction $$7 = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_LAVA));
        DensityFunction $$8 = getFunction(densityFunctions, SHIFT_X);
        DensityFunction $$9 = getFunction(densityFunctions, SHIFT_Z);
        DensityFunction $$10 = DensityFunctions.shiftedNoise2d($$8, $$9, 0.25, noiseParameters.getOrThrow(Noises.TEMPERATURE));
        DensityFunction $$11 = DensityFunctions.shiftedNoise2d($$8, $$9, 0.25, noiseParameters.getOrThrow(Noises.VEGETATION));
        DensityFunction $$12 = getFunction(densityFunctions, FACTOR);
        DensityFunction $$13 = getFunction(densityFunctions, DEPTH);
        DensityFunction $$14 = noiseGradientDensity(DensityFunctions.cache2d($$12), $$13);

        DensityFunction $$15 = getFunction(densityFunctions, CONTINENTS);
        DensityFunction function =  DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 0, 1.3, -1.5), $$15);

        return new NoiseRouter($$4, $$5, $$6, $$7, $$10, $$11, getFunction(densityFunctions, CONTINENTS), getFunction(densityFunctions, EROSION), $$13, getFunction(densityFunctions, RIDGES), slideOverworld(false, DensityFunctions.add($$14, DensityFunctions.constant(-0.703125)).clamp(-32.0, 32.0)), function, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
    }


    protected static NoiseRouter soulNoiseRouter(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
        DensityFunction $$4 = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_BARRIER), 0.5);
        DensityFunction $$5 = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        DensityFunction $$6 = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143);
        DensityFunction $$7 = DensityFunctions.noise(noiseParameters.getOrThrow(Noises.AQUIFER_LAVA));
        DensityFunction $$8 = getFunction(densityFunctions, SHIFT_X);
        DensityFunction $$9 = getFunction(densityFunctions, SHIFT_Z);
        DensityFunction $$10 = DensityFunctions.shiftedNoise2d($$8, $$9, 0.25, noiseParameters.getOrThrow(Noises.TEMPERATURE));
        DensityFunction $$11 = DensityFunctions.shiftedNoise2d($$8, $$9, 0.25, noiseParameters.getOrThrow(Noises.VEGETATION));
        DensityFunction $$12 = getFunction(densityFunctions, FACTOR);
        DensityFunction $$13 = getFunction(densityFunctions, DEPTH);
        DensityFunction $$14 = noiseGradientDensity(DensityFunctions.cache2d($$12), $$13);

        DensityFunction $$15 = getFunction(densityFunctions, SLOPED_CHEESE);
        DensityFunction function0 = DensityFunctions.interpolated($$15);

        return new NoiseRouter($$4, $$5, $$6, $$7, $$10, $$11, getFunction(densityFunctions, CONTINENTS), getFunction(densityFunctions, EROSION), $$13, getFunction(densityFunctions, RIDGES), slideOverworld(false, DensityFunctions.add($$14, DensityFunctions.constant(-0.703125)).clamp(-32.0, 32.0)), function0, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
    }

    protected static NoiseRouter nether(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
        return noNewCaves(densityFunctions, noiseParameters, slideNetherLike(densityFunctions, 0, 128));
    }

    private static DensityFunction slideNetherLike(HolderGetter<DensityFunction> densityFunctions, int minY, int maxY) {
        return slide(getFunction(densityFunctions, MY_BASE_3D_NOISE_NETHER), minY, maxY, 24, 0, 0.9375F, -8, 24, (double)2.5F);
    }

    private static NoiseRouter noNewCaves(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters, DensityFunction p_256378_) {
        DensityFunction densityfunction = getFunction(densityFunctions, SHIFT_X);
        DensityFunction densityfunction1 = getFunction(densityFunctions, SHIFT_Z);
        DensityFunction densityfunction2 = DensityFunctions.shiftedNoise2d(densityfunction, densityfunction1, (double)0.25F, noiseParameters.getOrThrow(Noises.TEMPERATURE));
        DensityFunction densityfunction3 = DensityFunctions.shiftedNoise2d(densityfunction, densityfunction1, (double)0.25F, noiseParameters.getOrThrow(Noises.VEGETATION));
        DensityFunction densityfunction4 = postProcess(p_256378_);
        return new NoiseRouter(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), densityfunction2, densityfunction3, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), densityfunction4, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
    }

    private static final ResourceKey<DensityFunction> SHIFT_X = createKey("shift_x");
    private static final ResourceKey<DensityFunction> SHIFT_Z = createKey("shift_z");
    public static final ResourceKey<DensityFunction> CONTINENTS = createKey("overworld/continents");
    public static final ResourceKey<DensityFunction> EROSION = createKey("overworld/erosion");
    public static final ResourceKey<DensityFunction> RIDGES = createKey("overworld/ridges");
    public static final ResourceKey<DensityFunction> FACTOR = createKey("overworld/factor");
    public static final ResourceKey<DensityFunction> DEPTH = createKey("overworld/depth");
    private static final ResourceKey<DensityFunction> SLOPED_CHEESE = createKey("overworld/sloped_cheese");
    private static final ResourceKey<DensityFunction> MY_BASE_3D_NOISE_NETHER = ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.fromNamespaceAndPath(CataclysmDimensionMod.MOD_ID, "nether/base_3d_noise"));

    private static ResourceKey<DensityFunction> createKey(String location) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.withDefaultNamespace(location));
    }

    private static DensityFunction slideOverworld(boolean amplified, DensityFunction densityFunction) {
        return slide(densityFunction, -64, 384, amplified ? 16 : 80, amplified ? 0 : 64, -0.078125, 0, 24, amplified ? 0.4 : 0.1171875);
    }

    private static DensityFunction noiseGradientDensity(DensityFunction p_212272_, DensityFunction p_212273_) {
        DensityFunction $$2 = DensityFunctions.mul(p_212273_, p_212272_);
        return DensityFunctions.mul(DensityFunctions.constant(4.0), $$2.quarterNegative());
    }

    private static DensityFunction postProcess(DensityFunction p_224493_) {
        DensityFunction $$1 = DensityFunctions.blendDensity(p_224493_);
        return DensityFunctions.mul(DensityFunctions.interpolated($$1), DensityFunctions.constant(0.64)).squeeze();
    }

    private static DensityFunction slide(DensityFunction density, int minY, int maxY, int fromYTop, int toYTop, double offset1, int fromYBottom, int toYBottom, double offset2) {
        DensityFunction topSlide = DensityFunctions.yClampedGradient(minY + maxY - fromYTop, minY + maxY - toYTop, 1, 0);
        density = DensityFunctions.lerp(topSlide, offset1, density);
        DensityFunction bottomSlide = DensityFunctions.yClampedGradient(minY + fromYBottom, minY + toYBottom, 0, 1);
        return DensityFunctions.lerp(bottomSlide, offset2, density);
    }

    private static DensityFunction getFunction(HolderGetter<DensityFunction> densityFunctions, ResourceKey<DensityFunction> key) {
        return new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(key));
    }

}
