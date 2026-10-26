package com.gokumods.modules.impl;

import com.gokumods.modules.Module;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class PlayerESP extends Module {
    public PlayerESP() {
        super("Player ESP", "Combat");
        addSetting("range", 128);
        addSetting("color", 0xFF00FFFF);
    }

    @Override
    public void onRender(Minecraft mc, WorldRenderContext ctx) {
        if (mc.world == null || mc.player == null) return;
        int range = ((Number) settings.get("range").value).intValue();
        Vec3d cam = mc.gameRenderer.getCamera().getPos();
        var lines = ctx.consumers().getBuffer(RenderLayer.getLines());
        var mat = ctx.matrixStack().peek().getPositionMatrix();

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player || p.isInvisible()) continue;
            if (mc.player.distanceTo(p) > range) continue;
            Box box = p.getBoundingBox().offset(-cam.x, -cam.y, -cam.z).expand(0.05);
            float x1 = (float) box.minX, y1 = (float) box.minY, z1 = (float) box.minZ;
            float x2 = (float) box.maxX, y2 = (float) box.maxY, z2 = (float) box.maxZ;
            float[][] segs = {{x1,y1,z1,x2,y1,z1},{x2,y1,z1,x2,y1,z2},{x2,y1,z2,x1,y1,z2},{x1,y1,z2,x1,y1,z1},
                             {x1,y2,z1,x2,y2,z1},{x2,y2,z1,x2,y2,z2},{x2,y2,z2,x1,y2,z2},{x1,y2,z2,x1,y2,z1},
                             {x1,y1,z1,x1,y2,z1},{x2,y1,z1,x2,y2,z1},{x2,y1,z2,x2,y2,z2},{x1,y1,z2,x1,y2,z2}};
            for (float[] s : segs) {
                lines.vertex(mat, s[0], s[1], s[2]).color(0f, 1f, 1f, 1f).normal(0, 1, 0);
                lines.vertex(mat, s[3], s[4], s[5]).color(0f, 1f, 1f, 1f).normal(0, 1, 0);
            }
        }
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}
