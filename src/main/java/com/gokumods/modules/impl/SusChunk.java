package com.gokumods.modules.impl;

import com.gokumods.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class SusChunk extends Module {
    public SusChunk() {
        super("Sus Chunk", "Base");
        addSetting("radiusChunks", 8);
        addSetting("scanIntervalTicks", 100);
        addSetting("oreThreshold", 30);
    }

    private int tick = 0;

    @Override
    public void onTick(Minecraft mc) {
        if (mc.world == null || mc.player == null) return;
        int interval = ((Number) settings.get("scanIntervalTicks").value).intValue();
        if (++tick < Math.max(20, interval)) return;
        tick = 0;

        int radius = ((Number) settings.get("radiusChunks").value).intValue();
        int threshold = ((Number) settings.get("oreThreshold").value).intValue();
        int cx = mc.player.getBlockX() >> 4, cz = mc.player.getBlockZ() >> 4;

        for (int dx = -radius; dx <= radius; dx++) for (int dz = -radius; dz <= radius; dz++) {
            int oreCount = 0;
            BlockPos origin = new BlockPos((cx + dx) << 4, -60, (cz + dz) << 4);
            for (int x = 0; x < 16; x += 2) for (int y = -60; y < 20; y += 2) for (int z = 0; z < 16; z += 2) {
                var b = mc.world.getBlockState(origin.add(x, y + 60, z)).getBlock();
                if (b == Blocks.DIAMOND_ORE || b == Blocks.DEEPSLATE_DIAMOND_ORE
                    || b == Blocks.ANCIENT_DEBRIS || b == Blocks.DEEPSLATE_GOLD_ORE) oreCount++;
            }
            if (oreCount >= threshold) {
                // TODO: highlight chunk in render pass (add a set of BlockPos and draw in onRender)
            }
        }
    }
}
