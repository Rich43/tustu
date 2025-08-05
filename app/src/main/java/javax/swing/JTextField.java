package javax.swing;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import javax.swing.text.TextAction;
import jdk.jfr.Enabled;

/* loaded from: rt.jar:javax/swing/JTextField.class */
public class JTextField extends JTextComponent implements SwingConstants {
    private Action action;
    private PropertyChangeListener actionPropertyChangeListener;
    public static final String notifyAction = "notify-field-accept";
    private BoundedRangeModel visibility;
    private int horizontalAlignment;
    private int columns;
    private int columnWidth;
    private String command;
    private static final Action[] defaultActions = {new NotifyAction()};
    private static final String uiClassID = "TextFieldUI";

    public JTextField() {
        this(null, null, 0);
    }

    public JTextField(String str) {
        this(null, str, 0);
    }

    public JTextField(int i2) {
        this(null, null, i2);
    }

    public JTextField(String str, int i2) {
        this(null, str, i2);
    }

    public JTextField(Document document, String str, int i2) {
        this.horizontalAlignment = 10;
        if (i2 < 0) {
            throw new IllegalArgumentException("columns less than zero.");
        }
        this.visibility = new DefaultBoundedRangeModel();
        this.visibility.addChangeListener(new ScrollRepainter());
        this.columns = i2;
        setDocument(document == null ? createDefaultModel() : document);
        if (str != null) {
            setText(str);
        }
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    @Override // javax.swing.text.JTextComponent
    public void setDocument(Document document) {
        if (document != null) {
            document.putProperty("filterNewlines", Boolean.TRUE);
        }
        super.setDocument(document);
    }

    @Override // javax.swing.JComponent, java.awt.Container
    public boolean isValidateRoot() {
        return !(SwingUtilities.getUnwrappedParent(this) instanceof JViewport);
    }

    public int getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    public void setHorizontalAlignment(int i2) {
        if (i2 == this.horizontalAlignment) {
            return;
        }
        int i3 = this.horizontalAlignment;
        if (i2 == 2 || i2 == 0 || i2 == 4 || i2 == 10 || i2 == 11) {
            this.horizontalAlignment = i2;
            firePropertyChange(AbstractButton.HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY, i3, this.horizontalAlignment);
            invalidate();
            repaint();
            return;
        }
        throw new IllegalArgumentException(AbstractButton.HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY);
    }

    protected Document createDefaultModel() {
        return new PlainDocument();
    }

    public int getColumns() {
        return this.columns;
    }

    public void setColumns(int i2) {
        int i3 = this.columns;
        if (i2 < 0) {
            throw new IllegalArgumentException("columns less than zero.");
        }
        if (i2 != i3) {
            this.columns = i2;
            invalidate();
        }
    }

    protected int getColumnWidth() {
        if (this.columnWidth == 0) {
            this.columnWidth = getFontMetrics(getFont()).charWidth('m');
        }
        return this.columnWidth;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        if (this.columns != 0) {
            Insets insets = getInsets();
            preferredSize.width = (this.columns * getColumnWidth()) + insets.left + insets.right;
        }
        return preferredSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        super.setFont(font);
        this.columnWidth = 0;
    }

    public synchronized void addActionListener(ActionListener actionListener) {
        this.listenerList.add(ActionListener.class, actionListener);
    }

    public synchronized void removeActionListener(ActionListener actionListener) {
        if (actionListener != null && getAction() == actionListener) {
            setAction(null);
        } else {
            this.listenerList.remove(ActionListener.class, actionListener);
        }
    }

    public synchronized ActionListener[] getActionListeners() {
        return (ActionListener[]) this.listenerList.getListeners(ActionListener.class);
    }

    protected void fireActionPerformed() {
        Object[] listenerList = this.listenerList.getListenerList();
        int modifiers = 0;
        AWTEvent currentEvent = EventQueue.getCurrentEvent();
        if (currentEvent instanceof InputEvent) {
            modifiers = ((InputEvent) currentEvent).getModifiers();
        } else if (currentEvent instanceof ActionEvent) {
            modifiers = ((ActionEvent) currentEvent).getModifiers();
        }
        ActionEvent actionEvent = new ActionEvent(this, 1001, this.command != null ? this.command : getText(), EventQueue.getMostRecentEventTime(), modifiers);
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ActionListener.class) {
                ((ActionListener) listenerList[length + 1]).actionPerformed(actionEvent);
            }
        }
    }

    public void setActionCommand(String str) {
        this.command = str;
    }

    public void setAction(Action action) {
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

    protected void configurePropertiesFromAction(Action action) {
        AbstractAction.setEnabledFromAction(this, action);
        AbstractAction.setToolTipTextFromAction(this, action);
        setActionCommandFromAction(action);
    }

    protected void actionPropertyChanged(Action action, String str) {
        if (str == Action.ACTION_COMMAND_KEY) {
            setActionCommandFromAction(action);
        } else if (str == Enabled.NAME) {
            AbstractAction.setEnabledFromAction(this, action);
        } else if (str == Action.SHORT_DESCRIPTION) {
            AbstractAction.setToolTipTextFromAction(this, action);
        }
    }

    private void setActionCommandFromAction(Action action) {
        setActionCommand(action == null ? null : (String) action.getValue(Action.ACTION_COMMAND_KEY));
    }

    protected PropertyChangeListener createActionPropertyChangeListener(Action action) {
        return new TextFieldActionPropertyChangeListener(this, action);
    }

    /* loaded from: rt.jar:javax/swing/JTextField$TextFieldActionPropertyChangeListener.class */
    private static class TextFieldActionPropertyChangeListener extends ActionPropertyChangeListener<JTextField> {
        TextFieldActionPropertyChangeListener(JTextField jTextField, Action action) {
            super(jTextField, action);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // javax.swing.ActionPropertyChangeListener
        public void actionPropertyChanged(JTextField jTextField, Action action, PropertyChangeEvent propertyChangeEvent) {
            if (AbstractAction.shouldReconfigure(propertyChangeEvent)) {
                jTextField.configurePropertiesFromAction(action);
            } else {
                jTextField.actionPropertyChanged(action, propertyChangeEvent.getPropertyName());
            }
        }
    }

    @Override // javax.swing.text.JTextComponent
    public Action[] getActions() {
        return TextAction.augmentList(super.getActions(), defaultActions);
    }

    public void postActionEvent() {
        fireActionPerformed();
    }

    public BoundedRangeModel getHorizontalVisibility() {
        return this.visibility;
    }

    public int getScrollOffset() {
        return this.visibility.getValue();
    }

    public void setScrollOffset(int i2) {
        this.visibility.setValue(i2);
    }

    @Override // javax.swing.JComponent
    public void scrollRectToVisible(Rectangle rectangle) {
        int value = (rectangle.f12372x + this.visibility.getValue()) - getInsets().left;
        int i2 = value + rectangle.width;
        if (value < this.visibility.getValue()) {
            this.visibility.setValue(value);
        } else if (i2 > this.visibility.getValue() + this.visibility.getExtent()) {
            this.visibility.setValue(i2 - this.visibility.getExtent());
        }
    }

    boolean hasActionListener() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ActionListener.class) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: rt.jar:javax/swing/JTextField$NotifyAction.class */
    static class NotifyAction extends TextAction {
        NotifyAction() {
            super(JTextField.notifyAction);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent focusedComponent = getFocusedComponent();
            if (focusedComponent instanceof JTextField) {
                ((JTextField) focusedComponent).postActionEvent();
            }
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            JTextComponent focusedComponent = getFocusedComponent();
            if (focusedComponent instanceof JTextField) {
                return ((JTextField) focusedComponent).hasActionListener();
            }
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/JTextField$ScrollRepainter.class */
    class ScrollRepainter implements ChangeListener, Serializable {
        ScrollRepainter() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            JTextField.this.repaint();
        }
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

    @Override // javax.swing.text.JTextComponent, javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        String str;
        if (this.horizontalAlignment == 2) {
            str = "LEFT";
        } else if (this.horizontalAlignment == 0) {
            str = "CENTER";
        } else if (this.horizontalAlignment == 4) {
            str = "RIGHT";
        } else if (this.horizontalAlignment == 10) {
            str = "LEADING";
        } else if (this.horizontalAlignment == 11) {
            str = "TRAILING";
        } else {
            str = "";
        }
        return super.paramString() + ",columns=" + this.columns + ",columnWidth=" + this.columnWidth + ",command=" + (this.command != null ? this.command : "") + ",horizontalAlignment=" + str;
    }

    @Override // javax.swing.text.JTextComponent, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJTextField();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JTextField$AccessibleJTextField.class */
    protected class AccessibleJTextField extends JTextComponent.AccessibleJTextComponent {
        protected AccessibleJTextField() {
            super();
        }

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            accessibleStateSet.add(AccessibleState.SINGLE_LINE);
            return accessibleStateSet;
        }
    }
}
