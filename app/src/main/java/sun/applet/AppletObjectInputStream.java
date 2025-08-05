package sun.applet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Array;

/* loaded from: rt.jar:sun/applet/AppletObjectInputStream.class */
class AppletObjectInputStream extends ObjectInputStream {
    private AppletClassLoader loader;

    public AppletObjectInputStream(InputStream inputStream, AppletClassLoader appletClassLoader) throws IOException {
        super(inputStream);
        if (appletClassLoader == null) {
            throw new AppletIllegalArgumentException("appletillegalargumentexception.objectinputstream");
        }
        this.loader = appletClassLoader;
    }

    private Class primitiveType(char c2) {
        switch (c2) {
            case 'B':
                return Byte.TYPE;
            case 'C':
                return Character.TYPE;
            case 'D':
                return Double.TYPE;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
                return null;
            case 'F':
                return Float.TYPE;
            case 'I':
                return Integer.TYPE;
            case 'J':
                return Long.TYPE;
            case 'S':
                return Short.TYPE;
            case 'Z':
                return Boolean.TYPE;
        }
    }

    @Override // java.io.ObjectInputStream
    protected Class resolveClass(ObjectStreamClass objectStreamClass) throws ClassNotFoundException, IOException {
        Class<?> clsPrimitiveType;
        String name = objectStreamClass.getName();
        if (name.startsWith("[")) {
            int i2 = 1;
            while (name.charAt(i2) == '[') {
                i2++;
            }
            if (name.charAt(i2) == 'L') {
                clsPrimitiveType = this.loader.loadClass(name.substring(i2 + 1, name.length() - 1));
            } else {
                if (name.length() != i2 + 1) {
                    throw new ClassNotFoundException(name);
                }
                clsPrimitiveType = primitiveType(name.charAt(i2));
            }
            int[] iArr = new int[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                iArr[i3] = 0;
            }
            return Array.newInstance(clsPrimitiveType, iArr).getClass();
        }
        return this.loader.loadClass(name);
    }
}
