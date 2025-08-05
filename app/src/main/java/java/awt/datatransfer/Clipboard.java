package java.awt.datatransfer;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import sun.awt.EventListenerAggregate;

/* loaded from: rt.jar:java/awt/datatransfer/Clipboard.class */
public class Clipboard {
    String name;
    protected ClipboardOwner owner;
    protected Transferable contents;
    private EventListenerAggregate flavorListeners;
    private Set<DataFlavor> currentDataFlavors;

    public Clipboard(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public synchronized void setContents(Transferable transferable, ClipboardOwner clipboardOwner) {
        final ClipboardOwner clipboardOwner2 = this.owner;
        final Transferable transferable2 = this.contents;
        this.owner = clipboardOwner;
        this.contents = transferable;
        if (clipboardOwner2 != null && clipboardOwner2 != clipboardOwner) {
            EventQueue.invokeLater(new Runnable() { // from class: java.awt.datatransfer.Clipboard.1
                @Override // java.lang.Runnable
                public void run() {
                    clipboardOwner2.lostOwnership(Clipboard.this, transferable2);
                }
            });
        }
        fireFlavorsChanged();
    }

    public synchronized Transferable getContents(Object obj) {
        return this.contents;
    }

    public DataFlavor[] getAvailableDataFlavors() {
        Transferable contents = getContents(null);
        if (contents == null) {
            return new DataFlavor[0];
        }
        return contents.getTransferDataFlavors();
    }

    public boolean isDataFlavorAvailable(DataFlavor dataFlavor) {
        if (dataFlavor == null) {
            throw new NullPointerException("flavor");
        }
        Transferable contents = getContents(null);
        if (contents == null) {
            return false;
        }
        return contents.isDataFlavorSupported(dataFlavor);
    }

    public Object getData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        if (dataFlavor == null) {
            throw new NullPointerException("flavor");
        }
        Transferable contents = getContents(null);
        if (contents == null) {
            throw new UnsupportedFlavorException(dataFlavor);
        }
        return contents.getTransferData(dataFlavor);
    }

    public synchronized void addFlavorListener(FlavorListener flavorListener) {
        if (flavorListener == null) {
            return;
        }
        if (this.flavorListeners == null) {
            this.currentDataFlavors = getAvailableDataFlavorSet();
            this.flavorListeners = new EventListenerAggregate(FlavorListener.class);
        }
        this.flavorListeners.add(flavorListener);
    }

    public synchronized void removeFlavorListener(FlavorListener flavorListener) {
        if (flavorListener == null || this.flavorListeners == null) {
            return;
        }
        this.flavorListeners.remove(flavorListener);
    }

    public synchronized FlavorListener[] getFlavorListeners() {
        return this.flavorListeners == null ? new FlavorListener[0] : (FlavorListener[]) this.flavorListeners.getListenersCopy();
    }

    private void fireFlavorsChanged() {
        if (this.flavorListeners == null) {
            return;
        }
        Set<DataFlavor> set = this.currentDataFlavors;
        this.currentDataFlavors = getAvailableDataFlavorSet();
        if (set.equals(this.currentDataFlavors)) {
            return;
        }
        for (final FlavorListener flavorListener : (FlavorListener[]) this.flavorListeners.getListenersInternal()) {
            EventQueue.invokeLater(new Runnable() { // from class: java.awt.datatransfer.Clipboard.2
                @Override // java.lang.Runnable
                public void run() {
                    flavorListener.flavorsChanged(new FlavorEvent(Clipboard.this));
                }
            });
        }
    }

    private Set<DataFlavor> getAvailableDataFlavorSet() {
        DataFlavor[] transferDataFlavors;
        HashSet hashSet = new HashSet();
        Transferable contents = getContents(null);
        if (contents != null && (transferDataFlavors = contents.getTransferDataFlavors()) != null) {
            hashSet.addAll(Arrays.asList(transferDataFlavors));
        }
        return hashSet;
    }
}
