package sun.awt.datatransfer;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.EventQueue;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.FlavorTable;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import sun.awt.AppContext;
import sun.awt.EventListenerAggregate;
import sun.awt.PeerEvent;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:sun/awt/datatransfer/SunClipboard.class */
public abstract class SunClipboard extends Clipboard implements PropertyChangeListener {
    private AppContext contentsContext;
    private final Object CLIPBOARD_FLAVOR_LISTENER_KEY;
    private volatile int numberOfFlavorListeners;
    private volatile long[] currentFormats;

    public abstract long getID();

    protected abstract void clearNativeContext();

    protected abstract void setContentsNative(Transferable transferable);

    protected abstract long[] getClipboardFormats();

    protected abstract byte[] getClipboardData(long j2) throws IOException;

    protected abstract void registerClipboardViewerChecked();

    protected abstract void unregisterClipboardViewerChecked();

    public SunClipboard(String str) {
        super(str);
        this.contentsContext = null;
        this.numberOfFlavorListeners = 0;
        this.CLIPBOARD_FLAVOR_LISTENER_KEY = new StringBuffer(str + "_CLIPBOARD_FLAVOR_LISTENER_KEY");
    }

    @Override // java.awt.datatransfer.Clipboard
    public synchronized void setContents(Transferable transferable, ClipboardOwner clipboardOwner) {
        if (transferable == null) {
            throw new NullPointerException(Constants.ELEMNAME_CONTENTS_STRING);
        }
        initContext();
        final ClipboardOwner clipboardOwner2 = this.owner;
        final Transferable transferable2 = this.contents;
        try {
            this.owner = clipboardOwner;
            this.contents = new TransferableProxy(transferable, true);
            setContentsNative(transferable);
            if (clipboardOwner2 != null && clipboardOwner2 != clipboardOwner) {
                EventQueue.invokeLater(new Runnable() { // from class: sun.awt.datatransfer.SunClipboard.1
                    @Override // java.lang.Runnable
                    public void run() {
                        clipboardOwner2.lostOwnership(SunClipboard.this, transferable2);
                    }
                });
            }
        } catch (Throwable th) {
            if (clipboardOwner2 != null && clipboardOwner2 != clipboardOwner) {
                EventQueue.invokeLater(new Runnable() { // from class: sun.awt.datatransfer.SunClipboard.1
                    @Override // java.lang.Runnable
                    public void run() {
                        clipboardOwner2.lostOwnership(SunClipboard.this, transferable2);
                    }
                });
            }
            throw th;
        }
    }

    private synchronized void initContext() {
        AppContext appContext = AppContext.getAppContext();
        if (this.contentsContext != appContext) {
            synchronized (appContext) {
                if (appContext.isDisposed()) {
                    throw new IllegalStateException("Can't set contents from disposed AppContext");
                }
                appContext.addPropertyChangeListener(AppContext.DISPOSED_PROPERTY_NAME, this);
            }
            if (this.contentsContext != null) {
                this.contentsContext.removePropertyChangeListener(AppContext.DISPOSED_PROPERTY_NAME, this);
            }
            this.contentsContext = appContext;
        }
    }

    @Override // java.awt.datatransfer.Clipboard
    public synchronized Transferable getContents(Object obj) {
        if (this.contents != null) {
            return this.contents;
        }
        return new ClipboardTransferable(this);
    }

    protected synchronized Transferable getContextContents() {
        if (AppContext.getAppContext() == this.contentsContext) {
            return this.contents;
        }
        return null;
    }

    @Override // java.awt.datatransfer.Clipboard
    public DataFlavor[] getAvailableDataFlavors() {
        Transferable contextContents = getContextContents();
        if (contextContents != null) {
            return contextContents.getTransferDataFlavors();
        }
        return DataTransferer.getInstance().getFlavorsForFormatsAsArray(getClipboardFormatsOpenClose(), getDefaultFlavorTable());
    }

    @Override // java.awt.datatransfer.Clipboard
    public boolean isDataFlavorAvailable(DataFlavor dataFlavor) {
        if (dataFlavor == null) {
            throw new NullPointerException("flavor");
        }
        Transferable contextContents = getContextContents();
        if (contextContents != null) {
            return contextContents.isDataFlavorSupported(dataFlavor);
        }
        return formatArrayAsDataFlavorSet(getClipboardFormatsOpenClose()).contains(dataFlavor);
    }

