package com.sun.corba.se.impl.copyobject;

import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.spi.copyobject.ObjectCopier;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Remote;
import org.omg.CORBA.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/copyobject/JavaStreamObjectCopierImpl.class */
public class JavaStreamObjectCopierImpl implements ObjectCopier {
    private ORB orb;

    public JavaStreamObjectCopierImpl(ORB orb) {
        this.orb = orb;
    }

    @Override // com.sun.corba.se.spi.copyobject.ObjectCopier
    public Object copy(Object obj) {
        if (obj instanceof Remote) {
            return Utility.autoConnect(obj, this.orb, true);
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(10000);
            new ObjectOutputStream(byteArrayOutputStream).writeObject(obj);
            return new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())).readObject();
        } catch (Exception e2) {
            System.out.println("Failed with exception:" + ((Object) e2));
            return null;
        }
    }
}
