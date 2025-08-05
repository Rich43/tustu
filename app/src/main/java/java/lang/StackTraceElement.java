package java.lang;

import java.io.Serializable;
import java.util.Objects;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:java/lang/StackTraceElement.class */
public final class StackTraceElement implements Serializable {
    private String declaringClass;
    private String methodName;
    private String fileName;
    private int lineNumber;
    private static final long serialVersionUID = 6992337162326171013L;

    public StackTraceElement(String str, String str2, String str3, int i2) {
        this.declaringClass = (String) Objects.requireNonNull(str, "Declaring class is null");
        this.methodName = (String) Objects.requireNonNull(str2, "Method name is null");
        this.fileName = str3;
        this.lineNumber = i2;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String getClassName() {
        return this.declaringClass;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public boolean isNativeMethod() {
        return this.lineNumber == -2;
    }

    public String toString() {
        return getClassName() + "." + this.methodName + (isNativeMethod() ? "(Native Method)" : (this.fileName == null || this.lineNumber < 0) ? this.fileName != null ? "(" + this.fileName + ")" : "(Unknown Source)" : "(" + this.fileName + CallSiteDescriptor.TOKEN_DELIMITER + this.lineNumber + ")");
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StackTraceElement)) {
            return false;
        }
        StackTraceElement stackTraceElement = (StackTraceElement) obj;
        return stackTraceElement.declaringClass.equals(this.declaringClass) && stackTraceElement.lineNumber == this.lineNumber && Objects.equals(this.methodName, stackTraceElement.methodName) && Objects.equals(this.fileName, stackTraceElement.fileName);
    }

    public int hashCode() {
        return (31 * ((31 * ((31 * this.declaringClass.hashCode()) + this.methodName.hashCode())) + Objects.hashCode(this.fileName))) + this.lineNumber;
    }
}
