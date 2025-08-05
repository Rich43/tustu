package java.lang;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Arrays;

/* loaded from: rt.jar:java/lang/StringBuffer.class */
public final class StringBuffer extends AbstractStringBuilder implements Serializable, CharSequence {
    private transient char[] toStringCache;
    static final long serialVersionUID = 3388685877147921107L;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("value", char[].class), new ObjectStreamField("count", Integer.TYPE), new ObjectStreamField("shared", Boolean.TYPE)};

    public StringBuffer() {
        super(16);
    }

    public StringBuffer(int i2) {
        super(i2);
    }

    public StringBuffer(String str) {
        super(str.length() + 16);
        append(str);
    }

    public StringBuffer(CharSequence charSequence) {
        this(charSequence.length() + 16);
        append(charSequence);
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.CharSequence
    public synchronized int length() {
        return this.count;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized int capacity() {
        return this.value.length;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized void ensureCapacity(int i2) {
        super.ensureCapacity(i2);
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized void trimToSize() {
        super.trimToSize();
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized void setLength(int i2) {
        this.toStringCache = null;
        super.setLength(i2);
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.CharSequence
    public synchronized char charAt(int i2) {
        if (i2 < 0 || i2 >= this.count) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        return this.value[i2];
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized int codePointAt(int i2) {
        return super.codePointAt(i2);
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized int codePointBefore(int i2) {
        return super.codePointBefore(i2);
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized int codePointCount(int i2, int i3) {
        return super.codePointCount(i2, i3);
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized int offsetByCodePoints(int i2, int i3) {
        return super.offsetByCodePoints(i2, i3);
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized void getChars(int i2, int i3, char[] cArr, int i4) {
        super.getChars(i2, i3, cArr, i4);
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized void setCharAt(int i2, char c2) {
        if (i2 < 0 || i2 >= this.count) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        this.toStringCache = null;
        this.value[i2] = c2;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer append(Object obj) {
        this.toStringCache = null;
        super.append(String.valueOf(obj));
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer append(String str) {
        this.toStringCache = null;
        super.append(str);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer append(StringBuffer stringBuffer) {
        this.toStringCache = null;
        super.append(stringBuffer);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer append(AbstractStringBuilder abstractStringBuilder) {
        this.toStringCache = null;
        super.append(abstractStringBuilder);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.Appendable
    public synchronized StringBuffer append(CharSequence charSequence) {
        this.toStringCache = null;
        super.append(charSequence);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.Appendable
    public synchronized StringBuffer append(CharSequence charSequence, int i2, int i3) {
        this.toStringCache = null;
        super.append(charSequence, i2, i3);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer append(char[] cArr) {
        this.toStringCache = null;
        super.append(cArr);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer append(char[] cArr, int i2, int i3) {
        this.toStringCache = null;
        super.append(cArr, i2, i3);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer append(boolean z2) {
        this.toStringCache = null;
        super.append(z2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.Appendable
    public synchronized StringBuffer append(char c2) {
        this.toStringCache = null;
        super.append(c2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer append(int i2) {
        this.toStringCache = null;
        super.append(i2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer appendCodePoint(int i2) {
        this.toStringCache = null;
        super.appendCodePoint(i2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer append(long j2) {
        this.toStringCache = null;
        super.append(j2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer append(float f2) {
        this.toStringCache = null;
        super.append(f2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer append(double d2) {
        this.toStringCache = null;
        super.append(d2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer delete(int i2, int i3) {
        this.toStringCache = null;
        super.delete(i2, i3);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer deleteCharAt(int i2) {
        this.toStringCache = null;
        super.deleteCharAt(i2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer replace(int i2, int i3, String str) {
        this.toStringCache = null;
        super.replace(i2, i3, str);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized String substring(int i2) {
        return substring(i2, this.count);
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.CharSequence
    public synchronized CharSequence subSequence(int i2, int i3) {
        return super.substring(i2, i3);
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized String substring(int i2, int i3) {
        return super.substring(i2, i3);
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer insert(int i2, char[] cArr, int i3, int i4) {
        this.toStringCache = null;
        super.insert(i2, cArr, i3, i4);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer insert(int i2, Object obj) {
        this.toStringCache = null;
        super.insert(i2, String.valueOf(obj));
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer insert(int i2, String str) {
        this.toStringCache = null;
        super.insert(i2, str);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer insert(int i2, char[] cArr) {
        this.toStringCache = null;
        super.insert(i2, cArr);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuffer insert(int i2, CharSequence charSequence) {
        super.insert(i2, charSequence);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer insert(int i2, CharSequence charSequence, int i3, int i4) {
        this.toStringCache = null;
        super.insert(i2, charSequence, i3, i4);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuffer insert(int i2, boolean z2) {
        super.insert(i2, z2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer insert(int i2, char c2) {
        this.toStringCache = null;
        super.insert(i2, c2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuffer insert(int i2, int i3) {
        super.insert(i2, i3);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuffer insert(int i2, long j2) {
        super.insert(i2, j2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuffer insert(int i2, float f2) {
        super.insert(i2, f2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public StringBuffer insert(int i2, double d2) {
        super.insert(i2, d2);
        return this;
    }

    @Override // java.lang.AbstractStringBuilder
    public int indexOf(String str) {
        return super.indexOf(str);
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized int indexOf(String str, int i2) {
        return super.indexOf(str, i2);
    }

    @Override // java.lang.AbstractStringBuilder
    public int lastIndexOf(String str) {
        return lastIndexOf(str, this.count);
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized int lastIndexOf(String str, int i2) {
        return super.lastIndexOf(str, i2);
    }

    @Override // java.lang.AbstractStringBuilder
    public synchronized StringBuffer reverse() {
        this.toStringCache = null;
        super.reverse();
        return this;
    }

    @Override // java.lang.AbstractStringBuilder, java.lang.CharSequence
    public synchronized String toString() {
        if (this.toStringCache == null) {
            this.toStringCache = Arrays.copyOfRange(this.value, 0, this.count);
        }
        return new String(this.toStringCache, true);
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("value", this.value);
        putFieldPutFields.put("count", this.count);
        putFieldPutFields.put("shared", false);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        this.value = (char[]) fields.get("value", (Object) null);
        int i2 = fields.get("count", 0);
        if (i2 < 0 || i2 > this.value.length) {
            throw new StreamCorruptedException("count value invalid");
        }
        this.count = i2;
    }
}
