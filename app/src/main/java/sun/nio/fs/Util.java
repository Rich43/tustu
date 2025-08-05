package sun.nio.fs;

import java.nio.charset.Charset;
import java.nio.file.LinkOption;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Set;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/nio/fs/Util.class */
class Util {
    private static final Charset jnuEncoding = Charset.forName((String) AccessController.doPrivileged(new GetPropertyAction("sun.jnu.encoding")));

    private Util() {
    }

    static Charset jnuEncoding() {
        return jnuEncoding;
    }

    static byte[] toBytes(String str) {
        return str.getBytes(jnuEncoding);
    }

    static String toString(byte[] bArr) {
        return new String(bArr, jnuEncoding);
    }

    static String[] split(String str, char c2) {
        int i2 = 0;
        for (int i3 = 0; i3 < str.length(); i3++) {
            if (str.charAt(i3) == c2) {
                i2++;
            }
        }
        String[] strArr = new String[i2 + 1];
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < str.length(); i6++) {
            if (str.charAt(i6) == c2) {
                int i7 = i4;
                i4++;
                strArr[i7] = str.substring(i5, i6);
                i5 = i6 + 1;
            }
        }
        strArr[i4] = str.substring(i5, str.length());
        return strArr;
    }

    @SafeVarargs
    static <E> Set<E> newSet(E... eArr) {
        HashSet hashSet = new HashSet();
        for (E e2 : eArr) {
            hashSet.add(e2);
        }
        return hashSet;
    }

    @SafeVarargs
    static <E> Set<E> newSet(Set<E> set, E... eArr) {
        HashSet hashSet = new HashSet(set);
        for (E e2 : eArr) {
            hashSet.add(e2);
        }
        return hashSet;
    }

    static boolean followLinks(LinkOption... linkOptionArr) {
        boolean z2 = true;
        for (LinkOption linkOption : linkOptionArr) {
            if (linkOption == LinkOption.NOFOLLOW_LINKS) {
                z2 = false;
            } else {
                if (linkOption == null) {
                    throw new NullPointerException();
                }
                throw new AssertionError((Object) "Should not get here");
            }
        }
        return z2;
    }
}
