package java.awt.datatransfer;

import java.io.IOException;
import java.io.StringReader;

/* loaded from: rt.jar:java/awt/datatransfer/StringSelection.class */
public class StringSelection implements Transferable, ClipboardOwner {
    private static final int STRING = 0;
    private static final int PLAIN_TEXT = 1;
    private static final DataFlavor[] flavors = {DataFlavor.stringFlavor, DataFlavor.plainTextFlavor};
    private String data;

    public StringSelection(String str) {
        this.data = str;
    }

    @Override // java.awt.datatransfer.Transferable
    public DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[]) flavors.clone();
    }

    @Override // java.awt.datatransfer.Transferable
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        for (int i2 = 0; i2 < flavors.length; i2++) {
            if (dataFlavor.equals(flavors[i2])) {
                return true;
            }
        }
        return false;
    }

    @Override // java.awt.datatransfer.Transferable
    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        if (dataFlavor.equals(flavors[0])) {
            return this.data;
        }
        if (dataFlavor.equals(flavors[1])) {
            return new StringReader(this.data == null ? "" : this.data);
        }
        throw new UnsupportedFlavorException(dataFlavor);
    }

    @Override // java.awt.datatransfer.ClipboardOwner
    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }
}
