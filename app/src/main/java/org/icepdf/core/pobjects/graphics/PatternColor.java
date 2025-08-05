package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.util.HashMap;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/PatternColor.class */
public class PatternColor extends PColorSpace {
    public static final Name PATTERN_KEY = new Name("Pattern");
    private Pattern pattern;
    private PColorSpace PColorSpace;

    public PatternColor(Library library, HashMap entries) {
        super(library, entries);
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public int getNumComponents() {
        if (this.PColorSpace != null) {
            return this.PColorSpace.getNumComponents();
        }
        return 0;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public Color getColor(float[] f2, boolean fillAndStroke) {
        if (this.PColorSpace != null) {
            return this.PColorSpace.getColor(f2);
        }
        return Color.black;
    }

    public Pattern getPattern(Reference reference) {
        if (this.entries != null) {
            return (Pattern) this.entries.get(reference);
        }
        return null;
    }

    public PColorSpace getPColorSpace() {
        return this.PColorSpace;
    }

    public void setPColorSpace(PColorSpace PColorSpace) {
        this.PColorSpace = PColorSpace;
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
