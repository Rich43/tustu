package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.accessibility.Accessible;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.CellRendererPane;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.Position;
import jdk.jfr.Enabled;
import sun.awt.AppContext;
import sun.swing.DefaultLookup;
import sun.swing.UIAction;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxUI.class */
public class BasicComboBoxUI extends ComboBoxUI {
    protected JComboBox comboBox;
    private static final String IS_TABLE_CELL_EDITOR = "JComboBox.isTableCellEditor";
    protected JList listBox;
    protected ComboPopup popup;
    protected Component editor;
    protected JButton arrowButton;
    protected KeyListener keyListener;
    protected FocusListener focusListener;
    protected PropertyChangeListener propertyChangeListener;
    protected ItemListener itemListener;
    protected MouseListener popupMouseListener;
    protected MouseMotionListener popupMouseMotionListener;
    protected KeyListener popupKeyListener;
    protected ListDataListener listDataListener;
    private Handler handler;
    JComboBox.KeySelectionManager keySelectionManager;
    private static final Object COMBO_UI_LIST_CELL_RENDERER_KEY = new StringBuffer("DefaultListCellRendererKey");
    static final StringBuffer HIDE_POPUP_KEY = new StringBuffer("HidePopupKey");
    private boolean sameBaseline;
    protected Insets padding;
    protected boolean hasFocus = false;
    private boolean isTableCellEditor = false;
    protected CellRendererPane currentValuePane = new CellRendererPane();
    private long timeFactor = 1000;
    private long lastTime = 0;
    private long time = 0;
    protected boolean isMinimumSizeDirty = true;
    protected Dimension cachedMinimumSize = new Dimension(0, 0);
    private boolean isDisplaySizeDirty = true;
    private Dimension cachedDisplaySize = new Dimension(0, 0);
    protected boolean squareButton = true;

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
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$602(javax.swing.plaf.basic.BasicComboBoxUI r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.lastTime = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.basic.BasicComboBoxUI.access$602(javax.swing.plaf.basic.BasicComboBoxUI, long):long");
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
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$702(javax.swing.plaf.basic.BasicComboBoxUI r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.time = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.basic.BasicComboBoxUI.access$702(javax.swing.plaf.basic.BasicComboBoxUI, long):long");
    }

    public BasicComboBoxUI() {
    }

    static {
    }

    private static ListCellRenderer getDefaultListCellRenderer() {
        ListCellRenderer defaultListCellRenderer = (ListCellRenderer) AppContext.getAppContext().get(COMBO_UI_LIST_CELL_RENDERER_KEY);
        if (defaultListCellRenderer == null) {
            defaultListCellRenderer = new DefaultListCellRenderer();
            AppContext.getAppContext().put(COMBO_UI_LIST_CELL_RENDERER_KEY, new DefaultListCellRenderer());
        }
        return defaultListCellRenderer;
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("hidePopup"));
        lazyActionMap.put(new Actions("pageDownPassThrough"));
        lazyActionMap.put(new Actions("pageUpPassThrough"));
        lazyActionMap.put(new Actions("homePassThrough"));
        lazyActionMap.put(new Actions("endPassThrough"));
        lazyActionMap.put(new Actions("selectNext"));
        lazyActionMap.put(new Actions("selectNext2"));
        lazyActionMap.put(new Actions("togglePopup"));
        lazyActionMap.put(new Actions("spacePopup"));
        lazyActionMap.put(new Actions("selectPrevious"));
        lazyActionMap.put(new Actions("selectPrevious2"));
        lazyActionMap.put(new Actions("enterPressed"));
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicComboBoxUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.isMinimumSizeDirty = true;
        this.comboBox = (JComboBox) jComponent;
        installDefaults();
        this.popup = createPopup();
        this.listBox = this.popup.getList();
        Boolean bool = (Boolean) jComponent.getClientProperty(IS_TABLE_CELL_EDITOR);
        if (bool != null) {
            this.isTableCellEditor = bool.equals(Boolean.TRUE);
        }
        if (this.comboBox.getRenderer() == null || (this.comboBox.getRenderer() instanceof UIResource)) {
            this.comboBox.setRenderer(createRenderer());
        }
        if (this.comboBox.getEditor() == null || (this.comboBox.getEditor() instanceof UIResource)) {
            this.comboBox.setEditor(createEditor());
        }
        installListeners();
        installComponents();
        this.comboBox.setLayout(createLayoutManager());
        this.comboBox.setRequestFocusEnabled(true);
        installKeyboardActions();
        this.comboBox.putClientProperty("doNotCancelPopup", HIDE_POPUP_KEY);
        if (this.keySelectionManager == null || (this.keySelectionManager instanceof UIResource)) {
            this.keySelectionManager = new DefaultKeySelectionManager();
        }
        this.comboBox.setKeySelectionManager(this.keySelectionManager);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        setPopupVisible(this.comboBox, false);
        this.popup.uninstallingUI();
        uninstallKeyboardActions();
        this.comboBox.setLayout(null);
        uninstallComponents();
        uninstallListeners();
        uninstallDefaults();
        if (this.comboBox.getRenderer() == null || (this.comboBox.getRenderer() instanceof UIResource)) {
            this.comboBox.setRenderer(null);
        }
        ComboBoxEditor editor = this.comboBox.getEditor();
        if (editor instanceof UIResource) {
            if (editor.getEditorComponent().hasFocus()) {
                this.comboBox.requestFocusInWindow();
            }
            this.comboBox.setEditor(null);
        }
        if (this.keySelectionManager instanceof UIResource) {
            this.comboBox.setKeySelectionManager(null);
        }
        this.handler = null;
        this.keyListener = null;
        this.focusListener = null;
        this.listDataListener = null;
        this.propertyChangeListener = null;
        this.popup = null;
        this.listBox = null;
        this.comboBox = null;
    }

    protected void installDefaults() {
        LookAndFeel.installColorsAndFont(this.comboBox, "ComboBox.background", "ComboBox.foreground", "ComboBox.font");
        LookAndFeel.installBorder(this.comboBox, "ComboBox.border");
        LookAndFeel.installProperty(this.comboBox, "opaque", Boolean.TRUE);
        Long l2 = (Long) UIManager.get("ComboBox.timeFactor");
        this.timeFactor = l2 == null ? 1000L : l2.longValue();
        Boolean bool = (Boolean) UIManager.get("ComboBox.squareButton");
        this.squareButton = bool == null ? true : bool.booleanValue();
        this.padding = UIManager.getInsets("ComboBox.padding");
    }

