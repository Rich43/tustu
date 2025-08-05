package javax.swing.plaf.basic;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TreeUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.DragRecognitionSupport;
import javax.swing.text.Position;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.FixedHeightLayoutCache;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.VariableHeightLayoutCache;
import sun.awt.AWTAccessor;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI.class */
public class BasicTreeUI extends TreeUI {
    protected transient Icon collapsedIcon;
    protected transient Icon expandedIcon;
    private Color hashColor;
    protected int leftChildIndent;
    protected int rightChildIndent;
    protected int totalChildIndent;
    protected Dimension preferredMinSize;
    protected int lastSelectedRow;
    protected JTree tree;
    protected transient TreeCellRenderer currentCellRenderer;
    protected boolean createdRenderer;
    protected transient TreeCellEditor cellEditor;
    protected boolean createdCellEditor;
    protected boolean stopEditingInCompleteEditing;
    protected CellRendererPane rendererPane;
    protected Dimension preferredSize;
    protected boolean validCachedPreferredSize;
    protected AbstractLayoutCache treeState;
    protected Hashtable<TreePath, Boolean> drawingCache;
    protected boolean largeModel;
    protected AbstractLayoutCache.NodeDimensions nodeDimensions;
    protected TreeModel treeModel;
    protected TreeSelectionModel treeSelectionModel;
    protected int depthOffset;
    protected Component editingComponent;
    protected TreePath editingPath;
    protected int editingRow;
    protected boolean editorHasDifferentSize;
    private int leadRow;
    private boolean ignoreLAChange;
    private boolean leftToRight;
    private PropertyChangeListener propertyChangeListener;
    private PropertyChangeListener selectionModelPropertyChangeListener;
    private MouseListener mouseListener;
    private FocusListener focusListener;
    private KeyListener keyListener;
    private ComponentListener componentListener;
    private CellEditorListener cellEditorListener;
    private TreeSelectionListener treeSelectionListener;
    private TreeModelListener treeModelListener;
    private TreeExpansionListener treeExpansionListener;
    private boolean lineTypeDashed;
    private Handler handler;
    private MouseEvent releaseEvent;
    private static final StringBuilder BASELINE_COMPONENT_KEY = new StringBuilder("Tree.baselineComponent");
    private static final Actions SHARED_ACTION = new Actions();
    private static final TransferHandler defaultTransferHandler = new TreeTransferHandler();
    private boolean paintLines = true;
    private long timeFactor = 1000;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicTreeUI();
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("selectPrevious"));
        lazyActionMap.put(new Actions("selectPreviousChangeLead"));
        lazyActionMap.put(new Actions("selectPreviousExtendSelection"));
        lazyActionMap.put(new Actions("selectNext"));
        lazyActionMap.put(new Actions("selectNextChangeLead"));
        lazyActionMap.put(new Actions("selectNextExtendSelection"));
        lazyActionMap.put(new Actions("selectChild"));
        lazyActionMap.put(new Actions("selectChildChangeLead"));
        lazyActionMap.put(new Actions("selectParent"));
        lazyActionMap.put(new Actions("selectParentChangeLead"));
        lazyActionMap.put(new Actions("scrollUpChangeSelection"));
        lazyActionMap.put(new Actions("scrollUpChangeLead"));
        lazyActionMap.put(new Actions("scrollUpExtendSelection"));
        lazyActionMap.put(new Actions("scrollDownChangeSelection"));
        lazyActionMap.put(new Actions("scrollDownExtendSelection"));
        lazyActionMap.put(new Actions("scrollDownChangeLead"));
        lazyActionMap.put(new Actions("selectFirst"));
        lazyActionMap.put(new Actions("selectFirstChangeLead"));
        lazyActionMap.put(new Actions("selectFirstExtendSelection"));
        lazyActionMap.put(new Actions("selectLast"));
        lazyActionMap.put(new Actions("selectLastChangeLead"));
        lazyActionMap.put(new Actions("selectLastExtendSelection"));
        lazyActionMap.put(new Actions("toggle"));
        lazyActionMap.put(new Actions("cancel"));
        lazyActionMap.put(new Actions("startEditing"));
        lazyActionMap.put(new Actions("selectAll"));
        lazyActionMap.put(new Actions("clearSelection"));
        lazyActionMap.put(new Actions("scrollLeft"));
        lazyActionMap.put(new Actions("scrollRight"));
        lazyActionMap.put(new Actions("scrollLeftExtendSelection"));
        lazyActionMap.put(new Actions("scrollRightExtendSelection"));
        lazyActionMap.put(new Actions("scrollRightChangeLead"));
        lazyActionMap.put(new Actions("scrollLeftChangeLead"));
        lazyActionMap.put(new Actions("expand"));
        lazyActionMap.put(new Actions(SchemaSymbols.ATTVAL_COLLAPSE));
        lazyActionMap.put(new Actions("moveSelectionToParent"));
        lazyActionMap.put(new Actions("addToSelection"));
        lazyActionMap.put(new Actions("toggleAndAnchor"));
        lazyActionMap.put(new Actions("extendTo"));
        lazyActionMap.put(new Actions("moveSelectionTo"));
        lazyActionMap.put(TransferHandler.getCutAction());
        lazyActionMap.put(TransferHandler.getCopyAction());
        lazyActionMap.put(TransferHandler.getPasteAction());
    }

    protected Color getHashColor() {
        return this.hashColor;
    }

    protected void setHashColor(Color color) {
        this.hashColor = color;
    }

    public void setLeftChildIndent(int i2) {
        this.leftChildIndent = i2;
        this.totalChildIndent = this.leftChildIndent + this.rightChildIndent;
        if (this.treeState != null) {
            this.treeState.invalidateSizes();
        }
        updateSize();
    }

    public int getLeftChildIndent() {
        return this.leftChildIndent;
    }

    public void setRightChildIndent(int i2) {
        this.rightChildIndent = i2;
        this.totalChildIndent = this.leftChildIndent + this.rightChildIndent;
        if (this.treeState != null) {
            this.treeState.invalidateSizes();
        }
        updateSize();
    }

    public int getRightChildIndent() {
        return this.rightChildIndent;
    }

    public void setExpandedIcon(Icon icon) {
        this.expandedIcon = icon;
    }

    public Icon getExpandedIcon() {
        return this.expandedIcon;
    }

    public void setCollapsedIcon(Icon icon) {
        this.collapsedIcon = icon;
    }

    public Icon getCollapsedIcon() {
        return this.collapsedIcon;
    }

    protected void setLargeModel(boolean z2) {
        if (getRowHeight() < 1) {
            z2 = false;
        }
        if (this.largeModel != z2) {
            completeEditing();
            this.largeModel = z2;
            this.treeState = createLayoutCache();
            configureLayoutCache();
            updateLayoutCacheExpandedNodesIfNecessary();
            updateSize();
        }
    }

    protected boolean isLargeModel() {
        return this.largeModel;
    }

    protected void setRowHeight(int i2) {
        completeEditing();
        if (this.treeState != null) {
            setLargeModel(this.tree.isLargeModel());
            this.treeState.setRowHeight(i2);
            updateSize();
        }
    }

    protected int getRowHeight() {
        if (this.tree == null) {
            return -1;
        }
        return this.tree.getRowHeight();
    }

    protected void setCellRenderer(TreeCellRenderer treeCellRenderer) {
        completeEditing();
        updateRenderer();
        if (this.treeState != null) {
            this.treeState.invalidateSizes();
            updateSize();
        }
    }

    protected TreeCellRenderer getCellRenderer() {
        return this.currentCellRenderer;
    }

    protected void setModel(TreeModel treeModel) {
        completeEditing();
        if (this.treeModel != null && this.treeModelListener != null) {
            this.treeModel.removeTreeModelListener(this.treeModelListener);
        }
        this.treeModel = treeModel;
        if (this.treeModel != null && this.treeModelListener != null) {
            this.treeModel.addTreeModelListener(this.treeModelListener);
        }
        if (this.treeState != null) {
            this.treeState.setModel(treeModel);
            updateLayoutCacheExpandedNodesIfNecessary();
            updateSize();
        }
    }

    protected TreeModel getModel() {
        return this.treeModel;
    }

    protected void setRootVisible(boolean z2) {
        completeEditing();
        updateDepthOffset();
        if (this.treeState != null) {
            this.treeState.setRootVisible(z2);
            this.treeState.invalidateSizes();
            updateSize();
        }
    }

    protected boolean isRootVisible() {
        if (this.tree != null) {
            return this.tree.isRootVisible();
        }
        return false;
    }

    protected void setShowsRootHandles(boolean z2) {
        completeEditing();
        updateDepthOffset();
        if (this.treeState != null) {
            this.treeState.invalidateSizes();
            updateSize();
        }
    }

    protected boolean getShowsRootHandles() {
        if (this.tree != null) {
            return this.tree.getShowsRootHandles();
        }
        return false;
    }

    protected void setCellEditor(TreeCellEditor treeCellEditor) {
        updateCellEditor();
    }

    protected TreeCellEditor getCellEditor() {
        if (this.tree != null) {
            return this.tree.getCellEditor();
        }
        return null;
    }

    protected void setEditable(boolean z2) {
        updateCellEditor();
    }

    protected boolean isEditable() {
        if (this.tree != null) {
            return this.tree.isEditable();
        }
        return false;
    }

    protected void setSelectionModel(TreeSelectionModel treeSelectionModel) {
        completeEditing();
        if (this.selectionModelPropertyChangeListener != null && this.treeSelectionModel != null) {
            this.treeSelectionModel.removePropertyChangeListener(this.selectionModelPropertyChangeListener);
        }
        if (this.treeSelectionListener != null && this.treeSelectionModel != null) {
            this.treeSelectionModel.removeTreeSelectionListener(this.treeSelectionListener);
        }
        this.treeSelectionModel = treeSelectionModel;
        if (this.treeSelectionModel != null) {
            if (this.selectionModelPropertyChangeListener != null) {
                this.treeSelectionModel.addPropertyChangeListener(this.selectionModelPropertyChangeListener);
            }
            if (this.treeSelectionListener != null) {
                this.treeSelectionModel.addTreeSelectionListener(this.treeSelectionListener);
            }
            if (this.treeState != null) {
                this.treeState.setSelectionModel(this.treeSelectionModel);
            }
        } else if (this.treeState != null) {
            this.treeState.setSelectionModel(null);
        }
        if (this.tree != null) {
            this.tree.repaint();
        }
    }

    protected TreeSelectionModel getSelectionModel() {
        return this.treeSelectionModel;
    }

    @Override // javax.swing.plaf.TreeUI
    public Rectangle getPathBounds(JTree jTree, TreePath treePath) {
        if (jTree != null && this.treeState != null) {
            return getPathBounds(treePath, jTree.getInsets(), new Rectangle());
        }
        return null;
    }

    private Rectangle getPathBounds(TreePath treePath, Insets insets, Rectangle rectangle) {
        Rectangle bounds = this.treeState.getBounds(treePath, rectangle);
        if (bounds != null) {
            if (this.leftToRight) {
                bounds.f12372x += insets.left;
            } else {
                bounds.f12372x = (this.tree.getWidth() - (bounds.f12372x + bounds.width)) - insets.right;
            }
            bounds.f12373y += insets.top;
        }
        return bounds;
    }

    @Override // javax.swing.plaf.TreeUI
    public TreePath getPathForRow(JTree jTree, int i2) {
        if (this.treeState != null) {
            return this.treeState.getPathForRow(i2);
        }
        return null;
    }

    @Override // javax.swing.plaf.TreeUI
    public int getRowForPath(JTree jTree, TreePath treePath) {
        if (this.treeState != null) {
            return this.treeState.getRowForPath(treePath);
        }
        return -1;
    }

    @Override // javax.swing.plaf.TreeUI
    public int getRowCount(JTree jTree) {
        if (this.treeState != null) {
            return this.treeState.getRowCount();
        }
        return 0;
    }

    @Override // javax.swing.plaf.TreeUI
    public TreePath getClosestPathForLocation(JTree jTree, int i2, int i3) {
        if (jTree != null && this.treeState != null) {
            return this.treeState.getPathClosestTo(i2, i3 - jTree.getInsets().top);
        }
        return null;
    }

    @Override // javax.swing.plaf.TreeUI
    public boolean isEditing(JTree jTree) {
        return this.editingComponent != null;
    }

    @Override // javax.swing.plaf.TreeUI
    public boolean stopEditing(JTree jTree) {
        if (this.editingComponent != null && this.cellEditor.stopCellEditing()) {
            completeEditing(false, false, true);
            return true;
        }
        return false;
    }

    @Override // javax.swing.plaf.TreeUI
    public void cancelEditing(JTree jTree) {
        if (this.editingComponent != null) {
            completeEditing(false, true, false);
        }
    }

    @Override // javax.swing.plaf.TreeUI
    public void startEditingAtPath(JTree jTree, TreePath treePath) {
        jTree.scrollPathToVisible(treePath);
        if (treePath != null && jTree.isVisible(treePath)) {
            startEditing(treePath, null);
        }
    }

    @Override // javax.swing.plaf.TreeUI
    public TreePath getEditingPath(JTree jTree) {
        return this.editingPath;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        if (jComponent == null) {
            throw new NullPointerException("null component passed to BasicTreeUI.installUI()");
        }
        this.tree = (JTree) jComponent;
        prepareForUIInstall();
        installDefaults();
        installKeyboardActions();
        installComponents();
        installListeners();
        completeUIInstall();
    }

    protected void prepareForUIInstall() {
        this.drawingCache = new Hashtable<>(7);
        this.leftToRight = BasicGraphicsUtils.isLeftToRight(this.tree);
        this.stopEditingInCompleteEditing = true;
        this.lastSelectedRow = -1;
        this.leadRow = -1;
        this.preferredSize = new Dimension();
        this.largeModel = this.tree.isLargeModel();
        if (getRowHeight() <= 0) {
            this.largeModel = false;
        }
        setModel(this.tree.getModel());
    }

    protected void completeUIInstall() {
        setShowsRootHandles(this.tree.getShowsRootHandles());
        updateRenderer();
        updateDepthOffset();
        setSelectionModel(this.tree.getSelectionModel());
        this.treeState = createLayoutCache();
        configureLayoutCache();
        updateSize();
    }

    protected void installDefaults() {
        if (this.tree.getBackground() == null || (this.tree.getBackground() instanceof UIResource)) {
            this.tree.setBackground(UIManager.getColor("Tree.background"));
        }
        if (getHashColor() == null || (getHashColor() instanceof UIResource)) {
            setHashColor(UIManager.getColor("Tree.hash"));
        }
        if (this.tree.getFont() == null || (this.tree.getFont() instanceof UIResource)) {
            this.tree.setFont(UIManager.getFont("Tree.font"));
        }
        setExpandedIcon((Icon) UIManager.get("Tree.expandedIcon"));
        setCollapsedIcon((Icon) UIManager.get("Tree.collapsedIcon"));
        setLeftChildIndent(((Integer) UIManager.get("Tree.leftChildIndent")).intValue());
        setRightChildIndent(((Integer) UIManager.get("Tree.rightChildIndent")).intValue());
        LookAndFeel.installProperty(this.tree, JTree.ROW_HEIGHT_PROPERTY, UIManager.get("Tree.rowHeight"));
        this.largeModel = this.tree.isLargeModel() && this.tree.getRowHeight() > 0;
        Object obj = UIManager.get("Tree.scrollsOnExpand");
        if (obj != null) {
            LookAndFeel.installProperty(this.tree, JTree.SCROLLS_ON_EXPAND_PROPERTY, obj);
        }
        this.paintLines = UIManager.getBoolean("Tree.paintLines");
        this.lineTypeDashed = UIManager.getBoolean("Tree.lineTypeDashed");
        Long l2 = (Long) UIManager.get("Tree.timeFactor");
        this.timeFactor = l2 != null ? l2.longValue() : 1000L;
        Object obj2 = UIManager.get("Tree.showsRootHandles");
        if (obj2 != null) {
            LookAndFeel.installProperty(this.tree, JTree.SHOWS_ROOT_HANDLES_PROPERTY, obj2);
        }
    }

    protected void installListeners() {
        PropertyChangeListener propertyChangeListenerCreatePropertyChangeListener = createPropertyChangeListener();
        this.propertyChangeListener = propertyChangeListenerCreatePropertyChangeListener;
        if (propertyChangeListenerCreatePropertyChangeListener != null) {
            this.tree.addPropertyChangeListener(this.propertyChangeListener);
        }
        MouseListener mouseListenerCreateMouseListener = createMouseListener();
        this.mouseListener = mouseListenerCreateMouseListener;
        if (mouseListenerCreateMouseListener != null) {
            this.tree.addMouseListener(this.mouseListener);
            if (this.mouseListener instanceof MouseMotionListener) {
                this.tree.addMouseMotionListener((MouseMotionListener) this.mouseListener);
            }
        }
        FocusListener focusListenerCreateFocusListener = createFocusListener();
        this.focusListener = focusListenerCreateFocusListener;
        if (focusListenerCreateFocusListener != null) {
            this.tree.addFocusListener(this.focusListener);
        }
        KeyListener keyListenerCreateKeyListener = createKeyListener();
        this.keyListener = keyListenerCreateKeyListener;
        if (keyListenerCreateKeyListener != null) {
            this.tree.addKeyListener(this.keyListener);
        }
        TreeExpansionListener treeExpansionListenerCreateTreeExpansionListener = createTreeExpansionListener();
        this.treeExpansionListener = treeExpansionListenerCreateTreeExpansionListener;
        if (treeExpansionListenerCreateTreeExpansionListener != null) {
            this.tree.addTreeExpansionListener(this.treeExpansionListener);
        }
        TreeModelListener treeModelListenerCreateTreeModelListener = createTreeModelListener();
        this.treeModelListener = treeModelListenerCreateTreeModelListener;
        if (treeModelListenerCreateTreeModelListener != null && this.treeModel != null) {
            this.treeModel.addTreeModelListener(this.treeModelListener);
        }
        PropertyChangeListener propertyChangeListenerCreateSelectionModelPropertyChangeListener = createSelectionModelPropertyChangeListener();
        this.selectionModelPropertyChangeListener = propertyChangeListenerCreateSelectionModelPropertyChangeListener;
        if (propertyChangeListenerCreateSelectionModelPropertyChangeListener != null && this.treeSelectionModel != null) {
            this.treeSelectionModel.addPropertyChangeListener(this.selectionModelPropertyChangeListener);
        }
        TreeSelectionListener treeSelectionListenerCreateTreeSelectionListener = createTreeSelectionListener();
        this.treeSelectionListener = treeSelectionListenerCreateTreeSelectionListener;
        if (treeSelectionListenerCreateTreeSelectionListener != null && this.treeSelectionModel != null) {
            this.treeSelectionModel.addTreeSelectionListener(this.treeSelectionListener);
        }
        TransferHandler transferHandler = this.tree.getTransferHandler();
        if (transferHandler == null || (transferHandler instanceof UIResource)) {
            this.tree.setTransferHandler(defaultTransferHandler);
            if (this.tree.getDropTarget() instanceof UIResource) {
                this.tree.setDropTarget(null);
            }
        }
        LookAndFeel.installProperty(this.tree, "opaque", Boolean.TRUE);
    }

    protected void installKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.tree, 1, getInputMap(1));
        SwingUtilities.replaceUIInputMap(this.tree, 0, getInputMap(0));
        LazyActionMap.installLazyActionMap(this.tree, BasicTreeUI.class, "Tree.actionMap");
    }

    InputMap getInputMap(int i2) {
        InputMap inputMap;
        if (i2 == 1) {
            return (InputMap) DefaultLookup.get(this.tree, this, "Tree.ancestorInputMap");
        }
        if (i2 == 0) {
            InputMap inputMap2 = (InputMap) DefaultLookup.get(this.tree, this, "Tree.focusInputMap");
            if (this.tree.getComponentOrientation().isLeftToRight() || (inputMap = (InputMap) DefaultLookup.get(this.tree, this, "Tree.focusInputMap.RightToLeft")) == null) {
                return inputMap2;
            }
            inputMap.setParent(inputMap2);
            return inputMap;
        }
        return null;
    }

    protected void installComponents() {
        CellRendererPane cellRendererPaneCreateCellRendererPane = createCellRendererPane();
        this.rendererPane = cellRendererPaneCreateCellRendererPane;
        if (cellRendererPaneCreateCellRendererPane != null) {
            this.tree.add(this.rendererPane);
        }
    }

    protected AbstractLayoutCache.NodeDimensions createNodeDimensions() {
        return new NodeDimensionsHandler();
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected MouseListener createMouseListener() {
        return getHandler();
    }

    protected FocusListener createFocusListener() {
        return getHandler();
    }

    protected KeyListener createKeyListener() {
        return getHandler();
    }

    protected PropertyChangeListener createSelectionModelPropertyChangeListener() {
        return getHandler();
    }

    protected TreeSelectionListener createTreeSelectionListener() {
        return getHandler();
    }

    protected CellEditorListener createCellEditorListener() {
        return getHandler();
    }

    protected ComponentListener createComponentListener() {
        return new ComponentHandler();
    }

    protected TreeExpansionListener createTreeExpansionListener() {
        return getHandler();
    }

    protected AbstractLayoutCache createLayoutCache() {
        if (isLargeModel() && getRowHeight() > 0) {
            return new FixedHeightLayoutCache();
        }
        return new VariableHeightLayoutCache();
    }

    protected CellRendererPane createCellRendererPane() {
        return new CellRendererPane();
    }

    protected TreeCellEditor createDefaultCellEditor() {
        if (this.currentCellRenderer != null && (this.currentCellRenderer instanceof DefaultTreeCellRenderer)) {
            return new DefaultTreeCellEditor(this.tree, (DefaultTreeCellRenderer) this.currentCellRenderer);
        }
        return new DefaultTreeCellEditor(this.tree, null);
    }

    protected TreeCellRenderer createDefaultCellRenderer() {
        return new DefaultTreeCellRenderer();
    }

    protected TreeModelListener createTreeModelListener() {
        return getHandler();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        completeEditing();
        prepareForUIUninstall();
        uninstallDefaults();
        uninstallListeners();
        uninstallKeyboardActions();
        uninstallComponents();
        completeUIUninstall();
    }

    protected void prepareForUIUninstall() {
    }

    protected void completeUIUninstall() {
        if (this.createdRenderer) {
            this.tree.setCellRenderer(null);
        }
        if (this.createdCellEditor) {
            this.tree.setCellEditor(null);
        }
        this.cellEditor = null;
        this.currentCellRenderer = null;
        this.rendererPane = null;
        this.componentListener = null;
        this.propertyChangeListener = null;
        this.mouseListener = null;
        this.focusListener = null;
        this.keyListener = null;
        setSelectionModel(null);
        this.treeState = null;
        this.drawingCache = null;
        this.selectionModelPropertyChangeListener = null;
        this.tree = null;
        this.treeModel = null;
        this.treeSelectionModel = null;
        this.treeSelectionListener = null;
        this.treeExpansionListener = null;
    }

    protected void uninstallDefaults() {
        if (this.tree.getTransferHandler() instanceof UIResource) {
            this.tree.setTransferHandler(null);
        }
    }

    protected void uninstallListeners() {
        if (this.componentListener != null) {
            this.tree.removeComponentListener(this.componentListener);
        }
        if (this.propertyChangeListener != null) {
            this.tree.removePropertyChangeListener(this.propertyChangeListener);
        }
        if (this.mouseListener != null) {
            this.tree.removeMouseListener(this.mouseListener);
            if (this.mouseListener instanceof MouseMotionListener) {
                this.tree.removeMouseMotionListener((MouseMotionListener) this.mouseListener);
            }
        }
        if (this.focusListener != null) {
            this.tree.removeFocusListener(this.focusListener);
        }
        if (this.keyListener != null) {
            this.tree.removeKeyListener(this.keyListener);
        }
        if (this.treeExpansionListener != null) {
            this.tree.removeTreeExpansionListener(this.treeExpansionListener);
        }
        if (this.treeModel != null && this.treeModelListener != null) {
            this.treeModel.removeTreeModelListener(this.treeModelListener);
        }
        if (this.selectionModelPropertyChangeListener != null && this.treeSelectionModel != null) {
            this.treeSelectionModel.removePropertyChangeListener(this.selectionModelPropertyChangeListener);
        }
        if (this.treeSelectionListener != null && this.treeSelectionModel != null) {
            this.treeSelectionModel.removeTreeSelectionListener(this.treeSelectionListener);
        }
        this.handler = null;
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(this.tree, null);
        SwingUtilities.replaceUIInputMap(this.tree, 1, null);
        SwingUtilities.replaceUIInputMap(this.tree, 0, null);
    }

    protected void uninstallComponents() {
        if (this.rendererPane != null) {
            this.tree.remove(this.rendererPane);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void redoTheLayout() {
        if (this.treeState != null) {
            this.treeState.invalidateSizes();
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        int baseline;
        super.getBaseline(jComponent, i2, i3);
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        Component treeCellRendererComponent = (Component) lookAndFeelDefaults.get(BASELINE_COMPONENT_KEY);
        if (treeCellRendererComponent == null) {
            treeCellRendererComponent = createDefaultCellRenderer().getTreeCellRendererComponent(this.tree, "a", false, false, false, -1, false);
            lookAndFeelDefaults.put(BASELINE_COMPONENT_KEY, treeCellRendererComponent);
        }
        int rowHeight = this.tree.getRowHeight();
        if (rowHeight > 0) {
            baseline = treeCellRendererComponent.getBaseline(Integer.MAX_VALUE, rowHeight);
        } else {
            Dimension preferredSize = treeCellRendererComponent.getPreferredSize();
            baseline = treeCellRendererComponent.getBaseline(preferredSize.width, preferredSize.height);
        }
        return baseline + this.tree.getInsets().top;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        boolean expandedState;
        boolean zHasBeenExpanded;
        if (this.tree != jComponent) {
            throw new InternalError("incorrect component");
        }
        if (this.treeState == null) {
            return;
        }
        Rectangle clipBounds = graphics.getClipBounds();
        Insets insets = this.tree.getInsets();
        TreePath closestPathForLocation = getClosestPathForLocation(this.tree, 0, clipBounds.f12373y);
        Enumeration<TreePath> visiblePathsFrom = this.treeState.getVisiblePathsFrom(closestPathForLocation);
        int rowForPath = this.treeState.getRowForPath(closestPathForLocation);
        int i2 = clipBounds.f12373y + clipBounds.height;
        this.drawingCache.clear();
        if (closestPathForLocation != null && visiblePathsFrom != null) {
            TreePath parentPath = closestPathForLocation.getParentPath();
            while (true) {
                TreePath treePath = parentPath;
                if (treePath == null) {
                    break;
                }
                paintVerticalPartOfLeg(graphics, clipBounds, insets, treePath);
                this.drawingCache.put(treePath, Boolean.TRUE);
                parentPath = treePath.getParentPath();
            }
            boolean z2 = false;
            Rectangle rectangle = new Rectangle();
            boolean zIsRootVisible = isRootVisible();
            while (!z2 && visiblePathsFrom.hasMoreElements()) {
                TreePath treePathNextElement2 = visiblePathsFrom.nextElement2();
                if (treePathNextElement2 != null) {
                    boolean zIsLeaf = this.treeModel.isLeaf(treePathNextElement2.getLastPathComponent());
                    if (zIsLeaf) {
                        zHasBeenExpanded = false;
                        expandedState = false;
                    } else {
                        expandedState = this.treeState.getExpandedState(treePathNextElement2);
                        zHasBeenExpanded = this.tree.hasBeenExpanded(treePathNextElement2);
                    }
                    Rectangle pathBounds = getPathBounds(treePathNextElement2, insets, rectangle);
                    if (pathBounds == null) {
                        return;
                    }
                    TreePath parentPath2 = treePathNextElement2.getParentPath();
                    if (parentPath2 != null) {
                        if (this.drawingCache.get(parentPath2) == null) {
                            paintVerticalPartOfLeg(graphics, clipBounds, insets, parentPath2);
                            this.drawingCache.put(parentPath2, Boolean.TRUE);
                        }
                        paintHorizontalPartOfLeg(graphics, clipBounds, insets, pathBounds, treePathNextElement2, rowForPath, expandedState, zHasBeenExpanded, zIsLeaf);
                    } else if (zIsRootVisible && rowForPath == 0) {
                        paintHorizontalPartOfLeg(graphics, clipBounds, insets, pathBounds, treePathNextElement2, rowForPath, expandedState, zHasBeenExpanded, zIsLeaf);
                    }
                    if (shouldPaintExpandControl(treePathNextElement2, rowForPath, expandedState, zHasBeenExpanded, zIsLeaf)) {
                        paintExpandControl(graphics, clipBounds, insets, pathBounds, treePathNextElement2, rowForPath, expandedState, zHasBeenExpanded, zIsLeaf);
                    }
                    paintRow(graphics, clipBounds, insets, pathBounds, treePathNextElement2, rowForPath, expandedState, zHasBeenExpanded, zIsLeaf);
                    if (pathBounds.f12373y + pathBounds.height >= i2) {
                        z2 = true;
                    }
                } else {
                    z2 = true;
                }
                rowForPath++;
            }
        }
        paintDropLine(graphics);
        this.rendererPane.removeAll();
        this.drawingCache.clear();
    }

    protected boolean isDropLine(JTree.DropLocation dropLocation) {
        return (dropLocation == null || dropLocation.getPath() == null || dropLocation.getChildIndex() == -1) ? false : true;
    }

    protected void paintDropLine(Graphics graphics) {
        Color color;
        JTree.DropLocation dropLocation = this.tree.getDropLocation();
        if (isDropLine(dropLocation) && (color = UIManager.getColor("Tree.dropLineColor")) != null) {
            graphics.setColor(color);
            Rectangle dropLineRect = getDropLineRect(dropLocation);
            graphics.fillRect(dropLineRect.f12372x, dropLineRect.f12373y, dropLineRect.width, dropLineRect.height);
        }
    }

    protected Rectangle getDropLineRect(JTree.DropLocation dropLocation) {
        Rectangle pathBounds;
        Rectangle pathBounds2;
        TreePath path = dropLocation.getPath();
        int childIndex = dropLocation.getChildIndex();
        boolean z2 = this.leftToRight;
        Insets insets = this.tree.getInsets();
        if (this.tree.getRowCount() == 0) {
            pathBounds = new Rectangle(insets.left, insets.top, (this.tree.getWidth() - insets.left) - insets.right, 0);
        } else {
            TreeModel model = getModel();
            Object root = model.getRoot();
            if (path.getLastPathComponent() == root && childIndex >= model.getChildCount(root)) {
                pathBounds = this.tree.getRowBounds(this.tree.getRowCount() - 1);
                pathBounds.f12373y += pathBounds.height;
                if (!this.tree.isRootVisible()) {
                    pathBounds2 = this.tree.getRowBounds(0);
                } else if (model.getChildCount(root) == 0) {
                    pathBounds2 = this.tree.getRowBounds(0);
                    pathBounds2.f12372x += this.totalChildIndent;
                    pathBounds2.width -= this.totalChildIndent + this.totalChildIndent;
                } else {
                    pathBounds2 = this.tree.getPathBounds(path.pathByAddingChild(model.getChild(root, model.getChildCount(root) - 1)));
                }
                pathBounds.f12372x = pathBounds2.f12372x;
                pathBounds.width = pathBounds2.width;
            } else {
                pathBounds = this.tree.getPathBounds(path.pathByAddingChild(model.getChild(path.getLastPathComponent(), childIndex)));
            }
        }
        if (pathBounds.f12373y != 0) {
            pathBounds.f12373y--;
        }
        if (!z2) {
            pathBounds.f12372x = (pathBounds.f12372x + pathBounds.width) - 100;
        }
        pathBounds.width = 100;
        pathBounds.height = 2;
        return pathBounds;
    }

    protected void paintHorizontalPartOfLeg(Graphics graphics, Rectangle rectangle, Insets insets, Rectangle rectangle2, TreePath treePath, int i2, boolean z2, boolean z3, boolean z4) {
        if (!this.paintLines) {
            return;
        }
        int pathCount = treePath.getPathCount() - 1;
        if ((pathCount == 0 || (pathCount == 1 && !isRootVisible())) && !getShowsRootHandles()) {
            return;
        }
        int i3 = rectangle.f12372x;
        int i4 = rectangle.f12372x + rectangle.width;
        int i5 = rectangle.f12373y;
        int i6 = rectangle.f12373y + rectangle.height;
        int i7 = rectangle2.f12373y + (rectangle2.height / 2);
        if (this.leftToRight) {
            int rightChildIndent = rectangle2.f12372x - getRightChildIndent();
            int horizontalLegBuffer = rectangle2.f12372x - getHorizontalLegBuffer();
            if (i7 >= i5 && i7 < i6 && horizontalLegBuffer >= i3 && rightChildIndent < i4 && rightChildIndent < horizontalLegBuffer) {
                graphics.setColor(getHashColor());
                paintHorizontalLine(graphics, this.tree, i7, rightChildIndent, horizontalLegBuffer - 1);
                return;
            }
            return;
        }
        int horizontalLegBuffer2 = rectangle2.f12372x + rectangle2.width + getHorizontalLegBuffer();
        int rightChildIndent2 = rectangle2.f12372x + rectangle2.width + getRightChildIndent();
        if (i7 >= i5 && i7 < i6 && rightChildIndent2 >= i3 && horizontalLegBuffer2 < i4 && horizontalLegBuffer2 < rightChildIndent2) {
            graphics.setColor(getHashColor());
            paintHorizontalLine(graphics, this.tree, i7, horizontalLegBuffer2, rightChildIndent2 - 1);
        }
    }

    protected void paintVerticalPartOfLeg(Graphics graphics, Rectangle rectangle, Insets insets, TreePath treePath) {
        int width;
        int iMax;
        TreeModel model;
        Rectangle pathBounds;
        if (!this.paintLines) {
            return;
        }
        int pathCount = treePath.getPathCount() - 1;
        if (pathCount == 0 && !getShowsRootHandles() && !isRootVisible()) {
            return;
        }
        int rowX = getRowX(-1, pathCount + 1);
        if (this.leftToRight) {
            width = (rowX - getRightChildIndent()) + insets.left;
        } else {
            width = (((this.tree.getWidth() - rowX) - insets.right) + getRightChildIndent()) - 1;
        }
        int i2 = rectangle.f12372x;
        int i3 = rectangle.f12372x + (rectangle.width - 1);
        if (width >= i2 && width <= i3) {
            int i4 = rectangle.f12373y;
            int i5 = rectangle.f12373y + rectangle.height;
            Rectangle pathBounds2 = getPathBounds(this.tree, treePath);
            Rectangle pathBounds3 = getPathBounds(this.tree, getLastChildPath(treePath));
            if (pathBounds3 == null) {
                return;
            }
            if (pathBounds2 == null) {
                iMax = Math.max(insets.top + getVerticalLegBuffer(), i4);
            } else {
                iMax = Math.max(pathBounds2.f12373y + pathBounds2.height + getVerticalLegBuffer(), i4);
            }
            if (pathCount == 0 && !isRootVisible() && (model = getModel()) != null) {
                Object root = model.getRoot();
                if (model.getChildCount(root) > 0 && (pathBounds = getPathBounds(this.tree, treePath.pathByAddingChild(model.getChild(root, 0)))) != null) {
                    iMax = Math.max(insets.top + getVerticalLegBuffer(), pathBounds.f12373y + (pathBounds.height / 2));
                }
            }
            int iMin = Math.min(pathBounds3.f12373y + (pathBounds3.height / 2), i5);
            if (iMax <= iMin) {
                graphics.setColor(getHashColor());
                paintVerticalLine(graphics, this.tree, width, iMax, iMin);
            }
        }
    }

    protected void paintExpandControl(Graphics graphics, Rectangle rectangle, Insets insets, Rectangle rectangle2, TreePath treePath, int i2, boolean z2, boolean z3, boolean z4) {
        int rightChildIndent;
        Object lastPathComponent = treePath.getLastPathComponent();
        if (!z4) {
            if (!z3 || this.treeModel.getChildCount(lastPathComponent) > 0) {
                if (this.leftToRight) {
                    rightChildIndent = (rectangle2.f12372x - getRightChildIndent()) + 1;
                } else {
                    rightChildIndent = ((rectangle2.f12372x + rectangle2.width) + getRightChildIndent()) - 1;
                }
                int i3 = rectangle2.f12373y + (rectangle2.height / 2);
                if (z2) {
                    Icon expandedIcon = getExpandedIcon();
                    if (expandedIcon != null) {
                        drawCentered(this.tree, graphics, expandedIcon, rightChildIndent, i3);
                        return;
                    }
                    return;
                }
                Icon collapsedIcon = getCollapsedIcon();
                if (collapsedIcon != null) {
                    drawCentered(this.tree, graphics, collapsedIcon, rightChildIndent, i3);
                }
            }
        }
    }

    protected void paintRow(Graphics graphics, Rectangle rectangle, Insets insets, Rectangle rectangle2, TreePath treePath, int i2, boolean z2, boolean z3, boolean z4) {
        int leadSelectionRow;
        if (this.editingComponent != null && this.editingRow == i2) {
            return;
        }
        if (this.tree.hasFocus()) {
            leadSelectionRow = getLeadSelectionRow();
        } else {
            leadSelectionRow = -1;
        }
        this.rendererPane.paintComponent(graphics, this.currentCellRenderer.getTreeCellRendererComponent(this.tree, treePath.getLastPathComponent(), this.tree.isRowSelected(i2), z2, z4, i2, leadSelectionRow == i2), this.tree, rectangle2.f12372x, rectangle2.f12373y, rectangle2.width, rectangle2.height, true);
    }

    protected boolean shouldPaintExpandControl(TreePath treePath, int i2, boolean z2, boolean z3, boolean z4) {
        if (z4) {
            return false;
        }
        int pathCount = treePath.getPathCount() - 1;
        if ((pathCount == 0 || (pathCount == 1 && !isRootVisible())) && !getShowsRootHandles()) {
            return false;
        }
        return true;
    }

    protected void paintVerticalLine(Graphics graphics, JComponent jComponent, int i2, int i3, int i4) {
        if (this.lineTypeDashed) {
            drawDashedVerticalLine(graphics, i2, i3, i4);
        } else {
            graphics.drawLine(i2, i3, i2, i4);
        }
    }

    protected void paintHorizontalLine(Graphics graphics, JComponent jComponent, int i2, int i3, int i4) {
        if (this.lineTypeDashed) {
            drawDashedHorizontalLine(graphics, i2, i3, i4);
        } else {
            graphics.drawLine(i3, i2, i4, i2);
        }
    }

    protected int getVerticalLegBuffer() {
        return 0;
    }

    protected int getHorizontalLegBuffer() {
        return 0;
    }

    private int findCenteredX(int i2, int i3) {
        if (this.leftToRight) {
            return i2 - ((int) Math.ceil(i3 / 2.0d));
        }
        return i2 - ((int) Math.floor(i3 / 2.0d));
    }

    protected void drawCentered(Component component, Graphics graphics, Icon icon, int i2, int i3) {
        icon.paintIcon(component, graphics, findCenteredX(i2, icon.getIconWidth()), i3 - (icon.getIconHeight() / 2));
    }

    protected void drawDashedHorizontalLine(Graphics graphics, int i2, int i3, int i4) {
        for (int i5 = i3 + (i3 % 2); i5 <= i4; i5 += 2) {
            graphics.drawLine(i5, i2, i5, i2);
        }
    }

    protected void drawDashedVerticalLine(Graphics graphics, int i2, int i3, int i4) {
        for (int i5 = i3 + (i3 % 2); i5 <= i4; i5 += 2) {
            graphics.drawLine(i2, i5, i2, i5);
        }
    }

    protected int getRowX(int i2, int i3) {
        return this.totalChildIndent * (i3 + this.depthOffset);
    }

    protected void updateLayoutCacheExpandedNodes() {
        if (this.treeModel != null && this.treeModel.getRoot() != null) {
            updateExpandedDescendants(new TreePath(this.treeModel.getRoot()));
        }
    }

    private void updateLayoutCacheExpandedNodesIfNecessary() {
        if (this.treeModel != null && this.treeModel.getRoot() != null) {
            TreePath treePath = new TreePath(this.treeModel.getRoot());
            if (this.tree.isExpanded(treePath)) {
                updateLayoutCacheExpandedNodes();
            } else {
                this.treeState.setExpandedState(treePath, false);
            }
        }
    }

    protected void updateExpandedDescendants(TreePath treePath) {
        completeEditing();
        if (this.treeState != null) {
            this.treeState.setExpandedState(treePath, true);
            Enumeration<TreePath> expandedDescendants = this.tree.getExpandedDescendants(treePath);
            if (expandedDescendants != null) {
                while (expandedDescendants.hasMoreElements()) {
                    this.treeState.setExpandedState(expandedDescendants.nextElement2(), true);
                }
            }
            updateLeadSelectionRow();
            updateSize();
        }
    }

    protected TreePath getLastChildPath(TreePath treePath) {
        int childCount;
        if (this.treeModel != null && (childCount = this.treeModel.getChildCount(treePath.getLastPathComponent())) > 0) {
            return treePath.pathByAddingChild(this.treeModel.getChild(treePath.getLastPathComponent(), childCount - 1));
        }
        return null;
    }

    protected void updateDepthOffset() {
        if (isRootVisible()) {
            if (getShowsRootHandles()) {
                this.depthOffset = 1;
                return;
            } else {
                this.depthOffset = 0;
                return;
            }
        }
        if (!getShowsRootHandles()) {
            this.depthOffset = -1;
        } else {
            this.depthOffset = 0;
        }
    }

    protected void updateCellEditor() {
        TreeCellEditor cellEditor;
        completeEditing();
        if (this.tree != null && this.tree.isEditable()) {
            cellEditor = this.tree.getCellEditor();
            if (cellEditor == null) {
                cellEditor = createDefaultCellEditor();
                if (cellEditor != null) {
                    this.tree.setCellEditor(cellEditor);
                    this.createdCellEditor = true;
                }
            }
        } else {
            cellEditor = null;
        }
        if (cellEditor != this.cellEditor) {
            if (this.cellEditor != null && this.cellEditorListener != null) {
                this.cellEditor.removeCellEditorListener(this.cellEditorListener);
            }
            this.cellEditor = cellEditor;
            if (this.cellEditorListener == null) {
                this.cellEditorListener = createCellEditorListener();
            }
            if (cellEditor != null && this.cellEditorListener != null) {
                cellEditor.addCellEditorListener(this.cellEditorListener);
            }
            this.createdCellEditor = false;
        }
    }

    protected void updateRenderer() {
        if (this.tree != null) {
            TreeCellRenderer cellRenderer = this.tree.getCellRenderer();
            if (cellRenderer == null) {
                this.tree.setCellRenderer(createDefaultCellRenderer());
                this.createdRenderer = true;
            } else {
                this.createdRenderer = false;
                this.currentCellRenderer = cellRenderer;
                if (this.createdCellEditor) {
                    this.tree.setCellEditor(null);
                }
            }
        } else {
            this.createdRenderer = false;
            this.currentCellRenderer = null;
        }
        updateCellEditor();
    }

    protected void configureLayoutCache() {
        if (this.treeState != null && this.tree != null) {
            if (this.nodeDimensions == null) {
                this.nodeDimensions = createNodeDimensions();
            }
            this.treeState.setNodeDimensions(this.nodeDimensions);
            this.treeState.setRootVisible(this.tree.isRootVisible());
            this.treeState.setRowHeight(this.tree.getRowHeight());
            this.treeState.setSelectionModel(getSelectionModel());
            if (this.treeState.getModel() != this.tree.getModel()) {
                this.treeState.setModel(this.tree.getModel());
            }
            updateLayoutCacheExpandedNodesIfNecessary();
            if (isLargeModel()) {
                if (this.componentListener == null) {
                    this.componentListener = createComponentListener();
                    if (this.componentListener != null) {
                        this.tree.addComponentListener(this.componentListener);
                        return;
                    }
                    return;
                }
                return;
            }
            if (this.componentListener != null) {
                this.tree.removeComponentListener(this.componentListener);
                this.componentListener = null;
                return;
            }
            return;
        }
        if (this.componentListener != null) {
            this.tree.removeComponentListener(this.componentListener);
            this.componentListener = null;
        }
    }

    protected void updateSize() {
        this.validCachedPreferredSize = false;
        this.tree.treeDidChange();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSize0() {
        this.validCachedPreferredSize = false;
        this.tree.revalidate();
    }

    protected void updateCachedPreferredSize() {
        JScrollBar horizontalScrollBar;
        if (this.treeState != null) {
            Insets insets = this.tree.getInsets();
            if (isLargeModel()) {
                Rectangle visibleRect = this.tree.getVisibleRect();
                if (visibleRect.f12372x == 0 && visibleRect.f12373y == 0 && visibleRect.width == 0 && visibleRect.height == 0 && this.tree.getVisibleRowCount() > 0) {
                    visibleRect.width = 1;
                    visibleRect.height = this.tree.getRowHeight() * this.tree.getVisibleRowCount();
                } else {
                    visibleRect.f12372x -= insets.left;
                    visibleRect.f12373y -= insets.top;
                }
                Container unwrappedParent = SwingUtilities.getUnwrappedParent(this.tree);
                if (unwrappedParent instanceof JViewport) {
                    Container parent = unwrappedParent.getParent();
                    if ((parent instanceof JScrollPane) && (horizontalScrollBar = ((JScrollPane) parent).getHorizontalScrollBar()) != null && horizontalScrollBar.isVisible()) {
                        int height = horizontalScrollBar.getHeight();
                        visibleRect.f12373y -= height;
                        visibleRect.height += height;
                    }
                }
                this.preferredSize.width = this.treeState.getPreferredWidth(visibleRect);
            } else {
                this.preferredSize.width = this.treeState.getPreferredWidth(null);
            }
            this.preferredSize.height = this.treeState.getPreferredHeight();
            this.preferredSize.width += insets.left + insets.right;
            this.preferredSize.height += insets.top + insets.bottom;
        }
        this.validCachedPreferredSize = true;
    }

    protected void pathWasExpanded(TreePath treePath) {
        if (this.tree != null) {
            this.tree.fireTreeExpanded(treePath);
        }
    }

    protected void pathWasCollapsed(TreePath treePath) {
        if (this.tree != null) {
            this.tree.fireTreeCollapsed(treePath);
        }
    }

    protected void ensureRowsAreVisible(int i2, int i3) {
        if (this.tree != null && i2 >= 0 && i3 < getRowCount(this.tree)) {
            boolean z2 = DefaultLookup.getBoolean(this.tree, this, "Tree.scrollsHorizontallyAndVertically", false);
            if (i2 == i3) {
                Rectangle pathBounds = getPathBounds(this.tree, getPathForRow(this.tree, i2));
                if (pathBounds != null) {
                    if (!z2) {
                        pathBounds.f12372x = this.tree.getVisibleRect().f12372x;
                        pathBounds.width = 1;
                    }
                    this.tree.scrollRectToVisible(pathBounds);
                    return;
                }
                return;
            }
            Rectangle pathBounds2 = getPathBounds(this.tree, getPathForRow(this.tree, i2));
            if (pathBounds2 != null) {
                Rectangle visibleRect = this.tree.getVisibleRect();
                Rectangle pathBounds3 = pathBounds2;
                int i4 = pathBounds2.f12373y;
                int i5 = i4 + visibleRect.height;
                int i6 = i2 + 1;
                while (i6 <= i3) {
                    pathBounds3 = getPathBounds(this.tree, getPathForRow(this.tree, i6));
                    if (pathBounds3 == null) {
                        return;
                    }
                    if (pathBounds3.f12373y + pathBounds3.height > i5) {
                        i6 = i3;
                    }
                    i6++;
                }
                this.tree.scrollRectToVisible(new Rectangle(visibleRect.f12372x, i4, 1, (pathBounds3.f12373y + pathBounds3.height) - i4));
            }
        }
    }

    public void setPreferredMinSize(Dimension dimension) {
        this.preferredMinSize = dimension;
    }

    public Dimension getPreferredMinSize() {
        if (this.preferredMinSize == null) {
            return null;
        }
        return new Dimension(this.preferredMinSize);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return getPreferredSize(jComponent, true);
    }

    public Dimension getPreferredSize(JComponent jComponent, boolean z2) {
        Dimension preferredMinSize = getPreferredMinSize();
        if (!this.validCachedPreferredSize) {
            updateCachedPreferredSize();
        }
        if (this.tree != null) {
            if (preferredMinSize != null) {
                return new Dimension(Math.max(preferredMinSize.width, this.preferredSize.width), Math.max(preferredMinSize.height, this.preferredSize.height));
            }
            return new Dimension(this.preferredSize.width, this.preferredSize.height);
        }
        if (preferredMinSize != null) {
            return preferredMinSize;
        }
        return new Dimension(0, 0);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        if (getPreferredMinSize() != null) {
            return getPreferredMinSize();
        }
        return new Dimension(0, 0);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        if (this.tree != null) {
            return getPreferredSize(this.tree);
        }
        if (getPreferredMinSize() != null) {
            return getPreferredMinSize();
        }
        return new Dimension(0, 0);
    }

    protected void completeEditing() {
        if (this.tree.getInvokesStopCellEditing() && this.stopEditingInCompleteEditing && this.editingComponent != null) {
            this.cellEditor.stopCellEditing();
        }
        completeEditing(false, true, false);
    }

    protected void completeEditing(boolean z2, boolean z3, boolean z4) {
        if (this.stopEditingInCompleteEditing && this.editingComponent != null) {
            Component component = this.editingComponent;
            TreePath treePath = this.editingPath;
            TreeCellEditor treeCellEditor = this.cellEditor;
            Object cellEditorValue = treeCellEditor.getCellEditorValue();
            Rectangle pathBounds = getPathBounds(this.tree, this.editingPath);
            boolean z5 = this.tree != null && (this.tree.hasFocus() || SwingUtilities.findFocusOwner(this.editingComponent) != null);
            this.editingComponent = null;
            this.editingPath = null;
            if (z2) {
                treeCellEditor.stopCellEditing();
            } else if (z3) {
                treeCellEditor.cancelCellEditing();
            }
            this.tree.remove(component);
            if (this.editorHasDifferentSize) {
                this.treeState.invalidatePathBounds(treePath);
                updateSize();
            } else if (pathBounds != null) {
                pathBounds.f12372x = 0;
                pathBounds.width = this.tree.getSize().width;
                this.tree.repaint(pathBounds);
            }
            if (z5) {
                this.tree.requestFocus();
            }
            if (z4) {
                this.treeModel.valueForPathChanged(treePath, cellEditorValue);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean startEditingOnRelease(TreePath treePath, MouseEvent mouseEvent, MouseEvent mouseEvent2) {
        this.releaseEvent = mouseEvent2;
        try {
            boolean zStartEditing = startEditing(treePath, mouseEvent);
            this.releaseEvent = null;
            return zStartEditing;
        } catch (Throwable th) {
            this.releaseEvent = null;
            throw th;
        }
    }

    protected boolean startEditing(TreePath treePath, MouseEvent mouseEvent) {
        if (isEditing(this.tree) && this.tree.getInvokesStopCellEditing() && !stopEditing(this.tree)) {
            return false;
        }
        completeEditing();
        if (this.cellEditor != null && this.tree.isPathEditable(treePath)) {
            int rowForPath = getRowForPath(this.tree, treePath);
            if (this.cellEditor.isCellEditable(mouseEvent)) {
                this.editingComponent = this.cellEditor.getTreeCellEditorComponent(this.tree, treePath.getLastPathComponent(), this.tree.isPathSelected(treePath), this.tree.isExpanded(treePath), this.treeModel.isLeaf(treePath.getLastPathComponent()), rowForPath);
                Rectangle pathBounds = getPathBounds(this.tree, treePath);
                if (pathBounds == null) {
                    return false;
                }
                this.editingRow = rowForPath;
                Dimension preferredSize = this.editingComponent.getPreferredSize();
                if (preferredSize.height != pathBounds.height && getRowHeight() > 0) {
                    preferredSize.height = getRowHeight();
                }
                if (preferredSize.width != pathBounds.width || preferredSize.height != pathBounds.height) {
                    this.editorHasDifferentSize = true;
                    this.treeState.invalidatePathBounds(treePath);
                    updateSize();
                    pathBounds = getPathBounds(this.tree, treePath);
                    if (pathBounds == null) {
                        return false;
                    }
                } else {
                    this.editorHasDifferentSize = false;
                }
                this.tree.add(this.editingComponent);
                this.editingComponent.setBounds(pathBounds.f12372x, pathBounds.f12373y, pathBounds.width, pathBounds.height);
                this.editingPath = treePath;
                AWTAccessor.getComponentAccessor().revalidateSynchronously(this.editingComponent);
                this.editingComponent.repaint();
                if (this.cellEditor.shouldSelectCell(mouseEvent)) {
                    this.stopEditingInCompleteEditing = false;
                    this.tree.setSelectionRow(rowForPath);
                    this.stopEditingInCompleteEditing = true;
                }
                Component componentCompositeRequestFocus = SwingUtilities2.compositeRequestFocus(this.editingComponent);
                boolean z2 = true;
                if (mouseEvent != null) {
                    Point pointConvertPoint = SwingUtilities.convertPoint(this.tree, new Point(mouseEvent.getX(), mouseEvent.getY()), this.editingComponent);
                    Component deepestComponentAt = SwingUtilities.getDeepestComponentAt(this.editingComponent, pointConvertPoint.f12370x, pointConvertPoint.f12371y);
                    if (deepestComponentAt != null) {
                        MouseInputHandler mouseInputHandler = new MouseInputHandler(this.tree, deepestComponentAt, mouseEvent, componentCompositeRequestFocus);
                        if (this.releaseEvent != null) {
                            mouseInputHandler.mouseReleased(this.releaseEvent);
                        }
                        z2 = false;
                    }
                }
                if (z2 && (componentCompositeRequestFocus instanceof JTextField)) {
                    ((JTextField) componentCompositeRequestFocus).selectAll();
                    return true;
                }
                return true;
            }
            this.editingComponent = null;
            return false;
        }
        return false;
    }

    protected void checkForClickInExpandControl(TreePath treePath, int i2, int i3) {
        if (isLocationInExpandControl(treePath, i2, i3)) {
            handleExpandControlClick(treePath, i2, i3);
        }
    }

    protected boolean isLocationInExpandControl(TreePath treePath, int i2, int i3) {
        int iconWidth;
        int width;
        if (treePath != null && !this.treeModel.isLeaf(treePath.getLastPathComponent())) {
            Insets insets = this.tree.getInsets();
            if (getExpandedIcon() != null) {
                iconWidth = getExpandedIcon().getIconWidth();
            } else {
                iconWidth = 8;
            }
            int rowX = getRowX(this.tree.getRowForPath(treePath), treePath.getPathCount() - 1);
            if (this.leftToRight) {
                width = ((rowX + insets.left) - getRightChildIndent()) + 1;
            } else {
                width = (((this.tree.getWidth() - rowX) - insets.right) + getRightChildIndent()) - 1;
            }
            int iFindCenteredX = findCenteredX(width, iconWidth);
            return i2 >= iFindCenteredX && i2 < iFindCenteredX + iconWidth;
        }
        return false;
    }

    protected void handleExpandControlClick(TreePath treePath, int i2, int i3) {
        toggleExpandState(treePath);
    }

    protected void toggleExpandState(TreePath treePath) {
        if (!this.tree.isExpanded(treePath)) {
            int rowForPath = getRowForPath(this.tree, treePath);
            this.tree.expandPath(treePath);
            updateSize();
            if (rowForPath != -1) {
                if (this.tree.getScrollsOnExpand()) {
                    ensureRowsAreVisible(rowForPath, rowForPath + this.treeState.getVisibleChildCount(treePath));
                    return;
                } else {
                    ensureRowsAreVisible(rowForPath, rowForPath);
                    return;
                }
            }
            return;
        }
        this.tree.collapsePath(treePath);
        updateSize();
    }

    protected boolean isToggleSelectionEvent(MouseEvent mouseEvent) {
        return SwingUtilities.isLeftMouseButton(mouseEvent) && BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent);
    }

    protected boolean isMultiSelectEvent(MouseEvent mouseEvent) {
        return SwingUtilities.isLeftMouseButton(mouseEvent) && mouseEvent.isShiftDown();
    }

    protected boolean isToggleEvent(MouseEvent mouseEvent) {
        int toggleClickCount;
        return SwingUtilities.isLeftMouseButton(mouseEvent) && (toggleClickCount = this.tree.getToggleClickCount()) > 0 && mouseEvent.getClickCount() % toggleClickCount == 0;
    }

    protected void selectPathForEvent(TreePath treePath, MouseEvent mouseEvent) {
        if (isMultiSelectEvent(mouseEvent)) {
            TreePath anchorSelectionPath = getAnchorSelectionPath();
            int rowForPath = anchorSelectionPath == null ? -1 : getRowForPath(this.tree, anchorSelectionPath);
            if (rowForPath == -1 || this.tree.getSelectionModel().getSelectionMode() == 1) {
                this.tree.setSelectionPath(treePath);
                return;
            }
            int rowForPath2 = getRowForPath(this.tree, treePath);
            if (isToggleSelectionEvent(mouseEvent)) {
                if (this.tree.isRowSelected(rowForPath)) {
                    this.tree.addSelectionInterval(rowForPath, rowForPath2);
                } else {
                    this.tree.removeSelectionInterval(rowForPath, rowForPath2);
                    this.tree.addSelectionInterval(rowForPath2, rowForPath2);
                }
            } else if (rowForPath2 < rowForPath) {
                this.tree.setSelectionInterval(rowForPath2, rowForPath);
            } else {
                this.tree.setSelectionInterval(rowForPath, rowForPath2);
            }
            this.lastSelectedRow = rowForPath2;
            setAnchorSelectionPath(anchorSelectionPath);
            setLeadSelectionPath(treePath);
            return;
        }
        if (isToggleSelectionEvent(mouseEvent)) {
            if (this.tree.isPathSelected(treePath)) {
                this.tree.removeSelectionPath(treePath);
            } else {
                this.tree.addSelectionPath(treePath);
            }
            this.lastSelectedRow = getRowForPath(this.tree, treePath);
            setAnchorSelectionPath(treePath);
            setLeadSelectionPath(treePath);
            return;
        }
        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
            this.tree.setSelectionPath(treePath);
            if (isToggleEvent(mouseEvent)) {
                toggleExpandState(treePath);
            }
        }
    }

    protected boolean isLeaf(int i2) {
        TreePath pathForRow = getPathForRow(this.tree, i2);
        if (pathForRow != null) {
            return this.treeModel.isLeaf(pathForRow.getLastPathComponent());
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAnchorSelectionPath(TreePath treePath) {
        this.ignoreLAChange = true;
        try {
            this.tree.setAnchorSelectionPath(treePath);
        } finally {
            this.ignoreLAChange = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TreePath getAnchorSelectionPath() {
        return this.tree.getAnchorSelectionPath();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLeadSelectionPath(TreePath treePath) {
        setLeadSelectionPath(treePath, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLeadSelectionPath(TreePath treePath, boolean z2) {
        Rectangle pathBounds = z2 ? getPathBounds(this.tree, getLeadSelectionPath()) : null;
        this.ignoreLAChange = true;
        try {
            this.tree.setLeadSelectionPath(treePath);
            this.ignoreLAChange = false;
            this.leadRow = getRowForPath(this.tree, treePath);
            if (z2) {
                if (pathBounds != null) {
                    this.tree.repaint(getRepaintPathBounds(pathBounds));
                }
                Rectangle pathBounds2 = getPathBounds(this.tree, treePath);
                if (pathBounds2 != null) {
                    this.tree.repaint(getRepaintPathBounds(pathBounds2));
                }
            }
        } catch (Throwable th) {
            this.ignoreLAChange = false;
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rectangle getRepaintPathBounds(Rectangle rectangle) {
        if (UIManager.getBoolean("Tree.repaintWholeRow")) {
            rectangle.f12372x = 0;
            rectangle.width = this.tree.getWidth();
        }
        return rectangle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TreePath getLeadSelectionPath() {
        return this.tree.getLeadSelectionPath();
    }

    protected void updateLeadSelectionRow() {
        this.leadRow = getRowForPath(this.tree, getLeadSelectionPath());
    }

    protected int getLeadSelectionRow() {
        return this.leadRow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void extendSelection(TreePath treePath) {
        TreePath anchorSelectionPath = getAnchorSelectionPath();
        int rowForPath = anchorSelectionPath == null ? -1 : getRowForPath(this.tree, anchorSelectionPath);
        int rowForPath2 = getRowForPath(this.tree, treePath);
        if (rowForPath == -1) {
            this.tree.setSelectionRow(rowForPath2);
            return;
        }
        if (rowForPath < rowForPath2) {
            this.tree.setSelectionInterval(rowForPath, rowForPath2);
        } else {
            this.tree.setSelectionInterval(rowForPath2, rowForPath);
        }
        setAnchorSelectionPath(anchorSelectionPath);
        setLeadSelectionPath(treePath);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void repaintPath(TreePath treePath) {
        Rectangle pathBounds;
        if (treePath != null && (pathBounds = getPathBounds(this.tree, treePath)) != null) {
            this.tree.repaint(pathBounds.f12372x, pathBounds.f12373y, pathBounds.width, pathBounds.height);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$TreeExpansionHandler.class */
    public class TreeExpansionHandler implements TreeExpansionListener {
        public TreeExpansionHandler() {
        }

        @Override // javax.swing.event.TreeExpansionListener
        public void treeExpanded(TreeExpansionEvent treeExpansionEvent) {
            BasicTreeUI.this.getHandler().treeExpanded(treeExpansionEvent);
        }

        @Override // javax.swing.event.TreeExpansionListener
        public void treeCollapsed(TreeExpansionEvent treeExpansionEvent) {
            BasicTreeUI.this.getHandler().treeCollapsed(treeExpansionEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$ComponentHandler.class */
    public class ComponentHandler extends ComponentAdapter implements ActionListener {
        protected Timer timer;
        protected JScrollBar scrollBar;

        public ComponentHandler() {
        }

        @Override // java.awt.event.ComponentAdapter, java.awt.event.ComponentListener
        public void componentMoved(ComponentEvent componentEvent) {
            if (this.timer == null) {
                JScrollPane scrollPane = getScrollPane();
                if (scrollPane == null) {
                    BasicTreeUI.this.updateSize();
                    return;
                }
                this.scrollBar = scrollPane.getVerticalScrollBar();
                if (this.scrollBar == null || !this.scrollBar.getValueIsAdjusting()) {
                    JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
                    this.scrollBar = horizontalScrollBar;
                    if (horizontalScrollBar != null && this.scrollBar.getValueIsAdjusting()) {
                        startTimer();
                        return;
                    } else {
                        BasicTreeUI.this.updateSize();
                        return;
                    }
                }
                startTimer();
            }
        }

        protected void startTimer() {
            if (this.timer == null) {
                this.timer = new Timer(200, this);
                this.timer.setRepeats(true);
            }
            this.timer.start();
        }

        protected JScrollPane getScrollPane() {
            Container container;
            Container parent = BasicTreeUI.this.tree.getParent();
            while (true) {
                container = parent;
                if (container == null || (container instanceof JScrollPane)) {
                    break;
                }
                parent = container.getParent();
            }
            if (container instanceof JScrollPane) {
                return (JScrollPane) container;
            }
            return null;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (this.scrollBar == null || !this.scrollBar.getValueIsAdjusting()) {
                if (this.timer != null) {
                    this.timer.stop();
                }
                BasicTreeUI.this.updateSize();
                this.timer = null;
                this.scrollBar = null;
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$TreeModelHandler.class */
    public class TreeModelHandler implements TreeModelListener {
        public TreeModelHandler() {
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesChanged(TreeModelEvent treeModelEvent) {
            BasicTreeUI.this.getHandler().treeNodesChanged(treeModelEvent);
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesInserted(TreeModelEvent treeModelEvent) {
            BasicTreeUI.this.getHandler().treeNodesInserted(treeModelEvent);
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesRemoved(TreeModelEvent treeModelEvent) {
            BasicTreeUI.this.getHandler().treeNodesRemoved(treeModelEvent);
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeStructureChanged(TreeModelEvent treeModelEvent) {
            BasicTreeUI.this.getHandler().treeStructureChanged(treeModelEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$TreeSelectionHandler.class */
    public class TreeSelectionHandler implements TreeSelectionListener {
        public TreeSelectionHandler() {
        }

        @Override // javax.swing.event.TreeSelectionListener
        public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
            BasicTreeUI.this.getHandler().valueChanged(treeSelectionEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$CellEditorHandler.class */
    public class CellEditorHandler implements CellEditorListener {
        public CellEditorHandler() {
        }

        @Override // javax.swing.event.CellEditorListener
        public void editingStopped(ChangeEvent changeEvent) {
            BasicTreeUI.this.getHandler().editingStopped(changeEvent);
        }

        @Override // javax.swing.event.CellEditorListener
        public void editingCanceled(ChangeEvent changeEvent) {
            BasicTreeUI.this.getHandler().editingCanceled(changeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$KeyHandler.class */
    public class KeyHandler extends KeyAdapter {
        protected Action repeatKeyAction;
        protected boolean isKeyDown;

        public KeyHandler() {
        }

        @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
        public void keyTyped(KeyEvent keyEvent) {
            BasicTreeUI.this.getHandler().keyTyped(keyEvent);
        }

        @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
        public void keyPressed(KeyEvent keyEvent) {
            BasicTreeUI.this.getHandler().keyPressed(keyEvent);
        }

        @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
        public void keyReleased(KeyEvent keyEvent) {
            BasicTreeUI.this.getHandler().keyReleased(keyEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$FocusHandler.class */
    public class FocusHandler implements FocusListener {
        public FocusHandler() {
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            BasicTreeUI.this.getHandler().focusGained(focusEvent);
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            BasicTreeUI.this.getHandler().focusLost(focusEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$NodeDimensionsHandler.class */
    public class NodeDimensionsHandler extends AbstractLayoutCache.NodeDimensions {
        public NodeDimensionsHandler() {
        }

        @Override // javax.swing.tree.AbstractLayoutCache.NodeDimensions
        public Rectangle getNodeDimensions(Object obj, int i2, int i3, boolean z2, Rectangle rectangle) {
            if (BasicTreeUI.this.editingComponent != null && BasicTreeUI.this.editingRow == i2) {
                Dimension preferredSize = BasicTreeUI.this.editingComponent.getPreferredSize();
                int rowHeight = BasicTreeUI.this.getRowHeight();
                if (rowHeight > 0 && rowHeight != preferredSize.height) {
                    preferredSize.height = rowHeight;
                }
                if (rectangle != null) {
                    rectangle.f12372x = getRowX(i2, i3);
                    rectangle.width = preferredSize.width;
                    rectangle.height = preferredSize.height;
                } else {
                    rectangle = new Rectangle(getRowX(i2, i3), 0, preferredSize.width, preferredSize.height);
                }
                return rectangle;
            }
            if (BasicTreeUI.this.currentCellRenderer != null) {
                Component treeCellRendererComponent = BasicTreeUI.this.currentCellRenderer.getTreeCellRendererComponent(BasicTreeUI.this.tree, obj, BasicTreeUI.this.tree.isRowSelected(i2), z2, BasicTreeUI.this.treeModel.isLeaf(obj), i2, false);
                if (BasicTreeUI.this.tree != null) {
                    BasicTreeUI.this.rendererPane.add(treeCellRendererComponent);
                    treeCellRendererComponent.validate();
                }
                Dimension preferredSize2 = treeCellRendererComponent.getPreferredSize();
                if (rectangle != null) {
                    rectangle.f12372x = getRowX(i2, i3);
                    rectangle.width = preferredSize2.width;
                    rectangle.height = preferredSize2.height;
                } else {
                    rectangle = new Rectangle(getRowX(i2, i3), 0, preferredSize2.width, preferredSize2.height);
                }
                return rectangle;
            }
            return null;
        }

        protected int getRowX(int i2, int i3) {
            return BasicTreeUI.this.getRowX(i2, i3);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$MouseHandler.class */
    public class MouseHandler extends MouseAdapter implements MouseMotionListener {
        public MouseHandler() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            BasicTreeUI.this.getHandler().mousePressed(mouseEvent);
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            BasicTreeUI.this.getHandler().mouseDragged(mouseEvent);
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            BasicTreeUI.this.getHandler().mouseMoved(mouseEvent);
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            BasicTreeUI.this.getHandler().mouseReleased(mouseEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$PropertyChangeHandler.class */
    public class PropertyChangeHandler implements PropertyChangeListener {
        public PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicTreeUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$SelectionModelPropertyChangeHandler.class */
    public class SelectionModelPropertyChangeHandler implements PropertyChangeListener {
        public SelectionModelPropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicTreeUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$TreeTraverseAction.class */
    public class TreeTraverseAction extends AbstractAction {
        protected int direction;
        private boolean changeSelection;

        public TreeTraverseAction(BasicTreeUI basicTreeUI, int i2, String str) {
            this(i2, str, true);
        }

        private TreeTraverseAction(int i2, String str, boolean z2) {
            this.direction = i2;
            this.changeSelection = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicTreeUI.this.tree == null) {
                return;
            }
            BasicTreeUI.SHARED_ACTION.traverse(BasicTreeUI.this.tree, BasicTreeUI.this, this.direction, this.changeSelection);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            return BasicTreeUI.this.tree != null && BasicTreeUI.this.tree.isEnabled();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$TreePageAction.class */
    public class TreePageAction extends AbstractAction {
        protected int direction;
        private boolean addToSelection;
        private boolean changeSelection;

        public TreePageAction(BasicTreeUI basicTreeUI, int i2, String str) {
            this(i2, str, false, true);
        }

        private TreePageAction(int i2, String str, boolean z2, boolean z3) {
            this.direction = i2;
            this.addToSelection = z2;
            this.changeSelection = z3;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicTreeUI.this.tree == null) {
                return;
            }
            BasicTreeUI.SHARED_ACTION.page(BasicTreeUI.this.tree, BasicTreeUI.this, this.direction, this.addToSelection, this.changeSelection);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            return BasicTreeUI.this.tree != null && BasicTreeUI.this.tree.isEnabled();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$TreeIncrementAction.class */
    public class TreeIncrementAction extends AbstractAction {
        protected int direction;
        private boolean addToSelection;
        private boolean changeSelection;

        public TreeIncrementAction(BasicTreeUI basicTreeUI, int i2, String str) {
            this(i2, str, false, true);
        }

        private TreeIncrementAction(int i2, String str, boolean z2, boolean z3) {
            this.direction = i2;
            this.addToSelection = z2;
            this.changeSelection = z3;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicTreeUI.this.tree == null) {
                return;
            }
            BasicTreeUI.SHARED_ACTION.increment(BasicTreeUI.this.tree, BasicTreeUI.this, this.direction, this.addToSelection, this.changeSelection);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            return BasicTreeUI.this.tree != null && BasicTreeUI.this.tree.isEnabled();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$TreeHomeAction.class */
    public class TreeHomeAction extends AbstractAction {
        protected int direction;
        private boolean addToSelection;
        private boolean changeSelection;

        public TreeHomeAction(BasicTreeUI basicTreeUI, int i2, String str) {
            this(i2, str, false, true);
        }

        private TreeHomeAction(int i2, String str, boolean z2, boolean z3) {
            this.direction = i2;
            this.changeSelection = z3;
            this.addToSelection = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicTreeUI.this.tree == null) {
                return;
            }
            BasicTreeUI.SHARED_ACTION.home(BasicTreeUI.this.tree, BasicTreeUI.this, this.direction, this.addToSelection, this.changeSelection);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            return BasicTreeUI.this.tree != null && BasicTreeUI.this.tree.isEnabled();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$TreeToggleAction.class */
    public class TreeToggleAction extends AbstractAction {
        public TreeToggleAction(String str) {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicTreeUI.this.tree == null) {
                return;
            }
            BasicTreeUI.SHARED_ACTION.toggle(BasicTreeUI.this.tree, BasicTreeUI.this);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            return BasicTreeUI.this.tree != null && BasicTreeUI.this.tree.isEnabled();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$TreeCancelEditingAction.class */
    public class TreeCancelEditingAction extends AbstractAction {
        public TreeCancelEditingAction(String str) {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicTreeUI.this.tree == null) {
                return;
            }
            BasicTreeUI.SHARED_ACTION.cancelEditing(BasicTreeUI.this.tree, BasicTreeUI.this);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            return BasicTreeUI.this.tree != null && BasicTreeUI.this.tree.isEnabled() && BasicTreeUI.this.isEditing(BasicTreeUI.this.tree);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$MouseInputHandler.class */
    public class MouseInputHandler implements MouseInputListener {
        protected Component source;
        protected Component destination;
        private Component focusComponent;
        private boolean dispatchedEvent;

        public MouseInputHandler(BasicTreeUI basicTreeUI, Component component, Component component2, MouseEvent mouseEvent) {
            this(component, component2, mouseEvent, null);
        }

        MouseInputHandler(Component component, Component component2, MouseEvent mouseEvent, Component component3) {
            this.source = component;
            this.destination = component2;
            this.source.addMouseListener(this);
            this.source.addMouseMotionListener(this);
            SwingUtilities2.setSkipClickCount(component2, mouseEvent.getClickCount() - 1);
            component2.dispatchEvent(SwingUtilities.convertMouseEvent(component, mouseEvent, component2));
            this.focusComponent = component3;
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            if (this.destination != null) {
                this.dispatchedEvent = true;
                this.destination.dispatchEvent(SwingUtilities.convertMouseEvent(this.source, mouseEvent, this.destination));
            }
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (this.destination != null) {
                this.destination.dispatchEvent(SwingUtilities.convertMouseEvent(this.source, mouseEvent, this.destination));
            }
            removeFromSource();
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            if (!SwingUtilities.isLeftMouseButton(mouseEvent)) {
                removeFromSource();
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            if (!SwingUtilities.isLeftMouseButton(mouseEvent)) {
                removeFromSource();
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (this.destination != null) {
                this.dispatchedEvent = true;
                this.destination.dispatchEvent(SwingUtilities.convertMouseEvent(this.source, mouseEvent, this.destination));
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            removeFromSource();
        }

        protected void removeFromSource() {
            if (this.source != null) {
                this.source.removeMouseListener(this);
                this.source.removeMouseMotionListener(this);
                if (this.focusComponent != null && this.focusComponent == this.destination && !this.dispatchedEvent && (this.focusComponent instanceof JTextField)) {
                    ((JTextField) this.focusComponent).selectAll();
                }
            }
            this.destination = null;
            this.source = null;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$TreeTransferHandler.class */
    static class TreeTransferHandler extends TransferHandler implements UIResource, Comparator<TreePath> {
        private JTree tree;

        TreeTransferHandler() {
        }

        @Override // javax.swing.TransferHandler
        protected Transferable createTransferable(JComponent jComponent) {
            if (jComponent instanceof JTree) {
                this.tree = (JTree) jComponent;
                TreePath[] selectionPaths = this.tree.getSelectionPaths();
                if (selectionPaths == null || selectionPaths.length == 0) {
                    return null;
                }
                StringBuffer stringBuffer = new StringBuffer();
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("<html>\n<body>\n<ul>\n");
                TreeModel model = this.tree.getModel();
                for (TreePath treePath : getDisplayOrderPaths(selectionPaths)) {
                    String displayString = getDisplayString(treePath, true, model.isLeaf(treePath.getLastPathComponent()));
                    stringBuffer.append(displayString + "\n");
                    stringBuffer2.append("  <li>" + displayString + "\n");
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                stringBuffer2.append("</ul>\n</body>\n</html>");
                this.tree = null;
                return new BasicTransferable(stringBuffer.toString(), stringBuffer2.toString());
            }
            return null;
        }

        @Override // java.util.Comparator
        public int compare(TreePath treePath, TreePath treePath2) {
            return this.tree.getRowForPath(treePath) - this.tree.getRowForPath(treePath2);
        }

        String getDisplayString(TreePath treePath, boolean z2, boolean z3) {
            int rowForPath = this.tree.getRowForPath(treePath);
            return this.tree.convertValueToText(treePath.getLastPathComponent(), z2, this.tree.isExpanded(rowForPath), z3, rowForPath, this.tree.getLeadSelectionRow() == rowForPath);
        }

        TreePath[] getDisplayOrderPaths(TreePath[] treePathArr) {
            ArrayList arrayList = new ArrayList();
            for (TreePath treePath : treePathArr) {
                arrayList.add(treePath);
            }
            Collections.sort(arrayList, this);
            int size = arrayList.size();
            TreePath[] treePathArr2 = new TreePath[size];
            for (int i2 = 0; i2 < size; i2++) {
                treePathArr2[i2] = (TreePath) arrayList.get(i2);
            }
            return treePathArr2;
        }

        @Override // javax.swing.TransferHandler
        public int getSourceActions(JComponent jComponent) {
            return 1;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$Handler.class */
    private class Handler implements CellEditorListener, FocusListener, KeyListener, MouseListener, MouseMotionListener, PropertyChangeListener, TreeExpansionListener, TreeModelListener, TreeSelectionListener, DragRecognitionSupport.BeforeDrag {
        private String prefix;
        private String typedString;
        private long lastTime;
        private boolean dragPressDidSelection;
        private boolean dragStarted;
        private TreePath pressedPath;
        private MouseEvent pressedEvent;
        private boolean valueChangedOnPress;

        private Handler() {
            this.prefix = "";
            this.typedString = "";
            this.lastTime = 0L;
        }

        @Override // java.awt.event.KeyListener
        public void keyTyped(KeyEvent keyEvent) {
            TreePath nextMatch;
            if (BasicTreeUI.this.tree == null || BasicTreeUI.this.tree.getRowCount() <= 0 || !BasicTreeUI.this.tree.hasFocus() || !BasicTreeUI.this.tree.isEnabled() || keyEvent.isAltDown() || BasicGraphicsUtils.isMenuShortcutKeyDown(keyEvent) || isNavigationKey(keyEvent)) {
                return;
            }
            boolean z2 = true;
            char keyChar = keyEvent.getKeyChar();
            long when = keyEvent.getWhen();
            int leadSelectionRow = BasicTreeUI.this.tree.getLeadSelectionRow();
            if (when - this.lastTime < BasicTreeUI.this.timeFactor) {
                this.typedString += keyChar;
                if (this.prefix.length() == 1 && keyChar == this.prefix.charAt(0)) {
                    leadSelectionRow++;
                } else {
                    this.prefix = this.typedString;
                }
            } else {
                leadSelectionRow++;
                this.typedString = "" + keyChar;
                this.prefix = this.typedString;
            }
            this.lastTime = when;
            if (leadSelectionRow < 0 || leadSelectionRow >= BasicTreeUI.this.tree.getRowCount()) {
                z2 = false;
                leadSelectionRow = 0;
            }
            TreePath nextMatch2 = BasicTreeUI.this.tree.getNextMatch(this.prefix, leadSelectionRow, Position.Bias.Forward);
            if (nextMatch2 != null) {
                BasicTreeUI.this.tree.setSelectionPath(nextMatch2);
                int rowForPath = BasicTreeUI.this.getRowForPath(BasicTreeUI.this.tree, nextMatch2);
                BasicTreeUI.this.ensureRowsAreVisible(rowForPath, rowForPath);
            } else if (z2 && (nextMatch = BasicTreeUI.this.tree.getNextMatch(this.prefix, 0, Position.Bias.Forward)) != null) {
                BasicTreeUI.this.tree.setSelectionPath(nextMatch);
                int rowForPath2 = BasicTreeUI.this.getRowForPath(BasicTreeUI.this.tree, nextMatch);
                BasicTreeUI.this.ensureRowsAreVisible(rowForPath2, rowForPath2);
            }
        }

        @Override // java.awt.event.KeyListener
        public void keyPressed(KeyEvent keyEvent) {
            if (BasicTreeUI.this.tree != null && isNavigationKey(keyEvent)) {
                this.prefix = "";
                this.typedString = "";
                this.lastTime = 0L;
            }
        }

        @Override // java.awt.event.KeyListener
        public void keyReleased(KeyEvent keyEvent) {
        }

        private boolean isNavigationKey(KeyEvent keyEvent) {
            InputMap inputMap = BasicTreeUI.this.tree.getInputMap(1);
            return (inputMap == null || inputMap.get(KeyStroke.getKeyStrokeForEvent(keyEvent)) == null) ? false : true;
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getSource() == BasicTreeUI.this.treeSelectionModel) {
                BasicTreeUI.this.treeSelectionModel.resetRowSelection();
                return;
            }
            if (propertyChangeEvent.getSource() == BasicTreeUI.this.tree) {
                String propertyName = propertyChangeEvent.getPropertyName();
                if (propertyName == JTree.LEAD_SELECTION_PATH_PROPERTY) {
                    if (!BasicTreeUI.this.ignoreLAChange) {
                        BasicTreeUI.this.updateLeadSelectionRow();
                        BasicTreeUI.this.repaintPath((TreePath) propertyChangeEvent.getOldValue());
                        BasicTreeUI.this.repaintPath((TreePath) propertyChangeEvent.getNewValue());
                    }
                } else if (propertyName == JTree.ANCHOR_SELECTION_PATH_PROPERTY && !BasicTreeUI.this.ignoreLAChange) {
                    BasicTreeUI.this.repaintPath((TreePath) propertyChangeEvent.getOldValue());
                    BasicTreeUI.this.repaintPath((TreePath) propertyChangeEvent.getNewValue());
                }
                if (propertyName == "cellRenderer") {
                    BasicTreeUI.this.setCellRenderer((TreeCellRenderer) propertyChangeEvent.getNewValue());
                    BasicTreeUI.this.redoTheLayout();
                    return;
                }
                if (propertyName == "model") {
                    BasicTreeUI.this.setModel((TreeModel) propertyChangeEvent.getNewValue());
                    return;
                }
                if (propertyName == JTree.ROOT_VISIBLE_PROPERTY) {
                    BasicTreeUI.this.setRootVisible(((Boolean) propertyChangeEvent.getNewValue()).booleanValue());
                    return;
                }
                if (propertyName == JTree.SHOWS_ROOT_HANDLES_PROPERTY) {
                    BasicTreeUI.this.setShowsRootHandles(((Boolean) propertyChangeEvent.getNewValue()).booleanValue());
                    return;
                }
                if (propertyName == JTree.ROW_HEIGHT_PROPERTY) {
                    BasicTreeUI.this.setRowHeight(((Integer) propertyChangeEvent.getNewValue()).intValue());
                    return;
                }
                if (propertyName == JTree.CELL_EDITOR_PROPERTY) {
                    BasicTreeUI.this.setCellEditor((TreeCellEditor) propertyChangeEvent.getNewValue());
                    return;
                }
                if (propertyName == JTree.EDITABLE_PROPERTY) {
                    BasicTreeUI.this.setEditable(((Boolean) propertyChangeEvent.getNewValue()).booleanValue());
                    return;
                }
                if (propertyName == JTree.LARGE_MODEL_PROPERTY) {
                    BasicTreeUI.this.setLargeModel(BasicTreeUI.this.tree.isLargeModel());
                    return;
                }
                if (propertyName == "selectionModel") {
                    BasicTreeUI.this.setSelectionModel(BasicTreeUI.this.tree.getSelectionModel());
                    return;
                }
                if (propertyName == "font") {
                    BasicTreeUI.this.completeEditing();
                    if (BasicTreeUI.this.treeState != null) {
                        BasicTreeUI.this.treeState.invalidateSizes();
                    }
                    BasicTreeUI.this.updateSize();
                    return;
                }
                if (propertyName == "componentOrientation") {
                    if (BasicTreeUI.this.tree != null) {
                        BasicTreeUI.this.leftToRight = BasicGraphicsUtils.isLeftToRight(BasicTreeUI.this.tree);
                        BasicTreeUI.this.redoTheLayout();
                        BasicTreeUI.this.tree.treeDidChange();
                        SwingUtilities.replaceUIInputMap(BasicTreeUI.this.tree, 0, BasicTreeUI.this.getInputMap(0));
                        return;
                    }
                    return;
                }
                if ("dropLocation" == propertyName) {
                    repaintDropLocation((JTree.DropLocation) propertyChangeEvent.getOldValue());
                    repaintDropLocation(BasicTreeUI.this.tree.getDropLocation());
                }
            }
        }

        private void repaintDropLocation(JTree.DropLocation dropLocation) {
            Rectangle pathBounds;
            if (dropLocation == null) {
                return;
            }
            if (BasicTreeUI.this.isDropLine(dropLocation)) {
                pathBounds = BasicTreeUI.this.getDropLineRect(dropLocation);
            } else {
                pathBounds = BasicTreeUI.this.tree.getPathBounds(dropLocation.getPath());
            }
            if (pathBounds != null) {
                BasicTreeUI.this.tree.repaint(pathBounds);
            }
        }

        private boolean isActualPath(TreePath treePath, int i2, int i3) {
            Rectangle pathBounds;
            return treePath != null && (pathBounds = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, treePath)) != null && i3 <= pathBounds.f12373y + pathBounds.height && i2 >= pathBounds.f12372x && i2 <= pathBounds.f12372x + pathBounds.width;
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (SwingUtilities2.shouldIgnore(mouseEvent, BasicTreeUI.this.tree)) {
                return;
            }
            if (BasicTreeUI.this.isEditing(BasicTreeUI.this.tree) && BasicTreeUI.this.tree.getInvokesStopCellEditing() && !BasicTreeUI.this.stopEditing(BasicTreeUI.this.tree)) {
                return;
            }
            BasicTreeUI.this.completeEditing();
            this.pressedPath = BasicTreeUI.this.getClosestPathForLocation(BasicTreeUI.this.tree, mouseEvent.getX(), mouseEvent.getY());
            if (BasicTreeUI.this.tree.getDragEnabled()) {
                mousePressedDND(mouseEvent);
            } else {
                SwingUtilities2.adjustFocus(BasicTreeUI.this.tree);
                handleSelection(mouseEvent);
            }
        }

        private void mousePressedDND(MouseEvent mouseEvent) {
            this.pressedEvent = mouseEvent;
            boolean z2 = true;
            this.dragStarted = false;
            this.valueChangedOnPress = false;
            if (isActualPath(this.pressedPath, mouseEvent.getX(), mouseEvent.getY()) && DragRecognitionSupport.mousePressed(mouseEvent)) {
                this.dragPressDidSelection = false;
                if (BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent)) {
                    return;
                }
                if (!mouseEvent.isShiftDown() && BasicTreeUI.this.tree.isPathSelected(this.pressedPath)) {
                    BasicTreeUI.this.setAnchorSelectionPath(this.pressedPath);
                    BasicTreeUI.this.setLeadSelectionPath(this.pressedPath, true);
                    return;
                } else {
                    this.dragPressDidSelection = true;
                    z2 = false;
                }
            }
            if (z2) {
                SwingUtilities2.adjustFocus(BasicTreeUI.this.tree);
            }
            handleSelection(mouseEvent);
        }

        void handleSelection(MouseEvent mouseEvent) {
            Rectangle pathBounds;
            if (this.pressedPath == null || (pathBounds = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, this.pressedPath)) == null || mouseEvent.getY() >= pathBounds.f12373y + pathBounds.height) {
                return;
            }
            if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                BasicTreeUI.this.checkForClickInExpandControl(this.pressedPath, mouseEvent.getX(), mouseEvent.getY());
            }
            int x2 = mouseEvent.getX();
            if (x2 >= pathBounds.f12372x && x2 < pathBounds.f12372x + pathBounds.width) {
                if (BasicTreeUI.this.tree.getDragEnabled() || !BasicTreeUI.this.startEditing(this.pressedPath, mouseEvent)) {
                    BasicTreeUI.this.selectPathForEvent(this.pressedPath, mouseEvent);
                }
            }
        }

        @Override // javax.swing.plaf.basic.DragRecognitionSupport.BeforeDrag
        public void dragStarting(MouseEvent mouseEvent) {
            this.dragStarted = true;
            if (BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent)) {
                BasicTreeUI.this.tree.addSelectionPath(this.pressedPath);
                BasicTreeUI.this.setAnchorSelectionPath(this.pressedPath);
                BasicTreeUI.this.setLeadSelectionPath(this.pressedPath, true);
            }
            this.pressedEvent = null;
            this.pressedPath = null;
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (!SwingUtilities2.shouldIgnore(mouseEvent, BasicTreeUI.this.tree) && BasicTreeUI.this.tree.getDragEnabled()) {
                DragRecognitionSupport.mouseDragged(mouseEvent, this);
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (SwingUtilities2.shouldIgnore(mouseEvent, BasicTreeUI.this.tree)) {
                return;
            }
            if (BasicTreeUI.this.tree.getDragEnabled()) {
                mouseReleasedDND(mouseEvent);
            }
            this.pressedEvent = null;
            this.pressedPath = null;
        }

        private void mouseReleasedDND(MouseEvent mouseEvent) {
            MouseEvent mouseEventMouseReleased = DragRecognitionSupport.mouseReleased(mouseEvent);
            if (mouseEventMouseReleased != null) {
                SwingUtilities2.adjustFocus(BasicTreeUI.this.tree);
                if (!this.dragPressDidSelection) {
                    handleSelection(mouseEventMouseReleased);
                }
            }
            if (!this.dragStarted && this.pressedPath != null && !this.valueChangedOnPress && isActualPath(this.pressedPath, this.pressedEvent.getX(), this.pressedEvent.getY())) {
                BasicTreeUI.this.startEditingOnRelease(this.pressedPath, this.pressedEvent, mouseEvent);
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            if (BasicTreeUI.this.tree != null) {
                Rectangle pathBounds = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, BasicTreeUI.this.tree.getLeadSelectionPath());
                if (pathBounds != null) {
                    BasicTreeUI.this.tree.repaint(BasicTreeUI.this.getRepaintPathBounds(pathBounds));
                }
                Rectangle pathBounds2 = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, BasicTreeUI.this.getLeadSelectionPath());
                if (pathBounds2 != null) {
                    BasicTreeUI.this.tree.repaint(BasicTreeUI.this.getRepaintPathBounds(pathBounds2));
                }
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            focusGained(focusEvent);
        }

        @Override // javax.swing.event.CellEditorListener
        public void editingStopped(ChangeEvent changeEvent) {
            BasicTreeUI.this.completeEditing(false, false, true);
        }

        @Override // javax.swing.event.CellEditorListener
        public void editingCanceled(ChangeEvent changeEvent) {
            BasicTreeUI.this.completeEditing(false, false, false);
        }

        @Override // javax.swing.event.TreeSelectionListener
        public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
            TreePath[] selectionPaths;
            this.valueChangedOnPress = true;
            BasicTreeUI.this.completeEditing();
            if (BasicTreeUI.this.tree.getExpandsSelectedPaths() && BasicTreeUI.this.treeSelectionModel != null && (selectionPaths = BasicTreeUI.this.treeSelectionModel.getSelectionPaths()) != null) {
                for (int length = selectionPaths.length - 1; length >= 0; length--) {
                    TreePath parentPath = selectionPaths[length].getParentPath();
                    boolean z2 = true;
                    while (parentPath != null) {
                        if (BasicTreeUI.this.treeModel.isLeaf(parentPath.getLastPathComponent())) {
                            z2 = false;
                            parentPath = null;
                        } else {
                            parentPath = parentPath.getParentPath();
                        }
                    }
                    if (z2) {
                        BasicTreeUI.this.tree.makeVisible(selectionPaths[length]);
                    }
                }
            }
            TreePath leadSelectionPath = BasicTreeUI.this.getLeadSelectionPath();
            BasicTreeUI.this.lastSelectedRow = BasicTreeUI.this.tree.getMinSelectionRow();
            TreePath leadSelectionPath2 = BasicTreeUI.this.tree.getSelectionModel().getLeadSelectionPath();
            BasicTreeUI.this.setAnchorSelectionPath(leadSelectionPath2);
            BasicTreeUI.this.setLeadSelectionPath(leadSelectionPath2);
            TreePath[] paths = treeSelectionEvent.getPaths();
            Rectangle visibleRect = BasicTreeUI.this.tree.getVisibleRect();
            boolean z3 = true;
            int width = BasicTreeUI.this.tree.getWidth();
            if (paths != null) {
                if (paths.length > 4) {
                    BasicTreeUI.this.tree.repaint();
                    z3 = false;
                } else {
                    for (TreePath treePath : paths) {
                        Rectangle pathBounds = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, treePath);
                        if (pathBounds != null && visibleRect.intersects(pathBounds)) {
                            BasicTreeUI.this.tree.repaint(0, pathBounds.f12373y, width, pathBounds.height);
                        }
                    }
                }
            }
            if (z3) {
                Rectangle pathBounds2 = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, leadSelectionPath);
                if (pathBounds2 != null && visibleRect.intersects(pathBounds2)) {
                    BasicTreeUI.this.tree.repaint(0, pathBounds2.f12373y, width, pathBounds2.height);
                }
                Rectangle pathBounds3 = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, leadSelectionPath2);
                if (pathBounds3 != null && visibleRect.intersects(pathBounds3)) {
                    BasicTreeUI.this.tree.repaint(0, pathBounds3.f12373y, width, pathBounds3.height);
                }
            }
        }

        @Override // javax.swing.event.TreeExpansionListener
        public void treeExpanded(TreeExpansionEvent treeExpansionEvent) {
            if (treeExpansionEvent != null && BasicTreeUI.this.tree != null) {
                BasicTreeUI.this.updateExpandedDescendants(treeExpansionEvent.getPath());
            }
        }

        @Override // javax.swing.event.TreeExpansionListener
        public void treeCollapsed(TreeExpansionEvent treeExpansionEvent) {
            if (treeExpansionEvent != null && BasicTreeUI.this.tree != null) {
                TreePath path = treeExpansionEvent.getPath();
                BasicTreeUI.this.completeEditing();
                if (path != null && BasicTreeUI.this.tree.isVisible(path)) {
                    BasicTreeUI.this.treeState.setExpandedState(path, false);
                    BasicTreeUI.this.updateLeadSelectionRow();
                    BasicTreeUI.this.updateSize();
                }
            }
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesChanged(TreeModelEvent treeModelEvent) {
            if (BasicTreeUI.this.treeState != null && treeModelEvent != null) {
                TreePath treePath = SwingUtilities2.getTreePath(treeModelEvent, BasicTreeUI.this.getModel());
                int[] childIndices = treeModelEvent.getChildIndices();
                if (childIndices == null || childIndices.length == 0) {
                    BasicTreeUI.this.treeState.treeNodesChanged(treeModelEvent);
                    BasicTreeUI.this.updateSize();
                    return;
                }
                if (BasicTreeUI.this.treeState.isExpanded(treePath)) {
                    int iMin = childIndices[0];
                    for (int length = childIndices.length - 1; length > 0; length--) {
                        iMin = Math.min(childIndices[length], iMin);
                    }
                    TreePath treePathPathByAddingChild = treePath.pathByAddingChild(BasicTreeUI.this.treeModel.getChild(treePath.getLastPathComponent(), iMin));
                    Rectangle pathBounds = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, treePathPathByAddingChild);
                    BasicTreeUI.this.treeState.treeNodesChanged(treeModelEvent);
                    BasicTreeUI.this.updateSize0();
                    Rectangle pathBounds2 = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, treePathPathByAddingChild);
                    if (pathBounds == null || pathBounds2 == null) {
                        return;
                    }
                    if (childIndices.length == 1 && pathBounds2.height == pathBounds.height) {
                        BasicTreeUI.this.tree.repaint(0, pathBounds.f12373y, BasicTreeUI.this.tree.getWidth(), pathBounds.height);
                        return;
                    } else {
                        BasicTreeUI.this.tree.repaint(0, pathBounds.f12373y, BasicTreeUI.this.tree.getWidth(), BasicTreeUI.this.tree.getHeight() - pathBounds.f12373y);
                        return;
                    }
                }
                BasicTreeUI.this.treeState.treeNodesChanged(treeModelEvent);
            }
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesInserted(TreeModelEvent treeModelEvent) {
            if (BasicTreeUI.this.treeState != null && treeModelEvent != null) {
                BasicTreeUI.this.treeState.treeNodesInserted(treeModelEvent);
                BasicTreeUI.this.updateLeadSelectionRow();
                TreePath treePath = SwingUtilities2.getTreePath(treeModelEvent, BasicTreeUI.this.getModel());
                if (BasicTreeUI.this.treeState.isExpanded(treePath)) {
                    BasicTreeUI.this.updateSize();
                    return;
                }
                int[] childIndices = treeModelEvent.getChildIndices();
                int childCount = BasicTreeUI.this.treeModel.getChildCount(treePath.getLastPathComponent());
                if (childIndices != null && childCount - childIndices.length == 0) {
                    BasicTreeUI.this.updateSize();
                }
            }
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesRemoved(TreeModelEvent treeModelEvent) {
            if (BasicTreeUI.this.treeState != null && treeModelEvent != null) {
                BasicTreeUI.this.treeState.treeNodesRemoved(treeModelEvent);
                BasicTreeUI.this.updateLeadSelectionRow();
                TreePath treePath = SwingUtilities2.getTreePath(treeModelEvent, BasicTreeUI.this.getModel());
                if (BasicTreeUI.this.treeState.isExpanded(treePath) || BasicTreeUI.this.treeModel.getChildCount(treePath.getLastPathComponent()) == 0) {
                    BasicTreeUI.this.updateSize();
                }
            }
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeStructureChanged(TreeModelEvent treeModelEvent) {
            if (BasicTreeUI.this.treeState != null && treeModelEvent != null) {
                BasicTreeUI.this.treeState.treeStructureChanged(treeModelEvent);
                BasicTreeUI.this.updateLeadSelectionRow();
                TreePath treePath = SwingUtilities2.getTreePath(treeModelEvent, BasicTreeUI.this.getModel());
                if (treePath != null) {
                    treePath = treePath.getParentPath();
                }
                if (treePath == null || BasicTreeUI.this.treeState.isExpanded(treePath)) {
                    BasicTreeUI.this.updateSize();
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTreeUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String SELECT_PREVIOUS = "selectPrevious";
        private static final String SELECT_PREVIOUS_CHANGE_LEAD = "selectPreviousChangeLead";
        private static final String SELECT_PREVIOUS_EXTEND_SELECTION = "selectPreviousExtendSelection";
        private static final String SELECT_NEXT = "selectNext";
        private static final String SELECT_NEXT_CHANGE_LEAD = "selectNextChangeLead";
        private static final String SELECT_NEXT_EXTEND_SELECTION = "selectNextExtendSelection";
        private static final String SELECT_CHILD = "selectChild";
        private static final String SELECT_CHILD_CHANGE_LEAD = "selectChildChangeLead";
        private static final String SELECT_PARENT = "selectParent";
        private static final String SELECT_PARENT_CHANGE_LEAD = "selectParentChangeLead";
        private static final String SCROLL_UP_CHANGE_SELECTION = "scrollUpChangeSelection";
        private static final String SCROLL_UP_CHANGE_LEAD = "scrollUpChangeLead";
        private static final String SCROLL_UP_EXTEND_SELECTION = "scrollUpExtendSelection";
        private static final String SCROLL_DOWN_CHANGE_SELECTION = "scrollDownChangeSelection";
        private static final String SCROLL_DOWN_EXTEND_SELECTION = "scrollDownExtendSelection";
        private static final String SCROLL_DOWN_CHANGE_LEAD = "scrollDownChangeLead";
        private static final String SELECT_FIRST = "selectFirst";
        private static final String SELECT_FIRST_CHANGE_LEAD = "selectFirstChangeLead";
        private static final String SELECT_FIRST_EXTEND_SELECTION = "selectFirstExtendSelection";
        private static final String SELECT_LAST = "selectLast";
        private static final String SELECT_LAST_CHANGE_LEAD = "selectLastChangeLead";
        private static final String SELECT_LAST_EXTEND_SELECTION = "selectLastExtendSelection";
        private static final String TOGGLE = "toggle";
        private static final String CANCEL_EDITING = "cancel";
        private static final String START_EDITING = "startEditing";
        private static final String SELECT_ALL = "selectAll";
        private static final String CLEAR_SELECTION = "clearSelection";
        private static final String SCROLL_LEFT = "scrollLeft";
        private static final String SCROLL_RIGHT = "scrollRight";
        private static final String SCROLL_LEFT_EXTEND_SELECTION = "scrollLeftExtendSelection";
        private static final String SCROLL_RIGHT_EXTEND_SELECTION = "scrollRightExtendSelection";
        private static final String SCROLL_RIGHT_CHANGE_LEAD = "scrollRightChangeLead";
        private static final String SCROLL_LEFT_CHANGE_LEAD = "scrollLeftChangeLead";
        private static final String EXPAND = "expand";
        private static final String COLLAPSE = "collapse";
        private static final String MOVE_SELECTION_TO_PARENT = "moveSelectionToParent";
        private static final String ADD_TO_SELECTION = "addToSelection";
        private static final String TOGGLE_AND_ANCHOR = "toggleAndAnchor";
        private static final String EXTEND_TO = "extendTo";
        private static final String MOVE_SELECTION_TO = "moveSelectionTo";

        Actions() {
            super(null);
        }

        Actions(String str) {
            super(str);
        }

        @Override // sun.swing.UIAction
        public boolean isEnabled(Object obj) {
            if ((obj instanceof JTree) && getName() == CANCEL_EDITING) {
                return ((JTree) obj).isEditing();
            }
            return true;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTree jTree = (JTree) actionEvent.getSource();
            BasicTreeUI basicTreeUI = (BasicTreeUI) BasicLookAndFeel.getUIOfType(jTree.getUI(), BasicTreeUI.class);
            if (basicTreeUI == null) {
                return;
            }
            String name = getName();
            if (name == SELECT_PREVIOUS) {
                increment(jTree, basicTreeUI, -1, false, true);
                return;
            }
            if (name == SELECT_PREVIOUS_CHANGE_LEAD) {
                increment(jTree, basicTreeUI, -1, false, false);
                return;
            }
            if (name == SELECT_PREVIOUS_EXTEND_SELECTION) {
                increment(jTree, basicTreeUI, -1, true, true);
                return;
            }
            if (name == SELECT_NEXT) {
                increment(jTree, basicTreeUI, 1, false, true);
                return;
            }
            if (name == SELECT_NEXT_CHANGE_LEAD) {
                increment(jTree, basicTreeUI, 1, false, false);
                return;
            }
            if (name == SELECT_NEXT_EXTEND_SELECTION) {
                increment(jTree, basicTreeUI, 1, true, true);
                return;
            }
            if (name == SELECT_CHILD) {
                traverse(jTree, basicTreeUI, 1, true);
                return;
            }
            if (name == SELECT_CHILD_CHANGE_LEAD) {
                traverse(jTree, basicTreeUI, 1, false);
                return;
            }
            if (name == SELECT_PARENT) {
                traverse(jTree, basicTreeUI, -1, true);
                return;
            }
            if (name == SELECT_PARENT_CHANGE_LEAD) {
                traverse(jTree, basicTreeUI, -1, false);
                return;
            }
            if (name == SCROLL_UP_CHANGE_SELECTION) {
                page(jTree, basicTreeUI, -1, false, true);
                return;
            }
            if (name == SCROLL_UP_CHANGE_LEAD) {
                page(jTree, basicTreeUI, -1, false, false);
                return;
            }
            if (name == SCROLL_UP_EXTEND_SELECTION) {
                page(jTree, basicTreeUI, -1, true, true);
                return;
            }
            if (name == SCROLL_DOWN_CHANGE_SELECTION) {
                page(jTree, basicTreeUI, 1, false, true);
                return;
            }
            if (name == SCROLL_DOWN_EXTEND_SELECTION) {
                page(jTree, basicTreeUI, 1, true, true);
                return;
            }
            if (name == SCROLL_DOWN_CHANGE_LEAD) {
                page(jTree, basicTreeUI, 1, false, false);
                return;
            }
            if (name == SELECT_FIRST) {
                home(jTree, basicTreeUI, -1, false, true);
                return;
            }
            if (name == SELECT_FIRST_CHANGE_LEAD) {
                home(jTree, basicTreeUI, -1, false, false);
                return;
            }
            if (name == SELECT_FIRST_EXTEND_SELECTION) {
                home(jTree, basicTreeUI, -1, true, true);
                return;
            }
            if (name == SELECT_LAST) {
                home(jTree, basicTreeUI, 1, false, true);
                return;
            }
            if (name == SELECT_LAST_CHANGE_LEAD) {
                home(jTree, basicTreeUI, 1, false, false);
                return;
            }
            if (name == SELECT_LAST_EXTEND_SELECTION) {
                home(jTree, basicTreeUI, 1, true, true);
                return;
            }
            if (name == TOGGLE) {
                toggle(jTree, basicTreeUI);
                return;
            }
            if (name == CANCEL_EDITING) {
                cancelEditing(jTree, basicTreeUI);
                return;
            }
            if (name == START_EDITING) {
                startEditing(jTree, basicTreeUI);
                return;
            }
            if (name == SELECT_ALL) {
                selectAll(jTree, basicTreeUI, true);
                return;
            }
            if (name == CLEAR_SELECTION) {
                selectAll(jTree, basicTreeUI, false);
                return;
            }
            if (name == ADD_TO_SELECTION) {
                if (basicTreeUI.getRowCount(jTree) > 0) {
                    int leadSelectionRow = basicTreeUI.getLeadSelectionRow();
                    if (!jTree.isRowSelected(leadSelectionRow)) {
                        TreePath anchorSelectionPath = basicTreeUI.getAnchorSelectionPath();
                        jTree.addSelectionRow(leadSelectionRow);
                        basicTreeUI.setAnchorSelectionPath(anchorSelectionPath);
                        return;
                    }
                    return;
                }
                return;
            }
            if (name == TOGGLE_AND_ANCHOR) {
                if (basicTreeUI.getRowCount(jTree) > 0) {
                    int leadSelectionRow2 = basicTreeUI.getLeadSelectionRow();
                    TreePath leadSelectionPath = basicTreeUI.getLeadSelectionPath();
                    if (!jTree.isRowSelected(leadSelectionRow2)) {
                        jTree.addSelectionRow(leadSelectionRow2);
                    } else {
                        jTree.removeSelectionRow(leadSelectionRow2);
                        basicTreeUI.setLeadSelectionPath(leadSelectionPath);
                    }
                    basicTreeUI.setAnchorSelectionPath(leadSelectionPath);
                    return;
                }
                return;
            }
            if (name == EXTEND_TO) {
                extendSelection(jTree, basicTreeUI);
                return;
            }
            if (name == MOVE_SELECTION_TO) {
                if (basicTreeUI.getRowCount(jTree) > 0) {
                    int leadSelectionRow3 = basicTreeUI.getLeadSelectionRow();
                    jTree.setSelectionInterval(leadSelectionRow3, leadSelectionRow3);
                    return;
                }
                return;
            }
            if (name == SCROLL_LEFT) {
                scroll(jTree, basicTreeUI, 0, -10);
                return;
            }
            if (name == SCROLL_RIGHT) {
                scroll(jTree, basicTreeUI, 0, 10);
                return;
            }
            if (name == SCROLL_LEFT_EXTEND_SELECTION) {
                scrollChangeSelection(jTree, basicTreeUI, -1, true, true);
                return;
            }
            if (name == SCROLL_RIGHT_EXTEND_SELECTION) {
                scrollChangeSelection(jTree, basicTreeUI, 1, true, true);
                return;
            }
            if (name == SCROLL_RIGHT_CHANGE_LEAD) {
                scrollChangeSelection(jTree, basicTreeUI, 1, false, false);
                return;
            }
            if (name == SCROLL_LEFT_CHANGE_LEAD) {
                scrollChangeSelection(jTree, basicTreeUI, -1, false, false);
                return;
            }
            if (name == EXPAND) {
                expand(jTree, basicTreeUI);
            } else if (name == "collapse") {
                collapse(jTree, basicTreeUI);
            } else if (name == MOVE_SELECTION_TO_PARENT) {
                moveSelectionToParent(jTree, basicTreeUI);
            }
        }

        private void scrollChangeSelection(JTree jTree, BasicTreeUI basicTreeUI, int i2, boolean z2, boolean z3) {
            TreePath closestPathForLocation;
            if (basicTreeUI.getRowCount(jTree) > 0 && basicTreeUI.treeSelectionModel != null) {
                Rectangle visibleRect = jTree.getVisibleRect();
                if (i2 == -1) {
                    closestPathForLocation = basicTreeUI.getClosestPathForLocation(jTree, visibleRect.f12372x, visibleRect.f12373y);
                    visibleRect.f12372x = Math.max(0, visibleRect.f12372x - visibleRect.width);
                } else {
                    visibleRect.f12372x = Math.min(Math.max(0, jTree.getWidth() - visibleRect.width), visibleRect.f12372x + visibleRect.width);
                    closestPathForLocation = basicTreeUI.getClosestPathForLocation(jTree, visibleRect.f12372x, visibleRect.f12373y + visibleRect.height);
                }
                jTree.scrollRectToVisible(visibleRect);
                if (z2) {
                    basicTreeUI.extendSelection(closestPathForLocation);
                } else if (!z3) {
                    basicTreeUI.setLeadSelectionPath(closestPathForLocation, true);
                } else {
                    jTree.setSelectionPath(closestPathForLocation);
                }
            }
        }

        private void scroll(JTree jTree, BasicTreeUI basicTreeUI, int i2, int i3) {
            Rectangle visibleRect = jTree.getVisibleRect();
            Dimension size = jTree.getSize();
            if (i2 == 0) {
                visibleRect.f12372x += i3;
                visibleRect.f12372x = Math.max(0, visibleRect.f12372x);
                visibleRect.f12372x = Math.min(Math.max(0, size.width - visibleRect.width), visibleRect.f12372x);
            } else {
                visibleRect.f12373y += i3;
                visibleRect.f12373y = Math.max(0, visibleRect.f12373y);
                visibleRect.f12373y = Math.min(Math.max(0, size.width - visibleRect.height), visibleRect.f12373y);
            }
            jTree.scrollRectToVisible(visibleRect);
        }

        private void extendSelection(JTree jTree, BasicTreeUI basicTreeUI) {
            int leadSelectionRow;
            if (basicTreeUI.getRowCount(jTree) > 0 && (leadSelectionRow = basicTreeUI.getLeadSelectionRow()) != -1) {
                TreePath leadSelectionPath = basicTreeUI.getLeadSelectionPath();
                TreePath anchorSelectionPath = basicTreeUI.getAnchorSelectionPath();
                int rowForPath = basicTreeUI.getRowForPath(jTree, anchorSelectionPath);
                if (rowForPath == -1) {
                    rowForPath = 0;
                }
                jTree.setSelectionInterval(rowForPath, leadSelectionRow);
                basicTreeUI.setLeadSelectionPath(leadSelectionPath);
                basicTreeUI.setAnchorSelectionPath(anchorSelectionPath);
            }
        }

        private void selectAll(JTree jTree, BasicTreeUI basicTreeUI, boolean z2) {
            int rowCount = basicTreeUI.getRowCount(jTree);
            if (rowCount > 0) {
                if (!z2) {
                    TreePath leadSelectionPath = basicTreeUI.getLeadSelectionPath();
                    TreePath anchorSelectionPath = basicTreeUI.getAnchorSelectionPath();
                    jTree.clearSelection();
                    basicTreeUI.setAnchorSelectionPath(anchorSelectionPath);
                    basicTreeUI.setLeadSelectionPath(leadSelectionPath);
                    return;
                }
                if (jTree.getSelectionModel().getSelectionMode() != 1) {
                    TreePath leadSelectionPath2 = basicTreeUI.getLeadSelectionPath();
                    TreePath anchorSelectionPath2 = basicTreeUI.getAnchorSelectionPath();
                    if (leadSelectionPath2 != null && !jTree.isVisible(leadSelectionPath2)) {
                        leadSelectionPath2 = null;
                    }
                    jTree.setSelectionInterval(0, rowCount - 1);
                    if (leadSelectionPath2 != null) {
                        basicTreeUI.setLeadSelectionPath(leadSelectionPath2);
                    }
                    if (anchorSelectionPath2 != null && jTree.isVisible(anchorSelectionPath2)) {
                        basicTreeUI.setAnchorSelectionPath(anchorSelectionPath2);
                        return;
                    }
                    return;
                }
                int leadSelectionRow = basicTreeUI.getLeadSelectionRow();
                if (leadSelectionRow != -1) {
                    jTree.setSelectionRow(leadSelectionRow);
                } else if (jTree.getMinSelectionRow() == -1) {
                    jTree.setSelectionRow(0);
                    basicTreeUI.ensureRowsAreVisible(0, 0);
                }
            }
        }

        private void startEditing(JTree jTree, BasicTreeUI basicTreeUI) {
            TreePath leadSelectionPath = basicTreeUI.getLeadSelectionPath();
            if ((leadSelectionPath != null ? basicTreeUI.getRowForPath(jTree, leadSelectionPath) : -1) != -1) {
                jTree.startEditingAtPath(leadSelectionPath);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cancelEditing(JTree jTree, BasicTreeUI basicTreeUI) {
            jTree.cancelEditing();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void toggle(JTree jTree, BasicTreeUI basicTreeUI) {
            int leadSelectionRow = basicTreeUI.getLeadSelectionRow();
            if (leadSelectionRow != -1 && !basicTreeUI.isLeaf(leadSelectionRow)) {
                TreePath anchorSelectionPath = basicTreeUI.getAnchorSelectionPath();
                TreePath leadSelectionPath = basicTreeUI.getLeadSelectionPath();
                basicTreeUI.toggleExpandState(basicTreeUI.getPathForRow(jTree, leadSelectionRow));
                basicTreeUI.setAnchorSelectionPath(anchorSelectionPath);
                basicTreeUI.setLeadSelectionPath(leadSelectionPath);
            }
        }

        private void expand(JTree jTree, BasicTreeUI basicTreeUI) {
            jTree.expandRow(basicTreeUI.getLeadSelectionRow());
        }

        private void collapse(JTree jTree, BasicTreeUI basicTreeUI) {
            jTree.collapseRow(basicTreeUI.getLeadSelectionRow());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void increment(JTree jTree, BasicTreeUI basicTreeUI, int i2, boolean z2, boolean z3) {
            int rowCount;
            int iMin;
            if (!z2 && !z3 && jTree.getSelectionModel().getSelectionMode() != 4) {
                z3 = true;
            }
            if (basicTreeUI.treeSelectionModel != null && (rowCount = jTree.getRowCount()) > 0) {
                int leadSelectionRow = basicTreeUI.getLeadSelectionRow();
                if (leadSelectionRow == -1) {
                    if (i2 == 1) {
                        iMin = 0;
                    } else {
                        iMin = rowCount - 1;
                    }
                } else {
                    iMin = Math.min(rowCount - 1, Math.max(0, leadSelectionRow + i2));
                }
                if (z2 && basicTreeUI.treeSelectionModel.getSelectionMode() != 1) {
                    basicTreeUI.extendSelection(jTree.getPathForRow(iMin));
                } else if (!z3) {
                    basicTreeUI.setLeadSelectionPath(jTree.getPathForRow(iMin), true);
                } else {
                    jTree.setSelectionInterval(iMin, iMin);
                }
                basicTreeUI.ensureRowsAreVisible(iMin, iMin);
                basicTreeUI.lastSelectedRow = iMin;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void traverse(JTree jTree, BasicTreeUI basicTreeUI, int i2, boolean z2) {
            int rowForPath;
            if (!z2 && jTree.getSelectionModel().getSelectionMode() != 4) {
                z2 = true;
            }
            int rowCount = jTree.getRowCount();
            if (rowCount > 0) {
                int leadSelectionRow = basicTreeUI.getLeadSelectionRow();
                if (leadSelectionRow == -1) {
                    rowForPath = 0;
                } else if (i2 == 1) {
                    TreePath pathForRow = basicTreeUI.getPathForRow(jTree, leadSelectionRow);
                    int childCount = jTree.getModel().getChildCount(pathForRow.getLastPathComponent());
                    rowForPath = -1;
                    if (!basicTreeUI.isLeaf(leadSelectionRow)) {
                        if (!jTree.isExpanded(leadSelectionRow)) {
                            basicTreeUI.toggleExpandState(pathForRow);
                        } else if (childCount > 0) {
                            rowForPath = Math.min(leadSelectionRow + 1, rowCount - 1);
                        }
                    }
                } else if (!basicTreeUI.isLeaf(leadSelectionRow) && jTree.isExpanded(leadSelectionRow)) {
                    basicTreeUI.toggleExpandState(basicTreeUI.getPathForRow(jTree, leadSelectionRow));
                    rowForPath = -1;
                } else {
                    TreePath pathForRow2 = basicTreeUI.getPathForRow(jTree, leadSelectionRow);
                    rowForPath = (pathForRow2 == null || pathForRow2.getPathCount() <= 1) ? -1 : basicTreeUI.getRowForPath(jTree, pathForRow2.getParentPath());
                }
                if (rowForPath != -1) {
                    if (!z2) {
                        basicTreeUI.setLeadSelectionPath(basicTreeUI.getPathForRow(jTree, rowForPath), true);
                    } else {
                        jTree.setSelectionInterval(rowForPath, rowForPath);
                    }
                    basicTreeUI.ensureRowsAreVisible(rowForPath, rowForPath);
                }
            }
        }

        private void moveSelectionToParent(JTree jTree, BasicTreeUI basicTreeUI) {
            int rowForPath;
            TreePath pathForRow = basicTreeUI.getPathForRow(jTree, basicTreeUI.getLeadSelectionRow());
            if (pathForRow != null && pathForRow.getPathCount() > 1 && (rowForPath = basicTreeUI.getRowForPath(jTree, pathForRow.getParentPath())) != -1) {
                jTree.setSelectionInterval(rowForPath, rowForPath);
                basicTreeUI.ensureRowsAreVisible(rowForPath, rowForPath);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void page(JTree jTree, BasicTreeUI basicTreeUI, int i2, boolean z2, boolean z3) {
            TreePath closestPathForLocation;
            if (!z2 && !z3 && jTree.getSelectionModel().getSelectionMode() != 4) {
                z3 = true;
            }
            if (basicTreeUI.getRowCount(jTree) > 0 && basicTreeUI.treeSelectionModel != null) {
                Dimension size = jTree.getSize();
                TreePath leadSelectionPath = basicTreeUI.getLeadSelectionPath();
                Rectangle visibleRect = jTree.getVisibleRect();
                if (i2 == -1) {
                    closestPathForLocation = basicTreeUI.getClosestPathForLocation(jTree, visibleRect.f12372x, visibleRect.f12373y);
                    if (closestPathForLocation.equals(leadSelectionPath)) {
                        visibleRect.f12373y = Math.max(0, visibleRect.f12373y - visibleRect.height);
                        closestPathForLocation = jTree.getClosestPathForLocation(visibleRect.f12372x, visibleRect.f12373y);
                    }
                } else {
                    visibleRect.f12373y = Math.min(size.height, (visibleRect.f12373y + visibleRect.height) - 1);
                    closestPathForLocation = jTree.getClosestPathForLocation(visibleRect.f12372x, visibleRect.f12373y);
                    if (closestPathForLocation.equals(leadSelectionPath)) {
                        visibleRect.f12373y = Math.min(size.height, (visibleRect.f12373y + visibleRect.height) - 1);
                        closestPathForLocation = jTree.getClosestPathForLocation(visibleRect.f12372x, visibleRect.f12373y);
                    }
                }
                Rectangle pathBounds = basicTreeUI.getPathBounds(jTree, closestPathForLocation);
                if (pathBounds != null) {
                    pathBounds.f12372x = visibleRect.f12372x;
                    pathBounds.width = visibleRect.width;
                    if (i2 == -1) {
                        pathBounds.height = visibleRect.height;
                    } else {
                        pathBounds.f12373y -= visibleRect.height - pathBounds.height;
                        pathBounds.height = visibleRect.height;
                    }
                    if (z2) {
                        basicTreeUI.extendSelection(closestPathForLocation);
                    } else if (!z3) {
                        basicTreeUI.setLeadSelectionPath(closestPathForLocation, true);
                    } else {
                        jTree.setSelectionPath(closestPathForLocation);
                    }
                    jTree.scrollRectToVisible(pathBounds);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void home(JTree jTree, final BasicTreeUI basicTreeUI, int i2, boolean z2, boolean z3) {
            if (!z2 && !z3 && jTree.getSelectionModel().getSelectionMode() != 4) {
                z3 = true;
            }
            final int rowCount = basicTreeUI.getRowCount(jTree);
            if (rowCount > 0) {
                if (i2 == -1) {
                    basicTreeUI.ensureRowsAreVisible(0, 0);
                    if (z2) {
                        TreePath anchorSelectionPath = basicTreeUI.getAnchorSelectionPath();
                        int rowForPath = anchorSelectionPath == null ? -1 : basicTreeUI.getRowForPath(jTree, anchorSelectionPath);
                        if (rowForPath == -1) {
                            jTree.setSelectionInterval(0, 0);
                            return;
                        }
                        jTree.setSelectionInterval(0, rowForPath);
                        basicTreeUI.setAnchorSelectionPath(anchorSelectionPath);
                        basicTreeUI.setLeadSelectionPath(basicTreeUI.getPathForRow(jTree, 0));
                        return;
                    }
                    if (!z3) {
                        basicTreeUI.setLeadSelectionPath(basicTreeUI.getPathForRow(jTree, 0), true);
                        return;
                    } else {
                        jTree.setSelectionInterval(0, 0);
                        return;
                    }
                }
                basicTreeUI.ensureRowsAreVisible(rowCount - 1, rowCount - 1);
                if (z2) {
                    TreePath anchorSelectionPath2 = basicTreeUI.getAnchorSelectionPath();
                    int rowForPath2 = anchorSelectionPath2 == null ? -1 : basicTreeUI.getRowForPath(jTree, anchorSelectionPath2);
                    if (rowForPath2 == -1) {
                        jTree.setSelectionInterval(rowCount - 1, rowCount - 1);
                    } else {
                        jTree.setSelectionInterval(rowForPath2, rowCount - 1);
                        basicTreeUI.setAnchorSelectionPath(anchorSelectionPath2);
                        basicTreeUI.setLeadSelectionPath(basicTreeUI.getPathForRow(jTree, rowCount - 1));
                    }
                } else if (!z3) {
                    basicTreeUI.setLeadSelectionPath(basicTreeUI.getPathForRow(jTree, rowCount - 1), true);
                } else {
                    jTree.setSelectionInterval(rowCount - 1, rowCount - 1);
                }
                if (basicTreeUI.isLargeModel()) {
                    SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.plaf.basic.BasicTreeUI.Actions.1
                        @Override // java.lang.Runnable
                        public void run() {
                            basicTreeUI.ensureRowsAreVisible(rowCount - 1, rowCount - 1);
                        }
                    });
                }
            }
        }
    }
}
