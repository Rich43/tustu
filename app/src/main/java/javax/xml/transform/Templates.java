package javax.xml.transform;

import java.util.Properties;

/* loaded from: rt.jar:javax/xml/transform/Templates.class */
public interface Templates {
    Transformer newTransformer() throws TransformerConfigurationException;

    Properties getOutputProperties();
}
