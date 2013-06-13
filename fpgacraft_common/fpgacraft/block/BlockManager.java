package fpgacraft.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import fpgacraft.lib.BlockIds;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockManager {
    SliceBlock sliceBlock = new SliceBlock(BlockIds.SLICE_BLOCK, Material.circuits);
    
    public void init() {
        GameRegistry.registerBlock(sliceBlock, "fpga_slice");
    }
    
    public void registerRecepies() {
        GameRegistry.addShapelessRecipe(new ItemStack(sliceBlock, 64), new ItemStack(Block.dirt));
    }
    
    public void registerNames() {
        LanguageRegistry.addName(sliceBlock, "FPGA Slice");
    }
}
