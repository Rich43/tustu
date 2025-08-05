package com.sun.corba.se.spi.ior;

import com.sun.corba.se.impl.ior.EncapsulationUtility;
import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/EncapsulationFactoryBase.class */
public abstract class EncapsulationFactoryBase implements IdentifiableFactory {
    private int id;

    protected abstract Identifiable readContents(InputStream inputStream);

    @Override // com.sun.corba.se.spi.ior.IdentifiableFactory
    public int getId() {
        return this.id;
    }

    public EncapsulationFactoryBase(int i2) {
        this.id = i2;
    }

    @Override // com.sun.corba.se.spi.ior.IdentifiableFactory
    public final Identifiable create(InputStream inputStream) {
        return readContents(EncapsulationUtility.getEncapsulationStream(inputStream));
    }
}
