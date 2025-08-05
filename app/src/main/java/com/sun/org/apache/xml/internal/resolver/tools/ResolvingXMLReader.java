package com.sun.org.apache.xml.internal.resolver.tools;

import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import jdk.xml.internal.JdkXmlUtils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/tools/ResolvingXMLReader.class */
public class ResolvingXMLReader extends ResolvingXMLFilter {
    public static boolean namespaceAware = true;
    public static boolean validating = false;

    public ResolvingXMLReader() {
        SAXParserFactory spf = JdkXmlUtils.getSAXFactory(this.catalogManager.overrideDefaultParser());
        spf.setValidating(validating);
        try {
            SAXParser parser = spf.newSAXParser();
            setParent(parser.getXMLReader());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ResolvingXMLReader(CatalogManager manager) {
        super(manager);
        SAXParserFactory spf = JdkXmlUtils.getSAXFactory(this.catalogManager.overrideDefaultParser());
        spf.setValidating(validating);
        try {
            SAXParser parser = spf.newSAXParser();
            setParent(parser.getXMLReader());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
