package org.jcp.xml.dsig.internal.dom;

import java.security.InvalidAlgorithmParameterException;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMBase64Transform.class */
public final class DOMBase64Transform extends ApacheTransform {
    @Override // javax.xml.crypto.dsig.TransformService
    public void init(TransformParameterSpec transformParameterSpec) throws InvalidAlgorithmParameterException {
        if (transformParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("params must be null");
        }
    }
}
