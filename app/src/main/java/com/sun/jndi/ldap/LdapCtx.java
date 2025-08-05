package com.sun.jndi.ldap;

import com.sun.jndi.ldap.ext.StartTlsResponseImpl;
import com.sun.jndi.toolkit.ctx.ComponentDirContext;
import com.sun.jndi.toolkit.ctx.Continuation;
import com.sun.jndi.toolkit.dir.HierMemDirCtx;
import com.sun.jndi.toolkit.dir.SearchFilter;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Binding;
import javax.naming.CommunicationException;
import javax.naming.CompositeName;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.ContextNotEmptyException;
import javax.naming.InvalidNameException;
import javax.naming.LimitExceededException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
import javax.naming.OperationNotSupportedException;
import javax.naming.PartialResultException;
import javax.naming.ServiceUnavailableException;
import javax.naming.SizeLimitExceededException;
import javax.naming.TimeLimitExceededException;
import javax.naming.directory.Attribute;
import javax.naming.directory.AttributeInUseException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.naming.directory.InvalidAttributeValueException;
import javax.naming.directory.InvalidSearchFilterException;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.NoSuchAttributeException;
import javax.naming.directory.SchemaViolationException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.event.EventDirContext;
import javax.naming.event.NamingListener;
import javax.naming.ldap.Control;
import javax.naming.ldap.ControlFactory;
import javax.naming.ldap.ExtendedRequest;
import javax.naming.ldap.ExtendedResponse;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.Rdn;
import javax.naming.ldap.UnsolicitedNotificationListener;
import javax.naming.spi.DirectoryManager;
import javax.swing.JSplitPane;
import org.icepdf.core.pobjects.graphics.Separation;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapCtx.class */
public final class LdapCtx extends ComponentDirContext implements EventDirContext, LdapContext {
    private static final boolean debug = false;
    private static final boolean HARD_CLOSE = true;
    private static final boolean SOFT_CLOSE = false;
    public static final int DEFAULT_PORT = 389;
    public static final int DEFAULT_SSL_PORT = 636;
    public static final String DEFAULT_HOST = "localhost";
    private static final boolean DEFAULT_DELETE_RDN = true;
    private static final boolean DEFAULT_TYPES_ONLY = false;
    private static final int DEFAULT_DEREF_ALIASES = 3;
    private static final int DEFAULT_LDAP_VERSION = 32;
    private static final int DEFAULT_BATCH_SIZE = 1;
    private static final int DEFAULT_REFERRAL_MODE = 3;
    private static final char DEFAULT_REF_SEPARATOR = '#';
    static final String DEFAULT_SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final int DEFAULT_REFERRAL_LIMIT = 10;
    private static final String STARTTLS_REQ_OID = "1.3.6.1.4.1.1466.20037";
    private static final String VERSION = "java.naming.ldap.version";
    private static final String BINARY_ATTRIBUTES = "java.naming.ldap.attributes.binary";
    private static final String DELETE_RDN = "java.naming.ldap.deleteRDN";
    private static final String DEREF_ALIASES = "java.naming.ldap.derefAliases";
    private static final String TYPES_ONLY = "java.naming.ldap.typesOnly";
    private static final String REF_SEPARATOR = "java.naming.ldap.ref.separator";
    private static final String SOCKET_FACTORY = "java.naming.ldap.factory.socket";
    static final String BIND_CONTROLS = "java.naming.ldap.control.connect";
    private static final String REFERRAL_LIMIT = "java.naming.ldap.referral.limit";
    private static final String TRACE_BER = "com.sun.jndi.ldap.trace.ber";
    private static final String NETSCAPE_SCHEMA_BUG = "com.sun.jndi.ldap.netscape.schemaBugs";
    private static final String OLD_NETSCAPE_SCHEMA_BUG = "com.sun.naming.netscape.schemaBugs";
    private static final String CONNECT_TIMEOUT = "com.sun.jndi.ldap.connect.timeout";
    private static final String READ_TIMEOUT = "com.sun.jndi.ldap.read.timeout";
    private static final String ENABLE_POOL = "com.sun.jndi.ldap.connect.pool";
    private static final String DOMAIN_NAME = "com.sun.jndi.ldap.domainname";
    private static final String WAIT_FOR_REPLY = "com.sun.jndi.ldap.search.waitForReply";
    private static final String REPLY_QUEUE_SIZE = "com.sun.jndi.ldap.search.replyQueueSize";
    private static final String ALLOWED_MECHS_SP = "jdk.jndi.ldap.mechsAllowedToSendCredentials";
    private static final String UNSECURED_CRED_TRANSMIT_MSG = "Transmission of credentials over unsecured connection is not allowed";
    int port_number;
    String hostname;
    LdapClient clnt;
    Hashtable<String, Object> envprops;
    int handleReferrals;
    boolean hasLdapsScheme;
    String currentDN;
    Name currentParsedDN;
    Vector<Control> respCtls;
    Control[] reqCtls;
    volatile boolean contextSeenStartTlsEnabled;
    private OutputStream trace;
    private boolean netscapeSchemaBug;
    private Control[] bindCtls;
    private int referralHopLimit;
    private Hashtable<String, DirContext> schemaTrees;
    private int batchSize;
    private boolean deleteRDN;
    private boolean typesOnly;
    private int derefAliases;
    private char addrEncodingSeparator;
    private Hashtable<String, Boolean> binaryAttrs;
    private int connectTimeout;
    private int readTimeout;
    private boolean waitForReply;
    private int replyQueueSize;
    private boolean useSsl;
    private boolean useDefaultPortNumber;
    private boolean parentIsLdapCtx;
    private int hopCount;
    private String url;
    private EventSupport eventSupport;
    private boolean unsolicited;
    private boolean sharable;
    private int enumCount;
    private boolean closeRequested;
    private static final String[] SCHEMA_ATTRIBUTES = {"objectClasses", "attributeTypes", "matchingRules", "ldapSyntaxes"};
    private static final String ALLOWED_MECHS_SP_VALUE = getMechsAllowedToSendCredentials();
    private static final Set<String> MECHS_ALLOWED_BY_SP = getMechsFromPropertyValue(ALLOWED_MECHS_SP_VALUE);
    private static final NameParser parser = new LdapNameParser();
    private static final ControlFactory myResponseControlFactory = new DefaultResponseControlFactory();
    private static final Control manageReferralControl = new ManageReferralControl(false);
    private static final HierMemDirCtx EMPTY_SCHEMA = new HierMemDirCtx();

    /* loaded from: rt.jar:com/sun/jndi/ldap/LdapCtx$SearchArgs.class */
    static final class SearchArgs {
        Name name;
        String filter;
        SearchControls cons;
        String[] reqAttrs;

        SearchArgs(Name name, String str, SearchControls searchControls, String[] strArr) {
            this.name = name;
            this.filter = str;
            this.cons = searchControls;
            this.reqAttrs = strArr;
        }
    }

    static {
        EMPTY_SCHEMA.setReadOnly(new SchemaViolationException("Cannot update schema object"));
    }

    public LdapCtx(String str, String str2, int i2, Hashtable<?, ?> hashtable, boolean z2) throws NamingException {
        this.hostname = null;
        this.clnt = null;
        this.envprops = null;
        this.handleReferrals = 3;
        this.hasLdapsScheme = false;
        this.respCtls = null;
        this.reqCtls = null;
        this.trace = null;
        this.netscapeSchemaBug = false;
        this.bindCtls = null;
        this.referralHopLimit = 10;
        this.schemaTrees = null;
        this.batchSize = 1;
        this.deleteRDN = true;
        this.typesOnly = false;
        this.derefAliases = 3;
        this.addrEncodingSeparator = '#';
        this.binaryAttrs = null;
        this.connectTimeout = -1;
        this.readTimeout = -1;
        this.waitForReply = true;
        this.replyQueueSize = -1;
        this.useSsl = false;
        this.useDefaultPortNumber = false;
        this.parentIsLdapCtx = false;
        this.hopCount = 1;
        this.url = null;
        this.unsolicited = false;
        this.sharable = true;
        this.enumCount = 0;
        this.closeRequested = false;
        this.hasLdapsScheme = z2;
        this.useSsl = z2;
        if (hashtable != null) {
            this.envprops = (Hashtable) hashtable.clone();
            if ("ssl".equals(this.envprops.get(Context.SECURITY_PROTOCOL))) {
                this.useSsl = true;
            }
            this.trace = (OutputStream) this.envprops.get(TRACE_BER);
            if (hashtable.get(NETSCAPE_SCHEMA_BUG) != null || hashtable.get(OLD_NETSCAPE_SCHEMA_BUG) != null) {
                this.netscapeSchemaBug = true;
            }
        }
        this.currentDN = str != null ? str : "";
        this.currentParsedDN = parser.parse(this.currentDN);
        this.hostname = (str2 == null || str2.length() <= 0) ? "localhost" : str2;
        if (this.hostname.charAt(0) == '[') {
            this.hostname = this.hostname.substring(1, this.hostname.length() - 1);
        }
        if (i2 > 0) {
            this.port_number = i2;
        } else {
            this.port_number = this.useSsl ? DEFAULT_SSL_PORT : DEFAULT_PORT;
            this.useDefaultPortNumber = true;
        }
        this.schemaTrees = new Hashtable<>(11, 0.75f);
        initEnv();
        try {
            connect(false);
        } catch (NamingException e2) {
            try {
                close();
            } catch (Exception e3) {
            }
            throw e2;
        }
    }

    LdapCtx(LdapCtx ldapCtx, String str) throws NamingException {
        this.hostname = null;
        this.clnt = null;
        this.envprops = null;
        this.handleReferrals = 3;
        this.hasLdapsScheme = false;
        this.respCtls = null;
        this.reqCtls = null;
        this.trace = null;
        this.netscapeSchemaBug = false;
        this.bindCtls = null;
        this.referralHopLimit = 10;
        this.schemaTrees = null;
        this.batchSize = 1;
        this.deleteRDN = true;
        this.typesOnly = false;
        this.derefAliases = 3;
        this.addrEncodingSeparator = '#';
        this.binaryAttrs = null;
        this.connectTimeout = -1;
        this.readTimeout = -1;
        this.waitForReply = true;
        this.replyQueueSize = -1;
        this.useSsl = false;
        this.useDefaultPortNumber = false;
        this.parentIsLdapCtx = false;
        this.hopCount = 1;
        this.url = null;
        this.unsolicited = false;
        this.sharable = true;
        this.enumCount = 0;
        this.closeRequested = false;
        this.useSsl = ldapCtx.useSsl;
        this.hasLdapsScheme = ldapCtx.hasLdapsScheme;
        this.useDefaultPortNumber = ldapCtx.useDefaultPortNumber;
        this.hostname = ldapCtx.hostname;
        this.port_number = ldapCtx.port_number;
        this.currentDN = str;
        if (ldapCtx.currentDN == this.currentDN) {
            this.currentParsedDN = ldapCtx.currentParsedDN;
        } else {
            this.currentParsedDN = parser.parse(this.currentDN);
        }
        this.envprops = ldapCtx.envprops;
        this.schemaTrees = ldapCtx.schemaTrees;
        this.clnt = ldapCtx.clnt;
        this.clnt.incRefCount();
        this.parentIsLdapCtx = (str == null || str.equals(ldapCtx.currentDN)) ? ldapCtx.parentIsLdapCtx : true;
        this.trace = ldapCtx.trace;
        this.netscapeSchemaBug = ldapCtx.netscapeSchemaBug;
        initEnv();
    }

