package com.sun.javafx.jmx;

import com.sun.javafx.tk.TKScene;
import javafx.geometry.Rectangle2D;

/* loaded from: jfxrt.jar:com/sun/javafx/jmx/HighlightRegion.class */
public class HighlightRegion extends Rectangle2D {
    private TKScene tkScene;
    private int hash;

    public HighlightRegion(TKScene tkScene, double x2, double y2, double w2, double h2) {
        super(x2, y2, w2, h2);
        this.hash = 0;
        this.tkScene = tkScene;
    }

    public TKScene getTKScene() {
        return this.tkScene;
    }

    @Override // javafx.geometry.Rectangle2D
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof HighlightRegion) {
            HighlightRegion other = (HighlightRegion) obj;
            return this.tkScene.equals(other.tkScene) && super.equals(other);
        }
        return false;
    }

    @Override // javafx.geometry.Rectangle2D
    public int hashCode() {
        if (this.hash == 0) {
            long bits = (31 * ((31 * 7) + super.hashCode())) + this.tkScene.hashCode();
            this.hash = (int) (bits ^ (bits >> 32));
        }
        return this.hash;
    }

    @Override // javafx.geometry.Rectangle2D
    public String toString() {
        return "HighlighRegion [tkScene = " + ((Object) this.tkScene) + ", rectangle = " + super.toString() + "]";
    }
}
