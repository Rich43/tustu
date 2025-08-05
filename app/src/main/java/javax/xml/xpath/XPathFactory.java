package javax.xml.xpath;

/* loaded from: rt.jar:javax/xml/xpath/XPathFactory.class */
public abstract class XPathFactory {
    public static final String DEFAULT_PROPERTY_NAME = "javax.xml.xpath.XPathFactory";
    public static final String DEFAULT_OBJECT_MODEL_URI = "http://java.sun.com/jaxp/xpath/dom";
    private static SecuritySupport ss = new SecuritySupport();

    public abstract boolean isObjectModelSupported(String str);

    public abstract void setFeature(String str, boolean z2) throws XPathFactoryConfigurationException;

    public abstract boolean getFeature(String str) throws XPathFactoryConfigurationException;

    public abstract void setXPathVariableResolver(XPathVariableResolver xPathVariableResolver);

    public abstract void setXPathFunctionResolver(XPathFunctionResolver xPathFunctionResolver);

    public abstract XPath newXPath();

    protected XPathFactory() {
    }

    public static XPathFactory newInstance() {
        try {
            return newInstance("http://java.sun.com/jaxp/xpath/dom");
        } catch (XPathFactoryConfigurationException xpathFactoryConfigurationException) {
            throw new RuntimeException("XPathFactory#newInstance() failed to create an XPathFactory for the default object model: http://java.sun.com/jaxp/xpath/dom with the XPathFactoryConfigurationException: " + xpathFactoryConfigurationException.toString());
        }
    }

    public static XPathFactory newInstance(String uri) throws XPathFactoryConfigurationException {
        if (uri == null) {
            throw new NullPointerException("XPathFactory#newInstance(String uri) cannot be called with uri == null");
        }
        if (uri.length() == 0) {
            throw new IllegalArgumentException("XPathFactory#newInstance(String uri) cannot be called with uri == \"\"");
        }
        ClassLoader classLoader = ss.getContextClassLoader();
        if (classLoader == null) {
            classLoader = XPathFactory.class.getClassLoader();
        }
        XPathFactory xpathFactory = new XPathFactoryFinder(classLoader).newFactory(uri);
        if (xpathFactory == null) {
            throw new XPathFactoryConfigurationException("No XPathFactory implementation found for the object model: " + uri);
        }
        return xpathFactory;
    }

    public static XPathFactory newInstance(String uri, String factoryClassName, ClassLoader classLoader) throws XPathFactoryConfigurationException {
        ClassLoader cl = classLoader;
        if (uri == null) {
            throw new NullPointerException("XPathFactory#newInstance(String uri) cannot be called with uri == null");
        }
        if (uri.length() == 0) {
            throw new IllegalArgumentException("XPathFactory#newInstance(String uri) cannot be called with uri == \"\"");
        }
        if (cl == null) {
            cl = ss.getContextClassLoader();
        }
        XPathFactory f2 = new XPathFactoryFinder(cl).createInstance(factoryClassName);
        if (f2 == null) {
            throw new XPathFactoryConfigurationException("No XPathFactory implementation found for the object model: " + uri);
        }
        if (f2.isObjectModelSupported(uri)) {
            return f2;
        }
        throw new XPathFactoryConfigurationException("Factory " + factoryClassName + " doesn't support given " + uri + " object model");
    }
}