    protected void installListeners() {
        ItemListener itemListenerCreateItemListener = createItemListener();
        this.itemListener = itemListenerCreateItemListener;
        if (itemListenerCreateItemListener != null) {
            this.comboBox.addItemListener(this.itemListener);
        }
        PropertyChangeListener propertyChangeListenerCreatePropertyChangeListener = createPropertyChangeListener();
        this.propertyChangeListener = propertyChangeListenerCreatePropertyChangeListener;
        if (propertyChangeListenerCreatePropertyChangeListener != null) {
            this.comboBox.addPropertyChangeListener(this.propertyChangeListener);
        }
        KeyListener keyListenerCreateKeyListener = createKeyListener();
        this.keyListener = keyListenerCreateKeyListener;
        if (keyListenerCreateKeyListener != null) {
            this.comboBox.addKeyListener(this.keyListener);
        }
        FocusListener focusListenerCreateFocusListener = createFocusListener();
        this.focusListener = focusListenerCreateFocusListener;
        if (focusListenerCreateFocusListener != null) {
            this.comboBox.addFocusListener(this.focusListener);
        }
        MouseListener mouseListener = this.popup.getMouseListener();
        this.popupMouseListener = mouseListener;
        if (mouseListener != null) {
            this.comboBox.addMouseListener(this.popupMouseListener);
        }
        MouseMotionListener mouseMotionListener = this.popup.getMouseMotionListener();
        this.popupMouseMotionListener = mouseMotionListener;
        if (mouseMotionListener != null) {
            this.comboBox.addMouseMotionListener(this.popupMouseMotionListener);
        }
        KeyListener keyListener = this.popup.getKeyListener();
        this.popupKeyListener = keyListener;
        if (keyListener != null) {
            this.comboBox.addKeyListener(this.popupKeyListener);
        }
        if (this.comboBox.getModel() != null) {
            ListDataListener listDataListenerCreateListDataListener = createListDataListener();
            this.listDataListener = listDataListenerCreateListDataListener;
            if (listDataListenerCreateListDataListener != null) {
                this.comboBox.getModel().addListDataListener(this.listDataListener);
            }
        }
    }

    protected void uninstallDefaults() {
        LookAndFeel.installColorsAndFont(this.comboBox, "ComboBox.background", "ComboBox.foreground", "ComboBox.font");
        LookAndFeel.uninstallBorder(this.comboBox);
    }

    protected void uninstallListeners() {
        if (this.keyListener != null) {
            this.comboBox.removeKeyListener(this.keyListener);
        }
        if (this.itemListener != null) {
            this.comboBox.removeItemListener(this.itemListener);
        }
        if (this.propertyChangeListener != null) {
            this.comboBox.removePropertyChangeListener(this.propertyChangeListener);
        }
        if (this.focusListener != null) {
            this.comboBox.removeFocusListener(this.focusListener);
        }
        if (this.popupMouseListener != null) {
            this.comboBox.removeMouseListener(this.popupMouseListener);
        }
        if (this.popupMouseMotionListener != null) {
            this.comboBox.removeMouseMotionListener(this.popupMouseMotionListener);
        }
        if (this.popupKeyListener != null) {
            this.comboBox.removeKeyListener(this.popupKeyListener);
        }
        if (this.comboBox.getModel() != null && this.listDataListener != null) {
            this.comboBox.getModel().removeListDataListener(this.listDataListener);
        }
    }

    protected ComboPopup createPopup() {
        return new BasicComboPopup(this.comboBox);
    }

    protected KeyListener createKeyListener() {
        return getHandler();
    }

    protected FocusListener createFocusListener() {
        return getHandler();
    }

    protected ListDataListener createListDataListener() {
        return getHandler();
    }

