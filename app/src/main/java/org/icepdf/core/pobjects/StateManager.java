package org.icepdf.core.pobjects;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/StateManager.class */
public class StateManager {
    private static final Logger logger = Logger.getLogger(StateManager.class.getName());
    private HashMap<Reference, PObject> changes = new HashMap<>();
    private PTrailer trailer;
    private int nextReferenceNumber;

    public StateManager(PTrailer trailer) {
        this.trailer = trailer;
        if (trailer != null) {
            this.nextReferenceNumber = trailer.getNumberOfObjects();
        }
    }

    public Reference getNewReferencNumber() {
        Reference newReference = new Reference(this.nextReferenceNumber, 0);
        this.nextReferenceNumber++;
        return newReference;
    }

    public void addChange(PObject pObject) {
        this.changes.put(pObject.getReference(), pObject);
        int objectNumber = pObject.getReference().getObjectNumber();
        if (this.nextReferenceNumber <= objectNumber) {
            this.nextReferenceNumber = objectNumber + 1;
        }
    }

    public boolean contains(Reference reference) {
        return this.changes.containsKey(reference);
    }

    public Object getChange(Reference reference) {
        return this.changes.get(reference);
    }

    public void removeChange(PObject pObject) {
        this.changes.remove(pObject.getReference());
    }

    public boolean isChanged() {
        return !this.changes.isEmpty();
    }

    public int getChangedSize() {
        return this.changes.size();
    }

    public Iterator<PObject> iteratorSortedByObjectNumber() {
        Collection<PObject> coll = this.changes.values();
        PObject[] arr = (PObject[]) coll.toArray(new PObject[coll.size()]);
        Arrays.sort(arr, new PObjectComparatorByReferenceObjectNumber());
        List<PObject> sortedList = Arrays.asList(arr);
        return sortedList.iterator();
    }

    public PTrailer getTrailer() {
        return this.trailer;
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/StateManager$PObjectComparatorByReferenceObjectNumber.class */
    private static class PObjectComparatorByReferenceObjectNumber implements Comparator<PObject> {
        private PObjectComparatorByReferenceObjectNumber() {
        }

        @Override // java.util.Comparator
        public int compare(PObject a2, PObject b2) {
            if (a2 == null && b2 == null) {
                return 0;
            }
            if (a2 == null) {
                return -1;
            }
            if (b2 == null) {
                return 1;
            }
            Reference ar2 = a2.getReference();
            Reference br2 = b2.getReference();
            if (ar2 == null && br2 == null) {
                return 0;
            }
            if (ar2 == null) {
                return -1;
            }
            if (br2 == null) {
                return 1;
            }
            int aron = ar2.getObjectNumber();
            int bron = br2.getObjectNumber();
            if (aron < bron) {
                return -1;
            }
            if (aron > bron) {
                return 1;
            }
            return 0;
        }
    }
}
