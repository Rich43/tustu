package com.sun.xml.internal.ws.api.pipe;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.WSService;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.client.WSServiceDelegate;
import com.sun.xml.internal.ws.client.dispatch.DataSourceDispatch;
import com.sun.xml.internal.ws.client.dispatch.DispatchImpl;
import com.sun.xml.internal.ws.client.dispatch.JAXBDispatch;
import com.sun.xml.internal.ws.client.dispatch.MessageDispatch;
import com.sun.xml.internal.ws.client.dispatch.PacketDispatch;
import com.sun.xml.internal.ws.client.dispatch.SOAPMessageDispatch;
import com.sun.xml.internal.ws.client.sei.SEIStub;
import com.sun.xml.internal.ws.developer.WSBindingProvider;
import com.sun.xml.internal.ws.model.SOAPSEIModel;
import java.lang.reflect.Proxy;
import javax.activation.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/Stubs.class */
public abstract class Stubs {
    private Stubs() {
    }

    @Deprecated
    public static Dispatch<SOAPMessage> createSAAJDispatch(QName portName, WSService owner, WSBinding binding, Service.Mode mode, Tube next, @Nullable WSEndpointReference epr) {
        DispatchImpl.checkValidSOAPMessageDispatch(binding, mode);
        return new SOAPMessageDispatch(portName, mode, (WSServiceDelegate) owner, next, (BindingImpl) binding, epr);
    }

    public static Dispatch<SOAPMessage> createSAAJDispatch(WSPortInfo portInfo, WSBinding binding, Service.Mode mode, @Nullable WSEndpointReference epr) {
        DispatchImpl.checkValidSOAPMessageDispatch(binding, mode);
        return new SOAPMessageDispatch(portInfo, mode, (BindingImpl) binding, epr);
    }

    @Deprecated
    public static Dispatch<DataSource> createDataSourceDispatch(QName portName, WSService owner, WSBinding binding, Service.Mode mode, Tube next, @Nullable WSEndpointReference epr) {
        DispatchImpl.checkValidDataSourceDispatch(binding, mode);
        return new DataSourceDispatch(portName, mode, (WSServiceDelegate) owner, next, (BindingImpl) binding, epr);
    }

    public static Dispatch<DataSource> createDataSourceDispatch(WSPortInfo portInfo, WSBinding binding, Service.Mode mode, @Nullable WSEndpointReference epr) {
        DispatchImpl.checkValidDataSourceDispatch(binding, mode);
        return new DataSourceDispatch(portInfo, mode, (BindingImpl) binding, epr);
    }

    @Deprecated
    public static Dispatch<Source> createSourceDispatch(QName portName, WSService owner, WSBinding binding, Service.Mode mode, Tube next, @Nullable WSEndpointReference epr) {
        return DispatchImpl.createSourceDispatch(portName, mode, (WSServiceDelegate) owner, next, (BindingImpl) binding, epr);
    }

    public static Dispatch<Source> createSourceDispatch(WSPortInfo portInfo, WSBinding binding, Service.Mode mode, @Nullable WSEndpointReference epr) {
        return DispatchImpl.createSourceDispatch(portInfo, mode, (BindingImpl) binding, epr);
    }

    public static <T> Dispatch<T> createDispatch(QName qName, WSService wSService, WSBinding wSBinding, Class<T> cls, Service.Mode mode, Tube tube, @Nullable WSEndpointReference wSEndpointReference) {
        if (cls == SOAPMessage.class) {
            return (Dispatch<T>) createSAAJDispatch(qName, wSService, wSBinding, mode, tube, wSEndpointReference);
        }
        if (cls == Source.class) {
            return (Dispatch<T>) createSourceDispatch(qName, wSService, wSBinding, mode, tube, wSEndpointReference);
        }
        if (cls == DataSource.class) {
            return (Dispatch<T>) createDataSourceDispatch(qName, wSService, wSBinding, mode, tube, wSEndpointReference);
        }
        if (cls == Message.class) {
            if (mode == Service.Mode.MESSAGE) {
                return (Dispatch<T>) createMessageDispatch(qName, wSService, wSBinding, tube, wSEndpointReference);
            }
            throw new WebServiceException(((Object) mode) + " not supported with Dispatch<Message>");
        }
        if (cls == Packet.class) {
            return (Dispatch<T>) createPacketDispatch(qName, wSService, wSBinding, tube, wSEndpointReference);
        }
        throw new WebServiceException("Unknown class type " + cls.getName());
    }

