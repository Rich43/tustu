package org.icepdf.core.pobjects;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/PTrailer.class */
public class PTrailer extends Dictionary {
    public static final Name SIZE_KEY = new Name("Size");
    public static final Name PREV_KEY = new Name("Prev");
    public static final Name ROOT_KEY = new Name("Root");
    public static final Name ENCRYPT_KEY = new Name("Encrypt");
    public static final Name INFO_KEY = new Name("Info");
    public static final Name ID_KEY = new Name("ID");
    public static final Name XREFSTM_KEY = new Name("XRefStm");
    private long position;
    private CrossReference crossReferenceTable;
    private CrossReference crossReferenceStream;

    public PTrailer(Library library, HashMap dictionary, CrossReference xrefTable, CrossReference xrefStream) {
        super(library, dictionary);
        this.crossReferenceTable = xrefTable;
        this.crossReferenceStream = xrefStream;
        if (this.crossReferenceTable != null) {
            this.crossReferenceTable.setTrailer(this);
        }
        if (this.crossReferenceStream != null) {
            this.crossReferenceStream.setTrailer(this);
        }
    }

    public int getNumberOfObjects() {
        return this.library.getInt(this.entries, SIZE_KEY);
    }

    public long getPrev() {
        return this.library.getLong(this.entries, PREV_KEY);
    }

    protected CrossReference getPrimaryCrossReference() {
        if (this.crossReferenceTable != null) {
            return this.crossReferenceTable;
        }
        loadXRefStmIfApplicable();
        return this.crossReferenceStream;
    }

    protected CrossReference getCrossReferenceTable() {
        return this.crossReferenceTable;
    }

    protected CrossReference getCrossReferenceStream() {
        return this.crossReferenceStream;
    }

    public Reference getRootCatalogReference() {
        return this.library.getObjectReference(this.entries, ROOT_KEY);
    }

    public Catalog getRootCatalog() {
        Object tmp = this.library.getObject(this.entries, ROOT_KEY);
        if (tmp instanceof Catalog) {
            return (Catalog) tmp;
        }
        if (tmp instanceof HashMap) {
            return new Catalog(this.library, (HashMap) tmp);
        }
        return null;
    }

    public HashMap<Object, Object> getEncrypt() {
        Object encryptParams = this.library.getObject(this.entries, ENCRYPT_KEY);
        if (encryptParams instanceof HashMap) {
            return (HashMap) encryptParams;
        }
        return null;
    }

    public PInfo getInfo() {
        Object info = this.library.getObject(this.entries, INFO_KEY);
        if (info instanceof HashMap) {
            return new PInfo(this.library, (HashMap) info);
        }
        return null;
    }

    public List getID() {
        return (List) this.library.getObject(this.entries, ID_KEY);
    }

    public long getPosition() {
        return this.position;
    }

    public void setPosition(long pos) {
        this.position = pos;
    }

    protected void addNextTrailer(PTrailer nextTrailer) {
        nextTrailer.getPrimaryCrossReference().addToEndOfChainOfPreviousXRefs(getPrimaryCrossReference());
        HashMap nextDictionary = nextTrailer.getDictionary();
        HashMap currDictionary = getDictionary();
        Set currKeys = currDictionary.keySet();
        for (Object currKey : currKeys) {
            if (!nextDictionary.containsKey(currKey)) {
                Object currValue = currDictionary.get(currKey);
                nextDictionary.put(currKey, currValue);
            }
        }
    }

    protected void addPreviousTrailer(PTrailer previousTrailer) {
        getPrimaryCrossReference().addToEndOfChainOfPreviousXRefs(previousTrailer.getPrimaryCrossReference());
        HashMap currDictionary = getDictionary();
        HashMap prevDictionary = previousTrailer.getDictionary();
        Set prevKeys = prevDictionary.keySet();
        for (Object prevKey : prevKeys) {
            if (!currDictionary.containsKey(prevKey)) {
                Object prevValue = prevDictionary.get(prevKey);
                currDictionary.put(prevKey, prevValue);
            }
        }
    }

    protected void onDemandLoadAndSetupPreviousTrailer() {
        PTrailer prevTrailer;
        long position = getPrev();
        if (position > 0 && (prevTrailer = this.library.getTrailerByFilePosition(position)) != null) {
            addPreviousTrailer(prevTrailer);
        }
    }

    protected void loadXRefStmIfApplicable() {
        PTrailer trailer;
        if (this.crossReferenceStream == null) {
            long xrefStreamPosition = this.library.getLong(this.entries, XREFSTM_KEY);
            if (xrefStreamPosition > 0 && (trailer = this.library.getTrailerByFilePosition(xrefStreamPosition)) != null) {
                this.crossReferenceStream = trailer.getCrossReferenceStream();
            }
        }
    }

    public HashMap getDictionary() {
        return this.entries;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public String toString() {
        return "PTRAILER= " + this.entries.toString() + " xref table=" + ((Object) this.crossReferenceTable) + "  xref stream=" + ((Object) this.crossReferenceStream);
    }
}
