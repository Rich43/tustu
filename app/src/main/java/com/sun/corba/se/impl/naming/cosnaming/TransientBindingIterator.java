package com.sun.corba.se.impl.naming.cosnaming;

import java.util.Enumeration;
import java.util.Hashtable;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.NameComponent;
import org.omg.PortableServer.POA;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/cosnaming/TransientBindingIterator.class */
public class TransientBindingIterator extends BindingIteratorImpl {
    private POA nsPOA;
    private int currentSize;
    private Hashtable theHashtable;
    private Enumeration theEnumeration;

    public TransientBindingIterator(ORB orb, Hashtable hashtable, POA poa) throws Exception {
        super(orb);
        this.theHashtable = hashtable;
        this.theEnumeration = this.theHashtable.elements();
        this.currentSize = this.theHashtable.size();
        this.nsPOA = poa;
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.BindingIteratorImpl
    public final boolean NextOne(BindingHolder bindingHolder) {
        boolean zHasMoreElements = this.theEnumeration.hasMoreElements();
        if (zHasMoreElements) {
            bindingHolder.value = ((InternalBindingValue) this.theEnumeration.nextElement2()).theBinding;
            this.currentSize--;
        } else {
            bindingHolder.value = new Binding(new NameComponent[0], BindingType.nobject);
        }
        return zHasMoreElements;
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.BindingIteratorImpl
    public final void Destroy() {
        try {
            byte[] bArrServant_to_id = this.nsPOA.servant_to_id(this);
            if (bArrServant_to_id != null) {
                this.nsPOA.deactivate_object(bArrServant_to_id);
            }
        } catch (Exception e2) {
            NamingUtils.errprint("BindingIterator.Destroy():caught exception:");
            NamingUtils.printException(e2);
        }
    }

    @Override // com.sun.corba.se.impl.naming.cosnaming.BindingIteratorImpl
    public final int RemainingElements() {
        return this.currentSize;
    }
}
