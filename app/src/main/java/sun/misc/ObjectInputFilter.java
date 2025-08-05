package sun.misc;

import java.io.ObjectInputStream;
import java.io.SerializablePermission;
import java.security.AccessController;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import jdk.internal.util.StaticProperty;
import org.icepdf.core.util.PdfOps;
import sun.util.logging.PlatformLogger;

@FunctionalInterface
/* loaded from: rt.jar:sun/misc/ObjectInputFilter.class */
public interface ObjectInputFilter {

    /* loaded from: rt.jar:sun/misc/ObjectInputFilter$FilterInfo.class */
    public interface FilterInfo {
        Class<?> serialClass();

        long arrayLength();

        long depth();

        long references();

        long streamBytes();
    }

    /* loaded from: rt.jar:sun/misc/ObjectInputFilter$Status.class */
    public enum Status {
        UNDECIDED,
        ALLOWED,
        REJECTED
    }

    Status checkInput(FilterInfo filterInfo);

    /* loaded from: rt.jar:sun/misc/ObjectInputFilter$Config.class */
    public static final class Config {
        private static final PlatformLogger configLog;
        private static final String SERIAL_FILTER_PROPNAME = "jdk.serialFilter";
        private static ObjectInputFilter serialFilter;
        private static final Object serialFilterLock = new Object();
        private static final ObjectInputFilter configuredFilter = (ObjectInputFilter) AccessController.doPrivileged(() -> {
            String strJdkSerialFilter = StaticProperty.jdkSerialFilter();
            if (strJdkSerialFilter == null) {
                strJdkSerialFilter = Security.getProperty(SERIAL_FILTER_PROPNAME);
            }
            if (strJdkSerialFilter != null) {
                PlatformLogger logger = PlatformLogger.getLogger("java.io.serialization");
                logger.info("Creating serialization filter from {0}", strJdkSerialFilter);
                try {
                    return createFilter(strJdkSerialFilter);
                } catch (RuntimeException e2) {
                    logger.warning("Error configuring filter: {0}", e2);
                    return null;
                }
            }
            return null;
        });

        private Config() {
        }

        static {
            configLog = configuredFilter != null ? PlatformLogger.getLogger("java.io.serialization") : null;
            serialFilter = configuredFilter;
        }

        static void filterLog(PlatformLogger.Level level, String str, Object... objArr) {
            if (configLog != null) {
                if (PlatformLogger.Level.INFO.equals(level)) {
                    configLog.info(str, objArr);
                } else if (PlatformLogger.Level.WARNING.equals(level)) {
                    configLog.warning(str, objArr);
                } else {
                    configLog.severe(str, objArr);
                }
            }
        }

        public static ObjectInputFilter getObjectInputFilter(ObjectInputStream objectInputStream) {
            Objects.requireNonNull(objectInputStream, "inputStream");
            return SharedSecrets.getJavaOISAccess().getObjectInputFilter(objectInputStream);
        }

        public static void setObjectInputFilter(ObjectInputStream objectInputStream, ObjectInputFilter objectInputFilter) {
            Objects.requireNonNull(objectInputStream, "inputStream");
            SharedSecrets.getJavaOISAccess().setObjectInputFilter(objectInputStream, objectInputFilter);
        }

        public static ObjectInputFilter getSerialFilter() {
            ObjectInputFilter objectInputFilter;
            synchronized (serialFilterLock) {
                objectInputFilter = serialFilter;
            }
            return objectInputFilter;
        }

