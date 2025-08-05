package com.sun.corba.se.impl.ior;

import com.sun.corba.se.impl.encoding.CDRInputStream;
import com.sun.corba.se.impl.logging.IORSystemException;
import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersion;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import org.omg.CORBA.OctetSeqHolder;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/WireObjectKeyTemplate.class */
public class WireObjectKeyTemplate implements ObjectKeyTemplate {
    private ORB orb;
    private IORSystemException wrapper;

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof WireObjectKeyTemplate;
    }

    public int hashCode() {
        return 53;
    }

    private byte[] getId(InputStream inputStream) {
        CDRInputStream cDRInputStream = (CDRInputStream) inputStream;
        int bufferLength = cDRInputStream.getBufferLength();
        byte[] bArr = new byte[bufferLength];
        cDRInputStream.read_octet_array(bArr, 0, bufferLength);
        return bArr;
    }

    public WireObjectKeyTemplate(ORB orb) {
        initORB(orb);
    }

    public WireObjectKeyTemplate(InputStream inputStream, OctetSeqHolder octetSeqHolder) {
        octetSeqHolder.value = getId(inputStream);
        initORB((ORB) inputStream.orb());
    }

    private void initORB(ORB orb) {
        this.orb = orb;
        this.wrapper = IORSystemException.get(orb, CORBALogDomains.OA_IOR);
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public void write(ObjectId objectId, OutputStream outputStream) {
        byte[] id = objectId.getId();
        outputStream.write_octet_array(id, 0, id.length);
    }

    @Override // com.sun.corba.se.spi.ior.Writeable
    public void write(OutputStream outputStream) {
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public int getSubcontractId() {
        return 2;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public int getServerId() {
        return -1;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public String getORBId() {
        throw this.wrapper.orbIdNotAvailable();
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public ObjectAdapterId getObjectAdapterId() {
        throw this.wrapper.objectAdapterIdNotAvailable();
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public byte[] getAdapterId() {
        throw this.wrapper.adapterIdNotAvailable();
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public ORBVersion getORBVersion() {
        return ORBVersionFactory.getFOREIGN();
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public CorbaServerRequestDispatcher getServerRequestDispatcher(ORB orb, ObjectId objectId) {
        return orb.getRequestDispatcherRegistry().getServerRequestDispatcher(new String(objectId.getId()));
    }
}
