package sun.reflect.generics.repository;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.lang.reflect.Type;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.parser.SignatureParser;
import sun.reflect.generics.tree.ClassSignature;
import sun.reflect.generics.tree.ClassTypeSignature;
import sun.reflect.generics.visitor.Reifier;

/* loaded from: rt.jar:sun/reflect/generics/repository/ClassRepository.class */
public class ClassRepository extends GenericDeclRepository<ClassSignature> {
    public static final ClassRepository NONE = make(Constants.OBJECT_SIG, null);
    private volatile Type superclass;
    private volatile Type[] superInterfaces;

    private ClassRepository(String str, GenericsFactory genericsFactory) {
        super(str, genericsFactory);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // sun.reflect.generics.repository.AbstractRepository
    public ClassSignature parse(String str) {
        return SignatureParser.make().parseClassSig(str);
    }

    public static ClassRepository make(String str, GenericsFactory genericsFactory) {
        return new ClassRepository(str, genericsFactory);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Type getSuperclass() {
        Type result = this.superclass;
        if (result == null) {
            Reifier reifier = getReifier();
            ((ClassSignature) getTree()).getSuperclass().accept(reifier);
            result = reifier.getResult();
            this.superclass = result;
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Type[] getSuperInterfaces() {
        Type[] typeArr = this.superInterfaces;
        if (typeArr == null) {
            ClassTypeSignature[] superInterfaces = ((ClassSignature) getTree()).getSuperInterfaces();
            typeArr = new Type[superInterfaces.length];
            for (int i2 = 0; i2 < superInterfaces.length; i2++) {
                Reifier reifier = getReifier();
                superInterfaces[i2].accept(reifier);
                typeArr[i2] = reifier.getResult();
            }
            this.superInterfaces = typeArr;
        }
        return (Type[]) typeArr.clone();
    }
}
