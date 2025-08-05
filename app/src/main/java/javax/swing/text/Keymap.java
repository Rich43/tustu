package javax.swing.text;

import javax.swing.Action;
import javax.swing.KeyStroke;

/* loaded from: rt.jar:javax/swing/text/Keymap.class */
public interface Keymap {
    String getName();

    Action getDefaultAction();

    void setDefaultAction(Action action);

    Action getAction(KeyStroke keyStroke);

    KeyStroke[] getBoundKeyStrokes();

    Action[] getBoundActions();

    KeyStroke[] getKeyStrokesForAction(Action action);

    boolean isLocallyDefined(KeyStroke keyStroke);

    void addActionForKeyStroke(KeyStroke keyStroke, Action action);

    void removeKeyStrokeBinding(KeyStroke keyStroke);

    void removeBindings();

    Keymap getResolveParent();

    void setResolveParent(Keymap keymap);
}
