package jdk.nashorn.internal.codegen.types;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/types/NumericType.class */
public abstract class NumericType extends Type implements BytecodeNumericOps {
    private static final long serialVersionUID = 1;

    protected NumericType(String name, Class<?> clazz, int weight, int slots) {
        super(name, clazz, weight, slots);
    }
}
