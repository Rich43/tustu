package com.oracle.webservices.internal.api.message;

import com.oracle.webservices.internal.api.EnvelopeStyle;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.util.ServiceFinder;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/message/MessageContextFactory.class */
public abstract class MessageContextFactory {
    private static final MessageContextFactory DEFAULT = new com.sun.xml.internal.ws.api.message.MessageContextFactory(new WebServiceFeature[0]);

    @Deprecated
    /* loaded from: rt.jar:com/oracle/webservices/internal/api/message/MessageContextFactory$Creator.class */
    private interface Creator {
        MessageContext create(MessageContextFactory messageContextFactory);
    }

    protected abstract MessageContextFactory newFactory(WebServiceFeature... webServiceFeatureArr);

    public abstract MessageContext createContext();

    public abstract MessageContext createContext(SOAPMessage sOAPMessage);

    public abstract MessageContext createContext(Source source);

    public abstract MessageContext createContext(Source source, EnvelopeStyle.Style style);

    public abstract MessageContext createContext(InputStream inputStream, String str) throws IOException;

    @Deprecated
    public abstract MessageContext createContext(InputStream inputStream, MimeHeaders mimeHeaders) throws IOException;

    @Deprecated
    public abstract MessageContext doCreate();

    @Deprecated
    public abstract MessageContext doCreate(SOAPMessage sOAPMessage);

    @Deprecated
    public abstract MessageContext doCreate(Source source, SOAPVersion sOAPVersion);

    public static MessageContextFactory createFactory(WebServiceFeature... f2) {
        return createFactory(null, f2);
    }

    public static MessageContextFactory createFactory(ClassLoader cl, WebServiceFeature... f2) {
        Iterator it = ServiceFinder.find(MessageContextFactory.class, cl).iterator();
        while (it.hasNext()) {
            MessageContextFactory factory = (MessageContextFactory) it.next();
            MessageContextFactory newfac = factory.newFactory(f2);
            if (newfac != null) {
                return newfac;
            }
        }
        return new com.sun.xml.internal.ws.api.message.MessageContextFactory(f2);
    }

    @Deprecated
    public static MessageContext create(ClassLoader... classLoader) {
        return serviceFinder(classLoader, new Creator() { // from class: com.oracle.webservices.internal.api.message.MessageContextFactory.1
            @Override // com.oracle.webservices.internal.api.message.MessageContextFactory.Creator
            public MessageContext create(MessageContextFactory f2) {
                return f2.doCreate();
            }
        });
    }

    @Deprecated
    public static MessageContext create(final SOAPMessage m2, ClassLoader... classLoader) {
        return serviceFinder(classLoader, new Creator() { // from class: com.oracle.webservices.internal.api.message.MessageContextFactory.2
            @Override // com.oracle.webservices.internal.api.message.MessageContextFactory.Creator
            public MessageContext create(MessageContextFactory f2) {
                return f2.doCreate(m2);
            }
        });
    }

    @Deprecated
    public static MessageContext create(final Source m2, final SOAPVersion v2, ClassLoader... classLoader) {
        return serviceFinder(classLoader, new Creator() { // from class: com.oracle.webservices.internal.api.message.MessageContextFactory.3
            @Override // com.oracle.webservices.internal.api.message.MessageContextFactory.Creator
            public MessageContext create(MessageContextFactory f2) {
                return f2.doCreate(m2, v2);
            }
        });
    }

    @Deprecated
    private static MessageContext serviceFinder(ClassLoader[] classLoader, Creator creator) {
        ClassLoader cl = classLoader.length == 0 ? null : classLoader[0];
        Iterator it = ServiceFinder.find(MessageContextFactory.class, cl).iterator();
        while (it.hasNext()) {
            MessageContextFactory factory = (MessageContextFactory) it.next();
            MessageContext messageContext = creator.create(factory);
            if (messageContext != null) {
                return messageContext;
            }
        }
        return creator.create(DEFAULT);
    }
}
