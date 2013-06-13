package fpgacraft;

import cpw.mods.fml.client.registry.ClientRegistry;
import fpgacraft.tileentity.render.SliceTileEntityRenderer;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(fpgacraft.tileentity.SliceTileEntity.class, new SliceTileEntityRenderer());
    }
}
