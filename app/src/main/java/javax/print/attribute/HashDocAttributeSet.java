package javax.print.attribute;

import java.io.Serializable;

/* loaded from: rt.jar:javax/print/attribute/HashDocAttributeSet.class */
public class HashDocAttributeSet extends HashAttributeSet implements DocAttributeSet, Serializable {
    private static final long serialVersionUID = -1128534486061432528L;

    public HashDocAttributeSet() {
        super((Class<?>) DocAttribute.class);
    }

    public HashDocAttributeSet(DocAttribute docAttribute) {
        super(docAttribute, (Class<?>) DocAttribute.class);
    }

    public HashDocAttributeSet(DocAttribute[] docAttributeArr) {
        super(docAttributeArr, (Class<?>) DocAttribute.class);
    }

    public HashDocAttributeSet(DocAttributeSet docAttributeSet) {
        super(docAttributeSet, (Class<?>) DocAttribute.class);
    }
}
