package com.sun.xml.internal.ws.policy.sourcemodel.wspolicy;

import com.sun.org.apache.xml.internal.security.utils.Constants;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/wspolicy/XmlToken.class */
public enum XmlToken {
    Policy("Policy", true),
    ExactlyOne("ExactlyOne", true),
    All("All", true),
    PolicyReference("PolicyReference", true),
    UsingPolicy("UsingPolicy", true),
    Name("Name", false),
    Optional("Optional", false),
    Ignorable("Ignorable", false),
    PolicyUris("PolicyURIs", false),
    Uri(Constants._ATT_URI, false),
    Digest("Digest", false),
    DigestAlgorithm("DigestAlgorithm", false),
    UNKNOWN("", true);

    private String tokenName;
    private boolean element;

    public static XmlToken resolveToken(String name) {
        for (XmlToken token : values()) {
            if (token.toString().equals(name)) {
                return token;
            }
        }
        return UNKNOWN;
    }

    XmlToken(String name, boolean element) {
        this.tokenName = name;
        this.element = element;
    }

    public boolean isElement() {
        return this.element;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.tokenName;
    }
}
