package javafx.embed.swing;

import com.sun.javafx.tk.Toolkit;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.SecondaryLoop;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.MouseDragGestureRecognizer;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.dnd.peer.DropTargetContextPeer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import sun.awt.AWTAccessor;
import sun.awt.dnd.SunDragSourceContextPeer;
import sun.swing.JLightweightFrame;

/* loaded from: jfxrt.jar:javafx/embed/swing/FXDnD.class */
final class FXDnD {
    private final SwingNode node;
    private static boolean fxAppThreadIsDispatchThread;
    private volatile SecondaryLoop loop;
    private volatile FXDragSourceContextPeer activeDSContextPeer;
    private boolean isDragSourceListenerInstalled = false;
    private MouseEvent pressEvent = null;
    private long pressTime = 0;
    private final Map<Component, FXDragGestureRecognizer> recognizers = new HashMap();
    private final EventHandler<MouseEvent> onMousePressHandler = event -> {
        this.pressEvent = event;
        this.pressTime = System.currentTimeMillis();
    };
    private final EventHandler<MouseEvent> onDragStartHandler = event -> {
        this.activeDSContextPeer = null;
        MouseEvent firstEv = getInitialGestureEvent();
        SwingFXUtils.runOnEDTAndWait(this, () -> {
            fireEvent((int) firstEv.getX(), (int) firstEv.getY(), this.pressTime, SwingEvents.fxMouseModsToMouseMods(firstEv));
        });
        if (this.activeDSContextPeer == null) {
            return;
        }
        event.consume();
        Dragboard db = getNode().startDragAndDrop((TransferMode[]) SwingDnD.dropActionsToTransferModes(this.activeDSContextPeer.sourceActions).toArray(new TransferMode[1]));
        Map<DataFormat, Object> fxData = new HashMap<>();
        for (String mt : this.activeDSContextPeer.transferable.getMimeTypes()) {
            DataFormat f2 = DataFormat.lookupMimeType(mt);
            if (f2 != null) {
                fxData.put(f2, this.activeDSContextPeer.transferable.getData(mt));
            }
        }
        boolean hasContent = db.setContent(fxData);
        if (!hasContent && !fxAppThreadIsDispatchThread) {
            this.loop.exit();
        }
    };
    private final EventHandler<DragEvent> onDragDoneHandler = event -> {
        event.consume();
        if (!fxAppThreadIsDispatchThread) {
            this.loop.exit();
        }
        if (this.activeDSContextPeer != null) {
            TransferMode mode = event.getTransferMode();
            this.activeDSContextPeer.dragDone(mode == null ? 0 : SwingDnD.transferModeToDropAction(mode), (int) event.getX(), (int) event.getY());
        }
    };
    private boolean isDropTargetListenerInstalled = false;
    private volatile FXDropTargetContextPeer activeDTContextPeer = null;
    private final Map<Component, DropTarget> dropTargets = new HashMap();
    private final EventHandler<DragEvent> onDragEnteredHandler = event -> {
        if (this.activeDTContextPeer == null) {
            this.activeDTContextPeer = new FXDropTargetContextPeer();
        }
        int action = this.activeDTContextPeer.postDropTargetEvent(event);
        if (action != 0) {
            event.consume();
        }
    };
    private final EventHandler<DragEvent> onDragExitedHandler = event -> {
        if (this.activeDTContextPeer == null) {
            this.activeDTContextPeer = new FXDropTargetContextPeer();
        }
        this.activeDTContextPeer.postDropTargetEvent(event);
        this.activeDTContextPeer = null;
    };
    private final EventHandler<DragEvent> onDragOverHandler = event -> {
        if (this.activeDTContextPeer == null) {
            this.activeDTContextPeer = new FXDropTargetContextPeer();
        }
        int action = this.activeDTContextPeer.postDropTargetEvent(event);
        if (action != 0) {
            event.acceptTransferModes((TransferMode[]) SwingDnD.dropActionsToTransferModes(action).toArray(new TransferMode[1]));
            event.consume();
        }
    };
    private final EventHandler<DragEvent> onDragDroppedHandler = event -> {
        if (this.activeDTContextPeer == null) {
            this.activeDTContextPeer = new FXDropTargetContextPeer();
        }
        int action = this.activeDTContextPeer.postDropTargetEvent(event);
        if (action != 0) {
            event.setDropCompleted(this.activeDTContextPeer.success);
            event.consume();
        }
        this.activeDTContextPeer = null;
    };

