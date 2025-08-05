package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.Nullable;
import com.sun.org.glassfish.gmbal.ManagedAttribute;
import com.sun.org.glassfish.gmbal.ManagedData;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

@ManagedData
/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/SDDocument.class */
public interface SDDocument {

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/SDDocument$Schema.class */
    public interface Schema extends SDDocument {
        @ManagedAttribute
        String getTargetNamespace();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/SDDocument$WSDL.class */
    public interface WSDL extends SDDocument {
        @ManagedAttribute
        String getTargetNamespace();

        @ManagedAttribute
        boolean hasPortType();

        @ManagedAttribute
        boolean hasService();

        @ManagedAttribute
        Set<QName> getAllServices();
    }

    @ManagedAttribute
    QName getRootName();

    @ManagedAttribute
    boolean isWSDL();

    @ManagedAttribute
    boolean isSchema();

    @ManagedAttribute
    Set<String> getImports();

    @ManagedAttribute
    URL getURL();

    void writeTo(@Nullable PortAddressResolver portAddressResolver, DocumentAddressResolver documentAddressResolver, OutputStream outputStream) throws IOException;

    void writeTo(PortAddressResolver portAddressResolver, DocumentAddressResolver documentAddressResolver, XMLStreamWriter xMLStreamWriter) throws XMLStreamException, IOException;
}
