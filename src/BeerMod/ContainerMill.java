package net.minecraft.src.BeerMod;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.SlotFurnace;

/**
 * GUI-Container for the Mill
 * I admit, except for the constructor i've got no decent clue of what the container is actually doing.
 * I assume it's related to synching up values due to packets send via SMP.
 * @author alexthesax
 *
 */
public class ContainerMill extends Container
{

    public TileEntityMill tileentity;
    public int progress;
    public int fuel;
    public int maxFuel;
	
    public ContainerMill(InventoryPlayer inventoryplayer, TileEntityMill tileentitytileentity)
    {
        progress = 0;
        fuel = 0;
        maxFuel = 0;
        tileentity = tileentitytileentity;
        addSlot(new Slot(tileentitytileentity, 0, 56, 17));
        addSlot(new Slot(tileentitytileentity, 1, 56, 53));
        addSlot(new SlotFurnace(inventoryplayer.player, tileentitytileentity, 2, 116, 35));
        for(int i = 0; i < 3; i++)
        {
            for(int k = 0; k < 9; k++)
            {
                addSlot(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }

        }

        for(int j = 0; j < 9; j++)
        {
            addSlot(new Slot(inventoryplayer, j, 8 + j * 18, 142));
        }

    }

    @Override
    public void updateCraftingResults()
    {
        super.updateCraftingResults();
        for(int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting)crafters.get(i);
            if(progress != tileentity.progress)
            {
                icrafting.updateCraftingInventoryInfo(this, 0, tileentity.progress);
            }
            if(fuel != tileentity.fuel)
            {
                icrafting.updateCraftingInventoryInfo(this, 1, tileentity.fuel);
            }
            if(maxFuel != tileentity.maxFuel)
            {
                icrafting.updateCraftingInventoryInfo(this, 2, tileentity.maxFuel);
            }
        }

        progress = tileentity.progress;
        fuel = tileentity.fuel;
        maxFuel = tileentity.maxFuel;
    }
    
    @Override
    public void func_20112_a(int i, int j)
    {
        if(i == 0)
        {
            tileentity.progress = j;
        }
        if(i == 1)
        {
            tileentity.fuel = j;
        }
        if(i == 2)
        {
            tileentity.maxFuel = j;
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return tileentity.canInteractWith(entityplayer);
    }

	public int guiInventorySize()
	{
		return 3;
	}

	public int getInput()
	{
		return 0;
	}
}
