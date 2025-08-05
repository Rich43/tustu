package net.lingala.zip4j.unzip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.zip.CRC32;
import net.lingala.zip4j.core.HeaderReader;
import net.lingala.zip4j.crypto.AESDecrypter;
import net.lingala.zip4j.crypto.IDecrypter;
import net.lingala.zip4j.crypto.StandardDecrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.InflaterInputStream;
import net.lingala.zip4j.io.PartInputStream;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/unzip/UnzipEngine.class */
public class UnzipEngine {
    private ZipModel zipModel;
    private FileHeader fileHeader;
    private int currSplitFileCounter = 0;
    private LocalFileHeader localFileHeader;
    private IDecrypter decrypter;
    private CRC32 crc;

    public UnzipEngine(ZipModel zipModel, FileHeader fileHeader) throws ZipException {
        if (zipModel == null || fileHeader == null) {
            throw new ZipException("Invalid parameters passed to StoreUnzip. One or more of the parameters were null");
        }
        this.zipModel = zipModel;
        this.fileHeader = fileHeader;
        this.crc = new CRC32();
    }

    public void unzipFile(ProgressMonitor progressMonitor, String outPath, String newFileName, UnzipParameters unzipParameters) throws ZipException {
        if (this.zipModel == null || this.fileHeader == null || !Zip4jUtil.isStringNotNullAndNotEmpty(outPath)) {
            throw new ZipException("Invalid parameters passed during unzipping file. One or more of the parameters were null");
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            try {
                byte[] buff = new byte[4096];
                is = getInputStream();
                os = getOutputStream(outPath, newFileName);
                do {
                    int readLength = is.read(buff);
                    if (readLength != -1) {
                        os.write(buff, 0, readLength);
                        progressMonitor.updateWorkCompleted(readLength);
                    } else {
                        closeStreams(is, os);
                        UnzipUtil.applyFileAttributes(this.fileHeader, new File(getOutputFileNameWithPath(outPath, newFileName)), unzipParameters);
                        return;
                    }
                } while (!progressMonitor.isCancelAllTasks());
                progressMonitor.setResult(3);
                progressMonitor.setState(0);
            } catch (IOException e2) {
                throw new ZipException(e2);
            } catch (Exception e3) {
                throw new ZipException(e3);
            }
        } finally {
            closeStreams(is, os);
        }
    }

    public ZipInputStream getInputStream() throws ZipException {
        if (this.fileHeader == null) {
            throw new ZipException("file header is null, cannot get inputstream");
        }
        RandomAccessFile raf = null;
        try {
            RandomAccessFile raf2 = createFileHandler(InternalZipConstants.READ_MODE);
            if (!checkLocalHeader()) {
                throw new ZipException("local header and file header do not match");
            }
            init(raf2);
            long comprSize = this.localFileHeader.getCompressedSize();
            long offsetStartOfData = this.localFileHeader.getOffsetStartOfData();
            if (this.localFileHeader.isEncrypted()) {
                if (this.localFileHeader.getEncryptionMethod() == 99) {
                    if (this.decrypter instanceof AESDecrypter) {
                        comprSize -= (((AESDecrypter) this.decrypter).getSaltLength() + ((AESDecrypter) this.decrypter).getPasswordVerifierLength()) + 10;
                        offsetStartOfData += ((AESDecrypter) this.decrypter).getSaltLength() + ((AESDecrypter) this.decrypter).getPasswordVerifierLength();
                    } else {
                        throw new ZipException(new StringBuffer("invalid decryptor when trying to calculate compressed size for AES encrypted file: ").append(this.fileHeader.getFileName()).toString());
                    }
                } else if (this.localFileHeader.getEncryptionMethod() == 0) {
                    comprSize -= 12;
                    offsetStartOfData += 12;
                }
            }
            int compressionMethod = this.fileHeader.getCompressionMethod();
            if (this.fileHeader.getEncryptionMethod() == 99) {
                if (this.fileHeader.getAesExtraDataRecord() != null) {
                    compressionMethod = this.fileHeader.getAesExtraDataRecord().getCompressionMethod();
                } else {
                    throw new ZipException(new StringBuffer("AESExtraDataRecord does not exist for AES encrypted file: ").append(this.fileHeader.getFileName()).toString());
                }
            }
            raf2.seek(offsetStartOfData);
            switch (compressionMethod) {
                case 0:
                    return new ZipInputStream(new PartInputStream(raf2, offsetStartOfData, comprSize, this));
                case 8:
                    return new ZipInputStream(new InflaterInputStream(raf2, offsetStartOfData, comprSize, this));
                default:
                    throw new ZipException("compression type not supported");
            }
        } catch (ZipException e2) {
            if (0 != 0) {
                try {
                    raf.close();
                } catch (IOException e3) {
                }
            }
            throw e2;
        } catch (Exception e4) {
            if (0 != 0) {
                try {
                    raf.close();
                } catch (IOException e5) {
                }
            }
            throw new ZipException(e4);
        }
    }

