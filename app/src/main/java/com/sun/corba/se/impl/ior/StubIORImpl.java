package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import sun.corba.SharedSecrets;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/StubIORImpl.class */
public class StubIORImpl {
    private int hashCode;
    private byte[] typeData;
    private int[] profileTags;
    private byte[][] profileData;

    public StubIORImpl() {
        this.hashCode = 0;
        this.typeData = null;
        this.profileTags = null;
        this.profileData = (byte[][]) null;
    }

    public String getRepositoryId() {
        if (this.typeData == null) {
            return null;
        }
        return new String(this.typeData);
    }

    /* JADX WARN: Type inference failed for: r1v8, types: [byte[], byte[][]] */
    public StubIORImpl(Object object) {
        OutputStream outputStreamCreate_output_stream = StubAdapter.getORB(object).create_output_stream();
        outputStreamCreate_output_stream.write_Object(object);
        InputStream inputStreamCreate_input_stream = outputStreamCreate_output_stream.create_input_stream();
        int i2 = inputStreamCreate_input_stream.read_long();
        this.typeData = new byte[i2];
        inputStreamCreate_input_stream.read_octet_array(this.typeData, 0, i2);
        int i3 = inputStreamCreate_input_stream.read_long();
        this.profileTags = new int[i3];
        this.profileData = new byte[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            this.profileTags[i4] = inputStreamCreate_input_stream.read_long();
            this.profileData[i4] = new byte[inputStreamCreate_input_stream.read_long()];
            inputStreamCreate_input_stream.read_octet_array(this.profileData[i4], 0, this.profileData[i4].length);
        }
    }

    public Delegate getDelegate(ORB orb) {
        OutputStream outputStreamCreate_output_stream = orb.create_output_stream();
        outputStreamCreate_output_stream.write_long(this.typeData.length);
        outputStreamCreate_output_stream.write_octet_array(this.typeData, 0, this.typeData.length);
        outputStreamCreate_output_stream.write_long(this.profileTags.length);
        for (int i2 = 0; i2 < this.profileTags.length; i2++) {
            outputStreamCreate_output_stream.write_long(this.profileTags[i2]);
            outputStreamCreate_output_stream.write_long(this.profileData[i2].length);
            outputStreamCreate_output_stream.write_octet_array(this.profileData[i2], 0, this.profileData[i2].length);
        }
        return StubAdapter.getDelegate(outputStreamCreate_output_stream.create_input_stream().read_Object());
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [byte[], byte[][]] */
    public void doRead(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int i2 = objectInputStream.readInt();
        SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, byte[].class, i2);
        this.typeData = new byte[i2];
        objectInputStream.readFully(this.typeData);
        int i3 = objectInputStream.readInt();
        SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, int[].class, i3);
        SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, byte[].class, i3);
        this.profileTags = new int[i3];
        this.profileData = new byte[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            this.profileTags[i4] = objectInputStream.readInt();
            int i5 = objectInputStream.readInt();
            SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, byte[].class, i5);
            this.profileData[i4] = new byte[i5];
            objectInputStream.readFully(this.profileData[i4]);
        }
    }

    public void doWrite(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeInt(this.typeData.length);
        objectOutputStream.write(this.typeData);
        objectOutputStream.writeInt(this.profileTags.length);
        for (int i2 = 0; i2 < this.profileTags.length; i2++) {
            objectOutputStream.writeInt(this.profileTags[i2]);
            objectOutputStream.writeInt(this.profileData[i2].length);
            objectOutputStream.write(this.profileData[i2]);
        }
    }

    public synchronized int hashCode() {
        if (this.hashCode == 0) {
            for (int i2 = 0; i2 < this.typeData.length; i2++) {
                this.hashCode = (this.hashCode * 37) + this.typeData[i2];
            }
            for (int i3 = 0; i3 < this.profileTags.length; i3++) {
                this.hashCode = (this.hashCode * 37) + this.profileTags[i3];
                for (int i4 = 0; i4 < this.profileData[i3].length; i4++) {
                    this.hashCode = (this.hashCode * 37) + this.profileData[i3][i4];
                }
            }
        }
        return this.hashCode;
    }

    private boolean equalArrays(int[] iArr, int[] iArr2) {
        if (iArr.length != iArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] != iArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    private boolean equalArrays(byte[] bArr, byte[] bArr2) {
        if (bArr.length != bArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if (bArr[i2] != bArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    private boolean equalArrays(byte[][] bArr, byte[][] bArr2) {
        if (bArr.length != bArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if (!equalArrays(bArr[i2], bArr2[i2])) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StubIORImpl)) {
            return false;
        }
        StubIORImpl stubIORImpl = (StubIORImpl) obj;
        return stubIORImpl.hashCode() == hashCode() && equalArrays(this.typeData, stubIORImpl.typeData) && equalArrays(this.profileTags, stubIORImpl.profileTags) && equalArrays(this.profileData, stubIORImpl.profileData);
    }

    private void appendByteArray(StringBuffer stringBuffer, byte[] bArr) {
        for (byte b2 : bArr) {
            stringBuffer.append(Integer.toHexString(b2));
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SimpleIORImpl[");
        stringBuffer.append(new String(this.typeData));
        for (int i2 = 0; i2 < this.profileTags.length; i2++) {
            stringBuffer.append(",(");
            stringBuffer.append(this.profileTags[i2]);
            stringBuffer.append(")");
            appendByteArray(stringBuffer, this.profileData[i2]);
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
