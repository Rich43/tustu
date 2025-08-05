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
import java.awt.IllegalComponentStateException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleIcon;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleText;
import javax.accessibility.AccessibleValue;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ListUI;
import javax.swing.text.Position;
import sun.awt.AWTAccessor;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/JList.class */
public class JList<E> extends JComponent implements Scrollable, Accessible {
    private static final String uiClassID = "ListUI";
    public static final int VERTICAL = 0;
    public static final int VERTICAL_WRAP = 1;
    public static final int HORIZONTAL_WRAP = 2;
    private int fixedCellWidth;
    private int fixedCellHeight;
    private int horizontalScrollIncrement;
    private E prototypeCellValue;
    private int visibleRowCount;
    private Color selectionForeground;
    private Color selectionBackground;
    private boolean dragEnabled;
    private ListSelectionModel selectionModel;
    private ListModel<E> dataModel;
    private ListCellRenderer<? super E> cellRenderer;
    private ListSelectionListener selectionListener;
    private int layoutOrientation;
    private DropMode dropMode;
    private transient DropLocation dropLocation;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JList.class.desiredAssertionStatus();
    }

    /* loaded from: rt.jar:javax/swing/JList$DropLocation.class */
    public static final class DropLocation extends TransferHandler.DropLocation {
        private final int index;
        private final boolean isInsert;

        private DropLocation(Point point, int i2, boolean z2) {
            super(point);
            this.index = i2;
            this.isInsert = z2;
        }

        public int getIndex() {
            return this.index;
        }

        public boolean isInsert() {
            return this.isInsert;
        }

        @Override // javax.swing.TransferHandler.DropLocation
        public String toString() {
            return getClass().getName() + "[dropPoint=" + ((Object) getDropPoint()) + ",index=" + this.index + ",insert=" + this.isInsert + "]";
        }
    }

    public JList(ListModel<E> listModel) {
        this.fixedCellWidth = -1;
        this.fixedCellHeight = -1;
        this.horizontalScrollIncrement = -1;
        this.visibleRowCount = 8;
        this.dropMode = DropMode.USE_SELECTION;
        if (listModel == null) {
            throw new IllegalArgumentException("dataModel must be non null");
        }
        ToolTipManager.sharedInstance().registerComponent(this);
        this.layoutOrientation = 0;
        this.dataModel = listModel;
        this.selectionModel = createSelectionModel();
        setAutoscrolls(true);
        setOpaque(true);
        updateUI();
    }

    public JList(final E[] eArr) {
        this(new AbstractListModel<E>() { // from class: javax.swing.JList.1
            @Override // javax.swing.ListModel
            public int getSize() {
                return eArr.length;
            }

            @Override // javax.swing.ListModel
            public E getElementAt(int i2) {
                return (E) eArr[i2];
            }
        });
    }

    public JList(final Vector<? extends E> vector) {
        this(new AbstractListModel<E>() { // from class: javax.swing.JList.2
            @Override // javax.swing.ListModel
            public int getSize() {
                return vector.size();
            }

            @Override // javax.swing.ListModel
            public E getElementAt(int i2) {
                return (E) vector.elementAt(i2);
            }
        });
    }

    public JList() {
        this(new AbstractListModel<E>() { // from class: javax.swing.JList.3
            @Override // javax.swing.ListModel
            public int getSize() {
                return 0;
            }

            @Override // javax.swing.ListModel
            public E getElementAt(int i2) {
                throw new IndexOutOfBoundsException("No Data Model");
            }
        });
    }

    public ListUI getUI() {
        return (ListUI) this.ui;
    }

    public void setUI(ListUI listUI) {
        super.setUI((ComponentUI) listUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((ListUI) UIManager.getUI(this));
        ListCellRenderer<? super E> cellRenderer = getCellRenderer();
        if (cellRenderer instanceof Component) {
            SwingUtilities.updateComponentTreeUI((Component) cellRenderer);
        }
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    private void updateFixedCellSize() {
        ListCellRenderer<? super E> cellRenderer = getCellRenderer();
        E prototypeCellValue = getPrototypeCellValue();
        if (cellRenderer != null && prototypeCellValue != null) {
            Component listCellRendererComponent = cellRenderer.getListCellRendererComponent(this, prototypeCellValue, 0, false, false);
            Font font = listCellRendererComponent.getFont();
            listCellRendererComponent.setFont(getFont());
            Dimension preferredSize = listCellRendererComponent.getPreferredSize();
            this.fixedCellWidth = preferredSize.width;
            this.fixedCellHeight = preferredSize.height;
            listCellRendererComponent.setFont(font);
        }
    }

    public E getPrototypeCellValue() {
        return this.prototypeCellValue;
    }

    public void setPrototypeCellValue(E e2) {
        E e3 = this.prototypeCellValue;
        this.prototypeCellValue = e2;
        if (e2 != null && !e2.equals(e3)) {
            updateFixedCellSize();
        }
        firePropertyChange("prototypeCellValue", e3, e2);
    }

    public int getFixedCellWidth() {
        return this.fixedCellWidth;
    }

    public void setFixedCellWidth(int i2) {
        int i3 = this.fixedCellWidth;
        this.fixedCellWidth = i2;
        firePropertyChange("fixedCellWidth", i3, this.fixedCellWidth);
    }

    public int getFixedCellHeight() {
        return this.fixedCellHeight;
    }

    public void setFixedCellHeight(int i2) {
        int i3 = this.fixedCellHeight;
        this.fixedCellHeight = i2;
        firePropertyChange("fixedCellHeight", i3, this.fixedCellHeight);
    }

    @Transient
    public ListCellRenderer<? super E> getCellRenderer() {
        return this.cellRenderer;
    }

    public void setCellRenderer(ListCellRenderer<? super E> listCellRenderer) {
        ListCellRenderer<? super E> listCellRenderer2 = this.cellRenderer;
        this.cellRenderer = listCellRenderer;
        if (listCellRenderer != null && !listCellRenderer.equals(listCellRenderer2)) {
            updateFixedCellSize();
        }
        firePropertyChange("cellRenderer", listCellRenderer2, listCellRenderer);
    }

    public Color getSelectionForeground() {
        return this.selectionForeground;
    }

    public void setSelectionForeground(Color color) {
        Color color2 = this.selectionForeground;
        this.selectionForeground = color;
        firePropertyChange("selectionForeground", color2, color);
    }

    public Color getSelectionBackground() {
        return this.selectionBackground;
    }

    public void setSelectionBackground(Color color) {
        Color color2 = this.selectionBackground;
        this.selectionBackground = color;
        firePropertyChange("selectionBackground", color2, color);
    }

    public int getVisibleRowCount() {
        return this.visibleRowCount;
    }

    public void setVisibleRowCount(int i2) {
        int i3 = this.visibleRowCount;
        this.visibleRowCount = Math.max(0, i2);
        firePropertyChange(JTree.VISIBLE_ROW_COUNT_PROPERTY, i3, i2);
    }

    public int getLayoutOrientation() {
        return this.layoutOrientation;
    }

    public void setLayoutOrientation(int i2) {
        int i3 = this.layoutOrientation;
        switch (i2) {
            case 0:
            case 1:
            case 2:
                this.layoutOrientation = i2;
                firePropertyChange("layoutOrientation", i3, i2);
                return;
            default:
                throw new IllegalArgumentException("layoutOrientation must be one of: VERTICAL, HORIZONTAL_WRAP or VERTICAL_WRAP");
        }
    }

    public int getFirstVisibleIndex() {
        int iLocationToIndex;
        Rectangle cellBounds;
        Rectangle visibleRect = getVisibleRect();
        if (getComponentOrientation().isLeftToRight()) {
            iLocationToIndex = locationToIndex(visibleRect.getLocation());
        } else {
            iLocationToIndex = locationToIndex(new Point((visibleRect.f12372x + visibleRect.width) - 1, visibleRect.f12373y));
        }
        if (iLocationToIndex != -1 && (cellBounds = getCellBounds(iLocationToIndex, iLocationToIndex)) != null) {
            SwingUtilities.computeIntersection(visibleRect.f12372x, visibleRect.f12373y, visibleRect.width, visibleRect.height, cellBounds);
            if (cellBounds.width == 0 || cellBounds.height == 0) {
                iLocationToIndex = -1;
            }
        }
        return iLocationToIndex;
    }

    public int getLastVisibleIndex() {
        Point point;
        Rectangle cellBounds;
        int i2;
        boolean zIsLeftToRight = getComponentOrientation().isLeftToRight();
        Rectangle visibleRect = getVisibleRect();
        if (zIsLeftToRight) {
            point = new Point((visibleRect.f12372x + visibleRect.width) - 1, (visibleRect.f12373y + visibleRect.height) - 1);
        } else {
            point = new Point(visibleRect.f12372x, (visibleRect.f12373y + visibleRect.height) - 1);
        }
        int iLocationToIndex = locationToIndex(point);
        if (iLocationToIndex != -1 && (cellBounds = getCellBounds(iLocationToIndex, iLocationToIndex)) != null) {
            SwingUtilities.computeIntersection(visibleRect.f12372x, visibleRect.f12373y, visibleRect.width, visibleRect.height, cellBounds);
            if (cellBounds.width == 0 || cellBounds.height == 0) {
                boolean z2 = getLayoutOrientation() == 2;
                Point point2 = z2 ? new Point(point.f12370x, visibleRect.f12373y) : new Point(visibleRect.f12372x, point.f12371y);
                int iLocationToIndex2 = -1;
                iLocationToIndex = -1;
                do {
                    i2 = iLocationToIndex2;
                    iLocationToIndex2 = locationToIndex(point2);
                    if (iLocationToIndex2 != -1) {
                        Rectangle cellBounds2 = getCellBounds(iLocationToIndex2, iLocationToIndex2);
                        if (iLocationToIndex2 != iLocationToIndex && cellBounds2 != null && cellBounds2.contains(point2)) {
                            iLocationToIndex = iLocationToIndex2;
                            if (z2) {
                                point2.f12371y = cellBounds2.f12373y + cellBounds2.height;
                                if (point2.f12371y >= point.f12371y) {
                                    i2 = iLocationToIndex2;
                                }
                            } else {
                                point2.f12370x = cellBounds2.f12372x + cellBounds2.width;
                                if (point2.f12370x >= point.f12370x) {
                                    i2 = iLocationToIndex2;
                                }
                            }
                        } else {
                            i2 = iLocationToIndex2;
                        }
                    }
                    if (iLocationToIndex2 == -1) {
                        break;
                    }
                } while (i2 != iLocationToIndex2);
            }
        }
        return iLocationToIndex;
    }

    public void ensureIndexIsVisible(int i2) {
        Rectangle cellBounds = getCellBounds(i2, i2);
        if (cellBounds != null) {
            scrollRectToVisible(cellBounds);
        }
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
        throw new IllegalArgumentException(((Object) dropMode) + ": Unsupported drop mode for list");
    }

    public final DropMode getDropMode() {
        return this.dropMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javax.swing.JComponent
    public DropLocation dropLocationForPoint(Point point) {
        DropLocation dropLocation = null;
        Rectangle cellBounds = null;
        int iLocationToIndex = locationToIndex(point);
        if (iLocationToIndex != -1) {
            cellBounds = getCellBounds(iLocationToIndex, iLocationToIndex);
        }
        switch (this.dropMode) {
            case USE_SELECTION:
            case ON:
                dropLocation = new DropLocation(point, (cellBounds == null || !cellBounds.contains(point)) ? -1 : iLocationToIndex, false);
                break;
            case INSERT:
                if (iLocationToIndex == -1) {
                    dropLocation = new DropLocation(point, getModel().getSize(), true);
                    break;
                } else {
                    if (this.layoutOrientation == 2) {
                        if (SwingUtilities2.liesInHorizontal(cellBounds, point, getComponentOrientation().isLeftToRight(), false) == SwingUtilities2.Section.TRAILING) {
                            iLocationToIndex++;
                        } else if (iLocationToIndex == getModel().getSize() - 1 && point.f12371y >= cellBounds.f12373y + cellBounds.height) {
                            iLocationToIndex++;
                        }
                    } else if (SwingUtilities2.liesInVertical(cellBounds, point, false) == SwingUtilities2.Section.TRAILING) {
                        iLocationToIndex++;
                    }
                    dropLocation = new DropLocation(point, iLocationToIndex, true);
                    break;
                }
                break;
            case ON_OR_INSERT:
                if (iLocationToIndex == -1) {
                    dropLocation = new DropLocation(point, getModel().getSize(), true);
                    break;
                } else {
                    boolean z2 = false;
                    if (this.layoutOrientation == 2) {
                        SwingUtilities2.Section sectionLiesInHorizontal = SwingUtilities2.liesInHorizontal(cellBounds, point, getComponentOrientation().isLeftToRight(), true);
                        if (sectionLiesInHorizontal == SwingUtilities2.Section.TRAILING) {
                            iLocationToIndex++;
                            z2 = true;
                        } else if (iLocationToIndex == getModel().getSize() - 1 && point.f12371y >= cellBounds.f12373y + cellBounds.height) {
                            iLocationToIndex++;
                            z2 = true;
                        } else if (sectionLiesInHorizontal == SwingUtilities2.Section.LEADING) {
                            z2 = true;
                        }
                    } else {
                        SwingUtilities2.Section sectionLiesInVertical = SwingUtilities2.liesInVertical(cellBounds, point, true);
                        if (sectionLiesInVertical == SwingUtilities2.Section.LEADING) {
                            z2 = true;
                        } else if (sectionLiesInVertical == SwingUtilities2.Section.TRAILING) {
                            iLocationToIndex++;
                            z2 = true;
                        }
                    }
                    dropLocation = new DropLocation(point, iLocationToIndex, z2);
                    break;
                }
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError((Object) "Unexpected drop mode");
                }
                break;
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
                    setSelectedIndices(((int[][]) obj)[0]);
                    SwingUtilities2.setLeadAnchorWithoutSelection(getSelectionModel(), ((int[][]) obj)[1][1], ((int[][]) obj)[1][0]);
                }
            } else {
                if (this.dropLocation == null) {
                    obj2 = new int[]{getSelectedIndices(), new int[]{getAnchorSelectionIndex(), getLeadSelectionIndex()}};
                } else {
                    obj2 = obj;
                }
                int index = dropLocation2.getIndex();
                if (index == -1) {
                    clearSelection();
                    getSelectionModel().setAnchorSelectionIndex(-1);
                    getSelectionModel().setLeadSelectionIndex(-1);
                } else {
                    setSelectionInterval(index, index);
                }
            }
        }
        DropLocation dropLocation3 = this.dropLocation;
        this.dropLocation = dropLocation2;
        firePropertyChange("dropLocation", dropLocation3, this.dropLocation);
        return obj2;
    }

    public final DropLocation getDropLocation() {
        return this.dropLocation;
    }

    public int getNextMatch(String str, int i2, Position.Bias bias) {
        String string;
        ListModel<E> model = getModel();
        int size = model.getSize();
        if (str == null) {
            throw new IllegalArgumentException();
        }
        if (i2 < 0 || i2 >= size) {
            throw new IllegalArgumentException();
        }
        String upperCase = str.toUpperCase();
        int i3 = bias == Position.Bias.Forward ? 1 : -1;
        int i4 = i2;
        do {
            E elementAt = model.getElementAt(i4);
            if (elementAt != null) {
                if (elementAt instanceof String) {
                    string = ((String) elementAt).toUpperCase();
                } else {
                    string = elementAt.toString();
                    if (string != null) {
                        string = string.toUpperCase();
                    }
                }
                if (string != null && string.startsWith(upperCase)) {
                    return i4;
                }
            }
            i4 = ((i4 + i3) + size) % size;
        } while (i4 != i2);
        return -1;
    }

    @Override // javax.swing.JComponent
    public String getToolTipText(MouseEvent mouseEvent) {
        Rectangle cellBounds;
        if (mouseEvent != null) {
            Point point = mouseEvent.getPoint();
            int iLocationToIndex = locationToIndex(point);
            ListCellRenderer<? super E> cellRenderer = getCellRenderer();
            if (iLocationToIndex != -1 && cellRenderer != null && (cellBounds = getCellBounds(iLocationToIndex, iLocationToIndex)) != null && cellBounds.contains(point.f12370x, point.f12371y)) {
                ListSelectionModel selectionModel = getSelectionModel();
                Component listCellRendererComponent = cellRenderer.getListCellRendererComponent(this, getModel().getElementAt(iLocationToIndex), iLocationToIndex, selectionModel.isSelectedIndex(iLocationToIndex), hasFocus() && selectionModel.getLeadSelectionIndex() == iLocationToIndex);
                if (listCellRendererComponent instanceof JComponent) {
                    point.translate(-cellBounds.f12372x, -cellBounds.f12373y);
                    MouseEvent mouseEvent2 = new MouseEvent(listCellRendererComponent, mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), point.f12370x, point.f12371y, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
                    AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
                    mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
                    String toolTipText = ((JComponent) listCellRendererComponent).getToolTipText(mouseEvent2);
                    if (toolTipText != null) {
                        return toolTipText;
                    }
                }
            }
        }
        return super.getToolTipText();
    }

    public int locationToIndex(Point point) {
        ListUI ui = getUI();
        if (ui != null) {
            return ui.locationToIndex(this, point);
        }
        return -1;
    }

    public Point indexToLocation(int i2) {
        ListUI ui = getUI();
        if (ui != null) {
            return ui.indexToLocation(this, i2);
        }
        return null;
    }

    public Rectangle getCellBounds(int i2, int i3) {
        ListUI ui = getUI();
        if (ui != null) {
            return ui.getCellBounds(this, i2, i3);
        }
        return null;
    }

    public ListModel<E> getModel() {
        return this.dataModel;
    }

    public void setModel(ListModel<E> listModel) {
        if (listModel == null) {
            throw new IllegalArgumentException("model must be non null");
        }
        ListModel<E> listModel2 = this.dataModel;
        this.dataModel = listModel;
        firePropertyChange("model", listModel2, this.dataModel);
        clearSelection();
    }

    public void setListData(final E[] eArr) {
        setModel(new AbstractListModel<E>() { // from class: javax.swing.JList.4
            @Override // javax.swing.ListModel
            public int getSize() {
                return eArr.length;
            }

            @Override // javax.swing.ListModel
            public E getElementAt(int i2) {
                return (E) eArr[i2];
            }
        });
    }

    public void setListData(final Vector<? extends E> vector) {
        setModel(new AbstractListModel<E>() { // from class: javax.swing.JList.5
            @Override // javax.swing.ListModel
            public int getSize() {
                return vector.size();
            }

            @Override // javax.swing.ListModel
            public E getElementAt(int i2) {
                return (E) vector.elementAt(i2);
            }
        });
    }

    protected ListSelectionModel createSelectionModel() {
        return new DefaultListSelectionModel();
    }

    public ListSelectionModel getSelectionModel() {
        return this.selectionModel;
    }

    protected void fireSelectionValueChanged(int i2, int i3, boolean z2) {
        Object[] listenerList = this.listenerList.getListenerList();
        ListSelectionEvent listSelectionEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ListSelectionListener.class) {
                if (listSelectionEvent == null) {
                    listSelectionEvent = new ListSelectionEvent(this, i2, i3, z2);
                }
                ((ListSelectionListener) listenerList[length + 1]).valueChanged(listSelectionEvent);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/JList$ListSelectionHandler.class */
    private class ListSelectionHandler implements ListSelectionListener, Serializable {
        private ListSelectionHandler() {
        }

        @Override // javax.swing.event.ListSelectionListener
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            JList.this.fireSelectionValueChanged(listSelectionEvent.getFirstIndex(), listSelectionEvent.getLastIndex(), listSelectionEvent.getValueIsAdjusting());
        }
    }

    public void addListSelectionListener(ListSelectionListener listSelectionListener) {
        if (this.selectionListener == null) {
            this.selectionListener = new ListSelectionHandler();
            getSelectionModel().addListSelectionListener(this.selectionListener);
        }
        this.listenerList.add(ListSelectionListener.class, listSelectionListener);
    }

    public void removeListSelectionListener(ListSelectionListener listSelectionListener) {
        this.listenerList.remove(ListSelectionListener.class, listSelectionListener);
    }

    public ListSelectionListener[] getListSelectionListeners() {
        return (ListSelectionListener[]) this.listenerList.getListeners(ListSelectionListener.class);
    }

    public void setSelectionModel(ListSelectionModel listSelectionModel) {
        if (listSelectionModel == null) {
            throw new IllegalArgumentException("selectionModel must be non null");
        }
        if (this.selectionListener != null) {
            this.selectionModel.removeListSelectionListener(this.selectionListener);
            listSelectionModel.addListSelectionListener(this.selectionListener);
        }
        ListSelectionModel listSelectionModel2 = this.selectionModel;
        this.selectionModel = listSelectionModel;
        firePropertyChange("selectionModel", listSelectionModel2, listSelectionModel);
    }

    public void setSelectionMode(int i2) {
        getSelectionModel().setSelectionMode(i2);
    }

    public int getSelectionMode() {
        return getSelectionModel().getSelectionMode();
    }

    public int getAnchorSelectionIndex() {
        return getSelectionModel().getAnchorSelectionIndex();
    }

    public int getLeadSelectionIndex() {
        return getSelectionModel().getLeadSelectionIndex();
    }

    public int getMinSelectionIndex() {
        return getSelectionModel().getMinSelectionIndex();
    }

    public int getMaxSelectionIndex() {
        return getSelectionModel().getMaxSelectionIndex();
    }

    public boolean isSelectedIndex(int i2) {
        return getSelectionModel().isSelectedIndex(i2);
    }

    public boolean isSelectionEmpty() {
        return getSelectionModel().isSelectionEmpty();
    }

    public void clearSelection() {
        getSelectionModel().clearSelection();
    }

    public void setSelectionInterval(int i2, int i3) {
        getSelectionModel().setSelectionInterval(i2, i3);
    }

    public void addSelectionInterval(int i2, int i3) {
        getSelectionModel().addSelectionInterval(i2, i3);
    }

    public void removeSelectionInterval(int i2, int i3) {
        getSelectionModel().removeSelectionInterval(i2, i3);
    }

    public void setValueIsAdjusting(boolean z2) {
        getSelectionModel().setValueIsAdjusting(z2);
    }

    public boolean getValueIsAdjusting() {
        return getSelectionModel().getValueIsAdjusting();
    }

    @Transient
    public int[] getSelectedIndices() {
        ListSelectionModel selectionModel = getSelectionModel();
        int minSelectionIndex = selectionModel.getMinSelectionIndex();
        int maxSelectionIndex = selectionModel.getMaxSelectionIndex();
        if (minSelectionIndex < 0 || maxSelectionIndex < 0) {
            return new int[0];
        }
        int[] iArr = new int[1 + (maxSelectionIndex - minSelectionIndex)];
        int i2 = 0;
        for (int i3 = minSelectionIndex; i3 <= maxSelectionIndex; i3++) {
            if (selectionModel.isSelectedIndex(i3)) {
                int i4 = i2;
                i2++;
                iArr[i4] = i3;
            }
        }
        int[] iArr2 = new int[i2];
        System.arraycopy(iArr, 0, iArr2, 0, i2);
        return iArr2;
    }

    public void setSelectedIndex(int i2) {
        if (i2 >= getModel().getSize()) {
            return;
        }
        getSelectionModel().setSelectionInterval(i2, i2);
    }

    public void setSelectedIndices(int[] iArr) {
        ListSelectionModel selectionModel = getSelectionModel();
        selectionModel.clearSelection();
        int size = getModel().getSize();
        for (int i2 : iArr) {
            if (i2 < size) {
                selectionModel.addSelectionInterval(i2, i2);
            }
        }
    }

    @Deprecated
    public Object[] getSelectedValues() {
        ListSelectionModel selectionModel = getSelectionModel();
        ListModel<E> model = getModel();
        int minSelectionIndex = selectionModel.getMinSelectionIndex();
        int maxSelectionIndex = selectionModel.getMaxSelectionIndex();
        if (minSelectionIndex < 0 || maxSelectionIndex < 0) {
            return new Object[0];
        }
        Object[] objArr = new Object[1 + (maxSelectionIndex - minSelectionIndex)];
        int i2 = 0;
        for (int i3 = minSelectionIndex; i3 <= maxSelectionIndex; i3++) {
            if (selectionModel.isSelectedIndex(i3)) {
                int i4 = i2;
                i2++;
                objArr[i4] = model.getElementAt(i3);
            }
        }
        Object[] objArr2 = new Object[i2];
        System.arraycopy(objArr, 0, objArr2, 0, i2);
        return objArr2;
    }

    public List<E> getSelectedValuesList() {
        ListSelectionModel selectionModel = getSelectionModel();
        ListModel<E> model = getModel();
        int minSelectionIndex = selectionModel.getMinSelectionIndex();
        int maxSelectionIndex = selectionModel.getMaxSelectionIndex();
        if (minSelectionIndex < 0 || maxSelectionIndex < 0) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (int i2 = minSelectionIndex; i2 <= maxSelectionIndex; i2++) {
            if (selectionModel.isSelectedIndex(i2)) {
                arrayList.add(model.getElementAt(i2));
            }
        }
        return arrayList;
    }

    public int getSelectedIndex() {
        return getMinSelectionIndex();
    }

    public E getSelectedValue() {
        int minSelectionIndex = getMinSelectionIndex();
        if (minSelectionIndex == -1) {
            return null;
        }
        return getModel().getElementAt(minSelectionIndex);
    }

    public void setSelectedValue(Object obj, boolean z2) {
        if (obj == null) {
            setSelectedIndex(-1);
        } else if (!obj.equals(getSelectedValue())) {
            ListModel<E> model = getModel();
            int size = model.getSize();
            for (int i2 = 0; i2 < size; i2++) {
                if (obj.equals(model.getElementAt(i2))) {
                    setSelectedIndex(i2);
                    if (z2) {
                        ensureIndexIsVisible(i2);
                    }
                    repaint();
                    return;
                }
            }
            setSelectedIndex(-1);
        }
        repaint();
    }

    private void checkScrollableParameters(Rectangle rectangle, int i2) {
        if (rectangle == null) {
            throw new IllegalArgumentException("visibleRect must be non-null");
        }
        switch (i2) {
            case 0:
            case 1:
                return;
            default:
                throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
        }
    }

    @Override // javax.swing.Scrollable
    public Dimension getPreferredScrollableViewportSize() {
        int i2;
        if (getLayoutOrientation() != 0) {
            return getPreferredSize();
        }
        Insets insets = getInsets();
        int i3 = insets.left + insets.right;
        int i4 = insets.top + insets.bottom;
        int visibleRowCount = getVisibleRowCount();
        int fixedCellWidth = getFixedCellWidth();
        int fixedCellHeight = getFixedCellHeight();
        if (fixedCellWidth > 0 && fixedCellHeight > 0) {
            return new Dimension(fixedCellWidth + i3, (visibleRowCount * fixedCellHeight) + i4);
        }
        if (getModel().getSize() > 0) {
            int i5 = getPreferredSize().width;
            Rectangle cellBounds = getCellBounds(0, 0);
            if (cellBounds != null) {
                i2 = (visibleRowCount * cellBounds.height) + i4;
            } else {
                i2 = 1;
            }
            return new Dimension(i5, i2);
        }
        return new Dimension(fixedCellWidth > 0 ? fixedCellWidth : 256, (fixedCellHeight > 0 ? fixedCellHeight : 16) * visibleRowCount);
    }

    @Override // javax.swing.Scrollable
    public int getScrollableUnitIncrement(Rectangle rectangle, int i2, int i3) {
        Point point;
        Rectangle cellBounds;
        int i4;
        int i5;
        checkScrollableParameters(rectangle, i2);
        if (i2 == 1) {
            int iLocationToIndex = locationToIndex(rectangle.getLocation());
            if (iLocationToIndex == -1) {
                return 0;
            }
            if (i3 > 0) {
                Rectangle cellBounds2 = getCellBounds(iLocationToIndex, iLocationToIndex);
                if (cellBounds2 == null) {
                    return 0;
                }
                return cellBounds2.height - (rectangle.f12373y - cellBounds2.f12373y);
            }
            Rectangle cellBounds3 = getCellBounds(iLocationToIndex, iLocationToIndex);
            if (cellBounds3.f12373y == rectangle.f12373y && iLocationToIndex == 0) {
                return 0;
            }
            if (cellBounds3.f12373y == rectangle.f12373y) {
                Point location = cellBounds3.getLocation();
                location.f12371y--;
                int iLocationToIndex2 = locationToIndex(location);
                Rectangle cellBounds4 = getCellBounds(iLocationToIndex2, iLocationToIndex2);
                if (cellBounds4 == null || cellBounds4.f12373y >= cellBounds3.f12373y) {
                    return 0;
                }
                return cellBounds4.height;
            }
            return rectangle.f12373y - cellBounds3.f12373y;
        }
        if (i2 == 0 && getLayoutOrientation() != 0) {
            boolean zIsLeftToRight = getComponentOrientation().isLeftToRight();
            if (zIsLeftToRight) {
                point = rectangle.getLocation();
            } else {
                point = new Point((rectangle.f12372x + rectangle.width) - 1, rectangle.f12373y);
            }
            int iLocationToIndex3 = locationToIndex(point);
            if (iLocationToIndex3 != -1 && (cellBounds = getCellBounds(iLocationToIndex3, iLocationToIndex3)) != null && cellBounds.contains(point)) {
                if (zIsLeftToRight) {
                    i4 = rectangle.f12372x;
                    i5 = cellBounds.f12372x;
                } else {
                    i4 = rectangle.f12372x + rectangle.width;
                    i5 = cellBounds.f12372x + cellBounds.width;
                }
                if (i5 != i4) {
                    if (i3 < 0) {
                        return Math.abs(i4 - i5);
                    }
                    if (zIsLeftToRight) {
                        return (i5 + cellBounds.width) - i4;
                    }
                    return i4 - cellBounds.f12372x;
                }
                return cellBounds.width;
            }
        }
        Font font = getFont();
        if (font != null) {
            return font.getSize();
        }
        return 1;
    }

    @Override // javax.swing.Scrollable
    public int getScrollableBlockIncrement(Rectangle rectangle, int i2, int i3) {
        Rectangle cellBounds;
        Rectangle cellBounds2;
        int i4;
        Rectangle cellBounds3;
        Rectangle cellBounds4;
        checkScrollableParameters(rectangle, i2);
        if (i2 == 1) {
            int i5 = rectangle.height;
            if (i3 > 0) {
                int iLocationToIndex = locationToIndex(new Point(rectangle.f12372x, (rectangle.f12373y + rectangle.height) - 1));
                if (iLocationToIndex != -1 && (cellBounds4 = getCellBounds(iLocationToIndex, iLocationToIndex)) != null) {
                    i5 = cellBounds4.f12373y - rectangle.f12373y;
                    if (i5 == 0 && iLocationToIndex < getModel().getSize() - 1) {
                        i5 = cellBounds4.height;
                    }
                }
            } else {
                int iLocationToIndex2 = locationToIndex(new Point(rectangle.f12372x, rectangle.f12373y - rectangle.height));
                int firstVisibleIndex = getFirstVisibleIndex();
                if (iLocationToIndex2 != -1) {
                    if (firstVisibleIndex == -1) {
                        firstVisibleIndex = locationToIndex(rectangle.getLocation());
                    }
                    Rectangle cellBounds5 = getCellBounds(iLocationToIndex2, iLocationToIndex2);
                    Rectangle cellBounds6 = getCellBounds(firstVisibleIndex, firstVisibleIndex);
                    if (cellBounds5 != null && cellBounds6 != null) {
                        while (cellBounds5.f12373y + rectangle.height < cellBounds6.f12373y + cellBounds6.height && cellBounds5.f12373y < cellBounds6.f12373y) {
                            iLocationToIndex2++;
                            cellBounds5 = getCellBounds(iLocationToIndex2, iLocationToIndex2);
                        }
                        i5 = rectangle.f12373y - cellBounds5.f12373y;
                        if (i5 <= 0 && cellBounds5.f12373y > 0 && (cellBounds3 = getCellBounds(iLocationToIndex2 - 1, i4)) != null) {
                            i5 = rectangle.f12373y - cellBounds3.f12373y;
                        }
                    }
                }
            }
            return i5;
        }
        if (i2 == 0 && getLayoutOrientation() != 0) {
            boolean zIsLeftToRight = getComponentOrientation().isLeftToRight();
            int i6 = rectangle.width;
            if (i3 > 0) {
                int iLocationToIndex3 = locationToIndex(new Point(rectangle.f12372x + (zIsLeftToRight ? rectangle.width - 1 : 0), rectangle.f12373y));
                if (iLocationToIndex3 != -1 && (cellBounds2 = getCellBounds(iLocationToIndex3, iLocationToIndex3)) != null) {
                    if (zIsLeftToRight) {
                        i6 = cellBounds2.f12372x - rectangle.f12372x;
                    } else {
                        i6 = (rectangle.f12372x + rectangle.width) - (cellBounds2.f12372x + cellBounds2.width);
                    }
                    if (i6 < 0) {
                        i6 += cellBounds2.width;
                    } else if (i6 == 0 && iLocationToIndex3 < getModel().getSize() - 1) {
                        i6 = cellBounds2.width;
                    }
                }
            } else {
                int iLocationToIndex4 = locationToIndex(new Point(rectangle.f12372x + (zIsLeftToRight ? -rectangle.width : (rectangle.width - 1) + rectangle.width), rectangle.f12373y));
                if (iLocationToIndex4 != -1 && (cellBounds = getCellBounds(iLocationToIndex4, iLocationToIndex4)) != null) {
                    int i7 = cellBounds.f12372x + cellBounds.width;
                    if (zIsLeftToRight) {
                        i6 = (cellBounds.f12372x >= rectangle.f12372x - rectangle.width || i7 >= rectangle.f12372x) ? rectangle.f12372x - cellBounds.f12372x : rectangle.f12372x - i7;
                    } else {
                        int i8 = rectangle.f12372x + rectangle.width;
                        i6 = (i7 <= i8 + rectangle.width || cellBounds.f12372x <= i8) ? i7 - i8 : cellBounds.f12372x - i8;
                    }
                }
            }
            return i6;
        }
        return rectangle.width;
    }

    @Override // javax.swing.Scrollable
    public boolean getScrollableTracksViewportWidth() {
        if (getLayoutOrientation() == 2 && getVisibleRowCount() <= 0) {
            return true;
        }
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        return (unwrappedParent instanceof JViewport) && unwrappedParent.getWidth() > getPreferredSize().width;
    }

    @Override // javax.swing.Scrollable
    public boolean getScrollableTracksViewportHeight() {
        if (getLayoutOrientation() == 1 && getVisibleRowCount() <= 0) {
            return true;
        }
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        return (unwrappedParent instanceof JViewport) && unwrappedParent.getHeight() > getPreferredSize().height;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",fixedCellHeight=" + this.fixedCellHeight + ",fixedCellWidth=" + this.fixedCellWidth + ",horizontalScrollIncrement=" + this.horizontalScrollIncrement + ",selectionBackground=" + (this.selectionBackground != null ? this.selectionBackground.toString() : "") + ",selectionForeground=" + (this.selectionForeground != null ? this.selectionForeground.toString() : "") + ",visibleRowCount=" + this.visibleRowCount + ",layoutOrientation=" + this.layoutOrientation;
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJList();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JList$AccessibleJList.class */
    protected class AccessibleJList extends JComponent.AccessibleJComponent implements AccessibleSelection, PropertyChangeListener, ListSelectionListener, ListDataListener {
        int leadSelectionIndex;

        public AccessibleJList() {
            super();
            JList.this.addPropertyChangeListener(this);
            JList.this.getSelectionModel().addListSelectionListener(this);
            JList.this.getModel().addListDataListener(this);
            this.leadSelectionIndex = JList.this.getLeadSelectionIndex();
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            Object oldValue = propertyChangeEvent.getOldValue();
            Object newValue = propertyChangeEvent.getNewValue();
            if (propertyName.compareTo("model") == 0) {
                if (oldValue != null && (oldValue instanceof ListModel)) {
                    ((ListModel) oldValue).removeListDataListener(this);
                }
                if (newValue != null && (newValue instanceof ListModel)) {
                    ((ListModel) newValue).addListDataListener(this);
                    return;
                }
                return;
            }
            if (propertyName.compareTo("selectionModel") == 0) {
                if (oldValue != null && (oldValue instanceof ListSelectionModel)) {
                    ((ListSelectionModel) oldValue).removeListSelectionListener(this);
                }
                if (newValue != null && (newValue instanceof ListSelectionModel)) {
                    ((ListSelectionModel) newValue).addListSelectionListener(this);
                }
                firePropertyChange(AccessibleContext.ACCESSIBLE_SELECTION_PROPERTY, false, true);
            }
        }

        @Override // javax.swing.event.ListSelectionListener
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            int i2 = this.leadSelectionIndex;
            this.leadSelectionIndex = JList.this.getLeadSelectionIndex();
            if (i2 != this.leadSelectionIndex) {
                firePropertyChange(AccessibleContext.ACCESSIBLE_ACTIVE_DESCENDANT_PROPERTY, i2 >= 0 ? getAccessibleChild(i2) : null, this.leadSelectionIndex >= 0 ? getAccessibleChild(this.leadSelectionIndex) : null);
            }
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, false, true);
            firePropertyChange(AccessibleContext.ACCESSIBLE_SELECTION_PROPERTY, false, true);
            AccessibleStateSet accessibleStateSet = getAccessibleStateSet();
            if (JList.this.getSelectionModel().getSelectionMode() != 0) {
                if (!accessibleStateSet.contains(AccessibleState.MULTISELECTABLE)) {
                    accessibleStateSet.add(AccessibleState.MULTISELECTABLE);
                    firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.MULTISELECTABLE);
                    return;
                }
                return;
            }
            if (accessibleStateSet.contains(AccessibleState.MULTISELECTABLE)) {
                accessibleStateSet.remove(AccessibleState.MULTISELECTABLE);
                firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.MULTISELECTABLE, null);
            }
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalAdded(ListDataEvent listDataEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, false, true);
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalRemoved(ListDataEvent listDataEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, false, true);
        }

        @Override // javax.swing.event.ListDataListener
        public void contentsChanged(ListDataEvent listDataEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, false, true);
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (JList.this.selectionModel.getSelectionMode() != 0) {
                accessibleStateSet.add(AccessibleState.MULTISELECTABLE);
            }
            return accessibleStateSet;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.LIST;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            int iLocationToIndex = JList.this.locationToIndex(point);
            if (iLocationToIndex >= 0) {
                return new ActionableAccessibleJListChild(JList.this, iLocationToIndex);
            }
            return null;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            return JList.this.getModel().getSize();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            if (i2 >= JList.this.getModel().getSize()) {
                return null;
            }
            return new ActionableAccessibleJListChild(JList.this, i2);
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleSelection getAccessibleSelection() {
            return this;
        }

        @Override // javax.accessibility.AccessibleSelection
        public int getAccessibleSelectionCount() {
            return JList.this.getSelectedIndices().length;
        }

        @Override // javax.accessibility.AccessibleSelection
        public Accessible getAccessibleSelection(int i2) {
            int accessibleSelectionCount = getAccessibleSelectionCount();
            if (i2 < 0 || i2 >= accessibleSelectionCount) {
                return null;
            }
            return getAccessibleChild(JList.this.getSelectedIndices()[i2]);
        }

        @Override // javax.accessibility.AccessibleSelection
        public boolean isAccessibleChildSelected(int i2) {
            return JList.this.isSelectedIndex(i2);
        }

        @Override // javax.accessibility.AccessibleSelection
        public void addAccessibleSelection(int i2) {
            JList.this.addSelectionInterval(i2, i2);
        }

        @Override // javax.accessibility.AccessibleSelection
        public void removeAccessibleSelection(int i2) {
            JList.this.removeSelectionInterval(i2, i2);
        }

        @Override // javax.accessibility.AccessibleSelection
        public void clearAccessibleSelection() {
            JList.this.clearSelection();
        }

        @Override // javax.accessibility.AccessibleSelection
        public void selectAllAccessibleSelection() {
            JList.this.addSelectionInterval(0, getAccessibleChildrenCount() - 1);
        }

        /* loaded from: rt.jar:javax/swing/JList$AccessibleJList$AccessibleJListChild.class */
        protected class AccessibleJListChild extends AccessibleContext implements Accessible, AccessibleComponent {
            private JList<E> parent;
            int indexInParent;
            private Component component = null;
            private AccessibleContext accessibleContext = null;
            private ListModel<E> listModel;
            private ListCellRenderer<? super E> cellRenderer;

            public AccessibleJListChild(JList<E> jList, int i2) {
                this.parent = null;
                this.cellRenderer = null;
                this.parent = jList;
                setAccessibleParent(jList);
                this.indexInParent = i2;
                if (jList != null) {
                    this.listModel = jList.getModel();
                    this.cellRenderer = jList.getCellRenderer();
                }
            }

            private Component getCurrentComponent() {
                return getComponentAtIndex(this.indexInParent);
            }

            AccessibleContext getCurrentAccessibleContext() {
                Component componentAtIndex = getComponentAtIndex(this.indexInParent);
                if (componentAtIndex instanceof Accessible) {
                    return componentAtIndex.getAccessibleContext();
                }
                return null;
            }

            private Component getComponentAtIndex(int i2) {
                if (i2 >= 0 && i2 < this.listModel.getSize() && this.parent != null && this.listModel != null && this.cellRenderer != null) {
                    return this.cellRenderer.getListCellRendererComponent(this.parent, this.listModel.getElementAt(i2), i2, this.parent.isSelectedIndex(i2), this.parent.isFocusOwner() && i2 == this.parent.getLeadSelectionIndex());
                }
                return null;
            }

            @Override // javax.accessibility.Accessible
            public AccessibleContext getAccessibleContext() {
                return this;
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleName() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleName();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public void setAccessibleName(String str) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.setAccessibleName(str);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleDescription() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleDescription();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public void setAccessibleDescription(String str) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.setAccessibleDescription(str);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleRole getAccessibleRole() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleRole();
                }
                return null;
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
                accessibleStateSet.add(AccessibleState.SELECTABLE);
                if (this.parent.isFocusOwner() && this.indexInParent == this.parent.getLeadSelectionIndex()) {
                    accessibleStateSet.add(AccessibleState.ACTIVE);
                }
                if (this.parent.isSelectedIndex(this.indexInParent)) {
                    accessibleStateSet.add(AccessibleState.SELECTED);
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
                accessibleStateSet.add(AccessibleState.TRANSIENT);
                return accessibleStateSet;
            }

            @Override // javax.accessibility.AccessibleContext
            public int getAccessibleIndexInParent() {
                return this.indexInParent;
            }

            @Override // javax.accessibility.AccessibleContext
            public int getAccessibleChildrenCount() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleChildrenCount();
                }
                return 0;
            }

            @Override // javax.accessibility.AccessibleContext
            public Accessible getAccessibleChild(int i2) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    Accessible accessibleChild = currentAccessibleContext.getAccessibleChild(i2);
                    currentAccessibleContext.setAccessibleParent(this);
                    return accessibleChild;
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public Locale getLocale() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getLocale();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.addPropertyChangeListener(propertyChangeListener);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.removePropertyChangeListener(propertyChangeListener);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleComponent getAccessibleComponent() {
                return this;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleSelection getAccessibleSelection() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleSelection();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleText getAccessibleText() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleText();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleValue getAccessibleValue() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleValue();
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
                int firstVisibleIndex = this.parent.getFirstVisibleIndex();
                int lastVisibleIndex = this.parent.getLastVisibleIndex();
                if (lastVisibleIndex == -1) {
                    lastVisibleIndex = this.parent.getModel().getSize() - 1;
                }
                return this.indexInParent >= firstVisibleIndex && this.indexInParent <= lastVisibleIndex;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setVisible(boolean z2) {
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean isShowing() {
                return this.parent.isShowing() && isVisible();
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
                if (this.parent != null) {
                    try {
                        Point locationOnScreen = this.parent.getLocationOnScreen();
                        Point pointIndexToLocation = this.parent.indexToLocation(this.indexInParent);
                        if (pointIndexToLocation != null) {
                            pointIndexToLocation.translate(locationOnScreen.f12370x, locationOnScreen.f12371y);
                            return pointIndexToLocation;
                        }
                        return null;
                    } catch (IllegalComponentStateException e2) {
                        return null;
                    }
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public Point getLocation() {
                if (this.parent != null) {
                    return this.parent.indexToLocation(this.indexInParent);
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setLocation(Point point) {
                if (this.parent != null && this.parent.contains(point)) {
                    JList.this.ensureIndexIsVisible(this.indexInParent);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Rectangle getBounds() {
                if (this.parent != null) {
                    return this.parent.getCellBounds(this.indexInParent, this.indexInParent);
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setBounds(Rectangle rectangle) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setBounds(rectangle);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Dimension getSize() {
                Rectangle bounds = getBounds();
                if (bounds != null) {
                    return bounds.getSize();
                }
                return null;
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

            @Override // javax.accessibility.AccessibleContext
            public AccessibleIcon[] getAccessibleIcon() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleIcon();
                }
                return null;
            }
        }

        /* loaded from: rt.jar:javax/swing/JList$AccessibleJList$ActionableAccessibleJListChild.class */
        private class ActionableAccessibleJListChild extends JList<E>.AccessibleJList.AccessibleJListChild implements AccessibleAction {
            ActionableAccessibleJListChild(JList<E> jList, int i2) {
                super(jList, i2);
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleAction getAccessibleAction() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext == null) {
                    return null;
                }
                AccessibleAction accessibleAction = currentAccessibleContext.getAccessibleAction();
                if (accessibleAction != null) {
                    return accessibleAction;
                }
                return this;
            }

            @Override // javax.accessibility.AccessibleAction
            public boolean doAccessibleAction(int i2) {
                if (i2 == 0) {
                    JList.this.setSelectedIndex(this.indexInParent);
                    return true;
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleAction
            public String getAccessibleActionDescription(int i2) {
                if (i2 == 0) {
                    return UIManager.getString("AbstractButton.clickText");
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleAction
            public int getAccessibleActionCount() {
                return 1;
            }
        }
    }
}
