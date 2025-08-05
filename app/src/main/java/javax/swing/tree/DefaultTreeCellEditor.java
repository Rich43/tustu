package javax.swing.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.FontUIResource;

/* loaded from: rt.jar:javax/swing/tree/DefaultTreeCellEditor.class */
public class DefaultTreeCellEditor implements ActionListener, TreeCellEditor, TreeSelectionListener {
    protected TreeCellEditor realEditor;
    protected DefaultTreeCellRenderer renderer;
    protected Container editingContainer;
    protected transient Component editingComponent;
    protected boolean canEdit;
    protected transient int offset;
    protected transient JTree tree;
    protected transient TreePath lastPath;
    protected transient Timer timer;
    protected transient int lastRow;
    protected Color borderSelectionColor;
    protected transient Icon editingIcon;
    protected Font font;

    public DefaultTreeCellEditor(JTree jTree, DefaultTreeCellRenderer defaultTreeCellRenderer) {
        this(jTree, defaultTreeCellRenderer, null);
    }

    public DefaultTreeCellEditor(JTree jTree, DefaultTreeCellRenderer defaultTreeCellRenderer, TreeCellEditor treeCellEditor) {
        this.renderer = defaultTreeCellRenderer;
        this.realEditor = treeCellEditor;
        if (this.realEditor == null) {
            this.realEditor = createTreeCellEditor();
        }
        this.editingContainer = createContainer();
        setTree(jTree);
        setBorderSelectionColor(UIManager.getColor("Tree.editorBorderSelectionColor"));
    }

    public void setBorderSelectionColor(Color color) {
        this.borderSelectionColor = color;
    }

    public Color getBorderSelectionColor() {
        return this.borderSelectionColor;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return this.font;
    }

    @Override // javax.swing.tree.TreeCellEditor
    public Component getTreeCellEditorComponent(JTree jTree, Object obj, boolean z2, boolean z3, boolean z4, int i2) {
        setTree(jTree);
        this.lastRow = i2;
        determineOffset(jTree, obj, z2, z3, z4, i2);
        if (this.editingComponent != null) {
            this.editingContainer.remove(this.editingComponent);
        }
        this.editingComponent = this.realEditor.getTreeCellEditorComponent(jTree, obj, z2, z3, z4, i2);
        TreePath pathForRow = jTree.getPathForRow(i2);
        this.canEdit = (this.lastPath == null || pathForRow == null || !this.lastPath.equals(pathForRow)) ? false : true;
        Font font = getFont();
        if (font == null) {
            if (this.renderer != null) {
                font = this.renderer.getFont();
            }
            if (font == null) {
                font = jTree.getFont();
            }
        }
        this.editingContainer.setFont(font);
        prepareForEditing();
        return this.editingContainer;
    }

    @Override // javax.swing.CellEditor
    public Object getCellEditorValue() {
        return this.realEditor.getCellEditorValue();
    }

    @Override // javax.swing.CellEditor
    public boolean isCellEditable(EventObject eventObject) {
        boolean z2 = false;
        boolean z3 = false;
        if (eventObject != null && (eventObject.getSource() instanceof JTree)) {
            setTree((JTree) eventObject.getSource());
            if (eventObject instanceof MouseEvent) {
                TreePath pathForLocation = this.tree.getPathForLocation(((MouseEvent) eventObject).getX(), ((MouseEvent) eventObject).getY());
                z3 = (this.lastPath == null || pathForLocation == null || !this.lastPath.equals(pathForLocation)) ? false : true;
                if (pathForLocation != null) {
                    this.lastRow = this.tree.getRowForPath(pathForLocation);
                    Object lastPathComponent = pathForLocation.getLastPathComponent();
                    determineOffset(this.tree, lastPathComponent, this.tree.isRowSelected(this.lastRow), this.tree.isExpanded(pathForLocation), this.tree.getModel().isLeaf(lastPathComponent), this.lastRow);
                }
            }
        }
        if (!this.realEditor.isCellEditable(eventObject)) {
            return false;
        }
        if (canEditImmediately(eventObject)) {
            z2 = true;
        } else if (z3 && shouldStartEditingTimer(eventObject)) {
            startEditingTimer();
        } else if (this.timer != null && this.timer.isRunning()) {
            this.timer.stop();
        }
        if (z2) {
            prepareForEditing();
        }
        return z2;
    }

    @Override // javax.swing.CellEditor
    public boolean shouldSelectCell(EventObject eventObject) {
        return this.realEditor.shouldSelectCell(eventObject);
    }

    @Override // javax.swing.CellEditor
    public boolean stopCellEditing() {
        if (this.realEditor.stopCellEditing()) {
            cleanupAfterEditing();
            return true;
        }
        return false;
    }

    @Override // javax.swing.CellEditor
    public void cancelCellEditing() {
        this.realEditor.cancelCellEditing();
        cleanupAfterEditing();
    }

