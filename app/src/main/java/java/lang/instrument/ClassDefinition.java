package java.lang.instrument;

/* loaded from: rt.jar:java/lang/instrument/ClassDefinition.class */
public final class ClassDefinition {
    private final Class<?> mClass;
    private final byte[] mClassFile;

    public ClassDefinition(Class<?> cls, byte[] bArr) {
        if (cls == null || bArr == null) {
            throw new NullPointerException();
        }
        this.mClass = cls;
        this.mClassFile = bArr;
    }

    public Class<?> getDefinitionClass() {
        return this.mClass;
    }

    public byte[] getDefinitionClassFile() {
        return this.mClassFile;
    }
}
