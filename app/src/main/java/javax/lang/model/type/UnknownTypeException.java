package javax.lang.model.type;

import javax.lang.model.UnknownEntityException;

/* loaded from: rt.jar:javax/lang/model/type/UnknownTypeException.class */
public class UnknownTypeException extends UnknownEntityException {
    private static final long serialVersionUID = 269;
    private transient TypeMirror type;
    private transient Object parameter;

    public UnknownTypeException(TypeMirror typeMirror, Object obj) {
        super("Unknown type: " + ((Object) typeMirror));
        this.type = typeMirror;
        this.parameter = obj;
    }

    public TypeMirror getUnknownType() {
        return this.type;
    }

    public Object getArgument() {
        return this.parameter;
    }
}
