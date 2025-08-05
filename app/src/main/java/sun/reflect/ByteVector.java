package sun.reflect;

/* loaded from: rt.jar:sun/reflect/ByteVector.class */
interface ByteVector {
    int getLength();

    byte get(int i2);

    void put(int i2, byte b2);

    void add(byte b2);

    void trim();

    byte[] getData();
}
