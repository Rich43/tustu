package com.sun.corba.se.impl.naming.cosnaming;

import com.sun.corba.se.impl.logging.NamingSystemException;
import com.sun.corba.se.impl.naming.namingutil.INSURLHandler;
import com.sun.corba.se.impl.orbutil.LogKeywords;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.geometry.VectorFormat;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.Object;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.BindingTypeHolder;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextExtPOA;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextOperations;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.CosNaming.NamingContextPackage.NotFoundReason;
import org.omg.PortableServer.POA;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/cosnaming/NamingContextImpl.class */
public abstract class NamingContextImpl extends NamingContextExtPOA implements NamingContextDataStore {
    protected POA nsPOA;
    private Logger readLogger;
    private Logger updateLogger;
    private Logger lifecycleLogger;
    private NamingSystemException wrapper;
    private static NamingSystemException staticWrapper = NamingSystemException.get(CORBALogDomains.NAMING_UPDATE);
    private InterOperableNamingImpl insImpl = new InterOperableNamingImpl();
    protected transient ORB orb;
    public static final boolean debug = false;

    public NamingContextImpl(ORB orb, POA poa) throws Exception {
        this.orb = orb;
        this.wrapper = NamingSystemException.get(orb, CORBALogDomains.NAMING_UPDATE);
        this.nsPOA = poa;
        this.readLogger = orb.getLogger(CORBALogDomains.NAMING_READ);
        this.updateLogger = orb.getLogger(CORBALogDomains.NAMING_UPDATE);
        this.lifecycleLogger = orb.getLogger(CORBALogDomains.NAMING_LIFECYCLE);
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public POA getNSPOA() {
        return this.nsPOA;
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void bind(NameComponent[] nameComponentArr, Object object) throws NotFound, AlreadyBound, InvalidName, CannotProceed {
        if (object == null) {
            this.updateLogger.warning("<<NAMING BIND>> unsuccessful because NULL Object cannot be Bound ");
            throw this.wrapper.objectIsNull();
        }
        doBind(this, nameComponentArr, object, false, BindingType.nobject);
        if (this.updateLogger.isLoggable(Level.FINE)) {
            this.updateLogger.fine("<<NAMING BIND>><<SUCCESS>> Name = " + NamingUtils.getDirectoryStructuredName(nameComponentArr));
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void bind_context(NameComponent[] nameComponentArr, NamingContext namingContext) throws NotFound, AlreadyBound, InvalidName, CannotProceed {
        if (namingContext == null) {
            this.updateLogger.warning("<<NAMING BIND>><<FAILURE>> NULL Context cannot be Bound ");
            throw new BAD_PARAM("Naming Context should not be null ");
        }
        doBind(this, nameComponentArr, namingContext, false, BindingType.ncontext);
        if (this.updateLogger.isLoggable(Level.FINE)) {
            this.updateLogger.fine("<<NAMING BIND>><<SUCCESS>> Name = " + NamingUtils.getDirectoryStructuredName(nameComponentArr));
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void rebind(NameComponent[] nameComponentArr, Object object) throws NotFound, InvalidName, CannotProceed {
        if (object == null) {
            this.updateLogger.warning("<<NAMING REBIND>><<FAILURE>> NULL Object cannot be Bound ");
            throw this.wrapper.objectIsNull();
        }
        try {
            doBind(this, nameComponentArr, object, true, BindingType.nobject);
            if (this.updateLogger.isLoggable(Level.FINE)) {
                this.updateLogger.fine("<<NAMING REBIND>><<SUCCESS>> Name = " + NamingUtils.getDirectoryStructuredName(nameComponentArr));
            }
        } catch (AlreadyBound e2) {
            this.updateLogger.warning(LogKeywords.NAMING_REBIND_FAILURE + NamingUtils.getDirectoryStructuredName(nameComponentArr) + " is already bound to a Naming Context");
            throw this.wrapper.namingCtxRebindAlreadyBound(e2);
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void rebind_context(NameComponent[] nameComponentArr, NamingContext namingContext) throws NotFound, InvalidName, CannotProceed {
        if (namingContext == null) {
            this.updateLogger.warning("<<NAMING REBIND>><<FAILURE>> NULL Context cannot be Bound ");
            throw this.wrapper.objectIsNull();
        }
        try {
            doBind(this, nameComponentArr, namingContext, true, BindingType.ncontext);
            if (this.updateLogger.isLoggable(Level.FINE)) {
                this.updateLogger.fine("<<NAMING REBIND>><<SUCCESS>> Name = " + NamingUtils.getDirectoryStructuredName(nameComponentArr));
            }
        } catch (AlreadyBound e2) {
            this.updateLogger.warning(LogKeywords.NAMING_REBIND_FAILURE + NamingUtils.getDirectoryStructuredName(nameComponentArr) + " is already bound to a CORBA Object");
            throw this.wrapper.namingCtxRebindctxAlreadyBound(e2);
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public Object resolve(NameComponent[] nameComponentArr) throws NotFound, InvalidName, CannotProceed {
        Object objectDoResolve = doResolve(this, nameComponentArr);
        if (objectDoResolve != null) {
            if (this.readLogger.isLoggable(Level.FINE)) {
                this.readLogger.fine("<<NAMING RESOLVE>><<SUCCESS>> Name: " + NamingUtils.getDirectoryStructuredName(nameComponentArr));
            }
        } else {
            this.readLogger.warning("<<NAMING RESOLVE>><<FAILURE>> Name: " + NamingUtils.getDirectoryStructuredName(nameComponentArr));
        }
        return objectDoResolve;
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void unbind(NameComponent[] nameComponentArr) throws NotFound, InvalidName, CannotProceed {
        doUnbind(this, nameComponentArr);
        if (this.updateLogger.isLoggable(Level.FINE)) {
            this.updateLogger.fine("<<NAMING UNBIND>><<SUCCESS>> Name: " + NamingUtils.getDirectoryStructuredName(nameComponentArr));
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void list(int i2, BindingListHolder bindingListHolder, BindingIteratorHolder bindingIteratorHolder) {
        synchronized (this) {
            List(i2, bindingListHolder, bindingIteratorHolder);
        }
        if (this.readLogger.isLoggable(Level.FINE) && bindingListHolder.value != null) {
            this.readLogger.fine("<<NAMING LIST>><<SUCCESS>>list(" + i2 + ") -> bindings[" + bindingListHolder.value.length + "] + iterator: " + ((Object) bindingIteratorHolder.value));
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public synchronized NamingContext new_context() {
        NamingContext namingContextNewContext;
        this.lifecycleLogger.fine("Creating New Naming Context ");
        synchronized (this) {
            namingContextNewContext = NewContext();
            if (namingContextNewContext != null) {
                this.lifecycleLogger.fine(LogKeywords.LIFECYCLE_CREATE_SUCCESS);
            } else {
                this.lifecycleLogger.severe(LogKeywords.LIFECYCLE_CREATE_FAILURE);
            }
        }
        return namingContextNewContext;
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public NamingContext bind_new_context(NameComponent[] nameComponentArr) throws NotFound, AlreadyBound, InvalidName, CannotProceed {
        NamingContextOperations namingContextOperations = null;
        try {
            NamingContext namingContextNew_context = new_context();
            bind_context(nameComponentArr, namingContextNew_context);
            namingContextOperations = null;
            if (0 != 0) {
                try {
                    namingContextOperations.destroy();
                } catch (NotEmpty e2) {
                }
            }
            if (this.updateLogger.isLoggable(Level.FINE)) {
                this.updateLogger.fine("<<NAMING BIND>>New Context Bound To " + NamingUtils.getDirectoryStructuredName(nameComponentArr));
            }
            return namingContextNew_context;
        } catch (Throwable th) {
            if (namingContextOperations != null) {
                try {
                    namingContextOperations.destroy();
                } catch (NotEmpty e3) {
                    throw th;
                }
            }
            throw th;
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void destroy() throws NotEmpty {
        this.lifecycleLogger.fine("Destroying Naming Context ");
        synchronized (this) {
            if (IsEmpty()) {
                Destroy();
                this.lifecycleLogger.fine(LogKeywords.LIFECYCLE_DESTROY_SUCCESS);
            } else {
                this.lifecycleLogger.warning("<<LIFECYCLE DESTROY>><<FAILURE>> NamingContext children are not destroyed still..");
                throw new NotEmpty();
            }
        }
    }

    public static void doBind(NamingContextDataStore namingContextDataStore, NameComponent[] nameComponentArr, Object object, boolean z2, BindingType bindingType) throws NotFound, AlreadyBound, InvalidName, CannotProceed {
        if (nameComponentArr.length < 1) {
            throw new InvalidName();
        }
        if (nameComponentArr.length == 1) {
            if (nameComponentArr[0].id.length() == 0 && nameComponentArr[0].kind.length() == 0) {
                throw new InvalidName();
            }
            synchronized (namingContextDataStore) {
                BindingTypeHolder bindingTypeHolder = new BindingTypeHolder();
                if (z2) {
                    if (namingContextDataStore.Resolve(nameComponentArr[0], bindingTypeHolder) != null) {
                        if (bindingTypeHolder.value.value() == BindingType.nobject.value()) {
                            if (bindingType.value() == BindingType.ncontext.value()) {
                                throw new NotFound(NotFoundReason.not_context, nameComponentArr);
                            }
                        } else if (bindingType.value() == BindingType.nobject.value()) {
                            throw new NotFound(NotFoundReason.not_object, nameComponentArr);
                        }
                        namingContextDataStore.Unbind(nameComponentArr[0]);
                    }
                } else if (namingContextDataStore.Resolve(nameComponentArr[0], bindingTypeHolder) != null) {
                    throw new AlreadyBound();
                }
                namingContextDataStore.Bind(nameComponentArr[0], object, bindingType);
            }
            return;
        }
        NamingContext namingContextResolveFirstAsContext = resolveFirstAsContext(namingContextDataStore, nameComponentArr);
        NameComponent[] nameComponentArr2 = new NameComponent[nameComponentArr.length - 1];
        System.arraycopy(nameComponentArr, 1, nameComponentArr2, 0, nameComponentArr.length - 1);
        switch (bindingType.value()) {
            case 0:
                if (z2) {
                    namingContextResolveFirstAsContext.rebind(nameComponentArr2, object);
                    return;
                } else {
                    namingContextResolveFirstAsContext.bind(nameComponentArr2, object);
                    return;
                }
            case 1:
                NamingContext namingContext = (NamingContext) object;
                if (z2) {
                    namingContextResolveFirstAsContext.rebind_context(nameComponentArr2, namingContext);
                    return;
                } else {
                    namingContextResolveFirstAsContext.bind_context(nameComponentArr2, namingContext);
                    return;
                }
            default:
                throw staticWrapper.namingCtxBadBindingtype();
        }
    }

    public static Object doResolve(NamingContextDataStore namingContextDataStore, NameComponent[] nameComponentArr) throws NotFound, InvalidName, CannotProceed {
        Object objectResolve;
        BindingTypeHolder bindingTypeHolder = new BindingTypeHolder();
        if (nameComponentArr.length < 1) {
            throw new InvalidName();
        }
        if (nameComponentArr.length == 1) {
            synchronized (namingContextDataStore) {
                objectResolve = namingContextDataStore.Resolve(nameComponentArr[0], bindingTypeHolder);
            }
            if (objectResolve == null) {
                throw new NotFound(NotFoundReason.missing_node, nameComponentArr);
            }
            return objectResolve;
        }
        if (nameComponentArr[1].id.length() == 0 && nameComponentArr[1].kind.length() == 0) {
            throw new InvalidName();
        }
        NamingContext namingContextResolveFirstAsContext = resolveFirstAsContext(namingContextDataStore, nameComponentArr);
        NameComponent[] nameComponentArr2 = new NameComponent[nameComponentArr.length - 1];
        System.arraycopy(nameComponentArr, 1, nameComponentArr2, 0, nameComponentArr.length - 1);
        try {
            return doResolve((NamingContextDataStore) namingContextDataStore.getNSPOA().reference_to_servant(namingContextResolveFirstAsContext), nameComponentArr2);
        } catch (Exception e2) {
            return namingContextResolveFirstAsContext.resolve(nameComponentArr2);
        }
    }

    public static void doUnbind(NamingContextDataStore namingContextDataStore, NameComponent[] nameComponentArr) throws NotFound, InvalidName, CannotProceed {
        Object objectUnbind;
        if (nameComponentArr.length < 1) {
            throw new InvalidName();
        }
        if (nameComponentArr.length == 1) {
            if (nameComponentArr[0].id.length() == 0 && nameComponentArr[0].kind.length() == 0) {
                throw new InvalidName();
            }
            synchronized (namingContextDataStore) {
                objectUnbind = namingContextDataStore.Unbind(nameComponentArr[0]);
            }
            if (objectUnbind == null) {
                throw new NotFound(NotFoundReason.missing_node, nameComponentArr);
            }
            return;
        }
        NamingContext namingContextResolveFirstAsContext = resolveFirstAsContext(namingContextDataStore, nameComponentArr);
        NameComponent[] nameComponentArr2 = new NameComponent[nameComponentArr.length - 1];
        System.arraycopy(nameComponentArr, 1, nameComponentArr2, 0, nameComponentArr.length - 1);
        namingContextResolveFirstAsContext.unbind(nameComponentArr2);
    }

    protected static NamingContext resolveFirstAsContext(NamingContextDataStore namingContextDataStore, NameComponent[] nameComponentArr) throws NotFound {
        Object objectResolve;
        BindingTypeHolder bindingTypeHolder = new BindingTypeHolder();
        synchronized (namingContextDataStore) {
            objectResolve = namingContextDataStore.Resolve(nameComponentArr[0], bindingTypeHolder);
            if (objectResolve == null) {
                throw new NotFound(NotFoundReason.missing_node, nameComponentArr);
            }
        }
        if (bindingTypeHolder.value != BindingType.ncontext) {
            throw new NotFound(NotFoundReason.not_context, nameComponentArr);
        }
        try {
            return NamingContextHelper.narrow(objectResolve);
        } catch (BAD_PARAM e2) {
            throw new NotFound(NotFoundReason.not_context, nameComponentArr);
        }
    }

    @Override // org.omg.CosNaming.NamingContextExtOperations
    public String to_string(NameComponent[] nameComponentArr) throws InvalidName {
        if (nameComponentArr == null || nameComponentArr.length == 0) {
            throw new InvalidName();
        }
        String strConvertToString = this.insImpl.convertToString(nameComponentArr);
        if (strConvertToString == null) {
            throw new InvalidName();
        }
        return strConvertToString;
    }

    @Override // org.omg.CosNaming.NamingContextExtOperations
    public NameComponent[] to_name(String str) throws InvalidName {
        if (str == null || str.length() == 0) {
            throw new InvalidName();
        }
        NameComponent[] nameComponentArrConvertToNameComponent = this.insImpl.convertToNameComponent(str);
        if (nameComponentArrConvertToNameComponent == null || nameComponentArrConvertToNameComponent.length == 0) {
            throw new InvalidName();
        }
        for (int i2 = 0; i2 < nameComponentArrConvertToNameComponent.length; i2++) {
            if ((nameComponentArrConvertToNameComponent[i2].id == null || nameComponentArrConvertToNameComponent[i2].id.length() == 0) && (nameComponentArrConvertToNameComponent[i2].kind == null || nameComponentArrConvertToNameComponent[i2].kind.length() == 0)) {
                throw new InvalidName();
            }
        }
        return nameComponentArrConvertToNameComponent;
    }

    @Override // org.omg.CosNaming.NamingContextExtOperations
    public String to_url(String str, String str2) throws InvalidAddress, InvalidName {
        if (str2 == null || str2.length() == 0) {
            throw new InvalidName();
        }
        if (str == null) {
            throw new InvalidAddress();
        }
        String strCreateURLBasedAddress = this.insImpl.createURLBasedAddress(str, str2);
        try {
            INSURLHandler.getINSURLHandler().parseURL(strCreateURLBasedAddress);
            return strCreateURLBasedAddress;
        } catch (BAD_PARAM e2) {
            throw new InvalidAddress();
        }
    }

    @Override // org.omg.CosNaming.NamingContextExtOperations
    public Object resolve_str(String str) throws NotFound, InvalidName, CannotProceed {
        if (str == null || str.length() == 0) {
            throw new InvalidName();
        }
        NameComponent[] nameComponentArrConvertToNameComponent = this.insImpl.convertToNameComponent(str);
        if (nameComponentArrConvertToNameComponent == null || nameComponentArrConvertToNameComponent.length == 0) {
            throw new InvalidName();
        }
        return resolve(nameComponentArrConvertToNameComponent);
    }

    public static String nameToString(NameComponent[] nameComponentArr) {
        StringBuffer stringBuffer = new StringBuffer(VectorFormat.DEFAULT_PREFIX);
        if (nameComponentArr != null || nameComponentArr.length > 0) {
            for (int i2 = 0; i2 < nameComponentArr.length; i2++) {
                if (i2 > 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append("[").append(nameComponentArr[i2].id).append(",").append(nameComponentArr[i2].kind).append("]");
            }
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }

    private static void dprint(String str) {
        NamingUtils.dprint("NamingContextImpl(" + Thread.currentThread().getName() + " at " + System.currentTimeMillis() + " ems): " + str);
    }
}
