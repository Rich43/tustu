package java.lang.invoke;

import java.lang.invoke.BoundMethodHandle;
import java.lang.invoke.LambdaForm;

/* loaded from: rt.jar:java/lang/invoke/SimpleMethodHandle.class */
final class SimpleMethodHandle extends BoundMethodHandle {
    static final BoundMethodHandle.SpeciesData SPECIES_DATA = BoundMethodHandle.SpeciesData.EMPTY;

    private SimpleMethodHandle(MethodType methodType, LambdaForm lambdaForm) {
        super(methodType, lambdaForm);
    }

    static BoundMethodHandle make(MethodType methodType, LambdaForm lambdaForm) {
        return new SimpleMethodHandle(methodType, lambdaForm);
    }

    @Override // java.lang.invoke.BoundMethodHandle
    public BoundMethodHandle.SpeciesData speciesData() {
        return SPECIES_DATA;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.lang.invoke.BoundMethodHandle, java.lang.invoke.MethodHandle
    public BoundMethodHandle copyWith(MethodType methodType, LambdaForm lambdaForm) {
        return make(methodType, lambdaForm);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.lang.invoke.BoundMethodHandle, java.lang.invoke.MethodHandle
    public String internalProperties() {
        return "\n& Class=" + getClass().getSimpleName();
    }

    @Override // java.lang.invoke.BoundMethodHandle
    public int fieldCount() {
        return 0;
    }

    @Override // java.lang.invoke.BoundMethodHandle
    final BoundMethodHandle copyWithExtendL(MethodType methodType, LambdaForm lambdaForm, Object obj) {
        return BoundMethodHandle.bindSingle(methodType, lambdaForm, obj);
    }

    @Override // java.lang.invoke.BoundMethodHandle
    final BoundMethodHandle copyWithExtendI(MethodType methodType, LambdaForm lambdaForm, int i2) {
        try {
            return SPECIES_DATA.extendWith(LambdaForm.BasicType.I_TYPE).constructor().invokeBasic(methodType, lambdaForm, i2);
        } catch (Throwable th) {
            throw MethodHandleStatics.uncaughtException(th);
        }
    }

    @Override // java.lang.invoke.BoundMethodHandle
    final BoundMethodHandle copyWithExtendJ(MethodType methodType, LambdaForm lambdaForm, long j2) {
        try {
            return SPECIES_DATA.extendWith(LambdaForm.BasicType.J_TYPE).constructor().invokeBasic(methodType, lambdaForm, j2);
        } catch (Throwable th) {
            throw MethodHandleStatics.uncaughtException(th);
        }
    }

    @Override // java.lang.invoke.BoundMethodHandle
    final BoundMethodHandle copyWithExtendF(MethodType methodType, LambdaForm lambdaForm, float f2) {
        try {
            return SPECIES_DATA.extendWith(LambdaForm.BasicType.F_TYPE).constructor().invokeBasic(methodType, lambdaForm, f2);
        } catch (Throwable th) {
            throw MethodHandleStatics.uncaughtException(th);
        }
    }

    @Override // java.lang.invoke.BoundMethodHandle
    final BoundMethodHandle copyWithExtendD(MethodType methodType, LambdaForm lambdaForm, double d2) {
        try {
            return SPECIES_DATA.extendWith(LambdaForm.BasicType.D_TYPE).constructor().invokeBasic(methodType, lambdaForm, d2);
        } catch (Throwable th) {
            throw MethodHandleStatics.uncaughtException(th);
        }
    }
}
