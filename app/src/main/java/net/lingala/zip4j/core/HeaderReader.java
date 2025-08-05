package net.lingala.zip4j.core;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.DigitalSignature;
import net.lingala.zip4j.model.EndCentralDirRecord;
import net.lingala.zip4j.model.ExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip64EndCentralDirLocator;
import net.lingala.zip4j.model.Zip64EndCentralDirRecord;
import net.lingala.zip4j.model.Zip64ExtendedInfo;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/core/HeaderReader.class */
public class HeaderReader {
    private RandomAccessFile zip4jRaf;
    private ZipModel zipModel;

    public HeaderReader(RandomAccessFile zip4jRaf) {
        this.zip4jRaf = null;
        this.zip4jRaf = zip4jRaf;
    }

    public ZipModel readAllHeaders() throws ZipException {
        return readAllHeaders(null);
    }

    public ZipModel readAllHeaders(String fileNameCharset) throws ZipException {
        this.zipModel = new ZipModel();
        this.zipModel.setFileNameCharset(fileNameCharset);
        this.zipModel.setEndCentralDirRecord(readEndOfCentralDirectoryRecord());
        this.zipModel.setZip64EndCentralDirLocator(readZip64EndCentralDirLocator());
        if (this.zipModel.isZip64Format()) {
            this.zipModel.setZip64EndCentralDirRecord(readZip64EndCentralDirRec());
            if (this.zipModel.getZip64EndCentralDirRecord() != null && this.zipModel.getZip64EndCentralDirRecord().getNoOfThisDisk() > 0) {
                this.zipModel.setSplitArchive(true);
            } else {
                this.zipModel.setSplitArchive(false);
            }
        }
        this.zipModel.setCentralDirectory(readCentralDirectory());
        return this.zipModel;
    }

