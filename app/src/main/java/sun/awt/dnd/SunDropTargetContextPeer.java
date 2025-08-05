package sun.awt.dnd;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DropTargetContextPeer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import sun.awt.AppContext;
import sun.awt.SunToolkit;
import sun.awt.datatransfer.DataTransferer;
import sun.awt.datatransfer.ToolkitThreadBlockedHandler;
import sun.security.util.SecurityConstants;
import sun.util.logging.PlatformLogger;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: rt.jar:sun/awt/dnd/SunDropTargetContextPeer.class */
public abstract class SunDropTargetContextPeer implements DropTargetContextPeer, Transferable {
    public static final boolean DISPATCH_SYNC = true;
    private DropTarget currentDT;
    private DropTargetContext currentDTC;
    private long[] currentT;
    private int currentA;
    private int currentSA;
    private int currentDA;
    private int previousDA;
    private long nativeDragContext;
    private Transferable local;
    private boolean dragRejected = false;
    protected int dropStatus = 0;
    protected boolean dropComplete = false;
    boolean dropInProcess = false;
    protected static final Object _globalLock = new Object();
    private static final PlatformLogger dndLog = PlatformLogger.getLogger("sun.awt.dnd.SunDropTargetContextPeer");
    protected static Transferable currentJVMLocalSourceTransferable = null;
    protected static final int STATUS_NONE = 0;
    protected static final int STATUS_WAIT = 1;
    protected static final int STATUS_ACCEPT = 2;
    protected static final int STATUS_REJECT = -1;

    protected abstract Object getNativeData(long j2) throws IOException;

    protected abstract void doDropDone(boolean z2, int i2, boolean z3);

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$102(sun.awt.dnd.SunDropTargetContextPeer r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.nativeDragContext = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.awt.dnd.SunDropTargetContextPeer.access$102(sun.awt.dnd.SunDropTargetContextPeer, long):long");
    }

    static {
    }

    public static void setCurrentJVMLocalSourceTransferable(Transferable transferable) throws InvalidDnDOperationException {
        synchronized (_globalLock) {
            if (transferable != null) {
                if (currentJVMLocalSourceTransferable != null) {
                    throw new InvalidDnDOperationException();
                }
            }
            currentJVMLocalSourceTransferable = transferable;
        }
    }

    private static Transferable getJVMLocalSourceTransferable() {
        return currentJVMLocalSourceTransferable;
    }

    public SunDropTargetContextPeer() {
    }

    @Override // java.awt.dnd.peer.DropTargetContextPeer
    public DropTarget getDropTarget() {
        return this.currentDT;
    }

    @Override // java.awt.dnd.peer.DropTargetContextPeer
    public synchronized void setTargetActions(int i2) {
        this.currentA = i2 & 1073741827;
    }

    @Override // java.awt.dnd.peer.DropTargetContextPeer
    public int getTargetActions() {
        return this.currentA;
    }

    @Override // java.awt.dnd.peer.DropTargetContextPeer
    public Transferable getTransferable() {
        return this;
    }

    @Override // java.awt.dnd.peer.DropTargetContextPeer
    public DataFlavor[] getTransferDataFlavors() {
        Transferable transferable = this.local;
        if (transferable != null) {
            return transferable.getTransferDataFlavors();
        }
        return DataTransferer.getInstance().getFlavorsForFormatsAsArray(this.currentT, DataTransferer.adaptFlavorMap(this.currentDT.getFlavorMap()));
    }

