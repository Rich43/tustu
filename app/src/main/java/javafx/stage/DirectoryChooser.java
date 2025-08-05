package javafx.stage;

import com.sun.javafx.tk.Toolkit;
import java.io.File;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/* loaded from: jfxrt.jar:javafx/stage/DirectoryChooser.class */
public final class DirectoryChooser {
    private StringProperty title;
    private ObjectProperty<File> initialDirectory;

    public final void setTitle(String value) {
        titleProperty().set(value);
    }

    public final String getTitle() {
        if (this.title != null) {
            return this.title.get();
        }
        return null;
    }

    public final StringProperty titleProperty() {
        if (this.title == null) {
            this.title = new SimpleStringProperty(this, "title");
        }
        return this.title;
    }

    public final void setInitialDirectory(File value) {
        initialDirectoryProperty().set(value);
    }

    public final File getInitialDirectory() {
        if (this.initialDirectory != null) {
            return this.initialDirectory.get();
        }
        return null;
    }

    public final ObjectProperty<File> initialDirectoryProperty() {
        if (this.initialDirectory == null) {
            this.initialDirectory = new SimpleObjectProperty(this, "initialDirectory");
        }
        return this.initialDirectory;
    }

    public File showDialog(Window ownerWindow) {
        return Toolkit.getToolkit().showDirectoryChooser(ownerWindow != null ? ownerWindow.impl_getPeer() : null, getTitle(), getInitialDirectory());
    }
}
