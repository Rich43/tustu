package jdk.jfr.internal.jfc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import jdk.jfr.Configuration;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.SecuritySupport;

/* loaded from: jfr.jar:jdk/jfr/internal/jfc/JFC.class */
public final class JFC {
    private static final int BUFFER_SIZE = 8192;
    private static final int MAXIMUM_FILE_SIZE = 1048576;
    private static final int MAX_BUFFER_SIZE = 2147483639;
    private static volatile List<KnownConfiguration> knownConfigurations;

    /* loaded from: jfr.jar:jdk/jfr/internal/jfc/JFC$KnownConfiguration.class */
    private static final class KnownConfiguration {
        private final String content;
        private final String filename;
        private final String name;
        private Configuration configuration;

        public KnownConfiguration(SecuritySupport.SafePath safePath) throws IOException {
            this.content = readContent(safePath);
            this.name = JFC.nameFromPath(safePath.toPath());
            this.filename = JFC.nullSafeFileName(safePath.toPath());
        }

        public boolean isNamed(String str) {
            return this.filename.equals(str) || this.name.equals(str);
        }

        public Configuration getConfigurationFile() throws IOException, ParseException {
            if (this.configuration == null) {
                this.configuration = JFCParser.createConfiguration(this.name, this.content);
            }
            return this.configuration;
        }

        public String getName() {
            return this.name;
        }

