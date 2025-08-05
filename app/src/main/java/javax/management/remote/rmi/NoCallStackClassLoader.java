package javax.management.remote.rmi;

import java.security.ProtectionDomain;

/* loaded from: rt.jar:javax/management/remote/rmi/NoCallStackClassLoader.class */
class NoCallStackClassLoader extends ClassLoader {
    private final String[] classNames;
    private final byte[][] byteCodes;
    private final String[] referencedClassNames;
    private final ClassLoader referencedClassLoader;
    private final ProtectionDomain protectionDomain;

    /* JADX WARN: Type inference failed for: r2v2, types: [byte[], byte[][]] */
    public NoCallStackClassLoader(String str, byte[] bArr, String[] strArr, ClassLoader classLoader, ProtectionDomain protectionDomain) {
        this(new String[]{str}, (byte[][]) new byte[]{bArr}, strArr, classLoader, protectionDomain);
    }

    public NoCallStackClassLoader(String[] strArr, byte[][] bArr, String[] strArr2, ClassLoader classLoader, ProtectionDomain protectionDomain) {
        super(null);
        if (strArr == null || strArr.length == 0 || bArr == null || strArr.length != bArr.length || strArr2 == null || protectionDomain == null) {
            throw new IllegalArgumentException();
        }
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2] == null || bArr[i2] == null) {
                throw new IllegalArgumentException();
            }
        }
        for (String str : strArr2) {
            if (str == null) {
                throw new IllegalArgumentException();
            }
        }
        this.classNames = strArr;
        this.byteCodes = bArr;
        this.referencedClassNames = strArr2;
        this.referencedClassLoader = classLoader;
        this.protectionDomain = protectionDomain;
    }

    @Override // java.lang.ClassLoader
    protected Class<?> findClass(String str) throws ClassNotFoundException {
        for (int i2 = 0; i2 < this.classNames.length; i2++) {
            if (str.equals(this.classNames[i2])) {
                return defineClass(this.classNames[i2], this.byteCodes[i2], 0, this.byteCodes[i2].length, this.protectionDomain);
            }
        }
        if (this.referencedClassLoader != null) {
            for (int i3 = 0; i3 < this.referencedClassNames.length; i3++) {
                if (str.equals(this.referencedClassNames[i3])) {
                    return this.referencedClassLoader.loadClass(str);
                }
            }
        }
        throw new ClassNotFoundException(str);
    }

    public static byte[] stringToBytes(String str) {
        int length = str.length();
        byte[] bArr = new byte[length];
        for (int i2 = 0; i2 < length; i2++) {
            bArr[i2] = (byte) str.charAt(i2);
        }
        return bArr;
    }
}
