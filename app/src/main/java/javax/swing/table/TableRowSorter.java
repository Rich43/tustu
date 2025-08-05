package javax.swing.table;

import java.text.Collator;
import java.util.Comparator;
import javax.swing.DefaultRowSorter;
import javax.swing.table.TableModel;

/* loaded from: rt.jar:javax/swing/table/TableRowSorter.class */
public class TableRowSorter<M extends TableModel> extends DefaultRowSorter<M, Integer> {
    private static final Comparator COMPARABLE_COMPARATOR = new ComparableComparator();
    private M tableModel;
    private TableStringConverter stringConverter;

    public TableRowSorter() {
        this(null);
    }

    public TableRowSorter(M m2) {
        setModel(m2);
    }

    public void setModel(M m2) {
        this.tableModel = m2;
        setModelWrapper(new TableRowSorterModelWrapper());
    }

    public void setStringConverter(TableStringConverter tableStringConverter) {
        this.stringConverter = tableStringConverter;
    }

    public TableStringConverter getStringConverter() {
        return this.stringConverter;
    }

    @Override // javax.swing.DefaultRowSorter
    public Comparator<?> getComparator(int i2) {
        Comparator<?> comparator = super.getComparator(i2);
        if (comparator != null) {
            return comparator;
        }
        Class<?> columnClass = getModel().getColumnClass(i2);
        if (columnClass == String.class) {
            return Collator.getInstance();
        }
        if (Comparable.class.isAssignableFrom(columnClass)) {
            return COMPARABLE_COMPARATOR;
        }
        return Collator.getInstance();
    }

    @Override // javax.swing.DefaultRowSorter
    protected boolean useToString(int i2) {
        Class<?> columnClass;
        if (super.getComparator(i2) != null || (columnClass = getModel().getColumnClass(i2)) == String.class || Comparable.class.isAssignableFrom(columnClass)) {
            return false;
        }
        return true;
    }

    /* loaded from: rt.jar:javax/swing/table/TableRowSorter$TableRowSorterModelWrapper.class */
    private class TableRowSorterModelWrapper extends DefaultRowSorter.ModelWrapper<M, Integer> {
        private TableRowSorterModelWrapper() {
        }

        @Override // javax.swing.DefaultRowSorter.ModelWrapper
        public M getModel() {
            return (M) TableRowSorter.this.tableModel;
        }

        @Override // javax.swing.DefaultRowSorter.ModelWrapper
        public int getColumnCount() {
            if (TableRowSorter.this.tableModel == null) {
                return 0;
            }
            return TableRowSorter.this.tableModel.getColumnCount();
        }

        @Override // javax.swing.DefaultRowSorter.ModelWrapper
        public int getRowCount() {
            if (TableRowSorter.this.tableModel == null) {
                return 0;
            }
            return TableRowSorter.this.tableModel.getRowCount();
        }

        @Override // javax.swing.DefaultRowSorter.ModelWrapper
        public Object getValueAt(int i2, int i3) {
            return TableRowSorter.this.tableModel.getValueAt(i2, i3);
        }

        @Override // javax.swing.DefaultRowSorter.ModelWrapper
        public String getStringValueAt(int i2, int i3) {
            String string;
            TableStringConverter stringConverter = TableRowSorter.this.getStringConverter();
            if (stringConverter != null) {
                String string2 = stringConverter.toString(TableRowSorter.this.tableModel, i2, i3);
                if (string2 != null) {
                    return string2;
                }
                return "";
            }
            Object valueAt = getValueAt(i2, i3);
            if (valueAt == null || (string = valueAt.toString()) == null) {
                return "";
            }
            return string;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javax.swing.DefaultRowSorter.ModelWrapper
        public Integer getIdentifier(int i2) {
            return Integer.valueOf(i2);
        }
    }

    /* loaded from: rt.jar:javax/swing/table/TableRowSorter$ComparableComparator.class */
    private static class ComparableComparator implements Comparator {
        private ComparableComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            return ((Comparable) obj).compareTo(obj2);
        }
    }
}
