package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/Handle.class */
public final class Handle {
    final int tag;
    final String owner;
    final String name;
    final String desc;

    public Handle(int i2, String str, String str2, String str3) {
        this.tag = i2;
        this.owner = str;
        this.name = str2;
        this.desc = str3;
    }

    public int getTag() {
        return this.tag;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Handle)) {
            return false;
        }
        Handle handle = (Handle) obj;
        return this.tag == handle.tag && this.owner.equals(handle.owner) && this.name.equals(handle.name) && this.desc.equals(handle.desc);
    }

    public int hashCode() {
        return this.tag + (this.owner.hashCode() * this.name.hashCode() * this.desc.hashCode());
    }

    public String toString() {
        return this.owner + '.' + this.name + this.desc + " (" + this.tag + ')';
    }
}
