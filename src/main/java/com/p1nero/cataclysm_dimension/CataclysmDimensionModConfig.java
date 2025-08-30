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

public class CataclysmDimensionModConfig {
    public static boolean ENABLE_TELEPORT_EYE = true;
    public static boolean KEEP_STRUCTURES_IN_ORIGINAL_DIMENSIONS = false;
    public static final String JSON = CataclysmDimensionMod.MOD_ID + ".json";

    public static final Logger LOGGER = LoggerFactory.getLogger("better_structure_block");
    public static void loadConfig() {
        File configFolder = new File("config" + File.separator + CataclysmDimensionMod.MOD_ID);
        if (!configFolder.exists()) {
            if(!configFolder.mkdirs()){
                LOGGER.info("Failed to create config folder.");
                return;
            }
        }

        File configFile = new File(configFolder, JSON);
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                LOGGER.info("Loading configuration file...");
                JsonObject config = new Gson().fromJson(reader, JsonObject.class);
                ENABLE_TELEPORT_EYE = config.get("enable_teleport_eye").getAsBoolean();
                KEEP_STRUCTURES_IN_ORIGINAL_DIMENSIONS = config.get("keep_structures_in_original_dimensions").getAsBoolean();
            } catch (IOException e) {
                LOGGER.error("Failed to load configuration file!{}", String.valueOf(e));
            }
        } else {
            try {
                LOGGER.info("Generating configuration file...");
                if(configFile.createNewFile()){
                    JsonObject config = new JsonObject();
                    config.addProperty("enable_teleport_eye", ENABLE_TELEPORT_EYE);
                    config.addProperty("keep_structures_in_original_dimensions", KEEP_STRUCTURES_IN_ORIGINAL_DIMENSIONS);
                    try (FileWriter writer = new FileWriter(configFile)) {
                        writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
                    }
                } else {
                    LOGGER.info("Error generating configuration file!");
                }
            } catch (IOException e) {
                LOGGER.info("Error generating configuration file!{}", String.valueOf(e));
            }
        }
    }
}