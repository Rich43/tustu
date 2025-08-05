package com.sun.xml.internal.ws.db.glassfish;

import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.bind.api.TypeReference;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.PropertyAccessor;
import com.sun.xml.internal.ws.spi.db.TypeInfo;
import com.sun.xml.internal.ws.spi.db.WrapperComposite;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: rt.jar:com/sun/xml/internal/ws/db/glassfish/JAXBRIContextWrapper.class */
public class JAXBRIContextWrapper implements BindingContext {
    private Map<TypeInfo, TypeReference> typeRefs;
    private Map<TypeReference, TypeInfo> typeInfos;
    private JAXBRIContext context;

    JAXBRIContextWrapper(JAXBRIContext cxt, Map<TypeInfo, TypeReference> refs) {
        this.context = cxt;
        this.typeRefs = refs;
        if (refs != null) {
            this.typeInfos = new HashMap();
            for (TypeInfo ti : refs.keySet()) {
                this.typeInfos.put(this.typeRefs.get(ti), ti);
            }
        }
    }

    TypeReference typeReference(TypeInfo ti) {
        if (this.typeRefs != null) {
            return this.typeRefs.get(ti);
        }
        return null;
    }

    TypeInfo typeInfo(TypeReference tr) {
        if (this.typeInfos != null) {
            return this.typeInfos.get(tr);
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public Marshaller createMarshaller() throws JAXBException {
        return this.context.createMarshaller();
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public Unmarshaller createUnmarshaller() throws JAXBException {
        return this.context.createUnmarshaller();
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public void generateSchema(SchemaOutputResolver outputResolver) throws IOException {
        this.context.generateSchema(outputResolver);
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public String getBuildId() {
        return this.context.getBuildId();
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public QName getElementName(Class o2) throws JAXBException {
        return this.context.getElementName(o2);
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public QName getElementName(Object o2) throws JAXBException {
        return this.context.getElementName(o2);
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public <B, V> PropertyAccessor<B, V> getElementPropertyAccessor(Class<B> wrapperBean, String nsUri, String localName) throws JAXBException {
        return new RawAccessorWrapper(this.context.getElementPropertyAccessor(wrapperBean, nsUri, localName));
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public List<String> getKnownNamespaceURIs() {
        return this.context.getKnownNamespaceURIs();
    }

    public RuntimeTypeInfoSet getRuntimeTypeInfoSet() {
        return this.context.getRuntimeTypeInfoSet();
    }

    public QName getTypeName(TypeReference tr) {
        return this.context.getTypeName(tr);
    }

    public int hashCode() {
        return this.context.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        JAXBRIContextWrapper other = (JAXBRIContextWrapper) obj;
        if (this.context == other.context) {
            return true;
        }
        if (this.context == null || !this.context.equals(other.context)) {
            return false;
        }
        return true;
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public boolean hasSwaRef() {
        return this.context.hasSwaRef();
    }

    public String toString() {
        return JAXBRIContextWrapper.class.getName() + " : " + this.context.toString();
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public XMLBridge createBridge(TypeInfo ti) {
        TypeReference tr = this.typeRefs.get(ti);
        Bridge b2 = this.context.createBridge(tr);
        return WrapperComposite.class.equals(ti.type) ? new WrapperBridge(this, b2) : new BridgeWrapper(this, b2);
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public JAXBContext getJAXBContext() {
        return this.context;
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public QName getTypeName(TypeInfo ti) {
        TypeReference tr = this.typeRefs.get(ti);
        return this.context.getTypeName(tr);
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public XMLBridge createFragmentBridge() {
        return new MarshallerBridge((JAXBContextImpl) this.context);
    }

    @Override // com.sun.xml.internal.ws.spi.db.BindingContext
    public Object newWrapperInstace(Class<?> wrapperType) throws IllegalAccessException, InstantiationException {
        return wrapperType.newInstance();
    }
}
