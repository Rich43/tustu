package gnu.io;

import java.util.EventListener;

/* loaded from: RXTXcomm.jar:gnu/io/ParallelPortEventListener.class */
public interface ParallelPortEventListener extends EventListener {
    void parallelEvent(ParallelPortEvent parallelPortEvent);
}
