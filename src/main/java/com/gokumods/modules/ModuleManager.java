package com.gokumods.modules;

import com.gokumods.modules.impl.*;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public final List<Module> modules = new ArrayList<>();

    public void init() {
        modules.add(new StorageESP());
        modules.add(new SusChunk());
        modules.add(new Freecam());
        modules.add(new PlayerESP());
        modules.add(new AutoAim());
        modules.add(new AutoAttack());
        modules.add(new KillAura());

        WorldRenderEvents.LAST.register(ctx -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null || mc.player == null) return;
            for (Module m : modules) if (m.isEnabled()) m.onRender(mc, ctx);
        });
    }

    public void onTick(Minecraft mc) {
        for (Module m : modules) if (m.isEnabled()) m.onTick(mc);
    }

    public Module get(String name) {
        return modules.stream().filter(m -> m.getName().equals(name)).findFirst().orElse(null);
    }

    public List<Module> getBySection(String section) {
        return modules.stream().filter(m -> m.getSection().equals(section)).toList();
    }
}
