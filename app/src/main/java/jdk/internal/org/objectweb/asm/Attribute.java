package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/Attribute.class */
public class Attribute {
    public final String type;
    byte[] value;
    Attribute next;

    protected Attribute(String str) {
        this.type = str;
    }

    public boolean isUnknown() {
        return true;
    }

    public boolean isCodeAttribute() {
        return false;
    }

    protected Label[] getLabels() {
        return null;
    }

    protected Attribute read(ClassReader classReader, int i2, int i3, char[] cArr, int i4, Label[] labelArr) {
        Attribute attribute = new Attribute(this.type);
        attribute.value = new byte[i3];
        System.arraycopy(classReader.f12859b, i2, attribute.value, 0, i3);
        return attribute;
    }

    protected ByteVector write(ClassWriter classWriter, byte[] bArr, int i2, int i3, int i4) {
        ByteVector byteVector = new ByteVector();
        byteVector.data = this.value;
        byteVector.length = this.value.length;
        return byteVector;
    }

    final int getCount() {
        int i2 = 0;
        Attribute attribute = this;
        while (true) {
            Attribute attribute2 = attribute;
            if (attribute2 != null) {
                i2++;
                attribute = attribute2.next;
            } else {
                return i2;
            }
        }
    }

    final int getSize(ClassWriter classWriter, byte[] bArr, int i2, int i3, int i4) {
        int i5 = 0;
        for (Attribute attribute = this; attribute != null; attribute = attribute.next) {
            classWriter.newUTF8(attribute.type);
            i5 += attribute.write(classWriter, bArr, i2, i3, i4).length + 6;
        }
        return i5;
    }

    final void put(ClassWriter classWriter, byte[] bArr, int i2, int i3, int i4, ByteVector byteVector) {
        Attribute attribute = this;
        while (true) {
            Attribute attribute2 = attribute;
            if (attribute2 != null) {
                ByteVector byteVectorWrite = attribute2.write(classWriter, bArr, i2, i3, i4);
                byteVector.putShort(classWriter.newUTF8(attribute2.type)).putInt(byteVectorWrite.length);
                byteVector.putByteArray(byteVectorWrite.data, 0, byteVectorWrite.length);
                attribute = attribute2.next;
            } else {
                return;
            }
        }
    }
}
