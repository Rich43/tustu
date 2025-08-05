package org.xml.sax;

/* loaded from: rt.jar:org/xml/sax/XMLFilter.class */
public interface XMLFilter extends XMLReader {
    void setParent(XMLReader xMLReader);

    XMLReader getParent();
}
