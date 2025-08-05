package javax.management.loading;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/* loaded from: rt.jar:javax/management/loading/MLetParser.class */
class MLetParser {

    /* renamed from: c, reason: collision with root package name */
    private int f12780c;
    private static String tag = "mlet";

    public void skipSpace(Reader reader) throws IOException {
        while (this.f12780c >= 0) {
            if (this.f12780c == 32 || this.f12780c == 9 || this.f12780c == 10 || this.f12780c == 13) {
                this.f12780c = reader.read();
            } else {
                return;
            }
        }
    }

    public String scanIdentifier(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            if ((this.f12780c >= 97 && this.f12780c <= 122) || ((this.f12780c >= 65 && this.f12780c <= 90) || ((this.f12780c >= 48 && this.f12780c <= 57) || this.f12780c == 95))) {
                sb.append((char) this.f12780c);
                this.f12780c = reader.read();
            } else {
                return sb.toString();
            }
        }
    }

    public Map<String, String> scanTag(Reader reader) throws IOException {
        HashMap map = new HashMap();
        skipSpace(reader);
        while (this.f12780c >= 0 && this.f12780c != 62) {
            if (this.f12780c == 60) {
                throw new IOException("Missing '>' in tag");
            }
            String strScanIdentifier = scanIdentifier(reader);
            String string = "";
            skipSpace(reader);
            if (this.f12780c == 61) {
                int i2 = -1;
                this.f12780c = reader.read();
                skipSpace(reader);
                if (this.f12780c == 39 || this.f12780c == 34) {
                    i2 = this.f12780c;
                    this.f12780c = reader.read();
                }
                StringBuilder sb = new StringBuilder();
                while (this.f12780c > 0 && ((i2 < 0 && this.f12780c != 32 && this.f12780c != 9 && this.f12780c != 10 && this.f12780c != 13 && this.f12780c != 62) || (i2 >= 0 && this.f12780c != i2))) {
                    sb.append((char) this.f12780c);
                    this.f12780c = reader.read();
                }
                if (this.f12780c == i2) {
                    this.f12780c = reader.read();
                }
                skipSpace(reader);
                string = sb.toString();
            }
            map.put(strScanIdentifier.toLowerCase(), string);
            skipSpace(reader);
        }
        return map;
    }

    public List<MLetContent> parse(URL url) throws IOException {
        URLConnection uRLConnectionOpenConnection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRLConnectionOpenConnection.getInputStream(), "UTF-8"));
        URL url2 = uRLConnectionOpenConnection.getURL();
        ArrayList arrayList = new ArrayList();
        Map<String, String> mapScanTag = null;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        while (true) {
            this.f12780c = bufferedReader.read();
            if (this.f12780c != -1) {
                if (this.f12780c == 60) {
                    this.f12780c = bufferedReader.read();
                    if (this.f12780c == 47) {
                        this.f12780c = bufferedReader.read();
                        String strScanIdentifier = scanIdentifier(bufferedReader);
                        if (this.f12780c != 62) {
                            throw new IOException("Missing '>' in tag");
                        }
                        if (strScanIdentifier.equalsIgnoreCase(tag)) {
                            if (mapScanTag != null) {
                                arrayList.add(new MLetContent(url2, mapScanTag, arrayList2, arrayList3));
                            }
                            mapScanTag = null;
                            arrayList2 = new ArrayList();
                            arrayList3 = new ArrayList();
                        }
                    } else {
                        String strScanIdentifier2 = scanIdentifier(bufferedReader);
                        if (strScanIdentifier2.equalsIgnoreCase(Constants.ELEMNAME_ARG_STRING)) {
                            Map<String, String> mapScanTag2 = scanTag(bufferedReader);
                            String str = mapScanTag2.get("type");
                            if (str == null) {
                                JmxProperties.MLET_LOGGER.logp(Level.FINER, MLetParser.class.getName(), "parse", "<arg type=... value=...> tag requires type parameter.");
                                throw new IOException("<arg type=... value=...> tag requires type parameter.");
                            }
                            if (mapScanTag != null) {
                                arrayList2.add(str);
                                String str2 = mapScanTag2.get("value");
                                if (str2 == null) {
                                    JmxProperties.MLET_LOGGER.logp(Level.FINER, MLetParser.class.getName(), "parse", "<arg type=... value=...> tag requires value parameter.");
                                    throw new IOException("<arg type=... value=...> tag requires value parameter.");
                                }
                                if (mapScanTag != null) {
                                    arrayList3.add(str2);
                                } else {
                                    JmxProperties.MLET_LOGGER.logp(Level.FINER, MLetParser.class.getName(), "parse", "<arg> tag outside <mlet> ... </mlet>.");
                                    throw new IOException("<arg> tag outside <mlet> ... </mlet>.");
                                }
                            } else {
                                JmxProperties.MLET_LOGGER.logp(Level.FINER, MLetParser.class.getName(), "parse", "<arg> tag outside <mlet> ... </mlet>.");
                                throw new IOException("<arg> tag outside <mlet> ... </mlet>.");
                            }
                        } else if (strScanIdentifier2.equalsIgnoreCase(tag)) {
                            mapScanTag = scanTag(bufferedReader);
                            if (mapScanTag.get("code") == null && mapScanTag.get("object") == null) {
                                JmxProperties.MLET_LOGGER.logp(Level.FINER, MLetParser.class.getName(), "parse", "<mlet> tag requires either code or object parameter.");
                                throw new IOException("<mlet> tag requires either code or object parameter.");
                            }
                            if (mapScanTag.get(Constants.ATTRNAME_ARCHIVE) == null) {
                                JmxProperties.MLET_LOGGER.logp(Level.FINER, MLetParser.class.getName(), "parse", "<mlet> tag requires archive parameter.");
                                throw new IOException("<mlet> tag requires archive parameter.");
                            }
                        } else {
                            continue;
                        }
                    }
                }
            } else {
                bufferedReader.close();
                return arrayList;
            }
        }
    }

    public List<MLetContent> parseURL(String str) throws IOException {
        URL url;
        String str2;
        if (str.indexOf(58) <= 1) {
            String property = System.getProperty("user.dir");
            if (property.charAt(0) == '/' || property.charAt(0) == File.separatorChar) {
                str2 = "file:";
            } else {
                str2 = "file:/";
            }
            url = new URL(new URL(str2 + property.replace(File.separatorChar, '/') + "/"), str);
        } else {
            url = new URL(str);
        }
        return parse(url);
    }
}
