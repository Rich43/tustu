package com.sun.xml.internal.txw2;

import com.sun.xml.internal.txw2.annotation.XmlElement;
import com.sun.xml.internal.txw2.annotation.XmlNamespace;
import com.sun.xml.internal.txw2.output.TXWSerializer;
import com.sun.xml.internal.txw2.output.XmlSerializer;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/TXW.class */
public abstract class TXW {
    private TXW() {
    }

    static QName getTagName(Class<?> c2) {
        Package pkg;
        XmlNamespace xn;
        String localName = "";
        String nsUri = "##default";
        XmlElement xe = (XmlElement) c2.getAnnotation(XmlElement.class);
        if (xe != null) {
            localName = xe.value();
            nsUri = xe.ns();
        }
        if (localName.length() == 0) {
            String localName2 = c2.getName();
            int idx = localName2.lastIndexOf(46);
            if (idx >= 0) {
                localName2 = localName2.substring(idx + 1);
            }
            localName = Character.toLowerCase(localName2.charAt(0)) + localName2.substring(1);
        }
        if (nsUri.equals("##default") && (pkg = c2.getPackage()) != null && (xn = (XmlNamespace) pkg.getAnnotation(XmlNamespace.class)) != null) {
            nsUri = xn.value();
        }
        if (nsUri.equals("##default")) {
            nsUri = "";
        }
        return new QName(nsUri, localName);
    }

    public static <T extends TypedXmlWriter> T create(Class<T> cls, XmlSerializer xmlSerializer) {
        if (xmlSerializer instanceof TXWSerializer) {
            return (T) ((TXWSerializer) xmlSerializer).txw._element(cls);
        }
        Document document = new Document(xmlSerializer);
        QName tagName = getTagName(cls);
        return (T) new ContainerElement(document, null, tagName.getNamespaceURI(), tagName.getLocalPart())._cast(cls);
    }

    public static <T extends TypedXmlWriter> T create(QName qName, Class<T> cls, XmlSerializer xmlSerializer) {
        if (xmlSerializer instanceof TXWSerializer) {
            return (T) ((TXWSerializer) xmlSerializer).txw._element(qName, cls);
        }
        return (T) new ContainerElement(new Document(xmlSerializer), null, qName.getNamespaceURI(), qName.getLocalPart())._cast(cls);
    }
}
