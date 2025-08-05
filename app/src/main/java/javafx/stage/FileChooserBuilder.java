package javafx.stage;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import javafx.stage.FileChooser;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/stage/FileChooserBuilder.class */
public final class FileChooserBuilder implements Builder<FileChooser> {
    private int __set;
    private Collection<? extends FileChooser.ExtensionFilter> extensionFilters;
    private File initialDirectory;
    private String title;

    protected FileChooserBuilder() {
    }

    public static FileChooserBuilder create() {
        return new FileChooserBuilder();
    }

    public void applyTo(FileChooser x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.getExtensionFilters().addAll(this.extensionFilters);
        }
        if ((set & 2) != 0) {
            x2.setInitialDirectory(this.initialDirectory);
        }
        if ((set & 4) != 0) {
            x2.setTitle(this.title);
        }
    }

    public FileChooserBuilder extensionFilters(Collection<? extends FileChooser.ExtensionFilter> x2) {
        this.extensionFilters = x2;
        this.__set |= 1;
        return this;
    }

    public FileChooserBuilder extensionFilters(FileChooser.ExtensionFilter... x2) {
        return extensionFilters(Arrays.asList(x2));
    }

    public FileChooserBuilder initialDirectory(File x2) {
        this.initialDirectory = x2;
        this.__set |= 2;
        return this;
    }

    public FileChooserBuilder title(String x2) {
        this.title = x2;
        this.__set |= 4;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public FileChooser build2() {
        FileChooser x2 = new FileChooser();
        applyTo(x2);
        return x2;
    }
}
