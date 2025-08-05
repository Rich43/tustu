package net.lingala.zip4j.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.SplitOutputStream;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.EndCentralDirRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.ArchiveMaintainer;
import net.lingala.zip4j.util.CRCUtil;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/zip/ZipEngine.class */
public class ZipEngine {
    private ZipModel zipModel;

    public ZipEngine(ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null in ZipEngine constructor");
        }
        this.zipModel = zipModel;
    }

    public void addFiles(ArrayList fileList, ZipParameters parameters, ProgressMonitor progressMonitor, boolean runInThread) throws ZipException {
        if (fileList == null || parameters == null) {
            throw new ZipException("one of the input parameters is null when adding files");
        }
        if (fileList.size() <= 0) {
            throw new ZipException("no files to add");
        }
        progressMonitor.setTotalWork(calculateTotalWork(fileList, parameters));
        progressMonitor.setCurrentOperation(0);
        progressMonitor.setState(1);
        progressMonitor.setResult(1);
        if (runInThread) {
            Thread thread = new Thread(this, InternalZipConstants.THREAD_NAME, fileList, parameters, progressMonitor) { // from class: net.lingala.zip4j.zip.ZipEngine.1
                final ZipEngine this$0;
                private final ArrayList val$fileList;
                private final ZipParameters val$parameters;
                private final ProgressMonitor val$progressMonitor;

                {
                    this.this$0 = this;
                    this.val$fileList = fileList;
                    this.val$parameters = parameters;
                    this.val$progressMonitor = progressMonitor;
                }

                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        this.this$0.initAddFiles(this.val$fileList, this.val$parameters, this.val$progressMonitor);
                    } catch (ZipException e2) {
                    }
                }
            };
            thread.start();
        } else {
            initAddFiles(fileList, parameters, progressMonitor);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAddFiles(ArrayList fileList, ZipParameters parameters, ProgressMonitor progressMonitor) throws ZipException {
        if (fileList == null || parameters == null) {
            throw new ZipException("one of the input parameters is null when adding files");
        }
        if (fileList.size() <= 0) {
            throw new ZipException("no files to add");
        }
        if (this.zipModel.getEndCentralDirRecord() == null) {
            this.zipModel.setEndCentralDirRecord(createEndOfCentralDirectoryRecord());
        }
        ZipOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            try {
                try {
                    checkParameters(parameters);
                    removeFilesIfExists(fileList, parameters, progressMonitor);
                    boolean isZipFileAlreadExists = Zip4jUtil.checkFileExists(this.zipModel.getZipFile());
                    SplitOutputStream splitOutputStream = new SplitOutputStream(new File(this.zipModel.getZipFile()), this.zipModel.getSplitLength());
                    ZipOutputStream outputStream2 = new ZipOutputStream(splitOutputStream, this.zipModel);
                    if (isZipFileAlreadExists) {
                        if (this.zipModel.getEndCentralDirRecord() == null) {
                            throw new ZipException("invalid end of central directory record");
                        }
                        splitOutputStream.seek(this.zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir());
                    }
                    byte[] readBuff = new byte[4096];
                    for (int i2 = 0; i2 < fileList.size(); i2++) {
                        ZipParameters fileParameters = (ZipParameters) parameters.clone();
                        progressMonitor.setFileName(((File) fileList.get(i2)).getAbsolutePath());
                        if (!((File) fileList.get(i2)).isDirectory()) {
                            if (fileParameters.isEncryptFiles() && fileParameters.getEncryptionMethod() == 0) {
                                progressMonitor.setCurrentOperation(3);
                                fileParameters.setSourceFileCRC((int) CRCUtil.computeFileCRC(((File) fileList.get(i2)).getAbsolutePath(), progressMonitor));
                                progressMonitor.setCurrentOperation(0);
                            }
                            if (Zip4jUtil.getFileLengh((File) fileList.get(i2)) == 0) {
                                fileParameters.setCompressionMethod(0);
                            }
                        }
                        outputStream2.putNextEntry((File) fileList.get(i2), fileParameters);
                        if (((File) fileList.get(i2)).isDirectory()) {
                            outputStream2.closeEntry();
                        } else {
                            inputStream = new FileInputStream((File) fileList.get(i2));
                            while (true) {
                                int readLen = inputStream.read(readBuff);
                                if (readLen == -1) {
                                    break;
                                }
                                outputStream2.write(readBuff, 0, readLen);
                                progressMonitor.updateWorkCompleted(readLen);
                            }
                            outputStream2.closeEntry();
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        }
                    }
                    outputStream2.finish();
                    progressMonitor.endProgressMonitorSuccess();
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e2) {
                        }
                    }
                    if (outputStream2 != null) {
                        try {
                            outputStream2.close();
                        } catch (IOException e3) {
                        }
                    }
                } catch (ZipException e4) {
                    progressMonitor.endProgressMonitorError(e4);
                    throw e4;
                }
            } catch (Exception e5) {
                progressMonitor.endProgressMonitorError(e5);
                throw new ZipException(e5);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    inputStream.close();
                } catch (IOException e6) {
                }
            }
            if (0 != 0) {
                try {
                    outputStream.close();
                } catch (IOException e7) {
                }
            }
            throw th;
        }
    }

    public void addStreamToZip(InputStream inputStream, ZipParameters parameters) throws ZipException {
        if (inputStream == null || parameters == null) {
            throw new ZipException("one of the input parameters is null, cannot add stream to zip");
        }
        ZipOutputStream outputStream = null;
        try {
            try {
                checkParameters(parameters);
                boolean isZipFileAlreadExists = Zip4jUtil.checkFileExists(this.zipModel.getZipFile());
                SplitOutputStream splitOutputStream = new SplitOutputStream(new File(this.zipModel.getZipFile()), this.zipModel.getSplitLength());
                ZipOutputStream outputStream2 = new ZipOutputStream(splitOutputStream, this.zipModel);
                if (isZipFileAlreadExists) {
                    if (this.zipModel.getEndCentralDirRecord() == null) {
                        throw new ZipException("invalid end of central directory record");
                    }
                    splitOutputStream.seek(this.zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir());
                }
                byte[] readBuff = new byte[4096];
                outputStream2.putNextEntry(null, parameters);
                if (!parameters.getFileNameInZip().endsWith("/") && !parameters.getFileNameInZip().endsWith(FXMLLoader.ESCAPE_PREFIX)) {
                    while (true) {
                        int readLen = inputStream.read(readBuff);
                        if (readLen == -1) {
                            break;
                        } else {
                            outputStream2.write(readBuff, 0, readLen);
                        }
                    }
                }
                outputStream2.closeEntry();
                outputStream2.finish();
                if (outputStream2 != null) {
                    try {
                        outputStream2.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (ZipException e3) {
                throw e3;
            } catch (Exception e4) {
                throw new ZipException(e4);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    outputStream.close();
                } catch (IOException e5) {
                }
            }
            throw th;
        }
    }

    public void addFolderToZip(File file, ZipParameters parameters, ProgressMonitor progressMonitor, boolean runInThread) throws ZipException {
        String rootFolderPath;
        if (file == null || parameters == null) {
            throw new ZipException("one of the input parameters is null, cannot add folder to zip");
        }
        if (!Zip4jUtil.checkFileExists(file.getAbsolutePath())) {
            throw new ZipException("input folder does not exist");
        }
        if (!file.isDirectory()) {
            throw new ZipException("input file is not a folder, user addFileToZip method to add files");
        }
        if (!Zip4jUtil.checkFileReadAccess(file.getAbsolutePath())) {
            throw new ZipException(new StringBuffer("cannot read folder: ").append(file.getAbsolutePath()).toString());
        }
        if (parameters.isIncludeRootFolder()) {
            if (file.getAbsolutePath() != null) {
                rootFolderPath = file.getAbsoluteFile().getParentFile() != null ? file.getAbsoluteFile().getParentFile().getAbsolutePath() : "";
            } else {
                rootFolderPath = file.getParentFile() != null ? file.getParentFile().getAbsolutePath() : "";
            }
        } else {
            rootFolderPath = file.getAbsolutePath();
        }
        parameters.setDefaultFolderPath(rootFolderPath);
        ArrayList fileList = Zip4jUtil.getFilesInDirectoryRec(file, parameters.isReadHiddenFiles());
        if (parameters.isIncludeRootFolder()) {
            if (fileList == null) {
                fileList = new ArrayList();
            }
            fileList.add(file);
        }
        addFiles(fileList, parameters, progressMonitor, runInThread);
    }

    private void checkParameters(ZipParameters parameters) throws ZipException {
        if (parameters == null) {
            throw new ZipException("cannot validate zip parameters");
        }
        if (parameters.getCompressionMethod() != 0 && parameters.getCompressionMethod() != 8) {
            throw new ZipException("unsupported compression type");
        }
        if (parameters.getCompressionMethod() == 8 && parameters.getCompressionLevel() < 0 && parameters.getCompressionLevel() > 9) {
            throw new ZipException("invalid compression level. compression level dor deflate should be in the range of 0-9");
        }
        if (parameters.isEncryptFiles()) {
            if (parameters.getEncryptionMethod() != 0 && parameters.getEncryptionMethod() != 99) {
                throw new ZipException("unsupported encryption method");
            }
            if (parameters.getPassword() == null || parameters.getPassword().length <= 0) {
                throw new ZipException("input password is empty or null");
            }
            return;
        }
        parameters.setAesKeyStrength(-1);
        parameters.setEncryptionMethod(-1);
    }

    private void removeFilesIfExists(ArrayList fileList, ZipParameters parameters, ProgressMonitor progressMonitor) throws ZipException {
        if (this.zipModel == null || this.zipModel.getCentralDirectory() == null || this.zipModel.getCentralDirectory().getFileHeaders() == null || this.zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
            return;
        }
        RandomAccessFile outputStream = null;
        try {
            for (int i2 = 0; i2 < fileList.size(); i2++) {
                try {
                    File file = (File) fileList.get(i2);
                    String fileName = Zip4jUtil.getRelativeFileName(file.getAbsolutePath(), parameters.getRootFolderInZip(), parameters.getDefaultFolderPath());
                    FileHeader fileHeader = Zip4jUtil.getFileHeader(this.zipModel, fileName);
                    if (fileHeader != null) {
                        if (outputStream != null) {
                            outputStream.close();
                            outputStream = null;
                        }
                        ArchiveMaintainer archiveMaintainer = new ArchiveMaintainer();
                        progressMonitor.setCurrentOperation(2);
                        HashMap retMap = archiveMaintainer.initRemoveZipFile(this.zipModel, fileHeader, progressMonitor);
                        if (progressMonitor.isCancelAllTasks()) {
                            progressMonitor.setResult(3);
                            progressMonitor.setState(0);
                            if (outputStream == null) {
                                return;
                            }
                            try {
                                outputStream.close();
                                return;
                            } catch (IOException e2) {
                                return;
                            }
                        }
                        progressMonitor.setCurrentOperation(0);
                        if (outputStream == null) {
                            outputStream = prepareFileOutputStream();
                            if (retMap != null && retMap.get(InternalZipConstants.OFFSET_CENTRAL_DIR) != null) {
                                try {
                                    long offsetCentralDir = Long.parseLong((String) retMap.get(InternalZipConstants.OFFSET_CENTRAL_DIR));
                                    if (offsetCentralDir >= 0) {
                                        outputStream.seek(offsetCentralDir);
                                    }
                                } catch (NumberFormatException e3) {
                                    throw new ZipException("NumberFormatException while parsing offset central directory. Cannot update already existing file header");
                                } catch (Exception e4) {
                                    throw new ZipException("Error while parsing offset central directory. Cannot update already existing file header");
                                }
                            }
                        } else {
                            continue;
                        }
                    }
                } catch (IOException e5) {
                    throw new ZipException(e5);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e6) {
                }
            }
        } catch (Throwable th) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e7) {
                }
            }
            throw th;
        }
    }

    private RandomAccessFile prepareFileOutputStream() throws ZipException {
        String outPath = this.zipModel.getZipFile();
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(outPath)) {
            throw new ZipException("invalid output path");
        }
        try {
            File outFile = new File(outPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            return new RandomAccessFile(outFile, InternalZipConstants.WRITE_MODE);
        } catch (FileNotFoundException e2) {
            throw new ZipException(e2);
        }
    }

    private EndCentralDirRecord createEndOfCentralDirectoryRecord() {
        EndCentralDirRecord endCentralDirRecord = new EndCentralDirRecord();
        endCentralDirRecord.setSignature(101010256L);
        endCentralDirRecord.setNoOfThisDisk(0);
        endCentralDirRecord.setTotNoOfEntriesInCentralDir(0);
        endCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(0);
        endCentralDirRecord.setOffsetOfStartOfCentralDir(0L);
        return endCentralDirRecord;
    }

    private long calculateTotalWork(ArrayList fileList, ZipParameters parameters) throws ZipException {
        if (fileList == null) {
            throw new ZipException("file list is null, cannot calculate total work");
        }
        long totalWork = 0;
        for (int i2 = 0; i2 < fileList.size(); i2++) {
            if ((fileList.get(i2) instanceof File) && ((File) fileList.get(i2)).exists()) {
                if (parameters.isEncryptFiles() && parameters.getEncryptionMethod() == 0) {
                    totalWork += Zip4jUtil.getFileLengh((File) fileList.get(i2)) * 2;
                } else {
                    totalWork += Zip4jUtil.getFileLengh((File) fileList.get(i2));
                }
                if (this.zipModel.getCentralDirectory() != null && this.zipModel.getCentralDirectory().getFileHeaders() != null && this.zipModel.getCentralDirectory().getFileHeaders().size() > 0) {
                    String relativeFileName = Zip4jUtil.getRelativeFileName(((File) fileList.get(i2)).getAbsolutePath(), parameters.getRootFolderInZip(), parameters.getDefaultFolderPath());
                    FileHeader fileHeader = Zip4jUtil.getFileHeader(this.zipModel, relativeFileName);
                    if (fileHeader != null) {
                        totalWork += Zip4jUtil.getFileLengh(new File(this.zipModel.getZipFile())) - fileHeader.getCompressedSize();
                    }
                }
            }
        }
        return totalWork;
    }
}
