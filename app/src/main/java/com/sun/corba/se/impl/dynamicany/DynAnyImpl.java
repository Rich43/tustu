package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Any;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.OutputStream;
import org.omg.DynamicAny.DynAny;
import org.omg.DynamicAny.DynAnyFactory;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynAnyImpl.class */
abstract class DynAnyImpl extends LocalObject implements DynAny {
    protected static final int NO_INDEX = -1;
    protected static final byte STATUS_DESTROYABLE = 0;
    protected static final byte STATUS_UNDESTROYABLE = 1;
    protected static final byte STATUS_DESTROYED = 2;
    protected ORB orb;
    protected ORBUtilSystemException wrapper;
    protected Any any;
    protected byte status;
    protected int index;
    private String[] __ids;

    public abstract Any to_any();

    public abstract boolean equal(DynAny dynAny);

    public abstract void destroy();

    public abstract DynAny copy();

    protected DynAnyImpl() {
        this.orb = null;
        this.any = null;
        this.status = (byte) 0;
        this.index = -1;
        this.__ids = new String[]{"IDL:omg.org/DynamicAny/DynAny:1.0"};
        this.wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_PRESENTATION);
    }

    protected DynAnyImpl(ORB orb, Any any, boolean z2) {
        this.orb = null;
        this.any = null;
        this.status = (byte) 0;
        this.index = -1;
        this.__ids = new String[]{"IDL:omg.org/DynamicAny/DynAny:1.0"};
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PRESENTATION);
        if (z2) {
            this.any = DynAnyUtil.copy(any, orb);
        } else {
            this.any = any;
        }
        this.index = -1;
    }

    protected DynAnyImpl(ORB orb, TypeCode typeCode) {
        this.orb = null;
        this.any = null;
        this.status = (byte) 0;
        this.index = -1;
        this.__ids = new String[]{"IDL:omg.org/DynamicAny/DynAny:1.0"};
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PRESENTATION);
        this.any = DynAnyUtil.createDefaultAnyOfType(typeCode, orb);
    }

    protected DynAnyFactory factory() {
        try {
            return (DynAnyFactory) this.orb.resolve_initial_references(ORBConstants.DYN_ANY_FACTORY_NAME);
        } catch (InvalidName e2) {
            throw new RuntimeException("Unable to find DynAnyFactory");
        }
    }

    protected Any getAny() {
        return this.any;
    }

    protected Any getAny(DynAny dynAny) {
        if (dynAny instanceof DynAnyImpl) {
            return ((DynAnyImpl) dynAny).getAny();
        }
        return dynAny.to_any();
    }

    protected void writeAny(OutputStream outputStream) {
        this.any.write_value(outputStream);
    }

    protected void setStatus(byte b2) {
        this.status = b2;
    }

    protected void clearData() {
        this.any.type(this.any.type());
    }

    public TypeCode type() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        return this.any.type();
    }

    public void assign(DynAny dynAny) throws TypeMismatch {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any != null && !this.any.type().equal(dynAny.type())) {
            throw new TypeMismatch();
        }
        this.any = dynAny.to_any();
    }

    public void from_any(Any any) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any != null && !this.any.type().equal(any.type())) {
            throw new TypeMismatch();
        }
        try {
            Any anyCopy = DynAnyUtil.copy(any, this.orb);
            if (!DynAnyUtil.isInitialized(anyCopy)) {
                throw new InvalidValue();
            }
            this.any = anyCopy;
        } catch (Exception e2) {
            throw new InvalidValue();
        }
    }

    public String[] _ids() {
        return (String[]) this.__ids.clone();
    }
}
