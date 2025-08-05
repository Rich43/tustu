package com.sun.jndi.toolkit.dir;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.Attribute;
import javax.naming.directory.AttributeModificationException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SchemaViolationException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.spi.DirStateFactory;
import javax.naming.spi.DirectoryManager;

/* loaded from: rt.jar:com/sun/jndi/toolkit/dir/HierMemDirCtx.class */
public class HierMemDirCtx implements DirContext {
    private static final boolean debug = false;
    private static final NameParser defaultParser = new HierarchicalNameParser();
    protected Hashtable<String, Object> myEnv;
    protected Hashtable<Name, Object> bindings;
    protected Attributes attrs;
    protected boolean ignoreCase;
    protected NamingException readOnlyEx;
    protected NameParser myParser;
    private boolean alwaysUseFactory;

    @Override // javax.naming.Context
    public void close() throws NamingException {
        this.myEnv = null;
        this.bindings = null;
        this.attrs = null;
    }

    @Override // javax.naming.Context
    public String getNameInNamespace() throws NamingException {
        throw new OperationNotSupportedException("Cannot determine full name");
    }

    public HierMemDirCtx() {
        this(null, false, false);
    }

    public HierMemDirCtx(boolean z2) {
        this(null, z2, false);
    }

    public HierMemDirCtx(Hashtable<String, Object> hashtable, boolean z2) {
        this(hashtable, z2, false);
    }

    protected HierMemDirCtx(Hashtable<String, Object> hashtable, boolean z2, boolean z3) {
        this.ignoreCase = false;
        this.readOnlyEx = null;
        this.myParser = defaultParser;
        this.myEnv = hashtable;
        this.ignoreCase = z2;
        init();
        this.alwaysUseFactory = z3;
    }

    private void init() {
        this.attrs = new BasicAttributes(this.ignoreCase);
        this.bindings = new Hashtable<>(11, 0.75f);
    }

    @Override // javax.naming.Context
    public Object lookup(String str) throws NamingException {
        return lookup(this.myParser.parse(str));
    }

    @Override // javax.naming.Context
    public Object lookup(Name name) throws NamingException {
        return doLookup(name, this.alwaysUseFactory);
    }

    public Object doLookup(Name name, boolean z2) throws NamingException {
        Object objDoLookup;
        Name nameCanonizeName = canonizeName(name);
        switch (nameCanonizeName.size()) {
            case 0:
                objDoLookup = this;
                break;
            case 1:
                objDoLookup = this.bindings.get(nameCanonizeName);
                break;
            default:
                HierMemDirCtx hierMemDirCtx = (HierMemDirCtx) this.bindings.get(nameCanonizeName.getPrefix(1));
                if (hierMemDirCtx == null) {
                    objDoLookup = null;
                    break;
                } else {
                    objDoLookup = hierMemDirCtx.doLookup(nameCanonizeName.getSuffix(1), false);
                    break;
                }
        }
        if (objDoLookup == null) {
            throw new NameNotFoundException(nameCanonizeName.toString());
        }
        if (z2) {
            try {
                return DirectoryManager.getObjectInstance(objDoLookup, nameCanonizeName, this, this.myEnv, objDoLookup instanceof HierMemDirCtx ? ((HierMemDirCtx) objDoLookup).attrs : null);
            } catch (NamingException e2) {
                throw e2;
            } catch (Exception e3) {
                NamingException namingException = new NamingException("Problem calling getObjectInstance");
                namingException.setRootCause(e3);
                throw namingException;
            }
        }
        return objDoLookup;
    }

    @Override // javax.naming.Context
    public void bind(String str, Object obj) throws NamingException {
        bind(this.myParser.parse(str), obj);
    }

    @Override // javax.naming.Context
    public void bind(Name name, Object obj) throws NamingException {
        doBind(name, obj, null, this.alwaysUseFactory);
    }

