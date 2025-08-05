package javax.naming.directory;

import java.io.Serializable;
import javax.naming.NamingEnumeration;

/* loaded from: rt.jar:javax/naming/directory/Attributes.class */
public interface Attributes extends Cloneable, Serializable {
    boolean isCaseIgnored();

    int size();

    Attribute get(String str);

    NamingEnumeration<? extends Attribute> getAll();

    NamingEnumeration<String> getIDs();

    Attribute put(String str, Object obj);

    Attribute put(Attribute attribute);

    Attribute remove(String str);

    Object clone();
}
