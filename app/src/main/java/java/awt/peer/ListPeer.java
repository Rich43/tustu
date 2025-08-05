package java.awt.peer;

import java.awt.Dimension;

/* loaded from: rt.jar:java/awt/peer/ListPeer.class */
public interface ListPeer extends ComponentPeer {
    int[] getSelectedIndexes();

    void add(String str, int i2);

    void delItems(int i2, int i3);

    void removeAll();

    void select(int i2);

    void deselect(int i2);

    void makeVisible(int i2);

    void setMultipleMode(boolean z2);

    Dimension getPreferredSize(int i2);

    Dimension getMinimumSize(int i2);
}