    @Override // java.awt.datatransfer.Clipboard
    public Object getData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        if (dataFlavor == null) {
            throw new NullPointerException("flavor");
        }
        Transferable contextContents = getContextContents();
        if (contextContents != null) {
            return contextContents.getTransferData(dataFlavor);
        }
        Transferable transferableCreateLocaleTransferable = null;
        try {
            openClipboard(null);
            long[] clipboardFormats = getClipboardFormats();
            Long l2 = (Long) DataTransferer.getInstance().getFlavorsForFormats(clipboardFormats, getDefaultFlavorTable()).get(dataFlavor);
            if (l2 == null) {
                throw new UnsupportedFlavorException(dataFlavor);
            }
            long jLongValue = l2.longValue();
            byte[] clipboardData = getClipboardData(jLongValue);
            if (DataTransferer.getInstance().isLocaleDependentTextFormat(jLongValue)) {
                transferableCreateLocaleTransferable = createLocaleTransferable(clipboardFormats);
            }
            return DataTransferer.getInstance().translateBytes(clipboardData, dataFlavor, jLongValue, transferableCreateLocaleTransferable);
        } finally {
            closeClipboard();
        }
    }

    protected Transferable createLocaleTransferable(long[] jArr) throws IOException {
        return null;
    }

    public void openClipboard(SunClipboard sunClipboard) {
    }

    public void closeClipboard() {
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (AppContext.DISPOSED_PROPERTY_NAME.equals(propertyChangeEvent.getPropertyName()) && Boolean.TRUE.equals(propertyChangeEvent.getNewValue())) {
            lostOwnershipLater((AppContext) propertyChangeEvent.getSource());
        }
    }

    protected void lostOwnershipImpl() {
        lostOwnershipLater(null);
    }

    protected void lostOwnershipLater(AppContext appContext) {
        AppContext appContext2 = this.contentsContext;
        if (appContext2 == null) {
            return;
        }
        SunToolkit.postEvent(appContext2, new PeerEvent(this, () -> {
            lostOwnershipNow(appContext);
        }, 1L));
    }

    protected void lostOwnershipNow(AppContext appContext) {
        synchronized (this) {
            AppContext appContext2 = this.contentsContext;
            if (appContext2 == null) {
                return;
            }
            if (appContext == null || appContext2 == appContext) {
                ClipboardOwner clipboardOwner = this.owner;
                Transferable transferable = this.contents;
                this.contentsContext = null;
                this.owner = null;
                this.contents = null;
                clearNativeContext();
                appContext2.removePropertyChangeListener(AppContext.DISPOSED_PROPERTY_NAME, this);
                if (clipboardOwner != null) {
                    clipboardOwner.lostOwnership(this, transferable);
                }
            }
        }
    }

    protected long[] getClipboardFormatsOpenClose() {
        try {
            openClipboard(null);
            return getClipboardFormats();
        } finally {
            closeClipboard();
        }
    }

    private static Set formatArrayAsDataFlavorSet(long[] jArr) {
        if (jArr == null) {
            return null;
        }
        return DataTransferer.getInstance().getFlavorsForFormatsAsSet(jArr, getDefaultFlavorTable());
    }

    @Override // java.awt.datatransfer.Clipboard
    public synchronized void addFlavorListener(FlavorListener flavorListener) {
        if (flavorListener == null) {
            return;
        }
        AppContext appContext = AppContext.getAppContext();
        EventListenerAggregate eventListenerAggregate = (EventListenerAggregate) appContext.get(this.CLIPBOARD_FLAVOR_LISTENER_KEY);
        if (eventListenerAggregate == null) {
            eventListenerAggregate = new EventListenerAggregate(FlavorListener.class);
            appContext.put(this.CLIPBOARD_FLAVOR_LISTENER_KEY, eventListenerAggregate);
        }
        eventListenerAggregate.add(flavorListener);
        int i2 = this.numberOfFlavorListeners;
        this.numberOfFlavorListeners = i2 + 1;
        if (i2 == 0) {
            long[] clipboardFormats = null;
            try {
                openClipboard(null);
                clipboardFormats = getClipboardFormats();
                closeClipboard();
            } catch (IllegalStateException e2) {
                closeClipboard();
            } catch (Throwable th) {
                closeClipboard();
                throw th;
            }
            this.currentFormats = clipboardFormats;
            registerClipboardViewerChecked();
        }
    }

    @Override // java.awt.datatransfer.Clipboard
    public synchronized void removeFlavorListener(FlavorListener flavorListener) {
        EventListenerAggregate eventListenerAggregate;
        if (flavorListener == null || (eventListenerAggregate = (EventListenerAggregate) AppContext.getAppContext().get(this.CLIPBOARD_FLAVOR_LISTENER_KEY)) == null || !eventListenerAggregate.remove(flavorListener)) {
            return;
        }
        int i2 = this.numberOfFlavorListeners - 1;
        this.numberOfFlavorListeners = i2;
        if (i2 == 0) {
            unregisterClipboardViewerChecked();
            this.currentFormats = null;
        }
    }

    @Override // java.awt.datatransfer.Clipboard
    public synchronized FlavorListener[] getFlavorListeners() {
        EventListenerAggregate eventListenerAggregate = (EventListenerAggregate) AppContext.getAppContext().get(this.CLIPBOARD_FLAVOR_LISTENER_KEY);
        return eventListenerAggregate == null ? new FlavorListener[0] : (FlavorListener[]) eventListenerAggregate.getListenersCopy();
    }

    public boolean areFlavorListenersRegistered() {
        return this.numberOfFlavorListeners > 0;
    }

    protected final void checkChange(long[] jArr) {
        EventListenerAggregate eventListenerAggregate;
        if (Arrays.equals(jArr, this.currentFormats)) {
            return;
        }
        this.currentFormats = jArr;
        for (AppContext appContext : AppContext.getAppContexts()) {
            if (appContext != null && !appContext.isDisposed() && (eventListenerAggregate = (EventListenerAggregate) appContext.get(this.CLIPBOARD_FLAVOR_LISTENER_KEY)) != null) {
                for (FlavorListener flavorListener : (FlavorListener[]) eventListenerAggregate.getListenersInternal()) {
                    SunToolkit.postEvent(appContext, new PeerEvent(this, new Runnable(flavorListener) { // from class: sun.awt.datatransfer.SunClipboard.1SunFlavorChangeNotifier
                        private final FlavorListener flavorListener;

                        {
                            this.flavorListener = flavorListener;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            if (this.flavorListener != null) {
                                this.flavorListener.flavorsChanged(new FlavorEvent(SunClipboard.this));
                            }
                        }
                    }, 1L));
                }
            }
        }
    }

    public static FlavorTable getDefaultFlavorTable() {
        return (FlavorTable) SystemFlavorMap.getDefaultFlavorMap();
    }
}
