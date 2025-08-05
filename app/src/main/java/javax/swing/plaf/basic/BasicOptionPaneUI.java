package javax.swing.plaf.basic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.util.Locale;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.OptionPaneUI;
import sun.security.action.GetPropertyAction;
import sun.swing.DefaultLookup;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicOptionPaneUI.class */
public class BasicOptionPaneUI extends OptionPaneUI {
    public static final int MinimumWidth = 262;
    public static final int MinimumHeight = 90;
    private static String newline;
    protected JOptionPane optionPane;
    protected Dimension minimumSize;
    protected JComponent inputComponent;
    protected Component initialFocusComponent;
    protected boolean hasCustomComponents;
    protected PropertyChangeListener propertyChangeListener;
    private Handler handler;

    static {
        newline = (String) AccessController.doPrivileged(new GetPropertyAction("line.separator"));
        if (newline == null) {
            newline = "\n";
        }
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("close"));
        BasicLookAndFeel.installAudioActionMap(lazyActionMap);
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicOptionPaneUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.optionPane = (JOptionPane) jComponent;
        installDefaults();
        this.optionPane.setLayout(createLayoutManager());
        installComponents();
        installListeners();
        installKeyboardActions();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallComponents();
        this.optionPane.setLayout(null);
        uninstallKeyboardActions();
        uninstallListeners();
        uninstallDefaults();
        this.optionPane = null;
    }

    protected void installDefaults() {
        LookAndFeel.installColorsAndFont(this.optionPane, "OptionPane.background", "OptionPane.foreground", "OptionPane.font");
        LookAndFeel.installBorder(this.optionPane, "OptionPane.border");
        this.minimumSize = UIManager.getDimension("OptionPane.minimumSize");
        LookAndFeel.installProperty(this.optionPane, "opaque", Boolean.TRUE);
    }

    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(this.optionPane);
    }

    protected void installComponents() {
        this.optionPane.add(createMessageArea());
        Container containerCreateSeparator = createSeparator();
        if (containerCreateSeparator != null) {
            this.optionPane.add(containerCreateSeparator);
        }
        this.optionPane.add(createButtonArea());
        this.optionPane.applyComponentOrientation(this.optionPane.getComponentOrientation());
    }

    protected void uninstallComponents() {
        this.hasCustomComponents = false;
        this.inputComponent = null;
        this.initialFocusComponent = null;
        this.optionPane.removeAll();
    }

    protected LayoutManager createLayoutManager() {
        return new BoxLayout(this.optionPane, 1);
    }

    protected void installListeners() {
        PropertyChangeListener propertyChangeListenerCreatePropertyChangeListener = createPropertyChangeListener();
        this.propertyChangeListener = propertyChangeListenerCreatePropertyChangeListener;
        if (propertyChangeListenerCreatePropertyChangeListener != null) {
            this.optionPane.addPropertyChangeListener(this.propertyChangeListener);
        }
    }

    protected void uninstallListeners() {
        if (this.propertyChangeListener != null) {
            this.optionPane.removePropertyChangeListener(this.propertyChangeListener);
            this.propertyChangeListener = null;
        }
        this.handler = null;
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

    protected void installKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.optionPane, 2, getInputMap(2));
        LazyActionMap.installLazyActionMap(this.optionPane, BasicOptionPaneUI.class, "OptionPane.actionMap");
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.optionPane, 2, null);
        SwingUtilities.replaceUIActionMap(this.optionPane, null);
    }

    InputMap getInputMap(int i2) {
        Object[] objArr;
        if (i2 == 2 && (objArr = (Object[]) DefaultLookup.get(this.optionPane, this, "OptionPane.windowBindings")) != null) {
            return LookAndFeel.makeComponentInputMap(this.optionPane, objArr);
        }
        return null;
    }

    public Dimension getMinimumOptionPaneSize() {
        if (this.minimumSize == null) {
            return new Dimension(262, 90);
        }
        return new Dimension(this.minimumSize.width, this.minimumSize.height);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        if (jComponent == this.optionPane) {
            Dimension minimumOptionPaneSize = getMinimumOptionPaneSize();
            LayoutManager layout = jComponent.getLayout();
            if (layout != null) {
                Dimension dimensionPreferredLayoutSize = layout.preferredLayoutSize(jComponent);
                if (minimumOptionPaneSize != null) {
                    return new Dimension(Math.max(dimensionPreferredLayoutSize.width, minimumOptionPaneSize.width), Math.max(dimensionPreferredLayoutSize.height, minimumOptionPaneSize.height));
                }
                return dimensionPreferredLayoutSize;
            }
            return minimumOptionPaneSize;
        }
        return null;
    }

    protected Container createMessageArea() {
        JPanel jPanel = new JPanel();
        Border border = (Border) DefaultLookup.get(this.optionPane, this, "OptionPane.messageAreaBorder");
        if (border != null) {
            jPanel.setBorder(border);
        }
        jPanel.setLayout(new BorderLayout());
        Container jPanel2 = new JPanel(new GridBagLayout());
        JPanel jPanel3 = new JPanel(new BorderLayout());
        jPanel2.setName("OptionPane.body");
        jPanel3.setName("OptionPane.realBody");
        if (getIcon() != null) {
            JPanel jPanel4 = new JPanel();
            jPanel4.setName("OptionPane.separator");
            jPanel4.setPreferredSize(new Dimension(15, 1));
            jPanel3.add(jPanel4, "Before");
        }
        jPanel3.add(jPanel2, BorderLayout.CENTER);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.anchor = DefaultLookup.getInt(this.optionPane, this, "OptionPane.messageAnchor", 10);
        gridBagConstraints.insets = new Insets(0, 0, 3, 0);
        addMessageComponents(jPanel2, gridBagConstraints, getMessage(), getMaxCharactersPerLineCount(), false);
        jPanel.add(jPanel3, BorderLayout.CENTER);
        addIcon(jPanel);
        return jPanel;
    }

    protected void addMessageComponents(Container container, GridBagConstraints gridBagConstraints, Object obj, int i2, boolean z2) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Component) {
            if ((obj instanceof JScrollPane) || (obj instanceof JPanel)) {
                gridBagConstraints.fill = 1;
                gridBagConstraints.weighty = 1.0d;
            } else {
                gridBagConstraints.fill = 2;
            }
            gridBagConstraints.weightx = 1.0d;
            container.add((Component) obj, gridBagConstraints);
            gridBagConstraints.weightx = 0.0d;
            gridBagConstraints.weighty = 0.0d;
            gridBagConstraints.fill = 0;
            gridBagConstraints.gridy++;
            if (!z2) {
                this.hasCustomComponents = true;
                return;
            }
            return;
        }
        if (obj instanceof Object[]) {
            for (Object obj2 : (Object[]) obj) {
                addMessageComponents(container, gridBagConstraints, obj2, i2, false);
            }
            return;
        }
        if (obj instanceof Icon) {
            JLabel jLabel = new JLabel((Icon) obj, 0);
            configureMessageLabel(jLabel);
            addMessageComponents(container, gridBagConstraints, jLabel, i2, true);
            return;
        }
        String string = obj.toString();
        int length = string.length();
        if (length <= 0) {
            return;
        }
        int length2 = 0;
        int iIndexOf = string.indexOf(newline);
        int i3 = iIndexOf;
        if (iIndexOf >= 0) {
            length2 = newline.length();
        } else {
            int iIndexOf2 = string.indexOf("\r\n");
            i3 = iIndexOf2;
            if (iIndexOf2 >= 0) {
                length2 = 2;
            } else {
                int iIndexOf3 = string.indexOf(10);
                i3 = iIndexOf3;
                if (iIndexOf3 >= 0) {
                    length2 = 1;
                }
            }
        }
        if (i3 >= 0) {
            if (i3 == 0) {
                JPanel jPanel = new JPanel() { // from class: javax.swing.plaf.basic.BasicOptionPaneUI.1
                    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
                    public Dimension getPreferredSize() {
                        Font font = getFont();
                        if (font != null) {
                            return new Dimension(1, font.getSize() + 2);
                        }
                        return new Dimension(0, 0);
                    }
                };
                jPanel.setName("OptionPane.break");
                addMessageComponents(container, gridBagConstraints, jPanel, i2, true);
            } else {
                addMessageComponents(container, gridBagConstraints, string.substring(0, i3), i2, false);
            }
            addMessageComponents(container, gridBagConstraints, string.substring(i3 + length2), i2, false);
            return;
        }
        if (length > i2) {
            Container containerCreateVerticalBox = Box.createVerticalBox();
            containerCreateVerticalBox.setName("OptionPane.verticalBox");
            burstStringInto(containerCreateVerticalBox, string, i2);
            addMessageComponents(container, gridBagConstraints, containerCreateVerticalBox, i2, true);
            return;
        }
        JLabel jLabel2 = new JLabel(string, 10);
        jLabel2.setName("OptionPane.label");
        configureMessageLabel(jLabel2);
        addMessageComponents(container, gridBagConstraints, jLabel2, i2, true);
    }

    protected Object getMessage() {
        JComponent jComponent;
        Object[] objArr;
        this.inputComponent = null;
        if (this.optionPane != null) {
            if (this.optionPane.getWantsInput()) {
                Object message = this.optionPane.getMessage();
                Object[] selectionValues = this.optionPane.getSelectionValues();
                Object initialSelectionValue = this.optionPane.getInitialSelectionValue();
                if (selectionValues != null) {
                    if (selectionValues.length < 20) {
                        JComboBox jComboBox = new JComboBox();
                        jComboBox.setName("OptionPane.comboBox");
                        for (Object obj : selectionValues) {
                            jComboBox.addItem(obj);
                        }
                        if (initialSelectionValue != null) {
                            jComboBox.setSelectedItem(initialSelectionValue);
                        }
                        this.inputComponent = jComboBox;
                        jComponent = jComboBox;
                    } else {
                        JList jList = new JList(selectionValues);
                        JComponent jScrollPane = new JScrollPane(jList);
                        jScrollPane.setName("OptionPane.scrollPane");
                        jList.setName("OptionPane.list");
                        jList.setVisibleRowCount(10);
                        jList.setSelectionMode(0);
                        if (initialSelectionValue != null) {
                            jList.setSelectedValue(initialSelectionValue, true);
                        }
                        jList.addMouseListener(getHandler());
                        jComponent = jScrollPane;
                        this.inputComponent = jList;
                    }
                } else {
                    MultiplexingTextField multiplexingTextField = new MultiplexingTextField(20);
                    multiplexingTextField.setName("OptionPane.textField");
                    multiplexingTextField.setKeyStrokes(new KeyStroke[]{KeyStroke.getKeyStroke("ENTER")});
                    if (initialSelectionValue != null) {
                        String string = initialSelectionValue.toString();
                        multiplexingTextField.setText(string);
                        multiplexingTextField.setSelectionStart(0);
                        multiplexingTextField.setSelectionEnd(string.length());
                    }
                    multiplexingTextField.addActionListener(getHandler());
                    this.inputComponent = multiplexingTextField;
                    jComponent = multiplexingTextField;
                }
                if (message == null) {
                    objArr = new Object[]{jComponent};
                } else {
                    objArr = new Object[]{message, jComponent};
                }
                return objArr;
            }
            return this.optionPane.getMessage();
        }
        return null;
    }

    protected void addIcon(Container container) {
        Icon icon = getIcon();
        if (icon != null) {
            JLabel jLabel = new JLabel(icon);
            jLabel.setName("OptionPane.iconLabel");
            jLabel.setVerticalAlignment(1);
            container.add(jLabel, "Before");
        }
    }

    protected Icon getIcon() {
        Icon icon = this.optionPane == null ? null : this.optionPane.getIcon();
        if (icon == null && this.optionPane != null) {
            icon = getIconForType(this.optionPane.getMessageType());
        }
        return icon;
    }

    protected Icon getIconForType(int i2) {
        if (i2 < 0 || i2 > 3) {
            return null;
        }
        String str = null;
        switch (i2) {
            case 0:
                str = "OptionPane.errorIcon";
                break;
            case 1:
                str = "OptionPane.informationIcon";
                break;
            case 2:
                str = "OptionPane.warningIcon";
                break;
            case 3:
                str = "OptionPane.questionIcon";
                break;
        }
        if (str != null) {
            return (Icon) DefaultLookup.get(this.optionPane, this, str);
        }
        return null;
    }

    protected int getMaxCharactersPerLineCount() {
        return this.optionPane.getMaxCharactersPerLineCount();
    }

    protected void burstStringInto(Container container, String str, int i2) {
        int length = str.length();
        if (length <= 0) {
            return;
        }
        if (length > i2) {
            int iLastIndexOf = str.lastIndexOf(32, i2);
            if (iLastIndexOf <= 0) {
                iLastIndexOf = str.indexOf(32, i2);
            }
            if (iLastIndexOf > 0 && iLastIndexOf < length) {
                burstStringInto(container, str.substring(0, iLastIndexOf), i2);
                burstStringInto(container, str.substring(iLastIndexOf + 1), i2);
                return;
            }
        }
        JLabel jLabel = new JLabel(str, 2);
        jLabel.setName("OptionPane.label");
        configureMessageLabel(jLabel);
        container.add(jLabel);
    }

    protected Container createSeparator() {
        return null;
    }

    protected Container createButtonArea() {
        JPanel jPanel = new JPanel();
        Border border = (Border) DefaultLookup.get(this.optionPane, this, "OptionPane.buttonAreaBorder");
        jPanel.setName("OptionPane.buttonArea");
        if (border != null) {
            jPanel.setBorder(border);
        }
        jPanel.setLayout(new ButtonAreaLayout(DefaultLookup.getBoolean(this.optionPane, this, "OptionPane.sameSizeButtons", true), DefaultLookup.getInt(this.optionPane, this, "OptionPane.buttonPadding", 6), DefaultLookup.getInt(this.optionPane, this, "OptionPane.buttonOrientation", 0), DefaultLookup.getBoolean(this.optionPane, this, "OptionPane.isYesLast", false)));
        addButtonComponents(jPanel, getButtons(), getInitialValueIndex());
        return jPanel;
    }

    protected void addButtonComponents(Container container, Object[] objArr, int i2) {
        JButton jButton;
        Component component;
        if (objArr != null && objArr.length > 0) {
            boolean sizeButtonsToSameWidth = getSizeButtonsToSameWidth();
            boolean z2 = true;
            int length = objArr.length;
            JButton[] jButtonArr = null;
            int iMax = 0;
            if (sizeButtonsToSameWidth) {
                jButtonArr = new JButton[length];
            }
            for (int i3 = 0; i3 < length; i3++) {
                Object obj = objArr[i3];
                if (obj instanceof Component) {
                    z2 = false;
                    component = (Component) obj;
                    container.add(component);
                    this.hasCustomComponents = true;
                } else {
                    if (obj instanceof ButtonFactory) {
                        jButton = ((ButtonFactory) obj).createButton();
                    } else if (obj instanceof Icon) {
                        jButton = new JButton((Icon) obj);
                    } else {
                        jButton = new JButton(obj.toString());
                    }
                    jButton.setName("OptionPane.button");
                    jButton.setMultiClickThreshhold(DefaultLookup.getInt(this.optionPane, this, "OptionPane.buttonClickThreshhold", 0));
                    configureButton(jButton);
                    container.add(jButton);
                    ActionListener actionListenerCreateButtonActionListener = createButtonActionListener(i3);
                    if (actionListenerCreateButtonActionListener != null) {
                        jButton.addActionListener(actionListenerCreateButtonActionListener);
                    }
                    component = jButton;
                }
                if (sizeButtonsToSameWidth && z2 && (component instanceof JButton)) {
                    jButtonArr[i3] = (JButton) component;
                    iMax = Math.max(iMax, component.getMinimumSize().width);
                }
                if (i3 == i2) {
                    this.initialFocusComponent = component;
                    if (this.initialFocusComponent instanceof JButton) {
                        ((JButton) this.initialFocusComponent).addHierarchyListener(new HierarchyListener() { // from class: javax.swing.plaf.basic.BasicOptionPaneUI.2
                            @Override // java.awt.event.HierarchyListener
                            public void hierarchyChanged(HierarchyEvent hierarchyEvent) {
                                JButton jButton2;
                                JRootPane rootPane;
                                if ((hierarchyEvent.getChangeFlags() & 1) != 0 && (rootPane = SwingUtilities.getRootPane((jButton2 = (JButton) hierarchyEvent.getComponent()))) != null) {
                                    rootPane.setDefaultButton(jButton2);
                                }
                            }
                        });
                    }
                }
            }
            ((ButtonAreaLayout) container.getLayout()).setSyncAllWidths(sizeButtonsToSameWidth && z2);
            if (DefaultLookup.getBoolean(this.optionPane, this, "OptionPane.setButtonMargin", true) && sizeButtonsToSameWidth && z2) {
                int i4 = length <= 2 ? 8 : 4;
                for (int i5 = 0; i5 < length; i5++) {
                    jButtonArr[i5].setMargin(new Insets(2, i4, 2, i4));
                }
            }
        }
    }

    protected ActionListener createButtonActionListener(int i2) {
        return new ButtonActionListener(i2);
    }

    protected Object[] getButtons() {
        ButtonFactory[] buttonFactoryArr;
        if (this.optionPane != null) {
            Object[] options = this.optionPane.getOptions();
            if (options == null) {
                int optionType = this.optionPane.getOptionType();
                Locale locale = this.optionPane.getLocale();
                int i2 = DefaultLookup.getInt(this.optionPane, this, "OptionPane.buttonMinimumWidth", -1);
                if (optionType == 0) {
                    buttonFactoryArr = new ButtonFactory[]{new ButtonFactory(UIManager.getString("OptionPane.yesButtonText", locale), getMnemonic("OptionPane.yesButtonMnemonic", locale), (Icon) DefaultLookup.get(this.optionPane, this, "OptionPane.yesIcon"), i2), new ButtonFactory(UIManager.getString("OptionPane.noButtonText", locale), getMnemonic("OptionPane.noButtonMnemonic", locale), (Icon) DefaultLookup.get(this.optionPane, this, "OptionPane.noIcon"), i2)};
                } else if (optionType == 1) {
                    buttonFactoryArr = new ButtonFactory[]{new ButtonFactory(UIManager.getString("OptionPane.yesButtonText", locale), getMnemonic("OptionPane.yesButtonMnemonic", locale), (Icon) DefaultLookup.get(this.optionPane, this, "OptionPane.yesIcon"), i2), new ButtonFactory(UIManager.getString("OptionPane.noButtonText", locale), getMnemonic("OptionPane.noButtonMnemonic", locale), (Icon) DefaultLookup.get(this.optionPane, this, "OptionPane.noIcon"), i2), new ButtonFactory(UIManager.getString("OptionPane.cancelButtonText", locale), getMnemonic("OptionPane.cancelButtonMnemonic", locale), (Icon) DefaultLookup.get(this.optionPane, this, "OptionPane.cancelIcon"), i2)};
                } else if (optionType == 2) {
                    buttonFactoryArr = new ButtonFactory[]{new ButtonFactory(UIManager.getString("OptionPane.okButtonText", locale), getMnemonic("OptionPane.okButtonMnemonic", locale), (Icon) DefaultLookup.get(this.optionPane, this, "OptionPane.okIcon"), i2), new ButtonFactory(UIManager.getString("OptionPane.cancelButtonText", locale), getMnemonic("OptionPane.cancelButtonMnemonic", locale), (Icon) DefaultLookup.get(this.optionPane, this, "OptionPane.cancelIcon"), i2)};
                } else {
                    buttonFactoryArr = new ButtonFactory[]{new ButtonFactory(UIManager.getString("OptionPane.okButtonText", locale), getMnemonic("OptionPane.okButtonMnemonic", locale), (Icon) DefaultLookup.get(this.optionPane, this, "OptionPane.okIcon"), i2)};
                }
                return buttonFactoryArr;
            }
            return options;
        }
        return null;
    }

    private int getMnemonic(String str, Locale locale) {
        String str2 = (String) UIManager.get(str, locale);
        if (str2 == null) {
            return 0;
        }
        try {
            return Integer.parseInt(str2);
        } catch (NumberFormatException e2) {
            return 0;
        }
    }

    protected boolean getSizeButtonsToSameWidth() {
        return true;
    }

    protected int getInitialValueIndex() {
        if (this.optionPane != null) {
            Object initialValue = this.optionPane.getInitialValue();
            Object[] options = this.optionPane.getOptions();
            if (options == null) {
                return 0;
            }
            if (initialValue != null) {
                for (int length = options.length - 1; length >= 0; length--) {
                    if (options[length].equals(initialValue)) {
                        return length;
                    }
                }
                return -1;
            }
            return -1;
        }
        return -1;
    }

    protected void resetInputValue() {
        if (this.inputComponent != null && (this.inputComponent instanceof JTextField)) {
            this.optionPane.setInputValue(((JTextField) this.inputComponent).getText());
            return;
        }
        if (this.inputComponent != null && (this.inputComponent instanceof JComboBox)) {
            this.optionPane.setInputValue(((JComboBox) this.inputComponent).getSelectedItem());
        } else if (this.inputComponent != null) {
            this.optionPane.setInputValue(((JList) this.inputComponent).getSelectedValue());
        }
    }

    @Override // javax.swing.plaf.OptionPaneUI
    public void selectInitialValue(JOptionPane jOptionPane) {
        JRootPane rootPane;
        if (this.inputComponent != null) {
            this.inputComponent.requestFocus();
            return;
        }
        if (this.initialFocusComponent != null) {
            this.initialFocusComponent.requestFocus();
        }
        if ((this.initialFocusComponent instanceof JButton) && (rootPane = SwingUtilities.getRootPane(this.initialFocusComponent)) != null) {
            rootPane.setDefaultButton((JButton) this.initialFocusComponent);
        }
    }

    @Override // javax.swing.plaf.OptionPaneUI
    public boolean containsCustomComponents(JOptionPane jOptionPane) {
        return this.hasCustomComponents;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicOptionPaneUI$ButtonAreaLayout.class */
    public static class ButtonAreaLayout implements LayoutManager {
        protected boolean syncAllWidths;
        protected int padding;
        protected boolean centersChildren;
        private int orientation;
        private boolean reverseButtons;
        private boolean useOrientation;

        public ButtonAreaLayout(boolean z2, int i2) {
            this.syncAllWidths = z2;
            this.padding = i2;
            this.centersChildren = true;
            this.useOrientation = false;
        }

        ButtonAreaLayout(boolean z2, int i2, int i3, boolean z3) {
            this(z2, i2);
            this.useOrientation = true;
            this.orientation = i3;
            this.reverseButtons = z3;
        }

        public void setSyncAllWidths(boolean z2) {
            this.syncAllWidths = z2;
        }

        public boolean getSyncAllWidths() {
            return this.syncAllWidths;
        }

        public void setPadding(int i2) {
            this.padding = i2;
        }

        public int getPadding() {
            return this.padding;
        }

        public void setCentersChildren(boolean z2) {
            this.centersChildren = z2;
            this.useOrientation = false;
        }

        public boolean getCentersChildren() {
            return this.centersChildren;
        }

        private int getOrientation(Container container) {
            if (!this.useOrientation) {
                return 0;
            }
            if (container.getComponentOrientation().isLeftToRight()) {
                return this.orientation;
            }
            switch (this.orientation) {
                case 0:
                    return 0;
                case 1:
                case 3:
                default:
                    return 2;
                case 2:
                    return 4;
                case 4:
                    return 2;
            }
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            int i2;
            int width;
            Component[] components = container.getComponents();
            if (components != null && components.length > 0) {
                int length = components.length;
                Insets insets = container.getInsets();
                int iMax = 0;
                int iMax2 = 0;
                int i3 = 0;
                int width2 = 0;
                int width3 = 0;
                boolean z2 = container.getComponentOrientation().isLeftToRight() ? this.reverseButtons : !this.reverseButtons;
                for (Component component : components) {
                    Dimension preferredSize = component.getPreferredSize();
                    iMax = Math.max(iMax, preferredSize.width);
                    iMax2 = Math.max(iMax2, preferredSize.height);
                    i3 += preferredSize.width;
                }
                if (getSyncAllWidths()) {
                    i3 = iMax * length;
                }
                int i4 = i3 + ((length - 1) * this.padding);
                switch (getOrientation(container)) {
                    case 0:
                        if (getCentersChildren() || length < 2) {
                            width2 = (container.getWidth() - i4) / 2;
                            break;
                        } else {
                            width2 = insets.left;
                            if (getSyncAllWidths()) {
                                width3 = ((((container.getWidth() - insets.left) - insets.right) - i4) / (length - 1)) + iMax;
                                break;
                            } else {
                                width3 = (((container.getWidth() - insets.left) - insets.right) - i4) / (length - 1);
                                break;
                            }
                        }
                        break;
                    case 2:
                        width2 = insets.left;
                        break;
                    case 4:
                        width2 = (container.getWidth() - insets.right) - i4;
                        break;
                }
                for (int i5 = 0; i5 < length; i5++) {
                    int i6 = z2 ? (length - i5) - 1 : i5;
                    Dimension preferredSize2 = components[i6].getPreferredSize();
                    if (getSyncAllWidths()) {
                        components[i6].setBounds(width2, insets.top, iMax, iMax2);
                    } else {
                        components[i6].setBounds(width2, insets.top, preferredSize2.width, preferredSize2.height);
                    }
                    if (width3 != 0) {
                        i2 = width2;
                        width = width3;
                    } else {
                        i2 = width2;
                        width = components[i6].getWidth() + this.padding;
                    }
                    width2 = i2 + width;
                }
            }
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            Component[] components;
            if (container != null && (components = container.getComponents()) != null && components.length > 0) {
                int length = components.length;
                int iMax = 0;
                Insets insets = container.getInsets();
                int i2 = insets.top + insets.bottom;
                int i3 = insets.left + insets.right;
                if (this.syncAllWidths) {
                    int iMax2 = 0;
                    for (Component component : components) {
                        Dimension preferredSize = component.getPreferredSize();
                        iMax = Math.max(iMax, preferredSize.height);
                        iMax2 = Math.max(iMax2, preferredSize.width);
                    }
                    return new Dimension(i3 + (iMax2 * length) + ((length - 1) * this.padding), i2 + iMax);
                }
                int i4 = 0;
                for (Component component2 : components) {
                    Dimension preferredSize2 = component2.getPreferredSize();
                    iMax = Math.max(iMax, preferredSize2.height);
                    i4 += preferredSize2.width;
                }
                return new Dimension(i3 + i4 + ((length - 1) * this.padding), i2 + iMax);
            }
            return new Dimension(0, 0);
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return minimumLayoutSize(container);
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicOptionPaneUI$PropertyChangeHandler.class */
    public class PropertyChangeHandler implements PropertyChangeListener {
        public PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicOptionPaneUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    private void configureMessageLabel(JLabel jLabel) {
        Color color = (Color) DefaultLookup.get(this.optionPane, this, "OptionPane.messageForeground");
        if (color != null) {
            jLabel.setForeground(color);
        }
        Font font = (Font) DefaultLookup.get(this.optionPane, this, "OptionPane.messageFont");
        if (font != null) {
            jLabel.setFont(font);
        }
    }

    private void configureButton(JButton jButton) {
        Font font = (Font) DefaultLookup.get(this.optionPane, this, "OptionPane.buttonFont");
        if (font != null) {
            jButton.setFont(font);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicOptionPaneUI$ButtonActionListener.class */
    public class ButtonActionListener implements ActionListener {
        protected int buttonIndex;

        public ButtonActionListener(int i2) {
            this.buttonIndex = i2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicOptionPaneUI.this.optionPane != null) {
                int optionType = BasicOptionPaneUI.this.optionPane.getOptionType();
                Object[] options = BasicOptionPaneUI.this.optionPane.getOptions();
                if (BasicOptionPaneUI.this.inputComponent != null && (options != null || optionType == -1 || ((optionType == 0 || optionType == 1 || optionType == 2) && this.buttonIndex == 0))) {
                    BasicOptionPaneUI.this.resetInputValue();
                }
                if (options == null) {
                    if (optionType == 2 && this.buttonIndex == 1) {
                        BasicOptionPaneUI.this.optionPane.setValue(2);
                        return;
                    } else {
                        BasicOptionPaneUI.this.optionPane.setValue(Integer.valueOf(this.buttonIndex));
                        return;
                    }
                }
                BasicOptionPaneUI.this.optionPane.setValue(options[this.buttonIndex]);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicOptionPaneUI$Handler.class */
    private class Handler implements ActionListener, MouseListener, PropertyChangeListener {
        private Handler() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicOptionPaneUI.this.optionPane.setInputValue(((JTextField) actionEvent.getSource()).getText());
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (mouseEvent.getClickCount() == 2) {
                JList jList = (JList) mouseEvent.getSource();
                BasicOptionPaneUI.this.optionPane.setInputValue(jList.getModel().getElementAt(jList.locationToIndex(mouseEvent.getPoint())));
                BasicOptionPaneUI.this.optionPane.setValue(0);
            }
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            boolean z2;
            if (propertyChangeEvent.getSource() == BasicOptionPaneUI.this.optionPane) {
                if ("ancestor" == propertyChangeEvent.getPropertyName()) {
                    JOptionPane jOptionPane = (JOptionPane) propertyChangeEvent.getSource();
                    if (propertyChangeEvent.getOldValue() == null) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    switch (jOptionPane.getMessageType()) {
                        case -1:
                            if (z2) {
                                BasicLookAndFeel.playSound(BasicOptionPaneUI.this.optionPane, "OptionPane.informationSound");
                                break;
                            }
                            break;
                        case 0:
                            if (z2) {
                                BasicLookAndFeel.playSound(BasicOptionPaneUI.this.optionPane, "OptionPane.errorSound");
                                break;
                            }
                            break;
                        case 1:
                            if (z2) {
                                BasicLookAndFeel.playSound(BasicOptionPaneUI.this.optionPane, "OptionPane.informationSound");
                                break;
                            }
                            break;
                        case 2:
                            if (z2) {
                                BasicLookAndFeel.playSound(BasicOptionPaneUI.this.optionPane, "OptionPane.warningSound");
                                break;
                            }
                            break;
                        case 3:
                            if (z2) {
                                BasicLookAndFeel.playSound(BasicOptionPaneUI.this.optionPane, "OptionPane.questionSound");
                                break;
                            }
                            break;
                        default:
                            System.err.println("Undefined JOptionPane type: " + jOptionPane.getMessageType());
                            break;
                    }
                }
                String propertyName = propertyChangeEvent.getPropertyName();
                if (propertyName == JOptionPane.OPTIONS_PROPERTY || propertyName == JOptionPane.INITIAL_VALUE_PROPERTY || propertyName == "icon" || propertyName == JOptionPane.MESSAGE_TYPE_PROPERTY || propertyName == JOptionPane.OPTION_TYPE_PROPERTY || propertyName == "message" || propertyName == JOptionPane.SELECTION_VALUES_PROPERTY || propertyName == JOptionPane.INITIAL_SELECTION_VALUE_PROPERTY || propertyName == JOptionPane.WANTS_INPUT_PROPERTY) {
                    BasicOptionPaneUI.this.uninstallComponents();
                    BasicOptionPaneUI.this.installComponents();
                    BasicOptionPaneUI.this.optionPane.validate();
                } else if (propertyName == "componentOrientation") {
                    ComponentOrientation componentOrientation = (ComponentOrientation) propertyChangeEvent.getNewValue();
                    JOptionPane jOptionPane2 = (JOptionPane) propertyChangeEvent.getSource();
                    if (componentOrientation != propertyChangeEvent.getOldValue()) {
                        jOptionPane2.applyComponentOrientation(componentOrientation);
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicOptionPaneUI$MultiplexingTextField.class */
    private static class MultiplexingTextField extends JTextField {
        private KeyStroke[] strokes;

        MultiplexingTextField(int i2) {
            super(i2);
        }

        void setKeyStrokes(KeyStroke[] keyStrokeArr) {
            this.strokes = keyStrokeArr;
        }

        @Override // javax.swing.JComponent
        protected boolean processKeyBinding(KeyStroke keyStroke, KeyEvent keyEvent, int i2, boolean z2) {
            boolean zProcessKeyBinding = super.processKeyBinding(keyStroke, keyEvent, i2, z2);
            if (zProcessKeyBinding && i2 != 2) {
                for (int length = this.strokes.length - 1; length >= 0; length--) {
                    if (this.strokes[length].equals(keyStroke)) {
                        return false;
                    }
                }
            }
            return zProcessKeyBinding;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicOptionPaneUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String CLOSE = "close";

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (getName() == CLOSE) {
                ((JOptionPane) actionEvent.getSource()).setValue(-1);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicOptionPaneUI$ButtonFactory.class */
    private static class ButtonFactory {
        private String text;
        private int mnemonic;
        private Icon icon;
        private int minimumWidth;

        ButtonFactory(String str, int i2, Icon icon, int i3) {
            this.minimumWidth = -1;
            this.text = str;
            this.mnemonic = i2;
            this.icon = icon;
            this.minimumWidth = i3;
        }

        JButton createButton() {
            JButton jButton;
            if (this.minimumWidth > 0) {
                jButton = new ConstrainedButton(this.text, this.minimumWidth);
            } else {
                jButton = new JButton(this.text);
            }
            if (this.icon != null) {
                jButton.setIcon(this.icon);
            }
            if (this.mnemonic != 0) {
                jButton.setMnemonic(this.mnemonic);
            }
            return jButton;
        }

        /* loaded from: rt.jar:javax/swing/plaf/basic/BasicOptionPaneUI$ButtonFactory$ConstrainedButton.class */
        private static class ConstrainedButton extends JButton {
            int minimumWidth;

            ConstrainedButton(String str, int i2) {
                super(str);
                this.minimumWidth = i2;
            }

            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMinimumSize() {
                Dimension minimumSize = super.getMinimumSize();
                minimumSize.width = Math.max(minimumSize.width, this.minimumWidth);
                return minimumSize;
            }

            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getPreferredSize() {
                Dimension preferredSize = super.getPreferredSize();
                preferredSize.width = Math.max(preferredSize.width, this.minimumWidth);
                return preferredSize;
            }
        }
    }
}
