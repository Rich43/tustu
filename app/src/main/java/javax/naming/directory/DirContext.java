package javax.naming.directory;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/directory/DirContext.class */
public interface DirContext extends Context {
    public static final int ADD_ATTRIBUTE = 1;
    public static final int REPLACE_ATTRIBUTE = 2;
    public static final int REMOVE_ATTRIBUTE = 3;

    Attributes getAttributes(Name name) throws NamingException;

    Attributes getAttributes(String str) throws NamingException;

    Attributes getAttributes(Name name, String[] strArr) throws NamingException;

    Attributes getAttributes(String str, String[] strArr) throws NamingException;

    void modifyAttributes(Name name, int i2, Attributes attributes) throws NamingException;

    void modifyAttributes(String str, int i2, Attributes attributes) throws NamingException;

    void modifyAttributes(Name name, ModificationItem[] modificationItemArr) throws NamingException;

    void modifyAttributes(String str, ModificationItem[] modificationItemArr) throws NamingException;

    void bind(Name name, Object obj, Attributes attributes) throws NamingException;

    void bind(String str, Object obj, Attributes attributes) throws NamingException;

    void rebind(Name name, Object obj, Attributes attributes) throws NamingException;

    void rebind(String str, Object obj, Attributes attributes) throws NamingException;

    DirContext createSubcontext(Name name, Attributes attributes) throws NamingException;

    DirContext createSubcontext(String str, Attributes attributes) throws NamingException;

    DirContext getSchema(Name name) throws NamingException;

    DirContext getSchema(String str) throws NamingException;

    DirContext getSchemaClassDefinition(Name name) throws NamingException;

    DirContext getSchemaClassDefinition(String str) throws NamingException;

    NamingEnumeration<SearchResult> search(Name name, Attributes attributes, String[] strArr) throws NamingException;

    NamingEnumeration<SearchResult> search(String str, Attributes attributes, String[] strArr) throws NamingException;

    NamingEnumeration<SearchResult> search(Name name, Attributes attributes) throws NamingException;

    NamingEnumeration<SearchResult> search(String str, Attributes attributes) throws NamingException;

    NamingEnumeration<SearchResult> search(Name name, String str, SearchControls searchControls) throws NamingException;

    NamingEnumeration<SearchResult> search(String str, String str2, SearchControls searchControls) throws NamingException;

    NamingEnumeration<SearchResult> search(Name name, String str, Object[] objArr, SearchControls searchControls) throws NamingException;

    NamingEnumeration<SearchResult> search(String str, String str2, Object[] objArr, SearchControls searchControls) throws NamingException;
}
