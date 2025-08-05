package sun.net.www;

import java.io.File;
import java.io.InputStream;
import java.util.StringTokenizer;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:sun/net/www/MimeEntry.class */
public class MimeEntry implements Cloneable {
    private String typeName;
    private String tempFileNameTemplate;
    private int action;
    private String command;
    private String description;
    private String imageFileName;
    private String[] fileExtensions;
    boolean starred;
    public static final int UNKNOWN = 0;
    public static final int LOAD_INTO_BROWSER = 1;
    public static final int SAVE_TO_FILE = 2;
    public static final int LAUNCH_APPLICATION = 3;
    static final String[] actionKeywords = {"unknown", "browser", "save", "application"};

    public MimeEntry(String str) {
        this(str, 0, null, null, null);
    }

    MimeEntry(String str, String str2, String str3) {
        this.typeName = str.toLowerCase();
        this.action = 0;
        this.command = null;
        this.imageFileName = str2;
        setExtensions(str3);
        this.starred = isStarred(this.typeName);
    }

    MimeEntry(String str, int i2, String str2, String str3) {
        this.typeName = str.toLowerCase();
        this.action = i2;
        this.command = str2;
        this.imageFileName = null;
        this.fileExtensions = null;
        this.tempFileNameTemplate = str3;
    }

    MimeEntry(String str, int i2, String str2, String str3, String[] strArr) {
        this.typeName = str.toLowerCase();
        this.action = i2;
        this.command = str2;
        this.imageFileName = str3;
        this.fileExtensions = strArr;
        this.starred = isStarred(str);
    }

    public synchronized String getType() {
        return this.typeName;
    }

    public synchronized void setType(String str) {
        this.typeName = str.toLowerCase();
    }

    public synchronized int getAction() {
        return this.action;
    }

    public synchronized void setAction(int i2, String str) {
        this.action = i2;
        this.command = str;
    }

    public synchronized void setAction(int i2) {
        this.action = i2;
    }

    public synchronized String getLaunchString() {
        return this.command;
    }

    public synchronized void setCommand(String str) {
        this.command = str;
    }

    public synchronized String getDescription() {
        return this.description != null ? this.description : this.typeName;
    }

    public synchronized void setDescription(String str) {
        this.description = str;
    }

    public String getImageFileName() {
        return this.imageFileName;
    }

    public synchronized void setImageFileName(String str) {
        if (new File(str).getParent() == null) {
            this.imageFileName = System.getProperty("java.net.ftp.imagepath." + str);
        } else {
            this.imageFileName = str;
        }
        if (str.lastIndexOf(46) < 0) {
            this.imageFileName += ".gif";
        }
    }

    public String getTempFileTemplate() {
        return this.tempFileNameTemplate;
    }

    public synchronized String[] getExtensions() {
        return this.fileExtensions;
    }

    public synchronized String getExtensionsAsList() {
        String str = "";
        if (this.fileExtensions != null) {
            for (int i2 = 0; i2 < this.fileExtensions.length; i2++) {
                str = str + this.fileExtensions[i2];
                if (i2 < this.fileExtensions.length - 1) {
                    str = str + ",";
                }
            }
        }
        return str;
    }

    public synchronized void setExtensions(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        int iCountTokens = stringTokenizer.countTokens();
        String[] strArr = new String[iCountTokens];
        for (int i2 = 0; i2 < iCountTokens; i2++) {
            strArr[i2] = ((String) stringTokenizer.nextElement2()).trim();
        }
        this.fileExtensions = strArr;
    }

    private boolean isStarred(String str) {
        return str != null && str.length() > 0 && str.endsWith("/*");
    }

    /* JADX WARN: Unreachable blocks removed: 3, instructions: 10 */
    public Object launch(java.net.URLConnection uRLConnection, InputStream inputStream, MimeTable mimeTable) throws ApplicationLaunchException {
        switch (this.action) {
            case 0:
                return null;
            case 1:
                try {
                    return uRLConnection.getContent();
                } catch (Exception e2) {
                    return null;
                }
            case 2:
                return inputStream;
            case 3:
                String strSubstring = this.command;
                int iIndexOf = strSubstring.indexOf(32);
                if (iIndexOf > 0) {
                    strSubstring = strSubstring.substring(0, iIndexOf);
                }
                return new MimeLauncher(this, uRLConnection, inputStream, mimeTable.getTempFileTemplate(), strSubstring);
            default:
                return null;
        }
    }

    public boolean matches(String str) {
        if (this.starred) {
            return str.startsWith(this.typeName);
        }
        return str.equals(this.typeName);
    }

    public Object clone() {
        MimeEntry mimeEntry = new MimeEntry(this.typeName);
        mimeEntry.action = this.action;
        mimeEntry.command = this.command;
        mimeEntry.description = this.description;
        mimeEntry.imageFileName = this.imageFileName;
        mimeEntry.tempFileNameTemplate = this.tempFileNameTemplate;
        mimeEntry.fileExtensions = this.fileExtensions;
        return mimeEntry;
    }

    public synchronized String toProperty() {
        StringBuffer stringBuffer = new StringBuffer();
        boolean z2 = false;
        int action = getAction();
        if (action != 0) {
            stringBuffer.append("action=" + actionKeywords[action]);
            z2 = true;
        }
        String launchString = getLaunchString();
        if (launchString != null && launchString.length() > 0) {
            if (z2) {
                stringBuffer.append(VectorFormat.DEFAULT_SEPARATOR);
            }
            stringBuffer.append("application=" + launchString);
            z2 = true;
        }
        if (getImageFileName() != null) {
            if (z2) {
                stringBuffer.append(VectorFormat.DEFAULT_SEPARATOR);
            }
            stringBuffer.append("icon=" + getImageFileName());
            z2 = true;
        }
        String extensionsAsList = getExtensionsAsList();
        if (extensionsAsList.length() > 0) {
            if (z2) {
                stringBuffer.append(VectorFormat.DEFAULT_SEPARATOR);
            }
            stringBuffer.append("file_extensions=" + extensionsAsList);
            z2 = true;
        }
        String description = getDescription();
        if (description != null && !description.equals(getType())) {
            if (z2) {
                stringBuffer.append(VectorFormat.DEFAULT_SEPARATOR);
            }
            stringBuffer.append("description=" + description);
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return "MimeEntry[contentType=" + this.typeName + ", image=" + this.imageFileName + ", action=" + this.action + ", command=" + this.command + ", extensions=" + getExtensionsAsList() + "]";
    }
}
