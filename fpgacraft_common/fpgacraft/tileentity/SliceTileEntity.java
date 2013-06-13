package fpgacraft.tileentity;

import fpgacraft.core.Slice;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class SliceTileEntity extends TileEntity {
    
    private ForgeDirection side;
    private ForgeDirection orientation;
    
    public Slice slice = new Slice();
    
    public SliceTileEntity() {
        super();
        slice.sides[2].connectTo(slice.sides[0]);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }
    
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }
    
    @Override
    public void updateEntity() {
        slice.update();
        
        if (slice.wasUpdated()) {
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, blockType.blockID);
        }
    }
    
    public void setSide(ForgeDirection side) {
        this.side = side;
    }
    
    public ForgeDirection getSide() {
        return side;
    }
    
    public void setOrientation(ForgeDirection orientation) {
        this.orientation = orientation;
    }
    
    public ForgeDirection getOrientation() {
        return orientation;
    }
    
    public void updateConnections() {
        
    }
}
