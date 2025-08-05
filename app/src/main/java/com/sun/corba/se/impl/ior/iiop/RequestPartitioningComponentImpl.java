package com.sun.corba.se.impl.ior.iiop;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.ior.TaggedComponentBase;
import com.sun.corba.se.spi.ior.iiop.RequestPartitioningComponent;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/RequestPartitioningComponentImpl.class */
public class RequestPartitioningComponentImpl extends TaggedComponentBase implements RequestPartitioningComponent {
    private static ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.OA_IOR);
    private int partitionToUse;

    public boolean equals(Object obj) {
        return (obj instanceof RequestPartitioningComponentImpl) && this.partitionToUse == ((RequestPartitioningComponentImpl) obj).partitionToUse;
    }

    public int hashCode() {
        return this.partitionToUse;
    }

    public String toString() {
        return "RequestPartitioningComponentImpl[partitionToUse=" + this.partitionToUse + "]";
    }

    public RequestPartitioningComponentImpl() {
        this.partitionToUse = 0;
    }

    public RequestPartitioningComponentImpl(int i2) {
        if (i2 < 0 || i2 > 63) {
            throw wrapper.invalidRequestPartitioningComponentValue(new Integer(i2), new Integer(0), new Integer(63));
        }
        this.partitionToUse = i2;
    }

    @Override // com.sun.corba.se.spi.ior.iiop.RequestPartitioningComponent
    public int getRequestPartitioningId() {
        return this.partitionToUse;
    }

    @Override // com.sun.corba.se.spi.ior.WriteContents
    public void writeContents(OutputStream outputStream) {
        outputStream.write_ulong(this.partitionToUse);
    }

    @Override // com.sun.corba.se.spi.ior.Identifiable
    public int getId() {
        return ORBConstants.TAG_REQUEST_PARTITIONING_ID;
    }
}
