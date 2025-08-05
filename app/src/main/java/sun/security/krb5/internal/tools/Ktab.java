package sun.security.krb5.internal.tools;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.internal.crypto.EType;
import sun.security.krb5.internal.ktab.KeyTab;
import sun.security.krb5.internal.ktab.KeyTabEntry;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/krb5/internal/tools/Ktab.class */
public class Ktab {
    KeyTab table;
    char action;
    String name;
    String principal;
    boolean showEType;
    boolean showTime;
    int etype = -1;
    char[] password = null;
    boolean forced = false;
    boolean append = false;
    int vDel = -1;
    int vAdd = -1;

    public static void main(String[] strArr) {
        Ktab ktab = new Ktab();
        if (strArr.length == 1 && strArr[0].equalsIgnoreCase("-help")) {
            ktab.printHelp();
        }
        if (strArr == null || strArr.length == 0) {
            ktab.action = 'l';
        } else {
            ktab.processArgs(strArr);
        }
        ktab.table = KeyTab.getInstance(ktab.name);
        if (ktab.table.isMissing() && ktab.action != 'a') {
            if (ktab.name == null) {
                System.out.println("No default key table exists.");
            } else {
                System.out.println("Key table " + ktab.name + " does not exist.");
            }
            System.exit(-1);
        }
        if (!ktab.table.isValid()) {
            if (ktab.name == null) {
                System.out.println("The format of the default key table  is incorrect.");
            } else {
                System.out.println("The format of key table " + ktab.name + " is incorrect.");
            }
            System.exit(-1);
        }
        switch (ktab.action) {
            case 'a':
                ktab.addEntry();
                break;
            case 'd':
                ktab.deleteEntry();
                break;
            case 'l':
                ktab.listKt();
                break;
            default:
                ktab.error("A command must be provided");
                break;
        }
    }

    void processArgs(String[] strArr) {
        boolean z2 = false;
        int i2 = 0;
        while (i2 < strArr.length) {
            if (strArr[i2].startsWith(LanguageTag.SEP)) {
                switch (strArr[i2].toLowerCase(Locale.US)) {
                    case "-l":
                        this.action = 'l';
                        break;
                    case "-a":
                        this.action = 'a';
                        i2++;
                        if (i2 >= strArr.length || strArr[i2].startsWith(LanguageTag.SEP)) {
                            error("A principal name must be specified after -a");
                        }
                        this.principal = strArr[i2];
                        break;
                    case "-d":
                        this.action = 'd';
                        i2++;
                        if (i2 >= strArr.length || strArr[i2].startsWith(LanguageTag.SEP)) {
                            error("A principal name must be specified after -d");
                        }
                        this.principal = strArr[i2];
                        break;
                    case "-e":
                        if (this.action == 'l') {
                            this.showEType = true;
                            break;
                        } else if (this.action == 'd') {
                            i2++;
                            if (i2 >= strArr.length || strArr[i2].startsWith(LanguageTag.SEP)) {
                                error("An etype must be specified after -e");
                            }
                            try {
                                this.etype = Integer.parseInt(strArr[i2]);
                                if (this.etype > 0) {
                                    break;
                                } else {
                                    throw new NumberFormatException();
                                    break;
                                }
                            } catch (NumberFormatException e2) {
                                error(strArr[i2] + " is not a valid etype");
                                break;
                            }
                        } else {
                            error(strArr[i2] + " is not valid after -" + this.action);
                            break;
                        }
                    case "-n":
                        i2++;
                        if (i2 >= strArr.length || strArr[i2].startsWith(LanguageTag.SEP)) {
                            error("A KVNO must be specified after -n");
                        }
                        try {
                            this.vAdd = Integer.parseInt(strArr[i2]);
                            if (this.vAdd >= 0) {
                                break;
                            } else {
                                throw new NumberFormatException();
                                break;
                            }
                        } catch (NumberFormatException e3) {
                            error(strArr[i2] + " is not a valid KVNO");
                            break;
                        }
                        break;
                    case "-k":
                        i2++;
                        if (i2 >= strArr.length || strArr[i2].startsWith(LanguageTag.SEP)) {
                            error("A keytab name must be specified after -k");
                        }
                        if (strArr[i2].length() >= 5 && strArr[i2].substring(0, 5).equalsIgnoreCase("FILE:")) {
                            this.name = strArr[i2].substring(5);
                            break;
                        } else {
                            this.name = strArr[i2];
                            break;
                        }
                        break;
                    case "-t":
                        this.showTime = true;
                        break;
                    case "-f":
                        this.forced = true;
                        break;
                    case "-append":
                        this.append = true;
                        break;
                    default:
                        error("Unknown command: " + strArr[i2]);
                        break;
                }
            } else {
                if (z2) {
                    error("Useless extra argument " + strArr[i2]);
                }
                if (this.action == 'a') {
                    this.password = strArr[i2].toCharArray();
                } else if (this.action == 'd') {
                    switch (strArr[i2]) {
                        case "all":
                            this.vDel = -1;
                            break;
                        case "old":
                            this.vDel = -2;
                            break;
                        default:
                            try {
                                this.vDel = Integer.parseInt(strArr[i2]);
                                if (this.vDel >= 0) {
                                    break;
                                } else {
                                    throw new NumberFormatException();
                                    break;
                                }
                            } catch (NumberFormatException e4) {
                                error(strArr[i2] + " is not a valid KVNO");
                                break;
                            }
                    }
                } else {
                    error("Useless extra argument " + strArr[i2]);
                }
                z2 = true;
            }
            i2++;
        }
    }

