package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.Logging;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Cell;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Duration;
import sun.util.logging.PlatformLogger;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/VirtualFlow.class */
public class VirtualFlow<T extends IndexedCell> extends Region {
    private static final int MIN_SCROLLING_LINES_PER_PAGE = 8;
    private BooleanProperty vertical;
    private int cellCount;
    private double position;
    private Callback<VirtualFlow, T> createCell;
    private double maxPrefBreadth;
    private double viewportBreadth;
    private double viewportLength;
    boolean lastVertical;
    double lastPosition;
    T accumCell;
    Group accumCellParent;
    final Group sheet;
    final ObservableList<Node> sheetChildren;
    ClippedContainer clipView;
    StackPane corner;
    private double lastX;
    private double lastY;
    private static final String NEW_CELL = "newcell";
    private static final double GOLDEN_RATIO_MULTIPLIER = 0.618033987d;
    Timeline sbTouchTimeline;
    KeyFrame sbTouchKF1;
    KeyFrame sbTouchKF2;
    private boolean needBreadthBar;
    private boolean needLengthBar;
    static final /* synthetic */ boolean $assertionsDisabled;
    private boolean touchDetected = false;
    private boolean mouseDown = false;
    private boolean pannable = true;
    private double fixedCellSize = 0.0d;
    private boolean fixedCellSizeEnabled = false;
    double lastWidth = -1.0d;
    double lastHeight = -1.0d;
    int lastCellCount = 0;
    double lastCellBreadth = -1.0d;
    double lastCellLength = -1.0d;
    final ArrayLinkedList<T> cells = new ArrayLinkedList<>();
    final ArrayLinkedList<T> pile = new ArrayLinkedList<>();
    private VirtualScrollBar hbar = new VirtualScrollBar(this);
    private VirtualScrollBar vbar = new VirtualScrollBar(this);
    private boolean isPanning = false;
    private final List<T> privateCells = new ArrayList();
    private boolean needsReconfigureCells = false;
    private boolean needsRecreateCells = false;
    private boolean needsRebuildCells = false;
    private boolean needsCellsLayout = false;
    private boolean sizeChanged = false;
    private final BitSet dirtyCells = new BitSet();
    private boolean tempVisibility = false;

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
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$802(com.sun.javafx.scene.control.skin.VirtualFlow r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.lastX = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.control.skin.VirtualFlow.access$802(com.sun.javafx.scene.control.skin.VirtualFlow, double):double");
    }

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
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$902(com.sun.javafx.scene.control.skin.VirtualFlow r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.lastY = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.control.skin.VirtualFlow.access$902(com.sun.javafx.scene.control.skin.VirtualFlow, double):double");
    }

    static {
        $assertionsDisabled = !VirtualFlow.class.desiredAssertionStatus();
    }

    public final void setVertical(boolean value) {
        verticalProperty().set(value);
    }

    public final boolean isVertical() {
        if (this.vertical == null) {
            return true;
        }
        return this.vertical.get();
    }

