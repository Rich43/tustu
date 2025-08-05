package sun.security.tools.keytool;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URLClassLoader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.AlgorithmParameters;
import java.security.CodeSigner;
import java.security.CryptoPrimitive;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.Timestamp;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CRL;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.text.Collator;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javafx.fxml.FXMLLoader;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.security.auth.x500.X500Principal;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.misc.HexDumpEncoder;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.pkcs10.PKCS10;
import sun.security.pkcs10.PKCS10Attribute;
import sun.security.pkcs12.PKCS12KeyStore;
import sun.security.provider.X509Factory;
import sun.security.provider.certpath.CertStoreHelper;
import sun.security.tools.KeyStoreUtil;
import sun.security.tools.PathList;
import sun.security.util.DerValue;
import sun.security.util.DisabledAlgorithmConstraints;
import sun.security.util.KeyUtil;
import sun.security.util.NamedCurve;
import sun.security.util.ObjectIdentifier;
import sun.security.util.Password;
import sun.security.util.Pem;
import sun.security.util.SecurityProperties;
import sun.security.util.SecurityProviderConstants;
import sun.security.util.SignatureUtil;
import sun.security.x509.AccessDescription;
import sun.security.x509.AlgorithmId;
import sun.security.x509.AuthorityInfoAccessExtension;
import sun.security.x509.AuthorityKeyIdentifierExtension;
import sun.security.x509.BasicConstraintsExtension;
import sun.security.x509.CRLDistributionPointsExtension;
import sun.security.x509.CRLExtensions;
import sun.security.x509.CRLReasonCodeExtension;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.DNSName;
import sun.security.x509.DistributionPoint;
import sun.security.x509.ExtendedKeyUsageExtension;
import sun.security.x509.Extension;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.GeneralNames;
import sun.security.x509.IPAddressName;
import sun.security.x509.IssuerAlternativeNameExtension;
import sun.security.x509.KeyIdentifier;
import sun.security.x509.KeyUsageExtension;
import sun.security.x509.OIDName;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.RFC822Name;
import sun.security.x509.SubjectAlternativeNameExtension;
import sun.security.x509.SubjectInfoAccessExtension;
import sun.security.x509.SubjectKeyIdentifierExtension;
import sun.security.x509.URIName;
import sun.security.x509.X500Name;
import sun.security.x509.X509CRLEntryImpl;
import sun.security.x509.X509CRLImpl;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/tools/keytool/Main.class */
public final class Main {
    private boolean debug = false;
    private Command command = null;
    private String sigAlgName = null;
    private String keyAlgName = null;
    private boolean verbose = false;
    private int keysize = -1;
    private boolean rfc = false;
    private long validity = 90;
    private String alias = null;
    private String dname = null;
    private String dest = null;
    private String filename = null;
    private String infilename = null;
    private String outfilename = null;
    private String srcksfname = null;
    private Set<Pair<String, String>> providers = null;
    private String storetype = null;
    private String srcProviderName = null;
    private String providerName = null;
    private String pathlist = null;
    private char[] storePass = null;
    private char[] storePassNew = null;
    private char[] keyPass = null;
    private char[] keyPassNew = null;
    private char[] newPass = null;
    private char[] destKeyPass = null;
    private char[] srckeyPass = null;
    private String ksfname = null;
    private File ksfile = null;
    private InputStream ksStream = null;
    private String sslserver = null;
    private String jarfile = null;
    private KeyStore keyStore = null;
    private boolean token = false;
    private boolean nullStream = false;
    private boolean kssave = false;
    private boolean noprompt = false;
    private boolean trustcacerts = false;
    private boolean nowarn = false;
    private boolean protectedPath = false;
    private boolean srcprotectedPath = false;
    private CertificateFactory cf = null;
    private KeyStore caks = null;
    private char[] srcstorePass = null;
    private String srcstoretype = null;
    private Set<char[]> passwords = new HashSet();
    private String startDate = null;
    private List<String> ids = new ArrayList();
    private List<String> v3ext = new ArrayList();
    private boolean inplaceImport = false;
    private String inplaceBackupName = null;
    private List<String> weakWarnings = new ArrayList();
    private boolean isPasswordlessKeyStore = false;
    private static final String NONE = "NONE";
    private static final String P11KEYSTORE = "PKCS11";
    private static final String P12KEYSTORE = "PKCS12";
    private static final String keyAlias = "mykey";
    private static final String[] extSupported;
    private static final byte[] CRLF = {13, 10};
    private static final DisabledAlgorithmConstraints DISABLED_CHECK = new DisabledAlgorithmConstraints(DisabledAlgorithmConstraints.PROPERTY_CERTPATH_DISABLED_ALGS);
    private static final DisabledAlgorithmConstraints LEGACY_CHECK = new DisabledAlgorithmConstraints(DisabledAlgorithmConstraints.PROPERTY_SECURITY_LEGACY_ALGS);
    private static final Set<CryptoPrimitive> SIG_PRIMITIVE_SET = Collections.unmodifiableSet(EnumSet.of(CryptoPrimitive.SIGNATURE));
    private static final Class<?>[] PARAM_STRING = {String.class};
    private static final ResourceBundle rb = ResourceBundle.getBundle("sun.security.tools.keytool.Resources");
    private static final Collator collator = Collator.getInstance();

    static {
        collator.setStrength(0);
        extSupported = new String[]{BasicConstraintsExtension.NAME, KeyUsageExtension.NAME, ExtendedKeyUsageExtension.NAME, SubjectAlternativeNameExtension.NAME, IssuerAlternativeNameExtension.NAME, SubjectInfoAccessExtension.NAME, AuthorityInfoAccessExtension.NAME, null, CRLDistributionPointsExtension.NAME};
    }

    /* loaded from: rt.jar:sun/security/tools/keytool/Main$Command.class */
    enum Command {
        CERTREQ("Generates.a.certificate.request", Option.ALIAS, Option.SIGALG, Option.FILEOUT, Option.KEYPASS, Option.KEYSTORE, Option.DNAME, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V, Option.PROTECTED),
        CHANGEALIAS("Changes.an.entry.s.alias", Option.ALIAS, Option.DESTALIAS, Option.KEYPASS, Option.KEYSTORE, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V, Option.PROTECTED),
        DELETE("Deletes.an.entry", Option.ALIAS, Option.KEYSTORE, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V, Option.PROTECTED),
        EXPORTCERT("Exports.certificate", Option.RFC, Option.ALIAS, Option.FILEOUT, Option.KEYSTORE, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V, Option.PROTECTED),
        GENKEYPAIR("Generates.a.key.pair", Option.ALIAS, Option.KEYALG, Option.KEYSIZE, Option.SIGALG, Option.DESTALIAS, Option.DNAME, Option.STARTDATE, Option.EXT, Option.VALIDITY, Option.KEYPASS, Option.KEYSTORE, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V, Option.PROTECTED),
        GENSECKEY("Generates.a.secret.key", Option.ALIAS, Option.KEYPASS, Option.KEYALG, Option.KEYSIZE, Option.KEYSTORE, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V, Option.PROTECTED),
        GENCERT("Generates.certificate.from.a.certificate.request", Option.RFC, Option.INFILE, Option.OUTFILE, Option.ALIAS, Option.SIGALG, Option.DNAME, Option.STARTDATE, Option.EXT, Option.VALIDITY, Option.KEYPASS, Option.KEYSTORE, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V, Option.PROTECTED),
        IMPORTCERT("Imports.a.certificate.or.a.certificate.chain", Option.NOPROMPT, Option.TRUSTCACERTS, Option.PROTECTED, Option.ALIAS, Option.FILEIN, Option.KEYPASS, Option.KEYSTORE, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V),
        IMPORTPASS("Imports.a.password", Option.ALIAS, Option.KEYPASS, Option.KEYALG, Option.KEYSIZE, Option.KEYSTORE, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V, Option.PROTECTED),
        IMPORTKEYSTORE("Imports.one.or.all.entries.from.another.keystore", Option.SRCKEYSTORE, Option.DESTKEYSTORE, Option.SRCSTORETYPE, Option.DESTSTORETYPE, Option.SRCSTOREPASS, Option.DESTSTOREPASS, Option.SRCPROTECTED, Option.SRCPROVIDERNAME, Option.DESTPROVIDERNAME, Option.SRCALIAS, Option.DESTALIAS, Option.SRCKEYPASS, Option.DESTKEYPASS, Option.NOPROMPT, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V),
        KEYPASSWD("Changes.the.key.password.of.an.entry", Option.ALIAS, Option.KEYPASS, Option.NEW, Option.KEYSTORE, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V),
        LIST("Lists.entries.in.a.keystore", Option.RFC, Option.ALIAS, Option.KEYSTORE, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V, Option.PROTECTED),
        PRINTCERT("Prints.the.content.of.a.certificate", Option.RFC, Option.FILEIN, Option.SSLSERVER, Option.JARFILE, Option.V),
        PRINTCERTREQ("Prints.the.content.of.a.certificate.request", Option.FILEIN, Option.V),
        PRINTCRL("Prints.the.content.of.a.CRL.file", Option.FILEIN, Option.V),
        STOREPASSWD("Changes.the.store.password.of.a.keystore", Option.NEW, Option.KEYSTORE, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V),
        KEYCLONE("Clones.a.key.entry", Option.ALIAS, Option.DESTALIAS, Option.KEYPASS, Option.NEW, Option.STORETYPE, Option.KEYSTORE, Option.STOREPASS, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V),
        SELFCERT("Generates.a.self.signed.certificate", Option.ALIAS, Option.SIGALG, Option.DNAME, Option.STARTDATE, Option.VALIDITY, Option.KEYPASS, Option.STORETYPE, Option.KEYSTORE, Option.STOREPASS, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V),
        GENCRL("Generates.CRL", Option.RFC, Option.FILEOUT, Option.ID, Option.ALIAS, Option.SIGALG, Option.EXT, Option.KEYPASS, Option.KEYSTORE, Option.STOREPASS, Option.STORETYPE, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V, Option.PROTECTED),
        IDENTITYDB("Imports.entries.from.a.JDK.1.1.x.style.identity.database", Option.FILEIN, Option.STORETYPE, Option.KEYSTORE, Option.STOREPASS, Option.PROVIDERNAME, Option.PROVIDERCLASS, Option.PROVIDERARG, Option.PROVIDERPATH, Option.V);

        final String description;
        final Option[] options;

        Command(String str, Option... optionArr) {
            this.description = str;
            this.options = optionArr;
        }

        @Override // java.lang.Enum
        public String toString() {
            return LanguageTag.SEP + name().toLowerCase(Locale.ENGLISH);
        }
    }

    /* loaded from: rt.jar:sun/security/tools/keytool/Main$Option.class */
    enum Option {
        ALIAS("alias", "<alias>", "alias.name.of.the.entry.to.process"),
        DESTALIAS("destalias", "<destalias>", "destination.alias"),
        DESTKEYPASS("destkeypass", "<arg>", "destination.key.password"),
        DESTKEYSTORE("destkeystore", "<destkeystore>", "destination.keystore.name"),
        DESTPROTECTED("destprotected", null, "destination.keystore.password.protected"),
        DESTPROVIDERNAME("destprovidername", "<destprovidername>", "destination.keystore.provider.name"),
        DESTSTOREPASS("deststorepass", "<arg>", "destination.keystore.password"),
        DESTSTORETYPE("deststoretype", "<deststoretype>", "destination.keystore.type"),
        DNAME("dname", "<dname>", "distinguished.name"),
        EXT("ext", "<value>", "X.509.extension"),
        FILEOUT(DeploymentDescriptorParser.ATTR_FILE, "<filename>", "output.file.name"),
        FILEIN(DeploymentDescriptorParser.ATTR_FILE, "<filename>", "input.file.name"),
        ID("id", "<id:reason>", "Serial.ID.of.cert.to.revoke"),
        INFILE("infile", "<filename>", "input.file.name"),
        KEYALG("keyalg", "<keyalg>", "key.algorithm.name"),
        KEYPASS("keypass", "<arg>", "key.password"),
        KEYSIZE("keysize", "<keysize>", "key.bit.size"),
        KEYSTORE("keystore", "<keystore>", "keystore.name"),
        NEW("new", "<arg>", "new.password"),
        NOPROMPT("noprompt", null, "do.not.prompt"),
        OUTFILE("outfile", "<filename>", "output.file.name"),
        PROTECTED("protected", null, "password.through.protected.mechanism"),
        PROVIDERARG("providerarg", "<arg>", "provider.argument"),
        PROVIDERCLASS("providerclass", "<providerclass>", "provider.class.name"),
        PROVIDERNAME("providername", "<providername>", "provider.name"),
        PROVIDERPATH("providerpath", "<pathlist>", "provider.classpath"),
        RFC("rfc", null, "output.in.RFC.style"),
        SIGALG("sigalg", "<sigalg>", "signature.algorithm.name"),
        SRCALIAS("srcalias", "<srcalias>", "source.alias"),
        SRCKEYPASS("srckeypass", "<arg>", "source.key.password"),
        SRCKEYSTORE("srckeystore", "<srckeystore>", "source.keystore.name"),
        SRCPROTECTED("srcprotected", null, "source.keystore.password.protected"),
        SRCPROVIDERNAME("srcprovidername", "<srcprovidername>", "source.keystore.provider.name"),
        SRCSTOREPASS("srcstorepass", "<arg>", "source.keystore.password"),
        SRCSTORETYPE("srcstoretype", "<srcstoretype>", "source.keystore.type"),
        SSLSERVER("sslserver", "<server[:port]>", "SSL.server.host.and.port"),
        JARFILE("jarfile", "<filename>", "signed.jar.file"),
        STARTDATE("startdate", "<startdate>", "certificate.validity.start.date.time"),
        STOREPASS("storepass", "<arg>", "keystore.password"),
        STORETYPE("storetype", "<storetype>", "keystore.type"),
        TRUSTCACERTS("trustcacerts", null, "trust.certificates.from.cacerts"),
        V(PdfOps.v_TOKEN, null, "verbose.output"),
        VALIDITY("validity", "<valDays>", "validity.number.of.days");

        final String name;
        final String arg;
        final String description;

        Option(String str, String str2, String str3) {
            this.name = str;
            this.arg = str2;
            this.description = str3;
        }

        @Override // java.lang.Enum
        public String toString() {
            return LanguageTag.SEP + this.name;
        }
    }

    private Main() {
    }

    public static void main(String[] strArr) throws Exception {
        new Main().run(strArr, System.out);
    }

    private void run(String[] strArr, PrintStream printStream) throws Exception {
        try {
            try {
                parseArgs(strArr);
                if (this.command != null) {
                    doCommands(printStream);
                }
                printWeakWarnings(false);
                for (char[] cArr : this.passwords) {
                    if (cArr != null) {
                        Arrays.fill(cArr, ' ');
                    }
                }
                if (this.ksStream != null) {
                    this.ksStream.close();
                }
            } catch (Exception e2) {
                System.out.println(rb.getString("keytool.error.") + ((Object) e2));
                if (this.verbose) {
                    e2.printStackTrace(System.out);
                }
                if (!this.debug) {
                    System.exit(1);
                    printWeakWarnings(false);
                    for (char[] cArr2 : this.passwords) {
                        if (cArr2 != null) {
                            Arrays.fill(cArr2, ' ');
                        }
                    }
                    if (this.ksStream != null) {
                        this.ksStream.close();
                        return;
                    }
                    return;
                }
                throw e2;
            }
        } catch (Throwable th) {
            printWeakWarnings(false);
            for (char[] cArr3 : this.passwords) {
                if (cArr3 != null) {
                    Arrays.fill(cArr3, ' ');
                }
            }
            if (this.ksStream != null) {
                this.ksStream.close();
            }
            throw th;
        }
    }

