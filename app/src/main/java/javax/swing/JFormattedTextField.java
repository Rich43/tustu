package javax.swing;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.InputMethodEvent;
import java.awt.im.InputContext;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.UIResource;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.InternationalFormatter;
import javax.swing.text.JTextComponent;
import javax.swing.text.NavigationFilter;
import javax.swing.text.NumberFormatter;
import javax.swing.text.TextAction;

/* loaded from: rt.jar:javax/swing/JFormattedTextField.class */
public class JFormattedTextField extends JTextField {
    private static final String uiClassID = "FormattedTextFieldUI";
    private static final Action[] defaultActions = {new CommitAction(), new CancelAction()};
    public static final int COMMIT = 0;
    public static final int COMMIT_OR_REVERT = 1;
    public static final int REVERT = 2;
    public static final int PERSIST = 3;
    private AbstractFormatterFactory factory;
    private AbstractFormatter format;
    private Object value;
    private boolean editValid;
    private int focusLostBehavior;
    private boolean edited;
    private DocumentListener documentListener;
    private Object mask;
    private ActionMap textFormatterActionMap;
    private boolean composedTextExists;
    private FocusLostHandler focusLostHandler;

    /* loaded from: rt.jar:javax/swing/JFormattedTextField$AbstractFormatterFactory.class */
    public static abstract class AbstractFormatterFactory {
        public abstract AbstractFormatter getFormatter(JFormattedTextField jFormattedTextField);
    }

    public JFormattedTextField() {
        this.composedTextExists = false;
        enableEvents(4L);
        setFocusLostBehavior(1);
    }

    public JFormattedTextField(Object obj) {
        this();
        setValue(obj);
    }

    public JFormattedTextField(Format format) {
        this();
        setFormatterFactory(getDefaultFormatterFactory(format));
    }

    public JFormattedTextField(AbstractFormatter abstractFormatter) {
        this((AbstractFormatterFactory) new DefaultFormatterFactory(abstractFormatter));
    }

    public JFormattedTextField(AbstractFormatterFactory abstractFormatterFactory) {
        this();
        setFormatterFactory(abstractFormatterFactory);
    }

    public JFormattedTextField(AbstractFormatterFactory abstractFormatterFactory, Object obj) {
        this(obj);
        setFormatterFactory(abstractFormatterFactory);
    }

    public void setFocusLostBehavior(int i2) {
        if (i2 != 0 && i2 != 1 && i2 != 3 && i2 != 2) {
            throw new IllegalArgumentException("setFocusLostBehavior must be one of: JFormattedTextField.COMMIT, JFormattedTextField.COMMIT_OR_REVERT, JFormattedTextField.PERSIST or JFormattedTextField.REVERT");
        }
        this.focusLostBehavior = i2;
    }

    public int getFocusLostBehavior() {
        return this.focusLostBehavior;
    }

    public void setFormatterFactory(AbstractFormatterFactory abstractFormatterFactory) {
        AbstractFormatterFactory abstractFormatterFactory2 = this.factory;
        this.factory = abstractFormatterFactory;
        firePropertyChange("formatterFactory", abstractFormatterFactory2, abstractFormatterFactory);
        setValue(getValue(), true, false);
    }

    public AbstractFormatterFactory getFormatterFactory() {
        return this.factory;
    }

    protected void setFormatter(AbstractFormatter abstractFormatter) {
        AbstractFormatter abstractFormatter2 = this.format;
        if (abstractFormatter2 != null) {
            abstractFormatter2.uninstall();
        }
        setEditValid(true);
        this.format = abstractFormatter;
        if (abstractFormatter != null) {
            abstractFormatter.install(this);
        }
        setEdited(false);
        firePropertyChange("textFormatter", abstractFormatter2, abstractFormatter);
    }

    public AbstractFormatter getFormatter() {
        return this.format;
    }

    public void setValue(Object obj) {
        if (obj != null && getFormatterFactory() == null) {
            setFormatterFactory(getDefaultFormatterFactory(obj));
        }
        setValue(obj, true, true);
    }

    public Object getValue() {
        return this.value;
    }

