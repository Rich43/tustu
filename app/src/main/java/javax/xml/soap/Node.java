package javax.xml.soap;

/* loaded from: rt.jar:javax/xml/soap/Node.class */
public interface Node extends org.w3c.dom.Node {
    String getValue();

    void setValue(String str);

    void setParentElement(SOAPElement sOAPElement) throws SOAPException;

    SOAPElement getParentElement();

    void detachNode();

    void recycleNode();
}
