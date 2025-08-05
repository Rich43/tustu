package net.lingala.zip4j.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import net.lingala.zip4j.core.HeaderReader;
import net.lingala.zip4j.core.HeaderWriter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.SplitOutputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/util/ArchiveMaintainer.class */
public class ArchiveMaintainer {
    public HashMap removeZipFile(ZipModel zipModel, FileHeader fileHeader, ProgressMonitor progressMonitor, boolean runInThread) throws ZipException {
        if (runInThread) {
            Thread thread = new Thread(this, InternalZipConstants.THREAD_NAME, zipModel, fileHeader, progressMonitor) { // from class: net.lingala.zip4j.util.ArchiveMaintainer.1
                final ArchiveMaintainer this$0;
                private final ZipModel val$zipModel;
                private final FileHeader val$fileHeader;
                private final ProgressMonitor val$progressMonitor;

                {
                    this.this$0 = this;
                    this.val$zipModel = zipModel;
                    this.val$fileHeader = fileHeader;
                    this.val$progressMonitor = progressMonitor;
                }

                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        this.this$0.initRemoveZipFile(this.val$zipModel, this.val$fileHeader, this.val$progressMonitor);
                        this.val$progressMonitor.endProgressMonitorSuccess();
                    } catch (ZipException e2) {
                    }
                }
            };
            thread.start();
            return null;
        }
        HashMap retMap = initRemoveZipFile(zipModel, fileHeader, progressMonitor);
        progressMonitor.endProgressMonitorSuccess();
        return retMap;
    }

    public HashMap initRemoveZipFile(ZipModel zipModel, FileHeader fileHeader, ProgressMonitor progressMonitor) throws ZipException {
        if (fileHeader == null || zipModel == null) {
            throw new ZipException("input parameters is null in maintain zip file, cannot remove file from archive");
        }
        OutputStream outputStream = null;
        RandomAccessFile inputStream = null;
        HashMap retMap = new HashMap();
        try {
            try {
                try {
                    int indexOfFileHeader = Zip4jUtil.getIndexOfFileHeader(zipModel, fileHeader);
                    if (indexOfFileHeader < 0) {
                        throw new ZipException("file header not found in zip model, cannot remove file");
                    }
                    if (zipModel.isSplitArchive()) {
                        throw new ZipException("This is a split archive. Zip file format does not allow updating split/spanned files");
                    }
                    long currTime = System.currentTimeMillis();
                    String tmpZipFileName = new StringBuffer(String.valueOf(zipModel.getZipFile())).append(currTime % 1000).toString();
                    File tmpFile = new File(tmpZipFileName);
                    while (tmpFile.exists()) {
                        long currTime2 = System.currentTimeMillis();
                        tmpZipFileName = new StringBuffer(String.valueOf(zipModel.getZipFile())).append(currTime2 % 1000).toString();
                        tmpFile = new File(tmpZipFileName);
                    }
                    try {
                        OutputStream outputStream2 = new SplitOutputStream(new File(tmpZipFileName));
                        File zipFile = new File(zipModel.getZipFile());
                        RandomAccessFile inputStream2 = createFileHandler(zipModel, InternalZipConstants.READ_MODE);
                        HeaderReader headerReader = new HeaderReader(inputStream2);
                        LocalFileHeader localFileHeader = headerReader.readLocalFileHeader(fileHeader);
                        if (localFileHeader == null) {
                            throw new ZipException("invalid local file header, cannot remove file from archive");
                        }
                        long offsetLocalFileHeader = fileHeader.getOffsetLocalHeader();
                        if (fileHeader.getZip64ExtendedInfo() != null && fileHeader.getZip64ExtendedInfo().getOffsetLocalHeader() != -1) {
                            offsetLocalFileHeader = fileHeader.getZip64ExtendedInfo().getOffsetLocalHeader();
                        }
                        long offsetEndOfCompressedFile = -1;
                        long offsetStartCentralDir = zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir();
                        if (zipModel.isZip64Format() && zipModel.getZip64EndCentralDirRecord() != null) {
                            offsetStartCentralDir = zipModel.getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo();
                        }
                        ArrayList fileHeaderList = zipModel.getCentralDirectory().getFileHeaders();
                        if (indexOfFileHeader == fileHeaderList.size() - 1) {
                            offsetEndOfCompressedFile = offsetStartCentralDir - 1;
                        } else {
                            FileHeader nextFileHeader = (FileHeader) fileHeaderList.get(indexOfFileHeader + 1);
                            if (nextFileHeader != null) {
                                offsetEndOfCompressedFile = nextFileHeader.getOffsetLocalHeader() - 1;
                                if (nextFileHeader.getZip64ExtendedInfo() != null && nextFileHeader.getZip64ExtendedInfo().getOffsetLocalHeader() != -1) {
                                    offsetEndOfCompressedFile = nextFileHeader.getZip64ExtendedInfo().getOffsetLocalHeader() - 1;
                                }
                            }
                        }
                        if (offsetLocalFileHeader < 0 || offsetEndOfCompressedFile < 0) {
                            throw new ZipException("invalid offset for start and end of local file, cannot remove file");
                        }
                        if (indexOfFileHeader == 0) {
                            if (zipModel.getCentralDirectory().getFileHeaders().size() > 1) {
                                copyFile(inputStream2, outputStream2, offsetEndOfCompressedFile + 1, offsetStartCentralDir, progressMonitor);
                            }
                        } else if (indexOfFileHeader == fileHeaderList.size() - 1) {
                            copyFile(inputStream2, outputStream2, 0L, offsetLocalFileHeader, progressMonitor);
                        } else {
                            copyFile(inputStream2, outputStream2, 0L, offsetLocalFileHeader, progressMonitor);
                            copyFile(inputStream2, outputStream2, offsetEndOfCompressedFile + 1, offsetStartCentralDir, progressMonitor);
                        }
                        if (progressMonitor.isCancelAllTasks()) {
                            progressMonitor.setResult(3);
                            progressMonitor.setState(0);
                            if (inputStream2 != null) {
                                try {
                                    inputStream2.close();
                                } catch (IOException e2) {
                                    throw new ZipException("cannot close input stream or output stream when trying to delete a file from zip file");
                                }
                            }
                            if (outputStream2 != null) {
                                outputStream2.close();
                            }
                            if (0 != 0) {
                                restoreFileName(zipFile, tmpZipFileName);
                                return null;
                            }
                            File newZipFile = new File(tmpZipFileName);
                            newZipFile.delete();
                            return null;
                        }
                        zipModel.getEndCentralDirRecord().setOffsetOfStartOfCentralDir(((SplitOutputStream) outputStream2).getFilePointer());
                        zipModel.getEndCentralDirRecord().setTotNoOfEntriesInCentralDir(zipModel.getEndCentralDirRecord().getTotNoOfEntriesInCentralDir() - 1);
                        zipModel.getEndCentralDirRecord().setTotNoOfEntriesInCentralDirOnThisDisk(zipModel.getEndCentralDirRecord().getTotNoOfEntriesInCentralDirOnThisDisk() - 1);
                        zipModel.getCentralDirectory().getFileHeaders().remove(indexOfFileHeader);
                        for (int i2 = indexOfFileHeader; i2 < zipModel.getCentralDirectory().getFileHeaders().size(); i2++) {
                            long offsetLocalHdr = ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i2)).getOffsetLocalHeader();
                            if (((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i2)).getZip64ExtendedInfo() != null && ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i2)).getZip64ExtendedInfo().getOffsetLocalHeader() != -1) {
                                offsetLocalHdr = ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i2)).getZip64ExtendedInfo().getOffsetLocalHeader();
                            }
                            ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i2)).setOffsetLocalHeader((offsetLocalHdr - (offsetEndOfCompressedFile - offsetLocalFileHeader)) - 1);
                        }
                        HeaderWriter headerWriter = new HeaderWriter();
                        headerWriter.finalizeZipFile(zipModel, outputStream2);
                        retMap.put(InternalZipConstants.OFFSET_CENTRAL_DIR, Long.toString(zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir()));
                        if (inputStream2 != null) {
                            try {
                                inputStream2.close();
                            } catch (IOException e3) {
                                throw new ZipException("cannot close input stream or output stream when trying to delete a file from zip file");
                            }
                        }
                        if (outputStream2 != null) {
                            outputStream2.close();
                        }
                        if (1 != 0) {
                            restoreFileName(zipFile, tmpZipFileName);
                        } else {
                            File newZipFile2 = new File(tmpZipFileName);
                            newZipFile2.delete();
                        }
                        return retMap;
                    } catch (FileNotFoundException e1) {
                        throw new ZipException(e1);
                    }
                } catch (Exception e4) {
                    progressMonitor.endProgressMonitorError(e4);
                    throw new ZipException(e4);
                }
            } catch (ZipException e5) {
                progressMonitor.endProgressMonitorError(e5);
                throw e5;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    inputStream.close();
                } catch (IOException e6) {
                    throw new ZipException("cannot close input stream or output stream when trying to delete a file from zip file");
                }
            }
            if (0 != 0) {
                outputStream.close();
            }
            if (0 != 0) {
                restoreFileName(null, null);
            } else {
                File newZipFile3 = new File((String) null);
                newZipFile3.delete();
            }
            throw th;
        }
    }

    private void restoreFileName(File zipFile, String tmpZipFileName) throws ZipException {
        if (zipFile.delete()) {
            File newZipFile = new File(tmpZipFileName);
            if (!newZipFile.renameTo(zipFile)) {
                throw new ZipException("cannot rename modified zip file");
            }
            return;
        }
        throw new ZipException("cannot delete old zip file");
    }

    private void copyFile(RandomAccessFile inputStream, OutputStream outputStream, long start, long end, ProgressMonitor progressMonitor) throws ZipException {
        byte[] buff;
        if (inputStream == null || outputStream == null) {
            throw new ZipException("input or output stream is null, cannot copy file");
        }
        if (start < 0) {
            throw new ZipException("starting offset is negative, cannot copy file");
        }
        if (end < 0) {
            throw new ZipException("end offset is negative, cannot copy file");
        }
        if (start > end) {
            throw new ZipException("start offset is greater than end offset, cannot copy file");
        }
        if (start == end) {
            return;
        }
        if (progressMonitor.isCancelAllTasks()) {
            progressMonitor.setResult(3);
            progressMonitor.setState(0);
            return;
        }
        try {
            inputStream.seek(start);
            long bytesRead = 0;
            long bytesToRead = end - start;
            if (end - start < 4096) {
                buff = new byte[(int) (end - start)];
            } else {
                buff = new byte[4096];
            }
            while (true) {
                int readLen = inputStream.read(buff);
                if (readLen != -1) {
                    outputStream.write(buff, 0, readLen);
                    progressMonitor.updateWorkCompleted(readLen);
                    if (progressMonitor.isCancelAllTasks()) {
                        progressMonitor.setResult(3);
                        return;
                    }
                    bytesRead += readLen;
                    if (bytesRead == bytesToRead) {
                        return;
                    }
                    if (bytesRead + buff.length > bytesToRead) {
                        buff = new byte[(int) (bytesToRead - bytesRead)];
                    }
                } else {
                    return;
                }
            }
        } catch (IOException e2) {
            throw new ZipException(e2);
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    private RandomAccessFile createFileHandler(ZipModel zipModel, String mode) throws ZipException {
        if (zipModel == null || !Zip4jUtil.isStringNotNullAndNotEmpty(zipModel.getZipFile())) {
            throw new ZipException("input parameter is null in getFilePointer, cannot create file handler to remove file");
        }
        try {
            return new RandomAccessFile(new File(zipModel.getZipFile()), mode);
        } catch (FileNotFoundException e2) {
            throw new ZipException(e2);
        }
    }

    public void mergeSplitZipFiles(ZipModel zipModel, File outputZipFile, ProgressMonitor progressMonitor, boolean runInThread) throws ZipException {
        if (runInThread) {
            Thread thread = new Thread(this, InternalZipConstants.THREAD_NAME, zipModel, outputZipFile, progressMonitor) { // from class: net.lingala.zip4j.util.ArchiveMaintainer.2
                final ArchiveMaintainer this$0;
                private final ZipModel val$zipModel;
                private final File val$outputZipFile;
                private final ProgressMonitor val$progressMonitor;

                {
                    this.this$0 = this;
                    this.val$zipModel = zipModel;
                    this.val$outputZipFile = outputZipFile;
                    this.val$progressMonitor = progressMonitor;
                }

                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        this.this$0.initMergeSplitZipFile(this.val$zipModel, this.val$outputZipFile, this.val$progressMonitor);
                    } catch (ZipException e2) {
                    }
                }
            };
            thread.start();
        } else {
            initMergeSplitZipFile(zipModel, outputZipFile, progressMonitor);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initMergeSplitZipFile(ZipModel zipModel, File outputZipFile, ProgressMonitor progressMonitor) throws ZipException {
        if (zipModel == null) {
            ZipException e2 = new ZipException("one of the input parameters is null, cannot merge split zip file");
            progressMonitor.endProgressMonitorError(e2);
            throw e2;
        }
        if (!zipModel.isSplitArchive()) {
            ZipException e3 = new ZipException("archive not a split zip file");
            progressMonitor.endProgressMonitorError(e3);
            throw e3;
        }
        OutputStream outputStream = null;
        RandomAccessFile inputStream = null;
        ArrayList fileSizeList = new ArrayList();
        long totBytesWritten = 0;
        boolean splitSigRemoved = false;
        try {
            try {
                try {
                    int totNoOfSplitFiles = zipModel.getEndCentralDirRecord().getNoOfThisDisk();
                    if (totNoOfSplitFiles <= 0) {
                        throw new ZipException("corrupt zip model, archive not a split zip file");
                    }
                    OutputStream outputStream2 = prepareOutputStreamForMerge(outputZipFile);
                    for (int i2 = 0; i2 <= totNoOfSplitFiles; i2++) {
                        inputStream = createSplitZipFileHandler(zipModel, i2);
                        int start = 0;
                        Long end = new Long(inputStream.length());
                        if (i2 == 0 && zipModel.getCentralDirectory() != null && zipModel.getCentralDirectory().getFileHeaders() != null && zipModel.getCentralDirectory().getFileHeaders().size() > 0) {
                            byte[] buff = new byte[4];
                            inputStream.seek(0L);
                            inputStream.read(buff);
                            if (Raw.readIntLittleEndian(buff, 0) == 134695760) {
                                start = 4;
                                splitSigRemoved = true;
                            }
                        }
                        if (i2 == totNoOfSplitFiles) {
                            end = new Long(zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir());
                        }
                        copyFile(inputStream, outputStream2, start, end.longValue(), progressMonitor);
                        totBytesWritten += end.longValue() - start;
                        if (progressMonitor.isCancelAllTasks()) {
                            progressMonitor.setResult(3);
                            progressMonitor.setState(0);
                            if (outputStream2 != null) {
                                try {
                                    outputStream2.close();
                                } catch (IOException e4) {
                                }
                            }
                            if (inputStream == null) {
                                return;
                            }
                            try {
                                inputStream.close();
                                return;
                            } catch (IOException e5) {
                                return;
                            }
                        }
                        fileSizeList.add(end);
                        try {
                            inputStream.close();
                        } catch (IOException e6) {
                        }
                    }
                    ZipModel newZipModel = (ZipModel) zipModel.clone();
                    newZipModel.getEndCentralDirRecord().setOffsetOfStartOfCentralDir(totBytesWritten);
                    updateSplitZipModel(newZipModel, fileSizeList, splitSigRemoved);
                    HeaderWriter headerWriter = new HeaderWriter();
                    headerWriter.finalizeZipFileWithoutValidations(newZipModel, outputStream2);
                    progressMonitor.endProgressMonitorSuccess();
                    if (outputStream2 != null) {
                        try {
                            outputStream2.close();
                        } catch (IOException e7) {
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e8) {
                        }
                    }
                } catch (IOException e9) {
                    progressMonitor.endProgressMonitorError(e9);
                    throw new ZipException(e9);
                }
            } catch (Exception e10) {
                progressMonitor.endProgressMonitorError(e10);
                throw new ZipException(e10);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    outputStream.close();
                } catch (IOException e11) {
                }
            }
            if (0 != 0) {
                try {
                    inputStream.close();
                } catch (IOException e12) {
                }
            }
            throw th;
        }
    }

    private RandomAccessFile createSplitZipFileHandler(ZipModel zipModel, int partNumber) throws ZipException {
        String partFile;
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot create split file handler");
        }
        if (partNumber < 0) {
            throw new ZipException("invlaid part number, cannot create split file handler");
        }
        try {
            String curZipFile = zipModel.getZipFile();
            if (partNumber == zipModel.getEndCentralDirRecord().getNoOfThisDisk()) {
                partFile = zipModel.getZipFile();
            } else if (partNumber >= 9) {
                partFile = new StringBuffer(String.valueOf(curZipFile.substring(0, curZipFile.lastIndexOf(".")))).append(".z").append(partNumber + 1).toString();
            } else {
                partFile = new StringBuffer(String.valueOf(curZipFile.substring(0, curZipFile.lastIndexOf(".")))).append(".z0").append(partNumber + 1).toString();
            }
            File tmpFile = new File(partFile);
            if (!Zip4jUtil.checkFileExists(tmpFile)) {
                throw new ZipException(new StringBuffer("split file does not exist: ").append(partFile).toString());
            }
            return new RandomAccessFile(tmpFile, InternalZipConstants.READ_MODE);
        } catch (FileNotFoundException e2) {
            throw new ZipException(e2);
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    private OutputStream prepareOutputStreamForMerge(File outFile) throws ZipException {
        if (outFile == null) {
            throw new ZipException("outFile is null, cannot create outputstream");
        }
        try {
            return new FileOutputStream(outFile);
        } catch (FileNotFoundException e2) {
            throw new ZipException(e2);
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    private void updateSplitZipModel(ZipModel zipModel, ArrayList fileSizeList, boolean splitSigRemoved) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot update split zip model");
        }
        zipModel.setSplitArchive(false);
        updateSplitFileHeader(zipModel, fileSizeList, splitSigRemoved);
        updateSplitEndCentralDirectory(zipModel);
        if (zipModel.isZip64Format()) {
            updateSplitZip64EndCentralDirLocator(zipModel, fileSizeList);
            updateSplitZip64EndCentralDirRec(zipModel, fileSizeList);
        }
    }

    private void updateSplitFileHeader(ZipModel zipModel, ArrayList fileSizeList, boolean splitSigRemoved) throws ZipException {
        try {
            if (zipModel.getCentralDirectory() == null) {
                throw new ZipException("corrupt zip model - getCentralDirectory, cannot update split zip model");
            }
            int fileHeaderCount = zipModel.getCentralDirectory().getFileHeaders().size();
            int splitSigOverhead = 0;
            if (splitSigRemoved) {
                splitSigOverhead = 4;
            }
            for (int i2 = 0; i2 < fileHeaderCount; i2++) {
                long offsetLHToAdd = 0;
                for (int j2 = 0; j2 < ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i2)).getDiskNumberStart(); j2++) {
                    offsetLHToAdd += ((Long) fileSizeList.get(j2)).longValue();
                }
                ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i2)).setOffsetLocalHeader((((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i2)).getOffsetLocalHeader() + offsetLHToAdd) - splitSigOverhead);
                ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i2)).setDiskNumberStart(0);
            }
        } catch (ZipException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    private void updateSplitEndCentralDirectory(ZipModel zipModel) throws ZipException {
        try {
            if (zipModel == null) {
                throw new ZipException("zip model is null - cannot update end of central directory for split zip model");
            }
            if (zipModel.getCentralDirectory() == null) {
                throw new ZipException("corrupt zip model - getCentralDirectory, cannot update split zip model");
            }
            zipModel.getEndCentralDirRecord().setNoOfThisDisk(0);
            zipModel.getEndCentralDirRecord().setNoOfThisDiskStartOfCentralDir(0);
            zipModel.getEndCentralDirRecord().setTotNoOfEntriesInCentralDir(zipModel.getCentralDirectory().getFileHeaders().size());
            zipModel.getEndCentralDirRecord().setTotNoOfEntriesInCentralDirOnThisDisk(zipModel.getCentralDirectory().getFileHeaders().size());
        } catch (ZipException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    private void updateSplitZip64EndCentralDirLocator(ZipModel zipModel, ArrayList fileSizeList) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot update split Zip64 end of central directory locator");
        }
        if (zipModel.getZip64EndCentralDirLocator() == null) {
            return;
        }
        zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(0);
        long offsetZip64EndCentralDirRec = 0;
        for (int i2 = 0; i2 < fileSizeList.size(); i2++) {
            offsetZip64EndCentralDirRec += ((Long) fileSizeList.get(i2)).longValue();
        }
        zipModel.getZip64EndCentralDirLocator().setOffsetZip64EndOfCentralDirRec(zipModel.getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec() + offsetZip64EndCentralDirRec);
        zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(1);
    }

    private void updateSplitZip64EndCentralDirRec(ZipModel zipModel, ArrayList fileSizeList) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot update split Zip64 end of central directory record");
        }
        if (zipModel.getZip64EndCentralDirRecord() == null) {
            return;
        }
        zipModel.getZip64EndCentralDirRecord().setNoOfThisDisk(0);
        zipModel.getZip64EndCentralDirRecord().setNoOfThisDiskStartOfCentralDir(0);
        zipModel.getZip64EndCentralDirRecord().setTotNoOfEntriesInCentralDirOnThisDisk(zipModel.getEndCentralDirRecord().getTotNoOfEntriesInCentralDir());
        long offsetStartCenDirWRTStartDiskNo = 0;
        for (int i2 = 0; i2 < fileSizeList.size(); i2++) {
            offsetStartCenDirWRTStartDiskNo += ((Long) fileSizeList.get(i2)).longValue();
        }
        zipModel.getZip64EndCentralDirRecord().setOffsetStartCenDirWRTStartDiskNo(zipModel.getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo() + offsetStartCenDirWRTStartDiskNo);
    }

    public void setComment(ZipModel zipModel, String comment) throws ZipException {
        if (comment == null) {
            throw new ZipException("comment is null, cannot update Zip file with comment");
        }
        if (zipModel == null) {
            throw new ZipException("zipModel is null, cannot update Zip file with comment");
        }
        String encodedComment = comment;
        byte[] commentBytes = comment.getBytes();
        int commentLength = comment.length();
        if (Zip4jUtil.isSupportedCharset(InternalZipConstants.CHARSET_COMMENTS_DEFAULT)) {
            try {
                encodedComment = new String(comment.getBytes(InternalZipConstants.CHARSET_COMMENTS_DEFAULT), InternalZipConstants.CHARSET_COMMENTS_DEFAULT);
                commentBytes = encodedComment.getBytes(InternalZipConstants.CHARSET_COMMENTS_DEFAULT);
                commentLength = encodedComment.length();
            } catch (UnsupportedEncodingException e2) {
                encodedComment = comment;
                commentBytes = comment.getBytes();
                commentLength = comment.length();
            }
        }
        if (commentLength > 65535) {
            throw new ZipException("comment length exceeds maximum length");
        }
        zipModel.getEndCentralDirRecord().setComment(encodedComment);
        zipModel.getEndCentralDirRecord().setCommentBytes(commentBytes);
        zipModel.getEndCentralDirRecord().setCommentLength(commentLength);
        SplitOutputStream outputStream = null;
        try {
            try {
                HeaderWriter headerWriter = new HeaderWriter();
                outputStream = new SplitOutputStream(zipModel.getZipFile());
                if (zipModel.isZip64Format()) {
                    outputStream.seek(zipModel.getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo());
                } else {
                    outputStream.seek(zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir());
                }
                headerWriter.finalizeZipFileWithoutValidations(zipModel, outputStream);
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (FileNotFoundException e4) {
                throw new ZipException(e4);
            } catch (IOException e5) {
                throw new ZipException(e5);
            }
        } catch (Throwable th) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e6) {
                }
            }
            throw th;
        }
    }

    public void initProgressMonitorForRemoveOp(ZipModel zipModel, FileHeader fileHeader, ProgressMonitor progressMonitor) throws ZipException {
        if (zipModel == null || fileHeader == null || progressMonitor == null) {
            throw new ZipException("one of the input parameters is null, cannot calculate total work");
        }
        progressMonitor.setCurrentOperation(2);
        progressMonitor.setFileName(fileHeader.getFileName());
        progressMonitor.setTotalWork(calculateTotalWorkForRemoveOp(zipModel, fileHeader));
        progressMonitor.setState(1);
    }

    private long calculateTotalWorkForRemoveOp(ZipModel zipModel, FileHeader fileHeader) throws ZipException {
        return Zip4jUtil.getFileLengh(new File(zipModel.getZipFile())) - fileHeader.getCompressedSize();
    }

    public void initProgressMonitorForMergeOp(ZipModel zipModel, ProgressMonitor progressMonitor) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot calculate total work for merge op");
        }
        progressMonitor.setCurrentOperation(4);
        progressMonitor.setFileName(zipModel.getZipFile());
        progressMonitor.setTotalWork(calculateTotalWorkForMergeOp(zipModel));
        progressMonitor.setState(1);
    }

    private long calculateTotalWorkForMergeOp(ZipModel zipModel) throws ZipException {
        String string;
        long totSize = 0;
        if (zipModel.isSplitArchive()) {
            int totNoOfSplitFiles = zipModel.getEndCentralDirRecord().getNoOfThisDisk();
            String curZipFile = zipModel.getZipFile();
            for (int i2 = 0; i2 <= totNoOfSplitFiles; i2++) {
                if (0 == zipModel.getEndCentralDirRecord().getNoOfThisDisk()) {
                    string = zipModel.getZipFile();
                } else {
                    string = 0 >= 9 ? new StringBuffer(String.valueOf(curZipFile.substring(0, curZipFile.lastIndexOf(".")))).append(".z").append(0 + 1).toString() : new StringBuffer(String.valueOf(curZipFile.substring(0, curZipFile.lastIndexOf(".")))).append(".z0").append(0 + 1).toString();
                }
                String partFile = string;
                totSize += Zip4jUtil.getFileLengh(new File(partFile));
            }
        }
        return totSize;
    }
}
