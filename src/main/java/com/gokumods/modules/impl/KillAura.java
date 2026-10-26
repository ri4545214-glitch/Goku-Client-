package com.gokumods.modules.impl;

import com.gokumods.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class KillAura extends Module {
    private int tick = 0;

    public KillAura() {
        super("Kill Aura", "Combat");
        addSetting("range", 4);
        addSetting("delayTicks", 2);
        addSetting("targetPlayers", true);
        addSetting("targetMobs", true);
    }

    @Override
    public void onTick(Minecraft mc) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        int delay = ((Number) settings.get("delayTicks").value).intValue();
        if (++tick < Math.max(1, delay)) return;
        tick = 0;

        double range = ((Number) settings.get("range").value).intValue();
        boolean players = (Boolean) settings.get("targetPlayers").value;
        boolean mobs = (Boolean) settings.get("targetMobs").value;

        for (Entity e : mc.world.getEntities()) {
            if (e == mc.player || !(e instanceof LivingEntity)) continue;
            if (e instanceof PlayerEntity && !players) continue;
            if (e instanceof Monster && !mobs) continue;
            if (mc.player.squaredDistanceTo(e) <= range * range) {
                mc.interactionManager.attackEntity(mc.player, e);
            }
        }
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
