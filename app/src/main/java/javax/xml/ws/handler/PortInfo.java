package javax.xml.ws.handler;

import javax.xml.namespace.QName;

/* loaded from: rt.jar:javax/xml/ws/handler/PortInfo.class */
public interface PortInfo {
    QName getServiceName();

    QName getPortName();

    String getBindingID();
}
