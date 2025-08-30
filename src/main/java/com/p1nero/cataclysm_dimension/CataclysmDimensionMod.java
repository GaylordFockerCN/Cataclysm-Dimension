package com.p1nero.cataclysm_dimension;

import com.github.L_Ender.cataclysm.init.ModItems;
import com.mojang.logging.LogUtils;
import com.p1nero.cataclysm_dimension.worldgen.CataclysmDimensions;
import com.p1nero.cataclysm_dimension.worldgen.placements.CDPlacementTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.slf4j.Logger;

import java.util.List;

@Mod(CataclysmDimensionMod.MOD_ID)
public class CataclysmDimensionMod {
    public static final String MOD_ID = "cataclysm_dimension";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CataclysmDimensionMod(ModContainer modContainer, IEventBus bus) {
        NeoForge.EVENT_BUS.addListener(this::onItemUse);
        NeoForge.EVENT_BUS.addListener(this::onToolTip);
        CataclysmDimensionModConfig.loadConfig();
        bus.addListener(this::onDatapackLoad);
        CDPlacementTypes.STRUCTURE_PLACEMENT_TYPES.register(bus);
    }

    private void onItemUse(LivingEntityUseItemEvent event){
        if(!CataclysmDimensionModConfig.ENABLE_TELEPORT_EYE) {
            return;
        }
        LivingEntity entity = event.getEntity();
        if(entity.level().isClientSide) {
            return;
        }
        if(entity.isShiftKeyDown()) {
            ItemStack itemStack = event.getItem();
            MinecraftServer minecraftServer = entity.level().getServer();
            if(minecraftServer == null) {
                return;
            }
            if(entity instanceof Player player && player.getCooldowns().isOnCooldown(itemStack.getItem())) {
                return;
            }
            boolean flag = true;
            if(itemStack.is(ModItems.ABYSS_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_ABYSSAL_DEPTHS_LEVEL_KEY);
                entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 1, false, true));
                entity.changeDimension(new DimensionTransition(level, new Vec3(0, 150, 0), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND));
            } else if(itemStack.is(ModItems.MECH_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_FORGE_OF_AEONS_LEVEL_KEY);
                entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 1, false, true));
                entity.changeDimension(new DimensionTransition(level, new Vec3(0, 150, 0), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND));
            } else if(itemStack.is(ModItems.FLAME_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_INFERNOS_MAW_LEVEL_KEY);
                entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 1, false, true));
                entity.changeDimension(new DimensionTransition(level, new Vec3(0, 64, 0), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND));
            } else if(itemStack.is(ModItems.VOID_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_BASTION_LOST_LEVEL_KEY);
                entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 1, false, true));
                entity.changeDimension(new DimensionTransition(level, new Vec3(0, 150, 0), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND));
            } else if(itemStack.is(ModItems.MONSTROUS_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_SOULS_ANVIL_LEVEL_KEY);
                entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 1, false, true));
                entity.changeDimension(new DimensionTransition(level, new Vec3(0, 64, 0), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND));
            } else if(itemStack.is(ModItems.DESERT_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_PHARAOHS_BANE_LEVEL_KEY);
                entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 400, 1, false, true));
                entity.changeDimension(new DimensionTransition(level, new Vec3(0, 200, 0), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND));
            } else if(itemStack.is(ModItems.CURSED_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_ETERNAL_FROSTHOLD_LEVEL_KEY);
                entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 400, 1, false, true));
                entity.changeDimension(new DimensionTransition(level, new Vec3(0, 200, 0), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND));
            } else if(itemStack.is(ModItems.STORM_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_SANCTUM_FALLEN_LEVEL_KEY);
                entity.changeDimension(new DimensionTransition(level, new Vec3(0, 200, 0), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND));
            } else {
                flag = false;
            }
            if(flag) {
                if(entity instanceof Player player) {
                    player.getCooldowns().addCooldown(itemStack.getItem(), 600);
                }
            }
        }
    }

    private void onToolTip(ItemTooltipEvent event) {
        if(!CataclysmDimensionModConfig.ENABLE_TELEPORT_EYE) {
            return;
        }
        if(List.of(ModItems.ABYSS_EYE.get(), ModItems.STORM_EYE.get(), ModItems.CURSED_EYE.get(), ModItems.MECH_EYE.get(), ModItems.FLAME_EYE.get(), ModItems.DESERT_EYE.get(), ModItems.MONSTROUS_EYE.get(), ModItems.VOID_EYE.get()).contains(event.getItemStack().getItem())) {
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
