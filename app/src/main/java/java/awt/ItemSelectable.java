package java.awt;

import java.awt.event.ItemListener;

/* loaded from: rt.jar:java/awt/ItemSelectable.class */
public interface ItemSelectable {
    Object[] getSelectedObjects();

    void addItemListener(ItemListener itemListener);

    void removeItemListener(ItemListener itemListener);
}
