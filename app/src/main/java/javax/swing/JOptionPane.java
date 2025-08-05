package javax.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.PrivilegedAction;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.OptionPaneUI;

/* loaded from: rt.jar:javax/swing/JOptionPane.class */
public class JOptionPane extends JComponent implements Accessible {
    private static final String uiClassID = "OptionPaneUI";
    public static final int DEFAULT_OPTION = -1;
    public static final int YES_NO_OPTION = 0;
    public static final int YES_NO_CANCEL_OPTION = 1;
    public static final int OK_CANCEL_OPTION = 2;
    public static final int YES_OPTION = 0;
    public static final int NO_OPTION = 1;
    public static final int CANCEL_OPTION = 2;
    public static final int OK_OPTION = 0;
    public static final int CLOSED_OPTION = -1;
    public static final int ERROR_MESSAGE = 0;
    public static final int INFORMATION_MESSAGE = 1;
    public static final int WARNING_MESSAGE = 2;
    public static final int QUESTION_MESSAGE = 3;
    public static final int PLAIN_MESSAGE = -1;
    public static final String ICON_PROPERTY = "icon";
    public static final String MESSAGE_PROPERTY = "message";
    public static final String VALUE_PROPERTY = "value";
    public static final String OPTIONS_PROPERTY = "options";
    public static final String INITIAL_VALUE_PROPERTY = "initialValue";
    public static final String MESSAGE_TYPE_PROPERTY = "messageType";
    public static final String OPTION_TYPE_PROPERTY = "optionType";
    public static final String SELECTION_VALUES_PROPERTY = "selectionValues";
    public static final String INITIAL_SELECTION_VALUE_PROPERTY = "initialSelectionValue";
    public static final String INPUT_VALUE_PROPERTY = "inputValue";
    public static final String WANTS_INPUT_PROPERTY = "wantsInput";
    protected transient Icon icon;
    protected transient Object message;
    protected transient Object[] options;
    protected transient Object initialValue;
    protected int messageType;
    protected int optionType;
    protected transient Object value;
    protected transient Object[] selectionValues;
    protected transient Object inputValue;
    protected transient Object initialSelectionValue;
    protected boolean wantsInput;
    public static final Object UNINITIALIZED_VALUE = "uninitializedValue";
    private static final Object sharedFrameKey = JOptionPane.class;

    public static String showInputDialog(Object obj) throws HeadlessException {
        return showInputDialog((Component) null, obj);
    }

    public static String showInputDialog(Object obj, Object obj2) {
        return showInputDialog(null, obj, obj2);
    }

    public static String showInputDialog(Component component, Object obj) throws HeadlessException {
        return showInputDialog(component, obj, UIManager.getString("OptionPane.inputDialogTitle", component), 3);
    }

    public static String showInputDialog(Component component, Object obj, Object obj2) {
        return (String) showInputDialog(component, obj, UIManager.getString("OptionPane.inputDialogTitle", component), 3, null, null, obj2);
    }

    public static String showInputDialog(Component component, Object obj, String str, int i2) throws HeadlessException {
        return (String) showInputDialog(component, obj, str, i2, null, null, null);
    }

    public static Object showInputDialog(Component component, Object obj, String str, int i2, Icon icon, Object[] objArr, Object obj2) throws HeadlessException {
        JOptionPane jOptionPane = new JOptionPane(obj, i2, 2, icon, null, null);
        jOptionPane.setWantsInput(true);
        jOptionPane.setSelectionValues(objArr);
        jOptionPane.setInitialSelectionValue(obj2);
        jOptionPane.setComponentOrientation((component == null ? getRootFrame() : component).getComponentOrientation());
        JDialog jDialogCreateDialog = jOptionPane.createDialog(component, str, styleFromMessageType(i2));
        jOptionPane.selectInitialValue();
        jDialogCreateDialog.show();
        jDialogCreateDialog.dispose();
        Object inputValue = jOptionPane.getInputValue();
        if (inputValue == UNINITIALIZED_VALUE) {
            return null;
        }
        return inputValue;
    }

