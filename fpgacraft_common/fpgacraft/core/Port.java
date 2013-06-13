package fpgacraft.core;

public class Port {
    public boolean state = false;
    public boolean sending = false;
    public boolean canSend;
    public boolean canReceive;
    public Port connection = null;
    public boolean updated = false;
    
    public Port(boolean send, boolean receive) {
        canSend = send;
        canReceive = receive;
    }
    
    public void connectTo(Port other) {
        if (canSend && other.canReceive) {
            this.connection = other;
            this.sending = true;
            other.sending = false;
        }
    }
    
    public void update() {
        if (connection != null && sending && connection.state != state) {
            connection.state = state;
            connection.updated = true;
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