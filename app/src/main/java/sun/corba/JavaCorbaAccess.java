package sun.corba;

import com.sun.corba.se.impl.io.ValueHandlerImpl;

/* loaded from: rt.jar:sun/corba/JavaCorbaAccess.class */
public interface JavaCorbaAccess {
    ValueHandlerImpl newValueHandlerImpl();

    Class<?> loadClass(String str) throws ClassNotFoundException;
}
