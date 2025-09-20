package com.p1nero.cataclysm_dimension;

import com.github.L_Ender.cataclysm.init.ModItems;
import com.mojang.logging.LogUtils;
import com.p1nero.cataclysm_dimension.worldgen.CataclysmDimensions;
import com.p1nero.cataclysm_dimension.worldgen.placements.CDPlacementTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.storage.IOWorker;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

@Mod(CataclysmDimensionMod.MOD_ID)
public class CataclysmDimensionMod {
    public static final String MOD_ID = "cataclysm_dimension";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CataclysmDimensionMod(ModContainer modContainer, IEventBus bus) {
        NeoForge.EVENT_BUS.addListener(this::onItemUse);
        NeoForge.EVENT_BUS.addListener(this::onToolTip);
        NeoForge.EVENT_BUS.addListener(this::onServerLevelTick);
        CataclysmDimensionModConfig.loadConfig();
        bus.addListener(this::onDatapackLoad);
        CDPlacementTypes.STRUCTURE_PLACEMENT_TYPES.register(bus);
    }

    private static final int COOLDOWN_TICKS = 600;
    private static final int DEFAULT_SLOW_FALLING_DURATION = 200;
    private static final int LONG_SLOW_FALLING_DURATION = 400;

    // 维度传送配置映射表
    private static final Map<Item, TeleportConfig> TELEPORT_CONFIGS = new HashMap<>();

    private void initTeleportConfigs() {
        // 初始化配置映射（实际使用时需要替换为对应的物品和维度键）
        TELEPORT_CONFIGS.put(ModItems.ABYSS_EYE.get(),
                new TeleportConfig(CataclysmDimensions.CATACLYSM_ABYSSAL_DEPTHS_LEVEL_KEY, 150, DEFAULT_SLOW_FALLING_DURATION));
        TELEPORT_CONFIGS.put(ModItems.MECH_EYE.get(),
                new TeleportConfig(CataclysmDimensions.CATACLYSM_FORGE_OF_AEONS_LEVEL_KEY, 150, DEFAULT_SLOW_FALLING_DURATION));
        TELEPORT_CONFIGS.put(ModItems.FLAME_EYE.get(),
                new TeleportConfig(CataclysmDimensions.CATACLYSM_INFERNOS_MAW_LEVEL_KEY, 64, DEFAULT_SLOW_FALLING_DURATION));
        TELEPORT_CONFIGS.put(ModItems.VOID_EYE.get(),
                new TeleportConfig(CataclysmDimensions.CATACLYSM_BASTION_LOST_LEVEL_KEY, 150, DEFAULT_SLOW_FALLING_DURATION));
        TELEPORT_CONFIGS.put(ModItems.MONSTROUS_EYE.get(),
                new TeleportConfig(CataclysmDimensions.CATACLYSM_SOULS_ANVIL_LEVEL_KEY, 64, DEFAULT_SLOW_FALLING_DURATION));
        TELEPORT_CONFIGS.put(ModItems.DESERT_EYE.get(),
                new TeleportConfig(CataclysmDimensions.CATACLYSM_PHARAOHS_BANE_LEVEL_KEY, 200, LONG_SLOW_FALLING_DURATION));
        TELEPORT_CONFIGS.put(ModItems.CURSED_EYE.get(),
                new TeleportConfig(CataclysmDimensions.CATACLYSM_ETERNAL_FROSTHOLD_LEVEL_KEY, 200, LONG_SLOW_FALLING_DURATION));
        TELEPORT_CONFIGS.put(ModItems.STORM_EYE.get(),
                new TeleportConfig(CataclysmDimensions.CATACLYSM_SANCTUM_FALLEN_LEVEL_KEY, 200, 0)); // 0表示无效果
    }

    private void onItemUse(LivingEntityUseItemEvent event) {
        if (!CataclysmDimensionModConfig.ENABLE_TELEPORT_EYE) {
            return;
        }

        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide || !entity.isShiftKeyDown()) {
            return;
        }

        ItemStack itemStack = event.getItem();
        MinecraftServer server = entity.level().getServer();
        if (server == null) {
            return;
        }

        if (entity instanceof Player player && player.getCooldowns().isOnCooldown(itemStack.getItem())) {
            return;
        }

        if(TELEPORT_CONFIGS.isEmpty()) {
            initTeleportConfigs();
        }

        TeleportConfig config = TELEPORT_CONFIGS.get(itemStack.getItem());
        if (config == null) {
            return;
        }

