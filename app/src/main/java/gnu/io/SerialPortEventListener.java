package gnu.io;

import java.util.EventListener;

/* loaded from: RXTXcomm.jar:gnu/io/SerialPortEventListener.class */
public interface SerialPortEventListener extends EventListener {
    void serialEvent(SerialPortEvent serialPortEvent);
}
