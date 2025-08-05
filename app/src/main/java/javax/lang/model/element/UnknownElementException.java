package javax.lang.model.element;

import javax.lang.model.UnknownEntityException;

/* loaded from: rt.jar:javax/lang/model/element/UnknownElementException.class */
public class UnknownElementException extends UnknownEntityException {
    private static final long serialVersionUID = 269;
    private transient Element element;
    private transient Object parameter;

    public UnknownElementException(Element element, Object obj) {
        super("Unknown element: " + ((Object) element));
        this.element = element;
        this.parameter = obj;
    }

    public Element getUnknownElement() {
        return this.element;
    }

    public Object getArgument() {
        return this.parameter;
    }
}
