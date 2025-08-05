package javax.xml.crypto.dsig.spec;

import javax.xml.crypto.XMLStructure;

/* loaded from: rt.jar:javax/xml/crypto/dsig/spec/XSLTTransformParameterSpec.class */
public final class XSLTTransformParameterSpec implements TransformParameterSpec {
    private XMLStructure stylesheet;

    public XSLTTransformParameterSpec(XMLStructure xMLStructure) {
        if (xMLStructure == null) {
            throw new NullPointerException();
        }
        this.stylesheet = xMLStructure;
    }

    public XMLStructure getStylesheet() {
        return this.stylesheet;
    }
}
