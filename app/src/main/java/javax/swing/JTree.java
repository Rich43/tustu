package javax.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.beans.ConstructorProperties;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Stack;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleText;
import javax.accessibility.AccessibleValue;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TreeUI;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.RowMapper;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import sun.awt.AWTAccessor;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/JTree.class */
public class JTree extends JComponent implements Scrollable, Accessible {
    private static final String uiClassID = "TreeUI";
    protected transient TreeModel treeModel;
    protected transient TreeSelectionModel selectionModel;
    protected boolean rootVisible;
    protected transient TreeCellRenderer cellRenderer;
    protected int rowHeight;
    private boolean rowHeightSet;
    private transient Hashtable<TreePath, Boolean> expandedState;
    protected boolean showsRootHandles;
    private boolean showsRootHandlesSet;
    protected transient TreeSelectionRedirector selectionRedirector;
    protected transient TreeCellEditor cellEditor;
    protected boolean editable;
    protected boolean largeModel;
    protected int visibleRowCount;
    protected boolean invokesStopCellEditing;
    protected boolean scrollsOnExpand;
    private boolean scrollsOnExpandSet;
    protected int toggleClickCount;
    protected transient TreeModelListener treeModelListener;
    private transient Stack<Stack<TreePath>> expandedStack;
    private TreePath leadPath;
    private TreePath anchorPath;
    private boolean expandsSelectedPaths;
    private boolean settingUI;
    private boolean dragEnabled;
    private DropMode dropMode;
    private transient DropLocation dropLocation;
    private int expandRow;
    private TreeTimer dropTimer;
    private transient TreeExpansionListener uiTreeExpansionListener;
    private static int TEMP_STACK_SIZE;
    public static final String CELL_RENDERER_PROPERTY = "cellRenderer";
    public static final String TREE_MODEL_PROPERTY = "model";
    public static final String ROOT_VISIBLE_PROPERTY = "rootVisible";
    public static final String SHOWS_ROOT_HANDLES_PROPERTY = "showsRootHandles";
    public static final String ROW_HEIGHT_PROPERTY = "rowHeight";
    public static final String CELL_EDITOR_PROPERTY = "cellEditor";
    public static final String EDITABLE_PROPERTY = "editable";
    public static final String LARGE_MODEL_PROPERTY = "largeModel";
    public static final String SELECTION_MODEL_PROPERTY = "selectionModel";
    public static final String VISIBLE_ROW_COUNT_PROPERTY = "visibleRowCount";
    public static final String INVOKES_STOP_CELL_EDITING_PROPERTY = "invokesStopCellEditing";
    public static final String SCROLLS_ON_EXPAND_PROPERTY = "scrollsOnExpand";
    public static final String TOGGLE_CLICK_COUNT_PROPERTY = "toggleClickCount";
    public static final String LEAD_SELECTION_PATH_PROPERTY = "leadSelectionPath";
    public static final String ANCHOR_SELECTION_PATH_PROPERTY = "anchorSelectionPath";
    public static final String EXPANDS_SELECTED_PATHS_PROPERTY = "expandsSelectedPaths";
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JTree.class.desiredAssertionStatus();
        TEMP_STACK_SIZE = 11;
    }

    /* loaded from: rt.jar:javax/swing/JTree$DropLocation.class */
    public static final class DropLocation extends TransferHandler.DropLocation {
        private final TreePath path;
        private final int index;

        private DropLocation(Point point, TreePath treePath, int i2) {
            super(point);
            this.path = treePath;
            this.index = i2;
        }

        public int getChildIndex() {
            return this.index;
        }

        public TreePath getPath() {
            return this.path;
        }

        @Override // javax.swing.TransferHandler.DropLocation
        public String toString() {
            return getClass().getName() + "[dropPoint=" + ((Object) getDropPoint()) + ",path=" + ((Object) this.path) + ",childIndex=" + this.index + "]";
        }
    }

    /* loaded from: rt.jar:javax/swing/JTree$TreeTimer.class */
    private class TreeTimer extends Timer {
        public TreeTimer() {
            super(2000, null);
            setRepeats(false);
        }

        @Override // javax.swing.Timer
        public void fireActionPerformed(ActionEvent actionEvent) {
            JTree.this.expandRow(JTree.this.expandRow);
        }
    }

    protected static TreeModel getDefaultTreeModel() {
        DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode("JTree");
        DefaultMutableTreeNode defaultMutableTreeNode2 = new DefaultMutableTreeNode("colors");
        defaultMutableTreeNode.add(defaultMutableTreeNode2);
        defaultMutableTreeNode2.add(new DefaultMutableTreeNode("blue"));
        defaultMutableTreeNode2.add(new DefaultMutableTreeNode("violet"));
        defaultMutableTreeNode2.add(new DefaultMutableTreeNode("red"));
        defaultMutableTreeNode2.add(new DefaultMutableTreeNode("yellow"));
        DefaultMutableTreeNode defaultMutableTreeNode3 = new DefaultMutableTreeNode("sports");
        defaultMutableTreeNode.add(defaultMutableTreeNode3);
        defaultMutableTreeNode3.add(new DefaultMutableTreeNode("basketball"));
        defaultMutableTreeNode3.add(new DefaultMutableTreeNode("soccer"));
        defaultMutableTreeNode3.add(new DefaultMutableTreeNode("football"));
        defaultMutableTreeNode3.add(new DefaultMutableTreeNode("hockey"));
        DefaultMutableTreeNode defaultMutableTreeNode4 = new DefaultMutableTreeNode("food");
        defaultMutableTreeNode.add(defaultMutableTreeNode4);
        defaultMutableTreeNode4.add(new DefaultMutableTreeNode("hot dogs"));
        defaultMutableTreeNode4.add(new DefaultMutableTreeNode("pizza"));
        defaultMutableTreeNode4.add(new DefaultMutableTreeNode("ravioli"));
        defaultMutableTreeNode4.add(new DefaultMutableTreeNode("bananas"));
        return new DefaultTreeModel(defaultMutableTreeNode);
    }

    protected static TreeModel createTreeModel(Object obj) {
        DefaultMutableTreeNode defaultMutableTreeNode;
        if ((obj instanceof Object[]) || (obj instanceof Hashtable) || (obj instanceof Vector)) {
            defaultMutableTreeNode = new DefaultMutableTreeNode("root");
            DynamicUtilTreeNode.createChildren(defaultMutableTreeNode, obj);
        } else {
            defaultMutableTreeNode = new DynamicUtilTreeNode("root", obj);
        }
        return new DefaultTreeModel(defaultMutableTreeNode, false);
    }

    public JTree() {
        this(getDefaultTreeModel());
    }

    public JTree(Object[] objArr) {
        this(createTreeModel(objArr));
        setRootVisible(false);
        setShowsRootHandles(true);
        expandRoot();
    }

    public JTree(Vector<?> vector) {
        this(createTreeModel(vector));
        setRootVisible(false);
        setShowsRootHandles(true);
        expandRoot();
    }

    public JTree(Hashtable<?, ?> hashtable) {
        this(createTreeModel(hashtable));
        setRootVisible(false);
        setShowsRootHandles(true);
        expandRoot();
    }

    public JTree(TreeNode treeNode) {
        this(treeNode, false);
    }

    public JTree(TreeNode treeNode, boolean z2) {
        this(new DefaultTreeModel(treeNode, z2));
    }

    @ConstructorProperties({"model"})
    public JTree(TreeModel treeModel) {
        this.rowHeightSet = false;
        this.showsRootHandlesSet = false;
        this.scrollsOnExpandSet = false;
        this.dropMode = DropMode.USE_SELECTION;
        this.expandRow = -1;
        this.expandedStack = new Stack<>();
        this.toggleClickCount = 2;
        this.expandedState = new Hashtable<>();
        setLayout(null);
        this.rowHeight = 16;
        this.visibleRowCount = 20;
        this.rootVisible = true;
        this.selectionModel = new DefaultTreeSelectionModel();
        this.cellRenderer = null;
        this.scrollsOnExpand = true;
        setOpaque(true);
        this.expandsSelectedPaths = true;
        updateUI();
        setModel(treeModel);
    }

    public TreeUI getUI() {
        return (TreeUI) this.ui;
    }

    public void setUI(TreeUI treeUI) {
        if (this.ui != treeUI) {
            this.settingUI = true;
            this.uiTreeExpansionListener = null;
            try {
                super.setUI((ComponentUI) treeUI);
            } finally {
                this.settingUI = false;
            }
        }
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((TreeUI) UIManager.getUI(this));
        SwingUtilities.updateRendererOrEditorUI(getCellRenderer());
        SwingUtilities.updateRendererOrEditorUI(getCellEditor());
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public TreeCellRenderer getCellRenderer() {
        return this.cellRenderer;
    }

    public void setCellRenderer(TreeCellRenderer treeCellRenderer) {
        TreeCellRenderer treeCellRenderer2 = this.cellRenderer;
        this.cellRenderer = treeCellRenderer;
        firePropertyChange("cellRenderer", treeCellRenderer2, this.cellRenderer);
        invalidate();
    }

    public void setEditable(boolean z2) {
        boolean z3 = this.editable;
        this.editable = z2;
        firePropertyChange(EDITABLE_PROPERTY, z3, z2);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, z3 ? AccessibleState.EDITABLE : null, z2 ? AccessibleState.EDITABLE : null);
        }
    }

    public boolean isEditable() {
        return this.editable;
    }

    public void setCellEditor(TreeCellEditor treeCellEditor) {
        TreeCellEditor treeCellEditor2 = this.cellEditor;
        this.cellEditor = treeCellEditor;
        firePropertyChange(CELL_EDITOR_PROPERTY, treeCellEditor2, treeCellEditor);
        invalidate();
    }

    public TreeCellEditor getCellEditor() {
        return this.cellEditor;
    }

    public TreeModel getModel() {
        return this.treeModel;
    }

    public void setModel(TreeModel treeModel) {
        clearSelection();
        TreeModel treeModel2 = this.treeModel;
        if (this.treeModel != null && this.treeModelListener != null) {
            this.treeModel.removeTreeModelListener(this.treeModelListener);
        }
        if (this.accessibleContext != null) {
            if (this.treeModel != null) {
                this.treeModel.removeTreeModelListener((TreeModelListener) this.accessibleContext);
            }
            if (treeModel != null) {
                treeModel.addTreeModelListener((TreeModelListener) this.accessibleContext);
            }
        }
        this.treeModel = treeModel;
        clearToggledPaths();
        if (this.treeModel != null) {
            if (this.treeModelListener == null) {
                this.treeModelListener = createTreeModelListener();
            }
            if (this.treeModelListener != null) {
                this.treeModel.addTreeModelListener(this.treeModelListener);
            }
            Object root = this.treeModel.getRoot();
            if (root != null && !this.treeModel.isLeaf(root)) {
                this.expandedState.put(new TreePath(root), Boolean.TRUE);
            }
        }
        firePropertyChange("model", treeModel2, this.treeModel);
        invalidate();
    }

    public boolean isRootVisible() {
        return this.rootVisible;
    }

    public void setRootVisible(boolean z2) {
        boolean z3 = this.rootVisible;
        this.rootVisible = z2;
        firePropertyChange(ROOT_VISIBLE_PROPERTY, z3, this.rootVisible);
        if (this.accessibleContext != null) {
            ((AccessibleJTree) this.accessibleContext).fireVisibleDataPropertyChange();
        }
    }

    public void setShowsRootHandles(boolean z2) {
        boolean z3 = this.showsRootHandles;
        getModel();
        this.showsRootHandles = z2;
        this.showsRootHandlesSet = true;
        firePropertyChange(SHOWS_ROOT_HANDLES_PROPERTY, z3, this.showsRootHandles);
        if (this.accessibleContext != null) {
            ((AccessibleJTree) this.accessibleContext).fireVisibleDataPropertyChange();
        }
        invalidate();
    }

    public boolean getShowsRootHandles() {
        return this.showsRootHandles;
    }

    public void setRowHeight(int i2) {
        int i3 = this.rowHeight;
        this.rowHeight = i2;
        this.rowHeightSet = true;
        firePropertyChange(ROW_HEIGHT_PROPERTY, i3, this.rowHeight);
        invalidate();
    }

    public int getRowHeight() {
        return this.rowHeight;
    }

    public boolean isFixedRowHeight() {
        return this.rowHeight > 0;
    }

    public void setLargeModel(boolean z2) {
        boolean z3 = this.largeModel;
        this.largeModel = z2;
        firePropertyChange(LARGE_MODEL_PROPERTY, z3, z2);
    }

    public boolean isLargeModel() {
        return this.largeModel;
    }

    public void setInvokesStopCellEditing(boolean z2) {
        boolean z3 = this.invokesStopCellEditing;
        this.invokesStopCellEditing = z2;
        firePropertyChange(INVOKES_STOP_CELL_EDITING_PROPERTY, z3, z2);
    }

    public boolean getInvokesStopCellEditing() {
        return this.invokesStopCellEditing;
    }

    public void setScrollsOnExpand(boolean z2) {
        boolean z3 = this.scrollsOnExpand;
        this.scrollsOnExpand = z2;
        this.scrollsOnExpandSet = true;
        firePropertyChange(SCROLLS_ON_EXPAND_PROPERTY, z3, z2);
    }

    public boolean getScrollsOnExpand() {
        return this.scrollsOnExpand;
    }

    public void setToggleClickCount(int i2) {
        int i3 = this.toggleClickCount;
        this.toggleClickCount = i2;
        firePropertyChange(TOGGLE_CLICK_COUNT_PROPERTY, i3, i2);
    }

    public int getToggleClickCount() {
        return this.toggleClickCount;
    }

    public void setExpandsSelectedPaths(boolean z2) {
        boolean z3 = this.expandsSelectedPaths;
        this.expandsSelectedPaths = z2;
        firePropertyChange(EXPANDS_SELECTED_PATHS_PROPERTY, z3, z2);
    }

    public boolean getExpandsSelectedPaths() {
        return this.expandsSelectedPaths;
    }

    public void setDragEnabled(boolean z2) {
        if (z2 && GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        this.dragEnabled = z2;
    }

    public boolean getDragEnabled() {
        return this.dragEnabled;
    }

    public final void setDropMode(DropMode dropMode) {
        if (dropMode != null) {
            switch (dropMode) {
                case USE_SELECTION:
                case ON:
                case INSERT:
                case ON_OR_INSERT:
                    this.dropMode = dropMode;
                    return;
            }
        }
        throw new IllegalArgumentException(((Object) dropMode) + ": Unsupported drop mode for tree");
    }

    public final DropMode getDropMode() {
        return this.dropMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javax.swing.JComponent
    public DropLocation dropLocationForPoint(Point point) {
        TreePath pathForRow;
        TreePath parentPath;
        DropLocation dropLocation = null;
        int closestRowForLocation = getClosestRowForLocation(point.f12370x, point.f12371y);
        Rectangle rowBounds = getRowBounds(closestRowForLocation);
        TreeModel model = getModel();
        Object root = model == null ? null : model.getRoot();
        TreePath treePath = root == null ? null : new TreePath(root);
        boolean z2 = closestRowForLocation == -1 || point.f12371y < rowBounds.f12373y || point.f12371y >= rowBounds.f12373y + rowBounds.height;
        switch (this.dropMode) {
            case USE_SELECTION:
            case ON:
                if (z2) {
                    dropLocation = new DropLocation(point, null, -1);
                    break;
                } else {
                    dropLocation = new DropLocation(point, getPathForRow(closestRowForLocation), -1);
                    break;
                }
            case INSERT:
            case ON_OR_INSERT:
                if (closestRowForLocation == -1) {
                    if (root != null && !model.isLeaf(root) && isExpanded(treePath)) {
                        dropLocation = new DropLocation(point, treePath, 0);
                        break;
                    } else {
                        dropLocation = new DropLocation(point, null, -1);
                        break;
                    }
                } else {
                    boolean z3 = this.dropMode == DropMode.ON_OR_INSERT || !model.isLeaf(getPathForRow(closestRowForLocation).getLastPathComponent());
                    SwingUtilities2.Section sectionLiesInVertical = SwingUtilities2.liesInVertical(rowBounds, point, z3);
                    if (sectionLiesInVertical == SwingUtilities2.Section.LEADING) {
                        pathForRow = getPathForRow(closestRowForLocation);
                        parentPath = pathForRow.getParentPath();
                    } else if (sectionLiesInVertical == SwingUtilities2.Section.TRAILING) {
                        int i2 = closestRowForLocation + 1;
                        if (i2 >= getRowCount()) {
                            if (model.isLeaf(root) || !isExpanded(treePath)) {
                                dropLocation = new DropLocation(point, null, -1);
                                break;
                            } else {
                                dropLocation = new DropLocation(point, treePath, model.getChildCount(root));
                                break;
                            }
                        } else {
                            pathForRow = getPathForRow(i2);
                            parentPath = pathForRow.getParentPath();
                        }
                    } else {
                        if (!$assertionsDisabled && !z3) {
                            throw new AssertionError();
                        }
                        dropLocation = new DropLocation(point, getPathForRow(closestRowForLocation), -1);
                        break;
                    }
                    if (parentPath != null) {
                        dropLocation = new DropLocation(point, parentPath, model.getIndexOfChild(parentPath.getLastPathComponent(), pathForRow.getLastPathComponent()));
                        break;
                    } else if (z3 || !model.isLeaf(root)) {
                        dropLocation = new DropLocation(point, treePath, -1);
                        break;
                    } else {
                        dropLocation = new DropLocation(point, null, -1);
                        break;
                    }
                }
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError((Object) "Unexpected drop mode");
                }
                break;
        }
        if (z2 || closestRowForLocation != this.expandRow) {
            cancelDropTimer();
        }
        if (!z2 && closestRowForLocation != this.expandRow && isCollapsed(closestRowForLocation)) {
            this.expandRow = closestRowForLocation;
            startDropTimer();
        }
        return dropLocation;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.swing.JComponent
    Object setDropLocation(TransferHandler.DropLocation dropLocation, Object obj, boolean z2) {
        Object obj2 = null;
        DropLocation dropLocation2 = (DropLocation) dropLocation;
        if (this.dropMode == DropMode.USE_SELECTION) {
            if (dropLocation2 == null) {
                if (!z2 && obj != null) {
                    setSelectionPaths(((TreePath[][]) obj)[0]);
                    setAnchorSelectionPath(((TreePath[][]) obj)[1][0]);
                    setLeadSelectionPath(((TreePath[][]) obj)[1][1]);
                }
            } else {
                if (this.dropLocation == null) {
                    TreePath[] selectionPaths = getSelectionPaths();
                    if (selectionPaths == null) {
                        selectionPaths = new TreePath[0];
                    }
                    obj2 = new TreePath[]{selectionPaths, new TreePath[]{getAnchorSelectionPath(), getLeadSelectionPath()}};
                } else {
                    obj2 = obj;
                }
                setSelectionPath(dropLocation2.getPath());
            }
        }
        DropLocation dropLocation3 = this.dropLocation;
        this.dropLocation = dropLocation2;
        firePropertyChange("dropLocation", dropLocation3, this.dropLocation);
        return obj2;
    }

    @Override // javax.swing.JComponent
    void dndDone() {
        cancelDropTimer();
        this.dropTimer = null;
    }

    public final DropLocation getDropLocation() {
        return this.dropLocation;
    }

    private void startDropTimer() {
        if (this.dropTimer == null) {
            this.dropTimer = new TreeTimer();
        }
        this.dropTimer.start();
    }

    private void cancelDropTimer() {
        if (this.dropTimer != null && this.dropTimer.isRunning()) {
            this.expandRow = -1;
            this.dropTimer.stop();
        }
    }

    public boolean isPathEditable(TreePath treePath) {
        return isEditable();
    }

    @Override // javax.swing.JComponent
    public String getToolTipText(MouseEvent mouseEvent) {
        String toolTipText = null;
        if (mouseEvent != null) {
            Point point = mouseEvent.getPoint();
            int rowForLocation = getRowForLocation(point.f12370x, point.f12371y);
            TreeCellRenderer cellRenderer = getCellRenderer();
            if (rowForLocation != -1 && cellRenderer != null) {
                TreePath pathForRow = getPathForRow(rowForLocation);
                Object lastPathComponent = pathForRow.getLastPathComponent();
                Component treeCellRendererComponent = cellRenderer.getTreeCellRendererComponent(this, lastPathComponent, isRowSelected(rowForLocation), isExpanded(rowForLocation), getModel().isLeaf(lastPathComponent), rowForLocation, true);
                if (treeCellRendererComponent instanceof JComponent) {
                    Rectangle pathBounds = getPathBounds(pathForRow);
                    point.translate(-pathBounds.f12372x, -pathBounds.f12373y);
                    MouseEvent mouseEvent2 = new MouseEvent(treeCellRendererComponent, mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), point.f12370x, point.f12371y, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
                    AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
                    mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
                    toolTipText = ((JComponent) treeCellRendererComponent).getToolTipText(mouseEvent2);
                }
            }
        }
        if (toolTipText == null) {
            toolTipText = getToolTipText();
        }
        return toolTipText;
    }

    public String convertValueToText(Object obj, boolean z2, boolean z3, boolean z4, int i2, boolean z5) {
        String string;
        if (obj != null && (string = obj.toString()) != null) {
            return string;
        }
        return "";
    }

    public int getRowCount() {
        TreeUI ui = getUI();
        if (ui != null) {
            return ui.getRowCount(this);
        }
        return 0;
    }

    public void setSelectionPath(TreePath treePath) {
        getSelectionModel().setSelectionPath(treePath);
    }

    public void setSelectionPaths(TreePath[] treePathArr) {
        getSelectionModel().setSelectionPaths(treePathArr);
    }

    public void setLeadSelectionPath(TreePath treePath) {
        TreePath treePath2 = this.leadPath;
        this.leadPath = treePath;
        firePropertyChange(LEAD_SELECTION_PATH_PROPERTY, treePath2, treePath);
        if (this.accessibleContext != null) {
            ((AccessibleJTree) this.accessibleContext).fireActiveDescendantPropertyChange(treePath2, treePath);
        }
    }

    public void setAnchorSelectionPath(TreePath treePath) {
        TreePath treePath2 = this.anchorPath;
        this.anchorPath = treePath;
        firePropertyChange(ANCHOR_SELECTION_PATH_PROPERTY, treePath2, treePath);
    }

    public void setSelectionRow(int i2) {
        setSelectionRows(new int[]{i2});
    }

    public void setSelectionRows(int[] iArr) {
        TreeUI ui = getUI();
        if (ui != null && iArr != null) {
            int length = iArr.length;
            TreePath[] treePathArr = new TreePath[length];
            for (int i2 = 0; i2 < length; i2++) {
                treePathArr[i2] = ui.getPathForRow(this, iArr[i2]);
            }
            setSelectionPaths(treePathArr);
        }
    }

    public void addSelectionPath(TreePath treePath) {
        getSelectionModel().addSelectionPath(treePath);
    }

    public void addSelectionPaths(TreePath[] treePathArr) {
        getSelectionModel().addSelectionPaths(treePathArr);
    }

    public void addSelectionRow(int i2) {
        addSelectionRows(new int[]{i2});
    }

    public void addSelectionRows(int[] iArr) {
        TreeUI ui = getUI();
        if (ui != null && iArr != null) {
            int length = iArr.length;
            TreePath[] treePathArr = new TreePath[length];
            for (int i2 = 0; i2 < length; i2++) {
                treePathArr[i2] = ui.getPathForRow(this, iArr[i2]);
            }
            addSelectionPaths(treePathArr);
        }
    }

    public Object getLastSelectedPathComponent() {
        TreePath selectionPath = getSelectionModel().getSelectionPath();
        if (selectionPath != null) {
            return selectionPath.getLastPathComponent();
        }
        return null;
    }

    public TreePath getLeadSelectionPath() {
        return this.leadPath;
    }

    public TreePath getAnchorSelectionPath() {
        return this.anchorPath;
    }

    public TreePath getSelectionPath() {
        return getSelectionModel().getSelectionPath();
    }

    public TreePath[] getSelectionPaths() {
        TreePath[] selectionPaths = getSelectionModel().getSelectionPaths();
        if (selectionPaths == null || selectionPaths.length <= 0) {
            return null;
        }
        return selectionPaths;
    }

    public int[] getSelectionRows() {
        return getSelectionModel().getSelectionRows();
    }

    public int getSelectionCount() {
        return this.selectionModel.getSelectionCount();
    }

    public int getMinSelectionRow() {
        return getSelectionModel().getMinSelectionRow();
    }

    public int getMaxSelectionRow() {
        return getSelectionModel().getMaxSelectionRow();
    }

    public int getLeadSelectionRow() {
        TreePath leadSelectionPath = getLeadSelectionPath();
        if (leadSelectionPath != null) {
            return getRowForPath(leadSelectionPath);
        }
        return -1;
    }

    public boolean isPathSelected(TreePath treePath) {
        return getSelectionModel().isPathSelected(treePath);
    }

    public boolean isRowSelected(int i2) {
        return getSelectionModel().isRowSelected(i2);
    }

    public Enumeration<TreePath> getExpandedDescendants(TreePath treePath) {
        if (!isExpanded(treePath)) {
            return null;
        }
        Enumeration<TreePath> enumerationKeys = this.expandedState.keys();
        Vector vector = null;
        if (enumerationKeys != null) {
            while (enumerationKeys.hasMoreElements()) {
                TreePath treePathNextElement = enumerationKeys.nextElement2();
                Boolean bool = this.expandedState.get(treePathNextElement);
                if (treePathNextElement != treePath && bool != null && bool.booleanValue() && treePath.isDescendant(treePathNextElement) && isVisible(treePathNextElement)) {
                    if (vector == null) {
                        vector = new Vector();
                    }
                    vector.addElement(treePathNextElement);
                }
            }
        }
        if (vector == null) {
            return Collections.enumeration(Collections.emptySet());
        }
        return vector.elements();
    }

    public boolean hasBeenExpanded(TreePath treePath) {
        return (treePath == null || this.expandedState.get(treePath) == null) ? false : true;
    }

    public boolean isExpanded(TreePath treePath) {
        TreePath parentPath;
        if (treePath == null) {
            return false;
        }
        do {
            Boolean bool = this.expandedState.get(treePath);
            if (bool == null || !bool.booleanValue()) {
                return false;
            }
            parentPath = treePath.getParentPath();
            treePath = parentPath;
        } while (parentPath != null);
        return true;
    }

    public boolean isExpanded(int i2) {
        TreePath pathForRow;
        Boolean bool;
        TreeUI ui = getUI();
        return (ui == null || (pathForRow = ui.getPathForRow(this, i2)) == null || (bool = this.expandedState.get(pathForRow)) == null || !bool.booleanValue()) ? false : true;
    }

    public boolean isCollapsed(TreePath treePath) {
        return !isExpanded(treePath);
    }

    public boolean isCollapsed(int i2) {
        return !isExpanded(i2);
    }

    public void makeVisible(TreePath treePath) {
        TreePath parentPath;
        if (treePath != null && (parentPath = treePath.getParentPath()) != null) {
            expandPath(parentPath);
        }
    }

    public boolean isVisible(TreePath treePath) {
        if (treePath != null) {
            TreePath parentPath = treePath.getParentPath();
            if (parentPath != null) {
                return isExpanded(parentPath);
            }
            return true;
        }
        return false;
    }

    public Rectangle getPathBounds(TreePath treePath) {
        TreeUI ui = getUI();
        if (ui != null) {
            return ui.getPathBounds(this, treePath);
        }
        return null;
    }

    public Rectangle getRowBounds(int i2) {
        return getPathBounds(getPathForRow(i2));
    }

    public void scrollPathToVisible(TreePath treePath) {
        if (treePath != null) {
            makeVisible(treePath);
            Rectangle pathBounds = getPathBounds(treePath);
            if (pathBounds != null) {
                scrollRectToVisible(pathBounds);
                if (this.accessibleContext != null) {
                    ((AccessibleJTree) this.accessibleContext).fireVisibleDataPropertyChange();
                }
            }
        }
    }

    public void scrollRowToVisible(int i2) {
        scrollPathToVisible(getPathForRow(i2));
    }

    public TreePath getPathForRow(int i2) {
        TreeUI ui = getUI();
        if (ui != null) {
            return ui.getPathForRow(this, i2);
        }
        return null;
    }

    public int getRowForPath(TreePath treePath) {
        TreeUI ui = getUI();
        if (ui != null) {
            return ui.getRowForPath(this, treePath);
        }
        return -1;
    }

    public void expandPath(TreePath treePath) {
        TreeModel model = getModel();
        if (treePath != null && model != null && !model.isLeaf(treePath.getLastPathComponent())) {
            setExpandedState(treePath, true);
        }
    }

    public void expandRow(int i2) {
        expandPath(getPathForRow(i2));
    }

    public void collapsePath(TreePath treePath) {
        setExpandedState(treePath, false);
    }

    public void collapseRow(int i2) {
        collapsePath(getPathForRow(i2));
    }

    public TreePath getPathForLocation(int i2, int i3) {
        Rectangle pathBounds;
        TreePath closestPathForLocation = getClosestPathForLocation(i2, i3);
        if (closestPathForLocation != null && (pathBounds = getPathBounds(closestPathForLocation)) != null && i2 >= pathBounds.f12372x && i2 < pathBounds.f12372x + pathBounds.width && i3 >= pathBounds.f12373y && i3 < pathBounds.f12373y + pathBounds.height) {
            return closestPathForLocation;
        }
        return null;
    }

    public int getRowForLocation(int i2, int i3) {
        return getRowForPath(getPathForLocation(i2, i3));
    }

    public TreePath getClosestPathForLocation(int i2, int i3) {
        TreeUI ui = getUI();
        if (ui != null) {
            return ui.getClosestPathForLocation(this, i2, i3);
        }
        return null;
    }

    public int getClosestRowForLocation(int i2, int i3) {
        return getRowForPath(getClosestPathForLocation(i2, i3));
    }

    public boolean isEditing() {
        TreeUI ui = getUI();
        if (ui != null) {
            return ui.isEditing(this);
        }
        return false;
    }

    public boolean stopEditing() {
        TreeUI ui = getUI();
        if (ui != null) {
            return ui.stopEditing(this);
        }
        return false;
    }

    public void cancelEditing() {
        TreeUI ui = getUI();
        if (ui != null) {
            ui.cancelEditing(this);
        }
    }

    public void startEditingAtPath(TreePath treePath) {
        TreeUI ui = getUI();
        if (ui != null) {
            ui.startEditingAtPath(this, treePath);
        }
    }

    public TreePath getEditingPath() {
        TreeUI ui = getUI();
        if (ui != null) {
            return ui.getEditingPath(this);
        }
        return null;
    }

    public void setSelectionModel(TreeSelectionModel treeSelectionModel) {
        if (treeSelectionModel == null) {
            treeSelectionModel = EmptySelectionModel.sharedInstance();
        }
        TreeSelectionModel treeSelectionModel2 = this.selectionModel;
        if (this.selectionModel != null && this.selectionRedirector != null) {
            this.selectionModel.removeTreeSelectionListener(this.selectionRedirector);
        }
        if (this.accessibleContext != null) {
            this.selectionModel.removeTreeSelectionListener((TreeSelectionListener) this.accessibleContext);
            treeSelectionModel.addTreeSelectionListener((TreeSelectionListener) this.accessibleContext);
        }
        this.selectionModel = treeSelectionModel;
        if (this.selectionRedirector != null) {
            this.selectionModel.addTreeSelectionListener(this.selectionRedirector);
        }
        firePropertyChange("selectionModel", treeSelectionModel2, this.selectionModel);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_SELECTION_PROPERTY, false, true);
        }
    }

    public TreeSelectionModel getSelectionModel() {
        return this.selectionModel;
    }

    protected TreePath[] getPathBetweenRows(int i2, int i3) {
        int rowCount;
        TreeUI ui = getUI();
        if (ui != null && (rowCount = getRowCount()) > 0 && ((i2 >= 0 || i3 >= 0) && (i2 < rowCount || i3 < rowCount))) {
            int iMin = Math.min(rowCount - 1, Math.max(i2, 0));
            int iMin2 = Math.min(rowCount - 1, Math.max(i3, 0));
            int iMin3 = Math.min(iMin, iMin2);
            int iMax = Math.max(iMin, iMin2);
            TreePath[] treePathArr = new TreePath[(iMax - iMin3) + 1];
            for (int i4 = iMin3; i4 <= iMax; i4++) {
                treePathArr[i4 - iMin3] = ui.getPathForRow(this, i4);
            }
            return treePathArr;
        }
        return new TreePath[0];
    }

    public void setSelectionInterval(int i2, int i3) {
        getSelectionModel().setSelectionPaths(getPathBetweenRows(i2, i3));
    }

    public void addSelectionInterval(int i2, int i3) {
        TreePath[] pathBetweenRows = getPathBetweenRows(i2, i3);
        if (pathBetweenRows != null && pathBetweenRows.length > 0) {
            getSelectionModel().addSelectionPaths(pathBetweenRows);
        }
    }

    public void removeSelectionInterval(int i2, int i3) {
        TreePath[] pathBetweenRows = getPathBetweenRows(i2, i3);
        if (pathBetweenRows != null && pathBetweenRows.length > 0) {
            getSelectionModel().removeSelectionPaths(pathBetweenRows);
        }
    }

    public void removeSelectionPath(TreePath treePath) {
        getSelectionModel().removeSelectionPath(treePath);
    }

    public void removeSelectionPaths(TreePath[] treePathArr) {
        getSelectionModel().removeSelectionPaths(treePathArr);
    }

    public void removeSelectionRow(int i2) {
        removeSelectionRows(new int[]{i2});
    }

    public void removeSelectionRows(int[] iArr) {
        TreeUI ui = getUI();
        if (ui != null && iArr != null) {
            int length = iArr.length;
            TreePath[] treePathArr = new TreePath[length];
            for (int i2 = 0; i2 < length; i2++) {
                treePathArr[i2] = ui.getPathForRow(this, iArr[i2]);
            }
            removeSelectionPaths(treePathArr);
        }
    }

    public void clearSelection() {
        getSelectionModel().clearSelection();
    }

    public boolean isSelectionEmpty() {
        return getSelectionModel().isSelectionEmpty();
    }

    public void addTreeExpansionListener(TreeExpansionListener treeExpansionListener) {
        if (this.settingUI) {
            this.uiTreeExpansionListener = treeExpansionListener;
        }
        this.listenerList.add(TreeExpansionListener.class, treeExpansionListener);
    }

    public void removeTreeExpansionListener(TreeExpansionListener treeExpansionListener) {
        this.listenerList.remove(TreeExpansionListener.class, treeExpansionListener);
        if (this.uiTreeExpansionListener == treeExpansionListener) {
            this.uiTreeExpansionListener = null;
        }
    }

    public TreeExpansionListener[] getTreeExpansionListeners() {
        return (TreeExpansionListener[]) this.listenerList.getListeners(TreeExpansionListener.class);
    }

    public void addTreeWillExpandListener(TreeWillExpandListener treeWillExpandListener) {
        this.listenerList.add(TreeWillExpandListener.class, treeWillExpandListener);
    }

    public void removeTreeWillExpandListener(TreeWillExpandListener treeWillExpandListener) {
        this.listenerList.remove(TreeWillExpandListener.class, treeWillExpandListener);
    }

    public TreeWillExpandListener[] getTreeWillExpandListeners() {
        return (TreeWillExpandListener[]) this.listenerList.getListeners(TreeWillExpandListener.class);
    }

    public void fireTreeExpanded(TreePath treePath) {
        Object[] listenerList = this.listenerList.getListenerList();
        TreeExpansionEvent treeExpansionEvent = null;
        if (this.uiTreeExpansionListener != null) {
            treeExpansionEvent = new TreeExpansionEvent(this, treePath);
            this.uiTreeExpansionListener.treeExpanded(treeExpansionEvent);
        }
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TreeExpansionListener.class && listenerList[length + 1] != this.uiTreeExpansionListener) {
                if (treeExpansionEvent == null) {
                    treeExpansionEvent = new TreeExpansionEvent(this, treePath);
                }
                ((TreeExpansionListener) listenerList[length + 1]).treeExpanded(treeExpansionEvent);
            }
        }
    }

    public void fireTreeCollapsed(TreePath treePath) {
        Object[] listenerList = this.listenerList.getListenerList();
        TreeExpansionEvent treeExpansionEvent = null;
        if (this.uiTreeExpansionListener != null) {
            treeExpansionEvent = new TreeExpansionEvent(this, treePath);
            this.uiTreeExpansionListener.treeCollapsed(treeExpansionEvent);
        }
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TreeExpansionListener.class && listenerList[length + 1] != this.uiTreeExpansionListener) {
                if (treeExpansionEvent == null) {
                    treeExpansionEvent = new TreeExpansionEvent(this, treePath);
                }
                ((TreeExpansionListener) listenerList[length + 1]).treeCollapsed(treeExpansionEvent);
            }
        }
    }

    public void fireTreeWillExpand(TreePath treePath) throws ExpandVetoException {
        Object[] listenerList = this.listenerList.getListenerList();
        TreeExpansionEvent treeExpansionEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TreeWillExpandListener.class) {
                if (treeExpansionEvent == null) {
                    treeExpansionEvent = new TreeExpansionEvent(this, treePath);
                }
                ((TreeWillExpandListener) listenerList[length + 1]).treeWillExpand(treeExpansionEvent);
            }
        }
    }

    public void fireTreeWillCollapse(TreePath treePath) throws ExpandVetoException {
        Object[] listenerList = this.listenerList.getListenerList();
        TreeExpansionEvent treeExpansionEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TreeWillExpandListener.class) {
                if (treeExpansionEvent == null) {
                    treeExpansionEvent = new TreeExpansionEvent(this, treePath);
                }
                ((TreeWillExpandListener) listenerList[length + 1]).treeWillCollapse(treeExpansionEvent);
            }
        }
    }

    public void addTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
        this.listenerList.add(TreeSelectionListener.class, treeSelectionListener);
        if (this.listenerList.getListenerCount(TreeSelectionListener.class) != 0 && this.selectionRedirector == null) {
            this.selectionRedirector = new TreeSelectionRedirector();
            this.selectionModel.addTreeSelectionListener(this.selectionRedirector);
        }
    }

    public void removeTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
        this.listenerList.remove(TreeSelectionListener.class, treeSelectionListener);
        if (this.listenerList.getListenerCount(TreeSelectionListener.class) == 0 && this.selectionRedirector != null) {
            this.selectionModel.removeTreeSelectionListener(this.selectionRedirector);
            this.selectionRedirector = null;
        }
    }

    public TreeSelectionListener[] getTreeSelectionListeners() {
        return (TreeSelectionListener[]) this.listenerList.getListeners(TreeSelectionListener.class);
    }

    protected void fireValueChanged(TreeSelectionEvent treeSelectionEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TreeSelectionListener.class) {
                ((TreeSelectionListener) listenerList[length + 1]).valueChanged(treeSelectionEvent);
            }
        }
    }

    public void treeDidChange() {
        revalidate();
        repaint();
    }

    public void setVisibleRowCount(int i2) {
        int i3 = this.visibleRowCount;
        this.visibleRowCount = i2;
        firePropertyChange(VISIBLE_ROW_COUNT_PROPERTY, i3, this.visibleRowCount);
        invalidate();
        if (this.accessibleContext != null) {
            ((AccessibleJTree) this.accessibleContext).fireVisibleDataPropertyChange();
        }
    }

    public int getVisibleRowCount() {
        return this.visibleRowCount;
    }

    private void expandRoot() {
        TreeModel model = getModel();
        if (model != null && model.getRoot() != null) {
            expandPath(new TreePath(model.getRoot()));
        }
    }

    public TreePath getNextMatch(String str, int i2, Position.Bias bias) {
        int rowCount = getRowCount();
        if (str == null) {
            throw new IllegalArgumentException();
        }
        if (i2 < 0 || i2 >= rowCount) {
            throw new IllegalArgumentException();
        }
        String upperCase = str.toUpperCase();
        int i3 = bias == Position.Bias.Forward ? 1 : -1;
        int i4 = i2;
        do {
            TreePath pathForRow = getPathForRow(i4);
            if (convertValueToText(pathForRow.getLastPathComponent(), isRowSelected(i4), isExpanded(i4), true, i4, false).toUpperCase().startsWith(upperCase)) {
                return pathForRow;
            }
            i4 = ((i4 + i3) + rowCount) % rowCount;
        } while (i4 != i2);
        return null;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Vector vector = new Vector();
        objectOutputStream.defaultWriteObject();
        if (this.cellRenderer != null && (this.cellRenderer instanceof Serializable)) {
            vector.addElement("cellRenderer");
            vector.addElement(this.cellRenderer);
        }
        if (this.cellEditor != null && (this.cellEditor instanceof Serializable)) {
            vector.addElement(CELL_EDITOR_PROPERTY);
            vector.addElement(this.cellEditor);
        }
        if (this.treeModel != null && (this.treeModel instanceof Serializable)) {
            vector.addElement("treeModel");
            vector.addElement(this.treeModel);
        }
        if (this.selectionModel != null && (this.selectionModel instanceof Serializable)) {
            vector.addElement("selectionModel");
            vector.addElement(this.selectionModel);
        }
        Object archivableExpandedState = getArchivableExpandedState();
        if (archivableExpandedState != null) {
            vector.addElement("expandedState");
            vector.addElement(archivableExpandedState);
        }
        objectOutputStream.writeObject(vector);
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.expandedState = new Hashtable<>();
        this.expandedStack = new Stack<>();
        Vector vector = (Vector) objectInputStream.readObject();
        int i2 = 0;
        int size = vector.size();
        if (0 < size && vector.elementAt(0).equals("cellRenderer")) {
            int i3 = 0 + 1;
            this.cellRenderer = (TreeCellRenderer) vector.elementAt(i3);
            i2 = i3 + 1;
        }
        if (i2 < size && vector.elementAt(i2).equals(CELL_EDITOR_PROPERTY)) {
            int i4 = i2 + 1;
            this.cellEditor = (TreeCellEditor) vector.elementAt(i4);
            i2 = i4 + 1;
        }
        if (i2 < size && vector.elementAt(i2).equals("treeModel")) {
            int i5 = i2 + 1;
            this.treeModel = (TreeModel) vector.elementAt(i5);
            i2 = i5 + 1;
        }
        if (i2 < size && vector.elementAt(i2).equals("selectionModel")) {
            int i6 = i2 + 1;
            this.selectionModel = (TreeSelectionModel) vector.elementAt(i6);
            i2 = i6 + 1;
        }
        if (i2 < size && vector.elementAt(i2).equals("expandedState")) {
            int i7 = i2 + 1;
            unarchiveExpandedState(vector.elementAt(i7));
            int i8 = i7 + 1;
        }
        if (this.listenerList.getListenerCount(TreeSelectionListener.class) != 0) {
            this.selectionRedirector = new TreeSelectionRedirector();
            this.selectionModel.addTreeSelectionListener(this.selectionRedirector);
        }
        if (this.treeModel != null) {
            this.treeModelListener = createTreeModelListener();
            if (this.treeModelListener != null) {
                this.treeModel.addTreeModelListener(this.treeModelListener);
            }
        }
    }

    private Object getArchivableExpandedState() {
        Enumeration<TreePath> enumerationKeys;
        int[] modelIndexsForPath;
        if (getModel() != null && (enumerationKeys = this.expandedState.keys()) != null) {
            Vector vector = new Vector();
            while (enumerationKeys.hasMoreElements()) {
                TreePath treePathNextElement = enumerationKeys.nextElement2();
                try {
                    modelIndexsForPath = getModelIndexsForPath(treePathNextElement);
                } catch (Error e2) {
                    modelIndexsForPath = null;
                }
                if (modelIndexsForPath != null) {
                    vector.addElement(modelIndexsForPath);
                    vector.addElement(this.expandedState.get(treePathNextElement));
                }
            }
            return vector;
        }
        return null;
    }

    private void unarchiveExpandedState(Object obj) {
        if (obj instanceof Vector) {
            Vector vector = (Vector) obj;
            int size = vector.size() - 1;
            while (size >= 0) {
                int i2 = size;
                int i3 = size - 1;
                Boolean bool = (Boolean) vector.elementAt(i2);
                try {
                    TreePath pathForIndexs = getPathForIndexs((int[]) vector.elementAt(i3));
                    if (pathForIndexs != null) {
                        this.expandedState.put(pathForIndexs, bool);
                    }
                } catch (Error e2) {
                }
                size = i3 - 1;
            }
        }
    }

    private int[] getModelIndexsForPath(TreePath treePath) {
        if (treePath != null) {
            TreeModel model = getModel();
            int pathCount = treePath.getPathCount();
            int[] iArr = new int[pathCount - 1];
            Object root = model.getRoot();
            for (int i2 = 1; i2 < pathCount; i2++) {
                iArr[i2 - 1] = model.getIndexOfChild(root, treePath.getPathComponent(i2));
                root = treePath.getPathComponent(i2);
                if (iArr[i2 - 1] < 0) {
                    return null;
                }
            }
            return iArr;
        }
        return null;
    }

    private TreePath getPathForIndexs(int[] iArr) {
        TreeModel model;
        if (iArr == null || (model = getModel()) == null) {
            return null;
        }
        Object root = model.getRoot();
        if (root == null) {
            return null;
        }
        TreePath treePath = new TreePath(root);
        for (int i2 : iArr) {
            root = model.getChild(root, i2);
            if (root == null) {
                return null;
            }
            treePath = treePath.pathByAddingChild(root);
        }
        return treePath;
    }

    /* loaded from: rt.jar:javax/swing/JTree$EmptySelectionModel.class */
    protected static class EmptySelectionModel extends DefaultTreeSelectionModel {
        protected static final EmptySelectionModel sharedInstance = new EmptySelectionModel();

        protected EmptySelectionModel() {
        }

        public static EmptySelectionModel sharedInstance() {
            return sharedInstance;
        }

        @Override // javax.swing.tree.DefaultTreeSelectionModel, javax.swing.tree.TreeSelectionModel
        public void setSelectionPaths(TreePath[] treePathArr) {
        }

        @Override // javax.swing.tree.DefaultTreeSelectionModel, javax.swing.tree.TreeSelectionModel
        public void addSelectionPaths(TreePath[] treePathArr) {
        }

        @Override // javax.swing.tree.DefaultTreeSelectionModel, javax.swing.tree.TreeSelectionModel
        public void removeSelectionPaths(TreePath[] treePathArr) {
        }

        @Override // javax.swing.tree.DefaultTreeSelectionModel, javax.swing.tree.TreeSelectionModel
        public void setSelectionMode(int i2) {
        }

        @Override // javax.swing.tree.DefaultTreeSelectionModel, javax.swing.tree.TreeSelectionModel
        public void setRowMapper(RowMapper rowMapper) {
        }

        @Override // javax.swing.tree.DefaultTreeSelectionModel, javax.swing.tree.TreeSelectionModel
        public void addTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
        }

        @Override // javax.swing.tree.DefaultTreeSelectionModel, javax.swing.tree.TreeSelectionModel
        public void removeTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
        }

        @Override // javax.swing.tree.DefaultTreeSelectionModel, javax.swing.tree.TreeSelectionModel
        public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        }

        @Override // javax.swing.tree.DefaultTreeSelectionModel, javax.swing.tree.TreeSelectionModel
        public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        }
    }

    /* loaded from: rt.jar:javax/swing/JTree$TreeSelectionRedirector.class */
    protected class TreeSelectionRedirector implements Serializable, TreeSelectionListener {
        protected TreeSelectionRedirector() {
        }

        @Override // javax.swing.event.TreeSelectionListener
        public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
            JTree.this.fireValueChanged((TreeSelectionEvent) treeSelectionEvent.cloneWithSource(JTree.this));
        }
    }

    @Override // javax.swing.Scrollable
    public Dimension getPreferredScrollableViewportSize() {
        Rectangle rowBounds;
        int i2 = getPreferredSize().width;
        int visibleRowCount = getVisibleRowCount();
        int rowHeight = -1;
        if (isFixedRowHeight()) {
            rowHeight = visibleRowCount * getRowHeight();
        } else {
            TreeUI ui = getUI();
            if (ui != null && visibleRowCount > 0) {
                int rowCount = ui.getRowCount(this);
                if (rowCount >= visibleRowCount) {
                    Rectangle rowBounds2 = getRowBounds(visibleRowCount - 1);
                    if (rowBounds2 != null) {
                        rowHeight = rowBounds2.f12373y + rowBounds2.height;
                    }
                } else if (rowCount > 0 && (rowBounds = getRowBounds(0)) != null) {
                    rowHeight = rowBounds.height * visibleRowCount;
                }
            }
            if (rowHeight == -1) {
                rowHeight = 16 * visibleRowCount;
            }
        }
        return new Dimension(i2, rowHeight);
    }

    @Override // javax.swing.Scrollable
    public int getScrollableUnitIncrement(Rectangle rectangle, int i2, int i3) {
        if (i2 == 1) {
            int closestRowForLocation = getClosestRowForLocation(0, rectangle.f12373y);
            if (closestRowForLocation != -1) {
                Rectangle rowBounds = getRowBounds(closestRowForLocation);
                if (rowBounds.f12373y != rectangle.f12373y) {
                    if (i3 < 0) {
                        return Math.max(0, rectangle.f12373y - rowBounds.f12373y);
                    }
                    return (rowBounds.f12373y + rowBounds.height) - rectangle.f12373y;
                }
                if (i3 < 0) {
                    if (closestRowForLocation != 0) {
                        return getRowBounds(closestRowForLocation - 1).height;
                    }
                    return 0;
                }
                return rowBounds.height;
            }
            return 0;
        }
        return 4;
    }

    @Override // javax.swing.Scrollable
    public int getScrollableBlockIncrement(Rectangle rectangle, int i2, int i3) {
        return i2 == 1 ? rectangle.height : rectangle.width;
    }

    @Override // javax.swing.Scrollable
    public boolean getScrollableTracksViewportWidth() {
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        return (unwrappedParent instanceof JViewport) && unwrappedParent.getWidth() > getPreferredSize().width;
    }

    @Override // javax.swing.Scrollable
    public boolean getScrollableTracksViewportHeight() {
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        return (unwrappedParent instanceof JViewport) && unwrappedParent.getHeight() > getPreferredSize().height;
    }

    protected void setExpandedState(TreePath treePath, boolean z2) {
        Stack<TreePath> stackPop;
        int size;
        int i2;
        if (treePath != null) {
            TreePath parentPath = treePath.getParentPath();
            if (this.expandedStack.size() == 0) {
                stackPop = new Stack<>();
            } else {
                stackPop = this.expandedStack.pop();
            }
            while (parentPath != null) {
                try {
                    if (isExpanded(parentPath)) {
                        parentPath = null;
                    } else {
                        stackPop.push(parentPath);
                        parentPath = parentPath.getParentPath();
                    }
                } finally {
                    if (this.expandedStack.size() < TEMP_STACK_SIZE) {
                        stackPop.removeAllElements();
                        this.expandedStack.push(stackPop);
                    }
                }
            }
            for (int size2 = stackPop.size() - 1; size2 >= 0; size2--) {
                TreePath treePathPop = stackPop.pop();
                if (!isExpanded(treePathPop)) {
                    try {
                        fireTreeWillExpand(treePathPop);
                        this.expandedState.put(treePathPop, Boolean.TRUE);
                        fireTreeExpanded(treePathPop);
                        if (this.accessibleContext != null) {
                            ((AccessibleJTree) this.accessibleContext).fireVisibleDataPropertyChange();
                        }
                    } catch (ExpandVetoException e2) {
                        if (size < i2) {
                            return;
                        } else {
                            return;
                        }
                    }
                }
            }
            if (this.expandedStack.size() < TEMP_STACK_SIZE) {
                stackPop.removeAllElements();
                this.expandedStack.push(stackPop);
            }
            if (!z2) {
                Boolean bool = this.expandedState.get(treePath);
                if (bool != null && bool.booleanValue()) {
                    try {
                        fireTreeWillCollapse(treePath);
                        this.expandedState.put(treePath, Boolean.FALSE);
                        fireTreeCollapsed(treePath);
                        if (removeDescendantSelectedPaths(treePath, false) && !isPathSelected(treePath)) {
                            addSelectionPath(treePath);
                        }
                        if (this.accessibleContext != null) {
                            ((AccessibleJTree) this.accessibleContext).fireVisibleDataPropertyChange();
                            return;
                        }
                        return;
                    } catch (ExpandVetoException e3) {
                        return;
                    }
                }
                return;
            }
            Boolean bool2 = this.expandedState.get(treePath);
            if (bool2 == null || !bool2.booleanValue()) {
                try {
                    fireTreeWillExpand(treePath);
                    this.expandedState.put(treePath, Boolean.TRUE);
                    fireTreeExpanded(treePath);
                    if (this.accessibleContext != null) {
                        ((AccessibleJTree) this.accessibleContext).fireVisibleDataPropertyChange();
                    }
                } catch (ExpandVetoException e4) {
                }
            }
        }
    }

    protected Enumeration<TreePath> getDescendantToggledPaths(TreePath treePath) {
        if (treePath == null) {
            return null;
        }
        Vector vector = new Vector();
        Enumeration<TreePath> enumerationKeys = this.expandedState.keys();
        while (enumerationKeys.hasMoreElements()) {
            TreePath treePathNextElement = enumerationKeys.nextElement2();
            if (treePath.isDescendant(treePathNextElement)) {
                vector.addElement(treePathNextElement);
            }
        }
        return vector.elements();
    }

    protected void removeDescendantToggledPaths(Enumeration<TreePath> enumeration) {
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Enumeration<TreePath> descendantToggledPaths = getDescendantToggledPaths(enumeration.nextElement2());
                if (descendantToggledPaths != null) {
                    while (descendantToggledPaths.hasMoreElements()) {
                        this.expandedState.remove(descendantToggledPaths.nextElement2());
                    }
                }
            }
        }
    }

    protected void clearToggledPaths() {
        this.expandedState.clear();
    }

    protected TreeModelListener createTreeModelListener() {
        return new TreeModelHandler();
    }

    protected boolean removeDescendantSelectedPaths(TreePath treePath, boolean z2) {
        TreePath[] descendantSelectedPaths = getDescendantSelectedPaths(treePath, z2);
        if (descendantSelectedPaths != null) {
            getSelectionModel().removeSelectionPaths(descendantSelectedPaths);
            return true;
        }
        return false;
    }

    private TreePath[] getDescendantSelectedPaths(TreePath treePath, boolean z2) {
        TreeSelectionModel selectionModel = getSelectionModel();
        TreePath[] selectionPaths = selectionModel != null ? selectionModel.getSelectionPaths() : null;
        if (selectionPaths != null) {
            boolean z3 = false;
            for (int length = selectionPaths.length - 1; length >= 0; length--) {
                if (selectionPaths[length] != null && treePath.isDescendant(selectionPaths[length]) && (!treePath.equals(selectionPaths[length]) || z2)) {
                    z3 = true;
                } else {
                    selectionPaths[length] = null;
                }
            }
            if (!z3) {
                selectionPaths = null;
            }
            return selectionPaths;
        }
        return null;
    }

    void removeDescendantSelectedPaths(TreeModelEvent treeModelEvent) {
        TreePath treePath = SwingUtilities2.getTreePath(treeModelEvent, getModel());
        Object[] children = treeModelEvent.getChildren();
        if (getSelectionModel() != null && treePath != null && children != null && children.length > 0) {
            for (int length = children.length - 1; length >= 0; length--) {
                removeDescendantSelectedPaths(treePath.pathByAddingChild(children[length]), true);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/JTree$TreeModelHandler.class */
    protected class TreeModelHandler implements TreeModelListener {
        protected TreeModelHandler() {
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesChanged(TreeModelEvent treeModelEvent) {
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesInserted(TreeModelEvent treeModelEvent) {
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeStructureChanged(TreeModelEvent treeModelEvent) {
            TreePath treePath;
            if (treeModelEvent == null || (treePath = SwingUtilities2.getTreePath(treeModelEvent, JTree.this.getModel())) == null) {
                return;
            }
            if (treePath.getPathCount() != 1) {
                if (JTree.this.expandedState.get(treePath) != null) {
                    Vector vector = new Vector(1);
                    boolean zIsExpanded = JTree.this.isExpanded(treePath);
                    vector.addElement(treePath);
                    JTree.this.removeDescendantToggledPaths(vector.elements());
                    if (zIsExpanded) {
                        TreeModel model = JTree.this.getModel();
                        if (model != null && !model.isLeaf(treePath.getLastPathComponent())) {
                            JTree.this.expandedState.put(treePath, Boolean.TRUE);
                        } else {
                            JTree.this.collapsePath(treePath);
                        }
                    }
                }
            } else {
                JTree.this.clearToggledPaths();
                Object root = JTree.this.treeModel.getRoot();
                if (root != null && !JTree.this.treeModel.isLeaf(root)) {
                    JTree.this.expandedState.put(treePath, Boolean.TRUE);
                }
            }
            JTree.this.removeDescendantSelectedPaths(treePath, false);
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesRemoved(TreeModelEvent treeModelEvent) {
            if (treeModelEvent == null) {
                return;
            }
            TreePath treePath = SwingUtilities2.getTreePath(treeModelEvent, JTree.this.getModel());
            Object[] children = treeModelEvent.getChildren();
            if (children == null) {
                return;
            }
            Vector vector = new Vector(Math.max(1, children.length));
            for (int length = children.length - 1; length >= 0; length--) {
                TreePath treePathPathByAddingChild = treePath.pathByAddingChild(children[length]);
                if (JTree.this.expandedState.get(treePathPathByAddingChild) != null) {
                    vector.addElement(treePathPathByAddingChild);
                }
            }
            if (vector.size() > 0) {
                JTree.this.removeDescendantToggledPaths(vector.elements());
            }
            TreeModel model = JTree.this.getModel();
            if (model == null || model.isLeaf(treePath.getLastPathComponent())) {
                JTree.this.expandedState.remove(treePath);
            }
            JTree.this.removeDescendantSelectedPaths(treeModelEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/JTree$DynamicUtilTreeNode.class */
    public static class DynamicUtilTreeNode extends DefaultMutableTreeNode {
        protected boolean hasChildren;
        protected Object childValue;
        protected boolean loadedChildren;

        public static void createChildren(DefaultMutableTreeNode defaultMutableTreeNode, Object obj) {
            if (obj instanceof Vector) {
                Vector vector = (Vector) obj;
                int size = vector.size();
                for (int i2 = 0; i2 < size; i2++) {
                    defaultMutableTreeNode.add(new DynamicUtilTreeNode(vector.elementAt(i2), vector.elementAt(i2)));
                }
                return;
            }
            if (obj instanceof Hashtable) {
                Hashtable hashtable = (Hashtable) obj;
                Enumeration enumerationKeys = hashtable.keys();
                while (enumerationKeys.hasMoreElements()) {
                    Object objNextElement = enumerationKeys.nextElement2();
                    defaultMutableTreeNode.add(new DynamicUtilTreeNode(objNextElement, hashtable.get(objNextElement)));
                }
                return;
            }
            if (obj instanceof Object[]) {
                Object[] objArr = (Object[]) obj;
                int length = objArr.length;
                for (int i3 = 0; i3 < length; i3++) {
                    defaultMutableTreeNode.add(new DynamicUtilTreeNode(objArr[i3], objArr[i3]));
                }
            }
        }

        public DynamicUtilTreeNode(Object obj, Object obj2) {
            super(obj);
            this.loadedChildren = false;
            this.childValue = obj2;
            if (obj2 != null) {
                if (obj2 instanceof Vector) {
                    setAllowsChildren(true);
                    return;
                }
                if (obj2 instanceof Hashtable) {
                    setAllowsChildren(true);
                    return;
                } else if (obj2 instanceof Object[]) {
                    setAllowsChildren(true);
                    return;
                } else {
                    setAllowsChildren(false);
                    return;
                }
            }
            setAllowsChildren(false);
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.TreeNode
        public boolean isLeaf() {
            return !getAllowsChildren();
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.TreeNode
        public int getChildCount() {
            if (!this.loadedChildren) {
                loadChildren();
            }
            return super.getChildCount();
        }

        protected void loadChildren() {
            this.loadedChildren = true;
            createChildren(this, this.childValue);
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.TreeNode
        public TreeNode getChildAt(int i2) {
            if (!this.loadedChildren) {
                loadChildren();
            }
            return super.getChildAt(i2);
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.TreeNode
        public Enumeration children() {
            if (!this.loadedChildren) {
                loadChildren();
            }
            return super.children();
        }
    }

    @Override // javax.swing.JComponent
    void setUIProperty(String str, Object obj) {
        if (str == ROW_HEIGHT_PROPERTY) {
            if (!this.rowHeightSet) {
                setRowHeight(((Number) obj).intValue());
                this.rowHeightSet = false;
                return;
            }
            return;
        }
        if (str == SCROLLS_ON_EXPAND_PROPERTY) {
            if (!this.scrollsOnExpandSet) {
                setScrollsOnExpand(((Boolean) obj).booleanValue());
                this.scrollsOnExpandSet = false;
                return;
            }
            return;
        }
        if (str == SHOWS_ROOT_HANDLES_PROPERTY) {
            if (!this.showsRootHandlesSet) {
                setShowsRootHandles(((Boolean) obj).booleanValue());
                this.showsRootHandlesSet = false;
                return;
            }
            return;
        }
        super.setUIProperty(str, obj);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        String str = this.rootVisible ? "true" : "false";
        String str2 = this.showsRootHandles ? "true" : "false";
        return super.paramString() + ",editable=" + (this.editable ? "true" : "false") + ",invokesStopCellEditing=" + (this.invokesStopCellEditing ? "true" : "false") + ",largeModel=" + (this.largeModel ? "true" : "false") + ",rootVisible=" + str + ",rowHeight=" + this.rowHeight + ",scrollsOnExpand=" + (this.scrollsOnExpand ? "true" : "false") + ",showsRootHandles=" + str2 + ",toggleClickCount=" + this.toggleClickCount + ",visibleRowCount=" + this.visibleRowCount;
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJTree();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JTree$AccessibleJTree.class */
    protected class AccessibleJTree extends JComponent.AccessibleJComponent implements AccessibleSelection, TreeSelectionListener, TreeModelListener, TreeExpansionListener {
        TreePath leadSelectionPath;
        Accessible leadSelectionAccessible;

        public AccessibleJTree() {
            super();
            TreeModel model = JTree.this.getModel();
            if (model != null) {
                model.addTreeModelListener(this);
            }
            JTree.this.addTreeExpansionListener(this);
            JTree.this.addTreeSelectionListener(this);
            this.leadSelectionPath = JTree.this.getLeadSelectionPath();
            this.leadSelectionAccessible = this.leadSelectionPath != null ? new AccessibleJTreeNode(JTree.this, this.leadSelectionPath, JTree.this) : null;
        }

        @Override // javax.swing.event.TreeSelectionListener
        public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_SELECTION_PROPERTY, false, true);
        }

        public void fireVisibleDataPropertyChange() {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, false, true);
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesChanged(TreeModelEvent treeModelEvent) {
            fireVisibleDataPropertyChange();
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesInserted(TreeModelEvent treeModelEvent) {
            fireVisibleDataPropertyChange();
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesRemoved(TreeModelEvent treeModelEvent) {
            fireVisibleDataPropertyChange();
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeStructureChanged(TreeModelEvent treeModelEvent) {
            fireVisibleDataPropertyChange();
        }

        @Override // javax.swing.event.TreeExpansionListener
        public void treeCollapsed(TreeExpansionEvent treeExpansionEvent) {
            fireVisibleDataPropertyChange();
            TreePath path = treeExpansionEvent.getPath();
            if (path != null) {
                firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, new PropertyChangeEvent(new AccessibleJTreeNode(JTree.this, path, null), AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.EXPANDED, AccessibleState.COLLAPSED));
            }
        }

        @Override // javax.swing.event.TreeExpansionListener
        public void treeExpanded(TreeExpansionEvent treeExpansionEvent) {
            fireVisibleDataPropertyChange();
            TreePath path = treeExpansionEvent.getPath();
            if (path != null) {
                firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, new PropertyChangeEvent(new AccessibleJTreeNode(JTree.this, path, null), AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.COLLAPSED, AccessibleState.EXPANDED));
            }
        }

        void fireActiveDescendantPropertyChange(TreePath treePath, TreePath treePath2) {
            if (treePath != treePath2) {
                firePropertyChange(AccessibleContext.ACCESSIBLE_ACTIVE_DESCENDANT_PROPERTY, treePath != null ? new AccessibleJTreeNode(JTree.this, treePath, null) : null, treePath2 != null ? new AccessibleJTreeNode(JTree.this, treePath2, null) : null);
            }
        }

        private AccessibleContext getCurrentAccessibleContext() {
            Component currentComponent = getCurrentComponent();
            if (currentComponent instanceof Accessible) {
                return currentComponent.getAccessibleContext();
            }
            return null;
        }

        private Component getCurrentComponent() {
            Object root;
            TreeModel model = JTree.this.getModel();
            if (model == null || (root = model.getRoot()) == null) {
                return null;
            }
            TreePath treePath = new TreePath(root);
            if (JTree.this.isVisible(treePath)) {
                TreeCellRenderer cellRenderer = JTree.this.getCellRenderer();
                TreeUI ui = JTree.this.getUI();
                if (ui != null) {
                    int rowForPath = ui.getRowForPath(JTree.this, treePath);
                    return cellRenderer.getTreeCellRendererComponent(JTree.this, root, JTree.this.isPathSelected(treePath), JTree.this.isExpanded(treePath), model.isLeaf(root), rowForPath, JTree.this.isFocusOwner() && JTree.this.getLeadSelectionRow() == rowForPath);
                }
                return null;
            }
            return null;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.TREE;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            TreePath closestPathForLocation = JTree.this.getClosestPathForLocation(point.f12370x, point.f12371y);
            if (closestPathForLocation != null) {
                return new AccessibleJTreeNode(JTree.this, closestPathForLocation, null);
            }
            return null;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            TreeModel model = JTree.this.getModel();
            if (model == null) {
                return 0;
            }
            if (JTree.this.isRootVisible()) {
                return 1;
            }
            Object root = model.getRoot();
            if (root == null) {
                return 0;
            }
            return model.getChildCount(root);
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            Object root;
            Object child;
            TreeModel model = JTree.this.getModel();
            if (model == null || (root = model.getRoot()) == null) {
                return null;
            }
            if (JTree.this.isRootVisible()) {
                if (i2 == 0) {
                    Object[] objArr = {root};
                    if (objArr[0] == null) {
                        return null;
                    }
                    return new AccessibleJTreeNode(JTree.this, new TreePath(objArr), JTree.this);
                }
                return null;
            }
            int childCount = model.getChildCount(root);
            if (i2 < 0 || i2 >= childCount || (child = model.getChild(root, i2)) == null) {
                return null;
            }
            return new AccessibleJTreeNode(JTree.this, new TreePath(new Object[]{root, child}), JTree.this);
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleIndexInParent() {
            return super.getAccessibleIndexInParent();
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleSelection getAccessibleSelection() {
            return this;
        }

        @Override // javax.accessibility.AccessibleSelection
        public int getAccessibleSelectionCount() {
            Object[] objArr = {JTree.this.treeModel.getRoot()};
            if (objArr[0] != null && JTree.this.isPathSelected(new TreePath(objArr))) {
                return 1;
            }
            return 0;
        }

        @Override // javax.accessibility.AccessibleSelection
        public Accessible getAccessibleSelection(int i2) {
            if (i2 == 0) {
                Object[] objArr = {JTree.this.treeModel.getRoot()};
                if (objArr[0] == null) {
                    return null;
                }
                TreePath treePath = new TreePath(objArr);
                if (JTree.this.isPathSelected(treePath)) {
                    return new AccessibleJTreeNode(JTree.this, treePath, JTree.this);
                }
                return null;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleSelection
        public boolean isAccessibleChildSelected(int i2) {
            if (i2 == 0) {
                Object[] objArr = {JTree.this.treeModel.getRoot()};
                if (objArr[0] == null) {
                    return false;
                }
                return JTree.this.isPathSelected(new TreePath(objArr));
            }
            return false;
        }

        @Override // javax.accessibility.AccessibleSelection
        public void addAccessibleSelection(int i2) {
            TreeModel model = JTree.this.getModel();
            if (model != null && i2 == 0) {
                Object[] objArr = {model.getRoot()};
                if (objArr[0] == null) {
                    return;
                }
                JTree.this.addSelectionPath(new TreePath(objArr));
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void removeAccessibleSelection(int i2) {
            TreeModel model = JTree.this.getModel();
            if (model != null && i2 == 0) {
                Object[] objArr = {model.getRoot()};
                if (objArr[0] == null) {
                    return;
                }
                JTree.this.removeSelectionPath(new TreePath(objArr));
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void clearAccessibleSelection() {
            int accessibleChildrenCount = getAccessibleChildrenCount();
            for (int i2 = 0; i2 < accessibleChildrenCount; i2++) {
                removeAccessibleSelection(i2);
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void selectAllAccessibleSelection() {
            TreeModel model = JTree.this.getModel();
            if (model != null) {
                Object[] objArr = {model.getRoot()};
                if (objArr[0] == null) {
                    return;
                }
                JTree.this.addSelectionPath(new TreePath(objArr));
            }
        }

        /* loaded from: rt.jar:javax/swing/JTree$AccessibleJTree$AccessibleJTreeNode.class */
        protected class AccessibleJTreeNode extends AccessibleContext implements Accessible, AccessibleComponent, AccessibleSelection, AccessibleAction {
            private JTree tree;
            private TreeModel treeModel;
            private Object obj;
            private TreePath path;
            private Accessible accessibleParent;
            private int index = 0;
            private boolean isLeaf;

            public AccessibleJTreeNode(JTree jTree, TreePath treePath, Accessible accessible) {
                this.tree = null;
                this.treeModel = null;
                this.obj = null;
                this.path = null;
                this.accessibleParent = null;
                this.isLeaf = false;
                this.tree = jTree;
                this.path = treePath;
                this.accessibleParent = accessible;
                this.treeModel = jTree.getModel();
                this.obj = treePath.getLastPathComponent();
                if (this.treeModel != null) {
                    this.isLeaf = this.treeModel.isLeaf(this.obj);
                }
            }

            private TreePath getChildTreePath(int i2) {
                if (i2 < 0 || i2 >= getAccessibleChildrenCount()) {
                    return null;
                }
                Object child = this.treeModel.getChild(this.obj, i2);
                Object[] path = this.path.getPath();
                Object[] objArr = new Object[path.length + 1];
                System.arraycopy(path, 0, objArr, 0, path.length);
                objArr[objArr.length - 1] = child;
                return new TreePath(objArr);
            }

            @Override // javax.accessibility.Accessible
            public AccessibleContext getAccessibleContext() {
                return this;
            }

            private AccessibleContext getCurrentAccessibleContext() {
                Component currentComponent = getCurrentComponent();
                if (currentComponent instanceof Accessible) {
                    return currentComponent.getAccessibleContext();
                }
                return null;
            }

            private Component getCurrentComponent() {
                TreeCellRenderer cellRenderer;
                TreeUI ui;
                if (this.tree.isVisible(this.path) && (cellRenderer = this.tree.getCellRenderer()) != null && (ui = this.tree.getUI()) != null) {
                    return cellRenderer.getTreeCellRendererComponent(this.tree, this.obj, this.tree.isPathSelected(this.path), this.tree.isExpanded(this.path), this.isLeaf, ui.getRowForPath(JTree.this, this.path), false);
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleName() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    String accessibleName = currentAccessibleContext.getAccessibleName();
                    if (accessibleName != null && accessibleName != "") {
                        return currentAccessibleContext.getAccessibleName();
                    }
                    return null;
                }
                if (this.accessibleName != null && this.accessibleName != "") {
                    return this.accessibleName;
                }
                return (String) JTree.this.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY);
            }

            @Override // javax.accessibility.AccessibleContext
            public void setAccessibleName(String str) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.setAccessibleName(str);
                } else {
                    super.setAccessibleName(str);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleDescription() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleDescription();
                }
                return super.getAccessibleDescription();
            }

            @Override // javax.accessibility.AccessibleContext
            public void setAccessibleDescription(String str) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.setAccessibleDescription(str);
                } else {
                    super.setAccessibleDescription(str);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleRole getAccessibleRole() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleRole();
                }
                return AccessibleRole.UNKNOWN;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleStateSet getAccessibleStateSet() {
                AccessibleStateSet accessibleStateSet;
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    accessibleStateSet = currentAccessibleContext.getAccessibleStateSet();
                } else {
                    accessibleStateSet = new AccessibleStateSet();
                }
                if (isShowing()) {
                    accessibleStateSet.add(AccessibleState.SHOWING);
                } else if (accessibleStateSet.contains(AccessibleState.SHOWING)) {
                    accessibleStateSet.remove(AccessibleState.SHOWING);
                }
                if (isVisible()) {
                    accessibleStateSet.add(AccessibleState.VISIBLE);
                } else if (accessibleStateSet.contains(AccessibleState.VISIBLE)) {
                    accessibleStateSet.remove(AccessibleState.VISIBLE);
                }
                if (this.tree.isPathSelected(this.path)) {
                    accessibleStateSet.add(AccessibleState.SELECTED);
                }
                if (this.path == JTree.this.getLeadSelectionPath()) {
                    accessibleStateSet.add(AccessibleState.ACTIVE);
                }
                if (!this.isLeaf) {
                    accessibleStateSet.add(AccessibleState.EXPANDABLE);
                }
                if (this.tree.isExpanded(this.path)) {
                    accessibleStateSet.add(AccessibleState.EXPANDED);
                } else {
                    accessibleStateSet.add(AccessibleState.COLLAPSED);
                }
                if (this.tree.isEditable()) {
                    accessibleStateSet.add(AccessibleState.EDITABLE);
                }
                return accessibleStateSet;
            }

            @Override // javax.accessibility.AccessibleContext
            public Accessible getAccessibleParent() {
                if (this.accessibleParent == null) {
                    Object[] path = this.path.getPath();
                    if (path.length > 1) {
                        Object obj = path[path.length - 2];
                        if (this.treeModel != null) {
                            this.index = this.treeModel.getIndexOfChild(obj, this.obj);
                        }
                        Object[] objArr = new Object[path.length - 1];
                        System.arraycopy(path, 0, objArr, 0, path.length - 1);
                        this.accessibleParent = AccessibleJTree.this.new AccessibleJTreeNode(this.tree, new TreePath(objArr), null);
                        setAccessibleParent(this.accessibleParent);
                    } else if (this.treeModel != null) {
                        this.accessibleParent = this.tree;
                        this.index = 0;
                        setAccessibleParent(this.accessibleParent);
                    }
                }
                return this.accessibleParent;
            }

            @Override // javax.accessibility.AccessibleContext
            public int getAccessibleIndexInParent() {
                if (this.accessibleParent == null) {
                    getAccessibleParent();
                }
                Object[] path = this.path.getPath();
                if (path.length > 1) {
                    Object obj = path[path.length - 2];
                    if (this.treeModel != null) {
                        this.index = this.treeModel.getIndexOfChild(obj, this.obj);
                    }
                }
                return this.index;
            }

            @Override // javax.accessibility.AccessibleContext
            public int getAccessibleChildrenCount() {
                return this.treeModel.getChildCount(this.obj);
            }

            @Override // javax.accessibility.AccessibleContext
            public Accessible getAccessibleChild(int i2) {
                if (i2 < 0 || i2 >= getAccessibleChildrenCount()) {
                    return null;
                }
                Object child = this.treeModel.getChild(this.obj, i2);
                Object[] path = this.path.getPath();
                Object[] objArr = new Object[path.length + 1];
                System.arraycopy(path, 0, objArr, 0, path.length);
                objArr[objArr.length - 1] = child;
                return AccessibleJTree.this.new AccessibleJTreeNode(JTree.this, new TreePath(objArr), this);
            }

            @Override // javax.accessibility.AccessibleContext
            public Locale getLocale() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getLocale();
                }
                return this.tree.getLocale();
            }

            @Override // javax.accessibility.AccessibleContext
            public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.addPropertyChangeListener(propertyChangeListener);
                } else {
                    super.addPropertyChangeListener(propertyChangeListener);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.removePropertyChangeListener(propertyChangeListener);
                } else {
                    super.removePropertyChangeListener(propertyChangeListener);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleAction getAccessibleAction() {
                return this;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleComponent getAccessibleComponent() {
                return this;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleSelection getAccessibleSelection() {
                if (getCurrentAccessibleContext() != null && this.isLeaf) {
                    return getCurrentAccessibleContext().getAccessibleSelection();
                }
                return this;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleText getAccessibleText() {
                if (getCurrentAccessibleContext() != null) {
                    return getCurrentAccessibleContext().getAccessibleText();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleValue getAccessibleValue() {
                if (getCurrentAccessibleContext() != null) {
                    return getCurrentAccessibleContext().getAccessibleValue();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public Color getBackground() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getBackground();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getBackground();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setBackground(Color color) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setBackground(color);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setBackground(color);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Color getForeground() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getForeground();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getForeground();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setForeground(Color color) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setForeground(color);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setForeground(color);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Cursor getCursor() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getCursor();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getCursor();
                }
                Accessible accessibleParent = getAccessibleParent();
                if (accessibleParent instanceof AccessibleComponent) {
                    return ((AccessibleComponent) accessibleParent).getCursor();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setCursor(Cursor cursor) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setCursor(cursor);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setCursor(cursor);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Font getFont() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getFont();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getFont();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setFont(Font font) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setFont(font);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setFont(font);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public FontMetrics getFontMetrics(Font font) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getFontMetrics(font);
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getFontMetrics(font);
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean isEnabled() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).isEnabled();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.isEnabled();
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setEnabled(boolean z2) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setEnabled(z2);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setEnabled(z2);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean isVisible() {
                Rectangle pathBounds = this.tree.getPathBounds(this.path);
                Rectangle visibleRect = this.tree.getVisibleRect();
                return (pathBounds == null || visibleRect == null || !visibleRect.intersects(pathBounds)) ? false : true;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setVisible(boolean z2) {
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean isShowing() {
                return this.tree.isShowing() && isVisible();
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean contains(Point point) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getBounds().contains(point);
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getBounds().contains(point);
                }
                return getBounds().contains(point);
            }

            @Override // javax.accessibility.AccessibleComponent
            public Point getLocationOnScreen() {
                if (this.tree != null) {
                    Point locationOnScreen = this.tree.getLocationOnScreen();
                    Rectangle pathBounds = this.tree.getPathBounds(this.path);
                    if (locationOnScreen != null && pathBounds != null) {
                        Point point = new Point(pathBounds.f12372x, pathBounds.f12373y);
                        point.translate(locationOnScreen.f12370x, locationOnScreen.f12371y);
                        return point;
                    }
                    return null;
                }
                return null;
            }

            protected Point getLocationInJTree() {
                Rectangle pathBounds = this.tree.getPathBounds(this.path);
                if (pathBounds != null) {
                    return pathBounds.getLocation();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public Point getLocation() {
                Rectangle bounds = getBounds();
                if (bounds != null) {
                    return bounds.getLocation();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setLocation(Point point) {
            }

            @Override // javax.accessibility.AccessibleComponent
            public Rectangle getBounds() {
                Rectangle pathBounds = this.tree.getPathBounds(this.path);
                Accessible accessibleParent = getAccessibleParent();
                if (accessibleParent != null && (accessibleParent instanceof AccessibleJTreeNode)) {
                    Point locationInJTree = ((AccessibleJTreeNode) accessibleParent).getLocationInJTree();
                    if (locationInJTree != null && pathBounds != null) {
                        pathBounds.translate(-locationInJTree.f12370x, -locationInJTree.f12371y);
                    } else {
                        return null;
                    }
                }
                return pathBounds;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setBounds(Rectangle rectangle) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setBounds(rectangle);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setBounds(rectangle);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Dimension getSize() {
                return getBounds().getSize();
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setSize(Dimension dimension) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setSize(dimension);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setSize(dimension);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Accessible getAccessibleAt(Point point) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getAccessibleAt(point);
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean isFocusTraversable() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).isFocusTraversable();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.isFocusTraversable();
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void requestFocus() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).requestFocus();
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.requestFocus();
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public void addFocusListener(FocusListener focusListener) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).addFocusListener(focusListener);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.addFocusListener(focusListener);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public void removeFocusListener(FocusListener focusListener) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).removeFocusListener(focusListener);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.removeFocusListener(focusListener);
                }
            }

            @Override // javax.accessibility.AccessibleSelection
            public int getAccessibleSelectionCount() {
                int i2 = 0;
                int accessibleChildrenCount = getAccessibleChildrenCount();
                for (int i3 = 0; i3 < accessibleChildrenCount; i3++) {
                    if (this.tree.isPathSelected(getChildTreePath(i3))) {
                        i2++;
                    }
                }
                return i2;
            }

            @Override // javax.accessibility.AccessibleSelection
            public Accessible getAccessibleSelection(int i2) {
                int accessibleChildrenCount = getAccessibleChildrenCount();
                if (i2 < 0 || i2 >= accessibleChildrenCount) {
                    return null;
                }
                int i3 = 0;
                for (int i4 = 0; i4 < accessibleChildrenCount && i2 >= i3; i4++) {
                    TreePath childTreePath = getChildTreePath(i4);
                    if (this.tree.isPathSelected(childTreePath)) {
                        if (i3 == i2) {
                            return AccessibleJTree.this.new AccessibleJTreeNode(this.tree, childTreePath, this);
                        }
                        i3++;
                    }
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleSelection
            public boolean isAccessibleChildSelected(int i2) {
                int accessibleChildrenCount = getAccessibleChildrenCount();
                if (i2 < 0 || i2 >= accessibleChildrenCount) {
                    return false;
                }
                return this.tree.isPathSelected(getChildTreePath(i2));
            }

            @Override // javax.accessibility.AccessibleSelection
            public void addAccessibleSelection(int i2) {
                if (JTree.this.getModel() != null && i2 >= 0 && i2 < getAccessibleChildrenCount()) {
                    JTree.this.addSelectionPath(getChildTreePath(i2));
                }
            }

            @Override // javax.accessibility.AccessibleSelection
            public void removeAccessibleSelection(int i2) {
                if (JTree.this.getModel() != null && i2 >= 0 && i2 < getAccessibleChildrenCount()) {
                    JTree.this.removeSelectionPath(getChildTreePath(i2));
                }
            }

            @Override // javax.accessibility.AccessibleSelection
            public void clearAccessibleSelection() {
                int accessibleChildrenCount = getAccessibleChildrenCount();
                for (int i2 = 0; i2 < accessibleChildrenCount; i2++) {
                    removeAccessibleSelection(i2);
                }
            }

            @Override // javax.accessibility.AccessibleSelection
            public void selectAllAccessibleSelection() {
                if (JTree.this.getModel() != null) {
                    int accessibleChildrenCount = getAccessibleChildrenCount();
                    for (int i2 = 0; i2 < accessibleChildrenCount; i2++) {
                        JTree.this.addSelectionPath(getChildTreePath(i2));
                    }
                }
            }

            @Override // javax.accessibility.AccessibleAction
            public int getAccessibleActionCount() {
                AccessibleAction accessibleAction;
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext == null || (accessibleAction = currentAccessibleContext.getAccessibleAction()) == null) {
                    return this.isLeaf ? 0 : 1;
                }
                return accessibleAction.getAccessibleActionCount() + (this.isLeaf ? 0 : 1);
            }

            @Override // javax.accessibility.AccessibleAction
            public String getAccessibleActionDescription(int i2) {
                AccessibleAction accessibleAction;
                if (i2 < 0 || i2 >= getAccessibleActionCount()) {
                    return null;
                }
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (i2 == 0) {
                    return AccessibleAction.TOGGLE_EXPAND;
                }
                if (currentAccessibleContext != null && (accessibleAction = currentAccessibleContext.getAccessibleAction()) != null) {
                    return accessibleAction.getAccessibleActionDescription(i2 - 1);
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleAction
            public boolean doAccessibleAction(int i2) {
                AccessibleAction accessibleAction;
                if (i2 < 0 || i2 >= getAccessibleActionCount()) {
                    return false;
                }
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (i2 == 0) {
                    if (JTree.this.isExpanded(this.path)) {
                        JTree.this.collapsePath(this.path);
                        return true;
                    }
                    JTree.this.expandPath(this.path);
                    return true;
                }
                if (currentAccessibleContext != null && (accessibleAction = currentAccessibleContext.getAccessibleAction()) != null) {
                    return accessibleAction.doAccessibleAction(i2 - 1);
                }
                return false;
            }
        }
    }
}
