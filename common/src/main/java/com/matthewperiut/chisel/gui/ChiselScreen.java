package com.matthewperiut.chisel.gui;

import com.matthewperiut.chisel.Chisel;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ChiselScreen extends HandledScreen<ScreenHandler> {
    public static final Identifier TEXTURE = new Identifier(Chisel.MOD_ID, "textures/chiselfabricgui.png");
    public static final int TEXTURE_WIDTH = 194;
    public static final int TEXTURE_HEIGHT = 209;

    public ChiselScreen(ScreenHandler handler, PlayerInventory inventory, Text title)
    {
        super(handler, inventory, title);
        this.titleY = 5;
        this.backgroundWidth =  TEXTURE_WIDTH;
        this.backgroundHeight = TEXTURE_HEIGHT;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY)
    {
        this.textRenderer.draw(matrices, this.title, this.titleX, this.titleY, 4210752);
    }
}