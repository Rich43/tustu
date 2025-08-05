package sun.net.www.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import sun.net.NetProperties;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/net/www/http/HttpCapture.class */
public class HttpCapture {
    private File file;
    private boolean incoming = true;
    private BufferedWriter out;
    private static boolean initialized = false;
    private static volatile ArrayList<Pattern> patterns = null;
    private static volatile ArrayList<String> capFiles = null;

    private static synchronized void init() {
        initialized = true;
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.net.www.http.HttpCapture.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                return NetProperties.get("sun.net.http.captureRules");
            }
        });
        if (str != null && !str.isEmpty()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(str));
                try {
                    for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                        String strTrim = line.trim();
                        if (!strTrim.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                            String[] strArrSplit = strTrim.split(",");
                            if (strArrSplit.length == 2) {
                                if (patterns == null) {
                                    patterns = new ArrayList<>();
                                    capFiles = new ArrayList<>();
                                }
                                patterns.add(Pattern.compile(strArrSplit[0].trim()));
                                capFiles.add(strArrSplit[1].trim());
                            }
                        }
                    }
                    try {
                        bufferedReader.close();
                    } catch (IOException e2) {
                    }
                } catch (IOException e3) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e4) {
                    }
                } catch (Throwable th) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e5) {
                    }
                    throw th;
                }
            } catch (FileNotFoundException e6) {
            }
        }
    }

    private static synchronized boolean isInitialized() {
        return initialized;
    }

    private HttpCapture(File file, URL url) {
        this.file = null;
        this.out = null;
        this.file = file;
        try {
            this.out = new BufferedWriter(new FileWriter(this.file, true));
            this.out.write("URL: " + ((Object) url) + "\n");
        } catch (IOException e2) {
            PlatformLogger.getLogger(HttpCapture.class.getName()).severe((String) null, e2);
        }
    }

    public synchronized void sent(int i2) throws IOException {
        if (this.incoming) {
            this.out.write("\n------>\n");
            this.incoming = false;
            this.out.flush();
        }
        this.out.write(i2);
    }

    public synchronized void received(int i2) throws IOException {
        if (!this.incoming) {
            this.out.write("\n<------\n");
            this.incoming = true;
            this.out.flush();
        }
        this.out.write(i2);
    }

    public synchronized void flush() throws IOException {
        this.out.flush();
    }

    public static HttpCapture getCapture(URL url) {
        File file;
        if (!isInitialized()) {
            init();
        }
        if (patterns == null || patterns.isEmpty()) {
            return null;
        }
        String string = url.toString();
        for (int i2 = 0; i2 < patterns.size(); i2++) {
            if (patterns.get(i2).matcher(string).find()) {
                String str = capFiles.get(i2);
                if (str.indexOf("%d") >= 0) {
                    Random random = new Random();
                    do {
                        file = new File(str.replace("%d", Integer.toString(random.nextInt())));
                    } while (file.exists());
                } else {
                    file = new File(str);
                }
                return new HttpCapture(file, url);
            }
        }
        return null;
    }
}
