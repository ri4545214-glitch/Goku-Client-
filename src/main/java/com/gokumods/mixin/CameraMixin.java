package com.gokumods.mixin;

import com.gokumods.GokuModsClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow private net.minecraft.util.math.Vec3d pos;

    @Inject(method = "update", at = @At("TAIL"))
    private void gokumods$updateFreecam(BlockView area, Entity focusedEntity, boolean thirdPerson,
                                        boolean inverseView, float tickDelta, CallbackInfo ci) {
        var mod = GokuModsClient.modules.get("Freecam");
        if (mod == null || !mod.isEnabled()) return;
        // TODO: substitute pos with the freecam position here.
        // Full implementation requires tracking your own Vec3d offset controlled by WASD/Space/Shift.
    }
}
