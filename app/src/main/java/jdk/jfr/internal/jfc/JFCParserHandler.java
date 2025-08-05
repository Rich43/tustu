package jdk.jfr.internal.jfc;

import java.util.LinkedHashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import jdk.internal.org.xml.sax.Attributes;
import jdk.internal.org.xml.sax.SAXException;
import jdk.internal.org.xml.sax.helpers.DefaultHandler;

/* loaded from: jfr.jar:jdk/jfr/internal/jfc/JFCParserHandler.class */
final class JFCParserHandler extends DefaultHandler {
    private static final String ELEMENT_CONFIGURATION = "configuration";
    private static final String ELEMENT_EVENT_TYPE = "event";
    private static final String ELEMENT_SETTING = "setting";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_LABEL = "label";
    private static final String ATTRIBUTE_DESCRIPTION = "description";
    private static final String ATTRIBUTE_PROVIDER = "provider";
    private static final String ATTRIBUTE_VERSION = "version";
    final Map<String, String> settings = new LinkedHashMap();
    private String currentEventPath;
    private String currentSettingsName;
    private StringBuilder currentCharacters;
    String label;
    String provider;
    String description;

    JFCParserHandler() {
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        switch (str3.toLowerCase()) {
            case "configuration":
                String value = attributes.getValue("version");
                if (value == null || !value.startsWith("2.")) {
                    throw new SAXException("This version of Flight Recorder can only read JFC file format version 2.x");
                }
                this.label = attributes.getValue(ATTRIBUTE_LABEL);
                this.description = getOptional(attributes, ATTRIBUTE_DESCRIPTION, "");
                this.provider = getOptional(attributes, ATTRIBUTE_PROVIDER, "");
                break;
                break;
            case "event":
                this.currentEventPath = attributes.getValue("name");
                break;
            case "setting":
                this.currentSettingsName = attributes.getValue("name");
                break;
        }
        this.currentCharacters = null;
    }

    private String getOptional(Attributes attributes, String str, String str2) {
        String value = attributes.getValue(str);
        return value == null ? str2 : value;
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i2, int i3) throws SAXException {
        if (this.currentCharacters == null) {
            this.currentCharacters = new StringBuilder(i3);
        }
        this.currentCharacters.append(cArr, i2, i3);
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) {
        switch (str3.toLowerCase()) {
            case "event":
                this.currentEventPath = null;
                break;
            case "setting":
                this.settings.put(this.currentEventPath + FXMLLoader.CONTROLLER_METHOD_PREFIX + this.currentSettingsName, "" + (this.currentCharacters == null ? "" : this.currentCharacters.toString()));
                this.currentSettingsName = null;
                break;
        }
    }

    public Map<String, String> getSettings() {
        return this.settings;
    }
}
