package fpgacraft.core;

public class LookupTable {
    public Port[] inputs = new Port[3];
    public Port[] outputs = new Port[2];
    public int[] table = new int[8];
    
    public LookupTable() {
        for (int i = 0; i < 3; i++) {
            inputs[i] = new Port(false, true);
        }
        
        for (int i = 0; i < 2; i++) {
            outputs[i] = new Port(true, false);
        }
    }
    
    public void update() {
        for (int i = 0; i < 2; i++) {
            outputs[i].update();
        }
        
        int s = (inputs[2].state ? 4 : 0) | (inputs[1].state ? 2 : 0) | (inputs[0].state ? 1 : 0);
        
        if (s >= 0 && s < 8) {
            outputs[0].state = (table[s] & 2) != 0;
            outputs[1].state = (table[s] & 1) != 0;
        }
    }
}