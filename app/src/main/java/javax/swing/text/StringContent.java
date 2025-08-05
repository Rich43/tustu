package javax.swing.text;

import java.io.Serializable;
import java.util.Vector;
import javax.swing.text.AbstractDocument;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/* loaded from: rt.jar:javax/swing/text/StringContent.class */
public final class StringContent implements AbstractDocument.Content, Serializable {
    private static final char[] empty = new char[0];
    private char[] data;
    private int count;
    transient Vector<PosRec> marks;

    public StringContent() {
        this(10);
    }

    public StringContent(int i2) {
        this.data = new char[i2 < 1 ? 1 : i2];
        this.data[0] = '\n';
        this.count = 1;
    }

    @Override // javax.swing.text.AbstractDocument.Content
    public int length() {
        return this.count;
    }

    @Override // javax.swing.text.AbstractDocument.Content
    public UndoableEdit insertString(int i2, String str) throws BadLocationException {
        if (i2 >= this.count || i2 < 0) {
            throw new BadLocationException("Invalid location", this.count);
        }
        char[] charArray = str.toCharArray();
        replace(i2, 0, charArray, 0, charArray.length);
        if (this.marks != null) {
            updateMarksForInsert(i2, str.length());
        }
        return new InsertUndo(i2, str.length());
    }

    @Override // javax.swing.text.AbstractDocument.Content
    public UndoableEdit remove(int i2, int i3) throws BadLocationException {
        if (i2 + i3 >= this.count) {
            throw new BadLocationException("Invalid range", this.count);
        }
        RemoveUndo removeUndo = new RemoveUndo(i2, getString(i2, i3));
        replace(i2, i3, empty, 0, 0);
        if (this.marks != null) {
            updateMarksForRemove(i2, i3);
        }
        return removeUndo;
    }

    @Override // javax.swing.text.AbstractDocument.Content
    public String getString(int i2, int i3) throws BadLocationException {
        if (i2 + i3 > this.count) {
            throw new BadLocationException("Invalid range", this.count);
        }
        return new String(this.data, i2, i3);
    }

    @Override // javax.swing.text.AbstractDocument.Content
    public void getChars(int i2, int i3, Segment segment) throws BadLocationException {
        if (i2 + i3 > this.count) {
            throw new BadLocationException("Invalid location", this.count);
        }
        segment.array = this.data;
        segment.offset = i2;
        segment.count = i3;
    }

    @Override // javax.swing.text.AbstractDocument.Content
    public Position createPosition(int i2) throws BadLocationException {
        if (this.marks == null) {
            this.marks = new Vector<>();
        }
        return new StickyPosition(i2);
    }

    void replace(int i2, int i3, char[] cArr, int i4, int i5) {
        int i6 = i5 - i3;
        int i7 = i2 + i3;
        int i8 = this.count - i7;
        int i9 = i7 + i6;
        if (this.count + i6 >= this.data.length) {
            char[] cArr2 = new char[Math.max(2 * this.data.length, this.count + i6)];
            System.arraycopy(this.data, 0, cArr2, 0, i2);
            System.arraycopy(cArr, i4, cArr2, i2, i5);
            System.arraycopy(this.data, i7, cArr2, i9, i8);
            this.data = cArr2;
        } else {
            System.arraycopy(this.data, i7, this.data, i9, i8);
            System.arraycopy(cArr, i4, this.data, i2, i5);
        }
        this.count += i6;
    }

    void resize(int i2) {
        char[] cArr = new char[i2];
        System.arraycopy(this.data, 0, cArr, 0, Math.min(i2, this.count));
        this.data = cArr;
    }

    synchronized void updateMarksForInsert(int i2, int i3) {
        if (i2 == 0) {
            i2 = 1;
        }
        int size = this.marks.size();
        int i4 = 0;
        while (i4 < size) {
            PosRec posRecElementAt = this.marks.elementAt(i4);
            if (posRecElementAt.unused) {
                this.marks.removeElementAt(i4);
                i4--;
                size--;
            } else if (posRecElementAt.offset >= i2) {
                posRecElementAt.offset += i3;
            }
            i4++;
        }
    }

    synchronized void updateMarksForRemove(int i2, int i3) {
        int size = this.marks.size();
        int i4 = 0;
        while (i4 < size) {
            PosRec posRecElementAt = this.marks.elementAt(i4);
            if (posRecElementAt.unused) {
                this.marks.removeElementAt(i4);
                i4--;
                size--;
            } else if (posRecElementAt.offset >= i2 + i3) {
                posRecElementAt.offset -= i3;
            } else if (posRecElementAt.offset >= i2) {
                posRecElementAt.offset = i2;
            }
            i4++;
        }
    }

