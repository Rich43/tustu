package javafx.embed.swing;

import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.HostDragStartListener;
import com.sun.javafx.tk.Toolkit;
import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javafx.scene.input.TransferMode;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/* loaded from: jfxrt.jar:javafx/embed/swing/SwingDnD.class */
final class SwingDnD {
    private final Transferable dndTransferable = new DnDTransferable();
    private final DragSource dragSource;
    private final DragSourceListener dragSourceListener;
    private SwingDragSource swingDragSource;
    private EmbeddedSceneDTInterface fxDropTarget;
    private EmbeddedSceneDSInterface fxDragSource;
    private MouseEvent me;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SwingDnD.class.desiredAssertionStatus();
    }

    SwingDnD(final JComponent comp, final EmbeddedSceneInterface embeddedScene) {
        comp.addMouseListener(new MouseAdapter() { // from class: javafx.embed.swing.SwingDnD.1
            @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
            public void mouseClicked(MouseEvent me) {
                SwingDnD.this.storeMouseEvent(me);
            }

            @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
            public void mouseDragged(MouseEvent me) {
                SwingDnD.this.storeMouseEvent(me);
            }

            @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
            public void mousePressed(MouseEvent me) {
                SwingDnD.this.storeMouseEvent(me);
            }

            @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
            public void mouseReleased(MouseEvent me) {
                SwingDnD.this.storeMouseEvent(me);
            }
        });
        this.dragSource = new DragSource();
        this.dragSourceListener = new DragSourceAdapter() { // from class: javafx.embed.swing.SwingDnD.2
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !SwingDnD.class.desiredAssertionStatus();
            }

            @Override // java.awt.dnd.DragSourceAdapter, java.awt.dnd.DragSourceListener
            public void dragDropEnd(DragSourceDropEvent dsde) {
                if (!$assertionsDisabled && SwingDnD.this.fxDragSource == null) {
                    throw new AssertionError();
                }
                try {
                    SwingDnD.this.fxDragSource.dragDropEnd(SwingDnD.dropActionToTransferMode(dsde.getDropAction()));
                } finally {
                    SwingDnD.this.fxDragSource = null;
                }
            }
        };
        DropTargetListener dtl = new DropTargetAdapter() { // from class: javafx.embed.swing.SwingDnD.3
            private TransferMode lastTransferMode;
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !SwingDnD.class.desiredAssertionStatus();
            }

            @Override // java.awt.dnd.DropTargetAdapter, java.awt.dnd.DropTargetListener
            public void dragEnter(DropTargetDragEvent e2) {
                if (SwingDnD.this.swingDragSource != null || SwingDnD.this.fxDropTarget != null) {
                    return;
                }
                if (!$assertionsDisabled && SwingDnD.this.swingDragSource != null) {
                    throw new AssertionError();
                }
                SwingDnD.this.swingDragSource = new SwingDragSource();
                SwingDnD.this.swingDragSource.updateContents(e2, false);
                if (!$assertionsDisabled && SwingDnD.this.fxDropTarget != null) {
                    throw new AssertionError();
                }
                SwingDnD.this.fxDropTarget = embeddedScene.createDropTarget();
                Point orig = e2.getLocation();
                Point screen = new Point(orig);
                SwingUtilities.convertPointToScreen(screen, comp);
                this.lastTransferMode = SwingDnD.this.fxDropTarget.handleDragEnter(orig.f12370x, orig.f12371y, screen.f12370x, screen.f12371y, SwingDnD.dropActionToTransferMode(e2.getDropAction()), SwingDnD.this.swingDragSource);
                SwingDnD.this.applyDragResult(this.lastTransferMode, e2);
            }

            @Override // java.awt.dnd.DropTargetAdapter, java.awt.dnd.DropTargetListener
            public void dragExit(DropTargetEvent e2) {
                if (!$assertionsDisabled && SwingDnD.this.swingDragSource == null) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && SwingDnD.this.fxDropTarget == null) {
                    throw new AssertionError();
                }
                try {
                    SwingDnD.this.fxDropTarget.handleDragLeave();
                } finally {
                    SwingDnD.this.endDnD();
                    this.lastTransferMode = null;
                }
            }

            @Override // java.awt.dnd.DropTargetAdapter, java.awt.dnd.DropTargetListener
            public void dragOver(DropTargetDragEvent e2) {
                if (!$assertionsDisabled && SwingDnD.this.swingDragSource == null) {
                    throw new AssertionError();
                }
                SwingDnD.this.swingDragSource.updateContents(e2, false);
                if (!$assertionsDisabled && SwingDnD.this.fxDropTarget == null) {
                    throw new AssertionError();
                }
                Point orig = e2.getLocation();
                Point screen = new Point(orig);
                SwingUtilities.convertPointToScreen(screen, comp);
                this.lastTransferMode = SwingDnD.this.fxDropTarget.handleDragOver(orig.f12370x, orig.f12371y, screen.f12370x, screen.f12371y, SwingDnD.dropActionToTransferMode(e2.getDropAction()));
                SwingDnD.this.applyDragResult(this.lastTransferMode, e2);
            }

            @Override // java.awt.dnd.DropTargetListener
            public void drop(DropTargetDropEvent e2) throws InvalidDnDOperationException {
                if (!$assertionsDisabled && SwingDnD.this.swingDragSource == null) {
                    throw new AssertionError();
                }
                SwingDnD.this.applyDropResult(this.lastTransferMode, e2);
                SwingDnD.this.swingDragSource.updateContents(e2, true);
                Point orig = e2.getLocation();
                Point screen = new Point(orig);
                SwingUtilities.convertPointToScreen(screen, comp);
                if (!$assertionsDisabled && SwingDnD.this.fxDropTarget == null) {
                    throw new AssertionError();
                }
                try {
                    this.lastTransferMode = SwingDnD.this.fxDropTarget.handleDragDrop(orig.f12370x, orig.f12371y, screen.f12370x, screen.f12371y, SwingDnD.dropActionToTransferMode(e2.getDropAction()));
                    try {
                        SwingDnD.this.applyDropResult(this.lastTransferMode, e2);
                    } catch (InvalidDnDOperationException e3) {
                    }
                } finally {
                    e2.dropComplete(this.lastTransferMode != null);
                    SwingDnD.this.endDnD();
                    this.lastTransferMode = null;
                }
            }
        };
        comp.setDropTarget(new DropTarget(comp, 1073741827, dtl));
    }

    void addNotify() {
        this.dragSource.addDragSourceListener(this.dragSourceListener);
    }

    void removeNotify() {
        this.dragSource.removeDragSourceListener(this.dragSourceListener);
    }

    HostDragStartListener getDragStartListener() {
        return (dragSource, dragAction) -> {
            if (!$assertionsDisabled && !Toolkit.getToolkit().isFxUserThread()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && dragSource == null) {
                throw new AssertionError();
            }
            SwingUtilities.invokeLater(() -> {
                if (!$assertionsDisabled && this.fxDragSource != null) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && this.swingDragSource != null) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && this.fxDropTarget != null) {
                    throw new AssertionError();
                }
                this.fxDragSource = dragSource;
                startDrag(this.me, this.dndTransferable, dragSource.getSupportedActions(), dragAction);
            });
        };
    }

    private void startDrag(MouseEvent e2, Transferable t2, Set<TransferMode> sa, TransferMode dragAction) throws InvalidDnDOperationException {
        if (!$assertionsDisabled && !sa.contains(dragAction)) {
            throw new AssertionError();
        }
        Point pt = new Point(e2.getX(), e2.getY());
        int action = transferModeToDropAction(dragAction);
        DragGestureRecognizer dgs = new DragGestureRecognizer(this.dragSource, e2, sa) { // from class: javafx.embed.swing.SwingDnD.1StubDragGestureRecognizer
            final /* synthetic */ MouseEvent val$e;
            final /* synthetic */ Set val$sa;

            {
                this.val$e = e2;
                this.val$sa = sa;
                Component component = e2.getComponent();
                setSourceActions(SwingDnD.transferModesToDropActions(this.val$sa));
                appendEvent(this.val$e);
            }

            @Override // java.awt.dnd.DragGestureRecognizer
            protected void registerListeners() {
            }

            @Override // java.awt.dnd.DragGestureRecognizer
            protected void unregisterListeners() {
            }
        };
        List<InputEvent> events = Arrays.asList(dgs.getTriggerEvent());
        DragGestureEvent dse = new DragGestureEvent(dgs, action, pt, events);
        dse.startDrag(null, t2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endDnD() {
        if (!$assertionsDisabled && this.swingDragSource == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.fxDropTarget == null) {
            throw new AssertionError();
        }
        this.fxDropTarget = null;
        this.swingDragSource = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void storeMouseEvent(MouseEvent me) {
        this.me = me;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyDragResult(TransferMode dragResult, DropTargetDragEvent e2) {
        if (dragResult == null) {
            e2.rejectDrag();
        } else {
            e2.acceptDrag(transferModeToDropAction(dragResult));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyDropResult(TransferMode dropResult, DropTargetDropEvent e2) {
        if (dropResult == null) {
            e2.rejectDrop();
        } else {
            e2.acceptDrop(transferModeToDropAction(dropResult));
        }
    }

    static TransferMode dropActionToTransferMode(int dropAction) {
        switch (dropAction) {
            case 0:
                return null;
            case 1:
                return TransferMode.COPY;
            case 2:
                return TransferMode.MOVE;
            case 1073741824:
                return TransferMode.LINK;
            default:
                throw new IllegalArgumentException();
        }
    }

    static int transferModeToDropAction(TransferMode tm) {
        switch (tm) {
            case COPY:
                return 1;
            case MOVE:
                return 2;
            case LINK:
                return 1073741824;
            default:
                throw new IllegalArgumentException();
        }
    }

    static Set<TransferMode> dropActionsToTransferModes(int dropActions) {
        Set<TransferMode> tms = EnumSet.noneOf(TransferMode.class);
        if ((dropActions & 1) != 0) {
            tms.add(TransferMode.COPY);
        }
        if ((dropActions & 2) != 0) {
            tms.add(TransferMode.MOVE);
        }
        if ((dropActions & 1073741824) != 0) {
            tms.add(TransferMode.LINK);
        }
        return Collections.unmodifiableSet(tms);
    }

    static int transferModesToDropActions(Set<TransferMode> tms) {
        int dropActions = 0;
        for (TransferMode tm : tms) {
            dropActions |= transferModeToDropAction(tm);
        }
        return dropActions;
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/SwingDnD$DnDTransferable.class */
    private final class DnDTransferable implements Transferable {
        static final /* synthetic */ boolean $assertionsDisabled;

        private DnDTransferable() {
        }

        static {
            $assertionsDisabled = !SwingDnD.class.desiredAssertionStatus();
        }

        @Override // java.awt.datatransfer.Transferable
        public Object getTransferData(DataFlavor flavor) throws UnsupportedEncodingException {
            if (!$assertionsDisabled && SwingDnD.this.fxDragSource == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !SwingUtilities.isEventDispatchThread()) {
                throw new AssertionError();
            }
            String mimeType = DataFlavorUtils.getFxMimeType(flavor);
            return DataFlavorUtils.adjustFxData(flavor, SwingDnD.this.fxDragSource.getData(mimeType));
        }

        @Override // java.awt.datatransfer.Transferable
        public DataFlavor[] getTransferDataFlavors() {
            if (!$assertionsDisabled && SwingDnD.this.fxDragSource == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !SwingUtilities.isEventDispatchThread()) {
                throw new AssertionError();
            }
            String[] mimeTypes = SwingDnD.this.fxDragSource.getMimeTypes();
            ArrayList<DataFlavor> flavors = new ArrayList<>(mimeTypes.length);
            for (String mime : mimeTypes) {
                try {
                    DataFlavor flavor = new DataFlavor(mime);
                    flavors.add(flavor);
                } catch (ClassNotFoundException e2) {
                }
            }
            return (DataFlavor[]) flavors.toArray(new DataFlavor[0]);
        }

        @Override // java.awt.datatransfer.Transferable
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            if (!$assertionsDisabled && SwingDnD.this.fxDragSource == null) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || SwingUtilities.isEventDispatchThread()) {
                return SwingDnD.this.fxDragSource.isMimeTypeAvailable(DataFlavorUtils.getFxMimeType(flavor));
            }
            throw new AssertionError();
        }
    }
}