    private void init(RandomAccessFile raf) throws ZipException {
        if (this.localFileHeader == null) {
            throw new ZipException("local file header is null, cannot initialize input stream");
        }
        try {
            initDecrypter(raf);
        } catch (ZipException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    private void initDecrypter(RandomAccessFile raf) throws ZipException {
        if (this.localFileHeader == null) {
            throw new ZipException("local file header is null, cannot init decrypter");
        }
        if (this.localFileHeader.isEncrypted()) {
            if (this.localFileHeader.getEncryptionMethod() == 0) {
                this.decrypter = new StandardDecrypter(this.fileHeader, getStandardDecrypterHeaderBytes(raf));
            } else {
                if (this.localFileHeader.getEncryptionMethod() == 99) {
                    this.decrypter = new AESDecrypter(this.localFileHeader, getAESSalt(raf), getAESPasswordVerifier(raf));
                    return;
                }
                throw new ZipException("unsupported encryption method");
            }
        }
    }

    private byte[] getStandardDecrypterHeaderBytes(RandomAccessFile raf) throws ZipException {
        try {
            byte[] headerBytes = new byte[12];
            raf.seek(this.localFileHeader.getOffsetStartOfData());
            raf.read(headerBytes, 0, 12);
            return headerBytes;
        } catch (IOException e2) {
            throw new ZipException(e2);
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    private byte[] getAESSalt(RandomAccessFile raf) throws ZipException {
        if (this.localFileHeader.getAesExtraDataRecord() == null) {
            return null;
        }
        try {
            AESExtraDataRecord aesExtraDataRecord = this.localFileHeader.getAesExtraDataRecord();
            byte[] saltBytes = new byte[calculateAESSaltLength(aesExtraDataRecord)];
            raf.seek(this.localFileHeader.getOffsetStartOfData());
            raf.read(saltBytes);
            return saltBytes;
        } catch (IOException e2) {
            throw new ZipException(e2);
        }
    }

    private byte[] getAESPasswordVerifier(RandomAccessFile raf) throws ZipException {
        try {
            byte[] pvBytes = new byte[2];
            raf.read(pvBytes);
            return pvBytes;
        } catch (IOException e2) {
            throw new ZipException(e2);
        }
    }

    private int calculateAESSaltLength(AESExtraDataRecord aesExtraDataRecord) throws ZipException {
        if (aesExtraDataRecord == null) {
            throw new ZipException("unable to determine salt length: AESExtraDataRecord is null");
        }
        switch (aesExtraDataRecord.getAesStrength()) {
            case 1:
                return 8;
            case 2:
                return 12;
            case 3:
                return 16;
            default:
                throw new ZipException("unable to determine salt length: invalid aes key strength");
        }
    }

    public void checkCRC() throws ZipException {
        if (this.fileHeader != null) {
            if (this.fileHeader.getEncryptionMethod() == 99) {
                if (this.decrypter != null && (this.decrypter instanceof AESDecrypter)) {
                    byte[] tmpMacBytes = ((AESDecrypter) this.decrypter).getCalculatedAuthenticationBytes();
                    byte[] storedMac = ((AESDecrypter) this.decrypter).getStoredMac();
                    byte[] calculatedMac = new byte[10];
                    if (calculatedMac == null || storedMac == null) {
                        throw new ZipException(new StringBuffer("CRC (MAC) check failed for ").append(this.fileHeader.getFileName()).toString());
                    }
                    System.arraycopy(tmpMacBytes, 0, calculatedMac, 0, 10);
                    if (!Arrays.equals(calculatedMac, storedMac)) {
                        throw new ZipException(new StringBuffer("invalid CRC (MAC) for file: ").append(this.fileHeader.getFileName()).toString());
                    }
                    return;
                }
                return;
            }
            long calculatedCRC = this.crc.getValue() & 4294967295L;
            if (calculatedCRC != this.fileHeader.getCrc32()) {
                String errMsg = new StringBuffer("invalid CRC for file: ").append(this.fileHeader.getFileName()).toString();
                if (this.localFileHeader.isEncrypted() && this.localFileHeader.getEncryptionMethod() == 0) {
                    errMsg = new StringBuffer(String.valueOf(errMsg)).append(" - Wrong Password?").toString();
                }
                throw new ZipException(errMsg);
            }
        }
    }

    private boolean checkLocalHeader() throws ZipException {
        RandomAccessFile rafForLH = null;
        try {
            try {
                RandomAccessFile rafForLH2 = checkSplitFile();
                if (rafForLH2 == null) {
                    rafForLH2 = new RandomAccessFile(new File(this.zipModel.getZipFile()), InternalZipConstants.READ_MODE);
                }
                HeaderReader headerReader = new HeaderReader(rafForLH2);
                this.localFileHeader = headerReader.readLocalFileHeader(this.fileHeader);
                if (this.localFileHeader == null) {
                    throw new ZipException("error reading local file header. Is this a valid zip file?");
                }
                if (this.localFileHeader.getCompressionMethod() != this.fileHeader.getCompressionMethod()) {
                    if (rafForLH2 == null) {
                        return false;
                    }
                    try {
                        rafForLH2.close();
                        return false;
                    } catch (IOException e2) {
                        return false;
                    } catch (Exception e3) {
                        return false;
                    }
                }
                if (rafForLH2 == null) {
                    return true;
                }
                try {
                    rafForLH2.close();
                    return true;
                } catch (IOException e4) {
                    return true;
                } catch (Exception e5) {
                    return true;
                }
            } catch (FileNotFoundException e6) {
                throw new ZipException(e6);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    rafForLH.close();
                } catch (IOException e7) {
                } catch (Exception e8) {
                }
            }
            throw th;
        }
    }

    private RandomAccessFile checkSplitFile() throws ZipException {
        String partFile;
        if (this.zipModel.isSplitArchive()) {
            int diskNumberStartOfFile = this.fileHeader.getDiskNumberStart();
            this.currSplitFileCounter = diskNumberStartOfFile + 1;
            String curZipFile = this.zipModel.getZipFile();
            if (diskNumberStartOfFile == this.zipModel.getEndCentralDirRecord().getNoOfThisDisk()) {
                partFile = this.zipModel.getZipFile();
            } else if (diskNumberStartOfFile >= 9) {
                partFile = new StringBuffer(String.valueOf(curZipFile.substring(0, curZipFile.lastIndexOf(".")))).append(".z").append(diskNumberStartOfFile + 1).toString();
            } else {
                partFile = new StringBuffer(String.valueOf(curZipFile.substring(0, curZipFile.lastIndexOf(".")))).append(".z0").append(diskNumberStartOfFile + 1).toString();
            }
            try {
                RandomAccessFile raf = new RandomAccessFile(partFile, InternalZipConstants.READ_MODE);
                if (this.currSplitFileCounter == 1) {
                    byte[] splitSig = new byte[4];
                    raf.read(splitSig);
                    if (Raw.readIntLittleEndian(splitSig, 0) != 134695760) {
                        throw new ZipException("invalid first part split file signature");
                    }
                }
                return raf;
            } catch (FileNotFoundException e2) {
                throw new ZipException(e2);
            } catch (IOException e3) {
                throw new ZipException(e3);
            }
        }
        return null;
    }

    private RandomAccessFile createFileHandler(String mode) throws ZipException {
        RandomAccessFile raf;
        if (this.zipModel == null || !Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getZipFile())) {
            throw new ZipException("input parameter is null in getFilePointer");
        }
        try {
            if (this.zipModel.isSplitArchive()) {
                raf = checkSplitFile();
            } else {
                raf = new RandomAccessFile(new File(this.zipModel.getZipFile()), mode);
            }
            return raf;
        } catch (FileNotFoundException e2) {
            throw new ZipException(e2);
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    private FileOutputStream getOutputStream(String outPath, String newFileName) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(outPath)) {
            throw new ZipException("invalid output path");
        }
        try {
            File file = new File(getOutputFileNameWithPath(outPath, newFileName));
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            return fileOutputStream;
        } catch (FileNotFoundException e2) {
            throw new ZipException(e2);
        }
    }

    private String getOutputFileNameWithPath(String outPath, String newFileName) throws ZipException {
        String fileName;
        if (Zip4jUtil.isStringNotNullAndNotEmpty(newFileName)) {
            fileName = newFileName;
        } else {
            fileName = this.fileHeader.getFileName();
        }
        return new StringBuffer(String.valueOf(outPath)).append(System.getProperty("file.separator")).append(fileName).toString();
    }

    public RandomAccessFile startNextSplitFile() throws IOException {
        String partFile;
        String currZipFile = this.zipModel.getZipFile();
        if (this.currSplitFileCounter == this.zipModel.getEndCentralDirRecord().getNoOfThisDisk()) {
            partFile = this.zipModel.getZipFile();
        } else if (this.currSplitFileCounter >= 9) {
            partFile = new StringBuffer(String.valueOf(currZipFile.substring(0, currZipFile.lastIndexOf(".")))).append(".z").append(this.currSplitFileCounter + 1).toString();
        } else {
            partFile = new StringBuffer(String.valueOf(currZipFile.substring(0, currZipFile.lastIndexOf(".")))).append(".z0").append(this.currSplitFileCounter + 1).toString();
        }
        this.currSplitFileCounter++;
        try {
            if (!Zip4jUtil.checkFileExists(partFile)) {
                throw new IOException(new StringBuffer("zip split file does not exist: ").append(partFile).toString());
            }
            return new RandomAccessFile(partFile, InternalZipConstants.READ_MODE);
        } catch (ZipException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    private void closeStreams(InputStream is, OutputStream os) throws ZipException {
        try {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e2) {
                    if (e2 != null && Zip4jUtil.isStringNotNullAndNotEmpty(e2.getMessage()) && e2.getMessage().indexOf(" - Wrong Password?") >= 0) {
                        throw new ZipException(e2.getMessage());
                    }
                    if (os != null) {
                        try {
                            os.close();
                            return;
                        } catch (IOException e3) {
                            return;
                        }
                    }
                    return;
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e4) {
                }
            }
        } catch (Throwable th) {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e5) {
                }
            }
            throw th;
        }
    }

    public void updateCRC(int b2) {
        this.crc.update(b2);
    }

    public void updateCRC(byte[] buff, int offset, int len) {
        if (buff != null) {
            this.crc.update(buff, offset, len);
        }
    }

    public FileHeader getFileHeader() {
        return this.fileHeader;
    }

    public IDecrypter getDecrypter() {
        return this.decrypter;
    }

    public ZipModel getZipModel() {
        return this.zipModel;
    }

    public LocalFileHeader getLocalFileHeader() {
        return this.localFileHeader;
    }
}
