package com.sun.xml.internal.ws.api.message;

import com.oracle.webservices.internal.api.EnvelopeStyle;
import com.oracle.webservices.internal.api.EnvelopeStyleFeature;
import com.oracle.webservices.internal.api.message.MessageContext;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.Codecs;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import com.sun.xml.internal.ws.transport.http.HttpAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.MTOMFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/MessageContextFactory.class */
public class MessageContextFactory extends com.oracle.webservices.internal.api.message.MessageContextFactory {
    private WSFeatureList features;
    private Codec soapCodec;
    private Codec xmlCodec;
    private EnvelopeStyleFeature envelopeStyle;
    private EnvelopeStyle.Style singleSoapStyle;

    public MessageContextFactory(WebServiceFeature[] wsf) {
        this(new WebServiceFeatureList(wsf));
    }

    public MessageContextFactory(WSFeatureList wsf) {
        this.features = wsf;
        this.envelopeStyle = (EnvelopeStyleFeature) this.features.get(EnvelopeStyleFeature.class);
        if (this.envelopeStyle == null) {
            this.envelopeStyle = new EnvelopeStyleFeature(EnvelopeStyle.Style.SOAP11);
            this.features.mergeFeatures(new WebServiceFeature[]{this.envelopeStyle}, false);
        }
        for (EnvelopeStyle.Style s2 : this.envelopeStyle.getStyles()) {
            if (s2.isXML()) {
                if (this.xmlCodec == null) {
                    this.xmlCodec = Codecs.createXMLCodec(this.features);
                }
            } else {
                if (this.soapCodec == null) {
                    this.soapCodec = Codecs.createSOAPBindingCodec(this.features);
                }
                this.singleSoapStyle = s2;
            }
        }
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContextFactory
    protected com.oracle.webservices.internal.api.message.MessageContextFactory newFactory(WebServiceFeature... f2) {
        return new MessageContextFactory(f2);
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContextFactory
    public MessageContext createContext() {
        return packet(null);
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContextFactory
    public MessageContext createContext(SOAPMessage soap) throws IllegalArgumentException {
        throwIfIllegalMessageArgument(soap);
        return packet(Messages.create(soap));
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContextFactory
    public MessageContext createContext(Source m2, EnvelopeStyle.Style envelopeStyle) throws IllegalArgumentException {
        throwIfIllegalMessageArgument(m2);
        return packet(Messages.create(m2, SOAPVersion.from(envelopeStyle)));
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContextFactory
    public MessageContext createContext(Source m2) throws IllegalArgumentException {
        throwIfIllegalMessageArgument(m2);
        return packet(Messages.create(m2, SOAPVersion.from(this.singleSoapStyle)));
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContextFactory
    public MessageContext createContext(InputStream in, String contentType) throws IOException, IllegalArgumentException {
        throwIfIllegalMessageArgument(in);
        Packet p2 = packet(null);
        this.soapCodec.decode(in, contentType, p2);
        return p2;
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContextFactory
    @Deprecated
    public MessageContext createContext(InputStream in, MimeHeaders headers) throws IOException {
        String contentType = getHeader(headers, "Content-Type");
        Packet packet = (Packet) createContext(in, contentType);
        packet.acceptableMimeTypes = getHeader(headers, XIncludeHandler.HTTP_ACCEPT);
        packet.soapAction = HttpAdapter.fixQuotesAroundSoapAction(getHeader(headers, "SOAPAction"));
        return packet;
    }

    static String getHeader(MimeHeaders headers, String name) {
        String[] values = headers.getHeader(name);
        if (values == null || values.length <= 0) {
            return null;
        }
        return values[0];
    }

    static Map<String, List<String>> toMap(MimeHeaders headers) {
        HashMap<String, List<String>> map = new HashMap<>();
        Iterator<MimeHeader> i2 = headers.getAllHeaders();
        while (i2.hasNext()) {
            MimeHeader mh = i2.next();
            List<String> values = map.get(mh.getName());
            if (values == null) {
                values = new ArrayList<>();
                map.put(mh.getName(), values);
            }
            values.add(mh.getValue());
        }
        return map;
    }

    public MessageContext createContext(Message m2) throws IllegalArgumentException {
        throwIfIllegalMessageArgument(m2);
        return packet(m2);
    }

    private Packet packet(Message m2) {
        Packet p2 = new Packet();
        p2.codec = this.soapCodec;
        if (m2 != null) {
            p2.setMessage(m2);
        }
        MTOMFeature mf = (MTOMFeature) this.features.get(MTOMFeature.class);
        if (mf != null) {
            p2.setMtomFeature(mf);
        }
        return p2;
    }

    private void throwIfIllegalMessageArgument(Object message) throws IllegalArgumentException {
        if (message == null) {
            throw new IllegalArgumentException("null messages are not allowed.  Consider using MessageContextFactory.createContext()");
        }
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContextFactory
    @Deprecated
    public MessageContext doCreate() {
        return packet(null);
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContextFactory
    @Deprecated
    public MessageContext doCreate(SOAPMessage m2) {
        return createContext(m2);
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContextFactory
    @Deprecated
    public MessageContext doCreate(Source x2, SOAPVersion soapVersion) {
        return packet(Messages.create(x2, soapVersion));
    }
}
