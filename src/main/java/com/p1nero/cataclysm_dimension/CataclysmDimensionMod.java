package com.p1nero.cataclysm_dimension;

import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModItems;
import com.mojang.logging.LogUtils;
import com.p1nero.cataclysm_dimension.client.CataclysmDimensionClient;
import com.p1nero.cataclysm_dimension.entity.CDEntities;
import com.p1nero.cataclysm_dimension.entity.ReturnRiftEntity;
import com.p1nero.cataclysm_dimension.worldgen.CataclysmDimensions;
import com.p1nero.cataclysm_dimension.worldgen.placements.CDPlacementTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod(CataclysmDimensionMod.MOD_ID)
public class CataclysmDimensionMod {
    public static final String MOD_ID = "cataclysm_dimension";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CataclysmDimensionMod(ModContainer modContainer, IEventBus bus) {
        NeoForge.EVENT_BUS.addListener(this::onItemUse);
        NeoForge.EVENT_BUS.addListener(this::onToolTip);
        NeoForge.EVENT_BUS.addListener(this::onServerLevelTick);
        NeoForge.EVENT_BUS.addListener(this::onPlayerChangedDimension);
        NeoForge.EVENT_BUS.addListener(this::onServerStarting);
        NeoForge.EVENT_BUS.addListener(this::onServerAboutToStart);
        // LOWEST:让取消死亡(复活图腾类)的监听器先行,免得 boss 没死也开裂隙
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onBossDeath);
        NeoForge.EVENT_BUS.addListener(this::onEntityJoinLevel);
        CataclysmDimensionModConfig.loadConfig();
        bus.addListener(EventPriority.HIGHEST, this::onDatapackLoad);
        bus.addListener(this::onCommonSetup);
        CDPlacementTypes.STRUCTURE_PLACEMENT_TYPES.register(bus);
        CDEntities.ENTITY_TYPES.register(bus);
        if (FMLEnvironment.dist.isClient()) {
            CataclysmDimensionClient.init(bus);
        }
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        // 主线程建好两张对照表,免得懒初始化在单人下被两个线程同时首触
        event.enqueueWork(() -> {
            initTeleportConfigs();
            initDimensionBosses();
        });
    }

    private static final int COOLDOWN_TICKS = 600;
    private static final int DEFAULT_SLOW_FALLING_DURATION = 200;
    private static final int LONG_SLOW_FALLING_DURATION = 400;

    // 维度传送配置映射表
    private static final Map<Item, TeleportConfig> TELEPORT_CONFIGS = new HashMap<>();

    private static void initTeleportConfigs() {
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
                new TeleportConfig(CataclysmDimensions.CATACLYSM_SANCTUM_FALLEN_LEVEL_KEY, 200, DEFAULT_SLOW_FALLING_DURATION)); // 0表示无效果
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
        Item eyeItem = itemStack.getItem(); // 先记住眼睛物品:后面若消耗(shrink 到 0)itemStack.getItem() 会变成 AIR
        MinecraftServer server = entity.level().getServer();
        if (server == null) {
            return;
        }

        if (entity instanceof Player player && player.getCooldowns().isOnCooldown(eyeItem)) {
            return;
        }

        if (TELEPORT_CONFIGS.isEmpty()) {
            initTeleportConfigs();
        }

        TeleportConfig config = TELEPORT_CONFIGS.get(itemStack.getItem());
        if (config == null) {
            return;
        }

        // 身处灾变维度内时禁止使用眼睛(可配置,默认禁止)
        if (!CataclysmDimensionModConfig.ALLOW_USE_EYE_IN_DIMENSION
                && CataclysmDimensions.LEVELS.contains(entity.level().dimension())) {
            if (entity instanceof Player player) {
                player.displayClientMessage(Component.translatable("tip.cataclysm_dimension.eye_disabled_in_dimension").withStyle(ChatFormatting.RED), true);
            }
            return;
        }

        ServerLevel targetLevel = server.getLevel(config.dimensionKey());
        if (targetLevel == null) {
            return;
        }

        // 可配置:传送时消耗对应的眼睛物品(创造模式不消耗)
        if (CataclysmDimensionModConfig.CONSUME_TELEPORT_EYE
                && !(entity instanceof Player creativePlayer && creativePlayer.isCreative())) {
            itemStack.shrink(1);
        }

        if (CataclysmDimensionModConfig.SLOW_FALL_WHEN_ENTER_DIMENSIONS && config.effectDuration() > 0) {
            entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, config.effectDuration(), 1, false, true));
        }

        // 记住出发点,供归返裂隙送回(已在灾变维度里再用眼时保留最初的出发点)
        if (entity instanceof ServerPlayer serverPlayer
                && !CataclysmDimensions.LEVELS.contains(serverPlayer.level().dimension())) {
            saveReturnPoint(serverPlayer);
        }

        Vec3 targetPosition = new Vec3(0, config.targetY(), 0);
        if (entity.level() instanceof ServerLevel originLevel) {
            playDepartureEffects(originLevel, entity.position());
        }
        entity.changeDimension(new DimensionTransition(targetLevel, targetPosition, Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND));
        targetLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, targetPosition.x, targetPosition.y + 1.0, targetPosition.z, 60, 0.6, 1.2, 0.6, 0.05);

        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(eyeItem, COOLDOWN_TICKS);
        }
    }

    private record TeleportConfig(ResourceKey<Level> dimensionKey, int targetY, int effectDuration) {
    }

    /** 维度 -> 对应的眼睛(归返裂隙渲染用;未知维度退回末影之眼)。 */
    public static Item getEyeItemFor(ResourceKey<Level> dimension) {
        if (TELEPORT_CONFIGS.isEmpty()) {
            initTeleportConfigs();
        }
        for (Map.Entry<Item, TeleportConfig> entry : TELEPORT_CONFIGS.entrySet()) {
            if (entry.getValue().dimensionKey().equals(dimension)) {
                return entry.getKey();
            }
        }
        return Items.ENDER_EYE;
    }

    // ===== 归返裂隙:大 boss 被击杀后在死亡点开启,右键送玩家回进入前的出发点 =====
    private static final String RETURN_POINT_TAG = MOD_ID + ":return_point";
    // 维度 -> 该维度的主 boss(精确匹配,小怪不触发)。熔岩竞技场是 Ignis,不是守门的 Ignited Revenant。
    private static final Map<ResourceKey<Level>, EntityType<?>> DIMENSION_BOSSES = new HashMap<>();
    // 已重盖、待清旧裂隙的维度:异步加载晚到的旧裂隙靠它拦(见 onEntityJoinLevel)
    private static final Set<ResourceLocation> PURGE_STALE_RIFTS = new HashSet<>();

    private static void initDimensionBosses() {
        DIMENSION_BOSSES.put(CataclysmDimensions.CATACLYSM_FORGE_OF_AEONS_LEVEL_KEY, ModEntities.THE_HARBINGER.get());
        DIMENSION_BOSSES.put(CataclysmDimensions.CATACLYSM_ABYSSAL_DEPTHS_LEVEL_KEY, ModEntities.THE_LEVIATHAN.get());
        DIMENSION_BOSSES.put(CataclysmDimensions.CATACLYSM_PHARAOHS_BANE_LEVEL_KEY, ModEntities.ANCIENT_REMNANT.get());
        DIMENSION_BOSSES.put(CataclysmDimensions.CATACLYSM_ETERNAL_FROSTHOLD_LEVEL_KEY, ModEntities.MALEDICTUS.get());
        DIMENSION_BOSSES.put(CataclysmDimensions.CATACLYSM_SANCTUM_FALLEN_LEVEL_KEY, ModEntities.SCYLLA.get());
        DIMENSION_BOSSES.put(CataclysmDimensions.CATACLYSM_SOULS_ANVIL_LEVEL_KEY, ModEntities.NETHERITE_MONSTROSITY.get());
        DIMENSION_BOSSES.put(CataclysmDimensions.CATACLYSM_INFERNOS_MAW_LEVEL_KEY, ModEntities.IGNIS.get());
        DIMENSION_BOSSES.put(CataclysmDimensions.CATACLYSM_BASTION_LOST_LEVEL_KEY, ModEntities.ENDER_GUARDIAN.get());
    }

    /**
     * 主 boss 被击杀:打赢才重置模式下此刻标脏,再按开关在死亡点开归返裂隙。
     */
    private void onBossDeath(LivingDeathEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) {
            return;
        }
        ResourceKey<Level> dimension = level.dimension();
        if (!CataclysmDimensions.LEVELS.contains(dimension)) {
            return;
        }
        if (DIMENSION_BOSSES.isEmpty()) {
            initDimensionBosses();
        }
        if (event.getEntity().getType() != DIMENSION_BOSSES.get(dimension)) {
            return;
        }
        if (CataclysmDimensionModConfig.RESET_STRUCTURE_REQUIRES_BOSS_KILL && DIRTY_DIMS.add(dimension.location())) {
            saveDirtyDims(level.getServer());
        }
        if (!CataclysmDimensionModConfig.ENABLE_RETURN_RIFT) {
            return;
        }
        // 已有裂隙(上一局残留 / 同局再杀)就不再开新的
        if (!level.getEntities(CDEntities.RETURN_RIFT.get(), rift -> true).isEmpty()) {
            return;
        }
        spawnReturnRift(level, event.getEntity().position());
    }

    /**
     * 旧裂隙可能在 restoreStructure 清扫后才从盘上异步载入;对已重盖的维度,这里把迟到的旧裂隙拦下。
     * 只拦 loadedFromDisk,不误伤新开的裂隙。
     */
    private void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.loadedFromDisk()
                && event.getEntity() instanceof ReturnRiftEntity
                && event.getLevel() instanceof ServerLevel level
                && PURGE_STALE_RIFTS.contains(level.dimension().location())) {
            event.setCanceled(true);
        }
    }

    private static void spawnReturnRift(ServerLevel level, Vec3 deathPos) {
        PURGE_STALE_RIFTS.remove(level.dimension().location());
        Vec3 pos = deathPos.add(0, 1.0, 0);
        if (pos.y < level.getMinBuildHeight() + 2) {
            // boss 坠入虚空死亡时,裂隙改开在出生点地表,否则玩家够不着
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, 0, 0);
            pos = new Vec3(0.5, y + 1.0, 0.5);
        }
        ReturnRiftEntity rift = new ReturnRiftEntity(CDEntities.RETURN_RIFT.get(), level);
        rift.setPos(pos);
        level.addFreshEntity(rift);
        level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.END_PORTAL_SPAWN, SoundSource.NEUTRAL, 0.8F, 1.2F);
        level.sendParticles(ParticleTypes.REVERSE_PORTAL, pos.x, pos.y + 0.5, pos.z, 80, 0.8, 0.8, 0.8, 0.1);
        level.sendParticles(ParticleTypes.END_ROD, pos.x, pos.y + 0.5, pos.z, 30, 0.5, 0.5, 0.5, 0.05);
        Component tip = Component.translatable("tip.cataclysm_dimension.rift_opened").withStyle(ChatFormatting.LIGHT_PURPLE);
        for (ServerPlayer player : level.players()) {
            player.displayClientMessage(tip, true);
        }
        LOGGER.info("[Cataclysm Dimension]: boss defeated in {}; return rift opened at {}.", level.dimension().location(), pos);
    }

    private static void saveReturnPoint(ServerPlayer player) {
        CompoundTag point = new CompoundTag();
        point.putString("dim", player.level().dimension().location().toString());
        point.putDouble("x", player.getX());
        point.putDouble("y", player.getY());
        point.putDouble("z", player.getZ());
        point.putFloat("yRot", player.getYRot());
        point.putFloat("xRot", player.getXRot());
        player.getPersistentData().put(RETURN_POINT_TAG, point);
    }

    /** 归返裂隙右键:送回进入维度前记录的出发点;记录缺失 / 维度不存在时退回主世界出生点。 */
    public static void teleportBack(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }
        player.stopRiding(); // 骑乘中跨维传送前先下坐骑
        ServerLevel targetLevel = null;
        Vec3 targetPos = null;
        float yRot = player.getYRot();
        float xRot = player.getXRot();
        CompoundTag data = player.getPersistentData();
        if (data.contains(RETURN_POINT_TAG)) {
            CompoundTag point = data.getCompound(RETURN_POINT_TAG);
            ResourceLocation dim = ResourceLocation.tryParse(point.getString("dim"));
            ServerLevel level = dim == null ? null : server.getLevel(ResourceKey.create(Registries.DIMENSION, dim));
            if (level != null) {
                targetLevel = level;
                targetPos = new Vec3(point.getDouble("x"), point.getDouble("y"), point.getDouble("z"));
                yRot = point.getFloat("yRot");
                xRot = point.getFloat("xRot");
            }
        }
        if (targetLevel == null) {
            targetLevel = server.overworld();
            BlockPos spawn = targetLevel.getSharedSpawnPos();
            targetPos = Vec3.atBottomCenterOf(spawn.atY(targetLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawn.getX(), spawn.getZ())));
        }
        playDepartureEffects(player.serverLevel(), player.position());
        player.changeDimension(new DimensionTransition(targetLevel, targetPos, Vec3.ZERO, yRot, xRot, DimensionTransition.PLAY_PORTAL_SOUND));
        targetLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, targetPos.x, targetPos.y + 1.0, targetPos.z, 60, 0.6, 1.2, 0.6, 0.05);
    }

    /** 出发点的离场音效 + 粒子(眼睛传送与裂隙归返共用)。 */
    private static void playDepartureEffects(ServerLevel level, Vec3 pos) {
        level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 0.7F);
        level.sendParticles(ParticleTypes.PORTAL, pos.x, pos.y + 1.0, pos.z, 40, 0.5, 1.0, 0.5, 0.3);
    }

    // ===== 结构地形重置(局内、绕开 C2ME):玩家再进用过的 boss 维度时,把该 boss 结构重盖成完好 =====
    // "脏"维度:本会话被玩家占用过(可能打坏了竞技场)的灾变维度。
    private static final Set<ResourceLocation> DIRTY_DIMS = new HashSet<>();
    // 待执行的结构重盖:维度 -> 触发玩家的落点(用于定位 boss 结构),以及剩余延迟 tick(等区块加载好再重盖)。
    private static final Map<ResourceLocation, BlockPos> PENDING_RESTORE_POS = new HashMap<>();
    private static final Map<ResourceLocation, Integer> PENDING_RESTORE_DELAY = new HashMap<>();
    private static final int RESTORE_DELAY_TICKS = 40; // 进入后等约 2s,确保竞技场区块已随玩家加载

    /**
     * 每 tick 推进结构地形重盖的待处理队列(见下方结构地形重置系统)。
     */
    private void onServerLevelTick(ServerTickEvent.Pre event) {
        processPendingStructureRestores(event.getServer());
    }

    /**
     * 玩家进入灾变维度:若该维度此前被占用过(可能被打坏)且当前无其他玩家在场,排入一次结构地形重盖
     * (让这个刚进来的玩家/后续玩家看到完好竞技场);随后把该维度标记为"脏"(本次占用可能又打坏)。
     * boss 再战交给基础模组的击杀-回刷怪器机制,这里只恢复地形。
     */
    private void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!CataclysmDimensionModConfig.RESET_STRUCTURE_ON_REENTRY) {
            return;
        }
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ResourceKey<Level> toKey = event.getTo();
        if (!CataclysmDimensions.LEVELS.contains(toKey)) {
            return;
        }
        ServerLevel level = player.serverLevel();
        if (level == null || !level.dimension().equals(toKey)) {
            return;
        }
        ResourceLocation loc = toKey.location();
        boolean hasOtherPlayers = level.players().stream().anyMatch(p -> p != player);
        if (DIRTY_DIMS.contains(loc) && !hasOtherPlayers) {
            // 这个玩家是"用过的竞技场"的第一个新访客 -> 延迟重盖(等区块随其加载好)。
            PENDING_RESTORE_POS.put(loc, player.blockPosition());
            PENDING_RESTORE_DELAY.put(loc, RESTORE_DELAY_TICKS);
            LOGGER.info("[Cataclysm Dimension]: {} re-entered (used before); scheduling structure restore at {}.", loc, player.blockPosition());
        }
        // "打赢才重置"开启时,标脏改由 onBossDeath 在主 boss 被击杀时做;否则沿用"进过就标脏"
        if (!CataclysmDimensionModConfig.RESET_STRUCTURE_REQUIRES_BOSS_KILL && DIRTY_DIMS.add(loc)) {
            saveDirtyDims(player.getServer()); // 跨重启持久化:记下"用过的维度"
        }
    }

    private void onServerStarting(ServerStartingEvent event) {
        // 静态状态跨存档存活,换档前清掉,免得旧档的重盖任务在新档误触发
        PENDING_RESTORE_POS.clear();
        PENDING_RESTORE_DELAY.clear();
        PURGE_STALE_RIFTS.clear();
        loadDirtyDims(event.getServer());
    }

    /**
     * 调试用:开关开启时,在关卡加载前清空各灾变维度的 region/entities/poi,使其重新按世界生成。
     * 放启动期做是因为运行期删这些文件会被 C2ME 打开的句柄挡下。
     */
    private void onServerAboutToStart(ServerAboutToStartEvent event) {
        if (!CataclysmDimensionModConfig.RESET_DIMENSION_IF_NO_PLAYER) {
            return;
        }
        MinecraftServer server = event.getServer();
        Path worldRoot = server.getWorldPath(LevelResource.ROOT);
        LOGGER.info("[Cataclysm Dimension]: map-repair enabled, wiping all cataclysm dimension chunks on startup.");
        for (ResourceKey<Level> levelKey : CataclysmDimensions.LEVELS) {
            ResourceLocation loc = levelKey.location();
            Path dimFolder = DimensionType.getStorageFolder(levelKey, worldRoot);
            try {
                deleteFile(dimFolder.resolve("region"), loc);
                deleteFile(dimFolder.resolve("entities"), loc);
                deleteFile(dimFolder.resolve("poi"), loc);
                LOGGER.info("[Cataclysm Dimension]: wiped chunks for {}.", loc);
            } catch (IOException e) {
                LOGGER.error("[Cataclysm Dimension]: failed to wipe chunks for {}.", loc, e);
            }
        }
        // 全量清档后脏维度记录已无意义,一并删掉。
        try {
            Files.deleteIfExists(dirtyDimsFile(server));
        } catch (IOException e) {
            LOGGER.warn("[Cataclysm Dimension]: failed to delete dirty-dims file after wipe.", e);
        }
    }

    private static Path dirtyDimsFile(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT).resolve("cataclysm_dimension_dirty_dims.txt");
    }

    /** 载入上次会话记录的"被用过的 boss 维度",使跨重启后首个访客仍触发结构复原。 */
    private static void loadDirtyDims(MinecraftServer server) {
        DIRTY_DIMS.clear();
        Path file = dirtyDimsFile(server);
        if (!Files.exists(file)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(file)) {
                String s = line.trim();
                if (s.isEmpty()) {
                    continue;
                }
                ResourceLocation rl = ResourceLocation.tryParse(s);
                if (rl != null) {
                    DIRTY_DIMS.add(rl);
                }
            }
        } catch (IOException e) {
            LOGGER.error("[Cataclysm Dimension]: failed to load dirty-dims file.", e);
        }
    }

    private static void saveDirtyDims(MinecraftServer server) {
        if (server == null) {
            return;
        }
        Path file = dirtyDimsFile(server);
        try {
            List<String> lines = new ArrayList<>();
            for (ResourceLocation rl : DIRTY_DIMS) {
                lines.add(rl.toString());
            }
            Files.write(file, lines);
        } catch (IOException e) {
            LOGGER.error("[Cataclysm Dimension]: failed to save dirty-dims file.", e);
        }
    }

    private void processPendingStructureRestores(MinecraftServer server) {
        if (PENDING_RESTORE_DELAY.isEmpty()) {
            return;
        }
        List<ResourceLocation> ready = new ArrayList<>();
        for (Map.Entry<ResourceLocation, Integer> e : PENDING_RESTORE_DELAY.entrySet()) {
            int left = e.getValue() - 1;
            if (left <= 0) {
                ready.add(e.getKey());
            } else {
                e.setValue(left);
            }
        }
        for (ResourceLocation loc : ready) {
            PENDING_RESTORE_DELAY.remove(loc);
            BlockPos around = PENDING_RESTORE_POS.remove(loc);
            ServerLevel level = server.getLevel(ResourceKey.create(Registries.DIMENSION, loc));
            if (level == null || around == null) {
                continue;
            }
            // 触发重盖的正是站在竞技场里的这个玩家,不能因附近有人而跳过;多人不打断由排入时的 !hasOtherPlayers 保证。
            try {
                restoreStructure(level, around);
            } catch (Exception ex) {
                LOGGER.error("[Cataclysm Dimension]: structure restore failed for {}.", loc, ex);
            }
        }
    }

    /**
     * 在 aroundPos 附近找到 boss 结构,把覆盖的每个已加载区块重盖回生成态(地形 + 刷怪器/boss),
     * 并先清掉竞技场里残留的怪。只改已加载区块,不碰区块 IO。
     */
    private void restoreStructure(ServerLevel level, BlockPos aroundPos) {
        // 顺带清掉上一局残留的归返裂隙(已载入的直接清,异步加载中的由 onEntityJoinLevel 拦)。
        PURGE_STALE_RIFTS.add(level.dimension().location());
        for (ReturnRiftEntity rift : level.getEntities(CDEntities.RETURN_RIFT.get(), r -> true)) {
            rift.discard();
        }
        StructureManager sm = level.structureManager();
        ChunkGenerator generator = level.getChunkSource().getGenerator();
        // 定位该维度的 boss 结构(专属维度里落点附近就是竞技场;取覆盖此位置的所有结构)。
        Set<Structure> structures = new HashSet<>(sm.getAllStructuresAt(aroundPos).keySet());
        if (structures.isEmpty()) {
            // 落点不在结构引用里时,扫描落点周边区块兜底。
            int cx0 = SectionPos.blockToSectionCoord(aroundPos.getX());
            int cz0 = SectionPos.blockToSectionCoord(aroundPos.getZ());
            for (int dx = -4; dx <= 4 && structures.isEmpty(); dx++) {
                for (int dz = -4; dz <= 4 && structures.isEmpty(); dz++) {
                    for (StructureStart s : sm.startsForStructure(new ChunkPos(cx0 + dx, cz0 + dz), st -> true)) {
                        if (s.isValid()) {
                            structures.add(s.getStructure());
                        }
                    }
                }
            }
        }
        if (structures.isEmpty()) {
            LOGGER.info("[Cataclysm Dimension]: no boss structure found near {} in {}, skip restore.", aroundPos, level.dimension().location());
            return;
        }
        RandomSource random = new WorldgenRandom(new LegacyRandomSource(level.getSeed()));
        boolean restoredAny = false;
        for (Structure structure : structures) {
            StructureStart start = sm.getStructureWithPieceAt(aroundPos, structure);
            if (start == null || !start.isValid()) {
                for (StructureStart s : sm.startsForStructure(new ChunkPos(aroundPos), st -> st == structure)) {
                    if (s.isValid()) {
                        start = s;
                        break;
                    }
                }
            }
            if (start == null || !start.isValid()) {
                continue;
            }
            BoundingBox bb = start.getBoundingBox();
            // 清掉竞技场范围内残留的怪(逃跑的旧 boss / 小怪 / 掉落物不动),避免重盖后 boss 重复。
            for (Mob mob : level.getEntitiesOfClass(Mob.class, AABB.of(bb))) {
                mob.discard();
            }
            int minCX = SectionPos.blockToSectionCoord(bb.minX());
            int maxCX = SectionPos.blockToSectionCoord(bb.maxX());
            int minCZ = SectionPos.blockToSectionCoord(bb.minZ());
            int maxCZ = SectionPos.blockToSectionCoord(bb.maxZ());
            for (int cx = minCX; cx <= maxCX; cx++) {
                for (int cz = minCZ; cz <= maxCZ; cz++) {
                    level.getChunk(cx, cz); // 确保该区块已加载(通常玩家进入时已加载;边缘同步补载)
                    ChunkPos cp = new ChunkPos(cx, cz);
                    int x0 = cp.getMinBlockX();
                    int z0 = cp.getMinBlockZ();
                    BoundingBox chunkBox = new BoundingBox(
                            Math.max(bb.minX(), x0), bb.minY(), Math.max(bb.minZ(), z0),
                            Math.min(bb.maxX(), x0 + 15), bb.maxY(), Math.min(bb.maxZ(), z0 + 15));
                    start.placeInChunk(level, sm, generator, random, chunkBox, cp);
                }
            }
            restoredAny = true;
            LOGGER.info("[Cataclysm Dimension]: restored structure {} in {}.", start.getStructure(), level.dimension().location());
        }
        // 打赢才重置模式:重盖成功即消耗标记,要再打赢才会再重盖;失败则保留、下次重试。
        if (restoredAny && CataclysmDimensionModConfig.RESET_STRUCTURE_REQUIRES_BOSS_KILL
                && DIRTY_DIMS.remove(level.dimension().location())) {
            saveDirtyDims(level.getServer());
        }
    }

    private void deleteFile(Path folder, ResourceLocation dimId) throws IOException {
        if (!Files.exists(folder)) {
            return;
        }
        Files.walkFileTree(folder, new SimpleFileVisitor<>() {
            @Override
            public @NotNull FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attrs) {
                try {
                    if (Files.deleteIfExists(file)) {
                        LOGGER.info("[Cataclysm Dimension]: {} cache Deleted -> {}", dimId, file);
                    }
                } catch (IOException e) {
                    // 文件可能仍被区块 IO(如 C2ME)占用而删不掉;跳过即可,留待下次清理。
                    LOGGER.warn("[Cataclysm Dimension]: {} could not delete {} (in use), skipping.", dimId, file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public @NotNull FileVisitResult visitFileFailed(@NotNull Path file, @NotNull IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void onToolTip(ItemTooltipEvent event) {
        if (!CataclysmDimensionModConfig.ENABLE_TELEPORT_EYE) {
            return;
        }
        if (TELEPORT_CONFIGS.isEmpty()) {
            initTeleportConfigs();
        }
        if (TELEPORT_CONFIGS.containsKey(event.getItemStack().getItem())) {
            event.getToolTip().add(Component.translatable("tip.cataclysm_dimension.enter").withStyle(ChatFormatting.GRAY));
        }
    }

    private void onDatapackLoad(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            addPacket(event, "base_dimension");
            addPacket(event, CataclysmDimensionModConfig.KEEP_STRUCTURES_IN_ORIGINAL_DIMENSIONS ? "keep_original" : "not_keep_original");
            if (CataclysmDimensionModConfig.RANDOM_SPREAD_IN_DIMENSION) {
                addPacket(event, CataclysmDimensionModConfig.KEEP_STRUCTURES_IN_ORIGINAL_DIMENSIONS ? "random_spread_dim" : "random_spread");
            }
            if (CataclysmDimensionModConfig.DISABLE_RESPAWN) {
                addPacket(event, "disable_respawn");
            }
        }
    }

    private void addPacket(AddPackFindersEvent event, String name) {
        event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(CataclysmDimensionMod.MOD_ID, "packs/" + name),
                PackType.SERVER_DATA,
                Component.literal(name),
                PackSource.WORLD,
                true,
                Pack.Position.TOP);
    }

}
