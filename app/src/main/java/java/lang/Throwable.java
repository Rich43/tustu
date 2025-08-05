package java.lang;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/* loaded from: rt.jar:java/lang/Throwable.class */
public class Throwable implements Serializable {
    private static final long serialVersionUID = -3042686055658047285L;
    private transient Object backtrace;
    private String detailMessage;
    private static final StackTraceElement[] UNASSIGNED_STACK;
    private Throwable cause;
    private StackTraceElement[] stackTrace;
    private static final List<Throwable> SUPPRESSED_SENTINEL;
    private List<Throwable> suppressedExceptions;
    private static final String NULL_CAUSE_MESSAGE = "Cannot suppress a null exception.";
    private static final String SELF_SUPPRESSION_MESSAGE = "Self-suppression not permitted";
    private static final String CAUSE_CAPTION = "Caused by: ";
    private static final String SUPPRESSED_CAPTION = "Suppressed: ";
    private static final Throwable[] EMPTY_THROWABLE_ARRAY;
    static final /* synthetic */ boolean $assertionsDisabled;

    private native Throwable fillInStackTrace(int i2);

    native int getStackTraceDepth();

    native StackTraceElement getStackTraceElement(int i2);

    static {
        $assertionsDisabled = !Throwable.class.desiredAssertionStatus();
        UNASSIGNED_STACK = new StackTraceElement[0];
        SUPPRESSED_SENTINEL = Collections.unmodifiableList(new ArrayList(0));
        EMPTY_THROWABLE_ARRAY = new Throwable[0];
    }

    /* loaded from: rt.jar:java/lang/Throwable$SentinelHolder.class */
    private static class SentinelHolder {
        public static final StackTraceElement STACK_TRACE_ELEMENT_SENTINEL = new StackTraceElement("", "", null, Integer.MIN_VALUE);
        public static final StackTraceElement[] STACK_TRACE_SENTINEL = {STACK_TRACE_ELEMENT_SENTINEL};

        private SentinelHolder() {
        }
    }

    public Throwable() {
        this.cause = this;
        this.stackTrace = UNASSIGNED_STACK;
        this.suppressedExceptions = SUPPRESSED_SENTINEL;
        fillInStackTrace();
    }

    public Throwable(String str) {
        this.cause = this;
        this.stackTrace = UNASSIGNED_STACK;
        this.suppressedExceptions = SUPPRESSED_SENTINEL;
        fillInStackTrace();
        this.detailMessage = str;
    }

    public Throwable(String str, Throwable th) {
        this.cause = this;
        this.stackTrace = UNASSIGNED_STACK;
        this.suppressedExceptions = SUPPRESSED_SENTINEL;
        fillInStackTrace();
        this.detailMessage = str;
        this.cause = th;
    }

    public Throwable(Throwable th) {
        this.cause = this;
        this.stackTrace = UNASSIGNED_STACK;
        this.suppressedExceptions = SUPPRESSED_SENTINEL;
        fillInStackTrace();
        this.detailMessage = th == null ? null : th.toString();
        this.cause = th;
    }

    protected Throwable(String str, Throwable th, boolean z2, boolean z3) {
        this.cause = this;
        this.stackTrace = UNASSIGNED_STACK;
        this.suppressedExceptions = SUPPRESSED_SENTINEL;
        if (z3) {
            fillInStackTrace();
        } else {
            this.stackTrace = null;
        }
        this.detailMessage = str;
        this.cause = th;
        if (!z2) {
            this.suppressedExceptions = null;
        }
    }

    public String getMessage() {
        return this.detailMessage;
    }

    public String getLocalizedMessage() {
        return getMessage();
    }

    public synchronized Throwable getCause() {
        if (this.cause == this) {
            return null;
        }
        return this.cause;
    }

    public synchronized Throwable initCause(Throwable th) {
        if (this.cause != this) {
            throw new IllegalStateException("Can't overwrite cause with " + Objects.toString(th, "a null"), this);
        }
        if (th == this) {
            throw new IllegalArgumentException("Self-causation not permitted", this);
        }
        this.cause = th;
        return this;
    }

    public String toString() {
        String name = getClass().getName();
        String localizedMessage = getLocalizedMessage();
        return localizedMessage != null ? name + ": " + localizedMessage : name;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream printStream) {
        printStackTrace(new WrappedPrintStream(printStream));
    }

