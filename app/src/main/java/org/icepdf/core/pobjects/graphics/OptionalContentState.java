package org.icepdf.core.pobjects.graphics;

import java.util.LinkedList;
import org.icepdf.core.pobjects.OptionalContents;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/OptionalContentState.class */
public class OptionalContentState {
    private LinkedList<OptionalContents> optionContents = new LinkedList<>();
    private boolean isEmpty = true;

    public void add(OptionalContents optionContent) {
        this.optionContents.add(optionContent);
        this.isEmpty = false;
    }

    public void remove() {
        this.optionContents.removeLast();
        this.isEmpty = this.optionContents.isEmpty();
    }

    public boolean isVisible() {
        return this.isEmpty || this.optionContents.getLast().isVisible();
    }
}
