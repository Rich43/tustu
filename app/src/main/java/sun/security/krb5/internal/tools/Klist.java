package sun.security.krb5.internal.tools;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.RealmException;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.ccache.Credentials;
import sun.security.krb5.internal.ccache.CredentialsCache;
import sun.security.krb5.internal.crypto.EType;
import sun.security.krb5.internal.ktab.KeyTab;
import sun.security.krb5.internal.ktab.KeyTabEntry;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/krb5/internal/tools/Klist.class */
public class Klist {
    Object target;
    char[] options = new char[4];
    String name;
    char action;
    private static boolean DEBUG = Krb5.DEBUG;

    public static void main(String[] strArr) {
        Klist klist = new Klist();
        if (strArr == null || strArr.length == 0) {
            klist.action = 'c';
        } else {
            klist.processArgs(strArr);
        }
        switch (klist.action) {
            case 'c':
                if (klist.name == null) {
                    klist.target = CredentialsCache.getInstance();
                    klist.name = CredentialsCache.cacheName();
                } else {
                    klist.target = CredentialsCache.getInstance(klist.name);
                }
                if (klist.target != null) {
                    klist.displayCache();
                    break;
                } else {
                    klist.displayMessage("Credentials cache");
                    System.exit(-1);
                    break;
                }
            case 'k':
                KeyTab keyTab = KeyTab.getInstance(klist.name);
                if (keyTab.isMissing()) {
                    System.out.println("KeyTab " + klist.name + " not found.");
                    System.exit(-1);
                } else if (!keyTab.isValid()) {
                    System.out.println("KeyTab " + klist.name + " format not supported.");
                    System.exit(-1);
                }
                klist.target = keyTab;
                klist.name = keyTab.tabName();
                klist.displayTab();
                break;
            default:
                if (klist.name != null) {
                    klist.printHelp();
                    System.exit(-1);
                    break;
                } else {
                    klist.target = CredentialsCache.getInstance();
                    klist.name = CredentialsCache.cacheName();
                    if (klist.target != null) {
                        klist.displayCache();
                        break;
                    } else {
                        klist.displayMessage("Credentials cache");
                        System.exit(-1);
                        break;
                    }
                }
        }
    }

