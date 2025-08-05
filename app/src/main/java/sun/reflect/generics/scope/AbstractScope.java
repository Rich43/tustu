package sun.reflect.generics.scope;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;

/* loaded from: rt.jar:sun/reflect/generics/scope/AbstractScope.class */
public abstract class AbstractScope<D extends GenericDeclaration> implements Scope {
    private final D recvr;
    private volatile Scope enclosingScope;

    protected abstract Scope computeEnclosingScope();

    protected AbstractScope(D d2) {
        this.recvr = d2;
    }

    protected D getRecvr() {
        return this.recvr;
    }

    protected Scope getEnclosingScope() {
        Scope scopeComputeEnclosingScope = this.enclosingScope;
        if (scopeComputeEnclosingScope == null) {
            scopeComputeEnclosingScope = computeEnclosingScope();
            this.enclosingScope = scopeComputeEnclosingScope;
        }
        return scopeComputeEnclosingScope;
    }

    @Override // sun.reflect.generics.scope.Scope
    public TypeVariable<?> lookup(String str) {
        for (TypeVariable<?> typeVariable : getRecvr().getTypeParameters()) {
            if (typeVariable.getName().equals(str)) {
                return typeVariable;
            }
        }
        return getEnclosingScope().lookup(str);
    }
}
