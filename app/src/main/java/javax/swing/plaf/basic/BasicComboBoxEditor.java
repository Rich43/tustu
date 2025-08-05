package javax.swing.plaf.basic;

import com.sun.javafx.fxml.BeanAdapter;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.ComboBoxEditor;
import javax.swing.JTextField;
import javax.swing.border.Border;
import sun.reflect.misc.MethodUtil;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxEditor.class */
public class BasicComboBoxEditor implements ComboBoxEditor, FocusListener {
    protected JTextField editor = createEditorComponent();
    private Object oldValue;

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxEditor$UIResource.class */
    public static class UIResource extends BasicComboBoxEditor implements javax.swing.plaf.UIResource {
    }

    @Override // javax.swing.ComboBoxEditor
    public Component getEditorComponent() {
        return this.editor;
    }

    protected JTextField createEditorComponent() {
        BorderlessTextField borderlessTextField = new BorderlessTextField("", 9);
        borderlessTextField.setBorder(null);
        return borderlessTextField;
    }

    @Override // javax.swing.ComboBoxEditor
    public void setItem(Object obj) {
        String string;
        if (obj != null) {
            string = obj.toString();
            if (string == null) {
                string = "";
            }
            this.oldValue = obj;
        } else {
            string = "";
        }
        if (!string.equals(this.editor.getText())) {
            this.editor.setText(string);
        }
    }

    @Override // javax.swing.ComboBoxEditor
    public Object getItem() {
        Object text = this.editor.getText();
        if (this.oldValue != null && !(this.oldValue instanceof String)) {
            if (text.equals(this.oldValue.toString())) {
                return this.oldValue;
            }
            try {
                text = MethodUtil.invoke(MethodUtil.getMethod(this.oldValue.getClass(), BeanAdapter.VALUE_OF_METHOD_NAME, new Class[]{String.class}), this.oldValue, new Object[]{this.editor.getText()});
            } catch (Exception e2) {
            }
        }
        return text;
    }

    @Override // javax.swing.ComboBoxEditor
    public void selectAll() {
        this.editor.selectAll();
        this.editor.requestFocus();
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }

    @Override // javax.swing.ComboBoxEditor
    public void addActionListener(ActionListener actionListener) {
        this.editor.addActionListener(actionListener);
    }

    @Override // javax.swing.ComboBoxEditor
    public void removeActionListener(ActionListener actionListener) {
        this.editor.removeActionListener(actionListener);
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxEditor$BorderlessTextField.class */
    static class BorderlessTextField extends JTextField {
        public BorderlessTextField(String str, int i2) {
            super(str, i2);
        }

        @Override // javax.swing.text.JTextComponent
        public void setText(String str) {
            if (getText().equals(str)) {
                return;
            }
            super.setText(str);
        }

        @Override // javax.swing.JComponent
        public void setBorder(Border border) {
            if (!(border instanceof UIResource)) {
                super.setBorder(border);
            }
        }
    }
}
