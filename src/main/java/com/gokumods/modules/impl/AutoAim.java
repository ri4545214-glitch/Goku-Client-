package com.gokumods.modules.impl;

import com.gokumods.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class AutoAim extends Module {
    public AutoAim() {
        super("Auto Aim", "Combat");
        addSetting("range", 6);
        addSetting("targetPlayers", true);
        addSetting("targetMobs", true);
    }

    @Override
    public void onTick(Minecraft mc) {
        if (mc.player == null || mc.world == null) return;
        double range = ((Number) settings.get("range").value).intValue();
        boolean players = (Boolean) settings.get("targetPlayers").value;
        boolean mobs = (Boolean) settings.get("targetMobs").value;

        Entity best = null;
        double bestDist = range * range;
        for (Entity e : mc.world.getEntities()) {
            if (e == mc.player || !(e instanceof LivingEntity)) continue;
            if (e instanceof PlayerEntity && !players) continue;
            if (e instanceof Monster && !mobs) continue;
            double d = mc.player.squaredDistanceTo(e);
            if (d < bestDist) { bestDist = d; best = e; }
        }
        if (best == null) return;

        double dx = best.getX() - mc.player.getX();
        double dy = best.getEyeY() - mc.player.getEyeY();
        double dz = best.getZ() - mc.player.getZ();
        double horiz = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90);
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, horiz));
        mc.player.setYaw(yaw);
        mc.player.setPitch(MathHelper.clamp(pitch, -90, 90));
    }
          }
