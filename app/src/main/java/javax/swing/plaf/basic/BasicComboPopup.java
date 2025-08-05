package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.accessibility.AccessibleContext;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup.class */
public class BasicComboPopup extends JPopupMenu implements ComboPopup {
    static final ListModel EmptyListModel = new EmptyListModelClass();
    private static Border LIST_BORDER = new LineBorder(Color.BLACK, 1);
    protected JComboBox comboBox;
    protected JList list;
    protected JScrollPane scroller;
    private Handler handler;
    protected MouseMotionListener mouseMotionListener;
    protected MouseListener mouseListener;
    protected KeyListener keyListener;
    protected ListSelectionListener listSelectionListener;
    protected MouseListener listMouseListener;
    protected MouseMotionListener listMouseMotionListener;
    protected PropertyChangeListener propertyChangeListener;
    protected ListDataListener listDataListener;
    protected ItemListener itemListener;
    private MouseWheelListener scrollerMouseWheelListener;
    protected Timer autoscrollTimer;
    protected static final int SCROLL_UP = 0;
    protected static final int SCROLL_DOWN = 1;
    protected boolean valueIsAdjusting = false;
    protected boolean hasEntered = false;
    protected boolean isAutoScrolling = false;
    protected int scrollDirection = 0;

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup$EmptyListModelClass.class */
    private static class EmptyListModelClass implements ListModel<Object>, Serializable {
        private EmptyListModelClass() {
        }

        @Override // javax.swing.ListModel
        public int getSize() {
            return 0;
        }

        @Override // javax.swing.ListModel
        public Object getElementAt(int i2) {
            return null;
        }

        @Override // javax.swing.ListModel
        public void addListDataListener(ListDataListener listDataListener) {
        }

        @Override // javax.swing.ListModel
        public void removeListDataListener(ListDataListener listDataListener) {
        }
    }