    @Override // javax.naming.directory.DirContext
    public void bind(String str, Object obj, Attributes attributes) throws NamingException {
        bind(this.myParser.parse(str), obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void bind(Name name, Object obj, Attributes attributes) throws NamingException {
        doBind(name, obj, attributes, this.alwaysUseFactory);
    }

    protected void doBind(Name name, Object obj, Attributes attributes, boolean z2) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("Cannot bind empty name");
        }
        if (z2) {
            DirStateFactory.Result stateToBind = DirectoryManager.getStateToBind(obj, name, this, this.myEnv, attributes);
            obj = stateToBind.getObject();
            attributes = stateToBind.getAttributes();
        }
        ((HierMemDirCtx) doLookup(getInternalName(name), false)).doBindAux(getLeafName(name), obj);
        if (attributes != null && attributes.size() > 0) {
            modifyAttributes(name, 1, attributes);
        }
    }

    protected void doBindAux(Name name, Object obj) throws NamingException {
        if (this.readOnlyEx != null) {
            throw ((NamingException) this.readOnlyEx.fillInStackTrace());
        }
        if (this.bindings.get(name) != null) {
            throw new NameAlreadyBoundException(name.toString());
        }
        if (obj instanceof HierMemDirCtx) {
            this.bindings.put(name, obj);
            return;
        }
        throw new SchemaViolationException("This context only supports binding objects of it's own kind");
    }

    @Override // javax.naming.Context
    public void rebind(String str, Object obj) throws NamingException {
        rebind(this.myParser.parse(str), obj);
    }

    @Override // javax.naming.Context
    public void rebind(Name name, Object obj) throws NamingException {
        doRebind(name, obj, null, this.alwaysUseFactory);
    }

    @Override // javax.naming.directory.DirContext
    public void rebind(String str, Object obj, Attributes attributes) throws NamingException {
        rebind(this.myParser.parse(str), obj, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void rebind(Name name, Object obj, Attributes attributes) throws NamingException {
        doRebind(name, obj, attributes, this.alwaysUseFactory);
    }

    protected void doRebind(Name name, Object obj, Attributes attributes, boolean z2) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("Cannot rebind empty name");
        }
        if (z2) {
            DirStateFactory.Result stateToBind = DirectoryManager.getStateToBind(obj, name, this, this.myEnv, attributes);
            obj = stateToBind.getObject();
            attributes = stateToBind.getAttributes();
        }
        ((HierMemDirCtx) doLookup(getInternalName(name), false)).doRebindAux(getLeafName(name), obj);
        if (attributes != null && attributes.size() > 0) {
            modifyAttributes(name, 1, attributes);
        }
    }

    protected void doRebindAux(Name name, Object obj) throws NamingException {
        if (this.readOnlyEx != null) {
            throw ((NamingException) this.readOnlyEx.fillInStackTrace());
        }
        if (obj instanceof HierMemDirCtx) {
            this.bindings.put(name, obj);
            return;
        }
        throw new SchemaViolationException("This context only supports binding objects of it's own kind");
    }

    @Override // javax.naming.Context
    public void unbind(String str) throws NamingException {
        unbind(this.myParser.parse(str));
    }

