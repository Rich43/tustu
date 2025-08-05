package com.sun.xml.internal.ws.spi.db;

import com.sun.xml.internal.ws.db.glassfish.JAXBRIContextFactory;
import com.sun.xml.internal.ws.util.ServiceConfigurationError;
import com.sun.xml.internal.ws.util.ServiceFinder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/BindingContextFactory.class */
public abstract class BindingContextFactory {
    public static final String DefaultDatabindingMode = "glassfish.jaxb";
    public static final String JAXB_CONTEXT_FACTORY_PROPERTY = BindingContextFactory.class.getName();
    public static final Logger LOGGER = Logger.getLogger(BindingContextFactory.class.getName());

    protected abstract BindingContext newContext(JAXBContext jAXBContext);

    protected abstract BindingContext newContext(BindingInfo bindingInfo);

    protected abstract boolean isFor(String str);

    protected abstract BindingContext getContext(Marshaller marshaller);

    public static Iterator<BindingContextFactory> serviceIterator() {
        ServiceFinder<BindingContextFactory> sf = ServiceFinder.find(BindingContextFactory.class);
        final Iterator<BindingContextFactory> ibcf = sf.iterator();
        return new Iterator<BindingContextFactory>() { // from class: com.sun.xml.internal.ws.spi.db.BindingContextFactory.1
            private BindingContextFactory bcf;

            @Override // java.util.Iterator
            public boolean hasNext() {
                while (ibcf.hasNext()) {
                    try {
                        this.bcf = (BindingContextFactory) ibcf.next();
                        return true;
                    } catch (ServiceConfigurationError e2) {
                        BindingContextFactory.LOGGER.warning("skipping factory: ServiceConfigurationError: " + e2.getMessage());
                    } catch (NoClassDefFoundError ncdfe) {
                        BindingContextFactory.LOGGER.fine("skipping factory: NoClassDefFoundError: " + ncdfe.getMessage());
                    }
                }
                return false;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public BindingContextFactory next() {
                if (BindingContextFactory.LOGGER.isLoggable(Level.FINER)) {
                    BindingContextFactory.LOGGER.finer("SPI found provider: " + this.bcf.getClass().getName());
                }
                return this.bcf;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static List<BindingContextFactory> factories() {
        List<BindingContextFactory> factories = new ArrayList<>();
        Iterator<BindingContextFactory> ibcf = serviceIterator();
        while (ibcf.hasNext()) {
            factories.add(ibcf.next());
        }
        if (factories.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.log(Level.FINER, "No SPI providers for BindingContextFactory found, adding: " + JAXBRIContextFactory.class.getName());
            }
            factories.add(new JAXBRIContextFactory());
        }
        return factories;
    }

    private static BindingContextFactory getFactory(String mode) {
        for (BindingContextFactory f2 : factories()) {
            if (f2.isFor(mode)) {
                return f2;
            }
        }
        return null;
    }

    public static BindingContext create(JAXBContext context) throws DatabindingException {
        return getJAXBFactory(context).newContext(context);
    }

    public static BindingContext create(BindingInfo bi2) {
        String mode = bi2.getDatabindingMode();
        if (mode != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Using SEI-configured databindng mode: " + mode);
            }
        } else {
            String property = System.getProperty("BindingContextFactory");
            mode = property;
            if (property != null) {
                bi2.setDatabindingMode(mode);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Using databindng: " + mode + " based on 'BindingContextFactory' System property");
                }
            } else {
                String property2 = System.getProperty(JAXB_CONTEXT_FACTORY_PROPERTY);
                mode = property2;
                if (property2 != null) {
                    bi2.setDatabindingMode(mode);
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "Using databindng: " + mode + " based on '" + JAXB_CONTEXT_FACTORY_PROPERTY + "' System property");
                    }
                } else {
                    Iterator<BindingContextFactory> it = factories().iterator();
                    if (it.hasNext()) {
                        BindingContextFactory factory = it.next();
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.log(Level.FINE, "Using SPI-determined databindng mode: " + factory.getClass().getName());
                        }
                        return factory.newContext(bi2);
                    }
                    LOGGER.log(Level.SEVERE, "No Binding Context Factories found.");
                    throw new DatabindingException("No Binding Context Factories found.");
                }
            }
        }
        BindingContextFactory f2 = getFactory(mode);
        if (f2 != null) {
            return f2.newContext(bi2);
        }
        LOGGER.severe("Unknown Databinding mode: " + mode);
        throw new DatabindingException("Unknown Databinding mode: " + mode);
    }

    public static boolean isContextSupported(Object o2) {
        if (o2 == null) {
            return false;
        }
        String pkgName = o2.getClass().getPackage().getName();
        for (BindingContextFactory f2 : factories()) {
            if (f2.isFor(pkgName)) {
                return true;
            }
        }
        return false;
    }

    static BindingContextFactory getJAXBFactory(Object o2) {
        String pkgName = o2.getClass().getPackage().getName();
        BindingContextFactory f2 = getFactory(pkgName);
        if (f2 != null) {
            return f2;
        }
        throw new DatabindingException("Unknown JAXBContext implementation: " + ((Object) o2.getClass()));
    }

    public static BindingContext getBindingContext(Marshaller m2) {
        return getJAXBFactory(m2).getContext(m2);
    }
}
