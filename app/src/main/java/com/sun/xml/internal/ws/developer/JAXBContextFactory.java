package com.sun.xml.internal.ws.developer;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.bind.api.TypeReference;
import com.sun.xml.internal.ws.api.model.SEIModel;
import java.util.List;
import javax.xml.bind.JAXBException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/JAXBContextFactory.class */
public interface JAXBContextFactory {
    public static final JAXBContextFactory DEFAULT = new JAXBContextFactory() { // from class: com.sun.xml.internal.ws.developer.JAXBContextFactory.1
        @Override // com.sun.xml.internal.ws.developer.JAXBContextFactory
        @NotNull
        public JAXBRIContext createJAXBContext(@NotNull SEIModel sei, @NotNull List<Class> classesToBind, @NotNull List<TypeReference> typeReferences) throws JAXBException {
            return JAXBRIContext.newInstance((Class[]) classesToBind.toArray(new Class[classesToBind.size()]), typeReferences, null, sei.getTargetNamespace(), false, null);
        }
    };

    @NotNull
    JAXBRIContext createJAXBContext(@NotNull SEIModel sEIModel, @NotNull List<Class> list, @NotNull List<TypeReference> list2) throws JAXBException;
}
