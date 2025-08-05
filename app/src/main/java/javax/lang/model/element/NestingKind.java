package javax.lang.model.element;

/* loaded from: rt.jar:javax/lang/model/element/NestingKind.class */
public enum NestingKind {
    TOP_LEVEL,
    MEMBER,
    LOCAL,
    ANONYMOUS;

    public boolean isNested() {
        return this != TOP_LEVEL;
    }
}
