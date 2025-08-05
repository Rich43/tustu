package sun.security.ssl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.misc.HexDumpEncoder;
import sun.security.action.GetPropertyAction;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.Extension;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

/* loaded from: jsse.jar:sun/security/ssl/SSLLogger.class */
public final class SSLLogger {
    private static final Logger logger;
    private static final String property;
    public static final boolean isOn;

    static {
        String strPrivilegedGetProperty = GetPropertyAction.privilegedGetProperty("javax.net.debug");
        if (strPrivilegedGetProperty != null) {
            if (strPrivilegedGetProperty.isEmpty()) {
                property = "";
                logger = Logger.getLogger("javax.net.ssl");
            } else {
                property = strPrivilegedGetProperty.toLowerCase(Locale.ENGLISH);
                if (property.equals("help")) {
                    help();
                }
                logger = new SSLConsoleLogger("javax.net.ssl", strPrivilegedGetProperty);
            }
            isOn = true;
            return;
        }
        property = null;
        logger = null;
        isOn = false;
    }

    private static void help() {
        System.err.println();
        System.err.println("help           print the help messages");
        System.err.println("expand         expand debugging information");
        System.err.println();
        System.err.println("all            turn on all debugging");
        System.err.println("ssl            turn on ssl debugging");
        System.err.println();
        System.err.println("The following can be used with ssl:");
        System.err.println("\trecord       enable per-record tracing");
        System.err.println("\thandshake    print each handshake message");
        System.err.println("\tkeygen       print key generation data");
        System.err.println("\tsession      print session activity");
        System.err.println("\tdefaultctx   print default SSL initialization");
        System.err.println("\tsslctx       print SSLContext tracing");
        System.err.println("\tsessioncache print session cache tracing");
        System.err.println("\tkeymanager   print key manager tracing");
        System.err.println("\ttrustmanager print trust manager tracing");
        System.err.println("\tpluggability print pluggability tracing");
        System.err.println();
        System.err.println("\thandshake debugging can be widened with:");
        System.err.println("\tdata         hex dump of each handshake message");
        System.err.println("\tverbose      verbose handshake message printing");
        System.err.println();
        System.err.println("\trecord debugging can be widened with:");
        System.err.println("\tplaintext    hex dump of record plaintext");
        System.err.println("\tpacket       print raw SSL/TLS packets");
        System.err.println();
        System.exit(0);
    }

