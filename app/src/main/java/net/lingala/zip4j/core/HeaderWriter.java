package net.lingala.zip4j.core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.SplitOutputStream;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip64EndCentralDirLocator;
import net.lingala.zip4j.model.Zip64EndCentralDirRecord;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/core/HeaderWriter.class */
public class HeaderWriter {
    private final int ZIP64_EXTRA_BUF = 50;

    public int writeLocalFileHeader(ZipModel zipModel, LocalFileHeader localFileHeader, OutputStream outputStream) throws ZipException {
        int headerLength;
        if (localFileHeader == null) {
            throw new ZipException("input parameters are null, cannot write local file header");
        }
        try {
            ArrayList byteArrayList = new ArrayList();
            byte[] shortByte = new byte[2];
            byte[] intByte = new byte[4];
            byte[] longByte = new byte[8];
            byte[] emptyLongByte = new byte[8];
            Raw.writeIntLittleEndian(intByte, 0, localFileHeader.getSignature());
            copyByteArrayToArrayList(intByte, byteArrayList);
            int headerLength2 = 0 + 4;
            Raw.writeShortLittleEndian(shortByte, 0, (short) localFileHeader.getVersionNeededToExtract());
            copyByteArrayToArrayList(shortByte, byteArrayList);
            copyByteArrayToArrayList(localFileHeader.getGeneralPurposeFlag(), byteArrayList);
            Raw.writeShortLittleEndian(shortByte, 0, (short) localFileHeader.getCompressionMethod());
            copyByteArrayToArrayList(shortByte, byteArrayList);
            int dateTime = localFileHeader.getLastModFileTime();
            Raw.writeIntLittleEndian(intByte, 0, dateTime);
            copyByteArrayToArrayList(intByte, byteArrayList);
            Raw.writeIntLittleEndian(intByte, 0, (int) localFileHeader.getCrc32());
            copyByteArrayToArrayList(intByte, byteArrayList);
            int headerLength3 = headerLength2 + 2 + 2 + 2 + 4 + 4;
            boolean writingZip64Rec = false;
            long uncompressedSize = localFileHeader.getUncompressedSize();
            if (uncompressedSize + 50 >= 4294967295L) {
                Raw.writeLongLittleEndian(longByte, 0, 4294967295L);
                System.arraycopy(longByte, 0, intByte, 0, 4);
                copyByteArrayToArrayList(intByte, byteArrayList);
                copyByteArrayToArrayList(intByte, byteArrayList);
                zipModel.setZip64Format(true);
                writingZip64Rec = true;
                localFileHeader.setWriteComprSizeInZip64ExtraRecord(true);
            } else {
                Raw.writeLongLittleEndian(longByte, 0, localFileHeader.getCompressedSize());
                System.arraycopy(longByte, 0, intByte, 0, 4);
                copyByteArrayToArrayList(intByte, byteArrayList);
                Raw.writeLongLittleEndian(longByte, 0, localFileHeader.getUncompressedSize());
                System.arraycopy(longByte, 0, intByte, 0, 4);
                copyByteArrayToArrayList(intByte, byteArrayList);
                localFileHeader.setWriteComprSizeInZip64ExtraRecord(false);
            }
            Raw.writeShortLittleEndian(shortByte, 0, (short) localFileHeader.getFileNameLength());
            copyByteArrayToArrayList(shortByte, byteArrayList);
            int headerLength4 = headerLength3 + 8 + 2;
            int extraFieldLength = 0;
            if (writingZip64Rec) {
                extraFieldLength = 0 + 20;
            }
            if (localFileHeader.getAesExtraDataRecord() != null) {
                extraFieldLength += 11;
            }
            Raw.writeShortLittleEndian(shortByte, 0, (short) extraFieldLength);
            copyByteArrayToArrayList(shortByte, byteArrayList);
            int headerLength5 = headerLength4 + 2;
            if (Zip4jUtil.isStringNotNullAndNotEmpty(zipModel.getFileNameCharset())) {
                byte[] fileNameBytes = localFileHeader.getFileName().getBytes(zipModel.getFileNameCharset());
                copyByteArrayToArrayList(fileNameBytes, byteArrayList);
                headerLength = headerLength5 + fileNameBytes.length;
            } else {
                copyByteArrayToArrayList(Zip4jUtil.convertCharset(localFileHeader.getFileName()), byteArrayList);
                headerLength = headerLength5 + Zip4jUtil.getEncodedStringLength(localFileHeader.getFileName());
            }
            if (writingZip64Rec) {
                Raw.writeShortLittleEndian(shortByte, 0, (short) 1);
                copyByteArrayToArrayList(shortByte, byteArrayList);
                Raw.writeShortLittleEndian(shortByte, 0, (short) 16);
                copyByteArrayToArrayList(shortByte, byteArrayList);
                Raw.writeLongLittleEndian(longByte, 0, localFileHeader.getUncompressedSize());
                copyByteArrayToArrayList(longByte, byteArrayList);
                copyByteArrayToArrayList(emptyLongByte, byteArrayList);
                int i2 = headerLength + 2 + 2 + 8 + 8;
            }
            if (localFileHeader.getAesExtraDataRecord() != null) {
                AESExtraDataRecord aesExtraDataRecord = localFileHeader.getAesExtraDataRecord();
                Raw.writeShortLittleEndian(shortByte, 0, (short) aesExtraDataRecord.getSignature());
                copyByteArrayToArrayList(shortByte, byteArrayList);
                Raw.writeShortLittleEndian(shortByte, 0, (short) aesExtraDataRecord.getDataSize());
                copyByteArrayToArrayList(shortByte, byteArrayList);
                Raw.writeShortLittleEndian(shortByte, 0, (short) aesExtraDataRecord.getVersionNumber());
                copyByteArrayToArrayList(shortByte, byteArrayList);
                copyByteArrayToArrayList(aesExtraDataRecord.getVendorID().getBytes(), byteArrayList);
                byte[] aesStrengthBytes = {(byte) aesExtraDataRecord.getAesStrength()};
                copyByteArrayToArrayList(aesStrengthBytes, byteArrayList);
                Raw.writeShortLittleEndian(shortByte, 0, (short) aesExtraDataRecord.getCompressionMethod());
                copyByteArrayToArrayList(shortByte, byteArrayList);
            }
            byte[] lhBytes = byteArrayListToByteArray(byteArrayList);
            outputStream.write(lhBytes);
            return lhBytes.length;
        } catch (ZipException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    public int writeExtendedLocalHeader(LocalFileHeader localFileHeader, OutputStream outputStream) throws ZipException, IOException {
        if (localFileHeader == null || outputStream == null) {
            throw new ZipException("input parameters is null, cannot write extended local header");
        }
        ArrayList byteArrayList = new ArrayList();
        byte[] intByte = new byte[4];
        Raw.writeIntLittleEndian(intByte, 0, 134695760);
        copyByteArrayToArrayList(intByte, byteArrayList);
        Raw.writeIntLittleEndian(intByte, 0, (int) localFileHeader.getCrc32());
        copyByteArrayToArrayList(intByte, byteArrayList);
        long compressedSize = localFileHeader.getCompressedSize();
        if (compressedSize >= 2147483647L) {
            compressedSize = 2147483647L;
        }
        Raw.writeIntLittleEndian(intByte, 0, (int) compressedSize);
        copyByteArrayToArrayList(intByte, byteArrayList);
        long uncompressedSize = localFileHeader.getUncompressedSize();
        if (uncompressedSize >= 2147483647L) {
            uncompressedSize = 2147483647L;
        }
        Raw.writeIntLittleEndian(intByte, 0, (int) uncompressedSize);
        copyByteArrayToArrayList(intByte, byteArrayList);
        byte[] extLocHdrBytes = byteArrayListToByteArray(byteArrayList);
        outputStream.write(extLocHdrBytes);
        return extLocHdrBytes.length;
    }

    public void finalizeZipFile(ZipModel zipModel, OutputStream outputStream) throws ZipException {
        if (zipModel == null || outputStream == null) {
            throw new ZipException("input parameters is null, cannot finalize zip file");
        }
        try {
            processHeaderData(zipModel, outputStream);
            long offsetCentralDir = zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir();
            List headerBytesList = new ArrayList();
            int sizeOfCentralDir = writeCentralDirectory(zipModel, outputStream, headerBytesList);
            if (zipModel.isZip64Format()) {
                if (zipModel.getZip64EndCentralDirRecord() == null) {
                    zipModel.setZip64EndCentralDirRecord(new Zip64EndCentralDirRecord());
                }
                if (zipModel.getZip64EndCentralDirLocator() == null) {
                    zipModel.setZip64EndCentralDirLocator(new Zip64EndCentralDirLocator());
                }
                zipModel.getZip64EndCentralDirLocator().setOffsetZip64EndOfCentralDirRec(offsetCentralDir + sizeOfCentralDir);
                if (outputStream instanceof SplitOutputStream) {
                    zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(((SplitOutputStream) outputStream).getCurrSplitFileCounter());
                    zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(((SplitOutputStream) outputStream).getCurrSplitFileCounter() + 1);
                } else {
                    zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(0);
                    zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(1);
                }
                writeZip64EndOfCentralDirectoryRecord(zipModel, outputStream, sizeOfCentralDir, offsetCentralDir, headerBytesList);
                writeZip64EndOfCentralDirectoryLocator(zipModel, outputStream, headerBytesList);
            }
            writeEndOfCentralDirectoryRecord(zipModel, outputStream, sizeOfCentralDir, offsetCentralDir, headerBytesList);
            writeZipHeaderBytes(zipModel, outputStream, byteArrayListToByteArray(headerBytesList));
        } catch (ZipException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    public void finalizeZipFileWithoutValidations(ZipModel zipModel, OutputStream outputStream) throws ZipException {
        if (zipModel == null || outputStream == null) {
            throw new ZipException("input parameters is null, cannot finalize zip file without validations");
        }
        try {
            List headerBytesList = new ArrayList();
            long offsetCentralDir = zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir();
            int sizeOfCentralDir = writeCentralDirectory(zipModel, outputStream, headerBytesList);
            if (zipModel.isZip64Format()) {
                if (zipModel.getZip64EndCentralDirRecord() == null) {
                    zipModel.setZip64EndCentralDirRecord(new Zip64EndCentralDirRecord());
                }
                if (zipModel.getZip64EndCentralDirLocator() == null) {
                    zipModel.setZip64EndCentralDirLocator(new Zip64EndCentralDirLocator());
                }
                zipModel.getZip64EndCentralDirLocator().setOffsetZip64EndOfCentralDirRec(offsetCentralDir + sizeOfCentralDir);
                writeZip64EndOfCentralDirectoryRecord(zipModel, outputStream, sizeOfCentralDir, offsetCentralDir, headerBytesList);
                writeZip64EndOfCentralDirectoryLocator(zipModel, outputStream, headerBytesList);
            }
            writeEndOfCentralDirectoryRecord(zipModel, outputStream, sizeOfCentralDir, offsetCentralDir, headerBytesList);
            writeZipHeaderBytes(zipModel, outputStream, byteArrayListToByteArray(headerBytesList));
        } catch (ZipException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    private void writeZipHeaderBytes(ZipModel zipModel, OutputStream outputStream, byte[] buff) throws ZipException {
        if (buff == null) {
            throw new ZipException("invalid buff to write as zip headers");
        }
        try {
            if ((outputStream instanceof SplitOutputStream) && ((SplitOutputStream) outputStream).checkBuffSizeAndStartNextSplitFile(buff.length)) {
                finalizeZipFile(zipModel, outputStream);
            } else {
                outputStream.write(buff);
            }
        } catch (IOException e2) {
            throw new ZipException(e2);
        }
    }

    private void processHeaderData(ZipModel zipModel, OutputStream outputStream) throws ZipException {
        try {
            int currSplitFileCounter = 0;
            if (outputStream instanceof SplitOutputStream) {
                zipModel.getEndCentralDirRecord().setOffsetOfStartOfCentralDir(((SplitOutputStream) outputStream).getFilePointer());
                currSplitFileCounter = ((SplitOutputStream) outputStream).getCurrSplitFileCounter();
            }
            if (zipModel.isZip64Format()) {
                if (zipModel.getZip64EndCentralDirRecord() == null) {
                    zipModel.setZip64EndCentralDirRecord(new Zip64EndCentralDirRecord());
                }
                if (zipModel.getZip64EndCentralDirLocator() == null) {
                    zipModel.setZip64EndCentralDirLocator(new Zip64EndCentralDirLocator());
                }
                zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(currSplitFileCounter);
                zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(currSplitFileCounter + 1);
            }
            zipModel.getEndCentralDirRecord().setNoOfThisDisk(currSplitFileCounter);
            zipModel.getEndCentralDirRecord().setNoOfThisDiskStartOfCentralDir(currSplitFileCounter);
        } catch (IOException e2) {
            throw new ZipException(e2);
        }
    }

    private int writeCentralDirectory(ZipModel zipModel, OutputStream outputStream, List headerBytesList) throws ZipException {
        if (zipModel == null || outputStream == null) {
            throw new ZipException("input parameters is null, cannot write central directory");
        }
        if (zipModel.getCentralDirectory() == null || zipModel.getCentralDirectory().getFileHeaders() == null || zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
            return 0;
        }
        int sizeOfCentralDir = 0;
        for (int i2 = 0; i2 < zipModel.getCentralDirectory().getFileHeaders().size(); i2++) {
            FileHeader fileHeader = (FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i2);
            int sizeOfFileHeader = writeFileHeader(zipModel, fileHeader, outputStream, headerBytesList);
            sizeOfCentralDir += sizeOfFileHeader;
        }
        return sizeOfCentralDir;
    }

    private int writeFileHeader(ZipModel zipModel, FileHeader fileHeader, OutputStream outputStream, List headerBytesList) throws ZipException {
        int sizeOfFileHeader;
        int sizeOfFileHeader2;
        if (fileHeader == null || outputStream == null) {
            throw new ZipException("input parameters is null, cannot write local file header");
        }
        try {
            byte[] shortByte = new byte[2];
            byte[] intByte = new byte[4];
            byte[] longByte = new byte[8];
            byte[] emptyShortByte = new byte[2];
            byte[] emptyIntByte = new byte[4];
            boolean writeZip64FileSize = false;
            boolean writeZip64OffsetLocalHeader = false;
            Raw.writeIntLittleEndian(intByte, 0, fileHeader.getSignature());
            copyByteArrayToArrayList(intByte, headerBytesList);
            Raw.writeShortLittleEndian(shortByte, 0, (short) fileHeader.getVersionMadeBy());
            copyByteArrayToArrayList(shortByte, headerBytesList);
            Raw.writeShortLittleEndian(shortByte, 0, (short) fileHeader.getVersionNeededToExtract());
            copyByteArrayToArrayList(shortByte, headerBytesList);
            copyByteArrayToArrayList(fileHeader.getGeneralPurposeFlag(), headerBytesList);
            Raw.writeShortLittleEndian(shortByte, 0, (short) fileHeader.getCompressionMethod());
            copyByteArrayToArrayList(shortByte, headerBytesList);
            int dateTime = fileHeader.getLastModFileTime();
            Raw.writeIntLittleEndian(intByte, 0, dateTime);
            copyByteArrayToArrayList(intByte, headerBytesList);
            Raw.writeIntLittleEndian(intByte, 0, (int) fileHeader.getCrc32());
            copyByteArrayToArrayList(intByte, headerBytesList);
            int sizeOfFileHeader3 = 0 + 4 + 2 + 2 + 2 + 2 + 4 + 4;
            if (fileHeader.getCompressedSize() >= 4294967295L || fileHeader.getUncompressedSize() + 50 >= 4294967295L) {
                Raw.writeLongLittleEndian(longByte, 0, 4294967295L);
                System.arraycopy(longByte, 0, intByte, 0, 4);
                copyByteArrayToArrayList(intByte, headerBytesList);
                copyByteArrayToArrayList(intByte, headerBytesList);
                sizeOfFileHeader = sizeOfFileHeader3 + 4 + 4;
                writeZip64FileSize = true;
            } else {
                Raw.writeLongLittleEndian(longByte, 0, fileHeader.getCompressedSize());
                System.arraycopy(longByte, 0, intByte, 0, 4);
                copyByteArrayToArrayList(intByte, headerBytesList);
                Raw.writeLongLittleEndian(longByte, 0, fileHeader.getUncompressedSize());
                System.arraycopy(longByte, 0, intByte, 0, 4);
                copyByteArrayToArrayList(intByte, headerBytesList);
                sizeOfFileHeader = sizeOfFileHeader3 + 4 + 4;
            }
            Raw.writeShortLittleEndian(shortByte, 0, (short) fileHeader.getFileNameLength());
            copyByteArrayToArrayList(shortByte, headerBytesList);
            int sizeOfFileHeader4 = sizeOfFileHeader + 2;
            byte[] offsetLocalHeaderBytes = new byte[4];
            if (fileHeader.getOffsetLocalHeader() > 4294967295L) {
                Raw.writeLongLittleEndian(longByte, 0, 4294967295L);
                System.arraycopy(longByte, 0, offsetLocalHeaderBytes, 0, 4);
                writeZip64OffsetLocalHeader = true;
            } else {
                Raw.writeLongLittleEndian(longByte, 0, fileHeader.getOffsetLocalHeader());
                System.arraycopy(longByte, 0, offsetLocalHeaderBytes, 0, 4);
            }
            int extraFieldLength = 0;
            if (writeZip64FileSize || writeZip64OffsetLocalHeader) {
                extraFieldLength = 0 + 4;
                if (writeZip64FileSize) {
                    extraFieldLength += 16;
                }
                if (writeZip64OffsetLocalHeader) {
                    extraFieldLength += 8;
                }
            }
            if (fileHeader.getAesExtraDataRecord() != null) {
                extraFieldLength += 11;
            }
            Raw.writeShortLittleEndian(shortByte, 0, (short) extraFieldLength);
            copyByteArrayToArrayList(shortByte, headerBytesList);
            copyByteArrayToArrayList(emptyShortByte, headerBytesList);
            Raw.writeShortLittleEndian(shortByte, 0, (short) fileHeader.getDiskNumberStart());
            copyByteArrayToArrayList(shortByte, headerBytesList);
            copyByteArrayToArrayList(emptyShortByte, headerBytesList);
            int sizeOfFileHeader5 = sizeOfFileHeader4 + 2 + 2 + 2 + 2;
            if (fileHeader.getExternalFileAttr() != null) {
                copyByteArrayToArrayList(fileHeader.getExternalFileAttr(), headerBytesList);
            } else {
                copyByteArrayToArrayList(emptyIntByte, headerBytesList);
            }
            copyByteArrayToArrayList(offsetLocalHeaderBytes, headerBytesList);
            int sizeOfFileHeader6 = sizeOfFileHeader5 + 4 + 4;
            if (Zip4jUtil.isStringNotNullAndNotEmpty(zipModel.getFileNameCharset())) {
                byte[] fileNameBytes = fileHeader.getFileName().getBytes(zipModel.getFileNameCharset());
                copyByteArrayToArrayList(fileNameBytes, headerBytesList);
                sizeOfFileHeader2 = sizeOfFileHeader6 + fileNameBytes.length;
            } else {
                copyByteArrayToArrayList(Zip4jUtil.convertCharset(fileHeader.getFileName()), headerBytesList);
                sizeOfFileHeader2 = sizeOfFileHeader6 + Zip4jUtil.getEncodedStringLength(fileHeader.getFileName());
            }
            if (writeZip64FileSize || writeZip64OffsetLocalHeader) {
                zipModel.setZip64Format(true);
                Raw.writeShortLittleEndian(shortByte, 0, (short) 1);
                copyByteArrayToArrayList(shortByte, headerBytesList);
                int sizeOfFileHeader7 = sizeOfFileHeader2 + 2;
                int dataSize = 0;
                if (writeZip64FileSize) {
                    dataSize = 0 + 16;
                }
                if (writeZip64OffsetLocalHeader) {
                    dataSize += 8;
                }
                Raw.writeShortLittleEndian(shortByte, 0, (short) dataSize);
                copyByteArrayToArrayList(shortByte, headerBytesList);
                sizeOfFileHeader2 = sizeOfFileHeader7 + 2;
                if (writeZip64FileSize) {
                    Raw.writeLongLittleEndian(longByte, 0, fileHeader.getUncompressedSize());
                    copyByteArrayToArrayList(longByte, headerBytesList);
                    Raw.writeLongLittleEndian(longByte, 0, fileHeader.getCompressedSize());
                    copyByteArrayToArrayList(longByte, headerBytesList);
                    sizeOfFileHeader2 = sizeOfFileHeader2 + 8 + 8;
                }
                if (writeZip64OffsetLocalHeader) {
                    Raw.writeLongLittleEndian(longByte, 0, fileHeader.getOffsetLocalHeader());
                    copyByteArrayToArrayList(longByte, headerBytesList);
                    sizeOfFileHeader2 += 8;
                }
            }
            if (fileHeader.getAesExtraDataRecord() != null) {
                AESExtraDataRecord aesExtraDataRecord = fileHeader.getAesExtraDataRecord();
                Raw.writeShortLittleEndian(shortByte, 0, (short) aesExtraDataRecord.getSignature());
                copyByteArrayToArrayList(shortByte, headerBytesList);
                Raw.writeShortLittleEndian(shortByte, 0, (short) aesExtraDataRecord.getDataSize());
                copyByteArrayToArrayList(shortByte, headerBytesList);
                Raw.writeShortLittleEndian(shortByte, 0, (short) aesExtraDataRecord.getVersionNumber());
                copyByteArrayToArrayList(shortByte, headerBytesList);
                copyByteArrayToArrayList(aesExtraDataRecord.getVendorID().getBytes(), headerBytesList);
                byte[] aesStrengthBytes = {(byte) aesExtraDataRecord.getAesStrength()};
                copyByteArrayToArrayList(aesStrengthBytes, headerBytesList);
                Raw.writeShortLittleEndian(shortByte, 0, (short) aesExtraDataRecord.getCompressionMethod());
                copyByteArrayToArrayList(shortByte, headerBytesList);
                sizeOfFileHeader2 += 11;
            }
            return sizeOfFileHeader2;
        } catch (Exception e2) {
            throw new ZipException(e2);
        }
    }

    private void writeZip64EndOfCentralDirectoryRecord(ZipModel zipModel, OutputStream outputStream, int sizeOfCentralDir, long offsetCentralDir, List headerBytesList) throws ZipException {
        if (zipModel == null || outputStream == null) {
            throw new ZipException("zip model or output stream is null, cannot write zip64 end of central directory record");
        }
        try {
            byte[] shortByte = new byte[2];
            byte[] emptyShortByte = new byte[2];
            byte[] intByte = new byte[4];
            byte[] longByte = new byte[8];
            Raw.writeIntLittleEndian(intByte, 0, 101075792);
            copyByteArrayToArrayList(intByte, headerBytesList);
            Raw.writeLongLittleEndian(longByte, 0, 44L);
            copyByteArrayToArrayList(longByte, headerBytesList);
            if (zipModel.getCentralDirectory() != null && zipModel.getCentralDirectory().getFileHeaders() != null && zipModel.getCentralDirectory().getFileHeaders().size() > 0) {
                Raw.writeShortLittleEndian(shortByte, 0, (short) ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(0)).getVersionMadeBy());
                copyByteArrayToArrayList(shortByte, headerBytesList);
                Raw.writeShortLittleEndian(shortByte, 0, (short) ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(0)).getVersionNeededToExtract());
                copyByteArrayToArrayList(shortByte, headerBytesList);
            } else {
                copyByteArrayToArrayList(emptyShortByte, headerBytesList);
                copyByteArrayToArrayList(emptyShortByte, headerBytesList);
            }
            Raw.writeIntLittleEndian(intByte, 0, zipModel.getEndCentralDirRecord().getNoOfThisDisk());
            copyByteArrayToArrayList(intByte, headerBytesList);
            Raw.writeIntLittleEndian(intByte, 0, zipModel.getEndCentralDirRecord().getNoOfThisDiskStartOfCentralDir());
            copyByteArrayToArrayList(intByte, headerBytesList);
            int numEntriesOnThisDisk = 0;
            if (zipModel.getCentralDirectory() == null || zipModel.getCentralDirectory().getFileHeaders() == null) {
                throw new ZipException("invalid central directory/file headers, cannot write end of central directory record");
            }
            int numEntries = zipModel.getCentralDirectory().getFileHeaders().size();
            if (zipModel.isSplitArchive()) {
                countNumberOfFileHeaderEntriesOnDisk(zipModel.getCentralDirectory().getFileHeaders(), zipModel.getEndCentralDirRecord().getNoOfThisDisk());
            } else {
                numEntriesOnThisDisk = numEntries;
            }
            Raw.writeLongLittleEndian(longByte, 0, numEntriesOnThisDisk);
            copyByteArrayToArrayList(longByte, headerBytesList);
            Raw.writeLongLittleEndian(longByte, 0, numEntries);
            copyByteArrayToArrayList(longByte, headerBytesList);
            Raw.writeLongLittleEndian(longByte, 0, sizeOfCentralDir);
            copyByteArrayToArrayList(longByte, headerBytesList);
            Raw.writeLongLittleEndian(longByte, 0, offsetCentralDir);
            copyByteArrayToArrayList(longByte, headerBytesList);
        } catch (ZipException zipException) {
            throw zipException;
        } catch (Exception e2) {
            throw new ZipException(e2);
        }
    }

    private void writeZip64EndOfCentralDirectoryLocator(ZipModel zipModel, OutputStream outputStream, List headerBytesList) throws ZipException {
        if (zipModel == null || outputStream == null) {
            throw new ZipException("zip model or output stream is null, cannot write zip64 end of central directory locator");
        }
        try {
            byte[] intByte = new byte[4];
            byte[] longByte = new byte[8];
            Raw.writeIntLittleEndian(intByte, 0, 117853008);
            copyByteArrayToArrayList(intByte, headerBytesList);
            Raw.writeIntLittleEndian(intByte, 0, zipModel.getZip64EndCentralDirLocator().getNoOfDiskStartOfZip64EndOfCentralDirRec());
            copyByteArrayToArrayList(intByte, headerBytesList);
            Raw.writeLongLittleEndian(longByte, 0, zipModel.getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec());
            copyByteArrayToArrayList(longByte, headerBytesList);
            Raw.writeIntLittleEndian(intByte, 0, zipModel.getZip64EndCentralDirLocator().getTotNumberOfDiscs());
            copyByteArrayToArrayList(intByte, headerBytesList);
        } catch (ZipException zipException) {
            throw zipException;
        } catch (Exception e2) {
            throw new ZipException(e2);
        }
    }

    private void writeEndOfCentralDirectoryRecord(ZipModel zipModel, OutputStream outputStream, int sizeOfCentralDir, long offsetCentralDir, List headrBytesList) throws ZipException {
        int numEntriesOnThisDisk;
        if (zipModel == null || outputStream == null) {
            throw new ZipException("zip model or output stream is null, cannot write end of central directory record");
        }
        try {
            byte[] shortByte = new byte[2];
            byte[] intByte = new byte[4];
            byte[] longByte = new byte[8];
            Raw.writeIntLittleEndian(intByte, 0, (int) zipModel.getEndCentralDirRecord().getSignature());
            copyByteArrayToArrayList(intByte, headrBytesList);
            Raw.writeShortLittleEndian(shortByte, 0, (short) zipModel.getEndCentralDirRecord().getNoOfThisDisk());
            copyByteArrayToArrayList(shortByte, headrBytesList);
            Raw.writeShortLittleEndian(shortByte, 0, (short) zipModel.getEndCentralDirRecord().getNoOfThisDiskStartOfCentralDir());
            copyByteArrayToArrayList(shortByte, headrBytesList);
            if (zipModel.getCentralDirectory() == null || zipModel.getCentralDirectory().getFileHeaders() == null) {
                throw new ZipException("invalid central directory/file headers, cannot write end of central directory record");
            }
            int numEntries = zipModel.getCentralDirectory().getFileHeaders().size();
            if (zipModel.isSplitArchive()) {
                numEntriesOnThisDisk = countNumberOfFileHeaderEntriesOnDisk(zipModel.getCentralDirectory().getFileHeaders(), zipModel.getEndCentralDirRecord().getNoOfThisDisk());
            } else {
                numEntriesOnThisDisk = numEntries;
            }
            Raw.writeShortLittleEndian(shortByte, 0, (short) numEntriesOnThisDisk);
            copyByteArrayToArrayList(shortByte, headrBytesList);
            Raw.writeShortLittleEndian(shortByte, 0, (short) numEntries);
            copyByteArrayToArrayList(shortByte, headrBytesList);
            Raw.writeIntLittleEndian(intByte, 0, sizeOfCentralDir);
            copyByteArrayToArrayList(intByte, headrBytesList);
            if (offsetCentralDir > 4294967295L) {
                Raw.writeLongLittleEndian(longByte, 0, 4294967295L);
                System.arraycopy(longByte, 0, intByte, 0, 4);
                copyByteArrayToArrayList(intByte, headrBytesList);
            } else {
                Raw.writeLongLittleEndian(longByte, 0, offsetCentralDir);
                System.arraycopy(longByte, 0, intByte, 0, 4);
                copyByteArrayToArrayList(intByte, headrBytesList);
            }
            int commentLength = 0;
            if (zipModel.getEndCentralDirRecord().getComment() != null) {
                commentLength = zipModel.getEndCentralDirRecord().getCommentLength();
            }
            Raw.writeShortLittleEndian(shortByte, 0, (short) commentLength);
            copyByteArrayToArrayList(shortByte, headrBytesList);
            if (commentLength > 0) {
                copyByteArrayToArrayList(zipModel.getEndCentralDirRecord().getCommentBytes(), headrBytesList);
            }
        } catch (Exception e2) {
            throw new ZipException(e2);
        }
    }

    public void updateLocalFileHeader(LocalFileHeader localFileHeader, long offset, int toUpdate, ZipModel zipModel, byte[] bytesToWrite, int noOfDisk, SplitOutputStream outputStream) throws ZipException {
        SplitOutputStream currOutputStream;
        String fileName;
        if (localFileHeader == null || offset < 0 || zipModel == null) {
            throw new ZipException("invalid input parameters, cannot update local file header");
        }
        try {
            boolean closeFlag = false;
            if (noOfDisk != outputStream.getCurrSplitFileCounter()) {
                File zipFile = new File(zipModel.getZipFile());
                String parentFile = zipFile.getParent();
                String fileNameWithoutExt = Zip4jUtil.getZipFileNameWithoutExt(zipFile.getName());
                String fileName2 = new StringBuffer(String.valueOf(parentFile)).append(System.getProperty("file.separator")).toString();
                if (noOfDisk < 9) {
                    fileName = new StringBuffer(String.valueOf(fileName2)).append(fileNameWithoutExt).append(".z0").append(noOfDisk + 1).toString();
                } else {
                    fileName = new StringBuffer(String.valueOf(fileName2)).append(fileNameWithoutExt).append(".z").append(noOfDisk + 1).toString();
                }
                currOutputStream = new SplitOutputStream(new File(fileName));
                closeFlag = true;
            } else {
                currOutputStream = outputStream;
            }
            long currOffset = currOutputStream.getFilePointer();
            if (currOutputStream == null) {
                throw new ZipException("invalid output stream handler, cannot update local file header");
            }
            switch (toUpdate) {
                case 14:
                    currOutputStream.seek(offset + toUpdate);
                    currOutputStream.write(bytesToWrite);
                    break;
                case 18:
                case 22:
                    updateCompressedSizeInLocalFileHeader(currOutputStream, localFileHeader, offset, toUpdate, bytesToWrite, zipModel.isZip64Format());
                    break;
            }
            if (closeFlag) {
                currOutputStream.close();
            } else {
                outputStream.seek(currOffset);
            }
        } catch (Exception e2) {
            throw new ZipException(e2);
        }
    }

    private void updateCompressedSizeInLocalFileHeader(SplitOutputStream outputStream, LocalFileHeader localFileHeader, long offset, long toUpdate, byte[] bytesToWrite, boolean isZip64Format) throws ZipException {
        if (outputStream == null) {
            throw new ZipException("invalid output stream, cannot update compressed size for local file header");
        }
        try {
            if (localFileHeader.isWriteComprSizeInZip64ExtraRecord()) {
                if (bytesToWrite.length != 8) {
                    throw new ZipException("attempting to write a non 8-byte compressed size block for a zip64 file");
                }
                long zip64CompressedSizeOffset = offset + toUpdate + 4 + 4 + 2 + 2 + localFileHeader.getFileNameLength() + 2 + 2 + 8;
                if (toUpdate == 22) {
                    zip64CompressedSizeOffset += 8;
                }
                outputStream.seek(zip64CompressedSizeOffset);
                outputStream.write(bytesToWrite);
                return;
            }
            outputStream.seek(offset + toUpdate);
            outputStream.write(bytesToWrite);
        } catch (IOException e2) {
            throw new ZipException(e2);
        }
    }

    private void copyByteArrayToArrayList(byte[] byteArray, List arrayList) throws ZipException {
        if (arrayList == null || byteArray == null) {
            throw new ZipException("one of the input parameters is null, cannot copy byte array to array list");
        }
        for (byte b2 : byteArray) {
            arrayList.add(Byte.toString(b2));
        }
    }

    private byte[] byteArrayListToByteArray(List arrayList) throws ZipException {
        if (arrayList == null) {
            throw new ZipException("input byte array list is null, cannot conver to byte array");
        }
        if (arrayList.size() <= 0) {
            return null;
        }
        byte[] retBytes = new byte[arrayList.size()];
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            retBytes[i2] = Byte.parseByte((String) arrayList.get(i2));
        }
        return retBytes;
    }

    private int countNumberOfFileHeaderEntriesOnDisk(ArrayList fileHeaders, int numOfDisk) throws ZipException {
        if (fileHeaders == null) {
            throw new ZipException("file headers are null, cannot calculate number of entries on this disk");
        }
        int noEntries = 0;
        for (int i2 = 0; i2 < fileHeaders.size(); i2++) {
            FileHeader fileHeader = (FileHeader) fileHeaders.get(i2);
            if (fileHeader.getDiskNumberStart() == numOfDisk) {
                noEntries++;
            }
        }
        return noEntries;
    }
}
