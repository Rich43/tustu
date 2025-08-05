package java.awt.peer;

import java.awt.Insets;

/* loaded from: rt.jar:java/awt/peer/ContainerPeer.class */
public interface ContainerPeer extends ComponentPeer {
    Insets getInsets();

    void beginValidate();

    void endValidate();

    void beginLayout();

    void endLayout();
}
