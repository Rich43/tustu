package javax.naming.spi;

import java.util.Hashtable;
import javax.naming.CannotProceedException;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/* loaded from: rt.jar:javax/naming/spi/ContinuationDirContext.class */
class ContinuationDirContext extends ContinuationContext implements DirContext {
    ContinuationDirContext(CannotProceedException cannotProceedException, Hashtable<?, ?> hashtable) {
        super(cannotProceedException, hashtable);
    }

    protected DirContextNamePair getTargetContext(Name name) throws NamingException {
        if (this.cpe.getResolvedObj() == null) {
            throw ((NamingException) this.cpe.fillInStackTrace());
        }
        Context context = NamingManager.getContext(this.cpe.getResolvedObj(), this.cpe.getAltName(), this.cpe.getAltNameCtx(), this.env);
        if (context == null) {
            throw ((NamingException) this.cpe.fillInStackTrace());
        }
        if (context instanceof DirContext) {
            return new DirContextNamePair((DirContext) context, name);
        }
        if (context instanceof Resolver) {
            ResolveResult resolveResultResolveToClass = ((Resolver) context).resolveToClass(name, DirContext.class);
            return new DirContextNamePair((DirContext) resolveResultResolveToClass.getResolvedObj(), resolveResultResolveToClass.getRemainingName());
        }
        Object objLookup = context.lookup(name);
        if (objLookup instanceof DirContext) {
            return new DirContextNamePair((DirContext) objLookup, new CompositeName());
        }
        throw ((NamingException) this.cpe.fillInStackTrace());
    }

    protected DirContextStringPair getTargetContext(String str) throws NamingException {
        if (this.cpe.getResolvedObj() == null) {
            throw ((NamingException) this.cpe.fillInStackTrace());
        }
        Context context = NamingManager.getContext(this.cpe.getResolvedObj(), this.cpe.getAltName(), this.cpe.getAltNameCtx(), this.env);
        if (context instanceof DirContext) {
            return new DirContextStringPair((DirContext) context, str);
        }
        if (context instanceof Resolver) {
            ResolveResult resolveResultResolveToClass = ((Resolver) context).resolveToClass(str, DirContext.class);
            DirContext dirContext = (DirContext) resolveResultResolveToClass.getResolvedObj();
            Name remainingName = resolveResultResolveToClass.getRemainingName();
            return new DirContextStringPair(dirContext, remainingName != null ? remainingName.toString() : "");
        }
        Object objLookup = context.lookup(str);
        if (objLookup instanceof DirContext) {
            return new DirContextStringPair((DirContext) objLookup, "");
        }
        throw ((NamingException) this.cpe.fillInStackTrace());
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(String str) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        return targetContext.getDirContext().getAttributes(targetContext.getString());
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(String str, String[] strArr) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        return targetContext.getDirContext().getAttributes(targetContext.getString(), strArr);
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(Name name) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        return targetContext.getDirContext().getAttributes(targetContext.getName());
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(Name name, String[] strArr) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        return targetContext.getDirContext().getAttributes(targetContext.getName(), strArr);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(Name name, int i2, Attributes attributes) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        targetContext.getDirContext().modifyAttributes(targetContext.getName(), i2, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(String str, int i2, Attributes attributes) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        targetContext.getDirContext().modifyAttributes(targetContext.getString(), i2, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(Name name, ModificationItem[] modificationItemArr) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        targetContext.getDirContext().modifyAttributes(targetContext.getName(), modificationItemArr);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(String str, ModificationItem[] modificationItemArr) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        targetContext.getDirContext().modifyAttributes(targetContext.getString(), modificationItemArr);
    }

    @Override // javax.naming.directory.DirContext
    public void bind(Name name, Object obj, Attributes attributes) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        targetContext.getDirContext().bind(targetContext.getName(), obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void bind(String str, Object obj, Attributes attributes) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        targetContext.getDirContext().bind(targetContext.getString(), obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void rebind(Name name, Object obj, Attributes attributes) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        targetContext.getDirContext().rebind(targetContext.getName(), obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void rebind(String str, Object obj, Attributes attributes) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        targetContext.getDirContext().rebind(targetContext.getString(), obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext createSubcontext(Name name, Attributes attributes) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        return targetContext.getDirContext().createSubcontext(targetContext.getName(), attributes);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext createSubcontext(String str, Attributes attributes) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        return targetContext.getDirContext().createSubcontext(targetContext.getString(), attributes);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes, String[] strArr) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        return targetContext.getDirContext().search(targetContext.getName(), attributes, strArr);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes, String[] strArr) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        return targetContext.getDirContext().search(targetContext.getString(), attributes, strArr);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        return targetContext.getDirContext().search(targetContext.getName(), attributes);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        return targetContext.getDirContext().search(targetContext.getString(), attributes);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, SearchControls searchControls) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        return targetContext.getDirContext().search(targetContext.getName(), str, searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, SearchControls searchControls) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        return targetContext.getDirContext().search(targetContext.getString(), str2, searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, Object[] objArr, SearchControls searchControls) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        return targetContext.getDirContext().search(targetContext.getName(), str, objArr, searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, Object[] objArr, SearchControls searchControls) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        return targetContext.getDirContext().search(targetContext.getString(), str2, objArr, searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchema(String str) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        return targetContext.getDirContext().getSchema(targetContext.getString());
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchema(Name name) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        return targetContext.getDirContext().getSchema(targetContext.getName());
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(String str) throws NamingException {
        DirContextStringPair targetContext = getTargetContext(str);
        return targetContext.getDirContext().getSchemaClassDefinition(targetContext.getString());
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(Name name) throws NamingException {
        DirContextNamePair targetContext = getTargetContext(name);
        return targetContext.getDirContext().getSchemaClassDefinition(targetContext.getName());
    }
}