    /* JADX WARN: Type inference failed for: r0v12, types: [java.io.RandomAccessFile, long] */
    private EndCentralDirRecord readEndOfCentralDirectoryRecord() throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("random access file was null", 3);
        }
        try {
            byte[] ebs = new byte[4];
            long pos = this.zip4jRaf.length() - 22;
            EndCentralDirRecord endCentralDirRecord = new EndCentralDirRecord();
            int counter = 0;
            do {
                ?? r0 = this.zip4jRaf;
                pos--;
                r0.seek(r0);
                counter++;
                if (Raw.readLeInt(this.zip4jRaf, ebs) == 101010256) {
                    break;
                }
            } while (counter <= 3000);
            if (Raw.readIntLittleEndian(ebs, 0) != 101010256) {
                throw new ZipException("zip headers not found. probably not a zip file");
            }
            byte[] intBuff = new byte[4];
            byte[] shortBuff = new byte[2];
            endCentralDirRecord.setSignature(101010256L);
            readIntoBuff(this.zip4jRaf, shortBuff);
            endCentralDirRecord.setNoOfThisDisk(Raw.readShortLittleEndian(shortBuff, 0));
            readIntoBuff(this.zip4jRaf, shortBuff);
            endCentralDirRecord.setNoOfThisDiskStartOfCentralDir(Raw.readShortLittleEndian(shortBuff, 0));
            readIntoBuff(this.zip4jRaf, shortBuff);
            endCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(Raw.readShortLittleEndian(shortBuff, 0));
            readIntoBuff(this.zip4jRaf, shortBuff);
            endCentralDirRecord.setTotNoOfEntriesInCentralDir(Raw.readShortLittleEndian(shortBuff, 0));
            readIntoBuff(this.zip4jRaf, intBuff);
            endCentralDirRecord.setSizeOfCentralDir(Raw.readIntLittleEndian(intBuff, 0));
            readIntoBuff(this.zip4jRaf, intBuff);
            byte[] longBuff = getLongByteFromIntByte(intBuff);
            endCentralDirRecord.setOffsetOfStartOfCentralDir(Raw.readLongLittleEndian(longBuff, 0));
            readIntoBuff(this.zip4jRaf, shortBuff);
            int commentLength = Raw.readShortLittleEndian(shortBuff, 0);
            endCentralDirRecord.setCommentLength(commentLength);
            if (commentLength > 0) {
                byte[] commentBuf = new byte[commentLength];
                readIntoBuff(this.zip4jRaf, commentBuf);
                endCentralDirRecord.setComment(new String(commentBuf));
                endCentralDirRecord.setCommentBytes(commentBuf);
            } else {
                endCentralDirRecord.setComment(null);
            }
            int diskNumber = endCentralDirRecord.getNoOfThisDisk();
            if (diskNumber > 0) {
                this.zipModel.setSplitArchive(true);
            } else {
                this.zipModel.setSplitArchive(false);
            }
            return endCentralDirRecord;
        } catch (IOException e2) {
            throw new ZipException("Probably not a zip file or a corrupted zip file", e2, 4);
        }
    }

    private CentralDirectory readCentralDirectory() throws ZipException {
        String fileName;
        if (this.zip4jRaf == null) {
            throw new ZipException("random access file was null", 3);
        }
        if (this.zipModel.getEndCentralDirRecord() == null) {
            throw new ZipException("EndCentralRecord was null, maybe a corrupt zip file");
        }
        try {
            CentralDirectory centralDirectory = new CentralDirectory();
            ArrayList fileHeaderList = new ArrayList();
            EndCentralDirRecord endCentralDirRecord = this.zipModel.getEndCentralDirRecord();
            long offSetStartCentralDir = endCentralDirRecord.getOffsetOfStartOfCentralDir();
            int centralDirEntryCount = endCentralDirRecord.getTotNoOfEntriesInCentralDir();
            if (this.zipModel.isZip64Format()) {
                offSetStartCentralDir = this.zipModel.getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo();
                centralDirEntryCount = (int) this.zipModel.getZip64EndCentralDirRecord().getTotNoOfEntriesInCentralDir();
            }
            this.zip4jRaf.seek(offSetStartCentralDir);
            byte[] intBuff = new byte[4];
            byte[] shortBuff = new byte[2];
            byte[] bArr = new byte[8];
            for (int i2 = 0; i2 < centralDirEntryCount; i2++) {
                FileHeader fileHeader = new FileHeader();
                readIntoBuff(this.zip4jRaf, intBuff);
                int signature = Raw.readIntLittleEndian(intBuff, 0);
                if (signature != 33639248) {
                    throw new ZipException(new StringBuffer("Expected central directory entry not found (#").append(i2 + 1).append(")").toString());
                }
                fileHeader.setSignature(signature);
                readIntoBuff(this.zip4jRaf, shortBuff);
                fileHeader.setVersionMadeBy(Raw.readShortLittleEndian(shortBuff, 0));
                readIntoBuff(this.zip4jRaf, shortBuff);
                fileHeader.setVersionNeededToExtract(Raw.readShortLittleEndian(shortBuff, 0));
                readIntoBuff(this.zip4jRaf, shortBuff);
                fileHeader.setFileNameUTF8Encoded((Raw.readShortLittleEndian(shortBuff, 0) & 2048) != 0);
                byte b2 = shortBuff[0];
                int result = b2 & 1;
                if (result != 0) {
                    fileHeader.setEncrypted(true);
                }
                fileHeader.setGeneralPurposeFlag((byte[]) shortBuff.clone());
                fileHeader.setDataDescriptorExists((b2 >> 3) == 1);
                readIntoBuff(this.zip4jRaf, shortBuff);
                fileHeader.setCompressionMethod(Raw.readShortLittleEndian(shortBuff, 0));
                readIntoBuff(this.zip4jRaf, intBuff);
                fileHeader.setLastModFileTime(Raw.readIntLittleEndian(intBuff, 0));
                readIntoBuff(this.zip4jRaf, intBuff);
                fileHeader.setCrc32(Raw.readIntLittleEndian(intBuff, 0));
                fileHeader.setCrcBuff((byte[]) intBuff.clone());
                readIntoBuff(this.zip4jRaf, intBuff);
                byte[] longBuff = getLongByteFromIntByte(intBuff);
                fileHeader.setCompressedSize(Raw.readLongLittleEndian(longBuff, 0));
                readIntoBuff(this.zip4jRaf, intBuff);
                byte[] longBuff2 = getLongByteFromIntByte(intBuff);
                fileHeader.setUncompressedSize(Raw.readLongLittleEndian(longBuff2, 0));
                readIntoBuff(this.zip4jRaf, shortBuff);
                int fileNameLength = Raw.readShortLittleEndian(shortBuff, 0);
                fileHeader.setFileNameLength(fileNameLength);
                readIntoBuff(this.zip4jRaf, shortBuff);
                int extraFieldLength = Raw.readShortLittleEndian(shortBuff, 0);
                fileHeader.setExtraFieldLength(extraFieldLength);
                readIntoBuff(this.zip4jRaf, shortBuff);
                int fileCommentLength = Raw.readShortLittleEndian(shortBuff, 0);
                fileHeader.setFileComment(new String(shortBuff));
                readIntoBuff(this.zip4jRaf, shortBuff);
                fileHeader.setDiskNumberStart(Raw.readShortLittleEndian(shortBuff, 0));
                readIntoBuff(this.zip4jRaf, shortBuff);
                fileHeader.setInternalFileAttr((byte[]) shortBuff.clone());
                readIntoBuff(this.zip4jRaf, intBuff);
                fileHeader.setExternalFileAttr((byte[]) intBuff.clone());
                readIntoBuff(this.zip4jRaf, intBuff);
                byte[] longBuff3 = getLongByteFromIntByte(intBuff);
                fileHeader.setOffsetLocalHeader(Raw.readLongLittleEndian(longBuff3, 0) & 4294967295L);
                if (fileNameLength > 0) {
                    byte[] fileNameBuf = new byte[fileNameLength];
                    readIntoBuff(this.zip4jRaf, fileNameBuf);
                    if (Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getFileNameCharset())) {
                        fileName = new String(fileNameBuf, this.zipModel.getFileNameCharset());
                    } else {
                        fileName = Zip4jUtil.decodeFileName(fileNameBuf, fileHeader.isFileNameUTF8Encoded());
                    }
                    if (fileName == null) {
                        throw new ZipException("fileName is null when reading central directory");
                    }
                    if (fileName.indexOf(new StringBuffer(CallSiteDescriptor.TOKEN_DELIMITER).append(System.getProperty("file.separator")).toString()) >= 0) {
                        fileName = fileName.substring(fileName.indexOf(new StringBuffer(CallSiteDescriptor.TOKEN_DELIMITER).append(System.getProperty("file.separator")).toString()) + 2);
                    }
                    fileHeader.setFileName(fileName);
                    fileHeader.setDirectory(fileName.endsWith("/") || fileName.endsWith(FXMLLoader.ESCAPE_PREFIX));
                } else {
                    fileHeader.setFileName(null);
                }
                readAndSaveExtraDataRecord(fileHeader);
                readAndSaveZip64ExtendedInfo(fileHeader);
                readAndSaveAESExtraDataRecord(fileHeader);
                if (fileCommentLength > 0) {
                    byte[] fileCommentBuf = new byte[fileCommentLength];
                    readIntoBuff(this.zip4jRaf, fileCommentBuf);
                    fileHeader.setFileComment(new String(fileCommentBuf));
                }
                fileHeaderList.add(fileHeader);
            }
            centralDirectory.setFileHeaders(fileHeaderList);
            DigitalSignature digitalSignature = new DigitalSignature();
            readIntoBuff(this.zip4jRaf, intBuff);
            int signature2 = Raw.readIntLittleEndian(intBuff, 0);
            if (signature2 != InternalZipConstants.DIGSIG) {
                return centralDirectory;
            }
            digitalSignature.setHeaderSignature(signature2);
            readIntoBuff(this.zip4jRaf, shortBuff);
            int sizeOfData = Raw.readShortLittleEndian(shortBuff, 0);
            digitalSignature.setSizeOfData(sizeOfData);
            if (sizeOfData > 0) {
                byte[] sigDataBuf = new byte[sizeOfData];
                readIntoBuff(this.zip4jRaf, sigDataBuf);
                digitalSignature.setSignatureData(new String(sigDataBuf));
            }
            return centralDirectory;
        } catch (IOException e2) {
            throw new ZipException(e2);
        }
    }

    private void readAndSaveExtraDataRecord(FileHeader fileHeader) throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("invalid file handler when trying to read extra data record");
        }
        if (fileHeader == null) {
            throw new ZipException("file header is null");
        }
        int extraFieldLength = fileHeader.getExtraFieldLength();
        if (extraFieldLength <= 0) {
            return;
        }
        fileHeader.setExtraDataRecords(readExtraDataRecords(extraFieldLength));
    }

    private void readAndSaveExtraDataRecord(LocalFileHeader localFileHeader) throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("invalid file handler when trying to read extra data record");
        }
        if (localFileHeader == null) {
            throw new ZipException("file header is null");
        }
        int extraFieldLength = localFileHeader.getExtraFieldLength();
        if (extraFieldLength <= 0) {
            return;
        }
        localFileHeader.setExtraDataRecords(readExtraDataRecords(extraFieldLength));
    }

    private ArrayList readExtraDataRecords(int extraFieldLength) throws ZipException {
        if (extraFieldLength <= 0) {
            return null;
        }
        try {
            byte[] extraFieldBuf = new byte[extraFieldLength];
            this.zip4jRaf.read(extraFieldBuf);
            int counter = 0;
            ArrayList extraDataList = new ArrayList();
            while (counter < extraFieldLength) {
                ExtraDataRecord extraDataRecord = new ExtraDataRecord();
                int header = Raw.readShortLittleEndian(extraFieldBuf, counter);
                extraDataRecord.setHeader(header);
                int counter2 = counter + 2;
                int sizeOfRec = Raw.readShortLittleEndian(extraFieldBuf, counter2);
                if (2 + sizeOfRec > extraFieldLength) {
                    sizeOfRec = Raw.readShortBigEndian(extraFieldBuf, counter2);
                    if (2 + sizeOfRec > extraFieldLength) {
                        break;
                    }
                }
                extraDataRecord.setSizeOfData(sizeOfRec);
                int counter3 = counter2 + 2;
                if (sizeOfRec > 0) {
                    byte[] data = new byte[sizeOfRec];
                    System.arraycopy(extraFieldBuf, counter3, data, 0, sizeOfRec);
                    extraDataRecord.setData(data);
                }
                counter = counter3 + sizeOfRec;
                extraDataList.add(extraDataRecord);
            }
            if (extraDataList.size() > 0) {
                return extraDataList;
            }
            return null;
        } catch (IOException e2) {
            throw new ZipException(e2);
        }
    }

    private Zip64EndCentralDirLocator readZip64EndCentralDirLocator() throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("invalid file handler when trying to read Zip64EndCentralDirLocator");
        }
        try {
            Zip64EndCentralDirLocator zip64EndCentralDirLocator = new Zip64EndCentralDirLocator();
            setFilePointerToReadZip64EndCentralDirLoc();
            byte[] intBuff = new byte[4];
            byte[] longBuff = new byte[8];
            readIntoBuff(this.zip4jRaf, intBuff);
            int signature = Raw.readIntLittleEndian(intBuff, 0);
            if (signature == InternalZipConstants.ZIP64ENDCENDIRLOC) {
                this.zipModel.setZip64Format(true);
                zip64EndCentralDirLocator.setSignature(signature);
                readIntoBuff(this.zip4jRaf, intBuff);
                zip64EndCentralDirLocator.setNoOfDiskStartOfZip64EndOfCentralDirRec(Raw.readIntLittleEndian(intBuff, 0));
                readIntoBuff(this.zip4jRaf, longBuff);
                zip64EndCentralDirLocator.setOffsetZip64EndOfCentralDirRec(Raw.readLongLittleEndian(longBuff, 0));
                readIntoBuff(this.zip4jRaf, intBuff);
                zip64EndCentralDirLocator.setTotNumberOfDiscs(Raw.readIntLittleEndian(intBuff, 0));
                return zip64EndCentralDirLocator;
            }
            this.zipModel.setZip64Format(false);
            return null;
        } catch (Exception e2) {
            throw new ZipException(e2);
        }
    }

    private Zip64EndCentralDirRecord readZip64EndCentralDirRec() throws ZipException {
        if (this.zipModel.getZip64EndCentralDirLocator() == null) {
            throw new ZipException("invalid zip64 end of central directory locator");
        }
        long offSetStartOfZip64CentralDir = this.zipModel.getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec();
        if (offSetStartOfZip64CentralDir >= 0) {
            try {
                this.zip4jRaf.seek(offSetStartOfZip64CentralDir);
                Zip64EndCentralDirRecord zip64EndCentralDirRecord = new Zip64EndCentralDirRecord();
                byte[] shortBuff = new byte[2];
                byte[] intBuff = new byte[4];
                byte[] longBuff = new byte[8];
                readIntoBuff(this.zip4jRaf, intBuff);
                int signature = Raw.readIntLittleEndian(intBuff, 0);
                if (signature != InternalZipConstants.ZIP64ENDCENDIRREC) {
                    throw new ZipException("invalid signature for zip64 end of central directory record");
                }
                zip64EndCentralDirRecord.setSignature(signature);
                readIntoBuff(this.zip4jRaf, longBuff);
                zip64EndCentralDirRecord.setSizeOfZip64EndCentralDirRec(Raw.readLongLittleEndian(longBuff, 0));
                readIntoBuff(this.zip4jRaf, shortBuff);
                zip64EndCentralDirRecord.setVersionMadeBy(Raw.readShortLittleEndian(shortBuff, 0));
                readIntoBuff(this.zip4jRaf, shortBuff);
                zip64EndCentralDirRecord.setVersionNeededToExtract(Raw.readShortLittleEndian(shortBuff, 0));
                readIntoBuff(this.zip4jRaf, intBuff);
                zip64EndCentralDirRecord.setNoOfThisDisk(Raw.readIntLittleEndian(intBuff, 0));
                readIntoBuff(this.zip4jRaf, intBuff);
                zip64EndCentralDirRecord.setNoOfThisDiskStartOfCentralDir(Raw.readIntLittleEndian(intBuff, 0));
                readIntoBuff(this.zip4jRaf, longBuff);
                zip64EndCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(Raw.readLongLittleEndian(longBuff, 0));
                readIntoBuff(this.zip4jRaf, longBuff);
                zip64EndCentralDirRecord.setTotNoOfEntriesInCentralDir(Raw.readLongLittleEndian(longBuff, 0));
                readIntoBuff(this.zip4jRaf, longBuff);
                zip64EndCentralDirRecord.setSizeOfCentralDir(Raw.readLongLittleEndian(longBuff, 0));
                readIntoBuff(this.zip4jRaf, longBuff);
                zip64EndCentralDirRecord.setOffsetStartCenDirWRTStartDiskNo(Raw.readLongLittleEndian(longBuff, 0));
                long extDataSecSize = zip64EndCentralDirRecord.getSizeOfZip64EndCentralDirRec() - 44;
                if (extDataSecSize > 0) {
                    byte[] extDataSecRecBuf = new byte[(int) extDataSecSize];
                    readIntoBuff(this.zip4jRaf, extDataSecRecBuf);
                    zip64EndCentralDirRecord.setExtensibleDataSector(extDataSecRecBuf);
                }
                return zip64EndCentralDirRecord;
            } catch (IOException e2) {
                throw new ZipException(e2);
            }
        }
        throw new ZipException("invalid offset for start of end of central directory record");
    }

    private void readAndSaveZip64ExtendedInfo(FileHeader fileHeader) throws ZipException {
        Zip64ExtendedInfo zip64ExtendedInfo;
        if (fileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (fileHeader.getExtraDataRecords() != null && fileHeader.getExtraDataRecords().size() > 0 && (zip64ExtendedInfo = readZip64ExtendedInfo(fileHeader.getExtraDataRecords(), fileHeader.getUncompressedSize(), fileHeader.getCompressedSize(), fileHeader.getOffsetLocalHeader(), fileHeader.getDiskNumberStart())) != null) {
            fileHeader.setZip64ExtendedInfo(zip64ExtendedInfo);
            if (zip64ExtendedInfo.getUnCompressedSize() != -1) {
                fileHeader.setUncompressedSize(zip64ExtendedInfo.getUnCompressedSize());
            }
            if (zip64ExtendedInfo.getCompressedSize() != -1) {
                fileHeader.setCompressedSize(zip64ExtendedInfo.getCompressedSize());
            }
            if (zip64ExtendedInfo.getOffsetLocalHeader() != -1) {
                fileHeader.setOffsetLocalHeader(zip64ExtendedInfo.getOffsetLocalHeader());
            }
            if (zip64ExtendedInfo.getDiskNumberStart() != -1) {
                fileHeader.setDiskNumberStart(zip64ExtendedInfo.getDiskNumberStart());
            }
        }
    }

    private void readAndSaveZip64ExtendedInfo(LocalFileHeader localFileHeader) throws ZipException {
        Zip64ExtendedInfo zip64ExtendedInfo;
        if (localFileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (localFileHeader.getExtraDataRecords() != null && localFileHeader.getExtraDataRecords().size() > 0 && (zip64ExtendedInfo = readZip64ExtendedInfo(localFileHeader.getExtraDataRecords(), localFileHeader.getUncompressedSize(), localFileHeader.getCompressedSize(), -1L, -1)) != null) {
            localFileHeader.setZip64ExtendedInfo(zip64ExtendedInfo);
            if (zip64ExtendedInfo.getUnCompressedSize() != -1) {
                localFileHeader.setUncompressedSize(zip64ExtendedInfo.getUnCompressedSize());
            }
            if (zip64ExtendedInfo.getCompressedSize() != -1) {
                localFileHeader.setCompressedSize(zip64ExtendedInfo.getCompressedSize());
            }
        }
    }

    private Zip64ExtendedInfo readZip64ExtendedInfo(ArrayList extraDataRecords, long unCompressedSize, long compressedSize, long offsetLocalHeader, int diskNumberStart) throws ZipException {
        for (int i2 = 0; i2 < extraDataRecords.size(); i2++) {
            ExtraDataRecord extraDataRecord = (ExtraDataRecord) extraDataRecords.get(i2);
            if (extraDataRecord != null && extraDataRecord.getHeader() == 1) {
                Zip64ExtendedInfo zip64ExtendedInfo = new Zip64ExtendedInfo();
                byte[] byteBuff = extraDataRecord.getData();
                if (extraDataRecord.getSizeOfData() > 0) {
                    byte[] longByteBuff = new byte[8];
                    byte[] intByteBuff = new byte[4];
                    int counter = 0;
                    boolean valueAdded = false;
                    if ((unCompressedSize & 65535) == 65535 && 0 < extraDataRecord.getSizeOfData()) {
                        System.arraycopy(byteBuff, 0, longByteBuff, 0, 8);
                        long val = Raw.readLongLittleEndian(longByteBuff, 0);
                        zip64ExtendedInfo.setUnCompressedSize(val);
                        counter = 0 + 8;
                        valueAdded = true;
                    }
                    if ((compressedSize & 65535) == 65535 && counter < extraDataRecord.getSizeOfData()) {
                        System.arraycopy(byteBuff, counter, longByteBuff, 0, 8);
                        long val2 = Raw.readLongLittleEndian(longByteBuff, 0);
                        zip64ExtendedInfo.setCompressedSize(val2);
                        counter += 8;
                        valueAdded = true;
                    }
                    if ((offsetLocalHeader & 65535) == 65535 && counter < extraDataRecord.getSizeOfData()) {
                        System.arraycopy(byteBuff, counter, longByteBuff, 0, 8);
                        long val3 = Raw.readLongLittleEndian(longByteBuff, 0);
                        zip64ExtendedInfo.setOffsetLocalHeader(val3);
                        counter += 8;
                        valueAdded = true;
                    }
                    if ((diskNumberStart & 65535) == 65535 && counter < extraDataRecord.getSizeOfData()) {
                        System.arraycopy(byteBuff, counter, intByteBuff, 0, 4);
                        int val4 = Raw.readIntLittleEndian(intByteBuff, 0);
                        zip64ExtendedInfo.setDiskNumberStart(val4);
                        int i3 = counter + 8;
                        valueAdded = true;
                    }
                    if (valueAdded) {
                        return zip64ExtendedInfo;
                    }
                    return null;
                }
                return null;
            }
        }
        return null;
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [java.io.RandomAccessFile, long] */
    private void setFilePointerToReadZip64EndCentralDirLoc() throws ZipException {
        try {
            byte[] ebs = new byte[4];
            long pos = this.zip4jRaf.length() - 22;
            do {
                ?? r0 = this.zip4jRaf;
                pos--;
                r0.seek(r0);
            } while (Raw.readLeInt(this.zip4jRaf, ebs) != 101010256);
            this.zip4jRaf.seek(((((this.zip4jRaf.getFilePointer() - 4) - 4) - 8) - 4) - 4);
        } catch (IOException e2) {
            throw new ZipException(e2);
        }
    }

    public LocalFileHeader readLocalFileHeader(FileHeader fileHeader) throws ZipException {
        if (fileHeader == null || this.zip4jRaf == null) {
            throw new ZipException("invalid read parameters for local header");
        }
        long locHdrOffset = fileHeader.getOffsetLocalHeader();
        if (fileHeader.getZip64ExtendedInfo() != null) {
            Zip64ExtendedInfo zip64ExtendedInfo = fileHeader.getZip64ExtendedInfo();
            if (zip64ExtendedInfo.getOffsetLocalHeader() > 0) {
                locHdrOffset = fileHeader.getOffsetLocalHeader();
            }
        }
        if (locHdrOffset < 0) {
            throw new ZipException("invalid local header offset");
        }
        try {
            this.zip4jRaf.seek(locHdrOffset);
            LocalFileHeader localFileHeader = new LocalFileHeader();
            byte[] shortBuff = new byte[2];
            byte[] intBuff = new byte[4];
            byte[] bArr = new byte[8];
            readIntoBuff(this.zip4jRaf, intBuff);
            int sig = Raw.readIntLittleEndian(intBuff, 0);
            if (sig != 67324752) {
                throw new ZipException(new StringBuffer("invalid local header signature for file: ").append(fileHeader.getFileName()).toString());
            }
            localFileHeader.setSignature(sig);
            int length = 0 + 4;
            readIntoBuff(this.zip4jRaf, shortBuff);
            localFileHeader.setVersionNeededToExtract(Raw.readShortLittleEndian(shortBuff, 0));
            int length2 = length + 2;
            readIntoBuff(this.zip4jRaf, shortBuff);
            localFileHeader.setFileNameUTF8Encoded((Raw.readShortLittleEndian(shortBuff, 0) & 2048) != 0);
            byte b2 = shortBuff[0];
            int result = b2 & 1;
            if (result != 0) {
                localFileHeader.setEncrypted(true);
            }
            localFileHeader.setGeneralPurposeFlag(shortBuff);
            int length3 = length2 + 2;
            String binary = Integer.toBinaryString(b2);
            if (binary.length() >= 4) {
                localFileHeader.setDataDescriptorExists(binary.charAt(3) == '1');
            }
            readIntoBuff(this.zip4jRaf, shortBuff);
            localFileHeader.setCompressionMethod(Raw.readShortLittleEndian(shortBuff, 0));
            readIntoBuff(this.zip4jRaf, intBuff);
            localFileHeader.setLastModFileTime(Raw.readIntLittleEndian(intBuff, 0));
            readIntoBuff(this.zip4jRaf, intBuff);
            localFileHeader.setCrc32(Raw.readIntLittleEndian(intBuff, 0));
            localFileHeader.setCrcBuff((byte[]) intBuff.clone());
            readIntoBuff(this.zip4jRaf, intBuff);
            byte[] longBuff = getLongByteFromIntByte(intBuff);
            localFileHeader.setCompressedSize(Raw.readLongLittleEndian(longBuff, 0));
            readIntoBuff(this.zip4jRaf, intBuff);
            byte[] longBuff2 = getLongByteFromIntByte(intBuff);
            localFileHeader.setUncompressedSize(Raw.readLongLittleEndian(longBuff2, 0));
            readIntoBuff(this.zip4jRaf, shortBuff);
            int fileNameLength = Raw.readShortLittleEndian(shortBuff, 0);
            localFileHeader.setFileNameLength(fileNameLength);
            readIntoBuff(this.zip4jRaf, shortBuff);
            int extraFieldLength = Raw.readShortLittleEndian(shortBuff, 0);
            localFileHeader.setExtraFieldLength(extraFieldLength);
            int length4 = length3 + 2 + 4 + 4 + 4 + 4 + 2 + 2;
            if (fileNameLength > 0) {
                byte[] fileNameBuf = new byte[fileNameLength];
                readIntoBuff(this.zip4jRaf, fileNameBuf);
                String fileName = Zip4jUtil.decodeFileName(fileNameBuf, localFileHeader.isFileNameUTF8Encoded());
                if (fileName == null) {
                    throw new ZipException("file name is null, cannot assign file name to local file header");
                }
                if (fileName.indexOf(new StringBuffer(CallSiteDescriptor.TOKEN_DELIMITER).append(System.getProperty("file.separator")).toString()) >= 0) {
                    fileName = fileName.substring(fileName.indexOf(new StringBuffer(CallSiteDescriptor.TOKEN_DELIMITER).append(System.getProperty("file.separator")).toString()) + 2);
                }
                localFileHeader.setFileName(fileName);
                length4 += fileNameLength;
            } else {
                localFileHeader.setFileName(null);
            }
            readAndSaveExtraDataRecord(localFileHeader);
            localFileHeader.setOffsetStartOfData(locHdrOffset + length4 + extraFieldLength);
            localFileHeader.setPassword(fileHeader.getPassword());
            readAndSaveZip64ExtendedInfo(localFileHeader);
            readAndSaveAESExtraDataRecord(localFileHeader);
            if (localFileHeader.isEncrypted() && localFileHeader.getEncryptionMethod() != 99) {
                if ((b2 & 64) == 64) {
                    localFileHeader.setEncryptionMethod(1);
                } else {
                    localFileHeader.setEncryptionMethod(0);
                }
            }
            if (localFileHeader.getCrc32() <= 0) {
                localFileHeader.setCrc32(fileHeader.getCrc32());
                localFileHeader.setCrcBuff(fileHeader.getCrcBuff());
            }
            if (localFileHeader.getCompressedSize() <= 0) {
                localFileHeader.setCompressedSize(fileHeader.getCompressedSize());
            }
            if (localFileHeader.getUncompressedSize() <= 0) {
                localFileHeader.setUncompressedSize(fileHeader.getUncompressedSize());
            }
            return localFileHeader;
        } catch (IOException e2) {
            throw new ZipException(e2);
        }
    }

    private void readAndSaveAESExtraDataRecord(FileHeader fileHeader) throws ZipException {
        AESExtraDataRecord aesExtraDataRecord;
        if (fileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (fileHeader.getExtraDataRecords() != null && fileHeader.getExtraDataRecords().size() > 0 && (aesExtraDataRecord = readAESExtraDataRecord(fileHeader.getExtraDataRecords())) != null) {
            fileHeader.setAesExtraDataRecord(aesExtraDataRecord);
            fileHeader.setEncryptionMethod(99);
        }
    }

    private void readAndSaveAESExtraDataRecord(LocalFileHeader localFileHeader) throws ZipException {
        AESExtraDataRecord aesExtraDataRecord;
        if (localFileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (localFileHeader.getExtraDataRecords() != null && localFileHeader.getExtraDataRecords().size() > 0 && (aesExtraDataRecord = readAESExtraDataRecord(localFileHeader.getExtraDataRecords())) != null) {
            localFileHeader.setAesExtraDataRecord(aesExtraDataRecord);
            localFileHeader.setEncryptionMethod(99);
        }
    }

    private AESExtraDataRecord readAESExtraDataRecord(ArrayList extraDataRecords) throws ZipException {
        if (extraDataRecords == null) {
            return null;
        }
        for (int i2 = 0; i2 < extraDataRecords.size(); i2++) {
            ExtraDataRecord extraDataRecord = (ExtraDataRecord) extraDataRecords.get(i2);
            if (extraDataRecord != null && extraDataRecord.getHeader() == 39169) {
                if (extraDataRecord.getData() == null) {
                    throw new ZipException("corrput AES extra data records");
                }
                AESExtraDataRecord aesExtraDataRecord = new AESExtraDataRecord();
                aesExtraDataRecord.setSignature(39169L);
                aesExtraDataRecord.setDataSize(extraDataRecord.getSizeOfData());
                byte[] aesData = extraDataRecord.getData();
                aesExtraDataRecord.setVersionNumber(Raw.readShortLittleEndian(aesData, 0));
                byte[] vendorIDBytes = new byte[2];
                System.arraycopy(aesData, 2, vendorIDBytes, 0, 2);
                aesExtraDataRecord.setVendorID(new String(vendorIDBytes));
                aesExtraDataRecord.setAesStrength(aesData[4] & 255);
                aesExtraDataRecord.setCompressionMethod(Raw.readShortLittleEndian(aesData, 5));
                return aesExtraDataRecord;
            }
        }
        return null;
    }

    private byte[] readIntoBuff(RandomAccessFile zip4jRaf, byte[] buf) throws ZipException {
        try {
            if (zip4jRaf.read(buf, 0, buf.length) != -1) {
                return buf;
            }
            throw new ZipException("unexpected end of file when reading short buff");
        } catch (IOException e2) {
            throw new ZipException("IOException when reading short buff", e2);
        }
    }

    private byte[] getLongByteFromIntByte(byte[] intByte) throws ZipException {
        if (intByte == null) {
            throw new ZipException("input parameter is null, cannot expand to 8 bytes");
        }
        if (intByte.length != 4) {
            throw new ZipException("invalid byte length, cannot expand to 8 bytes");
        }
        byte[] longBuff = {intByte[0], intByte[1], intByte[2], intByte[3], 0, 0, 0, 0};
        return longBuff;
    }
}
