package javafx.scene.layout;

import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/layout/BackgroundFill.class */
public final class BackgroundFill {
    final Paint fill;
    final CornerRadii radii;
    final Insets insets;
    private final int hash;

    public final Paint getFill() {
        return this.fill;
    }

    public final CornerRadii getRadii() {
        return this.radii;
    }

    public final Insets getInsets() {
        return this.insets;
    }

    public BackgroundFill(@NamedArg("fill") Paint fill, @NamedArg("radii") CornerRadii radii, @NamedArg("insets") Insets insets) {
        this.fill = fill == null ? Color.TRANSPARENT : fill;
        this.radii = radii == null ? CornerRadii.EMPTY : radii;
        this.insets = insets == null ? Insets.EMPTY : insets;
        int result = this.fill.hashCode();
        this.hash = (31 * ((31 * result) + this.radii.hashCode())) + this.insets.hashCode();
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        BackgroundFill that = (BackgroundFill) o2;
        return this.hash == that.hash && this.fill.equals(that.fill) && this.insets.equals(that.insets) && this.radii.equals(that.radii);
    }

    public int hashCode() {
        return this.hash;
    }
}
