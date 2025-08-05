package sun.reflect.generics.scope;

import java.lang.reflect.TypeVariable;

/* loaded from: rt.jar:sun/reflect/generics/scope/Scope.class */
public interface Scope {
    TypeVariable<?> lookup(String str);
}
