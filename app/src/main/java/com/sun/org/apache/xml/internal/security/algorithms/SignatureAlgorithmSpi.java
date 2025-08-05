package com.sun.org.apache.xml.internal.security.algorithms;

import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/algorithms/SignatureAlgorithmSpi.class */
public abstract class SignatureAlgorithmSpi {
    protected abstract String engineGetURI();

    protected abstract String engineGetJCEAlgorithmString();

    protected abstract String engineGetJCEProviderName();

    protected abstract void engineUpdate(byte[] bArr) throws XMLSignatureException;

    protected abstract void engineUpdate(byte b2) throws XMLSignatureException;

    protected abstract void engineUpdate(byte[] bArr, int i2, int i3) throws XMLSignatureException;

    protected abstract void engineInitSign(Key key) throws XMLSignatureException;

    protected abstract void engineInitSign(Key key, SecureRandom secureRandom) throws XMLSignatureException;

    protected abstract void engineInitSign(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws XMLSignatureException;

    protected abstract byte[] engineSign() throws XMLSignatureException;

    protected abstract void engineInitVerify(Key key) throws XMLSignatureException;

    protected abstract boolean engineVerify(byte[] bArr) throws XMLSignatureException;

    protected abstract void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws XMLSignatureException;

    protected abstract void engineSetHMACOutputLength(int i2) throws XMLSignatureException;

    protected void engineGetContextFromElement(Element element) {
    }

    public void reset() {
    }
}
