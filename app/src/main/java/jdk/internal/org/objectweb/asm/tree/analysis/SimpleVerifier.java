package jdk.internal.org.objectweb.asm.tree.analysis;

import java.util.List;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/analysis/SimpleVerifier.class */
public class SimpleVerifier extends BasicVerifier {
    private final Type currentClass;
    private final Type currentSuperClass;
    private final List<Type> currentClassInterfaces;
    private final boolean isInterface;
    private ClassLoader loader;

    public SimpleVerifier() {
        this(null, null, false);
    }

    public SimpleVerifier(Type type, Type type2, boolean z2) {
        this(type, type2, null, z2);
    }

    public SimpleVerifier(Type type, Type type2, List<Type> list, boolean z2) {
        this(Opcodes.ASM5, type, type2, list, z2);
    }

    protected SimpleVerifier(int i2, Type type, Type type2, List<Type> list, boolean z2) {
        super(i2);
        this.loader = getClass().getClassLoader();
        this.currentClass = type;
        this.currentSuperClass = type2;
        this.currentClassInterfaces = list;
        this.isInterface = z2;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.loader = classLoader;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.BasicInterpreter, jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue newValue(Type type) {
        if (type == null) {
            return BasicValue.UNINITIALIZED_VALUE;
        }
        boolean z2 = type.getSort() == 9;
        if (z2) {
            switch (type.getElementType().getSort()) {
                case 1:
                case 2:
                case 3:
                case 4:
                    return new BasicValue(type);
            }
        }
        BasicValue basicValueNewValue = super.newValue(type);
        if (BasicValue.REFERENCE_VALUE.equals(basicValueNewValue)) {
            if (z2) {
                String descriptor = newValue(type.getElementType()).getType().getDescriptor();
                for (int i2 = 0; i2 < type.getDimensions(); i2++) {
                    descriptor = '[' + descriptor;
                }
                basicValueNewValue = new BasicValue(Type.getType(descriptor));
            } else {
                basicValueNewValue = new BasicValue(type);
            }
        }
        return basicValueNewValue;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.BasicVerifier
    protected boolean isArrayValue(BasicValue basicValue) {
        Type type = basicValue.getType();
        return type != null && ("Lnull;".equals(type.getDescriptor()) || type.getSort() == 9);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.BasicVerifier
    protected BasicValue getElementValue(BasicValue basicValue) throws AnalyzerException {
        Type type = basicValue.getType();
        if (type != null) {
            if (type.getSort() == 9) {
                return newValue(Type.getType(type.getDescriptor().substring(1)));
            }
            if ("Lnull;".equals(type.getDescriptor())) {
                return basicValue;
            }
        }
        throw new Error("Internal error");
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.BasicVerifier
    protected boolean isSubTypeOf(BasicValue basicValue, BasicValue basicValue2) {
        Type type = basicValue2.getType();
        Type type2 = basicValue.getType();
        switch (type.getSort()) {
            case 5:
            case 6:
            case 7:
            case 8:
                return type2.equals(type);
            case 9:
            case 10:
                if ("Lnull;".equals(type2.getDescriptor())) {
                    return true;
                }
                if (type2.getSort() == 10 || type2.getSort() == 9) {
                    return isAssignableFrom(type, type2);
                }
                return false;
            default:
                throw new Error("Internal error");
        }
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.BasicInterpreter, jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue merge(BasicValue basicValue, BasicValue basicValue2) {
        if (!basicValue.equals(basicValue2)) {
            Type type = basicValue.getType();
            Type type2 = basicValue2.getType();
            if (type != null && ((type.getSort() == 10 || type.getSort() == 9) && type2 != null && (type2.getSort() == 10 || type2.getSort() == 9))) {
                if ("Lnull;".equals(type.getDescriptor())) {
                    return basicValue2;
                }
                if ("Lnull;".equals(type2.getDescriptor())) {
                    return basicValue;
                }
                if (isAssignableFrom(type, type2)) {
                    return basicValue;
                }
                if (isAssignableFrom(type2, type)) {
                    return basicValue2;
                }
                while (type != null && !isInterface(type)) {
                    type = getSuperClass(type);
                    if (isAssignableFrom(type, type2)) {
                        return newValue(type);
                    }
                }
                return BasicValue.REFERENCE_VALUE;
            }
            return BasicValue.UNINITIALIZED_VALUE;
        }
        return basicValue;
    }

    protected boolean isInterface(Type type) {
        if (this.currentClass != null && type.equals(this.currentClass)) {
            return this.isInterface;
        }
        return getClass(type).isInterface();
    }

    protected Type getSuperClass(Type type) {
        if (this.currentClass != null && type.equals(this.currentClass)) {
            return this.currentSuperClass;
        }
        Class<? super Object> superclass = getClass(type).getSuperclass();
        if (superclass == null) {
            return null;
        }
        return Type.getType(superclass);
    }

    protected boolean isAssignableFrom(Type type, Type type2) {
        if (type.equals(type2)) {
            return true;
        }
        if (this.currentClass != null && type.equals(this.currentClass)) {
            if (getSuperClass(type2) == null) {
                return false;
            }
            if (this.isInterface) {
                return type2.getSort() == 10 || type2.getSort() == 9;
            }
            return isAssignableFrom(type, getSuperClass(type2));
        }
        if (this.currentClass != null && type2.equals(this.currentClass)) {
            if (isAssignableFrom(type, this.currentSuperClass)) {
                return true;
            }
            if (this.currentClassInterfaces != null) {
                for (int i2 = 0; i2 < this.currentClassInterfaces.size(); i2++) {
                    if (isAssignableFrom(type, this.currentClassInterfaces.get(i2))) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        Class<?> cls = getClass(type);
        if (cls.isInterface()) {
            cls = Object.class;
        }
        return cls.isAssignableFrom(getClass(type2));
    }

    protected Class<?> getClass(Type type) {
        try {
            if (type.getSort() == 9) {
                return Class.forName(type.getDescriptor().replace('/', '.'), false, this.loader);
            }
            return Class.forName(type.getClassName(), false, this.loader);
        } catch (ClassNotFoundException e2) {
            throw new RuntimeException(e2.toString());
        }
    }
}
