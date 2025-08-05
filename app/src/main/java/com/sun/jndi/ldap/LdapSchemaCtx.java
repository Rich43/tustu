package com.sun.jndi.ldap;

import com.sun.jndi.toolkit.dir.HierMemDirCtx;
import java.util.Hashtable;
import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SchemaViolationException;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapSchemaCtx.class */
final class LdapSchemaCtx extends HierMemDirCtx {
    private static final boolean debug = false;
    private static final int LEAF = 0;
    private static final int SCHEMA_ROOT = 1;
    static final int OBJECTCLASS_ROOT = 2;
    static final int ATTRIBUTE_ROOT = 3;
    static final int SYNTAX_ROOT = 4;
    static final int MATCHRULE_ROOT = 5;
    static final int OBJECTCLASS = 6;
    static final int ATTRIBUTE = 7;
    static final int SYNTAX = 8;
    static final int MATCHRULE = 9;
    private SchemaInfo info;
    private boolean setupMode;
    private int objectType;

    static DirContext createSchemaTree(Hashtable<String, Object> hashtable, String str, LdapCtx ldapCtx, Attributes attributes, boolean z2) throws NamingException {
        try {
            LdapSchemaCtx ldapSchemaCtx = new LdapSchemaCtx(1, hashtable, new SchemaInfo(str, ldapCtx, new LdapSchemaParser(z2)));
            LdapSchemaParser.LDAP2JNDISchema(attributes, ldapSchemaCtx);
            return ldapSchemaCtx;
        } catch (NamingException e2) {
            ldapCtx.close();
            throw e2;
        }
    }

    private LdapSchemaCtx(int i2, Hashtable<String, Object> hashtable, SchemaInfo schemaInfo) {
        super(hashtable, true);
        this.info = null;
        this.setupMode = true;
        this.objectType = i2;
        this.info = schemaInfo;
    }

    @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx, javax.naming.Context
    public void close() throws NamingException {
        this.info.close();
    }

    @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx, javax.naming.directory.DirContext
    public final void bind(Name name, Object obj, Attributes attributes) throws NamingException {
        if (!this.setupMode) {
            if (obj != null) {
                throw new IllegalArgumentException("obj must be null");
            }
            addServerSchema(attributes);
        }
    }

    @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx
    protected final void doBind(Name name, Object obj, Attributes attributes, boolean z2) throws NamingException {
        if (!this.setupMode) {
            throw new SchemaViolationException("Cannot bind arbitrary object; use createSubcontext()");
        }
        super.doBind(name, obj, attributes, false);
    }

    @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx, javax.naming.directory.DirContext
    public final void rebind(Name name, Object obj, Attributes attributes) throws NamingException {
        try {
            doLookup(name, false);
            throw new SchemaViolationException("Cannot replace existing schema object");
        } catch (NameNotFoundException e2) {
            bind(name, obj, attributes);
        }
    }

    @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx
    protected final void doRebind(Name name, Object obj, Attributes attributes, boolean z2) throws NamingException {
        if (!this.setupMode) {
            throw new SchemaViolationException("Cannot bind arbitrary object; use createSubcontext()");
        }
        super.doRebind(name, obj, attributes, false);
    }

    @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx
    protected final void doUnbind(Name name) throws NamingException {
        if (!this.setupMode) {
            try {
                deleteServerSchema(((LdapSchemaCtx) doLookup(name, false)).attrs);
            } catch (NameNotFoundException e2) {
                return;
            }
        }
        super.doUnbind(name);
    }

    @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx
    protected final void doRename(Name name, Name name2) throws NamingException {
        if (!this.setupMode) {
            throw new SchemaViolationException("Cannot rename a schema object");
        }
        super.doRename(name, name2);
    }

    @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx
    protected final void doDestroySubcontext(Name name) throws NamingException {
        if (!this.setupMode) {
            try {
                deleteServerSchema(((LdapSchemaCtx) doLookup(name, false)).attrs);
            } catch (NameNotFoundException e2) {
                return;
            }
        }
        super.doDestroySubcontext(name);
    }

    final LdapSchemaCtx setup(int i2, String str, Attributes attributes) throws NamingException {
        try {
            this.setupMode = true;
            LdapSchemaCtx ldapSchemaCtx = (LdapSchemaCtx) super.doCreateSubcontext(new CompositeName(str), attributes);
            ldapSchemaCtx.objectType = i2;
            ldapSchemaCtx.setupMode = false;
            this.setupMode = false;
            return ldapSchemaCtx;
        } catch (Throwable th) {
            this.setupMode = false;
            throw th;
        }
    }

    @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx
    protected final DirContext doCreateSubcontext(Name name, Attributes attributes) throws NamingException {
        if (attributes == null || attributes.size() == 0) {
            throw new SchemaViolationException("Must supply attributes describing schema");
        }
        if (!this.setupMode) {
            addServerSchema(attributes);
        }
        return (LdapSchemaCtx) super.doCreateSubcontext(name, attributes);
    }

    private static final Attributes deepClone(Attributes attributes) throws NamingException {
        BasicAttributes basicAttributes = new BasicAttributes(true);
        NamingEnumeration<? extends Attribute> all = attributes.getAll();
        while (all.hasMore()) {
            basicAttributes.put((Attribute) all.next().clone());
        }
        return basicAttributes;
    }

    @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx
    protected final void doModifyAttributes(ModificationItem[] modificationItemArr) throws NamingException {
        if (this.setupMode) {
            super.doModifyAttributes(modificationItemArr);
            return;
        }
        Attributes attributesDeepClone = deepClone(this.attrs);
        applyMods(modificationItemArr, attributesDeepClone);
        modifyServerSchema(this.attrs, attributesDeepClone);
        this.attrs = attributesDeepClone;
    }

