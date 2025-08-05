package com.sun.corba.se.impl.naming.cosnaming;

import com.sun.corba.se.impl.logging.NamingSystemException;
import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.util.Properties;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/cosnaming/TransientNameServer.class */
public class TransientNameServer {
    private static boolean debug = false;
    static NamingSystemException wrapper = NamingSystemException.get(CORBALogDomains.NAMING);

    public static void trace(String str) {
        if (debug) {
            System.out.println(str);
        }
    }

    public static void initDebug(String[] strArr) {
        if (debug) {
            return;
        }
        for (String str : strArr) {
            if (str.equalsIgnoreCase("-debug")) {
                debug = true;
                return;
            }
        }
        debug = false;
    }

    private static Object initializeRootNamingContext(ORB orb) {
        try {
            return new TransientNameService((com.sun.corba.se.spi.orb.ORB) orb).initialNamingContext();
        } catch (SystemException e2) {
            throw wrapper.transNsCannotCreateInitialNcSys(e2);
        } catch (Exception e3) {
            throw wrapper.transNsCannotCreateInitialNc(e3);
        }
    }

    public static void main(String[] strArr) {
        initDebug(strArr);
        int i2 = 0;
        try {
            trace("Transient name server started with args " + ((Object) strArr));
            Properties properties = System.getProperties();
            properties.put(ORBConstants.SERVER_ID_PROPERTY, ORBConstants.NAME_SERVICE_SERVER_ID);
            properties.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
            try {
                String property = System.getProperty(ORBConstants.INITIAL_PORT_PROPERTY);
                if (property != null && property.length() > 0) {
                    i2 = Integer.parseInt(property);
                    if (i2 == 0) {
                        throw wrapper.transientNameServerBadPort();
                    }
                }
            } catch (NumberFormatException e2) {
            }
            if (System.getProperty(ORBConstants.INITIAL_HOST_PROPERTY) != null) {
                throw wrapper.transientNameServerBadHost();
            }
            for (int i3 = 0; i3 < strArr.length; i3++) {
                if (strArr[i3].equals("-ORBInitialPort") && i3 < strArr.length - 1) {
                    i2 = Integer.parseInt(strArr[i3 + 1]);
                    if (i2 == 0) {
                        throw wrapper.transientNameServerBadPort();
                    }
                }
                if (strArr[i3].equals("-ORBInitialHost")) {
                    throw wrapper.transientNameServerBadHost();
                }
            }
            if (i2 == 0) {
                i2 = 900;
                properties.put(ORBConstants.INITIAL_PORT_PROPERTY, Integer.toString(900));
            }
            properties.put(ORBConstants.PERSISTENT_SERVER_PORT_PROPERTY, Integer.toString(i2));
            ORB orbInit = ORB.init(strArr, properties);
            trace("ORB object returned from init: " + ((Object) orbInit));
            Object objectInitializeRootNamingContext = initializeRootNamingContext(orbInit);
            ((com.sun.corba.se.org.omg.CORBA.ORB) orbInit).register_initial_reference("NamingService", objectInitializeRootNamingContext);
            String strObject_to_string = null;
            if (objectInitializeRootNamingContext != null) {
                strObject_to_string = orbInit.object_to_string(objectInitializeRootNamingContext);
            } else {
                NamingUtils.errprint(CorbaResourceUtil.getText("tnameserv.exception", i2));
                NamingUtils.errprint(CorbaResourceUtil.getText("tnameserv.usage"));
                System.exit(1);
            }
            trace("name service created");
            System.out.println(CorbaResourceUtil.getText("tnameserv.hs1", strObject_to_string));
            System.out.println(CorbaResourceUtil.getText("tnameserv.hs2", i2));
            System.out.println(CorbaResourceUtil.getText("tnameserv.hs3"));
            Object obj = new Object();
            synchronized (obj) {
                obj.wait();
            }
        } catch (Exception e3) {
            if (0 != 0) {
                NamingUtils.errprint(CorbaResourceUtil.getText("tnameserv.invalidhostoption"));
            } else if (0 != 0) {
                NamingUtils.errprint(CorbaResourceUtil.getText("tnameserv.orbinitialport0"));
            } else {
                NamingUtils.errprint(CorbaResourceUtil.getText("tnameserv.exception", i2));
                NamingUtils.errprint(CorbaResourceUtil.getText("tnameserv.usage"));
            }
            e3.printStackTrace();
        }
    }

    private TransientNameServer() {
    }
}
