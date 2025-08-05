package com.sun.xml.internal.ws.policy.sourcemodel.wspolicy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/wspolicy/NamespaceVersion.class */
public enum NamespaceVersion {
    v1_2("http://schemas.xmlsoap.org/ws/2004/09/policy", "wsp1_2", XmlToken.Policy, XmlToken.ExactlyOne, XmlToken.All, XmlToken.PolicyReference, XmlToken.UsingPolicy, XmlToken.Name, XmlToken.Optional, XmlToken.Ignorable, XmlToken.PolicyUris, XmlToken.Uri, XmlToken.Digest, XmlToken.DigestAlgorithm),
    v1_5("http://www.w3.org/ns/ws-policy", "wsp", XmlToken.Policy, XmlToken.ExactlyOne, XmlToken.All, XmlToken.PolicyReference, XmlToken.UsingPolicy, XmlToken.Name, XmlToken.Optional, XmlToken.Ignorable, XmlToken.PolicyUris, XmlToken.Uri, XmlToken.Digest, XmlToken.DigestAlgorithm);

    private final String nsUri;
    private final String defaultNsPrefix;
    private final Map<XmlToken, QName> tokenToQNameCache;

    public static NamespaceVersion resolveVersion(String uri) {
        for (NamespaceVersion namespaceVersion : values()) {
            if (namespaceVersion.toString().equalsIgnoreCase(uri)) {
                return namespaceVersion;
            }
        }
        return null;
    }

    public static NamespaceVersion resolveVersion(QName name) {
        return resolveVersion(name.getNamespaceURI());
    }

    public static NamespaceVersion getLatestVersion() {
        return v1_5;
    }

    public static XmlToken resolveAsToken(QName name) {
        NamespaceVersion nsVersion = resolveVersion(name);
        if (nsVersion != null) {
            XmlToken token = XmlToken.resolveToken(name.getLocalPart());
            if (nsVersion.tokenToQNameCache.containsKey(token)) {
                return token;
            }
        }
        return XmlToken.UNKNOWN;
    }

    NamespaceVersion(String uri, String prefix, XmlToken... supportedTokens) {
        this.nsUri = uri;
        this.defaultNsPrefix = prefix;
        Map<XmlToken, QName> temp = new HashMap<>();
        for (XmlToken token : supportedTokens) {
            temp.put(token, new QName(this.nsUri, token.toString()));
        }
        this.tokenToQNameCache = Collections.unmodifiableMap(temp);
    }

    public String getDefaultNamespacePrefix() {
        return this.defaultNsPrefix;
    }

    public QName asQName(XmlToken token) throws IllegalArgumentException {
        return this.tokenToQNameCache.get(token);
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.nsUri;
    }
}