        public static void setSerialFilter(ObjectInputFilter objectInputFilter) {
            Objects.requireNonNull(objectInputFilter, "filter");
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new SerializablePermission("serialFilter"));
            }
            synchronized (serialFilterLock) {
                if (serialFilter != null) {
                    throw new IllegalStateException("Serial filter can only be set once");
                }
                serialFilter = objectInputFilter;
            }
        }

        public static ObjectInputFilter createFilter(String str) {
            Objects.requireNonNull(str, "pattern");
            return Global.createFilter(str, true);
        }

        public static ObjectInputFilter createFilter2(String str) {
            Objects.requireNonNull(str, "pattern");
            return Global.createFilter(str, false);
        }

        /* loaded from: rt.jar:sun/misc/ObjectInputFilter$Config$Global.class */
        static final class Global implements ObjectInputFilter {
            private final String pattern;
            private final List<Function<Class<?>, Status>> filters;
            private final boolean checkComponentType;
            private long maxArrayLength = Long.MAX_VALUE;
            private long maxDepth = Long.MAX_VALUE;
            private long maxReferences = Long.MAX_VALUE;
            private long maxStreamBytes = Long.MAX_VALUE;

            static ObjectInputFilter createFilter(String str, boolean z2) {
                Global global = new Global(str, z2);
                if (global.isEmpty()) {
                    return null;
                }
                return global;
            }

            private Global(String str, boolean z2) {
                this.pattern = str;
                this.checkComponentType = z2;
                String[] strArrSplit = str.split(";");
                this.filters = new ArrayList(strArrSplit.length);
                for (String str2 : strArrSplit) {
                    int length = str2.length();
                    if (length != 0 && !parseLimit(str2)) {
                        boolean z3 = str2.charAt(0) == '!';
                        if (str2.indexOf(47) >= 0) {
                            throw new IllegalArgumentException("invalid character \"/\" in: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
                        }
                        if (str2.endsWith("*")) {
                            if (str2.endsWith(".*")) {
                                String strSubstring = str2.substring(z3 ? 1 : 0, length - 1);
                                if (strSubstring.length() < 2) {
                                    throw new IllegalArgumentException("package missing in: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
                                }
                                if (z3) {
                                    this.filters.add(cls -> {
                                        return matchesPackage(cls, strSubstring) ? Status.REJECTED : Status.UNDECIDED;
                                    });
                                } else {
                                    this.filters.add(cls2 -> {
                                        return matchesPackage(cls2, strSubstring) ? Status.ALLOWED : Status.UNDECIDED;
                                    });
                                }
                            } else if (str2.endsWith(".**")) {
                                String strSubstring2 = str2.substring(z3 ? 1 : 0, length - 2);
                                if (strSubstring2.length() < 2) {
                                    throw new IllegalArgumentException("package missing in: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
                                }
                                if (z3) {
                                    this.filters.add(cls3 -> {
                                        return cls3.getName().startsWith(strSubstring2) ? Status.REJECTED : Status.UNDECIDED;
                                    });
                                } else {
                                    this.filters.add(cls4 -> {
                                        return cls4.getName().startsWith(strSubstring2) ? Status.ALLOWED : Status.UNDECIDED;
                                    });
                                }
                            } else {
                                String strSubstring3 = str2.substring(z3 ? 1 : 0, length - 1);
                                if (z3) {
                                    this.filters.add(cls5 -> {
                                        return cls5.getName().startsWith(strSubstring3) ? Status.REJECTED : Status.UNDECIDED;
                                    });
                                } else {
                                    this.filters.add(cls6 -> {
                                        return cls6.getName().startsWith(strSubstring3) ? Status.ALLOWED : Status.UNDECIDED;
                                    });
                                }
                            }
                        } else {
                            String strSubstring4 = str2.substring(z3 ? 1 : 0);
                            if (strSubstring4.isEmpty()) {
                                throw new IllegalArgumentException("class or package missing in: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
                            }
                            if (z3) {
                                this.filters.add(cls7 -> {
                                    return cls7.getName().equals(strSubstring4) ? Status.REJECTED : Status.UNDECIDED;
                                });
                            } else {
                                this.filters.add(cls8 -> {
                                    return cls8.getName().equals(strSubstring4) ? Status.ALLOWED : Status.UNDECIDED;
                                });
                            }
                        }
                    }
                }
            }

            private boolean isEmpty() {
                return this.filters.isEmpty() && this.maxArrayLength == Long.MAX_VALUE && this.maxDepth == Long.MAX_VALUE && this.maxReferences == Long.MAX_VALUE && this.maxStreamBytes == Long.MAX_VALUE;
            }

            private boolean parseLimit(String str) {
                int iIndexOf = str.indexOf(61);
                if (iIndexOf < 0) {
                    return false;
                }
                String strSubstring = str.substring(iIndexOf + 1);
                if (str.startsWith("maxdepth=")) {
                    this.maxDepth = parseValue(strSubstring);
                    return true;
                }
                if (str.startsWith("maxarray=")) {
                    this.maxArrayLength = parseValue(strSubstring);
                    return true;
                }
                if (str.startsWith("maxrefs=")) {
                    this.maxReferences = parseValue(strSubstring);
                    return true;
                }
                if (str.startsWith("maxbytes=")) {
                    this.maxStreamBytes = parseValue(strSubstring);
                    return true;
                }
                throw new IllegalArgumentException("unknown limit: " + str.substring(0, iIndexOf));
            }

            private static long parseValue(String str) throws IllegalArgumentException {
                long j2 = Long.parseLong(str);
                if (j2 < 0) {
                    throw new IllegalArgumentException("negative limit: " + str);
                }
                return j2;
            }

            @Override // sun.misc.ObjectInputFilter
            public Status checkInput(FilterInfo filterInfo) {
                if (filterInfo.references() < 0 || filterInfo.depth() < 0 || filterInfo.streamBytes() < 0 || filterInfo.references() > this.maxReferences || filterInfo.depth() > this.maxDepth || filterInfo.streamBytes() > this.maxStreamBytes) {
                    return Status.REJECTED;
                }
                Class<?> clsSerialClass = filterInfo.serialClass();
                if (clsSerialClass != null) {
                    if (clsSerialClass.isArray()) {
                        if (filterInfo.arrayLength() >= 0 && filterInfo.arrayLength() > this.maxArrayLength) {
                            return Status.REJECTED;
                        }
                        if (!this.checkComponentType) {
                            return Status.UNDECIDED;
                        }
                        do {
                            clsSerialClass = clsSerialClass.getComponentType();
                        } while (clsSerialClass.isArray());
                    }
                    if (clsSerialClass.isPrimitive()) {
                        return Status.UNDECIDED;
                    }
                    Class<?> cls = clsSerialClass;
                    return (Status) this.filters.stream().map(function -> {
                        return (Status) function.apply(cls);
                    }).filter(status -> {
                        return status != Status.UNDECIDED;
                    }).findFirst().orElse(Status.UNDECIDED);
                }
                return Status.UNDECIDED;
            }

            private static boolean matchesPackage(Class<?> cls, String str) {
                String name = cls.getName();
                return name.startsWith(str) && name.lastIndexOf(46) == str.length() - 1;
            }

            public String toString() {
                return this.pattern;
            }
        }
    }
}
