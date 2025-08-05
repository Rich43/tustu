package org.icepdf.core.pobjects.security;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/security/Permissions.class */
public class Permissions {
    private static final int PRINT_DOCUMENT_BIT_3 = -3900;
    private static final int MODIFY_DOCUMENT_BIT_4 = -3896;
    private static final int DATA_EXTRACTION_BIT_5 = -3888;
    private static final int MODIFY_TEXT_BIT_6 = -3872;
    private static final int MODIFY_FORMS_BIT_9 = -3648;
    private static final int ACCESSIBILITY_BIT_10 = -3392;
    private static final int ASSEMBLE_DOCUMENT_BIT_11 = -2880;
    private static final int PRINT_QUALITY_BIT_12 = -1856;
    public static final int PRINT_DOCUMENT = 0;
    public static final int PRINT_DOCUMENT_QUALITY = 1;
    public static final int MODIFY_DOCUMENT = 2;
    public static final int CONTENT_EXTRACTION = 3;
    public static final int AUTHORING_FORM_FIELDS = 4;
    public static final int FORM_FIELD_FILL_SIGNING = 5;
    public static final int CONTENT_ACCESSABILITY = 6;
    public static final int DOCUMENT_ASSEMBLY = 7;
    private int permissionFlags;
    private int revision;
    private boolean[] permissions = new boolean[10];
    boolean isInit = false;

    public Permissions(EncryptionDictionary dictionary) {
        this.permissionFlags = -3904;
        this.revision = 2;
        this.permissionFlags = dictionary.getPermissions();
        this.revision = dictionary.getRevisionNumber();
    }

    public void init() {
        for (int i2 = 0; i2 < this.permissions.length; i2++) {
            this.permissions[i2] = false;
        }
        if (this.revision == 2) {
            if ((this.permissionFlags & PRINT_DOCUMENT_BIT_3) == PRINT_DOCUMENT_BIT_3) {
                this.permissions[0] = true;
            }
            if ((this.permissionFlags & MODIFY_DOCUMENT_BIT_4) == MODIFY_DOCUMENT_BIT_4) {
                this.permissions[2] = true;
            }
            if ((this.permissionFlags & DATA_EXTRACTION_BIT_5) == DATA_EXTRACTION_BIT_5) {
                this.permissions[3] = true;
            }
            if (this.permissions[3]) {
                this.permissions[4] = true;
            }
            if (this.permissions[3]) {
                this.permissions[5] = true;
            }
            if (this.permissions[3]) {
                this.permissions[6] = true;
            }
            if (this.permissions[2]) {
                this.permissions[7] = true;
            }
            if ((this.permissionFlags & PRINT_QUALITY_BIT_12) == PRINT_QUALITY_BIT_12) {
                this.permissions[1] = true;
            }
            this.isInit = true;
            return;
        }
        if (this.revision >= 3) {
            if ((this.permissionFlags & PRINT_DOCUMENT_BIT_3) == PRINT_DOCUMENT_BIT_3) {
                this.permissions[0] = true;
            }
            if ((this.permissionFlags & MODIFY_DOCUMENT_BIT_4) == MODIFY_DOCUMENT_BIT_4) {
                this.permissions[2] = true;
            }
            if ((this.permissionFlags & DATA_EXTRACTION_BIT_5) == DATA_EXTRACTION_BIT_5) {
                this.permissions[3] = true;
            }
            if ((this.permissionFlags & MODIFY_TEXT_BIT_6) == MODIFY_TEXT_BIT_6) {
                this.permissions[4] = true;
            }
            if ((this.permissionFlags & MODIFY_FORMS_BIT_9) == MODIFY_FORMS_BIT_9) {
                this.permissions[5] = true;
            }
            if ((this.permissionFlags & ACCESSIBILITY_BIT_10) == ACCESSIBILITY_BIT_10) {
                this.permissions[6] = true;
            }
            if ((this.permissionFlags & ASSEMBLE_DOCUMENT_BIT_11) == ASSEMBLE_DOCUMENT_BIT_11) {
                this.permissions[7] = true;
            }
            if ((this.permissionFlags & PRINT_QUALITY_BIT_12) == PRINT_QUALITY_BIT_12) {
                this.permissions[1] = true;
            }
            this.isInit = true;
        }
    }

    public boolean getPermissions(int permissionIndex) {
        if (!this.isInit) {
            init();
        }
        return permissionIndex >= 0 && permissionIndex <= this.permissions.length && this.permissions[permissionIndex];
    }
}
