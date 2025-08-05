package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.CellRendererPane;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ListUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.DragRecognitionSupport;
import javax.swing.text.Position;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicListUI.class */
public class BasicListUI extends ListUI {
    protected CellRendererPane rendererPane;
    protected FocusListener focusListener;
    protected MouseInputListener mouseInputListener;
    protected ListSelectionListener listSelectionListener;
    protected ListDataListener listDataListener;
    protected PropertyChangeListener propertyChangeListener;
    private Handler handler;
    private int listHeight;
    private int listWidth;
    private int layoutOrientation;
    private int columnCount;
    private int preferredHeight;
    private int rowsPerColumn;
    protected static final int modelChanged = 1;
    protected static final int selectionModelChanged = 2;
    protected static final int fontChanged = 4;
    protected static final int fixedCellWidthChanged = 8;
    protected static final int fixedCellHeightChanged = 16;
    protected static final int prototypeCellValueChanged = 32;
    protected static final int cellRendererChanged = 64;
    private static final int layoutOrientationChanged = 128;
    private static final int heightChanged = 256;
    private static final int widthChanged = 512;
    private static final int componentOrientationChanged = 1024;
    private static final int DROP_LINE_THICKNESS = 2;
    private static final int CHANGE_LEAD = 0;
    private static final int CHANGE_SELECTION = 1;
    private static final int EXTEND_SELECTION = 2;
    private static final StringBuilder BASELINE_COMPONENT_KEY = new StringBuilder("List.baselineComponent");
    private static final TransferHandler defaultTransferHandler = new ListTransferHandler();
    protected JList list = null;
    protected int[] cellHeights = null;
    protected int cellHeight = -1;
    protected int cellWidth = -1;
    protected int updateLayoutStateNeeded = 1;
    private long timeFactor = 1000;
    private boolean isFileList = false;
    private boolean isLeftToRight = true;

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("selectPreviousColumn"));
        lazyActionMap.put(new Actions("selectPreviousColumnExtendSelection"));
        lazyActionMap.put(new Actions("selectPreviousColumnChangeLead"));
        lazyActionMap.put(new Actions("selectNextColumn"));
        lazyActionMap.put(new Actions("selectNextColumnExtendSelection"));
        lazyActionMap.put(new Actions("selectNextColumnChangeLead"));
        lazyActionMap.put(new Actions("selectPreviousRow"));
        lazyActionMap.put(new Actions("selectPreviousRowExtendSelection"));
        lazyActionMap.put(new Actions("selectPreviousRowChangeLead"));
        lazyActionMap.put(new Actions("selectNextRow"));
        lazyActionMap.put(new Actions("selectNextRowExtendSelection"));
        lazyActionMap.put(new Actions("selectNextRowChangeLead"));
        lazyActionMap.put(new Actions("selectFirstRow"));
        lazyActionMap.put(new Actions("selectFirstRowExtendSelection"));
        lazyActionMap.put(new Actions("selectFirstRowChangeLead"));
        lazyActionMap.put(new Actions("selectLastRow"));
        lazyActionMap.put(new Actions("selectLastRowExtendSelection"));
        lazyActionMap.put(new Actions("selectLastRowChangeLead"));
        lazyActionMap.put(new Actions("scrollUp"));
        lazyActionMap.put(new Actions("scrollUpExtendSelection"));
        lazyActionMap.put(new Actions("scrollUpChangeLead"));
        lazyActionMap.put(new Actions("scrollDown"));
        lazyActionMap.put(new Actions("scrollDownExtendSelection"));
        lazyActionMap.put(new Actions("scrollDownChangeLead"));
        lazyActionMap.put(new Actions("selectAll"));
        lazyActionMap.put(new Actions("clearSelection"));
        lazyActionMap.put(new Actions("addToSelection"));
        lazyActionMap.put(new Actions("toggleAndAnchor"));
        lazyActionMap.put(new Actions("extendTo"));
        lazyActionMap.put(new Actions("moveSelectionTo"));
        lazyActionMap.put(TransferHandler.getCutAction().getValue("Name"), TransferHandler.getCutAction());
        lazyActionMap.put(TransferHandler.getCopyAction().getValue("Name"), TransferHandler.getCopyAction());
        lazyActionMap.put(TransferHandler.getPasteAction().getValue("Name"), TransferHandler.getPasteAction());
    }

    protected void paintCell(Graphics graphics, int i2, Rectangle rectangle, ListCellRenderer listCellRenderer, ListModel listModel, ListSelectionModel listSelectionModel, int i3) {
        Object elementAt = listModel.getElementAt(i2);
        Component listCellRendererComponent = listCellRenderer.getListCellRendererComponent(this.list, elementAt, i2, listSelectionModel.isSelectedIndex(i2), this.list.hasFocus() && i2 == i3);
        int i4 = rectangle.f12372x;
        int i5 = rectangle.f12373y;
        int i6 = rectangle.width;
        int i7 = rectangle.height;
        if (this.isFileList) {
            int iMin = Math.min(i6, listCellRendererComponent.getPreferredSize().width + 4);
            if (!this.isLeftToRight) {
                i4 += i6 - iMin;
            }
            i6 = iMin;
        }
        this.rendererPane.paintComponent(graphics, listCellRendererComponent, this.list, i4, i5, i6, i7, true);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        Shape clip = graphics.getClip();
        paintImpl(graphics, jComponent);
        graphics.setClip(clip);
        paintDropLine(graphics);
    }

    private void paintImpl(Graphics graphics, JComponent jComponent) {
        int size;
        int iConvertLocationToColumn;
        int iConvertLocationToColumn2;
        switch (this.layoutOrientation) {
            case 1:
                if (this.list.getHeight() != this.listHeight) {
                    this.updateLayoutStateNeeded |= 256;
                    redrawList();
                    break;
                }
                break;
            case 2:
                if (this.list.getWidth() != this.listWidth) {
                    this.updateLayoutStateNeeded |= 512;
                    redrawList();
                    break;
                }
                break;
        }
        maybeUpdateLayoutState();
        ListCellRenderer cellRenderer = this.list.getCellRenderer();
        ListModel model = this.list.getModel();
        ListSelectionModel selectionModel = this.list.getSelectionModel();
        if (cellRenderer == null || (size = model.getSize()) == 0) {
            return;
        }
        Rectangle clipBounds = graphics.getClipBounds();
        if (jComponent.getComponentOrientation().isLeftToRight()) {
            iConvertLocationToColumn = convertLocationToColumn(clipBounds.f12372x, clipBounds.f12373y);
            iConvertLocationToColumn2 = convertLocationToColumn(clipBounds.f12372x + clipBounds.width, clipBounds.f12373y);
        } else {
            iConvertLocationToColumn = convertLocationToColumn(clipBounds.f12372x + clipBounds.width, clipBounds.f12373y);
            iConvertLocationToColumn2 = convertLocationToColumn(clipBounds.f12372x, clipBounds.f12373y);
        }
        int i2 = clipBounds.f12373y + clipBounds.height;
        int iAdjustIndex = adjustIndex(this.list.getLeadSelectionIndex(), this.list);
        int i3 = this.layoutOrientation == 2 ? this.columnCount : 1;
        for (int i4 = iConvertLocationToColumn; i4 <= iConvertLocationToColumn2; i4++) {
            int iConvertLocationToRowInColumn = convertLocationToRowInColumn(clipBounds.f12373y, i4);
            int rowCount = getRowCount(i4);
            int modelIndex = getModelIndex(i4, iConvertLocationToRowInColumn);
            Rectangle cellBounds = getCellBounds(this.list, modelIndex, modelIndex);
            if (cellBounds == null) {
                return;
            }
            while (iConvertLocationToRowInColumn < rowCount && cellBounds.f12373y < i2 && modelIndex < size) {
                cellBounds.height = getHeight(i4, iConvertLocationToRowInColumn);
                graphics.setClip(cellBounds.f12372x, cellBounds.f12373y, cellBounds.width, cellBounds.height);
                graphics.clipRect(clipBounds.f12372x, clipBounds.f12373y, clipBounds.width, clipBounds.height);
                paintCell(graphics, modelIndex, cellBounds, cellRenderer, model, selectionModel, iAdjustIndex);
                cellBounds.f12373y += cellBounds.height;
                modelIndex += i3;
                iConvertLocationToRowInColumn++;
            }
        }
        this.rendererPane.removeAll();
    }

    private void paintDropLine(Graphics graphics) {
        Color color;
        JList.DropLocation dropLocation = this.list.getDropLocation();
        if (dropLocation != null && dropLocation.isInsert() && (color = DefaultLookup.getColor(this.list, this, "List.dropLineColor", null)) != null) {
            graphics.setColor(color);
            Rectangle dropLineRect = getDropLineRect(dropLocation);
            graphics.fillRect(dropLineRect.f12372x, dropLineRect.f12373y, dropLineRect.width, dropLineRect.height);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rectangle getDropLineRect(JList.DropLocation dropLocation) {
        Rectangle cellBounds;
        int size = this.list.getModel().getSize();
        if (size == 0) {
            Insets insets = this.list.getInsets();
            if (this.layoutOrientation == 2) {
                if (this.isLeftToRight) {
                    return new Rectangle(insets.left, insets.top, 2, 20);
                }
                return new Rectangle((this.list.getWidth() - 2) - insets.right, insets.top, 2, 20);
            }
            return new Rectangle(insets.left, insets.top, (this.list.getWidth() - insets.left) - insets.right, 2);
        }
        int index = dropLocation.getIndex();
        boolean z2 = false;
        if (this.layoutOrientation == 2) {
            if (index == size) {
                z2 = true;
            } else if (index != 0 && convertModelToRow(index) != convertModelToRow(index - 1)) {
                Rectangle cellBounds2 = getCellBounds(this.list, index - 1);
                Rectangle cellBounds3 = getCellBounds(this.list, index);
                Point dropPoint = dropLocation.getDropPoint();
                if (this.isLeftToRight) {
                    z2 = Point2D.distance((double) (cellBounds2.f12372x + cellBounds2.width), (double) (cellBounds2.f12373y + ((int) (((double) cellBounds2.height) / 2.0d))), (double) dropPoint.f12370x, (double) dropPoint.f12371y) < Point2D.distance((double) cellBounds3.f12372x, (double) (cellBounds3.f12373y + ((int) (((double) cellBounds3.height) / 2.0d))), (double) dropPoint.f12370x, (double) dropPoint.f12371y);
                } else {
                    z2 = Point2D.distance((double) cellBounds2.f12372x, (double) (cellBounds2.f12373y + ((int) (((double) cellBounds2.height) / 2.0d))), (double) dropPoint.f12370x, (double) dropPoint.f12371y) < Point2D.distance((double) (cellBounds3.f12372x + cellBounds3.width), (double) (cellBounds3.f12373y + ((int) (((double) cellBounds2.height) / 2.0d))), (double) dropPoint.f12370x, (double) dropPoint.f12371y);
                }
            }
            if (z2) {
                cellBounds = getCellBounds(this.list, index - 1);
                if (this.isLeftToRight) {
                    cellBounds.f12372x += cellBounds.width;
                } else {
                    cellBounds.f12372x -= 2;
                }
            } else {
                cellBounds = getCellBounds(this.list, index);
                if (!this.isLeftToRight) {
                    cellBounds.f12372x += cellBounds.width - 2;
                }
            }
            if (cellBounds.f12372x >= this.list.getWidth()) {
                cellBounds.f12372x = this.list.getWidth() - 2;
            } else if (cellBounds.f12372x < 0) {
                cellBounds.f12372x = 0;
            }
            cellBounds.width = 2;
        } else if (this.layoutOrientation == 1) {
            if (index == size) {
                cellBounds = getCellBounds(this.list, index - 1);
                cellBounds.f12373y += cellBounds.height;
            } else if (index != 0 && convertModelToColumn(index) != convertModelToColumn(index - 1)) {
                Rectangle cellBounds4 = getCellBounds(this.list, index - 1);
                Rectangle cellBounds5 = getCellBounds(this.list, index);
                Point dropPoint2 = dropLocation.getDropPoint();
                if (Point2D.distance(cellBounds4.f12372x + ((int) (cellBounds4.width / 2.0d)), cellBounds4.f12373y + cellBounds4.height, dropPoint2.f12370x, dropPoint2.f12371y) < Point2D.distance(cellBounds5.f12372x + ((int) (cellBounds5.width / 2.0d)), cellBounds5.f12373y, dropPoint2.f12370x, dropPoint2.f12371y)) {
                    cellBounds = getCellBounds(this.list, index - 1);
                    cellBounds.f12373y += cellBounds.height;
                } else {
                    cellBounds = getCellBounds(this.list, index);
                }
            } else {
                cellBounds = getCellBounds(this.list, index);
            }
            if (cellBounds.f12373y >= this.list.getHeight()) {
                cellBounds.f12373y = this.list.getHeight() - 2;
            }
            cellBounds.height = 2;
        } else {
            if (index == size) {
                cellBounds = getCellBounds(this.list, index - 1);
                cellBounds.f12373y += cellBounds.height;
            } else {
                cellBounds = getCellBounds(this.list, index);
            }
            if (cellBounds.f12373y >= this.list.getHeight()) {
                cellBounds.f12373y = this.list.getHeight() - 2;
            }
            cellBounds.height = 2;
        }
        return cellBounds;
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        super.getBaseline(jComponent, i2, i3);
        int fixedCellHeight = this.list.getFixedCellHeight();
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        Component listCellRendererComponent = (Component) lookAndFeelDefaults.get(BASELINE_COMPONENT_KEY);
        if (listCellRendererComponent == null) {
            ListCellRenderer defaultListCellRenderer = (ListCellRenderer) UIManager.get("List.cellRenderer");
            if (defaultListCellRenderer == null) {
                defaultListCellRenderer = new DefaultListCellRenderer();
            }
            listCellRendererComponent = defaultListCellRenderer.getListCellRendererComponent(this.list, "a", -1, false, false);
            lookAndFeelDefaults.put(BASELINE_COMPONENT_KEY, listCellRendererComponent);
        }
        listCellRendererComponent.setFont(this.list.getFont());
        if (fixedCellHeight == -1) {
            fixedCellHeight = listCellRendererComponent.getPreferredSize().height;
        }
        return listCellRendererComponent.getBaseline(Integer.MAX_VALUE, fixedCellHeight) + this.list.getInsets().top;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        int i2;
        maybeUpdateLayoutState();
        int size = this.list.getModel().getSize() - 1;
        if (size < 0) {
            return new Dimension(0, 0);
        }
        Insets insets = this.list.getInsets();
        int i3 = (this.cellWidth * this.columnCount) + insets.left + insets.right;
        if (this.layoutOrientation != 0) {
            i2 = this.preferredHeight;
        } else {
            Rectangle cellBounds = getCellBounds(this.list, size);
            if (cellBounds != null) {
                i2 = cellBounds.f12373y + cellBounds.height + insets.bottom;
            } else {
                i2 = 0;
            }
        }
        return new Dimension(i3, i2);
    }

    protected void selectPreviousIndex() {
        int selectedIndex = this.list.getSelectedIndex();
        if (selectedIndex > 0) {
            int i2 = selectedIndex - 1;
            this.list.setSelectedIndex(i2);
            this.list.ensureIndexIsVisible(i2);
        }
    }

    protected void selectNextIndex() {
        int selectedIndex = this.list.getSelectedIndex();
        if (selectedIndex + 1 < this.list.getModel().getSize()) {
            int i2 = selectedIndex + 1;
            this.list.setSelectedIndex(i2);
            this.list.ensureIndexIsVisible(i2);
        }
    }

    protected void installKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.list, 0, getInputMap(0));
        LazyActionMap.installLazyActionMap(this.list, BasicListUI.class, "List.actionMap");
    }

    InputMap getInputMap(int i2) {
        InputMap inputMap;
        if (i2 == 0) {
            InputMap inputMap2 = (InputMap) DefaultLookup.get(this.list, this, "List.focusInputMap");
            if (this.isLeftToRight || (inputMap = (InputMap) DefaultLookup.get(this.list, this, "List.focusInputMap.RightToLeft")) == null) {
                return inputMap2;
            }
            inputMap.setParent(inputMap2);
            return inputMap;
        }
        return null;
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(this.list, null);
        SwingUtilities.replaceUIInputMap(this.list, 0, null);
    }

    protected void installListeners() {
        TransferHandler transferHandler = this.list.getTransferHandler();
        if (transferHandler == null || (transferHandler instanceof UIResource)) {
            this.list.setTransferHandler(defaultTransferHandler);
            if (this.list.getDropTarget() instanceof UIResource) {
                this.list.setDropTarget(null);
            }
        }
        this.focusListener = createFocusListener();
        this.mouseInputListener = createMouseInputListener();
        this.propertyChangeListener = createPropertyChangeListener();
        this.listSelectionListener = createListSelectionListener();
        this.listDataListener = createListDataListener();
        this.list.addFocusListener(this.focusListener);
        this.list.addMouseListener(this.mouseInputListener);
        this.list.addMouseMotionListener(this.mouseInputListener);
        this.list.addPropertyChangeListener(this.propertyChangeListener);
        this.list.addKeyListener(getHandler());
        ListModel model = this.list.getModel();
        if (model != null) {
            model.addListDataListener(this.listDataListener);
        }
        ListSelectionModel selectionModel = this.list.getSelectionModel();
        if (selectionModel != null) {
            selectionModel.addListSelectionListener(this.listSelectionListener);
        }
    }

    protected void uninstallListeners() {
        this.list.removeFocusListener(this.focusListener);
        this.list.removeMouseListener(this.mouseInputListener);
        this.list.removeMouseMotionListener(this.mouseInputListener);
        this.list.removePropertyChangeListener(this.propertyChangeListener);
        this.list.removeKeyListener(getHandler());
        ListModel model = this.list.getModel();
        if (model != null) {
            model.removeListDataListener(this.listDataListener);
        }
        ListSelectionModel selectionModel = this.list.getSelectionModel();
        if (selectionModel != null) {
            selectionModel.removeListSelectionListener(this.listSelectionListener);
        }
        this.focusListener = null;
        this.mouseInputListener = null;
        this.listSelectionListener = null;
        this.listDataListener = null;
        this.propertyChangeListener = null;
        this.handler = null;
    }

    protected void installDefaults() {
        this.list.setLayout(null);
        LookAndFeel.installBorder(this.list, "List.border");
        LookAndFeel.installColorsAndFont(this.list, "List.background", "List.foreground", "List.font");
        LookAndFeel.installProperty(this.list, "opaque", Boolean.TRUE);
        if (this.list.getCellRenderer() == null) {
            this.list.setCellRenderer((ListCellRenderer) UIManager.get("List.cellRenderer"));
        }
        Color selectionBackground = this.list.getSelectionBackground();
        if (selectionBackground == null || (selectionBackground instanceof UIResource)) {
            this.list.setSelectionBackground(UIManager.getColor("List.selectionBackground"));
        }
        Color selectionForeground = this.list.getSelectionForeground();
        if (selectionForeground == null || (selectionForeground instanceof UIResource)) {
            this.list.setSelectionForeground(UIManager.getColor("List.selectionForeground"));
        }
        Long l2 = (Long) UIManager.get("List.timeFactor");
        this.timeFactor = l2 != null ? l2.longValue() : 1000L;
        updateIsFileList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateIsFileList() {
        boolean zEquals = Boolean.TRUE.equals(this.list.getClientProperty("List.isFileList"));
        if (zEquals != this.isFileList) {
            this.isFileList = zEquals;
            Font font = this.list.getFont();
            if (font == null || (font instanceof UIResource)) {
                Font font2 = UIManager.getFont(zEquals ? "FileChooser.listFont" : "List.font");
                if (font2 != null && font2 != font) {
                    this.list.setFont(font2);
                }
            }
        }
    }

    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(this.list);
        if (this.list.getFont() instanceof UIResource) {
            this.list.setFont(null);
        }
        if (this.list.getForeground() instanceof UIResource) {
            this.list.setForeground(null);
        }
        if (this.list.getBackground() instanceof UIResource) {
            this.list.setBackground(null);
        }
        if (this.list.getSelectionBackground() instanceof UIResource) {
            this.list.setSelectionBackground(null);
        }
        if (this.list.getSelectionForeground() instanceof UIResource) {
            this.list.setSelectionForeground(null);
        }
        if (this.list.getCellRenderer() instanceof UIResource) {
            this.list.setCellRenderer(null);
        }
        if (this.list.getTransferHandler() instanceof UIResource) {
            this.list.setTransferHandler(null);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.list = (JList) jComponent;
        this.layoutOrientation = this.list.getLayoutOrientation();
        this.rendererPane = new CellRendererPane();
        this.list.add(this.rendererPane);
        this.columnCount = 1;
        this.updateLayoutStateNeeded = 1;
        this.isLeftToRight = this.list.getComponentOrientation().isLeftToRight();
        installDefaults();
        installListeners();
        installKeyboardActions();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallListeners();
        uninstallDefaults();
        uninstallKeyboardActions();
        this.cellHeight = -1;
        this.cellWidth = -1;
        this.cellHeights = null;
        this.listHeight = -1;
        this.listWidth = -1;
        this.list.remove(this.rendererPane);
        this.rendererPane = null;
        this.list = null;
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicListUI();
    }

    @Override // javax.swing.plaf.ListUI
    public int locationToIndex(JList jList, Point point) {
        maybeUpdateLayoutState();
        return convertLocationToModel(point.f12370x, point.f12371y);
    }

    @Override // javax.swing.plaf.ListUI
    public Point indexToLocation(JList jList, int i2) {
        maybeUpdateLayoutState();
        Rectangle cellBounds = getCellBounds(jList, i2, i2);
        if (cellBounds != null) {
            return new Point(cellBounds.f12372x, cellBounds.f12373y);
        }
        return null;
    }

    @Override // javax.swing.plaf.ListUI
    public Rectangle getCellBounds(JList jList, int i2, int i3) {
        Rectangle cellBounds;
        maybeUpdateLayoutState();
        int iMin = Math.min(i2, i3);
        int iMax = Math.max(i2, i3);
        if (iMin >= jList.getModel().getSize() || (cellBounds = getCellBounds(jList, iMin)) == null) {
            return null;
        }
        if (iMin == iMax) {
            return cellBounds;
        }
        Rectangle cellBounds2 = getCellBounds(jList, iMax);
        if (cellBounds2 != null) {
            if (this.layoutOrientation == 2) {
                if (convertModelToRow(iMin) != convertModelToRow(iMax)) {
                    cellBounds.f12372x = 0;
                    cellBounds.width = jList.getWidth();
                }
            } else if (cellBounds.f12372x != cellBounds2.f12372x) {
                cellBounds.f12373y = 0;
                cellBounds.height = jList.getHeight();
            }
            cellBounds.add(cellBounds2);
        }
        return cellBounds;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rectangle getCellBounds(JList jList, int i2) {
        int width;
        int rowHeight;
        maybeUpdateLayoutState();
        int iConvertModelToRow = convertModelToRow(i2);
        int iConvertModelToColumn = convertModelToColumn(i2);
        if (iConvertModelToRow == -1 || iConvertModelToColumn == -1) {
            return null;
        }
        Insets insets = jList.getInsets();
        int width2 = this.cellWidth;
        int i3 = insets.top;
        switch (this.layoutOrientation) {
            case 1:
            case 2:
                if (this.isLeftToRight) {
                    width = insets.left + (iConvertModelToColumn * this.cellWidth);
                } else {
                    width = (jList.getWidth() - insets.right) - ((iConvertModelToColumn + 1) * this.cellWidth);
                }
                i3 += this.cellHeight * iConvertModelToRow;
                rowHeight = this.cellHeight;
                break;
            default:
                width = insets.left;
                if (this.cellHeights == null) {
                    i3 += this.cellHeight * iConvertModelToRow;
                } else if (iConvertModelToRow >= this.cellHeights.length) {
                    i3 = 0;
                } else {
                    for (int i4 = 0; i4 < iConvertModelToRow; i4++) {
                        i3 += this.cellHeights[i4];
                    }
                }
                width2 = jList.getWidth() - (insets.left + insets.right);
                rowHeight = getRowHeight(i2);
                break;
        }
        return new Rectangle(width, i3, width2, rowHeight);
    }

    protected int getRowHeight(int i2) {
        return getHeight(0, i2);
    }

    protected int convertYToRow(int i2) {
        return convertLocationToRow(0, i2, false);
    }

    protected int convertRowToY(int i2) {
        if (i2 >= getRowCount(0) || i2 < 0) {
            return -1;
        }
        return getCellBounds(this.list, i2, i2).f12373y;
    }

    private int getHeight(int i2, int i3) {
        if (i2 < 0 || i2 > this.columnCount || i3 < 0) {
            return -1;
        }
        if (this.layoutOrientation != 0) {
            return this.cellHeight;
        }
        if (i3 >= this.list.getModel().getSize()) {
            return -1;
        }
        if (this.cellHeights == null) {
            return this.cellHeight;
        }
        if (i3 < this.cellHeights.length) {
            return this.cellHeights[i3];
        }
        return -1;
    }

    private int convertLocationToRow(int i2, int i3, boolean z2) {
        int size = this.list.getModel().getSize();
        if (size <= 0) {
            return -1;
        }
        Insets insets = this.list.getInsets();
        if (this.cellHeights == null) {
            int i4 = this.cellHeight == 0 ? 0 : (i3 - insets.top) / this.cellHeight;
            if (z2) {
                if (i4 < 0) {
                    i4 = 0;
                } else if (i4 >= size) {
                    i4 = size - 1;
                }
            }
            return i4;
        }
        if (size > this.cellHeights.length) {
            return -1;
        }
        int i5 = insets.top;
        int i6 = 0;
        if (z2 && i3 < i5) {
            return 0;
        }
        int i7 = 0;
        while (i7 < size) {
            if (i3 >= i5 && i3 < i5 + this.cellHeights[i7]) {
                return i6;
            }
            i5 += this.cellHeights[i7];
            i6++;
            i7++;
        }
        return i7 - 1;
    }

    private int convertLocationToRowInColumn(int i2, int i3) {
        int width = 0;
        if (this.layoutOrientation != 0) {
            if (this.isLeftToRight) {
                width = i3 * this.cellWidth;
            } else {
                width = (this.list.getWidth() - ((i3 + 1) * this.cellWidth)) - this.list.getInsets().right;
            }
        }
        return convertLocationToRow(width, i2, true);
    }

    private int convertLocationToModel(int i2, int i3) {
        int iConvertLocationToRow = convertLocationToRow(i2, i3, true);
        int iConvertLocationToColumn = convertLocationToColumn(i2, i3);
        if (iConvertLocationToRow >= 0 && iConvertLocationToColumn >= 0) {
            return getModelIndex(iConvertLocationToColumn, iConvertLocationToRow);
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRowCount(int i2) {
        if (i2 < 0 || i2 >= this.columnCount) {
            return -1;
        }
        if (this.layoutOrientation == 0 || (i2 == 0 && this.columnCount == 1)) {
            return this.list.getModel().getSize();
        }
        if (i2 >= this.columnCount) {
            return -1;
        }
        if (this.layoutOrientation == 1) {
            if (i2 < this.columnCount - 1) {
                return this.rowsPerColumn;
            }
            return this.list.getModel().getSize() - ((this.columnCount - 1) * this.rowsPerColumn);
        }
        if (i2 >= this.columnCount - ((this.columnCount * this.rowsPerColumn) - this.list.getModel().getSize())) {
            return Math.max(0, this.rowsPerColumn - 1);
        }
        return this.rowsPerColumn;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getModelIndex(int i2, int i3) {
        switch (this.layoutOrientation) {
            case 1:
                return Math.min(this.list.getModel().getSize() - 1, (this.rowsPerColumn * i2) + Math.min(i3, this.rowsPerColumn - 1));
            case 2:
                return Math.min(this.list.getModel().getSize() - 1, (i3 * this.columnCount) + i2);
            default:
                return i3;
        }
    }

    private int convertLocationToColumn(int i2, int i3) {
        int width;
        if (this.cellWidth <= 0 || this.layoutOrientation == 0) {
            return 0;
        }
        Insets insets = this.list.getInsets();
        if (this.isLeftToRight) {
            width = (i2 - insets.left) / this.cellWidth;
        } else {
            width = (((this.list.getWidth() - i2) - insets.right) - 1) / this.cellWidth;
        }
        if (width < 0) {
            return 0;
        }
        if (width >= this.columnCount) {
            return this.columnCount - 1;
        }
        return width;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int convertModelToRow(int i2) {
        int size = this.list.getModel().getSize();
        if (i2 < 0 || i2 >= size) {
            return -1;
        }
        if (this.layoutOrientation != 0 && this.columnCount > 1 && this.rowsPerColumn > 0) {
            if (this.layoutOrientation == 1) {
                return i2 % this.rowsPerColumn;
            }
            return i2 / this.columnCount;
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int convertModelToColumn(int i2) {
        int size = this.list.getModel().getSize();
        if (i2 < 0 || i2 >= size) {
            return -1;
        }
        if (this.layoutOrientation != 0 && this.rowsPerColumn > 0 && this.columnCount > 1) {
            if (this.layoutOrientation == 1) {
                return i2 / this.rowsPerColumn;
            }
            return i2 % this.columnCount;
        }
        return 0;
    }

    protected void maybeUpdateLayoutState() {
        if (this.updateLayoutStateNeeded != 0) {
            updateLayoutState();
            this.updateLayoutStateNeeded = 0;
        }
    }

    protected void updateLayoutState() {
        int fixedCellHeight = this.list.getFixedCellHeight();
        int fixedCellWidth = this.list.getFixedCellWidth();
        this.cellWidth = fixedCellWidth != -1 ? fixedCellWidth : -1;
        if (fixedCellHeight != -1) {
            this.cellHeight = fixedCellHeight;
            this.cellHeights = null;
        } else {
            this.cellHeight = -1;
            this.cellHeights = new int[this.list.getModel().getSize()];
        }
        if (fixedCellWidth == -1 || fixedCellHeight == -1) {
            ListModel model = this.list.getModel();
            int size = model.getSize();
            ListCellRenderer cellRenderer = this.list.getCellRenderer();
            if (cellRenderer != null) {
                for (int i2 = 0; i2 < size; i2++) {
                    Component listCellRendererComponent = cellRenderer.getListCellRendererComponent(this.list, model.getElementAt(i2), i2, false, false);
                    this.rendererPane.add(listCellRendererComponent);
                    Dimension preferredSize = listCellRendererComponent.getPreferredSize();
                    if (fixedCellWidth == -1) {
                        this.cellWidth = Math.max(preferredSize.width, this.cellWidth);
                    }
                    if (fixedCellHeight == -1) {
                        this.cellHeights[i2] = preferredSize.height;
                    }
                }
            } else {
                if (this.cellWidth == -1) {
                    this.cellWidth = 0;
                }
                if (this.cellHeights == null) {
                    this.cellHeights = new int[size];
                }
                for (int i3 = 0; i3 < size; i3++) {
                    this.cellHeights[i3] = 0;
                }
            }
        }
        this.columnCount = 1;
        if (this.layoutOrientation != 0) {
            updateHorizontalLayoutState(fixedCellWidth, fixedCellHeight);
        }
    }

    private void updateHorizontalLayoutState(int i2, int i3) {
        int i4;
        int visibleRowCount = this.list.getVisibleRowCount();
        int size = this.list.getModel().getSize();
        Insets insets = this.list.getInsets();
        this.listHeight = this.list.getHeight();
        this.listWidth = this.list.getWidth();
        if (size == 0) {
            this.columnCount = 0;
            this.rowsPerColumn = 0;
            this.preferredHeight = insets.top + insets.bottom;
            return;
        }
        if (i3 != -1) {
            i4 = i3;
        } else {
            int iMax = 0;
            if (this.cellHeights.length > 0) {
                iMax = this.cellHeights[this.cellHeights.length - 1];
                for (int length = this.cellHeights.length - 2; length >= 0; length--) {
                    iMax = Math.max(iMax, this.cellHeights[length]);
                }
            }
            int i5 = iMax;
            this.cellHeight = i5;
            i4 = i5;
            this.cellHeights = null;
        }
        this.rowsPerColumn = size;
        if (visibleRowCount > 0) {
            this.rowsPerColumn = visibleRowCount;
            this.columnCount = Math.max(1, size / this.rowsPerColumn);
            if (size > 0 && size > this.rowsPerColumn && size % this.rowsPerColumn != 0) {
                this.columnCount++;
            }
            if (this.layoutOrientation == 2) {
                this.rowsPerColumn = size / this.columnCount;
                if (size % this.columnCount > 0) {
                    this.rowsPerColumn++;
                }
            }
        } else if (this.layoutOrientation == 1 && i4 != 0) {
            this.rowsPerColumn = Math.max(1, ((this.listHeight - insets.top) - insets.bottom) / i4);
            this.columnCount = Math.max(1, size / this.rowsPerColumn);
            if (size > 0 && size > this.rowsPerColumn && size % this.rowsPerColumn != 0) {
                this.columnCount++;
            }
        } else if (this.layoutOrientation == 2 && this.cellWidth > 0 && this.listWidth > 0) {
            this.columnCount = Math.max(1, ((this.listWidth - insets.left) - insets.right) / this.cellWidth);
            this.rowsPerColumn = size / this.columnCount;
            if (size % this.columnCount > 0) {
                this.rowsPerColumn++;
            }
        }
        this.preferredHeight = (this.rowsPerColumn * this.cellHeight) + insets.top + insets.bottom;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicListUI$MouseInputHandler.class */
    public class MouseInputHandler implements MouseInputListener {
        public MouseInputHandler() {
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            BasicListUI.this.getHandler().mouseClicked(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            BasicListUI.this.getHandler().mouseEntered(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            BasicListUI.this.getHandler().mouseExited(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            BasicListUI.this.getHandler().mousePressed(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            BasicListUI.this.getHandler().mouseDragged(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            BasicListUI.this.getHandler().mouseMoved(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            BasicListUI.this.getHandler().mouseReleased(mouseEvent);
        }
    }

    protected MouseInputListener createMouseInputListener() {
        return getHandler();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicListUI$FocusHandler.class */
    public class FocusHandler implements FocusListener {
        public FocusHandler() {
        }

        protected void repaintCellFocus() {
            BasicListUI.this.getHandler().repaintCellFocus();
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            BasicListUI.this.getHandler().focusGained(focusEvent);
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            BasicListUI.this.getHandler().focusLost(focusEvent);
        }
    }

    protected FocusListener createFocusListener() {
        return getHandler();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicListUI$ListSelectionHandler.class */
    public class ListSelectionHandler implements ListSelectionListener {
        public ListSelectionHandler() {
        }

        @Override // javax.swing.event.ListSelectionListener
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            BasicListUI.this.getHandler().valueChanged(listSelectionEvent);
        }
    }

    protected ListSelectionListener createListSelectionListener() {
        return getHandler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void redrawList() {
        this.list.revalidate();
        this.list.repaint();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicListUI$ListDataHandler.class */
    public class ListDataHandler implements ListDataListener {
        public ListDataHandler() {
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalAdded(ListDataEvent listDataEvent) {
            BasicListUI.this.getHandler().intervalAdded(listDataEvent);
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalRemoved(ListDataEvent listDataEvent) {
            BasicListUI.this.getHandler().intervalRemoved(listDataEvent);
        }

        @Override // javax.swing.event.ListDataListener
        public void contentsChanged(ListDataEvent listDataEvent) {
            BasicListUI.this.getHandler().contentsChanged(listDataEvent);
        }
    }

    protected ListDataListener createListDataListener() {
        return getHandler();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicListUI$PropertyChangeHandler.class */
    public class PropertyChangeHandler implements PropertyChangeListener {
        public PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicListUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicListUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String SELECT_PREVIOUS_COLUMN = "selectPreviousColumn";
        private static final String SELECT_PREVIOUS_COLUMN_EXTEND = "selectPreviousColumnExtendSelection";
        private static final String SELECT_PREVIOUS_COLUMN_CHANGE_LEAD = "selectPreviousColumnChangeLead";
        private static final String SELECT_NEXT_COLUMN = "selectNextColumn";
        private static final String SELECT_NEXT_COLUMN_EXTEND = "selectNextColumnExtendSelection";
        private static final String SELECT_NEXT_COLUMN_CHANGE_LEAD = "selectNextColumnChangeLead";
        private static final String SELECT_PREVIOUS_ROW = "selectPreviousRow";
        private static final String SELECT_PREVIOUS_ROW_EXTEND = "selectPreviousRowExtendSelection";
        private static final String SELECT_PREVIOUS_ROW_CHANGE_LEAD = "selectPreviousRowChangeLead";
        private static final String SELECT_NEXT_ROW = "selectNextRow";
        private static final String SELECT_NEXT_ROW_EXTEND = "selectNextRowExtendSelection";
        private static final String SELECT_NEXT_ROW_CHANGE_LEAD = "selectNextRowChangeLead";
        private static final String SELECT_FIRST_ROW = "selectFirstRow";
        private static final String SELECT_FIRST_ROW_EXTEND = "selectFirstRowExtendSelection";
        private static final String SELECT_FIRST_ROW_CHANGE_LEAD = "selectFirstRowChangeLead";
        private static final String SELECT_LAST_ROW = "selectLastRow";
        private static final String SELECT_LAST_ROW_EXTEND = "selectLastRowExtendSelection";
        private static final String SELECT_LAST_ROW_CHANGE_LEAD = "selectLastRowChangeLead";
        private static final String SCROLL_UP = "scrollUp";
        private static final String SCROLL_UP_EXTEND = "scrollUpExtendSelection";
        private static final String SCROLL_UP_CHANGE_LEAD = "scrollUpChangeLead";
        private static final String SCROLL_DOWN = "scrollDown";
        private static final String SCROLL_DOWN_EXTEND = "scrollDownExtendSelection";
        private static final String SCROLL_DOWN_CHANGE_LEAD = "scrollDownChangeLead";
        private static final String SELECT_ALL = "selectAll";
        private static final String CLEAR_SELECTION = "clearSelection";
        private static final String ADD_TO_SELECTION = "addToSelection";
        private static final String TOGGLE_AND_ANCHOR = "toggleAndAnchor";
        private static final String EXTEND_TO = "extendTo";
        private static final String MOVE_SELECTION_TO = "moveSelectionTo";

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            String name = getName();
            JList jList = (JList) actionEvent.getSource();
            BasicListUI basicListUI = (BasicListUI) BasicLookAndFeel.getUIOfType(jList.getUI(), BasicListUI.class);
            if (name == SELECT_PREVIOUS_COLUMN) {
                changeSelection(jList, 1, getNextColumnIndex(jList, basicListUI, -1), -1);
                return;
            }
            if (name == SELECT_PREVIOUS_COLUMN_EXTEND) {
                changeSelection(jList, 2, getNextColumnIndex(jList, basicListUI, -1), -1);
                return;
            }
            if (name == SELECT_PREVIOUS_COLUMN_CHANGE_LEAD) {
                changeSelection(jList, 0, getNextColumnIndex(jList, basicListUI, -1), -1);
                return;
            }
            if (name == SELECT_NEXT_COLUMN) {
                changeSelection(jList, 1, getNextColumnIndex(jList, basicListUI, 1), 1);
                return;
            }
            if (name == SELECT_NEXT_COLUMN_EXTEND) {
                changeSelection(jList, 2, getNextColumnIndex(jList, basicListUI, 1), 1);
                return;
            }
            if (name == SELECT_NEXT_COLUMN_CHANGE_LEAD) {
                changeSelection(jList, 0, getNextColumnIndex(jList, basicListUI, 1), 1);
                return;
            }
            if (name == SELECT_PREVIOUS_ROW) {
                changeSelection(jList, 1, getNextIndex(jList, basicListUI, -1), -1);
                return;
            }
            if (name == SELECT_PREVIOUS_ROW_EXTEND) {
                changeSelection(jList, 2, getNextIndex(jList, basicListUI, -1), -1);
                return;
            }
            if (name == SELECT_PREVIOUS_ROW_CHANGE_LEAD) {
                changeSelection(jList, 0, getNextIndex(jList, basicListUI, -1), -1);
                return;
            }
            if (name == SELECT_NEXT_ROW) {
                changeSelection(jList, 1, getNextIndex(jList, basicListUI, 1), 1);
                return;
            }
            if (name == SELECT_NEXT_ROW_EXTEND) {
                changeSelection(jList, 2, getNextIndex(jList, basicListUI, 1), 1);
                return;
            }
            if (name == SELECT_NEXT_ROW_CHANGE_LEAD) {
                changeSelection(jList, 0, getNextIndex(jList, basicListUI, 1), 1);
                return;
            }
            if (name == SELECT_FIRST_ROW) {
                changeSelection(jList, 1, 0, -1);
                return;
            }
            if (name == SELECT_FIRST_ROW_EXTEND) {
                changeSelection(jList, 2, 0, -1);
                return;
            }
            if (name == SELECT_FIRST_ROW_CHANGE_LEAD) {
                changeSelection(jList, 0, 0, -1);
                return;
            }
            if (name == SELECT_LAST_ROW) {
                changeSelection(jList, 1, jList.getModel().getSize() - 1, 1);
                return;
            }
            if (name == SELECT_LAST_ROW_EXTEND) {
                changeSelection(jList, 2, jList.getModel().getSize() - 1, 1);
                return;
            }
            if (name == SELECT_LAST_ROW_CHANGE_LEAD) {
                changeSelection(jList, 0, jList.getModel().getSize() - 1, 1);
                return;
            }
            if (name == SCROLL_UP) {
                changeSelection(jList, 1, getNextPageIndex(jList, -1), -1);
                return;
            }
            if (name == SCROLL_UP_EXTEND) {
                changeSelection(jList, 2, getNextPageIndex(jList, -1), -1);
                return;
            }
            if (name == SCROLL_UP_CHANGE_LEAD) {
                changeSelection(jList, 0, getNextPageIndex(jList, -1), -1);
                return;
            }
            if (name == SCROLL_DOWN) {
                changeSelection(jList, 1, getNextPageIndex(jList, 1), 1);
                return;
            }
            if (name == SCROLL_DOWN_EXTEND) {
                changeSelection(jList, 2, getNextPageIndex(jList, 1), 1);
                return;
            }
            if (name == SCROLL_DOWN_CHANGE_LEAD) {
                changeSelection(jList, 0, getNextPageIndex(jList, 1), 1);
                return;
            }
            if (name == SELECT_ALL) {
                selectAll(jList);
                return;
            }
            if (name == CLEAR_SELECTION) {
                clearSelection(jList);
                return;
            }
            if (name == ADD_TO_SELECTION) {
                int iAdjustIndex = BasicListUI.adjustIndex(jList.getSelectionModel().getLeadSelectionIndex(), jList);
                if (!jList.isSelectedIndex(iAdjustIndex)) {
                    int anchorSelectionIndex = jList.getSelectionModel().getAnchorSelectionIndex();
                    jList.setValueIsAdjusting(true);
                    jList.addSelectionInterval(iAdjustIndex, iAdjustIndex);
                    jList.getSelectionModel().setAnchorSelectionIndex(anchorSelectionIndex);
                    jList.setValueIsAdjusting(false);
                    return;
                }
                return;
            }
            if (name == TOGGLE_AND_ANCHOR) {
                int iAdjustIndex2 = BasicListUI.adjustIndex(jList.getSelectionModel().getLeadSelectionIndex(), jList);
                if (jList.isSelectedIndex(iAdjustIndex2)) {
                    jList.removeSelectionInterval(iAdjustIndex2, iAdjustIndex2);
                    return;
                } else {
                    jList.addSelectionInterval(iAdjustIndex2, iAdjustIndex2);
                    return;
                }
            }
            if (name == EXTEND_TO) {
                changeSelection(jList, 2, BasicListUI.adjustIndex(jList.getSelectionModel().getLeadSelectionIndex(), jList), 0);
            } else if (name == MOVE_SELECTION_TO) {
                changeSelection(jList, 1, BasicListUI.adjustIndex(jList.getSelectionModel().getLeadSelectionIndex(), jList), 0);
            }
        }

        @Override // sun.swing.UIAction
        public boolean isEnabled(Object obj) {
            String name = getName();
            if (name == SELECT_PREVIOUS_COLUMN_CHANGE_LEAD || name == SELECT_NEXT_COLUMN_CHANGE_LEAD || name == SELECT_PREVIOUS_ROW_CHANGE_LEAD || name == SELECT_NEXT_ROW_CHANGE_LEAD || name == SELECT_FIRST_ROW_CHANGE_LEAD || name == SELECT_LAST_ROW_CHANGE_LEAD || name == SCROLL_UP_CHANGE_LEAD || name == SCROLL_DOWN_CHANGE_LEAD) {
                return obj != null && (((JList) obj).getSelectionModel() instanceof DefaultListSelectionModel);
            }
            return true;
        }

        private void clearSelection(JList jList) {
            jList.clearSelection();
        }

        private void selectAll(JList jList) {
            int size = jList.getModel().getSize();
            if (size > 0) {
                ListSelectionModel selectionModel = jList.getSelectionModel();
                int iAdjustIndex = BasicListUI.adjustIndex(selectionModel.getLeadSelectionIndex(), jList);
                if (selectionModel.getSelectionMode() == 0) {
                    if (iAdjustIndex == -1) {
                        int iAdjustIndex2 = BasicListUI.adjustIndex(jList.getMinSelectionIndex(), jList);
                        iAdjustIndex = iAdjustIndex2 == -1 ? 0 : iAdjustIndex2;
                    }
                    jList.setSelectionInterval(iAdjustIndex, iAdjustIndex);
                    jList.ensureIndexIsVisible(iAdjustIndex);
                    return;
                }
                jList.setValueIsAdjusting(true);
                int iAdjustIndex3 = BasicListUI.adjustIndex(selectionModel.getAnchorSelectionIndex(), jList);
                jList.setSelectionInterval(0, size - 1);
                SwingUtilities2.setLeadAnchorWithoutSelection(selectionModel, iAdjustIndex3, iAdjustIndex);
                jList.setValueIsAdjusting(false);
            }
        }

        private int getNextPageIndex(JList jList, int i2) {
            int iLocationToIndex;
            if (jList.getModel().getSize() == 0) {
                return -1;
            }
            Rectangle visibleRect = jList.getVisibleRect();
            int iAdjustIndex = BasicListUI.adjustIndex(jList.getSelectionModel().getLeadSelectionIndex(), jList);
            Rectangle rectangle = iAdjustIndex == -1 ? new Rectangle() : jList.getCellBounds(iAdjustIndex, iAdjustIndex);
            if (jList.getLayoutOrientation() == 1 && jList.getVisibleRowCount() <= 0) {
                if (!jList.getComponentOrientation().isLeftToRight()) {
                    i2 = -i2;
                }
                if (i2 < 0) {
                    visibleRect.f12372x = (rectangle.f12372x + rectangle.width) - visibleRect.width;
                    Point point = new Point(visibleRect.f12372x - 1, rectangle.f12373y);
                    iLocationToIndex = jList.locationToIndex(point);
                    Rectangle cellBounds = jList.getCellBounds(iLocationToIndex, iLocationToIndex);
                    if (visibleRect.intersects(cellBounds)) {
                        point.f12370x = cellBounds.f12372x - 1;
                        iLocationToIndex = jList.locationToIndex(point);
                        cellBounds = jList.getCellBounds(iLocationToIndex, iLocationToIndex);
                    }
                    if (cellBounds.f12373y != rectangle.f12373y) {
                        point.f12370x = cellBounds.f12372x + cellBounds.width;
                        iLocationToIndex = jList.locationToIndex(point);
                    }
                } else {
                    visibleRect.f12372x = rectangle.f12372x;
                    Point point2 = new Point(visibleRect.f12372x + visibleRect.width, rectangle.f12373y);
                    iLocationToIndex = jList.locationToIndex(point2);
                    Rectangle cellBounds2 = jList.getCellBounds(iLocationToIndex, iLocationToIndex);
                    if (visibleRect.intersects(cellBounds2)) {
                        point2.f12370x = cellBounds2.f12372x + cellBounds2.width;
                        iLocationToIndex = jList.locationToIndex(point2);
                        cellBounds2 = jList.getCellBounds(iLocationToIndex, iLocationToIndex);
                    }
                    if (cellBounds2.f12373y != rectangle.f12373y) {
                        point2.f12370x = cellBounds2.f12372x - 1;
                        iLocationToIndex = jList.locationToIndex(point2);
                    }
                }
            } else if (i2 < 0) {
                Point point3 = new Point(rectangle.f12372x, visibleRect.f12373y);
                iLocationToIndex = jList.locationToIndex(point3);
                if (iAdjustIndex <= iLocationToIndex) {
                    visibleRect.f12373y = (rectangle.f12373y + rectangle.height) - visibleRect.height;
                    point3.f12371y = visibleRect.f12373y;
                    iLocationToIndex = jList.locationToIndex(point3);
                    Rectangle cellBounds3 = jList.getCellBounds(iLocationToIndex, iLocationToIndex);
                    if (cellBounds3.f12373y < visibleRect.f12373y) {
                        point3.f12371y = cellBounds3.f12373y + cellBounds3.height;
                        iLocationToIndex = jList.locationToIndex(point3);
                        cellBounds3 = jList.getCellBounds(iLocationToIndex, iLocationToIndex);
                    }
                    if (cellBounds3.f12373y >= rectangle.f12373y) {
                        point3.f12371y = rectangle.f12373y - 1;
                        iLocationToIndex = jList.locationToIndex(point3);
                    }
                }
            } else {
                Point point4 = new Point(rectangle.f12372x, (visibleRect.f12373y + visibleRect.height) - 1);
                iLocationToIndex = jList.locationToIndex(point4);
                Rectangle cellBounds4 = jList.getCellBounds(iLocationToIndex, iLocationToIndex);
                if (cellBounds4.f12373y + cellBounds4.height > visibleRect.f12373y + visibleRect.height) {
                    point4.f12371y = cellBounds4.f12373y - 1;
                    int iLocationToIndex2 = jList.locationToIndex(point4);
                    jList.getCellBounds(iLocationToIndex2, iLocationToIndex2);
                    iLocationToIndex = Math.max(iLocationToIndex2, iAdjustIndex);
                }
                if (iAdjustIndex >= iLocationToIndex) {
                    visibleRect.f12373y = rectangle.f12373y;
                    point4.f12371y = (visibleRect.f12373y + visibleRect.height) - 1;
                    iLocationToIndex = jList.locationToIndex(point4);
                    Rectangle cellBounds5 = jList.getCellBounds(iLocationToIndex, iLocationToIndex);
                    if (cellBounds5.f12373y + cellBounds5.height > visibleRect.f12373y + visibleRect.height) {
                        point4.f12371y = cellBounds5.f12373y - 1;
                        iLocationToIndex = jList.locationToIndex(point4);
                        cellBounds5 = jList.getCellBounds(iLocationToIndex, iLocationToIndex);
                    }
                    if (cellBounds5.f12373y <= rectangle.f12373y) {
                        point4.f12371y = rectangle.f12373y + rectangle.height;
                        iLocationToIndex = jList.locationToIndex(point4);
                    }
                }
            }
            return iLocationToIndex;
        }

        private void changeSelection(JList jList, int i2, int i3, int i4) {
            if (i3 >= 0 && i3 < jList.getModel().getSize()) {
                ListSelectionModel selectionModel = jList.getSelectionModel();
                if (i2 == 0 && jList.getSelectionMode() != 2) {
                    i2 = 1;
                }
                adjustScrollPositionIfNecessary(jList, i3, i4);
                if (i2 == 2) {
                    int iAdjustIndex = BasicListUI.adjustIndex(selectionModel.getAnchorSelectionIndex(), jList);
                    if (iAdjustIndex == -1) {
                        iAdjustIndex = 0;
                    }
                    jList.setSelectionInterval(iAdjustIndex, i3);
                    return;
                }
                if (i2 == 1) {
                    jList.setSelectedIndex(i3);
                } else {
                    ((DefaultListSelectionModel) selectionModel).moveLeadSelectionIndex(i3);
                }
            }
        }

        private void adjustScrollPositionIfNecessary(JList jList, int i2, int i3) {
            if (i3 == 0) {
                return;
            }
            Rectangle cellBounds = jList.getCellBounds(i2, i2);
            Rectangle visibleRect = jList.getVisibleRect();
            if (cellBounds != null && !visibleRect.contains(cellBounds)) {
                if (jList.getLayoutOrientation() == 1 && jList.getVisibleRowCount() <= 0) {
                    if (jList.getComponentOrientation().isLeftToRight()) {
                        if (i3 > 0) {
                            int iMax = Math.max(0, (cellBounds.f12372x + cellBounds.width) - visibleRect.width);
                            int iLocationToIndex = jList.locationToIndex(new Point(iMax, cellBounds.f12373y));
                            Rectangle cellBounds2 = jList.getCellBounds(iLocationToIndex, iLocationToIndex);
                            if (cellBounds2.f12372x < iMax && cellBounds2.f12372x < cellBounds.f12372x) {
                                cellBounds2.f12372x += cellBounds2.width;
                                int iLocationToIndex2 = jList.locationToIndex(cellBounds2.getLocation());
                                cellBounds2 = jList.getCellBounds(iLocationToIndex2, iLocationToIndex2);
                            }
                            cellBounds = cellBounds2;
                        }
                        cellBounds.width = visibleRect.width;
                    } else if (i3 > 0) {
                        int i4 = cellBounds.f12372x + visibleRect.width;
                        int iLocationToIndex3 = jList.locationToIndex(new Point(i4, cellBounds.f12373y));
                        Rectangle cellBounds3 = jList.getCellBounds(iLocationToIndex3, iLocationToIndex3);
                        if (cellBounds3.f12372x + cellBounds3.width > i4 && cellBounds3.f12372x > cellBounds.f12372x) {
                            cellBounds3.width = 0;
                        }
                        cellBounds.f12372x = Math.max(0, (cellBounds3.f12372x + cellBounds3.width) - visibleRect.width);
                        cellBounds.width = visibleRect.width;
                    } else {
                        cellBounds.f12372x += Math.max(0, cellBounds.width - visibleRect.width);
                        cellBounds.width = Math.min(cellBounds.width, visibleRect.width);
                    }
                } else if (i3 > 0 && (cellBounds.f12373y < visibleRect.f12373y || cellBounds.f12373y + cellBounds.height > visibleRect.f12373y + visibleRect.height)) {
                    int iMax2 = Math.max(0, (cellBounds.f12373y + cellBounds.height) - visibleRect.height);
                    int iLocationToIndex4 = jList.locationToIndex(new Point(cellBounds.f12372x, iMax2));
                    Rectangle cellBounds4 = jList.getCellBounds(iLocationToIndex4, iLocationToIndex4);
                    if (cellBounds4.f12373y < iMax2 && cellBounds4.f12373y < cellBounds.f12373y) {
                        cellBounds4.f12373y += cellBounds4.height;
                        int iLocationToIndex5 = jList.locationToIndex(cellBounds4.getLocation());
                        cellBounds4 = jList.getCellBounds(iLocationToIndex5, iLocationToIndex5);
                    }
                    cellBounds = cellBounds4;
                    cellBounds.height = visibleRect.height;
                } else {
                    cellBounds.height = Math.min(cellBounds.height, visibleRect.height);
                }
                jList.scrollRectToVisible(cellBounds);
            }
        }

        private int getNextColumnIndex(JList jList, BasicListUI basicListUI, int i2) {
            if (jList.getLayoutOrientation() != 0) {
                int iAdjustIndex = BasicListUI.adjustIndex(jList.getLeadSelectionIndex(), jList);
                int size = jList.getModel().getSize();
                if (iAdjustIndex == -1 || size == 1) {
                    return 0;
                }
                if (basicListUI != null && basicListUI.columnCount > 1) {
                    int iConvertModelToColumn = basicListUI.convertModelToColumn(iAdjustIndex);
                    int iConvertModelToRow = basicListUI.convertModelToRow(iAdjustIndex);
                    int i3 = iConvertModelToColumn + i2;
                    if (i3 < basicListUI.columnCount && i3 >= 0 && iConvertModelToRow < basicListUI.getRowCount(i3)) {
                        return basicListUI.getModelIndex(i3, iConvertModelToRow);
                    }
                    return -1;
                }
                return -1;
            }
            return -1;
        }

        private int getNextIndex(JList jList, BasicListUI basicListUI, int i2) {
            int iAdjustIndex = BasicListUI.adjustIndex(jList.getLeadSelectionIndex(), jList);
            int size = jList.getModel().getSize();
            if (iAdjustIndex == -1) {
                if (size > 0) {
                    iAdjustIndex = i2 > 0 ? 0 : size - 1;
                }
            } else if (size == 1) {
                iAdjustIndex = 0;
            } else if (jList.getLayoutOrientation() == 2) {
                if (basicListUI != null) {
                    iAdjustIndex += basicListUI.columnCount * i2;
                }
            } else {
                iAdjustIndex += i2;
            }
            return iAdjustIndex;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicListUI$Handler.class */
    private class Handler implements FocusListener, KeyListener, ListDataListener, ListSelectionListener, MouseInputListener, PropertyChangeListener, DragRecognitionSupport.BeforeDrag {
        private String prefix;
        private String typedString;
        private long lastTime;
        private boolean dragPressDidSelection;

        private Handler() {
            this.prefix = "";
            this.typedString = "";
            this.lastTime = 0L;
        }

        @Override // java.awt.event.KeyListener
        public void keyTyped(KeyEvent keyEvent) {
            int nextMatch;
            JList jList = (JList) keyEvent.getSource();
            ListModel model = jList.getModel();
            if (model.getSize() == 0 || keyEvent.isAltDown() || BasicGraphicsUtils.isMenuShortcutKeyDown(keyEvent) || isNavigationKey(keyEvent)) {
                return;
            }
            boolean z2 = true;
            char keyChar = keyEvent.getKeyChar();
            long when = keyEvent.getWhen();
            int iAdjustIndex = BasicListUI.adjustIndex(jList.getLeadSelectionIndex(), BasicListUI.this.list);
            if (when - this.lastTime < BasicListUI.this.timeFactor) {
                this.typedString += keyChar;
                if (this.prefix.length() == 1 && keyChar == this.prefix.charAt(0)) {
                    iAdjustIndex++;
                } else {
                    this.prefix = this.typedString;
                }
            } else {
                iAdjustIndex++;
                this.typedString = "" + keyChar;
                this.prefix = this.typedString;
            }
            this.lastTime = when;
            if (iAdjustIndex < 0 || iAdjustIndex >= model.getSize()) {
                z2 = false;
                iAdjustIndex = 0;
            }
            int nextMatch2 = jList.getNextMatch(this.prefix, iAdjustIndex, Position.Bias.Forward);
            if (nextMatch2 >= 0) {
                jList.setSelectedIndex(nextMatch2);
                jList.ensureIndexIsVisible(nextMatch2);
            } else if (z2 && (nextMatch = jList.getNextMatch(this.prefix, 0, Position.Bias.Forward)) >= 0) {
                jList.setSelectedIndex(nextMatch);
                jList.ensureIndexIsVisible(nextMatch);
            }
        }

        @Override // java.awt.event.KeyListener
        public void keyPressed(KeyEvent keyEvent) {
            if (isNavigationKey(keyEvent)) {
                this.prefix = "";
                this.typedString = "";
                this.lastTime = 0L;
            }
        }

        @Override // java.awt.event.KeyListener
        public void keyReleased(KeyEvent keyEvent) {
        }

        private boolean isNavigationKey(KeyEvent keyEvent) {
            InputMap inputMap = BasicListUI.this.list.getInputMap(1);
            KeyStroke keyStrokeForEvent = KeyStroke.getKeyStrokeForEvent(keyEvent);
            if (inputMap != null && inputMap.get(keyStrokeForEvent) != null) {
                return true;
            }
            return false;
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName == "model") {
                ListModel listModel = (ListModel) propertyChangeEvent.getOldValue();
                ListModel listModel2 = (ListModel) propertyChangeEvent.getNewValue();
                if (listModel != null) {
                    listModel.removeListDataListener(BasicListUI.this.listDataListener);
                }
                if (listModel2 != null) {
                    listModel2.addListDataListener(BasicListUI.this.listDataListener);
                }
                BasicListUI.this.updateLayoutStateNeeded |= 1;
                BasicListUI.this.redrawList();
                return;
            }
            if (propertyName == "selectionModel") {
                ListSelectionModel listSelectionModel = (ListSelectionModel) propertyChangeEvent.getOldValue();
                ListSelectionModel listSelectionModel2 = (ListSelectionModel) propertyChangeEvent.getNewValue();
                if (listSelectionModel != null) {
                    listSelectionModel.removeListSelectionListener(BasicListUI.this.listSelectionListener);
                }
                if (listSelectionModel2 != null) {
                    listSelectionModel2.addListSelectionListener(BasicListUI.this.listSelectionListener);
                }
                BasicListUI.this.updateLayoutStateNeeded |= 1;
                BasicListUI.this.redrawList();
                return;
            }
            if (propertyName == "cellRenderer") {
                BasicListUI.this.updateLayoutStateNeeded |= 64;
                BasicListUI.this.redrawList();
                return;
            }
            if (propertyName == "font") {
                BasicListUI.this.updateLayoutStateNeeded |= 4;
                BasicListUI.this.redrawList();
                return;
            }
            if (propertyName == "prototypeCellValue") {
                BasicListUI.this.updateLayoutStateNeeded |= 32;
                BasicListUI.this.redrawList();
                return;
            }
            if (propertyName == "fixedCellHeight") {
                BasicListUI.this.updateLayoutStateNeeded |= 16;
                BasicListUI.this.redrawList();
                return;
            }
            if (propertyName == "fixedCellWidth") {
                BasicListUI.this.updateLayoutStateNeeded |= 8;
                BasicListUI.this.redrawList();
                return;
            }
            if (propertyName == "selectionForeground") {
                BasicListUI.this.list.repaint();
                return;
            }
            if (propertyName == "selectionBackground") {
                BasicListUI.this.list.repaint();
                return;
            }
            if ("layoutOrientation" == propertyName) {
                BasicListUI.this.updateLayoutStateNeeded |= 128;
                BasicListUI.this.layoutOrientation = BasicListUI.this.list.getLayoutOrientation();
                BasicListUI.this.redrawList();
                return;
            }
            if (JTree.VISIBLE_ROW_COUNT_PROPERTY == propertyName) {
                if (BasicListUI.this.layoutOrientation != 0) {
                    BasicListUI.this.updateLayoutStateNeeded |= 128;
                    BasicListUI.this.redrawList();
                    return;
                }
                return;
            }
            if ("componentOrientation" == propertyName) {
                BasicListUI.this.isLeftToRight = BasicListUI.this.list.getComponentOrientation().isLeftToRight();
                BasicListUI.this.updateLayoutStateNeeded |= 1024;
                BasicListUI.this.redrawList();
                SwingUtilities.replaceUIInputMap(BasicListUI.this.list, 0, BasicListUI.this.getInputMap(0));
                return;
            }
            if ("List.isFileList" == propertyName) {
                BasicListUI.this.updateIsFileList();
                BasicListUI.this.redrawList();
            } else if ("dropLocation" == propertyName) {
                repaintDropLocation((JList.DropLocation) propertyChangeEvent.getOldValue());
                repaintDropLocation(BasicListUI.this.list.getDropLocation());
            }
        }

        private void repaintDropLocation(JList.DropLocation dropLocation) {
            if (dropLocation == null) {
                return;
            }
            Rectangle dropLineRect = dropLocation.isInsert() ? BasicListUI.this.getDropLineRect(dropLocation) : BasicListUI.this.getCellBounds(BasicListUI.this.list, dropLocation.getIndex());
            if (dropLineRect != null) {
                BasicListUI.this.list.repaint(dropLineRect);
            }
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalAdded(ListDataEvent listDataEvent) {
            BasicListUI.this.updateLayoutStateNeeded = 1;
            int iMin = Math.min(listDataEvent.getIndex0(), listDataEvent.getIndex1());
            int iMax = Math.max(listDataEvent.getIndex0(), listDataEvent.getIndex1());
            ListSelectionModel selectionModel = BasicListUI.this.list.getSelectionModel();
            if (selectionModel != null) {
                selectionModel.insertIndexInterval(iMin, (iMax - iMin) + 1, true);
            }
            BasicListUI.this.redrawList();
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalRemoved(ListDataEvent listDataEvent) {
            BasicListUI.this.updateLayoutStateNeeded = 1;
            ListSelectionModel selectionModel = BasicListUI.this.list.getSelectionModel();
            if (selectionModel != null) {
                selectionModel.removeIndexInterval(listDataEvent.getIndex0(), listDataEvent.getIndex1());
            }
            BasicListUI.this.redrawList();
        }

        @Override // javax.swing.event.ListDataListener
        public void contentsChanged(ListDataEvent listDataEvent) {
            BasicListUI.this.updateLayoutStateNeeded = 1;
            BasicListUI.this.redrawList();
        }

        @Override // javax.swing.event.ListSelectionListener
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            BasicListUI.this.maybeUpdateLayoutState();
            int size = BasicListUI.this.list.getModel().getSize();
            Rectangle cellBounds = BasicListUI.this.getCellBounds(BasicListUI.this.list, Math.min(size - 1, Math.max(listSelectionEvent.getFirstIndex(), 0)), Math.min(size - 1, Math.max(listSelectionEvent.getLastIndex(), 0)));
            if (cellBounds != null) {
                BasicListUI.this.list.repaint(cellBounds.f12372x, cellBounds.f12373y, cellBounds.width, cellBounds.height);
            }
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
            if (SwingUtilities2.shouldIgnore(mouseEvent, BasicListUI.this.list)) {
                return;
            }
            boolean z2 = true;
            if (BasicListUI.this.list.getDragEnabled()) {
                int iLoc2IndexFileList = SwingUtilities2.loc2IndexFileList(BasicListUI.this.list, mouseEvent.getPoint());
                if (iLoc2IndexFileList != -1 && DragRecognitionSupport.mousePressed(mouseEvent)) {
                    this.dragPressDidSelection = false;
                    if (BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent)) {
                        return;
                    }
                    if (!mouseEvent.isShiftDown() && BasicListUI.this.list.isSelectedIndex(iLoc2IndexFileList)) {
                        BasicListUI.this.list.addSelectionInterval(iLoc2IndexFileList, iLoc2IndexFileList);
                        return;
                    } else {
                        z2 = false;
                        this.dragPressDidSelection = true;
                    }
                }
            } else {
                BasicListUI.this.list.setValueIsAdjusting(true);
            }
            if (z2) {
                SwingUtilities2.adjustFocus(BasicListUI.this.list);
            }
            adjustSelection(mouseEvent);
        }

        private void adjustSelection(MouseEvent mouseEvent) {
            boolean zIsSelectedIndex;
            int iLoc2IndexFileList = SwingUtilities2.loc2IndexFileList(BasicListUI.this.list, mouseEvent.getPoint());
            if (iLoc2IndexFileList < 0) {
                if (BasicListUI.this.isFileList && mouseEvent.getID() == 501) {
                    if (!mouseEvent.isShiftDown() || BasicListUI.this.list.getSelectionMode() == 0) {
                        BasicListUI.this.list.clearSelection();
                        return;
                    }
                    return;
                }
                return;
            }
            int iAdjustIndex = BasicListUI.adjustIndex(BasicListUI.this.list.getAnchorSelectionIndex(), BasicListUI.this.list);
            if (iAdjustIndex == -1) {
                iAdjustIndex = 0;
                zIsSelectedIndex = false;
            } else {
                zIsSelectedIndex = BasicListUI.this.list.isSelectedIndex(iAdjustIndex);
            }
            if (BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent)) {
                if (mouseEvent.isShiftDown()) {
                    if (zIsSelectedIndex) {
                        BasicListUI.this.list.addSelectionInterval(iAdjustIndex, iLoc2IndexFileList);
                        return;
                    }
                    BasicListUI.this.list.removeSelectionInterval(iAdjustIndex, iLoc2IndexFileList);
                    if (BasicListUI.this.isFileList) {
                        BasicListUI.this.list.addSelectionInterval(iLoc2IndexFileList, iLoc2IndexFileList);
                        BasicListUI.this.list.getSelectionModel().setAnchorSelectionIndex(iAdjustIndex);
                        return;
                    }
                    return;
                }
                if (BasicListUI.this.list.isSelectedIndex(iLoc2IndexFileList)) {
                    BasicListUI.this.list.removeSelectionInterval(iLoc2IndexFileList, iLoc2IndexFileList);
                    return;
                } else {
                    BasicListUI.this.list.addSelectionInterval(iLoc2IndexFileList, iLoc2IndexFileList);
                    return;
                }
            }
            if (mouseEvent.isShiftDown()) {
                BasicListUI.this.list.setSelectionInterval(iAdjustIndex, iLoc2IndexFileList);
            } else {
                BasicListUI.this.list.setSelectionInterval(iLoc2IndexFileList, iLoc2IndexFileList);
            }
        }

        @Override // javax.swing.plaf.basic.DragRecognitionSupport.BeforeDrag
        public void dragStarting(MouseEvent mouseEvent) {
            if (BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent)) {
                int iLoc2IndexFileList = SwingUtilities2.loc2IndexFileList(BasicListUI.this.list, mouseEvent.getPoint());
                BasicListUI.this.list.addSelectionInterval(iLoc2IndexFileList, iLoc2IndexFileList);
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            int iLocationToIndex;
            Rectangle cellBounds;
            if (SwingUtilities2.shouldIgnore(mouseEvent, BasicListUI.this.list)) {
                return;
            }
            if (BasicListUI.this.list.getDragEnabled()) {
                DragRecognitionSupport.mouseDragged(mouseEvent, this);
                return;
            }
            if (!mouseEvent.isShiftDown() && !BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent) && (iLocationToIndex = BasicListUI.this.locationToIndex(BasicListUI.this.list, mouseEvent.getPoint())) != -1 && !BasicListUI.this.isFileList && (cellBounds = BasicListUI.this.getCellBounds(BasicListUI.this.list, iLocationToIndex, iLocationToIndex)) != null) {
                BasicListUI.this.list.scrollRectToVisible(cellBounds);
                BasicListUI.this.list.setSelectionInterval(iLocationToIndex, iLocationToIndex);
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (SwingUtilities2.shouldIgnore(mouseEvent, BasicListUI.this.list)) {
                return;
            }
            if (BasicListUI.this.list.getDragEnabled()) {
                MouseEvent mouseEventMouseReleased = DragRecognitionSupport.mouseReleased(mouseEvent);
                if (mouseEventMouseReleased != null) {
                    SwingUtilities2.adjustFocus(BasicListUI.this.list);
                    if (!this.dragPressDidSelection) {
                        adjustSelection(mouseEventMouseReleased);
                        return;
                    }
                    return;
                }
                return;
            }
            BasicListUI.this.list.setValueIsAdjusting(false);
        }

        protected void repaintCellFocus() {
            Rectangle cellBounds;
            int iAdjustIndex = BasicListUI.adjustIndex(BasicListUI.this.list.getLeadSelectionIndex(), BasicListUI.this.list);
            if (iAdjustIndex != -1 && (cellBounds = BasicListUI.this.getCellBounds(BasicListUI.this.list, iAdjustIndex, iAdjustIndex)) != null) {
                BasicListUI.this.list.repaint(cellBounds.f12372x, cellBounds.f12373y, cellBounds.width, cellBounds.height);
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            repaintCellFocus();
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            repaintCellFocus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int adjustIndex(int i2, JList jList) {
        if (i2 < jList.getModel().getSize()) {
            return i2;
        }
        return -1;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicListUI$ListTransferHandler.class */
    static class ListTransferHandler extends TransferHandler implements UIResource {
        ListTransferHandler() {
        }

        @Override // javax.swing.TransferHandler
        protected Transferable createTransferable(JComponent jComponent) {
            Object[] selectedValues;
            if (!(jComponent instanceof JList) || (selectedValues = ((JList) jComponent).getSelectedValues()) == null || selectedValues.length == 0) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("<html>\n<body>\n<ul>\n");
            for (int i2 = 0; i2 < selectedValues.length; i2++) {
                Object obj = selectedValues[i2];
                String string = obj == null ? "" : obj.toString();
                stringBuffer.append(string + "\n");
                stringBuffer2.append("  <li>" + string + "\n");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            stringBuffer2.append("</ul>\n</body>\n</html>");
            return new BasicTransferable(stringBuffer.toString(), stringBuffer2.toString());
        }

        @Override // javax.swing.TransferHandler
        public int getSourceActions(JComponent jComponent) {
            return 1;
        }
    }
}
