package com.sun.corba.se.impl.ior;

import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import org.omg.CORBA.ORB;
import org.omg.CORBA_2_3.portable.OutputStream;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/ObjectKeyImpl.class */
public class ObjectKeyImpl implements ObjectKey {
    private ObjectKeyTemplate oktemp;
    private ObjectId id;

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ObjectKeyImpl)) {
            return false;
        }
        ObjectKeyImpl objectKeyImpl = (ObjectKeyImpl) obj;
        return this.oktemp.equals(objectKeyImpl.oktemp) && this.id.equals(objectKeyImpl.id);
    }

    public int hashCode() {
        return this.oktemp.hashCode() ^ this.id.hashCode();
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKey
    public ObjectKeyTemplate getTemplate() {
        return this.oktemp;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKey
    public ObjectId getId() {
        return this.id;
    }

    public ObjectKeyImpl(ObjectKeyTemplate objectKeyTemplate, ObjectId objectId) {
        this.oktemp = objectKeyTemplate;
        this.id = objectId;
    }

    @Override // com.sun.corba.se.spi.ior.Writeable
    public void write(OutputStream outputStream) {
        this.oktemp.write(this.id, outputStream);
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKey
    public byte[] getBytes(ORB orb) {
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((com.sun.corba.se.spi.orb.ORB) orb);
        write(encapsOutputStreamNewEncapsOutputStream);
        return encapsOutputStreamNewEncapsOutputStream.toByteArray();
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKey
    public CorbaServerRequestDispatcher getServerRequestDispatcher(com.sun.corba.se.spi.orb.ORB orb) {
        return this.oktemp.getServerRequestDispatcher(orb, this.id);
    }
}
