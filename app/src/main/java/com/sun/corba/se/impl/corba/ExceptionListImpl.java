package com.sun.corba.se.impl.corba;

import java.util.Vector;
import org.omg.CORBA.Bounds;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.TypeCode;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/ExceptionListImpl.class */
public class ExceptionListImpl extends ExceptionList {
    private final int INITIAL_CAPACITY = 2;
    private final int CAPACITY_INCREMENT = 2;
    private Vector _exceptions = new Vector(2, 2);

    @Override // org.omg.CORBA.ExceptionList
    public int count() {
        return this._exceptions.size();
    }

    @Override // org.omg.CORBA.ExceptionList
    public void add(TypeCode typeCode) {
        this._exceptions.addElement(typeCode);
    }

    @Override // org.omg.CORBA.ExceptionList
    public TypeCode item(int i2) throws Bounds {
        try {
            return (TypeCode) this._exceptions.elementAt(i2);
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new Bounds();
        }
    }

    @Override // org.omg.CORBA.ExceptionList
    public void remove(int i2) throws Bounds {
        try {
            this._exceptions.removeElementAt(i2);
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new Bounds();
        }
    }
}