    @Override // javax.naming.Context
    public void unbind(Name name) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("Cannot unbind empty name");
        }
        ((HierMemDirCtx) doLookup(getInternalName(name), false)).doUnbind(getLeafName(name));
    }

    protected void doUnbind(Name name) throws NamingException {
        if (this.readOnlyEx != null) {
            throw ((NamingException) this.readOnlyEx.fillInStackTrace());
        }
        this.bindings.remove(name);
    }

    @Override // javax.naming.Context
    public void rename(String str, String str2) throws NamingException {
        rename(this.myParser.parse(str), this.myParser.parse(str2));
    }

    @Override // javax.naming.Context
    public void rename(Name name, Name name2) throws NamingException {
        if (name2.isEmpty() || name.isEmpty()) {
            throw new InvalidNameException("Cannot rename empty name");
        }
        if (!getInternalName(name2).equals(getInternalName(name))) {
            throw new InvalidNameException("Cannot rename across contexts");
        }
        ((HierMemDirCtx) doLookup(getInternalName(name2), false)).doRename(getLeafName(name), getLeafName(name2));
    }

    protected void doRename(Name name, Name name2) throws NamingException {
        if (this.readOnlyEx != null) {
            throw ((NamingException) this.readOnlyEx.fillInStackTrace());
        }
        Name nameCanonizeName = canonizeName(name);
        Name nameCanonizeName2 = canonizeName(name2);
        if (this.bindings.get(nameCanonizeName2) != null) {
            throw new NameAlreadyBoundException(nameCanonizeName2.toString());
        }
        Object objRemove = this.bindings.remove(nameCanonizeName);
        if (objRemove == null) {
            throw new NameNotFoundException(nameCanonizeName.toString());
        }
        this.bindings.put(nameCanonizeName2, objRemove);
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(String str) throws NamingException {
        return list(this.myParser.parse(str));
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        return ((HierMemDirCtx) doLookup(name, false)).doList();
    }

    protected NamingEnumeration<NameClassPair> doList() throws NamingException {
        return new FlatNames(this.bindings.keys());
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(String str) throws NamingException {
        return listBindings(this.myParser.parse(str));
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        return ((HierMemDirCtx) doLookup(name, false)).doListBindings(this.alwaysUseFactory);
    }

    protected NamingEnumeration<Binding> doListBindings(boolean z2) throws NamingException {
        return new FlatBindings(this.bindings, this.myEnv, z2);
    }

    @Override // javax.naming.Context
    public void destroySubcontext(String str) throws NamingException {
        destroySubcontext(this.myParser.parse(str));
    }

    @Override // javax.naming.Context
    public void destroySubcontext(Name name) throws NamingException {
        ((HierMemDirCtx) doLookup(getInternalName(name), false)).doDestroySubcontext(getLeafName(name));
    }

    protected void doDestroySubcontext(Name name) throws NamingException {
        if (this.readOnlyEx != null) {
            throw ((NamingException) this.readOnlyEx.fillInStackTrace());
        }
        this.bindings.remove(canonizeName(name));
    }

    @Override // javax.naming.Context
    public Context createSubcontext(String str) throws NamingException {
        return createSubcontext(this.myParser.parse(str));
    }

    @Override // javax.naming.Context
    public Context createSubcontext(Name name) throws NamingException {
        return createSubcontext(name, (Attributes) null);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext createSubcontext(String str, Attributes attributes) throws NamingException {
        return createSubcontext(this.myParser.parse(str), attributes);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext createSubcontext(Name name, Attributes attributes) throws NamingException {
        return ((HierMemDirCtx) doLookup(getInternalName(name), false)).doCreateSubcontext(getLeafName(name), attributes);
    }

    protected DirContext doCreateSubcontext(Name name, Attributes attributes) throws NamingException {
        if (this.readOnlyEx != null) {
            throw ((NamingException) this.readOnlyEx.fillInStackTrace());
        }
        Name nameCanonizeName = canonizeName(name);
        if (this.bindings.get(nameCanonizeName) != null) {
            throw new NameAlreadyBoundException(nameCanonizeName.toString());
        }
        HierMemDirCtx hierMemDirCtxCreateNewCtx = createNewCtx();
        this.bindings.put(nameCanonizeName, hierMemDirCtxCreateNewCtx);
        if (attributes != null) {
            hierMemDirCtxCreateNewCtx.modifyAttributes("", 1, attributes);
        }
        return hierMemDirCtxCreateNewCtx;
    }

    @Override // javax.naming.Context
    public Object lookupLink(String str) throws NamingException {
        return lookupLink(this.myParser.parse(str));
    }

    @Override // javax.naming.Context
    public Object lookupLink(Name name) throws NamingException {
        return lookup(name);
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(String str) throws NamingException {
        return this.myParser;
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(Name name) throws NamingException {
        return this.myParser;
    }

    @Override // javax.naming.Context
    public String composeName(String str, String str2) throws NamingException {
        return composeName(new CompositeName(str), new CompositeName(str2)).toString();
    }

    @Override // javax.naming.Context
    public Name composeName(Name name, Name name2) throws NamingException {
        Name nameCanonizeName = canonizeName(name);
        Name name3 = (Name) canonizeName(name2).clone();
        name3.addAll(nameCanonizeName);
        return name3;
    }

    @Override // javax.naming.Context
    public Object addToEnvironment(String str, Object obj) throws NamingException {
        this.myEnv = this.myEnv == null ? new Hashtable<>(11, 0.75f) : (Hashtable) this.myEnv.clone();
        return this.myEnv.put(str, obj);
    }

    @Override // javax.naming.Context
    public Object removeFromEnvironment(String str) throws NamingException {
        if (this.myEnv == null) {
            return null;
        }
        this.myEnv = (Hashtable) this.myEnv.clone();
        return this.myEnv.remove(str);
    }

    @Override // javax.naming.Context
    public Hashtable<String, Object> getEnvironment() throws NamingException {
        if (this.myEnv == null) {
            return new Hashtable<>(5, 0.75f);
        }
        return (Hashtable) this.myEnv.clone();
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(String str) throws NamingException {
        return getAttributes(this.myParser.parse(str));
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(Name name) throws NamingException {
        return ((HierMemDirCtx) doLookup(name, false)).doGetAttributes();
    }

    protected Attributes doGetAttributes() throws NamingException {
        return (Attributes) this.attrs.clone();
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(String str, String[] strArr) throws NamingException {
        return getAttributes(this.myParser.parse(str), strArr);
    }

    @Override // javax.naming.directory.DirContext
    public Attributes getAttributes(Name name, String[] strArr) throws NamingException {
        return ((HierMemDirCtx) doLookup(name, false)).doGetAttributes(strArr);
    }

    protected Attributes doGetAttributes(String[] strArr) throws NamingException {
        if (strArr == null) {
            return doGetAttributes();
        }
        BasicAttributes basicAttributes = new BasicAttributes(this.ignoreCase);
        for (String str : strArr) {
            Attribute attribute = this.attrs.get(str);
            if (attribute != null) {
                basicAttributes.put(attribute);
            }
        }
        return basicAttributes;
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(String str, int i2, Attributes attributes) throws NamingException {
        modifyAttributes(this.myParser.parse(str), i2, attributes);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(Name name, int i2, Attributes attributes) throws NamingException {
        if (attributes == null || attributes.size() == 0) {
            throw new IllegalArgumentException("Cannot modify without an attribute");
        }
        NamingEnumeration<? extends Attribute> all = attributes.getAll();
        ModificationItem[] modificationItemArr = new ModificationItem[attributes.size()];
        for (int i3 = 0; i3 < modificationItemArr.length && all.hasMoreElements(); i3++) {
            modificationItemArr[i3] = new ModificationItem(i2, all.next());
        }
        modifyAttributes(name, modificationItemArr);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(String str, ModificationItem[] modificationItemArr) throws NamingException {
        modifyAttributes(this.myParser.parse(str), modificationItemArr);
    }

    @Override // javax.naming.directory.DirContext
    public void modifyAttributes(Name name, ModificationItem[] modificationItemArr) throws NamingException {
        ((HierMemDirCtx) doLookup(name, false)).doModifyAttributes(modificationItemArr);
    }

    protected void doModifyAttributes(ModificationItem[] modificationItemArr) throws NamingException {
        if (this.readOnlyEx != null) {
            throw ((NamingException) this.readOnlyEx.fillInStackTrace());
        }
        applyMods(modificationItemArr, this.attrs);
    }

    protected static Attributes applyMods(ModificationItem[] modificationItemArr, Attributes attributes) throws NamingException {
        for (ModificationItem modificationItem : modificationItemArr) {
            Attribute attribute = modificationItem.getAttribute();
            switch (modificationItem.getModificationOp()) {
                case 1:
                    Attribute attribute2 = attributes.get(attribute.getID());
                    if (attribute2 == null) {
                        attributes.put((Attribute) attribute.clone());
                        break;
                    } else {
                        NamingEnumeration<?> all = attribute.getAll();
                        while (all.hasMore()) {
                            attribute2.add(all.next());
                        }
                        break;
                    }
                case 2:
                    if (attribute.size() == 0) {
                        attributes.remove(attribute.getID());
                        break;
                    } else {
                        attributes.put((Attribute) attribute.clone());
                        break;
                    }
                case 3:
                    Attribute attribute3 = attributes.get(attribute.getID());
                    if (attribute3 == null) {
                        break;
                    } else if (attribute.size() == 0) {
                        attributes.remove(attribute.getID());
                        break;
                    } else {
                        NamingEnumeration<?> all2 = attribute.getAll();
                        while (all2.hasMore()) {
                            attribute3.remove(all2.next());
                        }
                        if (attribute3.size() == 0) {
                            attributes.remove(attribute.getID());
                            break;
                        } else {
                            break;
                        }
                    }
                default:
                    throw new AttributeModificationException("Unknown mod_op");
            }
        }
        return attributes;
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes) throws NamingException {
        return search(str, attributes, (String[]) null);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes) throws NamingException {
        return search(name, attributes, (String[]) null);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, Attributes attributes, String[] strArr) throws NamingException {
        return search(this.myParser.parse(str), attributes, strArr);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, Attributes attributes, String[] strArr) throws NamingException {
        HierMemDirCtx hierMemDirCtx = (HierMemDirCtx) doLookup(name, false);
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(strArr);
        return new LazySearchEnumerationImpl(hierMemDirCtx.doListBindings(false), new ContainmentFilter(attributes), searchControls, this, this.myEnv, false);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, SearchControls searchControls) throws NamingException {
        return new LazySearchEnumerationImpl(new HierContextEnumerator((DirContext) doLookup(name, false), searchControls != null ? searchControls.getSearchScope() : 1), new SearchFilter(str), searchControls, this, this.myEnv, this.alwaysUseFactory);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(Name name, String str, Object[] objArr, SearchControls searchControls) throws NamingException {
        return search(name, SearchFilter.format(str, objArr), searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, SearchControls searchControls) throws NamingException {
        return search(this.myParser.parse(str), str2, searchControls);
    }

    @Override // javax.naming.directory.DirContext
    public NamingEnumeration<SearchResult> search(String str, String str2, Object[] objArr, SearchControls searchControls) throws NamingException {
        return search(this.myParser.parse(str), str2, objArr, searchControls);
    }

    protected HierMemDirCtx createNewCtx() throws NamingException {
        return new HierMemDirCtx(this.myEnv, this.ignoreCase);
    }

    protected Name canonizeName(Name name) throws NamingException {
        Name hierarchicalName = name;
        if (!(name instanceof HierarchicalName)) {
            hierarchicalName = new HierarchicalName();
            int size = name.size();
            for (int i2 = 0; i2 < size; i2++) {
                hierarchicalName.add(i2, name.get(i2));
            }
        }
        return hierarchicalName;
    }

    protected Name getInternalName(Name name) throws NamingException {
        return name.getPrefix(name.size() - 1);
    }

    protected Name getLeafName(Name name) throws NamingException {
        return name.getSuffix(name.size() - 1);
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchema(String str) throws NamingException {
        throw new OperationNotSupportedException();
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchema(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(String str) throws NamingException {
        throw new OperationNotSupportedException();
    }

    @Override // javax.naming.directory.DirContext
    public DirContext getSchemaClassDefinition(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void setReadOnly(NamingException namingException) {
        this.readOnlyEx = namingException;
    }

    public void setIgnoreCase(boolean z2) {
        this.ignoreCase = z2;
    }

    public void setNameParser(NameParser nameParser) {
        this.myParser = nameParser;
    }

    /* loaded from: rt.jar:com/sun/jndi/toolkit/dir/HierMemDirCtx$BaseFlatNames.class */
    private abstract class BaseFlatNames<T> implements NamingEnumeration<T> {
        Enumeration<Name> names;

        @Override // javax.naming.NamingEnumeration
        public abstract T next() throws NamingException;

        BaseFlatNames(Enumeration<Name> enumeration) {
            this.names = enumeration;
        }

        @Override // java.util.Enumeration
        public final boolean hasMoreElements() {
            try {
                return hasMore();
            } catch (NamingException e2) {
                return false;
            }
        }

        @Override // javax.naming.NamingEnumeration
        public final boolean hasMore() throws NamingException {
            return this.names.hasMoreElements();
        }

        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public final T nextElement2() {
            try {
                return next();
            } catch (NamingException e2) {
                throw new NoSuchElementException(e2.toString());
            }
        }

        @Override // javax.naming.NamingEnumeration
        public final void close() {
            this.names = null;
        }
    }

    /* loaded from: rt.jar:com/sun/jndi/toolkit/dir/HierMemDirCtx$FlatNames.class */
    private final class FlatNames extends BaseFlatNames<NameClassPair> {
        FlatNames(Enumeration<Name> enumeration) {
            super(enumeration);
        }

        @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx.BaseFlatNames, javax.naming.NamingEnumeration
        public NameClassPair next() throws NamingException {
            Name nameNextElement2 = this.names.nextElement2();
            return new NameClassPair(nameNextElement2.toString(), HierMemDirCtx.this.bindings.get(nameNextElement2).getClass().getName());
        }
    }

    /* loaded from: rt.jar:com/sun/jndi/toolkit/dir/HierMemDirCtx$FlatBindings.class */
    private final class FlatBindings extends BaseFlatNames<Binding> {
        private Hashtable<Name, Object> bds;
        private Hashtable<String, Object> env;
        private boolean useFactory;

        FlatBindings(Hashtable<Name, Object> hashtable, Hashtable<String, Object> hashtable2, boolean z2) {
            super(hashtable.keys());
            this.env = hashtable2;
            this.bds = hashtable;
            this.useFactory = z2;
        }

        @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx.BaseFlatNames, javax.naming.NamingEnumeration
        public Binding next() throws NamingException {
            Name nameNextElement2 = this.names.nextElement2();
            HierMemDirCtx hierMemDirCtx = (HierMemDirCtx) this.bds.get(nameNextElement2);
            Object objectInstance = hierMemDirCtx;
            if (this.useFactory) {
                try {
                    objectInstance = DirectoryManager.getObjectInstance(hierMemDirCtx, nameNextElement2, HierMemDirCtx.this, this.env, hierMemDirCtx.getAttributes(""));
                } catch (NamingException e2) {
                    throw e2;
                } catch (Exception e3) {
                    NamingException namingException = new NamingException("Problem calling getObjectInstance");
                    namingException.setRootCause(e3);
                    throw namingException;
                }
            }
            return new Binding(nameNextElement2.toString(), objectInstance);
        }
    }

    /* loaded from: rt.jar:com/sun/jndi/toolkit/dir/HierMemDirCtx$HierContextEnumerator.class */
    public class HierContextEnumerator extends ContextEnumerator {
        public HierContextEnumerator(Context context, int i2) throws NamingException {
            super(context, i2);
        }

        protected HierContextEnumerator(Context context, int i2, String str, boolean z2) throws NamingException {
            super(context, i2, str, z2);
        }

        @Override // com.sun.jndi.toolkit.dir.ContextEnumerator
        protected NamingEnumeration<Binding> getImmediateChildren(Context context) throws NamingException {
            return ((HierMemDirCtx) context).doListBindings(false);
        }

        @Override // com.sun.jndi.toolkit.dir.ContextEnumerator
        protected ContextEnumerator newEnumerator(Context context, int i2, String str, boolean z2) throws NamingException {
            return HierMemDirCtx.this.new HierContextEnumerator(context, i2, str, z2);
        }
    }
}
