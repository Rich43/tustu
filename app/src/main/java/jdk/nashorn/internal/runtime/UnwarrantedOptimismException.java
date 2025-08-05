package jdk.nashorn.internal.runtime;

import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.fxml.FXMLLoader;
import jdk.nashorn.internal.codegen.types.Type;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/UnwarrantedOptimismException.class */
public final class UnwarrantedOptimismException extends RuntimeException {
    public static final int INVALID_PROGRAM_POINT = -1;
    public static final int FIRST_PROGRAM_POINT = 1;
    private Object returnValue;
    private final int programPoint;
    private final Type returnType;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !UnwarrantedOptimismException.class.desiredAssertionStatus();
    }

    public UnwarrantedOptimismException(Object returnValue, int programPoint) {
        this(returnValue, programPoint, getReturnType(returnValue));
    }

    public static boolean isValid(int programPoint) {
        if ($assertionsDisabled || programPoint >= -1) {
            return programPoint != -1;
        }
        throw new AssertionError();
    }

    private static Type getReturnType(Object v2) {
        if (v2 instanceof Double) {
            return Type.NUMBER;
        }
        if ($assertionsDisabled || !(v2 instanceof Integer)) {
            return Type.OBJECT;
        }
        throw new AssertionError((Object) (v2 + " is an int"));
    }

    public UnwarrantedOptimismException(Object returnValue, int programPoint, Type returnType) {
        super("", null, false, Context.DEBUG);
        if (!$assertionsDisabled && returnType == Type.OBJECT && returnValue != null && Type.typeFor(returnValue.getClass()).isNumeric()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && returnType == Type.INT) {
            throw new AssertionError();
        }
        this.returnValue = returnValue;
        this.programPoint = programPoint;
        this.returnType = returnType;
    }

    public static UnwarrantedOptimismException createNarrowest(Object returnValue, int programPoint) {
        if ((returnValue instanceof Float) || ((returnValue instanceof Long) && JSType.isRepresentableAsDouble(((Long) returnValue).longValue()))) {
            return new UnwarrantedOptimismException(Double.valueOf(((Number) returnValue).doubleValue()), programPoint, Type.NUMBER);
        }
        return new UnwarrantedOptimismException(returnValue, programPoint);
    }

    public Object getReturnValueDestructive() {
        Object retval = this.returnValue;
        this.returnValue = null;
        return retval;
    }

    Object getReturnValueNonDestructive() {
        return this.returnValue;
    }

    public Type getReturnType() {
        return this.returnType;
    }

    public boolean hasInvalidProgramPoint() {
        return this.programPoint == -1;
    }

    public int getProgramPoint() {
        return this.programPoint;
    }

    public boolean hasPrimitiveReturnValue() {
        return (this.returnValue instanceof Number) || (this.returnValue instanceof Boolean);
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return "UNWARRANTED OPTIMISM: [returnValue=" + this.returnValue + " (class=" + (this.returnValue == null ? FXMLLoader.NULL_KEYWORD : this.returnValue.getClass().getSimpleName()) + (hasInvalidProgramPoint() ? " <invalid program point>" : " @ program point #" + this.programPoint) + ")]";
    }

    private void writeObject(ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException(getClass().getName());
    }

    private void readObject(ObjectInputStream in) throws NotSerializableException {
        throw new NotSerializableException(getClass().getName());
    }
}
