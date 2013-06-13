package fpgacraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import fpgacraft.block.BlockManager;
import fpgacraft.lib.Reference;
import fpgacraft.tileentity.SliceTileEntity;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.VERSION_NUMBER
)
public class FPGACraft {
    
    BlockManager blockManager = new BlockManager();
    
    @SidedProxy (
            clientSide = "fpgacraft.ClientProxy",
            serverSide = "fpgacraft.CommonProxy"
    )
    public static CommonProxy proxy;
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        // Initialize blocks and items
        
        blockManager.init();
        GameRegistry.registerTileEntity(SliceTileEntity.class, "fpgacraft.tileentity.SliceTileEntity");
        proxy.registerRenderers();
    }
    
    @Init
    public void init(FMLInitializationEvent event) {
        // Register event handlers
        // Register crafting recepies
        
        blockManager.registerRecepies();
        blockManager.registerNames();
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        // After ALL mods have been loaded
        // Do mod compatability stuff here
    }
}
