package com.sun.xml.internal.ws.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import java.util.Iterator;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/AbstractFeaturedObjectImpl.class */
abstract class AbstractFeaturedObjectImpl extends AbstractExtensibleImpl implements WSDLFeaturedObject {
    protected WebServiceFeatureList features;

    protected AbstractFeaturedObjectImpl(XMLStreamReader xsr) {
        super(xsr);
    }

    protected AbstractFeaturedObjectImpl(String systemId, int lineNumber) {
        super(systemId, lineNumber);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject
    public final void addFeature(WebServiceFeature feature) {
        if (this.features == null) {
            this.features = new WebServiceFeatureList();
        }
        this.features.add(feature);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject
    @NotNull
    public WebServiceFeatureList getFeatures() {
        if (this.features == null) {
            return new WebServiceFeatureList();
        }
        return this.features;
    }

    public final WebServiceFeature getFeature(String id) {
        if (this.features != null) {
            Iterator<WebServiceFeature> it = this.features.iterator();
            while (it.hasNext()) {
                WebServiceFeature f2 = it.next();
                if (f2.getID().equals(id)) {
                    return f2;
                }
            }
            return null;
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject
    @Nullable
    public <F extends WebServiceFeature> F getFeature(@NotNull Class<F> cls) {
        if (this.features == null) {
            return null;
        }
        return (F) this.features.get((Class) cls);
    }
}
