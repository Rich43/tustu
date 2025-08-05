package javax.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.EventObject;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

/* loaded from: rt.jar:javax/swing/DefaultCellEditor.class */
public class DefaultCellEditor extends AbstractCellEditor implements TableCellEditor, TreeCellEditor {
    protected JComponent editorComponent;
    protected EditorDelegate delegate;
    protected int clickCountToStart;

    @ConstructorProperties({"component"})
    public DefaultCellEditor(final JTextField jTextField) {
        this.clickCountToStart = 1;
        this.editorComponent = jTextField;
        this.clickCountToStart = 2;
        this.delegate = new EditorDelegate() { // from class: javax.swing.DefaultCellEditor.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // javax.swing.DefaultCellEditor.EditorDelegate
            public void setValue(Object obj) {
                jTextField.setText(obj != null ? obj.toString() : "");
            }

            @Override // javax.swing.DefaultCellEditor.EditorDelegate
            public Object getCellEditorValue() {
                return jTextField.getText();
            }
        };
        jTextField.addActionListener(this.delegate);
    }

    public DefaultCellEditor(final JCheckBox jCheckBox) {
        this.clickCountToStart = 1;
        this.editorComponent = jCheckBox;
        this.delegate = new EditorDelegate() { // from class: javax.swing.DefaultCellEditor.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // javax.swing.DefaultCellEditor.EditorDelegate
            public void setValue(Object obj) {
                boolean zEquals = false;
                if (obj instanceof Boolean) {
                    zEquals = ((Boolean) obj).booleanValue();
                } else if (obj instanceof String) {
                    zEquals = obj.equals("true");
                }
                jCheckBox.setSelected(zEquals);
            }

            @Override // javax.swing.DefaultCellEditor.EditorDelegate
            public Object getCellEditorValue() {
                return Boolean.valueOf(jCheckBox.isSelected());
            }
        };
        jCheckBox.addActionListener(this.delegate);
        jCheckBox.setRequestFocusEnabled(false);
    }

    public DefaultCellEditor(final JComboBox jComboBox) {
        this.clickCountToStart = 1;
        this.editorComponent = jComboBox;
        jComboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        this.delegate = new EditorDelegate() { // from class: javax.swing.DefaultCellEditor.3
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // javax.swing.DefaultCellEditor.EditorDelegate
            public void setValue(Object obj) {
                jComboBox.setSelectedItem(obj);
            }

            @Override // javax.swing.DefaultCellEditor.EditorDelegate
            public Object getCellEditorValue() {
                return jComboBox.getSelectedItem();
            }

            @Override // javax.swing.DefaultCellEditor.EditorDelegate
            public boolean shouldSelectCell(EventObject eventObject) {
                return ((eventObject instanceof MouseEvent) && ((MouseEvent) eventObject).getID() == 506) ? false : true;
            }

            @Override // javax.swing.DefaultCellEditor.EditorDelegate
            public boolean stopCellEditing() {
                if (jComboBox.isEditable()) {
                    jComboBox.actionPerformed(new ActionEvent(DefaultCellEditor.this, 0, ""));
                }
                return super.stopCellEditing();
            }
        };
        jComboBox.addActionListener(this.delegate);
    }

    public Component getComponent() {
        return this.editorComponent;
    }

    public void setClickCountToStart(int i2) {
        this.clickCountToStart = i2;
    }

    public int getClickCountToStart() {
        return this.clickCountToStart;
    }

    @Override // javax.swing.CellEditor
    public Object getCellEditorValue() {
        return this.delegate.getCellEditorValue();
    }

    @Override // javax.swing.AbstractCellEditor, javax.swing.CellEditor
    public boolean isCellEditable(EventObject eventObject) {
        return this.delegate.isCellEditable(eventObject);
    }

    @Override // javax.swing.AbstractCellEditor, javax.swing.CellEditor
    public boolean shouldSelectCell(EventObject eventObject) {
        return this.delegate.shouldSelectCell(eventObject);
    }

    @Override // javax.swing.AbstractCellEditor, javax.swing.CellEditor
    public boolean stopCellEditing() {
        return this.delegate.stopCellEditing();
    }

    @Override // javax.swing.AbstractCellEditor, javax.swing.CellEditor
    public void cancelCellEditing() {
        this.delegate.cancelCellEditing();
    }

    @Override // javax.swing.tree.TreeCellEditor
    public Component getTreeCellEditorComponent(JTree jTree, Object obj, boolean z2, boolean z3, boolean z4, int i2) {
        this.delegate.setValue(jTree.convertValueToText(obj, z2, z3, z4, i2, false));
        return this.editorComponent;
    }

    public Component getTableCellEditorComponent(JTable jTable, Object obj, boolean z2, int i2, int i3) {
        this.delegate.setValue(obj);
        if (this.editorComponent instanceof JCheckBox) {
            Component tableCellRendererComponent = jTable.getCellRenderer(i2, i3).getTableCellRendererComponent(jTable, obj, z2, true, i2, i3);
            if (tableCellRendererComponent != null) {
                this.editorComponent.setOpaque(true);
                this.editorComponent.setBackground(tableCellRendererComponent.getBackground());
                if (tableCellRendererComponent instanceof JComponent) {
                    this.editorComponent.setBorder(((JComponent) tableCellRendererComponent).getBorder());
                }
            } else {
                this.editorComponent.setOpaque(false);
            }
        }
        return this.editorComponent;
    }

    /* loaded from: rt.jar:javax/swing/DefaultCellEditor$EditorDelegate.class */
    protected class EditorDelegate implements ActionListener, ItemListener, Serializable {
        protected Object value;

        protected EditorDelegate() {
        }

        public Object getCellEditorValue() {
            return this.value;
        }

        public void setValue(Object obj) {
            this.value = obj;
        }

        public boolean isCellEditable(EventObject eventObject) {
            return !(eventObject instanceof MouseEvent) || ((MouseEvent) eventObject).getClickCount() >= DefaultCellEditor.this.clickCountToStart;
        }

        public boolean shouldSelectCell(EventObject eventObject) {
            return true;
        }

        public boolean startCellEditing(EventObject eventObject) {
            return true;
        }

        public boolean stopCellEditing() {
            DefaultCellEditor.this.fireEditingStopped();
            return true;
        }

        public void cancelCellEditing() {
            DefaultCellEditor.this.fireEditingCanceled();
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            DefaultCellEditor.this.stopCellEditing();
        }

        @Override // java.awt.event.ItemListener
        public void itemStateChanged(ItemEvent itemEvent) {
            DefaultCellEditor.this.stopCellEditing();
        }
    }
}
