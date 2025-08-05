package net.lingala.zip4j.unzip;

import java.io.File;
import java.util.ArrayList;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/unzip/Unzip.class */
public class Unzip {
    private ZipModel zipModel;

    public Unzip(ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("ZipModel is null");
        }
        this.zipModel = zipModel;
    }

    public void extractAll(UnzipParameters unzipParameters, String outPath, ProgressMonitor progressMonitor, boolean runInThread) throws ZipException {
        CentralDirectory centralDirectory = this.zipModel.getCentralDirectory();
        if (centralDirectory == null || centralDirectory.getFileHeaders() == null) {
            throw new ZipException("invalid central directory in zipModel");
        }
        ArrayList fileHeaders = centralDirectory.getFileHeaders();
        progressMonitor.setCurrentOperation(1);
        progressMonitor.setTotalWork(calculateTotalWork(fileHeaders));
        progressMonitor.setState(1);
        if (runInThread) {
            Thread thread = new Thread(this, InternalZipConstants.THREAD_NAME, fileHeaders, unzipParameters, progressMonitor, outPath) { // from class: net.lingala.zip4j.unzip.Unzip.1
                final Unzip this$0;
                private final ArrayList val$fileHeaders;
                private final UnzipParameters val$unzipParameters;
                private final ProgressMonitor val$progressMonitor;
                private final String val$outPath;

                {
                    this.this$0 = this;
                    this.val$fileHeaders = fileHeaders;
                    this.val$unzipParameters = unzipParameters;
                    this.val$progressMonitor = progressMonitor;
                    this.val$outPath = outPath;
                }

                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        this.this$0.initExtractAll(this.val$fileHeaders, this.val$unzipParameters, this.val$progressMonitor, this.val$outPath);
                        this.val$progressMonitor.endProgressMonitorSuccess();
                    } catch (ZipException e2) {
                    }
                }
            };
            thread.start();
        } else {
            initExtractAll(fileHeaders, unzipParameters, progressMonitor, outPath);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initExtractAll(ArrayList fileHeaders, UnzipParameters unzipParameters, ProgressMonitor progressMonitor, String outPath) throws ZipException {
        for (int i2 = 0; i2 < fileHeaders.size(); i2++) {
            FileHeader fileHeader = (FileHeader) fileHeaders.get(i2);
            initExtractFile(fileHeader, outPath, unzipParameters, null, progressMonitor);
            if (progressMonitor.isCancelAllTasks()) {
                progressMonitor.setResult(3);
                progressMonitor.setState(0);
                return;
            }
        }
    }

    public void extractFile(FileHeader fileHeader, String outPath, UnzipParameters unzipParameters, String newFileName, ProgressMonitor progressMonitor, boolean runInThread) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("fileHeader is null");
        }
        progressMonitor.setCurrentOperation(1);
        progressMonitor.setTotalWork(fileHeader.getCompressedSize());
        progressMonitor.setState(1);
        progressMonitor.setPercentDone(0);
        progressMonitor.setFileName(fileHeader.getFileName());
        if (runInThread) {
            Thread thread = new Thread(this, InternalZipConstants.THREAD_NAME, fileHeader, outPath, unzipParameters, newFileName, progressMonitor) { // from class: net.lingala.zip4j.unzip.Unzip.2
                final Unzip this$0;
                private final FileHeader val$fileHeader;
                private final String val$outPath;
                private final UnzipParameters val$unzipParameters;
                private final String val$newFileName;
                private final ProgressMonitor val$progressMonitor;

                {
                    this.this$0 = this;
                    this.val$fileHeader = fileHeader;
                    this.val$outPath = outPath;
                    this.val$unzipParameters = unzipParameters;
                    this.val$newFileName = newFileName;
                    this.val$progressMonitor = progressMonitor;
                }

                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        this.this$0.initExtractFile(this.val$fileHeader, this.val$outPath, this.val$unzipParameters, this.val$newFileName, this.val$progressMonitor);
                        this.val$progressMonitor.endProgressMonitorSuccess();
                    } catch (ZipException e2) {
                    }
                }
            };
            thread.start();
        } else {
            initExtractFile(fileHeader, outPath, unzipParameters, newFileName, progressMonitor);
            progressMonitor.endProgressMonitorSuccess();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initExtractFile(FileHeader fileHeader, String outPath, UnzipParameters unzipParameters, String newFileName, ProgressMonitor progressMonitor) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("fileHeader is null");
        }
        try {
            progressMonitor.setFileName(fileHeader.getFileName());
            if (!outPath.endsWith(InternalZipConstants.FILE_SEPARATOR)) {
                outPath = new StringBuffer(String.valueOf(outPath)).append(InternalZipConstants.FILE_SEPARATOR).toString();
            }
            if (fileHeader.isDirectory()) {
                try {
                    String fileName = fileHeader.getFileName();
                    if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
                        return;
                    }
                    String completePath = new StringBuffer(String.valueOf(outPath)).append(fileName).toString();
                    File file = new File(completePath);
                    if (!file.exists()) {
                        file.mkdirs();
                        return;
                    }
                    return;
                } catch (Exception e2) {
                    progressMonitor.endProgressMonitorError(e2);
                    throw new ZipException(e2);
                }
            }
            checkOutputDirectoryStructure(fileHeader, outPath, newFileName);
            UnzipEngine unzipEngine = new UnzipEngine(this.zipModel, fileHeader);
            try {
                unzipEngine.unzipFile(progressMonitor, outPath, newFileName, unzipParameters);
            } catch (Exception e3) {
                progressMonitor.endProgressMonitorError(e3);
                throw new ZipException(e3);
            }
        } catch (ZipException e4) {
            progressMonitor.endProgressMonitorError(e4);
            throw e4;
        } catch (Exception e5) {
            progressMonitor.endProgressMonitorError(e5);
            throw new ZipException(e5);
        }
    }

    public ZipInputStream getInputStream(FileHeader fileHeader) throws ZipException {
        UnzipEngine unzipEngine = new UnzipEngine(this.zipModel, fileHeader);
        return unzipEngine.getInputStream();
    }

    private void checkOutputDirectoryStructure(FileHeader fileHeader, String outPath, String newFileName) throws ZipException {
        if (fileHeader == null || !Zip4jUtil.isStringNotNullAndNotEmpty(outPath)) {
            throw new ZipException("Cannot check output directory structure...one of the parameters was null");
        }
        String fileName = fileHeader.getFileName();
        if (Zip4jUtil.isStringNotNullAndNotEmpty(newFileName)) {
            fileName = newFileName;
        }
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
            return;
        }
        String compOutPath = new StringBuffer(String.valueOf(outPath)).append(fileName).toString();
        try {
            File file = new File(compOutPath);
            String parentDir = file.getParent();
            File parentDirFile = new File(parentDir);
            if (!parentDirFile.exists()) {
                parentDirFile.mkdirs();
            }
        } catch (Exception e2) {
            throw new ZipException(e2);
        }
    }

    private long calculateTotalWork(ArrayList fileHeaders) throws ZipException {
        long j2;
        long compressedSize;
        if (fileHeaders == null) {
            throw new ZipException("fileHeaders is null, cannot calculate total work");
        }
        long totalWork = 0;
        for (int i2 = 0; i2 < fileHeaders.size(); i2++) {
            FileHeader fileHeader = (FileHeader) fileHeaders.get(i2);
            if (fileHeader.getZip64ExtendedInfo() != null && fileHeader.getZip64ExtendedInfo().getUnCompressedSize() > 0) {
                j2 = totalWork;
                compressedSize = fileHeader.getZip64ExtendedInfo().getCompressedSize();
            } else {
                j2 = totalWork;
                compressedSize = fileHeader.getCompressedSize();
            }
            totalWork = j2 + compressedSize;
        }
        return totalWork;
    }
}