    void addEntry() {
        PrincipalName principalName = null;
        try {
            principalName = new PrincipalName(this.principal);
        } catch (KrbException e2) {
            System.err.println("Failed to add " + this.principal + " to keytab.");
            e2.printStackTrace();
            System.exit(-1);
        }
        if (this.password == null) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Password for " + principalName.toString() + CallSiteDescriptor.TOKEN_DELIMITER);
                System.out.flush();
                this.password = bufferedReader.readLine().toCharArray();
            } catch (IOException e3) {
                System.err.println("Failed to read the password.");
                e3.printStackTrace();
                System.exit(-1);
            }
        }
        try {
            this.table.addEntry(principalName, this.password, this.vAdd, this.append);
            Arrays.fill(this.password, '0');
            this.table.save();
            System.out.println("Done!");
            System.out.println("Service key for " + this.principal + " is saved in " + this.table.tabName());
        } catch (IOException e4) {
            System.err.println("Failed to save new entry.");
            e4.printStackTrace();
            System.exit(-1);
        } catch (KrbException e5) {
            System.err.println("Failed to add " + this.principal + " to keytab.");
            e5.printStackTrace();
            System.exit(-1);
        }
    }

    void listKt() {
        System.out.println("Keytab name: " + this.table.tabName());
        KeyTabEntry[] entries = this.table.getEntries();
        if (entries != null && entries.length > 0) {
            String[][] strArr = new String[entries.length + 1][this.showTime ? 3 : 2];
            int i2 = 0 + 1;
            strArr[0][0] = "KVNO";
            if (this.showTime) {
                i2++;
                strArr[0][i2] = "Timestamp";
            }
            int i3 = i2;
            int i4 = i2 + 1;
            strArr[0][i3] = "Principal";
            for (int i5 = 0; i5 < entries.length; i5++) {
                int i6 = 0 + 1;
                strArr[i5 + 1][0] = entries[i5].getKey().getKeyVersionNumber().toString();
                if (this.showTime) {
                    i6++;
                    strArr[i5 + 1][i6] = DateFormat.getDateTimeInstance(3, 3).format(new Date(entries[i5].getTimeStamp().getTime()));
                }
                String string = entries[i5].getService().toString();
                if (this.showEType) {
                    int eType = entries[i5].getKey().getEType();
                    int i7 = i6;
                    i4 = i6 + 1;
                    strArr[i5 + 1][i7] = string + " (" + eType + CallSiteDescriptor.TOKEN_DELIMITER + EType.toString(eType) + ")";
                } else {
                    int i8 = i6;
                    i4 = i6 + 1;
                    strArr[i5 + 1][i8] = string;
                }
            }
            int[] iArr = new int[i4];
            for (int i9 = 0; i9 < i4; i9++) {
                for (int i10 = 0; i10 <= entries.length; i10++) {
                    if (strArr[i10][i9].length() > iArr[i9]) {
                        iArr[i9] = strArr[i10][i9].length();
                    }
                }
                if (i9 != 0) {
                    iArr[i9] = -iArr[i9];
                }
            }
            for (int i11 = 0; i11 < i4; i11++) {
                System.out.printf(FXMLLoader.RESOURCE_KEY_PREFIX + iArr[i11] + "s ", strArr[0][i11]);
            }
            System.out.println();
            for (int i12 = 0; i12 < i4; i12++) {
                for (int i13 = 0; i13 < Math.abs(iArr[i12]); i13++) {
                    System.out.print(LanguageTag.SEP);
                }
                System.out.print(" ");
            }
            System.out.println();
            for (int i14 = 0; i14 < entries.length; i14++) {
                for (int i15 = 0; i15 < i4; i15++) {
                    System.out.printf(FXMLLoader.RESOURCE_KEY_PREFIX + iArr[i15] + "s ", strArr[i14 + 1][i15]);
                }
                System.out.println();
            }
            return;
        }
        System.out.println("0 entry.");
    }

    void deleteEntry() {
        PrincipalName principalName = null;
        try {
            principalName = new PrincipalName(this.principal);
            if (!this.forced) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Are you sure you want to delete service key(s) for " + principalName.toString() + " (" + (this.etype == -1 ? "all etypes" : "etype=" + this.etype) + ", " + (this.vDel == -1 ? "all kvno" : this.vDel == -2 ? "old kvno" : "kvno=" + this.vDel) + ") in " + this.table.tabName() + "? (Y/[N]): ");
                System.out.flush();
                String line = bufferedReader.readLine();
                if (!line.equalsIgnoreCase(Constants._TAG_Y) && !line.equalsIgnoreCase("Yes")) {
                    System.exit(0);
                }
            }
        } catch (IOException e2) {
            System.err.println("Error occurred while deleting the entry.  Deletion failed.");
            e2.printStackTrace();
            System.exit(-1);
        } catch (KrbException e3) {
            System.err.println("Error occurred while deleting the entry. Deletion failed.");
            e3.printStackTrace();
            System.exit(-1);
        }
        int iDeleteEntries = this.table.deleteEntries(principalName, this.etype, this.vDel);
        if (iDeleteEntries == 0) {
            System.err.println("No matched entry in the keytab. Deletion fails.");
            System.exit(-1);
            return;
        }
        try {
            this.table.save();
        } catch (IOException e4) {
            System.err.println("Error occurs while saving the keytab. Deletion fails.");
            e4.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Done! " + iDeleteEntries + " entries removed.");
    }

    void error(String... strArr) {
        for (String str : strArr) {
            System.out.println("Error: " + str + ".");
        }
        printHelp();
        System.exit(-1);
    }

    void printHelp() {
        System.out.println("\nUsage: ktab <commands> <options>");
        System.out.println();
        System.out.println("Available commands:");
        System.out.println();
        System.out.println("-l [-e] [-t]\n    list the keytab name and entries. -e with etype, -t with timestamp.");
        System.out.println("-a <principal name> [<password>] [-n <kvno>] [-append]\n    add new key entries to the keytab for the given principal name with\n    optional <password>. If a <kvno> is specified, new keys' Key Version\n    Numbers equal to the value, otherwise, automatically incrementing\n    the Key Version Numbers. If -append is specified, new keys are\n    appended to the keytab, otherwise, old keys for the\n    same principal are removed.");
        System.out.println("-d <principal name> [-f] [-e <etype>] [<kvno> | all | old]\n    delete key entries from the keytab for the specified principal. If\n    <kvno> is specified, delete keys whose Key Version Numbers match\n    kvno. If \"all\" is specified, delete all keys. If \"old\" is specified,\n    delete all keys except those with the highest kvno. Default action\n    is \"all\". If <etype> is specified, only keys of this encryption type\n    are deleted. <etype> should be specified as the numberic value etype\n    defined in RFC 3961, section 8. A prompt to confirm the deletion is\n    displayed unless -f is specified.");
        System.out.println();
        System.out.println("Common option(s):");
        System.out.println();
        System.out.println("-k <keytab name>\n    specify keytab name and path with prefix FILE:");
    }
}
