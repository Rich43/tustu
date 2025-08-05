package jdk.nashorn.internal.runtime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.runtime.options.Options;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/AstSerializer.class */
final class AstSerializer {
    private static final int COMPRESSION_LEVEL = Options.getIntProperty("nashorn.serialize.compression", 4);

    AstSerializer() {
    }

    static byte[] serialize(FunctionNode fn) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Deflater deflater = new Deflater(COMPRESSION_LEVEL);
        try {
            try {
                ObjectOutputStream oout = new ObjectOutputStream(new DeflaterOutputStream(out, deflater));
                Throwable th = null;
                try {
                    try {
                        oout.writeObject(fn);
                        if (oout != null) {
                            if (0 != 0) {
                                try {
                                    oout.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                oout.close();
                            }
                        }
                        return out.toByteArray();
                    } catch (Throwable th3) {
                        if (oout != null) {
                            if (th != null) {
                                try {
                                    oout.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                oout.close();
                            }
                        }
                        throw th3;
                    }
                } finally {
                }
            } catch (IOException e2) {
                throw new AssertionError("Unexpected exception serializing function", e2);
            }
        } finally {
            deflater.end();
        }
    }
}
