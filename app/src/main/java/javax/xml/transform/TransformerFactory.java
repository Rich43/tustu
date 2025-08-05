package javax.xml.transform;

/* loaded from: rt.jar:javax/xml/transform/TransformerFactory.class */
public abstract class TransformerFactory {
    public abstract Transformer newTransformer(Source source) throws TransformerConfigurationException;

    public abstract Transformer newTransformer() throws TransformerConfigurationException;

    public abstract Templates newTemplates(Source source) throws TransformerConfigurationException;

    public abstract Source getAssociatedStylesheet(Source source, String str, String str2, String str3) throws TransformerConfigurationException;

    public abstract void setURIResolver(URIResolver uRIResolver);

    public abstract URIResolver getURIResolver();

    public abstract void setFeature(String str, boolean z2) throws TransformerConfigurationException;

    public abstract boolean getFeature(String str);

    public abstract void setAttribute(String str, Object obj);

    public abstract Object getAttribute(String str);

    public abstract void setErrorListener(ErrorListener errorListener);

    public abstract ErrorListener getErrorListener();

    protected TransformerFactory() {
    }

    public static TransformerFactory newInstance() throws TransformerFactoryConfigurationError {
        return (TransformerFactory) FactoryFinder.find(TransformerFactory.class, "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
    }

    public static TransformerFactory newInstance(String factoryClassName, ClassLoader classLoader) throws TransformerFactoryConfigurationError {
        return (TransformerFactory) FactoryFinder.newInstance(TransformerFactory.class, factoryClassName, classLoader, false);
    }
}
