package com.sun.xml.internal.ws.util;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import com.sun.xml.internal.ws.api.server.AsyncProvider;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.internal.ws.handler.HandlerChainsModel;
import com.sun.xml.internal.ws.model.ReflectAnnotationReader;
import com.sun.xml.internal.ws.server.EndpointFactory;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.soap.SOAPMessageHandlers;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/HandlerAnnotationProcessor.class */
public class HandlerAnnotationProcessor {
    private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.util");

    public static HandlerAnnotationInfo buildHandlerInfo(@NotNull Class<?> clazz, QName serviceName, QName portName, WSBinding binding) {
        MetadataReader metadataReader = EndpointFactory.getExternalMetadatReader(clazz, binding);
        if (metadataReader == null) {
            metadataReader = new ReflectAnnotationReader();
        }
        HandlerChain handlerChain = (HandlerChain) metadataReader.getAnnotation(HandlerChain.class, clazz);
        if (handlerChain == null) {
            clazz = getSEI(clazz, metadataReader);
            if (clazz != null) {
                handlerChain = (HandlerChain) metadataReader.getAnnotation(HandlerChain.class, clazz);
            }
            if (handlerChain == null) {
                return null;
            }
        }
        if (clazz.getAnnotation(SOAPMessageHandlers.class) != null) {
            throw new UtilException("util.handler.cannot.combine.soapmessagehandlers", new Object[0]);
        }
        InputStream iStream = getFileAsStream(clazz, handlerChain);
        XMLStreamReader reader = XMLStreamReaderFactory.create((String) null, iStream, true);
        XMLStreamReaderUtil.nextElementContent(reader);
        HandlerAnnotationInfo handlerAnnInfo = HandlerChainsModel.parseHandlerFile(reader, clazz.getClassLoader(), serviceName, portName, binding);
        try {
            reader.close();
            iStream.close();
            return handlerAnnInfo;
        } catch (IOException e2) {
            e2.printStackTrace();
            throw new UtilException(e2.getMessage(), new Object[0]);
        } catch (XMLStreamException e3) {
            e3.printStackTrace();
            throw new UtilException(e3.getMessage(), new Object[0]);
        }
    }

    public static HandlerChainsModel buildHandlerChainsModel(Class<?> clazz) {
        HandlerChain handlerChain;
        if (clazz == null || (handlerChain = (HandlerChain) clazz.getAnnotation(HandlerChain.class)) == null) {
            return null;
        }
        InputStream iStream = getFileAsStream(clazz, handlerChain);
        XMLStreamReader reader = XMLStreamReaderFactory.create((String) null, iStream, true);
        XMLStreamReaderUtil.nextElementContent(reader);
        HandlerChainsModel handlerChainsModel = HandlerChainsModel.parseHandlerConfigFile(clazz, reader);
        try {
            reader.close();
            iStream.close();
            return handlerChainsModel;
        } catch (IOException e2) {
            e2.printStackTrace();
            throw new UtilException(e2.getMessage(), new Object[0]);
        } catch (XMLStreamException e3) {
            e3.printStackTrace();
            throw new UtilException(e3.getMessage(), new Object[0]);
        }
    }

    static Class getClass(String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e2) {
            throw new UtilException("util.handler.class.not.found", className);
        }
    }

    static Class getSEI(Class<?> clazz, MetadataReader metadataReader) {
        if (metadataReader == null) {
            metadataReader = new ReflectAnnotationReader();
        }
        if (Provider.class.isAssignableFrom(clazz) || AsyncProvider.class.isAssignableFrom(clazz) || Service.class.isAssignableFrom(clazz)) {
            return null;
        }
        WebService webService = (WebService) metadataReader.getAnnotation(WebService.class, clazz);
        if (webService == null) {
            throw new UtilException("util.handler.no.webservice.annotation", clazz.getCanonicalName());
        }
        String ei = webService.endpointInterface();
        if (ei.length() > 0) {
            Class<?> clazz2 = getClass(webService.endpointInterface());
            WebService ws = (WebService) metadataReader.getAnnotation(WebService.class, clazz2);
            if (ws == null) {
                throw new UtilException("util.handler.endpoint.interface.no.webservice", webService.endpointInterface());
            }
            return clazz2;
        }
        return null;
    }

    static InputStream getFileAsStream(Class clazz, HandlerChain chain) {
        URL url = clazz.getResource(chain.file());
        if (url == null) {
            url = Thread.currentThread().getContextClassLoader().getResource(chain.file());
        }
        if (url == null) {
            String tmp = clazz.getPackage().getName();
            url = Thread.currentThread().getContextClassLoader().getResource(tmp.replace('.', '/') + "/" + chain.file());
        }
        if (url == null) {
            throw new UtilException("util.failed.to.find.handlerchain.file", clazz.getName(), chain.file());
        }
        try {
            return url.openStream();
        } catch (IOException e2) {
            throw new UtilException("util.failed.to.parse.handlerchain.file", clazz.getName(), chain.file());
        }
    }
}
