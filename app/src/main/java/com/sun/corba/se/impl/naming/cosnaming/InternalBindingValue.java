package com.sun.corba.se.impl.naming.cosnaming;

import org.omg.CORBA.Object;
import org.omg.CosNaming.Binding;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/cosnaming/InternalBindingValue.class */
public class InternalBindingValue {
    public Binding theBinding;
    public String strObjectRef;
    public Object theObjectRef;

    public InternalBindingValue() {
    }

    public InternalBindingValue(Binding binding, String str) {
        this.theBinding = binding;
        this.strObjectRef = str;
    }
}