    protected Vector getPositionsInRange(Vector vector, int i2, int i3) {
        int size = this.marks.size();
        int i4 = i2 + i3;
        Vector vector2 = vector == null ? new Vector() : vector;
        int i5 = 0;
        while (i5 < size) {
            PosRec posRecElementAt = this.marks.elementAt(i5);
            if (posRecElementAt.unused) {
                this.marks.removeElementAt(i5);
                i5--;
                size--;
            } else if (posRecElementAt.offset >= i2 && posRecElementAt.offset <= i4) {
                vector2.addElement(new UndoPosRef(posRecElementAt));
            }
            i5++;
        }
        return vector2;
    }

    protected void updateUndoPositions(Vector vector) {
        for (int size = vector.size() - 1; size >= 0; size--) {
            UndoPosRef undoPosRef = (UndoPosRef) vector.elementAt(size);
            if (undoPosRef.rec.unused) {
                vector.removeElementAt(size);
            } else {
                undoPosRef.resetLocation();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StringContent$PosRec.class */
    final class PosRec {
        int offset;
        boolean unused;

        PosRec(int i2) {
            this.offset = i2;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StringContent$StickyPosition.class */
    final class StickyPosition implements Position {
        PosRec rec;

        StickyPosition(int i2) {
            this.rec = StringContent.this.new PosRec(i2);
            StringContent.this.marks.addElement(this.rec);
        }

        @Override // javax.swing.text.Position
        public int getOffset() {
            return this.rec.offset;
        }

        protected void finalize() throws Throwable {
            this.rec.unused = true;
        }

        public String toString() {
            return Integer.toString(getOffset());
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StringContent$UndoPosRef.class */
    final class UndoPosRef {
        protected int undoLocation;
        protected PosRec rec;

        UndoPosRef(PosRec posRec) {
            this.rec = posRec;
            this.undoLocation = posRec.offset;
        }

        protected void resetLocation() {
            this.rec.offset = this.undoLocation;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StringContent$InsertUndo.class */
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
                synchronized (StringContent.this) {
                    if (StringContent.this.marks != null) {
                        this.posRefs = StringContent.this.getPositionsInRange(null, this.offset, this.length);
                    }
                    this.string = StringContent.this.getString(this.offset, this.length);
                    StringContent.this.remove(this.offset, this.length);
                }
            } catch (BadLocationException e2) {
                throw new CannotUndoException();
            }
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void redo() throws CannotRedoException {
            super.redo();
            try {
                synchronized (StringContent.this) {
                    StringContent.this.insertString(this.offset, this.string);
                    this.string = null;
                    if (this.posRefs != null) {
                        StringContent.this.updateUndoPositions(this.posRefs);
                        this.posRefs = null;
                    }
                }
            } catch (BadLocationException e2) {
                throw new CannotRedoException();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StringContent$RemoveUndo.class */
    class RemoveUndo extends AbstractUndoableEdit {
        protected int offset;
        protected int length;
        protected String string;
        protected Vector posRefs;

        protected RemoveUndo(int i2, String str) {
            this.offset = i2;
            this.string = str;
            this.length = str.length();
            if (StringContent.this.marks != null) {
                this.posRefs = StringContent.this.getPositionsInRange(null, i2, this.length);
            }
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void undo() throws CannotUndoException {
            super.undo();
            try {
                synchronized (StringContent.this) {
                    StringContent.this.insertString(this.offset, this.string);
                    if (this.posRefs != null) {
                        StringContent.this.updateUndoPositions(this.posRefs);
                        this.posRefs = null;
                    }
                    this.string = null;
                }
            } catch (BadLocationException e2) {
                throw new CannotUndoException();
            }
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void redo() throws CannotRedoException {
            super.redo();
            try {
                synchronized (StringContent.this) {
                    this.string = StringContent.this.getString(this.offset, this.length);
                    if (StringContent.this.marks != null) {
                        this.posRefs = StringContent.this.getPositionsInRange(null, this.offset, this.length);
                    }
                    StringContent.this.remove(this.offset, this.length);
                }
            } catch (BadLocationException e2) {
                throw new CannotRedoException();
            }
        }
    }
}
