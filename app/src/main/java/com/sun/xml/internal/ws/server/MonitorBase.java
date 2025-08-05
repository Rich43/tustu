package com.sun.xml.internal.ws.server;

import com.sun.istack.internal.NotNull;
import com.sun.org.glassfish.external.amx.AMXGlassfish;
import com.sun.org.glassfish.gmbal.Description;
import com.sun.org.glassfish.gmbal.InheritedAttributes;
import com.sun.org.glassfish.gmbal.ManagedData;
import com.sun.org.glassfish.gmbal.ManagedObjectManager;
import com.sun.org.glassfish.gmbal.ManagedObjectManagerFactory;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.config.management.policy.ManagedClientAssertion;
import com.sun.xml.internal.ws.api.config.management.policy.ManagedServiceAssertion;
import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.client.Stub;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.management.ObjectName;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/MonitorBase.class */
public abstract class MonitorBase {
    private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.monitoring");
    private static ManagementAssertion.Setting clientMonitoring;
    private static ManagementAssertion.Setting endpointMonitoring;
    private static int typelibDebug;
    private static String registrationDebug;
    private static boolean runtimeDebug;
    private static int maxUniqueEndpointRootNameRetries;
    private static final String monitorProperty = "com.sun.xml.internal.ws.monitoring.";

    static {
        clientMonitoring = ManagementAssertion.Setting.NOT_SET;
        endpointMonitoring = ManagementAssertion.Setting.NOT_SET;
        typelibDebug = -1;
        registrationDebug = "NONE";
        runtimeDebug = false;
        maxUniqueEndpointRootNameRetries = 100;
        try {
            endpointMonitoring = propertyToSetting("com.sun.xml.internal.ws.monitoring.endpoint");
            clientMonitoring = propertyToSetting("com.sun.xml.internal.ws.monitoring.client");
            Integer i2 = Integer.getInteger("com.sun.xml.internal.ws.monitoring.typelibDebug");
            if (i2 != null) {
                typelibDebug = i2.intValue();
            }
            String s2 = System.getProperty("com.sun.xml.internal.ws.monitoring.registrationDebug");
            if (s2 != null) {
                registrationDebug = s2.toUpperCase();
            }
            String s3 = System.getProperty("com.sun.xml.internal.ws.monitoring.runtimeDebug");
            if (s3 != null && s3.toLowerCase().equals("true")) {
                runtimeDebug = true;
            }
            Integer i3 = Integer.getInteger("com.sun.xml.internal.ws.monitoring.maxUniqueEndpointRootNameRetries");
            if (i3 != null) {
                maxUniqueEndpointRootNameRetries = i3.intValue();
            }
        } catch (Exception e2) {
            logger.log(Level.WARNING, "Error while reading monitoring properties", (Throwable) e2);
        }
    }

    @NotNull
    public ManagedObjectManager createManagedObjectManager(WSEndpoint endpoint) throws WebServiceException {
        String rootName = endpoint.getServiceName().getLocalPart() + LanguageTag.SEP + endpoint.getPortName().getLocalPart();
        if (rootName.equals(LanguageTag.SEP)) {
            rootName = "provider";
        }
        String contextPath = getContextPath(endpoint);
        if (contextPath != null) {
            rootName = contextPath + LanguageTag.SEP + rootName;
        }
        ManagedServiceAssertion assertion = ManagedServiceAssertion.getAssertion(endpoint);
        if (assertion != null) {
            String id = assertion.getId();
            if (id != null) {
                rootName = id;
            }
            if (assertion.monitoringAttribute() == ManagementAssertion.Setting.OFF) {
                return disabled("This endpoint", rootName);
            }
        }
        if (endpointMonitoring.equals(ManagementAssertion.Setting.OFF)) {
            return disabled("Global endpoint", rootName);
        }
        return createMOMLoop(rootName, 0);
    }

    private String getContextPath(WSEndpoint endpoint) {
        try {
            Object container = endpoint.getContainer();
            Method getSPI = container.getClass().getDeclaredMethod("getSPI", Class.class);
            getSPI.setAccessible(true);
            Class servletContextClass = Class.forName("javax.servlet.ServletContext");
            Object servletContext = getSPI.invoke(container, servletContextClass);
            if (servletContext != null) {
                Method getContextPath = servletContextClass.getDeclaredMethod("getContextPath", new Class[0]);
                getContextPath.setAccessible(true);
                return (String) getContextPath.invoke(servletContext, new Object[0]);
            }
            return null;
        } catch (Throwable t2) {
            logger.log(Level.FINEST, "getContextPath", t2);
            return null;
        }
    }

    @NotNull
    public ManagedObjectManager createManagedObjectManager(Stub stub) throws WebServiceException {
        EndpointAddress ea = stub.requestContext.getEndpointAddress();
        if (ea == null) {
            return ManagedObjectManagerFactory.createNOOP();
        }
        String rootName = ea.toString();
        ManagedClientAssertion assertion = ManagedClientAssertion.getAssertion(stub.getPortInfo());
        if (assertion != null) {
            String id = assertion.getId();
            if (id != null) {
                rootName = id;
            }
            if (assertion.monitoringAttribute() == ManagementAssertion.Setting.OFF) {
                return disabled("This client", rootName);
            }
            if (assertion.monitoringAttribute() == ManagementAssertion.Setting.ON && clientMonitoring != ManagementAssertion.Setting.OFF) {
                return createMOMLoop(rootName, 0);
            }
        }
        if (clientMonitoring == ManagementAssertion.Setting.NOT_SET || clientMonitoring == ManagementAssertion.Setting.OFF) {
            return disabled("Global client", rootName);
        }
        return createMOMLoop(rootName, 0);
    }

