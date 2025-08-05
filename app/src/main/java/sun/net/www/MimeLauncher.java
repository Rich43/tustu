package sun.net.www;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/net/www/MimeLauncher.class */
class MimeLauncher extends Thread {
    java.net.URLConnection uc;

    /* renamed from: m, reason: collision with root package name */
    MimeEntry f13584m;
    String genericTempFileTemplate;
    InputStream is;
    String execPath;

    MimeLauncher(MimeEntry mimeEntry, java.net.URLConnection uRLConnection, InputStream inputStream, String str, String str2) throws ApplicationLaunchException {
        String strSubstring;
        super(str2);
        this.f13584m = mimeEntry;
        this.uc = uRLConnection;
        this.is = inputStream;
        this.genericTempFileTemplate = str;
        String launchString = this.f13584m.getLaunchString();
        if (!findExecutablePath(launchString)) {
            int iIndexOf = launchString.indexOf(32);
            if (iIndexOf != -1) {
                strSubstring = launchString.substring(0, iIndexOf);
            } else {
                strSubstring = launchString;
            }
            throw new ApplicationLaunchException(strSubstring);
        }
    }

    protected String getTempFileName(URL url, String str) {
        int iLastIndexOf = str.lastIndexOf("%s");
        String strSubstring = str.substring(0, iLastIndexOf);
        String strSubstring2 = "";
        if (iLastIndexOf < str.length() - 2) {
            strSubstring2 = str.substring(iLastIndexOf + 2);
        }
        long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
        while (true) {
            int iIndexOf = strSubstring.indexOf("%s");
            if (iIndexOf < 0) {
                break;
            }
            strSubstring = strSubstring.substring(0, iIndexOf) + jCurrentTimeMillis + strSubstring.substring(iIndexOf + 2);
        }
        String file = url.getFile();
        String strSubstring3 = "";
        int iLastIndexOf2 = file.lastIndexOf(46);
        if (iLastIndexOf2 >= 0 && iLastIndexOf2 > file.lastIndexOf(47)) {
            strSubstring3 = file.substring(iLastIndexOf2);
        }
        return strSubstring + ("HJ" + url.hashCode()) + jCurrentTimeMillis + strSubstring3 + strSubstring2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            String tempFileTemplate = this.f13584m.getTempFileTemplate();
            if (tempFileTemplate == null) {
                tempFileTemplate = this.genericTempFileTemplate;
            }
            String tempFileName = getTempFileName(this.uc.getURL(), tempFileTemplate);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(tempFileName);
                byte[] bArr = new byte[2048];
                while (true) {
                    try {
                        int i2 = this.is.read(bArr);
                        if (i2 < 0) {
                            break;
                        } else {
                            fileOutputStream.write(bArr, 0, i2);
                        }
                    } catch (IOException e2) {
                        fileOutputStream.close();
                        this.is.close();
                    } catch (Throwable th) {
                        fileOutputStream.close();
                        this.is.close();
                        throw th;
                    }
                }
                fileOutputStream.close();
                this.is.close();
            } catch (IOException e3) {
            }
            String str = this.execPath;
            while (true) {
                int iIndexOf = str.indexOf("%t");
                if (iIndexOf < 0) {
                    break;
                } else {
                    str = str.substring(0, iIndexOf) + this.uc.getContentType() + str.substring(iIndexOf + 2);
                }
            }
            boolean z2 = false;
            while (true) {
                int iIndexOf2 = str.indexOf("%s");
                if (iIndexOf2 < 0) {
                    break;
                }
                str = str.substring(0, iIndexOf2) + tempFileName + str.substring(iIndexOf2 + 2);
                z2 = true;
            }
            if (!z2) {
                str = str + " <" + tempFileName;
            }
            Runtime.getRuntime().exec(str);
        } catch (IOException e4) {
        }
    }

    private boolean findExecutablePath(String str) {
        String strSubstring;
        if (str == null || str.length() == 0) {
            return false;
        }
        int iIndexOf = str.indexOf(32);
        if (iIndexOf != -1) {
            strSubstring = str.substring(0, iIndexOf);
        } else {
            strSubstring = str;
        }
        if (new File(strSubstring).isFile()) {
            this.execPath = str;
            return true;
        }
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("exec.path"));
        if (str2 == null) {
            return false;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str2, CallSiteDescriptor.OPERATOR_DELIMITER);
        while (stringTokenizer.hasMoreElements()) {
            String str3 = (String) stringTokenizer.nextElement2();
            if (new File(str3 + File.separator + strSubstring).isFile()) {
                this.execPath = str3 + File.separator + str;
                return true;
            }
        }
        return false;
    }
}