    public final BooleanProperty verticalProperty() {
        if (this.vertical == null) {
            this.vertical = new BooleanPropertyBase(true) { // from class: com.sun.javafx.scene.control.skin.VirtualFlow.1
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    VirtualFlow.this.pile.clear();
                    VirtualFlow.this.sheetChildren.clear();
                    VirtualFlow.this.cells.clear();
                    VirtualFlow virtualFlow = VirtualFlow.this;
                    VirtualFlow.this.lastHeight = -1.0d;
                    virtualFlow.lastWidth = -1.0d;
                    VirtualFlow.this.setMaxPrefBreadth(-1.0d);
                    VirtualFlow.this.setViewportBreadth(0.0d);
                    VirtualFlow.this.setViewportLength(0.0d);
                    VirtualFlow.this.lastPosition = 0.0d;
                    VirtualFlow.this.hbar.setValue(0.0d);
                    VirtualFlow.this.vbar.setValue(0.0d);
                    VirtualFlow.this.setPosition(0.0d);
                    VirtualFlow.this.setNeedsLayout(true);
                    VirtualFlow.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return VirtualFlow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "vertical";
                }
            };
        }
        return this.vertical;
    }

    public boolean isPannable() {
        return this.pannable;
    }

    public void setPannable(boolean value) {
        this.pannable = value;
    }

    public int getCellCount() {
        return this.cellCount;
    }

    public void setCellCount(int i2) {
        int oldCount = this.cellCount;
        this.cellCount = i2;
        boolean countChanged = oldCount != this.cellCount;
        if (countChanged) {
            VirtualScrollBar lengthBar = isVertical() ? this.vbar : this.hbar;
            lengthBar.setMax(i2);
        }
        if (countChanged) {
            layoutChildren();
            this.sheetChildren.clear();
            Parent parent = getParent();
            if (parent != null) {
                parent.requestLayout();
            }
        }
    }

    public double getPosition() {
        return this.position;
    }

    public void setPosition(double newPosition) {
        boolean needsUpdate = this.position != newPosition;
        this.position = com.sun.javafx.util.Utils.clamp(0.0d, newPosition, 1.0d);
        if (needsUpdate) {
            requestLayout();
        }
    }

    public void setFixedCellSize(double value) {
        this.fixedCellSize = value;
        this.fixedCellSizeEnabled = this.fixedCellSize > 0.0d;
        this.needsCellsLayout = true;
        layoutChildren();
    }

    public Callback<VirtualFlow, T> getCreateCell() {
        return this.createCell;
    }

    public void setCreateCell(Callback<VirtualFlow, T> cc) {
        this.createCell = cc;
        if (this.createCell != null) {
            this.accumCell = null;
            setNeedsLayout(true);
            recreateCells();
            if (getParent() != null) {
                getParent().requestLayout();
            }
        }
    }

    protected final void setMaxPrefBreadth(double value) {
        this.maxPrefBreadth = value;
    }

    protected final double getMaxPrefBreadth() {
        return this.maxPrefBreadth;
    }

    protected final void setViewportBreadth(double value) {
        this.viewportBreadth = value;
    }

    protected final double getViewportBreadth() {
        return this.viewportBreadth;
    }

    void setViewportLength(double value) {
        this.viewportLength = value;
    }

    protected double getViewportLength() {
        return this.viewportLength;
    }

    protected List<T> getCells() {
        return this.cells;
    }

    protected final VirtualScrollBar getHbar() {
        return this.hbar;
    }

    protected final VirtualScrollBar getVbar() {
        return this.vbar;
    }

    public VirtualFlow() {
        getStyleClass().add("virtual-flow");
        setId("virtual-flow");
        this.sheet = new Group();
        this.sheet.getStyleClass().add("sheet");
        this.sheet.setAutoSizeChildren(false);
        this.sheetChildren = this.sheet.getChildren();
        this.clipView = new ClippedContainer(this);
        this.clipView.setNode(this.sheet);
        getChildren().add(this.clipView);
        this.accumCellParent = new Group();
        this.accumCellParent.setVisible(false);
        getChildren().add(this.accumCellParent);
        EventDispatcher blockEventDispatcher = (event, tail) -> {
            return event;
        };
        EventDispatcher oldHsbEventDispatcher = this.hbar.getEventDispatcher();
        this.hbar.setEventDispatcher((event2, tail2) -> {
            if (event2.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent) event2).isDirect()) {
                return tail2.prepend(blockEventDispatcher).prepend(oldHsbEventDispatcher).dispatchEvent(event2);
            }
            return oldHsbEventDispatcher.dispatchEvent(event2, tail2);
        });
        EventDispatcher oldVsbEventDispatcher = this.vbar.getEventDispatcher();
        this.vbar.setEventDispatcher((event3, tail3) -> {
            if (event3.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent) event3).isDirect()) {
                return tail3.prepend(blockEventDispatcher).prepend(oldVsbEventDispatcher).dispatchEvent(event3);
            }
            return oldVsbEventDispatcher.dispatchEvent(event3, tail3);
        });
        setOnScroll(new EventHandler<ScrollEvent>() { // from class: com.sun.javafx.scene.control.skin.VirtualFlow.2
            @Override // javafx.event.EventHandler
            public void handle(ScrollEvent event4) {
                double lineSize;
                if (BehaviorSkinBase.IS_TOUCH_SUPPORTED && !VirtualFlow.this.touchDetected && !VirtualFlow.this.mouseDown) {
                    VirtualFlow.this.startSBReleasedAnimation();
                }
                double virtualDelta = 0.0d;
                if (VirtualFlow.this.isVertical()) {
                    switch (AnonymousClass5.$SwitchMap$javafx$scene$input$ScrollEvent$VerticalTextScrollUnits[event4.getTextDeltaYUnits().ordinal()]) {
                        case 1:
                            virtualDelta = event4.getTextDeltaY() * VirtualFlow.this.lastHeight;
                            break;
                        case 2:
                            if (VirtualFlow.this.fixedCellSizeEnabled) {
                                lineSize = VirtualFlow.this.fixedCellSize;
                            } else {
                                T lastCell = VirtualFlow.this.cells.getLast();
                                lineSize = ((VirtualFlow.this.getCellPosition(lastCell) + VirtualFlow.this.getCellLength((VirtualFlow) lastCell)) - VirtualFlow.this.getCellPosition(VirtualFlow.this.cells.getFirst())) / VirtualFlow.this.cells.size();
                            }
                            if (VirtualFlow.this.lastHeight / lineSize < 8.0d) {
                                lineSize = VirtualFlow.this.lastHeight / 8.0d;
                            }
                            virtualDelta = event4.getTextDeltaY() * lineSize;
                            break;
                        case 3:
                            virtualDelta = event4.getDeltaY();
                            break;
                    }
                } else {
                    switch (AnonymousClass5.$SwitchMap$javafx$scene$input$ScrollEvent$HorizontalTextScrollUnits[event4.getTextDeltaXUnits().ordinal()]) {
                        case 1:
                        case 2:
                            double dx = event4.getDeltaX();
                            double dy = event4.getDeltaY();
                            virtualDelta = Math.abs(dx) > Math.abs(dy) ? dx : dy;
                            break;
                    }
                }
                if (virtualDelta != 0.0d) {
                    double result = VirtualFlow.this.adjustPixels(-virtualDelta);
                    if (result != 0.0d) {
                        event4.consume();
                    }
                }
                ScrollBar nonVirtualBar = VirtualFlow.this.isVertical() ? VirtualFlow.this.hbar : VirtualFlow.this.vbar;
                if (VirtualFlow.this.needBreadthBar) {
                    double nonVirtualDelta = VirtualFlow.this.isVertical() ? event4.getDeltaX() : event4.getDeltaY();
                    if (nonVirtualDelta != 0.0d) {
                        double newValue = nonVirtualBar.getValue() - nonVirtualDelta;
                        if (newValue < nonVirtualBar.getMin()) {
                            nonVirtualBar.setValue(nonVirtualBar.getMin());
                        } else if (newValue > nonVirtualBar.getMax()) {
                            nonVirtualBar.setValue(nonVirtualBar.getMax());
                        } else {
                            nonVirtualBar.setValue(newValue);
                        }
                        event4.consume();
                    }
                }
            }
        });
        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() { // from class: com.sun.javafx.scene.control.skin.VirtualFlow.3
            /* JADX WARN: Failed to check method for inline after forced processcom.sun.javafx.scene.control.skin.VirtualFlow.access$802(com.sun.javafx.scene.control.skin.VirtualFlow, double):double */
            /* JADX WARN: Failed to check method for inline after forced processcom.sun.javafx.scene.control.skin.VirtualFlow.access$902(com.sun.javafx.scene.control.skin.VirtualFlow, double):double */
            @Override // javafx.event.EventHandler
            public void handle(MouseEvent e2) {
                VirtualFlow.this.mouseDown = true;
                if (BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                    VirtualFlow.this.scrollBarOn();
                }
                if (VirtualFlow.this.isFocusTraversable()) {
                    boolean doFocusRequest = true;
                    Node focusOwner = VirtualFlow.this.getScene().getFocusOwner();
                    if (focusOwner != null) {
                        Parent parent = focusOwner.getParent();
                        while (true) {
                            Parent parent2 = parent;
                            if (parent2 == null) {
                                break;
                            }
                            if (parent2.equals(VirtualFlow.this)) {
                                doFocusRequest = false;
                                break;
                            }
                            parent = parent2.getParent();
                        }
                    }
                    if (doFocusRequest) {
                        VirtualFlow.this.requestFocus();
                    }
                }
                VirtualFlow.access$802(VirtualFlow.this, e2.getX());
                VirtualFlow.access$902(VirtualFlow.this, e2.getY());
                VirtualFlow.this.isPanning = (VirtualFlow.this.vbar.getBoundsInParent().contains(e2.getX(), e2.getY()) || VirtualFlow.this.hbar.getBoundsInParent().contains(e2.getX(), e2.getY())) ? false : true;
            }
        });
        addEventFilter(MouseEvent.MOUSE_RELEASED, e2 -> {
            this.mouseDown = false;
            if (BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                startSBReleasedAnimation();
            }
        });
        addEventFilter(MouseEvent.MOUSE_DRAGGED, e3 -> {
            if (BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                scrollBarOn();
            }
            if (this.isPanning && isPannable()) {
                double xDelta = this.lastX - e3.getX();
                double yDelta = this.lastY - e3.getY();
                double virtualDelta = isVertical() ? yDelta : xDelta;
                double actual = adjustPixels(virtualDelta);
                if (actual != 0.0d) {
                    if (isVertical()) {
                        this.lastY = e3.getY();
                    } else {
                        this.lastX = e3.getX();
                    }
                }
                double nonVirtualDelta = isVertical() ? xDelta : yDelta;
                ScrollBar nonVirtualBar = isVertical() ? this.hbar : this.vbar;
                if (nonVirtualBar.isVisible()) {
                    double newValue = nonVirtualBar.getValue() + nonVirtualDelta;
                    if (newValue < nonVirtualBar.getMin()) {
                        nonVirtualBar.setValue(nonVirtualBar.getMin());
                        return;
                    }
                    if (newValue > nonVirtualBar.getMax()) {
                        nonVirtualBar.setValue(nonVirtualBar.getMax());
                        return;
                    }
                    nonVirtualBar.setValue(newValue);
                    if (!isVertical()) {
                        this.lastY = e3.getY();
                    } else {
                        this.lastX = e3.getX();
                    }
                }
            }
        });
        this.vbar.setOrientation(Orientation.VERTICAL);
        this.vbar.addEventHandler(MouseEvent.ANY, event4 -> {
            event4.consume();
        });
        getChildren().add(this.vbar);
        this.hbar.setOrientation(Orientation.HORIZONTAL);
        this.hbar.addEventHandler(MouseEvent.ANY, event5 -> {
            event5.consume();
        });
        getChildren().add(this.hbar);
        this.corner = new StackPane();
        this.corner.getStyleClass().setAll("corner");
        getChildren().add(this.corner);
        InvalidationListener listenerX = valueModel -> {
            updateHbar();
        };
        verticalProperty().addListener(listenerX);
        this.hbar.valueProperty().addListener(listenerX);
        this.hbar.visibleProperty().addListener(listenerX);
        ChangeListener<Number> listenerY = (ov, t2, t1) -> {
            this.clipView.setClipY(isVertical() ? 0.0d : this.vbar.getValue());
        };
        this.vbar.valueProperty().addListener(listenerY);
        super.heightProperty().addListener((observable, oldHeight, newHeight) -> {
            if (oldHeight.doubleValue() == 0.0d && newHeight.doubleValue() > 0.0d) {
                recreateCells();
            }
        });
        setOnTouchPressed(e4 -> {
            this.touchDetected = true;
            scrollBarOn();
        });
        setOnTouchReleased(e5 -> {
            this.touchDetected = false;
            startSBReleasedAnimation();
        });
        setImpl_traversalEngine(new ParentTraversalEngine(this, new Algorithm() { // from class: com.sun.javafx.scene.control.skin.VirtualFlow.4
            Node selectNextAfterIndex(int index, TraversalContext context) {
                Node n2;
                do {
                    index++;
                    IndexedCell visibleCell = VirtualFlow.this.getVisibleCell(index);
                    if (visibleCell != null) {
                        if (visibleCell.isFocusTraversable()) {
                            return visibleCell;
                        }
                        n2 = context.selectFirstInParent(visibleCell);
                    } else {
                        return null;
                    }
                } while (n2 == null);
                return n2;
            }

            Node selectPreviousBeforeIndex(int index, TraversalContext context) {
                IndexedCell visibleCell;
                do {
                    index--;
                    visibleCell = VirtualFlow.this.getVisibleCell(index);
                    if (visibleCell != null) {
                        Node prev = context.selectLastInParent(visibleCell);
                        if (prev != null) {
                            return prev;
                        }
                    } else {
                        return null;
                    }
                } while (!visibleCell.isFocusTraversable());
                return visibleCell;
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node select(Node owner, Direction dir, TraversalContext context) {
                IndexedCell indexedCellFindOwnerCell;
                if (VirtualFlow.this.cells.isEmpty()) {
                    return null;
                }
                if (VirtualFlow.this.cells.contains(owner)) {
                    indexedCellFindOwnerCell = (IndexedCell) owner;
                } else {
                    indexedCellFindOwnerCell = findOwnerCell(owner);
                    Node next = context.selectInSubtree(indexedCellFindOwnerCell, owner, dir);
                    if (next != null) {
                        return next;
                    }
                    if (dir == Direction.NEXT) {
                        dir = Direction.NEXT_IN_LINE;
                    }
                }
                int cellIndex = indexedCellFindOwnerCell.getIndex();
                switch (AnonymousClass5.$SwitchMap$com$sun$javafx$scene$traversal$Direction[dir.ordinal()]) {
                    case 1:
                        return selectPreviousBeforeIndex(cellIndex, context);
                    case 2:
                        Node n2 = context.selectFirstInParent(indexedCellFindOwnerCell);
                        if (n2 != null) {
                            return n2;
                        }
                        break;
                    case 3:
                        break;
                    default:
                        return null;
                }
                return selectNextAfterIndex(cellIndex, context);
            }

            private T findOwnerCell(Node owner) {
                Parent parent = owner.getParent();
                while (true) {
                    Parent p2 = parent;
                    if (!VirtualFlow.this.cells.contains(p2)) {
                        parent = p2.getParent();
                    } else {
                        return (T) p2;
                    }
                }
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node selectFirst(TraversalContext context) {
                T firstCell = VirtualFlow.this.cells.getFirst();
                if (firstCell == null) {
                    return null;
                }
                if (firstCell.isFocusTraversable()) {
                    return firstCell;
                }
                Node n2 = context.selectFirstInParent(firstCell);
                if (n2 != null) {
                    return n2;
                }
                return selectNextAfterIndex(firstCell.getIndex(), context);
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node selectLast(TraversalContext context) {
                T lastCell = VirtualFlow.this.cells.getLast();
                if (lastCell == null) {
                    return null;
                }
                Node p2 = context.selectLastInParent(lastCell);
                if (p2 != null) {
                    return p2;
                }
                return lastCell.isFocusTraversable() ? lastCell : selectPreviousBeforeIndex(lastCell.getIndex(), context);
            }
        }));
    }

    /* renamed from: com.sun.javafx.scene.control.skin.VirtualFlow$5, reason: invalid class name */
    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/VirtualFlow$5.class */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$javafx$scene$input$ScrollEvent$VerticalTextScrollUnits;
        static final /* synthetic */ int[] $SwitchMap$javafx$scene$input$ScrollEvent$HorizontalTextScrollUnits;
        static final /* synthetic */ int[] $SwitchMap$com$sun$javafx$scene$traversal$Direction = new int[Direction.values().length];

        static {
            try {
                $SwitchMap$com$sun$javafx$scene$traversal$Direction[Direction.PREVIOUS.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$sun$javafx$scene$traversal$Direction[Direction.NEXT.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$sun$javafx$scene$traversal$Direction[Direction.NEXT_IN_LINE.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$javafx$scene$input$ScrollEvent$HorizontalTextScrollUnits = new int[ScrollEvent.HorizontalTextScrollUnits.values().length];
            try {
                $SwitchMap$javafx$scene$input$ScrollEvent$HorizontalTextScrollUnits[ScrollEvent.HorizontalTextScrollUnits.CHARACTERS.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$javafx$scene$input$ScrollEvent$HorizontalTextScrollUnits[ScrollEvent.HorizontalTextScrollUnits.NONE.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            $SwitchMap$javafx$scene$input$ScrollEvent$VerticalTextScrollUnits = new int[ScrollEvent.VerticalTextScrollUnits.values().length];
            try {
                $SwitchMap$javafx$scene$input$ScrollEvent$VerticalTextScrollUnits[ScrollEvent.VerticalTextScrollUnits.PAGES.ordinal()] = 1;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$javafx$scene$input$ScrollEvent$VerticalTextScrollUnits[ScrollEvent.VerticalTextScrollUnits.LINES.ordinal()] = 2;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$javafx$scene$input$ScrollEvent$VerticalTextScrollUnits[ScrollEvent.VerticalTextScrollUnits.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    void updateHbar() {
        if (isVisible() && getScene() != null && isVertical()) {
            if (this.hbar.isVisible()) {
                this.clipView.setClipX(this.hbar.getValue());
            } else {
                this.clipView.setClipX(0.0d);
                this.hbar.setValue(0.0d);
            }
        }
    }

    @Override // javafx.scene.Parent
    public void requestLayout() {
        setNeedsLayout(true);
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        if (this.needsRecreateCells) {
            this.lastWidth = -1.0d;
            this.lastHeight = -1.0d;
            releaseCell(this.accumCell);
            this.sheet.getChildren().clear();
            int max = this.cells.size();
            for (int i2 = 0; i2 < max; i2++) {
                this.cells.get(i2).updateIndex(-1);
            }
            this.cells.clear();
            this.pile.clear();
            releaseAllPrivateCells();
        } else if (this.needsRebuildCells) {
            this.lastWidth = -1.0d;
            this.lastHeight = -1.0d;
            releaseCell(this.accumCell);
            for (int i3 = 0; i3 < this.cells.size(); i3++) {
                this.cells.get(i3).updateIndex(-1);
            }
            addAllToPile();
            releaseAllPrivateCells();
        } else if (this.needsReconfigureCells) {
            setMaxPrefBreadth(-1.0d);
            this.lastWidth = -1.0d;
            this.lastHeight = -1.0d;
        }
        if (!this.dirtyCells.isEmpty()) {
            int cellsSize = this.cells.size();
            while (true) {
                int index = this.dirtyCells.nextSetBit(0);
                if (index == -1 || index >= cellsSize) {
                    break;
                }
                T cell = this.cells.get(index);
                if (cell != null) {
                    cell.requestLayout();
                }
                this.dirtyCells.clear(index);
            }
            setMaxPrefBreadth(-1.0d);
            this.lastWidth = -1.0d;
            this.lastHeight = -1.0d;
        }
        boolean hasSizeChange = this.sizeChanged;
        boolean recreatedOrRebuilt = this.needsRebuildCells || this.needsRecreateCells || this.sizeChanged;
        this.needsRecreateCells = false;
        this.needsReconfigureCells = false;
        this.needsRebuildCells = false;
        this.sizeChanged = false;
        if (this.needsCellsLayout) {
            int max2 = this.cells.size();
            for (int i4 = 0; i4 < max2; i4++) {
                Cell<?> cell2 = this.cells.get(i4);
                if (cell2 != null) {
                    cell2.requestLayout();
                }
            }
            this.needsCellsLayout = false;
            return;
        }
        double width = getWidth();
        double height = getHeight();
        boolean isVertical = isVertical();
        double position = getPosition();
        if (width <= 0.0d || height <= 0.0d) {
            addAllToPile();
            this.lastWidth = width;
            this.lastHeight = height;
            this.hbar.setVisible(false);
            this.vbar.setVisible(false);
            this.corner.setVisible(false);
            return;
        }
        boolean cellNeedsLayout = false;
        boolean thumbNeedsLayout = false;
        if (BehaviorSkinBase.IS_TOUCH_SUPPORTED && ((this.tempVisibility && (!this.hbar.isVisible() || !this.vbar.isVisible())) || (!this.tempVisibility && (this.hbar.isVisible() || this.vbar.isVisible())))) {
            thumbNeedsLayout = true;
        }
        if (0 == 0) {
            for (int i5 = 0; i5 < this.cells.size(); i5++) {
                cellNeedsLayout = this.cells.get(i5).isNeedsLayout();
                if (cellNeedsLayout) {
                    break;
                }
            }
        }
        IndexedCell firstVisibleCell = getFirstVisibleCell();
        if (!cellNeedsLayout && !thumbNeedsLayout) {
            boolean cellSizeChanged = false;
            if (firstVisibleCell != null) {
                double breadth = getCellBreadth(firstVisibleCell);
                double length = getCellLength((VirtualFlow<T>) firstVisibleCell);
                cellSizeChanged = (breadth == this.lastCellBreadth && length == this.lastCellLength) ? false : true;
                this.lastCellBreadth = breadth;
                this.lastCellLength = length;
            }
            if (width == this.lastWidth && height == this.lastHeight && this.cellCount == this.lastCellCount && isVertical == this.lastVertical && position == this.lastPosition && !cellSizeChanged) {
                return;
            }
        }
        boolean needTrailingCells = false;
        boolean rebuild = cellNeedsLayout || isVertical != this.lastVertical || this.cells.isEmpty() || getMaxPrefBreadth() == -1.0d || position != this.lastPosition || this.cellCount != this.lastCellCount || hasSizeChange || (isVertical && height < this.lastHeight) || (!isVertical && width < this.lastWidth);
        if (!rebuild) {
            double maxPrefBreadth = getMaxPrefBreadth();
            boolean foundMax = false;
            int i6 = 0;
            while (true) {
                if (i6 >= this.cells.size()) {
                    break;
                }
                double breadth2 = getCellBreadth(this.cells.get(i6));
                if (maxPrefBreadth != breadth2) {
                    if (breadth2 > maxPrefBreadth) {
                        rebuild = true;
                        break;
                    }
                } else {
                    foundMax = true;
                }
                i6++;
            }
            if (!foundMax) {
                rebuild = true;
            }
        }
        if (!rebuild && ((isVertical && height > this.lastHeight) || (!isVertical && width > this.lastWidth))) {
            needTrailingCells = true;
        }
        initViewport();
        int currentIndex = computeCurrentIndex();
        if (this.lastCellCount != this.cellCount) {
            if (position != 0.0d && position != 1.0d) {
                if (currentIndex >= this.cellCount) {
                    setPosition(1.0d);
                } else if (firstVisibleCell != null) {
                    double firstCellOffset = getCellPosition(firstVisibleCell);
                    int firstCellIndex = getCellIndex(firstVisibleCell);
                    adjustPositionToIndex(firstCellIndex);
                    double viewportTopToCellTop = -computeOffsetForCell(firstCellIndex);
                    adjustByPixelAmount(viewportTopToCellTop - firstCellOffset);
                }
            }
            currentIndex = computeCurrentIndex();
        }
        if (rebuild) {
            setMaxPrefBreadth(-1.0d);
            addAllToPile();
            double offset = -computeViewportOffset(getPosition());
            addLeadingCells(currentIndex, offset);
            addTrailingCells(true);
        } else if (needTrailingCells) {
            addTrailingCells(true);
        }
        computeBarVisiblity();
        updateScrollBarsAndCells(recreatedOrRebuilt);
        this.lastWidth = getWidth();
        this.lastHeight = getHeight();
        this.lastCellCount = getCellCount();
        this.lastVertical = isVertical();
        this.lastPosition = getPosition();
        cleanPile();
    }

    protected void addLeadingCells(int i2, double d2) {
        double cellLength = d2;
        int i3 = i2;
        boolean z2 = true;
        if (i3 == this.cellCount && cellLength == getViewportLength()) {
            i3--;
            z2 = false;
        }
        while (i3 >= 0 && (cellLength > 0.0d || z2)) {
            IndexedCell availableCell = getAvailableCell(i3);
            setCellIndex(availableCell, i3);
            resizeCellSize(availableCell);
            this.cells.addFirst(availableCell);
            if (z2) {
                z2 = false;
            } else {
                cellLength -= getCellLength((VirtualFlow<T>) availableCell);
            }
            positionCell(availableCell, cellLength);
            setMaxPrefBreadth(Math.max(getMaxPrefBreadth(), getCellBreadth(availableCell)));
            availableCell.setVisible(true);
            i3--;
        }
        if (this.cells.size() > 0) {
            T first = this.cells.getFirst();
            int cellIndex = getCellIndex(first);
            double cellPosition = getCellPosition(first);
            if (cellIndex == 0 && cellPosition > 0.0d) {
                setPosition(0.0d);
                double cellLength2 = 0.0d;
                for (int i4 = 0; i4 < this.cells.size(); i4++) {
                    T t2 = this.cells.get(i4);
                    positionCell(t2, cellLength2);
                    cellLength2 += getCellLength((VirtualFlow<T>) t2);
                }
                return;
            }
            return;
        }
        this.vbar.setValue(0.0d);
        this.hbar.setValue(0.0d);
    }

    protected boolean addTrailingCells(boolean z2) {
        if (this.cells.isEmpty()) {
            return false;
        }
        T last = this.cells.getLast();
        double cellPosition = getCellPosition(last) + getCellLength((VirtualFlow<T>) last);
        int cellIndex = getCellIndex(last) + 1;
        boolean z3 = cellIndex <= this.cellCount;
        double viewportLength = getViewportLength();
        if (cellPosition < 0.0d && !z2) {
            return false;
        }
        double d2 = viewportLength - cellPosition;
        while (cellPosition < viewportLength) {
            if (cellIndex >= this.cellCount) {
                if (cellPosition < viewportLength) {
                    z3 = false;
                }
                if (!z2) {
                    return z3;
                }
                if (cellIndex > d2) {
                    PlatformLogger controlsLogger = Logging.getControlsLogger();
                    if (controlsLogger.isLoggable(PlatformLogger.Level.INFO)) {
                        if (last != null) {
                            controlsLogger.info("index exceeds maxCellCount. Check size calculations for " + ((Object) last.getClass()));
                        } else {
                            controlsLogger.info("index exceeds maxCellCount");
                        }
                    }
                    return z3;
                }
            }
            IndexedCell availableCell = getAvailableCell(cellIndex);
            setCellIndex(availableCell, cellIndex);
            resizeCellSize(availableCell);
            this.cells.addLast(availableCell);
            positionCell(availableCell, cellPosition);
            setMaxPrefBreadth(Math.max(getMaxPrefBreadth(), getCellBreadth(availableCell)));
            cellPosition += getCellLength((VirtualFlow<T>) availableCell);
            availableCell.setVisible(true);
            cellIndex++;
        }
        T first = this.cells.getFirst();
        int cellIndex2 = getCellIndex(first);
        IndexedCell lastVisibleCell = getLastVisibleCell();
        double cellPosition2 = getCellPosition(first);
        double cellPosition3 = getCellPosition(lastVisibleCell) + getCellLength((VirtualFlow<T>) lastVisibleCell);
        if ((cellIndex2 != 0 || (cellIndex2 == 0 && cellPosition2 < 0.0d)) && z2 && lastVisibleCell != null && getCellIndex(lastVisibleCell) == this.cellCount - 1 && cellPosition3 < viewportLength) {
            double d3 = cellPosition3;
            double d4 = viewportLength - cellPosition3;
            while (d3 < viewportLength && cellIndex2 != 0 && (-cellPosition2) < d4) {
                cellIndex2--;
                IndexedCell availableCell2 = getAvailableCell(cellIndex2);
                setCellIndex(availableCell2, cellIndex2);
                resizeCellSize(availableCell2);
                this.cells.addFirst(availableCell2);
                double cellLength = getCellLength((VirtualFlow<T>) availableCell2);
                cellPosition2 -= cellLength;
                d3 += cellLength;
                positionCell(availableCell2, cellPosition2);
                setMaxPrefBreadth(Math.max(getMaxPrefBreadth(), getCellBreadth(availableCell2)));
                availableCell2.setVisible(true);
            }
            T first2 = this.cells.getFirst();
            double cellPosition4 = getCellPosition(first2);
            double d5 = viewportLength - cellPosition3;
            if (getCellIndex(first2) == 0 && d5 > (-cellPosition4)) {
                d5 = -cellPosition4;
            }
            for (int i2 = 0; i2 < this.cells.size(); i2++) {
                T t2 = this.cells.get(i2);
                positionCell(t2, getCellPosition(t2) + d5);
            }
            double cellPosition5 = getCellPosition(first2);
            if (getCellIndex(first2) == 0 && cellPosition5 == 0.0d) {
                setPosition(0.0d);
            } else if (getPosition() != 1.0d) {
                setPosition(1.0d);
            }
        }
        return z3;
    }

    private boolean computeBarVisiblity() {
        if (this.cells.isEmpty()) {
            this.needLengthBar = false;
            this.needBreadthBar = false;
            return true;
        }
        boolean isVertical = isVertical();
        boolean barVisibilityChanged = false;
        VirtualScrollBar breadthBar = isVertical ? this.hbar : this.vbar;
        VirtualScrollBar lengthBar = isVertical ? this.vbar : this.hbar;
        double viewportBreadth = getViewportBreadth();
        int cellsSize = this.cells.size();
        for (int i2 = 0; i2 < 2; i2++) {
            boolean lengthBarVisible = getPosition() > 0.0d || this.cellCount > cellsSize || (this.cellCount == cellsSize && getCellPosition(this.cells.getLast()) + getCellLength((VirtualFlow<T>) this.cells.getLast()) > getViewportLength()) || (this.cellCount == cellsSize - 1 && barVisibilityChanged && this.needBreadthBar);
            if (lengthBarVisible ^ this.needLengthBar) {
                this.needLengthBar = lengthBarVisible;
                barVisibilityChanged = true;
            }
            boolean breadthBarVisible = this.maxPrefBreadth > viewportBreadth;
            if (breadthBarVisible ^ this.needBreadthBar) {
                this.needBreadthBar = breadthBarVisible;
                barVisibilityChanged = true;
            }
        }
        if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
            updateViewportDimensions();
            breadthBar.setVisible(this.needBreadthBar);
            lengthBar.setVisible(this.needLengthBar);
        } else {
            breadthBar.setVisible(this.needBreadthBar && this.tempVisibility);
            lengthBar.setVisible(this.needLengthBar && this.tempVisibility);
        }
        return barVisibilityChanged;
    }

    private void updateViewportDimensions() {
        boolean isVertical = isVertical();
        double breadthBarLength = snapSize(isVertical ? this.hbar.prefHeight(-1.0d) : this.vbar.prefWidth(-1.0d));
        double lengthBarBreadth = snapSize(isVertical ? this.vbar.prefWidth(-1.0d) : this.hbar.prefHeight(-1.0d));
        setViewportBreadth((isVertical ? getWidth() : getHeight()) - (this.needLengthBar ? lengthBarBreadth : 0.0d));
        setViewportLength((isVertical ? getHeight() : getWidth()) - (this.needBreadthBar ? breadthBarLength : 0.0d));
    }

    private void initViewport() {
        boolean isVertical = isVertical();
        updateViewportDimensions();
        VirtualScrollBar breadthBar = isVertical ? this.hbar : this.vbar;
        VirtualScrollBar lengthBar = isVertical ? this.vbar : this.hbar;
        breadthBar.setVirtual(false);
        lengthBar.setVirtual(true);
    }

    @Override // javafx.scene.layout.Region
    protected void setWidth(double value) {
        if (value != this.lastWidth) {
            super.setWidth(value);
            this.sizeChanged = true;
            setNeedsLayout(true);
            requestLayout();
        }
    }

    @Override // javafx.scene.layout.Region
    protected void setHeight(double value) {
        if (value != this.lastHeight) {
            super.setHeight(value);
            this.sizeChanged = true;
            setNeedsLayout(true);
            requestLayout();
        }
    }

    private void updateScrollBarsAndCells(boolean recreate) {
        boolean isVertical = isVertical();
        VirtualScrollBar breadthBar = isVertical ? this.hbar : this.vbar;
        VirtualScrollBar lengthBar = isVertical ? this.vbar : this.hbar;
        fitCells();
        if (!this.cells.isEmpty()) {
            double currOffset = -computeViewportOffset(getPosition());
            int currIndex = computeCurrentIndex() - this.cells.getFirst().getIndex();
            int size = this.cells.size();
            double offset = currOffset;
            for (int i2 = currIndex - 1; i2 >= 0 && i2 < size; i2--) {
                T cell = this.cells.get(i2);
                offset -= getCellLength((VirtualFlow<T>) cell);
                positionCell(cell, offset);
            }
            double offset2 = currOffset;
            for (int i3 = currIndex; i3 >= 0 && i3 < size; i3++) {
                T cell2 = this.cells.get(i3);
                positionCell(cell2, offset2);
                offset2 += getCellLength((VirtualFlow<T>) cell2);
            }
        }
        this.corner.setVisible(breadthBar.isVisible() && lengthBar.isVisible());
        double sumCellLength = 0.0d;
        double flowLength = (isVertical ? getHeight() : getWidth()) - (breadthBar.isVisible() ? breadthBar.prefHeight(-1.0d) : 0.0d);
        double viewportBreadth = getViewportBreadth();
        double viewportLength = getViewportLength();
        if (breadthBar.isVisible()) {
            if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                if (isVertical) {
                    this.hbar.resizeRelocate(0.0d, viewportLength, viewportBreadth, this.hbar.prefHeight(viewportBreadth));
                } else {
                    this.vbar.resizeRelocate(viewportLength, 0.0d, this.vbar.prefWidth(viewportBreadth), viewportBreadth);
                }
            } else if (isVertical) {
                this.hbar.resizeRelocate(0.0d, viewportLength - this.hbar.getHeight(), viewportBreadth, this.hbar.prefHeight(viewportBreadth));
            } else {
                this.vbar.resizeRelocate(viewportLength - this.vbar.getWidth(), 0.0d, this.vbar.prefWidth(viewportBreadth), viewportBreadth);
            }
            if (getMaxPrefBreadth() != -1.0d) {
                double newMax = Math.max(1.0d, getMaxPrefBreadth() - viewportBreadth);
                if (newMax != breadthBar.getMax()) {
                    breadthBar.setMax(newMax);
                    double breadthBarValue = breadthBar.getValue();
                    boolean maxed = breadthBarValue != 0.0d && newMax == breadthBarValue;
                    if (maxed || breadthBarValue > newMax) {
                        breadthBar.setValue(newMax);
                    }
                    breadthBar.setVisibleAmount((viewportBreadth / getMaxPrefBreadth()) * newMax);
                }
            }
        }
        if (recreate && (lengthBar.isVisible() || BehaviorSkinBase.IS_TOUCH_SUPPORTED)) {
            int numCellsVisibleOnScreen = 0;
            int max = this.cells.size();
            for (int i4 = 0; i4 < max; i4++) {
                T cell3 = this.cells.get(i4);
                if (cell3 != null && !cell3.isEmpty()) {
                    sumCellLength += isVertical ? cell3.getHeight() : cell3.getWidth();
                    if (sumCellLength > flowLength) {
                        break;
                    } else {
                        numCellsVisibleOnScreen++;
                    }
                }
            }
            lengthBar.setMax(1.0d);
            if (numCellsVisibleOnScreen == 0 && this.cellCount == 1) {
                lengthBar.setVisibleAmount(flowLength / sumCellLength);
            } else {
                lengthBar.setVisibleAmount(numCellsVisibleOnScreen / this.cellCount);
            }
        }
        if (lengthBar.isVisible()) {
            if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                if (isVertical) {
                    this.vbar.resizeRelocate(viewportBreadth, 0.0d, this.vbar.prefWidth(viewportLength), viewportLength);
                } else {
                    this.hbar.resizeRelocate(0.0d, viewportBreadth, viewportLength, this.hbar.prefHeight(-1.0d));
                }
            } else if (isVertical) {
                this.vbar.resizeRelocate(viewportBreadth - this.vbar.getWidth(), 0.0d, this.vbar.prefWidth(viewportLength), viewportLength);
            } else {
                this.hbar.resizeRelocate(0.0d, viewportBreadth - this.hbar.getHeight(), viewportLength, this.hbar.prefHeight(-1.0d));
            }
        }
        if (this.corner.isVisible()) {
            if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
                this.corner.resize(this.vbar.getWidth(), this.hbar.getHeight());
                this.corner.relocate(this.hbar.getLayoutX() + this.hbar.getWidth(), this.vbar.getLayoutY() + this.vbar.getHeight());
            } else {
                this.corner.resize(this.vbar.getWidth(), this.hbar.getHeight());
                this.corner.relocate(this.hbar.getLayoutX() + (this.hbar.getWidth() - this.vbar.getWidth()), this.vbar.getLayoutY() + (this.vbar.getHeight() - this.hbar.getHeight()));
                this.hbar.resize(this.hbar.getWidth() - this.vbar.getWidth(), this.hbar.getHeight());
                this.vbar.resize(this.vbar.getWidth(), this.vbar.getHeight() - this.hbar.getHeight());
            }
        }
        this.clipView.resize(snapSize(isVertical ? viewportBreadth : viewportLength), snapSize(isVertical ? viewportLength : viewportBreadth));
        if (getPosition() != lengthBar.getValue()) {
            lengthBar.setValue(getPosition());
        }
    }

    private void fitCells() {
        double size = Math.max(getMaxPrefBreadth(), getViewportBreadth());
        boolean isVertical = isVertical();
        for (int i2 = 0; i2 < this.cells.size(); i2++) {
            Cell<?> cell = this.cells.get(i2);
            if (isVertical) {
                cell.resize(size, cell.prefHeight(size));
            } else {
                cell.resize(cell.prefWidth(size), size);
            }
        }
    }

    private void cull() {
        double viewportLength = getViewportLength();
        for (int i2 = this.cells.size() - 1; i2 >= 0; i2--) {
            T cell = this.cells.get(i2);
            double cellSize = getCellLength((VirtualFlow<T>) cell);
            double cellStart = getCellPosition(cell);
            double cellEnd = cellStart + cellSize;
            if (cellStart >= viewportLength || cellEnd < 0.0d) {
                addToPile(this.cells.remove(i2));
            }
        }
    }

    protected int getCellIndex(T cell) {
        return cell.getIndex();
    }

    public T getCell(int i2) {
        Callback<VirtualFlow, T> createCell;
        T t2;
        if (!this.cells.isEmpty() && (t2 = (T) getVisibleCell(i2)) != null) {
            return t2;
        }
        for (int i3 = 0; i3 < this.pile.size(); i3++) {
            T t3 = this.pile.get(i3);
            if (getCellIndex(t3) == i2) {
                return t3;
            }
        }
        if (this.pile.size() > 0) {
            return this.pile.get(0);
        }
        if (this.accumCell == null && (createCell = getCreateCell()) != null) {
            this.accumCell = createCell.call(this);
            this.accumCell.getProperties().put(NEW_CELL, null);
            this.accumCellParent.getChildren().setAll(this.accumCell);
            this.accumCell.setAccessibleRole(AccessibleRole.NODE);
            this.accumCell.getChildrenUnmodifiable().addListener(c2 -> {
                for (Node n2 : this.accumCell.getChildrenUnmodifiable()) {
                    n2.setAccessibleRole(AccessibleRole.NODE);
                }
            });
        }
        setCellIndex(this.accumCell, i2);
        resizeCellSize(this.accumCell);
        return this.accumCell;
    }

    private void releaseCell(T cell) {
        if (this.accumCell != null && cell == this.accumCell) {
            this.accumCell.updateIndex(-1);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v33, types: [javafx.scene.control.IndexedCell] */
    T getPrivateCell(int index) {
        Callback<VirtualFlow, T> createCell;
        T cell = null;
        if (!this.cells.isEmpty()) {
            cell = getVisibleCell(index);
            if (cell != null) {
                cell.layout();
                return cell;
            }
        }
        if (cell == null) {
            for (int i2 = 0; i2 < this.sheetChildren.size(); i2++) {
                T t2 = (T) this.sheetChildren.get(i2);
                if (getCellIndex(t2) == index) {
                    return t2;
                }
            }
        }
        if (cell == null && (createCell = getCreateCell()) != null) {
            cell = createCell.call(this);
        }
        if (cell != null) {
            setCellIndex(cell, index);
            resizeCellSize(cell);
            cell.setVisible(false);
            this.sheetChildren.add(cell);
            this.privateCells.add(cell);
        }
        return cell;
    }

    private void releaseAllPrivateCells() {
        this.sheetChildren.removeAll(this.privateCells);
        this.privateCells.clear();
    }

    protected double getCellLength(int index) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        IndexedCell cell = getCell(index);
        double length = getCellLength((VirtualFlow<T>) cell);
        releaseCell(cell);
        return length;
    }

    protected double getCellBreadth(int index) {
        IndexedCell cell = getCell(index);
        double b2 = getCellBreadth(cell);
        releaseCell(cell);
        return b2;
    }

    protected double getCellLength(T cell) {
        if (cell == null) {
            return 0.0d;
        }
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        if (isVertical()) {
            return cell.getLayoutBounds().getHeight();
        }
        return cell.getLayoutBounds().getWidth();
    }

    protected double getCellBreadth(Cell cell) {
        if (isVertical()) {
            return cell.prefWidth(-1.0d);
        }
        return cell.prefHeight(-1.0d);
    }

    protected double getCellPosition(T cell) {
        if (cell == null) {
            return 0.0d;
        }
        if (isVertical()) {
            return cell.getLayoutY();
        }
        return cell.getLayoutX();
    }

    protected void positionCell(T cell, double position) {
        if (isVertical()) {
            cell.setLayoutX(0.0d);
            cell.setLayoutY(snapSpace(position));
        } else {
            cell.setLayoutX(snapSpace(position));
            cell.setLayoutY(0.0d);
        }
    }

    protected void resizeCellSize(T cell) {
        if (cell == null) {
            return;
        }
        if (isVertical()) {
            double width = Math.max(getMaxPrefBreadth(), getViewportBreadth());
            cell.resize(width, this.fixedCellSizeEnabled ? this.fixedCellSize : Utils.boundedSize(cell.prefHeight(width), cell.minHeight(width), cell.maxHeight(width)));
        } else {
            double height = Math.max(getMaxPrefBreadth(), getViewportBreadth());
            cell.resize(this.fixedCellSizeEnabled ? this.fixedCellSize : Utils.boundedSize(cell.prefWidth(height), cell.minWidth(height), cell.maxWidth(height)), height);
        }
    }

    protected void setCellIndex(T cell, int index) {
        if (!$assertionsDisabled && cell == null) {
            throw new AssertionError();
        }
        cell.updateIndex(index);
        if ((cell.isNeedsLayout() && cell.getScene() != null) || cell.getProperties().containsKey(NEW_CELL)) {
            cell.applyCss();
            cell.getProperties().remove(NEW_CELL);
        }
    }

    protected T getAvailableCell(int prefIndex) {
        T cell = null;
        int i2 = 0;
        int max = this.pile.size();
        while (true) {
            if (i2 >= max) {
                break;
            }
            T _cell = this.pile.get(i2);
            if (!$assertionsDisabled && _cell == null) {
                throw new AssertionError();
            }
            if (getCellIndex(_cell) == prefIndex) {
                cell = _cell;
                this.pile.remove(i2);
                break;
            }
            cell = null;
            i2++;
        }
        if (cell == null) {
            if (this.pile.size() > 0) {
                boolean prefIndexIsEven = (prefIndex & 1) == 0;
                int i3 = 0;
                int max2 = this.pile.size();
                while (true) {
                    if (i3 >= max2) {
                        break;
                    }
                    T c2 = this.pile.get(i3);
                    int cellIndex = getCellIndex(c2);
                    if ((cellIndex & 1) == 0 && prefIndexIsEven) {
                        cell = c2;
                        this.pile.remove(i3);
                        break;
                    }
                    if ((cellIndex & 1) != 1 || prefIndexIsEven) {
                        i3++;
                    } else {
                        cell = c2;
                        this.pile.remove(i3);
                        break;
                    }
                }
                if (cell == null) {
                    cell = this.pile.removeFirst();
                }
            } else {
                cell = getCreateCell().call(this);
                cell.getProperties().put(NEW_CELL, null);
            }
        }
        if (cell.getParent() == null) {
            this.sheetChildren.add(cell);
        }
        return cell;
    }

    protected void addAllToPile() {
        int max = this.cells.size();
        for (int i2 = 0; i2 < max; i2++) {
            addToPile(this.cells.removeFirst());
        }
    }

    private void addToPile(T cell) {
        if (!$assertionsDisabled && cell == null) {
            throw new AssertionError();
        }
        this.pile.addLast(cell);
    }

    private void cleanPile() {
        boolean wasFocusOwner = false;
        int max = this.pile.size();
        for (int i2 = 0; i2 < max; i2++) {
            T cell = this.pile.get(i2);
            wasFocusOwner = wasFocusOwner || doesCellContainFocus(cell);
            cell.setVisible(false);
        }
        if (wasFocusOwner) {
            requestFocus();
        }
    }

    private boolean doesCellContainFocus(Cell<?> c2) {
        Scene scene = c2.getScene();
        Node focusOwner = scene == null ? null : scene.getFocusOwner();
        if (focusOwner != null) {
            if (c2.equals(focusOwner)) {
                return true;
            }
            Parent parent = focusOwner.getParent();
            while (true) {
                Parent p2 = parent;
                if (p2 != null && !(p2 instanceof VirtualFlow)) {
                    if (c2.equals(p2)) {
                        return true;
                    }
                    parent = p2.getParent();
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public T getVisibleCell(int index) {
        if (this.cells.isEmpty()) {
            return null;
        }
        T lastCell = this.cells.getLast();
        int lastIndex = getCellIndex(lastCell);
        if (index == lastIndex) {
            return lastCell;
        }
        T firstCell = this.cells.getFirst();
        int firstIndex = getCellIndex(firstCell);
        if (index == firstIndex) {
            return firstCell;
        }
        if (index > firstIndex && index < lastIndex) {
            T cell = this.cells.get(index - firstIndex);
            if (getCellIndex(cell) == index) {
                return cell;
            }
            return null;
        }
        return null;
    }

    public T getLastVisibleCell() {
        if (this.cells.isEmpty() || getViewportLength() <= 0.0d) {
            return null;
        }
        for (int i2 = this.cells.size() - 1; i2 >= 0; i2--) {
            T cell = this.cells.get(i2);
            if (!cell.isEmpty()) {
                return cell;
            }
        }
        return null;
    }

    public T getFirstVisibleCell() {
        if (this.cells.isEmpty() || getViewportLength() <= 0.0d) {
            return null;
        }
        T cell = this.cells.getFirst();
        if (cell.isEmpty()) {
            return null;
        }
        return cell;
    }

    public T getLastVisibleCellWithinViewPort() {
        if (this.cells.isEmpty() || getViewportLength() <= 0.0d) {
            return null;
        }
        double max = getViewportLength();
        for (int i2 = this.cells.size() - 1; i2 >= 0; i2--) {
            T cell = this.cells.get(i2);
            if (!cell.isEmpty()) {
                double cellStart = getCellPosition(cell);
                double cellEnd = cellStart + getCellLength((VirtualFlow<T>) cell);
                if (cellEnd <= max + 2.0d) {
                    return cell;
                }
            }
        }
        return null;
    }

    public T getFirstVisibleCellWithinViewPort() {
        if (this.cells.isEmpty() || getViewportLength() <= 0.0d) {
            return null;
        }
        for (int i2 = 0; i2 < this.cells.size(); i2++) {
            T cell = this.cells.get(i2);
            if (!cell.isEmpty()) {
                double cellStart = getCellPosition(cell);
                if (cellStart >= 0.0d) {
                    return cell;
                }
            }
        }
        return null;
    }

    public void showAsFirst(T firstCell) {
        if (firstCell != null) {
            adjustPixels(getCellPosition(firstCell));
        }
    }

    public void showAsLast(T lastCell) {
        if (lastCell != null) {
            adjustPixels((getCellPosition(lastCell) + getCellLength((VirtualFlow<T>) lastCell)) - getViewportLength());
        }
    }

    public void show(T cell) {
        if (cell != null) {
            double start = getCellPosition(cell);
            double length = getCellLength((VirtualFlow<T>) cell);
            double end = start + length;
            double viewportLength = getViewportLength();
            if (start < 0.0d) {
                adjustPixels(start);
            } else if (end > viewportLength) {
                adjustPixels(end - viewportLength);
            }
        }
    }

    public void show(int i2) {
        IndexedCell visibleCell = getVisibleCell(i2);
        if (visibleCell != null) {
            show((VirtualFlow<T>) visibleCell);
            return;
        }
        IndexedCell visibleCell2 = getVisibleCell(i2 - 1);
        if (visibleCell2 != null) {
            IndexedCell availableCell = getAvailableCell(i2);
            setCellIndex(availableCell, i2);
            resizeCellSize(availableCell);
            this.cells.addLast(availableCell);
            positionCell(availableCell, getCellPosition(visibleCell2) + getCellLength((VirtualFlow<T>) visibleCell2));
            setMaxPrefBreadth(Math.max(getMaxPrefBreadth(), getCellBreadth(availableCell)));
            availableCell.setVisible(true);
            show((VirtualFlow<T>) availableCell);
            return;
        }
        IndexedCell visibleCell3 = getVisibleCell(i2 + 1);
        if (visibleCell3 != null) {
            IndexedCell availableCell2 = getAvailableCell(i2);
            setCellIndex(availableCell2, i2);
            resizeCellSize(availableCell2);
            this.cells.addFirst(availableCell2);
            positionCell(availableCell2, getCellPosition(visibleCell3) - getCellLength((VirtualFlow<T>) availableCell2));
            setMaxPrefBreadth(Math.max(getMaxPrefBreadth(), getCellBreadth(availableCell2)));
            availableCell2.setVisible(true);
            show((VirtualFlow<T>) availableCell2);
            return;
        }
        adjustPositionToIndex(i2);
        addAllToPile();
        requestLayout();
    }

    public void scrollTo(int index) {
        boolean posSet = false;
        if (index >= this.cellCount - 1) {
            setPosition(1.0d);
            posSet = true;
        } else if (index < 0) {
            setPosition(0.0d);
            posSet = true;
        }
        if (!posSet) {
            adjustPositionToIndex(index);
            double offset = -computeOffsetForCell(index);
            adjustByPixelAmount(offset);
        }
        requestLayout();
    }

    public void scrollToOffset(int offset) {
        adjustPixels(offset * getCellLength(0));
    }

    public double adjustPixels(double delta) {
        if (delta == 0.0d) {
            return 0.0d;
        }
        boolean isVertical = isVertical();
        if (isVertical) {
            if (this.tempVisibility) {
                if (!this.needLengthBar) {
                    return 0.0d;
                }
            } else if (!this.vbar.isVisible()) {
                return 0.0d;
            }
        }
        if (!isVertical) {
            if (this.tempVisibility) {
                if (!this.needLengthBar) {
                    return 0.0d;
                }
            } else if (!this.hbar.isVisible()) {
                return 0.0d;
            }
        }
        double pos = getPosition();
        if (pos == 0.0d && delta < 0.0d) {
            return 0.0d;
        }
        if (pos == 1.0d && delta > 0.0d) {
            return 0.0d;
        }
        adjustByPixelAmount(delta);
        if (pos == getPosition()) {
            return 0.0d;
        }
        if (this.cells.size() > 0) {
            for (int i2 = 0; i2 < this.cells.size(); i2++) {
                T cell = this.cells.get(i2);
                if (!$assertionsDisabled && cell == null) {
                    throw new AssertionError();
                }
                positionCell(cell, getCellPosition(cell) - delta);
            }
            T firstCell = this.cells.getFirst();
            double layoutY = firstCell == null ? 0.0d : getCellPosition(firstCell);
            for (int i3 = 0; i3 < this.cells.size(); i3++) {
                T cell2 = this.cells.get(i3);
                if (!$assertionsDisabled && cell2 == null) {
                    throw new AssertionError();
                }
                double actualLayoutY = getCellPosition(cell2);
                if (actualLayoutY != layoutY) {
                    positionCell(cell2, layoutY);
                }
                layoutY += getCellLength((VirtualFlow<T>) cell2);
            }
            cull();
            T firstCell2 = this.cells.getFirst();
            if (firstCell2 != null) {
                int firstIndex = getCellIndex(firstCell2);
                double prevIndexSize = getCellLength(firstIndex - 1);
                addLeadingCells(firstIndex - 1, getCellPosition(firstCell2) - prevIndexSize);
            } else {
                int currentIndex = computeCurrentIndex();
                double offset = -computeViewportOffset(getPosition());
                addLeadingCells(currentIndex, offset);
            }
            if (!addTrailingCells(false)) {
                IndexedCell lastVisibleCell = getLastVisibleCell();
                double lastCellSize = getCellLength((VirtualFlow<T>) lastVisibleCell);
                double cellEnd = getCellPosition(lastVisibleCell) + lastCellSize;
                double viewportLength = getViewportLength();
                if (cellEnd < viewportLength) {
                    double emptySize = viewportLength - cellEnd;
                    for (int i4 = 0; i4 < this.cells.size(); i4++) {
                        T cell3 = this.cells.get(i4);
                        positionCell(cell3, getCellPosition(cell3) + emptySize);
                    }
                    setPosition(1.0d);
                    T firstCell3 = this.cells.getFirst();
                    int firstIndex2 = getCellIndex(firstCell3);
                    double prevIndexSize2 = getCellLength(firstIndex2 - 1);
                    addLeadingCells(firstIndex2 - 1, getCellPosition(firstCell3) - prevIndexSize2);
                }
            }
        }
        cull();
        updateScrollBarsAndCells(false);
        this.lastPosition = getPosition();
        return delta;
    }

    public void reconfigureCells() {
        this.needsReconfigureCells = true;
        requestLayout();
    }

    public void recreateCells() {
        this.needsRecreateCells = true;
        requestLayout();
    }

    public void rebuildCells() {
        this.needsRebuildCells = true;
        requestLayout();
    }

    public void requestCellLayout() {
        this.needsCellsLayout = true;
        requestLayout();
    }

    public void setCellDirty(int index) {
        this.dirtyCells.set(index);
        requestLayout();
    }

    private double getPrefBreadth(double oppDimension) {
        double max = getMaxCellWidth(10);
        if (oppDimension > -1.0d) {
            double prefLength = getPrefLength();
            max = Math.max(max, prefLength * GOLDEN_RATIO_MULTIPLIER);
        }
        return max;
    }

    private double getPrefLength() {
        double sum = 0.0d;
        int rows = Math.min(10, this.cellCount);
        for (int i2 = 0; i2 < rows; i2++) {
            sum += getCellLength(i2);
        }
        return sum;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        double w2 = isVertical() ? getPrefBreadth(height) : getPrefLength();
        return w2 + this.vbar.prefWidth(-1.0d);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        double h2 = isVertical() ? getPrefLength() : getPrefBreadth(width);
        return h2 + this.hbar.prefHeight(-1.0d);
    }

    double getMaxCellWidth(int rowsToCount) {
        double max = 0.0d;
        int rows = Math.max(1, rowsToCount == -1 ? this.cellCount : rowsToCount);
        for (int i2 = 0; i2 < rows; i2++) {
            max = Math.max(max, getCellBreadth(i2));
        }
        return max;
    }

    private double computeViewportOffset(double position) {
        double p2 = com.sun.javafx.util.Utils.clamp(0.0d, position, 1.0d);
        double fractionalPosition = p2 * getCellCount();
        int cellIndex = (int) fractionalPosition;
        double fraction = fractionalPosition - cellIndex;
        double cellSize = getCellLength(cellIndex);
        double pixelOffset = cellSize * fraction;
        double viewportOffset = getViewportLength() * p2;
        return pixelOffset - viewportOffset;
    }

    private void adjustPositionToIndex(int index) {
        int cellCount = getCellCount();
        if (cellCount <= 0) {
            setPosition(0.0d);
        } else {
            setPosition(index / cellCount);
        }
    }

    private void adjustByPixelAmount(double numPixels) {
        double viewportLength;
        double p2;
        if (numPixels == 0.0d) {
            return;
        }
        boolean forward = numPixels > 0.0d;
        int cellCount = getCellCount();
        double fractionalPosition = getPosition() * cellCount;
        int cellIndex = (int) fractionalPosition;
        if (forward && cellIndex == cellCount) {
            return;
        }
        double cellSize = getCellLength(cellIndex);
        double fraction = fractionalPosition - cellIndex;
        double pixelOffset = cellSize * fraction;
        double cellPercent = 1.0d / cellCount;
        double start = computeOffsetForCell(cellIndex);
        double end = cellSize + computeOffsetForCell(cellIndex + 1);
        double remaining = end - start;
        if (forward) {
            viewportLength = ((numPixels + pixelOffset) - (getViewportLength() * getPosition())) - start;
        } else {
            viewportLength = ((-numPixels) + end) - (pixelOffset - (getViewportLength() * getPosition()));
        }
        double n2 = viewportLength;
        while (true) {
            p2 = cellPercent * cellIndex;
            if (n2 <= remaining || ((!forward || cellIndex >= cellCount - 1) && (forward || cellIndex <= 0))) {
                break;
            }
            cellIndex = forward ? cellIndex + 1 : cellIndex - 1;
            n2 -= remaining;
            double cellSize2 = getCellLength(cellIndex);
            start = computeOffsetForCell(cellIndex);
            end = cellSize2 + computeOffsetForCell(cellIndex + 1);
            remaining = end - start;
        }
        if (n2 > remaining) {
            setPosition(forward ? 1.0d : 0.0d);
        } else if (forward) {
            double rate = cellPercent / Math.abs(end - start);
            setPosition(p2 + (rate * n2));
        } else {
            double rate2 = cellPercent / Math.abs(end - start);
            setPosition((p2 + cellPercent) - (rate2 * n2));
        }
    }

    private int computeCurrentIndex() {
        return (int) (getPosition() * getCellCount());
    }

    private double computeOffsetForCell(int itemIndex) {
        double cellCount = getCellCount();
        double p2 = com.sun.javafx.util.Utils.clamp(0.0d, itemIndex, cellCount) / cellCount;
        return -(getViewportLength() * p2);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/VirtualFlow$ClippedContainer.class */
    static class ClippedContainer extends Region {
        private Node node;
        private final Rectangle clipRect;

        public Node getNode() {
            return this.node;
        }

        public void setNode(Node n2) {
            this.node = n2;
            getChildren().clear();
            getChildren().add(this.node);
        }

        public void setClipX(double clipX) {
            setLayoutX(-clipX);
            this.clipRect.setLayoutX(clipX);
        }

        public void setClipY(double clipY) {
            setLayoutY(-clipY);
            this.clipRect.setLayoutY(clipY);
        }

        public ClippedContainer(VirtualFlow<?> flow) {
            if (flow == null) {
                throw new IllegalArgumentException("VirtualFlow can not be null");
            }
            getStyleClass().add("clipped-container");
            this.clipRect = new Rectangle();
            this.clipRect.setSmooth(false);
            setClip(this.clipRect);
            super.widthProperty().addListener(valueModel -> {
                this.clipRect.setWidth(getWidth());
            });
            super.heightProperty().addListener(valueModel2 -> {
                this.clipRect.setHeight(getHeight());
            });
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/VirtualFlow$ArrayLinkedList.class */
    public static class ArrayLinkedList<T> extends AbstractList<T> {
        private int firstIndex = -1;
        private int lastIndex = -1;
        private final ArrayList<T> array = new ArrayList<>(50);

        public ArrayLinkedList() {
            for (int i2 = 0; i2 < 50; i2++) {
                this.array.add(null);
            }
        }

        public T getFirst() {
            if (this.firstIndex == -1) {
                return null;
            }
            return this.array.get(this.firstIndex);
        }

        public T getLast() {
            if (this.lastIndex == -1) {
                return null;
            }
            return this.array.get(this.lastIndex);
        }

        public void addFirst(T cell) {
            if (this.firstIndex == -1) {
                int size = this.array.size() / 2;
                this.lastIndex = size;
                this.firstIndex = size;
                this.array.set(this.firstIndex, cell);
                return;
            }
            if (this.firstIndex == 0) {
                this.array.add(0, cell);
                this.lastIndex++;
            } else {
                ArrayList<T> arrayList = this.array;
                int i2 = this.firstIndex - 1;
                this.firstIndex = i2;
                arrayList.set(i2, cell);
            }
        }

        public void addLast(T cell) {
            if (this.firstIndex == -1) {
                int size = this.array.size() / 2;
                this.lastIndex = size;
                this.firstIndex = size;
                this.array.set(this.lastIndex, cell);
                return;
            }
            if (this.lastIndex == this.array.size() - 1) {
                ArrayList<T> arrayList = this.array;
                int i2 = this.lastIndex + 1;
                this.lastIndex = i2;
                arrayList.add(i2, cell);
                return;
            }
            ArrayList<T> arrayList2 = this.array;
            int i3 = this.lastIndex + 1;
            this.lastIndex = i3;
            arrayList2.set(i3, cell);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            if (this.firstIndex == -1) {
                return 0;
            }
            return (this.lastIndex - this.firstIndex) + 1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.firstIndex == -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public T get(int index) {
            if (index > this.lastIndex - this.firstIndex || index < 0) {
                return null;
            }
            return this.array.get(this.firstIndex + index);
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            for (int i2 = 0; i2 < this.array.size(); i2++) {
                this.array.set(i2, null);
            }
            this.lastIndex = -1;
            this.firstIndex = -1;
        }

        public T removeFirst() {
            if (isEmpty()) {
                return null;
            }
            return remove(0);
        }

        public T removeLast() {
            if (isEmpty()) {
                return null;
            }
            return remove(this.lastIndex - this.firstIndex);
        }

        @Override // java.util.AbstractList, java.util.List
        public T remove(int i2) {
            if (i2 > this.lastIndex - this.firstIndex || i2 < 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if (i2 == 0) {
                T t2 = this.array.get(this.firstIndex);
                this.array.set(this.firstIndex, null);
                if (this.firstIndex == this.lastIndex) {
                    this.lastIndex = -1;
                    this.firstIndex = -1;
                } else {
                    this.firstIndex++;
                }
                return t2;
            }
            if (i2 == this.lastIndex - this.firstIndex) {
                T t3 = this.array.get(this.lastIndex);
                ArrayList<T> arrayList = this.array;
                int i3 = this.lastIndex;
                this.lastIndex = i3 - 1;
                arrayList.set(i3, null);
                return t3;
            }
            T t4 = this.array.get(this.firstIndex + i2);
            this.array.set(this.firstIndex + i2, null);
            for (int i4 = this.firstIndex + i2 + 1; i4 <= this.lastIndex; i4++) {
                this.array.set(i4 - 1, this.array.get(i4));
            }
            ArrayList<T> arrayList2 = this.array;
            int i5 = this.lastIndex;
            this.lastIndex = i5 - 1;
            arrayList2.set(i5, null);
            return t4;
        }
    }

    protected void startSBReleasedAnimation() {
        if (this.sbTouchTimeline == null) {
            this.sbTouchTimeline = new Timeline();
            this.sbTouchKF1 = new KeyFrame(Duration.millis(0.0d), (EventHandler<ActionEvent>) event -> {
                this.tempVisibility = true;
                requestLayout();
            }, new KeyValue[0]);
            this.sbTouchKF2 = new KeyFrame(Duration.millis(1000.0d), (EventHandler<ActionEvent>) event2 -> {
                if (!this.touchDetected && !this.mouseDown) {
                    this.tempVisibility = false;
                    requestLayout();
                }
            }, new KeyValue[0]);
            this.sbTouchTimeline.getKeyFrames().addAll(this.sbTouchKF1, this.sbTouchKF2);
        }
        this.sbTouchTimeline.playFromStart();
    }

    protected void scrollBarOn() {
        this.tempVisibility = true;
        requestLayout();
    }
}
