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
    public static boolean RESET_DIMENSION_IF_NO_PLAYER = false;
    public static boolean SLOW_FALL_WHEN_ENTER_DIMENSIONS = true;
    public static boolean DISABLE_RESPAWN = false;

    // 配置键名常量
    private static final String ENABLE_TELEPORT_EYE_KEY = "enable_teleport_eye";
    private static final String KEEP_STRUCTURES_KEY = "keep_structures_in_original_dimensions";
    private static final String RANDOM_SPREAD_KEY = "random_spread_in_dimension";
    private static final String RESET_DIMENSION_KEY = "reset_dimension_if_no_player";
    private static final String SLOW_FALL_WHEN_ENTER_DIMENSIONS_KEY = "slow_fall_when_enter_dimensions";
    private static final String DISABLE_RESPAWN_KEY = "disable_respawn"; // 新增的键名常量

    public static final String JSON = CataclysmDimensionMod.MOD_ID + ".json";
    public static final Logger LOGGER = LoggerFactory.getLogger("cataclysm_dimension_config");

    // 默认配置映射 - 添加 DISABLE_RESPAWN
    private static final Map<String, Object> DEFAULT_CONFIG = Map.of(
            ENABLE_TELEPORT_EYE_KEY, ENABLE_TELEPORT_EYE,
            KEEP_STRUCTURES_KEY, KEEP_STRUCTURES_IN_ORIGINAL_DIMENSIONS,
            RANDOM_SPREAD_KEY, RANDOM_SPREAD_IN_DIMENSION,
            RESET_DIMENSION_KEY, RESET_DIMENSION_IF_NO_PLAYER,
            SLOW_FALL_WHEN_ENTER_DIMENSIONS_KEY, SLOW_FALL_WHEN_ENTER_DIMENSIONS,
            DISABLE_RESPAWN_KEY, DISABLE_RESPAWN // 新增默认配置项
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
    }
}