    @Override // javax.swing.CellEditor
    public void addCellEditorListener(CellEditorListener cellEditorListener) {
        this.realEditor.addCellEditorListener(cellEditorListener);
    }

    @Override // javax.swing.CellEditor
    public void removeCellEditorListener(CellEditorListener cellEditorListener) {
        this.realEditor.removeCellEditorListener(cellEditorListener);
    }

    public CellEditorListener[] getCellEditorListeners() {
        return ((DefaultCellEditor) this.realEditor).getCellEditorListeners();
    }

    @Override // javax.swing.event.TreeSelectionListener
    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        if (this.tree != null) {
            if (this.tree.getSelectionCount() == 1) {
                this.lastPath = this.tree.getSelectionPath();
            } else {
                this.lastPath = null;
            }
        }
        if (this.timer != null) {
            this.timer.stop();
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.tree != null && this.lastPath != null) {
            this.tree.startEditingAtPath(this.lastPath);
        }
    }

    protected void setTree(JTree jTree) {
        if (this.tree != jTree) {
            if (this.tree != null) {
                this.tree.removeTreeSelectionListener(this);
            }
            this.tree = jTree;
            if (this.tree != null) {
                this.tree.addTreeSelectionListener(this);
            }
            if (this.timer != null) {
                this.timer.stop();
            }
        }
    }

    protected boolean shouldStartEditingTimer(EventObject eventObject) {
        if ((eventObject instanceof MouseEvent) && SwingUtilities.isLeftMouseButton((MouseEvent) eventObject)) {
            MouseEvent mouseEvent = (MouseEvent) eventObject;
            return mouseEvent.getClickCount() == 1 && inHitRegion(mouseEvent.getX(), mouseEvent.getY());
        }
        return false;
    }

    protected void startEditingTimer() {
        if (this.timer == null) {
            this.timer = new Timer(1200, this);
            this.timer.setRepeats(false);
        }
        this.timer.start();
    }

    protected boolean canEditImmediately(EventObject eventObject) {
        if (!(eventObject instanceof MouseEvent) || !SwingUtilities.isLeftMouseButton((MouseEvent) eventObject)) {
            return eventObject == null;
        }
        MouseEvent mouseEvent = (MouseEvent) eventObject;
        return mouseEvent.getClickCount() > 2 && inHitRegion(mouseEvent.getX(), mouseEvent.getY());
    }

    protected boolean inHitRegion(int i2, int i3) {
        if (this.lastRow != -1 && this.tree != null) {
            Rectangle rowBounds = this.tree.getRowBounds(this.lastRow);
            if (this.tree.getComponentOrientation().isLeftToRight()) {
                if (rowBounds != null && i2 <= rowBounds.f12372x + this.offset && this.offset < rowBounds.width - 5) {
                    return false;
                }
                return true;
            }
            if (rowBounds == null) {
                return true;
            }
            if ((i2 >= ((rowBounds.f12372x + rowBounds.width) - this.offset) + 5 || i2 <= rowBounds.f12372x + 5) && this.offset < rowBounds.width - 5) {
                return false;
            }
            return true;
        }
        return true;
    }

    protected void determineOffset(JTree jTree, Object obj, boolean z2, boolean z3, boolean z4, int i2) {
        if (this.renderer != null) {
            if (z4) {
                this.editingIcon = this.renderer.getLeafIcon();
            } else if (z3) {
                this.editingIcon = this.renderer.getOpenIcon();
            } else {
                this.editingIcon = this.renderer.getClosedIcon();
            }
            if (this.editingIcon != null) {
                this.offset = this.renderer.getIconTextGap() + this.editingIcon.getIconWidth();
                return;
            } else {
                this.offset = this.renderer.getIconTextGap();
                return;
            }
        }
        this.editingIcon = null;
        this.offset = 0;
    }

    protected void prepareForEditing() {
        if (this.editingComponent != null) {
            this.editingContainer.add(this.editingComponent);
        }
    }

    protected Container createContainer() {
        return new EditorContainer();
    }

    protected TreeCellEditor createTreeCellEditor() {
        DefaultCellEditor defaultCellEditor = new DefaultCellEditor(new DefaultTextField(UIManager.getBorder("Tree.editorBorder"))) { // from class: javax.swing.tree.DefaultTreeCellEditor.1
            @Override // javax.swing.DefaultCellEditor, javax.swing.AbstractCellEditor, javax.swing.CellEditor
            public boolean shouldSelectCell(EventObject eventObject) {
                return super.shouldSelectCell(eventObject);
            }
        };
        defaultCellEditor.setClickCountToStart(1);
        return defaultCellEditor;
    }

    private void cleanupAfterEditing() {
        if (this.editingComponent != null) {
            this.editingContainer.remove(this.editingComponent);
        }
        this.editingComponent = null;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Vector vector = new Vector();
        objectOutputStream.defaultWriteObject();
        if (this.realEditor != null && (this.realEditor instanceof Serializable)) {
            vector.addElement("realEditor");
            vector.addElement(this.realEditor);
        }
        objectOutputStream.writeObject(vector);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Vector vector = (Vector) objectInputStream.readObject();
        if (0 < vector.size() && vector.elementAt(0).equals("realEditor")) {
            int i2 = 0 + 1;
            this.realEditor = (TreeCellEditor) vector.elementAt(i2);
            int i3 = i2 + 1;
        }
    }

    /* loaded from: rt.jar:javax/swing/tree/DefaultTreeCellEditor$DefaultTextField.class */
    public class DefaultTextField extends JTextField {
        protected Border border;

        public DefaultTextField(Border border) {
            setBorder(border);
        }

        @Override // javax.swing.JComponent
        public void setBorder(Border border) {
            super.setBorder(border);
            this.border = border;
        }

        @Override // javax.swing.JComponent
        public Border getBorder() {
            return this.border;
        }

        @Override // java.awt.Component, java.awt.MenuContainer
        public Font getFont() {
            Container parent;
            Font font = super.getFont();
            if ((font instanceof FontUIResource) && (parent = getParent()) != null && parent.getFont() != null) {
                font = parent.getFont();
            }
            return font;
        }

        @Override // javax.swing.JTextField, javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getPreferredSize() {
            Dimension preferredSize = super.getPreferredSize();
            if (DefaultTreeCellEditor.this.renderer != null && DefaultTreeCellEditor.this.getFont() == null) {
                preferredSize.height = DefaultTreeCellEditor.this.renderer.getPreferredSize().height;
            }
            return preferredSize;
        }
    }

    /* loaded from: rt.jar:javax/swing/tree/DefaultTreeCellEditor$EditorContainer.class */
    public class EditorContainer extends Container {
        public EditorContainer() {
            setLayout(null);
        }

        public void EditorContainer() {
            setLayout(null);
        }

        @Override // java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            int width = getWidth();
            int height = getHeight();
            if (DefaultTreeCellEditor.this.editingIcon != null) {
                int iCalculateIconY = calculateIconY(DefaultTreeCellEditor.this.editingIcon);
                if (getComponentOrientation().isLeftToRight()) {
                    DefaultTreeCellEditor.this.editingIcon.paintIcon(this, graphics, 0, iCalculateIconY);
                } else {
                    DefaultTreeCellEditor.this.editingIcon.paintIcon(this, graphics, width - DefaultTreeCellEditor.this.editingIcon.getIconWidth(), iCalculateIconY);
                }
            }
            Color borderSelectionColor = DefaultTreeCellEditor.this.getBorderSelectionColor();
            if (borderSelectionColor != null) {
                graphics.setColor(borderSelectionColor);
                graphics.drawRect(0, 0, width - 1, height - 1);
            }
            super.paint(graphics);
        }

        @Override // java.awt.Container, java.awt.Component
        public void doLayout() {
            if (DefaultTreeCellEditor.this.editingComponent != null) {
                int width = getWidth();
                int height = getHeight();
                if (getComponentOrientation().isLeftToRight()) {
                    DefaultTreeCellEditor.this.editingComponent.setBounds(DefaultTreeCellEditor.this.offset, 0, width - DefaultTreeCellEditor.this.offset, height);
                } else {
                    DefaultTreeCellEditor.this.editingComponent.setBounds(0, 0, width - DefaultTreeCellEditor.this.offset, height);
                }
            }
        }

        private int calculateIconY(Icon icon) {
            int iconHeight = icon.getIconHeight();
            int height = DefaultTreeCellEditor.this.editingComponent.getFontMetrics(DefaultTreeCellEditor.this.editingComponent.getFont()).getHeight();
            int i2 = (iconHeight / 2) - (height / 2);
            int iMin = Math.min(0, i2);
            return (getHeight() / 2) - (iMin + ((Math.max(iconHeight, i2 + height) - iMin) / 2));
        }

        @Override // java.awt.Container, java.awt.Component
        public Dimension getPreferredSize() {
            if (DefaultTreeCellEditor.this.editingComponent != null) {
                Dimension preferredSize = DefaultTreeCellEditor.this.editingComponent.getPreferredSize();
                preferredSize.width += DefaultTreeCellEditor.this.offset + 5;
                Dimension preferredSize2 = DefaultTreeCellEditor.this.renderer != null ? DefaultTreeCellEditor.this.renderer.getPreferredSize() : null;
                if (preferredSize2 != null) {
                    preferredSize.height = Math.max(preferredSize.height, preferredSize2.height);
                }
                if (DefaultTreeCellEditor.this.editingIcon != null) {
                    preferredSize.height = Math.max(preferredSize.height, DefaultTreeCellEditor.this.editingIcon.getIconHeight());
                }
                preferredSize.width = Math.max(preferredSize.width, 100);
                return preferredSize;
            }
            return new Dimension(0, 0);
        }
    }
}
