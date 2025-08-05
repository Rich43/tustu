package javax.xml.soap;

import java.util.Iterator;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:javax/xml/soap/Detail.class */
public interface Detail extends SOAPFaultElement {
    DetailEntry addDetailEntry(Name name) throws SOAPException;

    DetailEntry addDetailEntry(QName qName) throws SOAPException;

    Iterator getDetailEntries();
}
