package org.icepdf.core.pobjects.actions;

import java.util.HashMap;
import org.icepdf.core.pobjects.Destination;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.StateManager;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/actions/ActionFactory.class */
public class ActionFactory {
    public static final int GOTO_ACTION = 1;
    public static final int URI_ACTION = 2;
    public static final int LAUNCH_ACTION = 3;

    public static Action buildAction(Library library, int type) {
        StateManager stateManager = library.getStateManager();
        HashMap<Name, Object> entries = new HashMap<>();
        if (1 == type) {
            entries.put(Dictionary.TYPE_KEY, Action.ACTION_TYPE);
            entries.put(Action.ACTION_TYPE_KEY, Action.ACTION_TYPE_GOTO);
            entries.put(GoToAction.DESTINATION_KEY, new Destination(library, null));
            GoToAction action = new GoToAction(library, entries);
            action.setPObjectReference(stateManager.getNewReferencNumber());
            return action;
        }
        if (2 == type) {
            entries.put(Dictionary.TYPE_KEY, Action.ACTION_TYPE);
            entries.put(Action.ACTION_TYPE_KEY, Action.ACTION_TYPE_URI);
            entries.put(URIAction.URI_KEY, "");
            URIAction action2 = new URIAction(library, entries);
            action2.setPObjectReference(stateManager.getNewReferencNumber());
            return action2;
        }
        if (3 == type) {
            entries.put(Dictionary.TYPE_KEY, Action.ACTION_TYPE);
            entries.put(Action.ACTION_TYPE_KEY, Action.ACTION_TYPE_LAUNCH);
            entries.put(LaunchAction.FILE_KEY, "");
            LaunchAction action3 = new LaunchAction(library, entries);
            action3.setPObjectReference(stateManager.getNewReferencNumber());
            return action3;
        }
        return null;
    }
}