    void parseArgs(String[] strArr) {
        boolean z2 = strArr.length == 0;
        int i2 = 0;
        while (i2 < strArr.length && strArr[i2].startsWith(LanguageTag.SEP)) {
            String strSubstring = strArr[i2];
            if (i2 == strArr.length - 1) {
                Option[] optionArrValues = Option.values();
                int length = optionArrValues.length;
                int i3 = 0;
                while (true) {
                    if (i3 >= length) {
                        break;
                    }
                    Option option = optionArrValues[i3];
                    if (collator.compare(strSubstring, option.toString()) != 0) {
                        i3++;
                    } else if (option.arg != null) {
                        errorNeedArgument(strSubstring);
                    }
                }
            }
            String strSubstring2 = null;
            int iIndexOf = strSubstring.indexOf(58);
            if (iIndexOf > 0) {
                strSubstring2 = strSubstring.substring(iIndexOf + 1);
                strSubstring = strSubstring.substring(0, iIndexOf);
            }
            boolean z3 = false;
            Command[] commandArrValues = Command.values();
            int length2 = commandArrValues.length;
            int i4 = 0;
            while (true) {
                if (i4 >= length2) {
                    break;
                }
                Command command = commandArrValues[i4];
                if (collator.compare(strSubstring, command.toString()) != 0) {
                    i4++;
                } else {
                    this.command = command;
                    z3 = true;
                    break;
                }
            }
            if (!z3) {
                if (collator.compare(strSubstring, "-export") == 0) {
                    this.command = Command.EXPORTCERT;
                } else if (collator.compare(strSubstring, "-genkey") == 0) {
                    this.command = Command.GENKEYPAIR;
                } else if (collator.compare(strSubstring, "-import") == 0) {
                    this.command = Command.IMPORTCERT;
                } else if (collator.compare(strSubstring, "-importpassword") == 0) {
                    this.command = Command.IMPORTPASS;
                } else if (collator.compare(strSubstring, "-help") == 0) {
                    z2 = true;
                } else if (collator.compare(strSubstring, "-nowarn") == 0) {
                    this.nowarn = true;
                } else if (collator.compare(strSubstring, "-keystore") == 0 || collator.compare(strSubstring, "-destkeystore") == 0) {
                    i2++;
                    this.ksfname = strArr[i2];
                } else if (collator.compare(strSubstring, "-storepass") == 0 || collator.compare(strSubstring, "-deststorepass") == 0) {
                    i2++;
                    this.storePass = getPass(strSubstring2, strArr[i2]);
                    this.passwords.add(this.storePass);
                } else if (collator.compare(strSubstring, "-storetype") == 0 || collator.compare(strSubstring, "-deststoretype") == 0) {
                    i2++;
                    this.storetype = KeyStoreUtil.niceStoreTypeName(strArr[i2]);
                } else if (collator.compare(strSubstring, "-srcstorepass") == 0) {
                    i2++;
                    this.srcstorePass = getPass(strSubstring2, strArr[i2]);
                    this.passwords.add(this.srcstorePass);
                } else if (collator.compare(strSubstring, "-srcstoretype") == 0) {
                    i2++;
                    this.srcstoretype = KeyStoreUtil.niceStoreTypeName(strArr[i2]);
                } else if (collator.compare(strSubstring, "-srckeypass") == 0) {
                    i2++;
                    this.srckeyPass = getPass(strSubstring2, strArr[i2]);
                    this.passwords.add(this.srckeyPass);
                } else if (collator.compare(strSubstring, "-srcprovidername") == 0) {
                    i2++;
                    this.srcProviderName = strArr[i2];
                } else if (collator.compare(strSubstring, "-providername") == 0 || collator.compare(strSubstring, "-destprovidername") == 0) {
                    i2++;
                    this.providerName = strArr[i2];
                } else if (collator.compare(strSubstring, "-providerpath") == 0) {
                    i2++;
                    this.pathlist = strArr[i2];
                } else if (collator.compare(strSubstring, "-keypass") == 0) {
                    i2++;
                    this.keyPass = getPass(strSubstring2, strArr[i2]);
                    this.passwords.add(this.keyPass);
                } else if (collator.compare(strSubstring, "-new") == 0) {
                    i2++;
                    this.newPass = getPass(strSubstring2, strArr[i2]);
                    this.passwords.add(this.newPass);
                } else if (collator.compare(strSubstring, "-destkeypass") == 0) {
                    i2++;
                    this.destKeyPass = getPass(strSubstring2, strArr[i2]);
                    this.passwords.add(this.destKeyPass);
                } else if (collator.compare(strSubstring, "-alias") == 0 || collator.compare(strSubstring, "-srcalias") == 0) {
                    i2++;
                    this.alias = strArr[i2];
                } else if (collator.compare(strSubstring, "-dest") == 0 || collator.compare(strSubstring, "-destalias") == 0) {
                    i2++;
                    this.dest = strArr[i2];
                } else if (collator.compare(strSubstring, "-dname") == 0) {
                    i2++;
                    this.dname = strArr[i2];
                } else if (collator.compare(strSubstring, "-keysize") == 0) {
                    i2++;
                    this.keysize = Integer.parseInt(strArr[i2]);
                } else if (collator.compare(strSubstring, "-keyalg") == 0) {
                    i2++;
                    this.keyAlgName = strArr[i2];
                } else if (collator.compare(strSubstring, "-sigalg") == 0) {
                    i2++;
                    this.sigAlgName = strArr[i2];
                } else if (collator.compare(strSubstring, "-startdate") == 0) {
                    i2++;
                    this.startDate = strArr[i2];
                } else if (collator.compare(strSubstring, "-validity") == 0) {
                    i2++;
                    this.validity = Long.parseLong(strArr[i2]);
                } else if (collator.compare(strSubstring, "-ext") == 0) {
                    i2++;
                    this.v3ext.add(strArr[i2]);
                } else if (collator.compare(strSubstring, "-id") == 0) {
                    i2++;
                    this.ids.add(strArr[i2]);
                } else if (collator.compare(strSubstring, "-file") == 0) {
                    i2++;
                    this.filename = strArr[i2];
                } else if (collator.compare(strSubstring, "-infile") == 0) {
                    i2++;
                    this.infilename = strArr[i2];
                } else if (collator.compare(strSubstring, "-outfile") == 0) {
                    i2++;
                    this.outfilename = strArr[i2];
                } else if (collator.compare(strSubstring, "-sslserver") == 0) {
                    i2++;
                    this.sslserver = strArr[i2];
                } else if (collator.compare(strSubstring, "-jarfile") == 0) {
                    i2++;
                    this.jarfile = strArr[i2];
                } else if (collator.compare(strSubstring, "-srckeystore") == 0) {
                    i2++;
                    this.srcksfname = strArr[i2];
                } else if (collator.compare(strSubstring, "-provider") == 0 || collator.compare(strSubstring, "-providerclass") == 0) {
                    if (this.providers == null) {
                        this.providers = new HashSet(3);
                    }
                    i2++;
                    String str = strArr[i2];
                    String str2 = null;
                    if (strArr.length > i2 + 1) {
                        String str3 = strArr[i2 + 1];
                        if (collator.compare(str3, "-providerarg") == 0) {
                            if (strArr.length == i2 + 2) {
                                errorNeedArgument(str3);
                            }
                            str2 = strArr[i2 + 2];
                            i2 += 2;
                        }
                    }
                    this.providers.add(Pair.of(str, str2));
                } else if (collator.compare(strSubstring, "-v") == 0) {
                    this.verbose = true;
                } else if (collator.compare(strSubstring, "-debug") == 0) {
                    this.debug = true;
                } else if (collator.compare(strSubstring, "-rfc") == 0) {
                    this.rfc = true;
                } else if (collator.compare(strSubstring, "-noprompt") == 0) {
                    this.noprompt = true;
                } else if (collator.compare(strSubstring, "-trustcacerts") == 0) {
                    this.trustcacerts = true;
                } else if (collator.compare(strSubstring, "-protected") == 0 || collator.compare(strSubstring, "-destprotected") == 0) {
                    this.protectedPath = true;
                } else if (collator.compare(strSubstring, "-srcprotected") == 0) {
                    this.srcprotectedPath = true;
                } else {
                    System.err.println(rb.getString("Illegal.option.") + strSubstring);
                    tinyHelp();
                }
            }
            i2++;
        }
        if (i2 < strArr.length) {
            System.err.println(rb.getString("Illegal.option.") + strArr[i2]);
            tinyHelp();
        }
        if (this.command == null) {
            if (z2) {
                usage();
                return;
            } else {
                System.err.println(rb.getString("Usage.error.no.command.provided"));
                tinyHelp();
                return;
            }
        }
        if (z2) {
            usage();
            this.command = null;
        }
    }

    boolean isKeyStoreRelated(Command command) {
        return (command == Command.PRINTCERT || command == Command.PRINTCERTREQ) ? false : true;
    }

