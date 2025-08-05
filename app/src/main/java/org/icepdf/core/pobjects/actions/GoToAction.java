package org.icepdf.core.pobjects.actions;

import java.util.HashMap;
import org.icepdf.core.pobjects.Destination;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/actions/GoToAction.class */
public class GoToAction extends Action {
    public static final Name DESTINATION_KEY = new Name(PdfOps.D_TOKEN);
    private Destination destination;

    public GoToAction(Library l2, HashMap h2) {
        super(l2, h2);
        this.destination = new Destination(this.library, getObject(DESTINATION_KEY));
    }

    public void setDestination(Destination destination) {
        this.entries.put(DESTINATION_KEY, destination.getObject());
        this.destination = destination;
    }

    public Destination getDestination() {
        return this.destination;
    }
}
