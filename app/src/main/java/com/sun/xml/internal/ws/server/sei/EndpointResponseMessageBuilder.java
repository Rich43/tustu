package com.sun.xml.internal.ws.server.sei;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Messages;
import com.sun.xml.internal.ws.message.jaxb.JAXBMessage;
import com.sun.xml.internal.ws.model.ParameterImpl;
import com.sun.xml.internal.ws.model.WrapperParameter;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.DatabindingException;
import com.sun.xml.internal.ws.spi.db.PropertyAccessor;
import com.sun.xml.internal.ws.spi.db.WrapperComposite;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointResponseMessageBuilder.class */
public abstract class EndpointResponseMessageBuilder {
    public static final EndpointResponseMessageBuilder EMPTY_SOAP11 = new Empty(SOAPVersion.SOAP_11);
    public static final EndpointResponseMessageBuilder EMPTY_SOAP12 = new Empty(SOAPVersion.SOAP_12);

    public abstract Message createMessage(Object[] objArr, Object obj);

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointResponseMessageBuilder$Empty.class */
    private static final class Empty extends EndpointResponseMessageBuilder {
        private final SOAPVersion soapVersion;

        public Empty(SOAPVersion soapVersion) {
            this.soapVersion = soapVersion;
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointResponseMessageBuilder
        public Message createMessage(Object[] methodArgs, Object returnValue) {
            return Messages.createEmpty(this.soapVersion);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointResponseMessageBuilder$JAXB.class */
    private static abstract class JAXB extends EndpointResponseMessageBuilder {
        private final XMLBridge bridge;
        private final SOAPVersion soapVersion;
        static final /* synthetic */ boolean $assertionsDisabled;

        abstract Object build(Object[] objArr, Object obj);

        static {
            $assertionsDisabled = !EndpointResponseMessageBuilder.class.desiredAssertionStatus();
        }

        protected JAXB(XMLBridge bridge, SOAPVersion soapVersion) {
            if (!$assertionsDisabled && bridge == null) {
                throw new AssertionError();
            }
            this.bridge = bridge;
            this.soapVersion = soapVersion;
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointResponseMessageBuilder
        public final Message createMessage(Object[] methodArgs, Object returnValue) {
            return JAXBMessage.create(this.bridge, build(methodArgs, returnValue), this.soapVersion);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointResponseMessageBuilder$Bare.class */
    public static final class Bare extends JAXB {
        private final int methodPos;
        private final ValueGetter getter;

        public Bare(ParameterImpl p2, SOAPVersion soapVersion) {
            super(p2.getXMLBridge(), soapVersion);
            this.methodPos = p2.getIndex();
            this.getter = ValueGetter.get(p2);
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointResponseMessageBuilder.JAXB
        Object build(Object[] methodArgs, Object returnValue) {
            if (this.methodPos == -1) {
                return returnValue;
            }
            return this.getter.get(methodArgs[this.methodPos]);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointResponseMessageBuilder$Wrapped.class */
    static abstract class Wrapped extends JAXB {
        protected final int[] indices;
        protected final ValueGetter[] getters;
        protected XMLBridge[] parameterBridges;
        protected List<ParameterImpl> children;

        protected Wrapped(WrapperParameter wp, SOAPVersion soapVersion) {
            super(wp.getXMLBridge(), soapVersion);
            this.children = wp.getWrapperChildren();
            this.indices = new int[this.children.size()];
            this.getters = new ValueGetter[this.children.size()];
            for (int i2 = 0; i2 < this.indices.length; i2++) {
                ParameterImpl p2 = this.children.get(i2);
                this.indices[i2] = p2.getIndex();
                this.getters[i2] = ValueGetter.get(p2);
            }
        }

        WrapperComposite buildWrapperComposite(Object[] methodArgs, Object returnValue) {
            Object v2;
            WrapperComposite cs = new WrapperComposite();
            cs.bridges = this.parameterBridges;
            cs.values = new Object[this.parameterBridges.length];
            for (int i2 = this.indices.length - 1; i2 >= 0; i2--) {
                if (this.indices[i2] == -1) {
                    v2 = this.getters[i2].get(returnValue);
                } else {
                    v2 = this.getters[i2].get(methodArgs[this.indices[i2]]);
                }
                if (v2 == null) {
                    throw new WebServiceException("Method Parameter: " + ((Object) this.children.get(i2).getName()) + " cannot be null. This is BP 1.1 R2211 violation.");
                }
                cs.values[i2] = v2;
            }
            return cs;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointResponseMessageBuilder$DocLit.class */
    public static final class DocLit extends Wrapped {
        private final PropertyAccessor[] accessors;
        private final Class wrapper;
        private boolean dynamicWrapper;
        private BindingContext bindingContext;

        public DocLit(WrapperParameter wp, SOAPVersion soapVersion) {
            super(wp, soapVersion);
            this.bindingContext = wp.getOwner().getBindingContext();
            this.wrapper = (Class) wp.getXMLBridge().getTypeInfo().type;
            this.dynamicWrapper = WrapperComposite.class.equals(this.wrapper);
            this.children = wp.getWrapperChildren();
            this.parameterBridges = new XMLBridge[this.children.size()];
            this.accessors = new PropertyAccessor[this.children.size()];
            for (int i2 = 0; i2 < this.accessors.length; i2++) {
                ParameterImpl p2 = this.children.get(i2);
                QName name = p2.getName();
                if (this.dynamicWrapper) {
                    this.parameterBridges[i2] = this.children.get(i2).getInlinedRepeatedElementBridge();
                    if (this.parameterBridges[i2] == null) {
                        this.parameterBridges[i2] = this.children.get(i2).getXMLBridge();
                    }
                } else {
                    try {
                        this.accessors[i2] = this.dynamicWrapper ? null : p2.getOwner().getBindingContext().getElementPropertyAccessor(this.wrapper, name.getNamespaceURI(), name.getLocalPart());
                    } catch (JAXBException e2) {
                        throw new WebServiceException(((Object) this.wrapper) + " do not have a property of the name " + ((Object) name), e2);
                    }
                }
            }
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointResponseMessageBuilder.JAXB
        Object build(Object[] methodArgs, Object returnValue) {
            if (this.dynamicWrapper) {
                return buildWrapperComposite(methodArgs, returnValue);
            }
            try {
                Object bean = this.bindingContext.newWrapperInstace(this.wrapper);
                for (int i2 = this.indices.length - 1; i2 >= 0; i2--) {
                    if (this.indices[i2] == -1) {
                        this.accessors[i2].set(bean, returnValue);
                    } else {
                        this.accessors[i2].set(bean, this.getters[i2].get(methodArgs[this.indices[i2]]));
                    }
                }
                return bean;
            } catch (DatabindingException e2) {
                throw new WebServiceException(e2);
            } catch (IllegalAccessException e3) {
                Error x2 = new IllegalAccessError(e3.getMessage());
                x2.initCause(e3);
                throw x2;
            } catch (InstantiationException e4) {
                Error x3 = new InstantiationError(e4.getMessage());
                x3.initCause(e4);
                throw x3;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointResponseMessageBuilder$RpcLit.class */
    public static final class RpcLit extends Wrapped {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !EndpointResponseMessageBuilder.class.desiredAssertionStatus();
        }

        public RpcLit(WrapperParameter wp, SOAPVersion soapVersion) {
            super(wp, soapVersion);
            if (!$assertionsDisabled && wp.getTypeInfo().type != WrapperComposite.class) {
                throw new AssertionError();
            }
            this.parameterBridges = new XMLBridge[this.children.size()];
            for (int i2 = 0; i2 < this.parameterBridges.length; i2++) {
                this.parameterBridges[i2] = this.children.get(i2).getXMLBridge();
            }
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointResponseMessageBuilder.JAXB
        Object build(Object[] methodArgs, Object returnValue) {
            return buildWrapperComposite(methodArgs, returnValue);
        }
    }
}
