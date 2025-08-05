package javafx.scene.input;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import javafx.scene.input.ClipboardContentBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/input/ClipboardContentBuilder.class */
public class ClipboardContentBuilder<B extends ClipboardContentBuilder<B>> implements Builder<ClipboardContent> {
    private boolean __set;
    private Collection<? extends File> files;

    protected ClipboardContentBuilder() {
    }

    public static ClipboardContentBuilder<?> create() {
        return new ClipboardContentBuilder<>();
    }

    public void applyTo(ClipboardContent x2) {
        if (this.__set) {
            x2.getFiles().clear();
            x2.getFiles().addAll(this.files);
        }
    }

    public B files(Collection<? extends File> x2) {
        this.files = x2;
        this.__set = true;
        return this;
    }

    public B files(File... fileArr) {
        return (B) files(Arrays.asList(fileArr));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public ClipboardContent build2() {
        ClipboardContent x2 = new ClipboardContent();
        applyTo(x2);
        return x2;
    }
}
