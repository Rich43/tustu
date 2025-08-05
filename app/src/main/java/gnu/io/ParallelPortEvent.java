package gnu.io;

import java.util.EventObject;

/* loaded from: RXTXcomm.jar:gnu/io/ParallelPortEvent.class */
public class ParallelPortEvent extends EventObject {
    public static final int PAR_EV_ERROR = 1;
    public static final int PAR_EV_BUFFER = 2;
    private boolean OldValue;
    private boolean NewValue;
    private int eventType;

    public ParallelPortEvent(ParallelPort parallelPort, int i2, boolean z2, boolean z3) {
        super(parallelPort);
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
