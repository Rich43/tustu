package sun.rmi.log;

import java.io.InputStream;
import java.io.OutputStream;
import sun.rmi.server.MarshalInputStream;
import sun.rmi.server.MarshalOutputStream;

/* loaded from: rt.jar:sun/rmi/log/LogHandler.class */
public abstract class LogHandler {
    public abstract Object initialSnapshot() throws Exception;

    public abstract Object applyUpdate(Object obj, Object obj2) throws Exception;

    public void snapshot(OutputStream outputStream, Object obj) throws Exception {
        MarshalOutputStream marshalOutputStream = new MarshalOutputStream(outputStream);
        marshalOutputStream.writeObject(obj);
        marshalOutputStream.flush();
    }

    public Object recover(InputStream inputStream) throws Exception {
        return new MarshalInputStream(inputStream).readObject();
    }

    public void writeUpdate(LogOutputStream logOutputStream, Object obj) throws Exception {
        MarshalOutputStream marshalOutputStream = new MarshalOutputStream(logOutputStream);
        marshalOutputStream.writeObject(obj);
        marshalOutputStream.flush();
    }

    public Object readUpdate(LogInputStream logInputStream, Object obj) throws Exception {
        return applyUpdate(new MarshalInputStream(logInputStream).readObject(), obj);
    }
}
