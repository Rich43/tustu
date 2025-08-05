package javax.rmi.CORBA;

import java.io.Serializable;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:javax/rmi/CORBA/ValueHandlerMultiFormat.class */
public interface ValueHandlerMultiFormat extends ValueHandler {
    byte getMaximumStreamFormatVersion();

    void writeValue(OutputStream outputStream, Serializable serializable, byte b2);
}
