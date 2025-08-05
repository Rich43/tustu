package com.sun.corba.se.impl.naming.pcosnaming;

import com.sun.corba.se.impl.logging.NamingSystemException;
import com.sun.corba.se.impl.naming.cosnaming.InterOperableNamingImpl;
import com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore;
import com.sun.corba.se.impl.naming.cosnaming.NamingUtils;
import com.sun.corba.se.impl.naming.namingutil.INSURLHandler;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.commons.math3.geometry.VectorFormat;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.SystemException;
import org.omg.CosNaming.BindingIteratorHelper;
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
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.ServantRetentionPolicyValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/pcosnaming/NamingContextImpl.class */
public class NamingContextImpl extends NamingContextExtPOA implements NamingContextDataStore, Serializable {
    private transient ORB orb;
    private final String objKey;
    private final Hashtable theHashtable = new Hashtable();
    private transient NameService theNameServiceHandle;
    private transient ServantManagerImpl theServantManagerImplHandle;
    private transient InterOperableNamingImpl insImpl;
    private transient NamingSystemException readWrapper;
    private transient NamingSystemException updateWrapper;
    private static POA biPOA = null;
    private static boolean debug;

    public NamingContextImpl(ORB orb, String str, NameService nameService, ServantManagerImpl servantManagerImpl) throws Exception {
        this.orb = orb;
        this.readWrapper = NamingSystemException.get(orb, CORBALogDomains.NAMING_READ);
        this.updateWrapper = NamingSystemException.get(orb, CORBALogDomains.NAMING_UPDATE);
        debug = true;
        this.objKey = str;
        this.theNameServiceHandle = nameService;
        this.theServantManagerImplHandle = servantManagerImpl;
        this.insImpl = new InterOperableNamingImpl();
    }

    InterOperableNamingImpl getINSImpl() {
        if (this.insImpl == null) {
            this.insImpl = new InterOperableNamingImpl();
        }
        return this.insImpl;
    }

    public void setRootNameService(NameService nameService) {
        this.theNameServiceHandle = nameService;
    }

    public void setORB(ORB orb) {
        this.orb = orb;
    }

