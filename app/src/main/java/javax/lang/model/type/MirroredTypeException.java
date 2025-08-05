package javax.lang.model.type;

import java.io.IOException;
import java.io.ObjectInputStream;

/* loaded from: rt.jar:javax/lang/model/type/MirroredTypeException.class */
public class MirroredTypeException extends MirroredTypesException {
    private static final long serialVersionUID = 269;
    private transient TypeMirror type;

    public MirroredTypeException(TypeMirror typeMirror) {
        super("Attempt to access Class object for TypeMirror " + typeMirror.toString(), typeMirror);
        this.type = typeMirror;
    }

    public TypeMirror getTypeMirror() {
        return this.type;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.type = null;
        this.types = null;
    }
}
