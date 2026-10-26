package com.gokumods.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GokuConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path path;
    public static Data data = new Data();

    public static class Data {
        public Map<String, Boolean> enabled = new HashMap<>();
        public Map<String, Map<String, Object>> settings = new HashMap<>();
        public int guiX = 50, guiY = 50;
        public int guiColor = 0xAA000000;
    }

    public static void load() {
        path = FabricLoader.getInstance().getConfigDir().resolve("gokumods.json");
        if (Files.exists(path)) {
            try { data = GSON.fromJson(Files.readString(path), Data.class); }
            catch (IOException e) { e.printStackTrace(); }
        }
    }

    public static void save() {
        try { Files.writeString(path, GSON.toJson(data)); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public static boolean isEnabled(String m) { return data.enabled.getOrDefault(m, false); }
    public static void setEnabled(String m, boolean v) { data.enabled.put(m, v); save(); }

    public static Object getSetting(String m, String k, Object def) {
        return data.settings.computeIfAbsent(m, x -> new HashMap<>()).getOrDefault(k, def);
    }
    public static void setSetting(String m, String k, Object v) {
        data.settings.computeIfAbsent(m, x -> new HashMap<>()).put(k, v); save();
    }
                }
