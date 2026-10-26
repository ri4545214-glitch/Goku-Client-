package com.gokumods.modules;

import com.gokumods.config.GokuConfig;
import net.minecraft.client.Minecraft;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Module {
    private final String name;
    private final String section;
    private boolean enabled;
    public final Map<String, Setting<?>> settings = new LinkedHashMap<>();

    public Module(String name, String section) {
        this.name = name;
        this.section = section;
        this.enabled = GokuConfig.isEnabled(name);
    }

    public String getName() { return name; }
    public String getSection() { return section; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean v) { enabled = v; GokuConfig.setEnabled(name, v); }
    public void toggle() { setEnabled(!enabled); }

    public void onTick(Minecraft mc) {}
    public void onRender(Minecraft mc, net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext ctx) {}

    @SuppressWarnings("unchecked")
    protected <T> Setting<T> addSetting(String key, T def) {
        Setting<T> s = new Setting<>(key, def, this);
        settings.put(key, s);
        return s;
    }

    public static class Setting<T> {
        public final String key;
        public T value;
        private final Module owner;
        public Setting(String key, T def, Module owner) {
            this.key = key; this.value = def; this.owner = owner;
        }
        public void set(T v) {
            this.value = v;
            GokuConfig.setSetting(owner.getName(), key, v);
        }
    }
  }
