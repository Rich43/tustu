package java.nio.file.attribute;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: rt.jar:java/nio/file/attribute/PosixFilePermissions.class */
public final class PosixFilePermissions {
    private PosixFilePermissions() {
    }

    private static void writeBits(StringBuilder sb, boolean z2, boolean z3, boolean z4) {
        if (z2) {
            sb.append('r');
        } else {
            sb.append('-');
        }
        if (z3) {
            sb.append('w');
        } else {
            sb.append('-');
        }
        if (z4) {
            sb.append('x');
        } else {
            sb.append('-');
        }
    }

    public static String toString(Set<PosixFilePermission> set) {
        StringBuilder sb = new StringBuilder(9);
        writeBits(sb, set.contains(PosixFilePermission.OWNER_READ), set.contains(PosixFilePermission.OWNER_WRITE), set.contains(PosixFilePermission.OWNER_EXECUTE));
        writeBits(sb, set.contains(PosixFilePermission.GROUP_READ), set.contains(PosixFilePermission.GROUP_WRITE), set.contains(PosixFilePermission.GROUP_EXECUTE));
        writeBits(sb, set.contains(PosixFilePermission.OTHERS_READ), set.contains(PosixFilePermission.OTHERS_WRITE), set.contains(PosixFilePermission.OTHERS_EXECUTE));
        return sb.toString();
    }

    private static boolean isSet(char c2, char c3) {
        if (c2 == c3) {
            return true;
        }
        if (c2 == '-') {
            return false;
        }
        throw new IllegalArgumentException("Invalid mode");
    }

    private static boolean isR(char c2) {
        return isSet(c2, 'r');
    }

    private static boolean isW(char c2) {
        return isSet(c2, 'w');
    }

    private static boolean isX(char c2) {
        return isSet(c2, 'x');
    }

    public static Set<PosixFilePermission> fromString(String str) {
        if (str.length() != 9) {
            throw new IllegalArgumentException("Invalid mode");
        }
        EnumSet enumSetNoneOf = EnumSet.noneOf(PosixFilePermission.class);
        if (isR(str.charAt(0))) {
            enumSetNoneOf.add(PosixFilePermission.OWNER_READ);
        }
        if (isW(str.charAt(1))) {
            enumSetNoneOf.add(PosixFilePermission.OWNER_WRITE);
        }
        if (isX(str.charAt(2))) {
            enumSetNoneOf.add(PosixFilePermission.OWNER_EXECUTE);
        }
        if (isR(str.charAt(3))) {
            enumSetNoneOf.add(PosixFilePermission.GROUP_READ);
        }
        if (isW(str.charAt(4))) {
            enumSetNoneOf.add(PosixFilePermission.GROUP_WRITE);
        }
        if (isX(str.charAt(5))) {
            enumSetNoneOf.add(PosixFilePermission.GROUP_EXECUTE);
        }
        if (isR(str.charAt(6))) {
            enumSetNoneOf.add(PosixFilePermission.OTHERS_READ);
        }
        if (isW(str.charAt(7))) {
            enumSetNoneOf.add(PosixFilePermission.OTHERS_WRITE);
        }
        if (isX(str.charAt(8))) {
            enumSetNoneOf.add(PosixFilePermission.OTHERS_EXECUTE);
        }
        return enumSetNoneOf;
    }

    public static FileAttribute<Set<PosixFilePermission>> asFileAttribute(Set<PosixFilePermission> set) {
        final HashSet hashSet = new HashSet(set);
        Iterator<E> it = hashSet.iterator();
        while (it.hasNext()) {
            if (((PosixFilePermission) it.next()) == null) {
                throw new NullPointerException();
            }
        }
        return new FileAttribute<Set<PosixFilePermission>>() { // from class: java.nio.file.attribute.PosixFilePermissions.1
            @Override // java.nio.file.attribute.FileAttribute
            public String name() {
                return "posix:permissions";
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.nio.file.attribute.FileAttribute
            public Set<PosixFilePermission> value() {
                return Collections.unmodifiableSet(hashSet);
            }
        };
    }
}
