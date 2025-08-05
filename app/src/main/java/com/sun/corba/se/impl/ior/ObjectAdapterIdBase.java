package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.ObjectAdapterId;
import java.util.Iterator;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/ObjectAdapterIdBase.class */
abstract class ObjectAdapterIdBase implements ObjectAdapterId {
    ObjectAdapterIdBase() {
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ObjectAdapterId)) {
            return false;
        }
        Iterator it = iterator();
        Iterator it2 = ((ObjectAdapterId) obj).iterator();
        while (it.hasNext() && it2.hasNext()) {
            if (!((String) it.next()).equals((String) it2.next())) {
                return false;
            }
        }
        return it.hasNext() == it2.hasNext();
    }

    public int hashCode() {
        int iHashCode = 17;
        Iterator it = iterator();
        while (it.hasNext()) {
            iHashCode = (37 * iHashCode) + ((String) it.next()).hashCode();
        }
        return iHashCode;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ObjectAdapterID[");
        Iterator it = iterator();
        boolean z2 = true;
        while (it.hasNext()) {
            String str = (String) it.next();
            if (z2) {
                z2 = false;
            } else {
                stringBuffer.append("/");
            }
            stringBuffer.append(str);
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    @Override // com.sun.corba.se.spi.ior.Writeable
    public void write(OutputStream outputStream) {
        outputStream.write_long(getNumLevels());
        Iterator it = iterator();
        while (it.hasNext()) {
            outputStream.write_string((String) it.next());
        }
    }
}
