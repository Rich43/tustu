package sun.reflect.generics.repository;

import java.lang.reflect.Type;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.parser.SignatureParser;
import sun.reflect.generics.tree.FieldTypeSignature;
import sun.reflect.generics.tree.MethodTypeSignature;
import sun.reflect.generics.tree.TypeSignature;
import sun.reflect.generics.visitor.Reifier;

/* loaded from: rt.jar:sun/reflect/generics/repository/ConstructorRepository.class */
public class ConstructorRepository extends GenericDeclRepository<MethodTypeSignature> {
    private Type[] paramTypes;
    private Type[] exceptionTypes;

    protected ConstructorRepository(String str, GenericsFactory genericsFactory) {
        super(str, genericsFactory);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sun.reflect.generics.repository.AbstractRepository
    public MethodTypeSignature parse(String str) {
        return SignatureParser.make().parseMethodSig(str);
    }

    public static ConstructorRepository make(String str, GenericsFactory genericsFactory) {
        return new ConstructorRepository(str, genericsFactory);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Type[] getParameterTypes() {
        if (this.paramTypes == null) {
            TypeSignature[] parameterTypes = ((MethodTypeSignature) getTree()).getParameterTypes();
            Type[] typeArr = new Type[parameterTypes.length];
            for (int i2 = 0; i2 < parameterTypes.length; i2++) {
                Reifier reifier = getReifier();
                parameterTypes[i2].accept(reifier);
                typeArr[i2] = reifier.getResult();
            }
            this.paramTypes = typeArr;
        }
        return (Type[]) this.paramTypes.clone();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Type[] getExceptionTypes() {
        if (this.exceptionTypes == null) {
            FieldTypeSignature[] exceptionTypes = ((MethodTypeSignature) getTree()).getExceptionTypes();
            Type[] typeArr = new Type[exceptionTypes.length];
            for (int i2 = 0; i2 < exceptionTypes.length; i2++) {
                Reifier reifier = getReifier();
                exceptionTypes[i2].accept(reifier);
                typeArr[i2] = reifier.getResult();
            }
            this.exceptionTypes = typeArr;
        }
        return (Type[]) this.exceptionTypes.clone();
    }
}
