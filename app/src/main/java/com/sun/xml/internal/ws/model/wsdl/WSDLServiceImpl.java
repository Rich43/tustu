package com.sun.xml.internal.ws.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLServiceImpl.class */
public final class WSDLServiceImpl extends AbstractExtensibleImpl implements EditableWSDLService {
    private final QName name;
    private final Map<QName, EditableWSDLPort> ports;
    private final EditableWSDLModel parent;
    static final /* synthetic */ boolean $assertionsDisabled;

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
        $assertionsDisabled = !WSDLServiceImpl.class.desiredAssertionStatus();
    }

    public WSDLServiceImpl(XMLStreamReader xsr, EditableWSDLModel parent, QName name) {
        super(xsr);
        this.parent = parent;
        this.name = name;
        this.ports = new LinkedHashMap();
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService, com.sun.xml.internal.ws.api.model.wsdl.WSDLService
    @NotNull
    public EditableWSDLModel getParent() {
        return this.parent;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLService
    public QName getName() {
        return this.name;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService, com.sun.xml.internal.ws.api.model.wsdl.WSDLService
    public EditableWSDLPort get(QName portName) {
        return this.ports.get(portName);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService, com.sun.xml.internal.ws.api.model.wsdl.WSDLService
    public EditableWSDLPort getFirstPort() {
        if (this.ports.isEmpty()) {
            return null;
        }
        return this.ports.values().iterator().next();
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService, com.sun.xml.internal.ws.api.model.wsdl.WSDLService
    public Iterable<EditableWSDLPort> getPorts() {
        return this.ports.values();
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService, com.sun.xml.internal.ws.api.model.wsdl.WSDLService
    @Nullable
    public EditableWSDLPort getMatchingPort(QName portTypeName) {
        for (EditableWSDLPort port : getPorts()) {
            QName ptName = port.getBinding().getPortTypeName();
            if (!$assertionsDisabled && ptName == null) {
                throw new AssertionError();
            }
            if (ptName.equals(portTypeName)) {
                return port;
            }
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService
    public void put(QName portName, EditableWSDLPort port) {
        if (portName == null || port == null) {
            throw new NullPointerException();
        }
        this.ports.put(portName, port);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService
    public void freeze(EditableWSDLModel root) {
        for (EditableWSDLPort port : this.ports.values()) {
            port.freeze(root);
        }
    }
}
