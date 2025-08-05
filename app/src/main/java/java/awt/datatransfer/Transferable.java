package java.awt.datatransfer;

import java.io.IOException;

/* loaded from: rt.jar:java/awt/datatransfer/Transferable.class */
public interface Transferable {
    DataFlavor[] getTransferDataFlavors();

    boolean isDataFlavorSupported(DataFlavor dataFlavor);

    Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException;
}