    public static void showMessageDialog(Component component, Object obj) throws HeadlessException {
        showMessageDialog(component, obj, UIManager.getString("OptionPane.messageDialogTitle", component), 1);
    }

    public static void showMessageDialog(Component component, Object obj, String str, int i2) throws HeadlessException {
        showMessageDialog(component, obj, str, i2, null);
    }

    public static void showMessageDialog(Component component, Object obj, String str, int i2, Icon icon) throws HeadlessException {
        showOptionDialog(component, obj, str, -1, i2, icon, null, null);
    }

    public static int showConfirmDialog(Component component, Object obj) throws HeadlessException {
        return showConfirmDialog(component, obj, UIManager.getString("OptionPane.titleText"), 1);
    }

    public static int showConfirmDialog(Component component, Object obj, String str, int i2) throws HeadlessException {
        return showConfirmDialog(component, obj, str, i2, 3);
    }

    public static int showConfirmDialog(Component component, Object obj, String str, int i2, int i3) throws HeadlessException {
        return showConfirmDialog(component, obj, str, i2, i3, null);
    }

    public static int showConfirmDialog(Component component, Object obj, String str, int i2, int i3, Icon icon) throws HeadlessException {
        return showOptionDialog(component, obj, str, i2, i3, icon, null, null);
    }

    public static int showOptionDialog(Component component, Object obj, String str, int i2, int i3, Icon icon, Object[] objArr, Object obj2) throws HeadlessException {
        JOptionPane jOptionPane = new JOptionPane(obj, i3, i2, icon, objArr, obj2);
        jOptionPane.setInitialValue(obj2);
        jOptionPane.setComponentOrientation((component == null ? getRootFrame() : component).getComponentOrientation());
        JDialog jDialogCreateDialog = jOptionPane.createDialog(component, str, styleFromMessageType(i3));
        jOptionPane.selectInitialValue();
        jDialogCreateDialog.show();
        jDialogCreateDialog.dispose();
        Object value = jOptionPane.getValue();
        if (value == null) {
            return -1;
        }
        if (objArr == null) {
            if (value instanceof Integer) {
                return ((Integer) value).intValue();
            }
            return -1;
        }
        int length = objArr.length;
        for (int i4 = 0; i4 < length; i4++) {
            if (objArr[i4].equals(value)) {
                return i4;
            }
        }
        return -1;
    }

    public JDialog createDialog(Component component, String str) throws HeadlessException {
        return createDialog(component, str, styleFromMessageType(getMessageType()));
    }

    public JDialog createDialog(String str) throws HeadlessException {
        int iStyleFromMessageType = styleFromMessageType(getMessageType());
        JDialog jDialog = new JDialog((Dialog) null, str, true);
        initDialog(jDialog, iStyleFromMessageType, null);
        return jDialog;
    }

    private JDialog createDialog(Component component, String str, int i2) throws HeadlessException {
        JDialog jDialog;
        Window windowForComponent = getWindowForComponent(component);
        if (windowForComponent instanceof Frame) {
            jDialog = new JDialog((Frame) windowForComponent, str, true);
        } else {
            jDialog = new JDialog((Dialog) windowForComponent, str, true);
        }
        if (windowForComponent instanceof SwingUtilities.SharedOwnerFrame) {
            jDialog.addWindowListener(SwingUtilities.getSharedOwnerFrameShutdownListener());
        }
        initDialog(jDialog, i2, component);
        return jDialog;
    }

