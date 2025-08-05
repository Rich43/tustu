package javafx.scene.input;

/* loaded from: jfxrt.jar:javafx/scene/input/TransferMode.class */
public enum TransferMode {
    COPY,
    MOVE,
    LINK;

    public static final TransferMode[] ANY = {COPY, MOVE, LINK};
    public static final TransferMode[] COPY_OR_MOVE = {COPY, MOVE};
    public static final TransferMode[] NONE = new TransferMode[0];
}
