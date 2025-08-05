package javax.xml.parsers;

import javax.xml.validation.Schema;
import org.icepdf.core.util.PdfOps;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/* loaded from: rt.jar:javax/xml/parsers/SAXParserFactory.class */
public abstract class SAXParserFactory {
    private boolean validating = false;
    private boolean namespaceAware = false;

    public abstract SAXParser newSAXParser() throws ParserConfigurationException, SAXException;

    public abstract void setFeature(String str, boolean z2) throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException;

    public abstract boolean getFeature(String str) throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException;

    protected SAXParserFactory() {
    }

    public static SAXParserFactory newInstance() {
        return (SAXParserFactory) FactoryFinder.find(SAXParserFactory.class, "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
    }

    public static SAXParserFactory newInstance(String factoryClassName, ClassLoader classLoader) {
        return (SAXParserFactory) FactoryFinder.newInstance(SAXParserFactory.class, factoryClassName, classLoader, false);
    }

    public void setNamespaceAware(boolean awareness) {
        this.namespaceAware = awareness;
    }

    public void setValidating(boolean validating) {
        this.validating = validating;
    }

    public boolean isNamespaceAware() {
        return this.namespaceAware;
    }

    public boolean isValidating() {
        return this.validating;
    }

    public Schema getSchema() {
        throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + PdfOps.DOUBLE_QUOTE__TOKEN);
    }

    public void setSchema(Schema schema) {
        throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + PdfOps.DOUBLE_QUOTE__TOKEN);
    }

    public void setXIncludeAware(boolean state) {
        if (state) {
            throw new UnsupportedOperationException(" setXIncludeAware is not supported on this JAXP implementation or earlier: " + ((Object) getClass()));
        }
    }

    public boolean isXIncludeAware() {
        throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + PdfOps.DOUBLE_QUOTE__TOKEN);
    }
}
