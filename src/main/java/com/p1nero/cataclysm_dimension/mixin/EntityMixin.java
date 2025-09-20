package com.p1nero.cataclysm_dimension.mixin;

import com.p1nero.cataclysm_dimension.CataclysmDimensionModConfig;
import com.p1nero.cataclysm_dimension.worldgen.CataclysmDimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    private Level level;

    @Inject(method = "getEncodeId", at = @At("HEAD"), cancellable = true)
    private void cataclysm_dimension$getEncodeId(CallbackInfoReturnable<String> cir) {
        if(CataclysmDimensionModConfig.RESET_DIMENSION_IF_NO_PLAYER) {
            if(CataclysmDimensions.LEVELS.contains(this.level.dimension())) {
                cir.setReturnValue(null);
            }
        }
    }
}
