package java.util.prefs;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import sun.security.x509.PolicyMappingsExtension;

/* loaded from: rt.jar:java/util/prefs/XmlSupport.class */
class XmlSupport {
    private static final String PREFS_DTD_URI = "http://java.sun.com/dtd/preferences.dtd";
    private static final String PREFS_DTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- DTD for preferences --><!ELEMENT preferences (root) ><!ATTLIST preferences EXTERNAL_XML_VERSION CDATA \"0.0\"  ><!ELEMENT root (map, node*) ><!ATTLIST root          type (system|user) #REQUIRED ><!ELEMENT node (map, node*) ><!ATTLIST node          name CDATA #REQUIRED ><!ELEMENT map (entry*) ><!ATTLIST map  MAP_XML_VERSION CDATA \"0.0\"  ><!ELEMENT entry EMPTY ><!ATTLIST entry          key CDATA #REQUIRED          value CDATA #REQUIRED >";
    private static final String EXTERNAL_XML_VERSION = "1.0";
    private static final String MAP_XML_VERSION = "1.0";

    XmlSupport() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    static void export(OutputStream outputStream, Preferences preferences, boolean z2) throws DOMException, TransformerFactoryConfigurationError, BackingStoreException, IOException, IllegalArgumentException {
        if (((AbstractPreferences) preferences).isRemoved()) {
            throw new IllegalStateException("Node has been removed");
        }
        Document documentCreatePrefsDoc = createPrefsDoc("preferences");
        Element documentElement = documentCreatePrefsDoc.getDocumentElement();
        documentElement.setAttribute("EXTERNAL_XML_VERSION", "1.0");
        Element element = (Element) documentElement.appendChild(documentCreatePrefsDoc.createElement("root"));
        element.setAttribute("type", preferences.isUserNode() ? "user" : "system");
        ArrayList arrayList = new ArrayList();
        Preferences preferences2 = preferences;
        Preferences preferencesParent = preferences2.parent();
        while (true) {
            Preferences preferences3 = preferencesParent;
            if (preferences3 == null) {
                break;
            }
            arrayList.add(preferences2);
            preferences2 = preferences3;
            preferencesParent = preferences2.parent();
        }
        Element element2 = element;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            element2.appendChild(documentCreatePrefsDoc.createElement(PolicyMappingsExtension.MAP));
            element2 = (Element) element2.appendChild(documentCreatePrefsDoc.createElement("node"));
            element2.setAttribute("name", ((Preferences) arrayList.get(size)).name());
        }
        putPreferencesInXml(element2, documentCreatePrefsDoc, preferences, z2);
        writeDoc(documentCreatePrefsDoc, outputStream);
    }

    private static void putPreferencesInXml(Element element, Document document, Preferences preferences, boolean z2) throws DOMException, BackingStoreException {
        Preferences[] preferencesArr = null;
        String[] strArrChildrenNames = null;
        synchronized (((AbstractPreferences) preferences).lock) {
            if (((AbstractPreferences) preferences).isRemoved()) {
                element.getParentNode().removeChild(element);
                return;
            }
            String[] strArrKeys = preferences.keys();
            Element element2 = (Element) element.appendChild(document.createElement(PolicyMappingsExtension.MAP));
            for (int i2 = 0; i2 < strArrKeys.length; i2++) {
                Element element3 = (Element) element2.appendChild(document.createElement("entry"));
                element3.setAttribute("key", strArrKeys[i2]);
                element3.setAttribute("value", preferences.get(strArrKeys[i2], null));
            }
            if (z2) {
                strArrChildrenNames = preferences.childrenNames();
                preferencesArr = new Preferences[strArrChildrenNames.length];
                for (int i3 = 0; i3 < strArrChildrenNames.length; i3++) {
                    preferencesArr[i3] = preferences.node(strArrChildrenNames[i3]);
                }
            }
            if (z2) {
                for (int i4 = 0; i4 < strArrChildrenNames.length; i4++) {
                    Element element4 = (Element) element.appendChild(document.createElement("node"));
                    element4.setAttribute("name", strArrChildrenNames[i4]);
                    putPreferencesInXml(element4, document, preferencesArr[i4], z2);
                }
            }
        }
    }

    static void importPreferences(InputStream inputStream) throws IOException, InvalidPreferencesFormatException {
        try {
            Document documentLoadPrefsDoc = loadPrefsDoc(inputStream);
            String attribute = documentLoadPrefsDoc.getDocumentElement().getAttribute("EXTERNAL_XML_VERSION");
            if (attribute.compareTo("1.0") > 0) {
                throw new InvalidPreferencesFormatException("Exported preferences file format version " + attribute + " is not supported. This java installation can read versions 1.0 or older. You may need to install a newer version of JDK.");
            }
            Element element = (Element) documentLoadPrefsDoc.getDocumentElement().getChildNodes().item(0);
            ImportSubtree(element.getAttribute("type").equals("user") ? Preferences.userRoot() : Preferences.systemRoot(), element);
        } catch (SAXException e2) {
            throw new InvalidPreferencesFormatException(e2);
        }
    }

    private static Document createPrefsDoc(String str) {
        try {
            DOMImplementation dOMImplementation = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
            return dOMImplementation.createDocument(null, str, dOMImplementation.createDocumentType(str, null, PREFS_DTD_URI));
        } catch (ParserConfigurationException e2) {
            throw new AssertionError(e2);
        }
    }

    private static Document loadPrefsDoc(InputStream inputStream) throws SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactoryNewInstance = DocumentBuilderFactory.newInstance();
        documentBuilderFactoryNewInstance.setIgnoringElementContentWhitespace(true);
        documentBuilderFactoryNewInstance.setValidating(true);
        documentBuilderFactoryNewInstance.setCoalescing(true);
        documentBuilderFactoryNewInstance.setIgnoringComments(true);
        try {
            DocumentBuilder documentBuilderNewDocumentBuilder = documentBuilderFactoryNewInstance.newDocumentBuilder();
            documentBuilderNewDocumentBuilder.setEntityResolver(new Resolver());
            documentBuilderNewDocumentBuilder.setErrorHandler(new EH());
            return documentBuilderNewDocumentBuilder.parse(new InputSource(inputStream));
        } catch (ParserConfigurationException e2) {
            throw new AssertionError(e2);
        }
    }

    private static final void writeDoc(Document document, OutputStream outputStream) throws TransformerFactoryConfigurationError, IOException, IllegalArgumentException {
        try {
            TransformerFactory transformerFactoryNewInstance = TransformerFactory.newInstance();
            try {
                transformerFactoryNewInstance.setAttribute(TransformerFactoryImpl.INDENT_NUMBER, new Integer(2));
            } catch (IllegalArgumentException e2) {
            }
            Transformer transformerNewTransformer = transformerFactoryNewInstance.newTransformer();
            transformerNewTransformer.setOutputProperty("doctype-system", document.getDoctype().getSystemId());
            transformerNewTransformer.setOutputProperty("indent", "yes");
            transformerNewTransformer.transform(new DOMSource(document), new StreamResult(new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"))));
        } catch (TransformerException e3) {
            throw new AssertionError(e3);
        }
    }

    private static void ImportSubtree(Preferences preferences, Element element) {
        NodeList childNodes = element.getChildNodes();
        int length = childNodes.getLength();
        synchronized (((AbstractPreferences) preferences).lock) {
            if (((AbstractPreferences) preferences).isRemoved()) {
                return;
            }
            ImportPrefs(preferences, (Element) childNodes.item(0));
            Preferences[] preferencesArr = new Preferences[length - 1];
            for (int i2 = 1; i2 < length; i2++) {
                preferencesArr[i2 - 1] = preferences.node(((Element) childNodes.item(i2)).getAttribute("name"));
            }
            for (int i3 = 1; i3 < length; i3++) {
                ImportSubtree(preferencesArr[i3 - 1], (Element) childNodes.item(i3));
            }
        }
    }

    private static void ImportPrefs(Preferences preferences, Element element) {
        NodeList childNodes = element.getChildNodes();
        int length = childNodes.getLength();
        for (int i2 = 0; i2 < length; i2++) {
            Element element2 = (Element) childNodes.item(i2);
            preferences.put(element2.getAttribute("key"), element2.getAttribute("value"));
        }
    }

    static void exportMap(OutputStream outputStream, Map<String, String> map) throws DOMException, TransformerFactoryConfigurationError, IOException, IllegalArgumentException {
        Document documentCreatePrefsDoc = createPrefsDoc(PolicyMappingsExtension.MAP);
        Element documentElement = documentCreatePrefsDoc.getDocumentElement();
        documentElement.setAttribute("MAP_XML_VERSION", "1.0");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Element element = (Element) documentElement.appendChild(documentCreatePrefsDoc.createElement("entry"));
            element.setAttribute("key", entry.getKey());
            element.setAttribute("value", entry.getValue());
        }
        writeDoc(documentCreatePrefsDoc, outputStream);
    }

    static void importMap(InputStream inputStream, Map<String, String> map) throws IOException, InvalidPreferencesFormatException {
        try {
            Element documentElement = loadPrefsDoc(inputStream).getDocumentElement();
            String attribute = documentElement.getAttribute("MAP_XML_VERSION");
            if (attribute.compareTo("1.0") > 0) {
                throw new InvalidPreferencesFormatException("Preferences map file format version " + attribute + " is not supported. This java installation can read versions 1.0 or older. You may need to install a newer version of JDK.");
            }
            NodeList childNodes = documentElement.getChildNodes();
            int length = childNodes.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                Element element = (Element) childNodes.item(i2);
                map.put(element.getAttribute("key"), element.getAttribute("value"));
            }
        } catch (SAXException e2) {
            throw new InvalidPreferencesFormatException(e2);
        }
    }

    /* loaded from: rt.jar:java/util/prefs/XmlSupport$Resolver.class */
    private static class Resolver implements EntityResolver {
        private Resolver() {
        }

        @Override // org.xml.sax.EntityResolver
        public InputSource resolveEntity(String str, String str2) throws SAXException {
            if (str2.equals(XmlSupport.PREFS_DTD_URI)) {
                InputSource inputSource = new InputSource(new StringReader(XmlSupport.PREFS_DTD));
                inputSource.setSystemId(XmlSupport.PREFS_DTD_URI);
                return inputSource;
            }
            throw new SAXException("Invalid system identifier: " + str2);
        }
    }

    /* loaded from: rt.jar:java/util/prefs/XmlSupport$EH.class */
    private static class EH implements ErrorHandler {
        private EH() {
        }

        @Override // org.xml.sax.ErrorHandler
        public void error(SAXParseException sAXParseException) throws SAXException {
            throw sAXParseException;
        }

        @Override // org.xml.sax.ErrorHandler
        public void fatalError(SAXParseException sAXParseException) throws SAXException {
            throw sAXParseException;
        }

        @Override // org.xml.sax.ErrorHandler
        public void warning(SAXParseException sAXParseException) throws SAXException {
            throw sAXParseException;
        }
    }
}
