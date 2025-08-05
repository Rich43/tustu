package javafx.embed.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

/* loaded from: jfxrt.jar:javafx/embed/swing/CachingTransferable.class */
class CachingTransferable implements Transferable {
    private Map<String, Object> mimeType2Data = Collections.EMPTY_MAP;

    CachingTransferable() {
    }

    @Override // java.awt.datatransfer.Transferable
    public Object getTransferData(DataFlavor flavor) throws UnsupportedEncodingException {
        String mimeType = DataFlavorUtils.getFxMimeType(flavor);
        return DataFlavorUtils.adjustFxData(flavor, getData(mimeType));
    }

    @Override // java.awt.datatransfer.Transferable
    public DataFlavor[] getTransferDataFlavors() {
        String[] mimeTypes = getMimeTypes();
        return DataFlavorUtils.getDataFlavors(mimeTypes);
    }

    @Override // java.awt.datatransfer.Transferable
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return isMimeTypeAvailable(DataFlavorUtils.getFxMimeType(flavor));
    }

    void updateData(Transferable t2, boolean fetchData) {
        Map<String, DataFlavor> mimeType2DataFlavor = DataFlavorUtils.adjustSwingDataFlavors(t2.getTransferDataFlavors());
        try {
            this.mimeType2Data = DataFlavorUtils.readAllData(t2, mimeType2DataFlavor, fetchData);
        } catch (Exception e2) {
            this.mimeType2Data = Collections.EMPTY_MAP;
        }
    }

    void updateData(Clipboard cb, boolean fetchData) {
        this.mimeType2Data = new HashMap();
        for (DataFormat f2 : cb.getContentTypes()) {
            this.mimeType2Data.put(DataFlavorUtils.getMimeType(f2), fetchData ? cb.getContent(f2) : null);
        }
    }

    public Object getData(String mimeType) {
        return this.mimeType2Data.get(mimeType);
    }

    public String[] getMimeTypes() {
        return (String[]) this.mimeType2Data.keySet().toArray(new String[0]);
    }

    public boolean isMimeTypeAvailable(String mimeType) {
        return Arrays.asList(getMimeTypes()).contains(mimeType);
    }
}
