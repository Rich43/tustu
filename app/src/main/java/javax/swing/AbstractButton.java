package javax.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Transient;
import java.io.Serializable;
import java.text.BreakIterator;
import java.util.Enumeration;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleExtendedComponent;
import javax.accessibility.AccessibleIcon;
import javax.accessibility.AccessibleKeyBinding;
import javax.accessibility.AccessibleRelation;
import javax.accessibility.AccessibleRelationSet;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleText;
import javax.accessibility.AccessibleValue;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;
import jdk.jfr.Enabled;

/* loaded from: rt.jar:javax/swing/AbstractButton.class */
public abstract class AbstractButton extends JComponent implements ItemSelectable, SwingConstants {
    public static final String MODEL_CHANGED_PROPERTY = "model";
    public static final String TEXT_CHANGED_PROPERTY = "text";
    public static final String MNEMONIC_CHANGED_PROPERTY = "mnemonic";
    public static final String MARGIN_CHANGED_PROPERTY = "margin";
    public static final String VERTICAL_ALIGNMENT_CHANGED_PROPERTY = "verticalAlignment";
    public static final String HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY = "horizontalAlignment";
    public static final String VERTICAL_TEXT_POSITION_CHANGED_PROPERTY = "verticalTextPosition";
    public static final String HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY = "horizontalTextPosition";
    public static final String BORDER_PAINTED_CHANGED_PROPERTY = "borderPainted";
    public static final String FOCUS_PAINTED_CHANGED_PROPERTY = "focusPainted";
    public static final String ROLLOVER_ENABLED_CHANGED_PROPERTY = "rolloverEnabled";
    public static final String CONTENT_AREA_FILLED_CHANGED_PROPERTY = "contentAreaFilled";
    public static final String ICON_CHANGED_PROPERTY = "icon";
    public static final String PRESSED_ICON_CHANGED_PROPERTY = "pressedIcon";
    public static final String SELECTED_ICON_CHANGED_PROPERTY = "selectedIcon";
    public static final String ROLLOVER_ICON_CHANGED_PROPERTY = "rolloverIcon";
    public static final String ROLLOVER_SELECTED_ICON_CHANGED_PROPERTY = "rolloverSelectedIcon";
    public static final String DISABLED_ICON_CHANGED_PROPERTY = "disabledIcon";
    public static final String DISABLED_SELECTED_ICON_CHANGED_PROPERTY = "disabledSelectedIcon";
    private int mnemonic;
    private Handler handler;
    protected transient ChangeEvent changeEvent;
    private Action action;
    private PropertyChangeListener actionPropertyChangeListener;
    protected ButtonModel model = null;
    private String text = "";
    private Insets margin = null;
    private Insets defaultMargin = null;
    private Icon defaultIcon = null;
    private Icon pressedIcon = null;
    private Icon disabledIcon = null;
    private Icon selectedIcon = null;
    private Icon disabledSelectedIcon = null;
    private Icon rolloverIcon = null;
    private Icon rolloverSelectedIcon = null;
    private boolean paintBorder = true;
    private boolean paintFocus = true;
    private boolean rolloverEnabled = false;
    private boolean contentAreaFilled = true;
    private int verticalAlignment = 0;
    private int horizontalAlignment = 0;
    private int verticalTextPosition = 0;
    private int horizontalTextPosition = 11;
    private int iconTextGap = 4;
    private int mnemonicIndex = -1;
    private long multiClickThreshhold = 0;
    private boolean borderPaintedSet = false;
    private boolean rolloverEnabledSet = false;
    private boolean iconTextGapSet = false;
    private boolean contentAreaFilledSet = false;
    private boolean setLayout = false;
    boolean defaultCapable = true;
    protected ChangeListener changeListener = null;
    protected ActionListener actionListener = null;
    protected ItemListener itemListener = null;
    private boolean hideActionText = false;

    public void setHideActionText(boolean z2) throws IllegalArgumentException {
        if (z2 != this.hideActionText) {
            this.hideActionText = z2;
            if (getAction() != null) {
                setTextFromAction(getAction(), false);
            }
            firePropertyChange("hideActionText", !z2, z2);
        }
    }

