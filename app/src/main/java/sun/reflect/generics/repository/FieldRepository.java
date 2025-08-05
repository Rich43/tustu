package sun.reflect.generics.repository;

import java.lang.reflect.Type;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.parser.SignatureParser;
import sun.reflect.generics.tree.TypeSignature;
import sun.reflect.generics.visitor.Reifier;

/* loaded from: rt.jar:sun/reflect/generics/repository/FieldRepository.class */
public class FieldRepository extends AbstractRepository<TypeSignature> {
    private Type genericType;

    protected FieldRepository(String str, GenericsFactory genericsFactory) {
        super(str, genericsFactory);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sun.reflect.generics.repository.AbstractRepository
    public TypeSignature parse(String str) {
        return SignatureParser.make().parseTypeSig(str);
    }

    public static FieldRepository make(String str, GenericsFactory genericsFactory) {
        return new FieldRepository(str, genericsFactory);
    }

    public Type getGenericType() {
        if (this.genericType == null) {
            Reifier reifier = getReifier();
            getTree().accept(reifier);
            this.genericType = reifier.getResult();
        }
        return this.genericType;
    }
}