    void doCommands(PrintStream printStream) throws Exception {
        PrintStream printStream2;
        String string;
        ClassLoader systemClassLoader;
        Class<?> cls;
        Object objNewInstance;
        if (P11KEYSTORE.equalsIgnoreCase(this.storetype) || KeyStoreUtil.isWindowsKeyStore(this.storetype)) {
            this.token = true;
            if (this.ksfname == null) {
                this.ksfname = NONE;
            }
        }
        if (NONE.equals(this.ksfname)) {
            this.nullStream = true;
        }
        if (this.token && !this.nullStream) {
            System.err.println(MessageFormat.format(rb.getString(".keystore.must.be.NONE.if.storetype.is.{0}"), this.storetype));
            System.err.println();
            tinyHelp();
        }
        if (this.token && (this.command == Command.KEYPASSWD || this.command == Command.STOREPASSWD)) {
            throw new UnsupportedOperationException(MessageFormat.format(rb.getString(".storepasswd.and.keypasswd.commands.not.supported.if.storetype.is.{0}"), this.storetype));
        }
        if (this.token && (this.keyPass != null || this.newPass != null || this.destKeyPass != null)) {
            throw new IllegalArgumentException(MessageFormat.format(rb.getString(".keypass.and.new.can.not.be.specified.if.storetype.is.{0}"), this.storetype));
        }
        if (this.protectedPath && (this.storePass != null || this.keyPass != null || this.newPass != null || this.destKeyPass != null)) {
            throw new IllegalArgumentException(rb.getString("if.protected.is.specified.then.storepass.keypass.and.new.must.not.be.specified"));
        }
        if (this.srcprotectedPath && (this.srcstorePass != null || this.srckeyPass != null)) {
            throw new IllegalArgumentException(rb.getString("if.srcprotected.is.specified.then.srcstorepass.and.srckeypass.must.not.be.specified"));
        }
        if (KeyStoreUtil.isWindowsKeyStore(this.storetype) && (this.storePass != null || this.keyPass != null || this.newPass != null || this.destKeyPass != null)) {
            throw new IllegalArgumentException(rb.getString("if.keystore.is.not.password.protected.then.storepass.keypass.and.new.must.not.be.specified"));
        }
        if (KeyStoreUtil.isWindowsKeyStore(this.srcstoretype) && (this.srcstorePass != null || this.srckeyPass != null)) {
            throw new IllegalArgumentException(rb.getString("if.source.keystore.is.not.password.protected.then.srcstorepass.and.srckeypass.must.not.be.specified"));
        }
        if (this.validity <= 0) {
            throw new Exception(rb.getString("Validity.must.be.greater.than.zero"));
        }
        if (this.providers != null) {
            if (this.pathlist != null) {
                systemClassLoader = new URLClassLoader(PathList.pathToURLs(PathList.appendPath(PathList.appendPath(PathList.appendPath(null, System.getProperty("java.class.path")), System.getProperty("env.class.path")), this.pathlist)));
            } else {
                systemClassLoader = ClassLoader.getSystemClassLoader();
            }
            for (Pair<String, String> pair : this.providers) {
                String str = pair.fst;
                if (systemClassLoader != null) {
                    cls = systemClassLoader.loadClass(str);
                } else {
                    cls = Class.forName(str);
                }
                String str2 = pair.snd;
                if (str2 == null) {
                    objNewInstance = cls.newInstance();
                } else {
                    objNewInstance = cls.getConstructor(PARAM_STRING).newInstance(str2);
                }
                if (!(objNewInstance instanceof Provider)) {
                    throw new Exception(new MessageFormat(rb.getString("provName.not.a.provider")).format(new Object[]{str}));
                }
                Security.addProvider((Provider) objNewInstance);
            }
        }
        if (this.command == Command.LIST && this.verbose && this.rfc) {
            System.err.println(rb.getString("Must.not.specify.both.v.and.rfc.with.list.command"));
            tinyHelp();
        }
        if (this.command == Command.GENKEYPAIR && this.keyPass != null && this.keyPass.length < 6) {
            throw new Exception(rb.getString("Key.password.must.be.at.least.6.characters"));
        }
        if (this.newPass != null && this.newPass.length < 6) {
            throw new Exception(rb.getString("New.password.must.be.at.least.6.characters"));
        }
        if (this.destKeyPass != null && this.destKeyPass.length < 6) {
            throw new Exception(rb.getString("New.password.must.be.at.least.6.characters"));
        }
        if (this.ksfname == null) {
            this.ksfname = System.getProperty("user.home") + File.separator + ".keystore";
        }
        KeyStore keyStoreLoadSourceKeyStore = null;
        if (this.command == Command.IMPORTKEYSTORE) {
            this.inplaceImport = inplaceImportCheck();
            if (this.inplaceImport) {
                keyStoreLoadSourceKeyStore = loadSourceKeyStore();
                if (this.storePass == null) {
                    this.storePass = this.srcstorePass;
                }
            }
        }
        if (isKeyStoreRelated(this.command) && !this.nullStream && !this.inplaceImport) {
            try {
                this.ksfile = new File(this.ksfname);
                if (this.ksfile.exists() && this.ksfile.length() == 0) {
                    throw new Exception(rb.getString("Keystore.file.exists.but.is.empty.") + this.ksfname);
                }
                this.ksStream = new FileInputStream(this.ksfile);
            } catch (FileNotFoundException e2) {
                if (this.command != Command.GENKEYPAIR && this.command != Command.GENSECKEY && this.command != Command.IDENTITYDB && this.command != Command.IMPORTCERT && this.command != Command.IMPORTPASS && this.command != Command.IMPORTKEYSTORE && this.command != Command.PRINTCRL) {
                    throw new Exception(rb.getString("Keystore.file.does.not.exist.") + this.ksfname);
                }
            }
        }
        if ((this.command == Command.KEYCLONE || this.command == Command.CHANGEALIAS) && this.dest == null) {
            this.dest = getAlias("destination");
            if ("".equals(this.dest)) {
                throw new Exception(rb.getString("Must.specify.destination.alias"));
            }
        }
        if (this.command == Command.DELETE && this.alias == null) {
            this.alias = getAlias(null);
            if ("".equals(this.alias)) {
                throw new Exception(rb.getString("Must.specify.alias"));
            }
        }
        if (this.storetype == null) {
            this.storetype = KeyStore.getDefaultType();
        }
        if (this.providerName == null) {
            this.keyStore = KeyStore.getInstance(this.storetype);
        } else {
            this.keyStore = KeyStore.getInstance(this.storetype, this.providerName);
        }
        if (!this.nullStream) {
            if (this.inplaceImport) {
                this.keyStore.load(null, this.storePass);
            } else {
                this.keyStore.load(this.ksStream, this.storePass);
            }
            if (this.ksStream != null) {
                this.ksStream.close();
            }
        }
        if (this.keyStore.getProvider().getName().equals("SunJSSE") && this.storetype.equalsIgnoreCase(P12KEYSTORE)) {
            if (this.ksfile != null && this.ksStream != null) {
                try {
                    this.isPasswordlessKeyStore = PKCS12KeyStore.isPasswordless(this.ksfile);
                } catch (IOException e3) {
                }
            } else {
                this.isPasswordlessKeyStore = NONE.equals(SecurityProperties.privilegedGetOverridable("keystore.pkcs12.certProtectionAlgorithm")) && NONE.equals(SecurityProperties.privilegedGetOverridable("keystore.pkcs12.macAlgorithm"));
            }
        }
        if (P12KEYSTORE.equalsIgnoreCase(this.storetype) && this.command == Command.KEYPASSWD) {
            throw new UnsupportedOperationException(rb.getString(".keypasswd.commands.not.supported.if.storetype.is.PKCS12"));
        }
        if (this.nullStream && this.storePass != null) {
            this.keyStore.load(null, this.storePass);
        } else if (!this.nullStream && this.storePass != null) {
            if (this.ksStream == null && this.storePass.length < 6) {
                throw new Exception(rb.getString("Keystore.password.must.be.at.least.6.characters"));
            }
        } else if (this.storePass == null) {
            if (!this.protectedPath && !KeyStoreUtil.isWindowsKeyStore(this.storetype) && isKeyStoreRelated(this.command) && !this.isPasswordlessKeyStore) {
                if (this.command == Command.CERTREQ || this.command == Command.DELETE || this.command == Command.GENKEYPAIR || this.command == Command.GENSECKEY || this.command == Command.IMPORTCERT || this.command == Command.IMPORTPASS || this.command == Command.IMPORTKEYSTORE || this.command == Command.KEYCLONE || this.command == Command.CHANGEALIAS || this.command == Command.SELFCERT || this.command == Command.STOREPASSWD || this.command == Command.KEYPASSWD || this.command == Command.IDENTITYDB) {
                    int i2 = 0;
                    do {
                        if (this.command == Command.IMPORTKEYSTORE) {
                            System.err.print(rb.getString("Enter.destination.keystore.password."));
                        } else {
                            System.err.print(rb.getString("Enter.keystore.password."));
                        }
                        System.err.flush();
                        this.storePass = Password.readPassword(System.in);
                        this.passwords.add(this.storePass);
                        if (!this.nullStream && (this.storePass == null || this.storePass.length < 6)) {
                            System.err.println(rb.getString("Keystore.password.is.too.short.must.be.at.least.6.characters"));
                            this.storePass = null;
                        }
                        if (this.storePass != null && !this.nullStream && this.ksStream == null) {
                            System.err.print(rb.getString("Re.enter.new.password."));
                            char[] password = Password.readPassword(System.in);
                            this.passwords.add(password);
                            if (!Arrays.equals(this.storePass, password)) {
                                System.err.println(rb.getString("They.don.t.match.Try.again"));
                                this.storePass = null;
                            }
                        }
                        i2++;
                        if (this.storePass != null) {
                            break;
                        }
                    } while (i2 < 3);
                    if (this.storePass == null) {
                        System.err.println(rb.getString("Too.many.failures.try.later"));
                        return;
                    }
                } else if (this.command != Command.PRINTCRL) {
                    System.err.print(rb.getString("Enter.keystore.password."));
                    System.err.flush();
                    this.storePass = Password.readPassword(System.in);
                    this.passwords.add(this.storePass);
                }
            }
            if (this.nullStream) {
                this.keyStore.load(null, this.storePass);
            } else if (this.ksStream != null) {
                this.ksStream = new FileInputStream(this.ksfile);
                this.keyStore.load(this.ksStream, this.storePass);
                this.ksStream.close();
            }
        }
        if (this.storePass != null && P12KEYSTORE.equalsIgnoreCase(this.storetype)) {
            MessageFormat messageFormat = new MessageFormat(rb.getString("Warning.Different.store.and.key.passwords.not.supported.for.PKCS12.KeyStores.Ignoring.user.specified.command.value."));
            if (this.keyPass != null && !Arrays.equals(this.storePass, this.keyPass)) {
                System.err.println(messageFormat.format(new Object[]{"-keypass"}));
                this.keyPass = this.storePass;
            }
            if (this.newPass != null && !Arrays.equals(this.storePass, this.newPass)) {
                System.err.println(messageFormat.format(new Object[]{"-new"}));
                this.newPass = this.storePass;
            }
            if (this.destKeyPass != null && !Arrays.equals(this.storePass, this.destKeyPass)) {
                System.err.println(messageFormat.format(new Object[]{"-destkeypass"}));
                this.destKeyPass = this.storePass;
            }
        }
        if (this.command == Command.PRINTCERT || this.command == Command.IMPORTCERT || this.command == Command.IDENTITYDB || this.command == Command.PRINTCRL) {
            this.cf = CertificateFactory.getInstance("X509");
        }
        if (this.command != Command.IMPORTCERT) {
            this.trustcacerts = false;
        }
        if (this.trustcacerts) {
            this.caks = KeyStoreUtil.getCacertsKeyStore();
        }
        if (this.command == Command.CERTREQ) {
            if (this.filename != null) {
                PrintStream printStream3 = new PrintStream(new FileOutputStream(this.filename));
                Throwable th = null;
                try {
                    doCertReq(this.alias, this.sigAlgName, printStream3);
                    if (printStream3 != null) {
                        if (0 != 0) {
                            try {
                                printStream3.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            printStream3.close();
                        }
                    }
                } catch (Throwable th3) {
                    if (printStream3 != null) {
                        if (0 != 0) {
                            try {
                                printStream3.close();
                            } catch (Throwable th4) {
                                th.addSuppressed(th4);
                            }
                        } else {
                            printStream3.close();
                        }
                    }
                    throw th3;
                }
            } else {
                doCertReq(this.alias, this.sigAlgName, printStream);
            }
            if (this.verbose && this.filename != null) {
                System.err.println(new MessageFormat(rb.getString("Certification.request.stored.in.file.filename.")).format(new Object[]{this.filename}));
                System.err.println(rb.getString("Submit.this.to.your.CA"));
            }
        } else if (this.command == Command.DELETE) {
            doDeleteEntry(this.alias);
            this.kssave = true;
        } else if (this.command == Command.EXPORTCERT) {
            if (this.filename != null) {
                printStream2 = new PrintStream(new FileOutputStream(this.filename));
                Throwable th5 = null;
                try {
                    try {
                        doExportCert(this.alias, printStream2);
                        if (printStream2 != null) {
                            if (0 != 0) {
                                try {
                                    printStream2.close();
                                } catch (Throwable th6) {
                                    th5.addSuppressed(th6);
                                }
                            } else {
                                printStream2.close();
                            }
                        }
                    } finally {
                    }
                } catch (Throwable th7) {
                    th5 = th7;
                    throw th7;
                }
            } else {
                doExportCert(this.alias, printStream);
            }
            if (this.filename != null) {
                System.err.println(new MessageFormat(rb.getString("Certificate.stored.in.file.filename.")).format(new Object[]{this.filename}));
            }
        } else if (this.command == Command.GENKEYPAIR) {
            if (this.keyAlgName == null) {
                this.keyAlgName = "DSA";
            }
            doGenKeyPair(this.alias, this.dname, this.keyAlgName, this.keysize, this.sigAlgName);
            this.kssave = true;
        } else if (this.command == Command.GENSECKEY) {
            if (this.keyAlgName == null) {
                this.keyAlgName = "DES";
            }
            doGenSecretKey(this.alias, this.keyAlgName, this.keysize);
            this.kssave = true;
        } else if (this.command == Command.IMPORTPASS) {
            if (this.keyAlgName == null) {
                this.keyAlgName = "PBE";
            }
            doGenSecretKey(this.alias, this.keyAlgName, this.keysize);
            this.kssave = true;
        } else if (this.command == Command.IDENTITYDB) {
            if (this.filename != null) {
                FileInputStream fileInputStream = new FileInputStream(this.filename);
                Throwable th8 = null;
                try {
                    doImportIdentityDatabase(fileInputStream);
                    if (fileInputStream != null) {
                        if (0 != 0) {
                            try {
                                fileInputStream.close();
                            } catch (Throwable th9) {
                                th8.addSuppressed(th9);
                            }
                        } else {
                            fileInputStream.close();
                        }
                    }
                } catch (Throwable th10) {
                    if (fileInputStream != null) {
                        if (0 != 0) {
                            try {
                                fileInputStream.close();
                            } catch (Throwable th11) {
                                th8.addSuppressed(th11);
                            }
                        } else {
                            fileInputStream.close();
                        }
                    }
                    throw th10;
                }
            } else {
                doImportIdentityDatabase(System.in);
            }
        } else if (this.command == Command.IMPORTCERT) {
            InputStream fileInputStream2 = System.in;
            if (this.filename != null) {
                fileInputStream2 = new FileInputStream(this.filename);
            }
            String str3 = this.alias != null ? this.alias : keyAlias;
            try {
                if (this.keyStore.entryInstanceOf(str3, KeyStore.PrivateKeyEntry.class)) {
                    this.kssave = installReply(str3, fileInputStream2);
                    if (this.kssave) {
                        System.err.println(rb.getString("Certificate.reply.was.installed.in.keystore"));
                    } else {
                        System.err.println(rb.getString("Certificate.reply.was.not.installed.in.keystore"));
                    }
                } else if (!this.keyStore.containsAlias(str3) || this.keyStore.entryInstanceOf(str3, KeyStore.TrustedCertificateEntry.class)) {
                    this.kssave = addTrustedCert(str3, fileInputStream2);
                    if (this.kssave) {
                        System.err.println(rb.getString("Certificate.was.added.to.keystore"));
                    } else {
                        System.err.println(rb.getString("Certificate.was.not.added.to.keystore"));
                    }
                }
            } finally {
                if (fileInputStream2 != System.in) {
                    fileInputStream2.close();
                }
            }
        } else if (this.command == Command.IMPORTKEYSTORE) {
            if (keyStoreLoadSourceKeyStore == null) {
                keyStoreLoadSourceKeyStore = loadSourceKeyStore();
            }
            doImportKeyStore(keyStoreLoadSourceKeyStore);
            this.kssave = true;
        } else if (this.command == Command.KEYCLONE) {
            this.keyPassNew = this.newPass;
            if (this.alias == null) {
                this.alias = keyAlias;
            }
            if (!this.keyStore.containsAlias(this.alias)) {
                throw new Exception(new MessageFormat(rb.getString("Alias.alias.does.not.exist")).format(new Object[]{this.alias}));
            }
            if (!this.keyStore.entryInstanceOf(this.alias, KeyStore.PrivateKeyEntry.class)) {
                throw new Exception(new MessageFormat(rb.getString("Alias.alias.references.an.entry.type.that.is.not.a.private.key.entry.The.keyclone.command.only.supports.cloning.of.private.key")).format(new Object[]{this.alias}));
            }
            doCloneEntry(this.alias, this.dest, true);
            this.kssave = true;
        } else if (this.command == Command.CHANGEALIAS) {
            if (this.alias == null) {
                this.alias = keyAlias;
            }
            doCloneEntry(this.alias, this.dest, false);
            if (this.keyStore.containsAlias(this.alias)) {
                doDeleteEntry(this.alias);
            }
            this.kssave = true;
        } else if (this.command == Command.KEYPASSWD) {
            this.keyPassNew = this.newPass;
            doChangeKeyPasswd(this.alias);
            this.kssave = true;
        } else if (this.command == Command.LIST) {
            if (this.storePass == null && !KeyStoreUtil.isWindowsKeyStore(this.storetype) && !this.isPasswordlessKeyStore) {
                printNoIntegrityWarning();
            }
            if (this.alias != null) {
                doPrintEntry(rb.getString("the.certificate"), this.alias, printStream);
            } else {
                doPrintEntries(printStream);
            }
        } else if (this.command == Command.PRINTCERT) {
            doPrintCert(printStream);
        } else if (this.command == Command.SELFCERT) {
            doSelfCert(this.alias, this.dname, this.sigAlgName);
            this.kssave = true;
        } else if (this.command == Command.STOREPASSWD) {
            this.storePassNew = this.newPass;
            if (this.storePassNew == null) {
                this.storePassNew = getNewPasswd("keystore password", this.storePass);
            }
            this.kssave = true;
        } else if (this.command == Command.GENCERT) {
            if (this.alias == null) {
                this.alias = keyAlias;
            }
            InputStream fileInputStream3 = System.in;
            if (this.infilename != null) {
                fileInputStream3 = new FileInputStream(this.infilename);
            }
            PrintStream printStream4 = null;
            if (this.outfilename != null) {
                printStream4 = new PrintStream(new FileOutputStream(this.outfilename));
                printStream = printStream4;
            }
            try {
                doGenCert(this.alias, this.sigAlgName, fileInputStream3, printStream);
                if (fileInputStream3 != System.in) {
                    fileInputStream3.close();
                }
                if (printStream4 != null) {
                    printStream4.close();
                }
            } catch (Throwable th12) {
                if (fileInputStream3 != System.in) {
                    fileInputStream3.close();
                }
                if (printStream4 != null) {
                    printStream4.close();
                }
                throw th12;
            }
        } else if (this.command == Command.GENCRL) {
            if (this.alias == null) {
                this.alias = keyAlias;
            }
            if (this.filename != null) {
                printStream2 = new PrintStream(new FileOutputStream(this.filename));
                Throwable th13 = null;
                try {
                    try {
                        doGenCRL(printStream2);
                        if (printStream2 != null) {
                            if (0 != 0) {
                                try {
                                    printStream2.close();
                                } catch (Throwable th14) {
                                    th13.addSuppressed(th14);
                                }
                            } else {
                                printStream2.close();
                            }
                        }
                    } finally {
                    }
                } catch (Throwable th15) {
                    th13 = th15;
                    throw th15;
                }
            } else {
                doGenCRL(printStream);
            }
        } else if (this.command == Command.PRINTCERTREQ) {
            if (this.filename != null) {
                FileInputStream fileInputStream4 = new FileInputStream(this.filename);
                Throwable th16 = null;
                try {
                    try {
                        doPrintCertReq(fileInputStream4, printStream);
                        if (fileInputStream4 != null) {
                            if (0 != 0) {
                                try {
                                    fileInputStream4.close();
                                } catch (Throwable th17) {
                                    th16.addSuppressed(th17);
                                }
                            } else {
                                fileInputStream4.close();
                            }
                        }
                    } catch (Throwable th18) {
                        if (fileInputStream4 != null) {
                            if (th16 != null) {
                                try {
                                    fileInputStream4.close();
                                } catch (Throwable th19) {
                                    th16.addSuppressed(th19);
                                }
                            } else {
                                fileInputStream4.close();
                            }
                        }
                        throw th18;
                    }
                } catch (Throwable th20) {
                    th16 = th20;
                    throw th20;
                }
            } else {
                doPrintCertReq(System.in, printStream);
            }
        } else if (this.command == Command.PRINTCRL) {
            doPrintCRL(this.filename, printStream);
        }
        if (this.kssave) {
            if (this.verbose) {
                MessageFormat messageFormat2 = new MessageFormat(rb.getString(".Storing.ksfname."));
                Object[] objArr = new Object[1];
                objArr[0] = this.nullStream ? "keystore" : this.ksfname;
                System.err.println(messageFormat2.format(objArr));
            }
            if (this.token) {
                this.keyStore.store(null, null);
            } else {
                char[] cArr = this.storePassNew != null ? this.storePassNew : this.storePass;
                if (this.nullStream) {
                    this.keyStore.store(null, cArr);
                } else {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    this.keyStore.store(byteArrayOutputStream, cArr);
                    FileOutputStream fileOutputStream = new FileOutputStream(this.ksfname);
                    Throwable th21 = null;
                    try {
                        try {
                            fileOutputStream.write(byteArrayOutputStream.toByteArray());
                            if (fileOutputStream != null) {
                                if (0 != 0) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (Throwable th22) {
                                        th21.addSuppressed(th22);
                                    }
                                } else {
                                    fileOutputStream.close();
                                }
                            }
                        } catch (Throwable th23) {
                            th21 = th23;
                            throw th23;
                        }
                    } catch (Throwable th24) {
                        if (fileOutputStream != null) {
                            if (th21 != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (Throwable th25) {
                                    th21.addSuppressed(th25);
                                }
                            } else {
                                fileOutputStream.close();
                            }
                        }
                        throw th24;
                    }
                }
            }
        }
        if (isKeyStoreRelated(this.command) && !this.token && !this.nullStream && this.ksfname != null) {
            File file = new File(this.ksfname);
            if (file.exists()) {
                String strKeyStoreType = keyStoreType(file);
                if (strKeyStoreType.equalsIgnoreCase("JKS") || strKeyStoreType.equalsIgnoreCase("JCEKS")) {
                    boolean z2 = true;
                    Iterator it = Collections.list(this.keyStore.aliases()).iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (!this.keyStore.entryInstanceOf((String) it.next(), KeyStore.TrustedCertificateEntry.class)) {
                                z2 = false;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (!z2) {
                        this.weakWarnings.add(String.format(rb.getString("jks.storetype.warning"), strKeyStoreType, this.ksfname));
                    }
                }
                if (this.inplaceImport) {
                    String strKeyStoreType2 = keyStoreType(new File(this.inplaceBackupName));
                    if (strKeyStoreType.equalsIgnoreCase(strKeyStoreType2)) {
                        string = rb.getString("backup.keystore.warning");
                    } else {
                        string = rb.getString("migrate.keystore.warning");
                    }
                    this.weakWarnings.add(String.format(string, this.srcksfname, strKeyStoreType2, this.inplaceBackupName, strKeyStoreType));
                }
            }
        }
    }

    private String keyStoreType(File file) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
        Throwable th = null;
        try {
            try {
                int i2 = dataInputStream.readInt();
                if (i2 == -17957139) {
                    if (dataInputStream != null) {
                        if (0 != 0) {
                            try {
                                dataInputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            dataInputStream.close();
                        }
                    }
                    return "JKS";
                }
                if (i2 == -825307442) {
                    if (dataInputStream != null) {
                        if (0 != 0) {
                            try {
                                dataInputStream.close();
                            } catch (Throwable th3) {
                                th.addSuppressed(th3);
                            }
                        } else {
                            dataInputStream.close();
                        }
                    }
                    return "JCEKS";
                }
                if (dataInputStream != null) {
                    if (0 != 0) {
                        try {
                            dataInputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        dataInputStream.close();
                    }
                }
                return "Non JKS/JCEKS";
            } finally {
            }
        } catch (Throwable th5) {
            if (dataInputStream != null) {
                if (th != null) {
                    try {
                        dataInputStream.close();
                    } catch (Throwable th6) {
                        th.addSuppressed(th6);
                    }
                } else {
                    dataInputStream.close();
                }
            }
            throw th5;
        }
    }

    private void doGenCert(String str, String str2, InputStream inputStream, PrintStream printStream) throws Exception {
        if (!this.keyStore.containsAlias(str)) {
            throw new Exception(new MessageFormat(rb.getString("Alias.alias.does.not.exist")).format(new Object[]{str}));
        }
        Certificate certificate = this.keyStore.getCertificate(str);
        X500Name x500Name = (X500Name) ((X509CertInfo) new X509CertImpl(certificate.getEncoded()).get(X509CertInfo.IDENT)).get("subject.dname");
        Date startDate = getStartDate(this.startDate);
        CertificateValidity certificateValidity = new CertificateValidity(startDate, getLastDate(startDate, this.validity));
        PrivateKey privateKey = (PrivateKey) recoverKey(str, this.storePass, this.keyPass).fst;
        if (str2 == null) {
            str2 = getCompatibleSigAlgName(privateKey.getAlgorithm());
        }
        Signature signature = Signature.getInstance(str2);
        PSSParameterSpec defaultAlgorithmParameterSpec = AlgorithmId.getDefaultAlgorithmParameterSpec(str2, privateKey);
        SignatureUtil.initSignWithParam(signature, privateKey, defaultAlgorithmParameterSpec, null);
        X509CertInfo x509CertInfo = new X509CertInfo();
        AlgorithmId withParameterSpec = AlgorithmId.getWithParameterSpec(str2, defaultAlgorithmParameterSpec);
        x509CertInfo.set("validity", certificateValidity);
        x509CertInfo.set("serialNumber", new CertificateSerialNumber(new Random().nextInt() & Integer.MAX_VALUE));
        x509CertInfo.set("version", new CertificateVersion(2));
        x509CertInfo.set("algorithmID", new CertificateAlgorithmId(withParameterSpec));
        x509CertInfo.set("issuer", x500Name);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        boolean z2 = false;
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            if (line.startsWith("-----BEGIN") && line.indexOf("REQUEST") >= 0) {
                z2 = true;
            } else if (line.startsWith("-----END") && line.indexOf("REQUEST") >= 0) {
                break;
            } else if (z2) {
                stringBuffer.append(line);
            }
        }
        PKCS10 pkcs10 = new PKCS10(Pem.decode(new String(stringBuffer)));
        checkWeak(rb.getString("the.certificate.request"), pkcs10);
        x509CertInfo.set("key", new CertificateX509Key(pkcs10.getSubjectPublicKeyInfo()));
        x509CertInfo.set("subject", this.dname == null ? pkcs10.getSubjectName() : new X500Name(this.dname));
        CertificateExtensions certificateExtensions = null;
        for (PKCS10Attribute pKCS10Attribute : pkcs10.getAttributes().getAttributes()) {
            if (pKCS10Attribute.getAttributeId().equals((Object) PKCS9Attribute.EXTENSION_REQUEST_OID)) {
                certificateExtensions = (CertificateExtensions) pKCS10Attribute.getAttributeValue();
            }
        }
        x509CertInfo.set("extensions", createV3Extensions(certificateExtensions, null, this.v3ext, pkcs10.getSubjectPublicKeyInfo(), certificate.getPublicKey()));
        X509CertImpl x509CertImpl = new X509CertImpl(x509CertInfo);
        x509CertImpl.sign(privateKey, defaultAlgorithmParameterSpec, str2, null);
        dumpCert(x509CertImpl, printStream);
        for (Certificate certificate2 : this.keyStore.getCertificateChain(str)) {
            if (certificate2 instanceof X509Certificate) {
                X509Certificate x509Certificate = (X509Certificate) certificate2;
                if (!KeyStoreUtil.isSelfSigned(x509Certificate)) {
                    dumpCert(x509Certificate, printStream);
                }
            }
        }
        checkWeak(rb.getString("the.issuer"), this.keyStore.getCertificateChain(str));
        checkWeak(rb.getString("the.generated.certificate"), x509CertImpl);
    }

    private void doGenCRL(PrintStream printStream) throws Exception {
        if (this.ids == null) {
            throw new Exception("Must provide -id when -gencrl");
        }
        X500Name x500Name = (X500Name) ((X509CertInfo) new X509CertImpl(this.keyStore.getCertificate(this.alias).getEncoded()).get(X509CertInfo.IDENT)).get("subject.dname");
        Date startDate = getStartDate(this.startDate);
        Date lastDate = getLastDate(startDate, this.validity);
        new CertificateValidity(startDate, lastDate);
        PrivateKey privateKey = (PrivateKey) recoverKey(this.alias, this.storePass, this.keyPass).fst;
        if (this.sigAlgName == null) {
            this.sigAlgName = getCompatibleSigAlgName(privateKey.getAlgorithm());
        }
        X509CRLEntry[] x509CRLEntryArr = new X509CRLEntry[this.ids.size()];
        for (int i2 = 0; i2 < this.ids.size(); i2++) {
            String str = this.ids.get(i2);
            int iIndexOf = str.indexOf(58);
            if (iIndexOf >= 0) {
                CRLExtensions cRLExtensions = new CRLExtensions();
                cRLExtensions.set("Reason", new CRLReasonCodeExtension(Integer.parseInt(str.substring(iIndexOf + 1))));
                x509CRLEntryArr[i2] = new X509CRLEntryImpl(new BigInteger(str.substring(0, iIndexOf)), startDate, cRLExtensions);
            } else {
                x509CRLEntryArr[i2] = new X509CRLEntryImpl(new BigInteger(this.ids.get(i2)), startDate);
            }
        }
        X509CRLImpl x509CRLImpl = new X509CRLImpl(x500Name, startDate, lastDate, x509CRLEntryArr);
        x509CRLImpl.sign(privateKey, this.sigAlgName);
        if (this.rfc) {
            printStream.println("-----BEGIN X509 CRL-----");
            printStream.println(Base64.getMimeEncoder(64, CRLF).encodeToString(x509CRLImpl.getEncodedInternal()));
            printStream.println("-----END X509 CRL-----");
        } else {
            printStream.write(x509CRLImpl.getEncodedInternal());
        }
        checkWeak(rb.getString("the.generated.crl"), x509CRLImpl, privateKey);
    }

    private void doCertReq(String str, String str2, PrintStream printStream) throws Exception {
        if (str == null) {
            str = keyAlias;
        }
        Pair<Key, char[]> pairRecoverKey = recoverKey(str, this.storePass, this.keyPass);
        PrivateKey privateKey = (PrivateKey) pairRecoverKey.fst;
        if (this.keyPass == null) {
            this.keyPass = pairRecoverKey.snd;
        }
        Certificate certificate = this.keyStore.getCertificate(str);
        if (certificate == null) {
            throw new Exception(new MessageFormat(rb.getString("alias.has.no.public.key.certificate.")).format(new Object[]{str}));
        }
        PKCS10 pkcs10 = new PKCS10(certificate.getPublicKey());
        pkcs10.getAttributes().setAttribute("extensions", new PKCS10Attribute(PKCS9Attribute.EXTENSION_REQUEST_OID, createV3Extensions(null, null, this.v3ext, certificate.getPublicKey(), null)));
        if (str2 == null) {
            str2 = getCompatibleSigAlgName(privateKey.getAlgorithm());
        }
        Signature signature = Signature.getInstance(str2);
        SignatureUtil.initSignWithParam(signature, privateKey, AlgorithmId.getDefaultAlgorithmParameterSpec(str2, privateKey), null);
        pkcs10.encodeAndSign(this.dname == null ? new X500Name(((X509Certificate) certificate).getSubjectDN().toString()) : new X500Name(this.dname), signature);
        pkcs10.print(printStream);
        checkWeak(rb.getString("the.generated.certificate.request"), pkcs10);
    }

    private void doDeleteEntry(String str) throws Exception {
        if (!this.keyStore.containsAlias(str)) {
            throw new Exception(new MessageFormat(rb.getString("Alias.alias.does.not.exist")).format(new Object[]{str}));
        }
        this.keyStore.deleteEntry(str);
    }

    private void doExportCert(String str, PrintStream printStream) throws Exception {
        if (this.storePass == null && !KeyStoreUtil.isWindowsKeyStore(this.storetype) && !this.isPasswordlessKeyStore) {
            printNoIntegrityWarning();
        }
        if (str == null) {
            str = keyAlias;
        }
        if (!this.keyStore.containsAlias(str)) {
            throw new Exception(new MessageFormat(rb.getString("Alias.alias.does.not.exist")).format(new Object[]{str}));
        }
        X509Certificate x509Certificate = (X509Certificate) this.keyStore.getCertificate(str);
        if (x509Certificate == null) {
            throw new Exception(new MessageFormat(rb.getString("Alias.alias.has.no.certificate")).format(new Object[]{str}));
        }
        dumpCert(x509Certificate, printStream);
        checkWeak(rb.getString("the.certificate"), x509Certificate);
    }

    private char[] promptForKeyPass(String str, String str2, char[] cArr) throws Exception {
        if (cArr != null && P12KEYSTORE.equalsIgnoreCase(this.storetype)) {
            return cArr;
        }
        if (!this.token && !this.protectedPath) {
            int i2 = 0;
            while (i2 < 3) {
                System.err.print(new MessageFormat(rb.getString("Enter.key.password.for.alias.")).format(new Object[]{str}));
                if (cArr != null) {
                    System.err.println();
                    if (str2 == null) {
                        System.err.print(rb.getString(".RETURN.if.same.as.keystore.password."));
                    } else {
                        System.err.print(new MessageFormat(rb.getString(".RETURN.if.same.as.for.otherAlias.")).format(new Object[]{str2}));
                    }
                }
                System.err.flush();
                char[] password = Password.readPassword(System.in);
                this.passwords.add(password);
                if (password == null && cArr != null) {
                    return cArr;
                }
                if (password != null && password.length >= 6) {
                    System.err.print(rb.getString("Re.enter.new.password."));
                    char[] password2 = Password.readPassword(System.in);
                    this.passwords.add(password2);
                    if (!Arrays.equals(password, password2)) {
                        System.err.println(rb.getString("They.don.t.match.Try.again"));
                    } else {
                        return password;
                    }
                } else {
                    System.err.println(rb.getString("Key.password.is.too.short.must.be.at.least.6.characters"));
                }
                i2++;
            }
            if (i2 == 3) {
                if (this.command == Command.KEYCLONE) {
                    throw new Exception(rb.getString("Too.many.failures.Key.entry.not.cloned"));
                }
                throw new Exception(rb.getString("Too.many.failures.key.not.added.to.keystore"));
            }
            return null;
        }
        return null;
    }

    private char[] promptForCredential() throws Exception {
        if (System.console() == null) {
            char[] password = Password.readPassword(System.in);
            this.passwords.add(password);
            return password;
        }
        int i2 = 0;
        while (i2 < 3) {
            System.err.print(rb.getString("Enter.the.password.to.be.stored."));
            System.err.flush();
            char[] password2 = Password.readPassword(System.in);
            this.passwords.add(password2);
            System.err.print(rb.getString("Re.enter.password."));
            char[] password3 = Password.readPassword(System.in);
            this.passwords.add(password3);
            if (!Arrays.equals(password2, password3)) {
                System.err.println(rb.getString("They.don.t.match.Try.again"));
                i2++;
            } else {
                return password2;
            }
        }
        if (i2 == 3) {
            throw new Exception(rb.getString("Too.many.failures.key.not.added.to.keystore"));
        }
        return null;
    }

    private void doGenSecretKey(String str, String str2, int i2) throws Exception {
        SecretKey secretKeyGenerateKey;
        if (str == null) {
            str = keyAlias;
        }
        if (this.keyStore.containsAlias(str)) {
            throw new Exception(new MessageFormat(rb.getString("Secret.key.not.generated.alias.alias.already.exists")).format(new Object[]{str}));
        }
        boolean z2 = true;
        if (str2.toUpperCase(Locale.ENGLISH).startsWith("PBE")) {
            secretKeyGenerateKey = SecretKeyFactory.getInstance("PBE").generateSecret(new PBEKeySpec(promptForCredential()));
            if (!"PBE".equalsIgnoreCase(str2)) {
                z2 = false;
            }
            if (this.verbose) {
                MessageFormat messageFormat = new MessageFormat(rb.getString("Generated.keyAlgName.secret.key"));
                Object[] objArr = new Object[1];
                objArr[0] = z2 ? "PBE" : secretKeyGenerateKey.getAlgorithm();
                System.err.println(messageFormat.format(objArr));
            }
        } else {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(str2);
            if (i2 == -1) {
                if ("DES".equalsIgnoreCase(str2)) {
                    i2 = 56;
                } else if ("DESede".equalsIgnoreCase(str2)) {
                    i2 = 168;
                } else {
                    throw new Exception(rb.getString("Please.provide.keysize.for.secret.key.generation"));
                }
            }
            keyGenerator.init(i2);
            secretKeyGenerateKey = keyGenerator.generateKey();
            if (this.verbose) {
                System.err.println(new MessageFormat(rb.getString("Generated.keysize.bit.keyAlgName.secret.key")).format(new Object[]{new Integer(i2), secretKeyGenerateKey.getAlgorithm()}));
            }
        }
        if (this.keyPass == null) {
            this.keyPass = promptForKeyPass(str, null, this.storePass);
        }
        if (z2) {
            this.keyStore.setKeyEntry(str, secretKeyGenerateKey, this.keyPass, null);
        } else {
            this.keyStore.setEntry(str, new KeyStore.SecretKeyEntry(secretKeyGenerateKey), new KeyStore.PasswordProtection(this.keyPass, str2, null));
        }
    }

    private static String getCompatibleSigAlgName(String str) throws Exception {
        if ("DSA".equalsIgnoreCase(str)) {
            return "SHA256WithDSA";
        }
        if ("RSA".equalsIgnoreCase(str)) {
            return "SHA256WithRSA";
        }
        if ("EC".equalsIgnoreCase(str)) {
            return "SHA256withECDSA";
        }
        throw new Exception(rb.getString("Cannot.derive.signature.algorithm"));
    }

    private void doGenKeyPair(String str, String str2, String str3, int i2, String str4) throws Exception {
        X500Name x500Name;
        if (i2 == -1) {
            if ("EC".equalsIgnoreCase(str3)) {
                i2 = SecurityProviderConstants.DEF_EC_KEY_SIZE;
            } else if ("RSA".equalsIgnoreCase(str3)) {
                i2 = SecurityProviderConstants.DEF_RSA_KEY_SIZE;
            } else if ("RSASSA-PSS".equalsIgnoreCase(str3)) {
                i2 = SecurityProviderConstants.DEF_RSASSA_PSS_KEY_SIZE;
            } else if ("DSA".equalsIgnoreCase(str3)) {
                i2 = SecurityProviderConstants.DEF_DSA_KEY_SIZE;
            }
        }
        if (str == null) {
            str = keyAlias;
        }
        if (this.keyStore.containsAlias(str)) {
            throw new Exception(new MessageFormat(rb.getString("Key.pair.not.generated.alias.alias.already.exists")).format(new Object[]{str}));
        }
        if (str4 == null) {
            str4 = getCompatibleSigAlgName(str3);
        }
        CertAndKeyGen certAndKeyGen = new CertAndKeyGen(str3, str4, this.providerName);
        if (str2 == null) {
            x500Name = getX500Name();
        } else {
            x500Name = new X500Name(str2);
        }
        certAndKeyGen.generate(i2);
        PrivateKey privateKey = certAndKeyGen.getPrivateKey();
        X509Certificate[] x509CertificateArr = {certAndKeyGen.getSelfCertificate(x500Name, getStartDate(this.startDate), this.validity * 24 * 60 * 60, createV3Extensions(null, null, this.v3ext, certAndKeyGen.getPublicKeyAnyway(), null))};
        if (this.verbose) {
            System.err.println(new MessageFormat(rb.getString("Generating.keysize.bit.keyAlgName.key.pair.and.self.signed.certificate.sigAlgName.with.a.validity.of.validality.days.for")).format(new Object[]{new Integer(i2), privateKey.getAlgorithm(), x509CertificateArr[0].getSigAlgName(), new Long(this.validity), x500Name}));
        }
        if (this.keyPass == null) {
            this.keyPass = promptForKeyPass(str, null, this.storePass);
        }
        checkWeak(rb.getString("the.generated.certificate"), x509CertificateArr[0]);
        this.keyStore.setKeyEntry(str, privateKey, this.keyPass, x509CertificateArr);
    }

    private void doCloneEntry(String str, String str2, boolean z2) throws Exception {
        if (str == null) {
            str = keyAlias;
        }
        if (this.keyStore.containsAlias(str2)) {
            throw new Exception(new MessageFormat(rb.getString("Destination.alias.dest.already.exists")).format(new Object[]{str2}));
        }
        Pair<KeyStore.Entry, char[]> pairRecoverEntry = recoverEntry(this.keyStore, str, this.storePass, this.keyPass);
        KeyStore.Entry entry = pairRecoverEntry.fst;
        this.keyPass = pairRecoverEntry.snd;
        KeyStore.PasswordProtection passwordProtection = null;
        if (this.keyPass != null) {
            if (!z2 || P12KEYSTORE.equalsIgnoreCase(this.storetype)) {
                this.keyPassNew = this.keyPass;
            } else if (this.keyPassNew == null) {
                this.keyPassNew = promptForKeyPass(str2, str, this.keyPass);
            }
            passwordProtection = new KeyStore.PasswordProtection(this.keyPassNew);
        }
        this.keyStore.setEntry(str2, entry, passwordProtection);
    }

    private void doChangeKeyPasswd(String str) throws Exception {
        if (str == null) {
            str = keyAlias;
        }
        Pair<Key, char[]> pairRecoverKey = recoverKey(str, this.storePass, this.keyPass);
        Key key = pairRecoverKey.fst;
        if (this.keyPass == null) {
            this.keyPass = pairRecoverKey.snd;
        }
        if (this.keyPassNew == null) {
            this.keyPassNew = getNewPasswd(new MessageFormat(rb.getString("key.password.for.alias.")).format(new Object[]{str}), this.keyPass);
        }
        this.keyStore.setKeyEntry(str, key, this.keyPassNew, this.keyStore.getCertificateChain(str));
    }

    private void doImportIdentityDatabase(InputStream inputStream) throws Exception {
        System.err.println(rb.getString("No.entries.from.identity.database.added"));
    }

    private void doPrintEntry(String str, String str2, PrintStream printStream) throws Exception {
        if (!this.keyStore.containsAlias(str2)) {
            throw new Exception(new MessageFormat(rb.getString("Alias.alias.does.not.exist")).format(new Object[]{str2}));
        }
        if (this.verbose || this.rfc || this.debug) {
            printStream.println(new MessageFormat(rb.getString("Alias.name.alias")).format(new Object[]{str2}));
            if (!this.token) {
                printStream.println(new MessageFormat(rb.getString("Creation.date.keyStore.getCreationDate.alias.")).format(new Object[]{this.keyStore.getCreationDate(str2)}));
            }
        } else if (!this.token) {
            printStream.print(new MessageFormat(rb.getString("alias.keyStore.getCreationDate.alias.")).format(new Object[]{str2, this.keyStore.getCreationDate(str2)}));
        } else {
            printStream.print(new MessageFormat(rb.getString("alias.")).format(new Object[]{str2}));
        }
        if (this.keyStore.entryInstanceOf(str2, KeyStore.SecretKeyEntry.class)) {
            if (this.verbose || this.rfc || this.debug) {
                printStream.println(new MessageFormat(rb.getString("Entry.type.type.")).format(new Object[]{"SecretKeyEntry"}));
                return;
            } else {
                printStream.println("SecretKeyEntry, ");
                return;
            }
        }
        if (!this.keyStore.entryInstanceOf(str2, KeyStore.PrivateKeyEntry.class)) {
            if (this.keyStore.entryInstanceOf(str2, KeyStore.TrustedCertificateEntry.class)) {
                Certificate certificate = this.keyStore.getCertificate(str2);
                String str3 = new MessageFormat(rb.getString("Entry.type.type.")).format(new Object[]{"trustedCertEntry"}) + "\n";
                if (this.verbose && (certificate instanceof X509Certificate)) {
                    printStream.println(str3);
                    printX509Cert((X509Certificate) certificate, printStream);
                } else if (this.rfc) {
                    printStream.println(str3);
                    dumpCert(certificate, printStream);
                } else if (this.debug) {
                    for (KeyStore.Entry.Attribute attribute : this.keyStore.getEntry(str2, null).getAttributes()) {
                        System.out.println("Attribute " + attribute.getName() + ": " + attribute.getValue());
                    }
                    printStream.println(certificate.toString());
                } else {
                    printStream.println("trustedCertEntry, ");
                    printStream.println(rb.getString("Certificate.fingerprint.SHA.256.") + getCertFingerPrint("SHA-256", certificate));
                }
                checkWeak(str, certificate);
                return;
            }
            printStream.println(rb.getString("Unknown.Entry.Type"));
            return;
        }
        if (this.verbose || this.rfc || this.debug) {
            printStream.println(new MessageFormat(rb.getString("Entry.type.type.")).format(new Object[]{"PrivateKeyEntry"}));
        } else {
            printStream.println("PrivateKeyEntry, ");
        }
        Certificate[] certificateChain = this.keyStore.getCertificateChain(str2);
        if (certificateChain != null) {
            if (this.verbose || this.rfc || this.debug) {
                printStream.println(rb.getString("Certificate.chain.length.") + certificateChain.length);
                for (int i2 = 0; i2 < certificateChain.length; i2++) {
                    printStream.println(new MessageFormat(rb.getString("Certificate.i.1.")).format(new Object[]{new Integer(i2 + 1)}));
                    if (this.verbose && (certificateChain[i2] instanceof X509Certificate)) {
                        printX509Cert((X509Certificate) certificateChain[i2], printStream);
                    } else if (this.debug) {
                        printStream.println(certificateChain[i2].toString());
                    } else {
                        dumpCert(certificateChain[i2], printStream);
                    }
                    checkWeak(str, certificateChain[i2]);
                }
                return;
            }
            printStream.println(rb.getString("Certificate.fingerprint.SHA.256.") + getCertFingerPrint("SHA-256", certificateChain[0]));
            checkWeak(str, certificateChain[0]);
            return;
        }
        printStream.println(rb.getString("Certificate.chain.length.") + 0);
    }

    boolean inplaceImportCheck() throws Exception {
        if (P11KEYSTORE.equalsIgnoreCase(this.srcstoretype) || KeyStoreUtil.isWindowsKeyStore(this.srcstoretype)) {
            return false;
        }
        if (this.srcksfname != null) {
            File file = new File(this.srcksfname);
            if (file.exists() && file.length() == 0) {
                throw new Exception(rb.getString("Source.keystore.file.exists.but.is.empty.") + this.srcksfname);
            }
            if (file.getCanonicalFile().equals(new File(this.ksfname).getCanonicalFile())) {
                return true;
            }
            System.err.println(String.format(rb.getString("importing.keystore.status"), this.srcksfname, this.ksfname));
            return false;
        }
        throw new Exception(rb.getString("Please.specify.srckeystore"));
    }

    KeyStore loadSourceKeyStore() throws Exception {
        KeyStore keyStore;
        FileInputStream fileInputStream = null;
        File file = null;
        boolean zIsPasswordless = false;
        if (P11KEYSTORE.equalsIgnoreCase(this.srcstoretype) || KeyStoreUtil.isWindowsKeyStore(this.srcstoretype)) {
            if (!NONE.equals(this.srcksfname)) {
                System.err.println(MessageFormat.format(rb.getString(".keystore.must.be.NONE.if.storetype.is.{0}"), this.srcstoretype));
                System.err.println();
                tinyHelp();
            }
        } else {
            file = new File(this.srcksfname);
            fileInputStream = new FileInputStream(file);
        }
        try {
            if (this.srcstoretype == null) {
                this.srcstoretype = KeyStore.getDefaultType();
            }
            if (this.srcProviderName == null) {
                keyStore = KeyStore.getInstance(this.srcstoretype);
            } else {
                keyStore = KeyStore.getInstance(this.srcstoretype, this.srcProviderName);
            }
            if (keyStore.getProvider().getName().equals("SunJSSE") && this.srcstoretype.equalsIgnoreCase(P12KEYSTORE) && file != null && fileInputStream != null) {
                try {
                    zIsPasswordless = PKCS12KeyStore.isPasswordless(file);
                } catch (IOException e2) {
                }
            }
            if (this.srcstorePass == null && !this.srcprotectedPath && !KeyStoreUtil.isWindowsKeyStore(this.srcstoretype) && !zIsPasswordless) {
                System.err.print(rb.getString("Enter.source.keystore.password."));
                System.err.flush();
                this.srcstorePass = Password.readPassword(System.in);
                this.passwords.add(this.srcstorePass);
            }
            if (P12KEYSTORE.equalsIgnoreCase(this.srcstoretype) && this.srckeyPass != null && this.srcstorePass != null && !Arrays.equals(this.srcstorePass, this.srckeyPass)) {
                System.err.println(new MessageFormat(rb.getString("Warning.Different.store.and.key.passwords.not.supported.for.PKCS12.KeyStores.Ignoring.user.specified.command.value.")).format(new Object[]{"-srckeypass"}));
                this.srckeyPass = this.srcstorePass;
            }
            keyStore.load(fileInputStream, this.srcstorePass);
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (this.srcstorePass == null && !zIsPasswordless && !KeyStoreUtil.isWindowsKeyStore(this.srcstoretype)) {
                System.err.println();
                System.err.println(rb.getString(".WARNING.WARNING.WARNING."));
                System.err.println(rb.getString(".The.integrity.of.the.information.stored.in.the.srckeystore."));
                System.err.println(rb.getString(".WARNING.WARNING.WARNING."));
                System.err.println();
            }
            return keyStore;
        } catch (Throwable th) {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            throw th;
        }
    }

    private void doImportKeyStore(KeyStore keyStore) throws Exception {
        if (this.alias != null) {
            doImportKeyStoreSingle(keyStore, this.alias);
        } else {
            if (this.dest != null || this.srckeyPass != null) {
                throw new Exception(rb.getString("if.alias.not.specified.destalias.and.srckeypass.must.not.be.specified"));
            }
            doImportKeyStoreAll(keyStore);
        }
        if (this.inplaceImport) {
            int i2 = 1;
            while (true) {
                this.inplaceBackupName = this.srcksfname + ".old" + (i2 == 1 ? "" : Integer.valueOf(i2));
                File file = new File(this.inplaceBackupName);
                if (file.exists()) {
                    i2++;
                } else {
                    Files.copy(Paths.get(this.srcksfname, new String[0]), file.toPath(), new CopyOption[0]);
                    return;
                }
            }
        }
    }

    private int doImportKeyStoreSingle(KeyStore keyStore, String str) throws Exception {
        String strInputStringFromStdin = this.dest == null ? str : this.dest;
        if (this.keyStore.containsAlias(strInputStringFromStdin)) {
            Object[] objArr = {str};
            if (this.noprompt) {
                System.err.println(new MessageFormat(rb.getString("Warning.Overwriting.existing.alias.alias.in.destination.keystore")).format(objArr));
            } else if ("NO".equals(getYesNoReply(new MessageFormat(rb.getString("Existing.entry.alias.alias.exists.overwrite.no.")).format(objArr)))) {
                strInputStringFromStdin = inputStringFromStdin(rb.getString("Enter.new.alias.name.RETURN.to.cancel.import.for.this.entry."));
                if ("".equals(strInputStringFromStdin)) {
                    System.err.println(new MessageFormat(rb.getString("Entry.for.alias.alias.not.imported.")).format(objArr));
                    return 0;
                }
            }
        }
        Pair<KeyStore.Entry, char[]> pairRecoverEntry = recoverEntry(keyStore, str, this.srcstorePass, this.srckeyPass);
        KeyStore.Entry entry = pairRecoverEntry.fst;
        KeyStore.PasswordProtection passwordProtection = null;
        char[] cArr = null;
        if (this.destKeyPass != null) {
            cArr = this.destKeyPass;
            passwordProtection = new KeyStore.PasswordProtection(this.destKeyPass);
        } else if (pairRecoverEntry.snd != null) {
            cArr = pairRecoverEntry.snd;
            passwordProtection = new KeyStore.PasswordProtection(pairRecoverEntry.snd);
        }
        try {
            Certificate certificate = keyStore.getCertificate(str);
            if (certificate != null) {
                checkWeak("<" + strInputStringFromStdin + ">", certificate);
            }
            this.keyStore.setEntry(strInputStringFromStdin, entry, passwordProtection);
            if (P12KEYSTORE.equalsIgnoreCase(this.storetype) && cArr != null && !Arrays.equals(cArr, this.storePass)) {
                throw new Exception(rb.getString("The.destination.pkcs12.keystore.has.different.storepass.and.keypass.Please.retry.with.destkeypass.specified."));
            }
            return 1;
        } catch (KeyStoreException e2) {
            System.err.println(new MessageFormat(rb.getString("Problem.importing.entry.for.alias.alias.exception.Entry.for.alias.alias.not.imported.")).format(new Object[]{str, e2.toString()}));
            return 2;
        }
    }

    private void doImportKeyStoreAll(KeyStore keyStore) throws Exception {
        int i2 = 0;
        int size = keyStore.size();
        Enumeration<String> enumerationAliases = keyStore.aliases();
        while (enumerationAliases.hasMoreElements()) {
            String strNextElement2 = enumerationAliases.nextElement2();
            int iDoImportKeyStoreSingle = doImportKeyStoreSingle(keyStore, strNextElement2);
            if (iDoImportKeyStoreSingle == 1) {
                i2++;
                System.err.println(new MessageFormat(rb.getString("Entry.for.alias.alias.successfully.imported.")).format(new Object[]{strNextElement2}));
            } else if (iDoImportKeyStoreSingle == 2 && !this.noprompt && "YES".equals(getYesNoReply("Do you want to quit the import process? [no]:  "))) {
                break;
            }
        }
        System.err.println(new MessageFormat(rb.getString("Import.command.completed.ok.entries.successfully.imported.fail.entries.failed.or.cancelled")).format(new Object[]{Integer.valueOf(i2), Integer.valueOf(size - i2)}));
    }

    private void doPrintEntries(PrintStream printStream) throws Exception {
        MessageFormat messageFormat;
        String type = this.keyStore.getType();
        if ("JKS".equalsIgnoreCase(type) && this.ksfile != null && this.ksfile.exists() && !"JKS".equalsIgnoreCase(keyStoreType(this.ksfile))) {
            type = P12KEYSTORE;
        }
        printStream.println(rb.getString("Keystore.type.") + type);
        printStream.println(rb.getString("Keystore.provider.") + this.keyStore.getProvider().getName());
        printStream.println();
        if (this.keyStore.size() == 1) {
            messageFormat = new MessageFormat(rb.getString("Your.keystore.contains.keyStore.size.entry"));
        } else {
            messageFormat = new MessageFormat(rb.getString("Your.keystore.contains.keyStore.size.entries"));
        }
        printStream.println(messageFormat.format(new Object[]{new Integer(this.keyStore.size())}));
        printStream.println();
        ArrayList<String> list = Collections.list(this.keyStore.aliases());
        list.sort((v0, v1) -> {
            return v0.compareTo(v1);
        });
        for (String str : list) {
            doPrintEntry("<" + str + ">", str, printStream);
            if (this.verbose || this.rfc) {
                printStream.println(rb.getString("NEWLINE"));
                printStream.println(rb.getString("STAR"));
                printStream.println(rb.getString("STARNN"));
            }
        }
    }

    private static <T> Iterable<T> e2i(final Enumeration<T> enumeration) {
        return new Iterable<T>() { // from class: sun.security.tools.keytool.Main.1
            @Override // java.lang.Iterable, java.util.List
            public Iterator<T> iterator() {
                return new Iterator<T>() { // from class: sun.security.tools.keytool.Main.1.1
                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return enumeration.hasMoreElements();
                    }

                    @Override // java.util.Iterator
                    public T next() {
                        return (T) enumeration.nextElement2();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                };
            }
        };
    }

    /* JADX WARN: Finally extract failed */
    public static Collection<? extends CRL> loadCRLs(String str) throws Exception {
        InputStream fileInputStream = null;
        URI uri = null;
        if (str == null) {
            fileInputStream = System.in;
        } else {
            try {
                uri = new URI(str);
                if (!uri.getScheme().equals("ldap")) {
                    fileInputStream = uri.toURL().openStream();
                }
            } catch (Exception e2) {
                try {
                    fileInputStream = new FileInputStream(str);
                } catch (Exception e3) {
                    if (uri == null || uri.getScheme() == null) {
                        throw e3;
                    }
                    throw e2;
                }
            }
        }
        if (fileInputStream != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[4096];
                while (true) {
                    int i2 = fileInputStream.read(bArr);
                    if (i2 < 0) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, i2);
                }
                Collection<? extends CRL> collectionGenerateCRLs = CertificateFactory.getInstance("X509").generateCRLs(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                if (fileInputStream != System.in) {
                    fileInputStream.close();
                }
                return collectionGenerateCRLs;
            } catch (Throwable th) {
                if (fileInputStream != System.in) {
                    fileInputStream.close();
                }
                throw th;
            }
        }
        CertStoreHelper certStoreHelper = CertStoreHelper.getInstance("LDAP");
        String path = uri.getPath();
        if (path.charAt(0) == '/') {
            path = path.substring(1);
        }
        return certStoreHelper.getCertStore(uri).getCRLs(certStoreHelper.wrap(new X509CRLSelector(), (Collection<X500Principal>) null, path));
    }

