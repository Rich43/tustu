package javafx.scene.shape;

import javafx.scene.shape.PathElementBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/PathElementBuilder.class */
public abstract class PathElementBuilder<B extends PathElementBuilder<B>> {
    private boolean __set;
    private boolean absolute;

    protected PathElementBuilder() {
    }

    public void applyTo(PathElement x2) {
        if (this.__set) {
            x2.setAbsolute(this.absolute);
        }
    }

    public B absolute(boolean x2) {
        this.absolute = x2;
        this.__set = true;
        return this;
    }
}
