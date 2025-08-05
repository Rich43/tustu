package com.sun.jndi.cosnaming;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.jmx.remote.util.EnvHelp;
import com.sun.jndi.cosnaming.IiopUrl;
import com.sun.jndi.toolkit.corba.CorbaUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.Hashtable;
import java.util.Iterator;
import javax.naming.Binding;
import javax.naming.CannotProceedException;
import javax.naming.CommunicationException;
import javax.naming.CompositeName;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.Reference;
import javax.naming.spi.NamingManager;
import javax.naming.spi.ResolveResult;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.INV_OBJREF;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.NotFound;

/* loaded from: rt.jar:com/sun/jndi/cosnaming/CNCtx.class */
public class CNCtx implements Context {
    private static final boolean debug = false;
    private static ORB _defaultOrb;
    ORB _orb;
    public NamingContext _nc;
    private NameComponent[] _name;
    Hashtable<String, Object> _env;
    private static final String FED_PROP = "com.sun.jndi.cosnaming.federation";
    boolean federation;
    OrbReuseTracker orbTracker;
    int enumCount;
    boolean isCloseCalled;
    static final CNNameParser parser = new CNNameParser();
    public static final boolean trustURLCodebase = "true".equalsIgnoreCase((String) AccessController.doPrivileged(() -> {
        return System.getProperty("com.sun.jndi.cosnaming.object.trustURLCodebase", "false");
    }));

    private static synchronized ORB getDefaultOrb() {
        if (_defaultOrb == null) {
            _defaultOrb = CorbaUtils.getOrb(null, -1, new Hashtable());
        }
        return _defaultOrb;
    }

    /* JADX WARN: Multi-variable type inference failed */
    CNCtx(Hashtable<?, ?> hashtable) throws NamingException {
        this._name = null;
        this.federation = false;
        this.orbTracker = null;
        this.isCloseCalled = false;
        hashtable = hashtable != null ? (Hashtable) hashtable.clone() : hashtable;
        this._env = hashtable;
        this.federation = "true".equals(hashtable != null ? hashtable.get(FED_PROP) : null);
        initOrbAndRootContext(hashtable);
    }