    public static <T> Dispatch<T> createDispatch(WSPortInfo wSPortInfo, WSService wSService, WSBinding wSBinding, Class<T> cls, Service.Mode mode, @Nullable WSEndpointReference wSEndpointReference) {
        if (cls == SOAPMessage.class) {
            return (Dispatch<T>) createSAAJDispatch(wSPortInfo, wSBinding, mode, wSEndpointReference);
        }
        if (cls == Source.class) {
            return (Dispatch<T>) createSourceDispatch(wSPortInfo, wSBinding, mode, wSEndpointReference);
        }
        if (cls == DataSource.class) {
            return (Dispatch<T>) createDataSourceDispatch(wSPortInfo, wSBinding, mode, wSEndpointReference);
        }
        if (cls == Message.class) {
            if (mode == Service.Mode.MESSAGE) {
                return (Dispatch<T>) createMessageDispatch(wSPortInfo, wSBinding, wSEndpointReference);
            }
            throw new WebServiceException(((Object) mode) + " not supported with Dispatch<Message>");
        }
        if (cls == Packet.class) {
            if (mode == Service.Mode.MESSAGE) {
                return (Dispatch<T>) createPacketDispatch(wSPortInfo, wSBinding, wSEndpointReference);
            }
            throw new WebServiceException(((Object) mode) + " not supported with Dispatch<Packet>");
        }
        throw new WebServiceException("Unknown class type " + cls.getName());
    }

    @Deprecated
    public static Dispatch<Object> createJAXBDispatch(QName portName, WSService owner, WSBinding binding, JAXBContext jaxbContext, Service.Mode mode, Tube next, @Nullable WSEndpointReference epr) {
        return new JAXBDispatch(portName, jaxbContext, mode, (WSServiceDelegate) owner, next, (BindingImpl) binding, epr);
    }

    public static Dispatch<Object> createJAXBDispatch(WSPortInfo portInfo, WSBinding binding, JAXBContext jaxbContext, Service.Mode mode, @Nullable WSEndpointReference epr) {
        return new JAXBDispatch(portInfo, jaxbContext, mode, (BindingImpl) binding, epr);
    }

    @Deprecated
    public static Dispatch<Message> createMessageDispatch(QName portName, WSService owner, WSBinding binding, Tube next, @Nullable WSEndpointReference epr) {
        return new MessageDispatch(portName, (WSServiceDelegate) owner, next, (BindingImpl) binding, epr);
    }

    public static Dispatch<Message> createMessageDispatch(WSPortInfo portInfo, WSBinding binding, @Nullable WSEndpointReference epr) {
        return new MessageDispatch(portInfo, (BindingImpl) binding, epr);
    }

    public static Dispatch<Packet> createPacketDispatch(QName portName, WSService owner, WSBinding binding, Tube next, @Nullable WSEndpointReference epr) {
        return new PacketDispatch(portName, (WSServiceDelegate) owner, next, (BindingImpl) binding, epr);
    }

    public static Dispatch<Packet> createPacketDispatch(WSPortInfo portInfo, WSBinding binding, @Nullable WSEndpointReference epr) {
        return new PacketDispatch(portInfo, (BindingImpl) binding, epr);
    }

    public <T> T createPortProxy(WSService service, WSBinding binding, SEIModel model, Class<T> portInterface, Tube next, @Nullable WSEndpointReference epr) {
        SEIStub ps = new SEIStub((WSServiceDelegate) service, (BindingImpl) binding, (SOAPSEIModel) model, next, epr);
        return portInterface.cast(Proxy.newProxyInstance(portInterface.getClassLoader(), new Class[]{portInterface, WSBindingProvider.class}, ps));
    }

    public <T> T createPortProxy(WSPortInfo portInfo, WSBinding binding, SEIModel model, Class<T> portInterface, @Nullable WSEndpointReference epr) {
        SEIStub ps = new SEIStub(portInfo, (BindingImpl) binding, (SOAPSEIModel) model, epr);
        return portInterface.cast(Proxy.newProxyInstance(portInterface.getClassLoader(), new Class[]{portInterface, WSBindingProvider.class}, ps));
    }
}