    @NotNull
    private ManagedObjectManager disabled(String x2, String rootName) {
        String msg = x2 + " monitoring disabled. " + rootName + " will not be monitored";
        logger.log(Level.CONFIG, msg);
        return ManagedObjectManagerFactory.createNOOP();
    }

    @NotNull
    private ManagedObjectManager createMOMLoop(String rootName, int unique) {
        boolean isFederated = AMXGlassfish.getGlassfishVersion() != null;
        ManagedObjectManager mom = createMOM(isFederated);
        return createRoot(initMOM(mom), rootName, unique);
    }

    @NotNull
    private ManagedObjectManager createMOM(boolean isFederated) {
        ManagedObjectManager managedObjectManagerCreateStandalone;
        try {
            if (isFederated) {
                managedObjectManagerCreateStandalone = ManagedObjectManagerFactory.createFederated(AMXGlassfish.DEFAULT.serverMon(AMXGlassfish.DEFAULT.dasName()));
            } else {
                managedObjectManagerCreateStandalone = ManagedObjectManagerFactory.createStandalone("com.sun.metro");
            }
            return new RewritingMOM(managedObjectManagerCreateStandalone);
        } catch (Throwable t2) {
            if (isFederated) {
                logger.log(Level.CONFIG, "Problem while attempting to federate with GlassFish AMX monitoring.  Trying standalone.", t2);
                return createMOM(false);
            }
            logger.log(Level.WARNING, "Ignoring exception - starting up without monitoring", t2);
            return ManagedObjectManagerFactory.createNOOP();
        }
    }

    @NotNull
    private ManagedObjectManager initMOM(ManagedObjectManager mom) {
        try {
            if (typelibDebug != -1) {
                mom.setTypelibDebug(typelibDebug);
            }
            if (registrationDebug.equals("FINE")) {
                mom.setRegistrationDebug(ManagedObjectManager.RegistrationDebugLevel.FINE);
            } else if (registrationDebug.equals("NORMAL")) {
                mom.setRegistrationDebug(ManagedObjectManager.RegistrationDebugLevel.NORMAL);
            } else {
                mom.setRegistrationDebug(ManagedObjectManager.RegistrationDebugLevel.NONE);
            }
            mom.setRuntimeDebug(runtimeDebug);
            mom.suppressDuplicateRootReport(true);
            mom.stripPrefix("com.sun.xml.internal.ws.server", "com.sun.xml.internal.ws.rx.rm.runtime.sequence");
            mom.addAnnotation(WebServiceFeature.class, DummyWebServiceFeature.class.getAnnotation(ManagedData.class));
            mom.addAnnotation(WebServiceFeature.class, DummyWebServiceFeature.class.getAnnotation(Description.class));
            mom.addAnnotation(WebServiceFeature.class, DummyWebServiceFeature.class.getAnnotation(InheritedAttributes.class));
            mom.suspendJMXRegistration();
            return mom;
        } catch (Throwable t2) {
            try {
                mom.close();
            } catch (IOException e2) {
                logger.log(Level.CONFIG, "Ignoring exception caught when closing unused ManagedObjectManager", (Throwable) e2);
            }
            logger.log(Level.WARNING, "Ignoring exception - starting up without monitoring", t2);
            return ManagedObjectManagerFactory.createNOOP();
        }
    }

    private ManagedObjectManager createRoot(ManagedObjectManager mom, String rootName, int unique) {
        String name = rootName + (unique == 0 ? "" : LanguageTag.SEP + String.valueOf(unique));
        try {
            Object ignored = mom.createRoot(this, name);
            if (ignored != null) {
                ObjectName ignoredName = mom.getObjectName(mom.getRoot());
                if (ignoredName != null) {
                    logger.log(Level.INFO, "Metro monitoring rootname successfully set to: {0}", ignoredName);
                }
                return mom;
            }
            try {
                mom.close();
            } catch (IOException e2) {
                logger.log(Level.CONFIG, "Ignoring exception caught when closing unused ManagedObjectManager", (Throwable) e2);
            }
            String basemsg = "Duplicate Metro monitoring rootname: " + name + " : ";
            if (unique > maxUniqueEndpointRootNameRetries) {
                String msg = basemsg + "Giving up.";
                logger.log(Level.INFO, msg);
                return ManagedObjectManagerFactory.createNOOP();
            }
            String msg2 = basemsg + "Will try to make unique";
            logger.log(Level.CONFIG, msg2);
            return createMOMLoop(rootName, unique + 1);
        } catch (Throwable t2) {
            logger.log(Level.WARNING, "Error while creating monitoring root with name: " + rootName, t2);
            return ManagedObjectManagerFactory.createNOOP();
        }
    }

    private static ManagementAssertion.Setting propertyToSetting(String propName) {
        String s2 = System.getProperty(propName);
        if (s2 == null) {
            return ManagementAssertion.Setting.NOT_SET;
        }
        String s3 = s2.toLowerCase();
        if (s3.equals("false") || s3.equals("off")) {
            return ManagementAssertion.Setting.OFF;
        }
        if (s3.equals("true") || s3.equals(FXMLLoader.EVENT_HANDLER_PREFIX)) {
            return ManagementAssertion.Setting.ON;
        }
        return ManagementAssertion.Setting.NOT_SET;
    }
}
