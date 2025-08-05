package javax.swing;

import javax.swing.event.ListSelectionListener;

/* loaded from: rt.jar:javax/swing/ListSelectionModel.class */
public interface ListSelectionModel {
    public static final int SINGLE_SELECTION = 0;
    public static final int SINGLE_INTERVAL_SELECTION = 1;
    public static final int MULTIPLE_INTERVAL_SELECTION = 2;

    void setSelectionInterval(int i2, int i3);

    void addSelectionInterval(int i2, int i3);

    void removeSelectionInterval(int i2, int i3);

    int getMinSelectionIndex();

    int getMaxSelectionIndex();

    boolean isSelectedIndex(int i2);

    int getAnchorSelectionIndex();

    void setAnchorSelectionIndex(int i2);

    int getLeadSelectionIndex();

    void setLeadSelectionIndex(int i2);

    void clearSelection();

    boolean isSelectionEmpty();

    void insertIndexInterval(int i2, int i3, boolean z2);

    void removeIndexInterval(int i2, int i3);

    void setValueIsAdjusting(boolean z2);

    boolean getValueIsAdjusting();

    void setSelectionMode(int i2);

    int getSelectionMode();

    void addListSelectionListener(ListSelectionListener listSelectionListener);

    void removeListSelectionListener(ListSelectionListener listSelectionListener);
}
