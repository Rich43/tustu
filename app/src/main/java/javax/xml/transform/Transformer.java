package javax.xml.transform;

import java.util.Properties;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/xml/transform/Transformer.class */
public abstract class Transformer {
    public abstract void transform(Source source, Result result) throws TransformerException;

    public abstract void setParameter(String str, Object obj);

    public abstract Object getParameter(String str);

    public abstract void clearParameters();

    public abstract void setURIResolver(URIResolver uRIResolver);

    public abstract URIResolver getURIResolver();

    public abstract void setOutputProperties(Properties properties);

    public abstract Properties getOutputProperties();

    public abstract void setOutputProperty(String str, String str2) throws IllegalArgumentException;

    public abstract String getOutputProperty(String str) throws IllegalArgumentException;

    public abstract void setErrorListener(ErrorListener errorListener) throws IllegalArgumentException;

    public abstract ErrorListener getErrorListener();

    protected Transformer() {
    }

    public void reset() {
        throw new UnsupportedOperationException("This Transformer, \"" + getClass().getName() + "\", does not support the reset functionality.  Specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + PdfOps.DOUBLE_QUOTE__TOKEN);
    }
}
