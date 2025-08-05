package com.sun.corba.se.spi.servicecontext;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.orb.ORB;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/servicecontext/ServiceContextData.class */
public class ServiceContextData {
    private Class scClass;
    private Constructor scConstructor;
    private int scId;

    private void dprint(String str) {
        ORBUtility.dprint(this, str);
    }

    private void throwBadParam(String str, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(str);
        if (th != null) {
            bad_param.initCause(th);
        }
        throw bad_param;
    }

    public ServiceContextData(Class cls) {
        if (ORB.ORBInitDebug) {
            dprint("ServiceContextData constructor called for class " + ((Object) cls));
        }
        this.scClass = cls;
        try {
            if (ORB.ORBInitDebug) {
                dprint("Finding constructor for " + ((Object) cls));
            }
            try {
                this.scConstructor = cls.getConstructor(InputStream.class, GIOPVersion.class);
            } catch (NoSuchMethodException e2) {
                throwBadParam("Class does not have an InputStream constructor", e2);
            }
            if (ORB.ORBInitDebug) {
                dprint("Finding SERVICE_CONTEXT_ID field in " + ((Object) cls));
            }
            Field field = null;
            try {
                field = cls.getField("SERVICE_CONTEXT_ID");
            } catch (NoSuchFieldException e3) {
                throwBadParam("Class does not have a SERVICE_CONTEXT_ID member", e3);
            } catch (SecurityException e4) {
                throwBadParam("Could not access SERVICE_CONTEXT_ID member", e4);
            }
            if (ORB.ORBInitDebug) {
                dprint("Checking modifiers of SERVICE_CONTEXT_ID field in " + ((Object) cls));
            }
            int modifiers = field.getModifiers();
            if (!Modifier.isPublic(modifiers) || !Modifier.isStatic(modifiers) || !Modifier.isFinal(modifiers)) {
                throwBadParam("SERVICE_CONTEXT_ID field is not public static final", null);
            }
            if (ORB.ORBInitDebug) {
                dprint("Getting value of SERVICE_CONTEXT_ID in " + ((Object) cls));
            }
            try {
                this.scId = field.getInt(null);
            } catch (IllegalAccessException e5) {
                throwBadParam("Could not access value of SERVICE_CONTEXT_ID", e5);
            } catch (IllegalArgumentException e6) {
                throwBadParam("SERVICE_CONTEXT_ID not convertible to int", e6);
            }
        } catch (BAD_PARAM e7) {
            if (ORB.ORBInitDebug) {
                dprint("Exception in ServiceContextData constructor: " + ((Object) e7));
            }
            throw e7;
        } catch (Throwable th) {
            if (ORB.ORBInitDebug) {
                dprint("Unexpected Exception in ServiceContextData constructor: " + ((Object) th));
            }
        }
        if (ORB.ORBInitDebug) {
            dprint("ServiceContextData constructor completed");
        }
    }

    public ServiceContext makeServiceContext(InputStream inputStream, GIOPVersion gIOPVersion) {
        ServiceContext serviceContext = null;
        try {
            serviceContext = (ServiceContext) this.scConstructor.newInstance(inputStream, gIOPVersion);
        } catch (IllegalAccessException e2) {
            throwBadParam("InputStream constructor argument error", e2);
        } catch (IllegalArgumentException e3) {
            throwBadParam("InputStream constructor argument error", e3);
        } catch (InstantiationException e4) {
            throwBadParam("InputStream constructor called for abstract class", e4);
        } catch (InvocationTargetException e5) {
            throwBadParam("InputStream constructor threw exception " + ((Object) e5.getTargetException()), e5);
        }
        return serviceContext;
    }

    int getId() {
        return this.scId;
    }

    public String toString() {
        return "ServiceContextData[ scClass=" + ((Object) this.scClass) + " scConstructor=" + ((Object) this.scConstructor) + " scId=" + this.scId + " ]";
    }
}
