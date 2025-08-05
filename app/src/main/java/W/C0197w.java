package W;

import bH.C1005m;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import jdk.internal.dynalink.CallSiteDescriptor;

/* renamed from: W.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/w.class */
public class C0197w {
    public static R a(String str) {
        if (str.toLowerCase().startsWith("speeduino")) {
            return b(str);
        }
        if (str.toLowerCase().startsWith("rusefi")) {
            return c(str);
        }
        R r2 = new R();
        try {
            String strA = C1005m.a("http://www.shadowtuner.com/ShadowTuner/FindEcuDefinitionBySignature?action=checkAvailability&signature=" + bH.W.b(str, " ", "%20").trim() + "&key=" + (((System.currentTimeMillis() - 1000000) ^ (-1)) + ""));
            if (strA == null || !strA.startsWith("available=true")) {
                r2.a(false);
                r2.a("File not currently available on EFI Analytics servers.");
            } else {
                r2.a(Long.parseLong(strA.substring(strA.lastIndexOf(61) + 1)));
                r2.a(true);
                r2.a("File Available for signature " + str);
            }
            return r2;
        } catch (IOException e2) {
            r2.a("Unable to get ECU Definition for signature: " + str + ", service unavailable.");
            return r2;
        }
    }

    public static R a(String str, double d2) {
        R r2 = new R();
        if (str.toLowerCase().startsWith("speeduino")) {
            r2.a(false);
            r2.a("Newer iniVersion not currently supported by speeduino server.");
            return r2;
        }
        if (str.toLowerCase().startsWith("rusefi")) {
            r2.a(false);
            r2.a("Newer iniVersion not currently supported by rusEFI server.");
            return r2;
        }
        try {
            String strA = C1005m.a("http://www.shadowtuner.com/ShadowTuner/FindEcuDefinitionBySignature?action=checkForNewer&signature=" + bH.W.b(str, " ", "%20").trim() + "&iniVersion=" + d2 + "&key=" + (((System.currentTimeMillis() - 1000000) ^ (-1)) + ""));
            if (strA == null || !strA.startsWith("available=true")) {
                r2.a(false);
                r2.a("Newer File not currently available on EFI Analytics servers.");
            } else {
                r2.a(Long.parseLong(strA.substring(strA.indexOf("size=") + 5, strA.indexOf(CallSiteDescriptor.OPERATOR_DELIMITER, strA.indexOf("size=")))));
                String strSubstring = strA.substring(strA.indexOf("iniVersion=") + 11);
                if (strSubstring.indexOf(CallSiteDescriptor.OPERATOR_DELIMITER) != -1) {
                    strSubstring = strSubstring.substring(0, strSubstring.indexOf(CallSiteDescriptor.OPERATOR_DELIMITER));
                }
                try {
                    r2.a(Double.parseDouble(strSubstring));
                } catch (Exception e2) {
                    bH.C.c("Malformed iniVersion: " + strSubstring);
                }
                r2.a(true);
                r2.a("Newer File Available for signature " + str);
            }
            return r2;
        } catch (IOException e3) {
            r2.a("Unable to get ECU Definition for signature: " + str + ", service unavailable.");
            return r2;
        }
    }

