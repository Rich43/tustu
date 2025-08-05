package javax.naming.directory;

import java.io.Serializable;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/directory/Attribute.class */
public interface Attribute extends Cloneable, Serializable {
    public static final long serialVersionUID = 8707690322213556804L;

    NamingEnumeration<?> getAll() throws NamingException;

    Object get() throws NamingException;

    int size();

    String getID();

    boolean contains(Object obj);

    boolean add(Object obj);

    boolean remove(Object obj);

    void clear();

    DirContext getAttributeSyntaxDefinition() throws NamingException;

    DirContext getAttributeDefinition() throws NamingException;

    Object clone();

    boolean isOrdered();

    Object get(int i2) throws NamingException;

    Object remove(int i2);

    void add(int i2, Object obj);

    Object set(int i2, Object obj);
}
