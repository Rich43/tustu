package com.sun.xml.internal.ws.org.objectweb.asm;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/Attribute.class */
public class Attribute {
    public final String type;
    byte[] value;
    Attribute next;

    protected Attribute(String type) {
        this.type = type;
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

    protected Attribute read(ClassReader cr, int off, int len, char[] buf, int codeOff, Label[] labels) {
        Attribute attr = new Attribute(this.type);
        attr.value = new byte[len];
        System.arraycopy(cr.f12090b, off, attr.value, 0, len);
        return attr;
    }

    protected ByteVector write(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals) {
        ByteVector v2 = new ByteVector();
        v2.data = this.value;
        v2.length = this.value.length;
        return v2;
    }

    final int getCount() {
        int count = 0;
        Attribute attribute = this;
        while (true) {
            Attribute attr = attribute;
            if (attr != null) {
                count++;
                attribute = attr.next;
            } else {
                return count;
            }
        }
    }

    final int getSize(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals) {
        int size = 0;
        for (Attribute attr = this; attr != null; attr = attr.next) {
            cw.newUTF8(attr.type);
            size += attr.write(cw, code, len, maxStack, maxLocals).length + 6;
        }
        return size;
    }

    final void put(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals, ByteVector out) {
        Attribute attribute = this;
        while (true) {
            Attribute attr = attribute;
            if (attr != null) {
                ByteVector b2 = attr.write(cw, code, len, maxStack, maxLocals);
                out.putShort(cw.newUTF8(attr.type)).putInt(b2.length);
                out.putByteArray(b2.data, 0, b2.length);
                attribute = attr.next;
            } else {
                return;
            }
        }
    }
}
