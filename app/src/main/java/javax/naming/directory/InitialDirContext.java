package javax.naming.directory;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.NotContextException;

/* loaded from: rt.jar:javax/naming/directory/InitialDirContext.class */
public class InitialDirContext extends InitialContext implements DirContext {
    protected InitialDirContext(boolean z2) throws NamingException {
        super(z2);
    }

    public InitialDirContext() throws NamingException {
    }

    public InitialDirContext(Hashtable<?, ?> hashtable) throws NamingException {
        super(hashtable);
    }

    private DirContext getURLOrDefaultInitDirCtx(String str) throws NamingException {
        Context uRLOrDefaultInitCtx = getURLOrDefaultInitCtx(str);
        if (!(uRLOrDefaultInitCtx instanceof DirContext)) {
            if (uRLOrDefaultInitCtx == null) {
                throw new NoInitialContextException();
            }
            throw new NotContextException("Not an instance of DirContext");
        }
        return (DirContext) uRLOrDefaultInitCtx;
    }

    private DirContext getURLOrDefaultInitDirCtx(Name name) throws NamingException {
        Context uRLOrDefaultInitCtx = getURLOrDefaultInitCtx(name);
        if (!(uRLOrDefaultInitCtx instanceof DirContext)) {
            if (uRLOrDefaultInitCtx == null) {
                throw new NoInitialContextException();
            }
            throw new NotContextException("Not an instance of DirContext");
        }
        return (DirContext) uRLOrDefaultInitCtx;
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(String str) throws NamingException {
        return getAttributes(str, (String[]) null);
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(String str, String[] strArr) throws NamingException {
        return getURLOrDefaultInitDirCtx(str).getAttributes(str, strArr);
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(Name name) throws NamingException {
        return getAttributes(name, (String[]) null);
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(Name name, String[] strArr) throws NamingException {
        return getURLOrDefaultInitDirCtx(name).getAttributes(name, strArr);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(String str, int i2, Attributes attributes) throws NamingException {
        getURLOrDefaultInitDirCtx(str).modifyAttributes(str, i2, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(Name name, int i2, Attributes attributes) throws NamingException {
        getURLOrDefaultInitDirCtx(name).modifyAttributes(name, i2, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(String str, ModificationItem[] modificationItemArr) throws NamingException {
        getURLOrDefaultInitDirCtx(str).modifyAttributes(str, modificationItemArr);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(Name name, ModificationItem[] modificationItemArr) throws NamingException {
        getURLOrDefaultInitDirCtx(name).modifyAttributes(name, modificationItemArr);
    }

    @Override // javax.naming.directory.DirContext
    public void bind(String str, Object obj, Attributes attributes) throws NamingException {
        getURLOrDefaultInitDirCtx(str).bind(str, obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void bind(Name name, Object obj, Attributes attributes) throws NamingException {
        getURLOrDefaultInitDirCtx(name).bind(name, obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void rebind(String str, Object obj, Attributes attributes) throws NamingException {
        getURLOrDefaultInitDirCtx(str).rebind(str, obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void rebind(Name name, Object obj, Attributes attributes) throws NamingException {
        getURLOrDefaultInitDirCtx(name).rebind(name, obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext createSubcontext(String str, Attributes attributes) throws NamingException {
        return getURLOrDefaultInitDirCtx(str).createSubcontext(str, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext createSubcontext(Name name, Attributes attributes) throws NamingException {
        return getURLOrDefaultInitDirCtx(name).createSubcontext(name, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchema(String str) throws NamingException {
        return getURLOrDefaultInitDirCtx(str).getSchema(str);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchema(Name name) throws NamingException {
        return getURLOrDefaultInitDirCtx(name).getSchema(name);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(String str) throws NamingException {
        return getURLOrDefaultInitDirCtx(str).getSchemaClassDefinition(str);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(Name name) throws NamingException {
        return getURLOrDefaultInitDirCtx(name).getSchemaClassDefinition(name);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes) throws NamingException {
        return getURLOrDefaultInitDirCtx(str).search(str, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes) throws NamingException {
        return getURLOrDefaultInitDirCtx(name).search(name, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes, String[] strArr) throws NamingException {
        return getURLOrDefaultInitDirCtx(str).search(str, attributes, strArr);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes, String[] strArr) throws NamingException {
        return getURLOrDefaultInitDirCtx(name).search(name, attributes, strArr);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, SearchControls searchControls) throws NamingException {
        return getURLOrDefaultInitDirCtx(str).search(str, str2, searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, SearchControls searchControls) throws NamingException {
        return getURLOrDefaultInitDirCtx(name).search(name, str, searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, Object[] objArr, SearchControls searchControls) throws NamingException {
        return getURLOrDefaultInitDirCtx(str).search(str, str2, objArr, searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, Object[] objArr, SearchControls searchControls) throws NamingException {
        return getURLOrDefaultInitDirCtx(name).search(name, str, objArr, searchControls);
    }
}