        ServerLevel targetLevel = server.getLevel(config.dimensionKey());
        if (targetLevel == null) {
            return;
        }

        if (CataclysmDimensionModConfig.SLOW_FALL_WHEN_ENTER_DIMENSIONS && config.effectDuration() > 0) {
            entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, config.effectDuration(), 1, false, true));
        }

        Vec3 targetPosition = new Vec3(0, config.targetY(), 0);
        entity.changeDimension(new DimensionTransition(targetLevel, targetPosition, Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND));

        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(itemStack.getItem(), COOLDOWN_TICKS);
        }
    }

    private record TeleportConfig(ResourceKey<Level> dimensionKey, int targetY, int effectDuration) {}

    /**
     * 记录是否删过了，用ResourceLocation是因为输出比较直观
     */
    public static final Map<ResourceLocation, Boolean> RESOURCE_KEY_BOOLEAN_MAP = new HashMap<>();

    /**
     * 没人就重置维度
     */
    private void onServerLevelTick(LevelTickEvent.Pre event) {
        if (event.getLevel() instanceof ServerLevel serverLevel && CataclysmDimensions.LEVELS.contains(serverLevel.dimension())) {
            ResourceLocation resourceLocation = serverLevel.dimension().location();
            if(!serverLevel.players().isEmpty()) {
                RESOURCE_KEY_BOOLEAN_MAP.put(resourceLocation, false);
            }
            if(serverLevel.players().isEmpty() && CataclysmDimensionModConfig.RESET_DIMENSION_IF_NO_PLAYER && !RESOURCE_KEY_BOOLEAN_MAP.getOrDefault(resourceLocation, false)) {
                try {
                    LOGGER.info("[Cataclysm Dimension]: No player inside. trying to reset dimension {}.", resourceLocation);
                    IOWorker ioWorker = ((IOWorker) serverLevel.getChunkSource().chunkScanner());
                    if(Files.exists(ioWorker.storage.folder)) {
                        serverLevel.getAllEntities().forEach(Entity::discard);
                        serverLevel.noSave = false;
                        serverLevel.save(null, true, true);
                        ioWorker.storage.regionCache.clear();
                        Files.walkFileTree(ioWorker.storage.folder, new SimpleFileVisitor<>(){
                            @Override
                            public @NotNull FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attrs) throws IOException {
                                if(Files.deleteIfExists(file)){
                                    LOGGER.info("[Cataclysm Dimension]: {} region cache Deleted.", resourceLocation);
                                }
                                return FileVisitResult.CONTINUE;
                            }
                        });
                    } else {
                        LOGGER.info("[Cataclysm Dimension]: No region files in {}. Skipped.", resourceLocation);
                    }
                    RESOURCE_KEY_BOOLEAN_MAP.put(resourceLocation, true);
                } catch (Exception e) {
                    LOGGER.error("[Cataclysm Dimension]: Failed to reset dimension {}.", resourceLocation, e);
                }
            }
        }
    }

    private void onToolTip(ItemTooltipEvent event) {
        if(!CataclysmDimensionModConfig.ENABLE_TELEPORT_EYE) {
            return;
        }
        if(TELEPORT_CONFIGS.isEmpty()) {
            initTeleportConfigs();
        }
        if(TELEPORT_CONFIGS.containsKey(event.getItemStack().getItem())) {
            event.getToolTip().add(Component.translatable("tip.cataclysm_dimension.enter").withStyle(ChatFormatting.GRAY));
        }
    }

    private void onDatapackLoad(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            String name = CataclysmDimensionModConfig.KEEP_STRUCTURES_IN_ORIGINAL_DIMENSIONS ? "packs/keep_original" : "packs/not_keep_original";
            event.addPackFinders(
                    ResourceLocation.fromNamespaceAndPath(CataclysmDimensionMod.MOD_ID, name),
                    PackType.SERVER_DATA,
                    Component.literal(name),
                    PackSource.WORLD,
                    true,
                    Pack.Position.TOP);

            if(CataclysmDimensionModConfig.RANDOM_SPREAD_IN_DIMENSION) {
                name = CataclysmDimensionModConfig.KEEP_STRUCTURES_IN_ORIGINAL_DIMENSIONS ? "packs/random_spread_dim" : "packs/random_spread";
                event.addPackFinders(
                        ResourceLocation.fromNamespaceAndPath(CataclysmDimensionMod.MOD_ID, name),
                        PackType.SERVER_DATA,
                        Component.literal(name),
                        PackSource.WORLD,
                        true,
                        Pack.Position.TOP);
            }
        }
    }

}