    @Override // java.awt.Component
    public void show() throws HeadlessException {
        this.comboBox.firePopupMenuWillBecomeVisible();
        setListSelection(this.comboBox.getSelectedIndex());
        Point popupLocation = getPopupLocation();
        show(this.comboBox, popupLocation.f12370x, popupLocation.f12371y);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void hide() {
        MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
        MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
        int i2 = 0;
        while (true) {
            if (i2 >= selectedPath.length) {
                break;
            }
            if (selectedPath[i2] != this) {
                i2++;
            } else {
                menuSelectionManagerDefaultManager.clearSelectedPath();
                break;
            }
        }
        if (selectedPath.length > 0) {
            this.comboBox.repaint();
        }
    }

    @Override // javax.swing.plaf.basic.ComboPopup
    public JList getList() {
        return this.list;
    }

    @Override // javax.swing.plaf.basic.ComboPopup
    public MouseListener getMouseListener() {
        if (this.mouseListener == null) {
            this.mouseListener = createMouseListener();
        }
        return this.mouseListener;
    }

    @Override // javax.swing.plaf.basic.ComboPopup
    public MouseMotionListener getMouseMotionListener() {
        if (this.mouseMotionListener == null) {
            this.mouseMotionListener = createMouseMotionListener();
        }
        return this.mouseMotionListener;
    }

    @Override // javax.swing.plaf.basic.ComboPopup
    public KeyListener getKeyListener() {
        if (this.keyListener == null) {
            this.keyListener = createKeyListener();
        }
        return this.keyListener;
    }

    @Override // javax.swing.plaf.basic.ComboPopup
    public void uninstallingUI() {
        if (this.propertyChangeListener != null) {
            this.comboBox.removePropertyChangeListener(this.propertyChangeListener);
        }
        if (this.itemListener != null) {
            this.comboBox.removeItemListener(this.itemListener);
        }
        uninstallComboBoxModelListeners(this.comboBox.getModel());
        uninstallKeyboardActions();
        uninstallListListeners();
        uninstallScrollerListeners();
        this.list.setModel(EmptyListModel);
    }

    protected void uninstallComboBoxModelListeners(ComboBoxModel comboBoxModel) {
        if (comboBoxModel != null && this.listDataListener != null) {
            comboBoxModel.removeListDataListener(this.listDataListener);
        }
    }

    protected void uninstallKeyboardActions() {
    }

    public BasicComboPopup(JComboBox jComboBox) {
        setName("ComboPopup.popup");
        this.comboBox = jComboBox;
        setLightWeightPopupEnabled(this.comboBox.isLightWeightPopupEnabled());
        this.list = createList();
        this.list.setName("ComboBox.list");
        configureList();
        this.scroller = createScroller();
        this.scroller.setName("ComboBox.scrollPane");
        configureScroller();
        configurePopup();
        installComboBoxListeners();
        installKeyboardActions();
    }

    @Override // javax.swing.JPopupMenu
    protected void firePopupMenuWillBecomeVisible() {
        if (this.scrollerMouseWheelListener != null) {
            this.comboBox.addMouseWheelListener(this.scrollerMouseWheelListener);
        }
        super.firePopupMenuWillBecomeVisible();
    }

    @Override // javax.swing.JPopupMenu
    protected void firePopupMenuWillBecomeInvisible() {
        if (this.scrollerMouseWheelListener != null) {
            this.comboBox.removeMouseWheelListener(this.scrollerMouseWheelListener);
        }
        super.firePopupMenuWillBecomeInvisible();
        this.comboBox.firePopupMenuWillBecomeInvisible();
    }

    @Override // javax.swing.JPopupMenu
    protected void firePopupMenuCanceled() {
        if (this.scrollerMouseWheelListener != null) {
            this.comboBox.removeMouseWheelListener(this.scrollerMouseWheelListener);
        }
        super.firePopupMenuCanceled();
        this.comboBox.firePopupMenuCanceled();
    }

    protected MouseListener createMouseListener() {
        return getHandler();
    }

    protected MouseMotionListener createMouseMotionListener() {
        return getHandler();
    }

    protected KeyListener createKeyListener() {
        return null;
    }

    protected ListSelectionListener createListSelectionListener() {
        return null;
    }

    protected ListDataListener createListDataListener() {
        return null;
    }

    protected MouseListener createListMouseListener() {
        return getHandler();
    }

    protected MouseMotionListener createListMouseMotionListener() {
        return getHandler();
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    protected ItemListener createItemListener() {
        return getHandler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected JList createList() {
        return new JList(this.comboBox.getModel()) { // from class: javax.swing.plaf.basic.BasicComboPopup.1
            @Override // javax.swing.JComponent, java.awt.Component
            public void processMouseEvent(MouseEvent mouseEvent) {
                if (BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent)) {
                    MouseEvent mouseEvent2 = new MouseEvent((Component) mouseEvent.getSource(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers() ^ Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
                    AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
                    mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
                    mouseEvent = mouseEvent2;
                }
                super.processMouseEvent(mouseEvent);
            }
        };
    }

    protected void configureList() {
        this.list.setFont(this.comboBox.getFont());
        this.list.setForeground(this.comboBox.getForeground());
        this.list.setBackground(this.comboBox.getBackground());
        this.list.setSelectionForeground(UIManager.getColor("ComboBox.selectionForeground"));
        this.list.setSelectionBackground(UIManager.getColor("ComboBox.selectionBackground"));
        this.list.setBorder(null);
        this.list.setCellRenderer(this.comboBox.getRenderer());
        this.list.setFocusable(false);
        this.list.setSelectionMode(0);
        setListSelection(this.comboBox.getSelectedIndex());
        installListListeners();
    }

    protected void installListListeners() {
        MouseListener mouseListenerCreateListMouseListener = createListMouseListener();
        this.listMouseListener = mouseListenerCreateListMouseListener;
        if (mouseListenerCreateListMouseListener != null) {
            this.list.addMouseListener(this.listMouseListener);
        }
        MouseMotionListener mouseMotionListenerCreateListMouseMotionListener = createListMouseMotionListener();
        this.listMouseMotionListener = mouseMotionListenerCreateListMouseMotionListener;
        if (mouseMotionListenerCreateListMouseMotionListener != null) {
            this.list.addMouseMotionListener(this.listMouseMotionListener);
        }
        ListSelectionListener listSelectionListenerCreateListSelectionListener = createListSelectionListener();
        this.listSelectionListener = listSelectionListenerCreateListSelectionListener;
        if (listSelectionListenerCreateListSelectionListener != null) {
            this.list.addListSelectionListener(this.listSelectionListener);
        }
    }

    void uninstallListListeners() {
        if (this.listMouseListener != null) {
            this.list.removeMouseListener(this.listMouseListener);
            this.listMouseListener = null;
        }
        if (this.listMouseMotionListener != null) {
            this.list.removeMouseMotionListener(this.listMouseMotionListener);
            this.listMouseMotionListener = null;
        }
        if (this.listSelectionListener != null) {
            this.list.removeListSelectionListener(this.listSelectionListener);
            this.listSelectionListener = null;
        }
        this.handler = null;
    }

    protected JScrollPane createScroller() {
        JScrollPane jScrollPane = new JScrollPane(this.list, 20, 31);
        jScrollPane.setHorizontalScrollBar(null);
        return jScrollPane;
    }

    protected void configureScroller() {
        this.scroller.setFocusable(false);
        this.scroller.getVerticalScrollBar().setFocusable(false);
        this.scroller.setBorder(null);
        installScrollerListeners();
    }

    protected void configurePopup() {
        setLayout(new BoxLayout(this, 1));
        setBorderPainted(true);
        setBorder(LIST_BORDER);
        setOpaque(false);
        add(this.scroller);
        setDoubleBuffered(true);
        setFocusable(false);
    }

    private void installScrollerListeners() {
        this.scrollerMouseWheelListener = getHandler();
        if (this.scrollerMouseWheelListener != null) {
            this.scroller.addMouseWheelListener(this.scrollerMouseWheelListener);
        }
    }

    private void uninstallScrollerListeners() {
        if (this.scrollerMouseWheelListener != null) {
            this.scroller.removeMouseWheelListener(this.scrollerMouseWheelListener);
            this.scrollerMouseWheelListener = null;
        }
    }

    protected void installComboBoxListeners() {
        PropertyChangeListener propertyChangeListenerCreatePropertyChangeListener = createPropertyChangeListener();
        this.propertyChangeListener = propertyChangeListenerCreatePropertyChangeListener;
        if (propertyChangeListenerCreatePropertyChangeListener != null) {
            this.comboBox.addPropertyChangeListener(this.propertyChangeListener);
        }
        ItemListener itemListenerCreateItemListener = createItemListener();
        this.itemListener = itemListenerCreateItemListener;
        if (itemListenerCreateItemListener != null) {
            this.comboBox.addItemListener(this.itemListener);
        }
        installComboBoxModelListeners(this.comboBox.getModel());
    }

    protected void installComboBoxModelListeners(ComboBoxModel comboBoxModel) {
        if (comboBoxModel != null) {
            ListDataListener listDataListenerCreateListDataListener = createListDataListener();
            this.listDataListener = listDataListenerCreateListDataListener;
            if (listDataListenerCreateListDataListener != null) {
                comboBoxModel.addListDataListener(this.listDataListener);
            }
        }
    }

    protected void installKeyboardActions() {
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup$InvocationMouseHandler.class */
    protected class InvocationMouseHandler extends MouseAdapter {
        protected InvocationMouseHandler() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) throws HeadlessException {
            BasicComboPopup.this.getHandler().mousePressed(mouseEvent);
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            BasicComboPopup.this.getHandler().mouseReleased(mouseEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup$InvocationMouseMotionHandler.class */
    protected class InvocationMouseMotionHandler extends MouseMotionAdapter {
        protected InvocationMouseMotionHandler() {
        }

        @Override // java.awt.event.MouseMotionAdapter, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            BasicComboPopup.this.getHandler().mouseDragged(mouseEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup$InvocationKeyHandler.class */
    public class InvocationKeyHandler extends KeyAdapter {
        public InvocationKeyHandler() {
        }

        @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
        public void keyReleased(KeyEvent keyEvent) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup$ListSelectionHandler.class */
    protected class ListSelectionHandler implements ListSelectionListener {
        protected ListSelectionHandler() {
        }

        @Override // javax.swing.event.ListSelectionListener
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup$ListDataHandler.class */
    public class ListDataHandler implements ListDataListener {
        public ListDataHandler() {
        }

        @Override // javax.swing.event.ListDataListener
        public void contentsChanged(ListDataEvent listDataEvent) {
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalAdded(ListDataEvent listDataEvent) {
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalRemoved(ListDataEvent listDataEvent) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup$ListMouseHandler.class */
    protected class ListMouseHandler extends MouseAdapter {
        protected ListMouseHandler() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            BasicComboPopup.this.getHandler().mouseReleased(mouseEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup$ListMouseMotionHandler.class */
    protected class ListMouseMotionHandler extends MouseMotionAdapter {
        protected ListMouseMotionHandler() {
        }

        @Override // java.awt.event.MouseMotionAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            BasicComboPopup.this.getHandler().mouseMoved(mouseEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup$ItemHandler.class */
    protected class ItemHandler implements ItemListener {
        protected ItemHandler() {
        }

        @Override // java.awt.event.ItemListener
        public void itemStateChanged(ItemEvent itemEvent) {
            BasicComboPopup.this.getHandler().itemStateChanged(itemEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup$PropertyChangeHandler.class */
    protected class PropertyChangeHandler implements PropertyChangeListener {
        protected PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicComboPopup.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup$AutoScrollActionHandler.class */
    private class AutoScrollActionHandler implements ActionListener {
        private int direction;

        AutoScrollActionHandler(int i2) {
            this.direction = i2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (this.direction == 0) {
                BasicComboPopup.this.autoScrollUp();
            } else {
                BasicComboPopup.this.autoScrollDown();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboPopup$Handler.class */
    private class Handler implements ItemListener, MouseListener, MouseMotionListener, MouseWheelListener, PropertyChangeListener, Serializable {
        private Handler() {
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) throws HeadlessException {
            if (mouseEvent.getSource() == BasicComboPopup.this.list || !SwingUtilities.isLeftMouseButton(mouseEvent) || !BasicComboPopup.this.comboBox.isEnabled()) {
                return;
            }
            if (BasicComboPopup.this.comboBox.isEditable()) {
                Component editorComponent = BasicComboPopup.this.comboBox.getEditor().getEditorComponent();
                if (!(editorComponent instanceof JComponent) || ((JComponent) editorComponent).isRequestFocusEnabled()) {
                    editorComponent.requestFocus();
                }
            } else if (BasicComboPopup.this.comboBox.isRequestFocusEnabled()) {
                BasicComboPopup.this.comboBox.requestFocus();
            }
            BasicComboPopup.this.togglePopup();
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (mouseEvent.getSource() == BasicComboPopup.this.list) {
                if (BasicComboPopup.this.list.getModel().getSize() > 0) {
                    if (BasicComboPopup.this.comboBox.getSelectedIndex() == BasicComboPopup.this.list.getSelectedIndex()) {
                        BasicComboPopup.this.comboBox.getEditor().setItem(BasicComboPopup.this.list.getSelectedValue());
                    }
                    BasicComboPopup.this.comboBox.setSelectedIndex(BasicComboPopup.this.list.getSelectedIndex());
                }
                BasicComboPopup.this.comboBox.setPopupVisible(false);
                if (BasicComboPopup.this.comboBox.isEditable() && BasicComboPopup.this.comboBox.getEditor() != null) {
                    BasicComboPopup.this.comboBox.configureEditor(BasicComboPopup.this.comboBox.getEditor(), BasicComboPopup.this.comboBox.getSelectedItem());
                    return;
                }
                return;
            }
            Dimension size = ((Component) mouseEvent.getSource()).getSize();
            if (!new Rectangle(0, 0, size.width - 1, size.height - 1).contains(mouseEvent.getPoint())) {
                Point point = BasicComboPopup.this.convertMouseEvent(mouseEvent).getPoint();
                Rectangle rectangle = new Rectangle();
                BasicComboPopup.this.list.computeVisibleRect(rectangle);
                if (rectangle.contains(point)) {
                    if (BasicComboPopup.this.comboBox.getSelectedIndex() == BasicComboPopup.this.list.getSelectedIndex()) {
                        BasicComboPopup.this.comboBox.getEditor().setItem(BasicComboPopup.this.list.getSelectedValue());
                    }
                    BasicComboPopup.this.comboBox.setSelectedIndex(BasicComboPopup.this.list.getSelectedIndex());
                }
                BasicComboPopup.this.comboBox.setPopupVisible(false);
            }
            BasicComboPopup.this.hasEntered = false;
            BasicComboPopup.this.stopAutoScrolling();
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            if (mouseEvent.getSource() == BasicComboPopup.this.list) {
                Point point = mouseEvent.getPoint();
                Rectangle rectangle = new Rectangle();
                BasicComboPopup.this.list.computeVisibleRect(rectangle);
                if (rectangle.contains(point)) {
                    BasicComboPopup.this.updateListBoxSelectionForEvent(mouseEvent, false);
                }
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (mouseEvent.getSource() != BasicComboPopup.this.list && BasicComboPopup.this.isVisible()) {
                MouseEvent mouseEventConvertMouseEvent = BasicComboPopup.this.convertMouseEvent(mouseEvent);
                Rectangle rectangle = new Rectangle();
                BasicComboPopup.this.list.computeVisibleRect(rectangle);
                if (mouseEventConvertMouseEvent.getPoint().f12371y >= rectangle.f12373y && mouseEventConvertMouseEvent.getPoint().f12371y <= (rectangle.f12373y + rectangle.height) - 1) {
                    BasicComboPopup.this.hasEntered = true;
                    if (BasicComboPopup.this.isAutoScrolling) {
                        BasicComboPopup.this.stopAutoScrolling();
                    }
                    if (rectangle.contains(mouseEventConvertMouseEvent.getPoint())) {
                        BasicComboPopup.this.updateListBoxSelectionForEvent(mouseEventConvertMouseEvent, false);
                        return;
                    }
                    return;
                }
                if (BasicComboPopup.this.hasEntered) {
                    int i2 = mouseEventConvertMouseEvent.getPoint().f12371y < rectangle.f12373y ? 0 : 1;
                    if (BasicComboPopup.this.isAutoScrolling && BasicComboPopup.this.scrollDirection != i2) {
                        BasicComboPopup.this.stopAutoScrolling();
                        BasicComboPopup.this.startAutoScrolling(i2);
                        return;
                    } else {
                        if (!BasicComboPopup.this.isAutoScrolling) {
                            BasicComboPopup.this.startAutoScrolling(i2);
                            return;
                        }
                        return;
                    }
                }
                if (mouseEvent.getPoint().f12371y < 0) {
                    BasicComboPopup.this.hasEntered = true;
                    BasicComboPopup.this.startAutoScrolling(0);
                }
            }
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            JComboBox jComboBox = (JComboBox) propertyChangeEvent.getSource();
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName == "model") {
                ComboBoxModel comboBoxModel = (ComboBoxModel) propertyChangeEvent.getOldValue();
                ComboBoxModel comboBoxModel2 = (ComboBoxModel) propertyChangeEvent.getNewValue();
                BasicComboPopup.this.uninstallComboBoxModelListeners(comboBoxModel);
                BasicComboPopup.this.installComboBoxModelListeners(comboBoxModel2);
                BasicComboPopup.this.list.setModel(comboBoxModel2);
                if (BasicComboPopup.this.isVisible()) {
                    BasicComboPopup.this.hide();
                    return;
                }
                return;
            }
            if (propertyName == "renderer") {
                BasicComboPopup.this.list.setCellRenderer(jComboBox.getRenderer());
                if (BasicComboPopup.this.isVisible()) {
                    BasicComboPopup.this.hide();
                    return;
                }
                return;
            }
            if (propertyName == "componentOrientation") {
                ComponentOrientation componentOrientation = (ComponentOrientation) propertyChangeEvent.getNewValue();
                JList list = BasicComboPopup.this.getList();
                if (list != null && list.getComponentOrientation() != componentOrientation) {
                    list.setComponentOrientation(componentOrientation);
                }
                if (BasicComboPopup.this.scroller != null && BasicComboPopup.this.scroller.getComponentOrientation() != componentOrientation) {
                    BasicComboPopup.this.scroller.setComponentOrientation(componentOrientation);
                }
                if (componentOrientation != BasicComboPopup.this.getComponentOrientation()) {
                    BasicComboPopup.this.setComponentOrientation(componentOrientation);
                    return;
                }
                return;
            }
            if (propertyName == "lightWeightPopupEnabled") {
                BasicComboPopup.this.setLightWeightPopupEnabled(jComboBox.isLightWeightPopupEnabled());
            }
        }

        @Override // java.awt.event.ItemListener
        public void itemStateChanged(ItemEvent itemEvent) {
            if (itemEvent.getStateChange() != 1) {
                BasicComboPopup.this.setListSelection(-1);
            } else {
                BasicComboPopup.this.setListSelection(((JComboBox) itemEvent.getSource()).getSelectedIndex());
            }
        }

        @Override // java.awt.event.MouseWheelListener
        public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
            mouseWheelEvent.consume();
        }
    }

    @Override // java.awt.Component
    public boolean isFocusTraversable() {
        return false;
    }

    protected void startAutoScrolling(int i2) {
        if (this.isAutoScrolling) {
            this.autoscrollTimer.stop();
        }
        this.isAutoScrolling = true;
        if (i2 == 0) {
            this.scrollDirection = 0;
            this.list.setSelectedIndex(this.list.locationToIndex(SwingUtilities.convertPoint(this.scroller, new Point(1, 1), this.list)));
            this.autoscrollTimer = new Timer(100, new AutoScrollActionHandler(0));
        } else if (i2 == 1) {
            this.scrollDirection = 1;
            this.list.setSelectedIndex(this.list.locationToIndex(SwingUtilities.convertPoint(this.scroller, new Point(1, (this.scroller.getSize().height - 1) - 2), this.list)));
            this.autoscrollTimer = new Timer(100, new AutoScrollActionHandler(1));
        }
        this.autoscrollTimer.start();
    }

    protected void stopAutoScrolling() {
        this.isAutoScrolling = false;
        if (this.autoscrollTimer != null) {
            this.autoscrollTimer.stop();
            this.autoscrollTimer = null;
        }
    }

    protected void autoScrollUp() {
        int selectedIndex = this.list.getSelectedIndex();
        if (selectedIndex > 0) {
            this.list.setSelectedIndex(selectedIndex - 1);
            this.list.ensureIndexIsVisible(selectedIndex - 1);
        }
    }

    protected void autoScrollDown() {
        int selectedIndex = this.list.getSelectedIndex();
        if (selectedIndex < this.list.getModel().getSize() - 1) {
            this.list.setSelectedIndex(selectedIndex + 1);
            this.list.ensureIndexIsVisible(selectedIndex + 1);
        }
    }

    @Override // javax.swing.JPopupMenu, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        AccessibleContext accessibleContext = super.getAccessibleContext();
        accessibleContext.setAccessibleParent(this.comboBox);
        return accessibleContext;
    }

    protected void delegateFocus(MouseEvent mouseEvent) {
        if (this.comboBox.isEditable()) {
            Component editorComponent = this.comboBox.getEditor().getEditorComponent();
            if (!(editorComponent instanceof JComponent) || ((JComponent) editorComponent).isRequestFocusEnabled()) {
                editorComponent.requestFocus();
                return;
            }
            return;
        }
        if (this.comboBox.isRequestFocusEnabled()) {
            this.comboBox.requestFocus();
        }
    }

    protected void togglePopup() throws HeadlessException {
        if (isVisible()) {
            hide();
        } else {
            show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setListSelection(int i2) {
        if (i2 == -1) {
            this.list.clearSelection();
        } else {
            this.list.setSelectedIndex(i2);
            this.list.ensureIndexIsVisible(i2);
        }
    }

    protected MouseEvent convertMouseEvent(MouseEvent mouseEvent) {
        Point pointConvertPoint = SwingUtilities.convertPoint((Component) mouseEvent.getSource(), mouseEvent.getPoint(), this.list);
        MouseEvent mouseEvent2 = new MouseEvent((Component) mouseEvent.getSource(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), pointConvertPoint.f12370x, pointConvertPoint.f12371y, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
        AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
        mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
        return mouseEvent2;
    }

    protected int getPopupHeightForRowCount(int i2) {
        int iMin = Math.min(i2, this.comboBox.getItemCount());
        int height = 0;
        ListCellRenderer cellRenderer = this.list.getCellRenderer();
        for (int i3 = 0; i3 < iMin; i3++) {
            height += cellRenderer.getListCellRendererComponent(this.list, this.list.getModel().getElementAt(i3), i3, false, false).getPreferredSize().height;
        }
        if (height == 0) {
            height = this.comboBox.getHeight();
        }
        Border viewportBorder = this.scroller.getViewportBorder();
        if (viewportBorder != null) {
            Insets borderInsets = viewportBorder.getBorderInsets(null);
            height += borderInsets.top + borderInsets.bottom;
        }
        Border border = this.scroller.getBorder();
        if (border != null) {
            Insets borderInsets2 = border.getBorderInsets(null);
            height += borderInsets2.top + borderInsets2.bottom;
        }
        return height;
    }

    protected Rectangle computePopupBounds(int i2, int i3, int i4, int i5) throws HeadlessException {
        Rectangle rectangle;
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        GraphicsConfiguration graphicsConfiguration = this.comboBox.getGraphicsConfiguration();
        Point point = new Point();
        SwingUtilities.convertPointFromScreen(point, this.comboBox);
        if (graphicsConfiguration != null) {
            Insets screenInsets = defaultToolkit.getScreenInsets(graphicsConfiguration);
            rectangle = graphicsConfiguration.getBounds();
            rectangle.width -= screenInsets.left + screenInsets.right;
            rectangle.height -= screenInsets.top + screenInsets.bottom;
            rectangle.f12372x += point.f12370x + screenInsets.left;
            rectangle.f12373y += point.f12371y + screenInsets.top;
        } else {
            rectangle = new Rectangle(point, defaultToolkit.getScreenSize());
        }
        Rectangle rectangle2 = new Rectangle(i2, i3, i4, i5);
        if (i3 + i5 > rectangle.f12373y + rectangle.height && i5 < rectangle.height) {
            rectangle2.f12373y = -rectangle2.height;
        }
        return rectangle2;
    }

    private Point getPopupLocation() throws HeadlessException {
        Dimension size = this.comboBox.getSize();
        Insets insets = getInsets();
        size.setSize(size.width - (insets.right + insets.left), getPopupHeightForRowCount(this.comboBox.getMaximumRowCount()));
        Rectangle rectangleComputePopupBounds = computePopupBounds(0, this.comboBox.getBounds().height, size.width, size.height);
        Dimension size2 = rectangleComputePopupBounds.getSize();
        Point location = rectangleComputePopupBounds.getLocation();
        this.scroller.setMaximumSize(size2);
        this.scroller.setPreferredSize(size2);
        this.scroller.setMinimumSize(size2);
        this.list.revalidate();
        return location;
    }

    protected void updateListBoxSelectionForEvent(MouseEvent mouseEvent, boolean z2) {
        Point point = mouseEvent.getPoint();
        if (this.list == null) {
            return;
        }
        int iLocationToIndex = this.list.locationToIndex(point);
        if (iLocationToIndex == -1) {
            if (point.f12371y < 0) {
                iLocationToIndex = 0;
            } else {
                iLocationToIndex = this.comboBox.getModel().getSize() - 1;
            }
        }
        if (this.list.getSelectedIndex() != iLocationToIndex) {
            this.list.setSelectedIndex(iLocationToIndex);
            if (z2) {
                this.list.ensureIndexIsVisible(iLocationToIndex);
            }
        }
    }
}
