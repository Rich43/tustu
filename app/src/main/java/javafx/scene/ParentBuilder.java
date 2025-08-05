package javafx.scene;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.ParentBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/ParentBuilder.class */
public abstract class ParentBuilder<B extends ParentBuilder<B>> extends NodeBuilder<B> {
    private int __set;
    private Collection<? extends String> stylesheets;

    protected ParentBuilder() {
    }

    public void applyTo(Parent x2) {
        super.applyTo((Node) x2);
        int set = this.__set;
        if ((set & 2) != 0) {
            x2.getStylesheets().addAll(this.stylesheets);
        }
    }

    public B stylesheets(Collection<? extends String> x2) {
        this.stylesheets = x2;
        this.__set |= 2;
        return this;
    }

    public B stylesheets(String... strArr) {
        return (B) stylesheets(Arrays.asList(strArr));
    }
}
