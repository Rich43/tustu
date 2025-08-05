package com.sun.xml.internal.ws.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import com.sun.xml.internal.ws.resources.ClientMessages;
import com.sun.xml.internal.ws.util.exception.LocatableWebServiceException;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceFeature;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLPortImpl.class */
public final class WSDLPortImpl extends AbstractFeaturedObjectImpl implements EditableWSDLPort {
    private final QName name;
    private EndpointAddress address;
    private final QName bindingName;
    private final EditableWSDLService owner;
    private WSEndpointReference epr;
    private EditableWSDLBoundPortType boundPortType;
    static final /* synthetic */ boolean $assertionsDisabled;

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractFeaturedObjectImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject
    @Nullable
    public /* bridge */ /* synthetic */ WebServiceFeature getFeature(@NotNull Class cls) {
        return super.getFeature(cls);
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractFeaturedObjectImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject
    @NotNull
    public /* bridge */ /* synthetic */ WebServiceFeatureList getFeatures() {
        return super.getFeatures();
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractExtensibleImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public /* bridge */ /* synthetic */ boolean areRequiredExtensionsUnderstood() {
        return super.areRequiredExtensionsUnderstood();
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractExtensibleImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public /* bridge */ /* synthetic */ void addNotUnderstoodExtension(QName qName, Locator locator) {
        super.addNotUnderstoodExtension(qName, locator);
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractExtensibleImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public /* bridge */ /* synthetic */ List getNotUnderstoodExtensions() {
        return super.getNotUnderstoodExtensions();
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractExtensibleImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public /* bridge */ /* synthetic */ void addExtension(WSDLExtension wSDLExtension) {
        super.addExtension(wSDLExtension);
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractExtensibleImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public /* bridge */ /* synthetic */ WSDLExtension getExtension(Class cls) {
        return super.getExtension(cls);
    }

    static {
        $assertionsDisabled = !WSDLPortImpl.class.desiredAssertionStatus();
    }

    public WSDLPortImpl(XMLStreamReader xsr, EditableWSDLService owner, QName name, QName binding) {
        super(xsr);
        this.owner = owner;
        this.name = name;
        this.bindingName = binding;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPort
    public QName getName() {
        return this.name;
    }

    public QName getBindingName() {
        return this.bindingName;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPort
    public EndpointAddress getAddress() {
        return this.address;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort, com.sun.xml.internal.ws.api.model.wsdl.WSDLPort
    public EditableWSDLService getOwner() {
        return this.owner;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort
    public void setAddress(EndpointAddress address) {
        if (!$assertionsDisabled && address == null) {
            throw new AssertionError();
        }
        this.address = address;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort
    public void setEPR(@NotNull WSEndpointReference epr) {
        if (!$assertionsDisabled && epr == null) {
            throw new AssertionError();
        }
        addExtension(epr);
        this.epr = epr;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPort
    @Nullable
    public WSEndpointReference getEPR() {
        return this.epr;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort, com.sun.xml.internal.ws.api.model.wsdl.WSDLPort
    public EditableWSDLBoundPortType getBinding() {
        return this.boundPortType;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort
    public void freeze(EditableWSDLModel root) {
        this.boundPortType = root.getBinding(this.bindingName);
        if (this.boundPortType == null) {
            throw new LocatableWebServiceException(ClientMessages.UNDEFINED_BINDING(this.bindingName), getLocation());
        }
        if (this.features == null) {
            this.features = new WebServiceFeatureList();
        }
        this.features.setParentFeaturedObject(this.boundPortType);
        this.notUnderstoodExtensions.addAll(this.boundPortType.getNotUnderstoodExtensions());
    }
}
