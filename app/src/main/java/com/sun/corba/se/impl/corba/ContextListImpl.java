package com.sun.corba.se.impl.corba;

import java.util.Vector;
import org.omg.CORBA.Bounds;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/ContextListImpl.class */
public class ContextListImpl extends ContextList {
    private ORB _orb;
    private final int INITIAL_CAPACITY = 2;
    private final int CAPACITY_INCREMENT = 2;
    private Vector _contexts = new Vector(2, 2);

    public ContextListImpl(ORB orb) {
        this._orb = orb;
    }

    @Override // org.omg.CORBA.ContextList
    public int count() {
        return this._contexts.size();
    }

    @Override // org.omg.CORBA.ContextList
    public void add(String str) {
        this._contexts.addElement(str);
    }

    @Override // org.omg.CORBA.ContextList
    public String item(int i2) throws Bounds {
        try {
            return (String) this._contexts.elementAt(i2);
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new Bounds();
        }
    }

    @Override // org.omg.CORBA.ContextList
    public void remove(int i2) throws Bounds {
        try {
            this._contexts.removeElementAt(i2);
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new Bounds();
        }
    }
}
