package javax.xml.transform;

/* loaded from: rt.jar:javax/xml/transform/ErrorListener.class */
public interface ErrorListener {
    void warning(TransformerException transformerException) throws TransformerException;

    void error(TransformerException transformerException) throws TransformerException;

    void fatalError(TransformerException transformerException) throws TransformerException;
}
