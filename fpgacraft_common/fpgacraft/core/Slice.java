package fpgacraft.core;

public class Slice {
    public Port sides[] = new Port[4];
    public LookupTable lut = new LookupTable();
    private boolean updated = false;
    
    public Slice() {
        for (int i = 0; i < 4; i++) {
            sides[i] = new Port(true, true);
        }
    }
    
    public void update() {
        lut.update();
        
        for (int i = 0; i < 4; i++) {
            sides[i].update();
            
            if (sides[i].wasUpdated()) {
                updated = true;
            }
        }
    }
    
    public boolean wasUpdated() {
        if (updated) {
            updated = false;
            return true;
        }
        else {
            return false;
        }
    }
}
