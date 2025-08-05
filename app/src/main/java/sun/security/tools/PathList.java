package sun.security.tools;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

/* loaded from: rt.jar:sun/security/tools/PathList.class */
public class PathList {
    public static String appendPath(String str, String str2) {
        if (str == null || str.length() == 0) {
            return str2;
        }
        if (str2 == null || str2.length() == 0) {
            return str;
        }
        return str + File.pathSeparator + str2;
    }

    public static URL[] pathToURLs(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, File.pathSeparator);
        URL[] urlArr = new URL[stringTokenizer.countTokens()];
        int i2 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            URL urlFileToURL = fileToURL(new File(stringTokenizer.nextToken()));
            if (urlFileToURL != null) {
                int i3 = i2;
                i2++;
                urlArr[i3] = urlFileToURL;
            }
        }
        if (urlArr.length != i2) {
            URL[] urlArr2 = new URL[i2];
            System.arraycopy(urlArr, 0, urlArr2, 0, i2);
            urlArr = urlArr2;
        }
        return urlArr;
    }

    private static URL fileToURL(File file) {
        String absolutePath;
        try {
            absolutePath = file.getCanonicalPath();
        } catch (IOException e2) {
            absolutePath = file.getAbsolutePath();
        }
        String strReplace = absolutePath.replace(File.separatorChar, '/');
        if (!strReplace.startsWith("/")) {
            strReplace = "/" + strReplace;
        }
        if (!file.isFile()) {
            strReplace = strReplace + "/";
        }
        try {
            return new URL(DeploymentDescriptorParser.ATTR_FILE, "", strReplace);
        } catch (MalformedURLException e3) {
            throw new IllegalArgumentException(DeploymentDescriptorParser.ATTR_FILE);
        }
    }
}
