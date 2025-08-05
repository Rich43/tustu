package jdk.jfr.internal;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import jdk.internal.org.objectweb.asm.commons.Method;
import jdk.jfr.internal.EventInstrumentation;

/* loaded from: jfr.jar:jdk/jfr/internal/EventWriterMethod.class */
public enum EventWriterMethod {
    BEGIN_EVENT("(" + jdk.internal.org.objectweb.asm.Type.getType((Class<?>) PlatformEventType.class).getDescriptor() + ")Z", "???", "beginEvent"),
    END_EVENT(Constants.BOOLEAN_VALUE_SIG, "???", "endEvent"),
    PUT_BYTE("(B)V", SchemaSymbols.ATTVAL_BYTE, "putByte"),
    PUT_SHORT("(S)V", SchemaSymbols.ATTVAL_SHORT, "putShort"),
    PUT_INT("(I)V", "int", "putInt"),
    PUT_LONG("(J)V", SchemaSymbols.ATTVAL_LONG, "putLong"),
    PUT_FLOAT("(F)V", SchemaSymbols.ATTVAL_FLOAT, "putFloat"),
    PUT_DOUBLE("(D)V", SchemaSymbols.ATTVAL_DOUBLE, "putDouble"),
    PUT_CHAR("(C)V", "char", "putChar"),
    PUT_BOOLEAN("(Z)V", "boolean", "putBoolean"),
    PUT_THREAD("(Ljava/lang/Thread;)V", Type.THREAD.getName(), "putThread"),
    PUT_CLASS("(Ljava/lang/Class;)V", Type.CLASS.getName(), "putClass"),
    PUT_STRING("(Ljava/lang/String;Ljdk/jfr/internal/StringPool;)V", Type.STRING.getName(), "putString"),
    PUT_EVENT_THREAD("()V", Type.THREAD.getName(), "putEventThread"),
    PUT_STACK_TRACE("()V", "jdk.types.StackTrace", "putStackTrace");

    private final Method asmMethod;
    private final String typeDescriptor;

    EventWriterMethod(String str, String str2, String str3) {
        this.typeDescriptor = ASMToolkit.getDescriptor(str2);
        this.asmMethod = new Method(str3, str);
    }

    public Method asASM() {
        return this.asmMethod;
    }

    public static EventWriterMethod lookupMethod(EventInstrumentation.FieldInfo fieldInfo) {
        if (fieldInfo.fieldName.equals(EventInstrumentation.FIELD_EVENT_THREAD)) {
            return PUT_EVENT_THREAD;
        }
        for (EventWriterMethod eventWriterMethod : values()) {
            if (fieldInfo.fieldDescriptor.equals(eventWriterMethod.typeDescriptor)) {
                return eventWriterMethod;
            }
        }
        throw new Error("Unknown type " + fieldInfo.fieldDescriptor);
    }
}
