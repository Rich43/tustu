package javafx.stage;

import java.io.File;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/stage/DirectoryChooserBuilder.class */
public final class DirectoryChooserBuilder implements Builder<DirectoryChooser> {
    private int __set;
    private File initialDirectory;
    private String title;

    protected DirectoryChooserBuilder() {
    }

    public static DirectoryChooserBuilder create() {
        return new DirectoryChooserBuilder();
    }

    public void applyTo(DirectoryChooser x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setInitialDirectory(this.initialDirectory);
        }
        if ((set & 2) != 0) {
            x2.setTitle(this.title);
        }
    }

    public DirectoryChooserBuilder initialDirectory(File x2) {
        this.initialDirectory = x2;
        this.__set |= 1;
        return this;
    }

    public DirectoryChooserBuilder title(String x2) {
        this.title = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public DirectoryChooser build2() {
        DirectoryChooser x2 = new DirectoryChooser();
        applyTo(x2);
        return x2;
    }
}
