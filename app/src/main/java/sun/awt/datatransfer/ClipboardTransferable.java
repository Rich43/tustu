package sun.awt.datatransfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:sun/awt/datatransfer/ClipboardTransferable.class */
public class ClipboardTransferable implements Transferable {
    private final HashMap flavorsToData = new HashMap();
    private DataFlavor[] flavors;

    /* loaded from: rt.jar:sun/awt/datatransfer/ClipboardTransferable$DataFactory.class */
    private final class DataFactory {
        final long format;
        final byte[] data;

        DataFactory(long j2, byte[] bArr) {
            this.format = j2;
            this.data = bArr;
        }

        public Object getTransferData(DataFlavor dataFlavor) throws IOException {
            return DataTransferer.getInstance().translateBytes(this.data, dataFlavor, this.format, ClipboardTransferable.this);
        }
    }

    public ClipboardTransferable(SunClipboard sunClipboard) {
        this.flavors = new DataFlavor[0];
        sunClipboard.openClipboard(null);
        try {
            long[] clipboardFormats = sunClipboard.getClipboardFormats();
            if (clipboardFormats != null && clipboardFormats.length > 0) {
                HashMap map = new HashMap(clipboardFormats.length, 1.0f);
                Map flavorsForFormats = DataTransferer.getInstance().getFlavorsForFormats(clipboardFormats, SunClipboard.getDefaultFlavorTable());
                for (DataFlavor dataFlavor : flavorsForFormats.keySet()) {
                    fetchOneFlavor(sunClipboard, dataFlavor, (Long) flavorsForFormats.get(dataFlavor), map);
                }
                DataTransferer.getInstance();
                this.flavors = DataTransferer.setToSortedDataFlavorArray(this.flavorsToData.keySet());
            }
        } finally {
            sunClipboard.closeClipboard();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v28, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r17v1, types: [java.io.IOException] */
    private boolean fetchOneFlavor(SunClipboard sunClipboard, DataFlavor dataFlavor, Long l2, HashMap map) {
        if (!this.flavorsToData.containsKey(dataFlavor)) {
            long jLongValue = l2.longValue();
            byte[] clipboardData = null;
            if (!map.containsKey(l2)) {
                try {
                    clipboardData = sunClipboard.getClipboardData(jLongValue);
                } catch (IOException e2) {
                    clipboardData = e2;
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                map.put(l2, clipboardData);
            } else {
                clipboardData = map.get(l2);
            }
            if (clipboardData instanceof IOException) {
                this.flavorsToData.put(dataFlavor, clipboardData);
                return false;
            }
            if (clipboardData != null) {
                this.flavorsToData.put(dataFlavor, new DataFactory(jLongValue, clipboardData));
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // java.awt.datatransfer.Transferable
    public DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[]) this.flavors.clone();
    }

    @Override // java.awt.datatransfer.Transferable
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        return this.flavorsToData.containsKey(dataFlavor);
    }

    @Override // java.awt.datatransfer.Transferable
    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        if (!isDataFlavorSupported(dataFlavor)) {
            throw new UnsupportedFlavorException(dataFlavor);
        }
        Object transferData = this.flavorsToData.get(dataFlavor);
        if (transferData instanceof IOException) {
            throw ((IOException) transferData);
        }
        if (transferData instanceof DataFactory) {
            transferData = ((DataFactory) transferData).getTransferData(dataFlavor);
        }
        return transferData;
    }
}
