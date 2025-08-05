package com.sun.corba.se.impl.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/* loaded from: rt.jar:com/sun/corba/se/impl/util/ORBProperties.class */
public class ORBProperties {
    public static final String ORB_CLASS = "org.omg.CORBA.ORBClass=com.sun.corba.se.impl.orb.ORBImpl";
    public static final String ORB_SINGLETON_CLASS = "org.omg.CORBA.ORBSingletonClass=com.sun.corba.se.impl.orb.ORBSingleton";

    public static void main(String[] strArr) {
        try {
            File file = new File(System.getProperty("java.home") + File.separator + "lib" + File.separator + "orb.properties");
            if (file.exists()) {
                return;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            try {
                printWriter.println(ORB_CLASS);
                printWriter.println(ORB_SINGLETON_CLASS);
                printWriter.close();
                fileOutputStream.close();
            } catch (Throwable th) {
                printWriter.close();
                fileOutputStream.close();
                throw th;
            }
        } catch (Exception e2) {
        }
    }
}
