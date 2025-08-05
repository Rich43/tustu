package javax.rmi.CORBA;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import org.omg.CORBA.ORB;

/* loaded from: rt.jar:javax/rmi/CORBA/StubDelegate.class */
public interface StubDelegate {
    int hashCode(Stub stub);

    boolean equals(Stub stub, Object obj);

    String toString(Stub stub);

    void connect(Stub stub, ORB orb) throws RemoteException;

    void readObject(Stub stub, ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException;

    void writeObject(Stub stub, ObjectOutputStream objectOutputStream) throws IOException;
}
