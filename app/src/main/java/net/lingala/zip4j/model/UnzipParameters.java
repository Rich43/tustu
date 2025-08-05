package net.lingala.zip4j.model;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/model/UnzipParameters.class */
public class UnzipParameters {
    private boolean ignoreReadOnlyFileAttribute;
    private boolean ignoreHiddenFileAttribute;
    private boolean ignoreArchiveFileAttribute;
    private boolean ignoreSystemFileAttribute;
    private boolean ignoreAllFileAttributes;
    private boolean ignoreDateTimeAttributes;

    public boolean isIgnoreReadOnlyFileAttribute() {
        return this.ignoreReadOnlyFileAttribute;
    }

    public void setIgnoreReadOnlyFileAttribute(boolean ignoreReadOnlyFileAttribute) {
        this.ignoreReadOnlyFileAttribute = ignoreReadOnlyFileAttribute;
    }

    public boolean isIgnoreHiddenFileAttribute() {
        return this.ignoreHiddenFileAttribute;
    }

    public void setIgnoreHiddenFileAttribute(boolean ignoreHiddenFileAttribute) {
        this.ignoreHiddenFileAttribute = ignoreHiddenFileAttribute;
    }

    public boolean isIgnoreArchiveFileAttribute() {
        return this.ignoreArchiveFileAttribute;
    }

    public void setIgnoreArchiveFileAttribute(boolean ignoreArchiveFileAttribute) {
        this.ignoreArchiveFileAttribute = ignoreArchiveFileAttribute;
    }

    public boolean isIgnoreSystemFileAttribute() {
        return this.ignoreSystemFileAttribute;
    }

    public void setIgnoreSystemFileAttribute(boolean ignoreSystemFileAttribute) {
        this.ignoreSystemFileAttribute = ignoreSystemFileAttribute;
    }

    public boolean isIgnoreAllFileAttributes() {
        return this.ignoreAllFileAttributes;
    }

    public void setIgnoreAllFileAttributes(boolean ignoreAllFileAttributes) {
        this.ignoreAllFileAttributes = ignoreAllFileAttributes;
    }

    public boolean isIgnoreDateTimeAttributes() {
        return this.ignoreDateTimeAttributes;
    }

    public void setIgnoreDateTimeAttributes(boolean ignoreDateTimeAttributes) {
        this.ignoreDateTimeAttributes = ignoreDateTimeAttributes;
    }
}
