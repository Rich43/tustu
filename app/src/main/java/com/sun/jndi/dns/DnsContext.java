package com.sun.jndi.dns;

import com.sun.jndi.toolkit.ctx.ComponentDirContext;
import com.sun.jndi.toolkit.ctx.Continuation;
import java.util.Hashtable;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.spi.DirectoryManager;

/* loaded from: rt.jar:com/sun/jndi/dns/DnsContext.class */
public class DnsContext extends ComponentDirContext {
    DnsName domain;
    Hashtable<Object, Object> environment;
    private boolean envShared;
    private boolean parentIsDns;
    private String[] servers;
    private Resolver resolver;
    private boolean authoritative;
    private boolean recursion;
    private int timeout;
    private int retries;
    private static final int DEFAULT_INIT_TIMEOUT = 1000;
    private static final int DEFAULT_RETRIES = 4;
    private static final String INIT_TIMEOUT = "com.sun.jndi.dns.timeout.initial";
    private static final String RETRIES = "com.sun.jndi.dns.timeout.retries";
    private CT lookupCT;
    private static final String LOOKUP_ATTR = "com.sun.jndi.dns.lookup.attr";
    private static final String RECURSION = "com.sun.jndi.dns.recursion";
    private static final int ANY = 255;
    private static final boolean debug = false;
    static final NameParser nameParser = new DnsNameParser();
    private static final ZoneNode zoneTree = new ZoneNode(null);

    public DnsContext(String str, String[] strArr, Hashtable<?, ?> hashtable) throws NamingException {
        this.domain = new DnsName(str.endsWith(".") ? str : str + ".");
        this.servers = strArr == null ? null : (String[]) strArr.clone();
        this.environment = (Hashtable) hashtable.clone();
        this.envShared = false;
        this.parentIsDns = false;
        this.resolver = null;
        initFromEnvironment();
    }

    DnsContext(DnsContext dnsContext, DnsName dnsName) {
        this(dnsContext);
        this.domain = dnsName;
        this.parentIsDns = true;
    }

    private DnsContext(DnsContext dnsContext) {
        this.environment = dnsContext.environment;
        dnsContext.envShared = true;
        this.envShared = true;
        this.parentIsDns = dnsContext.parentIsDns;
        this.domain = dnsContext.domain;
        this.servers = dnsContext.servers;
        this.resolver = dnsContext.resolver;
        this.authoritative = dnsContext.authoritative;
        this.recursion = dnsContext.recursion;
        this.timeout = dnsContext.timeout;
        this.retries = dnsContext.retries;
        this.lookupCT = dnsContext.lookupCT;
    }