    public static List<CRL> readCRLsFromCert(X509Certificate x509Certificate) throws Exception {
        ArrayList arrayList = new ArrayList();
        CRLDistributionPointsExtension cRLDistributionPointsExtension = X509CertImpl.toImpl(x509Certificate).getCRLDistributionPointsExtension();
        if (cRLDistributionPointsExtension == null) {
            return arrayList;
        }
        Iterator<DistributionPoint> it = cRLDistributionPointsExtension.get(CRLDistributionPointsExtension.POINTS).iterator();
        while (it.hasNext()) {
            GeneralNames fullName = it.next().getFullName();
            if (fullName != null) {
                Iterator<GeneralName> it2 = fullName.names().iterator();
                while (true) {
                    if (it2.hasNext()) {
                        GeneralName next = it2.next();
                        if (next.getType() == 6) {
                            for (CRL crl : loadCRLs(((URIName) next.getName()).getName())) {
                                if (crl instanceof X509CRL) {
                                    arrayList.add((X509CRL) crl);
                                }
                            }
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    private static String verifyCRL(KeyStore keyStore, CRL crl) throws Exception {
        X500Principal issuerX500Principal = ((X509CRLImpl) crl).getIssuerX500Principal();
        for (String str : e2i(keyStore.aliases())) {
            Certificate certificate = keyStore.getCertificate(str);
            if ((certificate instanceof X509Certificate) && ((X509Certificate) certificate).getSubjectX500Principal().equals(issuerX500Principal)) {
                try {
                    ((X509CRLImpl) crl).verify(certificate.getPublicKey());
                    return str;
                } catch (Exception e2) {
                }
            }
        }
        return null;
    }

    private void doPrintCRL(String str, PrintStream printStream) throws Exception {
        for (CRL crl : loadCRLs(str)) {
            printCRL(crl, printStream);
            String strVerifyCRL = null;
            Certificate certificate = null;
            if (this.caks != null) {
                strVerifyCRL = verifyCRL(this.caks, crl);
                if (strVerifyCRL != null) {
                    certificate = this.caks.getCertificate(strVerifyCRL);
                    printStream.printf(rb.getString("verified.by.s.in.s.weak"), strVerifyCRL, "cacerts", withWeak(certificate.getPublicKey()));
                    printStream.println();
                }
            }
            if (strVerifyCRL == null && this.keyStore != null) {
                strVerifyCRL = verifyCRL(this.keyStore, crl);
                if (strVerifyCRL != null) {
                    certificate = this.keyStore.getCertificate(strVerifyCRL);
                    printStream.printf(rb.getString("verified.by.s.in.s.weak"), strVerifyCRL, "keystore", withWeak(certificate.getPublicKey()));
                    printStream.println();
                }
            }
            if (strVerifyCRL == null) {
                printStream.println(rb.getString("STAR"));
                printStream.println(rb.getString("warning.not.verified.make.sure.keystore.is.correct"));
                printStream.println(rb.getString("STARNN"));
            }
            checkWeak(rb.getString("the.crl"), crl, certificate == null ? null : certificate.getPublicKey());
        }
    }

    private void printCRL(CRL crl, PrintStream printStream) throws Exception {
        String string;
        X509CRL x509crl = (X509CRL) crl;
        if (this.rfc) {
            printStream.println("-----BEGIN X509 CRL-----");
            printStream.println(Base64.getMimeEncoder(64, CRLF).encodeToString(x509crl.getEncoded()));
            printStream.println("-----END X509 CRL-----");
        } else {
            if (crl instanceof X509CRLImpl) {
                X509CRLImpl x509CRLImpl = (X509CRLImpl) crl;
                string = x509CRLImpl.toStringWithAlgName(withWeak("" + ((Object) x509CRLImpl.getSigAlgId())));
            } else {
                string = crl.toString();
            }
            printStream.println(string);
        }
    }

    private void doPrintCertReq(InputStream inputStream, PrintStream printStream) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer stringBuffer = new StringBuffer();
        boolean z2 = false;
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            if (!z2) {
                if (line.startsWith("-----")) {
                    z2 = true;
                }
            } else if (line.startsWith("-----")) {
                break;
            } else {
                stringBuffer.append(line);
            }
        }
        PKCS10 pkcs10 = new PKCS10(Pem.decode(new String(stringBuffer)));
        PublicKey subjectPublicKeyInfo = pkcs10.getSubjectPublicKeyInfo();
        printStream.printf(rb.getString("PKCS.10.with.weak"), pkcs10.getSubjectName(), subjectPublicKeyInfo.getFormat(), withWeak(subjectPublicKeyInfo), withWeak(pkcs10.getSigAlg()));
        for (PKCS10Attribute pKCS10Attribute : pkcs10.getAttributes().getAttributes()) {
            if (pKCS10Attribute.getAttributeId().equals((Object) PKCS9Attribute.EXTENSION_REQUEST_OID)) {
                CertificateExtensions certificateExtensions = (CertificateExtensions) pKCS10Attribute.getAttributeValue();
                if (certificateExtensions != null) {
                    printExtensions(rb.getString("Extension.Request."), certificateExtensions, printStream);
                }
            } else {
                printStream.println("Attribute: " + ((Object) pKCS10Attribute.getAttributeId()));
                printStream.print(new PKCS9Attribute(pKCS10Attribute.getAttributeId(), pKCS10Attribute.getAttributeValue()).getName() + ": ");
                Object attributeValue = pKCS10Attribute.getAttributeValue();
                printStream.println(attributeValue instanceof String[] ? Arrays.toString((String[]) attributeValue) : attributeValue);
            }
        }
        if (this.debug) {
            printStream.println(pkcs10);
        }
        checkWeak(rb.getString("the.certificate.request"), pkcs10);
    }

    private void printCertFromStream(InputStream inputStream, PrintStream printStream) throws Exception {
        try {
            Collection<? extends Certificate> collectionGenerateCertificates = this.cf.generateCertificates(inputStream);
            if (collectionGenerateCertificates.isEmpty()) {
                throw new Exception(rb.getString("Empty.input"));
            }
            Certificate[] certificateArr = (Certificate[]) collectionGenerateCertificates.toArray(new Certificate[collectionGenerateCertificates.size()]);
            for (int i2 = 0; i2 < certificateArr.length; i2++) {
                try {
                    X509Certificate x509Certificate = (X509Certificate) certificateArr[i2];
                    if (certificateArr.length > 1) {
                        printStream.println(new MessageFormat(rb.getString("Certificate.i.1.")).format(new Object[]{new Integer(i2 + 1)}));
                    }
                    if (this.rfc) {
                        dumpCert(x509Certificate, printStream);
                    } else {
                        printX509Cert(x509Certificate, printStream);
                    }
                    if (i2 < certificateArr.length - 1) {
                        printStream.println();
                    }
                    checkWeak(oneInMany(rb.getString("the.certificate"), i2, certificateArr.length), x509Certificate);
                } catch (ClassCastException e2) {
                    throw new Exception(rb.getString("Not.X.509.certificate"));
                }
            }
        } catch (CertificateException e3) {
            throw new Exception(rb.getString("Failed.to.parse.input"), e3);
        }
    }

    private static String oneInMany(String str, int i2, int i3) {
        return i3 == 1 ? str : String.format(rb.getString("one.in.many"), str, Integer.valueOf(i2 + 1), Integer.valueOf(i3));
    }

    private void doPrintCert(PrintStream printStream) throws Exception {
        if (this.jarfile == null) {
            if (this.sslserver != null) {
                try {
                    Collection<? extends Certificate> certificates = CertStoreHelper.getInstance("SSLServer").getCertStore(new URI("https://" + this.sslserver)).getCertificates(null);
                    if (certificates.isEmpty()) {
                        throw new Exception(rb.getString("No.certificate.from.the.SSL.server"));
                    }
                    int i2 = 0;
                    for (Certificate certificate : certificates) {
                        try {
                            if (this.rfc) {
                                dumpCert(certificate, printStream);
                            } else {
                                printStream.println("Certificate #" + i2);
                                printStream.println("====================================");
                                printX509Cert((X509Certificate) certificate, printStream);
                                printStream.println();
                            }
                            int i3 = i2;
                            i2++;
                            checkWeak(oneInMany(rb.getString("the.certificate"), i3, certificates.size()), certificate);
                        } catch (Exception e2) {
                            if (this.debug) {
                                e2.printStackTrace();
                            }
                        }
                    }
                    return;
                } catch (CertStoreException e3) {
                    if (e3.getCause() instanceof IOException) {
                        throw new Exception(rb.getString("No.certificate.from.the.SSL.server"), e3.getCause());
                    }
                    throw e3;
                }
            }
            if (this.filename != null) {
                InputStream fileInputStream = new FileInputStream(this.filename);
                Throwable th = null;
                try {
                    try {
                        printCertFromStream(fileInputStream, printStream);
                        if (fileInputStream != null) {
                            if (0 != 0) {
                                try {
                                    fileInputStream.close();
                                    return;
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                    return;
                                }
                            }
                            fileInputStream.close();
                            return;
                        }
                        return;
                    } catch (Throwable th3) {
                        th = th3;
                        throw th3;
                    }
                } catch (Throwable th4) {
                    if (fileInputStream != null) {
                        if (th != null) {
                            try {
                                fileInputStream.close();
                            } catch (Throwable th5) {
                                th.addSuppressed(th5);
                            }
                        } else {
                            fileInputStream.close();
                        }
                    }
                    throw th4;
                }
            }
            printCertFromStream(System.in, printStream);
            return;
        }
        JarFile jarFile = new JarFile(this.jarfile, true);
        Enumeration<JarEntry> enumerationEntries = jarFile.entries();
        HashSet hashSet = new HashSet();
        byte[] bArr = new byte[8192];
        int i4 = 0;
        while (enumerationEntries.hasMoreElements()) {
            JarEntry jarEntryNextElement2 = enumerationEntries.nextElement2();
            InputStream inputStream = jarFile.getInputStream(jarEntryNextElement2);
            Throwable th6 = null;
            do {
                try {
                    try {
                    } catch (Throwable th7) {
                        th6 = th7;
                        throw th7;
                    }
                } catch (Throwable th8) {
                    if (inputStream != null) {
                        if (th6 != null) {
                            try {
                                inputStream.close();
                            } catch (Throwable th9) {
                                th6.addSuppressed(th9);
                            }
                        } else {
                            inputStream.close();
                        }
                    }
                    throw th8;
                }
            } while (inputStream.read(bArr) != -1);
            if (inputStream != null) {
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (Throwable th10) {
                        th6.addSuppressed(th10);
                    }
                } else {
                    inputStream.close();
                }
            }
            CodeSigner[] codeSigners = jarEntryNextElement2.getCodeSigners();
            if (codeSigners != null) {
                for (CodeSigner codeSigner : codeSigners) {
                    if (!hashSet.contains(codeSigner)) {
                        hashSet.add(codeSigner);
                        i4++;
                        printStream.printf(rb.getString("Signer.d."), Integer.valueOf(i4));
                        printStream.println();
                        printStream.println();
                        printStream.println(rb.getString("Signature."));
                        printStream.println();
                        List<? extends Certificate> certificates2 = codeSigner.getSignerCertPath().getCertificates();
                        int i5 = 0;
                        Iterator<? extends Certificate> it = certificates2.iterator();
                        while (it.hasNext()) {
                            X509Certificate x509Certificate = (X509Certificate) it.next();
                            if (this.rfc) {
                                printStream.println(rb.getString("Certificate.owner.") + ((Object) x509Certificate.getSubjectDN()) + "\n");
                                dumpCert(x509Certificate, printStream);
                            } else {
                                printX509Cert(x509Certificate, printStream);
                            }
                            printStream.println();
                            int i6 = i5;
                            i5++;
                            checkWeak(oneInMany(rb.getString("the.certificate"), i6, certificates2.size()), x509Certificate);
                        }
                        Timestamp timestamp = codeSigner.getTimestamp();
                        if (timestamp != null) {
                            printStream.println(rb.getString("Timestamp."));
                            printStream.println();
                            List<? extends Certificate> certificates3 = timestamp.getSignerCertPath().getCertificates();
                            int i7 = 0;
                            Iterator<? extends Certificate> it2 = certificates3.iterator();
                            while (it2.hasNext()) {
                                X509Certificate x509Certificate2 = (X509Certificate) it2.next();
                                if (this.rfc) {
                                    printStream.println(rb.getString("Certificate.owner.") + ((Object) x509Certificate2.getSubjectDN()) + "\n");
                                    dumpCert(x509Certificate2, printStream);
                                } else {
                                    printX509Cert(x509Certificate2, printStream);
                                }
                                printStream.println();
                                int i8 = i7;
                                i7++;
                                checkWeak(oneInMany(rb.getString("the.tsa.certificate"), i8, certificates3.size()), x509Certificate2);
                            }
                        }
                    }
                }
            }
        }
        jarFile.close();
        if (hashSet.isEmpty()) {
            printStream.println(rb.getString("Not.a.signed.jar.file"));
        }
    }

    private void doSelfCert(String str, String str2, String str3) throws Exception {
        X500Name x500Name;
        if (str == null) {
            str = keyAlias;
        }
        Pair<Key, char[]> pairRecoverKey = recoverKey(str, this.storePass, this.keyPass);
        PrivateKey privateKey = (PrivateKey) pairRecoverKey.fst;
        if (this.keyPass == null) {
            this.keyPass = pairRecoverKey.snd;
        }
        if (str3 == null) {
            str3 = getCompatibleSigAlgName(privateKey.getAlgorithm());
        }
        Certificate certificate = this.keyStore.getCertificate(str);
        if (certificate == null) {
            throw new Exception(new MessageFormat(rb.getString("alias.has.no.public.key")).format(new Object[]{str}));
        }
        if (!(certificate instanceof X509Certificate)) {
            throw new Exception(new MessageFormat(rb.getString("alias.has.no.X.509.certificate")).format(new Object[]{str}));
        }
        X509CertInfo x509CertInfo = (X509CertInfo) new X509CertImpl(certificate.getEncoded()).get(X509CertInfo.IDENT);
        Date startDate = getStartDate(this.startDate);
        x509CertInfo.set("validity", new CertificateValidity(startDate, getLastDate(startDate, this.validity)));
        x509CertInfo.set("serialNumber", new CertificateSerialNumber(new Random().nextInt() & Integer.MAX_VALUE));
        if (str2 == null) {
            x500Name = (X500Name) x509CertInfo.get("subject.dname");
        } else {
            x500Name = new X500Name(str2);
            x509CertInfo.set("subject.dname", x500Name);
        }
        x509CertInfo.set("issuer.dname", x500Name);
        X509CertImpl x509CertImpl = new X509CertImpl(x509CertInfo);
        PSSParameterSpec defaultAlgorithmParameterSpec = AlgorithmId.getDefaultAlgorithmParameterSpec(str3, privateKey);
        x509CertImpl.sign(privateKey, defaultAlgorithmParameterSpec, str3, null);
        x509CertInfo.set("algorithmID.algorithm", (AlgorithmId) x509CertImpl.get(X509CertImpl.SIG_ALG));
        x509CertInfo.set("version", new CertificateVersion(2));
        x509CertInfo.set("extensions", createV3Extensions(null, (CertificateExtensions) x509CertInfo.get("extensions"), this.v3ext, certificate.getPublicKey(), null));
        X509CertImpl x509CertImpl2 = new X509CertImpl(x509CertInfo);
        x509CertImpl2.sign(privateKey, defaultAlgorithmParameterSpec, str3, null);
        this.keyStore.setKeyEntry(str, privateKey, this.keyPass != null ? this.keyPass : this.storePass, new Certificate[]{x509CertImpl2});
        if (this.verbose) {
            System.err.println(rb.getString("New.certificate.self.signed."));
            System.err.print(x509CertImpl2.toString());
            System.err.println();
        }
    }

    private boolean installReply(String str, InputStream inputStream) throws Exception {
        Certificate[] certificateArrValidateReply;
        if (str == null) {
            str = keyAlias;
        }
        Pair<Key, char[]> pairRecoverKey = recoverKey(str, this.storePass, this.keyPass);
        PrivateKey privateKey = (PrivateKey) pairRecoverKey.fst;
        if (this.keyPass == null) {
            this.keyPass = pairRecoverKey.snd;
        }
        Certificate certificate = this.keyStore.getCertificate(str);
        if (certificate == null) {
            throw new Exception(new MessageFormat(rb.getString("alias.has.no.public.key.certificate.")).format(new Object[]{str}));
        }
        Collection<? extends Certificate> collectionGenerateCertificates = this.cf.generateCertificates(inputStream);
        if (collectionGenerateCertificates.isEmpty()) {
            throw new Exception(rb.getString("Reply.has.no.certificates"));
        }
        Certificate[] certificateArr = (Certificate[]) collectionGenerateCertificates.toArray(new Certificate[collectionGenerateCertificates.size()]);
        if (certificateArr.length == 1) {
            certificateArrValidateReply = establishCertChain(certificate, certificateArr[0]);
        } else {
            certificateArrValidateReply = validateReply(str, certificate, certificateArr);
        }
        if (certificateArrValidateReply != null) {
            this.keyStore.setKeyEntry(str, privateKey, this.keyPass != null ? this.keyPass : this.storePass, certificateArrValidateReply);
            return true;
        }
        return false;
    }

    private boolean addTrustedCert(String str, InputStream inputStream) throws Exception {
        if (str == null) {
            throw new Exception(rb.getString("Must.specify.alias"));
        }
        if (this.keyStore.containsAlias(str)) {
            throw new Exception(new MessageFormat(rb.getString("Certificate.not.imported.alias.alias.already.exists")).format(new Object[]{str}));
        }
        try {
            X509Certificate x509Certificate = (X509Certificate) this.cf.generateCertificate(inputStream);
            if (this.noprompt) {
                checkWeak(rb.getString("the.input"), x509Certificate);
                this.keyStore.setCertificateEntry(str, x509Certificate);
                return true;
            }
            boolean z2 = false;
            if (KeyStoreUtil.isSelfSigned(x509Certificate)) {
                x509Certificate.verify(x509Certificate.getPublicKey());
                z2 = true;
            }
            String yesNoReply = null;
            String certificateAlias = this.keyStore.getCertificateAlias(x509Certificate);
            if (certificateAlias != null) {
                System.err.println(new MessageFormat(rb.getString("Certificate.already.exists.in.keystore.under.alias.trustalias.")).format(new Object[]{certificateAlias}));
                checkWeak(rb.getString("the.input"), x509Certificate);
                printWeakWarnings(true);
                yesNoReply = getYesNoReply(rb.getString("Do.you.still.want.to.add.it.no."));
            } else if (z2) {
                if (this.trustcacerts && this.caks != null) {
                    String certificateAlias2 = this.caks.getCertificateAlias(x509Certificate);
                    certificateAlias = certificateAlias2;
                    if (certificateAlias2 != null) {
                        System.err.println(new MessageFormat(rb.getString("Certificate.already.exists.in.system.wide.CA.keystore.under.alias.trustalias.")).format(new Object[]{certificateAlias}));
                        checkWeak(rb.getString("the.input"), x509Certificate);
                        printWeakWarnings(true);
                        yesNoReply = getYesNoReply(rb.getString("Do.you.still.want.to.add.it.to.your.own.keystore.no."));
                    }
                }
                if (certificateAlias == null) {
                    printX509Cert(x509Certificate, System.out);
                    checkWeak(rb.getString("the.input"), x509Certificate);
                    printWeakWarnings(true);
                    yesNoReply = getYesNoReply(rb.getString("Trust.this.certificate.no."));
                }
            }
            if (yesNoReply != null) {
                if ("YES".equals(yesNoReply)) {
                    this.keyStore.setCertificateEntry(str, x509Certificate);
                    return true;
                }
                return false;
            }
            try {
                if (establishCertChain(null, x509Certificate) != null) {
                    this.keyStore.setCertificateEntry(str, x509Certificate);
                    return true;
                }
                return false;
            } catch (Exception e2) {
                printX509Cert(x509Certificate, System.out);
                checkWeak(rb.getString("the.input"), x509Certificate);
                printWeakWarnings(true);
                if ("YES".equals(getYesNoReply(rb.getString("Trust.this.certificate.no.")))) {
                    this.keyStore.setCertificateEntry(str, x509Certificate);
                    return true;
                }
                return false;
            }
        } catch (ClassCastException | CertificateException e3) {
            throw new Exception(rb.getString("Input.not.an.X.509.certificate"));
        }
    }

    private char[] getNewPasswd(String str, char[] cArr) throws Exception {
        char[] password = null;
        for (int i2 = 0; i2 < 3; i2++) {
            System.err.print(new MessageFormat(rb.getString("New.prompt.")).format(new Object[]{str}));
            char[] password2 = Password.readPassword(System.in);
            this.passwords.add(password2);
            if (password2 == null || password2.length < 6) {
                System.err.println(rb.getString("Password.is.too.short.must.be.at.least.6.characters"));
            } else if (Arrays.equals(password2, cArr)) {
                System.err.println(rb.getString("Passwords.must.differ"));
            } else {
                System.err.print(new MessageFormat(rb.getString("Re.enter.new.prompt.")).format(new Object[]{str}));
                password = Password.readPassword(System.in);
                this.passwords.add(password);
                if (!Arrays.equals(password2, password)) {
                    System.err.println(rb.getString("They.don.t.match.Try.again"));
                } else {
                    Arrays.fill(password, ' ');
                    return password2;
                }
            }
            if (password2 != null) {
                Arrays.fill(password2, ' ');
            }
            if (password != null) {
                Arrays.fill(password, ' ');
                password = null;
            }
        }
        throw new Exception(rb.getString("Too.many.failures.try.later"));
    }

    private String getAlias(String str) throws Exception {
        if (str != null) {
            System.err.print(new MessageFormat(rb.getString("Enter.prompt.alias.name.")).format(new Object[]{str}));
        } else {
            System.err.print(rb.getString("Enter.alias.name."));
        }
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

    private String inputStringFromStdin(String str) throws Exception {
        System.err.print(str);
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

    private char[] getKeyPasswd(String str, String str2, char[] cArr) throws Exception {
        char[] password;
        int i2 = 0;
        do {
            if (cArr != null) {
                System.err.println(new MessageFormat(rb.getString("Enter.key.password.for.alias.")).format(new Object[]{str}));
                System.err.print(new MessageFormat(rb.getString(".RETURN.if.same.as.for.otherAlias.")).format(new Object[]{str2}));
            } else {
                System.err.print(new MessageFormat(rb.getString("Enter.key.password.for.alias.")).format(new Object[]{str}));
            }
            System.err.flush();
            password = Password.readPassword(System.in);
            this.passwords.add(password);
            if (password == null) {
                password = cArr;
            }
            i2++;
            if (password != null) {
                break;
            }
        } while (i2 < 3);
        if (password == null) {
            throw new Exception(rb.getString("Too.many.failures.try.later"));
        }
        return password;
    }

    private String withWeak(String str) {
        return DISABLED_CHECK.permits(SIG_PRIMITIVE_SET, str, (AlgorithmParameters) null) ? LEGACY_CHECK.permits(SIG_PRIMITIVE_SET, str, (AlgorithmParameters) null) ? str : String.format(rb.getString("with.weak"), str) : String.format(rb.getString("with.disabled"), str);
    }

    private String fullDisplayAlgName(Key key) {
        String algorithm = key.getAlgorithm();
        if (key instanceof ECKey) {
            ECParameterSpec params = ((ECKey) key).getParams();
            if (params instanceof NamedCurve) {
                algorithm = algorithm + " (" + params.toString().split(" ")[0] + ")";
            }
        }
        return algorithm;
    }

    private String withWeak(PublicKey publicKey) {
        int keySize = KeyUtil.getKeySize(publicKey);
        String strFullDisplayAlgName = fullDisplayAlgName(publicKey);
        return DISABLED_CHECK.permits(SIG_PRIMITIVE_SET, publicKey) ? LEGACY_CHECK.permits(SIG_PRIMITIVE_SET, publicKey) ? keySize >= 0 ? String.format(rb.getString("key.bit"), Integer.valueOf(keySize), strFullDisplayAlgName) : String.format(rb.getString("unknown.size.1"), strFullDisplayAlgName) : String.format(rb.getString("key.bit.weak"), Integer.valueOf(keySize), strFullDisplayAlgName) : String.format(rb.getString("key.bit.disabled"), Integer.valueOf(keySize), strFullDisplayAlgName);
    }

    private void printX509Cert(X509Certificate x509Certificate, PrintStream printStream) throws Exception {
        CertificateExtensions certificateExtensions;
        MessageFormat messageFormat = new MessageFormat(rb.getString(".PATTERN.printX509Cert.with.weak"));
        PublicKey publicKey = x509Certificate.getPublicKey();
        String sigAlgName = x509Certificate.getSigAlgName();
        if (!isTrustedCert(x509Certificate)) {
            sigAlgName = withWeak(sigAlgName);
        }
        printStream.println(messageFormat.format(new Object[]{x509Certificate.getSubjectDN().toString(), x509Certificate.getIssuerDN().toString(), x509Certificate.getSerialNumber().toString(16), x509Certificate.getNotBefore().toString(), x509Certificate.getNotAfter().toString(), getCertFingerPrint("SHA-1", x509Certificate), getCertFingerPrint("SHA-256", x509Certificate), sigAlgName, withWeak(publicKey), Integer.valueOf(x509Certificate.getVersion())}));
        if ((x509Certificate instanceof X509CertImpl) && (certificateExtensions = (CertificateExtensions) ((X509CertInfo) ((X509CertImpl) x509Certificate).get(X509CertInfo.IDENT)).get("extensions")) != null) {
            printExtensions(rb.getString("Extensions."), certificateExtensions, printStream);
        }
    }

    private static void printExtensions(String str, CertificateExtensions certificateExtensions, PrintStream printStream) throws Exception {
        int i2 = 0;
        Iterator<Extension> it = certificateExtensions.getAllExtensions().iterator();
        Iterator<Extension> it2 = certificateExtensions.getUnparseableExtensions().values().iterator();
        while (true) {
            if (it.hasNext() || it2.hasNext()) {
                Extension next = it.hasNext() ? it.next() : it2.next();
                if (i2 == 0) {
                    printStream.println();
                    printStream.println(str);
                    printStream.println();
                }
                i2++;
                printStream.print(FXMLLoader.CONTROLLER_METHOD_PREFIX + i2 + ": " + ((Object) next));
                if (next.getClass() == Extension.class) {
                    if (next.getExtensionValue().length == 0) {
                        printStream.println(rb.getString(".Empty.value."));
                    } else {
                        new HexDumpEncoder().encodeBuffer(next.getExtensionValue(), printStream);
                        printStream.println();
                    }
                }
                printStream.println();
            } else {
                return;
            }
        }
    }

    private static Pair<String, Certificate> getSigner(Certificate certificate, KeyStore keyStore) throws Exception {
        if (keyStore.getCertificateAlias(certificate) != null) {
            return new Pair<>("", certificate);
        }
        Enumeration<String> enumerationAliases = keyStore.aliases();
        while (enumerationAliases.hasMoreElements()) {
            String strNextElement2 = enumerationAliases.nextElement2();
            Certificate certificate2 = keyStore.getCertificate(strNextElement2);
            if (certificate2 != null) {
                try {
                    certificate.verify(certificate2.getPublicKey());
                    return new Pair<>(strNextElement2, certificate2);
                } catch (Exception e2) {
                }
            }
        }
        return null;
    }

    private X500Name getX500Name() throws IOException {
        X500Name x500Name;
        String strInputString;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String strInputString2 = "Unknown";
        String strInputString3 = "Unknown";
        String strInputString4 = "Unknown";
        String strInputString5 = "Unknown";
        String strInputString6 = "Unknown";
        String strInputString7 = "Unknown";
        int i2 = 20;
        do {
            int i3 = i2;
            i2--;
            if (i3 < 0) {
                throw new RuntimeException(rb.getString("Too.many.retries.program.terminated"));
            }
            strInputString2 = inputString(bufferedReader, rb.getString("What.is.your.first.and.last.name."), strInputString2);
            strInputString3 = inputString(bufferedReader, rb.getString("What.is.the.name.of.your.organizational.unit."), strInputString3);
            strInputString4 = inputString(bufferedReader, rb.getString("What.is.the.name.of.your.organization."), strInputString4);
            strInputString5 = inputString(bufferedReader, rb.getString("What.is.the.name.of.your.City.or.Locality."), strInputString5);
            strInputString6 = inputString(bufferedReader, rb.getString("What.is.the.name.of.your.State.or.Province."), strInputString6);
            strInputString7 = inputString(bufferedReader, rb.getString("What.is.the.two.letter.country.code.for.this.unit."), strInputString7);
            x500Name = new X500Name(strInputString2, strInputString3, strInputString4, strInputString5, strInputString6, strInputString7);
            strInputString = inputString(bufferedReader, new MessageFormat(rb.getString("Is.name.correct.")).format(new Object[]{x500Name}), rb.getString("no"));
            if (collator.compare(strInputString, rb.getString("yes")) == 0) {
                break;
            }
        } while (collator.compare(strInputString, rb.getString(PdfOps.y_TOKEN)) != 0);
        System.err.println();
        return x500Name;
    }

    private String inputString(BufferedReader bufferedReader, String str, String str2) throws IOException {
        System.err.println(str);
        System.err.print(new MessageFormat(rb.getString(".defaultValue.")).format(new Object[]{str2}));
        System.err.flush();
        String line = bufferedReader.readLine();
        if (line == null || collator.compare(line, "") == 0) {
            line = str2;
        }
        return line;
    }

    private void dumpCert(Certificate certificate, PrintStream printStream) throws IOException, CertificateException {
        if (this.rfc) {
            printStream.println(X509Factory.BEGIN_CERT);
            printStream.println(Base64.getMimeEncoder(64, CRLF).encodeToString(certificate.getEncoded()));
            printStream.println(X509Factory.END_CERT);
            return;
        }
        printStream.write(certificate.getEncoded());
    }

    private void byte2hex(byte b2, StringBuffer stringBuffer) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        stringBuffer.append(cArr[(b2 & 240) >> 4]);
        stringBuffer.append(cArr[b2 & 15]);
    }

    private String toHexString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        int length = bArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            byte2hex(bArr[i2], stringBuffer);
            if (i2 < length - 1) {
                stringBuffer.append(CallSiteDescriptor.TOKEN_DELIMITER);
            }
        }
        return stringBuffer.toString();
    }

    private Pair<Key, char[]> recoverKey(String str, char[] cArr, char[] cArr2) throws Exception {
        if (KeyStoreUtil.isWindowsKeyStore(this.storetype)) {
            return Pair.of(this.keyStore.getKey(str, null), null);
        }
        if (!this.keyStore.containsAlias(str)) {
            throw new Exception(new MessageFormat(rb.getString("Alias.alias.does.not.exist")).format(new Object[]{str}));
        }
        if (!this.keyStore.entryInstanceOf(str, KeyStore.PrivateKeyEntry.class) && !this.keyStore.entryInstanceOf(str, KeyStore.SecretKeyEntry.class)) {
            throw new Exception(new MessageFormat(rb.getString("Alias.alias.has.no.key")).format(new Object[]{str}));
        }
        if (cArr2 == null) {
            if (cArr != null) {
                try {
                    Key key = this.keyStore.getKey(str, cArr);
                    this.passwords.add(cArr);
                    return Pair.of(key, cArr);
                } catch (UnrecoverableKeyException e2) {
                    if (this.token) {
                        throw e2;
                    }
                }
            }
            char[] keyPasswd = getKeyPasswd(str, null, null);
            return Pair.of(this.keyStore.getKey(str, keyPasswd), keyPasswd);
        }
        return Pair.of(this.keyStore.getKey(str, cArr2), cArr2);
    }

    private Pair<KeyStore.Entry, char[]> recoverEntry(KeyStore keyStore, String str, char[] cArr, char[] cArr2) throws Exception {
        if (!keyStore.containsAlias(str)) {
            throw new Exception(new MessageFormat(rb.getString("Alias.alias.does.not.exist")).format(new Object[]{str}));
        }
        try {
            return Pair.of(keyStore.getEntry(str, null), null);
        } catch (UnrecoverableEntryException e2) {
            if (P11KEYSTORE.equalsIgnoreCase(keyStore.getType()) || KeyStoreUtil.isWindowsKeyStore(keyStore.getType())) {
                throw e2;
            }
            if (cArr2 != null) {
                return Pair.of(keyStore.getEntry(str, new KeyStore.PasswordProtection(cArr2)), cArr2);
            }
            if (cArr != null) {
                try {
                    return Pair.of(keyStore.getEntry(str, new KeyStore.PasswordProtection(cArr)), cArr);
                } catch (UnrecoverableEntryException e3) {
                    if (P12KEYSTORE.equalsIgnoreCase(keyStore.getType())) {
                        throw e3;
                    }
                    char[] keyPasswd = getKeyPasswd(str, null, null);
                    return Pair.of(keyStore.getEntry(str, new KeyStore.PasswordProtection(keyPasswd)), keyPasswd);
                }
            }
            char[] keyPasswd2 = getKeyPasswd(str, null, null);
            return Pair.of(keyStore.getEntry(str, new KeyStore.PasswordProtection(keyPasswd2)), keyPasswd2);
        }
    }

    private String getCertFingerPrint(String str, Certificate certificate) throws Exception {
        return toHexString(MessageDigest.getInstance(str).digest(certificate.getEncoded()));
    }

    private void printNoIntegrityWarning() {
        System.err.println();
        System.err.println(rb.getString(".WARNING.WARNING.WARNING."));
        System.err.println(rb.getString(".The.integrity.of.the.information.stored.in.your.keystore."));
        System.err.println(rb.getString(".WARNING.WARNING.WARNING."));
        System.err.println();
    }

    private Certificate[] validateReply(String str, Certificate certificate, Certificate[] certificateArr) throws Exception {
        checkWeak(rb.getString("reply"), certificateArr);
        PublicKey publicKey = certificate.getPublicKey();
        int i2 = 0;
        while (i2 < certificateArr.length && !publicKey.equals(certificateArr[i2].getPublicKey())) {
            i2++;
        }
        if (i2 == certificateArr.length) {
            throw new Exception(new MessageFormat(rb.getString("Certificate.reply.does.not.contain.public.key.for.alias.")).format(new Object[]{str}));
        }
        Certificate certificate2 = certificateArr[0];
        certificateArr[0] = certificateArr[i2];
        certificateArr[i2] = certificate2;
        X509Certificate x509Certificate = (X509Certificate) certificateArr[0];
        for (int i3 = 1; i3 < certificateArr.length - 1; i3++) {
            int i4 = i3;
            while (true) {
                if (i4 >= certificateArr.length) {
                    break;
                }
                if (!KeyStoreUtil.signedBy(x509Certificate, (X509Certificate) certificateArr[i4])) {
                    i4++;
                } else {
                    Certificate certificate3 = certificateArr[i3];
                    certificateArr[i3] = certificateArr[i4];
                    certificateArr[i4] = certificate3;
                    x509Certificate = (X509Certificate) certificateArr[i3];
                    break;
                }
            }
            if (i4 == certificateArr.length) {
                throw new Exception(rb.getString("Incomplete.certificate.chain.in.reply"));
            }
        }
        if (this.noprompt) {
            return certificateArr;
        }
        Certificate certificate4 = certificateArr[certificateArr.length - 1];
        boolean z2 = true;
        Pair<String, Certificate> signer = getSigner(certificate4, this.keyStore);
        if (signer == null && this.trustcacerts && this.caks != null) {
            signer = getSigner(certificate4, this.caks);
            z2 = false;
        }
        if (signer == null) {
            System.err.println();
            System.err.println(rb.getString("Top.level.certificate.in.reply."));
            printX509Cert((X509Certificate) certificate4, System.out);
            System.err.println();
            System.err.print(rb.getString(".is.not.trusted."));
            printWeakWarnings(true);
            if ("NO".equals(getYesNoReply(rb.getString("Install.reply.anyway.no.")))) {
                return null;
            }
        } else if (signer.snd != certificate4) {
            Certificate[] certificateArr2 = new Certificate[certificateArr.length + 1];
            System.arraycopy(certificateArr, 0, certificateArr2, 0, certificateArr.length);
            certificateArr2[certificateArr2.length - 1] = signer.snd;
            certificateArr = certificateArr2;
            checkWeak(String.format(rb.getString(z2 ? "alias.in.keystore" : "alias.in.cacerts"), signer.fst), signer.snd);
        }
        return certificateArr;
    }

    private Certificate[] establishCertChain(Certificate certificate, Certificate certificate2) throws Exception {
        if (certificate != null) {
            if (!certificate.getPublicKey().equals(certificate2.getPublicKey())) {
                throw new Exception(rb.getString("Public.keys.in.reply.and.keystore.don.t.match"));
            }
            if (certificate2.equals(certificate)) {
                throw new Exception(rb.getString("Certificate.reply.and.certificate.in.keystore.are.identical"));
            }
        }
        Hashtable<Principal, Vector<Pair<String, X509Certificate>>> hashtable = null;
        if (this.keyStore.size() > 0) {
            hashtable = new Hashtable<>(11);
            keystorecerts2Hashtable(this.keyStore, hashtable);
        }
        if (this.trustcacerts && this.caks != null && this.caks.size() > 0) {
            if (hashtable == null) {
                hashtable = new Hashtable<>(11);
            }
            keystorecerts2Hashtable(this.caks, hashtable);
        }
        Vector<Pair<String, X509Certificate>> vector = new Vector<>(2);
        if (buildChain(new Pair<>(rb.getString("the.input"), (X509Certificate) certificate2), vector, hashtable)) {
            Iterator<Pair<String, X509Certificate>> it = vector.iterator();
            while (it.hasNext()) {
                Pair<String, X509Certificate> next = it.next();
                checkWeak(next.fst, next.snd);
            }
            Certificate[] certificateArr = new Certificate[vector.size()];
            int i2 = 0;
            for (int size = vector.size() - 1; size >= 0; size--) {
                certificateArr[i2] = vector.elementAt(size).snd;
                i2++;
            }
            return certificateArr;
        }
        throw new Exception(rb.getString("Failed.to.establish.chain.from.reply"));
    }

    private boolean buildChain(Pair<String, X509Certificate> pair, Vector<Pair<String, X509Certificate>> vector, Hashtable<Principal, Vector<Pair<String, X509Certificate>>> hashtable) {
        if (KeyStoreUtil.isSelfSigned(pair.snd)) {
            vector.addElement(pair);
            return true;
        }
        Vector<Pair<String, X509Certificate>> vector2 = hashtable.get(pair.snd.getIssuerDN());
        if (vector2 == null) {
            return false;
        }
        Enumeration<Pair<String, X509Certificate>> enumerationElements = vector2.elements();
        while (enumerationElements.hasMoreElements()) {
            Pair<String, X509Certificate> pairNextElement2 = enumerationElements.nextElement2();
            try {
                pair.snd.verify(pairNextElement2.snd.getPublicKey());
            } catch (Exception e2) {
            }
            if (buildChain(pairNextElement2, vector, hashtable)) {
                vector.addElement(pair);
                return true;
            }
        }
        return false;
    }

    private String getYesNoReply(String str) throws IOException {
        String str2;
        int i2 = 20;
        do {
            int i3 = i2;
            i2--;
            if (i3 < 0) {
                throw new RuntimeException(rb.getString("Too.many.retries.program.terminated"));
            }
            System.err.print(str);
            System.err.flush();
            String line = new BufferedReader(new InputStreamReader(System.in)).readLine();
            if (collator.compare(line, "") == 0 || collator.compare(line, rb.getString(PdfOps.n_TOKEN)) == 0 || collator.compare(line, rb.getString("no")) == 0) {
                str2 = "NO";
            } else if (collator.compare(line, rb.getString(PdfOps.y_TOKEN)) == 0 || collator.compare(line, rb.getString("yes")) == 0) {
                str2 = "YES";
            } else {
                System.err.println(rb.getString("Wrong.answer.try.again"));
                str2 = null;
            }
        } while (str2 == null);
        return str2;
    }

    private void keystorecerts2Hashtable(KeyStore keyStore, Hashtable<Principal, Vector<Pair<String, X509Certificate>>> hashtable) throws Exception {
        Enumeration<String> enumerationAliases = keyStore.aliases();
        while (enumerationAliases.hasMoreElements()) {
            String strNextElement2 = enumerationAliases.nextElement2();
            Certificate certificate = keyStore.getCertificate(strNextElement2);
            if (certificate != null) {
                Principal subjectDN = ((X509Certificate) certificate).getSubjectDN();
                Pair<String, X509Certificate> pair = new Pair<>(String.format(rb.getString(keyStore == this.caks ? "alias.in.cacerts" : "alias.in.keystore"), strNextElement2), (X509Certificate) certificate);
                Vector<Pair<String, X509Certificate>> vector = hashtable.get(subjectDN);
                if (vector == null) {
                    vector = new Vector<>();
                    vector.addElement(pair);
                } else if (!vector.contains(pair)) {
                    vector.addElement(pair);
                }
                hashtable.put(subjectDN, vector);
            }
        }
    }

    private static Date getStartDate(String str) throws IOException, NumberFormatException {
        int i2;
        int i3;
        char cCharAt;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        if (str != null) {
            IOException iOException = new IOException(rb.getString("Illegal.startdate.value"));
            int length = str.length();
            if (length == 0) {
                throw iOException;
            }
            if (str.charAt(0) == '-' || str.charAt(0) == '+') {
                int i4 = 0;
                while (true) {
                    int i5 = i4;
                    if (i5 < length) {
                        switch (str.charAt(i5)) {
                            case '+':
                                i2 = 1;
                                break;
                            case '-':
                                i2 = -1;
                                break;
                            default:
                                throw iOException;
                        }
                        int i6 = i5 + 1;
                        while (i6 < length && (cCharAt = str.charAt(i6)) >= '0' && cCharAt <= '9') {
                            i6++;
                        }
                        if (i6 == i5 + 1) {
                            throw iOException;
                        }
                        int i7 = Integer.parseInt(str.substring(i5 + 1, i6));
                        if (i6 >= length) {
                            throw iOException;
                        }
                        switch (str.charAt(i6)) {
                            case 'H':
                                i3 = 10;
                                break;
                            case 'M':
                                i3 = 12;
                                break;
                            case 'S':
                                i3 = 13;
                                break;
                            case 'd':
                                i3 = 5;
                                break;
                            case 'm':
                                i3 = 2;
                                break;
                            case 'y':
                                i3 = 1;
                                break;
                            default:
                                throw iOException;
                        }
                        gregorianCalendar.add(i3, i2 * i7);
                        i4 = i6 + 1;
                    }
                }
            } else {
                String strSubstring = null;
                String strSubstring2 = null;
                if (length == 19) {
                    strSubstring = str.substring(0, 10);
                    strSubstring2 = str.substring(11);
                    if (str.charAt(10) != ' ') {
                        throw iOException;
                    }
                } else if (length == 10) {
                    strSubstring = str;
                } else if (length == 8) {
                    strSubstring2 = str;
                } else {
                    throw iOException;
                }
                if (strSubstring != null) {
                    if (strSubstring.matches("\\d\\d\\d\\d\\/\\d\\d\\/\\d\\d")) {
                        gregorianCalendar.set(Integer.valueOf(strSubstring.substring(0, 4)).intValue(), Integer.valueOf(strSubstring.substring(5, 7)).intValue() - 1, Integer.valueOf(strSubstring.substring(8, 10)).intValue());
                    } else {
                        throw iOException;
                    }
                }
                if (strSubstring2 != null) {
                    if (strSubstring2.matches("\\d\\d:\\d\\d:\\d\\d")) {
                        gregorianCalendar.set(11, Integer.valueOf(strSubstring2.substring(0, 2)).intValue());
                        gregorianCalendar.set(12, Integer.valueOf(strSubstring2.substring(0, 2)).intValue());
                        gregorianCalendar.set(13, Integer.valueOf(strSubstring2.substring(0, 2)).intValue());
                        gregorianCalendar.set(14, 0);
                    } else {
                        throw iOException;
                    }
                }
            }
        }
        return gregorianCalendar.getTime();
    }

    private static int oneOf(String str, String... strArr) throws Exception {
        int[] iArr = new int[strArr.length];
        int i2 = 0;
        int i3 = Integer.MAX_VALUE;
        for (int i4 = 0; i4 < strArr.length; i4++) {
            String str2 = strArr[i4];
            if (str2 == null) {
                i3 = i4;
            } else if (str2.toLowerCase(Locale.ENGLISH).startsWith(str.toLowerCase(Locale.ENGLISH))) {
                int i5 = i2;
                i2++;
                iArr[i5] = i4;
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                boolean z2 = true;
                for (char c2 : str2.toCharArray()) {
                    if (z2) {
                        stringBuffer.append(c2);
                        z2 = false;
                    } else if (!Character.isLowerCase(c2)) {
                        stringBuffer.append(c2);
                    }
                }
                if (stringBuffer.toString().equalsIgnoreCase(str)) {
                    int i6 = i2;
                    i2++;
                    iArr[i6] = i4;
                }
            }
        }
        if (i2 == 0) {
            return -1;
        }
        if (i2 == 1) {
            return iArr[0];
        }
        if (iArr[1] > i3) {
            return iArr[0];
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(new MessageFormat(rb.getString("command.{0}.is.ambiguous.")).format(new Object[]{str}));
        stringBuffer2.append("\n    ");
        for (int i7 = 0; i7 < i2 && iArr[i7] < i3; i7++) {
            stringBuffer2.append(' ');
            stringBuffer2.append(strArr[iArr[i7]]);
        }
        throw new Exception(stringBuffer2.toString());
    }

    private GeneralName createGeneralName(String str, String str2) throws Exception {
        GeneralNameInterface oIDName;
        int iOneOf = oneOf(str, "EMAIL", Constants._ATT_URI, "DNS", "IP", "OID");
        if (iOneOf < 0) {
            throw new Exception(rb.getString("Unrecognized.GeneralName.type.") + str);
        }
        switch (iOneOf) {
            case 0:
                oIDName = new RFC822Name(str2);
                break;
            case 1:
                oIDName = new URIName(str2);
                break;
            case 2:
                oIDName = new DNSName(str2);
                break;
            case 3:
                oIDName = new IPAddressName(str2);
                break;
            default:
                oIDName = new OIDName(str2);
                break;
        }
        return new GeneralName(oIDName);
    }

    private ObjectIdentifier findOidForExtName(String str) throws Exception {
        switch (oneOf(str, extSupported)) {
            case 0:
                return PKIXExtensions.BasicConstraints_Id;
            case 1:
                return PKIXExtensions.KeyUsage_Id;
            case 2:
                return PKIXExtensions.ExtendedKeyUsage_Id;
            case 3:
                return PKIXExtensions.SubjectAlternativeName_Id;
            case 4:
                return PKIXExtensions.IssuerAlternativeName_Id;
            case 5:
                return PKIXExtensions.SubjectInfoAccess_Id;
            case 6:
                return PKIXExtensions.AuthInfoAccess_Id;
            case 7:
            default:
                return new ObjectIdentifier(str);
            case 8:
                return PKIXExtensions.CRLDistributionPoints_Id;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v124, types: [int] */
    /* JADX WARN: Type inference failed for: r0v128, types: [int] */
    /* JADX WARN: Type inference failed for: r0v135, types: [int] */
    private CertificateExtensions createV3Extensions(CertificateExtensions certificateExtensions, CertificateExtensions certificateExtensions2, List<String> list, PublicKey publicKey, PublicKey publicKey2) throws Exception {
        String strSubstring;
        String strSubstring2;
        byte[] bArrCopyOf;
        byte b2;
        ObjectIdentifier objectIdentifier;
        if (certificateExtensions2 != null && certificateExtensions != null) {
            throw new Exception("One of request and original should be null.");
        }
        if (certificateExtensions2 == null) {
            certificateExtensions2 = new CertificateExtensions();
        }
        if (certificateExtensions != null) {
            try {
                Iterator<String> it = list.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    String next = it.next();
                    if (next.toLowerCase(Locale.ENGLISH).startsWith("honored=")) {
                        List<String> listAsList = Arrays.asList(next.toLowerCase(Locale.ENGLISH).substring(8).split(","));
                        if (listAsList.contains("all")) {
                            certificateExtensions2 = certificateExtensions;
                        }
                        for (String str : listAsList) {
                            if (!str.equals("all")) {
                                boolean z2 = true;
                                int iOneOf = -1;
                                String strSubstring3 = null;
                                if (str.startsWith(LanguageTag.SEP)) {
                                    z2 = false;
                                    strSubstring3 = str.substring(1);
                                } else {
                                    int iIndexOf = str.indexOf(58);
                                    if (iIndexOf >= 0) {
                                        strSubstring3 = str.substring(0, iIndexOf);
                                        iOneOf = oneOf(str.substring(iIndexOf + 1), "critical", "non-critical");
                                        if (iOneOf == -1) {
                                            throw new Exception(rb.getString("Illegal.value.") + str);
                                        }
                                    }
                                }
                                String nameByOid = certificateExtensions.getNameByOid(findOidForExtName(strSubstring3));
                                if (z2) {
                                    Extension extension = certificateExtensions.get(nameByOid);
                                    if ((!extension.isCritical() && iOneOf == 0) || (extension.isCritical() && iOneOf == 1)) {
                                        certificateExtensions2.set(nameByOid, Extension.newExtension(extension.getExtensionId(), !extension.isCritical(), extension.getExtensionValue()));
                                    }
                                } else {
                                    certificateExtensions2.delete(nameByOid);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        }
        for (String str2 : list) {
            boolean z3 = false;
            int iIndexOf2 = str2.indexOf(61);
            if (iIndexOf2 >= 0) {
                strSubstring = str2.substring(0, iIndexOf2);
                strSubstring2 = str2.substring(iIndexOf2 + 1);
            } else {
                strSubstring = str2;
                strSubstring2 = null;
            }
            int iIndexOf3 = strSubstring.indexOf(58);
            if (iIndexOf3 >= 0) {
                if (oneOf(strSubstring.substring(iIndexOf3 + 1), "critical") == 0) {
                    z3 = true;
                }
                strSubstring = strSubstring.substring(0, iIndexOf3);
            }
            if (!strSubstring.equalsIgnoreCase("honored")) {
                int iOneOf2 = oneOf(strSubstring, extSupported);
                switch (iOneOf2) {
                    case -1:
                        ObjectIdentifier objectIdentifier2 = new ObjectIdentifier(strSubstring);
                        if (strSubstring2 != null) {
                            byte[] bArr = new byte[(strSubstring2.length() / 2) + 1];
                            int i2 = 0;
                            for (char c2 : strSubstring2.toCharArray()) {
                                if (c2 >= '0' && c2 <= '9') {
                                    b2 = c2 - '0';
                                } else if (c2 >= 'A' && c2 <= 'F') {
                                    b2 = (c2 - 'A') + 10;
                                } else if (c2 >= 'a' && c2 <= 'f') {
                                    b2 = (c2 - 'a') + 10;
                                }
                                if (i2 % 2 == 0) {
                                    bArr[i2 / 2] = (byte) (b2 << 4);
                                } else {
                                    int i3 = i2 / 2;
                                    bArr[i3] = (byte) (bArr[i3] + b2);
                                }
                                i2++;
                            }
                            if (i2 % 2 != 0) {
                                throw new Exception(rb.getString("Odd.number.of.hex.digits.found.") + str2);
                            }
                            bArrCopyOf = Arrays.copyOf(bArr, i2 / 2);
                        } else {
                            bArrCopyOf = new byte[0];
                        }
                        certificateExtensions2.set(objectIdentifier2.toString(), new Extension(objectIdentifier2, z3, new DerValue((byte) 4, bArrCopyOf).toByteArray()));
                        break;
                    case 0:
                        int i4 = -1;
                        boolean z4 = false;
                        if (strSubstring2 == null) {
                            z4 = true;
                        } else {
                            try {
                                i4 = Integer.parseInt(strSubstring2);
                                z4 = true;
                            } catch (NumberFormatException e3) {
                                for (String str3 : strSubstring2.split(",")) {
                                    String[] strArrSplit = str3.split(CallSiteDescriptor.TOKEN_DELIMITER);
                                    if (strArrSplit.length != 2) {
                                        throw new Exception(rb.getString("Illegal.value.") + str2);
                                    }
                                    if (strArrSplit[0].equalsIgnoreCase("ca")) {
                                        z4 = Boolean.parseBoolean(strArrSplit[1]);
                                    } else if (strArrSplit[0].equalsIgnoreCase("pathlen")) {
                                        i4 = Integer.parseInt(strArrSplit[1]);
                                    } else {
                                        throw new Exception(rb.getString("Illegal.value.") + str2);
                                    }
                                }
                            }
                        }
                        certificateExtensions2.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(Boolean.valueOf(z3), z4, i4));
                        break;
                    case 1:
                        if (strSubstring2 != null) {
                            boolean[] zArr = new boolean[9];
                            for (String str4 : strSubstring2.split(",")) {
                                int iOneOf3 = oneOf(str4, "digitalSignature", "nonRepudiation", "keyEncipherment", "dataEncipherment", "keyAgreement", "keyCertSign", "cRLSign", "encipherOnly", "decipherOnly", "contentCommitment");
                                if (iOneOf3 < 0) {
                                    throw new Exception(rb.getString("Unknown.keyUsage.type.") + str4);
                                }
                                if (iOneOf3 == 9) {
                                    iOneOf3 = 1;
                                }
                                zArr[iOneOf3] = true;
                            }
                            KeyUsageExtension keyUsageExtension = new KeyUsageExtension(zArr);
                            certificateExtensions2.set(KeyUsageExtension.NAME, Extension.newExtension(keyUsageExtension.getExtensionId(), z3, keyUsageExtension.getExtensionValue()));
                            break;
                        } else {
                            throw new Exception(rb.getString("Illegal.value.") + str2);
                        }
                    case 2:
                        if (strSubstring2 != null) {
                            Vector vector = new Vector();
                            for (String str5 : strSubstring2.split(",")) {
                                int iOneOf4 = oneOf(str5, "anyExtendedKeyUsage", "serverAuth", "clientAuth", "codeSigning", "emailProtection", "", "", "", "timeStamping", "OCSPSigning");
                                if (iOneOf4 < 0) {
                                    try {
                                        vector.add(new ObjectIdentifier(str5));
                                    } catch (Exception e4) {
                                        throw new Exception(rb.getString("Unknown.extendedkeyUsage.type.") + str5);
                                    }
                                } else if (iOneOf4 == 0) {
                                    vector.add(new ObjectIdentifier("2.5.29.37.0"));
                                } else {
                                    vector.add(new ObjectIdentifier("1.3.6.1.5.5.7.3." + iOneOf4));
                                }
                            }
                            certificateExtensions2.set(ExtendedKeyUsageExtension.NAME, new ExtendedKeyUsageExtension(Boolean.valueOf(z3), (Vector<ObjectIdentifier>) vector));
                            break;
                        } else {
                            throw new Exception(rb.getString("Illegal.value.") + str2);
                        }
                    case 3:
                    case 4:
                        if (strSubstring2 != null) {
                            String[] strArrSplit2 = strSubstring2.split(",");
                            GeneralNames generalNames = new GeneralNames();
                            for (String str6 : strArrSplit2) {
                                int iIndexOf4 = str6.indexOf(58);
                                if (iIndexOf4 < 0) {
                                    throw new Exception("Illegal item " + str6 + " in " + str2);
                                }
                                generalNames.add(createGeneralName(str6.substring(0, iIndexOf4), str6.substring(iIndexOf4 + 1)));
                            }
                            if (iOneOf2 == 3) {
                                certificateExtensions2.set(SubjectAlternativeNameExtension.NAME, new SubjectAlternativeNameExtension(Boolean.valueOf(z3), generalNames));
                            } else {
                                certificateExtensions2.set(IssuerAlternativeNameExtension.NAME, new IssuerAlternativeNameExtension(Boolean.valueOf(z3), generalNames));
                            }
                            break;
                        } else {
                            throw new Exception(rb.getString("Illegal.value.") + str2);
                        }
                    case 5:
                    case 6:
                        if (z3) {
                            throw new Exception(rb.getString("This.extension.cannot.be.marked.as.critical.") + str2);
                        }
                        if (strSubstring2 != null) {
                            ArrayList arrayList = new ArrayList();
                            for (String str7 : strSubstring2.split(",")) {
                                int iIndexOf5 = str7.indexOf(58);
                                int iIndexOf6 = str7.indexOf(58, iIndexOf5 + 1);
                                if (iIndexOf5 < 0 || iIndexOf6 < 0) {
                                    throw new Exception(rb.getString("Illegal.value.") + str2);
                                }
                                String strSubstring4 = str7.substring(0, iIndexOf5);
                                String strSubstring5 = str7.substring(iIndexOf5 + 1, iIndexOf6);
                                String strSubstring6 = str7.substring(iIndexOf6 + 1);
                                int iOneOf5 = oneOf(strSubstring4, "", "ocsp", "caIssuers", "timeStamping", "", "caRepository");
                                if (iOneOf5 < 0) {
                                    try {
                                        objectIdentifier = new ObjectIdentifier(strSubstring4);
                                    } catch (Exception e5) {
                                        throw new Exception(rb.getString("Unknown.AccessDescription.type.") + strSubstring4);
                                    }
                                } else {
                                    objectIdentifier = new ObjectIdentifier("1.3.6.1.5.5.7.48." + iOneOf5);
                                }
                                arrayList.add(new AccessDescription(objectIdentifier, createGeneralName(strSubstring5, strSubstring6)));
                            }
                            if (iOneOf2 == 5) {
                                certificateExtensions2.set(SubjectInfoAccessExtension.NAME, new SubjectInfoAccessExtension(arrayList));
                            } else {
                                certificateExtensions2.set(AuthorityInfoAccessExtension.NAME, new AuthorityInfoAccessExtension(arrayList));
                            }
                            break;
                        } else {
                            throw new Exception(rb.getString("Illegal.value.") + str2);
                        }
                    case 7:
                    default:
                        throw new Exception(rb.getString("Unknown.extension.type.") + str2);
                    case 8:
                        if (strSubstring2 != null) {
                            String[] strArrSplit3 = strSubstring2.split(",");
                            GeneralNames generalNames2 = new GeneralNames();
                            for (String str8 : strArrSplit3) {
                                int iIndexOf7 = str8.indexOf(58);
                                if (iIndexOf7 < 0) {
                                    throw new Exception("Illegal item " + str8 + " in " + str2);
                                }
                                generalNames2.add(createGeneralName(str8.substring(0, iIndexOf7), str8.substring(iIndexOf7 + 1)));
                            }
                            certificateExtensions2.set(CRLDistributionPointsExtension.NAME, new CRLDistributionPointsExtension(z3, (List<DistributionPoint>) Collections.singletonList(new DistributionPoint(generalNames2, (boolean[]) null, (GeneralNames) null))));
                            break;
                        } else {
                            throw new Exception(rb.getString("Illegal.value.") + str2);
                        }
                }
            }
        }
        certificateExtensions2.set(SubjectKeyIdentifierExtension.NAME, new SubjectKeyIdentifierExtension(new KeyIdentifier(publicKey).getIdentifier()));
        if (publicKey2 != null && !publicKey.equals(publicKey2)) {
            certificateExtensions2.set(AuthorityKeyIdentifierExtension.NAME, new AuthorityKeyIdentifierExtension(new KeyIdentifier(publicKey2), null, null));
        }
        return certificateExtensions2;
    }

    private Date getLastDate(Date date, long j2) throws Exception {
        Date date2 = new Date();
        date2.setTime(date.getTime() + (j2 * 1000 * 24 * 60 * 60));
        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        gregorianCalendar.setTime(date2);
        if (gregorianCalendar.get(1) > 9999) {
            throw new Exception("Validity period ends at calendar year " + gregorianCalendar.get(1) + " which is greater than 9999");
        }
        return date2;
    }

    private boolean isTrustedCert(Certificate certificate) throws KeyStoreException {
        if (this.caks != null && this.caks.getCertificateAlias(certificate) != null) {
            return true;
        }
        String certificateAlias = this.keyStore.getCertificateAlias(certificate);
        return certificateAlias != null && this.keyStore.isCertificateEntry(certificateAlias);
    }

    private void checkWeak(String str, String str2, Key key) {
        if (str2 != null) {
            if (!DISABLED_CHECK.permits(SIG_PRIMITIVE_SET, str2, (AlgorithmParameters) null)) {
                this.weakWarnings.add(String.format(rb.getString("whose.sigalg.disabled"), str, str2));
            } else if (!LEGACY_CHECK.permits(SIG_PRIMITIVE_SET, str2, (AlgorithmParameters) null)) {
                this.weakWarnings.add(String.format(rb.getString("whose.sigalg.weak"), str, str2));
            }
        }
        if (key != null) {
            if (!DISABLED_CHECK.permits(SIG_PRIMITIVE_SET, key)) {
                this.weakWarnings.add(String.format(rb.getString("whose.key.disabled"), str, String.format(rb.getString("key.bit"), Integer.valueOf(KeyUtil.getKeySize(key)), fullDisplayAlgName(key))));
            } else if (!LEGACY_CHECK.permits(SIG_PRIMITIVE_SET, key)) {
                this.weakWarnings.add(String.format(rb.getString("whose.key.weak"), str, String.format(rb.getString("key.bit"), Integer.valueOf(KeyUtil.getKeySize(key)), fullDisplayAlgName(key))));
            }
        }
    }

    private void checkWeak(String str, Certificate[] certificateArr) throws KeyStoreException {
        for (int i2 = 0; i2 < certificateArr.length; i2++) {
            Certificate certificate = certificateArr[i2];
            if (certificate instanceof X509Certificate) {
                X509Certificate x509Certificate = (X509Certificate) certificate;
                String strOneInMany = str;
                if (certificateArr.length > 1) {
                    strOneInMany = oneInMany(str, i2, certificateArr.length);
                }
                checkWeak(strOneInMany, x509Certificate);
            }
        }
    }

    private void checkWeak(String str, Certificate certificate) throws KeyStoreException {
        if (certificate instanceof X509Certificate) {
            X509Certificate x509Certificate = (X509Certificate) certificate;
            checkWeak(str, isTrustedCert(certificate) ? null : x509Certificate.getSigAlgName(), x509Certificate.getPublicKey());
        }
    }

    private void checkWeak(String str, PKCS10 pkcs10) {
        checkWeak(str, pkcs10.getSigAlg(), pkcs10.getSubjectPublicKeyInfo());
    }

    private void checkWeak(String str, CRL crl, Key key) {
        if (crl instanceof X509CRLImpl) {
            checkWeak(str, ((X509CRLImpl) crl).getSigAlgName(), key);
        }
    }

    private void printWeakWarnings(boolean z2) {
        if (!this.weakWarnings.isEmpty() && !this.nowarn) {
            System.err.println("\nWarning:");
            Iterator<String> it = this.weakWarnings.iterator();
            while (it.hasNext()) {
                System.err.println(it.next());
            }
            if (z2) {
                System.err.println();
            }
        }
        this.weakWarnings.clear();
    }

    private void usage() {
        Command command;
        if (this.command != null) {
            System.err.println("keytool " + ((Object) this.command) + rb.getString(".OPTION."));
            System.err.println();
            System.err.println(rb.getString(this.command.description));
            System.err.println();
            System.err.println(rb.getString("Options."));
            System.err.println();
            String[] strArr = new String[this.command.options.length];
            String[] strArr2 = new String[this.command.options.length];
            int length = 0;
            for (int i2 = 0; i2 < strArr.length; i2++) {
                Option option = this.command.options[i2];
                strArr[i2] = option.toString();
                if (option.arg != null) {
                    int i3 = i2;
                    strArr[i3] = strArr[i3] + " " + option.arg;
                }
                if (strArr[i2].length() > length) {
                    length = strArr[i2].length();
                }
                strArr2[i2] = rb.getString(option.description);
            }
            for (int i4 = 0; i4 < strArr.length; i4++) {
                System.err.printf(" %-" + length + "s  %s\n", strArr[i4], strArr2[i4]);
            }
            System.err.println();
            System.err.println(rb.getString("Use.keytool.help.for.all.available.commands"));
            return;
        }
        System.err.println(rb.getString("Key.and.Certificate.Management.Tool"));
        System.err.println();
        System.err.println(rb.getString("Commands."));
        System.err.println();
        Command[] commandArrValues = Command.values();
        int length2 = commandArrValues.length;
        for (int i5 = 0; i5 < length2 && (command = commandArrValues[i5]) != Command.KEYCLONE; i5++) {
            System.err.printf(" %-20s%s\n", command, rb.getString(command.description));
        }
        System.err.println();
        System.err.println(rb.getString("Use.keytool.command.name.help.for.usage.of.command.name"));
    }

    private void tinyHelp() {
        usage();
        if (this.debug) {
            throw new RuntimeException("NO BIG ERROR, SORRY");
        }
        System.exit(1);
    }

    private void errorNeedArgument(String str) {
        System.err.println(new MessageFormat(rb.getString("Command.option.flag.needs.an.argument.")).format(new Object[]{str}));
        tinyHelp();
    }

    private char[] getPass(String str, String str2) {
        char[] passWithModifier = KeyStoreUtil.getPassWithModifier(str, str2, rb, collator);
        if (passWithModifier != null) {
            return passWithModifier;
        }
        tinyHelp();
        return null;
    }
}
