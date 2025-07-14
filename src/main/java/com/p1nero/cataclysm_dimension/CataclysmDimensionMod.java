package com.p1nero.cataclysm_dimension;

import com.github.L_Ender.cataclysm.init.ModItems;
import com.p1nero.cataclysm_dimension.worldgen.CataclysmDimensions;
import com.p1nero.cataclysm_dimension.worldgen.placements.CDPlacementTypes;
import com.p1nero.cataclysm_dimension.worldgen.portal.CDNetherTeleporter;
import com.p1nero.cataclysm_dimension.worldgen.portal.CDTeleporter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

@Mod(CataclysmDimensionMod.MOD_ID)
public class CataclysmDimensionMod {
    public static final String MOD_ID = "cataclysm_dimension";

    public CataclysmDimensionMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.addListener(this::onItemUse);
        MinecraftForge.EVENT_BUS.addListener(this::onToolTip);
        CDPlacementTypes.STRUCTURE_PLACEMENT_TYPES.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CDConfig.SPEC);
    }

    private void onItemUse(LivingEntityUseItemEvent event){
        if(!CDConfig.ENABLE_TELEPORT_EYE.get()) {
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
                entity.changeDimension(level, new CDTeleporter(new BlockPos(0, 200, 0)));
            } else if(itemStack.is(ModItems.MECH_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_FORGE_OF_AEONS_LEVEL_KEY);
                entity.changeDimension(level, new CDTeleporter(new BlockPos(0, 150, 0)));
            } else if(itemStack.is(ModItems.FLAME_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_INFERNOS_MAW_LEVEL_KEY);
                entity.changeDimension(level, new CDNetherTeleporter(new BlockPos(0, 64, 0)));
            } else if(itemStack.is(ModItems.VOID_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_BASTION_LOST_LEVEL_KEY);
                entity.changeDimension(level, new CDTeleporter(new BlockPos(0, 150, 0)));
            } else if(itemStack.is(ModItems.MONSTROUS_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_SOULS_ANVIL_LEVEL_KEY);
                entity.changeDimension(level, new CDNetherTeleporter(new BlockPos(0, 64, 0)));
            } else if(itemStack.is(ModItems.DESERT_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_PHARAOHS_BANE_LEVEL_KEY);
                entity.changeDimension(level, new CDTeleporter(new BlockPos(0, 200, 0), 400));
            } else if(itemStack.is(ModItems.CURSED_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_ETERNAL_FROSTHOLD_LEVEL_KEY);
                entity.changeDimension(level, new CDTeleporter(new BlockPos(0, 200, 0), 400));
            } else if(itemStack.is(ModItems.STORM_EYE.get())) {
                ServerLevel level = minecraftServer.getLevel(CataclysmDimensions.CATACLYSM_SANCTUM_FALLEN_LEVEL_KEY);
                entity.changeDimension(level, new CDTeleporter(new BlockPos(0, 200, 0)));
            } else {
                flag = false;
            }
            if(flag) {
                if(entity instanceof ServerPlayer player) {
                    player.getCooldowns().addCooldown(itemStack.getItem(), 600);
                    player.connection.send(new ClientboundSoundPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.PORTAL_TRAVEL), SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 1.0F, 1.0F, player.getRandom().nextInt()));

                }
            }
        }
    }

    private void onToolTip(ItemTooltipEvent event) {
        if(!CDConfig.ENABLE_TELEPORT_EYE.get()) {
            return;
        }
        if(List.of(ModItems.ABYSS_EYE.get(), ModItems.STORM_EYE.get(), ModItems.CURSED_EYE.get(), ModItems.MECH_EYE.get(), ModItems.FLAME_EYE.get(), ModItems.DESERT_EYE.get(), ModItems.MONSTROUS_EYE.get(), ModItems.VOID_EYE.get()).contains(event.getItemStack().getItem())) {
            event.getToolTip().add(Component.translatable("tip.cataclysm_dimension.enter").withStyle(ChatFormatting.GRAY));
        }
    }

}
