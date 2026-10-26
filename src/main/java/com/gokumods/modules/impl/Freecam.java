package com.gokumods.modules.impl;

import com.gokumods.modules.Module;
import net.minecraft.client.Minecraft;

public class Freecam extends Module {
    public Freecam() {
        super("Freecam", "Base");
        addSetting("speed", 2);
        addSetting("loadChunksStandingStill", true);
    }

    @Override
    public void onTick(Minecraft mc) {
        if (mc.player == null || mc.world == null) return;
        boolean auto = (Boolean) settings.get("loadChunksStandingStill").value;
        if (auto && mc.player.getVelocity().lengthSquared() < 0.001) {
            // TODO: extend with a CameraMixin to actually detach the camera entity
            // For chunk loading, keep the player's view center in sync:
            mc.world.getChunkSource().updateLoadDistance(8);
        }
    }
}
