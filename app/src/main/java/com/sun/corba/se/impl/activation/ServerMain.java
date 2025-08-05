package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.ActivatorHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Properties;
import org.omg.CORBA.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/activation/ServerMain.class */
public class ServerMain {
    public static final int OK = 0;
    public static final int MAIN_CLASS_NOT_FOUND = 1;
    public static final int NO_MAIN_METHOD = 2;
    public static final int APPLICATION_ERROR = 3;
    public static final int UNKNOWN_ERROR = 4;
    public static final int NO_SERVER_ID = 5;
    public static final int REGISTRATION_FAILED = 6;
    private static final boolean debug = false;

    public static String printResult(int i2) {
        switch (i2) {
            case 0:
                return "Server terminated normally";
            case 1:
                return "main class not found";
            case 2:
                return "no main method";
            case 3:
                return "application error";
            case 4:
            default:
                return "unknown error";
            case 5:
                return "server ID not defined";
            case 6:
                return "server registration failed";
        }
    }

    private void redirectIOStreams() {
        try {
            String str = System.getProperty(ORBConstants.DB_DIR_PROPERTY) + System.getProperty("file.separator") + ORBConstants.SERVER_LOG_DIR + System.getProperty("file.separator");
            new File(str);
            String property = System.getProperty(ORBConstants.SERVER_ID_PROPERTY);
            FileOutputStream fileOutputStream = new FileOutputStream(str + property + ".out", true);
            FileOutputStream fileOutputStream2 = new FileOutputStream(str + property + ".err", true);
            PrintStream printStream = new PrintStream((OutputStream) fileOutputStream, true);
            PrintStream printStream2 = new PrintStream((OutputStream) fileOutputStream2, true);
            System.setOut(printStream);
            System.setErr(printStream2);
            logInformation("Server started");
        } catch (Exception e2) {
        }
    }

    private static void writeLogMessage(PrintStream printStream, String str) {
        printStream.print("[" + new Date().toString() + "] " + str + "\n");
    }

    public static void logInformation(String str) {
        writeLogMessage(System.out, "        " + str);
    }

    public static void logError(String str) {
        writeLogMessage(System.out, "ERROR:  " + str);
        writeLogMessage(System.err, "ERROR:  " + str);
    }

    public static void logTerminal(String str, int i2) {
        if (i2 == 0) {
            writeLogMessage(System.out, "        " + str);
        } else {
            writeLogMessage(System.out, "FATAL:  " + printResult(i2) + ": " + str);
            writeLogMessage(System.err, "FATAL:  " + printResult(i2) + ": " + str);
        }
        System.exit(i2);
    }

    private Method getMainMethod(Class cls) {
        Method declaredMethod = null;
        try {
            declaredMethod = cls.getDeclaredMethod("main", String[].class);
        } catch (Exception e2) {
            logTerminal(e2.getMessage(), 2);
        }
        if (!isPublicStaticVoid(declaredMethod)) {
            logTerminal("", 2);
        }
        return declaredMethod;
    }

    private boolean isPublicStaticVoid(Method method) {
        int modifiers = method.getModifiers();
        if (!Modifier.isPublic(modifiers) || !Modifier.isStatic(modifiers)) {
            logError(method.getName() + " is not public static");
            return false;
        }
        if (method.getExceptionTypes().length != 0) {
            logError(method.getName() + " declares exceptions");
            return false;
        }
        if (!method.getReturnType().equals(Void.TYPE)) {
            logError(method.getName() + " does not have a void return type");
            return false;
        }
        return true;
    }

    private Method getNamedMethod(Class cls, String str) {
        try {
            Method declaredMethod = cls.getDeclaredMethod(str, ORB.class);
            if (!isPublicStaticVoid(declaredMethod)) {
                return null;
            }
            return declaredMethod;
        } catch (Exception e2) {
            return null;
        }
    }

    private void run(String[] strArr) {
        Class<?> cls;
        try {
            redirectIOStreams();
            String property = System.getProperty(ORBConstants.SERVER_NAME_PROPERTY);
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader == null) {
                contextClassLoader = ClassLoader.getSystemClassLoader();
            }
            try {
                cls = Class.forName(property);
            } catch (ClassNotFoundException e2) {
                cls = Class.forName(property, true, contextClassLoader);
            }
            Method mainMethod = getMainMethod(cls);
            if (Boolean.getBoolean(ORBConstants.SERVER_DEF_VERIFY_PROPERTY)) {
                if (mainMethod == null) {
                    logTerminal("", 2);
                } else {
                    logTerminal("", 0);
                }
            }
            registerCallback(cls);
            mainMethod.invoke(null, strArr);
        } catch (ClassNotFoundException e3) {
            logTerminal("ClassNotFound exception: " + e3.getMessage(), 1);
        } catch (Exception e4) {
            logTerminal("Exception: " + e4.getMessage(), 3);
        }
    }

    public static void main(String[] strArr) {
        new ServerMain().run(strArr);
    }

    private int getServerId() {
        Integer integer = Integer.getInteger(ORBConstants.SERVER_ID_PROPERTY);
        if (integer == null) {
            logTerminal("", 5);
        }
        return integer.intValue();
    }

    private void registerCallback(Class cls) {
        Method namedMethod = getNamedMethod(cls, "install");
        Method namedMethod2 = getNamedMethod(cls, "uninstall");
        Method namedMethod3 = getNamedMethod(cls, "shutdown");
        Properties properties = new Properties();
        properties.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
        properties.put(ORBConstants.ACTIVATED_PROPERTY, "false");
        ORB orbInit = ORB.init((String[]) null, properties);
        try {
            ActivatorHelper.narrow(orbInit.resolve_initial_references(ORBConstants.SERVER_ACTIVATOR_NAME)).active(getServerId(), new ServerCallback(orbInit, namedMethod, namedMethod2, namedMethod3));
        } catch (Exception e2) {
            logTerminal("exception " + e2.getMessage(), 6);
        }
    }
}
