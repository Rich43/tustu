package java.util.prefs;

import java.util.EventListener;

/* loaded from: rt.jar:java/util/prefs/NodeChangeListener.class */
public interface NodeChangeListener extends EventListener {
    void childAdded(NodeChangeEvent nodeChangeEvent);

    void childRemoved(NodeChangeEvent nodeChangeEvent);
}