    @Override // javax.naming.ldap.LdapContext
    public LdapContext newInstance(Control[] controlArr) throws NamingException {
        LdapCtx ldapCtx = new LdapCtx(this, this.currentDN);
        ldapCtx.setRequestControls(controlArr);
        return ldapCtx;
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_bind(Name name, Object obj, Continuation continuation) throws NamingException {
        c_bind(name, obj, null, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected void c_bind(Name name, Object obj, Attributes attributes, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        try {
            ensureOpen();
            if (obj == null) {
                if (attributes == null) {
                    throw new IllegalArgumentException("cannot bind null object with no attributes");
                }
            } else {
                attributes = Obj.determineBindAttrs(this.addrEncodingSeparator, obj, attributes, false, name, this, this.envprops);
            }
            String strFullyQualifiedName = fullyQualifiedName(name);
            LdapResult ldapResultAdd = this.clnt.add(new LdapEntry(strFullyQualifiedName, addRdnAttributes(strFullyQualifiedName, attributes, attributes != attributes)), this.reqCtls);
            this.respCtls = ldapResultAdd.resControls;
            if (ldapResultAdd.status != 0) {
                processReturnCode(ldapResultAdd, name);
            }
        } catch (LdapReferralException e2) {
            e = e2;
            if (this.handleReferrals == 2) {
                throw continuation.fillInException(e);
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    ldapReferralContext.bind(name, obj, attributes);
                    ldapReferralContext.close();
                    return;
                } catch (LdapReferralException e3) {
                    e = e3;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (IOException e4) {
            CommunicationException communicationException = new CommunicationException(e4.getMessage());
            communicationException.setRootCause(e4);
            throw continuation.fillInException(communicationException);
        } catch (NamingException e5) {
            throw continuation.fillInException(e5);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_rebind(Name name, Object obj, Continuation continuation) throws IOException, NamingException {
        c_rebind(name, obj, null, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected void c_rebind(Name name, Object obj, Attributes attributes, Continuation continuation) throws IOException, NamingException {
        continuation.setError(this, name);
        Attributes attributesC_getAttributes = null;
        try {
            try {
                attributesC_getAttributes = c_getAttributes(name, null, continuation);
            } catch (LdapReferralException e2) {
                e = e2;
                if (this.handleReferrals == 2) {
                    throw continuation.fillInException(e);
                }
                while (true) {
                    LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                    try {
                        ldapReferralContext.rebind(name, obj, attributes);
                        ldapReferralContext.close();
                        return;
                    } catch (LdapReferralException e3) {
                        e = e3;
                        ldapReferralContext.close();
                    } catch (Throwable th) {
                        ldapReferralContext.close();
                        throw th;
                    }
                }
            } catch (IOException e4) {
                CommunicationException communicationException = new CommunicationException(e4.getMessage());
                communicationException.setRootCause(e4);
                throw continuation.fillInException(communicationException);
            } catch (NamingException e5) {
                throw continuation.fillInException(e5);
            }
        } catch (NameNotFoundException e6) {
        }
        if (attributesC_getAttributes == null) {
            c_bind(name, obj, attributes, continuation);
            return;
        }
        if (attributes == null && (obj instanceof DirContext)) {
            attributes = ((DirContext) obj).getAttributes("");
        }
        Attributes attributes2 = (Attributes) attributesC_getAttributes.clone();
        if (attributes == null) {
            Attribute attribute = attributesC_getAttributes.get(Obj.JAVA_ATTRIBUTES[0]);
            if (attribute != null) {
                Attribute attribute2 = (Attribute) attribute.clone();
                for (int i2 = 0; i2 < Obj.JAVA_OBJECT_CLASSES.length; i2++) {
                    attribute2.remove(Obj.JAVA_OBJECT_CLASSES_LOWER[i2]);
                    attribute2.remove(Obj.JAVA_OBJECT_CLASSES[i2]);
                }
                attributesC_getAttributes.put(attribute2);
            }
            for (int i3 = 1; i3 < Obj.JAVA_ATTRIBUTES.length; i3++) {
                attributesC_getAttributes.remove(Obj.JAVA_ATTRIBUTES[i3]);
            }
            attributes = attributesC_getAttributes;
        }
        if (obj != null) {
            attributes = Obj.determineBindAttrs(this.addrEncodingSeparator, obj, attributes, attributes != attributes, name, this, this.envprops);
        }
        String strFullyQualifiedName = fullyQualifiedName(name);
        LdapResult ldapResultDelete = this.clnt.delete(strFullyQualifiedName, this.reqCtls);
        this.respCtls = ldapResultDelete.resControls;
        if (ldapResultDelete.status != 0) {
            processReturnCode(ldapResultDelete, name);
            return;
        }
        Throwable th2 = null;
        try {
            ldapResultDelete = this.clnt.add(new LdapEntry(strFullyQualifiedName, addRdnAttributes(strFullyQualifiedName, attributes, attributes != attributes)), this.reqCtls);
            if (ldapResultDelete.resControls != null) {
                this.respCtls = appendVector(this.respCtls, ldapResultDelete.resControls);
            }
        } catch (IOException | NamingException e7) {
            th2 = e7;
        }
        if ((th2 != null && !(th2 instanceof LdapReferralException)) || ldapResultDelete.status != 0) {
            LdapResult ldapResultAdd = this.clnt.add(new LdapEntry(strFullyQualifiedName, attributes2), this.reqCtls);
            if (ldapResultAdd.resControls != null) {
                this.respCtls = appendVector(this.respCtls, ldapResultAdd.resControls);
            }
            if (th2 == null) {
                processReturnCode(ldapResultDelete, name);
            }
        }
        if (th2 instanceof NamingException) {
            throw ((NamingException) th2);
        }
        if (th2 instanceof IOException) {
            throw ((IOException) th2);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_unbind(Name name, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        try {
            ensureOpen();
            String strFullyQualifiedName = fullyQualifiedName(name);
            LdapResult ldapResultDelete = this.clnt.delete(strFullyQualifiedName, this.reqCtls);
            this.respCtls = ldapResultDelete.resControls;
            adjustDeleteStatus(strFullyQualifiedName, ldapResultDelete);
            if (ldapResultDelete.status != 0) {
                processReturnCode(ldapResultDelete, name);
            }
        } catch (LdapReferralException e2) {
            e = e2;
            if (this.handleReferrals == 2) {
                throw continuation.fillInException(e);
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    ldapReferralContext.unbind(name);
                    ldapReferralContext.close();
                    return;
                } catch (LdapReferralException e3) {
                    e = e3;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (IOException e4) {
            CommunicationException communicationException = new CommunicationException(e4.getMessage());
            communicationException.setRootCause(e4);
            throw continuation.fillInException(communicationException);
        } catch (NamingException e5) {
            throw continuation.fillInException(e5);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_rename(Name name, Name name2, Continuation continuation) throws NamingException {
        Name prefix;
        Name name3;
        String strFullyQualifiedName = null;
        continuation.setError(this, name);
        try {
            ensureOpen();
            if (name.isEmpty()) {
                prefix = parser.parse("");
            } else {
                Name name4 = parser.parse(name.get(0));
                prefix = name4.getPrefix(name4.size() - 1);
            }
            if (name2 instanceof CompositeName) {
                name3 = parser.parse(name2.get(0));
            } else {
                name3 = name2;
            }
            Name prefix2 = name3.getPrefix(name3.size() - 1);
            if (!prefix.equals(prefix2)) {
                if (!this.clnt.isLdapv3) {
                    throw new InvalidNameException("LDAPv2 doesn't support changing the parent as a result of a rename");
                }
                strFullyQualifiedName = fullyQualifiedName(prefix2.toString());
            }
            LdapResult ldapResultModdn = this.clnt.moddn(fullyQualifiedName(name), name3.get(name3.size() - 1), this.deleteRDN, strFullyQualifiedName, this.reqCtls);
            this.respCtls = ldapResultModdn.resControls;
            if (ldapResultModdn.status != 0) {
                processReturnCode(ldapResultModdn, name);
            }
        } catch (LdapReferralException e2) {
            e = e2;
            e.setNewRdn(null);
            if (0 != 0) {
                NamingException partialResultException = new PartialResultException("Cannot continue referral processing when newSuperior is nonempty: " + ((String) null));
                partialResultException.setRootCause(continuation.fillInException(e));
                throw continuation.fillInException(partialResultException);
            }
            if (this.handleReferrals == 2) {
                throw continuation.fillInException(e);
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    ldapReferralContext.rename(name, name2);
                    ldapReferralContext.close();
                    return;
                } catch (LdapReferralException e3) {
                    e = e3;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (IOException e4) {
            NamingException communicationException = new CommunicationException(e4.getMessage());
            communicationException.setRootCause(e4);
            throw continuation.fillInException(communicationException);
        } catch (NamingException e5) {
            throw continuation.fillInException(e5);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    protected Context c_createSubcontext(Name name, Continuation continuation) throws NamingException {
        return c_createSubcontext(name, null, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected DirContext c_createSubcontext(Name name, Attributes attributes, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        try {
            ensureOpen();
            if (attributes == null) {
                BasicAttribute basicAttribute = new BasicAttribute(Obj.JAVA_ATTRIBUTES[0], Obj.JAVA_OBJECT_CLASSES[0]);
                basicAttribute.add(JSplitPane.TOP);
                attributes = new BasicAttributes(true);
                attributes.put(basicAttribute);
            }
            String strFullyQualifiedName = fullyQualifiedName(name);
            LdapResult ldapResultAdd = this.clnt.add(new LdapEntry(strFullyQualifiedName, addRdnAttributes(strFullyQualifiedName, attributes, attributes != attributes)), this.reqCtls);
            this.respCtls = ldapResultAdd.resControls;
            if (ldapResultAdd.status != 0) {
                processReturnCode(ldapResultAdd, name);
                return null;
            }
            return new LdapCtx(this, strFullyQualifiedName);
        } catch (LdapReferralException e2) {
            e = e2;
            if (this.handleReferrals == 2) {
                throw continuation.fillInException(e);
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    DirContext dirContextCreateSubcontext = ldapReferralContext.createSubcontext(name, attributes);
                    ldapReferralContext.close();
                    return dirContextCreateSubcontext;
                } catch (LdapReferralException e3) {
                    e = e3;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (IOException e4) {
            CommunicationException communicationException = new CommunicationException(e4.getMessage());
            communicationException.setRootCause(e4);
            throw continuation.fillInException(communicationException);
        } catch (NamingException e5) {
            throw continuation.fillInException(e5);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    protected void c_destroySubcontext(Name name, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        try {
            ensureOpen();
            String strFullyQualifiedName = fullyQualifiedName(name);
            LdapResult ldapResultDelete = this.clnt.delete(strFullyQualifiedName, this.reqCtls);
            this.respCtls = ldapResultDelete.resControls;
            adjustDeleteStatus(strFullyQualifiedName, ldapResultDelete);
            if (ldapResultDelete.status != 0) {
                processReturnCode(ldapResultDelete, name);
            }
        } catch (LdapReferralException e2) {
            e = e2;
            if (this.handleReferrals == 2) {
                throw continuation.fillInException(e);
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    ldapReferralContext.destroySubcontext(name);
                    ldapReferralContext.close();
                    return;
                } catch (LdapReferralException e3) {
                    e = e3;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (IOException e4) {
            CommunicationException communicationException = new CommunicationException(e4.getMessage());
            communicationException.setRootCause(e4);
            throw continuation.fillInException(communicationException);
        } catch (NamingException e5) {
            throw continuation.fillInException(e5);
        }
    }

    private static Attributes addRdnAttributes(String str, Attributes attributes, boolean z2) throws NamingException {
        if (str.equals("")) {
            return attributes;
        }
        List<Rdn> rdns = new javax.naming.ldap.LdapName(str).getRdns();
        NamingEnumeration<? extends Attribute> all = rdns.get(rdns.size() - 1).toAttributes().getAll();
        while (all.hasMore()) {
            Attribute next = all.next();
            if (attributes.get(next.getID()) == null && (attributes.isCaseIgnored() || !containsIgnoreCase(attributes.getIDs(), next.getID()))) {
                if (!z2) {
                    attributes = (Attributes) attributes.clone();
                    z2 = true;
                }
                attributes.put(next);
            }
        }
        return attributes;
    }

    private static boolean containsIgnoreCase(NamingEnumeration<String> namingEnumeration, String str) throws NamingException {
        while (namingEnumeration.hasMore()) {
            if (namingEnumeration.next().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    private void adjustDeleteStatus(String str, LdapResult ldapResult) {
        if (ldapResult.status == 32 && ldapResult.matchedDN != null) {
            try {
                if (parser.parse(str).size() - parser.parse(ldapResult.matchedDN).size() == 1) {
                    ldapResult.status = 0;
                }
            } catch (NamingException e2) {
            }
        }
    }

    private static <T> Vector<T> appendVector(Vector<T> vector, Vector<T> vector2) {
        if (vector == null) {
            vector = vector2;
        } else {
            for (int i2 = 0; i2 < vector2.size(); i2++) {
                vector.addElement(vector2.elementAt(i2));
            }
        }
        return vector;
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    protected Object c_lookupLink(Name name, Continuation continuation) throws NamingException {
        return c_lookup(name, continuation);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v53, types: [javax.naming.directory.Attributes] */
    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    protected Object c_lookup(Name name, Continuation continuation) throws NamingException {
        BasicAttributes basicAttributes;
        continuation.setError(this, name);
        Object ldapCtx = null;
        try {
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(0);
            searchControls.setReturningAttributes(null);
            searchControls.setReturningObjFlag(true);
            LdapResult ldapResultDoSearchOnce = doSearchOnce(name, "(objectClass=*)", searchControls, true);
            this.respCtls = ldapResultDoSearchOnce.resControls;
            if (ldapResultDoSearchOnce.status != 0) {
                processReturnCode(ldapResultDoSearchOnce, name);
            }
            if (ldapResultDoSearchOnce.entries == null || ldapResultDoSearchOnce.entries.size() != 1) {
                basicAttributes = new BasicAttributes(true);
            } else {
                LdapEntry ldapEntryElementAt = ldapResultDoSearchOnce.entries.elementAt(0);
                basicAttributes = ldapEntryElementAt.attributes;
                Vector<Control> vector = ldapEntryElementAt.respCtls;
                if (vector != null) {
                    appendVector(this.respCtls, vector);
                }
            }
            if (basicAttributes.get(Obj.JAVA_ATTRIBUTES[2]) != null) {
                ldapCtx = Obj.decodeObject(basicAttributes);
            }
            if (ldapCtx == null) {
                ldapCtx = new LdapCtx(this, fullyQualifiedName(name));
            }
            try {
                return DirectoryManager.getObjectInstance(ldapCtx, name, this, this.envprops, basicAttributes);
            } catch (NamingException e2) {
                throw continuation.fillInException(e2);
            } catch (Exception e3) {
                NamingException namingException = new NamingException("problem generating object using object factory");
                namingException.setRootCause(e3);
                throw continuation.fillInException(namingException);
            }
        } catch (LdapReferralException e4) {
            e = e4;
            if (this.handleReferrals == 2) {
                throw continuation.fillInException(e);
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    Object objLookup = ldapReferralContext.lookup(name);
                    ldapReferralContext.close();
                    return objLookup;
                } catch (LdapReferralException e5) {
                    e = e5;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (NamingException e6) {
            throw continuation.fillInException(e6);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    protected NamingEnumeration<NameClassPair> c_list(Name name, Continuation continuation) throws NamingException {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{Obj.JAVA_ATTRIBUTES[0], Obj.JAVA_ATTRIBUTES[2]});
        searchControls.setReturningObjFlag(true);
        continuation.setError(this, name);
        LdapResult ldapResultDoSearch = null;
        try {
            ldapResultDoSearch = doSearch(name, "(objectClass=*)", searchControls, true, true);
            if (ldapResultDoSearch.status != 0 || ldapResultDoSearch.referrals != null) {
                processReturnCode(ldapResultDoSearch, name);
            }
            return new LdapNamingEnumeration(this, ldapResultDoSearch, name, continuation);
        } catch (LdapReferralException e2) {
            e = e2;
            if (this.handleReferrals == 2) {
                throw continuation.fillInException(e);
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    NamingEnumeration<NameClassPair> list = ldapReferralContext.list(name);
                    ldapReferralContext.close();
                    return list;
                } catch (LdapReferralException e3) {
                    e = e3;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (LimitExceededException e4) {
            LdapNamingEnumeration ldapNamingEnumeration = new LdapNamingEnumeration(this, ldapResultDoSearch, name, continuation);
            ldapNamingEnumeration.setNamingException((LimitExceededException) continuation.fillInException(e4));
            return ldapNamingEnumeration;
        } catch (PartialResultException e5) {
            LdapNamingEnumeration ldapNamingEnumeration2 = new LdapNamingEnumeration(this, ldapResultDoSearch, name, continuation);
            ldapNamingEnumeration2.setNamingException((PartialResultException) continuation.fillInException(e5));
            return ldapNamingEnumeration2;
        } catch (NamingException e6) {
            throw continuation.fillInException(e6);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    protected NamingEnumeration<Binding> c_listBindings(Name name, Continuation continuation) throws NamingException {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(null);
        searchControls.setReturningObjFlag(true);
        continuation.setError(this, name);
        LdapResult ldapResultDoSearch = null;
        try {
            ldapResultDoSearch = doSearch(name, "(objectClass=*)", searchControls, true, true);
            if (ldapResultDoSearch.status != 0 || ldapResultDoSearch.referrals != null) {
                processReturnCode(ldapResultDoSearch, name);
            }
            return new LdapBindingEnumeration(this, ldapResultDoSearch, name, continuation);
        } catch (LdapReferralException e2) {
            e = e2;
            if (this.handleReferrals == 2) {
                throw continuation.fillInException(e);
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    NamingEnumeration<Binding> namingEnumerationListBindings = ldapReferralContext.listBindings(name);
                    ldapReferralContext.close();
                    return namingEnumerationListBindings;
                } catch (LdapReferralException e3) {
                    e = e3;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (LimitExceededException e4) {
            LdapBindingEnumeration ldapBindingEnumeration = new LdapBindingEnumeration(this, ldapResultDoSearch, name, continuation);
            ldapBindingEnumeration.setNamingException(continuation.fillInException(e4));
            return ldapBindingEnumeration;
        } catch (PartialResultException e5) {
            LdapBindingEnumeration ldapBindingEnumeration2 = new LdapBindingEnumeration(this, ldapResultDoSearch, name, continuation);
            ldapBindingEnumeration2.setNamingException(continuation.fillInException(e5));
            return ldapBindingEnumeration2;
        } catch (NamingException e6) {
            throw continuation.fillInException(e6);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.AtomicContext, com.sun.jndi.toolkit.ctx.ComponentContext
    protected NameParser c_getNameParser(Name name, Continuation continuation) throws NamingException {
        continuation.setSuccess();
        return parser;
    }

    @Override // javax.naming.Context
    public String getNameInNamespace() {
        return this.currentDN;
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext, javax.naming.Context
    public Name composeName(Name name, Name name2) throws NamingException {
        if ((name instanceof javax.naming.ldap.LdapName) && (name2 instanceof javax.naming.ldap.LdapName)) {
            Name name3 = (Name) name2.clone();
            name3.addAll(name);
            return new CompositeName().add(name3.toString());
        }
        if (!(name instanceof CompositeName)) {
            name = new CompositeName().add(name.toString());
        }
        if (!(name2 instanceof CompositeName)) {
            name2 = new CompositeName().add(name2.toString());
        }
        int size = name2.size() - 1;
        if (name.isEmpty() || name2.isEmpty() || name.get(0).equals("") || name2.get(size).equals("")) {
            return super.composeName(name, name2);
        }
        Name name4 = (Name) name2.clone();
        name4.addAll(name);
        if (this.parentIsLdapCtx) {
            String strConcatNames = concatNames(name4.get(size + 1), name4.get(size));
            name4.remove(size + 1);
            name4.remove(size);
            name4.add(size, strConcatNames);
        }
        return name4;
    }

    private String fullyQualifiedName(Name name) {
        return name.isEmpty() ? this.currentDN : fullyQualifiedName(name.get(0));
    }

    private String fullyQualifiedName(String str) {
        return concatNames(str, this.currentDN);
    }

    private static String concatNames(String str, String str2) {
        if (str == null || str.equals("")) {
            return str2;
        }
        if (str2 == null || str2.equals("")) {
            return str;
        }
        return str + "," + str2;
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected Attributes c_getAttributes(Name name, String[] strArr, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(0);
        searchControls.setReturningAttributes(strArr);
        try {
            LdapResult ldapResultDoSearchOnce = doSearchOnce(name, "(objectClass=*)", searchControls, true);
            this.respCtls = ldapResultDoSearchOnce.resControls;
            if (ldapResultDoSearchOnce.status != 0) {
                processReturnCode(ldapResultDoSearchOnce, name);
            }
            if (ldapResultDoSearchOnce.entries == null || ldapResultDoSearchOnce.entries.size() != 1) {
                return new BasicAttributes(true);
            }
            LdapEntry ldapEntryElementAt = ldapResultDoSearchOnce.entries.elementAt(0);
            Vector<Control> vector = ldapEntryElementAt.respCtls;
            if (vector != null) {
                appendVector(this.respCtls, vector);
            }
            setParents(ldapEntryElementAt.attributes, (Name) name.clone());
            return ldapEntryElementAt.attributes;
        } catch (LdapReferralException e2) {
            e = e2;
            if (this.handleReferrals == 2) {
                throw continuation.fillInException(e);
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    Attributes attributes = ldapReferralContext.getAttributes(name, strArr);
                    ldapReferralContext.close();
                    return attributes;
                } catch (LdapReferralException e3) {
                    e = e3;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (NamingException e4) {
            throw continuation.fillInException(e4);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected void c_modifyAttributes(Name name, int i2, Attributes attributes, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        try {
            ensureOpen();
            if (attributes == null || attributes.size() == 0) {
                return;
            }
            String strFullyQualifiedName = fullyQualifiedName(name);
            int iConvertToLdapModCode = convertToLdapModCode(i2);
            int[] iArr = new int[attributes.size()];
            Attribute[] attributeArr = new Attribute[attributes.size()];
            NamingEnumeration<? extends Attribute> all = attributes.getAll();
            for (int i3 = 0; i3 < iArr.length && all.hasMore(); i3++) {
                iArr[i3] = iConvertToLdapModCode;
                attributeArr[i3] = all.next();
            }
            LdapResult ldapResultModify = this.clnt.modify(strFullyQualifiedName, iArr, attributeArr, this.reqCtls);
            this.respCtls = ldapResultModify.resControls;
            if (ldapResultModify.status != 0) {
                processReturnCode(ldapResultModify, name);
            }
        } catch (LdapReferralException e2) {
            e = e2;
            if (this.handleReferrals == 2) {
                throw continuation.fillInException(e);
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    ldapReferralContext.modifyAttributes(name, i2, attributes);
                    ldapReferralContext.close();
                    return;
                } catch (LdapReferralException e3) {
                    e = e3;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (IOException e4) {
            CommunicationException communicationException = new CommunicationException(e4.getMessage());
            communicationException.setRootCause(e4);
            throw continuation.fillInException(communicationException);
        } catch (NamingException e5) {
            throw continuation.fillInException(e5);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected void c_modifyAttributes(Name name, ModificationItem[] modificationItemArr, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        try {
            ensureOpen();
            if (modificationItemArr == null || modificationItemArr.length == 0) {
                return;
            }
            String strFullyQualifiedName = fullyQualifiedName(name);
            int[] iArr = new int[modificationItemArr.length];
            Attribute[] attributeArr = new Attribute[modificationItemArr.length];
            for (int i2 = 0; i2 < iArr.length; i2++) {
                ModificationItem modificationItem = modificationItemArr[i2];
                iArr[i2] = convertToLdapModCode(modificationItem.getModificationOp());
                attributeArr[i2] = modificationItem.getAttribute();
            }
            LdapResult ldapResultModify = this.clnt.modify(strFullyQualifiedName, iArr, attributeArr, this.reqCtls);
            this.respCtls = ldapResultModify.resControls;
            if (ldapResultModify.status != 0) {
                processReturnCode(ldapResultModify, name);
            }
        } catch (LdapReferralException e2) {
            e = e2;
            if (this.handleReferrals == 2) {
                throw continuation.fillInException(e);
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    ldapReferralContext.modifyAttributes(name, modificationItemArr);
                    ldapReferralContext.close();
                    return;
                } catch (LdapReferralException e3) {
                    e = e3;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (IOException e4) {
            CommunicationException communicationException = new CommunicationException(e4.getMessage());
            communicationException.setRootCause(e4);
            throw continuation.fillInException(communicationException);
        } catch (NamingException e5) {
            throw continuation.fillInException(e5);
        }
    }

    private static int convertToLdapModCode(int i2) {
        switch (i2) {
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 1;
            default:
                throw new IllegalArgumentException("Invalid modification code");
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected DirContext c_getSchema(Name name, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        try {
            return getSchemaTree(name);
        } catch (NamingException e2) {
            throw continuation.fillInException(e2);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected DirContext c_getSchemaClassDefinition(Name name, Continuation continuation) throws NamingException {
        continuation.setError(this, name);
        try {
            Attribute attribute = c_getAttributes(name, new String[]{"objectclass"}, continuation).get("objectclass");
            if (attribute == null || attribute.size() == 0) {
                return EMPTY_SCHEMA;
            }
            Context context = (Context) c_getSchema(name, continuation).lookup("ClassDefinition");
            HierMemDirCtx hierMemDirCtx = new HierMemDirCtx();
            NamingEnumeration<?> all = attribute.getAll();
            while (all.hasMoreElements()) {
                String str = (String) all.nextElement2();
                hierMemDirCtx.bind(str, (DirContext) context.lookup(str));
            }
            hierMemDirCtx.setReadOnly(new SchemaViolationException("Cannot update schema object"));
            return hierMemDirCtx;
        } catch (NamingException e2) {
            throw continuation.fillInException(e2);
        }
    }

    private DirContext getSchemaTree(Name name) throws NamingException {
        String schemaEntry = getSchemaEntry(name, true);
        DirContext dirContextBuildSchemaTree = this.schemaTrees.get(schemaEntry);
        if (dirContextBuildSchemaTree == null) {
            dirContextBuildSchemaTree = buildSchemaTree(schemaEntry);
            this.schemaTrees.put(schemaEntry, dirContextBuildSchemaTree);
        }
        return dirContextBuildSchemaTree;
    }

    private DirContext buildSchemaTree(String str) throws NamingException {
        NamingEnumeration<SearchResult> namingEnumerationSearchAux = searchAux(new CompositeName().add(str), "(objectClass=subschema)", new SearchControls(0, 0L, 0, SCHEMA_ATTRIBUTES, true, false), false, true, new Continuation());
        if (!namingEnumerationSearchAux.hasMore()) {
            throw new OperationNotSupportedException("Cannot get read subschemasubentry: " + str);
        }
        SearchResult next = namingEnumerationSearchAux.next();
        namingEnumerationSearchAux.close();
        Object object = next.getObject();
        if (!(object instanceof LdapCtx)) {
            throw new NamingException("Cannot get schema object as DirContext: " + str);
        }
        return LdapSchemaCtx.createSchemaTree(this.envprops, str, (LdapCtx) object, next.getAttributes(), this.netscapeSchemaBug);
    }

    private String getSchemaEntry(Name name, boolean z2) throws NamingException {
        try {
            NamingEnumeration<SearchResult> namingEnumerationSearchAux = searchAux(name, "objectclass=*", new SearchControls(0, 0L, 0, new String[]{"subschemasubentry"}, false, false), z2, true, new Continuation());
            if (!namingEnumerationSearchAux.hasMoreElements()) {
                throw new ConfigurationException("Requesting schema of nonexistent entry: " + ((Object) name));
            }
            SearchResult next = namingEnumerationSearchAux.next();
            namingEnumerationSearchAux.close();
            Attribute attribute = next.getAttributes().get("subschemasubentry");
            if (attribute == null || attribute.size() < 0) {
                if (this.currentDN.length() == 0 && name.isEmpty()) {
                    throw new OperationNotSupportedException("Cannot read subschemasubentry of root DSE");
                }
                return getSchemaEntry(new CompositeName(), false);
            }
            return (String) attribute.get();
        } catch (NamingException e2) {
            if (!this.clnt.isLdapv3 && this.currentDN.length() == 0 && name.isEmpty()) {
                throw new OperationNotSupportedException("Cannot get schema information from server");
            }
            throw e2;
        }
    }

    void setParents(Attributes attributes, Name name) throws NamingException {
        NamingEnumeration<? extends Attribute> all = attributes.getAll();
        while (all.hasMore()) {
            ((LdapAttribute) all.next()).setParent(this, name);
        }
    }

    String getURL() {
        if (this.url == null) {
            this.url = LdapURL.toUrlString(this.hostname, this.port_number, this.currentDN, this.hasLdapsScheme);
        }
        return this.url;
    }

    protected NamingEnumeration<SearchResult> c_search(Name name, Attributes attributes, Continuation continuation) throws NamingException {
        return c_search(name, attributes, (String[]) null, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected NamingEnumeration<SearchResult> c_search(Name name, Attributes attributes, String[] strArr, Continuation continuation) throws NamingException {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(strArr);
        try {
            return c_search(name, SearchFilter.format(attributes), searchControls, continuation);
        } catch (NamingException e2) {
            continuation.setError(this, name);
            throw continuation.fillInException(e2);
        }
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected NamingEnumeration<SearchResult> c_search(Name name, String str, SearchControls searchControls, Continuation continuation) throws NamingException {
        return searchAux(name, str, cloneSearchControls(searchControls), true, this.waitForReply, continuation);
    }

    @Override // com.sun.jndi.toolkit.ctx.ComponentDirContext
    protected NamingEnumeration<SearchResult> c_search(Name name, String str, Object[] objArr, SearchControls searchControls, Continuation continuation) throws NamingException {
        try {
            return c_search(name, SearchFilter.format(str, objArr), searchControls, continuation);
        } catch (NamingException e2) {
            continuation.setError(this, name);
            throw continuation.fillInException(e2);
        }
    }

    NamingEnumeration<SearchResult> searchAux(Name name, String str, SearchControls searchControls, boolean z2, boolean z3, Continuation continuation) throws NamingException {
        LdapResult ldapResultDoSearch = null;
        String[] strArr = new String[2];
        if (searchControls == null) {
            searchControls = new SearchControls();
        }
        String[] returningAttributes = searchControls.getReturningAttributes();
        if (searchControls.getReturningObjFlag() && returningAttributes != null) {
            boolean z4 = false;
            int length = returningAttributes.length - 1;
            while (true) {
                if (length < 0) {
                    break;
                }
                if (!returningAttributes[length].equals("*")) {
                    length--;
                } else {
                    z4 = true;
                    break;
                }
            }
            if (!z4) {
                String[] strArr2 = new String[returningAttributes.length + Obj.JAVA_ATTRIBUTES.length];
                System.arraycopy(returningAttributes, 0, strArr2, 0, returningAttributes.length);
                System.arraycopy(Obj.JAVA_ATTRIBUTES, 0, strArr2, returningAttributes.length, Obj.JAVA_ATTRIBUTES.length);
                searchControls.setReturningAttributes(strArr2);
            }
        }
        SearchArgs searchArgs = new SearchArgs(name, str, searchControls, returningAttributes);
        continuation.setError(this, name);
        try {
            if (searchToCompare(str, searchControls, strArr)) {
                ldapResultDoSearch = compare(name, strArr[0], strArr[1]);
                if (!ldapResultDoSearch.compareToSearchResult(fullyQualifiedName(name))) {
                    processReturnCode(ldapResultDoSearch, name);
                }
            } else {
                ldapResultDoSearch = doSearch(name, str, searchControls, z2, z3);
                processReturnCode(ldapResultDoSearch, name);
            }
            return new LdapSearchEnumeration(this, ldapResultDoSearch, fullyQualifiedName(name), searchArgs, continuation);
        } catch (LdapReferralException e2) {
            e = e2;
            if (this.handleReferrals == 2) {
                throw continuation.fillInException(e);
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    NamingEnumeration<SearchResult> namingEnumerationSearch = ldapReferralContext.search(name, str, searchControls);
                    ldapReferralContext.close();
                    return namingEnumerationSearch;
                } catch (LdapReferralException e3) {
                    e = e3;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (IOException e4) {
            CommunicationException communicationException = new CommunicationException(e4.getMessage());
            communicationException.setRootCause(e4);
            throw continuation.fillInException(communicationException);
        } catch (LimitExceededException e5) {
            LdapSearchEnumeration ldapSearchEnumeration = new LdapSearchEnumeration(this, ldapResultDoSearch, fullyQualifiedName(name), searchArgs, continuation);
            ldapSearchEnumeration.setNamingException(e5);
            return ldapSearchEnumeration;
        } catch (PartialResultException e6) {
            LdapSearchEnumeration ldapSearchEnumeration2 = new LdapSearchEnumeration(this, ldapResultDoSearch, fullyQualifiedName(name), searchArgs, continuation);
            ldapSearchEnumeration2.setNamingException(e6);
            return ldapSearchEnumeration2;
        } catch (NamingException e7) {
            throw continuation.fillInException(e7);
        }
    }

    LdapResult getSearchReply(LdapClient ldapClient, LdapResult ldapResult) throws NamingException {
        if (this.clnt != ldapClient) {
            throw new CommunicationException("Context's connection changed; unable to continue enumeration");
        }
        try {
            return ldapClient.getSearchReply(this.batchSize, ldapResult, this.binaryAttrs);
        } catch (IOException e2) {
            CommunicationException communicationException = new CommunicationException(e2.getMessage());
            communicationException.setRootCause(e2);
            throw communicationException;
        }
    }

    private LdapResult doSearchOnce(Name name, String str, SearchControls searchControls, boolean z2) throws NamingException {
        int i2 = this.batchSize;
        this.batchSize = 2;
        LdapResult ldapResultDoSearch = doSearch(name, str, searchControls, z2, true);
        this.batchSize = i2;
        return ldapResultDoSearch;
    }

    private LdapResult doSearch(Name name, String str, SearchControls searchControls, boolean z2, boolean z3) throws NamingException {
        int i2;
        String strFullyQualifiedName;
        ensureOpen();
        try {
            switch (searchControls.getSearchScope()) {
                case 0:
                    i2 = 0;
                    break;
                case 1:
                default:
                    i2 = 1;
                    break;
                case 2:
                    i2 = 2;
                    break;
            }
            String[] returningAttributes = searchControls.getReturningAttributes();
            if (returningAttributes != null && returningAttributes.length == 0) {
                returningAttributes = new String[]{SerializerConstants.XMLVERSION11};
            }
            if (z2) {
                strFullyQualifiedName = fullyQualifiedName(name);
            } else {
                strFullyQualifiedName = name.isEmpty() ? "" : name.get(0);
            }
            String str2 = strFullyQualifiedName;
            int timeLimit = searchControls.getTimeLimit();
            int i3 = 0;
            if (timeLimit > 0) {
                i3 = (timeLimit / 1000) + 1;
            }
            LdapResult ldapResultSearch = this.clnt.search(str2, i2, this.derefAliases, (int) searchControls.getCountLimit(), i3, searchControls.getReturningObjFlag() ? false : this.typesOnly, returningAttributes, str, this.batchSize, this.reqCtls, this.binaryAttrs, z3, this.replyQueueSize);
            this.respCtls = ldapResultSearch.resControls;
            return ldapResultSearch;
        } catch (IOException e2) {
            CommunicationException communicationException = new CommunicationException(e2.getMessage());
            communicationException.setRootCause(e2);
            throw communicationException;
        }
    }

    private static boolean searchToCompare(String str, SearchControls searchControls, String[] strArr) {
        String[] returningAttributes;
        if (searchControls.getSearchScope() != 0 || (returningAttributes = searchControls.getReturningAttributes()) == null || returningAttributes.length != 0 || !filterToAssertion(str, strArr)) {
            return false;
        }
        return true;
    }

    private static boolean filterToAssertion(String str, String[] strArr) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, "=");
        if (stringTokenizer.countTokens() != 2) {
            return false;
        }
        strArr[0] = stringTokenizer.nextToken();
        strArr[1] = stringTokenizer.nextToken();
        if (strArr[1].indexOf(42) != -1) {
            return false;
        }
        boolean z2 = false;
        int length = strArr[1].length();
        if (strArr[0].charAt(0) == '(' && strArr[1].charAt(length - 1) == ')') {
            z2 = true;
        } else if (strArr[0].charAt(0) == '(' || strArr[1].charAt(length - 1) == ')') {
            return false;
        }
        if (new StringTokenizer(strArr[0], "()&|!=~><*", true).countTokens() != (z2 ? 2 : 1)) {
            return false;
        }
        if (new StringTokenizer(strArr[1], "()&|!=~><*", true).countTokens() != (z2 ? 2 : 1)) {
            return false;
        }
        if (z2) {
            strArr[0] = strArr[0].substring(1);
            strArr[1] = strArr[1].substring(0, length - 1);
            return true;
        }
        return true;
    }

    private LdapResult compare(Name name, String str, String str2) throws IOException, NamingException {
        ensureOpen();
        LdapResult ldapResultCompare = this.clnt.compare(fullyQualifiedName(name), str, str2, this.reqCtls);
        this.respCtls = ldapResultCompare.resControls;
        return ldapResultCompare;
    }

    private static SearchControls cloneSearchControls(SearchControls searchControls) {
        if (searchControls == null) {
            return null;
        }
        String[] returningAttributes = searchControls.getReturningAttributes();
        if (returningAttributes != null) {
            String[] strArr = new String[returningAttributes.length];
            System.arraycopy(returningAttributes, 0, strArr, 0, returningAttributes.length);
            returningAttributes = strArr;
        }
        return new SearchControls(searchControls.getSearchScope(), searchControls.getCountLimit(), searchControls.getTimeLimit(), returningAttributes, searchControls.getReturningObjFlag(), searchControls.getDerefLinkFlag());
    }

    @Override // com.sun.jndi.toolkit.ctx.PartialCompositeContext
    protected Hashtable<String, Object> p_getEnvironment() {
        return this.envprops;
    }

    @Override // javax.naming.Context
    public Hashtable<String, Object> getEnvironment() throws NamingException {
        return this.envprops == null ? new Hashtable<>(5, 0.75f) : (Hashtable) this.envprops.clone();
    }

    @Override // javax.naming.Context
    public Object removeFromEnvironment(String str) throws NamingException {
        if (this.envprops == null || this.envprops.get(str) == null) {
            return null;
        }
        switch (str) {
            case "java.naming.ldap.ref.separator":
                this.addrEncodingSeparator = '#';
                break;
            case "java.naming.ldap.typesOnly":
                this.typesOnly = false;
                break;
            case "java.naming.ldap.deleteRDN":
                this.deleteRDN = true;
                break;
            case "java.naming.ldap.derefAliases":
                this.derefAliases = 3;
                break;
            case "java.naming.batchsize":
                this.batchSize = 1;
                break;
            case "java.naming.ldap.referral.limit":
                this.referralHopLimit = 10;
                break;
            case "java.naming.referral":
                setReferralMode(null, true);
                break;
            case "java.naming.ldap.attributes.binary":
                setBinaryAttributes(null);
                break;
            case "com.sun.jndi.ldap.connect.timeout":
                this.connectTimeout = -1;
                break;
            case "com.sun.jndi.ldap.read.timeout":
                this.readTimeout = -1;
                break;
            case "com.sun.jndi.ldap.search.waitForReply":
                this.waitForReply = true;
                break;
            case "com.sun.jndi.ldap.search.replyQueueSize":
                this.replyQueueSize = -1;
                break;
            case "java.naming.security.protocol":
                closeConnection(false);
                if (this.useSsl && !this.hasLdapsScheme) {
                    this.useSsl = false;
                    this.url = null;
                    if (this.useDefaultPortNumber) {
                        this.port_number = DEFAULT_PORT;
                        break;
                    }
                }
                break;
            case "java.naming.ldap.version":
            case "java.naming.ldap.factory.socket":
                closeConnection(false);
                break;
            case "java.naming.security.authentication":
            case "java.naming.security.principal":
            case "java.naming.security.credentials":
                this.sharable = false;
                break;
        }
        this.envprops = (Hashtable) this.envprops.clone();
        return this.envprops.remove(str);
    }

    @Override // javax.naming.Context
    public Object addToEnvironment(String str, Object obj) throws NamingException {
        if (obj == null) {
            return removeFromEnvironment(str);
        }
        switch (str) {
            case "java.naming.ldap.ref.separator":
                setRefSeparator((String) obj);
                break;
            case "java.naming.ldap.typesOnly":
                setTypesOnly((String) obj);
                break;
            case "java.naming.ldap.deleteRDN":
                setDeleteRDN((String) obj);
                break;
            case "java.naming.ldap.derefAliases":
                setDerefAliases((String) obj);
                break;
            case "java.naming.batchsize":
                setBatchSize((String) obj);
                break;
            case "java.naming.ldap.referral.limit":
                setReferralLimit((String) obj);
                break;
            case "java.naming.referral":
                setReferralMode((String) obj, true);
                break;
            case "java.naming.ldap.attributes.binary":
                setBinaryAttributes((String) obj);
                break;
            case "com.sun.jndi.ldap.connect.timeout":
                setConnectTimeout((String) obj);
                break;
            case "com.sun.jndi.ldap.read.timeout":
                setReadTimeout((String) obj);
                break;
            case "com.sun.jndi.ldap.search.waitForReply":
                setWaitForReply((String) obj);
                break;
            case "com.sun.jndi.ldap.search.replyQueueSize":
                setReplyQueueSize((String) obj);
                break;
            case "java.naming.security.protocol":
                closeConnection(false);
                if ("ssl".equals(obj)) {
                    this.useSsl = true;
                    this.url = null;
                    if (this.useDefaultPortNumber) {
                        this.port_number = DEFAULT_SSL_PORT;
                        break;
                    }
                }
                break;
            case "java.naming.ldap.version":
            case "java.naming.ldap.factory.socket":
                closeConnection(false);
                break;
            case "java.naming.security.authentication":
            case "java.naming.security.principal":
            case "java.naming.security.credentials":
                this.sharable = false;
                break;
        }
        this.envprops = this.envprops == null ? new Hashtable<>(5, 0.75f) : (Hashtable) this.envprops.clone();
        return this.envprops.put(str, obj);
    }

    void setProviderUrl(String str) {
        if (this.envprops != null) {
            this.envprops.put(Context.PROVIDER_URL, str);
        }
    }

    void setDomainName(String str) {
        if (this.envprops != null) {
            this.envprops.put(DOMAIN_NAME, str);
        }
    }

    private void initEnv() throws NamingException {
        if (this.envprops == null) {
            setReferralMode(null, false);
            return;
        }
        setBatchSize((String) this.envprops.get(Context.BATCHSIZE));
        setRefSeparator((String) this.envprops.get(REF_SEPARATOR));
        setDeleteRDN((String) this.envprops.get(DELETE_RDN));
        setTypesOnly((String) this.envprops.get(TYPES_ONLY));
        setDerefAliases((String) this.envprops.get(DEREF_ALIASES));
        setReferralLimit((String) this.envprops.get(REFERRAL_LIMIT));
        setBinaryAttributes((String) this.envprops.get(BINARY_ATTRIBUTES));
        this.bindCtls = cloneControls((Control[]) this.envprops.get(BIND_CONTROLS));
        setReferralMode((String) this.envprops.get(Context.REFERRAL), false);
        setConnectTimeout((String) this.envprops.get(CONNECT_TIMEOUT));
        setReadTimeout((String) this.envprops.get(READ_TIMEOUT));
        setWaitForReply((String) this.envprops.get(WAIT_FOR_REPLY));
        setReplyQueueSize((String) this.envprops.get(REPLY_QUEUE_SIZE));
    }

    private void setDeleteRDN(String str) {
        if (str != null && str.equalsIgnoreCase("false")) {
            this.deleteRDN = false;
        } else {
            this.deleteRDN = true;
        }
    }

    private void setTypesOnly(String str) {
        if (str != null && str.equalsIgnoreCase("true")) {
            this.typesOnly = true;
        } else {
            this.typesOnly = false;
        }
    }

    private void setBatchSize(String str) {
        if (str != null) {
            this.batchSize = Integer.parseInt(str);
        } else {
            this.batchSize = 1;
        }
    }

    private void setReferralMode(String str, boolean z2) {
        if (str != null) {
            switch (str) {
                case "follow-scheme":
                    this.handleReferrals = 4;
                    break;
                case "follow":
                    this.handleReferrals = 1;
                    break;
                case "throw":
                    this.handleReferrals = 2;
                    break;
                case "ignore":
                    this.handleReferrals = 3;
                    break;
                default:
                    throw new IllegalArgumentException("Illegal value for java.naming.referral property.");
            }
        } else {
            this.handleReferrals = 3;
        }
        if (this.handleReferrals == 3) {
            this.reqCtls = addControl(this.reqCtls, manageReferralControl);
        } else if (z2) {
            this.reqCtls = removeControl(this.reqCtls, manageReferralControl);
        }
    }

    private void setDerefAliases(String str) {
        if (str != null) {
            switch (str) {
                case "never":
                    this.derefAliases = 0;
                    return;
                case "searching":
                    this.derefAliases = 1;
                    return;
                case "finding":
                    this.derefAliases = 2;
                    return;
                case "always":
                    this.derefAliases = 3;
                    return;
                default:
                    throw new IllegalArgumentException("Illegal value for java.naming.ldap.derefAliases property.");
            }
        }
        this.derefAliases = 3;
    }

    private void setRefSeparator(String str) throws NamingException {
        if (str != null && str.length() > 0) {
            this.addrEncodingSeparator = str.charAt(0);
        } else {
            this.addrEncodingSeparator = '#';
        }
    }

    private void setReferralLimit(String str) {
        if (str != null) {
            this.referralHopLimit = Integer.parseInt(str);
            if (this.referralHopLimit == 0) {
                this.referralHopLimit = Integer.MAX_VALUE;
                return;
            }
            return;
        }
        this.referralHopLimit = 10;
    }

    void setHopCount(int i2) {
        this.hopCount = i2;
    }

    private void setConnectTimeout(String str) {
        if (str != null) {
            this.connectTimeout = Integer.parseInt(str);
        } else {
            this.connectTimeout = -1;
        }
    }

    private void setReplyQueueSize(String str) {
        if (str != null) {
            this.replyQueueSize = Integer.parseInt(str);
            if (this.replyQueueSize <= 0) {
                this.replyQueueSize = -1;
                return;
            }
            return;
        }
        this.replyQueueSize = -1;
    }

    private void setWaitForReply(String str) {
        if (str != null && str.equalsIgnoreCase("false")) {
            this.waitForReply = false;
        } else {
            this.waitForReply = true;
        }
    }

    private void setReadTimeout(String str) {
        if (str != null) {
            this.readTimeout = Integer.parseInt(str);
        } else {
            this.readTimeout = -1;
        }
    }

    private static Vector<Vector<String>> extractURLs(String str) {
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int iIndexOf = str.indexOf(10, i2);
            if (iIndexOf < 0) {
                break;
            }
            i2 = iIndexOf + 1;
            i3++;
        }
        Vector<Vector<String>> vector = new Vector<>(i3);
        int iIndexOf2 = str.indexOf(10);
        while (true) {
            int i4 = iIndexOf2 + 1;
            int iIndexOf3 = str.indexOf(10, i4);
            iIndexOf2 = iIndexOf3;
            if (iIndexOf3 >= 0) {
                Vector<String> vector2 = new Vector<>(1);
                vector2.addElement(str.substring(i4, iIndexOf2));
                vector.addElement(vector2);
            } else {
                Vector<String> vector3 = new Vector<>(1);
                vector3.addElement(str.substring(i4));
                vector.addElement(vector3);
                return vector;
            }
        }
    }

    private void setBinaryAttributes(String str) {
        if (str == null) {
            this.binaryAttrs = null;
            return;
        }
        this.binaryAttrs = new Hashtable<>(11, 0.75f);
        StringTokenizer stringTokenizer = new StringTokenizer(str.toLowerCase(Locale.ENGLISH), " ");
        while (stringTokenizer.hasMoreTokens()) {
            this.binaryAttrs.put(stringTokenizer.nextToken(), Boolean.TRUE);
        }
    }

    protected void finalize() {
        try {
            close();
        } catch (NamingException e2) {
        }
    }

    @Override // javax.naming.Context
    public synchronized void close() throws NamingException {
        if (this.eventSupport != null) {
            this.eventSupport.cleanup();
            removeUnsolicited();
        }
        if (this.enumCount > 0) {
            this.closeRequested = true;
        } else {
            closeConnection(false);
        }
    }

    @Override // javax.naming.ldap.LdapContext
    public void reconnect(Control[] controlArr) throws NamingException {
        this.envprops = this.envprops == null ? new Hashtable<>(5, 0.75f) : (Hashtable) this.envprops.clone();
        if (controlArr == null) {
            this.envprops.remove(BIND_CONTROLS);
            this.bindCtls = null;
        } else {
            Hashtable<String, Object> hashtable = this.envprops;
            Control[] controlArrCloneControls = cloneControls(controlArr);
            this.bindCtls = controlArrCloneControls;
            hashtable.put(BIND_CONTROLS, controlArrCloneControls);
        }
        this.sharable = false;
        ensureOpen();
    }

    private static String getMechsAllowedToSendCredentials() {
        PrivilegedAction privilegedAction = () -> {
            return System.getProperty(ALLOWED_MECHS_SP);
        };
        return System.getSecurityManager() == null ? (String) privilegedAction.run2() : (String) AccessController.doPrivileged(privilegedAction);
    }

    private static Set<String> getMechsFromPropertyValue(String str) {
        if (str == null || str.isEmpty()) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet();
        for (String str2 : str.trim().split("\\s*,\\s*")) {
            if (!str2.isEmpty()) {
                hashSet.add(str2);
            }
        }
        return Collections.unmodifiableSet(hashSet);
    }

    private boolean isConnectionEncrypted() {
        return this.hasLdapsScheme || this.clnt.isUpgradedToStartTls();
    }

    private void ensureCanTransmitCredentials(String str) throws NamingException {
        if (Separation.COLORANT_NONE.equalsIgnoreCase(str) || "anonymous".equalsIgnoreCase(str)) {
            return;
        }
        String str2 = (String) this.envprops.get(ALLOWED_MECHS_SP);
        boolean z2 = false;
        boolean z3 = (ALLOWED_MECHS_SP_VALUE == null && str2 == null) ? false : true;
        if (isConnectionEncrypted()) {
            return;
        }
        if (this.contextSeenStartTlsEnabled || z3) {
            if ("simple".equalsIgnoreCase(str) && !this.envprops.containsKey(Context.SECURITY_PRINCIPAL)) {
                return;
            }
            if (str2 == null) {
                z2 = true;
                str2 = ALLOWED_MECHS_SP_VALUE;
            }
            if ("all".equalsIgnoreCase(str2)) {
                return;
            }
            if (!(z2 ? MECHS_ALLOWED_BY_SP : getMechsFromPropertyValue(str2)).contains(str)) {
                throw new NamingException(UNSECURED_CRED_TRANSMIT_MSG);
            }
        }
    }

    private void ensureOpen() throws NamingException {
        ensureOpen(false);
    }

    private void ensureOpen(boolean z2) throws NamingException {
        try {
            if (this.clnt == null) {
                this.schemaTrees = new Hashtable<>(11, 0.75f);
                connect(z2);
            } else if (!this.sharable || z2) {
                synchronized (this.clnt) {
                    if (!this.clnt.isLdapv3 || this.clnt.referenceCount > 1 || this.clnt.usingSaslStreams() || !this.clnt.conn.useable) {
                        closeConnection(false);
                    }
                }
                this.schemaTrees = new Hashtable<>(11, 0.75f);
                connect(z2);
            }
        } finally {
            this.sharable = true;
        }
    }

    private void connect(boolean z2) throws NamingException {
        int i2;
        LdapResult ldapResultAuthenticate;
        String str = null;
        Object obj = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        boolean zEqualsIgnoreCase = false;
        if (this.envprops != null) {
            str = (String) this.envprops.get(Context.SECURITY_PRINCIPAL);
            obj = this.envprops.get(Context.SECURITY_CREDENTIALS);
            str5 = (String) this.envprops.get(VERSION);
            str2 = this.useSsl ? "ssl" : (String) this.envprops.get(Context.SECURITY_PROTOCOL);
            str3 = (String) this.envprops.get(SOCKET_FACTORY);
            str4 = (String) this.envprops.get(Context.SECURITY_AUTHENTICATION);
            zEqualsIgnoreCase = "true".equalsIgnoreCase((String) this.envprops.get(ENABLE_POOL));
        }
        if (str3 == null) {
            str3 = "ssl".equals(str2) ? DEFAULT_SSL_FACTORY : null;
        }
        if (str4 == null) {
            str4 = str == null ? Separation.COLORANT_NONE : "simple";
        }
        try {
            boolean z3 = this.clnt == null;
            if (z3) {
                i2 = str5 != null ? Integer.parseInt(str5) : 32;
                this.clnt = LdapClient.getInstance(zEqualsIgnoreCase, this.hostname, this.port_number, str3, this.connectTimeout, this.readTimeout, this.trace, i2, str4, this.bindCtls, str2, str, obj, this.envprops);
                this.contextSeenStartTlsEnabled |= this.clnt.isUpgradedToStartTls();
                if (this.clnt.authenticateCalled()) {
                    return;
                }
            } else if (this.sharable && z2) {
                return;
            } else {
                i2 = 3;
            }
            synchronized (this.clnt.conn.startTlsLock) {
                ensureCanTransmitCredentials(str4);
                ldapResultAuthenticate = this.clnt.authenticate(z3, str, obj, i2, str4, this.bindCtls, this.envprops);
            }
            this.respCtls = ldapResultAuthenticate.resControls;
            if (ldapResultAuthenticate.status != 0) {
                if (z3) {
                    closeConnection(true);
                }
                processReturnCode(ldapResultAuthenticate);
            }
        } catch (LdapReferralException e2) {
            if (this.handleReferrals == 2) {
                throw e2;
            }
            NamingException namingException = null;
            while (true) {
                NamingException namingException2 = namingException;
                String nextReferral = e2.getNextReferral();
                if (nextReferral == null) {
                    if (namingException2 != null) {
                        throw ((NamingException) namingException2.fillInStackTrace());
                    }
                    throw new NamingException("Internal error processing referral during connection");
                }
                LdapURL ldapURL = new LdapURL(nextReferral);
                this.hostname = ldapURL.getHost();
                if (this.hostname != null && this.hostname.charAt(0) == '[') {
                    this.hostname = this.hostname.substring(1, this.hostname.length() - 1);
                }
                this.port_number = ldapURL.getPort();
                try {
                    connect(z2);
                    return;
                } catch (NamingException e3) {
                    namingException = e3;
                }
            }
        }
    }

    private void closeConnection(boolean z2) {
        removeUnsolicited();
        if (this.clnt != null) {
            this.clnt.close(this.reqCtls, z2);
            this.clnt = null;
        }
    }

    synchronized void incEnumCount() {
        this.enumCount++;
    }

    synchronized void decEnumCount() {
        this.enumCount--;
        if (this.enumCount == 0 && this.closeRequested) {
            try {
                close();
            } catch (NamingException e2) {
            }
        }
    }

    protected void processReturnCode(LdapResult ldapResult) throws NamingException {
        processReturnCode(ldapResult, null, this, null, this.envprops, null);
    }

    void processReturnCode(LdapResult ldapResult, Name name) throws NamingException {
        processReturnCode(ldapResult, new CompositeName().add(this.currentDN), this, name, this.envprops, fullyQualifiedName(name));
    }

    protected void processReturnCode(LdapResult ldapResult, Name name, Object obj, Name name2, Hashtable<?, ?> hashtable, String str) throws NamingException {
        NamingException namingExceptionMapErrorCode;
        Vector<String> vectorElementAt;
        String errorMessage = LdapClient.getErrorMessage(ldapResult.status, ldapResult.errorMessage);
        LdapReferralException ldapReferralException = null;
        switch (ldapResult.status) {
            case 0:
                if (ldapResult.referrals != null) {
                    if (this.handleReferrals == 3) {
                        namingExceptionMapErrorCode = new PartialResultException("Unprocessed Continuation Reference(s)");
                        break;
                    } else {
                        int size = ldapResult.referrals.size();
                        LdapReferralException ldapReferralException2 = null;
                        LdapReferralException ldapReferralException3 = null;
                        for (int i2 = 0; i2 < size; i2++) {
                            ldapReferralException = new LdapReferralException(name, obj, name2, "Continuation Reference", hashtable, str, this.handleReferrals, this.reqCtls);
                            ldapReferralException.setReferralInfo(ldapResult.referrals.elementAt(i2), true);
                            if (this.hopCount > 1) {
                                ldapReferralException.setHopCount(this.hopCount);
                            }
                            if (ldapReferralException2 == null) {
                                ldapReferralException3 = ldapReferralException;
                                ldapReferralException2 = ldapReferralException;
                            } else {
                                ldapReferralException3.nextReferralEx = ldapReferralException;
                                ldapReferralException3 = ldapReferralException;
                            }
                        }
                        ldapResult.referrals = null;
                        if (ldapResult.refEx == null) {
                            ldapResult.refEx = ldapReferralException2;
                        } else {
                            LdapReferralException ldapReferralException4 = ldapResult.refEx;
                            while (true) {
                                LdapReferralException ldapReferralException5 = ldapReferralException4;
                                if (ldapReferralException5.nextReferralEx != null) {
                                    ldapReferralException4 = ldapReferralException5.nextReferralEx;
                                } else {
                                    ldapReferralException5.nextReferralEx = ldapReferralException2;
                                }
                            }
                        }
                        if (this.hopCount > this.referralHopLimit) {
                            LimitExceededException limitExceededException = new LimitExceededException("Referral limit exceeded");
                            limitExceededException.setRootCause(ldapReferralException);
                            throw limitExceededException;
                        }
                        return;
                    }
                } else {
                    return;
                }
            case 9:
                if (this.handleReferrals != 3 && ldapResult.errorMessage != null && !ldapResult.errorMessage.equals("")) {
                    ldapResult.referrals = extractURLs(ldapResult.errorMessage);
                    LdapReferralException ldapReferralException6 = new LdapReferralException(name, obj, name2, errorMessage, hashtable, str, this.handleReferrals, this.reqCtls);
                    if (this.hopCount > 1) {
                        ldapReferralException6.setHopCount(this.hopCount);
                    }
                    if ((ldapResult.entries == null || ldapResult.entries.isEmpty()) && ldapResult.referrals != null && ldapResult.referrals.size() == 1) {
                        ldapReferralException6.setReferralInfo(ldapResult.referrals, false);
                        if (this.hopCount > this.referralHopLimit) {
                            LimitExceededException limitExceededException2 = new LimitExceededException("Referral limit exceeded");
                            limitExceededException2.setRootCause(ldapReferralException6);
                            namingExceptionMapErrorCode = limitExceededException2;
                            break;
                        } else {
                            namingExceptionMapErrorCode = ldapReferralException6;
                            break;
                        }
                    } else {
                        ldapReferralException6.setReferralInfo(ldapResult.referrals, true);
                        ldapResult.refEx = ldapReferralException6;
                        return;
                    }
                } else {
                    namingExceptionMapErrorCode = new PartialResultException(errorMessage);
                    break;
                }
                break;
            case 10:
                if (this.handleReferrals == 3) {
                    namingExceptionMapErrorCode = new PartialResultException(errorMessage);
                    break;
                } else {
                    LdapReferralException ldapReferralException7 = new LdapReferralException(name, obj, name2, errorMessage, hashtable, str, this.handleReferrals, this.reqCtls);
                    if (ldapResult.referrals == null) {
                        vectorElementAt = null;
                    } else if (this.handleReferrals == 4) {
                        vectorElementAt = new Vector<>();
                        Iterator<String> it = ldapResult.referrals.elementAt(0).iterator();
                        while (it.hasNext()) {
                            String next = it.next();
                            if (next.startsWith("ldap:")) {
                                vectorElementAt.add(next);
                            }
                        }
                        if (vectorElementAt.isEmpty()) {
                            vectorElementAt = null;
                        }
                    } else {
                        vectorElementAt = ldapResult.referrals.elementAt(0);
                    }
                    ldapReferralException7.setReferralInfo(vectorElementAt, false);
                    if (this.hopCount > 1) {
                        ldapReferralException7.setHopCount(this.hopCount);
                    }
                    if (this.hopCount > this.referralHopLimit) {
                        LimitExceededException limitExceededException3 = new LimitExceededException("Referral limit exceeded");
                        limitExceededException3.setRootCause(ldapReferralException7);
                        namingExceptionMapErrorCode = limitExceededException3;
                        break;
                    } else {
                        namingExceptionMapErrorCode = ldapReferralException7;
                        break;
                    }
                }
            case 34:
            case 64:
                if (name2 != null) {
                    namingExceptionMapErrorCode = new InvalidNameException(name2.toString() + ": " + errorMessage);
                    break;
                } else {
                    namingExceptionMapErrorCode = new InvalidNameException(errorMessage);
                    break;
                }
            default:
                namingExceptionMapErrorCode = mapErrorCode(ldapResult.status, ldapResult.errorMessage);
                break;
        }
        namingExceptionMapErrorCode.setResolvedName(name);
        namingExceptionMapErrorCode.setResolvedObj(obj);
        namingExceptionMapErrorCode.setRemainingName(name2);
        throw namingExceptionMapErrorCode;
    }

    public static NamingException mapErrorCode(int i2, String str) {
        NamingException namingException;
        if (i2 == 0) {
            return null;
        }
        String errorMessage = LdapClient.getErrorMessage(i2, str);
        switch (i2) {
            case 1:
                namingException = new NamingException(errorMessage);
                break;
            case 2:
                namingException = new CommunicationException(errorMessage);
                break;
            case 3:
                namingException = new TimeLimitExceededException(errorMessage);
                break;
            case 4:
                namingException = new SizeLimitExceededException(errorMessage);
                break;
            case 5:
            case 6:
            case 35:
                namingException = new NamingException(errorMessage);
                break;
            case 7:
            case 8:
            case 13:
            case 48:
                namingException = new AuthenticationNotSupportedException(errorMessage);
                break;
            case 9:
                namingException = new NamingException(errorMessage);
                break;
            case 10:
                namingException = new NamingException(errorMessage);
                break;
            case 11:
                namingException = new LimitExceededException(errorMessage);
                break;
            case 12:
                namingException = new OperationNotSupportedException(errorMessage);
                break;
            case 14:
            case 49:
                namingException = new AuthenticationException(errorMessage);
                break;
            case 15:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            default:
                namingException = new NamingException(errorMessage);
                break;
            case 16:
                namingException = new NoSuchAttributeException(errorMessage);
                break;
            case 17:
                namingException = new InvalidAttributeIdentifierException(errorMessage);
                break;
            case 18:
                namingException = new InvalidSearchFilterException(errorMessage);
                break;
            case 19:
            case 21:
                namingException = new InvalidAttributeValueException(errorMessage);
                break;
            case 20:
                namingException = new AttributeInUseException(errorMessage);
                break;
            case 32:
                namingException = new NameNotFoundException(errorMessage);
                break;
            case 33:
                namingException = new NamingException(errorMessage);
                break;
            case 34:
            case 64:
                namingException = new InvalidNameException(errorMessage);
                break;
            case 36:
                namingException = new NamingException(errorMessage);
                break;
            case 50:
                namingException = new NoPermissionException(errorMessage);
                break;
            case 51:
            case 52:
                namingException = new ServiceUnavailableException(errorMessage);
                break;
            case 53:
                namingException = new OperationNotSupportedException(errorMessage);
                break;
            case 54:
                namingException = new NamingException(errorMessage);
                break;
            case 65:
            case 67:
            case 69:
                namingException = new SchemaViolationException(errorMessage);
                break;
            case 66:
                namingException = new ContextNotEmptyException(errorMessage);
                break;
            case 68:
                namingException = new NameAlreadyBoundException(errorMessage);
                break;
            case 80:
                namingException = new NamingException(errorMessage);
                break;
        }
        return namingException;
    }

    @Override // javax.naming.ldap.LdapContext
    public ExtendedResponse extendedOperation(ExtendedRequest extendedRequest) throws NamingException {
        boolean zEquals = extendedRequest.getID().equals("1.3.6.1.4.1.1466.20037");
        ensureOpen(zEquals);
        try {
            LdapResult ldapResultExtendedOp = this.clnt.extendedOp(extendedRequest.getID(), extendedRequest.getEncodedValue(), this.reqCtls, zEquals);
            this.respCtls = ldapResultExtendedOp.resControls;
            if (ldapResultExtendedOp.status != 0) {
                processReturnCode(ldapResultExtendedOp, new CompositeName());
            }
            ExtendedResponse extendedResponseCreateExtendedResponse = extendedRequest.createExtendedResponse(ldapResultExtendedOp.extensionId, ldapResultExtendedOp.extensionValue, 0, ldapResultExtendedOp.extensionValue == null ? 0 : ldapResultExtendedOp.extensionValue.length);
            if (extendedResponseCreateExtendedResponse instanceof StartTlsResponseImpl) {
                ((StartTlsResponseImpl) extendedResponseCreateExtendedResponse).setConnection(this.clnt.conn, (String) (this.envprops != null ? this.envprops.get(DOMAIN_NAME) : null));
                this.contextSeenStartTlsEnabled |= zEquals;
            }
            return extendedResponseCreateExtendedResponse;
        } catch (LdapReferralException e2) {
            e = e2;
            if (this.handleReferrals == 2) {
                throw e;
            }
            while (true) {
                LdapReferralContext ldapReferralContext = (LdapReferralContext) e.getReferralContext(this.envprops, this.bindCtls);
                try {
                    ExtendedResponse extendedResponseExtendedOperation = ldapReferralContext.extendedOperation(extendedRequest);
                    ldapReferralContext.close();
                    return extendedResponseExtendedOperation;
                } catch (LdapReferralException e3) {
                    e = e3;
                    ldapReferralContext.close();
                } catch (Throwable th) {
                    ldapReferralContext.close();
                    throw th;
                }
            }
        } catch (IOException e4) {
            CommunicationException communicationException = new CommunicationException(e4.getMessage());
            communicationException.setRootCause(e4);
            throw communicationException;
        }
    }

    @Override // javax.naming.ldap.LdapContext
    public void setRequestControls(Control[] controlArr) throws NamingException {
        if (this.handleReferrals == 3) {
            this.reqCtls = addControl(controlArr, manageReferralControl);
        } else {
            this.reqCtls = cloneControls(controlArr);
        }
    }

    @Override // javax.naming.ldap.LdapContext
    public Control[] getRequestControls() throws NamingException {
        return cloneControls(this.reqCtls);
    }

    @Override // javax.naming.ldap.LdapContext
    public Control[] getConnectControls() throws NamingException {
        return cloneControls(this.bindCtls);
    }

    @Override // javax.naming.ldap.LdapContext
    public Control[] getResponseControls() throws NamingException {
        if (this.respCtls != null) {
            return convertControls(this.respCtls);
        }
        return null;
    }

    Control[] convertControls(Vector<Control> vector) throws NamingException {
        int size = vector.size();
        if (size == 0) {
            return null;
        }
        Control[] controlArr = new Control[size];
        for (int i2 = 0; i2 < size; i2++) {
            controlArr[i2] = myResponseControlFactory.getControlInstance(vector.elementAt(i2));
            if (controlArr[i2] == null) {
                controlArr[i2] = ControlFactory.getControlInstance(vector.elementAt(i2), this, this.envprops);
            }
        }
        return controlArr;
    }

    private static Control[] addControl(Control[] controlArr, Control control) {
        if (controlArr == null) {
            return new Control[]{control};
        }
        if (findControl(controlArr, control) != -1) {
            return controlArr;
        }
        Control[] controlArr2 = new Control[controlArr.length + 1];
        System.arraycopy(controlArr, 0, controlArr2, 0, controlArr.length);
        controlArr2[controlArr.length] = control;
        return controlArr2;
    }

    private static int findControl(Control[] controlArr, Control control) {
        for (int i2 = 0; i2 < controlArr.length; i2++) {
            if (controlArr[i2] == control) {
                return i2;
            }
        }
        return -1;
    }

    private static Control[] removeControl(Control[] controlArr, Control control) {
        if (controlArr == null) {
            return null;
        }
        int iFindControl = findControl(controlArr, control);
        if (iFindControl == -1) {
            return controlArr;
        }
        Control[] controlArr2 = new Control[controlArr.length - 1];
        System.arraycopy(controlArr, 0, controlArr2, 0, iFindControl);
        System.arraycopy(controlArr, iFindControl + 1, controlArr2, iFindControl, (controlArr.length - iFindControl) - 1);
        return controlArr2;
    }

    private static Control[] cloneControls(Control[] controlArr) {
        if (controlArr == null) {
            return null;
        }
        Control[] controlArr2 = new Control[controlArr.length];
        System.arraycopy(controlArr, 0, controlArr2, 0, controlArr.length);
        return controlArr2;
    }

    @Override // javax.naming.event.EventContext
    public void addNamingListener(Name name, int i2, NamingListener namingListener) throws NamingException {
        addNamingListener(getTargetName(name), i2, namingListener);
    }

    @Override // javax.naming.event.EventContext
    public void addNamingListener(String str, int i2, NamingListener namingListener) throws NamingException {
        if (this.eventSupport == null) {
            this.eventSupport = new EventSupport(this);
        }
        this.eventSupport.addNamingListener(getTargetName(new CompositeName(str)), i2, namingListener);
        if ((namingListener instanceof UnsolicitedNotificationListener) && !this.unsolicited) {
            addUnsolicited();
        }
    }

    @Override // javax.naming.event.EventContext
    public void removeNamingListener(NamingListener namingListener) throws NamingException {
        if (this.eventSupport == null) {
            return;
        }
        this.eventSupport.removeNamingListener(namingListener);
        if ((namingListener instanceof UnsolicitedNotificationListener) && !this.eventSupport.hasUnsolicited()) {
            removeUnsolicited();
        }
    }

    @Override // javax.naming.event.EventDirContext
    public void addNamingListener(String str, String str2, SearchControls searchControls, NamingListener namingListener) throws NamingException {
        if (this.eventSupport == null) {
            this.eventSupport = new EventSupport(this);
        }
        this.eventSupport.addNamingListener(getTargetName(new CompositeName(str)), str2, cloneSearchControls(searchControls), namingListener);
        if ((namingListener instanceof UnsolicitedNotificationListener) && !this.unsolicited) {
            addUnsolicited();
        }
    }

    @Override // javax.naming.event.EventDirContext
    public void addNamingListener(Name name, String str, SearchControls searchControls, NamingListener namingListener) throws NamingException {
        addNamingListener(getTargetName(name), str, searchControls, namingListener);
    }

    @Override // javax.naming.event.EventDirContext
    public void addNamingListener(Name name, String str, Object[] objArr, SearchControls searchControls, NamingListener namingListener) throws NamingException {
        addNamingListener(getTargetName(name), str, objArr, searchControls, namingListener);
    }

    @Override // javax.naming.event.EventDirContext
    public void addNamingListener(String str, String str2, Object[] objArr, SearchControls searchControls, NamingListener namingListener) throws NamingException {
        addNamingListener(getTargetName(new CompositeName(str)), SearchFilter.format(str2, objArr), searchControls, namingListener);
    }

    @Override // javax.naming.event.EventContext
    public boolean targetMustExist() {
        return true;
    }

    private static String getTargetName(Name name) throws NamingException {
        if (name instanceof CompositeName) {
            if (name.size() > 1) {
                throw new InvalidNameException("Target cannot span multiple namespaces: " + ((Object) name));
            }
            if (name.isEmpty()) {
                return "";
            }
            return name.get(0);
        }
        return name.toString();
    }

    private void addUnsolicited() throws NamingException {
        ensureOpen();
        synchronized (this.eventSupport) {
            this.clnt.addUnsolicited(this);
            this.unsolicited = true;
        }
    }

    private void removeUnsolicited() {
        if (this.eventSupport == null) {
            return;
        }
        synchronized (this.eventSupport) {
            if (this.unsolicited && this.clnt != null) {
                this.clnt.removeUnsolicited(this);
            }
            this.unsolicited = false;
        }
    }

    void fireUnsolicited(Object obj) {
        synchronized (this.eventSupport) {
            if (this.unsolicited) {
                this.eventSupport.fireUnsolicited(obj);
                if (obj instanceof NamingException) {
                    this.unsolicited = false;
                }
            }
        }
    }
}
