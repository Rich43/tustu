package sun.management;

/* loaded from: rt.jar:sun/management/DiagnosticCommandArgumentInfo.class */
class DiagnosticCommandArgumentInfo {
    private final String name;
    private final String description;
    private final String type;
    private final String defaultValue;
    private final boolean mandatory;
    private final boolean option;
    private final boolean multiple;
    private final int position;

    String getName() {
        return this.name;
    }

    String getDescription() {
        return this.description;
    }

    String getType() {
        return this.type;
    }

    String getDefault() {
        return this.defaultValue;
    }

    boolean isMandatory() {
        return this.mandatory;
    }

    boolean isOption() {
        return this.option;
    }

    boolean isMultiple() {
        return this.multiple;
    }

    int getPosition() {
        return this.position;
    }

    DiagnosticCommandArgumentInfo(String str, String str2, String str3, String str4, boolean z2, boolean z3, boolean z4, int i2) {
        this.name = str;
        this.description = str2;
        this.type = str3;
        this.defaultValue = str4;
        this.mandatory = z2;
        this.option = z3;
        this.multiple = z4;
        this.position = i2;
    }
}
