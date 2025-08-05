package javax.rmi.CORBA;

import com.sun.corba.se.impl.javax.rmi.CORBA.StubDelegateImpl;
import com.sun.corba.se.impl.orbutil.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.RMIClassLoader;
import java.security.AccessController;
import java.util.Properties;
import org.omg.CORBA.INITIALIZE;
import org.omg.CORBA.ORB;
import org.omg.CORBA_2_3.portable.ObjectImpl;

/* loaded from: rt.jar:javax/rmi/CORBA/Stub.class */
public abstract class Stub extends ObjectImpl implements Serializable {
    private static final long serialVersionUID = 1087775603798577179L;
    private transient StubDelegate stubDelegate = null;
    private static Class stubDelegateClass;
    private static final String StubClassKey = "javax.rmi.CORBA.StubClass";

    static {
        stubDelegateClass = null;
        Object objCreateDelegate = createDelegate(StubClassKey);
        if (objCreateDelegate != null) {
            stubDelegateClass = objCreateDelegate.getClass();
        }
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public int hashCode() {
        if (this.stubDelegate == null) {
            setDefaultDelegate();
        }
        if (this.stubDelegate != null) {
            return this.stubDelegate.hashCode(this);
        }
        return 0;
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public boolean equals(Object obj) {
        if (this.stubDelegate == null) {
            setDefaultDelegate();
        }
        if (this.stubDelegate != null) {
            return this.stubDelegate.equals(this, obj);
        }
        return false;
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String toString() {
        if (this.stubDelegate == null) {
            setDefaultDelegate();
        }
        if (this.stubDelegate != null) {
            String string = this.stubDelegate.toString(this);
            if (string == null) {
                return super.toString();
            }
            return string;
        }
        return super.toString();
    }

    public void connect(ORB orb) throws RemoteException {
        if (this.stubDelegate == null) {
            setDefaultDelegate();
        }
        if (this.stubDelegate != null) {
            this.stubDelegate.connect(this, orb);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (this.stubDelegate == null) {
            setDefaultDelegate();
        }
        if (this.stubDelegate != null) {
            this.stubDelegate.readObject(this, objectInputStream);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.stubDelegate == null) {
            setDefaultDelegate();
        }
        if (this.stubDelegate != null) {
            this.stubDelegate.writeObject(this, objectOutputStream);
        }
    }

    private void setDefaultDelegate() {
        if (stubDelegateClass != null) {
            try {
                this.stubDelegate = (StubDelegate) stubDelegateClass.newInstance();
            } catch (Exception e2) {
            }
        }
    }

    private static Object createDelegate(String str) {
        Properties oRBPropertiesFile;
        String property = (String) AccessController.doPrivileged(new GetPropertyAction(str));
        if (property == null && (oRBPropertiesFile = getORBPropertiesFile()) != null) {
            property = oRBPropertiesFile.getProperty(str);
        }
        if (property == null) {
            return new StubDelegateImpl();
        }
        try {
            return loadDelegateClass(property).newInstance();
        } catch (ClassNotFoundException e2) {
            INITIALIZE initialize = new INITIALIZE("Cannot instantiate " + property);
            initialize.initCause(e2);
            throw initialize;
        } catch (Exception e3) {
            INITIALIZE initialize2 = new INITIALIZE("Error while instantiating" + property);
            initialize2.initCause(e3);
            throw initialize2;
        }
    }

    private static Class loadDelegateClass(String str) throws ClassNotFoundException {
        try {
            return Class.forName(str, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e2) {
            try {
                return RMIClassLoader.loadClass(str);
            } catch (MalformedURLException e3) {
                throw new ClassNotFoundException("Could not load " + str + ": " + e3.toString());
            }
        }
    }

    private static Properties getORBPropertiesFile() {
        return (Properties) AccessController.doPrivileged(new GetORBPropertiesFileAction());
    }
}
