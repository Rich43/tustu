package com.sun.corba.se.impl.naming.cosnaming;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingHolder;
import org.omg.CosNaming.BindingIteratorPOA;
import org.omg.CosNaming.BindingListHolder;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/cosnaming/BindingIteratorImpl.class */
public abstract class BindingIteratorImpl extends BindingIteratorPOA {
    protected ORB orb;

    protected abstract boolean NextOne(BindingHolder bindingHolder);

    protected abstract void Destroy();

    protected abstract int RemainingElements();

    public BindingIteratorImpl(ORB orb) throws Exception {
        this.orb = orb;
    }

    @Override // org.omg.CosNaming.BindingIteratorOperations
    public synchronized boolean next_one(BindingHolder bindingHolder) {
        return NextOne(bindingHolder);
    }

    @Override // org.omg.CosNaming.BindingIteratorOperations
    public synchronized boolean next_n(int i2, BindingListHolder bindingListHolder) {
        if (i2 == 0) {
            throw new BAD_PARAM(" 'how_many' parameter is set to 0 which is invalid");
        }
        return list(i2, bindingListHolder);
    }

    public boolean list(int i2, BindingListHolder bindingListHolder) {
        int iMin = Math.min(RemainingElements(), i2);
        Binding[] bindingArr = new Binding[iMin];
        BindingHolder bindingHolder = new BindingHolder();
        int i3 = 0;
        while (i3 < iMin && NextOne(bindingHolder)) {
            bindingArr[i3] = bindingHolder.value;
            i3++;
        }
        if (i3 == 0) {
            bindingListHolder.value = new Binding[0];
            return false;
        }
        bindingListHolder.value = bindingArr;
        return true;
    }

    @Override // org.omg.CosNaming.BindingIteratorOperations
    public synchronized void destroy() {
        Destroy();
    }
}