    private CNCtx() {
        this._name = null;
        this.federation = false;
        this.orbTracker = null;
        this.isCloseCalled = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static ResolveResult createUsingURL(String str, Hashtable<?, ?> hashtable) throws NamingException {
        CNCtx cNCtx = new CNCtx();
        if (hashtable != null) {
            hashtable = (Hashtable) hashtable.clone();
        }
        cNCtx._env = hashtable;
        return new ResolveResult(cNCtx, parser.parse(cNCtx.initUsingUrl(hashtable != null ? (ORB) hashtable.get(EnvHelp.DEFAULT_ORB) : null, str, hashtable)));
    }

    CNCtx(ORB orb, OrbReuseTracker orbReuseTracker, NamingContext namingContext, Hashtable<String, Object> hashtable, NameComponent[] nameComponentArr) throws NamingException {
        this._name = null;
        this.federation = false;
        this.orbTracker = null;
        this.isCloseCalled = false;
        if (orb == null || namingContext == null) {
            throw new ConfigurationException("Must supply ORB or NamingContext");
        }
        if (orb != null) {
            this._orb = orb;
        } else {
            this._orb = getDefaultOrb();
        }
        this._nc = namingContext;
        this._env = hashtable;
        this._name = nameComponentArr;
        this.federation = "true".equals(hashtable != null ? hashtable.get(FED_PROP) : null);
    }

    NameComponent[] makeFullName(NameComponent[] nameComponentArr) {
        if (this._name == null || this._name.length == 0) {
            return nameComponentArr;
        }
        NameComponent[] nameComponentArr2 = new NameComponent[this._name.length + nameComponentArr.length];
        System.arraycopy(this._name, 0, nameComponentArr2, 0, this._name.length);
        System.arraycopy(nameComponentArr, 0, nameComponentArr2, this._name.length, nameComponentArr.length);
        return nameComponentArr2;
    }

    @Override // javax.naming.Context
    public String getNameInNamespace() throws NamingException {
        if (this._name == null || this._name.length == 0) {
            return "";
        }
        return CNNameParser.cosNameToInsString(this._name);
    }

    private static boolean isCorbaUrl(String str) {
        return str.startsWith("iiop://") || str.startsWith("iiopname://") || str.startsWith("corbaname:");
    }

    private void initOrbAndRootContext(Hashtable<?, ?> hashtable) throws NamingException {
        ORB defaultOrb = null;
        if (0 == 0 && hashtable != null) {
            defaultOrb = (ORB) hashtable.get(EnvHelp.DEFAULT_ORB);
        }
        if (defaultOrb == null) {
            defaultOrb = getDefaultOrb();
        }
        String str = null;
        if (hashtable != null) {
            str = (String) hashtable.get(Context.PROVIDER_URL);
        }
        if (str != null && !isCorbaUrl(str)) {
            setOrbAndRootContext(defaultOrb, getStringifiedIor(str));
            return;
        }
        if (str != null) {
            String strInitUsingUrl = initUsingUrl(defaultOrb, str, hashtable);
            if (strInitUsingUrl.length() > 0) {
                this._name = CNNameParser.nameToCosName(parser.parse(strInitUsingUrl));
                try {
                    this._nc = NamingContextHelper.narrow(this._nc.resolve(this._name));
                    if (this._nc == null) {
                        throw new ConfigurationException(strInitUsingUrl + " does not name a NamingContext");
                    }
                    return;
                } catch (BAD_PARAM e2) {
                    throw new ConfigurationException(strInitUsingUrl + " does not name a NamingContext");
                } catch (Exception e3) {
                    throw ExceptionMapper.mapException(e3, this, this._name);
                }
            }
            return;
        }
        setOrbAndRootContext(defaultOrb, (String) null);
    }

    private String initUsingUrl(ORB orb, String str, Hashtable<?, ?> hashtable) throws NamingException {
        if (str.startsWith("iiop://") || str.startsWith("iiopname://")) {
            return initUsingIiopUrl(orb, str, hashtable);
        }
        return initUsingCorbanameUrl(orb, str, hashtable);
    }

    private String initUsingIiopUrl(ORB orb, String str, Hashtable<?, ?> hashtable) throws NamingException {
        if (orb == null) {
            orb = getDefaultOrb();
        }
        try {
            IiopUrl iiopUrl = new IiopUrl(str);
            NamingException namingException = null;
            Iterator<IiopUrl.Address> it = iiopUrl.getAddresses().iterator();
            while (it.hasNext()) {
                IiopUrl.Address next = it.next();
                try {
                    try {
                        setOrbAndRootContext(orb, orb.string_to_object("corbaloc:iiop:" + next.host + CallSiteDescriptor.TOKEN_DELIMITER + next.port + "/NameService"));
                        return iiopUrl.getStringName();
                    } catch (Exception e2) {
                        setOrbAndRootContext(orb, (String) null);
                        return iiopUrl.getStringName();
                    }
                } catch (NamingException e3) {
                    namingException = e3;
                }
            }
            if (namingException != null) {
                throw namingException;
            }
            throw new ConfigurationException("Problem with URL: " + str);
        } catch (MalformedURLException e4) {
            throw new ConfigurationException(e4.getMessage());
        }
    }

    private String initUsingCorbanameUrl(ORB orb, String str, Hashtable<?, ?> hashtable) throws NamingException {
        if (orb == null) {
            orb = getDefaultOrb();
        }
        try {
            CorbanameUrl corbanameUrl = new CorbanameUrl(str);
            String location = corbanameUrl.getLocation();
            corbanameUrl.getStringName();
            setOrbAndRootContext(orb, location);
            return corbanameUrl.getStringName();
        } catch (MalformedURLException e2) {
            throw new ConfigurationException(e2.getMessage());
        }
    }

    private void setOrbAndRootContext(ORB orb, String str) throws NamingException {
        Object objectResolve_initial_references;
        this._orb = orb;
        try {
            if (str != null) {
                objectResolve_initial_references = this._orb.string_to_object(str);
            } else {
                objectResolve_initial_references = this._orb.resolve_initial_references(ORBConstants.PERSISTENT_NAME_SERVICE_NAME);
            }
            this._nc = NamingContextHelper.narrow(objectResolve_initial_references);
            if (this._nc == null) {
                if (str != null) {
                    throw new ConfigurationException("Cannot convert IOR to a NamingContext: " + str);
                }
                throw new ConfigurationException("ORB.resolve_initial_references(\"NameService\") does not return a NamingContext");
            }
        } catch (BAD_PARAM e2) {
            ConfigurationException configurationException = new ConfigurationException("Invalid URL or IOR: " + str);
            configurationException.setRootCause(e2);
            throw configurationException;
        } catch (COMM_FAILURE e3) {
            CommunicationException communicationException = new CommunicationException("Cannot connect to ORB");
            communicationException.setRootCause(e3);
            throw communicationException;
        } catch (INV_OBJREF e4) {
            ConfigurationException configurationException2 = new ConfigurationException("Invalid object reference: " + str);
            configurationException2.setRootCause(e4);
            throw configurationException2;
        } catch (InvalidName e5) {
            ConfigurationException configurationException3 = new ConfigurationException("COS Name Service not registered with ORB under the name 'NameService'");
            configurationException3.setRootCause(e5);
            throw configurationException3;
        }
    }

    private void setOrbAndRootContext(ORB orb, Object object) throws NamingException {
        this._orb = orb;
        try {
            this._nc = NamingContextHelper.narrow(object);
            if (this._nc == null) {
                throw new ConfigurationException("Cannot convert object reference to NamingContext: " + ((Object) object));
            }
        } catch (COMM_FAILURE e2) {
            CommunicationException communicationException = new CommunicationException("Cannot connect to ORB");
            communicationException.setRootCause(e2);
            throw communicationException;
        }
    }

    private String getStringifiedIor(String str) throws NamingException {
        String line;
        if (str.startsWith(ORBConstants.STRINGIFY_PREFIX) || str.startsWith("corbaloc:")) {
            return str;
        }
        InputStream inputStreamOpenStream = null;
        try {
            try {
                inputStreamOpenStream = new URL(str).openStream();
                if (inputStreamOpenStream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamOpenStream, "8859_1"));
                    do {
                        line = bufferedReader.readLine();
                        if (line != null) {
                        }
                    } while (!line.startsWith(ORBConstants.STRINGIFY_PREFIX));
                    if (inputStreamOpenStream != null) {
                        try {
                            inputStreamOpenStream.close();
                        } catch (IOException e2) {
                            ConfigurationException configurationException = new ConfigurationException("Invalid URL: " + str);
                            configurationException.setRootCause(e2);
                            throw configurationException;
                        }
                    }
                    return line;
                }
                if (inputStreamOpenStream != null) {
                    try {
                        inputStreamOpenStream.close();
                    } catch (IOException e3) {
                        ConfigurationException configurationException2 = new ConfigurationException("Invalid URL: " + str);
                        configurationException2.setRootCause(e3);
                        throw configurationException2;
                    }
                }
                throw new ConfigurationException(str + " does not contain an IOR");
            } catch (IOException e4) {
                ConfigurationException configurationException3 = new ConfigurationException("Invalid URL: " + str);
                configurationException3.setRootCause(e4);
                throw configurationException3;
            }
        } catch (Throwable th) {
            if (inputStreamOpenStream != null) {
                try {
                    inputStreamOpenStream.close();
                } catch (IOException e5) {
                    ConfigurationException configurationException4 = new ConfigurationException("Invalid URL: " + str);
                    configurationException4.setRootCause(e5);
                    throw configurationException4;
                }
            }
            throw th;
        }
    }

    Object callResolve(NameComponent[] nameComponentArr) throws NamingException {
        try {
            Object objectResolve = this._nc.resolve(nameComponentArr);
            try {
                NamingContext namingContextNarrow = NamingContextHelper.narrow(objectResolve);
                if (namingContextNarrow != null) {
                    return new CNCtx(this._orb, this.orbTracker, namingContextNarrow, this._env, makeFullName(nameComponentArr));
                }
                return objectResolve;
            } catch (SystemException e2) {
                return objectResolve;
            }
        } catch (Exception e3) {
            throw ExceptionMapper.mapException(e3, this, nameComponentArr);
        }
    }

    @Override // javax.naming.Context
    public Object lookup(String str) throws NamingException {
        return lookup(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public Object lookup(Name name) throws NamingException {
        if (this._nc == null) {
            throw new ConfigurationException("Context does not have a corresponding NamingContext");
        }
        if (name.size() == 0) {
            return this;
        }
        try {
            Object objCallResolve = callResolve(CNNameParser.nameToCosName(name));
            try {
                if (CorbaUtils.isObjectFactoryTrusted(objCallResolve)) {
                    objCallResolve = NamingManager.getObjectInstance(objCallResolve, name, this, this._env);
                }
                return objCallResolve;
            } catch (NamingException e2) {
                throw e2;
            } catch (Exception e3) {
                NamingException namingException = new NamingException("problem generating object using object factory");
                namingException.setRootCause(e3);
                throw namingException;
            }
        } catch (CannotProceedException e4) {
            return getContinuationContext(e4).lookup(e4.getRemainingName());
        }
    }

    private void callBindOrRebind(NameComponent[] nameComponentArr, Name name, Object obj, boolean z2) throws NamingException {
        if (this._nc == null) {
            throw new ConfigurationException("Context does not have a corresponding NamingContext");
        }
        try {
            Object stateToBind = NamingManager.getStateToBind(obj, name, this, this._env);
            if (stateToBind instanceof CNCtx) {
                stateToBind = ((CNCtx) stateToBind)._nc;
            }
            if (stateToBind instanceof NamingContext) {
                NamingContext namingContextNarrow = NamingContextHelper.narrow((Object) stateToBind);
                if (z2) {
                    this._nc.rebind_context(nameComponentArr, namingContextNarrow);
                } else {
                    this._nc.bind_context(nameComponentArr, namingContextNarrow);
                }
            } else if (stateToBind instanceof Object) {
                if (z2) {
                    this._nc.rebind(nameComponentArr, (Object) stateToBind);
                } else {
                    this._nc.bind(nameComponentArr, (Object) stateToBind);
                }
            } else {
                throw new IllegalArgumentException("Only instances of org.omg.CORBA.Object can be bound");
            }
        } catch (BAD_PARAM e2) {
            NotContextException notContextException = new NotContextException(name.toString());
            notContextException.setRootCause(e2);
            throw notContextException;
        } catch (Exception e3) {
            throw ExceptionMapper.mapException(e3, this, nameComponentArr);
        }
    }

    @Override // javax.naming.Context
    public void bind(Name name, Object obj) throws NamingException {
        if (name.size() == 0) {
            throw new InvalidNameException("Name is empty");
        }
        try {
            callBindOrRebind(CNNameParser.nameToCosName(name), name, obj, false);
        } catch (CannotProceedException e2) {
            getContinuationContext(e2).bind(e2.getRemainingName(), obj);
        }
    }

    private static Context getContinuationContext(CannotProceedException cannotProceedException) throws NamingException {
        try {
            return NamingManager.getContinuationContext(cannotProceedException);
        } catch (CannotProceedException e2) {
            Object resolvedObj = e2.getResolvedObj();
            if ((resolvedObj instanceof Reference) && (((Reference) resolvedObj).get("nns").getContent() instanceof Context)) {
                NameNotFoundException nameNotFoundException = new NameNotFoundException("No object reference bound for specified name");
                nameNotFoundException.setRootCause(cannotProceedException.getRootCause());
                nameNotFoundException.setRemainingName(cannotProceedException.getRemainingName());
                throw nameNotFoundException;
            }
            throw e2;
        }
    }

    @Override // javax.naming.Context
    public void bind(String str, Object obj) throws NamingException {
        bind(new CompositeName(str), obj);
    }

    @Override // javax.naming.Context
    public void rebind(Name name, Object obj) throws NamingException {
        if (name.size() == 0) {
            throw new InvalidNameException("Name is empty");
        }
        try {
            callBindOrRebind(CNNameParser.nameToCosName(name), name, obj, true);
        } catch (CannotProceedException e2) {
            getContinuationContext(e2).rebind(e2.getRemainingName(), obj);
        }
    }

    @Override // javax.naming.Context
    public void rebind(String str, Object obj) throws NamingException {
        rebind(new CompositeName(str), obj);
    }

    private void callUnbind(NameComponent[] nameComponentArr) throws NamingException {
        if (this._nc == null) {
            throw new ConfigurationException("Context does not have a corresponding NamingContext");
        }
        try {
            this._nc.unbind(nameComponentArr);
        } catch (NotFound e2) {
            if (!leafNotFound(e2, nameComponentArr[nameComponentArr.length - 1])) {
                throw ExceptionMapper.mapException(e2, this, nameComponentArr);
            }
        } catch (Exception e3) {
            throw ExceptionMapper.mapException(e3, this, nameComponentArr);
        }
    }

    private boolean leafNotFound(NotFound notFound, NameComponent nameComponent) {
        if (notFound.why.value() == 0 && notFound.rest_of_name.length == 1) {
            NameComponent nameComponent2 = notFound.rest_of_name[0];
            if (nameComponent2.id.equals(nameComponent.id) && (nameComponent2.kind == nameComponent.kind || (nameComponent2.kind != null && nameComponent2.kind.equals(nameComponent.kind)))) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.naming.Context
    public void unbind(String str) throws NamingException {
        unbind(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public void unbind(Name name) throws NamingException {
        if (name.size() == 0) {
            throw new InvalidNameException("Name is empty");
        }
        try {
            callUnbind(CNNameParser.nameToCosName(name));
        } catch (CannotProceedException e2) {
            getContinuationContext(e2).unbind(e2.getRemainingName());
        }
    }

    @Override // javax.naming.Context
    public void rename(String str, String str2) throws NamingException {
        rename(new CompositeName(str), new CompositeName(str2));
    }

    @Override // javax.naming.Context
    public void rename(Name name, Name name2) throws NamingException {
        if (this._nc == null) {
            throw new ConfigurationException("Context does not have a corresponding NamingContext");
        }
        if (name.size() == 0 || name2.size() == 0) {
            throw new InvalidNameException("One or both names empty");
        }
        bind(name2, lookup(name));
        unbind(name);
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(String str) throws NamingException {
        return list(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        return listBindings(name);
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(String str) throws NamingException {
        return listBindings(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        if (this._nc == null) {
            throw new ConfigurationException("Context does not have a corresponding NamingContext");
        }
        if (name.size() > 0) {
            try {
                Object objLookup = lookup(name);
                if (objLookup instanceof CNCtx) {
                    return new CNBindingEnumeration((CNCtx) objLookup, true, this._env);
                }
                throw new NotContextException(name.toString());
            } catch (NamingException e2) {
                throw e2;
            } catch (BAD_PARAM e3) {
                NotContextException notContextException = new NotContextException(name.toString());
                notContextException.setRootCause(e3);
                throw notContextException;
            }
        }
        return new CNBindingEnumeration(this, false, this._env);
    }

    private void callDestroy(NamingContext namingContext) throws NamingException {
        if (this._nc == null) {
            throw new ConfigurationException("Context does not have a corresponding NamingContext");
        }
        try {
            namingContext.destroy();
        } catch (Exception e2) {
            throw ExceptionMapper.mapException(e2, this, null);
        }
    }

    @Override // javax.naming.Context
    public void destroySubcontext(String str) throws NamingException {
        destroySubcontext(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public void destroySubcontext(Name name) throws NamingException {
        if (this._nc == null) {
            throw new ConfigurationException("Context does not have a corresponding NamingContext");
        }
        NamingContext namingContext = this._nc;
        NameComponent[] nameComponentArrNameToCosName = CNNameParser.nameToCosName(name);
        if (name.size() > 0) {
            try {
                CNCtx cNCtx = (CNCtx) ((Context) callResolve(nameComponentArrNameToCosName));
                namingContext = cNCtx._nc;
                cNCtx.close();
            } catch (ClassCastException e2) {
                throw new NotContextException(name.toString());
            } catch (CannotProceedException e3) {
                getContinuationContext(e3).destroySubcontext(e3.getRemainingName());
                return;
            } catch (NameNotFoundException e4) {
                if ((e4.getRootCause() instanceof NotFound) && leafNotFound((NotFound) e4.getRootCause(), nameComponentArrNameToCosName[nameComponentArrNameToCosName.length - 1])) {
                    return;
                } else {
                    throw e4;
                }
            } catch (NamingException e5) {
                throw e5;
            }
        }
        callDestroy(namingContext);
        callUnbind(nameComponentArrNameToCosName);
    }

    private Context callBindNewContext(NameComponent[] nameComponentArr) throws NamingException {
        if (this._nc == null) {
            throw new ConfigurationException("Context does not have a corresponding NamingContext");
        }
        try {
            return new CNCtx(this._orb, this.orbTracker, this._nc.bind_new_context(nameComponentArr), this._env, makeFullName(nameComponentArr));
        } catch (Exception e2) {
            throw ExceptionMapper.mapException(e2, this, nameComponentArr);
        }
    }

    @Override // javax.naming.Context
    public Context createSubcontext(String str) throws NamingException {
        return createSubcontext(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public Context createSubcontext(Name name) throws NamingException {
        if (name.size() == 0) {
            throw new InvalidNameException("Name is empty");
        }
        try {
            return callBindNewContext(CNNameParser.nameToCosName(name));
        } catch (CannotProceedException e2) {
            return getContinuationContext(e2).createSubcontext(e2.getRemainingName());
        }
    }

    @Override // javax.naming.Context
    public Object lookupLink(String str) throws NamingException {
        return lookupLink(new CompositeName(str));
    }

    @Override // javax.naming.Context
    public Object lookupLink(Name name) throws NamingException {
        return lookup(name);
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(String str) throws NamingException {
        return parser;
    }

    @Override // javax.naming.Context
    public NameParser getNameParser(Name name) throws NamingException {
        return parser;
    }

    @Override // javax.naming.Context
    public Hashtable<String, Object> getEnvironment() throws NamingException {
        if (this._env == null) {
            return new Hashtable<>(5, 0.75f);
        }
        return (Hashtable) this._env.clone();
    }

    @Override // javax.naming.Context
    public String composeName(String str, String str2) throws NamingException {
        return composeName(new CompositeName(str), new CompositeName(str2)).toString();
    }

    @Override // javax.naming.Context
    public Name composeName(Name name, Name name2) throws NamingException {
        return ((Name) name2.clone()).addAll(name);
    }

    @Override // javax.naming.Context
    public Object addToEnvironment(String str, Object obj) throws NamingException {
        if (this._env == null) {
            this._env = new Hashtable<>(7, 0.75f);
        } else {
            this._env = (Hashtable) this._env.clone();
        }
        return this._env.put(str, obj);
    }

    @Override // javax.naming.Context
    public Object removeFromEnvironment(String str) throws NamingException {
        if (this._env != null && this._env.get(str) != null) {
            this._env = (Hashtable) this._env.clone();
            return this._env.remove(str);
        }
        return null;
    }

    public synchronized void incEnumCount() {
        this.enumCount++;
    }

    public synchronized void decEnumCount() throws NamingException {
        this.enumCount--;
        if (this.enumCount == 0 && this.isCloseCalled) {
            close();
        }
    }

    @Override // javax.naming.Context
    public synchronized void close() throws NamingException {
        if (this.enumCount > 0) {
            this.isCloseCalled = true;
        }
    }

    protected void finalize() {
        try {
            close();
        } catch (NamingException e2) {
        }
    }
}