    public void setServantManagerImpl(ServantManagerImpl servantManagerImpl) {
        this.theServantManagerImplHandle = servantManagerImpl;
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public POA getNSPOA() {
        return this.theNameServiceHandle.getNSPOA();
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void bind(NameComponent[] nameComponentArr, Object object) throws NotFound, AlreadyBound, InvalidName, CannotProceed {
        if (object == null) {
            throw this.updateWrapper.objectIsNull();
        }
        if (debug) {
            dprint("bind " + nameToString(nameComponentArr) + " to " + ((Object) object));
        }
        doBind(this, nameComponentArr, object, false, BindingType.nobject);
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void bind_context(NameComponent[] nameComponentArr, NamingContext namingContext) throws NotFound, AlreadyBound, InvalidName, CannotProceed {
        if (namingContext == null) {
            throw this.updateWrapper.objectIsNull();
        }
        doBind(this, nameComponentArr, namingContext, false, BindingType.ncontext);
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void rebind(NameComponent[] nameComponentArr, Object object) throws NotFound, InvalidName, CannotProceed {
        if (object == null) {
            throw this.updateWrapper.objectIsNull();
        }
        try {
            if (debug) {
                dprint("rebind " + nameToString(nameComponentArr) + " to " + ((Object) object));
            }
            doBind(this, nameComponentArr, object, true, BindingType.nobject);
        } catch (AlreadyBound e2) {
            throw this.updateWrapper.namingCtxRebindAlreadyBound(e2);
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void rebind_context(NameComponent[] nameComponentArr, NamingContext namingContext) throws NotFound, InvalidName, CannotProceed {
        try {
            if (debug) {
                dprint("rebind_context " + nameToString(nameComponentArr) + " to " + ((Object) namingContext));
            }
            doBind(this, nameComponentArr, namingContext, true, BindingType.ncontext);
        } catch (AlreadyBound e2) {
            throw this.updateWrapper.namingCtxRebindAlreadyBound(e2);
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public Object resolve(NameComponent[] nameComponentArr) throws NotFound, InvalidName, CannotProceed {
        if (debug) {
            dprint("resolve " + nameToString(nameComponentArr));
        }
        return doResolve(this, nameComponentArr);
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void unbind(NameComponent[] nameComponentArr) throws NotFound, InvalidName, CannotProceed {
        if (debug) {
            dprint("unbind " + nameToString(nameComponentArr));
        }
        doUnbind(this, nameComponentArr);
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public void list(int i2, BindingListHolder bindingListHolder, BindingIteratorHolder bindingIteratorHolder) {
        if (debug) {
            dprint("list(" + i2 + ")");
        }
        synchronized (this) {
            List(i2, bindingListHolder, bindingIteratorHolder);
        }
        if (debug && bindingListHolder.value != null) {
            dprint("list(" + i2 + ") -> bindings[" + bindingListHolder.value.length + "] + iterator: " + ((Object) bindingIteratorHolder.value));
        }
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public synchronized NamingContext new_context() {
        NamingContext namingContextNewContext;
        if (debug) {
            dprint("new_context()");
        }
        synchronized (this) {
            namingContextNewContext = NewContext();
        }
        return namingContextNewContext;
    }

    @Override // org.omg.CosNaming.NamingContextOperations
    public NamingContext bind_new_context(NameComponent[] nameComponentArr) throws NotFound, AlreadyBound, InvalidName, CannotProceed {
        NamingContextOperations namingContextOperations = null;
        try {
            if (debug) {
                dprint("bind_new_context " + nameToString(nameComponentArr));
            }
            NamingContext namingContextNew_context = new_context();
            bind_context(nameComponentArr, namingContextNew_context);
            namingContextOperations = null;
            if (0 != 0) {
                try {
                    namingContextOperations.destroy();
                } catch (NotEmpty e2) {
                }
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
        if (debug) {
            dprint("destroy ");
        }
        synchronized (this) {
            if (IsEmpty()) {
                Destroy();
            } else {
                throw new NotEmpty();
            }
        }
    }

    private void doBind(NamingContextDataStore namingContextDataStore, NameComponent[] nameComponentArr, Object object, boolean z2, BindingType bindingType) throws NotFound, AlreadyBound, InvalidName, CannotProceed {
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
                throw this.updateWrapper.namingCtxBadBindingtype();
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
        return namingContextResolveFirstAsContext.resolve(nameComponentArr2);
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

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public void Bind(NameComponent nameComponent, Object object, BindingType bindingType) {
        InternalBindingValue internalBindingValue;
        if (object == null) {
            return;
        }
        InternalBindingKey internalBindingKey = new InternalBindingKey(nameComponent);
        try {
            if (bindingType.value() == 0) {
                internalBindingValue = new InternalBindingValue(bindingType, this.orb.object_to_string(object));
                internalBindingValue.setObjectRef(object);
            } else {
                internalBindingValue = new InternalBindingValue(bindingType, this.theNameServiceHandle.getObjectKey(object));
                internalBindingValue.setObjectRef(object);
            }
            if (((InternalBindingValue) this.theHashtable.put(internalBindingKey, internalBindingValue)) != null) {
                throw this.updateWrapper.namingCtxRebindAlreadyBound();
            }
            try {
                this.theServantManagerImplHandle.updateContext(this.objKey, this);
            } catch (Exception e2) {
                throw this.updateWrapper.bindUpdateContextFailed(e2);
            }
        } catch (Exception e3) {
            throw this.updateWrapper.bindFailure(e3);
        }
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public Object Resolve(NameComponent nameComponent, BindingTypeHolder bindingTypeHolder) throws SystemException {
        if (nameComponent.id.length() == 0 && nameComponent.kind.length() == 0) {
            bindingTypeHolder.value = BindingType.ncontext;
            return this.theNameServiceHandle.getObjectReferenceFromKey(this.objKey);
        }
        InternalBindingValue internalBindingValue = (InternalBindingValue) this.theHashtable.get(new InternalBindingKey(nameComponent));
        if (internalBindingValue == null) {
            return null;
        }
        bindingTypeHolder.value = internalBindingValue.theBindingType;
        try {
            if (internalBindingValue.strObjectRef.startsWith("NC")) {
                bindingTypeHolder.value = BindingType.ncontext;
                return this.theNameServiceHandle.getObjectReferenceFromKey(internalBindingValue.strObjectRef);
            }
            Object objectRef = internalBindingValue.getObjectRef();
            if (objectRef == null) {
                try {
                    objectRef = this.orb.string_to_object(internalBindingValue.strObjectRef);
                    internalBindingValue.setObjectRef(objectRef);
                } catch (Exception e2) {
                    throw this.readWrapper.resolveConversionFailure(CompletionStatus.COMPLETED_MAYBE, e2);
                }
            }
            return objectRef;
        } catch (Exception e3) {
            throw this.readWrapper.resolveFailure(CompletionStatus.COMPLETED_MAYBE, e3);
        }
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public Object Unbind(NameComponent nameComponent) throws SystemException {
        try {
            InternalBindingValue internalBindingValue = null;
            try {
                internalBindingValue = (InternalBindingValue) this.theHashtable.remove(new InternalBindingKey(nameComponent));
            } catch (Exception e2) {
            }
            this.theServantManagerImplHandle.updateContext(this.objKey, this);
            if (internalBindingValue == null) {
                return null;
            }
            if (internalBindingValue.strObjectRef.startsWith("NC")) {
                this.theServantManagerImplHandle.readInContext(internalBindingValue.strObjectRef);
                return this.theNameServiceHandle.getObjectReferenceFromKey(internalBindingValue.strObjectRef);
            }
            Object objectRef = internalBindingValue.getObjectRef();
            if (objectRef == null) {
                objectRef = this.orb.string_to_object(internalBindingValue.strObjectRef);
            }
            return objectRef;
        } catch (Exception e3) {
            throw this.updateWrapper.unbindFailure(CompletionStatus.COMPLETED_MAYBE, e3);
        }
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public void List(int i2, BindingListHolder bindingListHolder, BindingIteratorHolder bindingIteratorHolder) throws SystemException {
        if (biPOA == null) {
            createbiPOA();
        }
        try {
            PersistentBindingIterator persistentBindingIterator = new PersistentBindingIterator(this.orb, (Hashtable) this.theHashtable.clone(), biPOA);
            persistentBindingIterator.list(i2, bindingListHolder);
            bindingIteratorHolder.value = BindingIteratorHelper.narrow(biPOA.id_to_reference(biPOA.activate_object(persistentBindingIterator)));
        } catch (SystemException e2) {
            throw e2;
        } catch (Exception e3) {
            throw this.readWrapper.transNcListGotExc(e3);
        }
    }

    private synchronized void createbiPOA() {
        if (biPOA != null) {
            return;
        }
        try {
            POA poa = (POA) this.orb.resolve_initial_references(ORBConstants.ROOT_POA_NAME);
            poa.the_POAManager().activate();
            Policy[] policyArr = new Policy[3];
            int i2 = 0 + 1;
            policyArr[0] = poa.create_lifespan_policy(LifespanPolicyValue.TRANSIENT);
            int i3 = i2 + 1;
            policyArr[i2] = poa.create_id_assignment_policy(IdAssignmentPolicyValue.SYSTEM_ID);
            int i4 = i3 + 1;
            policyArr[i3] = poa.create_servant_retention_policy(ServantRetentionPolicyValue.RETAIN);
            biPOA = poa.create_POA("BindingIteratorPOA", null, policyArr);
            biPOA.the_POAManager().activate();
        } catch (Exception e2) {
            throw this.readWrapper.namingCtxBindingIteratorCreate(e2);
        }
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public NamingContext NewContext() throws SystemException {
        try {
            return this.theNameServiceHandle.NewContext();
        } catch (SystemException e2) {
            throw e2;
        } catch (Exception e3) {
            throw this.updateWrapper.transNcNewctxGotExc(e3);
        }
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public void Destroy() throws SystemException {
    }

    @Override // org.omg.CosNaming.NamingContextExtOperations
    public String to_string(NameComponent[] nameComponentArr) throws InvalidName {
        if (nameComponentArr == null || nameComponentArr.length == 0) {
            throw new InvalidName();
        }
        String strConvertToString = getINSImpl().convertToString(nameComponentArr);
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
        NameComponent[] nameComponentArrConvertToNameComponent = getINSImpl().convertToNameComponent(str);
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
        String strCreateURLBasedAddress;
        if (str2 == null || str2.length() == 0) {
            throw new InvalidName();
        }
        if (str == null) {
            throw new InvalidAddress();
        }
        try {
            strCreateURLBasedAddress = getINSImpl().createURLBasedAddress(str, str2);
        } catch (Exception e2) {
            strCreateURLBasedAddress = null;
        }
        try {
            INSURLHandler.getINSURLHandler().parseURL(strCreateURLBasedAddress);
            return strCreateURLBasedAddress;
        } catch (BAD_PARAM e3) {
            throw new InvalidAddress();
        }
    }

    @Override // org.omg.CosNaming.NamingContextExtOperations
    public Object resolve_str(String str) throws NotFound, InvalidName, CannotProceed {
        if (str == null || str.length() == 0) {
            throw new InvalidName();
        }
        NameComponent[] nameComponentArrConvertToNameComponent = getINSImpl().convertToNameComponent(str);
        if (nameComponentArrConvertToNameComponent == null || nameComponentArrConvertToNameComponent.length == 0) {
            throw new InvalidName();
        }
        return resolve(nameComponentArrConvertToNameComponent);
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public boolean IsEmpty() {
        return this.theHashtable.isEmpty();
    }

    public void printSize() {
        System.out.println("Hashtable Size = " + this.theHashtable.size());
        Enumeration enumerationKeys = this.theHashtable.keys();
        while (enumerationKeys.hasMoreElements()) {
            InternalBindingValue internalBindingValue = (InternalBindingValue) this.theHashtable.get(enumerationKeys.nextElement2());
            if (internalBindingValue != null) {
                System.out.println("value = " + internalBindingValue.strObjectRef);
            }
        }
    }
}
