package sun.awt.windows;

import com.sun.javafx.font.LogicalFont;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.NotSerializableException;
import java.util.SortedMap;
import sun.awt.datatransfer.DataTransferer;
import sun.awt.datatransfer.SunClipboard;

/* loaded from: rt.jar:sun/awt/windows/WClipboard.class */
final class WClipboard extends SunClipboard {
    private boolean isClipboardViewerRegistered;

    @Override // sun.awt.datatransfer.SunClipboard
    public native void openClipboard(SunClipboard sunClipboard) throws IllegalStateException;

    @Override // sun.awt.datatransfer.SunClipboard
    public native void closeClipboard();

    private native void publishClipboardData(long j2, byte[] bArr);

    private static native void init();

    @Override // sun.awt.datatransfer.SunClipboard
    protected native long[] getClipboardFormats();

    @Override // sun.awt.datatransfer.SunClipboard
    protected native byte[] getClipboardData(long j2) throws IOException;

    private native void registerClipboardViewer();

    WClipboard() {
        super(LogicalFont.SYSTEM);
    }

    @Override // sun.awt.datatransfer.SunClipboard
    public long getID() {
        return 0L;
    }

    @Override // sun.awt.datatransfer.SunClipboard
    protected void setContentsNative(Transferable transferable) throws IllegalStateException {
        SortedMap<Long, DataFlavor> formatsForTransferable = WDataTransferer.getInstance().getFormatsForTransferable(transferable, getDefaultFlavorTable());
        openClipboard(this);
        try {
            for (Long l2 : formatsForTransferable.keySet()) {
                DataFlavor dataFlavor = formatsForTransferable.get(l2);
                try {
                    publishClipboardData(l2.longValue(), WDataTransferer.getInstance().translateTransferable(transferable, dataFlavor, l2.longValue()));
                } catch (IOException e2) {
                    if (!dataFlavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType) || !(e2 instanceof NotSerializableException)) {
                        e2.printStackTrace();
                    }
                }
            }
        } finally {
            closeClipboard();
        }
    }

    private void lostSelectionOwnershipImpl() {
        lostOwnershipImpl();
    }

    @Override // sun.awt.datatransfer.SunClipboard
    protected void clearNativeContext() {
    }

    static {
        init();
    }

    @Override // sun.awt.datatransfer.SunClipboard
    protected void registerClipboardViewerChecked() {
        if (!this.isClipboardViewerRegistered) {
            registerClipboardViewer();
            this.isClipboardViewerRegistered = true;
        }
    }

    @Override // sun.awt.datatransfer.SunClipboard
    protected void unregisterClipboardViewerChecked() {
    }

    private void handleContentsChanged() {
        if (!areFlavorListenersRegistered()) {
            return;
        }
        long[] clipboardFormats = null;
        try {
            openClipboard(null);
            clipboardFormats = getClipboardFormats();
        } catch (IllegalStateException e2) {
        } finally {
            closeClipboard();
        }
        checkChange(clipboardFormats);
    }

    @Override // sun.awt.datatransfer.SunClipboard
    protected Transferable createLocaleTransferable(long[] jArr) throws IOException {
        boolean z2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= jArr.length) {
                break;
            }
            if (jArr[i2] != 16) {
                i2++;
            } else {
                z2 = true;
                break;
            }
        }
        if (!z2) {
            return null;
        }
        try {
            final byte[] clipboardData = getClipboardData(16L);
            return new Transferable() { // from class: sun.awt.windows.WClipboard.1
                @Override // java.awt.datatransfer.Transferable
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{DataTransferer.javaTextEncodingFlavor};
                }

                @Override // java.awt.datatransfer.Transferable
                public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
                    return dataFlavor.equals(DataTransferer.javaTextEncodingFlavor);
                }

                @Override // java.awt.datatransfer.Transferable
                public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException {
                    if (isDataFlavorSupported(dataFlavor)) {
                        return clipboardData;
                    }
                    throw new UnsupportedFlavorException(dataFlavor);
                }
            };
        } catch (IOException e2) {
            return null;
        }
    }
}
