package jdk.jfr.internal;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.util.CheckClassAdapter;
import jdk.jfr.Event;
import jdk.jfr.FlightRecorderPermission;
import jdk.jfr.Recording;
import jdk.jfr.RecordingState;
import jdk.jfr.internal.handlers.EventHandler;
import jdk.jfr.internal.settings.PeriodSetting;
import jdk.jfr.internal.settings.StackTraceSetting;
import jdk.jfr.internal.settings.ThresholdSetting;
import net.lingala.zip4j.util.InternalZipConstants;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfr.jar:jdk/jfr/internal/Utils.class */
public final class Utils {
    private static final String INFINITY = "infinity";
    private static Boolean SAVE_GENERATED;
    public static final String EVENTS_PACKAGE_NAME = "jdk.jfr.events";
    public static final String INSTRUMENT_PACKAGE_NAME = "jdk.jfr.internal.instrument";
    public static final String HANDLERS_PACKAGE_NAME = "jdk.jfr.internal.handlers";
    public static final String REGISTER_EVENT = "registerEvent";
    public static final String ACCESS_FLIGHT_RECORDER = "accessFlightRecorder";
    private static final String LEGACY_EVENT_NAME_PREFIX = "com.oracle.jdk.";

    public static void checkAccessFlightRecorder() throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new FlightRecorderPermission(ACCESS_FLIGHT_RECORDER));
        }
    }

    public static void checkRegisterPermission() throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new FlightRecorderPermission(REGISTER_EVENT));
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/Utils$TimespanUnit.class */
    private enum TimespanUnit {
        NANOSECONDS(Constants.ATTRNAME_NS, 1000),
        MICROSECONDS("us", 1000),
        MILLISECONDS("ms", 1000),
        SECONDS(PdfOps.s_TOKEN, 60),
        MINUTES(PdfOps.m_TOKEN, 60),
        HOURS(PdfOps.h_TOKEN, 24),
        DAYS(PdfOps.d_TOKEN, 7);

        final String text;
        final long amount;

        TimespanUnit(String str, long j2) {
            this.text = str;
            this.amount = j2;
        }
    }

    private static String formatDataAmount(String str, long j2) {
        int iLog = (int) (Math.log(Math.abs(j2)) / Math.log(1024.0d));
        return String.format(str, Double.valueOf(j2 / Math.pow(1024.0d, iLog)), Character.valueOf("kMGTPE".charAt(iLog - 1)));
    }

    public static String formatBytesCompact(long j2) {
        if (j2 < 1024) {
            return String.valueOf(j2);
        }
        return formatDataAmount("%.1f%cB", j2);
    }

    public static String formatBits(long j2) {
        if (j2 == 1 || j2 == -1) {
            return j2 + " bit";
        }
        if (j2 < 1024 && j2 > -1024) {
            return j2 + " bits";
        }
        return formatDataAmount("%.1f %cbit", j2);
    }

    public static String formatBytes(long j2) {
        if (j2 == 1 || j2 == -1) {
            return j2 + " byte";
        }
        if (j2 < 1024 && j2 > -1024) {
            return j2 + " bytes";
        }
        return formatDataAmount("%.1f %cB", j2);
    }

    public static String formatBytesPerSecond(long j2) {
        if (j2 < 1024 && j2 > -1024) {
            return j2 + " byte/s";
        }
        return formatDataAmount("%.1f %cB/s", j2);
    }

    public static String formatBitsPerSecond(long j2) {
        if (j2 < 1024 && j2 > -1024) {
            return j2 + " bps";
        }
        return formatDataAmount("%.1f %cbps", j2);
    }

    public static String formatTimespan(Duration duration, String str) {
        if (duration == null) {
            return "0";
        }
        long nanos = duration.toNanos();
        TimespanUnit timespanUnit = TimespanUnit.NANOSECONDS;
        for (TimespanUnit timespanUnit2 : TimespanUnit.values()) {
            timespanUnit = timespanUnit2;
            long j2 = timespanUnit2.amount;
            if (timespanUnit == TimespanUnit.DAYS || nanos < j2 || nanos % j2 != 0) {
                break;
            }
            nanos /= j2;
        }
        return String.format("%d%s%s", Long.valueOf(nanos), str, timespanUnit.text);
    }

    public static long parseTimespanWithInfinity(String str) {
        if ("infinity".equals(str)) {
            return Long.MAX_VALUE;
        }
        return parseTimespan(str);
    }

    public static long parseTimespan(String str) {
        if (str.endsWith(Constants.ATTRNAME_NS)) {
            return Long.parseLong(str.substring(0, str.length() - 2).trim());
        }
        if (str.endsWith("us")) {
            return TimeUnit.NANOSECONDS.convert(Long.parseLong(str.substring(0, str.length() - 2).trim()), TimeUnit.MICROSECONDS);
        }
        if (str.endsWith("ms")) {
            return TimeUnit.NANOSECONDS.convert(Long.parseLong(str.substring(0, str.length() - 2).trim()), TimeUnit.MILLISECONDS);
        }
        if (str.endsWith(PdfOps.s_TOKEN)) {
            return TimeUnit.NANOSECONDS.convert(Long.parseLong(str.substring(0, str.length() - 1).trim()), TimeUnit.SECONDS);
        }
        if (str.endsWith(PdfOps.m_TOKEN)) {
            return 60 * TimeUnit.NANOSECONDS.convert(Long.parseLong(str.substring(0, str.length() - 1).trim()), TimeUnit.SECONDS);
        }
        if (str.endsWith(PdfOps.h_TOKEN)) {
            return 3600 * TimeUnit.NANOSECONDS.convert(Long.parseLong(str.substring(0, str.length() - 1).trim()), TimeUnit.SECONDS);
        }
        if (str.endsWith(PdfOps.d_TOKEN)) {
            return 86400 * TimeUnit.NANOSECONDS.convert(Long.parseLong(str.substring(0, str.length() - 1).trim()), TimeUnit.SECONDS);
        }
        try {
            Long.parseLong(str);
            throw new NumberFormatException("Timespan + '" + str + "' is missing unit. Valid units are ns, us, s, m, h and d.");
        } catch (NumberFormatException e2) {
            throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + str + "' is not a valid timespan. Shoule be numeric value followed by a unit, i.e. 20 ms. Valid units are ns, us, s, m, h and d.");
        }
    }

    static List<Annotation> getAnnotations(Class<?> cls) {
        ArrayList arrayList = new ArrayList();
        for (Annotation annotation : cls.getAnnotations()) {
            arrayList.addAll(getAnnotation(annotation));
        }
        return arrayList;
    }

    private static List<? extends Annotation> getAnnotation(Annotation annotation) {
        Repeatable repeatable;
        Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
        Method valueMethod = getValueMethod(clsAnnotationType);
        if (valueMethod != null) {
            Class<?> returnType = valueMethod.getReturnType();
            if (returnType.isArray() && (repeatable = (Repeatable) returnType.getComponentType().getAnnotation(Repeatable.class)) != null && clsAnnotationType == repeatable.value()) {
                return getAnnotationValues(annotation, valueMethod);
            }
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(annotation);
        return arrayList;
    }

    static boolean isAfter(RecordingState recordingState, RecordingState recordingState2) {
        return recordingState.ordinal() > recordingState2.ordinal();
    }

    static boolean isBefore(RecordingState recordingState, RecordingState recordingState2) {
        return recordingState.ordinal() < recordingState2.ordinal();
    }

    static boolean isState(RecordingState recordingState, RecordingState... recordingStateArr) {
        for (RecordingState recordingState2 : recordingStateArr) {
            if (recordingState2 == recordingState) {
                return true;
            }
        }
        return false;
    }

    private static List<Annotation> getAnnotationValues(Annotation annotation, Method method) {
        try {
            return Arrays.asList((Annotation[]) method.invoke(annotation, new Object[0]));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
            return new ArrayList();
        }
    }

    private static Method getValueMethod(Class<?> cls) {
        try {
            return cls.getMethod("value", new Class[0]);
        } catch (NoSuchMethodException e2) {
            return null;
        }
    }

    public static void touch(Path path) throws IOException {
        new RandomAccessFile(path.toFile(), InternalZipConstants.WRITE_MODE).close();
    }

    public static Class<?> unboxType(Class<?> cls) {
        if (cls == Integer.class) {
            return Integer.TYPE;
        }
        if (cls == Long.class) {
            return Long.TYPE;
        }
        if (cls == Float.class) {
            return Float.TYPE;
        }
        if (cls == Double.class) {
            return Double.TYPE;
        }
        if (cls == Byte.class) {
            return Byte.TYPE;
        }
        if (cls == Short.class) {
            return Short.TYPE;
        }
        if (cls == Boolean.class) {
            return Boolean.TYPE;
        }
        if (cls == Character.class) {
            return Character.TYPE;
        }
        return cls;
    }

    static long nanosToTicks(long j2) {
        return (long) (j2 * JVM.getJVM().getTimeConversionFactor());
    }

    static synchronized EventHandler getHandler(Class<? extends Event> cls) {
        ensureValidEventSubclass(cls);
        try {
            Field declaredField = cls.getDeclaredField("eventHandler");
            SecuritySupport.setAccessible(declaredField);
            return (EventHandler) declaredField.get(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e2) {
            throw new InternalError("Could not access event handler");
        }
    }

    static synchronized void setHandler(Class<? extends Event> cls, EventHandler eventHandler) {
        ensureValidEventSubclass(cls);
        try {
            Field declaredField = cls.getDeclaredField("eventHandler");
            SecuritySupport.setAccessible(declaredField);
            declaredField.set(null, eventHandler);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e2) {
            throw new InternalError("Could not access event handler");
        }
    }

    public static Map<String, String> sanitizeNullFreeStringMap(Map<String, String> map) {
        HashMap map2 = new HashMap(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key == null) {
                throw new NullPointerException("Null key is not allowed in map");
            }
            String value = entry.getValue();
            if (value == null) {
                throw new NullPointerException("Null value is not allowed in map");
            }
            map2.put(key, value);
        }
        return map2;
    }

    public static <T> List<T> sanitizeNullFreeList(List<T> list, Class<T> cls) {
        ArrayList arrayList = new ArrayList(list.size());
        for (T t2 : list) {
            if (t2 == null) {
                throw new NullPointerException("Null is not an allowed element in list");
            }
            if (t2.getClass() != cls) {
                throw new ClassCastException();
            }
            arrayList.add(t2);
        }
        return arrayList;
    }

    static List<Field> getVisibleEventFields(Class<?> cls) throws SecurityException {
        ensureValidEventSubclass(cls);
        ArrayList arrayList = new ArrayList();
        Class<?> superclass = cls;
        while (true) {
            Class<?> cls2 = superclass;
            if (cls2 != Event.class) {
                for (Field field : cls2.getDeclaredFields()) {
                    if (cls2 == cls || !Modifier.isPrivate(field.getModifiers())) {
                        arrayList.add(field);
                    }
                }
                superclass = cls2.getSuperclass();
            } else {
                return arrayList;
            }
        }
    }

    public static void ensureValidEventSubclass(Class<?> cls) {
        if (Event.class.isAssignableFrom(cls) && Modifier.isAbstract(cls.getModifiers())) {
            throw new IllegalArgumentException("Abstract event classes are not allowed");
        }
        if (cls == Event.class || !Event.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Must be a subclass to " + Event.class.getName());
        }
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [java.io.FileOutputStream, java.io.FileWriter] */
    public static void writeGeneratedASM(String str, byte[] bArr) {
        if (SAVE_GENERATED == null) {
            SAVE_GENERATED = Boolean.valueOf(SecuritySupport.getBooleanProperty("jfr.save.generated.asm"));
        }
        if (SAVE_GENERATED.booleanValue()) {
            try {
                ?? fileOutputStream = new FileOutputStream(str + ".class");
                Throwable th = null;
                try {
                    try {
                        fileOutputStream.write(bArr);
                        if (fileOutputStream != 0) {
                            if (0 != 0) {
                                try {
                                    fileOutputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                fileOutputStream.close();
                            }
                        }
                        try {
                            FileWriter fileWriter = new FileWriter(str + ".asm");
                            Throwable th3 = null;
                            PrintWriter printWriter = new PrintWriter(fileWriter);
                            Throwable th4 = null;
                            try {
                                try {
                                    CheckClassAdapter.verify(new ClassReader(bArr), true, printWriter);
                                    if (printWriter != null) {
                                        if (0 != 0) {
                                            try {
                                                printWriter.close();
                                            } catch (Throwable th5) {
                                                th4.addSuppressed(th5);
                                            }
                                        } else {
                                            printWriter.close();
                                        }
                                    }
                                    if (fileWriter != null) {
                                        if (0 != 0) {
                                            try {
                                                fileWriter.close();
                                            } catch (Throwable th6) {
                                                th3.addSuppressed(th6);
                                            }
                                        } else {
                                            fileWriter.close();
                                        }
                                    }
                                    Logger.log(LogTag.JFR_SYSTEM_BYTECODE, LogLevel.INFO, "Instrumented code saved to " + str + ".class and .asm");
                                } catch (Throwable th7) {
                                    if (printWriter != null) {
                                        if (th4 != null) {
                                            try {
                                                printWriter.close();
                                            } catch (Throwable th8) {
                                                th4.addSuppressed(th8);
                                            }
                                        } else {
                                            printWriter.close();
                                        }
                                    }
                                    throw th7;
                                }
                            } catch (Throwable th9) {
                                th4 = th9;
                                throw th9;
                            }
                        } finally {
                        }
                    } finally {
                    }
                } catch (Throwable th10) {
                    th = th10;
                    throw th10;
                }
            } catch (IOException e2) {
                Logger.log(LogTag.JFR_SYSTEM_BYTECODE, LogLevel.INFO, "Could not save instrumented code, for " + str + ".class and .asm");
            }
        }
    }

    public static void ensureInitialized(Class<? extends Event> cls) {
        SecuritySupport.ensureClassIsInitialized(cls);
    }

    public static Object makePrimitiveArray(String str, List<Object> list) {
        int size;
        size = list.size();
        switch (str) {
            case "int":
                int[] iArr = new int[size];
                for (int i2 = 0; i2 < size; i2++) {
                    iArr[i2] = ((Integer) list.get(i2)).intValue();
                }
                return iArr;
            case "long":
                long[] jArr = new long[size];
                for (int i3 = 0; i3 < size; i3++) {
                    jArr[i3] = ((Long) list.get(i3)).longValue();
                }
                return jArr;
            case "float":
                float[] fArr = new float[size];
                for (int i4 = 0; i4 < size; i4++) {
                    fArr[i4] = ((Float) list.get(i4)).floatValue();
                }
                return fArr;
            case "double":
                double[] dArr = new double[size];
                for (int i5 = 0; i5 < size; i5++) {
                    dArr[i5] = ((Double) list.get(i5)).doubleValue();
                }
                return dArr;
            case "short":
                short[] sArr = new short[size];
                for (int i6 = 0; i6 < size; i6++) {
                    sArr[i6] = ((Short) list.get(i6)).shortValue();
                }
                return sArr;
            case "char":
                char[] cArr = new char[size];
                for (int i7 = 0; i7 < size; i7++) {
                    cArr[i7] = ((Character) list.get(i7)).charValue();
                }
                return cArr;
            case "byte":
                byte[] bArr = new byte[size];
                for (int i8 = 0; i8 < size; i8++) {
                    bArr[i8] = ((Byte) list.get(i8)).byteValue();
                }
                return bArr;
            case "boolean":
                boolean[] zArr = new boolean[size];
                for (int i9 = 0; i9 < size; i9++) {
                    zArr[i9] = ((Boolean) list.get(i9)).booleanValue();
                }
                return zArr;
            case "java.lang.String":
                String[] strArr = new String[size];
                for (int i10 = 0; i10 < size; i10++) {
                    strArr[i10] = (String) list.get(i10);
                }
                return strArr;
            default:
                return null;
        }
    }

    public static boolean isSettingVisible(Control control, boolean z2) {
        if (control instanceof ThresholdSetting) {
            return !z2;
        }
        if (control instanceof PeriodSetting) {
            return z2;
        }
        return ((control instanceof StackTraceSetting) && z2) ? false : true;
    }

    public static boolean isSettingVisible(long j2, boolean z2) {
        if (ThresholdSetting.isType(j2)) {
            return !z2;
        }
        if (PeriodSetting.isType(j2)) {
            return z2;
        }
        return (StackTraceSetting.isType(j2) && z2) ? false : true;
    }

    public static Type getValidType(Class<?> cls, String str) {
        Objects.requireNonNull(cls, "Null is not a valid type for value descriptor " + str);
        if (cls.isArray()) {
            cls = cls.getComponentType();
            if (cls != String.class && !cls.isPrimitive()) {
                throw new IllegalArgumentException("Only arrays of primitives and Strings are allowed");
            }
        }
        Type knownType = Type.getKnownType(cls);
        if (knownType == null || knownType == Type.STACK_TRACE) {
            throw new IllegalArgumentException("Only primitive types, java.lang.Thread, java.lang.String and java.lang.Class are allowed for value descriptors. " + cls.getName());
        }
        return knownType;
    }

    public static <T> List<T> smallUnmodifiable(List<T> list) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        if (list.size() == 1) {
            return Collections.singletonList(list.get(0));
        }
        return Collections.unmodifiableList(list);
    }

    public static String upgradeLegacyJDKEvent(String str) {
        int iLastIndexOf;
        if (str.length() <= LEGACY_EVENT_NAME_PREFIX.length()) {
            return str;
        }
        if (str.startsWith(LEGACY_EVENT_NAME_PREFIX) && (iLastIndexOf = str.lastIndexOf(".")) == LEGACY_EVENT_NAME_PREFIX.length() - 1) {
            return Type.EVENT_NAME_PREFIX + str.substring(iLastIndexOf + 1);
        }
        return str;
    }

    public static String makeFilename(Recording recording) {
        return "hotspot-pid-" + JVM.getJVM().getPid() + (recording == null ? "" : "-id-" + Long.toString(recording.getId())) + LanguageTag.SEP + Repository.REPO_DATE_FORMAT.format(LocalDateTime.now()) + ".jfr";
    }
}