    private void printStackTrace(PrintStreamOrWriter printStreamOrWriter) {
        Set<Throwable> setNewSetFromMap = Collections.newSetFromMap(new IdentityHashMap());
        setNewSetFromMap.add(this);
        synchronized (printStreamOrWriter.lock()) {
            printStreamOrWriter.println(this);
            StackTraceElement[] ourStackTrace = getOurStackTrace();
            for (StackTraceElement stackTraceElement : ourStackTrace) {
                printStreamOrWriter.println("\tat " + ((Object) stackTraceElement));
            }
            for (Throwable th : getSuppressed()) {
                th.printEnclosedStackTrace(printStreamOrWriter, ourStackTrace, SUPPRESSED_CAPTION, "\t", setNewSetFromMap);
            }
            Throwable cause = getCause();
            if (cause != null) {
                cause.printEnclosedStackTrace(printStreamOrWriter, ourStackTrace, CAUSE_CAPTION, "", setNewSetFromMap);
            }
        }
    }

    private void printEnclosedStackTrace(PrintStreamOrWriter printStreamOrWriter, StackTraceElement[] stackTraceElementArr, String str, String str2, Set<Throwable> set) {
        if (!$assertionsDisabled && !Thread.holdsLock(printStreamOrWriter.lock())) {
            throw new AssertionError();
        }
        if (set.contains(this)) {
            printStreamOrWriter.println(str2 + str + "[CIRCULAR REFERENCE: " + ((Object) this) + "]");
            return;
        }
        set.add(this);
        StackTraceElement[] ourStackTrace = getOurStackTrace();
        int length = ourStackTrace.length - 1;
        for (int length2 = stackTraceElementArr.length - 1; length >= 0 && length2 >= 0 && ourStackTrace[length].equals(stackTraceElementArr[length2]); length2--) {
            length--;
        }
        int length3 = (ourStackTrace.length - 1) - length;
        printStreamOrWriter.println(str2 + str + ((Object) this));
        for (int i2 = 0; i2 <= length; i2++) {
            printStreamOrWriter.println(str2 + "\tat " + ((Object) ourStackTrace[i2]));
        }
        if (length3 != 0) {
            printStreamOrWriter.println(str2 + "\t... " + length3 + " more");
        }
        for (Throwable th : getSuppressed()) {
            th.printEnclosedStackTrace(printStreamOrWriter, ourStackTrace, SUPPRESSED_CAPTION, str2 + "\t", set);
        }
        Throwable cause = getCause();
        if (cause != null) {
            cause.printEnclosedStackTrace(printStreamOrWriter, ourStackTrace, CAUSE_CAPTION, str2, set);
        }
    }

    public void printStackTrace(PrintWriter printWriter) {
        printStackTrace(new WrappedPrintWriter(printWriter));
    }

    /* loaded from: rt.jar:java/lang/Throwable$PrintStreamOrWriter.class */
    private static abstract class PrintStreamOrWriter {
        abstract Object lock();

        abstract void println(Object obj);

        private PrintStreamOrWriter() {
        }
    }

    /* loaded from: rt.jar:java/lang/Throwable$WrappedPrintStream.class */
    private static class WrappedPrintStream extends PrintStreamOrWriter {
        private final PrintStream printStream;

        WrappedPrintStream(PrintStream printStream) {
            super();
            this.printStream = printStream;
        }

        @Override // java.lang.Throwable.PrintStreamOrWriter
        Object lock() {
            return this.printStream;
        }

        @Override // java.lang.Throwable.PrintStreamOrWriter
        void println(Object obj) {
            this.printStream.println(obj);
        }
    }

    /* loaded from: rt.jar:java/lang/Throwable$WrappedPrintWriter.class */
    private static class WrappedPrintWriter extends PrintStreamOrWriter {
        private final PrintWriter printWriter;

        WrappedPrintWriter(PrintWriter printWriter) {
            super();
            this.printWriter = printWriter;
        }

        @Override // java.lang.Throwable.PrintStreamOrWriter
        Object lock() {
            return this.printWriter;
        }

        @Override // java.lang.Throwable.PrintStreamOrWriter
        void println(Object obj) {
            this.printWriter.println(obj);
        }
    }

    public synchronized Throwable fillInStackTrace() {
        if (this.stackTrace != null || this.backtrace != null) {
            fillInStackTrace(0);
            this.stackTrace = UNASSIGNED_STACK;
        }
        return this;
    }

    public StackTraceElement[] getStackTrace() {
        return (StackTraceElement[]) getOurStackTrace().clone();
    }

