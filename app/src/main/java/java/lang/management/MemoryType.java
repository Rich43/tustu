package java.lang.management;

/* loaded from: rt.jar:java/lang/management/MemoryType.class */
public enum MemoryType {
    HEAP("Heap memory"),
    NON_HEAP("Non-heap memory");

    private final String description;
    private static final long serialVersionUID = 6992337162326171013L;

    MemoryType(String str) {
        this.description = str;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.description;
    }
}
