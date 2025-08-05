package java.nio.file.attribute;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: rt.jar:java/nio/file/attribute/AclEntry.class */
public final class AclEntry {
    private final AclEntryType type;
    private final UserPrincipal who;
    private final Set<AclEntryPermission> perms;
    private final Set<AclEntryFlag> flags;
    private volatile int hash;

    private AclEntry(AclEntryType aclEntryType, UserPrincipal userPrincipal, Set<AclEntryPermission> set, Set<AclEntryFlag> set2) {
        this.type = aclEntryType;
        this.who = userPrincipal;
        this.perms = set;
        this.flags = set2;
    }

    /* loaded from: rt.jar:java/nio/file/attribute/AclEntry$Builder.class */
    public static final class Builder {
        private AclEntryType type;
        private UserPrincipal who;
        private Set<AclEntryPermission> perms;
        private Set<AclEntryFlag> flags;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !AclEntry.class.desiredAssertionStatus();
        }

        private Builder(AclEntryType aclEntryType, UserPrincipal userPrincipal, Set<AclEntryPermission> set, Set<AclEntryFlag> set2) {
            if (!$assertionsDisabled && (set == null || set2 == null)) {
                throw new AssertionError();
            }
            this.type = aclEntryType;
            this.who = userPrincipal;
            this.perms = set;
            this.flags = set2;
        }

        public AclEntry build() {
            if (this.type == null) {
                throw new IllegalStateException("Missing type component");
            }
            if (this.who == null) {
                throw new IllegalStateException("Missing who component");
            }
            return new AclEntry(this.type, this.who, this.perms, this.flags);
        }

        public Builder setType(AclEntryType aclEntryType) {
            if (aclEntryType == null) {
                throw new NullPointerException();
            }
            this.type = aclEntryType;
            return this;
        }

        public Builder setPrincipal(UserPrincipal userPrincipal) {
            if (userPrincipal == null) {
                throw new NullPointerException();
            }
            this.who = userPrincipal;
            return this;
        }

        private static void checkSet(Set<?> set, Class<?> cls) {
            for (Object obj : set) {
                if (obj == null) {
                    throw new NullPointerException();
                }
                cls.cast(obj);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v7, types: [java.util.Set] */
        public Builder setPermissions(Set<AclEntryPermission> set) {
            EnumSet enumSetCopyOf;
            if (set.isEmpty()) {
                enumSetCopyOf = Collections.emptySet();
            } else {
                enumSetCopyOf = EnumSet.copyOf(set);
                checkSet(enumSetCopyOf, AclEntryPermission.class);
            }
            this.perms = enumSetCopyOf;
            return this;
        }

        public Builder setPermissions(AclEntryPermission... aclEntryPermissionArr) {
            EnumSet enumSetNoneOf = EnumSet.noneOf(AclEntryPermission.class);
            for (AclEntryPermission aclEntryPermission : aclEntryPermissionArr) {
                if (aclEntryPermission == null) {
                    throw new NullPointerException();
                }
                enumSetNoneOf.add(aclEntryPermission);
            }
            this.perms = enumSetNoneOf;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v7, types: [java.util.Set] */
        public Builder setFlags(Set<AclEntryFlag> set) {
            EnumSet enumSetCopyOf;
            if (set.isEmpty()) {
                enumSetCopyOf = Collections.emptySet();
            } else {
                enumSetCopyOf = EnumSet.copyOf(set);
                checkSet(enumSetCopyOf, AclEntryFlag.class);
            }
            this.flags = enumSetCopyOf;
            return this;
        }

        public Builder setFlags(AclEntryFlag... aclEntryFlagArr) {
            EnumSet enumSetNoneOf = EnumSet.noneOf(AclEntryFlag.class);
            for (AclEntryFlag aclEntryFlag : aclEntryFlagArr) {
                if (aclEntryFlag == null) {
                    throw new NullPointerException();
                }
                enumSetNoneOf.add(aclEntryFlag);
            }
            this.flags = enumSetNoneOf;
            return this;
        }
    }

    public static Builder newBuilder() {
        return new Builder(null, null, Collections.emptySet(), Collections.emptySet());
    }

    public static Builder newBuilder(AclEntry aclEntry) {
        return new Builder(aclEntry.type, aclEntry.who, aclEntry.perms, aclEntry.flags);
    }

    public AclEntryType type() {
        return this.type;
    }

    public UserPrincipal principal() {
        return this.who;
    }

    public Set<AclEntryPermission> permissions() {
        return new HashSet(this.perms);
    }

    public Set<AclEntryFlag> flags() {
        return new HashSet(this.flags);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof AclEntry)) {
            return false;
        }
        AclEntry aclEntry = (AclEntry) obj;
        if (this.type != aclEntry.type || !this.who.equals(aclEntry.who) || !this.perms.equals(aclEntry.perms) || !this.flags.equals(aclEntry.flags)) {
            return false;
        }
        return true;
    }

    private static int hash(int i2, Object obj) {
        return (i2 * 127) + obj.hashCode();
    }

    public int hashCode() {
        if (this.hash != 0) {
            return this.hash;
        }
        this.hash = hash(hash(hash(this.type.hashCode(), this.who), this.perms), this.flags);
        return this.hash;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.who.getName());
        sb.append(':');
        Iterator<AclEntryPermission> it = this.perms.iterator();
        while (it.hasNext()) {
            sb.append(it.next().name());
            sb.append('/');
        }
        sb.setLength(sb.length() - 1);
        sb.append(':');
        if (!this.flags.isEmpty()) {
            Iterator<AclEntryFlag> it2 = this.flags.iterator();
            while (it2.hasNext()) {
                sb.append(it2.next().name());
                sb.append('/');
            }
            sb.setLength(sb.length() - 1);
            sb.append(':');
        }
        sb.append(this.type.name());
        return sb.toString();
    }
}
