package com.sun.corba.se.impl.naming.cosnaming;

import com.sun.corba.se.impl.logging.NamingSystemException;
import com.sun.corba.se.impl.orbutil.LogKeywords;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingIteratorHelper;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.BindingTypeHolder;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.PortableServer.POA;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/cosnaming/TransientNamingContext.class */
public class TransientNamingContext extends NamingContextImpl implements NamingContextDataStore {
    private Logger readLogger;
    private Logger updateLogger;
    private Logger lifecycleLogger;
    private NamingSystemException wrapper;
    private final Hashtable theHashtable;
    public Object localRoot;

    public TransientNamingContext(ORB orb, Object object, POA poa) throws Exception {
        super(orb, poa);
        this.theHashtable = new Hashtable();
        this.wrapper = NamingSystemException.get(orb, CORBALogDomains.NAMING);
        this.localRoot = object;
        this.readLogger = orb.getLogger(CORBALogDomains.NAMING_READ);
        this.updateLogger = orb.getLogger(CORBALogDomains.NAMING_UPDATE);
        this.lifecycleLogger = orb.getLogger(CORBALogDomains.NAMING_LIFECYCLE);
        this.lifecycleLogger.fine("Root TransientNamingContext LIFECYCLE.CREATED");
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public final void Bind(NameComponent nameComponent, Object object, BindingType bindingType) throws SystemException {
        InternalBindingKey internalBindingKey = new InternalBindingKey(nameComponent);
        InternalBindingValue internalBindingValue = new InternalBindingValue(new Binding(new NameComponent[]{nameComponent}, bindingType), null);
        internalBindingValue.theObjectRef = object;
        if (((InternalBindingValue) this.theHashtable.put(internalBindingKey, internalBindingValue)) != null) {
            this.updateLogger.warning("<<NAMING BIND>>Name " + getName(nameComponent) + " Was Already Bound");
            throw this.wrapper.transNcBindAlreadyBound();
        }
        if (this.updateLogger.isLoggable(Level.FINE)) {
            this.updateLogger.fine("<<NAMING BIND>><<SUCCESS>>Name Component: " + nameComponent.id + "." + nameComponent.kind);
        }
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public final Object Resolve(NameComponent nameComponent, BindingTypeHolder bindingTypeHolder) throws SystemException {
        if (nameComponent.id.length() == 0 && nameComponent.kind.length() == 0) {
            bindingTypeHolder.value = BindingType.ncontext;
            return this.localRoot;
        }
        InternalBindingValue internalBindingValue = (InternalBindingValue) this.theHashtable.get(new InternalBindingKey(nameComponent));
        if (internalBindingValue == null) {
            return null;
        }
        if (this.readLogger.isLoggable(Level.FINE)) {
            this.readLogger.fine("<<NAMING RESOLVE>><<SUCCESS>>Namecomponent :" + getName(nameComponent));
        }
        bindingTypeHolder.value = internalBindingValue.theBinding.binding_type;
        return internalBindingValue.theObjectRef;
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public final Object Unbind(NameComponent nameComponent) throws SystemException {
        InternalBindingValue internalBindingValue = (InternalBindingValue) this.theHashtable.remove(new InternalBindingKey(nameComponent));
        if (internalBindingValue == null) {
            if (this.updateLogger.isLoggable(Level.FINE)) {
                this.updateLogger.fine("<<NAMING UNBIND>><<FAILURE>> There was no binding with the name " + getName(nameComponent) + " to Unbind ");
                return null;
            }
            return null;
        }
        if (this.updateLogger.isLoggable(Level.FINE)) {
            this.updateLogger.fine("<<NAMING UNBIND>><<SUCCESS>> NameComponent:  " + getName(nameComponent));
        }
        return internalBindingValue.theObjectRef;
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public final void List(int i2, BindingListHolder bindingListHolder, BindingIteratorHolder bindingIteratorHolder) throws SystemException {
        try {
            TransientBindingIterator transientBindingIterator = new TransientBindingIterator(this.orb, (Hashtable) this.theHashtable.clone(), this.nsPOA);
            transientBindingIterator.list(i2, bindingListHolder);
            bindingIteratorHolder.value = BindingIteratorHelper.narrow(this.nsPOA.id_to_reference(this.nsPOA.activate_object(transientBindingIterator)));
        } catch (SystemException e2) {
            this.readLogger.warning(LogKeywords.NAMING_LIST_FAILURE + ((Object) e2));
            throw e2;
        } catch (Exception e3) {
            this.readLogger.severe(LogKeywords.NAMING_LIST_FAILURE + ((Object) e3));
            throw this.wrapper.transNcListGotExc(e3);
        }
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public final NamingContext NewContext() throws SystemException {
        try {
            Object objectId_to_reference = this.nsPOA.id_to_reference(this.nsPOA.activate_object(new TransientNamingContext(this.orb, this.localRoot, this.nsPOA)));
            this.lifecycleLogger.fine("TransientNamingContext LIFECYCLE.CREATE SUCCESSFUL");
            return NamingContextHelper.narrow(objectId_to_reference);
        } catch (SystemException e2) {
            this.lifecycleLogger.log(Level.WARNING, LogKeywords.LIFECYCLE_CREATE_FAILURE, (Throwable) e2);
            throw e2;
        } catch (Exception e3) {
            this.lifecycleLogger.log(Level.WARNING, LogKeywords.LIFECYCLE_CREATE_FAILURE, (Throwable) e3);
            throw this.wrapper.transNcNewctxGotExc(e3);
        }
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public final void Destroy() throws SystemException {
        try {
            byte[] bArrServant_to_id = this.nsPOA.servant_to_id(this);
            if (bArrServant_to_id != null) {
                this.nsPOA.deactivate_object(bArrServant_to_id);
            }
            if (this.lifecycleLogger.isLoggable(Level.FINE)) {
                this.lifecycleLogger.fine(LogKeywords.LIFECYCLE_DESTROY_SUCCESS);
            }
        } catch (SystemException e2) {
            this.lifecycleLogger.log(Level.WARNING, LogKeywords.LIFECYCLE_DESTROY_FAILURE, (Throwable) e2);
            throw e2;
        } catch (Exception e3) {
            this.lifecycleLogger.log(Level.WARNING, LogKeywords.LIFECYCLE_DESTROY_FAILURE, (Throwable) e3);
            throw this.wrapper.transNcDestroyGotExc(e3);
        }
    }

    private String getName(NameComponent nameComponent) {
        return nameComponent.id + "." + nameComponent.kind;
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore
    public final boolean IsEmpty() {
        return this.theHashtable.isEmpty();
    }
}
