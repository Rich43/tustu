package com.sun.xml.internal.ws.server;

import com.oracle.webservices.internal.api.databinding.WSDLResolver;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
import com.sun.xml.internal.ws.api.server.SDDocument;
import com.sun.xml.internal.ws.api.server.SDDocumentSource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/WSDLGenResolver.class */
final class WSDLGenResolver implements WSDLResolver {
    private final List<SDDocumentImpl> docs;
    private SDDocumentSource concreteWsdlSource;
    private SDDocumentImpl abstractWsdl;
    private SDDocumentImpl concreteWsdl;
    private final QName serviceName;
    private final QName portTypeName;
    private final List<SDDocumentSource> newDocs = new ArrayList();
    private final Map<String, List<SDDocumentImpl>> nsMapping = new HashMap();

    /* JADX WARN: Multi-variable type inference failed */
    public WSDLGenResolver(@NotNull List<SDDocumentImpl> docs, QName serviceName, QName portTypeName) {
        this.docs = docs;
        this.serviceName = serviceName;
        this.portTypeName = portTypeName;
        for (SDDocumentImpl sDDocumentImpl : docs) {
            if (sDDocumentImpl.isWSDL()) {
                SDDocument.WSDL wsdl = (SDDocument.WSDL) sDDocumentImpl;
                if (wsdl.hasPortType()) {
                    this.abstractWsdl = sDDocumentImpl;
                }
            }
            if (sDDocumentImpl.isSchema()) {
                SDDocument.Schema schema = (SDDocument.Schema) sDDocumentImpl;
                List<SDDocumentImpl> sysIds = this.nsMapping.get(schema.getTargetNamespace());
                if (sysIds == null) {
                    sysIds = new ArrayList();
                    this.nsMapping.put(schema.getTargetNamespace(), sysIds);
                }
                sysIds.add(sDDocumentImpl);
            }
        }
    }

    @Override // com.oracle.webservices.internal.api.databinding.WSDLResolver
    public Result getWSDL(String filename) {
        URL url = createURL(filename);
        MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
        xsb.setSystemId(url.toExternalForm());
        this.concreteWsdlSource = SDDocumentSource.create(url, xsb);
        this.newDocs.add(this.concreteWsdlSource);
        XMLStreamBufferResult r2 = new XMLStreamBufferResult(xsb);
        r2.setSystemId(filename);
        return r2;
    }

    private URL createURL(String filename) {
        try {
            return new URL("file:///" + filename);
        } catch (MalformedURLException e2) {
            throw new WebServiceException(e2);
        }
    }

    /* JADX WARN: Type inference failed for: r1v15, types: [T, java.lang.String] */
    @Override // com.oracle.webservices.internal.api.databinding.WSDLResolver
    public Result getAbstractWSDL(Holder<String> filename) {
        if (this.abstractWsdl != null) {
            filename.value = this.abstractWsdl.getURL().toString();
            return null;
        }
        URL url = createURL(filename.value);
        MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
        xsb.setSystemId(url.toExternalForm());
        SDDocumentSource abstractWsdlSource = SDDocumentSource.create(url, xsb);
        this.newDocs.add(abstractWsdlSource);
        XMLStreamBufferResult r2 = new XMLStreamBufferResult(xsb);
        r2.setSystemId(filename.value);
        return r2;
    }

    /* JADX WARN: Type inference failed for: r1v18, types: [T, java.lang.String] */
    @Override // com.oracle.webservices.internal.api.databinding.WSDLResolver
    public Result getSchemaOutput(String namespace, Holder<String> filename) {
        List<SDDocumentImpl> schemas = this.nsMapping.get(namespace);
        if (schemas != null) {
            if (schemas.size() > 1) {
                throw new ServerRtException("server.rt.err", "More than one schema for the target namespace " + namespace);
            }
            filename.value = schemas.get(0).getURL().toExternalForm();
            return null;
        }
        URL url = createURL(filename.value);
        MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
        xsb.setSystemId(url.toExternalForm());
        SDDocumentSource sd = SDDocumentSource.create(url, xsb);
        this.newDocs.add(sd);
        XMLStreamBufferResult r2 = new XMLStreamBufferResult(xsb);
        r2.setSystemId(filename.value);
        return r2;
    }

    public SDDocumentImpl updateDocs() {
        for (SDDocumentSource doc : this.newDocs) {
            SDDocumentImpl docImpl = SDDocumentImpl.create(doc, this.serviceName, this.portTypeName);
            if (doc == this.concreteWsdlSource) {
                this.concreteWsdl = docImpl;
            }
            this.docs.add(docImpl);
        }
        return this.concreteWsdl;
    }
}
