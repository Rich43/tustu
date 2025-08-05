package com.sun.jndi.ldap;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapAttribute.class */
final class LdapAttribute extends BasicAttribute {
    static final long serialVersionUID = -4288716561020779584L;
    private transient DirContext baseCtx;
    private Name rdn;
    private String baseCtxURL;
    private Hashtable<String, ? super String> baseCtxEnv;

    @Override // javax.naming.directory.BasicAttribute, javax.naming.directory.Attribute
    public Object clone() {
        LdapAttribute ldapAttribute = new LdapAttribute(this.attrID, this.baseCtx, this.rdn);
        ldapAttribute.values = (Vector) this.values.clone();
        return ldapAttribute;
    }

    @Override // javax.naming.directory.BasicAttribute, javax.naming.directory.Attribute
    public boolean add(Object obj) {
        this.values.addElement(obj);
        return true;
    }

    LdapAttribute(String str) {
        super(str);
        this.baseCtx = null;
        this.rdn = new CompositeName();
    }

    private LdapAttribute(String str, DirContext dirContext, Name name) {
        super(str);
        this.baseCtx = null;
        this.rdn = new CompositeName();
        this.baseCtx = dirContext;
        this.rdn = name;
    }

    void setParent(DirContext dirContext, Name name) {
        this.baseCtx = dirContext;
        this.rdn = name;
    }

    private DirContext getBaseCtx() throws NamingException {
        if (this.baseCtx == null) {
            if (this.baseCtxEnv == null) {
                this.baseCtxEnv = new Hashtable<>(3);
            }
            this.baseCtxEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            this.baseCtxEnv.put(Context.PROVIDER_URL, this.baseCtxURL);
            this.baseCtx = new InitialDirContext(this.baseCtxEnv);
        }
        return this.baseCtx;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        setBaseCtxInfo();
        objectOutputStream.defaultWriteObject();
    }

    private void setBaseCtxInfo() {
        Hashtable<String, ? super String> hashtable = null;
        Hashtable<String, ? super String> hashtable2 = null;
        if (this.baseCtx != null) {
            hashtable = ((LdapCtx) this.baseCtx).envprops;
            this.baseCtxURL = ((LdapCtx) this.baseCtx).getURL();
        }
        if (hashtable != null && hashtable.size() > 0) {
            for (String str : hashtable.keySet()) {
                if (str.indexOf("security") != -1) {
                    if (hashtable2 == null) {
                        hashtable2 = (Hashtable) hashtable.clone();
                    }
                    hashtable2.remove(str);
                }
            }
        }
        this.baseCtxEnv = hashtable2 == null ? hashtable : hashtable2;
    }

    @Override // javax.naming.directory.BasicAttribute, javax.naming.directory.Attribute
    public DirContext getAttributeSyntaxDefinition() throws NamingException {
        DirContext schema = getBaseCtx().getSchema(this.rdn);
        Attribute attribute = ((DirContext) schema.lookup("AttributeDefinition/" + getID())).getAttributes("").get("SYNTAX");
        if (attribute == null || attribute.size() == 0) {
            throw new NameNotFoundException(getID() + "does not have a syntax associated with it");
        }
        return (DirContext) schema.lookup("SyntaxDefinition/" + ((String) attribute.get()));
    }

    @Override // javax.naming.directory.BasicAttribute, javax.naming.directory.Attribute
    public DirContext getAttributeDefinition() throws NamingException {
        return (DirContext) getBaseCtx().getSchema(this.rdn).lookup("AttributeDefinition/" + getID());
    }
}
