package javax.xml.namespace;

import java.util.Iterator;

/* loaded from: rt.jar:javax/xml/namespace/NamespaceContext.class */
public interface NamespaceContext {
    String getNamespaceURI(String str);

    String getPrefix(String str);

    Iterator getPrefixes(String str);
}
