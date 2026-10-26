package com.gokumods;

import com.gokumods.config.GokuConfig;
import com.gokumods.gui.GokuModsScreen;
import com.gokumods.modules.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class GokuModsClient implements ClientModInitializer {
    public static final String MOD_ID = "gokumods";
    public static KeyMapping openGuiKey;
    public static ModuleManager modules;

    @Override
    public void onInitializeClient() {
        GokuConfig.load();
        modules = new ModuleManager();
        modules.init();

        KeyMapping.Category cat = KeyMapping.Category.register(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "main"));
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.gokumods.open_gui", GLFW.GLFW_KEY_RIGHT_ALT, cat));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.consumeClick()) {
                if (client.screen == null) client.setScreen(new GokuModsScreen());
            }
            if (client.player != null) modules.onTick(client);
        });
    }
}
