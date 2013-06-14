package fpgacraft.block;

import fpgacraft.core.Port;
import fpgacraft.tileentity.SliceTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/*
 * The metadata for this block determines orientation. Because all possible orientations would be
 * 24 states, and we can only store 16, the slice can only be rotated when attached to the ground
 * or ceiling. The metadata values are then as follows:
 * 
 * 0-3: Ground
 * 4-7: Ceiling
 * 8: North
 * 9: South
 * 10: West
 * 11: East
 * 
 * If the value is set to 12-15, I detonate the thermonuclear device.
 */
public class SliceBlock extends Block {

    public SliceBlock(int par1, Material par2Material) {
        super(par1, par2Material);
    }
    
    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new SliceTileEntity();
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbor_id) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        
        if (tileEntity == null || !(tileEntity instanceof SliceTileEntity)) {
            return;
        }
        
        SliceTileEntity sliceTileEntity = (SliceTileEntity)tileEntity;
        int metadata = world.getBlockMetadata(x, y, z);
        
        sliceTileEntity.updateConnections();
        
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
            ForgeDirection direction = ForgeDirection.VALID_DIRECTIONS[i];
            int power = world.getIndirectPowerLevelTo(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, ForgeDirection.OPPOSITES[i]);

            int sliceSide = directionMap[i][metadata];
            
            if (sliceSide >= 0) {
                Port port = sliceTileEntity.slice.sides[sliceSide];

                if (port.sending) {
                    System.out.println("Setting port " + sliceSide + " state: " + power);
                    port.state = (power > 0);
                }
            }
        }
    }
    
    /*
     * (x, y, z) is the coordinate of the block that the new block was placed on. Side is the side
     * of the block that was clicked. (hitX, hitY, hitZ) is the coordinate that was clicked.
     */
    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        int opposite = ForgeDirection.OPPOSITES[side];
        
        if (opposite == 1) {
            metadata += 4;
        }
        else if (opposite > 1) {
            metadata = opposite + 6;
        }
        
        return metadata;
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving player, ItemStack item) {
        int metadata = world.getBlockMetadata(x, y, z);
        
        if (metadata < 8) {
            int rot = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            world.setBlockMetadataWithNotify(x, y, z, metadata + rot, 2);
        }
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int i, int j, int k, int l) {
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        
        switch (metadata) {
            case 0:
            case 1:
            case 2:
            case 3:
                this.setBlockBounds(0.0625f, 0f, 0.0625f, 0.9375f, 0.125f, 0.9375f); break;
            
            case 4:
            case 5:
            case 6:
            case 7:
                this.setBlockBounds(0.0625f, 0.875f, 0.0625f, 0.9375f, 1f, 0.9375f); break;
            
            case 8:
                this.setBlockBounds(0.0625f, 0.0625f, 0f, 0.9375f, 0.9375f, 0.125f); break;
            
            case 9:
                this.setBlockBounds(0.0625f, 0.0625f, 0.875f, 0.9375f, 0.9375f, 1f); break;
            
            case 10:
                this.setBlockBounds(0f, 0.0625f, 0.0625f, 0.125f, 0.9375f, 0.9375f); break;
             
            case 11:
                this.setBlockBounds(0.875f, 0.0625f, 0.0625f, 1f, 0.9375f, 0.9375f); break;
        }
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }
    
    @Override
    public boolean canProvidePower()
    {
        return true;
    }
    
    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return 0;
    }

    @Override 
    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntity tileEntity = blockAccess.getBlockTileEntity(x, y, z);
        
        if (tileEntity == null || !(tileEntity instanceof SliceTileEntity)) {
            return 0;
        }
        
        SliceTileEntity sliceTileEntity = (SliceTileEntity)tileEntity;
        int metadata = blockAccess.getBlockMetadata(x, y, z);
        
        int sliceSide = directionMap[side][metadata];
        
        if (sliceSide >= 0) {
            Port p = sliceTileEntity.slice.sides[sliceSide];
        
            if (p.canReceive && !p.sending && p.connection != null) {
                return sliceTileEntity.slice.sides[sliceSide].state ? 15 : 0;
            }
            else {
                return 0;
            }
        }
        else {
            return 0;
        }
    }
    
    // directionMap[side][metadata] = sliceSide
    // down, up, north, south, west, east?
    int[][] directionMap = {
            {-1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 1}, // down
            {-1, -1, -1, -1, -1, -1, -1, -1, 3, 3, 3, 3}, // up
            {3, 2, 1, 0, 3, 2, 1, 0, -1, -1, 0, 2}, // north
            {1, 0, 3, 2, 1, 0, 3, 2, -1, -1, 2, 0}, // south
            {2, 1, 0, 3, 2, 1, 0, 3, 2, 0, -1, -1}, // west
            {0, 3, 2, 1, 0, 3, 2, 1, 0, 2, -1, -1} // east
    };
}
