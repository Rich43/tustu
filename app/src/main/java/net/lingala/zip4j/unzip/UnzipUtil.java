package net.lingala.zip4j.unzip;

import java.io.File;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.util.Zip4jUtil;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/unzip/UnzipUtil.class */
public class UnzipUtil {
    public static void applyFileAttributes(FileHeader fileHeader, File file) throws ZipException {
        applyFileAttributes(fileHeader, file, null);
    }

    public static void applyFileAttributes(FileHeader fileHeader, File file, UnzipParameters unzipParameters) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("cannot set file properties: file header is null");
        }
        if (file == null) {
            throw new ZipException("cannot set file properties: output file is null");
        }
        if (!Zip4jUtil.checkFileExists(file)) {
            throw new ZipException("cannot set file properties: file doesnot exist");
        }
        if (unzipParameters == null || !unzipParameters.isIgnoreDateTimeAttributes()) {
            setFileLastModifiedTime(fileHeader, file);
        }
        if (unzipParameters == null) {
            setFileAttributes(fileHeader, file, true, true, true, true);
        } else if (unzipParameters.isIgnoreAllFileAttributes()) {
            setFileAttributes(fileHeader, file, false, false, false, false);
        } else {
            setFileAttributes(fileHeader, file, !unzipParameters.isIgnoreReadOnlyFileAttribute(), !unzipParameters.isIgnoreHiddenFileAttribute(), !unzipParameters.isIgnoreArchiveFileAttribute(), !unzipParameters.isIgnoreSystemFileAttribute());
        }
    }

    private static void setFileAttributes(FileHeader fileHeader, File file, boolean setReadOnly, boolean setHidden, boolean setArchive, boolean setSystem) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("invalid file header. cannot set file attributes");
        }
        byte[] externalAttrbs = fileHeader.getExternalFileAttr();
        if (externalAttrbs == null) {
            return;
        }
        switch (externalAttrbs[0]) {
            case 1:
                if (setReadOnly) {
                    Zip4jUtil.setFileReadOnly(file);
                    return;
                }
                return;
            case 2:
            case 18:
                if (setHidden) {
                    Zip4jUtil.setFileHidden(file);
                    return;
                }
                return;
            case 3:
                if (setReadOnly) {
                    Zip4jUtil.setFileReadOnly(file);
                }
                if (setHidden) {
                    Zip4jUtil.setFileHidden(file);
                    return;
                }
                return;
            case 32:
            case 48:
                if (setArchive) {
                    Zip4jUtil.setFileArchive(file);
                    return;
                }
                return;
            case 33:
                if (setArchive) {
                    Zip4jUtil.setFileArchive(file);
                }
                if (setReadOnly) {
                    Zip4jUtil.setFileReadOnly(file);
                    return;
                }
                return;
            case 34:
            case 50:
                if (setArchive) {
                    Zip4jUtil.setFileArchive(file);
                }
                if (setHidden) {
                    Zip4jUtil.setFileHidden(file);
                    return;
                }
                return;
            case 35:
                if (setArchive) {
                    Zip4jUtil.setFileArchive(file);
                }
                if (setReadOnly) {
                    Zip4jUtil.setFileReadOnly(file);
                }
                if (setHidden) {
                    Zip4jUtil.setFileHidden(file);
                    return;
                }
                return;
            case 38:
                if (setReadOnly) {
                    Zip4jUtil.setFileReadOnly(file);
                }
                if (setHidden) {
                    Zip4jUtil.setFileHidden(file);
                }
                if (setSystem) {
                    Zip4jUtil.setFileSystemMode(file);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private static void setFileLastModifiedTime(FileHeader fileHeader, File file) throws ZipException {
        if (fileHeader.getLastModFileTime() > 0 && file.exists()) {
            file.setLastModified(Zip4jUtil.dosToJavaTme(fileHeader.getLastModFileTime()));
        }
    }
}
