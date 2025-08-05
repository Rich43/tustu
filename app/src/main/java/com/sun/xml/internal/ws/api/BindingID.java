package com.sun.xml.internal.ws.api;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.binding.SOAPBindingImpl;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import com.sun.xml.internal.ws.developer.JAXWSProperties;
import com.sun.xml.internal.ws.encoding.SOAPBindingCodec;
import com.sun.xml.internal.ws.encoding.XMLHTTPBindingCodec;
import com.sun.xml.internal.ws.util.ServiceFinder;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPBinding;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/BindingID.class */
public abstract class BindingID {
    public static final SOAPHTTPImpl X_SOAP12_HTTP = new SOAPHTTPImpl(SOAPVersion.SOAP_12, SOAPBindingImpl.X_SOAP12HTTP_BINDING, true);
    public static final SOAPHTTPImpl SOAP12_HTTP = new SOAPHTTPImpl(SOAPVersion.SOAP_12, "http://www.w3.org/2003/05/soap/bindings/HTTP/", true);
    public static final SOAPHTTPImpl SOAP11_HTTP = new SOAPHTTPImpl(SOAPVersion.SOAP_11, SOAPBinding.SOAP11HTTP_BINDING, true);
    public static final SOAPHTTPImpl SOAP12_HTTP_MTOM = new SOAPHTTPImpl(SOAPVersion.SOAP_12, SOAPBinding.SOAP12HTTP_MTOM_BINDING, true, true);
    public static final SOAPHTTPImpl SOAP11_HTTP_MTOM = new SOAPHTTPImpl(SOAPVersion.SOAP_11, SOAPBinding.SOAP11HTTP_MTOM_BINDING, true, true);
    public static final BindingID XML_HTTP = new Impl(SOAPVersion.SOAP_11, HTTPBinding.HTTP_BINDING, false) { // from class: com.sun.xml.internal.ws.api.BindingID.1
        @Override // com.sun.xml.internal.ws.api.BindingID
        public Codec createEncoder(WSBinding binding) {
            return new XMLHTTPBindingCodec(binding.getFeatures());
        }
    };
    private static final BindingID REST_HTTP = new Impl(SOAPVersion.SOAP_11, JAXWSProperties.REST_BINDING, true) { // from class: com.sun.xml.internal.ws.api.BindingID.2
        @Override // com.sun.xml.internal.ws.api.BindingID
        public Codec createEncoder(WSBinding binding) {
            return new XMLHTTPBindingCodec(binding.getFeatures());
        }
    };

    public abstract SOAPVersion getSOAPVersion();

    @NotNull
    public abstract Codec createEncoder(@NotNull WSBinding wSBinding);

    public abstract String toString();

    @NotNull
    public final WSBinding createBinding() {
        return BindingImpl.create(this);
    }

    @NotNull
    public String getTransport() {
        return "http://schemas.xmlsoap.org/soap/http";
    }

    @NotNull
    public final WSBinding createBinding(WebServiceFeature... features) {
        return BindingImpl.create(this, features);
    }

    @NotNull
    public final WSBinding createBinding(WSFeatureList features) {
        return createBinding(features.toArray());
    }

    public WebServiceFeatureList createBuiltinFeatureList() {
        return new WebServiceFeatureList();
    }

    public boolean canGenerateWSDL() {
        return false;
    }

