package sun.reflect.generics.repository;

import java.lang.reflect.TypeVariable;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.tree.FormalTypeParameter;
import sun.reflect.generics.tree.Signature;
import sun.reflect.generics.visitor.Reifier;

/* loaded from: rt.jar:sun/reflect/generics/repository/GenericDeclRepository.class */
public abstract class GenericDeclRepository<S extends Signature> extends AbstractRepository<S> {
    private volatile TypeVariable<?>[] typeParams;

    protected GenericDeclRepository(String str, GenericsFactory genericsFactory) {
        super(str, genericsFactory);
    }

    public TypeVariable<?>[] getTypeParameters() {
        TypeVariable<?>[] typeVariableArr = this.typeParams;
        if (typeVariableArr == null) {
            FormalTypeParameter[] formalTypeParameters = getTree().getFormalTypeParameters();
            typeVariableArr = new TypeVariable[formalTypeParameters.length];
            for (int i2 = 0; i2 < formalTypeParameters.length; i2++) {
                Reifier reifier = getReifier();
                formalTypeParameters[i2].accept(reifier);
                typeVariableArr[i2] = (TypeVariable) reifier.getResult();
            }
            this.typeParams = typeVariableArr;
        }
        return (TypeVariable[]) typeVariableArr.clone();
    }
}
