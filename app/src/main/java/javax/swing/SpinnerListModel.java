package javax.swing;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/* loaded from: rt.jar:javax/swing/SpinnerListModel.class */
public class SpinnerListModel extends AbstractSpinnerModel implements Serializable {
    private List list;
    private int index;

    public SpinnerListModel(List<?> list) {
        if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("SpinnerListModel(List) expects non-null non-empty List");
        }
        this.list = list;
        this.index = 0;
    }

    public SpinnerListModel(Object[] objArr) {
        if (objArr == null || objArr.length == 0) {
            throw new IllegalArgumentException("SpinnerListModel(Object[]) expects non-null non-empty Object[]");
        }
        this.list = Arrays.asList(objArr);
        this.index = 0;
    }

    public SpinnerListModel() {
        this(new Object[]{Constants.ELEMNAME_EMPTY_STRING});
    }

    public List<?> getList() {
        return this.list;
    }

    public void setList(List<?> list) {
        if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("invalid list");
        }
        if (!list.equals(this.list)) {
            this.list = list;
            this.index = 0;
            fireStateChanged();
        }
    }

    @Override // javax.swing.SpinnerModel
    public Object getValue() {
        return this.list.get(this.index);
    }

    @Override // javax.swing.SpinnerModel
    public void setValue(Object obj) {
        int iIndexOf = this.list.indexOf(obj);
        if (iIndexOf == -1) {
            throw new IllegalArgumentException("invalid sequence element");
        }
        if (iIndexOf != this.index) {
            this.index = iIndexOf;
            fireStateChanged();
        }
    }

    @Override // javax.swing.SpinnerModel
    public Object getNextValue() {
        if (this.index >= this.list.size() - 1) {
            return null;
        }
        return this.list.get(this.index + 1);
    }

    @Override // javax.swing.SpinnerModel
    public Object getPreviousValue() {
        if (this.index <= 0) {
            return null;
        }
        return this.list.get(this.index - 1);
    }

    Object findNextMatch(String str) {
        int size = this.list.size();
        if (size == 0) {
            return null;
        }
        int i2 = this.index;
        do {
            Object obj = this.list.get(i2);
            String string = obj.toString();
            if (string != null && string.startsWith(str)) {
                return obj;
            }
            i2 = (i2 + 1) % size;
        } while (i2 != this.index);
        return null;
    }
}