    public String getParameter(String parameterName, String defaultValue) {
        return defaultValue;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BindingID)) {
            return false;
        }
        return toString().equals(obj.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    @NotNull
    public static BindingID parse(String lexical) throws WebServiceException {
        if (lexical.equals(XML_HTTP.toString())) {
            return XML_HTTP;
        }
        if (lexical.equals(REST_HTTP.toString())) {
            return REST_HTTP;
        }
        if (belongsTo(lexical, SOAP11_HTTP.toString())) {
            return customize(lexical, SOAP11_HTTP);
        }
        if (belongsTo(lexical, SOAP12_HTTP.toString())) {
            return customize(lexical, SOAP12_HTTP);
        }
        if (belongsTo(lexical, SOAPBindingImpl.X_SOAP12HTTP_BINDING)) {
            return customize(lexical, X_SOAP12_HTTP);
        }
        Iterator it = ServiceFinder.find(BindingIDFactory.class).iterator();
        while (it.hasNext()) {
            BindingIDFactory f2 = (BindingIDFactory) it.next();
            BindingID r2 = f2.parse(lexical);
            if (r2 != null) {
                return r2;
            }
        }
        throw new WebServiceException("Wrong binding ID: " + lexical);
    }

    private static boolean belongsTo(String lexical, String id) {
        return lexical.equals(id) || lexical.startsWith(new StringBuilder().append(id).append('?').toString());
    }

    private static SOAPHTTPImpl customize(String lexical, SOAPHTTPImpl base) {
        if (lexical.equals(base.toString())) {
            return base;
        }
        SOAPHTTPImpl r2 = new SOAPHTTPImpl(base.getSOAPVersion(), lexical, base.canGenerateWSDL());
        try {
            if (lexical.indexOf(63) == -1) {
                return r2;
            }
            String query = URLDecoder.decode(lexical.substring(lexical.indexOf(63) + 1), "UTF-8");
            for (String token : query.split("&")) {
                int idx = token.indexOf(61);
                if (idx < 0) {
                    throw new WebServiceException("Malformed binding ID (no '=' in " + token + ")");
                }
                r2.parameters.put(token.substring(0, idx), token.substring(idx + 1));
            }
            return r2;
        } catch (UnsupportedEncodingException e2) {
            throw new AssertionError(e2);
        }
    }

    @NotNull
    public static BindingID parse(Class<?> implClass) {
        BindingType bindingType = (BindingType) implClass.getAnnotation(BindingType.class);
        if (bindingType != null) {
            String bindingId = bindingType.value();
            if (bindingId.length() > 0) {
                return parse(bindingId);
            }
        }
        return SOAP11_HTTP;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/BindingID$Impl.class */
    private static abstract class Impl extends BindingID {
        final SOAPVersion version;
        private final String lexical;
        private final boolean canGenerateWSDL;

        public Impl(SOAPVersion version, String lexical, boolean canGenerateWSDL) {
            this.version = version;
            this.lexical = lexical;
            this.canGenerateWSDL = canGenerateWSDL;
        }

        @Override // com.sun.xml.internal.ws.api.BindingID
        public SOAPVersion getSOAPVersion() {
            return this.version;
        }

        @Override // com.sun.xml.internal.ws.api.BindingID
        public String toString() {
            return this.lexical;
        }

        @Override // com.sun.xml.internal.ws.api.BindingID
        @Deprecated
        public boolean canGenerateWSDL() {
            return this.canGenerateWSDL;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/BindingID$SOAPHTTPImpl.class */
    public static final class SOAPHTTPImpl extends Impl implements Cloneable {
        Map<String, String> parameters;
        static final String MTOM_PARAM = "mtom";

        public SOAPHTTPImpl(SOAPVersion version, String lexical, boolean canGenerateWSDL) {
            super(version, lexical, canGenerateWSDL);
            this.parameters = new HashMap();
        }

        public SOAPHTTPImpl(SOAPVersion version, String lexical, boolean canGenerateWSDL, boolean mtomEnabled) {
            this(version, lexical, canGenerateWSDL);
            String mtomStr = mtomEnabled ? "true" : "false";
            this.parameters.put(MTOM_PARAM, mtomStr);
        }

        @Override // com.sun.xml.internal.ws.api.BindingID
        @NotNull
        public Codec createEncoder(WSBinding binding) {
            return new SOAPBindingCodec(binding.getFeatures());
        }

        private Boolean isMTOMEnabled() {
            String mtom = this.parameters.get(MTOM_PARAM);
            if (mtom == null) {
                return null;
            }
            return Boolean.valueOf(mtom);
        }

        @Override // com.sun.xml.internal.ws.api.BindingID
        public WebServiceFeatureList createBuiltinFeatureList() {
            WebServiceFeatureList r2 = super.createBuiltinFeatureList();
            Boolean mtom = isMTOMEnabled();
            if (mtom != null) {
                r2.add(new MTOMFeature(mtom.booleanValue()));
            }
            return r2;
        }

        @Override // com.sun.xml.internal.ws.api.BindingID
        public String getParameter(String parameterName, String defaultValue) {
            if (this.parameters.get(parameterName) == null) {
                return super.getParameter(parameterName, defaultValue);
            }
            return this.parameters.get(parameterName);
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public SOAPHTTPImpl m2313clone() throws CloneNotSupportedException {
            return (SOAPHTTPImpl) super.clone();
        }
    }
}
