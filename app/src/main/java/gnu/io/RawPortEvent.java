package gnu.io;

import java.util.EventObject;

/* loaded from: RXTXcomm.jar:gnu/io/RawPortEvent.class */
public class RawPortEvent extends EventObject {
    public static final int DATA_AVAILABLE = 1;
    public static final int OUTPUT_BUFFER_EMPTY = 2;
    public static final int CTS = 3;
    public static final int DSR = 4;
    public static final int RI = 5;
    public static final int CD = 6;
    public static final int OE = 7;
    public static final int PE = 8;
    public static final int FE = 9;
    public static final int BI = 10;
    private boolean OldValue;
    private boolean NewValue;
    private int eventType;

    public RawPortEvent(RawPort rawPort, int i2, boolean z2, boolean z3) {
        super(rawPort);
        this.OldValue = z2;
        this.NewValue = z3;
        this.eventType = i2;
    }

    public int getEventType() {
        return this.eventType;
    }

    public boolean getNewValue() {
        return this.NewValue;
    }

    public boolean getOldValue() {
        return this.OldValue;
    }
}
