package com.sun.scenario.effect.impl;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.security.AccessController;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/BufferUtil.class */
public class BufferUtil {
    public static final int SIZEOF_BYTE = 1;
    public static final int SIZEOF_SHORT = 2;
    public static final int SIZEOF_INT = 4;
    public static final int SIZEOF_FLOAT = 4;
    public static final int SIZEOF_DOUBLE = 8;
    private static boolean isCDCFP;
    private static Class byteOrderClass;
    private static Object nativeOrderObject;
    private static Method orderMethod;

    private BufferUtil() {
    }

    public static void nativeOrder(ByteBuffer buf) {
        if (!isCDCFP) {
            try {
                if (byteOrderClass == null) {
                    byteOrderClass = (Class) AccessController.doPrivileged(() -> {
                        return Class.forName("java.nio.ByteOrder", true, null);
                    });
                    orderMethod = ByteBuffer.class.getMethod(Constants.ATTRNAME_ORDER, byteOrderClass);
                    Method nativeOrderMethod = byteOrderClass.getMethod("nativeOrder", (Class[]) null);
                    nativeOrderObject = nativeOrderMethod.invoke(null, (Object[]) null);
                }
            } catch (Throwable th) {
                isCDCFP = true;
            }
            if (!isCDCFP) {
                try {
                    orderMethod.invoke(buf, nativeOrderObject);
                } catch (Throwable th2) {
                }
            }
        }
    }

    public static ByteBuffer newByteBuffer(int numElements) {
        ByteBuffer bb2 = ByteBuffer.allocateDirect(numElements);
        nativeOrder(bb2);
        return bb2;
    }

    public static DoubleBuffer newDoubleBuffer(int numElements) {
        ByteBuffer bb2 = newByteBuffer(numElements * 8);
        return bb2.asDoubleBuffer();
    }

    public static FloatBuffer newFloatBuffer(int numElements) {
        ByteBuffer bb2 = newByteBuffer(numElements * 4);
        return bb2.asFloatBuffer();
    }

    public static IntBuffer newIntBuffer(int numElements) {
        ByteBuffer bb2 = newByteBuffer(numElements * 4);
        return bb2.asIntBuffer();
    }

    public static ShortBuffer newShortBuffer(int numElements) {
        ByteBuffer bb2 = newByteBuffer(numElements * 2);
        return bb2.asShortBuffer();
    }
}