    private void initDialog(final JDialog jDialog, int i2, Component component) {
        jDialog.setComponentOrientation(getComponentOrientation());
        Container contentPane = jDialog.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);
        jDialog.setResizable(false);
        if (JDialog.isDefaultLookAndFeelDecorated() && UIManager.getLookAndFeel().getSupportsWindowDecorations()) {
            jDialog.setUndecorated(true);
            getRootPane().setWindowDecorationStyle(i2);
        }
        jDialog.pack();
        jDialog.setLocationRelativeTo(component);
        final PropertyChangeListener propertyChangeListener = new PropertyChangeListener() { // from class: javax.swing.JOptionPane.1
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if (jDialog.isVisible() && propertyChangeEvent.getSource() == JOptionPane.this && propertyChangeEvent.getPropertyName().equals("value") && propertyChangeEvent.getNewValue() != null && propertyChangeEvent.getNewValue() != JOptionPane.UNINITIALIZED_VALUE) {
                    jDialog.setVisible(false);
                }
            }
        };
        WindowAdapter windowAdapter = new WindowAdapter() { // from class: javax.swing.JOptionPane.2
            private boolean gotFocus = false;

            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
            public void windowClosing(WindowEvent windowEvent) {
                JOptionPane.this.setValue(null);
            }

            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
            public void windowClosed(WindowEvent windowEvent) {
                JOptionPane.this.removePropertyChangeListener(propertyChangeListener);
                jDialog.getContentPane().removeAll();
            }

            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowFocusListener
            public void windowGainedFocus(WindowEvent windowEvent) {
                if (!this.gotFocus) {
                    JOptionPane.this.selectInitialValue();
                    this.gotFocus = true;
                }
            }
        };
        jDialog.addWindowListener(windowAdapter);
        jDialog.addWindowFocusListener(windowAdapter);
        jDialog.addComponentListener(new ComponentAdapter() { // from class: javax.swing.JOptionPane.3
            @Override // java.awt.event.ComponentAdapter, java.awt.event.ComponentListener
            public void componentShown(ComponentEvent componentEvent) {
                JOptionPane.this.setValue(JOptionPane.UNINITIALIZED_VALUE);
            }
        });
        addPropertyChangeListener(propertyChangeListener);
    }

    public static void showInternalMessageDialog(Component component, Object obj) {
        showInternalMessageDialog(component, obj, UIManager.getString("OptionPane.messageDialogTitle", component), 1);
    }

    public static void showInternalMessageDialog(Component component, Object obj, String str, int i2) {
        showInternalMessageDialog(component, obj, str, i2, null);
    }

    public static void showInternalMessageDialog(Component component, Object obj, String str, int i2, Icon icon) {
        showInternalOptionDialog(component, obj, str, -1, i2, icon, null, null);
    }

    public static int showInternalConfirmDialog(Component component, Object obj) {
        return showInternalConfirmDialog(component, obj, UIManager.getString("OptionPane.titleText"), 1);
    }

    public static int showInternalConfirmDialog(Component component, Object obj, String str, int i2) {
        return showInternalConfirmDialog(component, obj, str, i2, 3);
    }

    public static int showInternalConfirmDialog(Component component, Object obj, String str, int i2, int i3) {
        return showInternalConfirmDialog(component, obj, str, i2, i3, null);
    }

    public static int showInternalConfirmDialog(Component component, Object obj, String str, int i2, int i3, Icon icon) {
        return showInternalOptionDialog(component, obj, str, i2, i3, icon, null, null);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(13:0|2|(11:6|(2:7|(3:9|(2:11|57)(1:58)|12)(0))|13|(1:15)|16|22|(2:53|24)|26|(1:30)|31|(1:33)(2:35|(2:37|(2:39|40)(1:41))(4:43|(3:46|(3:60|48|49)(1:50)|44)|59|51)))(0)|55|13|(0)|16|22|(0)|26|(2:28|30)|31|(0)(0)) */
    /* JADX WARN: Removed duplicated region for block: B:15:0x008d A[Catch: IllegalAccessException -> 0x009c, IllegalArgumentException -> 0x00a1, InvocationTargetException -> 0x00a6, TryCatch #3 {IllegalAccessException -> 0x009c, IllegalArgumentException -> 0x00a1, InvocationTargetException -> 0x00a6, blocks: (B:13:0x0075, B:15:0x008d), top: B:55:0x0075 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00da A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00dc  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00af A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int showInternalOptionDialog(java.awt.Component r9, java.lang.Object r10, java.lang.String r11, int r12, int r13, javax.swing.Icon r14, java.lang.Object[] r15, java.lang.Object r16) {
        /*
            Method dump skipped, instructions count: 283
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.JOptionPane.showInternalOptionDialog(java.awt.Component, java.lang.Object, java.lang.String, int, int, javax.swing.Icon, java.lang.Object[], java.lang.Object):int");
    }

    public static String showInternalInputDialog(Component component, Object obj) {
        return showInternalInputDialog(component, obj, UIManager.getString("OptionPane.inputDialogTitle", component), 3);
    }

    public static String showInternalInputDialog(Component component, Object obj, String str, int i2) {
        return (String) showInternalInputDialog(component, obj, str, i2, null, null, null);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(13:0|2|(11:6|(2:7|(3:9|(2:11|41)(1:42)|12)(0))|13|(1:15)|16|22|(2:37|24)|26|(1:30)|31|(1:33)(2:35|36))(0)|39|13|(0)|16|22|(0)|26|(2:28|30)|31|(0)(0)) */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0097 A[Catch: IllegalAccessException -> 0x00a6, IllegalArgumentException -> 0x00ab, InvocationTargetException -> 0x00b0, TryCatch #3 {IllegalAccessException -> 0x00a6, IllegalArgumentException -> 0x00ab, InvocationTargetException -> 0x00b0, blocks: (B:13:0x007f, B:15:0x0097), top: B:39:0x007f }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00e7 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.Object showInternalInputDialog(java.awt.Component r9, java.lang.Object r10, java.lang.String r11, int r12, javax.swing.Icon r13, java.lang.Object[] r14, java.lang.Object r15) {
        /*
            Method dump skipped, instructions count: 236
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.JOptionPane.showInternalInputDialog(java.awt.Component, java.lang.Object, java.lang.String, int, javax.swing.Icon, java.lang.Object[], java.lang.Object):java.lang.Object");
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0013, code lost:
    
        if (r0 == null) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public javax.swing.JInternalFrame createInternalFrame(java.awt.Component r9, java.lang.String r10) {
        /*
            Method dump skipped, instructions count: 356
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.JOptionPane.createInternalFrame(java.awt.Component, java.lang.String):javax.swing.JInternalFrame");
    }

    public static Frame getFrameForComponent(Component component) throws HeadlessException {
        if (component == null) {
            return getRootFrame();
        }
        if (component instanceof Frame) {
            return (Frame) component;
        }
        return getFrameForComponent(component.getParent());
    }

    static Window getWindowForComponent(Component component) throws HeadlessException {
        if (component == null) {
            return getRootFrame();
        }
        if ((component instanceof Frame) || (component instanceof Dialog)) {
            return (Window) component;
        }
        return getWindowForComponent(component.getParent());
    }

    public static JDesktopPane getDesktopPaneForComponent(Component component) {
        if (component == null) {
            return null;
        }
        if (component instanceof JDesktopPane) {
            return (JDesktopPane) component;
        }
        return getDesktopPaneForComponent(component.getParent());
    }

    public static void setRootFrame(Frame frame) {
        if (frame != null) {
            SwingUtilities.appContextPut(sharedFrameKey, frame);
        } else {
            SwingUtilities.appContextRemove(sharedFrameKey);
        }
    }

    public static Frame getRootFrame() throws HeadlessException {
        Frame sharedOwnerFrame = (Frame) SwingUtilities.appContextGet(sharedFrameKey);
        if (sharedOwnerFrame == null) {
            sharedOwnerFrame = SwingUtilities.getSharedOwnerFrame();
            SwingUtilities.appContextPut(sharedFrameKey, sharedOwnerFrame);
        }
        return sharedOwnerFrame;
    }

    public JOptionPane() {
        this("JOptionPane message");
    }

    public JOptionPane(Object obj) {
        this(obj, -1);
    }

    public JOptionPane(Object obj, int i2) {
        this(obj, i2, -1);
    }

    public JOptionPane(Object obj, int i2, int i3) {
        this(obj, i2, i3, null);
    }

    public JOptionPane(Object obj, int i2, int i3, Icon icon) {
        this(obj, i2, i3, icon, null);
    }

    public JOptionPane(Object obj, int i2, int i3, Icon icon, Object[] objArr) {
        this(obj, i2, i3, icon, objArr, null);
    }

    public JOptionPane(Object obj, int i2, int i3, Icon icon, Object[] objArr, Object obj2) {
        this.message = obj;
        this.options = objArr;
        this.initialValue = obj2;
        this.icon = icon;
        setMessageType(i2);
        setOptionType(i3);
        this.value = UNINITIALIZED_VALUE;
        this.inputValue = UNINITIALIZED_VALUE;
        updateUI();
    }

    public void setUI(OptionPaneUI optionPaneUI) {
        if (this.ui != optionPaneUI) {
            super.setUI((ComponentUI) optionPaneUI);
            invalidate();
        }
    }

    public OptionPaneUI getUI() {
        return (OptionPaneUI) this.ui;
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((OptionPaneUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public void setMessage(Object obj) {
        Object obj2 = this.message;
        this.message = obj;
        firePropertyChange("message", obj2, this.message);
    }

    public Object getMessage() {
        return this.message;
    }

    public void setIcon(Icon icon) {
        Icon icon2 = this.icon;
        this.icon = icon;
        firePropertyChange("icon", icon2, this.icon);
    }

    public Icon getIcon() {
        return this.icon;
    }

    public void setValue(Object obj) {
        Object obj2 = this.value;
        this.value = obj;
        firePropertyChange("value", obj2, this.value);
    }

    public Object getValue() {
        return this.value;
    }

    public void setOptions(Object[] objArr) {
        Object[] objArr2 = this.options;
        this.options = objArr;
        firePropertyChange(OPTIONS_PROPERTY, objArr2, this.options);
    }

    public Object[] getOptions() {
        if (this.options != null) {
            int length = this.options.length;
            Object[] objArr = new Object[length];
            System.arraycopy(this.options, 0, objArr, 0, length);
            return objArr;
        }
        return this.options;
    }

    public void setInitialValue(Object obj) {
        Object obj2 = this.initialValue;
        this.initialValue = obj;
        firePropertyChange(INITIAL_VALUE_PROPERTY, obj2, this.initialValue);
    }

    public Object getInitialValue() {
        return this.initialValue;
    }

    public void setMessageType(int i2) {
        if (i2 != 0 && i2 != 1 && i2 != 2 && i2 != 3 && i2 != -1) {
            throw new RuntimeException("JOptionPane: type must be one of JOptionPane.ERROR_MESSAGE, JOptionPane.INFORMATION_MESSAGE, JOptionPane.WARNING_MESSAGE, JOptionPane.QUESTION_MESSAGE or JOptionPane.PLAIN_MESSAGE");
        }
        int i3 = this.messageType;
        this.messageType = i2;
        firePropertyChange(MESSAGE_TYPE_PROPERTY, i3, this.messageType);
    }

    public int getMessageType() {
        return this.messageType;
    }

    public void setOptionType(int i2) {
        if (i2 != -1 && i2 != 0 && i2 != 1 && i2 != 2) {
            throw new RuntimeException("JOptionPane: option type must be one of JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_CANCEL_OPTION or JOptionPane.OK_CANCEL_OPTION");
        }
        int i3 = this.optionType;
        this.optionType = i2;
        firePropertyChange(OPTION_TYPE_PROPERTY, i3, this.optionType);
    }

    public int getOptionType() {
        return this.optionType;
    }

    public void setSelectionValues(Object[] objArr) {
        Object[] objArr2 = this.selectionValues;
        this.selectionValues = objArr;
        firePropertyChange(SELECTION_VALUES_PROPERTY, objArr2, objArr);
        if (this.selectionValues != null) {
            setWantsInput(true);
        }
    }

    public Object[] getSelectionValues() {
        return this.selectionValues;
    }

    public void setInitialSelectionValue(Object obj) {
        Object obj2 = this.initialSelectionValue;
        this.initialSelectionValue = obj;
        firePropertyChange(INITIAL_SELECTION_VALUE_PROPERTY, obj2, obj);
    }

    public Object getInitialSelectionValue() {
        return this.initialSelectionValue;
    }

    public void setInputValue(Object obj) {
        Object obj2 = this.inputValue;
        this.inputValue = obj;
        firePropertyChange(INPUT_VALUE_PROPERTY, obj2, obj);
    }

    public Object getInputValue() {
        return this.inputValue;
    }

    public int getMaxCharactersPerLineCount() {
        return Integer.MAX_VALUE;
    }

    public void setWantsInput(boolean z2) {
        boolean z3 = this.wantsInput;
        this.wantsInput = z2;
        firePropertyChange(WANTS_INPUT_PROPERTY, z3, z2);
    }

    public boolean getWantsInput() {
        return this.wantsInput;
    }

    public void selectInitialValue() {
        OptionPaneUI ui = getUI();
        if (ui != null) {
            ui.selectInitialValue(this);
        }
    }

    private static int styleFromMessageType(int i2) {
        switch (i2) {
            case -1:
            default:
                return 2;
            case 0:
                return 4;
            case 1:
                return 3;
            case 2:
                return 8;
            case 3:
                return 7;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Vector vector = new Vector();
        objectOutputStream.defaultWriteObject();
        if (this.icon != null && (this.icon instanceof Serializable)) {
            vector.addElement("icon");
            vector.addElement(this.icon);
        }
        if (this.message != null && (this.message instanceof Serializable)) {
            vector.addElement("message");
            vector.addElement(this.message);
        }
        if (this.options != null) {
            Vector vector2 = new Vector();
            int length = this.options.length;
            for (int i2 = 0; i2 < length; i2++) {
                if (this.options[i2] instanceof Serializable) {
                    vector2.addElement(this.options[i2]);
                }
            }
            if (vector2.size() > 0) {
                Object[] objArr = new Object[vector2.size()];
                vector2.copyInto(objArr);
                vector.addElement(OPTIONS_PROPERTY);
                vector.addElement(objArr);
            }
        }
        if (this.initialValue != null && (this.initialValue instanceof Serializable)) {
            vector.addElement(INITIAL_VALUE_PROPERTY);
            vector.addElement(this.initialValue);
        }
        if (this.value != null && (this.value instanceof Serializable)) {
            vector.addElement("value");
            vector.addElement(this.value);
        }
        if (this.selectionValues != null) {
            boolean z2 = true;
            int i3 = 0;
            int length2 = this.selectionValues.length;
            while (true) {
                if (i3 >= length2) {
                    break;
                }
                if (this.selectionValues[i3] == null || (this.selectionValues[i3] instanceof Serializable)) {
                    i3++;
                } else {
                    z2 = false;
                    break;
                }
            }
            if (z2) {
                vector.addElement(SELECTION_VALUES_PROPERTY);
                vector.addElement(this.selectionValues);
            }
        }
        if (this.inputValue != null && (this.inputValue instanceof Serializable)) {
            vector.addElement(INPUT_VALUE_PROPERTY);
            vector.addElement(this.inputValue);
        }
        if (this.initialSelectionValue != null && (this.initialSelectionValue instanceof Serializable)) {
            vector.addElement(INITIAL_SELECTION_VALUE_PROPERTY);
            vector.addElement(this.initialSelectionValue);
        }
        objectOutputStream.writeObject(vector);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Vector vector = (Vector) objectInputStream.readObject();
        int i2 = 0;
        int size = vector.size();
        if (0 < size && vector.elementAt(0).equals("icon")) {
            int i3 = 0 + 1;
            this.icon = (Icon) vector.elementAt(i3);
            i2 = i3 + 1;
        }
        if (i2 < size && vector.elementAt(i2).equals("message")) {
            int i4 = i2 + 1;
            this.message = vector.elementAt(i4);
            i2 = i4 + 1;
        }
        if (i2 < size && vector.elementAt(i2).equals(OPTIONS_PROPERTY)) {
            int i5 = i2 + 1;
            this.options = (Object[]) vector.elementAt(i5);
            i2 = i5 + 1;
        }
        if (i2 < size && vector.elementAt(i2).equals(INITIAL_VALUE_PROPERTY)) {
            int i6 = i2 + 1;
            this.initialValue = vector.elementAt(i6);
            i2 = i6 + 1;
        }
        if (i2 < size && vector.elementAt(i2).equals("value")) {
            int i7 = i2 + 1;
            this.value = vector.elementAt(i7);
            i2 = i7 + 1;
        }
        if (i2 < size && vector.elementAt(i2).equals(SELECTION_VALUES_PROPERTY)) {
            int i8 = i2 + 1;
            this.selectionValues = (Object[]) vector.elementAt(i8);
            i2 = i8 + 1;
        }
        if (i2 < size && vector.elementAt(i2).equals(INPUT_VALUE_PROPERTY)) {
            int i9 = i2 + 1;
            this.inputValue = vector.elementAt(i9);
            i2 = i9 + 1;
        }
        if (i2 < size && vector.elementAt(i2).equals(INITIAL_SELECTION_VALUE_PROPERTY)) {
            int i10 = i2 + 1;
            this.initialSelectionValue = vector.elementAt(i10);
            int i11 = i10 + 1;
        }
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
        String str;
        String str2;
        String string = this.icon != null ? this.icon.toString() : "";
        String string2 = this.initialValue != null ? this.initialValue.toString() : "";
        String string3 = this.message != null ? this.message.toString() : "";
        if (this.messageType == 0) {
            str = "ERROR_MESSAGE";
        } else if (this.messageType == 1) {
            str = "INFORMATION_MESSAGE";
        } else if (this.messageType == 2) {
            str = "WARNING_MESSAGE";
        } else if (this.messageType == 3) {
            str = "QUESTION_MESSAGE";
        } else if (this.messageType == -1) {
            str = "PLAIN_MESSAGE";
        } else {
            str = "";
        }
        if (this.optionType == -1) {
            str2 = "DEFAULT_OPTION";
        } else if (this.optionType == 0) {
            str2 = "YES_NO_OPTION";
        } else if (this.optionType == 1) {
            str2 = "YES_NO_CANCEL_OPTION";
        } else if (this.optionType == 2) {
            str2 = "OK_CANCEL_OPTION";
        } else {
            str2 = "";
        }
        return super.paramString() + ",icon=" + string + ",initialValue=" + string2 + ",message=" + string3 + ",messageType=" + str + ",optionType=" + str2 + ",wantsInput=" + (this.wantsInput ? "true" : "false");
    }

    /* loaded from: rt.jar:javax/swing/JOptionPane$ModalPrivilegedAction.class */
    private static class ModalPrivilegedAction implements PrivilegedAction<Method> {
        private Class<?> clazz;
        private String methodName;

        public ModalPrivilegedAction(Class<?> cls, String str) {
            this.clazz = cls;
            this.methodName = str;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Method run() throws SecurityException {
            Method declaredMethod = null;
            try {
                declaredMethod = this.clazz.getDeclaredMethod(this.methodName, (Class[]) null);
            } catch (NoSuchMethodException e2) {
            }
            if (declaredMethod != null) {
                declaredMethod.setAccessible(true);
            }
            return declaredMethod;
        }
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJOptionPane();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JOptionPane$AccessibleJOptionPane.class */
    protected class AccessibleJOptionPane extends JComponent.AccessibleJComponent {
        protected AccessibleJOptionPane() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            switch (JOptionPane.this.messageType) {
                case 0:
                case 1:
                case 2:
                    return AccessibleRole.ALERT;
                default:
                    return AccessibleRole.OPTION_PANE;
            }
        }
    }
}
