package com.p1nero.cataclysm_dimension.worldgen.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class CDNetherTeleporter implements ITeleporter {
    @NotNull
    public BlockPos pos;
    public int slowFallTick = 200;

    public CDNetherTeleporter(@NotNull BlockPos pos) {
        this.pos = pos;
    }

    public CDNetherTeleporter(@NotNull BlockPos pos, int slowFallTick) {
        this.pos = pos;
        this.slowFallTick = slowFallTick;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destinationWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        return repositionEntity.apply(false);
    }

    /**
     * 在这里实现出生点。
     * @param entity The entity teleporting before the teleport
     * @param destinationLevel The world the entity is teleporting to
     * @param defaultPortalInfo A reference to the vanilla method for getting portal info. You should implement your own logic instead of using this
     *
     * @return 更新后的传送信息。
     */
    @Override
    public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destinationLevel, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        PortalInfo pos;
        while (!destinationLevel.getBlockState(this.pos).is(Blocks.AIR)){
            this.pos = this.pos.west();
        }
        if(entity instanceof ServerPlayer player){
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, slowFallTick, 1, false, true));
        }
        pos = new PortalInfo(this.pos.getCenter(), Vec3.ZERO, entity.getYRot(), entity.getXRot());
        return pos;
    }
}