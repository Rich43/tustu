package java.util.logging;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import sun.misc.JavaLangAccess;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/util/logging/LogRecord.class */
public class LogRecord implements Serializable {
    private static final AtomicLong globalSequenceNumber = new AtomicLong(0);
    private static final int MIN_SEQUENTIAL_THREAD_ID = 1073741823;
    private static final AtomicInteger nextThreadId = new AtomicInteger(MIN_SEQUENTIAL_THREAD_ID);
    private static final ThreadLocal<Integer> threadIds = new ThreadLocal<>();
    private Level level;
    private long sequenceNumber;
    private String sourceClassName;
    private String sourceMethodName;
    private String message;
    private int threadID;
    private long millis;
    private Throwable thrown;
    private String loggerName;
    private String resourceBundleName;
    private transient boolean needToInferCaller;
    private transient Object[] parameters;
    private transient ResourceBundle resourceBundle;
    private static final long serialVersionUID = 5372048053134512534L;

    private int defaultThreadID() {
        long id = Thread.currentThread().getId();
        if (id < 1073741823) {
            return (int) id;
        }
        Integer numValueOf = threadIds.get();
        if (numValueOf == null) {
            numValueOf = Integer.valueOf(nextThreadId.getAndIncrement());
            threadIds.set(numValueOf);
        }
        return numValueOf.intValue();
    }

    public LogRecord(Level level, String str) {
        level.getClass();
        this.level = level;
        this.message = str;
        this.sequenceNumber = globalSequenceNumber.getAndIncrement();
        this.threadID = defaultThreadID();
        this.millis = System.currentTimeMillis();
        this.needToInferCaller = true;
    }

    public String getLoggerName() {
        return this.loggerName;
    }

    public void setLoggerName(String str) {
        this.loggerName = str;
    }

    public ResourceBundle getResourceBundle() {
        return this.resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public String getResourceBundleName() {
        return this.resourceBundleName;
    }

    public void setResourceBundleName(String str) {
        this.resourceBundleName = str;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        if (level == null) {
            throw new NullPointerException();
        }
        this.level = level;
    }

    public long getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(long j2) {
        this.sequenceNumber = j2;
    }

    public String getSourceClassName() {
        if (this.needToInferCaller) {
            inferCaller();
        }
        return this.sourceClassName;
    }

    public void setSourceClassName(String str) {
        this.sourceClassName = str;
        this.needToInferCaller = false;
    }

    public String getSourceMethodName() {
        if (this.needToInferCaller) {
            inferCaller();
        }
        return this.sourceMethodName;
    }

    public void setSourceMethodName(String str) {
        this.sourceMethodName = str;
        this.needToInferCaller = false;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public Object[] getParameters() {
        return this.parameters;
    }

    public void setParameters(Object[] objArr) {
        this.parameters = objArr;
    }

    public int getThreadID() {
        return this.threadID;
    }

    public void setThreadID(int i2) {
        this.threadID = i2;
    }

    public long getMillis() {
        return this.millis;
    }

    public void setMillis(long j2) {
        this.millis = j2;
    }

    public Throwable getThrown() {
        return this.thrown;
    }

    public void setThrown(Throwable th) {
        this.thrown = th;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeByte(1);
        objectOutputStream.writeByte(0);
        if (this.parameters == null) {
            objectOutputStream.writeInt(-1);
            return;
        }
        objectOutputStream.writeInt(this.parameters.length);
        for (int i2 = 0; i2 < this.parameters.length; i2++) {
            if (this.parameters[i2] == null) {
                objectOutputStream.writeObject(null);
            } else {
                objectOutputStream.writeObject(this.parameters[i2].toString());
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        byte b2 = objectInputStream.readByte();
        byte b3 = objectInputStream.readByte();
        if (b2 != 1) {
            throw new IOException("LogRecord: bad version: " + ((int) b2) + "." + ((int) b3));
        }
        int i2 = objectInputStream.readInt();
        if (i2 < -1) {
            throw new NegativeArraySizeException();
        }
        if (i2 == -1) {
            this.parameters = null;
        } else if (i2 < 255) {
            this.parameters = new Object[i2];
            for (int i3 = 0; i3 < this.parameters.length; i3++) {
                this.parameters[i3] = objectInputStream.readObject();
            }
        } else {
            ArrayList arrayList = new ArrayList(Math.min(i2, 1024));
            for (int i4 = 0; i4 < i2; i4++) {
                arrayList.add(objectInputStream.readObject());
            }
            this.parameters = arrayList.toArray(new Object[arrayList.size()]);
        }
        if (this.resourceBundleName != null) {
            try {
                this.resourceBundle = ResourceBundle.getBundle(this.resourceBundleName, Locale.getDefault(), ClassLoader.getSystemClassLoader());
            } catch (MissingResourceException e2) {
                this.resourceBundle = null;
            }
        }
        this.needToInferCaller = false;
    }

    private void inferCaller() {
        this.needToInferCaller = false;
        JavaLangAccess javaLangAccess = SharedSecrets.getJavaLangAccess();
        Throwable th = new Throwable();
        int stackTraceDepth = javaLangAccess.getStackTraceDepth(th);
        boolean z2 = true;
        for (int i2 = 0; i2 < stackTraceDepth; i2++) {
            StackTraceElement stackTraceElement = javaLangAccess.getStackTraceElement(th, i2);
            String className = stackTraceElement.getClassName();
            boolean zIsLoggerImplFrame = isLoggerImplFrame(className);
            if (z2) {
                if (zIsLoggerImplFrame) {
                    z2 = false;
                }
            } else if (!zIsLoggerImplFrame && !className.startsWith("java.lang.reflect.") && !className.startsWith("sun.reflect.")) {
                setSourceClassName(className);
                setSourceMethodName(stackTraceElement.getMethodName());
                return;
            }
        }
    }

    private boolean isLoggerImplFrame(String str) {
        return str.equals("java.util.logging.Logger") || str.startsWith("java.util.logging.LoggingProxyImpl") || str.startsWith("sun.util.logging.");
    }
}
