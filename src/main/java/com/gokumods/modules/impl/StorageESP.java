package com.gokumods.modules.impl;

import com.gokumods.modules.Module;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class StorageESP extends Module {
    public StorageESP() {
        super("Storage ESP", "Base");
        addSetting("showChests", true);
        addSetting("showHoppers", true);
        addSetting("showShulkers", true);
        addSetting("range", 9999);
    }

    @Override
    public void onRender(Minecraft mc, WorldRenderContext ctx) {
        if (mc.world == null || mc.player == null) return;
        MatrixStack matrices = ctx.matrixStack();
        VertexConsumer lines = ctx.consumers().getBuffer(RenderLayer.getLines());
        Vec3d cam = mc.gameRenderer.getCamera().getPos();

        boolean chests = (Boolean) settings.get("showChests").value;
        boolean hoppers = (Boolean) settings.get("showHoppers").value;
        boolean shulkers = (Boolean) settings.get("showShulkers").value;
        int range = ((Number) settings.get("range").value).intValue();

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();

        int cx = mc.player.getBlockX() >> 4, cz = mc.player.getBlockZ() >> 4;
        int r = Math.min(range / 16 + 1, 16);
        for (int dx = -r; dx <= r; dx++) for (int dz = -r; dz <= r; dz++) {
            var chunk = mc.world.getChunk(cx + dx, cz + dz);
            if (chunk == null) continue;
            for (var entry : chunk.getBlockEntities().entrySet()) {
                var be = entry.getValue();
                BlockPos pos = entry.getKey();
                if (mc.player.getPos().squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > (double) range * range) continue;
                int color = -1;
                var type = be.getType();
                if (chests && (type == BlockEntityType.CHEST || type == BlockEntityType.TRAPPED_CHEST || type == BlockEntityType.BARREL)) color = 0xFFFFAA00;
                if (hoppers && type == BlockEntityType.HOPPER) color = 0xFFFF5555;
                if (shulkers && type == BlockEntityType.SHULKER_BOX) color = 0xFFFF55FF;
                if (color == -1) continue;
                Box box = new Box(pos).offset(-cam.x, -cam.y, -cam.z).expand(0.01);
                drawBox(matrices, lines, box, color);
            }
        }

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private static void drawBox(MatrixStack m, VertexConsumer vc, Box b, int c) {
        float a = ((c >> 24) & 0xFF) / 255f;
        float r = ((c >> 16) & 0xFF) / 255f;
        float g = ((c >> 8) & 0xFF) / 255f;
        float bl = (c & 0xFF) / 255f;
        Matrix4f mat = m.peek().getPositionMatrix();
        float x1 = (float) b.minX, y1 = (float) b.minY, z1 = (float) b.minZ;
        float x2 = (float) b.maxX, y2 = (float) b.maxY, z2 = (float) b.maxZ;
        float[][] pts = {{x1,y1,z1,x2,y1,z1},{x2,y1,z1,x2,y1,z2},{x2,y1,z2,x1,y1,z2},{x1,y1,z2,x1,y1,z1},
                         {x1,y2,z1,x2,y2,z1},{x2,y2,z1,x2,y2,z2},{x2,y2,z2,x1,y2,z2},{x1,y2,z2,x1,y2,z1},
                         {x1,y1,z1,x1,y2,z1},{x2,y1,z1,x2,y2,z1},{x2,y1,z2,x2,y2,z2},{x1,y1,z2,x1,y2,z2}};
        for (float[] p : pts) {
            vc.vertex(mat, p[0], p[1], p[2]).color(r, g, bl, a).normal(0, 1, 0);
            vc.vertex(mat, p[3], p[4], p[5]).color(r, g, bl, a).normal(0, 1, 0);
        }
    }
                 }
