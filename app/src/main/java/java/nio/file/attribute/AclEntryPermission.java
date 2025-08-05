package java.nio.file.attribute;

/* loaded from: rt.jar:java/nio/file/attribute/AclEntryPermission.class */
public enum AclEntryPermission {
    READ_DATA,
    WRITE_DATA,
    APPEND_DATA,
    READ_NAMED_ATTRS,
    WRITE_NAMED_ATTRS,
    EXECUTE,
    DELETE_CHILD,
    READ_ATTRIBUTES,
    WRITE_ATTRIBUTES,
    DELETE,
    READ_ACL,
    WRITE_ACL,
    WRITE_OWNER,
    SYNCHRONIZE;

    public static final AclEntryPermission LIST_DIRECTORY = READ_DATA;
    public static final AclEntryPermission ADD_FILE = WRITE_DATA;
    public static final AclEntryPermission ADD_SUBDIRECTORY = APPEND_DATA;
}
