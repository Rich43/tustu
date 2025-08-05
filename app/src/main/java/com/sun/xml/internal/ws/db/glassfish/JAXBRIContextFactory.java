package com.sun.xml.internal.ws.db.glassfish;

import com.sun.xml.internal.bind.api.CompositeStructure;
import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.bind.api.TypeReference;
import com.sun.xml.internal.bind.v2.ContextFactory;
import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;
import com.sun.xml.internal.bind.v2.runtime.MarshallerImpl;
import com.sun.xml.internal.ws.developer.JAXBContextFactory;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.BindingContextFactory;
import com.sun.xml.internal.ws.spi.db.BindingInfo;
import com.sun.xml.internal.ws.spi.db.DatabindingException;
import com.sun.xml.internal.ws.spi.db.TypeInfo;
import com.sun.xml.internal.ws.spi.db.WrapperComposite;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/* loaded from: rt.jar:com/sun/xml/internal/ws/db/glassfish/JAXBRIContextFactory.class */
public class JAXBRIContextFactory extends BindingContextFactory {
    @Override // com.sun.xml.internal.ws.spi.db.BindingContextFactory
    public BindingContext newContext(JAXBContext context) {
        return new JAXBRIContextWrapper((JAXBRIContext) context, null);
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContextFactory
    public BindingContext newContext(BindingInfo bi2) {
        JAXBRIContext jAXBRIContextCreateContext;
        Class[] classes = (Class[]) bi2.contentClasses().toArray(new Class[bi2.contentClasses().size()]);
        for (int i2 = 0; i2 < classes.length; i2++) {
            if (WrapperComposite.class.equals(classes[i2])) {
                classes[i2] = CompositeStructure.class;
            }
        }
        Map<TypeInfo, TypeReference> typeInfoMappings = typeInfoMappings(bi2.typeInfos());
        Map<Class, Class> subclassReplacements = bi2.subclassReplacements();
        String defaultNamespaceRemap = bi2.getDefaultNamespace();
        Boolean c14nSupport = (Boolean) bi2.properties().get("c14nSupport");
        RuntimeAnnotationReader ar2 = (RuntimeAnnotationReader) bi2.properties().get("com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader");
        JAXBContextFactory jaxbContextFactory = (JAXBContextFactory) bi2.properties().get(JAXBContextFactory.class.getName());
        try {
            if (jaxbContextFactory != null) {
                jAXBRIContextCreateContext = jaxbContextFactory.createJAXBContext(bi2.getSEIModel(), toList(classes), toList(typeInfoMappings.values()));
            } else {
                jAXBRIContextCreateContext = ContextFactory.createContext(classes, typeInfoMappings.values(), subclassReplacements, defaultNamespaceRemap, c14nSupport != null ? c14nSupport.booleanValue() : false, ar2, false, false, false);
            }
            JAXBRIContext context = jAXBRIContextCreateContext;
            return new JAXBRIContextWrapper(context, typeInfoMappings);
        } catch (Exception e2) {
            throw new DatabindingException(e2);
        }
    }

    private <T> List<T> toList(T[] a2) {
        List<T> l2 = new ArrayList<>();
        l2.addAll(Arrays.asList(a2));
        return l2;
    }

    private <T> List<T> toList(Collection<T> col) {
        if (col instanceof List) {
            return (List) col;
        }
        List<T> l2 = new ArrayList<>();
        l2.addAll(col);
        return l2;
    }

    private Map<TypeInfo, TypeReference> typeInfoMappings(Collection<TypeInfo> typeInfos) {
        Map<TypeInfo, TypeReference> map = new HashMap<>();
        for (TypeInfo ti : typeInfos) {
            Type type = WrapperComposite.class.equals(ti.type) ? CompositeStructure.class : ti.type;
            TypeReference tr = new TypeReference(ti.tagName, type, ti.annotations);
            map.put(ti, tr);
        }
        return map;
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContextFactory
    protected BindingContext getContext(Marshaller m2) {
        return newContext(((MarshallerImpl) m2).getContext());
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContextFactory
    protected boolean isFor(String str) {
        return str.equals("glassfish.jaxb") || str.equals(getClass().getName()) || str.equals("com.sun.xml.internal.bind.v2.runtime");
    }
}
