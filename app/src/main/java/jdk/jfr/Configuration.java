package jdk.jfr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jdk.jfr.internal.JVMSupport;
import jdk.jfr.internal.jfc.JFC;

/* loaded from: jfr.jar:jdk/jfr/Configuration.class */
public final class Configuration {
    private final Map<String, String> settings;
    private final String label;
    private final String description;
    private final String provider;
    private final String contents;
    private final String name;

    Configuration(String str, String str2, String str3, String str4, Map<String, String> map, String str5) {
        this.name = str;
        this.label = str2;
        this.description = str3;
        this.provider = str4;
        this.settings = map;
        this.contents = str5;
    }

    public Map<String, String> getSettings() {
        return new LinkedHashMap(this.settings);
    }

    public String getName() {
        return this.name;
    }

    public String getLabel() {
        return this.label;
    }

    public String getDescription() {
        return this.description;
    }

    public String getProvider() {
        return this.provider;
    }

    public String getContents() {
        return this.contents;
    }

    public static Configuration create(Path path) throws IOException, ParseException {
        Objects.requireNonNull(path);
        JVMSupport.ensureWithIOException();
        BufferedReader bufferedReaderNewBufferedReader = Files.newBufferedReader(path);
        Throwable th = null;
        try {
            Configuration configurationCreate = JFC.create(JFC.nameFromPath(path), bufferedReaderNewBufferedReader);
            if (bufferedReaderNewBufferedReader != null) {
                if (0 != 0) {
                    try {
                        bufferedReaderNewBufferedReader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    bufferedReaderNewBufferedReader.close();
                }
            }
            return configurationCreate;
        } catch (Throwable th3) {
            if (bufferedReaderNewBufferedReader != null) {
                if (0 != 0) {
                    try {
                        bufferedReaderNewBufferedReader.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    bufferedReaderNewBufferedReader.close();
                }
            }
            throw th3;
        }
    }

    public static Configuration create(Reader reader) throws IOException, ParseException {
        Objects.requireNonNull(reader);
        JVMSupport.ensureWithIOException();
        return JFC.create(null, reader);
    }

    public static Configuration getConfiguration(String str) throws IOException, ParseException {
        JVMSupport.ensureWithIOException();
        return JFC.getPredefined(str);
    }

    public static List<Configuration> getConfigurations() {
        if (JVMSupport.isNotAvailable()) {
            return new ArrayList();
        }
        return Collections.unmodifiableList(JFC.getConfigurations());
    }
}