    void processArgs(String[] strArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2].length() >= 2 && strArr[i2].startsWith(LanguageTag.SEP)) {
                switch (new Character(strArr[i2].charAt(1)).charValue()) {
                    case 'K':
                        this.options[1] = 'K';
                        break;
                    case 'a':
                        this.options[2] = 'a';
                        break;
                    case 'c':
                        this.action = 'c';
                        break;
                    case 'e':
                        this.options[0] = 'e';
                        break;
                    case 'f':
                        this.options[1] = 'f';
                        break;
                    case 'k':
                        this.action = 'k';
                        break;
                    case 'n':
                        this.options[3] = 'n';
                        break;
                    case 't':
                        this.options[2] = 't';
                        break;
                    default:
                        printHelp();
                        System.exit(-1);
                        break;
                }
            } else if (!strArr[i2].startsWith(LanguageTag.SEP) && i2 == strArr.length - 1) {
                this.name = strArr[i2];
            } else {
                printHelp();
                System.exit(-1);
            }
        }
    }

    void displayTab() {
        KeyTabEntry[] entries = ((KeyTab) this.target).getEntries();
        if (entries.length == 0) {
            System.out.println("\nKey tab: " + this.name + ",  0 entries found.\n");
            return;
        }
        if (entries.length == 1) {
            System.out.println("\nKey tab: " + this.name + ", " + entries.length + " entry found.\n");
        } else {
            System.out.println("\nKey tab: " + this.name + ", " + entries.length + " entries found.\n");
        }
        for (int i2 = 0; i2 < entries.length; i2++) {
            System.out.println("[" + (i2 + 1) + "] Service principal: " + entries[i2].getService().toString());
            System.out.println("\t KVNO: " + ((Object) entries[i2].getKey().getKeyVersionNumber()));
            if (this.options[0] == 'e') {
                System.out.println("\t Key type: " + entries[i2].getKey().getEType());
            }
            if (this.options[1] == 'K') {
                entries[i2].getKey();
                System.out.println("\t Key: " + entries[i2].getKeyString());
            }
            if (this.options[2] == 't') {
                System.out.println("\t Time stamp: " + format(entries[i2].getTimeStamp()));
            }
        }
    }

    void displayCache() {
        String str;
        String canonicalHostName;
        CredentialsCache credentialsCache = (CredentialsCache) this.target;
        Credentials[] credsList = credentialsCache.getCredsList();
        if (credsList == null) {
            System.out.println("No credentials available in the cache " + this.name);
            System.exit(-1);
        }
        System.out.println("\nCredentials cache: " + this.name);
        String string = credentialsCache.getPrimaryPrincipal().toString();
        if (credsList.length == 1) {
            System.out.println("\nDefault principal: " + string + ", " + credsList.length + " entry found.\n");
        } else {
            System.out.println("\nDefault principal: " + string + ", " + credsList.length + " entries found.\n");
        }
        if (credsList != null) {
            for (int i2 = 0; i2 < credsList.length; i2++) {
                try {
                    if (credsList[i2].getStartTime() != null) {
                        str = format(credsList[i2].getStartTime());
                    } else {
                        str = format(credsList[i2].getAuthTime());
                    }
                    String str2 = format(credsList[i2].getEndTime());
                    System.out.println("[" + (i2 + 1) + "]  Service Principal:  " + credsList[i2].getServicePrincipal().toString());
                    PrincipalName servicePrincipal2 = credsList[i2].getServicePrincipal2();
                    if (servicePrincipal2 != null) {
                        System.out.println("     Second Service:     " + ((Object) servicePrincipal2));
                    }
                    String string2 = credsList[i2].getClientPrincipal().toString();
                    if (!string2.equals(string)) {
                        System.out.println("     Client Principal:   " + string2);
                    }
                    System.out.println("     Valid starting:     " + str);
                    System.out.println("     Expires:            " + str2);
                    if (credsList[i2].getRenewTill() != null) {
                        System.out.println("     Renew until:        " + format(credsList[i2].getRenewTill()));
                    }
                    if (this.options[0] == 'e') {
                        String string3 = EType.toString(credsList[i2].getEType());
                        String string4 = EType.toString(credsList[i2].getTktEType());
                        if (credsList[i2].getTktEType2() == 0) {
                            System.out.println("     EType (skey, tkt):  " + string3 + ", " + string4);
                        } else {
                            System.out.println("     EType (skey, tkts): " + string3 + ", " + string4 + ", " + EType.toString(credsList[i2].getTktEType2()));
                        }
                    }
                    if (this.options[1] == 'f') {
                        System.out.println("     Flags:              " + credsList[i2].getTicketFlags().toString());
                    }
                    if (this.options[2] == 'a') {
                        boolean z2 = true;
                        InetAddress[] clientAddresses = credsList[i2].setKrbCreds().getClientAddresses();
                        if (clientAddresses != null) {
                            for (InetAddress inetAddress : clientAddresses) {
                                if (this.options[3] == 'n') {
                                    canonicalHostName = inetAddress.getHostAddress();
                                } else {
                                    canonicalHostName = inetAddress.getCanonicalHostName();
                                }
                                System.out.println("     " + (z2 ? "Addresses:" : "          ") + "       " + canonicalHostName);
                                z2 = false;
                            }
                        } else {
                            System.out.println("     [No host addresses info]");
                        }
                    }
                } catch (RealmException e2) {
                    System.out.println("Error reading principal from the entry.");
                    if (DEBUG) {
                        e2.printStackTrace();
                    }
                    System.exit(-1);
                }
            }
        } else {
            System.out.println("\nNo entries found.");
        }
        List<CredentialsCache.ConfigEntry> configEntries = credentialsCache.getConfigEntries();
        if (configEntries != null && !configEntries.isEmpty()) {
            System.out.println("\nConfig entries:");
            Iterator<CredentialsCache.ConfigEntry> it = configEntries.iterator();
            while (it.hasNext()) {
                System.out.println("     " + ((Object) it.next()));
            }
        }
    }

    void displayMessage(String str) {
        if (this.name == null) {
            System.out.println("Default " + str + " not found.");
        } else {
            System.out.println(str + " " + this.name + " not found.");
        }
    }

    private String format(KerberosTime kerberosTime) {
        String string = kerberosTime.toDate().toString();
        return string.substring(4, 7) + " " + string.substring(8, 10) + ", " + string.substring(24) + " " + string.substring(11, 19);
    }

    void printHelp() {
        System.out.println("\nUsage: klist [[-c] [-f] [-e] [-a [-n]]] [-k [-t] [-K]] [name]");
        System.out.println("   name\t name of credentials cache or  keytab with the prefix. File-based cache or keytab's prefix is FILE:.");
        System.out.println("   -c specifies that credential cache is to be listed");
        System.out.println("   -k specifies that key tab is to be listed");
        System.out.println("   options for credentials caches:");
        System.out.println("\t-f \t shows credentials flags");
        System.out.println("\t-e \t shows the encryption type");
        System.out.println("\t-a \t shows addresses");
        System.out.println("\t  -n \t   do not reverse-resolve addresses");
        System.out.println("   options for keytabs:");
        System.out.println("\t-t \t shows keytab entry timestamps");
        System.out.println("\t-K \t shows keytab entry key value");
        System.out.println("\t-e \t shows keytab entry key type");
        System.out.println("\nUsage: java sun.security.krb5.tools.Klist -help for help.");
    }
}
