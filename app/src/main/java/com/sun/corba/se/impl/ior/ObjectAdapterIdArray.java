package com.sun.corba.se.impl.ior;

import java.util.Arrays;
import java.util.Iterator;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/ObjectAdapterIdArray.class */
public class ObjectAdapterIdArray extends ObjectAdapterIdBase {
    private final String[] objectAdapterId;

    @Override // com.sun.corba.se.impl.ior.ObjectAdapterIdBase, com.sun.corba.se.spi.ior.Writeable
    public /* bridge */ /* synthetic */ void write(OutputStream outputStream) {
        super.write(outputStream);
    }

    @Override // com.sun.corba.se.impl.ior.ObjectAdapterIdBase
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.sun.corba.se.impl.ior.ObjectAdapterIdBase
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.sun.corba.se.impl.ior.ObjectAdapterIdBase
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public ObjectAdapterIdArray(String[] strArr) {
        this.objectAdapterId = strArr;
    }

    public ObjectAdapterIdArray(String str, String str2) {
        this.objectAdapterId = new String[2];
        this.objectAdapterId[0] = str;
        this.objectAdapterId[1] = str2;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectAdapterId
    public int getNumLevels() {
        return this.objectAdapterId.length;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectAdapterId
    public Iterator iterator() {
        return Arrays.asList(this.objectAdapterId).iterator();
    }

    @Override // com.sun.corba.se.spi.ior.ObjectAdapterId
    public String[] getAdapterName() {
        return (String[]) this.objectAdapterId.clone();
    }
}