    private synchronized StackTraceElement[] getOurStackTrace() {
        if (this.stackTrace == UNASSIGNED_STACK || (this.stackTrace == null && this.backtrace != null)) {
            int stackTraceDepth = getStackTraceDepth();
            this.stackTrace = new StackTraceElement[stackTraceDepth];
            for (int i2 = 0; i2 < stackTraceDepth; i2++) {
                this.stackTrace[i2] = getStackTraceElement(i2);
            }
        } else if (this.stackTrace == null) {
            return UNASSIGNED_STACK;
        }
        return this.stackTrace;
    }

    public void setStackTrace(StackTraceElement[] stackTraceElementArr) {
        StackTraceElement[] stackTraceElementArr2 = (StackTraceElement[]) stackTraceElementArr.clone();
        for (int i2 = 0; i2 < stackTraceElementArr2.length; i2++) {
            if (stackTraceElementArr2[i2] == null) {
                throw new NullPointerException("stackTrace[" + i2 + "]");
            }
        }
        synchronized (this) {
            if (this.stackTrace == null && this.backtrace == null) {
                return;
            }
            this.stackTrace = stackTraceElementArr2;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        List<Throwable> list = this.suppressedExceptions;
        this.suppressedExceptions = SUPPRESSED_SENTINEL;
        StackTraceElement[] stackTraceElementArr = this.stackTrace;
        this.stackTrace = (StackTraceElement[]) UNASSIGNED_STACK.clone();
        if (list != null) {
            int iValidateSuppressedExceptionsList = validateSuppressedExceptionsList(list);
            if (iValidateSuppressedExceptionsList > 0) {
                ArrayList arrayList = new ArrayList(Math.min(100, iValidateSuppressedExceptionsList));
                for (Throwable th : list) {
                    if (th == null) {
                        throw new NullPointerException(NULL_CAUSE_MESSAGE);
                    }
                    if (th == this) {
                        throw new IllegalArgumentException(SELF_SUPPRESSION_MESSAGE);
                    }
                    arrayList.add(th);
                }
                this.suppressedExceptions = arrayList;
            }
        } else {
            this.suppressedExceptions = null;
        }
        if (stackTraceElementArr != null) {
            StackTraceElement[] stackTraceElementArr2 = (StackTraceElement[]) stackTraceElementArr.clone();
            if (stackTraceElementArr2.length >= 1) {
                if (stackTraceElementArr2.length == 1 && SentinelHolder.STACK_TRACE_ELEMENT_SENTINEL.equals(stackTraceElementArr2[0])) {
                    this.stackTrace = null;
                    return;
                }
                for (StackTraceElement stackTraceElement : stackTraceElementArr2) {
                    if (stackTraceElement == null) {
                        throw new NullPointerException("null StackTraceElement in serial stream.");
                    }
                }
                this.stackTrace = stackTraceElementArr2;
            }
        }
    }

    private int validateSuppressedExceptionsList(List<Throwable> list) throws IOException {
        if (Object.class.getClassLoader() != list.getClass().getClassLoader()) {
            throw new StreamCorruptedException("List implementation not on the bootclasspath.");
        }
        int size = list.size();
        if (size < 0) {
            throw new StreamCorruptedException("Negative list size reported.");
        }
        return size;
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        getOurStackTrace();
        StackTraceElement[] stackTraceElementArr = this.stackTrace;
        try {
            if (this.stackTrace == null) {
                this.stackTrace = SentinelHolder.STACK_TRACE_SENTINEL;
            }
            objectOutputStream.defaultWriteObject();
        } finally {
            this.stackTrace = stackTraceElementArr;
        }
    }

    public final synchronized void addSuppressed(Throwable th) {
        if (th == this) {
            throw new IllegalArgumentException(SELF_SUPPRESSION_MESSAGE, th);
        }
        if (th == null) {
            throw new NullPointerException(NULL_CAUSE_MESSAGE);
        }
        if (this.suppressedExceptions == null) {
            return;
        }
        if (this.suppressedExceptions == SUPPRESSED_SENTINEL) {
            this.suppressedExceptions = new ArrayList(1);
        }
        this.suppressedExceptions.add(th);
    }

    public final synchronized Throwable[] getSuppressed() {
        if (this.suppressedExceptions == SUPPRESSED_SENTINEL || this.suppressedExceptions == null) {
            return EMPTY_THROWABLE_ARRAY;
        }
        return (Throwable[]) this.suppressedExceptions.toArray(EMPTY_THROWABLE_ARRAY);
    }
}
