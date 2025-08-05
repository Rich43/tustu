package com.sun.glass.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/glass/ui/CommonDialogs.class */
public class CommonDialogs {

    /* loaded from: jfxrt.jar:com/sun/glass/ui/CommonDialogs$Type.class */
    public static final class Type {
        public static final int OPEN = 0;
        public static final int SAVE = 1;
    }

    /* loaded from: jfxrt.jar:com/sun/glass/ui/CommonDialogs$ExtensionFilter.class */
    public static final class ExtensionFilter {
        private final String description;
        private final List<String> extensions;

        public ExtensionFilter(String description, List<String> extensions) {
            Application.checkEventThread();
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("Description parameter must be non-null and not empty");
            }
            if (extensions == null || extensions.isEmpty()) {
                throw new IllegalArgumentException("Extensions parameter must be non-null and not empty");
            }
            for (String extension : extensions) {
                if (extension == null || extension.length() == 0) {
                    throw new IllegalArgumentException("Each extension must be non-null and not empty");
                }
            }
            this.description = description;
            this.extensions = extensions;
        }

        public String getDescription() {
            Application.checkEventThread();
            return this.description;
        }

        public List<String> getExtensions() {
            Application.checkEventThread();
            return this.extensions;
        }

        private String[] extensionsToArray() {
            Application.checkEventThread();
            return (String[]) this.extensions.toArray(new String[this.extensions.size()]);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/glass/ui/CommonDialogs$FileChooserResult.class */
    public static final class FileChooserResult {
        private final List<File> files;
        private final ExtensionFilter filter;

        public FileChooserResult(List<File> files, ExtensionFilter filter) {
            if (files == null) {
                throw new NullPointerException("files should not be null");
            }
            this.files = files;
            this.filter = filter;
        }

        public FileChooserResult() {
            this(new ArrayList(), null);
        }

        public List<File> getFiles() {
            return this.files;
        }

        public ExtensionFilter getExtensionFilter() {
            return this.filter;
        }
    }

    private CommonDialogs() {
    }

    public static FileChooserResult showFileChooser(Window owner, File folder, String filename, String title, int type, boolean multipleMode, List<ExtensionFilter> extensionFilters, int defaultFilterIndex) {
        Application.checkEventThread();
        String _folder = convertFolder(folder);
        if (filename == null) {
            filename = "";
        }
        if (type != 0 && type != 1) {
            throw new IllegalArgumentException("Type parameter must be equal to one of the constants from Type");
        }
        ExtensionFilter[] _extensionFilters = null;
        if (extensionFilters != null) {
            _extensionFilters = (ExtensionFilter[]) extensionFilters.toArray(new ExtensionFilter[extensionFilters.size()]);
        }
        if (extensionFilters == null || extensionFilters.isEmpty() || defaultFilterIndex < 0 || defaultFilterIndex >= extensionFilters.size()) {
            defaultFilterIndex = 0;
        }
        return Application.GetApplication().staticCommonDialogs_showFileChooser(owner, _folder, filename, convertTitle(title), type, multipleMode, _extensionFilters, defaultFilterIndex);
    }

    public static File showFolderChooser(Window owner, File folder, String title) {
        Application.checkEventThread();
        return Application.GetApplication().staticCommonDialogs_showFolderChooser(owner, convertFolder(folder), convertTitle(title));
    }

    private static String convertFolder(File folder) {
        if (folder != null) {
            if (folder.isDirectory()) {
                try {
                    return folder.getCanonicalPath();
                } catch (IOException e2) {
                    throw new IllegalArgumentException("Unable to get a canonical path for folder", e2);
                }
            }
            throw new IllegalArgumentException("Folder parameter must be a valid folder");
        }
        return "";
    }

    private static String convertTitle(String title) {
        return title != null ? title : "";
    }

    protected static FileChooserResult createFileChooserResult(String[] files, ExtensionFilter[] extensionFilters, int index) {
        List<File> list = new ArrayList<>();
        for (String s2 : files) {
            if (s2 != null) {
                list.add(new File(s2));
            }
        }
        return new FileChooserResult(list, (extensionFilters == null || index < 0 || index >= extensionFilters.length) ? null : extensionFilters[index]);
    }
}
