package ru.nightidk.deathnote.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.nightidk.deathnote.DeathNote;
import ru.nightidk.deathnote.menu.InfinityCraftingScreenHandler;

public class InfinityTableScreen extends HandledScreen<InfinityCraftingScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(DeathNote.MOD_ID, "textures/gui/infinity_crafting_gui.png");

    public InfinityTableScreen(InfinityCraftingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 234;
        this.backgroundHeight = 278;
    }

    @Override
    protected void init() {
        super.init();
        titleX = 8;
        titleY = 6;
        playerInventoryTitleX = 39;
        playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context, delta, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
    }
}
