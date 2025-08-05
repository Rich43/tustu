package org.icepdf.core.pobjects;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.Parser;
import org.icepdf.core.util.PdfOps;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/CrossReference.class */
public class CrossReference {
    private static final Logger logger = Logger.getLogger(CrossReference.class.toString());
    public static final Name SIZE_KEY = new Name("Size");
    public static final Name INDEX_KEY = new Name("Index");
    public static final Name W_KEY = new Name(PdfOps.W_TOKEN);
    private HashMap<Number, Entry> hObjectNumber2Entry = new HashMap<>(4096);
    private PTrailer pTrailer;
    private CrossReference xrefPrevious;
    private CrossReference xrefPeer;
    private boolean bIsCrossReferenceTable;
    private boolean bHaveTriedLoadingPrevious;
    private boolean bHaveTriedLoadingPeer;
    protected int offset;

    public void setTrailer(PTrailer trailer) {
        this.pTrailer = trailer;
    }

    public void addXRefTableEntries(Parser parser) {
        this.bIsCrossReferenceTable = true;
        while (true) {
            try {
                Object startingObjectNumberOrTrailer = parser.getNumberOrStringWithMark(16);
                if (!(startingObjectNumberOrTrailer instanceof Number)) {
                    parser.ungetNumberOrStringWithReset();
                    return;
                }
                int startingObjectNumber = ((Number) startingObjectNumberOrTrailer).intValue();
                int numEntries = ((Number) parser.getToken()).intValue();
                int currNumber = startingObjectNumber;
                for (int i2 = 0; i2 < numEntries; i2++) {
                    long filePosition = parser.getIntSurroundedByWhitespace();
                    int generationNum = parser.getIntSurroundedByWhitespace();
                    char usedOrFree = parser.getCharSurroundedByWhitespace();
                    if (usedOrFree == 'n') {
                        addUsedEntry(currNumber, filePosition, generationNum);
                    } else if (usedOrFree == 'f' && startingObjectNumber > 0 && filePosition == 0 && generationNum == 65535) {
                        currNumber--;
                    }
                    currNumber++;
                }
            } catch (IOException e2) {
                logger.log(Level.SEVERE, "Error parsing xRef table entries.", (Throwable) e2);
                return;
            }
        }
    }

    public void addXRefStreamEntries(Library library, HashMap xrefStreamHash, InputStream streamInput) {
        try {
            int size = library.getInt(xrefStreamHash, SIZE_KEY);
            List<Number> objNumAndEntriesCountPairs = (List) library.getObject(xrefStreamHash, INDEX_KEY);
            if (objNumAndEntriesCountPairs == null) {
                objNumAndEntriesCountPairs = new ArrayList<>(2);
                objNumAndEntriesCountPairs.add(0);
                objNumAndEntriesCountPairs.add(Integer.valueOf(size));
            }
            List fieldSizesVec = (List) library.getObject(xrefStreamHash, W_KEY);
            int[] fieldSizes = null;
            if (fieldSizesVec != null) {
                fieldSizes = new int[fieldSizesVec.size()];
                for (int i2 = 0; i2 < fieldSizesVec.size(); i2++) {
                    fieldSizes[i2] = ((Number) fieldSizesVec.get(i2)).intValue();
                }
            }
            int fieldTypeSize = fieldSizes[0];
            int fieldTwoSize = fieldSizes[1];
            int fieldThreeSize = fieldSizes[2];
            for (int xrefSubsection = 0; xrefSubsection < objNumAndEntriesCountPairs.size(); xrefSubsection += 2) {
                int startingObjectNumber = objNumAndEntriesCountPairs.get(xrefSubsection).intValue();
                int entriesCount = objNumAndEntriesCountPairs.get(xrefSubsection + 1).intValue();
                int afterObjectNumber = startingObjectNumber + entriesCount;
                for (int objectNumber = startingObjectNumber; objectNumber < afterObjectNumber; objectNumber++) {
                    int entryType = 1;
                    if (fieldTypeSize > 0) {
                        entryType = Utils.readIntWithVaryingBytesBE(streamInput, fieldTypeSize);
                    }
                    if (entryType == 1) {
                        long filePositionOfObject = Utils.readLongWithVaryingBytesBE(streamInput, fieldTwoSize);
                        int generationNumber = 0;
                        if (fieldThreeSize > 0) {
                            generationNumber = Utils.readIntWithVaryingBytesBE(streamInput, fieldThreeSize);
                        }
                        addUsedEntry(objectNumber, filePositionOfObject, generationNumber);
                    } else if (entryType == 2) {
                        int objectNumberOfContainingObjectStream = Utils.readIntWithVaryingBytesBE(streamInput, fieldTwoSize);
                        int indexWithinObjectStream = Utils.readIntWithVaryingBytesBE(streamInput, fieldThreeSize);
                        addCompressedEntry(objectNumber, objectNumberOfContainingObjectStream, indexWithinObjectStream);
                    } else if (entryType == 0) {
                        Utils.readIntWithVaryingBytesBE(streamInput, fieldTwoSize);
                        Utils.readIntWithVaryingBytesBE(streamInput, fieldThreeSize);
                    }
                }
            }
        } catch (IOException e2) {
            logger.log(Level.SEVERE, "Error parsing xRef stream entries.", (Throwable) e2);
        }
    }

