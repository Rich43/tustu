package org.icepdf.core.pobjects.graphics.text;

import java.util.Comparator;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/text/WordPositionComparator.class */
public class WordPositionComparator implements Comparator<AbstractText> {
    @Override // java.util.Comparator
    public int compare(AbstractText lt1, AbstractText lt2) {
        return Float.compare(lt1.getBounds().f12404x, lt2.getBounds().f12404x);
    }
}
