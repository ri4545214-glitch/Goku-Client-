package com.gokumods.gui;

import com.gokumods.GokuModsClient;
import com.gokumods.config.GokuConfig;
import com.gokumods.modules.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class GokuModsScreen extends Screen {
    private int panelX, panelY;
    private boolean dragging;
    private int dragOffX, dragOffY;
    private final int W = 240, ROW = 16, HEADER = 20;

    public GokuModsScreen() {
        super(Text.literal("Goku Mod's"));
        this.panelX = GokuConfig.data.guiX;
        this.panelY = GokuConfig.data.guiY;
    }

    private int totalHeight() {
        int h = HEADER;
        for (String s : new String[]{"Base", "Combat"}) {
            h += 12 + GokuModsClient.modules.getBySection(s).size() * ROW + 4;
        }
        return h + 5;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float d) {
        ctx.fill(panelX, panelY, panelX + W, panelY + totalHeight(), GokuConfig.data.guiColor);
        ctx.drawTextWithShadow(textRenderer, "Goku Mod's", panelX + 6, panelY + 6, 0xFFFFFF);

        int y = panelY + HEADER;
        for (String sec : new String[]{"Base", "Combat"}) {
            int col = sec.equals("Base") ? 0xFFAA00 : 0xFF5555;
            ctx.drawTextWithShadow(textRenderer, sec, panelX + 6, y, col);
            y += 12;
            for (Module m : GokuModsClient.modules.getBySection(sec)) {
                drawRow(ctx, m, panelX + 6, y);
                y += ROW;
            }
            y += 4;
        }
        super.render(ctx, mx, my, d);
    }

    private void drawRow(DrawContext ctx, Module m, int x, int y) {
        ctx.drawTextWithShadow(textRenderer, m.getName(), x, y, 0xFFFFFF);
        int swX = panelX + W - 30, swY = y + 1, swH = 10, swW = 20;
        ctx.fill(swX, swY, swX + swW, swY + swH, m.isEnabled() ? 0xFF00CC00 : 0xFF444444);
        int knobX = m.isEnabled() ? swX + swW - swH : swX;
        ctx.fill(knobX, swY, knobX + swH, swY + swH, 0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        if (mx >= panelX && mx <= panelX + W && my >= panelY && my <= panelY + HEADER && btn == 0) {
            dragging = true; dragOffX = (int) mx - panelX; dragOffY = (int) my - panelY;
            return true;
        }
        int y = panelY + HEADER;
        for (String sec : new String[]{"Base", "Combat"}) {
            y += 12;
            for (Module m : GokuModsClient.modules.getBySection(sec)) {
                if (mx >= panelX + 6 && mx <= panelX + W - 6 && my >= y && my <= y + ROW) {
                    if (btn == 0) { m.toggle(); return true; }
                    if (btn == 1) { client.setScreen(new ModuleSettingsScreen(m, this)); return true; }
                }
                y += ROW;
            }
            y += 4;
        }
        return super.mouseClicked(mx, my, btn);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
        if (dragging) {
            panelX = (int) mx - dragOffX; panelY = (int) my - dragOffY;
            GokuConfig.data.guiX = panelX; GokuConfig.data.guiY = panelY;
            return true;
        }
        return super.mouseDragged(mx, my, btn, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int btn) {
        if (dragging) { dragging = false; GokuConfig.save(); return true; }
        return super.mouseReleased(mx, my, btn);
    }
}
