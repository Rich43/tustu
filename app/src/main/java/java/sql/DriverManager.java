package java.sql;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

/* loaded from: rt.jar:java/sql/DriverManager.class */
public class DriverManager {
    private static final CopyOnWriteArrayList<DriverInfo> registeredDrivers = new CopyOnWriteArrayList<>();
    private static volatile int loginTimeout = 0;
    private static volatile PrintWriter logWriter = null;
    private static volatile PrintStream logStream = null;
    private static final Object logSync = new Object();
    static final SQLPermission SET_LOG_PERMISSION;
    static final SQLPermission DEREGISTER_DRIVER_PERMISSION;

    static {
        loadInitialDrivers();
        println("JDBC DriverManager initialized");
        SET_LOG_PERMISSION = new SQLPermission("setLog");
        DEREGISTER_DRIVER_PERMISSION = new SQLPermission("deregisterDriver");
    }

    private DriverManager() {
    }

    public static PrintWriter getLogWriter() {
        return logWriter;
    }

    public static void setLogWriter(PrintWriter printWriter) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SET_LOG_PERMISSION);
        }
        logStream = null;
        logWriter = printWriter;
    }

    @CallerSensitive
    public static Connection getConnection(String str, Properties properties) throws SQLException {
        return getConnection(str, properties, Reflection.getCallerClass());
    }

    @CallerSensitive
    public static Connection getConnection(String str, String str2, String str3) throws SQLException {
        Properties properties = new Properties();
        if (str2 != null) {
            properties.put("user", str2);
        }
        if (str3 != null) {
            properties.put("password", str3);
        }
        return getConnection(str, properties, Reflection.getCallerClass());
    }

    @CallerSensitive
    public static Connection getConnection(String str) throws SQLException {
        return getConnection(str, new Properties(), Reflection.getCallerClass());
    }

    @CallerSensitive
    public static Driver getDriver(String str) throws SQLException {
        println("DriverManager.getDriver(\"" + str + "\")");
        Class<?> callerClass = Reflection.getCallerClass();
        Iterator<DriverInfo> it = registeredDrivers.iterator();
        while (it.hasNext()) {
            DriverInfo next = it.next();
            if (isDriverAllowed(next.driver, callerClass)) {
                try {
                    if (next.driver.acceptsURL(str)) {
                        println("getDriver returning " + next.driver.getClass().getName());
                        return next.driver;
                    }
                    continue;
                } catch (SQLException e2) {
                }
            } else {
                println("    skipping: " + next.driver.getClass().getName());
            }
        }
        println("getDriver: no suitable driver");
        throw new SQLException("No suitable driver", "08001");
    }

    public static synchronized void registerDriver(Driver driver) throws SQLException {
        registerDriver(driver, null);
    }

    public static synchronized void registerDriver(Driver driver, DriverAction driverAction) throws SQLException {
        if (driver != null) {
            registeredDrivers.addIfAbsent(new DriverInfo(driver, driverAction));
            println("registerDriver: " + ((Object) driver));
            return;
        }
        throw new NullPointerException();
    }

    @CallerSensitive
    public static synchronized void deregisterDriver(Driver driver) throws SQLException {
        if (driver == null) {
            return;
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(DEREGISTER_DRIVER_PERMISSION);
        }
        println("DriverManager.deregisterDriver: " + ((Object) driver));
        DriverInfo driverInfo = new DriverInfo(driver, null);
        if (registeredDrivers.contains(driverInfo)) {
            if (isDriverAllowed(driver, Reflection.getCallerClass())) {
                DriverInfo driverInfo2 = registeredDrivers.get(registeredDrivers.indexOf(driverInfo));
                if (driverInfo2.action() != null) {
                    driverInfo2.action().deregister();
                }
                registeredDrivers.remove(driverInfo);
                return;
            }
            throw new SecurityException();
        }
        println("    couldn't find driver to unload");
    }

    @CallerSensitive
    public static Enumeration<Driver> getDrivers() {
        Vector vector = new Vector();
        Class<?> callerClass = Reflection.getCallerClass();
        Iterator<DriverInfo> it = registeredDrivers.iterator();
        while (it.hasNext()) {
            DriverInfo next = it.next();
            if (isDriverAllowed(next.driver, callerClass)) {
                vector.addElement(next.driver);
            } else {
                println("    skipping: " + next.getClass().getName());
            }
        }
        return vector.elements();
    }

    public static void setLoginTimeout(int i2) {
        loginTimeout = i2;
    }

    public static int getLoginTimeout() {
        return loginTimeout;
    }

    @Deprecated
    public static void setLogStream(PrintStream printStream) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SET_LOG_PERMISSION);
        }
        logStream = printStream;
        if (printStream != null) {
            logWriter = new PrintWriter(printStream);
        } else {
            logWriter = null;
        }
    }

    @Deprecated
    public static PrintStream getLogStream() {
        return logStream;
    }

    public static void println(String str) {
        synchronized (logSync) {
            if (logWriter != null) {
                logWriter.println(str);
                logWriter.flush();
            }
        }
    }

    private static boolean isDriverAllowed(Driver driver, Class<?> cls) {
        return isDriverAllowed(driver, cls != null ? cls.getClassLoader() : null);
    }

    private static boolean isDriverAllowed(Driver driver, ClassLoader classLoader) {
        boolean z2 = false;
        if (driver != null) {
            Class<?> cls = null;
            try {
                cls = Class.forName(driver.getClass().getName(), true, classLoader);
            } catch (Exception e2) {
            }
            z2 = cls == driver.getClass();
        }
        return z2;
    }

    private static void loadInitialDrivers() {
        String str;
        try {
            str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.sql.DriverManager.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public String run2() {
                    return System.getProperty("jdbc.drivers");
                }
            });
        } catch (Exception e2) {
            str = null;
        }
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.sql.DriverManager.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                Iterator it = ServiceLoader.load(Driver.class).iterator();
                while (it.hasNext()) {
                    try {
                        it.next();
                    } catch (Throwable th) {
                        return null;
                    }
                }
                return null;
            }
        });
        println("DriverManager.initialize: jdbc.drivers = " + str);
        if (str == null || str.equals("")) {
            return;
        }
        String[] strArrSplit = str.split(CallSiteDescriptor.TOKEN_DELIMITER);
        println("number of Drivers:" + strArrSplit.length);
        for (String str2 : strArrSplit) {
            try {
                println("DriverManager.Initialize: loading " + str2);
                Class.forName(str2, true, ClassLoader.getSystemClassLoader());
            } catch (Exception e3) {
                println("DriverManager.Initialize: load failed: " + ((Object) e3));
            }
        }
    }

    private static Connection getConnection(String str, Properties properties, Class<?> cls) throws SQLException {
        ClassLoader classLoader = cls != null ? cls.getClassLoader() : null;
        synchronized (DriverManager.class) {
            if (classLoader == null) {
                classLoader = Thread.currentThread().getContextClassLoader();
            }
        }
        if (str == null) {
            throw new SQLException("The url cannot be null", "08001");
        }
        println("DriverManager.getConnection(\"" + str + "\")");
        SQLException sQLException = null;
        Iterator<DriverInfo> it = registeredDrivers.iterator();
        while (it.hasNext()) {
            DriverInfo next = it.next();
            if (isDriverAllowed(next.driver, classLoader)) {
                try {
                    println("    trying " + next.driver.getClass().getName());
                    Connection connectionConnect = next.driver.connect(str, properties);
                    if (connectionConnect != null) {
                        println("getConnection returning " + next.driver.getClass().getName());
                        return connectionConnect;
                    }
                    continue;
                } catch (SQLException e2) {
                    if (sQLException == null) {
                        sQLException = e2;
                    }
                }
            } else {
                println("    skipping: " + next.getClass().getName());
            }
        }
        if (sQLException != null) {
            println("getConnection failed: " + ((Object) sQLException));
            throw sQLException;
        }
        println("getConnection: no suitable driver found for " + str);
        throw new SQLException("No suitable driver found for " + str, "08001");
    }
}
