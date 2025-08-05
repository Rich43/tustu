package sun.management;

import java.util.List;

/* loaded from: rt.jar:sun/management/DiagnosticCommandInfo.class */
class DiagnosticCommandInfo {
    private final String name;
    private final String description;
    private final String impact;
    private final String permissionClass;
    private final String permissionName;
    private final String permissionAction;
    private final boolean enabled;
    private final List<DiagnosticCommandArgumentInfo> arguments;

    String getName() {
        return this.name;
    }

    String getDescription() {
        return this.description;
    }

    String getImpact() {
        return this.impact;
    }

    String getPermissionClass() {
        return this.permissionClass;
    }

    String getPermissionName() {
        return this.permissionName;
    }

    String getPermissionAction() {
        return this.permissionAction;
    }

    boolean isEnabled() {
        return this.enabled;
    }

    List<DiagnosticCommandArgumentInfo> getArgumentsInfo() {
        return this.arguments;
    }

    DiagnosticCommandInfo(String str, String str2, String str3, String str4, String str5, String str6, boolean z2, List<DiagnosticCommandArgumentInfo> list) {
        this.name = str;
        this.description = str2;
        this.impact = str3;
        this.permissionClass = str4;
        this.permissionName = str5;
        this.permissionAction = str6;
        this.enabled = z2;
        this.arguments = list;
    }
}
