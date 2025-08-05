package javax.rmi.CORBA;

import java.io.Serializable;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.SendingContext.RunTime;

/* loaded from: rt.jar:javax/rmi/CORBA/ValueHandler.class */
public interface ValueHandler {
    void writeValue(OutputStream outputStream, Serializable serializable);

    Serializable readValue(InputStream inputStream, int i2, Class cls, String str, RunTime runTime);

    String getRMIRepositoryID(Class cls);

    boolean isCustomMarshaled(Class cls);

    RunTime getRunTimeCodeBase();

    Serializable writeReplace(Serializable serializable);
}
