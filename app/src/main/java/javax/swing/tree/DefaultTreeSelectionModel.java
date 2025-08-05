package javax.swing.tree;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/* loaded from: rt.jar:javax/swing/tree/DefaultTreeSelectionModel.class */
public class DefaultTreeSelectionModel implements Cloneable, Serializable, TreeSelectionModel {
    public static final String SELECTION_MODE_PROPERTY = "selectionMode";
    protected SwingPropertyChangeSupport changeSupport;
    protected TreePath[] selection;
    protected transient RowMapper rowMapper;
    protected TreePath leadPath;
    protected EventListenerList listenerList = new EventListenerList();
    protected DefaultListSelectionModel listSelectionModel = new DefaultListSelectionModel();
    protected int selectionMode = 4;
    protected int leadRow = -1;
    protected int leadIndex = -1;
    private Hashtable<TreePath, Boolean> uniquePaths = new Hashtable<>();
    private Hashtable<TreePath, Boolean> lastPaths = new Hashtable<>();
    private TreePath[] tempPaths = new TreePath[1];

    public void setRowMapper(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
        resetRowSelection();
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public RowMapper getRowMapper() {
        return this.rowMapper;
    }

    public void setSelectionMode(int i2) {
        int i3 = this.selectionMode;
        this.selectionMode = i2;
        if (this.selectionMode != 1 && this.selectionMode != 2 && this.selectionMode != 4) {
            this.selectionMode = 4;
        }
        if (i3 != this.selectionMode && this.changeSupport != null) {
            this.changeSupport.firePropertyChange(SELECTION_MODE_PROPERTY, Integer.valueOf(i3), Integer.valueOf(this.selectionMode));
        }
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public int getSelectionMode() {
        return this.selectionMode;
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public void setSelectionPath(TreePath treePath) {
        if (treePath == null) {
            setSelectionPaths(null);
        } else {
            setSelectionPaths(new TreePath[]{treePath});
        }
    }

    public void setSelectionPaths(TreePath[] treePathArr) {
        int length;
        int length2;
        TreePath[] treePathArr2 = treePathArr;
        if (treePathArr2 == null) {
            length = 0;
        } else {
            length = treePathArr2.length;
        }
        if (this.selection == null) {
            length2 = 0;
        } else {
            length2 = this.selection.length;
        }
        if (length + length2 != 0) {
            if (this.selectionMode == 1) {
                if (length > 1) {
                    treePathArr2 = new TreePath[]{treePathArr[0]};
                    length = 1;
                }
            } else if (this.selectionMode == 2 && length > 0 && !arePathsContiguous(treePathArr2)) {
                treePathArr2 = new TreePath[]{treePathArr[0]};
                length = 1;
            }
            TreePath treePath = this.leadPath;
            Vector<?> vector = new Vector<>(length + length2);
            ArrayList arrayList = new ArrayList(length);
            this.lastPaths.clear();
            this.leadPath = null;
            for (int i2 = 0; i2 < length; i2++) {
                TreePath treePath2 = treePathArr2[i2];
                if (treePath2 != null && this.lastPaths.get(treePath2) == null) {
                    this.lastPaths.put(treePath2, Boolean.TRUE);
                    if (this.uniquePaths.get(treePath2) == null) {
                        vector.addElement(new PathPlaceHolder(treePath2, true));
                    }
                    this.leadPath = treePath2;
                    arrayList.add(treePath2);
                }
            }
            TreePath[] treePathArr3 = (TreePath[]) arrayList.toArray(new TreePath[arrayList.size()]);
            for (int i3 = 0; i3 < length2; i3++) {
                if (this.selection[i3] != null && this.lastPaths.get(this.selection[i3]) == null) {
                    vector.addElement(new PathPlaceHolder(this.selection[i3], false));
                }
            }
            this.selection = treePathArr3;
            Hashtable<TreePath, Boolean> hashtable = this.uniquePaths;
            this.uniquePaths = this.lastPaths;
            this.lastPaths = hashtable;
            this.lastPaths.clear();
            insureUniqueness();
            updateLeadIndex();
            resetRowSelection();
            if (vector.size() > 0) {
                notifyPathChange(vector, treePath);
            }
        }
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public void addSelectionPath(TreePath treePath) {
        if (treePath != null) {
            addSelectionPaths(new TreePath[]{treePath});
        }
    }

    public void addSelectionPaths(TreePath[] treePathArr) {
        int length;
        int length2 = treePathArr == null ? 0 : treePathArr.length;
        if (length2 > 0) {
            if (this.selectionMode == 1) {
                setSelectionPaths(treePathArr);
                return;
            }
            if (this.selectionMode == 2 && !canPathsBeAdded(treePathArr)) {
                if (arePathsContiguous(treePathArr)) {
                    setSelectionPaths(treePathArr);
                    return;
                } else {
                    setSelectionPaths(new TreePath[]{treePathArr[0]});
                    return;
                }
            }
            TreePath treePath = this.leadPath;
            Vector<?> vector = null;
            if (this.selection == null) {
                length = 0;
            } else {
                length = this.selection.length;
            }
            this.lastPaths.clear();
            int i2 = 0;
            for (int i3 = 0; i3 < length2; i3++) {
                if (treePathArr[i3] != null) {
                    if (this.uniquePaths.get(treePathArr[i3]) == null) {
                        i2++;
                        if (vector == null) {
                            vector = new Vector<>();
                        }
                        vector.addElement(new PathPlaceHolder(treePathArr[i3], true));
                        this.uniquePaths.put(treePathArr[i3], Boolean.TRUE);
                        this.lastPaths.put(treePathArr[i3], Boolean.TRUE);
                    }
                    this.leadPath = treePathArr[i3];
                }
            }
            if (this.leadPath == null) {
                this.leadPath = treePath;
            }
            if (i2 > 0) {
                TreePath[] treePathArr2 = new TreePath[length + i2];
                if (length > 0) {
                    System.arraycopy(this.selection, 0, treePathArr2, 0, length);
                }
                if (i2 != treePathArr.length) {
                    Enumeration<TreePath> enumerationKeys = this.lastPaths.keys();
                    int i4 = length;
                    while (enumerationKeys.hasMoreElements()) {
                        int i5 = i4;
                        i4++;
                        treePathArr2[i5] = enumerationKeys.nextElement2();
                    }
                } else {
                    System.arraycopy(treePathArr, 0, treePathArr2, length, i2);
                }
                this.selection = treePathArr2;
                insureUniqueness();
                updateLeadIndex();
                resetRowSelection();
                notifyPathChange(vector, treePath);
            } else {
                this.leadPath = treePath;
            }
            this.lastPaths.clear();
        }
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public void removeSelectionPath(TreePath treePath) {
        if (treePath != null) {
            removeSelectionPaths(new TreePath[]{treePath});
        }
    }

    public void removeSelectionPaths(TreePath[] treePathArr) {
        if (treePathArr != null && this.selection != null && treePathArr.length > 0) {
            if (!canPathsBeRemoved(treePathArr)) {
                clearSelection();
                return;
            }
            Vector<?> vector = null;
            for (int length = treePathArr.length - 1; length >= 0; length--) {
                if (treePathArr[length] != null && this.uniquePaths.get(treePathArr[length]) != null) {
                    if (vector == null) {
                        vector = new Vector<>(treePathArr.length);
                    }
                    this.uniquePaths.remove(treePathArr[length]);
                    vector.addElement(new PathPlaceHolder(treePathArr[length], false));
                }
            }
            if (vector != null) {
                int size = vector.size();
                TreePath treePath = this.leadPath;
                if (size == this.selection.length) {
                    this.selection = null;
                } else {
                    Enumeration<TreePath> enumerationKeys = this.uniquePaths.keys();
                    int i2 = 0;
                    this.selection = new TreePath[this.selection.length - size];
                    while (enumerationKeys.hasMoreElements()) {
                        int i3 = i2;
                        i2++;
                        this.selection[i3] = enumerationKeys.nextElement2();
                    }
                }
                if (this.leadPath != null && this.uniquePaths.get(this.leadPath) == null) {
                    if (this.selection != null) {
                        this.leadPath = this.selection[this.selection.length - 1];
                    } else {
                        this.leadPath = null;
                    }
                } else if (this.selection != null) {
                    this.leadPath = this.selection[this.selection.length - 1];
                } else {
                    this.leadPath = null;
                }
                updateLeadIndex();
                resetRowSelection();
                notifyPathChange(vector, treePath);
            }
        }
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public TreePath getSelectionPath() {
        if (this.selection != null && this.selection.length > 0) {
            return this.selection[0];
        }
        return null;
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public TreePath[] getSelectionPaths() {
        if (this.selection != null) {
            int length = this.selection.length;
            TreePath[] treePathArr = new TreePath[length];
            System.arraycopy(this.selection, 0, treePathArr, 0, length);
            return treePathArr;
        }
        return new TreePath[0];
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public int getSelectionCount() {
        if (this.selection == null) {
            return 0;
        }
        return this.selection.length;
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public boolean isPathSelected(TreePath treePath) {
        return (treePath == null || this.uniquePaths.get(treePath) == null) ? false : true;
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public boolean isSelectionEmpty() {
        return this.selection == null || this.selection.length == 0;
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public void clearSelection() {
        if (this.selection != null && this.selection.length > 0) {
            int length = this.selection.length;
            boolean[] zArr = new boolean[length];
            for (int i2 = 0; i2 < length; i2++) {
                zArr[i2] = false;
            }
            TreeSelectionEvent treeSelectionEvent = new TreeSelectionEvent(this, this.selection, zArr, this.leadPath, (TreePath) null);
            this.leadPath = null;
            this.leadRow = -1;
            this.leadIndex = -1;
            this.uniquePaths.clear();
            this.selection = null;
            resetRowSelection();
            fireValueChanged(treeSelectionEvent);
        }
    }

    public void addTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
        this.listenerList.add(TreeSelectionListener.class, treeSelectionListener);
    }

    public void removeTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
        this.listenerList.remove(TreeSelectionListener.class, treeSelectionListener);
    }

    public TreeSelectionListener[] getTreeSelectionListeners() {
        return (TreeSelectionListener[]) this.listenerList.getListeners(TreeSelectionListener.class);
    }

    protected void fireValueChanged(TreeSelectionEvent treeSelectionEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TreeSelectionListener.class) {
                ((TreeSelectionListener) listenerList[length + 1]).valueChanged(treeSelectionEvent);
            }
        }
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        return (T[]) this.listenerList.getListeners(cls);
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public int[] getSelectionRows() {
        if (this.rowMapper != null && this.selection != null && this.selection.length > 0) {
            int[] rowsForPaths = this.rowMapper.getRowsForPaths(this.selection);
            if (rowsForPaths != null) {
                int i2 = 0;
                for (int length = rowsForPaths.length - 1; length >= 0; length--) {
                    if (rowsForPaths[length] == -1) {
                        i2++;
                    }
                }
                if (i2 > 0) {
                    if (i2 == rowsForPaths.length) {
                        rowsForPaths = null;
                    } else {
                        int[] iArr = new int[rowsForPaths.length - i2];
                        int i3 = 0;
                        for (int length2 = rowsForPaths.length - 1; length2 >= 0; length2--) {
                            if (rowsForPaths[length2] != -1) {
                                int i4 = i3;
                                i3++;
                                iArr[i4] = rowsForPaths[length2];
                            }
                        }
                        rowsForPaths = iArr;
                    }
                }
            }
            return rowsForPaths;
        }
        return new int[0];
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public int getMinSelectionRow() {
        return this.listSelectionModel.getMinSelectionIndex();
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public int getMaxSelectionRow() {
        return this.listSelectionModel.getMaxSelectionIndex();
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public boolean isRowSelected(int i2) {
        return this.listSelectionModel.isSelectedIndex(i2);
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public void resetRowSelection() {
        this.listSelectionModel.clearSelection();
        if (this.selection != null && this.rowMapper != null) {
            int[] rowsForPaths = this.rowMapper.getRowsForPaths(this.selection);
            int length = this.selection.length;
            for (int i2 = 0; i2 < length; i2++) {
                int i3 = rowsForPaths[i2];
                if (i3 != -1) {
                    this.listSelectionModel.addSelectionInterval(i3, i3);
                }
            }
            if (this.leadIndex != -1 && rowsForPaths != null) {
                this.leadRow = rowsForPaths[this.leadIndex];
            } else if (this.leadPath != null) {
                this.tempPaths[0] = this.leadPath;
                int[] rowsForPaths2 = this.rowMapper.getRowsForPaths(this.tempPaths);
                this.leadRow = rowsForPaths2 != null ? rowsForPaths2[0] : -1;
            } else {
                this.leadRow = -1;
            }
            insureRowContinuity();
            return;
        }
        this.leadRow = -1;
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public int getLeadSelectionRow() {
        return this.leadRow;
    }

    @Override // javax.swing.tree.TreeSelectionModel
    public TreePath getLeadSelectionPath() {
        return this.leadPath;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.changeSupport == null) {
            this.changeSupport = new SwingPropertyChangeSupport(this);
        }
        this.changeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.changeSupport == null) {
            return;
        }
        this.changeSupport.removePropertyChangeListener(propertyChangeListener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        if (this.changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return this.changeSupport.getPropertyChangeListeners();
    }

    protected void insureRowContinuity() {
        if (this.selectionMode == 2 && this.selection != null && this.rowMapper != null) {
            DefaultListSelectionModel defaultListSelectionModel = this.listSelectionModel;
            int minSelectionIndex = defaultListSelectionModel.getMinSelectionIndex();
            if (minSelectionIndex != -1) {
                int maxSelectionIndex = defaultListSelectionModel.getMaxSelectionIndex();
                for (int i2 = minSelectionIndex; i2 <= maxSelectionIndex; i2++) {
                    if (!defaultListSelectionModel.isSelectedIndex(i2)) {
                        if (i2 == minSelectionIndex) {
                            clearSelection();
                        } else {
                            TreePath[] treePathArr = new TreePath[i2 - minSelectionIndex];
                            int[] rowsForPaths = this.rowMapper.getRowsForPaths(this.selection);
                            for (int i3 = 0; i3 < rowsForPaths.length; i3++) {
                                if (rowsForPaths[i3] < i2) {
                                    treePathArr[rowsForPaths[i3] - minSelectionIndex] = this.selection[i3];
                                }
                            }
                            setSelectionPaths(treePathArr);
                            return;
                        }
                    }
                }
                return;
            }
            return;
        }
        if (this.selectionMode == 1 && this.selection != null && this.selection.length > 1) {
            setSelectionPath(this.selection[0]);
        }
    }

    protected boolean arePathsContiguous(TreePath[] treePathArr) {
        int i2;
        if (this.rowMapper == null || treePathArr.length < 2) {
            return true;
        }
        BitSet bitSet = new BitSet(32);
        int length = treePathArr.length;
        int i3 = 0;
        TreePath[] treePathArr2 = {treePathArr[0]};
        int i4 = this.rowMapper.getRowsForPaths(treePathArr2)[0];
        for (int i5 = 0; i5 < length; i5++) {
            if (treePathArr[i5] != null) {
                treePathArr2[0] = treePathArr[i5];
                int[] rowsForPaths = this.rowMapper.getRowsForPaths(treePathArr2);
                if (rowsForPaths == null || (i2 = rowsForPaths[0]) == -1 || i2 < i4 - length || i2 > i4 + length) {
                    return false;
                }
                if (i2 < i4) {
                    i4 = i2;
                }
                if (!bitSet.get(i2)) {
                    bitSet.set(i2);
                    i3++;
                }
            }
        }
        int i6 = i3 + i4;
        for (int i7 = i4; i7 < i6; i7++) {
            if (!bitSet.get(i7)) {
                return false;
            }
        }
        return true;
    }

    protected boolean canPathsBeAdded(TreePath[] treePathArr) {
        if (treePathArr == null || treePathArr.length == 0 || this.rowMapper == null || this.selection == null || this.selectionMode == 4) {
            return true;
        }
        BitSet bitSet = new BitSet();
        DefaultListSelectionModel defaultListSelectionModel = this.listSelectionModel;
        int minSelectionIndex = defaultListSelectionModel.getMinSelectionIndex();
        int maxSelectionIndex = defaultListSelectionModel.getMaxSelectionIndex();
        TreePath[] treePathArr2 = new TreePath[1];
        if (minSelectionIndex != -1) {
            for (int i2 = minSelectionIndex; i2 <= maxSelectionIndex; i2++) {
                if (defaultListSelectionModel.isSelectedIndex(i2)) {
                    bitSet.set(i2);
                }
            }
        } else {
            treePathArr2[0] = treePathArr[0];
            int i3 = this.rowMapper.getRowsForPaths(treePathArr2)[0];
            maxSelectionIndex = i3;
            minSelectionIndex = i3;
        }
        for (int length = treePathArr.length - 1; length >= 0; length--) {
            if (treePathArr[length] != null) {
                treePathArr2[0] = treePathArr[length];
                int[] rowsForPaths = this.rowMapper.getRowsForPaths(treePathArr2);
                if (rowsForPaths == null) {
                    return false;
                }
                int i4 = rowsForPaths[0];
                minSelectionIndex = Math.min(i4, minSelectionIndex);
                maxSelectionIndex = Math.max(i4, maxSelectionIndex);
                if (i4 == -1) {
                    return false;
                }
                bitSet.set(i4);
            }
        }
        for (int i5 = minSelectionIndex; i5 <= maxSelectionIndex; i5++) {
            if (!bitSet.get(i5)) {
                return false;
            }
        }
        return true;
    }

    protected boolean canPathsBeRemoved(TreePath[] treePathArr) {
        if (this.rowMapper == null || this.selection == null || this.selectionMode == 4) {
            return true;
        }
        BitSet bitSet = new BitSet();
        int length = treePathArr.length;
        int iMin = -1;
        int i2 = 0;
        TreePath[] treePathArr2 = new TreePath[1];
        this.lastPaths.clear();
        for (int i3 = 0; i3 < length; i3++) {
            if (treePathArr[i3] != null) {
                this.lastPaths.put(treePathArr[i3], Boolean.TRUE);
            }
        }
        for (int length2 = this.selection.length - 1; length2 >= 0; length2--) {
            if (this.lastPaths.get(this.selection[length2]) == null) {
                treePathArr2[0] = this.selection[length2];
                int[] rowsForPaths = this.rowMapper.getRowsForPaths(treePathArr2);
                if (rowsForPaths != null && rowsForPaths[0] != -1 && !bitSet.get(rowsForPaths[0])) {
                    i2++;
                    if (iMin == -1) {
                        iMin = rowsForPaths[0];
                    } else {
                        iMin = Math.min(iMin, rowsForPaths[0]);
                    }
                    bitSet.set(rowsForPaths[0]);
                }
            }
        }
        this.lastPaths.clear();
        if (i2 > 1) {
            for (int i4 = (iMin + i2) - 1; i4 >= iMin; i4--) {
                if (!bitSet.get(i4)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    @Deprecated
    protected void notifyPathChange(Vector<?> vector, TreePath treePath) {
        int size = vector.size();
        boolean[] zArr = new boolean[size];
        TreePath[] treePathArr = new TreePath[size];
        for (int i2 = 0; i2 < size; i2++) {
            PathPlaceHolder pathPlaceHolder = (PathPlaceHolder) vector.elementAt(i2);
            zArr[i2] = pathPlaceHolder.isNew;
            treePathArr[i2] = pathPlaceHolder.path;
        }
        fireValueChanged(new TreeSelectionEvent(this, treePathArr, zArr, treePath, this.leadPath));
    }

    protected void updateLeadIndex() {
        if (this.leadPath != null) {
            if (this.selection == null) {
                this.leadPath = null;
                this.leadRow = -1;
                this.leadIndex = -1;
                return;
            }
            this.leadIndex = -1;
            this.leadRow = -1;
            for (int length = this.selection.length - 1; length >= 0; length--) {
                if (this.selection[length] == this.leadPath) {
                    this.leadIndex = length;
                    return;
                }
            }
            return;
        }
        this.leadIndex = -1;
    }

    protected void insureUniqueness() {
    }

    public String toString() {
        int[] rowsForPaths;
        int selectionCount = getSelectionCount();
        StringBuffer stringBuffer = new StringBuffer();
        if (this.rowMapper != null) {
            rowsForPaths = this.rowMapper.getRowsForPaths(this.selection);
        } else {
            rowsForPaths = null;
        }
        stringBuffer.append(getClass().getName() + " " + hashCode() + " [ ");
        for (int i2 = 0; i2 < selectionCount; i2++) {
            if (rowsForPaths != null) {
                stringBuffer.append(this.selection[i2].toString() + "@" + Integer.toString(rowsForPaths[i2]) + " ");
            } else {
                stringBuffer.append(this.selection[i2].toString() + " ");
            }
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        DefaultTreeSelectionModel defaultTreeSelectionModel = (DefaultTreeSelectionModel) super.clone();
        defaultTreeSelectionModel.changeSupport = null;
        if (this.selection != null) {
            int length = this.selection.length;
            defaultTreeSelectionModel.selection = new TreePath[length];
            System.arraycopy(this.selection, 0, defaultTreeSelectionModel.selection, 0, length);
        }
        defaultTreeSelectionModel.listenerList = new EventListenerList();
        defaultTreeSelectionModel.listSelectionModel = (DefaultListSelectionModel) this.listSelectionModel.clone();
        defaultTreeSelectionModel.uniquePaths = new Hashtable<>();
        defaultTreeSelectionModel.lastPaths = new Hashtable<>();
        defaultTreeSelectionModel.tempPaths = new TreePath[1];
        return defaultTreeSelectionModel;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Object[] objArr;
        objectOutputStream.defaultWriteObject();
        if (this.rowMapper != null && (this.rowMapper instanceof Serializable)) {
            objArr = new Object[]{"rowMapper", this.rowMapper};
        } else {
            objArr = new Object[0];
        }
        objectOutputStream.writeObject(objArr);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Object[] objArr = (Object[]) objectInputStream.readObject();
        if (objArr.length > 0 && objArr[0].equals("rowMapper")) {
            this.rowMapper = (RowMapper) objArr[1];
        }
    }
}
