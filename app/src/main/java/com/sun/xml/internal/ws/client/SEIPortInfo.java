package com.sun.xml.internal.ws.client;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.binding.SOAPBindingImpl;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import com.sun.xml.internal.ws.model.SOAPSEIModel;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/SEIPortInfo.class */
public final class SEIPortInfo extends PortInfo {
    public final Class sei;
    public final SOAPSEIModel model;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SEIPortInfo.class.desiredAssertionStatus();
    }

    public SEIPortInfo(WSServiceDelegate owner, Class sei, SOAPSEIModel model, @NotNull WSDLPort portModel) {
        super(owner, portModel);
        this.sei = sei;
        this.model = model;
        if ($assertionsDisabled) {
            return;
        }
        if (sei == null || model == null) {
            throw new AssertionError();
        }
    }

    @Override // com.sun.xml.internal.ws.client.PortInfo
    public BindingImpl createBinding(WebServiceFeature[] webServiceFeatures, Class<?> portInterface) {
        BindingImpl binding = super.createBinding(webServiceFeatures, portInterface);
        return setKnownHeaders(binding);
    }

    public BindingImpl createBinding(WebServiceFeatureList webServiceFeatures, Class<?> portInterface) {
        BindingImpl binding = super.createBinding(webServiceFeatures, portInterface, null);
        return setKnownHeaders(binding);
    }

    private BindingImpl setKnownHeaders(BindingImpl binding) {
        if (binding instanceof SOAPBindingImpl) {
            ((SOAPBindingImpl) binding).setPortKnownHeaders(this.model.getKnownHeaders());
        }
        return binding;
    }
}
