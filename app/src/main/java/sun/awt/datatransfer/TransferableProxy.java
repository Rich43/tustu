package sun.awt.datatransfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:sun/awt/datatransfer/TransferableProxy.class */
public class TransferableProxy implements Transferable {
    protected final Transferable transferable;
    protected final boolean isLocal;

    public TransferableProxy(Transferable transferable, boolean z2) {
        this.transferable = transferable;
        this.isLocal = z2;
    }

    @Override // java.awt.datatransfer.Transferable
    public DataFlavor[] getTransferDataFlavors() {
        return this.transferable.getTransferDataFlavors();
    }

    @Override // java.awt.datatransfer.Transferable
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        return this.transferable.isDataFlavorSupported(dataFlavor);
    }

    @Override // java.awt.datatransfer.Transferable
    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        Object transferData = this.transferable.getTransferData(dataFlavor);
        if (transferData != null && this.isLocal && dataFlavor.isFlavorSerializedObjectType()) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ClassLoaderObjectOutputStream classLoaderObjectOutputStream = new ClassLoaderObjectOutputStream(byteArrayOutputStream);
            classLoaderObjectOutputStream.writeObject(transferData);
            try {
                transferData = new ClassLoaderObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), classLoaderObjectOutputStream.getClassLoaderMap()).readObject();
            } catch (ClassNotFoundException e2) {
                throw ((IOException) new IOException().initCause(e2));
            }
        }
        return transferData;
    }
}