    public Entry getEntryForObject(Integer objectNumber) {
        Entry entry = this.hObjectNumber2Entry.get(objectNumber);
        if (entry != null) {
            return entry;
        }
        if (this.bIsCrossReferenceTable && !this.bHaveTriedLoadingPeer && this.xrefPeer == null && this.pTrailer != null) {
            this.pTrailer.loadXRefStmIfApplicable();
            this.xrefPeer = this.pTrailer.getCrossReferenceStream();
            this.bHaveTriedLoadingPeer = true;
        }
        if (this.xrefPeer != null) {
            entry = this.xrefPeer.getEntryForObject(objectNumber);
            if (entry != null) {
                return entry;
            }
        }
        if (!this.bHaveTriedLoadingPrevious && this.xrefPrevious == null && this.pTrailer != null) {
            this.pTrailer.onDemandLoadAndSetupPreviousTrailer();
            this.bHaveTriedLoadingPrevious = true;
        }
        if (this.xrefPrevious != null) {
            entry = this.xrefPrevious.getEntryForObject(objectNumber);
            if (entry != null) {
                return entry;
            }
        }
        return entry;
    }

    public void addToEndOfChainOfPreviousXRefs(CrossReference prev) {
        if (this.xrefPrevious == null) {
            this.xrefPrevious = prev;
        } else {
            this.xrefPrevious.addToEndOfChainOfPreviousXRefs(prev);
        }
    }

    protected void addFreeEntry(int objectNumber, int nextFreeObjectNumber, int generationNumberIfReused) {
        new FreeEntry(objectNumber, nextFreeObjectNumber, generationNumberIfReused);
    }

    protected void addUsedEntry(int objectNumber, long filePositionOfObject, int generationNumber) {
        UsedEntry entry = new UsedEntry(objectNumber, filePositionOfObject, generationNumber);
        this.hObjectNumber2Entry.put(Integer.valueOf(objectNumber), entry);
    }

    protected void addCompressedEntry(int objectNumber, int objectNumberOfContainingObjectStream, int indexWithinObjectStream) {
        CompressedEntry entry = new CompressedEntry(objectNumber, objectNumberOfContainingObjectStream, indexWithinObjectStream);
        this.hObjectNumber2Entry.put(Integer.valueOf(objectNumber), entry);
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/CrossReference$Entry.class */
    public static class Entry {
        public static final int TYPE_FREE = 0;
        public static final int TYPE_USED = 1;
        public static final int TYPE_COMPRESSED = 2;
        private int Type;
        private int objectNumber;

        Entry(int type, int objectNumber) {
            this.Type = type;
            this.objectNumber = objectNumber;
        }

        int getType() {
            return this.Type;
        }

        int getObjectNumber() {
            return this.objectNumber;
        }
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/CrossReference$FreeEntry.class */
    public static class FreeEntry extends Entry {
        private int nextFreeObjectNumber;
        private int generationNumberIfReused;

        FreeEntry(int objectNumber, int nextFreeObjectNumber, int generationNumberIfReused) {
            super(0, objectNumber);
            this.nextFreeObjectNumber = nextFreeObjectNumber;
            this.generationNumberIfReused = generationNumberIfReused;
        }

        public int getNextFreeObjectNumber() {
            return this.nextFreeObjectNumber;
        }

        public int getGenerationNumberIfReused() {
            return this.generationNumberIfReused;
        }
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/CrossReference$UsedEntry.class */
    public class UsedEntry extends Entry {
        private long filePositionOfObject;
        private int generationNumber;

        UsedEntry(int objectNumber, long filePositionOfObject, int generationNumber) {
            super(1, objectNumber);
            this.filePositionOfObject = filePositionOfObject;
            this.generationNumber = generationNumber;
        }

        public long getFilePositionOfObject() {
            return this.filePositionOfObject + CrossReference.this.offset;
        }

        public int getGenerationNumber() {
            return this.generationNumber;
        }

        public void setFilePositionOfObject(long filePositionOfObject) {
            this.filePositionOfObject = filePositionOfObject;
        }
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/CrossReference$CompressedEntry.class */
    public static class CompressedEntry extends Entry {
        private int objectNumberOfContainingObjectStream;
        private int indexWithinObjectStream;

        CompressedEntry(int objectNumber, int objectNumberOfContainingObjectStream, int indexWithinObjectStream) {
            super(2, objectNumber);
            this.objectNumberOfContainingObjectStream = objectNumberOfContainingObjectStream;
            this.indexWithinObjectStream = indexWithinObjectStream;
        }

        public int getObjectNumberOfContainingObjectStream() {
            return this.objectNumberOfContainingObjectStream;
        }

        public int getIndexWithinObjectStream() {
            return this.indexWithinObjectStream;
        }
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
