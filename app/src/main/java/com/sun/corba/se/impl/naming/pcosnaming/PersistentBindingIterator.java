package com.sun.corba.se.impl.naming.pcosnaming;

import com.sun.corba.se.impl.naming.cosnaming.BindingIteratorImpl;
import java.util.Enumeration;
import java.util.Hashtable;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.NameComponent;
import org.omg.PortableServer.POA;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/pcosnaming/PersistentBindingIterator.class */
public class PersistentBindingIterator extends BindingIteratorImpl {
    private POA biPOA;
    private int currentSize;
    private Hashtable theHashtable;
    private Enumeration theEnumeration;
    private ORB orb;

    public PersistentBindingIterator(ORB orb, Hashtable hashtable, POA poa) throws Exception {
        super(orb);
        this.orb = orb;
        this.theHashtable = hashtable;
        this.theEnumeration = this.theHashtable.keys();
        this.currentSize = this.theHashtable.size();
        this.biPOA = poa;
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.BindingIteratorImpl
    public final boolean NextOne(BindingHolder bindingHolder) {
        boolean zHasMoreElements = this.theEnumeration.hasMoreElements();
        if (zHasMoreElements) {
            InternalBindingKey internalBindingKey = (InternalBindingKey) this.theEnumeration.nextElement2();
            bindingHolder.value = new Binding(new NameComponent[]{new NameComponent(internalBindingKey.id, internalBindingKey.kind)}, ((InternalBindingValue) this.theHashtable.get(internalBindingKey)).theBindingType);
        } else {
            bindingHolder.value = new Binding(new NameComponent[0], BindingType.nobject);
        }
        return zHasMoreElements;
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.BindingIteratorImpl
    public final void Destroy() {
        try {
            byte[] bArrServant_to_id = this.biPOA.servant_to_id(this);
            if (bArrServant_to_id != null) {
                this.biPOA.deactivate_object(bArrServant_to_id);
            }
        } catch (Exception e2) {
            throw new INTERNAL("Exception in BindingIterator.Destroy " + ((Object) e2));
        }
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.BindingIteratorImpl
    public final int RemainingElements() {
        return this.currentSize;
    }
}
