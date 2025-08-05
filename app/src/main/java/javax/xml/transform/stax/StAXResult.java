package javax.xml.transform.stax;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;

/* loaded from: rt.jar:javax/xml/transform/stax/StAXResult.class */
public class StAXResult implements Result {
    public static final String FEATURE = "http://javax.xml.transform.stax.StAXResult/feature";
    private XMLEventWriter xmlEventWriter;
    private XMLStreamWriter xmlStreamWriter;
    private String systemId;

    public StAXResult(XMLEventWriter xmlEventWriter) {
        this.xmlEventWriter = null;
        this.xmlStreamWriter = null;
        this.systemId = null;
        if (xmlEventWriter == null) {
            throw new IllegalArgumentException("StAXResult(XMLEventWriter) with XMLEventWriter == null");
        }
        this.xmlEventWriter = xmlEventWriter;
    }

    public StAXResult(XMLStreamWriter xmlStreamWriter) {
        this.xmlEventWriter = null;
        this.xmlStreamWriter = null;
        this.systemId = null;
        if (xmlStreamWriter == null) {
            throw new IllegalArgumentException("StAXResult(XMLStreamWriter) with XMLStreamWriter == null");
        }
        this.xmlStreamWriter = xmlStreamWriter;
    }

    public XMLEventWriter getXMLEventWriter() {
        return this.xmlEventWriter;
    }

    public XMLStreamWriter getXMLStreamWriter() {
        return this.xmlStreamWriter;
    }

    @Override // javax.xml.transform.Result
    public void setSystemId(String systemId) {
        throw new UnsupportedOperationException("StAXResult#setSystemId(systemId) cannot set the system identifier for a StAXResult");
    }

    @Override // javax.xml.transform.Result
    public String getSystemId() {
        return null;
    }
}
