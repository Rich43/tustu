package javax.print.attribute;

import java.io.Serializable;
import java.util.Vector;

/* loaded from: rt.jar:javax/print/attribute/SetOfIntegerSyntax.class */
public abstract class SetOfIntegerSyntax implements Serializable, Cloneable {
    private static final long serialVersionUID = 3666874174847632203L;
    private int[][] members;

    protected SetOfIntegerSyntax(String str) {
        this.members = parse(str);
    }

    private static int[][] parse(String str) {
        Vector vector = new Vector();
        int length = str == null ? 0 : str.length();
        int i2 = 0;
        boolean z2 = false;
        int i3 = 0;
        int i4 = 0;
        while (i2 < length) {
            int i5 = i2;
            i2++;
            char cCharAt = str.charAt(i5);
            switch (z2) {
                case false:
                    if (Character.isWhitespace(cCharAt)) {
                        z2 = false;
                        break;
                    } else {
                        int iDigit = Character.digit(cCharAt, 10);
                        if (iDigit != -1) {
                            i3 = iDigit;
                            z2 = true;
                            break;
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                case true:
                    if (Character.isWhitespace(cCharAt)) {
                        z2 = 2;
                        break;
                    } else {
                        int iDigit2 = Character.digit(cCharAt, 10);
                        if (iDigit2 != -1) {
                            i3 = (10 * i3) + iDigit2;
                            z2 = true;
                            break;
                        } else if (cCharAt == '-' || cCharAt == ':') {
                            z2 = 3;
                            break;
                        } else if (cCharAt == ',') {
                            accumulate(vector, i3, i3);
                            z2 = 6;
                            break;
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                case true:
                    if (Character.isWhitespace(cCharAt)) {
                        z2 = 2;
                        break;
                    } else if (cCharAt == '-' || cCharAt == ':') {
                        z2 = 3;
                        break;
                    } else if (cCharAt == ',') {
                        accumulate(vector, i3, i3);
                        z2 = 6;
                        break;
                    } else {
                        throw new IllegalArgumentException();
                    }
                    break;
                case true:
                    if (Character.isWhitespace(cCharAt)) {
                        z2 = 3;
                        break;
                    } else {
                        int iDigit3 = Character.digit(cCharAt, 10);
                        if (iDigit3 != -1) {
                            i4 = iDigit3;
                            z2 = 4;
                            break;
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                case true:
                    if (Character.isWhitespace(cCharAt)) {
                        z2 = 5;
                        break;
                    } else {
                        int iDigit4 = Character.digit(cCharAt, 10);
                        if (iDigit4 != -1) {
                            i4 = (10 * i4) + iDigit4;
                            z2 = 4;
                            break;
                        } else if (cCharAt == ',') {
                            accumulate(vector, i3, i4);
                            z2 = 6;
                            break;
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                case true:
                    if (Character.isWhitespace(cCharAt)) {
                        z2 = 5;
                        break;
                    } else if (cCharAt == ',') {
                        accumulate(vector, i3, i4);
                        z2 = 6;
                        break;
                    } else {
                        throw new IllegalArgumentException();
                    }
                case true:
                    if (Character.isWhitespace(cCharAt)) {
                        z2 = 6;
                        break;
                    } else {
                        int iDigit5 = Character.digit(cCharAt, 10);
                        if (iDigit5 != -1) {
                            i3 = iDigit5;
                            z2 = true;
                            break;
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
            }
        }
        switch (z2) {
            case true:
            case true:
                accumulate(vector, i3, i3);
                break;
            case true:
            case true:
                throw new IllegalArgumentException();
            case true:
            case true:
                accumulate(vector, i3, i4);
                break;
        }
        return canonicalArrayForm(vector);
    }

    private static void accumulate(Vector vector, int i2, int i3) {
        if (i2 <= i3) {
            vector.add(new int[]{i2, i3});
            for (int size = vector.size() - 2; size >= 0; size--) {
                int[] iArr = (int[]) vector.elementAt(size);
                int i4 = iArr[0];
                int i5 = iArr[1];
                int[] iArr2 = (int[]) vector.elementAt(size + 1);
                int i6 = iArr2[0];
                int i7 = iArr2[1];
                if (Math.max(i4, i6) - Math.min(i5, i7) <= 1) {
                    vector.setElementAt(new int[]{Math.min(i4, i6), Math.max(i5, i7)}, size);
                    vector.remove(size + 1);
                } else if (i4 > i6) {
                    vector.setElementAt(iArr2, size);
                    vector.setElementAt(iArr, size + 1);
                } else {
                    return;
                }
            }
        }
    }

    private static int[][] canonicalArrayForm(Vector vector) {
        return (int[][]) vector.toArray(new int[vector.size()]);
    }

    protected SetOfIntegerSyntax(int[][] iArr) {
        this.members = parse(iArr);
    }

    private static int[][] parse(int[][] iArr) {
        int i2;
        int i3;
        Vector vector = new Vector();
        int length = iArr == null ? 0 : iArr.length;
        for (int i4 = 0; i4 < length; i4++) {
            if (iArr[i4].length == 1) {
                int i5 = iArr[i4][0];
                i3 = i5;
                i2 = i5;
            } else if (iArr[i4].length == 2) {
                i2 = iArr[i4][0];
                i3 = iArr[i4][1];
            } else {
                throw new IllegalArgumentException();
            }
            if (i2 <= i3 && i2 < 0) {
                throw new IllegalArgumentException();
            }
            accumulate(vector, i2, i3);
        }
        return canonicalArrayForm(vector);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [int[], int[][]] */
    protected SetOfIntegerSyntax(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        this.members = new int[]{new int[]{i2, i2}};
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected SetOfIntegerSyntax(int i2, int i3) {
        if (i2 <= i3 && i2 < 0) {
            throw new IllegalArgumentException();
        }
        this.members = i2 <= i3 ? new int[]{new int[]{i2, i3}} : new int[0];
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [int[], int[][]] */
    public int[][] getMembers() {
        int length = this.members.length;
        ?? r0 = new int[length];
        for (int i2 = 0; i2 < length; i2++) {
            r0[i2] = new int[]{this.members[i2][0], this.members[i2][1]};
        }
        return r0;
    }

    public boolean contains(int i2) {
        int length = this.members.length;
        for (int i3 = 0; i3 < length && i2 >= this.members[i3][0]; i3++) {
            if (i2 <= this.members[i3][1]) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(IntegerSyntax integerSyntax) {
        return contains(integerSyntax.getValue());
    }

    public int next(int i2) {
        int length = this.members.length;
        for (int i3 = 0; i3 < length; i3++) {
            if (i2 < this.members[i3][0]) {
                return this.members[i3][0];
            }
            if (i2 < this.members[i3][1]) {
                return i2 + 1;
            }
        }
        return -1;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof SetOfIntegerSyntax)) {
            int[][] iArr = this.members;
            int[][] iArr2 = ((SetOfIntegerSyntax) obj).members;
            int length = iArr.length;
            if (length == iArr2.length) {
                for (int i2 = 0; i2 < length; i2++) {
                    if (iArr[i2][0] != iArr2[i2][0] || iArr[i2][1] != iArr2[i2][1]) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int i2 = 0;
        int length = this.members.length;
        for (int i3 = 0; i3 < length; i3++) {
            i2 += this.members[i3][0] + this.members[i3][1];
        }
        return i2;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        int length = this.members.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (i2 > 0) {
                stringBuffer.append(',');
            }
            stringBuffer.append(this.members[i2][0]);
            if (this.members[i2][0] != this.members[i2][1]) {
                stringBuffer.append('-');
                stringBuffer.append(this.members[i2][1]);
            }
        }
        return stringBuffer.toString();
    }
}
