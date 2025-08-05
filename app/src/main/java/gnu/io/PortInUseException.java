package gnu.io;

/* loaded from: RXTXcomm.jar:gnu/io/PortInUseException.class */
public class PortInUseException extends Exception {
    public String currentOwner;

    PortInUseException(String str) {
        super(str);
        this.currentOwner = str;
    }

    public PortInUseException() {
    }
}
