package com.sun.xml.internal.ws.developer;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.glassfish.gmbal.ManagedAttribute;
import com.sun.org.glassfish.gmbal.ManagedData;
import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.bind.api.TypeReference;
import com.sun.xml.internal.ws.api.FeatureConstructor;
import com.sun.xml.internal.ws.api.model.SEIModel;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.ws.WebServiceFeature;

@ManagedData
/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/UsesJAXBContextFeature.class */
public class UsesJAXBContextFeature extends WebServiceFeature {
    public static final String ID = "http://jax-ws.dev.java.net/features/uses-jaxb-context";
    private final JAXBContextFactory factory;

    @FeatureConstructor({"value"})
    public UsesJAXBContextFeature(@NotNull Class<? extends JAXBContextFactory> factoryClass) {
        try {
            this.factory = factoryClass.getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (IllegalAccessException e2) {
            Error x2 = new IllegalAccessError(e2.getMessage());
            x2.initCause(e2);
            throw x2;
        } catch (InstantiationException e3) {
            Error x3 = new InstantiationError(e3.getMessage());
            x3.initCause(e3);
            throw x3;
        } catch (NoSuchMethodException e4) {
            Error x4 = new NoSuchMethodError(e4.getMessage());
            x4.initCause(e4);
            throw x4;
        } catch (InvocationTargetException e5) {
            Error x5 = new InstantiationError(e5.getMessage());
            x5.initCause(e5);
            throw x5;
        }
    }

    public UsesJAXBContextFeature(@Nullable JAXBContextFactory factory) {
        this.factory = factory;
    }

    public UsesJAXBContextFeature(@Nullable final JAXBRIContext context) {
        this.factory = new JAXBContextFactory() { // from class: com.sun.xml.internal.ws.developer.UsesJAXBContextFeature.1
            @Override // com.sun.xml.internal.ws.developer.JAXBContextFactory
            @NotNull
            public JAXBRIContext createJAXBContext(@NotNull SEIModel sei, @NotNull List<Class> classesToBind, @NotNull List<TypeReference> typeReferences) throws JAXBException {
                return context;
            }
        };
    }

    @ManagedAttribute
    @Nullable
    public JAXBContextFactory getFactory() {
        return this.factory;
    }

    @Override // javax.xml.ws.WebServiceFeature
    @ManagedAttribute
    public String getID() {
        return ID;
    }
}
