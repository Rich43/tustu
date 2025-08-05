package javax.xml.soap;

import java.util.Iterator;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:javax/xml/soap/SOAPHeader.class */
public interface SOAPHeader extends SOAPElement {
    SOAPHeaderElement addHeaderElement(Name name) throws SOAPException;

    SOAPHeaderElement addHeaderElement(QName qName) throws SOAPException;

    Iterator examineMustUnderstandHeaderElements(String str);

    Iterator examineHeaderElements(String str);

    Iterator extractHeaderElements(String str);

    SOAPHeaderElement addNotUnderstoodHeaderElement(QName qName) throws SOAPException;

    SOAPHeaderElement addUpgradeHeaderElement(Iterator it) throws SOAPException;

    SOAPHeaderElement addUpgradeHeaderElement(String[] strArr) throws SOAPException;

    SOAPHeaderElement addUpgradeHeaderElement(String str) throws SOAPException;

    Iterator examineAllHeaderElements();

    Iterator extractAllHeaderElements();
}
