package com.gokumods.gui;

import com.gokumods.modules.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ModuleSettingsScreen extends Screen {
    private final Module module;
    private final Screen parent;

    public ModuleSettingsScreen(Module m, Screen parent) {
        super(Text.literal(m.getName() + " Settings"));
        this.module = m; this.parent = parent;
    }

    @Override
    protected void init() {
        int y = 50;
        for (var e : module.settings.entrySet()) {
            var s = e.getValue();
            if (s.value instanceof Boolean b) {
                addDrawableChild(ButtonWidget.builder(
                    Text.literal(s.key + ": " + b),
                    btn -> { s.set(!(Boolean) s.value); btn.setMessage(Text.literal(s.key + ": " + s.value)); })
                    .dimensions(width / 2 - 100, y, 200, 20).build());
            } else if (s.value instanceof Number n) {
                final double min = 0, max = 10000;
                addDrawableChild(new SliderWidget(width / 2 - 100, y, 200, 20,
                    Text.literal(s.key + ": " + n.intValue()),
                    (n.doubleValue() - min) / (max - min)) {
                    @Override protected void updateMessage() {
                        setMessage(Text.literal(s.key + ": " + (int) (min + value * (max - min))));
                    }
                    @Override protected void applyValue() {
                        s.set((int) (min + value * (max - min)));
                    }
                });
            }
            y += 24;
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("Back"),
            b -> client.setScreen(parent)).dimensions(width / 2 - 60, height - 40, 120, 20).build());
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float d) {
        renderBackground(ctx, mx, my, d);
        ctx.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
        super.render(ctx, mx, my, d);
    }
}