    @Override // com.sun.jndi.toolkit.dir.HierMemDirCtx
    protected final HierMemDirCtx createNewCtx() {
        return new LdapSchemaCtx(0, this.myEnv, this.info);
    }

    private final void addServerSchema(Attributes attributes) throws NamingException {
        Attribute attributeStringifyMatchRuleDesc;
        switch (this.objectType) {
            case 1:
                throw new SchemaViolationException("Cannot create new entry under schema root");
            case 2:
                attributeStringifyMatchRuleDesc = this.info.parser.stringifyObjDesc(attributes);
                break;
            case 3:
                attributeStringifyMatchRuleDesc = this.info.parser.stringifyAttrDesc(attributes);
                break;
            case 4:
                attributeStringifyMatchRuleDesc = this.info.parser.stringifySyntaxDesc(attributes);
                break;
            case 5:
                attributeStringifyMatchRuleDesc = this.info.parser.stringifyMatchRuleDesc(attributes);
                break;
            default:
                throw new SchemaViolationException("Cannot create child of schema object");
        }
        BasicAttributes basicAttributes = new BasicAttributes(true);
        basicAttributes.put(attributeStringifyMatchRuleDesc);
        this.info.modifyAttributes(this.myEnv, 1, basicAttributes);
    }

    private final void deleteServerSchema(Attributes attributes) throws NamingException {
        Attribute attributeStringifyMatchRuleDesc;
        switch (this.objectType) {
            case 1:
                throw new SchemaViolationException("Cannot delete schema root");
            case 2:
                attributeStringifyMatchRuleDesc = this.info.parser.stringifyObjDesc(attributes);
                break;
            case 3:
                attributeStringifyMatchRuleDesc = this.info.parser.stringifyAttrDesc(attributes);
                break;
            case 4:
                attributeStringifyMatchRuleDesc = this.info.parser.stringifySyntaxDesc(attributes);
                break;
            case 5:
                attributeStringifyMatchRuleDesc = this.info.parser.stringifyMatchRuleDesc(attributes);
                break;
            default:
                throw new SchemaViolationException("Cannot delete child of schema object");
        }
        this.info.modifyAttributes(this.myEnv, new ModificationItem[]{new ModificationItem(3, attributeStringifyMatchRuleDesc)});
    }

    private final void modifyServerSchema(Attributes attributes, Attributes attributes2) throws NamingException {
        Attribute attributeStringifyMatchRuleDesc;
        Attribute attributeStringifyMatchRuleDesc2;
        switch (this.objectType) {
            case 6:
                attributeStringifyMatchRuleDesc = this.info.parser.stringifyObjDesc(attributes);
                attributeStringifyMatchRuleDesc2 = this.info.parser.stringifyObjDesc(attributes2);
                break;
            case 7:
                attributeStringifyMatchRuleDesc = this.info.parser.stringifyAttrDesc(attributes);
                attributeStringifyMatchRuleDesc2 = this.info.parser.stringifyAttrDesc(attributes2);
                break;
            case 8:
                attributeStringifyMatchRuleDesc = this.info.parser.stringifySyntaxDesc(attributes);
                attributeStringifyMatchRuleDesc2 = this.info.parser.stringifySyntaxDesc(attributes2);
                break;
            case 9:
                attributeStringifyMatchRuleDesc = this.info.parser.stringifyMatchRuleDesc(attributes);
                attributeStringifyMatchRuleDesc2 = this.info.parser.stringifyMatchRuleDesc(attributes2);
                break;
            default:
                throw new SchemaViolationException("Cannot modify schema root");
        }
        this.info.modifyAttributes(this.myEnv, new ModificationItem[]{new ModificationItem(3, attributeStringifyMatchRuleDesc), new ModificationItem(1, attributeStringifyMatchRuleDesc2)});
    }

    /* loaded from: rt.jar:com/sun/jndi/ldap/LdapSchemaCtx$SchemaInfo.class */
    private static final class SchemaInfo {
        private LdapCtx schemaEntry;
        private String schemaEntryName;
        LdapSchemaParser parser;
        private String host;
        private int port;
        private boolean hasLdapsScheme;

        SchemaInfo(String str, LdapCtx ldapCtx, LdapSchemaParser ldapSchemaParser) {
            this.schemaEntryName = str;
            this.schemaEntry = ldapCtx;
            this.parser = ldapSchemaParser;
            this.port = ldapCtx.port_number;
            this.host = ldapCtx.hostname;
            this.hasLdapsScheme = ldapCtx.hasLdapsScheme;
        }

        synchronized void close() throws NamingException {
            if (this.schemaEntry != null) {
                this.schemaEntry.close();
                this.schemaEntry = null;
            }
        }

        private LdapCtx reopenEntry(Hashtable<?, ?> hashtable) throws NamingException {
            return new LdapCtx(this.schemaEntryName, this.host, this.port, hashtable, this.hasLdapsScheme);
        }

        synchronized void modifyAttributes(Hashtable<?, ?> hashtable, ModificationItem[] modificationItemArr) throws NamingException {
            if (this.schemaEntry == null) {
                this.schemaEntry = reopenEntry(hashtable);
            }
            this.schemaEntry.modifyAttributes("", modificationItemArr);
        }

        synchronized void modifyAttributes(Hashtable<?, ?> hashtable, int i2, Attributes attributes) throws NamingException {
            if (this.schemaEntry == null) {
                this.schemaEntry = reopenEntry(hashtable);
            }
            this.schemaEntry.modifyAttributes("", i2, attributes);
        }
    }
}
