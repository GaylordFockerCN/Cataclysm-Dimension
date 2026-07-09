package com.p1nero.cataclysm_dimension;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CataclysmDimensionModConfig {
    // 配置项默认值
    public static boolean ENABLE_TELEPORT_EYE = true;
    public static boolean KEEP_STRUCTURES_IN_ORIGINAL_DIMENSIONS = false;
    public static boolean RANDOM_SPREAD_IN_DIMENSION = false;
    // 调试开关:开启后每次存档启动时清空全部灾变维度区块,按世界生成重铺一份完好地图(修坏图用)。默认关。
    public static boolean RESET_DIMENSION_IF_NO_PLAYER = false;
    public static boolean SLOW_FALL_WHEN_ENTER_DIMENSIONS = true;
    public static boolean DISABLE_RESPAWN = false;
    // 传送进入维度时是否消耗对应的眼睛物品(创造模式不消耗)
    public static boolean CONSUME_TELEPORT_EYE = false;
    // 身处灾变维度内时是否仍可使用眼睛(默认否:维度内不可用)
    public static boolean ALLOW_USE_EYE_IN_DIMENSION = false;
    // 玩家再次进入用过的 boss 维度时,把该 boss 结构地形重盖成完好(让下一个玩家看到干净竞技场;
    // boss 再战由基础模组的击杀-回刷怪器负责,此项只管地形)。
    public static boolean RESET_STRUCTURE_ON_REENTRY = true;
    // 大 boss 被击杀后生成归返裂隙(右键离开维度)。默认关。
    public static boolean ENABLE_RETURN_RIFT = false;
    // 开启后只有主 boss 被击杀过、重进才重盖结构(重盖成功即消耗标记,要再打赢才再重盖)。默认关=每次重进都重盖。
    public static boolean RESET_STRUCTURE_REQUIRES_BOSS_KILL = false;

    // 配置键名常量
    private static final String ENABLE_TELEPORT_EYE_KEY = "enable_teleport_eye";
    private static final String KEEP_STRUCTURES_KEY = "keep_structures_in_original_dimensions";
    private static final String RANDOM_SPREAD_KEY = "random_spread_in_dimension";
    private static final String RESET_DIMENSION_KEY = "reset_dimension_if_no_player";
    private static final String SLOW_FALL_WHEN_ENTER_DIMENSIONS_KEY = "slow_fall_when_enter_dimensions";
    private static final String DISABLE_RESPAWN_KEY = "disable_respawn"; // 新增的键名常量
    private static final String CONSUME_TELEPORT_EYE_KEY = "consume_teleport_eye";
    private static final String ALLOW_USE_EYE_IN_DIMENSION_KEY = "allow_use_eye_in_dimension";
    private static final String RESET_STRUCTURE_ON_REENTRY_KEY = "reset_structure_on_reentry";
    private static final String ENABLE_RETURN_RIFT_KEY = "enable_return_rift";
    private static final String RESET_STRUCTURE_REQUIRES_BOSS_KILL_KEY = "reset_structure_requires_boss_kill";

    public static final String JSON = CataclysmDimensionMod.MOD_ID + ".json";
    public static final Logger LOGGER = LoggerFactory.getLogger("cataclysm_dimension_config");

    // 默认配置映射
    private static final Map<String, Object> DEFAULT_CONFIG = Map.ofEntries(
            Map.entry(ENABLE_TELEPORT_EYE_KEY, ENABLE_TELEPORT_EYE),
            Map.entry(KEEP_STRUCTURES_KEY, KEEP_STRUCTURES_IN_ORIGINAL_DIMENSIONS),
            Map.entry(RANDOM_SPREAD_KEY, RANDOM_SPREAD_IN_DIMENSION),
            Map.entry(RESET_DIMENSION_KEY, RESET_DIMENSION_IF_NO_PLAYER),
            Map.entry(SLOW_FALL_WHEN_ENTER_DIMENSIONS_KEY, SLOW_FALL_WHEN_ENTER_DIMENSIONS),
            Map.entry(DISABLE_RESPAWN_KEY, DISABLE_RESPAWN),
            Map.entry(CONSUME_TELEPORT_EYE_KEY, CONSUME_TELEPORT_EYE),
            Map.entry(ALLOW_USE_EYE_IN_DIMENSION_KEY, ALLOW_USE_EYE_IN_DIMENSION),
            Map.entry(RESET_STRUCTURE_ON_REENTRY_KEY, RESET_STRUCTURE_ON_REENTRY),
            Map.entry(ENABLE_RETURN_RIFT_KEY, ENABLE_RETURN_RIFT),
            Map.entry(RESET_STRUCTURE_REQUIRES_BOSS_KILL_KEY, RESET_STRUCTURE_REQUIRES_BOSS_KILL)
    );

    public static void loadConfig() {
        File configFolder = new File("config", CataclysmDimensionMod.MOD_ID);
        File configFile = new File(configFolder, JSON);

        // 确保配置目录存在
        if (!configFolder.exists() && !configFolder.mkdirs()) {
            LOGGER.error("Failed to create config folder: {}", configFolder.getAbsolutePath());
            return;
        }

        // 如果配置文件不存在，创建默认配置
        if (!configFile.exists()) {
            generateConfig(configFile);
            return;
        }

        // 加载现有配置
        try (FileReader reader = new FileReader(configFile)) {
            JsonObject config = new Gson().fromJson(reader, JsonObject.class);
            boolean needsUpdate = false;

            // 检查并添加缺失的配置项
            for (Map.Entry<String, Object> entry : DEFAULT_CONFIG.entrySet()) {
                String key = entry.getKey();
                if (!config.has(key)) {
                    LOGGER.info("Adding missing config key: {}", key);
                    if (entry.getValue() instanceof Boolean) {
                        config.addProperty(key, (Boolean) entry.getValue());
                    } else if (entry.getValue() instanceof Number) {
                        config.addProperty(key, (Number) entry.getValue());
                    }
                    needsUpdate = true;
                }
            }

            // 如果有缺失的配置项，更新配置文件
            if (needsUpdate) {
                try (FileWriter writer = new FileWriter(configFile)) {
                    writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
                }
            }

            // 读取配置值 - 添加 DISABLE_RESPAWN 的读取
            ENABLE_TELEPORT_EYE = config.get(ENABLE_TELEPORT_EYE_KEY).getAsBoolean();
            RANDOM_SPREAD_IN_DIMENSION = config.get(RANDOM_SPREAD_KEY).getAsBoolean();
            KEEP_STRUCTURES_IN_ORIGINAL_DIMENSIONS = config.get(KEEP_STRUCTURES_KEY).getAsBoolean();
            RESET_DIMENSION_IF_NO_PLAYER = config.get(RESET_DIMENSION_KEY).getAsBoolean();
            SLOW_FALL_WHEN_ENTER_DIMENSIONS = config.get(SLOW_FALL_WHEN_ENTER_DIMENSIONS_KEY).getAsBoolean();
            DISABLE_RESPAWN = config.get(DISABLE_RESPAWN_KEY).getAsBoolean(); // 新增配置项读取
            CONSUME_TELEPORT_EYE = config.get(CONSUME_TELEPORT_EYE_KEY).getAsBoolean();
            ALLOW_USE_EYE_IN_DIMENSION = config.get(ALLOW_USE_EYE_IN_DIMENSION_KEY).getAsBoolean();
            RESET_STRUCTURE_ON_REENTRY = config.get(RESET_STRUCTURE_ON_REENTRY_KEY).getAsBoolean();
            ENABLE_RETURN_RIFT = config.get(ENABLE_RETURN_RIFT_KEY).getAsBoolean();
            RESET_STRUCTURE_REQUIRES_BOSS_KILL = config.get(RESET_STRUCTURE_REQUIRES_BOSS_KILL_KEY).getAsBoolean();

        } catch (IOException e) {
            LOGGER.error("Failed to load configuration file: {}", e.getMessage());
            // 加载失败时使用默认值
            useDefaultValues();
        } catch (Exception e) {
            LOGGER.error("Error parsing configuration file: {}", e.getMessage());
            // 解析错误时重新生成配置文件
            generateConfig(configFile);
            useDefaultValues();
        }
    }

    private static void generateConfig(File configFile) {
        try {
            if (configFile.createNewFile()) {
                LOGGER.info("Generating configuration file: {}", configFile.getAbsolutePath());

                JsonObject config = new JsonObject();
                for (Map.Entry<String, Object> entry : DEFAULT_CONFIG.entrySet()) {
                    if (entry.getValue() instanceof Boolean) {
                        config.addProperty(entry.getKey(), (Boolean) entry.getValue());
                    } else if (entry.getValue() instanceof Number) {
                        config.addProperty(entry.getKey(), (Number) entry.getValue());
                    }
                }

                try (FileWriter writer = new FileWriter(configFile)) {
                    writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
                }
            } else {
                LOGGER.error("Failed to create configuration file: {}", configFile.getAbsolutePath());
            }
        } catch (IOException e) {
            LOGGER.error("Error generating configuration file: {}", e.getMessage());
        }
    }

    private static void useDefaultValues() {
        ENABLE_TELEPORT_EYE = (Boolean) DEFAULT_CONFIG.get(ENABLE_TELEPORT_EYE_KEY);
        RANDOM_SPREAD_IN_DIMENSION = (Boolean) DEFAULT_CONFIG.get(RANDOM_SPREAD_KEY);
        KEEP_STRUCTURES_IN_ORIGINAL_DIMENSIONS = (Boolean) DEFAULT_CONFIG.get(KEEP_STRUCTURES_KEY);
        RESET_DIMENSION_IF_NO_PLAYER = (Boolean) DEFAULT_CONFIG.get(RESET_DIMENSION_KEY);
        SLOW_FALL_WHEN_ENTER_DIMENSIONS = (Boolean) DEFAULT_CONFIG.get(SLOW_FALL_WHEN_ENTER_DIMENSIONS_KEY);
        DISABLE_RESPAWN = (Boolean) DEFAULT_CONFIG.get(DISABLE_RESPAWN_KEY);
        CONSUME_TELEPORT_EYE = (Boolean) DEFAULT_CONFIG.get(CONSUME_TELEPORT_EYE_KEY);
        ALLOW_USE_EYE_IN_DIMENSION = (Boolean) DEFAULT_CONFIG.get(ALLOW_USE_EYE_IN_DIMENSION_KEY);
        RESET_STRUCTURE_ON_REENTRY = (Boolean) DEFAULT_CONFIG.get(RESET_STRUCTURE_ON_REENTRY_KEY);
        ENABLE_RETURN_RIFT = (Boolean) DEFAULT_CONFIG.get(ENABLE_RETURN_RIFT_KEY);
        RESET_STRUCTURE_REQUIRES_BOSS_KILL = (Boolean) DEFAULT_CONFIG.get(RESET_STRUCTURE_REQUIRES_BOSS_KILL_KEY);
    }
}