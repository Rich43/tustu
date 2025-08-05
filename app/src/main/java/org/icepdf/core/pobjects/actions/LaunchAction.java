package org.icepdf.core.pobjects.actions;

import com.sun.glass.ui.Platform;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.HashMap;
import org.icepdf.core.pobjects.LiteralStringObject;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/actions/LaunchAction.class */
public class LaunchAction extends Action {
    public static final Name FILE_KEY = new Name(PdfOps.F_TOKEN);
    public static final Name WIN_KEY = new Name(Platform.WINDOWS);
    public static final Name MAC_KEY = new Name(Platform.MAC);
    public static final Name UNIX_KEY = new Name("Unix");
    public static final Name NEW_WINDOW_KEY = new Name("NewWindow");
    private String externalFile;
    private FileSpecification fileSpecification;
    private Boolean isNewWindow;
    private WindowsLaunchParameters winLaunchParameters;

    public LaunchAction(Library l2, HashMap h2) {
        super(l2, h2);
        this.winLaunchParameters = new WindowsLaunchParameters();
    }

    public String getExternalFile() {
        Object value = getObject(FILE_KEY);
        if (value instanceof StringObject) {
            this.externalFile = ((StringObject) value).getDecryptedLiteralString(this.library.getSecurityManager());
        } else if (getFileSpecification() != null) {
            this.externalFile = getFileSpecification().getFileSpecification();
        }
        return this.externalFile;
    }

    public void setExternalFile(String externalFile) {
        StringObject tmp = new LiteralStringObject(externalFile, getPObjectReference(), this.library.securityManager);
        this.entries.put(FILE_KEY, tmp);
        this.externalFile = externalFile;
    }

    public boolean getNewWindow() {
        Object value = getObject(NEW_WINDOW_KEY);
        if (value instanceof Boolean) {
            this.isNewWindow = (Boolean) value;
        }
        return this.isNewWindow.booleanValue();
    }

    public WindowsLaunchParameters getWinLaunchParameters() {
        return this.winLaunchParameters;
    }

    public FileSpecification getFileSpecification() {
        Object value = getObject(FILE_KEY);
        if (value instanceof HashMap) {
            this.fileSpecification = new FileSpecification(this.library, (HashMap) value);
        }
        return this.fileSpecification;
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/actions/LaunchAction$WindowsLaunchParameters.class */
    public class WindowsLaunchParameters {
        private final Name FILE_KEY = new Name(PdfOps.F_TOKEN);
        private final Name DIRECTORY_KEY = new Name(PdfOps.D_TOKEN);
        private final Name OPEN_KEY = new Name("O");
        private final Name PARAMETER_KEY = new Name(Constants._TAG_P);
        private FileSpecification launchFileSpecification;
        private String launchFile;
        private String defaultDirectory;
        private String operation;
        private String parameters;

        public WindowsLaunchParameters() {
            Object value = LaunchAction.this.getObject(this.FILE_KEY);
            if (value instanceof HashMap) {
                this.launchFileSpecification = new FileSpecification(LaunchAction.this.library, (HashMap) value);
            } else if (value instanceof StringObject) {
                this.launchFile = ((StringObject) value).getDecryptedLiteralString(LaunchAction.this.library.getSecurityManager());
            }
            Object value2 = LaunchAction.this.getObject(this.DIRECTORY_KEY);
            if (value2 instanceof StringObject) {
                this.defaultDirectory = ((StringObject) value2).getDecryptedLiteralString(LaunchAction.this.library.getSecurityManager());
            }
            Object value3 = LaunchAction.this.getObject(this.OPEN_KEY);
            if (value3 instanceof StringObject) {
                this.operation = ((StringObject) value3).getDecryptedLiteralString(LaunchAction.this.library.getSecurityManager());
            }
            Object value4 = LaunchAction.this.getObject(this.PARAMETER_KEY);
            if (value4 instanceof StringObject) {
                this.parameters = ((StringObject) value4).getLiteralString();
            }
        }

        public String getLaunchFile() {
            return this.launchFile;
        }

        public String getDefaultDirectory() {
            return this.defaultDirectory;
        }

        public String getOperation() {
            return this.operation;
        }

        public String getParameters() {
            return this.parameters;
        }

        public FileSpecification getLaunchFileSpecification() {
            return this.launchFileSpecification;
        }
    }
}
