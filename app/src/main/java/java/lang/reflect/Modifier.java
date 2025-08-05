package java.lang.reflect;

import java.security.AccessController;
import sun.reflect.ReflectionFactory;

/* loaded from: rt.jar:java/lang/reflect/Modifier.class */
public class Modifier {
    public static final int PUBLIC = 1;
    public static final int PRIVATE = 2;
    public static final int PROTECTED = 4;
    public static final int STATIC = 8;
    public static final int FINAL = 16;
    public static final int SYNCHRONIZED = 32;
    public static final int VOLATILE = 64;
    public static final int TRANSIENT = 128;
    public static final int NATIVE = 256;
    public static final int INTERFACE = 512;
    public static final int ABSTRACT = 1024;
    public static final int STRICT = 2048;
    static final int BRIDGE = 64;
    static final int VARARGS = 128;
    static final int SYNTHETIC = 4096;
    static final int ANNOTATION = 8192;
    static final int ENUM = 16384;
    static final int MANDATED = 32768;
    private static final int CLASS_MODIFIERS = 3103;
    private static final int INTERFACE_MODIFIERS = 3087;
    private static final int CONSTRUCTOR_MODIFIERS = 7;
    private static final int METHOD_MODIFIERS = 3391;
    private static final int FIELD_MODIFIERS = 223;
    private static final int PARAMETER_MODIFIERS = 16;
    static final int ACCESS_MODIFIERS = 7;

    static {
        ((ReflectionFactory) AccessController.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction())).setLangReflectAccess(new ReflectAccess());
    }

    public static boolean isPublic(int i2) {
        return (i2 & 1) != 0;
    }

    public static boolean isPrivate(int i2) {
        return (i2 & 2) != 0;
    }

    public static boolean isProtected(int i2) {
        return (i2 & 4) != 0;
    }

    public static boolean isStatic(int i2) {
        return (i2 & 8) != 0;
    }

    public static boolean isFinal(int i2) {
        return (i2 & 16) != 0;
    }

    public static boolean isSynchronized(int i2) {
        return (i2 & 32) != 0;
    }

    public static boolean isVolatile(int i2) {
        return (i2 & 64) != 0;
    }

    public static boolean isTransient(int i2) {
        return (i2 & 128) != 0;
    }

    public static boolean isNative(int i2) {
        return (i2 & 256) != 0;
    }

    public static boolean isInterface(int i2) {
        return (i2 & 512) != 0;
    }

    public static boolean isAbstract(int i2) {
        return (i2 & 1024) != 0;
    }

    public static boolean isStrict(int i2) {
        return (i2 & 2048) != 0;
    }

    public static String toString(int i2) {
        StringBuilder sb = new StringBuilder();
        if ((i2 & 1) != 0) {
            sb.append("public ");
        }
        if ((i2 & 4) != 0) {
            sb.append("protected ");
        }
        if ((i2 & 2) != 0) {
            sb.append("private ");
        }
        if ((i2 & 1024) != 0) {
            sb.append("abstract ");
        }
        if ((i2 & 8) != 0) {
            sb.append("static ");
        }
        if ((i2 & 16) != 0) {
            sb.append("final ");
        }
        if ((i2 & 128) != 0) {
            sb.append("transient ");
        }
        if ((i2 & 64) != 0) {
            sb.append("volatile ");
        }
        if ((i2 & 32) != 0) {
            sb.append("synchronized ");
        }
        if ((i2 & 256) != 0) {
            sb.append("native ");
        }
        if ((i2 & 2048) != 0) {
            sb.append("strictfp ");
        }
        if ((i2 & 512) != 0) {
            sb.append("interface ");
        }
        int length = sb.length();
        if (length > 0) {
            return sb.toString().substring(0, length - 1);
        }
        return "";
    }

    static boolean isSynthetic(int i2) {
        return (i2 & 4096) != 0;
    }

    static boolean isMandated(int i2) {
        return (i2 & 32768) != 0;
    }

    public static int classModifiers() {
        return CLASS_MODIFIERS;
    }

    public static int interfaceModifiers() {
        return INTERFACE_MODIFIERS;
    }

    public static int constructorModifiers() {
        return 7;
    }

    public static int methodModifiers() {
        return 3391;
    }

    public static int fieldModifiers() {
        return 223;
    }

    public static int parameterModifiers() {
        return 16;
    }
}
