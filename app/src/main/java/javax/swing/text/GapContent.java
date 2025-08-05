package javax.swing.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Vector;
import javax.swing.text.AbstractDocument;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/* loaded from: rt.jar:javax/swing/text/GapContent.class */
public class GapContent extends GapVector implements AbstractDocument.Content, Serializable {
    private static final char[] empty = new char[0];
    private transient MarkVector marks;
    private transient MarkData search;
    private transient int unusedMarks;
    private transient ReferenceQueue<StickyPosition> queue;
    static final int GROWTH_SIZE = 524288;

    public GapContent() {
        this(10);
    }

    public GapContent(int i2) {
        super(Math.max(i2, 2));
        this.unusedMarks = 0;
        char[] cArr = {'\n'};
        replace(0, 0, cArr, cArr.length);
        this.marks = new MarkVector();
        this.search = new MarkData(0);
        this.queue = new ReferenceQueue<>();
    }

    @Override // javax.swing.text.GapVector
    protected Object allocateArray(int i2) {
        return new char[i2];
    }

    @Override // javax.swing.text.GapVector
    protected int getArrayLength() {
        return ((char[]) getArray()).length;
    }

    @Override // javax.swing.text.AbstractDocument.Content
    public int length() {
        return getArrayLength() - (getGapEnd() - getGapStart());
    }

    @Override // javax.swing.text.AbstractDocument.Content
    public UndoableEdit insertString(int i2, String str) throws BadLocationException {
        if (i2 > length() || i2 < 0) {
            throw new BadLocationException("Invalid insert", length());
        }
        char[] charArray = str.toCharArray();
        replace(i2, 0, charArray, charArray.length);
        return new InsertUndo(i2, str.length());
    }

    @Override // javax.swing.text.AbstractDocument.Content
    public UndoableEdit remove(int i2, int i3) throws BadLocationException {
        if (i2 + i3 >= length()) {
            throw new BadLocationException("Invalid remove", length() + 1);
        }
        RemoveUndo removeUndo = new RemoveUndo(i2, getString(i2, i3));
        replace(i2, i3, empty, 0);
        return removeUndo;
    }

    @Override // javax.swing.text.AbstractDocument.Content
    public String getString(int i2, int i3) throws BadLocationException {
        Segment segment = new Segment();
        getChars(i2, i3, segment);
        return new String(segment.array, segment.offset, segment.count);
    }

