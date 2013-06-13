package fpgacraft.tileentity.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

import fpgacraft.lib.Texture;
import fpgacraft.tileentity.SliceTileEntity;
import fpgacraft.tileentity.model.SliceModel;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public class SliceTileEntityRenderer extends TileEntitySpecialRenderer {
    
    SliceModel sliceModel = new SliceModel();

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
        if (!(tileentity instanceof SliceTileEntity)) {
            return;
        }
        
        SliceTileEntity sliceTileEntity = (SliceTileEntity)tileentity;
        
        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);
            renderSlice(sliceTileEntity);
        }
        GL11.glPopMatrix();
    }
    
    private void renderSlice(SliceTileEntity slice) {
        float brightness = slice.blockType.getBlockBrightness(slice.worldObj, slice.xCoord, slice.yCoord, slice.zCoord);
        int light = slice.worldObj.getLightBrightnessForSkyBlocks(slice.xCoord, slice.yCoord, slice.zCoord, 0);
        Tessellator.instance.setColorOpaque_F(brightness, brightness, brightness);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, light % 65536, light / 65536);
        
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(Texture.SLICE);
        int metadata = slice.getBlockMetadata();
        
        GL11.glPushMatrix();
        {
            if (metadata >= 4 && metadata < 8) {
                GL11.glTranslated(0.5, 0.0, 0.5);
                GL11.glRotatef(180, 0f, 0f, 1f);
                GL11.glTranslated(-0.5, -1.0, -0.5);
            }
            
            if (metadata < 8) {
                GL11.glTranslated(0.5, 0.0, 0.5);
            
                if (metadata == 1 || metadata == 6) {
                    GL11.glRotatef(270f, 0f, 1f, 0f);
                }
                else if (metadata == 2 || metadata == 5) {
                    GL11.glRotatef(180f, 0f, 1f, 0f);
                }
                else if (metadata == 3 || metadata == 4) {
                    GL11.glRotatef(90f, 0f, 1f, 0f);
                }
            
                GL11.glTranslated(-0.5, 0.0, -0.5);
            }
            else if (metadata == 8) { // -Z
                GL11.glRotatef(90, 1f, 0f, 0f);
                GL11.glTranslatef(0f, 0f, -1f);
                
                GL11.glRotatef(180f, 0f, 1f, 0f);
                GL11.glTranslatef(-1f, 0f, -1f);
            }
            else if (metadata == 9) { // +Z
                GL11.glTranslatef(0f, 0f, 1f);
                GL11.glRotatef(-90, 1f, 0f, 0f);
            }
            else if (metadata == 10) { // -X
                GL11.glTranslatef(0f, 1f, 0f);
                GL11.glRotatef(-90, 0f, 0f, 1f);
                
                GL11.glRotatef(-90, 0f, 1f, 0f);
                GL11.glTranslatef(0f, 0f, -1f);
            }
            else if (metadata == 11) { // +X
                GL11.glTranslatef(1f, 0f, 0f);
                GL11.glRotatef(90, 0f, 0f, 1f);
                
                GL11.glRotatef(90, 0f, 1f, 0f);
                GL11.glTranslatef(-1f, 0f, 0f);
            }
            
            sliceModel.render((Entity)null, 0f, 0f, -0.1f, 0f, 0f, 0.0625f);
        }
        GL11.glPopMatrix();
    }

}
