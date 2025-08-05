package com.sun.corba.se.impl.naming.pcosnaming;

import java.io.Serializable;
import org.omg.CORBA.Object;
import org.omg.CosNaming.BindingType;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/pcosnaming/InternalBindingValue.class */
public class InternalBindingValue implements Serializable {
    public BindingType theBindingType;
    public String strObjectRef;
    private transient Object theObjectRef;

    public InternalBindingValue() {
    }

    public InternalBindingValue(BindingType bindingType, String str) {
        this.theBindingType = bindingType;
        this.strObjectRef = str;
    }

    public Object getObjectRef() {
        return this.theObjectRef;
    }

    public void setObjectRef(Object object) {
        this.theObjectRef = object;
    }
}