    @Override // javax.swing.text.AbstractDocument.Content
    public void getChars(int i2, int i3, Segment segment) throws BadLocationException {
        int i4 = i2 + i3;
        if (i2 < 0 || i4 < 0) {
            throw new BadLocationException("Invalid location", -1);
        }
        if (i4 > length() || i2 > length()) {
            throw new BadLocationException("Invalid location", length() + 1);
        }
        int gapStart = getGapStart();
        int gapEnd = getGapEnd();
        char[] cArr = (char[]) getArray();
        if (i2 + i3 <= gapStart) {
            segment.array = cArr;
            segment.offset = i2;
        } else if (i2 >= gapStart) {
            segment.array = cArr;
            segment.offset = (gapEnd + i2) - gapStart;
        } else {
            int i5 = gapStart - i2;
            if (segment.isPartialReturn()) {
                segment.array = cArr;
                segment.offset = i2;
                segment.count = i5;
                return;
            } else {
                segment.array = new char[i3];
                segment.offset = 0;
                System.arraycopy(cArr, i2, segment.array, 0, i5);
                System.arraycopy(cArr, gapEnd, segment.array, i5, i3 - i5);
            }
        }
        segment.count = i3;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x008b  */
    @Override // javax.swing.text.AbstractDocument.Content
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public javax.swing.text.Position createPosition(int r8) throws javax.swing.text.BadLocationException {
        /*
            r7 = this;
        L0:
            r0 = r7
            java.lang.ref.ReferenceQueue<javax.swing.text.GapContent$StickyPosition> r0 = r0.queue
            java.lang.ref.Reference r0 = r0.poll()
            if (r0 == 0) goto L17
            r0 = r7
            r1 = r0
            int r1 = r1.unusedMarks
            r2 = 1
            int r1 = r1 + r2
            r0.unusedMarks = r1
            goto L0
        L17:
            r0 = r7
            int r0 = r0.unusedMarks
            r1 = 5
            r2 = r7
            javax.swing.text.GapContent$MarkVector r2 = r2.marks
            int r2 = r2.size()
            r3 = 10
            int r2 = r2 / r3
            int r1 = java.lang.Math.max(r1, r2)
            if (r0 <= r1) goto L30
            r0 = r7
            r0.removeUnusedMarks()
        L30:
            r0 = r7
            int r0 = r0.getGapStart()
            r9 = r0
            r0 = r7
            int r0 = r0.getGapEnd()
            r10 = r0
            r0 = r8
            r1 = r9
            if (r0 >= r1) goto L43
            r0 = r8
            goto L48
        L43:
            r0 = r8
            r1 = r10
            r2 = r9
            int r1 = r1 - r2
            int r0 = r0 + r1
        L48:
            r11 = r0
            r0 = r7
            javax.swing.text.GapContent$MarkData r0 = r0.search
            r1 = r11
            r0.index = r1
            r0 = r7
            r1 = r7
            javax.swing.text.GapContent$MarkData r1 = r1.search
            int r0 = r0.findSortIndex(r1)
            r12 = r0
            r0 = r12
            r1 = r7
            javax.swing.text.GapContent$MarkVector r1 = r1.marks
            int r1 = r1.size()
            if (r0 >= r1) goto L8b
            r0 = r7
            javax.swing.text.GapContent$MarkVector r0 = r0.marks
            r1 = r12
            javax.swing.text.GapContent$MarkData r0 = r0.elementAt(r1)
            r1 = r0
            r13 = r1
            int r0 = r0.index
            r1 = r11
            if (r0 != r1) goto L8b
            r0 = r13
            javax.swing.text.GapContent$StickyPosition r0 = r0.getPosition()
            r1 = r0
            r14 = r1
            if (r0 == 0) goto L8b
            goto Lb9
        L8b:
            javax.swing.text.GapContent$StickyPosition r0 = new javax.swing.text.GapContent$StickyPosition
            r1 = r0
            r2 = r7
            r1.<init>()
            r14 = r0
            javax.swing.text.GapContent$MarkData r0 = new javax.swing.text.GapContent$MarkData
            r1 = r0
            r2 = r7
            r3 = r11
            r4 = r14
            r5 = r7
            java.lang.ref.ReferenceQueue<javax.swing.text.GapContent$StickyPosition> r5 = r5.queue
            r1.<init>(r3, r4, r5)
            r13 = r0
            r0 = r14
            r1 = r13
            r0.setMark(r1)
            r0 = r7
            javax.swing.text.GapContent$MarkVector r0 = r0.marks
            r1 = r13
            r2 = r12
            r0.insertElementAt(r1, r2)
        Lb9:
            r0 = r14
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.text.GapContent.createPosition(int):javax.swing.text.Position");
    }

    /* loaded from: rt.jar:javax/swing/text/GapContent$MarkData.class */
    final class MarkData extends WeakReference<StickyPosition> {
        int index;

        MarkData(int i2) {
            super(null);
            this.index = i2;
        }

        MarkData(int i2, StickyPosition stickyPosition, ReferenceQueue<? super StickyPosition> referenceQueue) {
            super(stickyPosition, referenceQueue);
            this.index = i2;
        }

        public final int getOffset() {
            int gapStart = GapContent.this.getGapStart();
            return Math.max(this.index < gapStart ? this.index : this.index - (GapContent.this.getGapEnd() - gapStart), 0);
        }

        StickyPosition getPosition() {
            return get();
        }
    }

    /* loaded from: rt.jar:javax/swing/text/GapContent$StickyPosition.class */
    final class StickyPosition implements Position {
        MarkData mark;

        StickyPosition() {
        }

        void setMark(MarkData markData) {
            this.mark = markData;
        }

        @Override // javax.swing.text.Position
        public final int getOffset() {
            return this.mark.getOffset();
        }

        public String toString() {
            return Integer.toString(getOffset());
        }
    }

    @Override // javax.swing.text.GapVector
    protected void shiftEnd(int i2) {
        int gapEnd = getGapEnd();
        super.shiftEnd(i2);
        int gapEnd2 = getGapEnd() - gapEnd;
        int iFindMarkAdjustIndex = findMarkAdjustIndex(gapEnd);
        int size = this.marks.size();
        for (int i3 = iFindMarkAdjustIndex; i3 < size; i3++) {
            this.marks.elementAt(i3).index += gapEnd2;
        }
    }

    @Override // javax.swing.text.GapVector
    int getNewArraySize(int i2) {
        if (i2 < 524288) {
            return super.getNewArraySize(i2);
        }
        return i2 + 524288;
    }

    @Override // javax.swing.text.GapVector
    protected void shiftGap(int i2) {
        int gapStart = getGapStart();
        int i3 = i2 - gapStart;
        int gapEnd = getGapEnd();
        int i4 = gapEnd + i3;
        int i5 = gapEnd - gapStart;
        super.shiftGap(i2);
        if (i3 > 0) {
            int iFindMarkAdjustIndex = findMarkAdjustIndex(gapStart);
            int size = this.marks.size();
            for (int i6 = iFindMarkAdjustIndex; i6 < size; i6++) {
                MarkData markDataElementAt = this.marks.elementAt(i6);
                if (markDataElementAt.index >= i4) {
                    break;
                }
                markDataElementAt.index -= i5;
            }
        } else if (i3 < 0) {
            int iFindMarkAdjustIndex2 = findMarkAdjustIndex(i2);
            int size2 = this.marks.size();
            for (int i7 = iFindMarkAdjustIndex2; i7 < size2; i7++) {
                MarkData markDataElementAt2 = this.marks.elementAt(i7);
                if (markDataElementAt2.index >= gapEnd) {
                    break;
                }
                markDataElementAt2.index += i5;
            }
        }
        resetMarksAtZero();
    }

    protected void resetMarksAtZero() {
        if (this.marks != null && getGapStart() == 0) {
            int gapEnd = getGapEnd();
            int size = this.marks.size();
            for (int i2 = 0; i2 < size; i2++) {
                MarkData markDataElementAt = this.marks.elementAt(i2);
                if (markDataElementAt.index <= gapEnd) {
                    markDataElementAt.index = 0;
                } else {
                    return;
                }
            }
        }
    }

    @Override // javax.swing.text.GapVector
    protected void shiftGapStartDown(int i2) {
        int iFindMarkAdjustIndex = findMarkAdjustIndex(i2);
        int size = this.marks.size();
        int gapStart = getGapStart();
        int gapEnd = getGapEnd();
        for (int i3 = iFindMarkAdjustIndex; i3 < size; i3++) {
            MarkData markDataElementAt = this.marks.elementAt(i3);
            if (markDataElementAt.index > gapStart) {
                break;
            }
            markDataElementAt.index = gapEnd;
        }
        super.shiftGapStartDown(i2);
        resetMarksAtZero();
    }

    @Override // javax.swing.text.GapVector
    protected void shiftGapEndUp(int i2) {
        int iFindMarkAdjustIndex = findMarkAdjustIndex(getGapEnd());
        int size = this.marks.size();
        for (int i3 = iFindMarkAdjustIndex; i3 < size; i3++) {
            MarkData markDataElementAt = this.marks.elementAt(i3);
            if (markDataElementAt.index >= i2) {
                break;
            }
            markDataElementAt.index = i2;
        }
        super.shiftGapEndUp(i2);
        resetMarksAtZero();
    }

    final int compare(MarkData markData, MarkData markData2) {
        if (markData.index < markData2.index) {
            return -1;
        }
        if (markData.index > markData2.index) {
            return 1;
        }
        return 0;
    }

    final int findMarkAdjustIndex(int i2) {
        this.search.index = Math.max(i2, 1);
        int iFindSortIndex = findSortIndex(this.search);
        for (int i3 = iFindSortIndex - 1; i3 >= 0 && this.marks.elementAt(i3).index == this.search.index; i3--) {
            iFindSortIndex--;
        }
        return iFindSortIndex;
    }

    final int findSortIndex(MarkData markData) {
        int i2 = 0;
        int size = this.marks.size() - 1;
        int i3 = 0;
        if (size == -1) {
            return 0;
        }
        int iCompare = compare(markData, this.marks.elementAt(size));
        if (iCompare > 0) {
            return size + 1;
        }
        while (i2 <= size) {
            i3 = i2 + ((size - i2) / 2);
            iCompare = compare(markData, this.marks.elementAt(i3));
            if (iCompare == 0) {
                return i3;
            }
            if (iCompare < 0) {
                size = i3 - 1;
            } else {
                i2 = i3 + 1;
            }
        }
        return iCompare < 0 ? i3 : i3 + 1;
    }

    final void removeUnusedMarks() {
        int size = this.marks.size();
        MarkVector markVector = new MarkVector(size);
        for (int i2 = 0; i2 < size; i2++) {
            MarkData markDataElementAt = this.marks.elementAt(i2);
            if (markDataElementAt.get() != null) {
                markVector.addElement(markDataElementAt);
            }
        }
        this.marks = markVector;
        this.unusedMarks = 0;
    }

    /* loaded from: rt.jar:javax/swing/text/GapContent$MarkVector.class */
    static class MarkVector extends GapVector {
        MarkData[] oneMark;

        MarkVector() {
            this.oneMark = new MarkData[1];
        }

        MarkVector(int i2) {
            super(i2);
            this.oneMark = new MarkData[1];
        }

        @Override // javax.swing.text.GapVector
        protected Object allocateArray(int i2) {
            return new MarkData[i2];
        }

        @Override // javax.swing.text.GapVector
        protected int getArrayLength() {
            return ((MarkData[]) getArray()).length;
        }

        public int size() {
            return getArrayLength() - (getGapEnd() - getGapStart());
        }

        public void insertElementAt(MarkData markData, int i2) {
            this.oneMark[0] = markData;
            replace(i2, 0, this.oneMark, 1);
        }

        public void addElement(MarkData markData) {
            insertElementAt(markData, size());
        }

        public MarkData elementAt(int i2) {
            int gapStart = getGapStart();
            int gapEnd = getGapEnd();
            MarkData[] markDataArr = (MarkData[]) getArray();
            if (i2 < gapStart) {
                return markDataArr[i2];
            }
            return markDataArr[i2 + (gapEnd - gapStart)];
        }

        protected void replaceRange(int i2, int i3, Object[] objArr) {
            int gapStart = getGapStart();
            int gapEnd = getGapEnd();
            int i4 = i2;
            int i5 = 0;
            Object[] objArr2 = (Object[]) getArray();
            if (i2 >= gapStart) {
                i4 += gapEnd - gapStart;
                i3 += gapEnd - gapStart;
            } else if (i3 >= gapStart) {
                i3 += gapEnd - gapStart;
                while (i4 < gapStart) {
                    int i6 = i4;
                    i4++;
                    int i7 = i5;
                    i5++;
                    objArr2[i6] = objArr[i7];
                }
                i4 = gapEnd;
            } else {
                while (i4 < i3) {
                    int i8 = i4;
                    i4++;
                    int i9 = i5;
                    i5++;
                    objArr2[i8] = objArr[i9];
                }
            }
            while (i4 < i3) {
                int i10 = i4;
                i4++;
                int i11 = i5;
                i5++;
                objArr2[i10] = objArr[i11];
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.marks = new MarkVector();
        this.search = new MarkData(0);
        this.queue = new ReferenceQueue<>();
    }

    protected Vector getPositionsInRange(Vector vector, int i2, int i3) {
        int iFindMarkAdjustIndex;
        int iFindMarkAdjustIndex2;
        int i4 = i2 + i3;
        int gapStart = getGapStart();
        int gapEnd = getGapEnd();
        if (i2 < gapStart) {
            if (i2 == 0) {
                iFindMarkAdjustIndex = 0;
            } else {
                iFindMarkAdjustIndex = findMarkAdjustIndex(i2);
            }
            if (i4 >= gapStart) {
                iFindMarkAdjustIndex2 = findMarkAdjustIndex(i4 + (gapEnd - gapStart) + 1);
            } else {
                iFindMarkAdjustIndex2 = findMarkAdjustIndex(i4 + 1);
            }
        } else {
            iFindMarkAdjustIndex = findMarkAdjustIndex(i2 + (gapEnd - gapStart));
            iFindMarkAdjustIndex2 = findMarkAdjustIndex(i4 + (gapEnd - gapStart) + 1);
        }
        Vector vector2 = vector == null ? new Vector(Math.max(1, iFindMarkAdjustIndex2 - iFindMarkAdjustIndex)) : vector;
        for (int i5 = iFindMarkAdjustIndex; i5 < iFindMarkAdjustIndex2; i5++) {
            vector2.addElement(new UndoPosRef(this.marks.elementAt(i5)));
        }
        return vector2;
    }

    protected void updateUndoPositions(Vector vector, int i2, int i3) {
        int iFindMarkAdjustIndex;
        int i4 = i2 + i3;
        int gapEnd = getGapEnd();
        int iFindMarkAdjustIndex2 = findMarkAdjustIndex(gapEnd + 1);
        if (i2 != 0) {
            iFindMarkAdjustIndex = findMarkAdjustIndex(gapEnd);
        } else {
            iFindMarkAdjustIndex = 0;
        }
        for (int size = vector.size() - 1; size >= 0; size--) {
            ((UndoPosRef) vector.elementAt(size)).resetLocation(i4, gapEnd);
        }
        if (iFindMarkAdjustIndex < iFindMarkAdjustIndex2) {
            Object[] objArr = new Object[iFindMarkAdjustIndex2 - iFindMarkAdjustIndex];
            int i5 = 0;
            if (i2 == 0) {
                for (int i6 = iFindMarkAdjustIndex; i6 < iFindMarkAdjustIndex2; i6++) {
                    MarkData markDataElementAt = this.marks.elementAt(i6);
                    if (markDataElementAt.index == 0) {
                        int i7 = i5;
                        i5++;
                        objArr[i7] = markDataElementAt;
                    }
                }
                for (int i8 = iFindMarkAdjustIndex; i8 < iFindMarkAdjustIndex2; i8++) {
                    MarkData markDataElementAt2 = this.marks.elementAt(i8);
                    if (markDataElementAt2.index != 0) {
                        int i9 = i5;
                        i5++;
                        objArr[i9] = markDataElementAt2;
                    }
                }
            } else {
                for (int i10 = iFindMarkAdjustIndex; i10 < iFindMarkAdjustIndex2; i10++) {
                    MarkData markDataElementAt3 = this.marks.elementAt(i10);
                    if (markDataElementAt3.index != gapEnd) {
                        int i11 = i5;
                        i5++;
                        objArr[i11] = markDataElementAt3;
                    }
                }
                for (int i12 = iFindMarkAdjustIndex; i12 < iFindMarkAdjustIndex2; i12++) {
                    MarkData markDataElementAt4 = this.marks.elementAt(i12);
                    if (markDataElementAt4.index == gapEnd) {
                        int i13 = i5;
                        i5++;
                        objArr[i13] = markDataElementAt4;
                    }
                }
            }
            this.marks.replaceRange(iFindMarkAdjustIndex, iFindMarkAdjustIndex2, objArr);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/GapContent$UndoPosRef.class */
    final class UndoPosRef {
        protected int undoLocation;
        protected MarkData rec;

        UndoPosRef(MarkData markData) {
            this.rec = markData;
            this.undoLocation = markData.getOffset();
        }

        protected void resetLocation(int i2, int i3) {
            if (this.undoLocation != i2) {
                this.rec.index = this.undoLocation;
            } else {
                this.rec.index = i3;
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/GapContent$InsertUndo.class */
    class InsertUndo extends AbstractUndoableEdit {
        protected int offset;
        protected int length;
        protected String string;
        protected Vector posRefs;

        protected InsertUndo(int i2, int i3) {
            this.offset = i2;
            this.length = i3;
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void undo() throws CannotUndoException {
            super.undo();
            try {
                this.posRefs = GapContent.this.getPositionsInRange(null, this.offset, this.length);
                this.string = GapContent.this.getString(this.offset, this.length);
                GapContent.this.remove(this.offset, this.length);
            } catch (BadLocationException e2) {
                throw new CannotUndoException();
            }
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void redo() throws CannotRedoException {
            super.redo();
            try {
                GapContent.this.insertString(this.offset, this.string);
                this.string = null;
                if (this.posRefs != null) {
                    GapContent.this.updateUndoPositions(this.posRefs, this.offset, this.length);
                    this.posRefs = null;
                }
            } catch (BadLocationException e2) {
                throw new CannotRedoException();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/GapContent$RemoveUndo.class */
    class RemoveUndo extends AbstractUndoableEdit {
        protected int offset;
        protected int length;
        protected String string;
        protected Vector posRefs;

        protected RemoveUndo(int i2, String str) {
            this.offset = i2;
            this.string = str;
            this.length = str.length();
            this.posRefs = GapContent.this.getPositionsInRange(null, i2, this.length);
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void undo() throws CannotUndoException {
            super.undo();
            try {
                GapContent.this.insertString(this.offset, this.string);
                if (this.posRefs != null) {
                    GapContent.this.updateUndoPositions(this.posRefs, this.offset, this.length);
                    this.posRefs = null;
                }
                this.string = null;
            } catch (BadLocationException e2) {
                throw new CannotUndoException();
            }
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void redo() throws CannotRedoException {
            super.redo();
            try {
                this.string = GapContent.this.getString(this.offset, this.length);
                this.posRefs = GapContent.this.getPositionsInRange(null, this.offset, this.length);
                GapContent.this.remove(this.offset, this.length);
            } catch (BadLocationException e2) {
                throw new CannotRedoException();
            }
        }
    }
}
