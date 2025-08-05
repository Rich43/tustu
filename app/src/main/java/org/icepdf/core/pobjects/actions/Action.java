package org.icepdf.core.pobjects.actions;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.HashMap;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/actions/Action.class */
public class Action extends Dictionary {
    public static final Name ACTION_TYPE = new Name("Action");
    public static final Name ACTION_TYPE_KEY = new Name(PdfOps.S_TOKEN);
    public static final Name NEXT_KEY = new Name("Next");
    public static final Name ACTION_TYPE_GOTO = new Name("GoTo");
    public static final Name ACTION_TYPE_GOTO_REMOTE = new Name("GoToR");
    public static final Name ACTION_TYPE_LAUNCH = new Name("Launch");
    public static final Name ACTION_TYPE_URI = new Name(Constants._ATT_URI);
    private String type;

    public Action(Library l2, HashMap h2) {
        super(l2, h2);
        this.type = getObject(ACTION_TYPE_KEY).toString();
    }

    public static Action buildAction(Library library, HashMap hashMap) {
        Name actionType = (Name) hashMap.get(ACTION_TYPE_KEY);
        if (actionType != null) {
            if (actionType.equals(ACTION_TYPE_GOTO)) {
                return new GoToAction(library, hashMap);
            }
            if (actionType.equals(ACTION_TYPE_GOTO_REMOTE)) {
                return new GoToRAction(library, hashMap);
            }
            if (actionType.equals(ACTION_TYPE_LAUNCH)) {
                return new LaunchAction(library, hashMap);
            }
            if (actionType.equals(ACTION_TYPE_URI)) {
                return new URIAction(library, hashMap);
            }
        }
        return new Action(library, hashMap);
    }

    public String getType() {
        return this.type;
    }

    public boolean similar(Action obj) {
        if (getPObjectReference() != null && obj.getPObjectReference() != null) {
            return getPObjectReference().equals(obj.getPObjectReference());
        }
        return getType().equals(obj.getType());
    }
}