    public void commitEdit() throws ParseException {
        AbstractFormatter formatter = getFormatter();
        if (formatter != null) {
            setValue(formatter.stringToValue(getText()), false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEditValid(boolean z2) {
        if (z2 != this.editValid) {
            this.editValid = z2;
            firePropertyChange("editValid", Boolean.valueOf(!z2), Boolean.valueOf(z2));
        }
    }

    public boolean isEditValid() {
        return this.editValid;
    }

    protected void invalidEdit() {
        UIManager.getLookAndFeel().provideErrorFeedback(this);
    }

    @Override // javax.swing.text.JTextComponent, java.awt.Component
    protected void processInputMethodEvent(InputMethodEvent inputMethodEvent) {
        AttributedCharacterIterator text = inputMethodEvent.getText();
        int committedCharacterCount = inputMethodEvent.getCommittedCharacterCount();
        if (text != null) {
            this.composedTextExists = text.getEndIndex() - text.getBeginIndex() > committedCharacterCount;
        } else {
            this.composedTextExists = false;
        }
        super.processInputMethodEvent(inputMethodEvent);
    }

    @Override // java.awt.Component
    protected void processFocusEvent(FocusEvent focusEvent) {
        super.processFocusEvent(focusEvent);
        if (focusEvent.isTemporary()) {
            return;
        }
        if (isEdited() && focusEvent.getID() == 1005) {
            InputContext inputContext = getInputContext();
            if (this.focusLostHandler == null) {
                this.focusLostHandler = new FocusLostHandler();
            }
            if (inputContext != null && this.composedTextExists) {
                inputContext.endComposition();
                EventQueue.invokeLater(this.focusLostHandler);
                return;
            } else {
                this.focusLostHandler.run();
                return;
            }
        }
        if (!isEdited()) {
            setValue(getValue(), true, true);
        }
    }

    /* loaded from: rt.jar:javax/swing/JFormattedTextField$FocusLostHandler.class */
    private class FocusLostHandler implements Runnable, Serializable {
        private FocusLostHandler() {
        }

        @Override // java.lang.Runnable
        public void run() {
            int focusLostBehavior = JFormattedTextField.this.getFocusLostBehavior();
            if (focusLostBehavior == 0 || focusLostBehavior == 1) {
                try {
                    JFormattedTextField.this.commitEdit();
                    JFormattedTextField.this.setValue(JFormattedTextField.this.getValue(), true, true);
                    return;
                } catch (ParseException e2) {
                    JFormattedTextField jFormattedTextField = JFormattedTextField.this;
                    if (focusLostBehavior == 1) {
                        JFormattedTextField.this.setValue(JFormattedTextField.this.getValue(), true, true);
                        return;
                    }
                    return;
                }
            }
            if (focusLostBehavior == 2) {
                JFormattedTextField.this.setValue(JFormattedTextField.this.getValue(), true, true);
            }
        }
    }

    @Override // javax.swing.JTextField, javax.swing.text.JTextComponent
    public Action[] getActions() {
        return TextAction.augmentList(super.getActions(), defaultActions);
    }

    @Override // javax.swing.JTextField, javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    @Override // javax.swing.JTextField, javax.swing.text.JTextComponent
    public void setDocument(Document document) {
        if (this.documentListener != null && getDocument() != null) {
            getDocument().removeDocumentListener(this.documentListener);
        }
        super.setDocument(document);
        if (this.documentListener == null) {
            this.documentListener = new DocumentHandler();
        }
        document.addDocumentListener(this.documentListener);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void setFormatterActions(Action[] actionArr) {
        if (actionArr == null) {
            if (this.textFormatterActionMap != null) {
                this.textFormatterActionMap.clear();
                return;
            }
            return;
        }
        if (this.textFormatterActionMap == null) {
            ActionMap actionMap = getActionMap();
            this.textFormatterActionMap = new ActionMap();
            while (actionMap != null) {
                ActionMap parent = actionMap.getParent();
                if ((parent instanceof UIResource) || parent == null) {
                    actionMap.setParent(this.textFormatterActionMap);
                    this.textFormatterActionMap.setParent(parent);
                    break;
                }
                actionMap = parent;
            }
        }
        for (int length = actionArr.length - 1; length >= 0; length--) {
            Object value = actionArr[length].getValue("Name");
            if (value != null) {
                this.textFormatterActionMap.put(value, actionArr[length]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setValue(Object obj, boolean z2, boolean z3) {
        AbstractFormatter formatter;
        Object obj2 = this.value;
        this.value = obj;
        if (z2) {
            AbstractFormatterFactory formatterFactory = getFormatterFactory();
            if (formatterFactory != null) {
                formatter = formatterFactory.getFormatter(this);
            } else {
                formatter = null;
            }
            setFormatter(formatter);
        } else {
            setEditValid(true);
        }
        setEdited(false);
        if (z3) {
            firePropertyChange("value", obj2, obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEdited(boolean z2) {
        this.edited = z2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEdited() {
        return this.edited;
    }

    private AbstractFormatterFactory getDefaultFormatterFactory(Object obj) {
        if (obj instanceof DateFormat) {
            return new DefaultFormatterFactory(new DateFormatter((DateFormat) obj));
        }
        if (obj instanceof NumberFormat) {
            return new DefaultFormatterFactory(new NumberFormatter((NumberFormat) obj));
        }
        if (obj instanceof Format) {
            return new DefaultFormatterFactory(new InternationalFormatter((Format) obj));
        }
        if (obj instanceof Date) {
            return new DefaultFormatterFactory(new DateFormatter());
        }
        if (obj instanceof Number) {
            NumberFormatter numberFormatter = new NumberFormatter();
            numberFormatter.setValueClass(obj.getClass());
            NumberFormatter numberFormatter2 = new NumberFormatter(new DecimalFormat("#.#"));
            numberFormatter2.setValueClass(obj.getClass());
            return new DefaultFormatterFactory(numberFormatter, numberFormatter, numberFormatter2);
        }
        return new DefaultFormatterFactory(new DefaultFormatter());
    }

    /* loaded from: rt.jar:javax/swing/JFormattedTextField$AbstractFormatter.class */
    public static abstract class AbstractFormatter implements Serializable {
        private JFormattedTextField ftf;

        public abstract Object stringToValue(String str) throws ParseException;

        public abstract String valueToString(Object obj) throws ParseException;

        public void install(JFormattedTextField jFormattedTextField) {
            if (this.ftf != null) {
                uninstall();
            }
            this.ftf = jFormattedTextField;
            if (jFormattedTextField != null) {
                try {
                    jFormattedTextField.setText(valueToString(jFormattedTextField.getValue()));
                } catch (ParseException e2) {
                    jFormattedTextField.setText("");
                    setEditValid(false);
                }
                installDocumentFilter(getDocumentFilter());
                jFormattedTextField.setNavigationFilter(getNavigationFilter());
                jFormattedTextField.setFormatterActions(getActions());
            }
        }

        public void uninstall() {
            if (this.ftf != null) {
                installDocumentFilter(null);
                this.ftf.setNavigationFilter(null);
                this.ftf.setFormatterActions(null);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public JFormattedTextField getFormattedTextField() {
            return this.ftf;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void invalidEdit() {
            JFormattedTextField formattedTextField = getFormattedTextField();
            if (formattedTextField != null) {
                formattedTextField.invalidEdit();
            }
        }

        protected void setEditValid(boolean z2) {
            JFormattedTextField formattedTextField = getFormattedTextField();
            if (formattedTextField != null) {
                formattedTextField.setEditValid(z2);
            }
        }

        protected Action[] getActions() {
            return null;
        }

        protected DocumentFilter getDocumentFilter() {
            return null;
        }

        protected NavigationFilter getNavigationFilter() {
            return null;
        }

        protected Object clone() throws CloneNotSupportedException {
            AbstractFormatter abstractFormatter = (AbstractFormatter) super.clone();
            abstractFormatter.ftf = null;
            return abstractFormatter;
        }

        private void installDocumentFilter(DocumentFilter documentFilter) {
            JFormattedTextField formattedTextField = getFormattedTextField();
            if (formattedTextField != null) {
                Document document = formattedTextField.getDocument();
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).setDocumentFilter(documentFilter);
                }
                document.putProperty(DocumentFilter.class, null);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/JFormattedTextField$CommitAction.class */
    static class CommitAction extends JTextField.NotifyAction {
        CommitAction() {
        }

        @Override // javax.swing.JTextField.NotifyAction, java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent focusedComponent = getFocusedComponent();
            if (focusedComponent instanceof JFormattedTextField) {
                try {
                    ((JFormattedTextField) focusedComponent).commitEdit();
                } catch (ParseException e2) {
                    ((JFormattedTextField) focusedComponent).invalidEdit();
                    return;
                }
            }
            super.actionPerformed(actionEvent);
        }

        @Override // javax.swing.JTextField.NotifyAction, javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            JTextComponent focusedComponent = getFocusedComponent();
            if (focusedComponent instanceof JFormattedTextField) {
                if (!((JFormattedTextField) focusedComponent).isEdited()) {
                    return false;
                }
                return true;
            }
            return super.isEnabled();
        }
    }

    /* loaded from: rt.jar:javax/swing/JFormattedTextField$CancelAction.class */
    private static class CancelAction extends TextAction {
        public CancelAction() {
            super("reset-field-edit");
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent focusedComponent = getFocusedComponent();
            if (focusedComponent instanceof JFormattedTextField) {
                JFormattedTextField jFormattedTextField = (JFormattedTextField) focusedComponent;
                jFormattedTextField.setValue(jFormattedTextField.getValue());
            }
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            JTextComponent focusedComponent = getFocusedComponent();
            if (focusedComponent instanceof JFormattedTextField) {
                if (!((JFormattedTextField) focusedComponent).isEdited()) {
                    return false;
                }
                return true;
            }
            return super.isEnabled();
        }
    }

    /* loaded from: rt.jar:javax/swing/JFormattedTextField$DocumentHandler.class */
    private class DocumentHandler implements DocumentListener, Serializable {
        private DocumentHandler() {
        }

        @Override // javax.swing.event.DocumentListener
        public void insertUpdate(DocumentEvent documentEvent) {
            JFormattedTextField.this.setEdited(true);
        }

        @Override // javax.swing.event.DocumentListener
        public void removeUpdate(DocumentEvent documentEvent) {
            JFormattedTextField.this.setEdited(true);
        }

        @Override // javax.swing.event.DocumentListener
        public void changedUpdate(DocumentEvent documentEvent) {
        }
    }
}
