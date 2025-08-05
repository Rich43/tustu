package com.sun.corba.se.impl.corba;

import com.sun.corba.se.spi.orb.ORB;
import java.util.Vector;
import org.omg.CORBA.Any;
import org.omg.CORBA.Bounds;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/NVListImpl.class */
public class NVListImpl extends NVList {
    private final int INITIAL_CAPACITY = 4;
    private final int CAPACITY_INCREMENT = 2;
    private Vector _namedValues;
    private ORB orb;

    public NVListImpl(ORB orb) {
        this.INITIAL_CAPACITY = 4;
        this.CAPACITY_INCREMENT = 2;
        this.orb = orb;
        this._namedValues = new Vector(4, 2);
    }

    public NVListImpl(ORB orb, int i2) {
        this.INITIAL_CAPACITY = 4;
        this.CAPACITY_INCREMENT = 2;
        this.orb = orb;
        this._namedValues = new Vector(i2);
    }

    @Override // org.omg.CORBA.NVList
    public int count() {
        return this._namedValues.size();
    }

    @Override // org.omg.CORBA.NVList
    public NamedValue add(int i2) {
        NamedValueImpl namedValueImpl = new NamedValueImpl(this.orb, "", new AnyImpl(this.orb), i2);
        this._namedValues.addElement(namedValueImpl);
        return namedValueImpl;
    }

    @Override // org.omg.CORBA.NVList
    public NamedValue add_item(String str, int i2) {
        NamedValueImpl namedValueImpl = new NamedValueImpl(this.orb, str, new AnyImpl(this.orb), i2);
        this._namedValues.addElement(namedValueImpl);
        return namedValueImpl;
    }

    @Override // org.omg.CORBA.NVList
    public NamedValue add_value(String str, Any any, int i2) {
        NamedValueImpl namedValueImpl = new NamedValueImpl(this.orb, str, any, i2);
        this._namedValues.addElement(namedValueImpl);
        return namedValueImpl;
    }

    @Override // org.omg.CORBA.NVList
    public NamedValue item(int i2) throws Bounds {
        try {
            return (NamedValue) this._namedValues.elementAt(i2);
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new Bounds();
        }
    }

    @Override // org.omg.CORBA.NVList
    public void remove(int i2) throws Bounds {
        try {
            this._namedValues.removeElementAt(i2);
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new Bounds();
        }
    }
}
