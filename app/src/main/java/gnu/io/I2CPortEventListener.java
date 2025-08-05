package gnu.io;

import java.util.EventListener;

/* loaded from: RXTXcomm.jar:gnu/io/I2CPortEventListener.class */
public interface I2CPortEventListener extends EventListener {
    void I2CEvent(I2CPortEvent i2CPortEvent);
}
