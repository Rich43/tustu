package java.awt.peer;

import java.awt.Dimension;

/* loaded from: rt.jar:java/awt/peer/TextAreaPeer.class */
public interface TextAreaPeer extends TextComponentPeer {
    void insert(String str, int i2);

    void replaceRange(String str, int i2, int i3);

    Dimension getPreferredSize(int i2, int i3);

    Dimension getMinimumSize(int i2, int i3);
}