    public boolean getHideActionText() {
        return this.hideActionText;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) throws IllegalArgumentException {
        String str2 = this.text;
        this.text = str;
        firePropertyChange("text", str2, str);
        updateDisplayedMnemonicIndex(str, getMnemonic());
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, str2, str);
        }
        if (str == null || str2 == null || !str.equals(str2)) {
            revalidate();
            repaint();
        }
    }

    public boolean isSelected() {
        return this.model.isSelected();
    }

    public void setSelected(boolean z2) {
        isSelected();
        this.model.setSelected(z2);
    }

    public void doClick() {
        doClick(68);
    }

    public void doClick(int i2) {
        Dimension size = getSize();
        this.model.setArmed(true);
        this.model.setPressed(true);
        paintImmediately(new Rectangle(0, 0, size.width, size.height));
        try {
            Thread.currentThread();
            Thread.sleep(i2);
        } catch (InterruptedException e2) {
        }
        this.model.setPressed(false);
        this.model.setArmed(false);
    }

    public void setMargin(Insets insets) {
        if (insets instanceof UIResource) {
            this.defaultMargin = insets;
        } else if (this.margin instanceof UIResource) {
            this.defaultMargin = this.margin;
        }
        if (insets == null && this.defaultMargin != null) {
            insets = this.defaultMargin;
        }
        Insets insets2 = this.margin;
        this.margin = insets;
        firePropertyChange(MARGIN_CHANGED_PROPERTY, insets2, insets);
        if (insets2 == null || !insets2.equals(insets)) {
            revalidate();
            repaint();
        }
    }

    public Insets getMargin() {
        if (this.margin == null) {
            return null;
        }
        return (Insets) this.margin.clone();
    }

    public Icon getIcon() {
        return this.defaultIcon;
    }

    public void setIcon(Icon icon) {
        Icon icon2 = this.defaultIcon;
        this.defaultIcon = icon;
        if (icon != icon2 && (this.disabledIcon instanceof UIResource)) {
            this.disabledIcon = null;
        }
        firePropertyChange("icon", icon2, icon);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, icon2, icon);
        }
        if (icon != icon2) {
            if (icon == null || icon2 == null || icon.getIconWidth() != icon2.getIconWidth() || icon.getIconHeight() != icon2.getIconHeight()) {
                revalidate();
            }
            repaint();
        }
    }

    public Icon getPressedIcon() {
        return this.pressedIcon;
    }

    public void setPressedIcon(Icon icon) {
        Icon icon2 = this.pressedIcon;
        this.pressedIcon = icon;
        firePropertyChange(PRESSED_ICON_CHANGED_PROPERTY, icon2, icon);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, icon2, icon);
        }
        if (icon != icon2 && getModel().isPressed()) {
            repaint();
        }
    }

    public Icon getSelectedIcon() {
        return this.selectedIcon;
    }

    public void setSelectedIcon(Icon icon) {
        Icon icon2 = this.selectedIcon;
        this.selectedIcon = icon;
        if (icon != icon2 && (this.disabledSelectedIcon instanceof UIResource)) {
            this.disabledSelectedIcon = null;
        }
        firePropertyChange(SELECTED_ICON_CHANGED_PROPERTY, icon2, icon);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, icon2, icon);
        }
        if (icon != icon2 && isSelected()) {
            repaint();
        }
    }

    public Icon getRolloverIcon() {
        return this.rolloverIcon;
    }

    public void setRolloverIcon(Icon icon) {
        Icon icon2 = this.rolloverIcon;
        this.rolloverIcon = icon;
        firePropertyChange(ROLLOVER_ICON_CHANGED_PROPERTY, icon2, icon);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, icon2, icon);
        }
        setRolloverEnabled(true);
        if (icon != icon2) {
            repaint();
        }
    }

    public Icon getRolloverSelectedIcon() {
        return this.rolloverSelectedIcon;
    }

    public void setRolloverSelectedIcon(Icon icon) {
        Icon icon2 = this.rolloverSelectedIcon;
        this.rolloverSelectedIcon = icon;
        firePropertyChange(ROLLOVER_SELECTED_ICON_CHANGED_PROPERTY, icon2, icon);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, icon2, icon);
        }
        setRolloverEnabled(true);
        if (icon != icon2 && isSelected()) {
            repaint();
        }
    }

    @Transient
    public Icon getDisabledIcon() {
        if (this.disabledIcon == null) {
            this.disabledIcon = UIManager.getLookAndFeel().getDisabledIcon(this, getIcon());
            if (this.disabledIcon != null) {
                firePropertyChange(DISABLED_ICON_CHANGED_PROPERTY, (Object) null, this.disabledIcon);
            }
        }
        return this.disabledIcon;
    }

    public void setDisabledIcon(Icon icon) {
        Icon icon2 = this.disabledIcon;
        this.disabledIcon = icon;
        firePropertyChange(DISABLED_ICON_CHANGED_PROPERTY, icon2, icon);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, icon2, icon);
        }
        if (icon != icon2 && !isEnabled()) {
            repaint();
        }
    }

    public Icon getDisabledSelectedIcon() {
        if (this.disabledSelectedIcon == null) {
            if (this.selectedIcon != null) {
                this.disabledSelectedIcon = UIManager.getLookAndFeel().getDisabledSelectedIcon(this, getSelectedIcon());
            } else {
                return getDisabledIcon();
            }
        }
        return this.disabledSelectedIcon;
    }

    public void setDisabledSelectedIcon(Icon icon) {
        Icon icon2 = this.disabledSelectedIcon;
        this.disabledSelectedIcon = icon;
        firePropertyChange(DISABLED_SELECTED_ICON_CHANGED_PROPERTY, icon2, icon);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, icon2, icon);
        }
        if (icon != icon2) {
            if (icon == null || icon2 == null || icon.getIconWidth() != icon2.getIconWidth() || icon.getIconHeight() != icon2.getIconHeight()) {
                revalidate();
            }
            if (!isEnabled() && isSelected()) {
                repaint();
            }
        }
    }

    public int getVerticalAlignment() {
        return this.verticalAlignment;
    }

    public void setVerticalAlignment(int i2) {
        if (i2 == this.verticalAlignment) {
            return;
        }
        int i3 = this.verticalAlignment;
        this.verticalAlignment = checkVerticalKey(i2, VERTICAL_ALIGNMENT_CHANGED_PROPERTY);
        firePropertyChange(VERTICAL_ALIGNMENT_CHANGED_PROPERTY, i3, this.verticalAlignment);
        repaint();
    }

    public int getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    public void setHorizontalAlignment(int i2) {
        if (i2 == this.horizontalAlignment) {
            return;
        }
        int i3 = this.horizontalAlignment;
        this.horizontalAlignment = checkHorizontalKey(i2, HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY);
        firePropertyChange(HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY, i3, this.horizontalAlignment);
        repaint();
    }

    public int getVerticalTextPosition() {
        return this.verticalTextPosition;
    }

    public void setVerticalTextPosition(int i2) {
        if (i2 == this.verticalTextPosition) {
            return;
        }
        int i3 = this.verticalTextPosition;
        this.verticalTextPosition = checkVerticalKey(i2, VERTICAL_TEXT_POSITION_CHANGED_PROPERTY);
        firePropertyChange(VERTICAL_TEXT_POSITION_CHANGED_PROPERTY, i3, this.verticalTextPosition);
        revalidate();
        repaint();
    }

    public int getHorizontalTextPosition() {
        return this.horizontalTextPosition;
    }

    public void setHorizontalTextPosition(int i2) {
        if (i2 == this.horizontalTextPosition) {
            return;
        }
        int i3 = this.horizontalTextPosition;
        this.horizontalTextPosition = checkHorizontalKey(i2, HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY);
        firePropertyChange(HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY, i3, this.horizontalTextPosition);
        revalidate();
        repaint();
    }

    public int getIconTextGap() {
        return this.iconTextGap;
    }

    public void setIconTextGap(int i2) {
        int i3 = this.iconTextGap;
        this.iconTextGap = i2;
        this.iconTextGapSet = true;
        firePropertyChange("iconTextGap", i3, i2);
        if (i2 != i3) {
            revalidate();
            repaint();
        }
    }

    protected int checkHorizontalKey(int i2, String str) {
        if (i2 == 2 || i2 == 0 || i2 == 4 || i2 == 10 || i2 == 11) {
            return i2;
        }
        throw new IllegalArgumentException(str);
    }

    protected int checkVerticalKey(int i2, String str) {
        if (i2 == 1 || i2 == 0 || i2 == 3) {
            return i2;
        }
        throw new IllegalArgumentException(str);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void removeNotify() {
        super.removeNotify();
        if (isRolloverEnabled()) {
            getModel().setRollover(false);
        }
    }

    public void setActionCommand(String str) {
        getModel().setActionCommand(str);
    }

    public String getActionCommand() {
        String actionCommand = getModel().getActionCommand();
        if (actionCommand == null) {
            actionCommand = getText();
        }
        return actionCommand;
    }

    public void setAction(Action action) throws IllegalArgumentException {
        Action action2 = getAction();
        if (this.action == null || !this.action.equals(action)) {
            this.action = action;
            if (action2 != null) {
                removeActionListener(action2);
                action2.removePropertyChangeListener(this.actionPropertyChangeListener);
                this.actionPropertyChangeListener = null;
            }
            configurePropertiesFromAction(this.action);
            if (this.action != null) {
                if (!isListener(ActionListener.class, this.action)) {
                    addActionListener(this.action);
                }
                this.actionPropertyChangeListener = createActionPropertyChangeListener(this.action);
                this.action.addPropertyChangeListener(this.actionPropertyChangeListener);
            }
            firePropertyChange("action", action2, this.action);
        }
    }

    private boolean isListener(Class cls, ActionListener actionListener) {
        boolean z2 = false;
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == cls && listenerList[length + 1] == actionListener) {
                z2 = true;
            }
        }
        return z2;
    }

    public Action getAction() {
        return this.action;
    }

    protected void configurePropertiesFromAction(Action action) throws IllegalArgumentException {
        setMnemonicFromAction(action);
        setTextFromAction(action, false);
        AbstractAction.setToolTipTextFromAction(this, action);
        setIconFromAction(action);
        setActionCommandFromAction(action);
        AbstractAction.setEnabledFromAction(this, action);
        if (AbstractAction.hasSelectedKey(action) && shouldUpdateSelectedStateFromAction()) {
            setSelectedFromAction(action);
        }
        setDisplayedMnemonicIndexFromAction(action, false);
    }

    @Override // javax.swing.JComponent
    void clientPropertyChanged(Object obj, Object obj2, Object obj3) throws IllegalArgumentException {
        if (obj == "hideActionText") {
            boolean zBooleanValue = obj3 instanceof Boolean ? ((Boolean) obj3).booleanValue() : false;
            if (getHideActionText() != zBooleanValue) {
                setHideActionText(zBooleanValue);
            }
        }
    }

    boolean shouldUpdateSelectedStateFromAction() {
        return false;
    }

    protected void actionPropertyChanged(Action action, String str) throws IllegalArgumentException {
        if (str == "Name") {
            setTextFromAction(action, true);
            return;
        }
        if (str == Enabled.NAME) {
            AbstractAction.setEnabledFromAction(this, action);
            return;
        }
        if (str == Action.SHORT_DESCRIPTION) {
            AbstractAction.setToolTipTextFromAction(this, action);
            return;
        }
        if (str == Action.SMALL_ICON) {
            smallIconChanged(action);
            return;
        }
        if (str == Action.MNEMONIC_KEY) {
            setMnemonicFromAction(action);
            return;
        }
        if (str == Action.ACTION_COMMAND_KEY) {
            setActionCommandFromAction(action);
            return;
        }
        if (str == Action.SELECTED_KEY && AbstractAction.hasSelectedKey(action) && shouldUpdateSelectedStateFromAction()) {
            setSelectedFromAction(action);
        } else if (str == Action.DISPLAYED_MNEMONIC_INDEX_KEY) {
            setDisplayedMnemonicIndexFromAction(action, true);
        } else if (str == Action.LARGE_ICON_KEY) {
            largeIconChanged(action);
        }
    }

    private void setDisplayedMnemonicIndexFromAction(Action action, boolean z2) throws IllegalArgumentException {
        int iIntValue;
        Integer num = action == null ? null : (Integer) action.getValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY);
        if (z2 || num != null) {
            if (num == null) {
                iIntValue = -1;
            } else {
                iIntValue = num.intValue();
                String text = getText();
                if (text == null || iIntValue >= text.length()) {
                    iIntValue = -1;
                }
            }
            setDisplayedMnemonicIndex(iIntValue);
        }
    }

    private void setMnemonicFromAction(Action action) throws IllegalArgumentException {
        Integer num = action == null ? null : (Integer) action.getValue(Action.MNEMONIC_KEY);
        setMnemonic(num == null ? 0 : num.intValue());
    }

    private void setTextFromAction(Action action, boolean z2) throws IllegalArgumentException {
        boolean hideActionText = getHideActionText();
        if (!z2) {
            setText((action == null || hideActionText) ? null : (String) action.getValue("Name"));
        } else if (!hideActionText) {
            setText((String) action.getValue("Name"));
        }
    }

    void setIconFromAction(Action action) {
        Icon icon = null;
        if (action != null) {
            icon = (Icon) action.getValue(Action.LARGE_ICON_KEY);
            if (icon == null) {
                icon = (Icon) action.getValue(Action.SMALL_ICON);
            }
        }
        setIcon(icon);
    }

    void smallIconChanged(Action action) {
        if (action.getValue(Action.LARGE_ICON_KEY) == null) {
            setIconFromAction(action);
        }
    }

    void largeIconChanged(Action action) {
        setIconFromAction(action);
    }

    private void setActionCommandFromAction(Action action) {
        setActionCommand(action != null ? (String) action.getValue(Action.ACTION_COMMAND_KEY) : null);
    }

    private void setSelectedFromAction(Action action) {
        ButtonGroup group;
        boolean zIsSelected = false;
        if (action != null) {
            zIsSelected = AbstractAction.isSelected(action);
        }
        if (zIsSelected != isSelected()) {
            setSelected(zIsSelected);
            if (!zIsSelected && isSelected() && (getModel() instanceof DefaultButtonModel) && (group = ((DefaultButtonModel) getModel()).getGroup()) != null) {
                group.clearSelection();
            }
        }
    }

    protected PropertyChangeListener createActionPropertyChangeListener(Action action) {
        return createActionPropertyChangeListener0(action);
    }

    PropertyChangeListener createActionPropertyChangeListener0(Action action) {
        return new ButtonActionPropertyChangeListener(this, action);
    }

    /* loaded from: rt.jar:javax/swing/AbstractButton$ButtonActionPropertyChangeListener.class */
    private static class ButtonActionPropertyChangeListener extends ActionPropertyChangeListener<AbstractButton> {
        ButtonActionPropertyChangeListener(AbstractButton abstractButton, Action action) {
            super(abstractButton, action);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // javax.swing.ActionPropertyChangeListener
        public void actionPropertyChanged(AbstractButton abstractButton, Action action, PropertyChangeEvent propertyChangeEvent) throws IllegalArgumentException {
            if (AbstractAction.shouldReconfigure(propertyChangeEvent)) {
                abstractButton.configurePropertiesFromAction(action);
            } else {
                abstractButton.actionPropertyChanged(action, propertyChangeEvent.getPropertyName());
            }
        }
    }

    public boolean isBorderPainted() {
        return this.paintBorder;
    }

    public void setBorderPainted(boolean z2) {
        boolean z3 = this.paintBorder;
        this.paintBorder = z2;
        this.borderPaintedSet = true;
        firePropertyChange(BORDER_PAINTED_CHANGED_PROPERTY, z3, this.paintBorder);
        if (z2 != z3) {
            revalidate();
            repaint();
        }
    }

    @Override // javax.swing.JComponent
    protected void paintBorder(Graphics graphics) {
        if (isBorderPainted()) {
            super.paintBorder(graphics);
        }
    }

    public boolean isFocusPainted() {
        return this.paintFocus;
    }

    public void setFocusPainted(boolean z2) {
        boolean z3 = this.paintFocus;
        this.paintFocus = z2;
        firePropertyChange(FOCUS_PAINTED_CHANGED_PROPERTY, z3, this.paintFocus);
        if (z2 != z3 && isFocusOwner()) {
            revalidate();
            repaint();
        }
    }

    public boolean isContentAreaFilled() {
        return this.contentAreaFilled;
    }

    public void setContentAreaFilled(boolean z2) {
        boolean z3 = this.contentAreaFilled;
        this.contentAreaFilled = z2;
        this.contentAreaFilledSet = true;
        firePropertyChange(CONTENT_AREA_FILLED_CHANGED_PROPERTY, z3, this.contentAreaFilled);
        if (z2 != z3) {
            repaint();
        }
    }

    public boolean isRolloverEnabled() {
        return this.rolloverEnabled;
    }

    public void setRolloverEnabled(boolean z2) {
        boolean z3 = this.rolloverEnabled;
        this.rolloverEnabled = z2;
        this.rolloverEnabledSet = true;
        firePropertyChange(ROLLOVER_ENABLED_CHANGED_PROPERTY, z3, this.rolloverEnabled);
        if (z2 != z3) {
            repaint();
        }
    }

    public int getMnemonic() {
        return this.mnemonic;
    }

    public void setMnemonic(int i2) throws IllegalArgumentException {
        getMnemonic();
        this.model.setMnemonic(i2);
        updateMnemonicProperties();
    }

    public void setMnemonic(char c2) throws IllegalArgumentException {
        int i2 = c2;
        if (i2 >= 97 && i2 <= 122) {
            i2 -= 32;
        }
        setMnemonic(i2);
    }

    public void setDisplayedMnemonicIndex(int i2) throws IllegalArgumentException {
        int i3 = this.mnemonicIndex;
        if (i2 == -1) {
            this.mnemonicIndex = -1;
        } else {
            String text = getText();
            int length = text == null ? 0 : text.length();
            if (i2 < -1 || i2 >= length) {
                throw new IllegalArgumentException("index == " + i2);
            }
        }
        this.mnemonicIndex = i2;
        firePropertyChange("displayedMnemonicIndex", i3, i2);
        if (i2 != i3) {
            revalidate();
            repaint();
        }
    }

    public int getDisplayedMnemonicIndex() {
        return this.mnemonicIndex;
    }

    private void updateDisplayedMnemonicIndex(String str, int i2) throws IllegalArgumentException {
        setDisplayedMnemonicIndex(SwingUtilities.findDisplayedMnemonicIndex(str, i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMnemonicProperties() throws IllegalArgumentException {
        int mnemonic = this.model.getMnemonic();
        if (this.mnemonic != mnemonic) {
            int i2 = this.mnemonic;
            this.mnemonic = mnemonic;
            firePropertyChange(MNEMONIC_CHANGED_PROPERTY, i2, this.mnemonic);
            updateDisplayedMnemonicIndex(getText(), this.mnemonic);
            revalidate();
            repaint();
        }
    }

    public void setMultiClickThreshhold(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException("threshhold must be >= 0");
        }
        this.multiClickThreshhold = j2;
    }

    public long getMultiClickThreshhold() {
        return this.multiClickThreshhold;
    }

    public ButtonModel getModel() {
        return this.model;
    }

    public void setModel(ButtonModel buttonModel) throws IllegalArgumentException {
        ButtonModel model = getModel();
        if (model != null) {
            model.removeChangeListener(this.changeListener);
            model.removeActionListener(this.actionListener);
            model.removeItemListener(this.itemListener);
            this.changeListener = null;
            this.actionListener = null;
            this.itemListener = null;
        }
        this.model = buttonModel;
        if (buttonModel != null) {
            this.changeListener = createChangeListener();
            this.actionListener = createActionListener();
            this.itemListener = createItemListener();
            buttonModel.addChangeListener(this.changeListener);
            buttonModel.addActionListener(this.actionListener);
            buttonModel.addItemListener(this.itemListener);
            updateMnemonicProperties();
            super.setEnabled(buttonModel.isEnabled());
        } else {
            this.mnemonic = 0;
        }
        updateDisplayedMnemonicIndex(getText(), this.mnemonic);
        firePropertyChange("model", model, buttonModel);
        if (buttonModel != model) {
            revalidate();
            repaint();
        }
    }

    public ButtonUI getUI() {
        return (ButtonUI) this.ui;
    }

    public void setUI(ButtonUI buttonUI) {
        super.setUI((ComponentUI) buttonUI);
        if (this.disabledIcon instanceof UIResource) {
            setDisabledIcon(null);
        }
        if (this.disabledSelectedIcon instanceof UIResource) {
            setDisabledSelectedIcon(null);
        }
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
    }

    @Override // java.awt.Container
    protected void addImpl(Component component, Object obj, int i2) {
        if (!this.setLayout) {
            setLayout(new OverlayLayout(this));
        }
        super.addImpl(component, obj, i2);
    }

    @Override // java.awt.Container
    public void setLayout(LayoutManager layoutManager) {
        this.setLayout = true;
        super.setLayout(layoutManager);
    }

    public void addChangeListener(ChangeListener changeListener) {
        this.listenerList.add(ChangeListener.class, changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener) {
        this.listenerList.remove(ChangeListener.class, changeListener);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) this.listenerList.getListeners(ChangeListener.class);
    }

    protected void fireStateChanged() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ChangeListener.class) {
                if (this.changeEvent == null) {
                    this.changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listenerList[length + 1]).stateChanged(this.changeEvent);
            }
        }
    }

    public void addActionListener(ActionListener actionListener) {
        this.listenerList.add(ActionListener.class, actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        if (actionListener != null && getAction() == actionListener) {
            setAction(null);
        } else {
            this.listenerList.remove(ActionListener.class, actionListener);
        }
    }

    public ActionListener[] getActionListeners() {
        return (ActionListener[]) this.listenerList.getListeners(ActionListener.class);
    }

    protected ChangeListener createChangeListener() {
        return getHandler();
    }

    /* loaded from: rt.jar:javax/swing/AbstractButton$ButtonChangeListener.class */
    protected class ButtonChangeListener implements ChangeListener, Serializable {
        ButtonChangeListener() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
            AbstractButton.this.getHandler().stateChanged(changeEvent);
        }
    }

    protected void fireActionPerformed(ActionEvent actionEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        ActionEvent actionEvent2 = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ActionListener.class) {
                if (actionEvent2 == null) {
                    String actionCommand = actionEvent.getActionCommand();
                    if (actionCommand == null) {
                        actionCommand = getActionCommand();
                    }
                    actionEvent2 = new ActionEvent(this, 1001, actionCommand, actionEvent.getWhen(), actionEvent.getModifiers());
                }
                ((ActionListener) listenerList[length + 1]).actionPerformed(actionEvent2);
            }
        }
    }

    protected void fireItemStateChanged(ItemEvent itemEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        ItemEvent itemEvent2 = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ItemListener.class) {
                if (itemEvent2 == null) {
                    itemEvent2 = new ItemEvent(this, 701, this, itemEvent.getStateChange());
                }
                ((ItemListener) listenerList[length + 1]).itemStateChanged(itemEvent2);
            }
        }
        if (this.accessibleContext != null) {
            if (itemEvent.getStateChange() == 1) {
                this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.SELECTED);
                this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY, 0, 1);
            } else {
                this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.SELECTED, null);
                this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY, 1, 0);
            }
        }
    }

    protected ActionListener createActionListener() {
        return getHandler();
    }

    protected ItemListener createItemListener() {
        return getHandler();
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        if (!z2 && this.model.isRollover()) {
            this.model.setRollover(false);
        }
        super.setEnabled(z2);
        this.model.setEnabled(z2);
    }

    @Deprecated
    public String getLabel() {
        return getText();
    }

    @Deprecated
    public void setLabel(String str) throws IllegalArgumentException {
        setText(str);
    }

    @Override // java.awt.ItemSelectable
    public void addItemListener(ItemListener itemListener) {
        this.listenerList.add(ItemListener.class, itemListener);
    }

    @Override // java.awt.ItemSelectable
    public void removeItemListener(ItemListener itemListener) {
        this.listenerList.remove(ItemListener.class, itemListener);
    }

    public ItemListener[] getItemListeners() {
        return (ItemListener[]) this.listenerList.getListeners(ItemListener.class);
    }

    @Override // java.awt.ItemSelectable
    public Object[] getSelectedObjects() {
        if (!isSelected()) {
            return null;
        }
        return new Object[]{getText()};
    }

    protected void init(String str, Icon icon) throws IllegalArgumentException {
        if (str != null) {
            setText(str);
        }
        if (icon != null) {
            setIcon(icon);
        }
        updateUI();
        setAlignmentX(0.0f);
        setAlignmentY(0.5f);
    }

    @Override // java.awt.Component, java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        Icon selectedIcon = null;
        if (!this.model.isEnabled()) {
            if (this.model.isSelected()) {
                selectedIcon = getDisabledSelectedIcon();
            } else {
                selectedIcon = getDisabledIcon();
            }
        } else if (this.model.isPressed() && this.model.isArmed()) {
            selectedIcon = getPressedIcon();
        } else if (isRolloverEnabled() && this.model.isRollover()) {
            if (this.model.isSelected()) {
                selectedIcon = getRolloverSelectedIcon();
            } else {
                selectedIcon = getRolloverIcon();
            }
        } else if (this.model.isSelected()) {
            selectedIcon = getSelectedIcon();
        }
        if (selectedIcon == null) {
            selectedIcon = getIcon();
        }
        if (selectedIcon == null || !SwingUtilities.doesIconReferenceImage(selectedIcon, image)) {
            return false;
        }
        return super.imageUpdate(image, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.JComponent
    void setUIProperty(String str, Object obj) {
        if (str == BORDER_PAINTED_CHANGED_PROPERTY) {
            if (!this.borderPaintedSet) {
                setBorderPainted(((Boolean) obj).booleanValue());
                this.borderPaintedSet = false;
                return;
            }
            return;
        }
        if (str == ROLLOVER_ENABLED_CHANGED_PROPERTY) {
            if (!this.rolloverEnabledSet) {
                setRolloverEnabled(((Boolean) obj).booleanValue());
                this.rolloverEnabledSet = false;
                return;
            }
            return;
        }
        if (str == "iconTextGap") {
            if (!this.iconTextGapSet) {
                setIconTextGap(((Number) obj).intValue());
                this.iconTextGapSet = false;
                return;
            }
            return;
        }
        if (str == CONTENT_AREA_FILLED_CHANGED_PROPERTY) {
            if (!this.contentAreaFilledSet) {
                setContentAreaFilled(((Boolean) obj).booleanValue());
                this.contentAreaFilledSet = false;
                return;
            }
            return;
        }
        super.setUIProperty(str, obj);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        String string = (this.defaultIcon == null || this.defaultIcon == this) ? "" : this.defaultIcon.toString();
        String string2 = (this.pressedIcon == null || this.pressedIcon == this) ? "" : this.pressedIcon.toString();
        String string3 = (this.disabledIcon == null || this.disabledIcon == this) ? "" : this.disabledIcon.toString();
        String string4 = (this.selectedIcon == null || this.selectedIcon == this) ? "" : this.selectedIcon.toString();
        String string5 = (this.disabledSelectedIcon == null || this.disabledSelectedIcon == this) ? "" : this.disabledSelectedIcon.toString();
        String string6 = (this.rolloverIcon == null || this.rolloverIcon == this) ? "" : this.rolloverIcon.toString();
        String string7 = (this.rolloverSelectedIcon == null || this.rolloverSelectedIcon == this) ? "" : this.rolloverSelectedIcon.toString();
        return super.paramString() + ",defaultIcon=" + string + ",disabledIcon=" + string3 + ",disabledSelectedIcon=" + string5 + ",margin=" + ((Object) this.margin) + ",paintBorder=" + (this.paintBorder ? "true" : "false") + ",paintFocus=" + (this.paintFocus ? "true" : "false") + ",pressedIcon=" + string2 + ",rolloverEnabled=" + (this.rolloverEnabled ? "true" : "false") + ",rolloverIcon=" + string6 + ",rolloverSelectedIcon=" + string7 + ",selectedIcon=" + string4 + ",text=" + this.text;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    /* loaded from: rt.jar:javax/swing/AbstractButton$Handler.class */
    class Handler implements ActionListener, ChangeListener, ItemListener, Serializable {
        Handler() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
            changeEvent.getSource();
            AbstractButton.this.updateMnemonicProperties();
            if (AbstractButton.this.isEnabled() != AbstractButton.this.model.isEnabled()) {
                AbstractButton.this.setEnabled(AbstractButton.this.model.isEnabled());
            }
            AbstractButton.this.fireStateChanged();
            AbstractButton.this.repaint();
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            AbstractButton.this.fireActionPerformed(actionEvent);
        }

        @Override // java.awt.event.ItemListener
        public void itemStateChanged(ItemEvent itemEvent) {
            Action action;
            boolean zIsSelected;
            AbstractButton.this.fireItemStateChanged(itemEvent);
            if (AbstractButton.this.shouldUpdateSelectedStateFromAction() && (action = AbstractButton.this.getAction()) != null && AbstractAction.hasSelectedKey(action) && AbstractAction.isSelected(action) != (zIsSelected = AbstractButton.this.isSelected())) {
                action.putValue(Action.SELECTED_KEY, Boolean.valueOf(zIsSelected));
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/AbstractButton$AccessibleAbstractButton.class */
    protected abstract class AccessibleAbstractButton extends JComponent.AccessibleJComponent implements AccessibleAction, AccessibleValue, AccessibleText, AccessibleExtendedComponent {
        protected AccessibleAbstractButton() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            String accessibleName = this.accessibleName;
            if (accessibleName == null) {
                accessibleName = (String) AbstractButton.this.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY);
            }
            if (accessibleName == null) {
                accessibleName = AbstractButton.this.getText();
            }
            if (accessibleName == null) {
                accessibleName = super.getAccessibleName();
            }
            return accessibleName;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleIcon[] getAccessibleIcon() {
            Object accessibleContext;
            Icon icon = AbstractButton.this.getIcon();
            if ((icon instanceof Accessible) && (accessibleContext = ((Accessible) icon).getAccessibleContext()) != null && (accessibleContext instanceof AccessibleIcon)) {
                return new AccessibleIcon[]{(AccessibleIcon) accessibleContext};
            }
            return null;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (AbstractButton.this.getModel().isArmed()) {
                accessibleStateSet.add(AccessibleState.ARMED);
            }
            if (AbstractButton.this.isFocusOwner()) {
                accessibleStateSet.add(AccessibleState.FOCUSED);
            }
            if (AbstractButton.this.getModel().isPressed()) {
                accessibleStateSet.add(AccessibleState.PRESSED);
            }
            if (AbstractButton.this.isSelected()) {
                accessibleStateSet.add(AccessibleState.CHECKED);
            }
            return accessibleStateSet;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleRelationSet getAccessibleRelationSet() {
            ButtonModel model;
            ButtonGroup group;
            AccessibleRelationSet accessibleRelationSet = super.getAccessibleRelationSet();
            if (!accessibleRelationSet.contains(AccessibleRelation.MEMBER_OF) && (model = AbstractButton.this.getModel()) != null && (model instanceof DefaultButtonModel) && (group = ((DefaultButtonModel) model).getGroup()) != null) {
                int buttonCount = group.getButtonCount();
                Object[] objArr = new Object[buttonCount];
                Enumeration<AbstractButton> elements = group.getElements();
                for (int i2 = 0; i2 < buttonCount; i2++) {
                    if (elements.hasMoreElements()) {
                        objArr[i2] = elements.nextElement2();
                    }
                }
                AccessibleRelation accessibleRelation = new AccessibleRelation(AccessibleRelation.MEMBER_OF);
                accessibleRelation.setTarget(objArr);
                accessibleRelationSet.add(accessibleRelation);
            }
            return accessibleRelationSet;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        @Override // javax.accessibility.AccessibleAction
        public int getAccessibleActionCount() {
            return 1;
        }

        @Override // javax.accessibility.AccessibleAction
        public String getAccessibleActionDescription(int i2) {
            if (i2 == 0) {
                return UIManager.getString("AbstractButton.clickText");
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleAction
        public boolean doAccessibleAction(int i2) {
            if (i2 == 0) {
                AbstractButton.this.doClick();
                return true;
            }
            return false;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getCurrentAccessibleValue() {
            if (AbstractButton.this.isSelected()) {
                return 1;
            }
            return 0;
        }

        @Override // javax.accessibility.AccessibleValue
        public boolean setCurrentAccessibleValue(Number number) {
            if (number == null) {
                return false;
            }
            if (number.intValue() == 0) {
                AbstractButton.this.setSelected(false);
                return true;
            }
            AbstractButton.this.setSelected(true);
            return true;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMinimumAccessibleValue() {
            return 0;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMaximumAccessibleValue() {
            return 1;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleText getAccessibleText() {
            if (((View) AbstractButton.this.getClientProperty("html")) != null) {
                return this;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public int getIndexAtPoint(Point point) {
            Rectangle textRectangle;
            View view = (View) AbstractButton.this.getClientProperty("html");
            if (view == null || (textRectangle = getTextRectangle()) == null) {
                return -1;
            }
            return view.viewToModel(point.f12370x, point.f12371y, new Rectangle2D.Float(textRectangle.f12372x, textRectangle.f12373y, textRectangle.width, textRectangle.height), new Position.Bias[1]);
        }

        @Override // javax.accessibility.AccessibleText
        public Rectangle getCharacterBounds(int i2) {
            Rectangle textRectangle;
            View view = (View) AbstractButton.this.getClientProperty("html");
            if (view == null || (textRectangle = getTextRectangle()) == null) {
                return null;
            }
            try {
                return view.modelToView(i2, new Rectangle2D.Float(textRectangle.f12372x, textRectangle.f12373y, textRectangle.width, textRectangle.height), Position.Bias.Forward).getBounds();
            } catch (BadLocationException e2) {
                return null;
            }
        }

        @Override // javax.accessibility.AccessibleText
        public int getCharCount() {
            View view = (View) AbstractButton.this.getClientProperty("html");
            if (view != null) {
                Document document = view.getDocument();
                if (document instanceof StyledDocument) {
                    return ((StyledDocument) document).getLength();
                }
            }
            return AbstractButton.this.accessibleContext.getAccessibleName().length();
        }

        @Override // javax.accessibility.AccessibleText
        public int getCaretPosition() {
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public String getAtIndex(int i2, int i3) {
            if (i3 < 0 || i3 >= getCharCount()) {
                return null;
            }
            switch (i2) {
                case 1:
                    try {
                        return getText(i3, 1);
                    } catch (BadLocationException e2) {
                        return null;
                    }
                case 2:
                    try {
                        String text = getText(0, getCharCount());
                        BreakIterator wordInstance = BreakIterator.getWordInstance(getLocale());
                        wordInstance.setText(text);
                        return text.substring(wordInstance.previous(), wordInstance.following(i3));
                    } catch (BadLocationException e3) {
                        return null;
                    }
                case 3:
                    try {
                        String text2 = getText(0, getCharCount());
                        BreakIterator sentenceInstance = BreakIterator.getSentenceInstance(getLocale());
                        sentenceInstance.setText(text2);
                        return text2.substring(sentenceInstance.previous(), sentenceInstance.following(i3));
                    } catch (BadLocationException e4) {
                        return null;
                    }
                default:
                    return null;
            }
        }

        @Override // javax.accessibility.AccessibleText
        public String getAfterIndex(int i2, int i3) {
            int iFollowing;
            int iFollowing2;
            if (i3 < 0 || i3 >= getCharCount()) {
                return null;
            }
            switch (i2) {
                case 1:
                    if (i3 + 1 >= getCharCount()) {
                        return null;
                    }
                    try {
                        return getText(i3 + 1, 1);
                    } catch (BadLocationException e2) {
                        return null;
                    }
                case 2:
                    try {
                        String text = getText(0, getCharCount());
                        BreakIterator wordInstance = BreakIterator.getWordInstance(getLocale());
                        wordInstance.setText(text);
                        int iFollowing3 = wordInstance.following(i3);
                        if (iFollowing3 == -1 || iFollowing3 >= text.length() || (iFollowing = wordInstance.following(iFollowing3)) == -1 || iFollowing >= text.length()) {
                            return null;
                        }
                        return text.substring(iFollowing3, iFollowing);
                    } catch (BadLocationException e3) {
                        return null;
                    }
                case 3:
                    try {
                        String text2 = getText(0, getCharCount());
                        BreakIterator sentenceInstance = BreakIterator.getSentenceInstance(getLocale());
                        sentenceInstance.setText(text2);
                        int iFollowing4 = sentenceInstance.following(i3);
                        if (iFollowing4 == -1 || iFollowing4 > text2.length() || (iFollowing2 = sentenceInstance.following(iFollowing4)) == -1 || iFollowing2 > text2.length()) {
                            return null;
                        }
                        return text2.substring(iFollowing4, iFollowing2);
                    } catch (BadLocationException e4) {
                        return null;
                    }
                default:
                    return null;
            }
        }

        @Override // javax.accessibility.AccessibleText
        public String getBeforeIndex(int i2, int i3) {
            if (i3 < 0 || i3 > getCharCount() - 1) {
                return null;
            }
            switch (i2) {
                case 1:
                    if (i3 == 0) {
                        return null;
                    }
                    try {
                        return getText(i3 - 1, 1);
                    } catch (BadLocationException e2) {
                        return null;
                    }
                case 2:
                    try {
                        String text = getText(0, getCharCount());
                        BreakIterator wordInstance = BreakIterator.getWordInstance(getLocale());
                        wordInstance.setText(text);
                        wordInstance.following(i3);
                        int iPrevious = wordInstance.previous();
                        int iPrevious2 = wordInstance.previous();
                        if (iPrevious2 == -1) {
                            return null;
                        }
                        return text.substring(iPrevious2, iPrevious);
                    } catch (BadLocationException e3) {
                        return null;
                    }
                case 3:
                    try {
                        String text2 = getText(0, getCharCount());
                        BreakIterator sentenceInstance = BreakIterator.getSentenceInstance(getLocale());
                        sentenceInstance.setText(text2);
                        sentenceInstance.following(i3);
                        int iPrevious3 = sentenceInstance.previous();
                        int iPrevious4 = sentenceInstance.previous();
                        if (iPrevious4 == -1) {
                            return null;
                        }
                        return text2.substring(iPrevious4, iPrevious3);
                    } catch (BadLocationException e4) {
                        return null;
                    }
                default:
                    return null;
            }
        }

        @Override // javax.accessibility.AccessibleText
        public AttributeSet getCharacterAttribute(int i2) {
            Element characterElement;
            View view = (View) AbstractButton.this.getClientProperty("html");
            if (view != null) {
                Document document = view.getDocument();
                if ((document instanceof StyledDocument) && (characterElement = ((StyledDocument) document).getCharacterElement(i2)) != null) {
                    return characterElement.getAttributes();
                }
                return null;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public int getSelectionStart() {
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public int getSelectionEnd() {
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public String getSelectedText() {
            return null;
        }

        private String getText(int i2, int i3) throws BadLocationException {
            View view = (View) AbstractButton.this.getClientProperty("html");
            if (view != null) {
                Document document = view.getDocument();
                if (document instanceof StyledDocument) {
                    return ((StyledDocument) document).getText(i2, i3);
                }
                return null;
            }
            return null;
        }

        private Rectangle getTextRectangle() {
            String text = AbstractButton.this.getText();
            Icon icon = AbstractButton.this.isEnabled() ? AbstractButton.this.getIcon() : AbstractButton.this.getDisabledIcon();
            if (icon == null && text == null) {
                return null;
            }
            Rectangle rectangle = new Rectangle();
            Rectangle rectangle2 = new Rectangle();
            Rectangle rectangle3 = new Rectangle();
            Insets insets = AbstractButton.this.getInsets(new Insets(0, 0, 0, 0));
            rectangle3.f12372x = insets.left;
            rectangle3.f12373y = insets.top;
            rectangle3.width = AbstractButton.this.getWidth() - (insets.left + insets.right);
            rectangle3.height = AbstractButton.this.getHeight() - (insets.top + insets.bottom);
            SwingUtilities.layoutCompoundLabel(AbstractButton.this, getFontMetrics(getFont()), text, icon, AbstractButton.this.getVerticalAlignment(), AbstractButton.this.getHorizontalAlignment(), AbstractButton.this.getVerticalTextPosition(), AbstractButton.this.getHorizontalTextPosition(), rectangle3, rectangle, rectangle2, 0);
            return rectangle2;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent
        AccessibleExtendedComponent getAccessibleExtendedComponent() {
            return this;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, javax.accessibility.AccessibleExtendedComponent
        public String getToolTipText() {
            return AbstractButton.this.getToolTipText();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, javax.accessibility.AccessibleExtendedComponent
        public String getTitledBorderText() {
            return super.getTitledBorderText();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, javax.accessibility.AccessibleExtendedComponent
        public AccessibleKeyBinding getAccessibleKeyBinding() {
            int mnemonic = AbstractButton.this.getMnemonic();
            if (mnemonic == 0) {
                return null;
            }
            return new ButtonKeyBinding(mnemonic);
        }

        /* loaded from: rt.jar:javax/swing/AbstractButton$AccessibleAbstractButton$ButtonKeyBinding.class */
        class ButtonKeyBinding implements AccessibleKeyBinding {
            int mnemonic;

            ButtonKeyBinding(int i2) {
                this.mnemonic = i2;
            }

            @Override // javax.accessibility.AccessibleKeyBinding
            public int getAccessibleKeyBindingCount() {
                return 1;
            }

            @Override // javax.accessibility.AccessibleKeyBinding
            public Object getAccessibleKeyBinding(int i2) {
                if (i2 != 0) {
                    throw new IllegalArgumentException();
                }
                return KeyStroke.getKeyStroke(this.mnemonic, 0);
            }
        }
    }
}
