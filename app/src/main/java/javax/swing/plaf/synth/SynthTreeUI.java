package javax.swing.plaf.synth;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import sun.swing.plaf.synth.SynthIcon;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthTreeUI.class */
public class SynthTreeUI extends BasicTreeUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;
    private SynthStyle cellStyle;
    private SynthContext paintContext;
    private boolean drawHorizontalLines;
    private boolean drawVerticalLines;
    private Object linesStyle;
    private int padding;
    private boolean useTreeColors;
    private Icon expandedIconWrapper = new ExpandedIconWrapper();

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthTreeUI();
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    public Icon getExpandedIcon() {
        return this.expandedIconWrapper;
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void installDefaults() {
        updateStyle(this.tree);
    }

    private void updateStyle(JTree jTree) {
        SynthContext context = getContext(jTree, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            setExpandedIcon(this.style.getIcon(context, "Tree.expandedIcon"));
            setCollapsedIcon(this.style.getIcon(context, "Tree.collapsedIcon"));
            setLeftChildIndent(this.style.getInt(context, "Tree.leftChildIndent", 0));
            setRightChildIndent(this.style.getInt(context, "Tree.rightChildIndent", 0));
            this.drawHorizontalLines = this.style.getBoolean(context, "Tree.drawHorizontalLines", true);
            this.drawVerticalLines = this.style.getBoolean(context, "Tree.drawVerticalLines", true);
            this.linesStyle = this.style.get(context, "Tree.linesStyle");
            Object obj = this.style.get(context, "Tree.rowHeight");
            if (obj != null) {
                LookAndFeel.installProperty(jTree, JTree.ROW_HEIGHT_PROPERTY, obj);
            }
            Object obj2 = this.style.get(context, "Tree.scrollsOnExpand");
            LookAndFeel.installProperty(jTree, JTree.SCROLLS_ON_EXPAND_PROPERTY, obj2 != null ? obj2 : Boolean.TRUE);
            this.padding = this.style.getInt(context, "Tree.padding", 0);
            this.largeModel = jTree.isLargeModel() && jTree.getRowHeight() > 0;
            this.useTreeColors = this.style.getBoolean(context, "Tree.rendererUseTreeColors", true);
            LookAndFeel.installProperty(jTree, JTree.SHOWS_ROOT_HANDLES_PROPERTY, Boolean.valueOf(this.style.getBoolean(context, "Tree.showsRootHandles", Boolean.TRUE.booleanValue())));
            if (synthStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
        SynthContext context2 = getContext(jTree, Region.TREE_CELL, 1);
        this.cellStyle = SynthLookAndFeel.updateStyle(context2, this);
        context2.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void installListeners() {
        super.installListeners();
        this.tree.addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, SynthLookAndFeel.getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SynthContext getContext(JComponent jComponent, Region region) {
        return getContext(jComponent, region, getComponentState(jComponent, region));
    }

    private SynthContext getContext(JComponent jComponent, Region region, int i2) {
        return SynthContext.getContext(jComponent, region, this.cellStyle, i2);
    }

    private int getComponentState(JComponent jComponent, Region region) {
        return 513;
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected TreeCellEditor createDefaultCellEditor() {
        SynthTreeCellEditor synthTreeCellEditor;
        TreeCellRenderer cellRenderer = this.tree.getCellRenderer();
        if (cellRenderer != null && (cellRenderer instanceof DefaultTreeCellRenderer)) {
            synthTreeCellEditor = new SynthTreeCellEditor(this.tree, (DefaultTreeCellRenderer) cellRenderer);
        } else {
            synthTreeCellEditor = new SynthTreeCellEditor(this.tree, null);
        }
        return synthTreeCellEditor;
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected TreeCellRenderer createDefaultCellRenderer() {
        return new SynthTreeCellRenderer();
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.tree, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        SynthContext context2 = getContext(this.tree, Region.TREE_CELL, 1);
        this.cellStyle.uninstallDefaults(context2);
        context2.dispose();
        this.cellStyle = null;
        if (this.tree.getTransferHandler() instanceof UIResource) {
            this.tree.setTransferHandler(null);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.tree.removePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintTreeBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintTreeBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        boolean expandedState;
        boolean zHasBeenExpanded;
        boolean expandedState2;
        boolean zHasBeenExpanded2;
        this.paintContext = synthContext;
        updateLeadSelectionRow();
        Rectangle clipBounds = graphics.getClipBounds();
        Insets insets = this.tree.getInsets();
        TreePath closestPathForLocation = getClosestPathForLocation(this.tree, 0, clipBounds.f12373y);
        Enumeration<TreePath> visiblePathsFrom = this.treeState.getVisiblePathsFrom(closestPathForLocation);
        int rowForPath = this.treeState.getRowForPath(closestPathForLocation);
        int i2 = clipBounds.f12373y + clipBounds.height;
        TreeModel model = this.tree.getModel();
        SynthContext context = getContext(this.tree, Region.TREE_CELL);
        this.drawingCache.clear();
        setHashColor(synthContext.getStyle().getColor(synthContext, ColorType.FOREGROUND));
        if (visiblePathsFrom != null) {
            boolean z2 = false;
            Rectangle rectangle = new Rectangle(0, 0, this.tree.getWidth(), 0);
            TreeCellRenderer cellRenderer = this.tree.getCellRenderer();
            DefaultTreeCellRenderer defaultTreeCellRenderer = cellRenderer instanceof DefaultTreeCellRenderer ? (DefaultTreeCellRenderer) cellRenderer : null;
            configureRenderer(context);
            while (!z2 && visiblePathsFrom.hasMoreElements()) {
                TreePath treePathNextElement2 = visiblePathsFrom.nextElement2();
                Rectangle pathBounds = getPathBounds(this.tree, treePathNextElement2);
                if (treePathNextElement2 != null && pathBounds != null) {
                    boolean zIsLeaf = model.isLeaf(treePathNextElement2.getLastPathComponent());
                    if (zIsLeaf) {
                        zHasBeenExpanded2 = false;
                        expandedState2 = false;
                    } else {
                        expandedState2 = this.treeState.getExpandedState(treePathNextElement2);
                        zHasBeenExpanded2 = this.tree.hasBeenExpanded(treePathNextElement2);
                    }
                    rectangle.f12373y = pathBounds.f12373y;
                    rectangle.height = pathBounds.height;
                    paintRow(cellRenderer, defaultTreeCellRenderer, synthContext, context, graphics, clipBounds, insets, pathBounds, rectangle, treePathNextElement2, rowForPath, expandedState2, zHasBeenExpanded2, zIsLeaf);
                    if (pathBounds.f12373y + pathBounds.height >= i2) {
                        z2 = true;
                    }
                } else {
                    z2 = true;
                }
                rowForPath++;
            }
            boolean zIsRootVisible = this.tree.isRootVisible();
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
            boolean z3 = false;
            Enumeration<TreePath> visiblePathsFrom2 = this.treeState.getVisiblePathsFrom(closestPathForLocation);
            while (!z3 && visiblePathsFrom2.hasMoreElements()) {
                TreePath treePathNextElement22 = visiblePathsFrom2.nextElement2();
                Rectangle pathBounds2 = getPathBounds(this.tree, treePathNextElement22);
                if (treePathNextElement22 != null && pathBounds2 != null) {
                    boolean zIsLeaf2 = model.isLeaf(treePathNextElement22.getLastPathComponent());
                    if (zIsLeaf2) {
                        zHasBeenExpanded = false;
                        expandedState = false;
                    } else {
                        expandedState = this.treeState.getExpandedState(treePathNextElement22);
                        zHasBeenExpanded = this.tree.hasBeenExpanded(treePathNextElement22);
                    }
                    TreePath parentPath2 = treePathNextElement22.getParentPath();
                    if (parentPath2 != null) {
                        if (this.drawingCache.get(parentPath2) == null) {
                            paintVerticalPartOfLeg(graphics, clipBounds, insets, parentPath2);
                            this.drawingCache.put(parentPath2, Boolean.TRUE);
                        }
                        paintHorizontalPartOfLeg(graphics, clipBounds, insets, pathBounds2, treePathNextElement22, rowForPath, expandedState, zHasBeenExpanded, zIsLeaf2);
                    } else if (zIsRootVisible && rowForPath == 0) {
                        paintHorizontalPartOfLeg(graphics, clipBounds, insets, pathBounds2, treePathNextElement22, rowForPath, expandedState, zHasBeenExpanded, zIsLeaf2);
                    }
                    if (shouldPaintExpandControl(treePathNextElement22, rowForPath, expandedState, zHasBeenExpanded, zIsLeaf2)) {
                        paintExpandControl(graphics, clipBounds, insets, pathBounds2, treePathNextElement22, rowForPath, expandedState, zHasBeenExpanded, zIsLeaf2);
                    }
                    if (pathBounds2.f12373y + pathBounds2.height >= i2) {
                        z3 = true;
                    }
                } else {
                    z3 = true;
                }
                rowForPath++;
            }
        }
        context.dispose();
        paintDropLine(graphics);
        this.rendererPane.removeAll();
        this.paintContext = null;
    }

    private void configureRenderer(SynthContext synthContext) {
        TreeCellRenderer cellRenderer = this.tree.getCellRenderer();
        if (cellRenderer instanceof DefaultTreeCellRenderer) {
            DefaultTreeCellRenderer defaultTreeCellRenderer = (DefaultTreeCellRenderer) cellRenderer;
            SynthStyle style = synthContext.getStyle();
            synthContext.setComponentState(513);
            Color textSelectionColor = defaultTreeCellRenderer.getTextSelectionColor();
            if (textSelectionColor == null || (textSelectionColor instanceof UIResource)) {
                defaultTreeCellRenderer.setTextSelectionColor(style.getColor(synthContext, ColorType.TEXT_FOREGROUND));
            }
            Color backgroundSelectionColor = defaultTreeCellRenderer.getBackgroundSelectionColor();
            if (backgroundSelectionColor == null || (backgroundSelectionColor instanceof UIResource)) {
                defaultTreeCellRenderer.setBackgroundSelectionColor(style.getColor(synthContext, ColorType.TEXT_BACKGROUND));
            }
            synthContext.setComponentState(1);
            Color textNonSelectionColor = defaultTreeCellRenderer.getTextNonSelectionColor();
            if (textNonSelectionColor == null || (textNonSelectionColor instanceof UIResource)) {
                defaultTreeCellRenderer.setTextNonSelectionColor(style.getColorForState(synthContext, ColorType.TEXT_FOREGROUND));
            }
            Color backgroundNonSelectionColor = defaultTreeCellRenderer.getBackgroundNonSelectionColor();
            if (backgroundNonSelectionColor == null || (backgroundNonSelectionColor instanceof UIResource)) {
                defaultTreeCellRenderer.setBackgroundNonSelectionColor(style.getColorForState(synthContext, ColorType.TEXT_BACKGROUND));
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void paintHorizontalPartOfLeg(Graphics graphics, Rectangle rectangle, Insets insets, Rectangle rectangle2, TreePath treePath, int i2, boolean z2, boolean z3, boolean z4) {
        if (this.drawHorizontalLines) {
            super.paintHorizontalPartOfLeg(graphics, rectangle, insets, rectangle2, treePath, i2, z2, z3, z4);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void paintHorizontalLine(Graphics graphics, JComponent jComponent, int i2, int i3, int i4) {
        this.paintContext.getStyle().getGraphicsUtils(this.paintContext).drawLine(this.paintContext, "Tree.horizontalLine", graphics, i3, i2, i4, i2, this.linesStyle);
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void paintVerticalPartOfLeg(Graphics graphics, Rectangle rectangle, Insets insets, TreePath treePath) {
        if (this.drawVerticalLines) {
            super.paintVerticalPartOfLeg(graphics, rectangle, insets, treePath);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void paintVerticalLine(Graphics graphics, JComponent jComponent, int i2, int i3, int i4) {
        this.paintContext.getStyle().getGraphicsUtils(this.paintContext).drawLine(this.paintContext, "Tree.verticalLine", graphics, i2, i3, i2, i4, this.linesStyle);
    }

    private void paintRow(TreeCellRenderer treeCellRenderer, DefaultTreeCellRenderer defaultTreeCellRenderer, SynthContext synthContext, SynthContext synthContext2, Graphics graphics, Rectangle rectangle, Insets insets, Rectangle rectangle2, Rectangle rectangle3, TreePath treePath, int i2, boolean z2, boolean z3, boolean z4) {
        int leadSelectionRow;
        boolean zIsRowSelected = this.tree.isRowSelected(i2);
        JTree.DropLocation dropLocation = this.tree.getDropLocation();
        boolean z5 = dropLocation != null && dropLocation.getChildIndex() == -1 && treePath == dropLocation.getPath();
        int i3 = 1;
        if (zIsRowSelected || z5) {
            i3 = 1 | 512;
        }
        if (this.tree.isFocusOwner() && i2 == getLeadSelectionRow()) {
            i3 |= 256;
        }
        synthContext2.setComponentState(i3);
        if (defaultTreeCellRenderer != null && (defaultTreeCellRenderer.getBorderSelectionColor() instanceof UIResource)) {
            defaultTreeCellRenderer.setBorderSelectionColor(this.style.getColor(synthContext2, ColorType.FOCUS));
        }
        SynthLookAndFeel.updateSubregion(synthContext2, graphics, rectangle3);
        synthContext2.getPainter().paintTreeCellBackground(synthContext2, graphics, rectangle3.f12372x, rectangle3.f12373y, rectangle3.width, rectangle3.height);
        synthContext2.getPainter().paintTreeCellBorder(synthContext2, graphics, rectangle3.f12372x, rectangle3.f12373y, rectangle3.width, rectangle3.height);
        if (this.editingComponent != null && this.editingRow == i2) {
            return;
        }
        if (this.tree.hasFocus()) {
            leadSelectionRow = getLeadSelectionRow();
        } else {
            leadSelectionRow = -1;
        }
        this.rendererPane.paintComponent(graphics, treeCellRenderer.getTreeCellRendererComponent(this.tree, treePath.getLastPathComponent(), zIsRowSelected, z2, z4, i2, leadSelectionRow == i2), this.tree, rectangle2.f12372x, rectangle2.f12373y, rectangle2.width, rectangle2.height, true);
    }

    private int findCenteredX(int i2, int i3) {
        if (this.tree.getComponentOrientation().isLeftToRight()) {
            return i2 - ((int) Math.ceil(i3 / 2.0d));
        }
        return i2 - ((int) Math.floor(i3 / 2.0d));
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void paintExpandControl(Graphics graphics, Rectangle rectangle, Insets insets, Rectangle rectangle2, TreePath treePath, int i2, boolean z2, boolean z3, boolean z4) {
        boolean zIsPathSelected = this.tree.getSelectionModel().isPathSelected(treePath);
        int componentState = this.paintContext.getComponentState();
        if (zIsPathSelected) {
            this.paintContext.setComponentState(componentState | 512);
        }
        super.paintExpandControl(graphics, rectangle, insets, rectangle2, treePath, i2, z2, z3, z4);
        this.paintContext.setComponentState(componentState);
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void drawCentered(Component component, Graphics graphics, Icon icon, int i2, int i3) {
        int iconWidth = SynthIcon.getIconWidth(icon, this.paintContext);
        int iconHeight = SynthIcon.getIconHeight(icon, this.paintContext);
        SynthIcon.paintIcon(icon, this.paintContext, graphics, findCenteredX(i2, iconWidth), i3 - (iconHeight / 2), iconWidth, iconHeight);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JTree) propertyChangeEvent.getSource());
        }
        if ("dropLocation" == propertyChangeEvent.getPropertyName()) {
            repaintDropLocation((JTree.DropLocation) propertyChangeEvent.getOldValue());
            repaintDropLocation(this.tree.getDropLocation());
        }
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected void paintDropLine(Graphics graphics) {
        Color color;
        JTree.DropLocation dropLocation = this.tree.getDropLocation();
        if (isDropLine(dropLocation) && (color = (Color) this.style.get(this.paintContext, "Tree.dropLineColor")) != null) {
            graphics.setColor(color);
            Rectangle dropLineRect = getDropLineRect(dropLocation);
            graphics.fillRect(dropLineRect.f12372x, dropLineRect.f12373y, dropLineRect.width, dropLineRect.height);
        }
    }

    private void repaintDropLocation(JTree.DropLocation dropLocation) {
        Rectangle pathBounds;
        if (dropLocation == null) {
            return;
        }
        if (isDropLine(dropLocation)) {
            pathBounds = getDropLineRect(dropLocation);
        } else {
            pathBounds = this.tree.getPathBounds(dropLocation.getPath());
            if (pathBounds != null) {
                pathBounds.f12372x = 0;
                pathBounds.width = this.tree.getWidth();
            }
        }
        if (pathBounds != null) {
            this.tree.repaint(pathBounds);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTreeUI
    protected int getRowX(int i2, int i3) {
        return super.getRowX(i2, i3) + this.padding;
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthTreeUI$SynthTreeCellRenderer.class */
    private class SynthTreeCellRenderer extends DefaultTreeCellRenderer implements UIResource {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !SynthTreeUI.class.desiredAssertionStatus();
        }

        SynthTreeCellRenderer() {
        }

        @Override // java.awt.Component
        public String getName() {
            return "Tree.cellRenderer";
        }

        @Override // javax.swing.tree.DefaultTreeCellRenderer, javax.swing.tree.TreeCellRenderer
        public Component getTreeCellRendererComponent(JTree jTree, Object obj, boolean z2, boolean z3, boolean z4, int i2, boolean z5) {
            if (!SynthTreeUI.this.useTreeColors && (z2 || z5)) {
                SynthLookAndFeel.setSelectedUI((SynthLabelUI) SynthLookAndFeel.getUIOfType(getUI(), SynthLabelUI.class), z2, z5, jTree.isEnabled(), false);
            } else {
                SynthLookAndFeel.resetSelectedUI();
            }
            return super.getTreeCellRendererComponent(jTree, obj, z2, z3, z4, i2, z5);
        }

        @Override // javax.swing.tree.DefaultTreeCellRenderer, javax.swing.JComponent, java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            paintComponent(graphics);
            if (this.hasFocus) {
                SynthContext context = SynthTreeUI.this.getContext(SynthTreeUI.this.tree, Region.TREE_CELL);
                if (context.getStyle() == null) {
                    if (!$assertionsDisabled) {
                        throw new AssertionError((Object) "SynthTreeCellRenderer is being used outside of UI that created it");
                    }
                    return;
                }
                int iconWidth = 0;
                Icon icon = getIcon();
                if (icon != null && getText() != null) {
                    iconWidth = icon.getIconWidth() + Math.max(0, getIconTextGap() - 1);
                }
                if (this.selected) {
                    context.setComponentState(513);
                } else {
                    context.setComponentState(1);
                }
                if (getComponentOrientation().isLeftToRight()) {
                    context.getPainter().paintTreeCellFocus(context, graphics, iconWidth, 0, getWidth() - iconWidth, getHeight());
                } else {
                    context.getPainter().paintTreeCellFocus(context, graphics, 0, 0, getWidth() - iconWidth, getHeight());
                }
                context.dispose();
            }
            SynthLookAndFeel.resetSelectedUI();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthTreeUI$SynthTreeCellEditor.class */
    private static class SynthTreeCellEditor extends DefaultTreeCellEditor {
        public SynthTreeCellEditor(JTree jTree, DefaultTreeCellRenderer defaultTreeCellRenderer) {
            super(jTree, defaultTreeCellRenderer);
            setBorderSelectionColor(null);
        }

        @Override // javax.swing.tree.DefaultTreeCellEditor
        protected TreeCellEditor createTreeCellEditor() {
            DefaultCellEditor defaultCellEditor = new DefaultCellEditor(new JTextField() { // from class: javax.swing.plaf.synth.SynthTreeUI.SynthTreeCellEditor.1
                @Override // java.awt.Component
                public String getName() {
                    return "Tree.cellEditor";
                }
            });
            defaultCellEditor.setClickCountToStart(1);
            return defaultCellEditor;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthTreeUI$ExpandedIconWrapper.class */
    private class ExpandedIconWrapper extends SynthIcon {
        private ExpandedIconWrapper() {
        }

        @Override // sun.swing.plaf.synth.SynthIcon
        public void paintIcon(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            if (synthContext == null) {
                SynthContext context = SynthTreeUI.this.getContext(SynthTreeUI.this.tree);
                SynthIcon.paintIcon(SynthTreeUI.this.expandedIcon, context, graphics, i2, i3, i4, i5);
                context.dispose();
                return;
            }
            SynthIcon.paintIcon(SynthTreeUI.this.expandedIcon, synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // sun.swing.plaf.synth.SynthIcon
        public int getIconWidth(SynthContext synthContext) {
            int iconWidth;
            if (synthContext == null) {
                SynthContext context = SynthTreeUI.this.getContext(SynthTreeUI.this.tree);
                iconWidth = SynthIcon.getIconWidth(SynthTreeUI.this.expandedIcon, context);
                context.dispose();
            } else {
                iconWidth = SynthIcon.getIconWidth(SynthTreeUI.this.expandedIcon, synthContext);
            }
            return iconWidth;
        }

        @Override // sun.swing.plaf.synth.SynthIcon
        public int getIconHeight(SynthContext synthContext) {
            int iconHeight;
            if (synthContext == null) {
                SynthContext context = SynthTreeUI.this.getContext(SynthTreeUI.this.tree);
                iconHeight = SynthIcon.getIconHeight(SynthTreeUI.this.expandedIcon, context);
                context.dispose();
            } else {
                iconHeight = SynthIcon.getIconHeight(SynthTreeUI.this.expandedIcon, synthContext);
            }
            return iconHeight;
        }
    }
}
