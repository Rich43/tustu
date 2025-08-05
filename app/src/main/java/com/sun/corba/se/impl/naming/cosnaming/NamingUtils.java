package com.sun.corba.se.impl.naming.cosnaming;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.omg.CosNaming.NameComponent;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/cosnaming/NamingUtils.class */
public class NamingUtils {
    public static boolean debug = false;
    public static PrintStream debugStream;
    public static PrintStream errStream;

    private NamingUtils() {
    }

    public static void dprint(String str) {
        if (debug && debugStream != null) {
            debugStream.println(str);
        }
    }

    public static void errprint(String str) {
        if (errStream != null) {
            errStream.println(str);
        } else {
            System.err.println(str);
        }
    }

    public static void printException(Exception exc) {
        if (errStream != null) {
            exc.printStackTrace(errStream);
        } else {
            exc.printStackTrace();
        }
    }

    public static void makeDebugStream(File file) throws IOException {
        debugStream = new PrintStream(new DataOutputStream(new FileOutputStream(file)));
        debugStream.println("Debug Stream Enabled.");
    }

    public static void makeErrStream(File file) throws IOException {
        if (debug) {
            errStream = new PrintStream(new DataOutputStream(new FileOutputStream(file)));
            dprint("Error stream setup completed.");
        }
    }

    static String getDirectoryStructuredName(NameComponent[] nameComponentArr) {
        StringBuffer stringBuffer = new StringBuffer("/");
        for (int i2 = 0; i2 < nameComponentArr.length; i2++) {
            stringBuffer.append(nameComponentArr[i2].id + "." + nameComponentArr[i2].kind);
        }
        return stringBuffer.toString();
    }
}
