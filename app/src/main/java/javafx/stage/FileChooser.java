package javafx.stage;

import com.sun.glass.ui.CommonDialogs;
import com.sun.javafx.tk.FileChooserType;
import com.sun.javafx.tk.Toolkit;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/stage/FileChooser.class */
public final class FileChooser {
    private StringProperty title;
    private ObjectProperty<File> initialDirectory;
    private ObjectProperty<String> initialFileName;
    private ObservableList<ExtensionFilter> extensionFilters = FXCollections.observableArrayList();
    private ObjectProperty<ExtensionFilter> selectedExtensionFilter;

    /* loaded from: jfxrt.jar:javafx/stage/FileChooser$ExtensionFilter.class */
    public static final class ExtensionFilter {
        private final String description;
        private final List<String> extensions;

        public ExtensionFilter(String description, String... extensions) {
            validateArgs(description, extensions);
            this.description = description;
            this.extensions = Collections.unmodifiableList(Arrays.asList((Object[]) extensions.clone()));
        }

        public ExtensionFilter(String description, List<String> extensions) {
            String[] extensionsArray = extensions != null ? (String[]) extensions.toArray(new String[extensions.size()]) : null;
            validateArgs(description, extensionsArray);
            this.description = description;
            this.extensions = Collections.unmodifiableList(Arrays.asList(extensionsArray));
        }

        public String getDescription() {
            return this.description;
        }

        public List<String> getExtensions() {
            return this.extensions;
        }

        private static void validateArgs(String description, String[] extensions) {
            if (description == null) {
                throw new NullPointerException("Description must not be null");
            }
            if (description.isEmpty()) {
                throw new IllegalArgumentException("Description must not be empty");
            }
            if (extensions == null) {
                throw new NullPointerException("Extensions must not be null");
            }
            if (extensions.length == 0) {
                throw new IllegalArgumentException("At least one extension must be defined");
            }
            for (String extension : extensions) {
                if (extension == null) {
                    throw new NullPointerException("Extension must not be null");
                }
                if (extension.isEmpty()) {
                    throw new IllegalArgumentException("Extension must not be empty");
                }
            }
        }
    }

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

    public final void setInitialFileName(String value) {
        initialFileNameProperty().set(value);
    }

    public final String getInitialFileName() {
        if (this.initialFileName != null) {
            return this.initialFileName.get();
        }
        return null;
    }

    public final ObjectProperty<String> initialFileNameProperty() {
        if (this.initialFileName == null) {
            this.initialFileName = new SimpleObjectProperty(this, "initialFileName");
        }
        return this.initialFileName;
    }

    public ObservableList<ExtensionFilter> getExtensionFilters() {
        return this.extensionFilters;
    }

    public final ObjectProperty<ExtensionFilter> selectedExtensionFilterProperty() {
        if (this.selectedExtensionFilter == null) {
            this.selectedExtensionFilter = new SimpleObjectProperty(this, "selectedExtensionFilter");
        }
        return this.selectedExtensionFilter;
    }

    public final void setSelectedExtensionFilter(ExtensionFilter filter) {
        selectedExtensionFilterProperty().setValue(filter);
    }

    public final ExtensionFilter getSelectedExtensionFilter() {
        if (this.selectedExtensionFilter != null) {
            return this.selectedExtensionFilter.get();
        }
        return null;
    }

    public File showOpenDialog(Window ownerWindow) {
        List<File> selectedFiles = showDialog(ownerWindow, FileChooserType.OPEN);
        if (selectedFiles == null || selectedFiles.size() <= 0) {
            return null;
        }
        return selectedFiles.get(0);
    }

    public List<File> showOpenMultipleDialog(Window ownerWindow) {
        List<File> selectedFiles = showDialog(ownerWindow, FileChooserType.OPEN_MULTIPLE);
        if (selectedFiles == null || selectedFiles.size() <= 0) {
            return null;
        }
        return Collections.unmodifiableList(selectedFiles);
    }

    public File showSaveDialog(Window ownerWindow) {
        List<File> selectedFiles = showDialog(ownerWindow, FileChooserType.SAVE);
        if (selectedFiles == null || selectedFiles.size() <= 0) {
            return null;
        }
        return selectedFiles.get(0);
    }

    private ExtensionFilter findSelectedFilter(CommonDialogs.ExtensionFilter filter) {
        if (filter != null) {
            String description = filter.getDescription();
            List<String> extensions = filter.getExtensions();
            for (ExtensionFilter ef : this.extensionFilters) {
                if (description.equals(ef.getDescription()) && extensions.equals(ef.getExtensions())) {
                    return ef;
                }
            }
            return null;
        }
        return null;
    }

    private List<File> showDialog(Window ownerWindow, FileChooserType fileChooserType) {
        CommonDialogs.FileChooserResult result = Toolkit.getToolkit().showFileChooser(ownerWindow != null ? ownerWindow.impl_getPeer() : null, getTitle(), getInitialDirectory(), getInitialFileName(), fileChooserType, this.extensionFilters, getSelectedExtensionFilter());
        if (result == null) {
            return null;
        }
        List<File> files = result.getFiles();
        if (files != null && files.size() > 0) {
            selectedExtensionFilterProperty().set(findSelectedFilter(result.getExtensionFilter()));
        }
        return files;
    }
}
