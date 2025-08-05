package com.sun.xml.internal.bind.v2.bytecode;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.xml.internal.bind.Util;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/bytecode/ClassTailor.class */
public final class ClassTailor {
    private static final Logger logger;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ClassTailor.class.desiredAssertionStatus();
        logger = Util.getClassLogger();
    }

    private ClassTailor() {
    }

    public static String toVMClassName(Class c2) {
        if (!$assertionsDisabled && c2.isPrimitive()) {
            throw new AssertionError();
        }
        if (c2.isArray()) {
            return toVMTypeName(c2);
        }
        return c2.getName().replace('.', '/');
    }

    public static String toVMTypeName(Class c2) {
        if (c2.isArray()) {
            return '[' + toVMTypeName(c2.getComponentType());
        }
        if (c2.isPrimitive()) {
            if (c2 == Boolean.TYPE) {
                return Constants.HASIDCALL_INDEX_SIG;
            }
            if (c2 == Character.TYPE) {
                return "C";
            }
            if (c2 == Byte.TYPE) {
                return PdfOps.B_TOKEN;
            }
            if (c2 == Double.TYPE) {
                return PdfOps.D_TOKEN;
            }
            if (c2 == Float.TYPE) {
                return PdfOps.F_TOKEN;
            }
            if (c2 == Integer.TYPE) {
                return "I";
            }
            if (c2 == Long.TYPE) {
                return "J";
            }
            if (c2 == Short.TYPE) {
                return PdfOps.S_TOKEN;
            }
            throw new IllegalArgumentException(c2.getName());
        }
        return 'L' + c2.getName().replace('.', '/') + ';';
    }

    public static byte[] tailor(Class templateClass, String newClassName, String... replacements) {
        String vmname = toVMClassName(templateClass);
        return tailor(SecureLoader.getClassClassLoader(templateClass).getResourceAsStream(vmname + ".class"), vmname, newClassName, replacements);
    }

    public static byte[] tailor(InputStream image, String templateClassName, String newClassName, String... replacements) {
        DataInputStream in = new DataInputStream(image);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            DataOutputStream out = new DataOutputStream(baos);
            long l2 = in.readLong();
            out.writeLong(l2);
            int count = in.readShort();
            out.writeShort(count);
            int i2 = 0;
            while (i2 < count) {
                byte tag = in.readByte();
                out.writeByte(tag);
                switch (tag) {
                    case 0:
                        break;
                    case 1:
                        String value = in.readUTF();
                        if (value.equals(templateClassName)) {
                            value = newClassName;
                        } else {
                            int j2 = 0;
                            while (true) {
                                if (j2 < replacements.length) {
                                    if (!value.equals(replacements[j2])) {
                                        j2 += 2;
                                    } else {
                                        value = replacements[j2 + 1];
                                    }
                                }
                            }
                        }
                        out.writeUTF(value);
                        break;
                    case 2:
                    default:
                        throw new IllegalArgumentException("Unknown constant type " + ((int) tag));
                    case 3:
                    case 4:
                        out.writeInt(in.readInt());
                        break;
                    case 5:
                    case 6:
                        i2++;
                        out.writeLong(in.readLong());
                        break;
                    case 7:
                    case 8:
                        out.writeShort(in.readShort());
                        break;
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        out.writeInt(in.readInt());
                        break;
                }
                i2++;
            }
            byte[] buf = new byte[512];
            while (true) {
                int len = in.read(buf);
                if (len > 0) {
                    out.write(buf, 0, len);
                } else {
                    in.close();
                    out.close();
                    return baos.toByteArray();
                }
            }
        } catch (IOException e2) {
            logger.log(Level.WARNING, "failed to tailor", (Throwable) e2);
            return null;
        }
    }
}
