package java.awt.datatransfer;

/* loaded from: rt.jar:java/awt/datatransfer/UnsupportedFlavorException.class */
public class UnsupportedFlavorException extends Exception {
    private static final long serialVersionUID = 5383814944251665601L;

    public UnsupportedFlavorException(DataFlavor dataFlavor) {
        super(dataFlavor != null ? dataFlavor.getHumanPresentableName() : null);
    }
}