    @Override // java.awt.datatransfer.Transferable
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        Transferable transferable = this.local;
        if (transferable != null) {
            return transferable.isDataFlavorSupported(dataFlavor);
        }
        return DataTransferer.getInstance().getFlavorsForFormats(this.currentT, DataTransferer.adaptFlavorMap(this.currentDT.getFlavorMap())).containsKey(dataFlavor);
    }

    @Override // java.awt.datatransfer.Transferable
    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException, InvalidDnDOperationException {
        SecurityManager securityManager = System.getSecurityManager();
        try {
            if (!this.dropInProcess && securityManager != null) {
                securityManager.checkPermission(SecurityConstants.AWT.ACCESS_CLIPBOARD_PERMISSION);
            }
            Transferable transferable = this.local;
            if (transferable != null) {
                return transferable.getTransferData(dataFlavor);
            }
            if (this.dropStatus != 2 || this.dropComplete) {
                throw new InvalidDnDOperationException("No drop current");
            }
            Long l2 = (Long) DataTransferer.getInstance().getFlavorsForFormats(this.currentT, DataTransferer.adaptFlavorMap(this.currentDT.getFlavorMap())).get(dataFlavor);
            if (l2 == null) {
                throw new UnsupportedFlavorException(dataFlavor);
            }
            if (dataFlavor.isRepresentationClassRemote() && this.currentDA != 1073741824) {
                throw new InvalidDnDOperationException("only ACTION_LINK is permissable for transfer of java.rmi.Remote objects");
            }
            long jLongValue = l2.longValue();
            Object nativeData = getNativeData(jLongValue);
            if (nativeData instanceof byte[]) {
                try {
                    return DataTransferer.getInstance().translateBytes((byte[]) nativeData, dataFlavor, jLongValue, this);
                } catch (IOException e2) {
                    throw new InvalidDnDOperationException(e2.getMessage());
                }
            }
            if (nativeData instanceof InputStream) {
                try {
                    return DataTransferer.getInstance().translateStream((InputStream) nativeData, dataFlavor, jLongValue, this);
                } catch (IOException e3) {
                    throw new InvalidDnDOperationException(e3.getMessage());
                }
            }
            throw new IOException("no native data was transfered");
        } catch (Exception e4) {
            Thread threadCurrentThread = Thread.currentThread();
            threadCurrentThread.getUncaughtExceptionHandler().uncaughtException(threadCurrentThread, e4);
            return null;
        }
    }

    @Override // java.awt.dnd.peer.DropTargetContextPeer
    public boolean isTransferableJVMLocal() {
        return (this.local == null && getJVMLocalSourceTransferable() == null) ? false : true;
    }

    private int handleEnterMessage(Component component, int i2, int i3, int i4, int i5, long[] jArr, long j2) {
        return postDropTargetEvent(component, i2, i3, i4, i5, jArr, j2, 504, true);
    }

    protected void processEnterMessage(SunDropTargetEvent sunDropTargetEvent) {
        Component component = (Component) sunDropTargetEvent.getSource();
        DropTarget dropTarget = component.getDropTarget();
        Point point = sunDropTargetEvent.getPoint();
        this.local = getJVMLocalSourceTransferable();
        if (this.currentDTC != null) {
            this.currentDTC.removeNotify();
            this.currentDTC = null;
        }
        if (component.isShowing() && dropTarget != null && dropTarget.isActive()) {
            this.currentDT = dropTarget;
            this.currentDTC = this.currentDT.getDropTargetContext();
            this.currentDTC.addNotify(this);
            this.currentA = dropTarget.getDefaultActions();
            try {
                dropTarget.dragEnter(new DropTargetDragEvent(this.currentDTC, point, this.currentDA, this.currentSA));
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                this.currentDA = 0;
                return;
            }
        }
        this.currentDT = null;
        this.currentDTC = null;
        this.currentDA = 0;
        this.currentSA = 0;
        this.currentA = 0;
    }

    private void handleExitMessage(Component component, long j2) {
        postDropTargetEvent(component, 0, 0, 0, 0, null, j2, 505, true);
    }

    protected void processExitMessage(SunDropTargetEvent sunDropTargetEvent) {
        DropTarget dropTarget = ((Component) sunDropTargetEvent.getSource()).getDropTarget();
        if (dropTarget == null) {
            this.currentDT = null;
            this.currentT = null;
            if (this.currentDTC != null) {
                this.currentDTC.removeNotify();
            }
            this.currentDTC = null;
            return;
        }
        if (dropTarget != this.currentDT) {
            if (this.currentDTC != null) {
                this.currentDTC.removeNotify();
            }
            this.currentDT = dropTarget;
            this.currentDTC = dropTarget.getDropTargetContext();
            this.currentDTC.addNotify(this);
        }
        DropTargetContext dropTargetContext = this.currentDTC;
        try {
            if (dropTarget.isActive()) {
                try {
                    dropTarget.dragExit(new DropTargetEvent(dropTargetContext));
                    this.currentA = 0;
                    this.currentSA = 0;
                    this.currentDA = 0;
                    this.currentDT = null;
                    this.currentT = null;
                    this.currentDTC.removeNotify();
                    this.currentDTC = null;
                    this.local = null;
                    this.dragRejected = false;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    this.currentA = 0;
                    this.currentSA = 0;
                    this.currentDA = 0;
                    this.currentDT = null;
                    this.currentT = null;
                    this.currentDTC.removeNotify();
                    this.currentDTC = null;
                    this.local = null;
                    this.dragRejected = false;
                }
            }
        } catch (Throwable th) {
            this.currentA = 0;
            this.currentSA = 0;
            this.currentDA = 0;
            this.currentDT = null;
            this.currentT = null;
            this.currentDTC.removeNotify();
            this.currentDTC = null;
            this.local = null;
            this.dragRejected = false;
            throw th;
        }
    }

    private int handleMotionMessage(Component component, int i2, int i3, int i4, int i5, long[] jArr, long j2) {
        return postDropTargetEvent(component, i2, i3, i4, i5, jArr, j2, 506, true);
    }

    protected void processMotionMessage(SunDropTargetEvent sunDropTargetEvent, boolean z2) {
        Component component = (Component) sunDropTargetEvent.getSource();
        Point point = sunDropTargetEvent.getPoint();
        sunDropTargetEvent.getID();
        DropTarget dropTarget = component.getDropTarget();
        if (component.isShowing() && dropTarget != null && dropTarget.isActive()) {
            if (this.currentDT != dropTarget) {
                if (this.currentDTC != null) {
                    this.currentDTC.removeNotify();
                }
                this.currentDT = dropTarget;
                this.currentDTC = null;
            }
            DropTargetContext dropTargetContext = this.currentDT.getDropTargetContext();
            if (dropTargetContext != this.currentDTC) {
                if (this.currentDTC != null) {
                    this.currentDTC.removeNotify();
                }
                this.currentDTC = dropTargetContext;
                this.currentDTC.addNotify(this);
            }
            this.currentA = this.currentDT.getDefaultActions();
            try {
                DropTargetDragEvent dropTargetDragEvent = new DropTargetDragEvent(dropTargetContext, point, this.currentDA, this.currentSA);
                if (z2) {
                    dropTarget.dropActionChanged(dropTargetDragEvent);
                } else {
                    dropTarget.dragOver(dropTargetDragEvent);
                }
                if (this.dragRejected) {
                    this.currentDA = 0;
                }
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                this.currentDA = 0;
                return;
            }
        }
        this.currentDA = 0;
    }

    private void handleDropMessage(Component component, int i2, int i3, int i4, int i5, long[] jArr, long j2) {
        postDropTargetEvent(component, i2, i3, i4, i5, jArr, j2, 502, false);
    }

    protected void processDropMessage(SunDropTargetEvent sunDropTargetEvent) {
        Component component = (Component) sunDropTargetEvent.getSource();
        Point point = sunDropTargetEvent.getPoint();
        DropTarget dropTarget = component.getDropTarget();
        this.dropStatus = 1;
        this.dropComplete = false;
        if (component.isShowing() && dropTarget != null && dropTarget.isActive()) {
            DropTargetContext dropTargetContext = dropTarget.getDropTargetContext();
            this.currentDT = dropTarget;
            if (this.currentDTC != null) {
                this.currentDTC.removeNotify();
            }
            this.currentDTC = dropTargetContext;
            this.currentDTC.addNotify(this);
            this.currentA = dropTarget.getDefaultActions();
            synchronized (_globalLock) {
                Transferable jVMLocalSourceTransferable = getJVMLocalSourceTransferable();
                this.local = jVMLocalSourceTransferable;
                if (jVMLocalSourceTransferable != null) {
                    setCurrentJVMLocalSourceTransferable(null);
                }
            }
            this.dropInProcess = true;
            try {
                dropTarget.drop(new DropTargetDropEvent(dropTargetContext, point, this.currentDA, this.currentSA, this.local != null));
                if (this.dropStatus == 1) {
                    rejectDrop();
                } else if (!this.dropComplete) {
                    dropComplete(false);
                }
                this.dropInProcess = false;
                return;
            } catch (Throwable th) {
                if (this.dropStatus == 1) {
                    rejectDrop();
                } else if (!this.dropComplete) {
                    dropComplete(false);
                }
                this.dropInProcess = false;
                throw th;
            }
        }
        rejectDrop();
    }

    protected int postDropTargetEvent(Component component, int i2, int i3, int i4, int i5, long[] jArr, long j2, int i6, boolean z2) {
        AppContext appContextTargetToAppContext = SunToolkit.targetToAppContext(component);
        EventDispatcher eventDispatcher = new EventDispatcher(this, i4, i5, jArr, j2, z2);
        SunDropTargetEvent sunDropTargetEvent = new SunDropTargetEvent(component, i6, i2, i3, eventDispatcher);
        if (z2) {
            DataTransferer.getInstance().getToolkitThreadBlockedHandler().lock();
        }
        SunToolkit.postEvent(appContextTargetToAppContext, sunDropTargetEvent);
        eventPosted(sunDropTargetEvent);
        if (z2) {
            while (!eventDispatcher.isDone()) {
                DataTransferer.getInstance().getToolkitThreadBlockedHandler().enter();
            }
            DataTransferer.getInstance().getToolkitThreadBlockedHandler().unlock();
            return eventDispatcher.getReturnValue();
        }
        return 0;
    }

    @Override // java.awt.dnd.peer.DropTargetContextPeer
    public synchronized void acceptDrag(int i2) {
        if (this.currentDT == null) {
            throw new InvalidDnDOperationException("No Drag pending");
        }
        this.currentDA = mapOperation(i2);
        if (this.currentDA != 0) {
            this.dragRejected = false;
        }
    }

    @Override // java.awt.dnd.peer.DropTargetContextPeer
    public synchronized void rejectDrag() {
        if (this.currentDT == null) {
            throw new InvalidDnDOperationException("No Drag pending");
        }
        this.currentDA = 0;
        this.dragRejected = true;
    }

    @Override // java.awt.dnd.peer.DropTargetContextPeer
    public synchronized void acceptDrop(int i2) {
        if (i2 == 0) {
            throw new IllegalArgumentException("invalid acceptDrop() action");
        }
        if (this.dropStatus == 1 || this.dropStatus == 2) {
            int iMapOperation = mapOperation(i2 & this.currentSA);
            this.currentA = iMapOperation;
            this.currentDA = iMapOperation;
            this.dropStatus = 2;
            this.dropComplete = false;
            return;
        }
        throw new InvalidDnDOperationException("invalid acceptDrop()");
    }

    @Override // java.awt.dnd.peer.DropTargetContextPeer
    public synchronized void rejectDrop() {
        if (this.dropStatus != 1) {
            throw new InvalidDnDOperationException("invalid rejectDrop()");
        }
        this.dropStatus = -1;
        this.currentDA = 0;
        dropComplete(false);
    }

    private int mapOperation(int i2) {
        int[] iArr = {2, 1, 1073741824};
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i4 >= iArr.length) {
                break;
            }
            if ((i2 & iArr[i4]) != iArr[i4]) {
                i4++;
            } else {
                i3 = iArr[i4];
                break;
            }
        }
        return i3;
    }

    @Override // java.awt.dnd.peer.DropTargetContextPeer
    public synchronized void dropComplete(boolean z2) {
        if (this.dropStatus == 0) {
            throw new InvalidDnDOperationException("No Drop pending");
        }
        if (this.currentDTC != null) {
            this.currentDTC.removeNotify();
        }
        this.currentDT = null;
        this.currentDTC = null;
        this.currentT = null;
        this.currentA = 0;
        synchronized (_globalLock) {
            currentJVMLocalSourceTransferable = null;
        }
        this.dropStatus = 0;
        this.dropComplete = true;
        try {
            doDropDone(z2, this.currentDA, this.local != null);
            this.currentDA = 0;
            this.nativeDragContext = 0L;
        } catch (Throwable th) {
            this.currentDA = 0;
            this.nativeDragContext = 0L;
            throw th;
        }
    }

    protected synchronized long getNativeDragContext() {
        return this.nativeDragContext;
    }

    protected void eventPosted(SunDropTargetEvent sunDropTargetEvent) {
    }

    protected void eventProcessed(SunDropTargetEvent sunDropTargetEvent, int i2, boolean z2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:sun/awt/dnd/SunDropTargetContextPeer$EventDispatcher.class */
    public static class EventDispatcher {
        private final SunDropTargetContextPeer peer;
        private final int dropAction;
        private final int actions;
        private final long[] formats;
        private long nativeCtxt;
        private final boolean dispatchType;
        private boolean dispatcherDone = false;
        private int returnValue = 0;
        private final HashSet eventSet = new HashSet(3);
        static final ToolkitThreadBlockedHandler handler = DataTransferer.getInstance().getToolkitThreadBlockedHandler();

        EventDispatcher(SunDropTargetContextPeer sunDropTargetContextPeer, int i2, int i3, long[] jArr, long j2, boolean z2) {
            this.peer = sunDropTargetContextPeer;
            this.nativeCtxt = j2;
            this.dropAction = i2;
            this.actions = i3;
            this.formats = null == jArr ? null : Arrays.copyOf(jArr, jArr.length);
            this.dispatchType = z2;
        }

        void dispatchEvent(SunDropTargetEvent sunDropTargetEvent) {
            switch (sunDropTargetEvent.getID()) {
                case 502:
                    dispatchDropEvent(sunDropTargetEvent);
                    return;
                case 503:
                default:
                    throw new InvalidDnDOperationException();
                case 504:
                    dispatchEnterEvent(sunDropTargetEvent);
                    return;
                case 505:
                    dispatchExitEvent(sunDropTargetEvent);
                    return;
                case 506:
                    dispatchMotionEvent(sunDropTargetEvent);
                    return;
            }
        }

        /* JADX WARN: Failed to check method for inline after forced processsun.awt.dnd.SunDropTargetContextPeer.access$102(sun.awt.dnd.SunDropTargetContextPeer, long):long */
        private void dispatchEnterEvent(SunDropTargetEvent sunDropTargetEvent) {
            synchronized (this.peer) {
                this.peer.previousDA = this.dropAction;
                SunDropTargetContextPeer.access$102(this.peer, this.nativeCtxt);
                this.peer.currentT = this.formats;
                this.peer.currentSA = this.actions;
                this.peer.currentDA = this.dropAction;
                this.peer.dropStatus = 2;
                this.peer.dropComplete = false;
                try {
                    this.peer.processEnterMessage(sunDropTargetEvent);
                    this.peer.dropStatus = 0;
                    setReturnValue(this.peer.currentDA);
                } catch (Throwable th) {
                    this.peer.dropStatus = 0;
                    throw th;
                }
            }
        }

        /* JADX WARN: Failed to check method for inline after forced processsun.awt.dnd.SunDropTargetContextPeer.access$102(sun.awt.dnd.SunDropTargetContextPeer, long):long */
        private void dispatchMotionEvent(SunDropTargetEvent sunDropTargetEvent) {
            synchronized (this.peer) {
                boolean z2 = this.peer.previousDA != this.dropAction;
                this.peer.previousDA = this.dropAction;
                SunDropTargetContextPeer.access$102(this.peer, this.nativeCtxt);
                this.peer.currentT = this.formats;
                this.peer.currentSA = this.actions;
                this.peer.currentDA = this.dropAction;
                this.peer.dropStatus = 2;
                this.peer.dropComplete = false;
                try {
                    this.peer.processMotionMessage(sunDropTargetEvent, z2);
                    this.peer.dropStatus = 0;
                    setReturnValue(this.peer.currentDA);
                } catch (Throwable th) {
                    this.peer.dropStatus = 0;
                    throw th;
                }
            }
        }

        /* JADX WARN: Failed to check method for inline after forced processsun.awt.dnd.SunDropTargetContextPeer.access$102(sun.awt.dnd.SunDropTargetContextPeer, long):long */
        private void dispatchExitEvent(SunDropTargetEvent sunDropTargetEvent) {
            synchronized (this.peer) {
                SunDropTargetContextPeer.access$102(this.peer, this.nativeCtxt);
                this.peer.processExitMessage(sunDropTargetEvent);
            }
        }

        /* JADX WARN: Failed to check method for inline after forced processsun.awt.dnd.SunDropTargetContextPeer.access$102(sun.awt.dnd.SunDropTargetContextPeer, long):long */
        private void dispatchDropEvent(SunDropTargetEvent sunDropTargetEvent) {
            synchronized (this.peer) {
                SunDropTargetContextPeer.access$102(this.peer, this.nativeCtxt);
                this.peer.currentT = this.formats;
                this.peer.currentSA = this.actions;
                this.peer.currentDA = this.dropAction;
                this.peer.processDropMessage(sunDropTargetEvent);
            }
        }

        void setReturnValue(int i2) {
            this.returnValue = i2;
        }

        int getReturnValue() {
            return this.returnValue;
        }

        boolean isDone() {
            return this.eventSet.isEmpty();
        }

        void registerEvent(SunDropTargetEvent sunDropTargetEvent) {
            handler.lock();
            if (!this.eventSet.add(sunDropTargetEvent) && SunDropTargetContextPeer.dndLog.isLoggable(PlatformLogger.Level.FINE)) {
                SunDropTargetContextPeer.dndLog.fine("Event is already registered: " + ((Object) sunDropTargetEvent));
            }
            handler.unlock();
        }

        /* JADX WARN: Failed to check method for inline after forced processsun.awt.dnd.SunDropTargetContextPeer.access$102(sun.awt.dnd.SunDropTargetContextPeer, long):long */
        void unregisterEvent(SunDropTargetEvent sunDropTargetEvent) {
            boolean z2;
            handler.lock();
            try {
                if (this.eventSet.remove(sunDropTargetEvent)) {
                    if (this.eventSet.isEmpty()) {
                        if (!this.dispatcherDone && this.dispatchType) {
                            handler.exit();
                        }
                        this.dispatcherDone = true;
                    }
                    handler.unlock();
                    try {
                        this.peer.eventProcessed(sunDropTargetEvent, this.returnValue, this.dispatcherDone);
                        if (z2) {
                            return;
                        } else {
                            return;
                        }
                    } finally {
                        if (this.dispatcherDone) {
                            this.nativeCtxt = 0L;
                            SunDropTargetContextPeer.access$102(this.peer, 0L);
                        }
                    }
                }
                handler.unlock();
            } catch (Throwable th) {
                handler.unlock();
                throw th;
            }
        }

        public void unregisterAllEvents() {
            handler.lock();
            try {
                Object[] array = this.eventSet.toArray();
                handler.unlock();
                if (array != null) {
                    for (Object obj : array) {
                        unregisterEvent((SunDropTargetEvent) obj);
                    }
                }
            } catch (Throwable th) {
                handler.unlock();
                throw th;
            }
        }
    }
}