    @Override // javax.naming.Context
    public void close() {
        if (this.resolver != null) {
            this.resolver.close();
            this.resolver = null;
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected Hashtable<?, ?> p_getEnvironment() {
        return this.environment;
    }

    @Override // javax.naming.Context
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return (Hashtable) this.environment.clone();
    }

    @Override // javax.naming.Context
    public Object addToEnvironment(String str, Object obj) throws NumberFormatException, NamingException {
        int i2;
        if (str.equals(LOOKUP_ATTR)) {
            this.lookupCT = getLookupCT((String) obj);
        } else if (str.equals(Context.AUTHORITATIVE)) {
            this.authoritative = "true".equalsIgnoreCase((String) obj);
        } else if (str.equals(RECURSION)) {
            this.recursion = "true".equalsIgnoreCase((String) obj);
        } else if (str.equals(INIT_TIMEOUT)) {
            int i3 = Integer.parseInt((String) obj);
            if (this.timeout != i3) {
                this.timeout = i3;
                this.resolver = null;
            }
        } else if (str.equals(RETRIES) && this.retries != (i2 = Integer.parseInt((String) obj))) {
            this.retries = i2;
            this.resolver = null;
        }
        if (!this.envShared) {
            return this.environment.put(str, obj);
        }
        if (this.environment.get(str) != obj) {
            this.environment = (Hashtable) this.environment.clone();
            this.envShared = false;
            return this.environment.put(str, obj);
        }
        return obj;
    }

    @Override // javax.naming.Context
    public Object removeFromEnvironment(String str) throws NamingException {
        if (str.equals(LOOKUP_ATTR)) {
            this.lookupCT = getLookupCT(null);
        } else if (str.equals(Context.AUTHORITATIVE)) {
            this.authoritative = false;
        } else if (str.equals(RECURSION)) {
            this.recursion = true;
        } else if (str.equals(INIT_TIMEOUT)) {
            if (this.timeout != 1000) {
                this.timeout = 1000;
                this.resolver = null;
            }
        } else if (str.equals(RETRIES) && this.retries != 4) {
            this.retries = 4;
            this.resolver = null;
        }
        if (!this.envShared) {
            return this.environment.remove(str);
        }
        if (this.environment.get(str) != null) {
            this.environment = (Hashtable) this.environment.clone();
            this.envShared = false;
            return this.environment.remove(str);
        }
        return null;
    }

    void setProviderUrl(String str) {
        this.environment.put(Context.PROVIDER_URL, str);
    }

    private void initFromEnvironment() throws InvalidAttributeIdentifierException {
        this.lookupCT = getLookupCT((String) this.environment.get(LOOKUP_ATTR));
        this.authoritative = "true".equalsIgnoreCase((String) this.environment.get(Context.AUTHORITATIVE));
        String str = (String) this.environment.get(RECURSION);
        this.recursion = str == null || "true".equalsIgnoreCase(str);
        String str2 = (String) this.environment.get(INIT_TIMEOUT);
        this.timeout = str2 == null ? 1000 : Integer.parseInt(str2);
        String str3 = (String) this.environment.get(RETRIES);
        this.retries = str3 == null ? 4 : Integer.parseInt(str3);
    }

    private CT getLookupCT(String str) throws InvalidAttributeIdentifierException {
        return str == null ? new CT(1, 16) : fromAttrId(str);
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    public Object c_lookup(Name name, Continuation continuation) throws NamingException {
        continuation.setSuccess();
        if (name.isEmpty()) {
            DnsContext dnsContext = new DnsContext(this);
            dnsContext.resolver = new Resolver(this.servers, this.timeout, this.retries);
            return dnsContext;
        }
        try {
            DnsName dnsNameFullyQualify = fullyQualify(name);
            return DirectoryManager.getObjectInstance(new DnsContext(this, dnsNameFullyQualify), name, this, this.environment, rrsToAttrs(getResolver().query(dnsNameFullyQualify, this.lookupCT.rrclass, this.lookupCT.rrtype, this.recursion, this.authoritative), null));
        } catch (NamingException e2) {
            continuation.setError(this, name);
            throw continuation.fillInException(e2);
        } catch (Exception e3) {
            continuation.setError(this, name);
            NamingException namingException = new NamingException("Problem generating object using object factory");
            namingException.setRootCause(e3);
            throw continuation.fillInException(namingException);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    public Object c_lookupLink(Name name, Continuation continuation) throws NamingException {
        return c_lookup(name, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    public NamingEnumeration<NameClassPair> c_list(Name name, Continuation continuation) throws NamingException {
        continuation.setSuccess();
        try {
            DnsName dnsNameFullyQualify = fullyQualify(name);
            return new NameClassPairEnumeration(new DnsContext(this, dnsNameFullyQualify), getNameNode(dnsNameFullyQualify).getChildren());
        } catch (NamingException e2) {
            continuation.setError(this, name);
            throw continuation.fillInException(e2);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    public NamingEnumeration<Binding> c_listBindings(Name name, Continuation continuation) throws NamingException {
        continuation.setSuccess();
        try {
            DnsName dnsNameFullyQualify = fullyQualify(name);
            return new BindingEnumeration(new DnsContext(this, dnsNameFullyQualify), getNameNode(dnsNameFullyQualify).getChildren());
        } catch (NamingException e2) {
            continuation.setError(this, name);
            throw continuation.fillInException(e2);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    public void c_bind(Name name, Object obj, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    public void c_rebind(Name name, Object obj, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    public void c_unbind(Name name, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    public void c_rename(Name name, Name name2, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    public Context c_createSubcontext(Name name, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    public void c_destroySubcontext(Name name, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    public NameParser c_getNameParser(Name name, Continuation continuation) throws NamingException {
        continuation.setSuccess();
        return nameParser;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    public void c_bind(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    public void c_rebind(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    public DirContext c_createSubcontext(Name name, Attributes attributes, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    public Attributes c_getAttributes(Name name, String[] strArr, Continuation continuation) throws NamingException {
        continuation.setSuccess();
        try {
            DnsName dnsNameFullyQualify = fullyQualify(name);
            CT[] ctArrAttrIdsToClassesAndTypes = attrIdsToClassesAndTypes(strArr);
            CT classAndTypeToQuery = getClassAndTypeToQuery(ctArrAttrIdsToClassesAndTypes);
            return rrsToAttrs(getResolver().query(dnsNameFullyQualify, classAndTypeToQuery.rrclass, classAndTypeToQuery.rrtype, this.recursion, this.authoritative), ctArrAttrIdsToClassesAndTypes);
        } catch (NamingException e2) {
            continuation.setError(this, name);
            throw continuation.fillInException(e2);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    public void c_modifyAttributes(Name name, int i2, Attributes attributes, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    public void c_modifyAttributes(Name name, ModificationItem[] modificationItemArr, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    public NamingEnumeration<SearchResult> c_search(Name name, Attributes attributes, String[] strArr, Continuation continuation) throws NamingException {
        throw new OperationNotSupportedException();
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    public NamingEnumeration<SearchResult> c_search(Name name, String str, SearchControls searchControls, Continuation continuation) throws NamingException {
        throw new OperationNotSupportedException();
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    public NamingEnumeration<SearchResult> c_search(Name name, String str, Object[] objArr, SearchControls searchControls, Continuation continuation) throws NamingException {
        throw new OperationNotSupportedException();
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    public DirContext c_getSchema(Name name, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    public DirContext c_getSchemaClassDefinition(Name name, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        throw continuation.fillInException(new OperationNotSupportedException());
    }

    @Override // javax.naming.Context
    public String getNameInNamespace() {
        return this.domain.toString();
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext, javax.naming.Context
    public Name composeName(Name name, Name name2) throws NamingException {
        DnsName dnsName;
        if (!(name2 instanceof DnsName) && !(name2 instanceof CompositeName)) {
            name2 = new DnsName().addAll(name2);
        }
        if (!(name instanceof DnsName) && !(name instanceof CompositeName)) {
            name = new DnsName().addAll(name);
        }
        if ((name2 instanceof DnsName) && (name instanceof DnsName)) {
            DnsName dnsName2 = (DnsName) name2.clone();
            dnsName2.addAll(name);
            return new CompositeName().add(dnsName2.toString());
        }
        Name nameAdd = name2 instanceof CompositeName ? name2 : new CompositeName().add(name2.toString());
        Name nameAdd2 = name instanceof CompositeName ? name : new CompositeName().add(name.toString());
        int size = nameAdd.size() - 1;
        if (nameAdd2.isEmpty() || nameAdd2.get(0).equals("") || nameAdd.isEmpty() || nameAdd.get(size).equals("")) {
            return super.composeName(nameAdd2, nameAdd);
        }
        Name name3 = name2 == nameAdd ? (CompositeName) nameAdd.clone() : nameAdd;
        name3.addAll(nameAdd2);
        if (this.parentIsDns) {
            if (name2 instanceof DnsName) {
                dnsName = (DnsName) name2.clone();
            } else {
                dnsName = new DnsName(nameAdd.get(size));
            }
            DnsName dnsName3 = dnsName;
            dnsName3.addAll(name instanceof DnsName ? name : new DnsName(nameAdd2.get(0)));
            name3.remove(size + 1);
            name3.remove(size);
            name3.add(size, dnsName3.toString());
        }
        return name3;
    }

    private synchronized Resolver getResolver() throws NamingException {
        if (this.resolver == null) {
            this.resolver = new Resolver(this.servers, this.timeout, this.retries);
        }
        return this.resolver;
    }

    DnsName fullyQualify(Name name) throws NamingException {
        DnsName dnsName;
        if (name.isEmpty()) {
            return this.domain;
        }
        if (name instanceof CompositeName) {
            dnsName = new DnsName(name.get(0));
        } else {
            dnsName = (DnsName) new DnsName().addAll(name);
        }
        DnsName dnsName2 = dnsName;
        if (dnsName2.hasRootLabel()) {
            if (this.domain.size() == 1) {
                return dnsName2;
            }
            throw new InvalidNameException("DNS name " + ((Object) dnsName2) + " not relative to " + ((Object) this.domain));
        }
        return (DnsName) dnsName2.addAll(0, this.domain);
    }

    private static Attributes rrsToAttrs(ResourceRecords resourceRecords, CT[] ctArr) {
        BasicAttributes basicAttributes = new BasicAttributes(true);
        for (int i2 = 0; i2 < resourceRecords.answer.size(); i2++) {
            ResourceRecord resourceRecordElementAt = resourceRecords.answer.elementAt(i2);
            int type = resourceRecordElementAt.getType();
            int rrclass = resourceRecordElementAt.getRrclass();
            if (classAndTypeMatch(rrclass, type, ctArr)) {
                String attrId = toAttrId(rrclass, type);
                Attribute basicAttribute = basicAttributes.get(attrId);
                if (basicAttribute == null) {
                    basicAttribute = new BasicAttribute(attrId);
                    basicAttributes.put(basicAttribute);
                }
                basicAttribute.add(resourceRecordElementAt.getRdata());
            }
        }
        return basicAttributes;
    }

    private static boolean classAndTypeMatch(int i2, int i3, CT[] ctArr) {
        if (ctArr == null) {
            return true;
        }
        for (CT ct : ctArr) {
            boolean z2 = ct.rrclass == 255 || ct.rrclass == i2;
            boolean z3 = ct.rrtype == 255 || ct.rrtype == i3;
            if (z2 && z3) {
                return true;
            }
        }
        return false;
    }

    private static String toAttrId(int i2, int i3) {
        String typeName = ResourceRecord.getTypeName(i3);
        if (i2 != 1) {
            typeName = ResourceRecord.getRrclassName(i2) + " " + typeName;
        }
        return typeName;
    }

    private static CT fromAttrId(String str) throws InvalidAttributeIdentifierException {
        int rrclass;
        if (str.equals("")) {
            throw new InvalidAttributeIdentifierException("Attribute ID cannot be empty");
        }
        int iIndexOf = str.indexOf(32);
        if (iIndexOf < 0) {
            rrclass = 1;
        } else {
            String strSubstring = str.substring(0, iIndexOf);
            rrclass = ResourceRecord.getRrclass(strSubstring);
            if (rrclass < 0) {
                throw new InvalidAttributeIdentifierException("Unknown resource record class '" + strSubstring + '\'');
            }
        }
        String strSubstring2 = str.substring(iIndexOf + 1);
        int type = ResourceRecord.getType(strSubstring2);
        if (type < 0) {
            throw new InvalidAttributeIdentifierException("Unknown resource record type '" + strSubstring2 + '\'');
        }
        return new CT(rrclass, type);
    }

    private static CT[] attrIdsToClassesAndTypes(String[] strArr) throws InvalidAttributeIdentifierException {
        if (strArr == null) {
            return null;
        }
        CT[] ctArr = new CT[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            ctArr[i2] = fromAttrId(strArr[i2]);
        }
        return ctArr;
    }

    private static CT getClassAndTypeToQuery(CT[] ctArr) {
        int i2;
        int i3;
        if (ctArr == null) {
            i2 = 255;
            i3 = 255;
        } else if (ctArr.length == 0) {
            i2 = 1;
            i3 = 255;
        } else {
            i2 = ctArr[0].rrclass;
            i3 = ctArr[0].rrtype;
            for (int i4 = 1; i4 < ctArr.length; i4++) {
                if (i2 != ctArr[i4].rrclass) {
                    i2 = 255;
                }
                if (i3 != ctArr[i4].rrtype) {
                    i3 = 255;
                }
            }
        }
        return new CT(i2, i3);
    }

    private NameNode getNameNode(DnsName dnsName) throws NamingException {
        ZoneNode deepestPopulated;
        ZoneNode zoneNode;
        NameNode nameNodePopulateZone;
        NameNode nameNode;
        NameNode contents;
        NameNode nameNode2;
        dprint("getNameNode(" + ((Object) dnsName) + ")");
        synchronized (zoneTree) {
            deepestPopulated = zoneTree.getDeepestPopulated(dnsName);
        }
        dprint("Deepest related zone in zone tree: " + (deepestPopulated != null ? deepestPopulated.getLabel() : "[none]"));
        if (deepestPopulated != null) {
            synchronized (deepestPopulated) {
                contents = deepestPopulated.getContents();
            }
            if (contents != null && (nameNode2 = contents.get(dnsName, deepestPopulated.depth() + 1)) != null && !nameNode2.isZoneCut()) {
                dprint("Found node " + ((Object) dnsName) + " in zone tree");
                boolean zIsZoneCurrent = isZoneCurrent(deepestPopulated, (DnsName) dnsName.getPrefix(deepestPopulated.depth() + 1));
                boolean z2 = false;
                synchronized (deepestPopulated) {
                    if (contents != deepestPopulated.getContents()) {
                        z2 = true;
                    } else if (!zIsZoneCurrent) {
                        deepestPopulated.depopulate();
                    } else {
                        return nameNode2;
                    }
                    dprint("Zone not current; discarding node");
                    if (z2) {
                        return getNameNode(dnsName);
                    }
                }
            }
        }
        dprint("Adding node " + ((Object) dnsName) + " to zone tree");
        DnsName dnsNameFindZoneName = getResolver().findZoneName(dnsName, 1, this.recursion);
        dprint("Node's zone is " + ((Object) dnsNameFindZoneName));
        synchronized (zoneTree) {
            zoneNode = (ZoneNode) zoneTree.add(dnsNameFindZoneName, 1);
        }
        synchronized (zoneNode) {
            if (zoneNode.isPopulated()) {
                nameNodePopulateZone = zoneNode.getContents();
            } else {
                nameNodePopulateZone = populateZone(zoneNode, dnsNameFindZoneName);
            }
            nameNode = nameNodePopulateZone;
        }
        NameNode nameNode3 = nameNode.get(dnsName, dnsNameFindZoneName.size());
        if (nameNode3 == null) {
            throw new ConfigurationException("DNS error: node not found in its own zone");
        }
        dprint("Found node in newly-populated zone");
        return nameNode3;
    }

    private NameNode populateZone(ZoneNode zoneNode, DnsName dnsName) throws NamingException {
        dprint("Populating zone " + ((Object) dnsName));
        ResourceRecords resourceRecordsQueryZone = getResolver().queryZone(dnsName, 1, this.recursion);
        dprint("zone xfer complete: " + resourceRecordsQueryZone.answer.size() + " records");
        return zoneNode.populate(dnsName, resourceRecordsQueryZone);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0037  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean isZoneCurrent(com.sun.jndi.dns.ZoneNode r6, com.sun.jndi.dns.DnsName r7) throws javax.naming.NamingException {
        /*
            r5 = this;
            r0 = r6
            boolean r0 = r0.isPopulated()
            if (r0 != 0) goto L9
            r0 = 0
            return r0
        L9:
            r0 = r5
            com.sun.jndi.dns.Resolver r0 = r0.getResolver()
            r1 = r7
            r2 = 1
            r3 = r5
            boolean r3 = r3.recursion
            com.sun.jndi.dns.ResourceRecord r0 = r0.findSoa(r1, r2, r3)
            r8 = r0
            r0 = r6
            r1 = r0
            r9 = r1
            monitor-enter(r0)
            r0 = r8
            if (r0 != 0) goto L24
            r0 = r6
            r0.depopulate()     // Catch: java.lang.Throwable -> L3c
        L24:
            r0 = r6
            boolean r0 = r0.isPopulated()     // Catch: java.lang.Throwable -> L3c
            if (r0 == 0) goto L37
            r0 = r6
            r1 = r8
            int r0 = r0.compareSerialNumberTo(r1)     // Catch: java.lang.Throwable -> L3c
            if (r0 < 0) goto L37
            r0 = 1
            goto L38
        L37:
            r0 = 0
        L38:
            r1 = r9
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L3c
            return r0
        L3c:
            r10 = move-exception
            r0 = r9
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L3c
            r0 = r10
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jndi.dns.DnsContext.isZoneCurrent(com.sun.jndi.dns.ZoneNode, com.sun.jndi.dns.DnsName):boolean");
    }

    private static final void dprint(String str) {
    }
}
