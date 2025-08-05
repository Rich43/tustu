package javax.swing.undo;

import java.util.Hashtable;

/* loaded from: rt.jar:javax/swing/undo/StateEditable.class */
public interface StateEditable {
    public static final String RCSID = "$Id: StateEditable.java,v 1.2 1997/09/08 19:39:08 marklin Exp $";

    void storeState(Hashtable<Object, Object> hashtable);

    void restoreState(Hashtable<?, ?> hashtable);
}
