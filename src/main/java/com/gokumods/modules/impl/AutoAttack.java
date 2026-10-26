package com.gokumods.modules.impl;

import com.gokumods.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class AutoAttack extends Module {
    private int cooldown = 0;

    public AutoAttack() {
        super("Auto Attack", "Combat");
        addSetting("range", 3);
        addSetting("cps", 8);
        addSetting("targetPlayers", true);
        addSetting("targetMobs", true);
    }

    @Override
    public void onTick(Minecraft mc) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        int cps = ((Number) settings.get("cps").value).intValue();
        if (++cooldown < 20 / Math.max(1, cps)) return;
        cooldown = 0;

        double range = ((Number) settings.get("range").value).intValue();
        boolean players = (Boolean) settings.get("targetPlayers").value;
        boolean mobs = (Boolean) settings.get("targetMobs").value;

        Entity best = null; double bestDist = range * range;
        for (Entity e : mc.world.getEntities()) {
            if (e == mc.player || !(e instanceof LivingEntity)) continue;
            if (e instanceof PlayerEntity && !players) continue;
            if (e instanceof Monster && !mobs) continue;
            double d = mc.player.squaredDistanceTo(e);
            if (d < bestDist) { bestDist = d; best = e; }
        }
        if (best != null) {
            mc.interactionManager.attackEntity(mc.player, best);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