    public static boolean isOn(String str) {
        if (property == null) {
            return false;
        }
        if (property.isEmpty()) {
            return true;
        }
        for (String str2 : str.split(",")) {
            if (!hasOption(str2.trim())) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasOption(String str) {
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        if (property.contains("all")) {
            return true;
        }
        int iIndexOf = property.indexOf("ssl");
        if (iIndexOf != -1 && property.indexOf("sslctx", iIndexOf) != -1 && !lowerCase.equals("data") && !lowerCase.equals("packet") && !lowerCase.equals("plaintext")) {
            return true;
        }
        return property.contains(lowerCase);
    }

    public static void severe(String str, Object... objArr) {
        log(Level.SEVERE, str, objArr);
    }

    public static void warning(String str, Object... objArr) {
        log(Level.WARNING, str, objArr);
    }

    public static void info(String str, Object... objArr) {
        log(Level.INFO, str, objArr);
    }

    public static void fine(String str, Object... objArr) {
        log(Level.FINE, str, objArr);
    }

    public static void finer(String str, Object... objArr) {
        log(Level.FINER, str, objArr);
    }

    public static void finest(String str, Object... objArr) {
        log(Level.ALL, str, objArr);
    }

    private static void log(Level level, String str, Object... objArr) {
        if (logger != null && logger.isLoggable(level)) {
            if (objArr == null || objArr.length == 0) {
                logger.log(level, str);
                return;
            }
            try {
                logger.log(level, str, SSLSimpleFormatter.formatParameters(objArr));
            } catch (Exception e2) {
            }
        }
    }

    static String toString(Object... objArr) {
        try {
            return SSLSimpleFormatter.formatParameters(objArr);
        } catch (Exception e2) {
            return "unexpected exception thrown: " + e2.getMessage();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLLogger$SSLConsoleLogger.class */
    private static class SSLConsoleLogger extends Logger {
        private final String loggerName;
        private final boolean useCompactFormat;

        SSLConsoleLogger(String str, String str2) {
            super(str, null);
            this.loggerName = str;
            this.useCompactFormat = !str2.toLowerCase(Locale.ENGLISH).contains("expand");
        }

        @Override // java.util.logging.Logger
        public String getName() {
            return this.loggerName;
        }

        @Override // java.util.logging.Logger
        public boolean isLoggable(Level level) {
            return level != Level.OFF;
        }

        @Override // java.util.logging.Logger
        public void log(LogRecord logRecord) {
            if (isLoggable(logRecord.getLevel())) {
                try {
                    System.err.write((logRecord.getThrown() != null ? SSLSimpleFormatter.format(this, logRecord.getLevel(), logRecord.getMessage(), logRecord.getThrown()) : SSLSimpleFormatter.format(this, logRecord.getLevel(), logRecord.getMessage(), logRecord.getParameters())).getBytes("UTF-8"));
                } catch (Exception e2) {
                }
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLLogger$SSLSimpleFormatter.class */
    private static class SSLSimpleFormatter {
        private static final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() { // from class: sun.security.ssl.SSLLogger.SSLSimpleFormatter.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public SimpleDateFormat initialValue() {
                return new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS z", Locale.ENGLISH);
            }
        };
        private static final MessageFormat basicCertFormat = new MessageFormat("\"version\"            : \"v{0}\",\n\"serial number\"      : \"{1}\",\n\"signature algorithm\": \"{2}\",\n\"issuer\"             : \"{3}\",\n\"not before\"         : \"{4}\",\n\"not  after\"         : \"{5}\",\n\"subject\"            : \"{6}\",\n\"subject public key\" : \"{7}\"\n", Locale.ENGLISH);
        private static final MessageFormat extendedCertFormart = new MessageFormat("\"version\"            : \"v{0}\",\n\"serial number\"      : \"{1}\",\n\"signature algorithm\": \"{2}\",\n\"issuer\"             : \"{3}\",\n\"not before\"         : \"{4}\",\n\"not  after\"         : \"{5}\",\n\"subject\"            : \"{6}\",\n\"subject public key\" : \"{7}\",\n\"extensions\"         : [\n{8}\n]\n", Locale.ENGLISH);
        private static final MessageFormat messageFormatNoParas = new MessageFormat("'{'\n  \"logger\"      : \"{0}\",\n  \"level\"       : \"{1}\",\n  \"thread id\"   : \"{2}\",\n  \"thread name\" : \"{3}\",\n  \"time\"        : \"{4}\",\n  \"caller\"      : \"{5}\",\n  \"message\"     : \"{6}\"\n'}'\n", Locale.ENGLISH);
        private static final MessageFormat messageCompactFormatNoParas = new MessageFormat("{0}|{1}|{2}|{3}|{4}|{5}|{6}\n", Locale.ENGLISH);
        private static final MessageFormat messageFormatWithParas = new MessageFormat("'{'\n  \"logger\"      : \"{0}\",\n  \"level\"       : \"{1}\",\n  \"thread id\"   : \"{2}\",\n  \"thread name\" : \"{3}\",\n  \"time\"        : \"{4}\",\n  \"caller\"      : \"{5}\",\n  \"message\"     : \"{6}\",\n  \"specifics\"   : [\n{7}\n  ]\n'}'\n", Locale.ENGLISH);
        private static final MessageFormat messageCompactFormatWithParas = new MessageFormat("{0}|{1}|{2}|{3}|{4}|{5}|{6} (\n{7}\n)\n", Locale.ENGLISH);
        private static final MessageFormat keyObjectFormat = new MessageFormat("\"{0}\" : '{'\n{1}'}'\n", Locale.ENGLISH);

        private SSLSimpleFormatter() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String format(SSLConsoleLogger sSLConsoleLogger, Level level, String str, Object... objArr) {
            String strIndent;
            if (objArr == null || objArr.length == 0) {
                Object[] objArr2 = {sSLConsoleLogger.loggerName, level.getName(), Utilities.toHexString(Thread.currentThread().getId()), Thread.currentThread().getName(), dateFormat.get().format(new Date(System.currentTimeMillis())), formatCaller(), str};
                if (sSLConsoleLogger.useCompactFormat) {
                    return messageCompactFormatNoParas.format(objArr2);
                }
                return messageFormatNoParas.format(objArr2);
            }
            Object[] objArr3 = new Object[8];
            objArr3[0] = sSLConsoleLogger.loggerName;
            objArr3[1] = level.getName();
            objArr3[2] = Utilities.toHexString(Thread.currentThread().getId());
            objArr3[3] = Thread.currentThread().getName();
            objArr3[4] = dateFormat.get().format(new Date(System.currentTimeMillis()));
            objArr3[5] = formatCaller();
            objArr3[6] = str;
            if (sSLConsoleLogger.useCompactFormat) {
                strIndent = formatParameters(objArr);
            } else {
                strIndent = Utilities.indent(formatParameters(objArr));
            }
            objArr3[7] = strIndent;
            if (sSLConsoleLogger.useCompactFormat) {
                return messageCompactFormatWithParas.format(objArr3);
            }
            return messageFormatWithParas.format(objArr3);
        }

        private static String formatCaller() {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (int i2 = 1; i2 < stackTrace.length; i2++) {
                StackTraceElement stackTraceElement = stackTrace[i2];
                if (!stackTraceElement.getClassName().startsWith(SSLLogger.class.getName()) && !stackTraceElement.getClassName().startsWith(Logger.class.getName())) {
                    return stackTraceElement.getFileName() + CallSiteDescriptor.TOKEN_DELIMITER + stackTraceElement.getLineNumber();
                }
            }
            return "unknown caller";
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String formatParameters(Object... objArr) {
            StringBuilder sb = new StringBuilder(512);
            boolean z2 = true;
            for (Object obj : objArr) {
                if (z2) {
                    z2 = false;
                } else {
                    sb.append(",\n");
                }
                if (obj instanceof Throwable) {
                    sb.append(formatThrowable((Throwable) obj));
                } else if (obj instanceof Certificate) {
                    sb.append(formatCertificate((Certificate) obj));
                } else if (obj instanceof ByteArrayInputStream) {
                    sb.append(formatByteArrayInputStream((ByteArrayInputStream) obj));
                } else if (obj instanceof ByteBuffer) {
                    sb.append(formatByteBuffer((ByteBuffer) obj));
                } else if (obj instanceof byte[]) {
                    sb.append(formatByteArrayInputStream(new ByteArrayInputStream((byte[]) obj)));
                } else if (obj instanceof Map.Entry) {
                    sb.append(formatMapEntry((Map.Entry) obj));
                } else {
                    sb.append(formatObject(obj));
                }
            }
            return sb.toString();
        }

        private static String formatThrowable(Throwable th) {
            StringBuilder sb = new StringBuilder(512);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            Throwable th2 = null;
            try {
                try {
                    th.printStackTrace(printStream);
                    sb.append(Utilities.indent(byteArrayOutputStream.toString()));
                    if (printStream != null) {
                        if (0 != 0) {
                            try {
                                printStream.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            printStream.close();
                        }
                    }
                    return keyObjectFormat.format(new Object[]{"throwable", sb.toString()});
                } finally {
                }
            } catch (Throwable th4) {
                if (printStream != null) {
                    if (th2 != null) {
                        try {
                            printStream.close();
                        } catch (Throwable th5) {
                            th2.addSuppressed(th5);
                        }
                    } else {
                        printStream.close();
                    }
                }
                throw th4;
            }
        }

        private static String formatCertificate(Certificate certificate) {
            if (!(certificate instanceof X509Certificate)) {
                return Utilities.indent(certificate.toString());
            }
            StringBuilder sb = new StringBuilder(512);
            try {
                X509CertImpl impl = X509CertImpl.toImpl((X509Certificate) certificate);
                CertificateExtensions certificateExtensions = (CertificateExtensions) ((X509CertInfo) impl.get(X509CertInfo.IDENT)).get("extensions");
                if (certificateExtensions == null) {
                    sb.append(Utilities.indent(basicCertFormat.format(new Object[]{Integer.valueOf(impl.getVersion()), Utilities.toHexString(impl.getSerialNumber().toByteArray()), impl.getSigAlgName(), impl.getIssuerX500Principal().toString(), dateFormat.get().format(impl.getNotBefore()), dateFormat.get().format(impl.getNotAfter()), impl.getSubjectX500Principal().toString(), impl.getPublicKey().getAlgorithm()})));
                } else {
                    StringBuilder sb2 = new StringBuilder(512);
                    boolean z2 = true;
                    for (Extension extension : certificateExtensions.getAllExtensions()) {
                        if (z2) {
                            z2 = false;
                        } else {
                            sb2.append(",\n");
                        }
                        sb2.append("{\n" + Utilities.indent(extension.toString()) + "\n}");
                    }
                    sb.append(Utilities.indent(extendedCertFormart.format(new Object[]{Integer.valueOf(impl.getVersion()), Utilities.toHexString(impl.getSerialNumber().toByteArray()), impl.getSigAlgName(), impl.getIssuerX500Principal().toString(), dateFormat.get().format(impl.getNotBefore()), dateFormat.get().format(impl.getNotAfter()), impl.getSubjectX500Principal().toString(), impl.getPublicKey().getAlgorithm(), Utilities.indent(sb2.toString())})));
                }
            } catch (Exception e2) {
            }
            return Utilities.indent(keyObjectFormat.format(new Object[]{"certificate", sb.toString()}));
        }

        private static String formatByteArrayInputStream(ByteArrayInputStream byteArrayInputStream) {
            ByteArrayOutputStream byteArrayOutputStream;
            Throwable th;
            StringBuilder sb = new StringBuilder(512);
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                th = null;
            } catch (IOException e2) {
            }
            try {
                try {
                    new HexDumpEncoder().encodeBuffer(byteArrayInputStream, byteArrayOutputStream);
                    sb.append(Utilities.indent(byteArrayOutputStream.toString()));
                    if (byteArrayOutputStream != null) {
                        if (0 != 0) {
                            try {
                                byteArrayOutputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            byteArrayOutputStream.close();
                        }
                    }
                    return sb.toString();
                } finally {
                }
            } finally {
            }
        }

        private static String formatByteBuffer(ByteBuffer byteBuffer) {
            StringBuilder sb = new StringBuilder(512);
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Throwable th = null;
                try {
                    try {
                        new HexDumpEncoder().encodeBuffer(byteBuffer.duplicate(), byteArrayOutputStream);
                        sb.append(Utilities.indent(byteArrayOutputStream.toString()));
                        if (byteArrayOutputStream != null) {
                            if (0 != 0) {
                                try {
                                    byteArrayOutputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                byteArrayOutputStream.close();
                            }
                        }
                    } finally {
                    }
                } finally {
                }
            } catch (IOException e2) {
            }
            return sb.toString();
        }

        private static String formatMapEntry(Map.Entry<String, ?> entry) {
            String string;
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                string = PdfOps.DOUBLE_QUOTE__TOKEN + key + "\": \"" + ((String) value) + PdfOps.DOUBLE_QUOTE__TOKEN;
            } else if (value instanceof String[]) {
                StringBuilder sb = new StringBuilder(512);
                String[] strArr = (String[]) value;
                sb.append(PdfOps.DOUBLE_QUOTE__TOKEN + key + "\": [\n");
                for (String str : strArr) {
                    sb.append("      \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
                    if (str != strArr[strArr.length - 1]) {
                        sb.append(",");
                    }
                    sb.append("\n");
                }
                sb.append("      ]");
                string = sb.toString();
            } else if (value instanceof byte[]) {
                string = PdfOps.DOUBLE_QUOTE__TOKEN + key + "\": \"" + Utilities.toHexString((byte[]) value) + PdfOps.DOUBLE_QUOTE__TOKEN;
            } else if (value instanceof Byte) {
                string = PdfOps.DOUBLE_QUOTE__TOKEN + key + "\": \"" + Utilities.toHexString(((Byte) value).byteValue()) + PdfOps.DOUBLE_QUOTE__TOKEN;
            } else {
                string = PdfOps.DOUBLE_QUOTE__TOKEN + key + "\": \"" + value.toString() + PdfOps.DOUBLE_QUOTE__TOKEN;
            }
            return Utilities.indent(string);
        }

        private static String formatObject(Object obj) {
            return obj.toString();
        }
    }
}
