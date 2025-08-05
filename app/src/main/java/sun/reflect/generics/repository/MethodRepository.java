package sun.reflect.generics.repository;

import java.lang.reflect.Type;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.tree.MethodTypeSignature;
import sun.reflect.generics.visitor.Reifier;

/* loaded from: rt.jar:sun/reflect/generics/repository/MethodRepository.class */
public class MethodRepository extends ConstructorRepository {
    private Type returnType;

    private MethodRepository(String str, GenericsFactory genericsFactory) {
        super(str, genericsFactory);
    }

    public static MethodRepository make(String str, GenericsFactory genericsFactory) {
        return new MethodRepository(str, genericsFactory);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Type getReturnType() {
        if (this.returnType == null) {
            Reifier reifier = getReifier();
            ((MethodTypeSignature) getTree()).getReturnType().accept(reifier);
            this.returnType = reifier.getResult();
        }
        return this.returnType;
    }
}
