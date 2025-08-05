package java.lang.instrument;

import java.security.ProtectionDomain;

/* loaded from: rt.jar:java/lang/instrument/ClassFileTransformer.class */
public interface ClassFileTransformer {
    byte[] transform(ClassLoader classLoader, String str, Class<?> cls, ProtectionDomain protectionDomain, byte[] bArr) throws IllegalClassFormatException;
}
