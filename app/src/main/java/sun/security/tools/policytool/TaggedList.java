package sun.security.tools.policytool;

import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/TaggedList.class */
class TaggedList extends JList {
    private static final long serialVersionUID = -5676238110427785853L;
    private List<Object> data;

    public TaggedList(int i2, boolean z2) {
        super(new DefaultListModel());
        this.data = new LinkedList();
        setVisibleRowCount(i2);
        setSelectionMode(z2 ? 2 : 0);
    }

    public Object getObject(int i2) {
        return this.data.get(i2);
    }

    public void addTaggedItem(String str, Object obj) {
        ((DefaultListModel) getModel()).addElement(str);
        this.data.add(obj);
    }

    public void replaceTaggedItem(String str, Object obj, int i2) {
        ((DefaultListModel) getModel()).set(i2, str);
        this.data.set(i2, obj);
    }

    public void removeTaggedItem(int i2) {
        ((DefaultListModel) getModel()).remove(i2);
        this.data.remove(i2);
    }
}
