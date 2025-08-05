package sun.management;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;

/* loaded from: rt.jar:sun/management/StackTraceElementCompositeData.class */
public class StackTraceElementCompositeData extends LazyCompositeData {
    private final StackTraceElement ste;
    private static final CompositeType stackTraceElementCompositeType;
    private static final String CLASS_NAME = "className";
    private static final String METHOD_NAME = "methodName";
    private static final String FILE_NAME = "fileName";
    private static final String LINE_NUMBER = "lineNumber";
    private static final String NATIVE_METHOD = "nativeMethod";
    private static final String[] stackTraceElementItemNames;
    private static final long serialVersionUID = -2704607706598396827L;

    private StackTraceElementCompositeData(StackTraceElement stackTraceElement) {
        this.ste = stackTraceElement;
    }

    public StackTraceElement getStackTraceElement() {
        return this.ste;
    }

    public static StackTraceElement from(CompositeData compositeData) {
        validateCompositeData(compositeData);
        return new StackTraceElement(getString(compositeData, CLASS_NAME), getString(compositeData, METHOD_NAME), getString(compositeData, FILE_NAME), getInt(compositeData, LINE_NUMBER));
    }

    public static CompositeData toCompositeData(StackTraceElement stackTraceElement) {
        return new StackTraceElementCompositeData(stackTraceElement).getCompositeData();
    }

    @Override // sun.management.LazyCompositeData
    protected CompositeData getCompositeData() {
        try {
            return new CompositeDataSupport(stackTraceElementCompositeType, stackTraceElementItemNames, new Object[]{this.ste.getClassName(), this.ste.getMethodName(), this.ste.getFileName(), new Integer(this.ste.getLineNumber()), new Boolean(this.ste.isNativeMethod())});
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    static {
        try {
            stackTraceElementCompositeType = (CompositeType) MappedMXBeanType.toOpenType(StackTraceElement.class);
            stackTraceElementItemNames = new String[]{CLASS_NAME, METHOD_NAME, FILE_NAME, LINE_NUMBER, NATIVE_METHOD};
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    public static void validateCompositeData(CompositeData compositeData) {
        if (compositeData == null) {
            throw new NullPointerException("Null CompositeData");
        }
        if (!isTypeMatched(stackTraceElementCompositeType, compositeData.getCompositeType())) {
            throw new IllegalArgumentException("Unexpected composite type for StackTraceElement");
        }
    }
}
