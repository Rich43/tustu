package com.sun.xml.internal.ws.model.wsdl;

import com.oracle.webservices.internal.api.message.BasePropertySet;
import com.oracle.webservices.internal.api.message.PropertySet;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.model.SEIModel;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import org.xml.sax.InputSource;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLProperties.class */
public abstract class WSDLProperties extends BasePropertySet {
    private static final BasePropertySet.PropertyMap model = parse(WSDLProperties.class);

    @Nullable
    private final SEIModel seiModel;

    @PropertySet.Property({"javax.xml.ws.wsdl.service"})
    public abstract QName getWSDLService();

    @PropertySet.Property({"javax.xml.ws.wsdl.port"})
    public abstract QName getWSDLPort();

    @PropertySet.Property({MessageContext.WSDL_INTERFACE})
    public abstract QName getWSDLPortType();

    protected WSDLProperties(@Nullable SEIModel seiModel) {
        this.seiModel = seiModel;
    }

    @PropertySet.Property({MessageContext.WSDL_DESCRIPTION})
    public InputSource getWSDLDescription() {
        if (this.seiModel != null) {
            return new InputSource(this.seiModel.getWSDLLocation());
        }
        return null;
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    protected BasePropertySet.PropertyMap getPropertyMap() {
        return model;
    }
}
