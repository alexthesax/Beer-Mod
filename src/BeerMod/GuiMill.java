package net.minecraft.src.BeerMod;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;

import org.lwjgl.opengl.GL11;

/**
 * GUI for Mill
 * @author alexthesax
 */
public class GuiMill extends GuiContainer
{
	/**
	 * The linked Mill TE
	 */
    public TileEntityMill tileentity;
	
    public GuiMill(InventoryPlayer inventoryplayer, TileEntityMill te)
    {
        super(new ContainerMill(inventoryplayer, te));		// TODO: Implement then link accordingly!
        tileentity = te;
    }

    /**
     * Draws Text-Strings in front of the normal GUI
     */
    @Override
    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Mill", 58, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }
    
    /**
     * Draws the entire GUI
     * Mainly C&P'd from GuiFurnace
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/BeerModSprites/GuiMill.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        if(tileentity.fuel>0)
        {
            int l = tileentity.gaugeFuelScaled(12);
            drawTexturedModalRect(j + 56, (k + 36 + 12) - l, 176, 12 - l, 14, l + 2);
        }
        int i1 = tileentity.gaugeProgressScaled(24);
        drawTexturedModalRect(j + 79, k + 34, 176, 14, i1 + 1, 16);
    }
}