package com.sun.org.apache.xml.internal.security.keys.content.keyvalues;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import java.security.PublicKey;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/keyvalues/KeyValueContent.class */
public interface KeyValueContent {
    PublicKey getPublicKey() throws XMLSecurityException;
}
