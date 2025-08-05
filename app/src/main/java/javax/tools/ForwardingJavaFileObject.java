package javax.tools;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

/* loaded from: rt.jar:javax/tools/ForwardingJavaFileObject.class */
public class ForwardingJavaFileObject<F extends JavaFileObject> extends ForwardingFileObject<F> implements JavaFileObject {
    protected ForwardingJavaFileObject(F f2) {
        super(f2);
    }

    @Override // javax.tools.JavaFileObject
    public JavaFileObject.Kind getKind() {
        return ((JavaFileObject) this.fileObject).getKind();
    }

    @Override // javax.tools.JavaFileObject
    public boolean isNameCompatible(String str, JavaFileObject.Kind kind) {
        return ((JavaFileObject) this.fileObject).isNameCompatible(str, kind);
    }

    @Override // javax.tools.JavaFileObject
    public NestingKind getNestingKind() {
        return ((JavaFileObject) this.fileObject).getNestingKind();
    }

    @Override // javax.tools.JavaFileObject
    public Modifier getAccessLevel() {
        return ((JavaFileObject) this.fileObject).getAccessLevel();
    }
}
