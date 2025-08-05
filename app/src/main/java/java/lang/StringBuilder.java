package java.lang;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

/* loaded from: rt.jar:java/lang/StringBuilder.class */
public final class StringBuilder extends AbstractStringBuilder implements Serializable, CharSequence {
    static final long serialVersionUID = 4383685877147921099L;

    @Override // java.lang.AbstractStringBuilder
    public /* bridge */ /* synthetic */ String substring(int i2, int i3) {
        return super.substring(i2, i3);
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.CharSequence
    public /* bridge */ /* synthetic */ CharSequence subSequence(int i2, int i3) {
        return super.subSequence(i2, i3);
    }

    @Override // java.lang.AbstractStringBuilder
    public /* bridge */ /* synthetic */ String substring(int i2) {
        return super.substring(i2);
    }

    @Override // java.lang.AbstractStringBuilder
    public /* bridge */ /* synthetic */ void setCharAt(int i2, char c2) {
        super.setCharAt(i2, c2);
    }

    @Override // java.lang.AbstractStringBuilder
    public /* bridge */ /* synthetic */ void getChars(int i2, int i3, char[] cArr, int i4) {
        super.getChars(i2, i3, cArr, i4);
    }

    @Override // java.lang.AbstractStringBuilder
    public /* bridge */ /* synthetic */ int offsetByCodePoints(int i2, int i3) {
        return super.offsetByCodePoints(i2, i3);
    }

    @Override // java.lang.AbstractStringBuilder
    public /* bridge */ /* synthetic */ int codePointCount(int i2, int i3) {
        return super.codePointCount(i2, i3);
    }

    @Override // java.lang.AbstractStringBuilder
    public /* bridge */ /* synthetic */ int codePointBefore(int i2) {
        return super.codePointBefore(i2);
    }

    @Override // java.lang.AbstractStringBuilder
    public /* bridge */ /* synthetic */ int codePointAt(int i2) {
        return super.codePointAt(i2);
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.CharSequence
    public /* bridge */ /* synthetic */ char charAt(int i2) {
        return super.charAt(i2);
    }

    @Override // java.lang.AbstractStringBuilder
    public /* bridge */ /* synthetic */ void setLength(int i2) {
        super.setLength(i2);
    }

    @Override // java.lang.AbstractStringBuilder
    public /* bridge */ /* synthetic */ void trimToSize() {
        super.trimToSize();
    }

    @Override // java.lang.AbstractStringBuilder
    public /* bridge */ /* synthetic */ void ensureCapacity(int i2) {
        super.ensureCapacity(i2);
    }

    @Override // java.lang.AbstractStringBuilder
    public /* bridge */ /* synthetic */ int capacity() {
        return super.capacity();
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.CharSequence
    public /* bridge */ /* synthetic */ int length() {
        return super.length();
    }

    public StringBuilder() {
        super(16);
    }

    public StringBuilder(int i2) {
        super(i2);
    }

    public StringBuilder(String str) {
        super(str.length() + 16);
        append(str);
    }

    public StringBuilder(CharSequence charSequence) {
        this(charSequence.length() + 16);
        append(charSequence);
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder append(Object obj) {
        return append(String.valueOf(obj));
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder append(String str) {
        super.append(str);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder append(StringBuffer stringBuffer) {
        super.append(stringBuffer);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.Appendable
    public StringBuilder append(CharSequence charSequence) {
        super.append(charSequence);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.Appendable
    public StringBuilder append(CharSequence charSequence, int i2, int i3) {
        super.append(charSequence, i2, i3);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder append(char[] cArr) {
        super.append(cArr);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder append(char[] cArr, int i2, int i3) {
        super.append(cArr, i2, i3);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder append(boolean z2) {
        super.append(z2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.Appendable
    public StringBuilder append(char c2) {
        super.append(c2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder append(int i2) {
        super.append(i2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder append(long j2) {
        super.append(j2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder append(float f2) {
        super.append(f2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder append(double d2) {
        super.append(d2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder appendCodePoint(int i2) {
        super.appendCodePoint(i2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder delete(int i2, int i3) {
        super.delete(i2, i3);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder deleteCharAt(int i2) {
        super.deleteCharAt(i2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder replace(int i2, int i3, String str) {
        super.replace(i2, i3, str);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder insert(int i2, char[] cArr, int i3, int i4) {
        super.insert(i2, cArr, i3, i4);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder insert(int i2, Object obj) {
        super.insert(i2, obj);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder insert(int i2, String str) {
        super.insert(i2, str);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder insert(int i2, char[] cArr) {
        super.insert(i2, cArr);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder insert(int i2, CharSequence charSequence) {
        super.insert(i2, charSequence);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder insert(int i2, CharSequence charSequence, int i3, int i4) {
        super.insert(i2, charSequence, i3, i4);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder insert(int i2, boolean z2) {
        super.insert(i2, z2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder insert(int i2, char c2) {
        super.insert(i2, c2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder insert(int i2, int i3) {
        super.insert(i2, i3);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder insert(int i2, long j2) {
        super.insert(i2, j2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder insert(int i2, float f2) {
        super.insert(i2, f2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder insert(int i2, double d2) {
        super.insert(i2, d2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public int indexOf(String str) {
        return super.indexOf(str);
    }

    @Override // java.lang.AbstractStringBuilder
    public int indexOf(String str, int i2) {
        return super.indexOf(str, i2);
    }

    @Override // java.lang.AbstractStringBuilder
    public int lastIndexOf(String str) {
        return super.lastIndexOf(str);
    }

    @Override // java.lang.AbstractStringBuilder
    public int lastIndexOf(String str, int i2) {
        return super.lastIndexOf(str, i2);
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuilder reverse() {
        super.reverse();
        return this;
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.CharSequence
    public String toString() {
        return new String(this.value, 0, this.count);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.count);
        objectOutputStream.writeObject(this.value);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int i2 = objectInputStream.readInt();
        this.value = (char[]) objectInputStream.readObject();
        if (i2 < 0 || i2 > this.value.length) {
            throw new StreamCorruptedException("count value invalid");
        }
        this.count = i2;
    }
}
