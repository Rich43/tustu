package java.text;

/* loaded from: rt.jar:java/text/RuleBasedCollationKey.class */
final class RuleBasedCollationKey extends CollationKey {
    private String key;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.text.CollationKey, java.lang.Comparable
    public int compareTo(CollationKey collationKey) {
        int iCompareTo = this.key.compareTo(((RuleBasedCollationKey) collationKey).key);
        if (iCompareTo <= -1) {
            return -1;
        }
        if (iCompareTo >= 1) {
            return 1;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        return this.key.equals(((RuleBasedCollationKey) obj).key);
    }

    public int hashCode() {
        return this.key.hashCode();
    }

    @Override // java.text.CollationKey
    public byte[] toByteArray() {
        char[] charArray = this.key.toCharArray();
        byte[] bArr = new byte[2 * charArray.length];
        int i2 = 0;
        for (int i3 = 0; i3 < charArray.length; i3++) {
            int i4 = i2;
            int i5 = i2 + 1;
            bArr[i4] = (byte) (charArray[i3] >>> '\b');
            i2 = i5 + 1;
            bArr[i5] = (byte) (charArray[i3] & 255);
        }
        return bArr;
    }

    RuleBasedCollationKey(String str, String str2) {
        super(str);
        this.key = null;
        this.key = str2;
    }
}
