package com.sun.corba.se.impl.ior;

import com.sun.corba.se.impl.logging.IORSystemException;
import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersion;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import java.util.Iterator;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/ObjectKeyTemplateBase.class */
public abstract class ObjectKeyTemplateBase implements ObjectKeyTemplate {
    public static final String JIDL_ORB_ID = "";
    private static final String[] JIDL_OAID_STRINGS = {"TransientObjectAdapter"};
    public static final ObjectAdapterId JIDL_OAID = new ObjectAdapterIdArray(JIDL_OAID_STRINGS);
    private ORB orb;
    protected IORSystemException wrapper;
    private ORBVersion version;
    private int magic;
    private int scid;
    private int serverid;
    private String orbid;
    private ObjectAdapterId oaid;
    private byte[] adapterId = computeAdapterId();

    protected abstract void writeTemplate(OutputStream outputStream);

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public byte[] getAdapterId() {
        return (byte[]) this.adapterId.clone();
    }

    private byte[] computeAdapterId() {
        ByteBuffer byteBuffer = new ByteBuffer();
        byteBuffer.append(getServerId());
        byteBuffer.append(this.orbid);
        byteBuffer.append(this.oaid.getNumLevels());
        Iterator it = this.oaid.iterator();
        while (it.hasNext()) {
            byteBuffer.append((String) it.next());
        }
        byteBuffer.trimToSize();
        return byteBuffer.toArray();
    }

    public ObjectKeyTemplateBase(ORB orb, int i2, int i3, int i4, String str, ObjectAdapterId objectAdapterId) {
        this.orb = orb;
        this.wrapper = IORSystemException.get(orb, CORBALogDomains.OA_IOR);
        this.magic = i2;
        this.scid = i3;
        this.serverid = i4;
        this.orbid = str;
        this.oaid = objectAdapterId;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ObjectKeyTemplateBase)) {
            return false;
        }
        ObjectKeyTemplateBase objectKeyTemplateBase = (ObjectKeyTemplateBase) obj;
        return this.magic == objectKeyTemplateBase.magic && this.scid == objectKeyTemplateBase.scid && this.serverid == objectKeyTemplateBase.serverid && this.version.equals(objectKeyTemplateBase.version) && this.orbid.equals(objectKeyTemplateBase.orbid) && this.oaid.equals(objectKeyTemplateBase.oaid);
    }

    public int hashCode() {
        return (37 * ((37 * ((37 * ((37 * ((37 * ((37 * 17) + this.magic)) + this.scid)) + this.serverid)) + this.version.hashCode())) + this.orbid.hashCode())) + this.oaid.hashCode();
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public int getSubcontractId() {
        return this.scid;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public int getServerId() {
        return this.serverid;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public String getORBId() {
        return this.orbid;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public ObjectAdapterId getObjectAdapterId() {
        return this.oaid;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public void write(ObjectId objectId, OutputStream outputStream) {
        writeTemplate(outputStream);
        objectId.write(outputStream);
    }

    @Override // com.sun.corba.se.spi.ior.Writeable
    public void write(OutputStream outputStream) {
        writeTemplate(outputStream);
    }

    protected int getMagic() {
        return this.magic;
    }

    public void setORBVersion(ORBVersion oRBVersion) {
        this.version = oRBVersion;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public ORBVersion getORBVersion() {
        return this.version;
    }

    protected byte[] readObjectKey(InputStream inputStream) {
        int i2 = inputStream.read_long();
        byte[] bArr = new byte[i2];
        inputStream.read_octet_array(bArr, 0, i2);
        return bArr;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public CorbaServerRequestDispatcher getServerRequestDispatcher(ORB orb, ObjectId objectId) {
        return orb.getRequestDispatcherRegistry().getServerRequestDispatcher(this.scid);
    }
}