    protected ItemListener createItemListener() {
        return null;
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    protected LayoutManager createLayoutManager() {
        return getHandler();
    }

    protected ListCellRenderer createRenderer() {
        return new BasicComboBoxRenderer.UIResource();
    }

    protected ComboBoxEditor createEditor() {
        return new BasicComboBoxEditor.UIResource();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxUI$KeyHandler.class */
    public class KeyHandler extends KeyAdapter {
        public KeyHandler() {
        }

        @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
        public void keyPressed(KeyEvent keyEvent) {
            BasicComboBoxUI.this.getHandler().keyPressed(keyEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxUI$FocusHandler.class */
    public class FocusHandler implements FocusListener {
        public FocusHandler() {
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            BasicComboBoxUI.this.getHandler().focusGained(focusEvent);
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            BasicComboBoxUI.this.getHandler().focusLost(focusEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxUI$ListDataHandler.class */
    public class ListDataHandler implements ListDataListener {
        public ListDataHandler() {
        }

        @Override // javax.swing.event.ListDataListener
        public void contentsChanged(ListDataEvent listDataEvent) {
            BasicComboBoxUI.this.getHandler().contentsChanged(listDataEvent);
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalAdded(ListDataEvent listDataEvent) {
            BasicComboBoxUI.this.getHandler().intervalAdded(listDataEvent);
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalRemoved(ListDataEvent listDataEvent) {
            BasicComboBoxUI.this.getHandler().intervalRemoved(listDataEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxUI$ItemHandler.class */
    public class ItemHandler implements ItemListener {
        public ItemHandler() {
        }

        @Override // java.awt.event.ItemListener
        public void itemStateChanged(ItemEvent itemEvent) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxUI$PropertyChangeHandler.class */
    public class PropertyChangeHandler implements PropertyChangeListener {
        public PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicComboBoxUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateToolTipTextForChildren() {
        Component[] components = this.comboBox.getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof JComponent) {
                ((JComponent) components[i2]).setToolTipText(this.comboBox.getToolTipText());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxUI$ComboBoxLayoutManager.class */
    public class ComboBoxLayoutManager implements LayoutManager {
        public ComboBoxLayoutManager() {
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return BasicComboBoxUI.this.getHandler().preferredLayoutSize(container);
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            return BasicComboBoxUI.this.getHandler().minimumLayoutSize(container);
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            BasicComboBoxUI.this.getHandler().layoutContainer(container);
        }
    }

    protected void installComponents() {
        this.arrowButton = createArrowButton();
        if (this.arrowButton != null) {
            this.comboBox.add(this.arrowButton);
            configureArrowButton();
        }
        if (this.comboBox.isEditable()) {
            addEditor();
        }
        this.comboBox.add(this.currentValuePane);
    }

    protected void uninstallComponents() {
        if (this.arrowButton != null) {
            unconfigureArrowButton();
        }
        if (this.editor != null) {
            unconfigureEditor();
        }
        this.comboBox.removeAll();
        this.arrowButton = null;
    }

    public void addEditor() {
        removeEditor();
        this.editor = this.comboBox.getEditor().getEditorComponent();
        if (this.editor != null) {
            configureEditor();
            this.comboBox.add(this.editor);
            if (this.comboBox.isFocusOwner()) {
                this.editor.requestFocusInWindow();
            }
        }
    }

    public void removeEditor() {
        if (this.editor != null) {
            unconfigureEditor();
            this.comboBox.remove(this.editor);
            this.editor = null;
        }
    }

    protected void configureEditor() {
        this.editor.setEnabled(this.comboBox.isEnabled());
        this.editor.setFocusable(this.comboBox.isFocusable());
        this.editor.setFont(this.comboBox.getFont());
        if (this.focusListener != null) {
            this.editor.addFocusListener(this.focusListener);
        }
        this.editor.addFocusListener(getHandler());
        this.comboBox.getEditor().addActionListener(getHandler());
        if (this.editor instanceof JComponent) {
            ((JComponent) this.editor).putClientProperty("doNotCancelPopup", HIDE_POPUP_KEY);
            ((JComponent) this.editor).setInheritsPopupMenu(true);
        }
        this.comboBox.configureEditor(this.comboBox.getEditor(), this.comboBox.getSelectedItem());
        this.editor.addPropertyChangeListener(this.propertyChangeListener);
    }

    protected void unconfigureEditor() {
        if (this.focusListener != null) {
            this.editor.removeFocusListener(this.focusListener);
        }
        this.editor.removePropertyChangeListener(this.propertyChangeListener);
        this.editor.removeFocusListener(getHandler());
        this.comboBox.getEditor().removeActionListener(getHandler());
    }

    public void configureArrowButton() {
        if (this.arrowButton != null) {
            this.arrowButton.setEnabled(this.comboBox.isEnabled());
            this.arrowButton.setFocusable(this.comboBox.isFocusable());
            this.arrowButton.setRequestFocusEnabled(false);
            this.arrowButton.addMouseListener(this.popup.getMouseListener());
            this.arrowButton.addMouseMotionListener(this.popup.getMouseMotionListener());
            this.arrowButton.resetKeyboardActions();
            this.arrowButton.putClientProperty("doNotCancelPopup", HIDE_POPUP_KEY);
            this.arrowButton.setInheritsPopupMenu(true);
        }
    }

    public void unconfigureArrowButton() {
        if (this.arrowButton != null) {
            this.arrowButton.removeMouseListener(this.popup.getMouseListener());
            this.arrowButton.removeMouseMotionListener(this.popup.getMouseMotionListener());
        }
    }

    protected JButton createArrowButton() {
        BasicArrowButton basicArrowButton = new BasicArrowButton(5, UIManager.getColor("ComboBox.buttonBackground"), UIManager.getColor("ComboBox.buttonShadow"), UIManager.getColor("ComboBox.buttonDarkShadow"), UIManager.getColor("ComboBox.buttonHighlight"));
        basicArrowButton.setName("ComboBox.arrowButton");
        return basicArrowButton;
    }

    @Override // javax.swing.plaf.ComboBoxUI
    public boolean isPopupVisible(JComboBox jComboBox) {
        return this.popup.isVisible();
    }

    @Override // javax.swing.plaf.ComboBoxUI
    public void setPopupVisible(JComboBox jComboBox, boolean z2) {
        if (z2) {
            this.popup.show();
        } else {
            this.popup.hide();
        }
    }

    @Override // javax.swing.plaf.ComboBoxUI
    public boolean isFocusTraversable(JComboBox jComboBox) {
        return !this.comboBox.isEditable();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        this.hasFocus = this.comboBox.hasFocus();
        if (!this.comboBox.isEditable()) {
            Rectangle rectangleRectangleForCurrentValue = rectangleForCurrentValue();
            paintCurrentValueBackground(graphics, rectangleRectangleForCurrentValue, this.hasFocus);
            paintCurrentValue(graphics, rectangleRectangleForCurrentValue, this.hasFocus);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return getMinimumSize(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        if (!this.isMinimumSizeDirty) {
            return new Dimension(this.cachedMinimumSize);
        }
        Dimension displaySize = getDisplaySize();
        Insets insets = getInsets();
        int i2 = this.squareButton ? displaySize.height : this.arrowButton.getPreferredSize().width;
        displaySize.height += insets.top + insets.bottom;
        displaySize.width += insets.left + insets.right + i2;
        this.cachedMinimumSize.setSize(displaySize.width, displaySize.height);
        this.isMinimumSizeDirty = false;
        return new Dimension(displaySize);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) throws IllegalArgumentException {
        JLabel jLabel;
        String text;
        super.getBaseline(jComponent, i2, i3);
        int baseline = -1;
        getDisplaySize();
        if (this.sameBaseline) {
            Insets insets = jComponent.getInsets();
            int iMax = Math.max((i3 - insets.top) - insets.bottom, 0);
            if (!this.comboBox.isEditable()) {
                ListCellRenderer renderer = this.comboBox.getRenderer();
                if (renderer == null) {
                    renderer = new DefaultListCellRenderer();
                }
                Object elementAt = null;
                Object prototypeDisplayValue = this.comboBox.getPrototypeDisplayValue();
                if (prototypeDisplayValue != null) {
                    elementAt = prototypeDisplayValue;
                } else if (this.comboBox.getModel().getSize() > 0) {
                    elementAt = this.comboBox.getModel().getElementAt(0);
                }
                Component listCellRendererComponent = renderer.getListCellRendererComponent(this.listBox, elementAt, -1, false, false);
                if ((listCellRendererComponent instanceof JLabel) && ((text = (jLabel = (JLabel) listCellRendererComponent).getText()) == null || text.isEmpty())) {
                    jLabel.setText(" ");
                }
                if (listCellRendererComponent instanceof JComponent) {
                    listCellRendererComponent.setFont(this.comboBox.getFont());
                }
                baseline = listCellRendererComponent.getBaseline(i2, iMax);
            } else {
                baseline = this.editor.getBaseline(i2, iMax);
            }
            if (baseline > 0) {
                baseline += insets.top;
            }
        }
        return baseline;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        getDisplaySize();
        if (this.comboBox.isEditable()) {
            return this.editor.getBaselineResizeBehavior();
        }
        if (this.sameBaseline) {
            ListCellRenderer renderer = this.comboBox.getRenderer();
            if (renderer == null) {
                renderer = new DefaultListCellRenderer();
            }
            Object elementAt = null;
            Object prototypeDisplayValue = this.comboBox.getPrototypeDisplayValue();
            if (prototypeDisplayValue != null) {
                elementAt = prototypeDisplayValue;
            } else if (this.comboBox.getModel().getSize() > 0) {
                elementAt = this.comboBox.getModel().getElementAt(0);
            }
            if (elementAt != null) {
                return renderer.getListCellRendererComponent(this.listBox, elementAt, -1, false, false).getBaselineResizeBehavior();
            }
        }
        return Component.BaselineResizeBehavior.OTHER;
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getAccessibleChildrenCount(JComponent jComponent) {
        if (this.comboBox.isEditable()) {
            return 2;
        }
        return 1;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Accessible getAccessibleChild(JComponent jComponent, int i2) {
        switch (i2) {
            case 0:
                if (this.popup instanceof Accessible) {
                    ((Accessible) this.popup).getAccessibleContext().setAccessibleParent(this.comboBox);
                    return (Accessible) this.popup;
                }
                return null;
            case 1:
                if (this.comboBox.isEditable() && (this.editor instanceof Accessible)) {
                    ((Accessible) this.editor).getAccessibleContext().setAccessibleParent(this.comboBox);
                    return (Accessible) this.editor;
                }
                return null;
            default:
                return null;
        }
    }

    protected boolean isNavigationKey(int i2) {
        return i2 == 38 || i2 == 40 || i2 == 224 || i2 == 225;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNavigationKey(int i2, int i3) {
        InputMap inputMap = this.comboBox.getInputMap(1);
        KeyStroke keyStroke = KeyStroke.getKeyStroke(i2, i3);
        if (inputMap != null && inputMap.get(keyStroke) != null) {
            return true;
        }
        return false;
    }

    protected void selectNextPossibleValue() {
        int selectedIndex;
        if (this.comboBox.isPopupVisible()) {
            selectedIndex = this.listBox.getSelectedIndex();
        } else {
            selectedIndex = this.comboBox.getSelectedIndex();
        }
        if (selectedIndex < this.comboBox.getModel().getSize() - 1) {
            this.listBox.setSelectedIndex(selectedIndex + 1);
            this.listBox.ensureIndexIsVisible(selectedIndex + 1);
            if (!this.isTableCellEditor && (!UIManager.getBoolean("ComboBox.noActionOnKeyNavigation") || !this.comboBox.isPopupVisible())) {
                this.comboBox.setSelectedIndex(selectedIndex + 1);
            }
            this.comboBox.repaint();
        }
    }

    protected void selectPreviousPossibleValue() {
        int selectedIndex;
        if (this.comboBox.isPopupVisible()) {
            selectedIndex = this.listBox.getSelectedIndex();
        } else {
            selectedIndex = this.comboBox.getSelectedIndex();
        }
        if (selectedIndex > 0) {
            this.listBox.setSelectedIndex(selectedIndex - 1);
            this.listBox.ensureIndexIsVisible(selectedIndex - 1);
            if (!this.isTableCellEditor && (!UIManager.getBoolean("ComboBox.noActionOnKeyNavigation") || !this.comboBox.isPopupVisible())) {
                this.comboBox.setSelectedIndex(selectedIndex - 1);
            }
            this.comboBox.repaint();
        }
    }

    protected void toggleOpenClose() {
        setPopupVisible(this.comboBox, !isPopupVisible(this.comboBox));
    }

    protected Rectangle rectangleForCurrentValue() {
        int width = this.comboBox.getWidth();
        int height = this.comboBox.getHeight();
        Insets insets = getInsets();
        int width2 = height - (insets.top + insets.bottom);
        if (this.arrowButton != null) {
            width2 = this.arrowButton.getWidth();
        }
        if (BasicGraphicsUtils.isLeftToRight(this.comboBox)) {
            return new Rectangle(insets.left, insets.top, width - ((insets.left + insets.right) + width2), height - (insets.top + insets.bottom));
        }
        return new Rectangle(insets.left + width2, insets.top, width - ((insets.left + insets.right) + width2), height - (insets.top + insets.bottom));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Insets getInsets() {
        return this.comboBox.getInsets();
    }

    public void paintCurrentValue(Graphics graphics, Rectangle rectangle, boolean z2) {
        Component listCellRendererComponent;
        ListCellRenderer renderer = this.comboBox.getRenderer();
        if (z2 && !isPopupVisible(this.comboBox)) {
            listCellRendererComponent = renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, true, false);
        } else {
            listCellRendererComponent = renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);
            listCellRendererComponent.setBackground(UIManager.getColor("ComboBox.background"));
        }
        listCellRendererComponent.setFont(this.comboBox.getFont());
        if (z2 && !isPopupVisible(this.comboBox)) {
            listCellRendererComponent.setForeground(this.listBox.getSelectionForeground());
            listCellRendererComponent.setBackground(this.listBox.getSelectionBackground());
        } else if (this.comboBox.isEnabled()) {
            listCellRendererComponent.setForeground(this.comboBox.getForeground());
            listCellRendererComponent.setBackground(this.comboBox.getBackground());
        } else {
            listCellRendererComponent.setForeground(DefaultLookup.getColor(this.comboBox, this, "ComboBox.disabledForeground", null));
            listCellRendererComponent.setBackground(DefaultLookup.getColor(this.comboBox, this, "ComboBox.disabledBackground", null));
        }
        boolean z3 = false;
        if (listCellRendererComponent instanceof JPanel) {
            z3 = true;
        }
        int i2 = rectangle.f12372x;
        int i3 = rectangle.f12373y;
        int i4 = rectangle.width;
        int i5 = rectangle.height;
        if (this.padding != null) {
            i2 = rectangle.f12372x + this.padding.left;
            i3 = rectangle.f12373y + this.padding.top;
            i4 = rectangle.width - (this.padding.left + this.padding.right);
            i5 = rectangle.height - (this.padding.top + this.padding.bottom);
        }
        this.currentValuePane.paintComponent(graphics, listCellRendererComponent, this.comboBox, i2, i3, i4, i5, z3);
    }

    public void paintCurrentValueBackground(Graphics graphics, Rectangle rectangle, boolean z2) {
        Color color = graphics.getColor();
        if (this.comboBox.isEnabled()) {
            graphics.setColor(DefaultLookup.getColor(this.comboBox, this, "ComboBox.background", null));
        } else {
            graphics.setColor(DefaultLookup.getColor(this.comboBox, this, "ComboBox.disabledBackground", null));
        }
        graphics.fillRect(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
        graphics.setColor(color);
    }

    void repaintCurrentValue() {
        Rectangle rectangleRectangleForCurrentValue = rectangleForCurrentValue();
        this.comboBox.repaint(rectangleRectangleForCurrentValue.f12372x, rectangleRectangleForCurrentValue.f12373y, rectangleRectangleForCurrentValue.width, rectangleRectangleForCurrentValue.height);
    }

    protected Dimension getDefaultSize() {
        Dimension sizeForComponent = getSizeForComponent(getDefaultListCellRenderer().getListCellRendererComponent(this.listBox, " ", -1, false, false));
        return new Dimension(sizeForComponent.width, sizeForComponent.height);
    }

    protected Dimension getDisplaySize() {
        if (!this.isDisplaySizeDirty) {
            return new Dimension(this.cachedDisplaySize);
        }
        Dimension dimension = new Dimension();
        ListCellRenderer renderer = this.comboBox.getRenderer();
        if (renderer == null) {
            renderer = new DefaultListCellRenderer();
        }
        this.sameBaseline = true;
        Object prototypeDisplayValue = this.comboBox.getPrototypeDisplayValue();
        if (prototypeDisplayValue != null) {
            dimension = getSizeForComponent(renderer.getListCellRendererComponent(this.listBox, prototypeDisplayValue, -1, false, false));
        } else {
            ComboBoxModel model = this.comboBox.getModel();
            int size = model.getSize();
            int i2 = -1;
            if (size > 0) {
                for (int i3 = 0; i3 < size; i3++) {
                    E elementAt = model.getElementAt(i3);
                    Component listCellRendererComponent = renderer.getListCellRendererComponent(this.listBox, elementAt, -1, false, false);
                    Dimension sizeForComponent = getSizeForComponent(listCellRendererComponent);
                    if (this.sameBaseline && elementAt != 0 && (!(elementAt instanceof String) || !"".equals(elementAt))) {
                        int baseline = listCellRendererComponent.getBaseline(sizeForComponent.width, sizeForComponent.height);
                        if (baseline == -1) {
                            this.sameBaseline = false;
                        } else if (i2 == -1) {
                            i2 = baseline;
                        } else if (i2 != baseline) {
                            this.sameBaseline = false;
                        }
                    }
                    dimension.width = Math.max(dimension.width, sizeForComponent.width);
                    dimension.height = Math.max(dimension.height, sizeForComponent.height);
                }
            } else {
                dimension = getDefaultSize();
                if (this.comboBox.isEditable()) {
                    dimension.width = 100;
                }
            }
        }
        if (this.comboBox.isEditable()) {
            Dimension preferredSize = this.editor.getPreferredSize();
            dimension.width = Math.max(dimension.width, preferredSize.width);
            dimension.height = Math.max(dimension.height, preferredSize.height);
        }
        if (this.padding != null) {
            dimension.width += this.padding.left + this.padding.right;
            dimension.height += this.padding.top + this.padding.bottom;
        }
        this.cachedDisplaySize.setSize(dimension.width, dimension.height);
        this.isDisplaySizeDirty = false;
        return dimension;
    }

    protected Dimension getSizeForComponent(Component component) {
        this.currentValuePane.add(component);
        component.setFont(this.comboBox.getFont());
        Dimension preferredSize = component.getPreferredSize();
        this.currentValuePane.remove(component);
        return preferredSize;
    }

    protected void installKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.comboBox, 1, getInputMap(1));
        LazyActionMap.installLazyActionMap(this.comboBox, BasicComboBoxUI.class, "ComboBox.actionMap");
    }

    InputMap getInputMap(int i2) {
        if (i2 == 1) {
            return (InputMap) DefaultLookup.get(this.comboBox, this, "ComboBox.ancestorInputMap");
        }
        return null;
    }

    boolean isTableCellEditor() {
        return this.isTableCellEditor;
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.comboBox, 1, null);
        SwingUtilities.replaceUIActionMap(this.comboBox, null);
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String HIDE = "hidePopup";
        private static final String DOWN = "selectNext";
        private static final String DOWN_2 = "selectNext2";
        private static final String TOGGLE = "togglePopup";
        private static final String TOGGLE_2 = "spacePopup";
        private static final String UP = "selectPrevious";
        private static final String UP_2 = "selectPrevious2";
        private static final String ENTER = "enterPressed";
        private static final String PAGE_DOWN = "pageDownPassThrough";
        private static final String PAGE_UP = "pageUpPassThrough";
        private static final String HOME = "homePassThrough";
        private static final String END = "endPassThrough";

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            Object obj;
            Action action;
            Object selectedValue;
            String name = getName();
            JComboBox jComboBox = (JComboBox) actionEvent.getSource();
            BasicComboBoxUI basicComboBoxUI = (BasicComboBoxUI) BasicLookAndFeel.getUIOfType(jComboBox.getUI(), BasicComboBoxUI.class);
            if (name == HIDE) {
                jComboBox.firePopupMenuCanceled();
                jComboBox.setPopupVisible(false);
                return;
            }
            if (name == PAGE_DOWN || name == PAGE_UP || name == HOME || name == END) {
                int nextIndex = getNextIndex(jComboBox, name);
                if (nextIndex >= 0 && nextIndex < jComboBox.getItemCount()) {
                    if (UIManager.getBoolean("ComboBox.noActionOnKeyNavigation") && jComboBox.isPopupVisible()) {
                        basicComboBoxUI.listBox.setSelectedIndex(nextIndex);
                        basicComboBoxUI.listBox.ensureIndexIsVisible(nextIndex);
                        jComboBox.repaint();
                        return;
                    }
                    jComboBox.setSelectedIndex(nextIndex);
                    return;
                }
                return;
            }
            if (name == DOWN) {
                if (jComboBox.isShowing()) {
                    if (jComboBox.isPopupVisible()) {
                        if (basicComboBoxUI != null) {
                            basicComboBoxUI.selectNextPossibleValue();
                            return;
                        }
                        return;
                    }
                    jComboBox.setPopupVisible(true);
                    return;
                }
                return;
            }
            if (name == DOWN_2) {
                if (jComboBox.isShowing()) {
                    if ((jComboBox.isEditable() || (basicComboBoxUI != null && basicComboBoxUI.isTableCellEditor())) && !jComboBox.isPopupVisible()) {
                        jComboBox.setPopupVisible(true);
                        return;
                    } else {
                        if (basicComboBoxUI != null) {
                            basicComboBoxUI.selectNextPossibleValue();
                            return;
                        }
                        return;
                    }
                }
                return;
            }
            if (name == TOGGLE || name == TOGGLE_2) {
                if (basicComboBoxUI != null) {
                    if (name == TOGGLE || !jComboBox.isEditable()) {
                        if (basicComboBoxUI.isTableCellEditor()) {
                            jComboBox.setSelectedIndex(basicComboBoxUI.popup.getList().getSelectedIndex());
                            return;
                        } else {
                            jComboBox.setPopupVisible(!jComboBox.isPopupVisible());
                            return;
                        }
                    }
                    return;
                }
                return;
            }
            if (name == UP) {
                if (basicComboBoxUI != null) {
                    if (basicComboBoxUI.isPopupVisible(jComboBox)) {
                        basicComboBoxUI.selectPreviousPossibleValue();
                        return;
                    } else {
                        if (DefaultLookup.getBoolean(jComboBox, basicComboBoxUI, "ComboBox.showPopupOnNavigation", false)) {
                            basicComboBoxUI.setPopupVisible(jComboBox, true);
                            return;
                        }
                        return;
                    }
                }
                return;
            }
            if (name == UP_2) {
                if (jComboBox.isShowing() && basicComboBoxUI != null) {
                    if (jComboBox.isEditable() && !jComboBox.isPopupVisible()) {
                        jComboBox.setPopupVisible(true);
                        return;
                    } else {
                        basicComboBoxUI.selectPreviousPossibleValue();
                        return;
                    }
                }
                return;
            }
            if (name == ENTER) {
                if (!jComboBox.isPopupVisible()) {
                    if (basicComboBoxUI.isTableCellEditor && !jComboBox.isEditable()) {
                        jComboBox.setSelectedItem(jComboBox.getSelectedItem());
                    }
                    JRootPane rootPane = SwingUtilities.getRootPane(jComboBox);
                    if (rootPane != null) {
                        InputMap inputMap = rootPane.getInputMap(2);
                        ActionMap actionMap = rootPane.getActionMap();
                        if (inputMap != null && actionMap != null && (obj = inputMap.get(KeyStroke.getKeyStroke(10, 0))) != null && (action = actionMap.get(obj)) != null) {
                            action.actionPerformed(new ActionEvent(rootPane, actionEvent.getID(), actionEvent.getActionCommand(), actionEvent.getWhen(), actionEvent.getModifiers()));
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (UIManager.getBoolean("ComboBox.noActionOnKeyNavigation")) {
                    Object selectedValue2 = basicComboBoxUI.popup.getList().getSelectedValue();
                    if (selectedValue2 != null) {
                        jComboBox.getEditor().setItem(selectedValue2);
                        jComboBox.setSelectedItem(selectedValue2);
                    }
                    jComboBox.setPopupVisible(false);
                    return;
                }
                boolean z2 = UIManager.getBoolean("ComboBox.isEnterSelectablePopup");
                if ((!jComboBox.isEditable() || z2 || basicComboBoxUI.isTableCellEditor) && (selectedValue = basicComboBoxUI.popup.getList().getSelectedValue()) != null) {
                    jComboBox.getEditor().setItem(selectedValue);
                    jComboBox.setSelectedItem(selectedValue);
                }
                jComboBox.setPopupVisible(false);
            }
        }

        private int getNextIndex(JComboBox jComboBox, String str) {
            int maximumRowCount = jComboBox.getMaximumRowCount();
            int selectedIndex = jComboBox.getSelectedIndex();
            if (UIManager.getBoolean("ComboBox.noActionOnKeyNavigation") && (jComboBox.getUI() instanceof BasicComboBoxUI)) {
                selectedIndex = ((BasicComboBoxUI) jComboBox.getUI()).listBox.getSelectedIndex();
            }
            if (str == PAGE_UP) {
                int i2 = selectedIndex - maximumRowCount;
                if (i2 < 0) {
                    return 0;
                }
                return i2;
            }
            if (str == PAGE_DOWN) {
                int i3 = selectedIndex + maximumRowCount;
                int itemCount = jComboBox.getItemCount();
                return i3 < itemCount ? i3 : itemCount - 1;
            }
            if (str == HOME) {
                return 0;
            }
            if (str == END) {
                return jComboBox.getItemCount() - 1;
            }
            return jComboBox.getSelectedIndex();
        }

        @Override // sun.swing.UIAction
        public boolean isEnabled(Object obj) {
            if (getName() == HIDE) {
                return obj != null && ((JComboBox) obj).isPopupVisible();
            }
            return true;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxUI$Handler.class */
    private class Handler implements ActionListener, FocusListener, KeyListener, LayoutManager, ListDataListener, PropertyChangeListener {
        private Handler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyChangeEvent.getSource() == BasicComboBoxUI.this.editor) {
                if ("border".equals(propertyName)) {
                    BasicComboBoxUI.this.isMinimumSizeDirty = true;
                    BasicComboBoxUI.this.isDisplaySizeDirty = true;
                    BasicComboBoxUI.this.comboBox.revalidate();
                    return;
                }
                return;
            }
            JComboBox jComboBox = (JComboBox) propertyChangeEvent.getSource();
            if (propertyName == "model") {
                ComboBoxModel comboBoxModel = (ComboBoxModel) propertyChangeEvent.getNewValue();
                ComboBoxModel comboBoxModel2 = (ComboBoxModel) propertyChangeEvent.getOldValue();
                if (comboBoxModel2 != null && BasicComboBoxUI.this.listDataListener != null) {
                    comboBoxModel2.removeListDataListener(BasicComboBoxUI.this.listDataListener);
                }
                if (comboBoxModel != null && BasicComboBoxUI.this.listDataListener != null) {
                    comboBoxModel.addListDataListener(BasicComboBoxUI.this.listDataListener);
                }
                if (BasicComboBoxUI.this.editor != null) {
                    jComboBox.configureEditor(jComboBox.getEditor(), jComboBox.getSelectedItem());
                }
                BasicComboBoxUI.this.isMinimumSizeDirty = true;
                BasicComboBoxUI.this.isDisplaySizeDirty = true;
                jComboBox.revalidate();
                jComboBox.repaint();
                return;
            }
            if (propertyName == "editor" && jComboBox.isEditable()) {
                BasicComboBoxUI.this.addEditor();
                jComboBox.revalidate();
                return;
            }
            if (propertyName == JTree.EDITABLE_PROPERTY) {
                if (jComboBox.isEditable()) {
                    jComboBox.setRequestFocusEnabled(false);
                    BasicComboBoxUI.this.addEditor();
                } else {
                    jComboBox.setRequestFocusEnabled(true);
                    BasicComboBoxUI.this.removeEditor();
                }
                BasicComboBoxUI.this.updateToolTipTextForChildren();
                jComboBox.revalidate();
                return;
            }
            if (propertyName == Enabled.NAME) {
                boolean zIsEnabled = jComboBox.isEnabled();
                if (BasicComboBoxUI.this.editor != null) {
                    BasicComboBoxUI.this.editor.setEnabled(zIsEnabled);
                }
                if (BasicComboBoxUI.this.arrowButton != null) {
                    BasicComboBoxUI.this.arrowButton.setEnabled(zIsEnabled);
                }
                jComboBox.repaint();
                return;
            }
            if (propertyName == "focusable") {
                boolean zIsFocusable = jComboBox.isFocusable();
                if (BasicComboBoxUI.this.editor != null) {
                    BasicComboBoxUI.this.editor.setFocusable(zIsFocusable);
                }
                if (BasicComboBoxUI.this.arrowButton != null) {
                    BasicComboBoxUI.this.arrowButton.setFocusable(zIsFocusable);
                }
                jComboBox.repaint();
                return;
            }
            if (propertyName == "maximumRowCount") {
                if (BasicComboBoxUI.this.isPopupVisible(jComboBox)) {
                    BasicComboBoxUI.this.setPopupVisible(jComboBox, false);
                    BasicComboBoxUI.this.setPopupVisible(jComboBox, true);
                    return;
                }
                return;
            }
            if (propertyName == "font") {
                BasicComboBoxUI.this.listBox.setFont(jComboBox.getFont());
                if (BasicComboBoxUI.this.editor != null) {
                    BasicComboBoxUI.this.editor.setFont(jComboBox.getFont());
                }
                BasicComboBoxUI.this.isMinimumSizeDirty = true;
                BasicComboBoxUI.this.isDisplaySizeDirty = true;
                jComboBox.validate();
                return;
            }
            if (propertyName == JComponent.TOOL_TIP_TEXT_KEY) {
                BasicComboBoxUI.this.updateToolTipTextForChildren();
                return;
            }
            if (propertyName == BasicComboBoxUI.IS_TABLE_CELL_EDITOR) {
                Boolean bool = (Boolean) propertyChangeEvent.getNewValue();
                BasicComboBoxUI.this.isTableCellEditor = bool.equals(Boolean.TRUE);
            } else if (propertyName == "prototypeDisplayValue") {
                BasicComboBoxUI.this.isMinimumSizeDirty = true;
                BasicComboBoxUI.this.isDisplaySizeDirty = true;
                jComboBox.revalidate();
            } else if (propertyName == "renderer") {
                BasicComboBoxUI.this.isMinimumSizeDirty = true;
                BasicComboBoxUI.this.isDisplaySizeDirty = true;
                jComboBox.revalidate();
            }
        }

        /* JADX WARN: Failed to check method for inline after forced processjavax.swing.plaf.basic.BasicComboBoxUI.access$602(javax.swing.plaf.basic.BasicComboBoxUI, long):long */
        /* JADX WARN: Failed to check method for inline after forced processjavax.swing.plaf.basic.BasicComboBoxUI.access$702(javax.swing.plaf.basic.BasicComboBoxUI, long):long */
        @Override // java.awt.event.KeyListener
        public void keyPressed(KeyEvent keyEvent) {
            if (BasicComboBoxUI.this.isNavigationKey(keyEvent.getKeyCode(), keyEvent.getModifiers())) {
                BasicComboBoxUI.access$602(BasicComboBoxUI.this, 0L);
                return;
            }
            if (BasicComboBoxUI.this.comboBox.isEnabled() && BasicComboBoxUI.this.comboBox.getModel().getSize() != 0 && isTypeAheadKey(keyEvent) && keyEvent.getKeyChar() != 65535) {
                BasicComboBoxUI.access$702(BasicComboBoxUI.this, keyEvent.getWhen());
                if (BasicComboBoxUI.this.comboBox.selectWithKeyChar(keyEvent.getKeyChar())) {
                    keyEvent.consume();
                }
            }
        }

        @Override // java.awt.event.KeyListener
        public void keyTyped(KeyEvent keyEvent) {
        }

        @Override // java.awt.event.KeyListener
        public void keyReleased(KeyEvent keyEvent) {
        }

        private boolean isTypeAheadKey(KeyEvent keyEvent) {
            return (keyEvent.isAltDown() || BasicGraphicsUtils.isMenuShortcutKeyDown(keyEvent)) ? false : true;
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            ComboBoxEditor editor = BasicComboBoxUI.this.comboBox.getEditor();
            if (editor != null && focusEvent.getSource() == editor.getEditorComponent()) {
                return;
            }
            BasicComboBoxUI.this.hasFocus = true;
            BasicComboBoxUI.this.comboBox.repaint();
            if (BasicComboBoxUI.this.comboBox.isEditable() && BasicComboBoxUI.this.editor != null) {
                BasicComboBoxUI.this.editor.requestFocus();
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            ComboBoxEditor editor = BasicComboBoxUI.this.comboBox.getEditor();
            if (editor != null && focusEvent.getSource() == editor.getEditorComponent()) {
                Object item = editor.getItem();
                Object selectedItem = BasicComboBoxUI.this.comboBox.getSelectedItem();
                if (!focusEvent.isTemporary() && item != null) {
                    if (!item.equals(selectedItem == null ? "" : selectedItem)) {
                        BasicComboBoxUI.this.comboBox.actionPerformed(new ActionEvent(editor, 0, "", EventQueue.getMostRecentEventTime(), 0));
                    }
                }
            }
            BasicComboBoxUI.this.hasFocus = false;
            if (!focusEvent.isTemporary()) {
                BasicComboBoxUI.this.setPopupVisible(BasicComboBoxUI.this.comboBox, false);
            }
            BasicComboBoxUI.this.comboBox.repaint();
        }

        @Override // javax.swing.event.ListDataListener
        public void contentsChanged(ListDataEvent listDataEvent) {
            if (listDataEvent.getIndex0() != -1 || listDataEvent.getIndex1() != -1) {
                BasicComboBoxUI.this.isMinimumSizeDirty = true;
                BasicComboBoxUI.this.comboBox.revalidate();
            }
            if (BasicComboBoxUI.this.comboBox.isEditable() && BasicComboBoxUI.this.editor != null) {
                BasicComboBoxUI.this.comboBox.configureEditor(BasicComboBoxUI.this.comboBox.getEditor(), BasicComboBoxUI.this.comboBox.getSelectedItem());
            }
            BasicComboBoxUI.this.isDisplaySizeDirty = true;
            BasicComboBoxUI.this.comboBox.repaint();
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalAdded(ListDataEvent listDataEvent) {
            contentsChanged(listDataEvent);
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalRemoved(ListDataEvent listDataEvent) {
            contentsChanged(listDataEvent);
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return container.getPreferredSize();
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            return container.getMinimumSize();
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            JComboBox jComboBox = (JComboBox) container;
            int width = jComboBox.getWidth();
            int height = jComboBox.getHeight();
            Insets insets = BasicComboBoxUI.this.getInsets();
            int i2 = height - (insets.top + insets.bottom);
            int i3 = i2;
            if (BasicComboBoxUI.this.arrowButton != null) {
                Insets insets2 = BasicComboBoxUI.this.arrowButton.getInsets();
                i3 = BasicComboBoxUI.this.squareButton ? i2 : BasicComboBoxUI.this.arrowButton.getPreferredSize().width + insets2.left + insets2.right;
            }
            if (BasicComboBoxUI.this.arrowButton != null) {
                if (BasicGraphicsUtils.isLeftToRight(jComboBox)) {
                    BasicComboBoxUI.this.arrowButton.setBounds(width - (insets.right + i3), insets.top, i3, i2);
                } else {
                    BasicComboBoxUI.this.arrowButton.setBounds(insets.left, insets.top, i3, i2);
                }
            }
            if (BasicComboBoxUI.this.editor != null) {
                BasicComboBoxUI.this.editor.setBounds(BasicComboBoxUI.this.rectangleForCurrentValue());
            }
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            Action action;
            Object item = BasicComboBoxUI.this.comboBox.getEditor().getItem();
            if (item != null) {
                if (!BasicComboBoxUI.this.comboBox.isPopupVisible() && !item.equals(BasicComboBoxUI.this.comboBox.getSelectedItem())) {
                    BasicComboBoxUI.this.comboBox.setSelectedItem(BasicComboBoxUI.this.comboBox.getEditor().getItem());
                }
                ActionMap actionMap = BasicComboBoxUI.this.comboBox.getActionMap();
                if (actionMap != null && (action = actionMap.get("enterPressed")) != null) {
                    action.actionPerformed(new ActionEvent(BasicComboBoxUI.this.comboBox, actionEvent.getID(), actionEvent.getActionCommand(), actionEvent.getModifiers()));
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxUI$DefaultKeySelectionManager.class */
    class DefaultKeySelectionManager implements JComboBox.KeySelectionManager, UIResource {
        private String prefix = "";
        private String typedString = "";

        DefaultKeySelectionManager() {
        }

        /* JADX WARN: Failed to check method for inline after forced processjavax.swing.plaf.basic.BasicComboBoxUI.access$602(javax.swing.plaf.basic.BasicComboBoxUI, long):long */
        @Override // javax.swing.JComboBox.KeySelectionManager
        public int selectionForKey(char c2, ComboBoxModel comboBoxModel) {
            if (BasicComboBoxUI.this.lastTime == 0) {
                this.prefix = "";
                this.typedString = "";
            }
            boolean z2 = true;
            int selectedIndex = BasicComboBoxUI.this.comboBox.getSelectedIndex();
            if (BasicComboBoxUI.this.time - BasicComboBoxUI.this.lastTime < BasicComboBoxUI.this.timeFactor) {
                this.typedString += c2;
                if (this.prefix.length() == 1 && c2 == this.prefix.charAt(0)) {
                    selectedIndex++;
                } else {
                    this.prefix = this.typedString;
                }
            } else {
                selectedIndex++;
                this.typedString = "" + c2;
                this.prefix = this.typedString;
            }
            BasicComboBoxUI.access$602(BasicComboBoxUI.this, BasicComboBoxUI.this.time);
            if (selectedIndex < 0 || selectedIndex >= comboBoxModel.getSize()) {
                z2 = false;
                selectedIndex = 0;
            }
            int nextMatch = BasicComboBoxUI.this.listBox.getNextMatch(this.prefix, selectedIndex, Position.Bias.Forward);
            if (nextMatch < 0 && z2) {
                nextMatch = BasicComboBoxUI.this.listBox.getNextMatch(this.prefix, 0, Position.Bias.Forward);
            }
            return nextMatch;
        }
    }
}