    private static R b(String str) {
        String str2 = "https://speeduino.com/fw/" + str.toLowerCase().replace("speeduino", "").trim() + ".ini";
        R r2 = new R();
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(str2).openConnection();
                httpURLConnection2.setRequestMethod("HEAD");
                if (httpURLConnection2.getResponseCode() == 200) {
                    r2.a(httpURLConnection2.getContentLength());
                    r2.a(true);
                    r2.a("File Available for signature " + str);
                } else {
                    r2.a("File not currently available on Speeduino servers.");
                }
                if (httpURLConnection2 != null) {
                    try {
                        httpURLConnection2.disconnect();
                    } catch (Exception e2) {
                    }
                }
                return r2;
            } catch (Exception e3) {
                bH.C.c("Failed to get ini: " + e3.getMessage());
                r2.a("Unable to get ECU Definition for signature: " + str + ", service unavailable.");
                if (0 != 0) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (Exception e4) {
                    }
                }
                return r2;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    httpURLConnection.disconnect();
                } catch (Exception e5) {
                }
            }
            throw th;
        }
    }

    private static R c(String str) {
        String str2 = "https://rusefi.com/online/ini/" + bH.W.b(str.toLowerCase(), " ", "/").trim().replace(".", "/").trim() + ".ini";
        R r2 = new R();
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(str2).openConnection();
                httpURLConnection2.setRequestMethod("HEAD");
                if (httpURLConnection2.getResponseCode() == 200) {
                    r2.a(httpURLConnection2.getContentLength());
                    r2.a(true);
                    r2.a("File Available for signature " + str);
                } else {
                    r2.a("File not currently available on Speeduino servers.");
                }
                if (httpURLConnection2 != null) {
                    try {
                        httpURLConnection2.disconnect();
                    } catch (Exception e2) {
                    }
                }
                return r2;
            } catch (Exception e3) {
                bH.C.c("Failed to get ini: " + e3.getMessage());
                r2.a("Unable to get ECU Definition for signature: " + str + ", service unavailable.");
                if (0 != 0) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (Exception e4) {
                    }
                }
                return r2;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    httpURLConnection.disconnect();
                } catch (Exception e5) {
                }
            }
            throw th;
        }
    }

    public static R a(String str, File file) {
        if (str.toLowerCase().startsWith("speeduino")) {
            return b(str, file);
        }
        if (str.toLowerCase().startsWith("rusefi")) {
            return c(str, file);
        }
        R r2 = new R();
        try {
            String strB = C0200z.b(str);
            String strTrim = bH.W.b(str, " ", "%20").trim();
            String strA = C1005m.a("http://www.shadowtuner.com/ShadowTuner/FindEcuDefinitionBySignature?action=checkAvailability&signature=" + strTrim);
            if (strA == null || !strA.startsWith("available=true")) {
                if (strA == null || !strA.startsWith("available=false")) {
                    return r2;
                }
                r2.a("File for signature " + bH.W.b(strTrim, "%20", " ") + " not available on server, contact your Firmware provider.");
                return r2;
            }
            r2.a(Long.parseLong(strA.substring(strA.lastIndexOf(61) + 1)));
            File file2 = new File(file, strB);
            C1005m.a("http://www.shadowtuner.com/ShadowTuner/FindEcuDefinitionBySignature?signature=" + strTrim, file2.getAbsolutePath());
            if (file2.length() >= r2.b() - 20) {
                r2.a(true);
                r2.a(file2);
            } else {
                r2.a("File Available, but download failed.");
            }
            return r2;
        } catch (IOException e2) {
            bH.C.d("Unable to get ECU Definition for signature: " + str + ", service unavailable.");
            r2.a("Unable to get ECU Definition for signature: " + str + ", service unavailable.");
            return r2;
        } catch (Exception e3) {
            bH.C.d("Unexpected error downloading file for signature: " + str + ", service unavailable.");
            r2.a("Unexpected error downloading file.");
            return r2;
        }
    }

    private static R b(String str, File file) {
        String str2 = "https://speeduino.com/fw/" + str.toLowerCase().replace("speeduino", "").trim() + ".ini";
        R r2 = new R();
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(str2).openConnection();
                httpURLConnection2.setRequestMethod("HEAD");
                if (httpURLConnection2.getResponseCode() == 200) {
                    r2.a(httpURLConnection2.getContentLength());
                    r2.a(true);
                    r2.a((String) null);
                    File file2 = new File(file, C0200z.b(str));
                    C1005m.a(str2, file2.getAbsolutePath());
                    r2.a(file2);
                } else {
                    r2.a("File not currently available on Speeduino servers.");
                }
                if (httpURLConnection2 != null) {
                    try {
                        httpURLConnection2.disconnect();
                    } catch (Exception e2) {
                    }
                }
                return r2;
            } catch (Exception e3) {
                bH.C.c("Failed to get ini: " + e3.getMessage());
                r2.a("Unable to get ECU Definition for signature: " + str + ", service unavailable.");
                if (0 != 0) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (Exception e4) {
                    }
                }
                return r2;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    httpURLConnection.disconnect();
                } catch (Exception e5) {
                }
            }
            throw th;
        }
    }

    private static R c(String str, File file) {
        String str2 = "https://rusefi.com/online/ini/" + bH.W.b(str.toLowerCase(), " ", "/").trim().replace(".", "/").trim() + ".ini";
        R r2 = new R();
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(str2).openConnection();
                httpURLConnection2.setRequestMethod("HEAD");
                if (httpURLConnection2.getResponseCode() == 200) {
                    r2.a(httpURLConnection2.getContentLength());
                    r2.a(true);
                    r2.a((String) null);
                    File file2 = new File(file, C0200z.b(str));
                    C1005m.a(str2, file2.getAbsolutePath());
                    r2.a(file2);
                } else {
                    r2.a("File not currently available on rusEFI servers.");
                }
                if (httpURLConnection2 != null) {
                    try {
                        httpURLConnection2.disconnect();
                    } catch (Exception e2) {
                    }
                }
                return r2;
            } catch (Exception e3) {
                bH.C.c("Failed to get ini: " + e3.getMessage());
                r2.a("Unable to get ECU Definition for signature: " + str + ", service unavailable.");
                if (0 != 0) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (Exception e4) {
                    }
                }
                return r2;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    httpURLConnection.disconnect();
                } catch (Exception e5) {
                }
            }
            throw th;
        }
    }

    public static void a(String str, double d2, File file) {
        new C0198x(str, d2, file).start();
    }
}
