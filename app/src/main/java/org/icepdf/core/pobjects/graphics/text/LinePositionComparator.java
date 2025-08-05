package org.icepdf.core.pobjects.graphics.text;

import java.util.Comparator;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/text/LinePositionComparator.class */
public class LinePositionComparator implements Comparator<AbstractText> {
    @Override // java.util.Comparator
    public int compare(AbstractText lt1, AbstractText lt2) {
        return Float.compare(lt2.getBounds().f12405y, lt1.getBounds().f12405y);
    }
}
