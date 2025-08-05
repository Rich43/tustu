package java.awt.peer;

import java.awt.Adjustable;

/* loaded from: rt.jar:java/awt/peer/ScrollPanePeer.class */
public interface ScrollPanePeer extends ContainerPeer {
    int getHScrollbarHeight();

    int getVScrollbarWidth();

    void setScrollPosition(int i2, int i3);

    void childResized(int i2, int i3);

    void setUnitIncrement(Adjustable adjustable, int i2);

    void setValue(Adjustable adjustable, int i2);
}