        private static String readContent(SecuritySupport.SafePath safePath) throws IOException {
            if (SecuritySupport.getFileSize(safePath) > 1048576) {
                throw new IOException("Configuration with more than 1048576 characters can't be read.");
            }
            InputStream inputStreamNewFileInputStream = SecuritySupport.newFileInputStream(safePath);
            Throwable th = null;
            try {
                String content = JFC.readContent(inputStreamNewFileInputStream);
                if (inputStreamNewFileInputStream != null) {
                    if (0 != 0) {
                        try {
                            inputStreamNewFileInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        inputStreamNewFileInputStream.close();
                    }
                }
                return content;
            } catch (Throwable th3) {
                if (inputStreamNewFileInputStream != null) {
                    if (0 != 0) {
                        try {
                            inputStreamNewFileInputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        inputStreamNewFileInputStream.close();
                    }
                }
                throw th3;
            }
        }
    }

    private JFC() {
    }

    public static Configuration create(String str, Reader reader) throws IOException, ParseException {
        return JFCParser.createConfiguration(str, reader);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String nullSafeFileName(Path path) throws IOException {
        Path fileName = path.getFileName();
        if (fileName == null) {
            throw new IOException("Path has no file name");
        }
        return fileName.toString();
    }

    public static String nameFromPath(Path path) throws IOException {
        String strNullSafeFileName = nullSafeFileName(path);
        if (strNullSafeFileName.endsWith(".jfc")) {
            return strNullSafeFileName.substring(0, strNullSafeFileName.length() - ".jfc".length());
        }
        return strNullSafeFileName;
    }

    public static Configuration createKnown(String str) throws IOException, ParseException {
        for (KnownConfiguration knownConfiguration : getKnownConfigurations()) {
            if (knownConfiguration.isNamed(str)) {
                return knownConfiguration.getConfigurationFile();
            }
        }
        SecuritySupport.SafePath safePath = SecuritySupport.JFC_DIRECTORY;
        if (safePath != null && SecuritySupport.exists(safePath)) {
            Iterator it = Arrays.asList("", ".jfc").iterator();
            while (it.hasNext()) {
                SecuritySupport.SafePath safePath2 = new SecuritySupport.SafePath(safePath.toPath().resolveSibling(str + ((String) it.next())));
                if (SecuritySupport.exists(safePath2) && !SecuritySupport.isDirectory(safePath2)) {
                    Reader readerNewFileReader = SecuritySupport.newFileReader(safePath2);
                    Throwable th = null;
                    try {
                        try {
                            Configuration configurationCreateConfiguration = JFCParser.createConfiguration(nameFromPath(safePath2.toPath()), readerNewFileReader);
                            if (readerNewFileReader != null) {
                                if (0 != 0) {
                                    try {
                                        readerNewFileReader.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    readerNewFileReader.close();
                                }
                            }
                            return configurationCreateConfiguration;
                        } finally {
                        }
                    } catch (Throwable th3) {
                        if (readerNewFileReader != null) {
                            if (th != null) {
                                try {
                                    readerNewFileReader.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                readerNewFileReader.close();
                            }
                        }
                        throw th3;
                    }
                }
            }
        }
        Path path = Paths.get(str, new String[0]);
        String strNameFromPath = nameFromPath(path);
        BufferedReader bufferedReaderNewBufferedReader = Files.newBufferedReader(path);
        Throwable th5 = null;
        try {
            try {
                Configuration configurationCreateConfiguration2 = JFCParser.createConfiguration(strNameFromPath, bufferedReaderNewBufferedReader);
                if (bufferedReaderNewBufferedReader != null) {
                    if (0 != 0) {
                        try {
                            bufferedReaderNewBufferedReader.close();
                        } catch (Throwable th6) {
                            th5.addSuppressed(th6);
                        }
                    } else {
                        bufferedReaderNewBufferedReader.close();
                    }
                }
                return configurationCreateConfiguration2;
            } finally {
            }
        } catch (Throwable th7) {
            if (bufferedReaderNewBufferedReader != null) {
                if (th5 != null) {
                    try {
                        bufferedReaderNewBufferedReader.close();
                    } catch (Throwable th8) {
                        th5.addSuppressed(th8);
                    }
                } else {
                    bufferedReaderNewBufferedReader.close();
                }
            }
            throw th7;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String readContent(InputStream inputStream) throws IOException {
        return new String(read(inputStream, 8192), StandardCharsets.UTF_8);
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0074, code lost:
    
        if (r8 != r10) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0081, code lost:
    
        return java.util.Arrays.copyOf(r9, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:?, code lost:
    
        return r9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static byte[] read(java.io.InputStream r6, int r7) throws java.io.IOException {
        /*
            r0 = r7
            r8 = r0
            r0 = r8
            byte[] r0 = new byte[r0]
            r9 = r0
            r0 = 0
            r10 = r0
        L9:
            r0 = r6
            r1 = r9
            r2 = r10
            r3 = r8
            r4 = r10
            int r3 = r3 - r4
            int r0 = r0.read(r1, r2, r3)
            r1 = r0
            r11 = r1
            if (r0 <= 0) goto L24
            r0 = r10
            r1 = r11
            int r0 = r0 + r1
            r10 = r0
            goto L9
        L24:
            r0 = r11
            if (r0 < 0) goto L71
            r0 = r6
            int r0 = r0.read()
            r1 = r0
            r11 = r1
            if (r0 >= 0) goto L36
            goto L71
        L36:
            r0 = r8
            r1 = 2147483639(0x7ffffff7, float:NaN)
            r2 = r8
            int r1 = r1 - r2
            if (r0 > r1) goto L4b
            r0 = r8
            r1 = 1
            int r0 = r0 << r1
            r1 = 8192(0x2000, float:1.148E-41)
            int r0 = java.lang.Math.max(r0, r1)
            r8 = r0
            goto L5e
        L4b:
            r0 = r8
            r1 = 2147483639(0x7ffffff7, float:NaN)
            if (r0 != r1) goto L5b
            java.lang.OutOfMemoryError r0 = new java.lang.OutOfMemoryError
            r1 = r0
            java.lang.String r2 = "Required array size too large"
            r1.<init>(r2)
            throw r0
        L5b:
            r0 = 2147483639(0x7ffffff7, float:NaN)
            r8 = r0
        L5e:
            r0 = r9
            r1 = r8
            byte[] r0 = java.util.Arrays.copyOf(r0, r1)
            r9 = r0
            r0 = r9
            r1 = r10
            int r10 = r10 + 1
            r2 = r11
            byte r2 = (byte) r2
            r0[r1] = r2
            goto L9
        L71:
            r0 = r8
            r1 = r10
            if (r0 != r1) goto L7b
            r0 = r9
            goto L81
        L7b:
            r0 = r9
            r1 = r10
            byte[] r0 = java.util.Arrays.copyOf(r0, r1)
        L81:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.jfr.internal.jfc.JFC.read(java.io.InputStream, int):byte[]");
    }

    public static List<Configuration> getConfigurations() {
        ArrayList arrayList = new ArrayList();
        for (KnownConfiguration knownConfiguration : getKnownConfigurations()) {
            try {
                arrayList.add(knownConfiguration.getConfigurationFile());
            } catch (IOException e2) {
                Logger.log(LogTag.JFR, LogLevel.WARN, "Could not load configuration " + knownConfiguration.getName() + ". " + e2.getMessage());
            } catch (ParseException e3) {
                Logger.log(LogTag.JFR, LogLevel.WARN, "Could not parse configuration " + knownConfiguration.getName() + ". " + e3.getMessage());
            }
        }
        return arrayList;
    }

    private static List<KnownConfiguration> getKnownConfigurations() {
        if (knownConfigurations == null) {
            ArrayList arrayList = new ArrayList();
            Iterator<SecuritySupport.SafePath> it = SecuritySupport.getPredefinedJFCFiles().iterator();
            while (it.hasNext()) {
                try {
                    arrayList.add(new KnownConfiguration(it.next()));
                } catch (IOException e2) {
                }
            }
            knownConfigurations = arrayList;
        }
        return knownConfigurations;
    }

    public static Configuration getPredefined(String str) throws IOException, ParseException {
        for (KnownConfiguration knownConfiguration : getKnownConfigurations()) {
            if (knownConfiguration.getName().equals(str)) {
                return knownConfiguration.getConfigurationFile();
            }
        }
        throw new NoSuchFileException("Could not locate configuration with name " + str);
    }
}
