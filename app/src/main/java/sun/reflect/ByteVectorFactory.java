package sun.reflect;

/* loaded from: rt.jar:sun/reflect/ByteVectorFactory.class */
class ByteVectorFactory {
    ByteVectorFactory() {
    }

    static ByteVector create() {
        return new ByteVectorImpl();
    }

    static ByteVector create(int i2) {
        return new ByteVectorImpl(i2);
    }
}