    private SwingNode getNode() {
        return this.node;
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: javafx.embed.swing.FXDnD.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                boolean unused = FXDnD.fxAppThreadIsDispatchThread = "true".equals(System.getProperty("javafx.embed.singleThread"));
                return null;
            }
        });
    }

    FXDnD(SwingNode node) {
        this.node = node;
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/FXDnD$ComponentMapper.class */
    private class ComponentMapper<T> {

        /* renamed from: x, reason: collision with root package name */
        private int f12632x;

        /* renamed from: y, reason: collision with root package name */
        private int f12633y;
        private T object;

        private ComponentMapper(Map<Component, T> map, int xArg, int yArg) {
            Component parent;
            this.object = null;
            this.f12632x = xArg;
            this.f12633y = yArg;
            JLightweightFrame lwFrame = FXDnD.this.node.getLightweightFrame();
            Component c2 = AWTAccessor.getContainerAccessor().findComponentAt(lwFrame, this.f12632x, this.f12633y, false);
            if (c2 == null) {
                return;
            }
            synchronized (c2.getTreeLock()) {
                do {
                    this.object = map.get(c2);
                    if (this.object != null) {
                        break;
                    }
                    parent = c2.getParent();
                    c2 = parent;
                } while (parent != null);
                if (this.object != null) {
                    while (c2 != lwFrame && c2 != null) {
                        this.f12632x -= c2.getX();
                        this.f12633y -= c2.getY();
                        c2 = c2.getParent();
                    }
                }
            }
        }
    }

    public <T> ComponentMapper<T> mapComponent(Map<Component, T> map, int x2, int y2) {
        return new ComponentMapper<>(map, x2, y2);
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/FXDnD$FXDragGestureRecognizer.class */
    private class FXDragGestureRecognizer extends MouseDragGestureRecognizer {
        FXDragGestureRecognizer(DragSource ds, Component c2, int srcActions, DragGestureListener dgl) {
            super(ds, c2, srcActions, dgl);
            if (c2 != null) {
                FXDnD.this.recognizers.put(c2, this);
            }
        }

        @Override // java.awt.dnd.DragGestureRecognizer
        public void setComponent(Component c2) {
            Component old = getComponent();
            if (old != null) {
                FXDnD.this.recognizers.remove(old);
            }
            super.setComponent(c2);
            if (c2 != null) {
                FXDnD.this.recognizers.put(c2, this);
            }
        }

        @Override // java.awt.dnd.MouseDragGestureRecognizer, java.awt.dnd.DragGestureRecognizer
        protected void registerListeners() {
            SwingFXUtils.runOnFxThread(() -> {
                if (!FXDnD.this.isDragSourceListenerInstalled) {
                    FXDnD.this.node.addEventHandler(MouseEvent.MOUSE_PRESSED, FXDnD.this.onMousePressHandler);
                    FXDnD.this.node.addEventHandler(MouseEvent.DRAG_DETECTED, FXDnD.this.onDragStartHandler);
                    FXDnD.this.node.addEventHandler(DragEvent.DRAG_DONE, FXDnD.this.onDragDoneHandler);
                    FXDnD.this.isDragSourceListenerInstalled = true;
                }
            });
        }

        @Override // java.awt.dnd.MouseDragGestureRecognizer, java.awt.dnd.DragGestureRecognizer
        protected void unregisterListeners() {
            SwingFXUtils.runOnFxThread(() -> {
                if (FXDnD.this.isDragSourceListenerInstalled) {
                    FXDnD.this.node.removeEventHandler(MouseEvent.MOUSE_PRESSED, FXDnD.this.onMousePressHandler);
                    FXDnD.this.node.removeEventHandler(MouseEvent.DRAG_DETECTED, FXDnD.this.onDragStartHandler);
                    FXDnD.this.node.removeEventHandler(DragEvent.DRAG_DONE, FXDnD.this.onDragDoneHandler);
                    FXDnD.this.isDragSourceListenerInstalled = false;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void fireEvent(int x2, int y2, long evTime, int modifiers) {
            appendEvent(new java.awt.event.MouseEvent(getComponent(), 501, evTime, modifiers, x2, y2, 0, false));
            int initialAction = SunDragSourceContextPeer.convertModifiersToDropAction(modifiers, getSourceActions());
            fireDragGestureRecognized(initialAction, new Point(x2, y2));
        }
    }

    private void fireEvent(int x2, int y2, long evTime, int modifiers) {
        ComponentMapper<FXDragGestureRecognizer> mapper = mapComponent(this.recognizers, x2, y2);
        FXDragGestureRecognizer r2 = (FXDragGestureRecognizer) ((ComponentMapper) mapper).object;
        if (r2 != null) {
            r2.fireEvent(((ComponentMapper) mapper).f12632x, ((ComponentMapper) mapper).f12633y, evTime, modifiers);
        } else {
            SwingFXUtils.leaveFXNestedLoop(this);
        }
    }

    private MouseEvent getInitialGestureEvent() {
        return this.pressEvent;
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/FXDnD$FXDragSourceContextPeer.class */
    private final class FXDragSourceContextPeer extends SunDragSourceContextPeer {
        private volatile int sourceActions;
        private final CachingTransferable transferable;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !FXDnD.class.desiredAssertionStatus();
        }

        @Override // sun.awt.dnd.SunDragSourceContextPeer
        public void startSecondaryEventLoop() {
            Toolkit.getToolkit().enterNestedEventLoop(this);
        }

        @Override // sun.awt.dnd.SunDragSourceContextPeer
        public void quitSecondaryEventLoop() {
            if (!$assertionsDisabled && Platform.isFxApplicationThread()) {
                throw new AssertionError();
            }
            Platform.runLater(() -> {
                Toolkit.getToolkit().exitNestedEventLoop(this, null);
            });
        }

        @Override // sun.awt.dnd.SunDragSourceContextPeer
        protected void setNativeCursor(long nativeCtxt, Cursor c2, int cType) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dragDone(int operation, int x2, int y2) {
            dragDropFinished(operation != 0, operation, x2, y2);
        }

        FXDragSourceContextPeer(DragGestureEvent dge) {
            super(dge);
            this.sourceActions = 0;
            this.transferable = new CachingTransferable();
        }

        @Override // sun.awt.dnd.SunDragSourceContextPeer
        protected void startDrag(Transferable trans, long[] formats, Map formatMap) {
            FXDnD.this.activeDSContextPeer = this;
            this.transferable.updateData(trans, true);
            this.sourceActions = getDragSourceContext().getSourceActions();
            if (!FXDnD.fxAppThreadIsDispatchThread) {
                FXDnD.this.loop = java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().createSecondaryLoop();
                SwingFXUtils.leaveFXNestedLoop(FXDnD.this);
                if (!FXDnD.this.loop.enter()) {
                }
            }
        }
    }

    public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> abstractRecognizerClass, DragSource ds, Component c2, int srcActions, DragGestureListener dgl) {
        return new FXDragGestureRecognizer(ds, c2, srcActions, dgl);
    }

    public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dge) throws InvalidDnDOperationException {
        return new FXDragSourceContextPeer(dge);
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/FXDnD$FXDropTargetContextPeer.class */
    private final class FXDropTargetContextPeer implements DropTargetContextPeer {
        private int targetActions;
        private int currentAction;
        private DropTarget dt;
        private DropTargetContext ctx;
        private final CachingTransferable transferable;
        private boolean success;
        private int dropAction;

        private FXDropTargetContextPeer() {
            this.targetActions = 0;
            this.currentAction = 0;
            this.dt = null;
            this.ctx = null;
            this.transferable = new CachingTransferable();
            this.success = false;
            this.dropAction = 0;
        }

        @Override // java.awt.dnd.peer.DropTargetContextPeer
        public synchronized void setTargetActions(int actions) {
            this.targetActions = actions;
        }

        @Override // java.awt.dnd.peer.DropTargetContextPeer
        public synchronized int getTargetActions() {
            return this.targetActions;
        }

        @Override // java.awt.dnd.peer.DropTargetContextPeer
        public synchronized DropTarget getDropTarget() {
            return this.dt;
        }

        @Override // java.awt.dnd.peer.DropTargetContextPeer
        public synchronized boolean isTransferableJVMLocal() {
            return false;
        }

        @Override // java.awt.dnd.peer.DropTargetContextPeer
        public synchronized DataFlavor[] getTransferDataFlavors() {
            return this.transferable.getTransferDataFlavors();
        }

        @Override // java.awt.dnd.peer.DropTargetContextPeer
        public synchronized Transferable getTransferable() {
            return this.transferable;
        }

        @Override // java.awt.dnd.peer.DropTargetContextPeer
        public synchronized void acceptDrag(int dragAction) {
            this.currentAction = dragAction;
        }

        @Override // java.awt.dnd.peer.DropTargetContextPeer
        public synchronized void rejectDrag() {
            this.currentAction = 0;
        }

        @Override // java.awt.dnd.peer.DropTargetContextPeer
        public synchronized void acceptDrop(int dropAction) {
            this.dropAction = dropAction;
        }

        @Override // java.awt.dnd.peer.DropTargetContextPeer
        public synchronized void rejectDrop() {
            this.dropAction = 0;
        }

        @Override // java.awt.dnd.peer.DropTargetContextPeer
        public synchronized void dropComplete(boolean success) {
            this.success = success;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int postDropTargetEvent(DragEvent event) {
            ComponentMapper<DropTarget> mapper = FXDnD.this.mapComponent(FXDnD.this.dropTargets, (int) event.getX(), (int) event.getY());
            EventType<?> fxEvType = event.getEventType();
            Dragboard db = event.getDragboard();
            this.transferable.updateData(db, DragEvent.DRAG_DROPPED.equals(fxEvType));
            int sourceActions = SwingDnD.transferModesToDropActions(db.getTransferModes());
            int userAction = event.getTransferMode() == null ? 0 : SwingDnD.transferModeToDropAction(event.getTransferMode());
            DropTarget target = ((ComponentMapper) mapper).object != null ? (DropTarget) ((ComponentMapper) mapper).object : this.dt;
            SwingFXUtils.runOnEDTAndWait(FXDnD.this, () -> {
                if (target != this.dt) {
                    if (this.ctx != null) {
                        this.ctx.removeNotify();
                    }
                    this.ctx = null;
                    this.dropAction = 0;
                    this.currentAction = 0;
                }
                if (target != null) {
                    if (this.ctx == null) {
                        this.ctx = target.getDropTargetContext();
                        this.ctx.addNotify(this);
                    }
                    if (DragEvent.DRAG_DROPPED.equals(fxEvType)) {
                        target.drop(new DropTargetDropEvent(this.ctx, new Point(mapper.f12632x, mapper.f12633y), userAction, sourceActions));
                    } else {
                        DropTargetDragEvent awtEvent = new DropTargetDragEvent(this.ctx, new Point(mapper.f12632x, mapper.f12633y), userAction, sourceActions);
                        if (DragEvent.DRAG_OVER.equals(fxEvType)) {
                            target.dragOver(awtEvent);
                        } else if (DragEvent.DRAG_ENTERED.equals(fxEvType)) {
                            target.dragEnter(awtEvent);
                        } else if (DragEvent.DRAG_EXITED.equals(fxEvType)) {
                            target.dragExit(awtEvent);
                        }
                    }
                }
                this.dt = (DropTarget) mapper.object;
                if (this.dt == null) {
                    if (this.ctx != null) {
                        this.ctx.removeNotify();
                    }
                    this.ctx = null;
                    this.dropAction = 0;
                    this.currentAction = 0;
                }
                if (DragEvent.DRAG_DROPPED.equals(fxEvType) || DragEvent.DRAG_EXITED.equals(fxEvType)) {
                    if (this.ctx != null) {
                        this.ctx.removeNotify();
                    }
                    this.ctx = null;
                }
                SwingFXUtils.leaveFXNestedLoop(FXDnD.this);
            });
            return DragEvent.DRAG_DROPPED.equals(fxEvType) ? this.dropAction : this.currentAction;
        }
    }

    public void addDropTarget(DropTarget dt) {
        this.dropTargets.put(dt.getComponent(), dt);
        Platform.runLater(() -> {
            if (!this.isDropTargetListenerInstalled) {
                this.node.addEventHandler(DragEvent.DRAG_ENTERED, this.onDragEnteredHandler);
                this.node.addEventHandler(DragEvent.DRAG_EXITED, this.onDragExitedHandler);
                this.node.addEventHandler(DragEvent.DRAG_OVER, this.onDragOverHandler);
                this.node.addEventHandler(DragEvent.DRAG_DROPPED, this.onDragDroppedHandler);
                this.isDropTargetListenerInstalled = true;
            }
        });
    }

    public void removeDropTarget(DropTarget dt) {
        this.dropTargets.remove(dt.getComponent());
        Platform.runLater(() -> {
            if (this.isDropTargetListenerInstalled && this.dropTargets.isEmpty()) {
                this.node.removeEventHandler(DragEvent.DRAG_ENTERED, this.onDragEnteredHandler);
                this.node.removeEventHandler(DragEvent.DRAG_EXITED, this.onDragExitedHandler);
                this.node.removeEventHandler(DragEvent.DRAG_OVER, this.onDragOverHandler);
                this.node.removeEventHandler(DragEvent.DRAG_DROPPED, this.onDragDroppedHandler);
                this.isDropTargetListenerInstalled = true;
            }
        });
    }
}
