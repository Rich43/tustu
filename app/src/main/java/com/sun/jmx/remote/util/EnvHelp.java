package com.sun.jmx.remote.util;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.remote.security.NotificationAccessController;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/jmx/remote/util/EnvHelp.class */
public class EnvHelp {
    public static final String CREDENTIAL_TYPES = "jmx.remote.rmi.server.credential.types";
    private static final String DEFAULT_CLASS_LOADER = "jmx.remote.default.class.loader";
    private static final String DEFAULT_CLASS_LOADER_NAME = "jmx.remote.default.class.loader.name";
    public static final String BUFFER_SIZE_PROPERTY = "jmx.remote.x.notification.buffer.size";
    public static final String MAX_FETCH_NOTIFS = "jmx.remote.x.notification.fetch.max";
    public static final String FETCH_TIMEOUT = "jmx.remote.x.notification.fetch.timeout";
    public static final String NOTIF_ACCESS_CONTROLLER = "com.sun.jmx.remote.notification.access.controller";
    public static final String DEFAULT_ORB = "java.naming.corba.orb";
    public static final String HIDDEN_ATTRIBUTES = "jmx.remote.x.hidden.attributes";
    public static final String DEFAULT_HIDDEN_ATTRIBUTES = "java.naming.security.* jmx.remote.authenticator jmx.remote.context jmx.remote.default.class.loader jmx.remote.message.connection.server jmx.remote.object.wrapping jmx.remote.rmi.client.socket.factory jmx.remote.rmi.server.socket.factory jmx.remote.sasl.callback.handler jmx.remote.tls.socket.factory jmx.remote.x.access.file jmx.remote.x.password.file ";
    public static final String SERVER_CONNECTION_TIMEOUT = "jmx.remote.x.server.connection.timeout";
    public static final String CLIENT_CONNECTION_CHECK_PERIOD = "jmx.remote.x.client.connection.check.period";
    public static final String JMX_SERVER_DAEMON = "jmx.remote.x.daemon";
    private static final SortedSet<String> defaultHiddenStrings = new TreeSet();
    private static final SortedSet<String> defaultHiddenPrefixes = new TreeSet();
    private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "EnvHelp");

    public static ClassLoader resolveServerClassLoader(Map<String, ?> map, MBeanServer mBeanServer) throws InstanceNotFoundException {
        if (map == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        Object obj = map.get("jmx.remote.default.class.loader");
        Object obj2 = map.get("jmx.remote.default.class.loader.name");
        if (obj != null && obj2 != null) {
            throw new IllegalArgumentException("Only one of jmx.remote.default.class.loader or jmx.remote.default.class.loader.name should be specified.");
        }
        if (obj == null && obj2 == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        if (obj != null) {
            if (obj instanceof ClassLoader) {
                return (ClassLoader) obj;
            }
            throw new IllegalArgumentException("ClassLoader object is not an instance of " + ClassLoader.class.getName() + " : " + obj.getClass().getName());
        }
        if (obj2 instanceof ObjectName) {
            ObjectName objectName = (ObjectName) obj2;
            if (mBeanServer == null) {
                throw new IllegalArgumentException("Null MBeanServer object");
            }
            return mBeanServer.getClassLoader(objectName);
        }
        throw new IllegalArgumentException("ClassLoader name is not an instance of " + ObjectName.class.getName() + " : " + obj2.getClass().getName());
    }

    public static ClassLoader resolveClientClassLoader(Map<String, ?> map) {
        if (map == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        Object obj = map.get("jmx.remote.default.class.loader");
        if (obj == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        if (obj instanceof ClassLoader) {
            return (ClassLoader) obj;
        }
        throw new IllegalArgumentException("ClassLoader object is not an instance of " + ClassLoader.class.getName() + " : " + obj.getClass().getName());
    }

    public static <T extends Throwable> T initCause(T t2, Throwable th) {
        t2.initCause(th);
        return t2;
    }

    public static Throwable getCause(Throwable th) {
        Throwable th2 = th;
        try {
            th2 = (Throwable) th.getClass().getMethod("getCause", (Class[]) null).invoke(th, (Object[]) null);
        } catch (Exception e2) {
        }
        return th2 != null ? th2 : th;
    }

    public static int getNotifBufferSize(Map<String, ?> map) {
        int i2 = 1000;
        try {
            String str = (String) AccessController.doPrivileged(new GetPropertyAction(BUFFER_SIZE_PROPERTY));
            if (str != null) {
                i2 = Integer.parseInt(str);
            } else {
                String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("jmx.remote.x.buffer.size"));
                if (str2 != null) {
                    i2 = Integer.parseInt(str2);
                }
            }
        } catch (RuntimeException e2) {
            logger.warning("getNotifBufferSize", "Can't use System property jmx.remote.x.notification.buffer.size: " + ((Object) e2));
            logger.debug("getNotifBufferSize", e2);
        }
        int integerAttribute = i2;
        try {
            if (map.containsKey(BUFFER_SIZE_PROPERTY)) {
                integerAttribute = (int) getIntegerAttribute(map, BUFFER_SIZE_PROPERTY, i2, 0L, 2147483647L);
            } else {
                integerAttribute = (int) getIntegerAttribute(map, "jmx.remote.x.buffer.size", i2, 0L, 2147483647L);
            }
        } catch (RuntimeException e3) {
            logger.warning("getNotifBufferSize", "Can't determine queuesize (using default): " + ((Object) e3));
            logger.debug("getNotifBufferSize", e3);
        }
        return integerAttribute;
    }

    public static int getMaxFetchNotifNumber(Map<String, ?> map) {
        return (int) getIntegerAttribute(map, MAX_FETCH_NOTIFS, 1000L, 1L, 2147483647L);
    }

    public static long getFetchTimeout(Map<String, ?> map) {
        return getIntegerAttribute(map, FETCH_TIMEOUT, 60000L, 0L, Long.MAX_VALUE);
    }

    public static NotificationAccessController getNotificationAccessController(Map<String, ?> map) {
        if (map == null) {
            return null;
        }
        return (NotificationAccessController) map.get(NOTIF_ACCESS_CONTROLLER);
    }

    public static long getIntegerAttribute(Map<String, ?> map, String str, long j2, long j3, long j4) throws NumberFormatException {
        Object obj;
        long jLongValue;
        if (map == null || (obj = map.get(str)) == null) {
            return j2;
        }
        if (obj instanceof Number) {
            jLongValue = ((Number) obj).longValue();
        } else if (obj instanceof String) {
            jLongValue = Long.parseLong((String) obj);
        } else {
            throw new IllegalArgumentException("Attribute " + str + " value must be Integer or String: " + obj);
        }
        if (jLongValue < j3) {
            throw new IllegalArgumentException("Attribute " + str + " value must be at least " + j3 + ": " + jLongValue);
        }
        if (jLongValue > j4) {
            throw new IllegalArgumentException("Attribute " + str + " value must be at most " + j4 + ": " + jLongValue);
        }
        return jLongValue;
    }

    public static void checkAttributes(Map<?, ?> map) {
        for (Object obj : map.keySet()) {
            if (!(obj instanceof String)) {
                throw new IllegalArgumentException("Attributes contain key that is not a string: " + obj);
            }
        }
    }

    public static <V> Map<String, V> filterAttributes(Map<String, V> map) {
        if (logger.traceOn()) {
            logger.trace("filterAttributes", "starts");
        }
        TreeMap treeMap = new TreeMap(map);
        purgeUnserializable(treeMap.values());
        hideAttributes(treeMap);
        return treeMap;
    }

    private static void purgeUnserializable(Collection<?> collection) {
        logger.trace("purgeUnserializable", "starts");
        ObjectOutputStream objectOutputStream = null;
        int i2 = 0;
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next == null || (next instanceof String)) {
                if (logger.traceOn()) {
                    logger.trace("purgeUnserializable", "Value trivially serializable: " + next);
                }
            } else {
                if (objectOutputStream == null) {
                    try {
                        objectOutputStream = new ObjectOutputStream(new SinkOutputStream());
                    } catch (IOException e2) {
                        if (logger.traceOn()) {
                            logger.trace("purgeUnserializable", "Value not serializable: " + next + ": " + ((Object) e2));
                        }
                        it.remove();
                        objectOutputStream = null;
                    }
                }
                objectOutputStream.writeObject(next);
                if (logger.traceOn()) {
                    logger.trace("purgeUnserializable", "Value serializable: " + next);
                }
            }
            i2++;
        }
    }

    private static void hideAttributes(SortedMap<String, ?> sortedMap) {
        SortedSet<String> treeSet;
        SortedSet<String> treeSet2;
        String next;
        String next2;
        int iCompareTo;
        String strSubstring;
        if (sortedMap.isEmpty()) {
            return;
        }
        String str = (String) sortedMap.get(HIDDEN_ATTRIBUTES);
        if (str != null) {
            if (str.startsWith("=")) {
                strSubstring = str.substring(1);
            } else {
                strSubstring = str + " java.naming.security.* jmx.remote.authenticator jmx.remote.context jmx.remote.default.class.loader jmx.remote.message.connection.server jmx.remote.object.wrapping jmx.remote.rmi.client.socket.factory jmx.remote.rmi.server.socket.factory jmx.remote.sasl.callback.handler jmx.remote.tls.socket.factory jmx.remote.x.access.file jmx.remote.x.password.file ";
            }
            treeSet = new TreeSet();
            treeSet2 = new TreeSet();
            parseHiddenAttributes(strSubstring, treeSet, treeSet2);
        } else {
            synchronized (defaultHiddenStrings) {
                if (defaultHiddenStrings.isEmpty()) {
                    parseHiddenAttributes(DEFAULT_HIDDEN_ATTRIBUTES, defaultHiddenStrings, defaultHiddenPrefixes);
                }
                treeSet = defaultHiddenStrings;
                treeSet2 = defaultHiddenPrefixes;
            }
        }
        String str2 = sortedMap.lastKey() + "X";
        Iterator<String> it = sortedMap.keySet().iterator();
        Iterator<String> it2 = treeSet.iterator();
        Iterator<String> it3 = treeSet2.iterator();
        if (it2.hasNext()) {
            next = it2.next();
        } else {
            next = str2;
        }
        if (it3.hasNext()) {
            next2 = it3.next();
        } else {
            next2 = str2;
        }
        while (it.hasNext()) {
            String next3 = it.next();
            while (true) {
                iCompareTo = next.compareTo(next3);
                if (iCompareTo >= 0) {
                    break;
                } else if (it2.hasNext()) {
                    next = it2.next();
                } else {
                    next = str2;
                }
            }
            if (iCompareTo == 0) {
                it.remove();
            } else {
                while (true) {
                    if (next2.compareTo(next3) > 0) {
                        break;
                    }
                    if (next3.startsWith(next2)) {
                        it.remove();
                        break;
                    } else if (it3.hasNext()) {
                        next2 = it3.next();
                    } else {
                        next2 = str2;
                    }
                }
            }
        }
    }

    private static void parseHiddenAttributes(String str, SortedSet<String> sortedSet, SortedSet<String> sortedSet2) {
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (strNextToken.endsWith("*")) {
                sortedSet2.add(strNextToken.substring(0, strNextToken.length() - 1));
            } else {
                sortedSet.add(strNextToken);
            }
        }
    }

    public static long getServerConnectionTimeout(Map<String, ?> map) {
        return getIntegerAttribute(map, SERVER_CONNECTION_TIMEOUT, 120000L, 0L, Long.MAX_VALUE);
    }

    public static long getConnectionCheckPeriod(Map<String, ?> map) {
        return getIntegerAttribute(map, CLIENT_CONNECTION_CHECK_PERIOD, 60000L, 0L, Long.MAX_VALUE);
    }

    public static boolean computeBooleanFromString(String str) {
        return computeBooleanFromString(str, false);
    }

    public static boolean computeBooleanFromString(String str, boolean z2) {
        if (str == null) {
            return z2;
        }
        if (str.equalsIgnoreCase("true")) {
            return true;
        }
        if (str.equalsIgnoreCase("false")) {
            return false;
        }
        throw new IllegalArgumentException("Property value must be \"true\" or \"false\" instead of \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
    }

    public static <K, V> Hashtable<K, V> mapToHashtable(Map<K, V> map) {
        HashMap map2 = new HashMap(map);
        if (map2.containsKey(null)) {
            map2.remove(null);
        }
        Iterator<V> it = map2.values().iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                it.remove();
            }
        }
        return new Hashtable<>(map2);
    }

    public static boolean isServerDaemon(Map<String, ?> map) {
        return map != null && "true".equalsIgnoreCase((String) map.get(JMX_SERVER_DAEMON));
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/util/EnvHelp$SinkOutputStream.class */
    private static final class SinkOutputStream extends OutputStream {
        private SinkOutputStream() {
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) {
        }

        @Override // java.io.OutputStream
        public void write(int i2) {
        }
    }
}
