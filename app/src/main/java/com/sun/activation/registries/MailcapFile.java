package com.sun.activation.registries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/sun/activation/registries/MailcapFile.class */
public class MailcapFile {
    private Map type_hash = new HashMap();
    private Map fallback_hash = new HashMap();
    private Map native_commands = new HashMap();
    private static boolean addReverse;

    static {
        addReverse = false;
        try {
            addReverse = Boolean.getBoolean("javax.activation.addreverse");
        } catch (Throwable th) {
        }
    }

    public MailcapFile(String new_fname) throws IOException {
        if (LogSupport.isLoggable()) {
            LogSupport.log("new MailcapFile: file " + new_fname);
        }
        FileReader reader = null;
        try {
            reader = new FileReader(new_fname);
            parse(new BufferedReader(reader));
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e2) {
                }
            }
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e3) {
                }
            }
            throw th;
        }
    }

    public MailcapFile(InputStream is) throws IOException {
        if (LogSupport.isLoggable()) {
            LogSupport.log("new MailcapFile: InputStream");
        }
        parse(new BufferedReader(new InputStreamReader(is, "iso-8859-1")));
    }

    public MailcapFile() {
        if (LogSupport.isLoggable()) {
            LogSupport.log("new MailcapFile: default");
        }
    }

    public Map getMailcapList(String mime_type) {
        Map search_result = (Map) this.type_hash.get(mime_type);
        int separator = mime_type.indexOf(47);
        String subtype = mime_type.substring(separator + 1);
        if (!subtype.equals("*")) {
            String type = mime_type.substring(0, separator + 1) + "*";
            Map wildcard_result = (Map) this.type_hash.get(type);
            if (wildcard_result != null) {
                search_result = search_result != null ? mergeResults(search_result, wildcard_result) : wildcard_result;
            }
        }
        return search_result;
    }

    public Map getMailcapFallbackList(String mime_type) {
        Map search_result = (Map) this.fallback_hash.get(mime_type);
        int separator = mime_type.indexOf(47);
        String subtype = mime_type.substring(separator + 1);
        if (!subtype.equals("*")) {
            String type = mime_type.substring(0, separator + 1) + "*";
            Map wildcard_result = (Map) this.fallback_hash.get(type);
            if (wildcard_result != null) {
                search_result = search_result != null ? mergeResults(search_result, wildcard_result) : wildcard_result;
            }
        }
        return search_result;
    }

    public String[] getMimeTypes() {
        Set types = new HashSet(this.type_hash.keySet());
        types.addAll(this.fallback_hash.keySet());
        types.addAll(this.native_commands.keySet());
        String[] mts = new String[types.size()];
        return (String[]) types.toArray(mts);
    }

    public String[] getNativeCommands(String mime_type) {
        String[] cmds = null;
        List v2 = (List) this.native_commands.get(mime_type.toLowerCase(Locale.ENGLISH));
        if (v2 != null) {
            String[] cmds2 = new String[v2.size()];
            cmds = (String[]) v2.toArray(cmds2);
        }
        return cmds;
    }

    private Map mergeResults(Map first, Map second) {
        Map clonedHash = new HashMap(first);
        for (String verb : second.keySet()) {
            List cmdVector = (List) clonedHash.get(verb);
            if (cmdVector == null) {
                clonedHash.put(verb, second.get(verb));
            } else {
                List oldV = (List) second.get(verb);
                ArrayList arrayList = new ArrayList(cmdVector);
                arrayList.addAll(oldV);
                clonedHash.put(verb, arrayList);
            }
        }
        return clonedHash;
    }

    public void appendToMailcap(String mail_cap) {
        if (LogSupport.isLoggable()) {
            LogSupport.log("appendToMailcap: " + mail_cap);
        }
        try {
            parse(new StringReader(mail_cap));
        } catch (IOException e2) {
        }
    }

    private void parse(Reader reader) throws IOException {
        BufferedReader buf_reader = new BufferedReader(reader);
        String continued = null;
        while (true) {
            String line = buf_reader.readLine();
            if (line != null) {
                String line2 = line.trim();
                try {
                    if (line2.charAt(0) != '#') {
                        if (line2.charAt(line2.length() - 1) == '\\') {
                            if (continued != null) {
                                continued = continued + line2.substring(0, line2.length() - 1);
                            } else {
                                continued = line2.substring(0, line2.length() - 1);
                            }
                        } else if (continued != null) {
                            try {
                                parseLine(continued + line2);
                            } catch (MailcapParseException e2) {
                            }
                            continued = null;
                        } else {
                            try {
                                parseLine(line2);
                            } catch (MailcapParseException e3) {
                            }
                        }
                    }
                } catch (StringIndexOutOfBoundsException e4) {
                }
            } else {
                return;
            }
        }
    }

    protected void parseLine(String mailcapEntry) throws MailcapParseException, IOException {
        int currentToken;
        MailcapTokenizer tokenizer = new MailcapTokenizer(mailcapEntry);
        tokenizer.setIsAutoquoting(false);
        if (LogSupport.isLoggable()) {
            LogSupport.log("parse: " + mailcapEntry);
        }
        int currentToken2 = tokenizer.nextToken();
        if (currentToken2 != 2) {
            reportParseError(2, currentToken2, tokenizer.getCurrentTokenValue());
        }
        String primaryType = tokenizer.getCurrentTokenValue().toLowerCase(Locale.ENGLISH);
        String subType = "*";
        int currentToken3 = tokenizer.nextToken();
        if (currentToken3 != 47 && currentToken3 != 59) {
            reportParseError(47, 59, currentToken3, tokenizer.getCurrentTokenValue());
        }
        if (currentToken3 == 47) {
            int currentToken4 = tokenizer.nextToken();
            if (currentToken4 != 2) {
                reportParseError(2, currentToken4, tokenizer.getCurrentTokenValue());
            }
            subType = tokenizer.getCurrentTokenValue().toLowerCase(Locale.ENGLISH);
            currentToken3 = tokenizer.nextToken();
        }
        String mimeType = primaryType + "/" + subType;
        if (LogSupport.isLoggable()) {
            LogSupport.log("  Type: " + mimeType);
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (currentToken3 != 59) {
            reportParseError(59, currentToken3, tokenizer.getCurrentTokenValue());
        }
        tokenizer.setIsAutoquoting(true);
        int currentToken5 = tokenizer.nextToken();
        tokenizer.setIsAutoquoting(false);
        if (currentToken5 != 2 && currentToken5 != 59) {
            reportParseError(2, 59, currentToken5, tokenizer.getCurrentTokenValue());
        }
        if (currentToken5 == 2) {
            List v2 = (List) this.native_commands.get(mimeType);
            if (v2 == null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(mailcapEntry);
                this.native_commands.put(mimeType, arrayList);
            } else {
                v2.add(mailcapEntry);
            }
        }
        if (currentToken5 != 59) {
            currentToken5 = tokenizer.nextToken();
        }
        if (currentToken5 != 59) {
            if (currentToken5 != 5) {
                reportParseError(5, 59, currentToken5, tokenizer.getCurrentTokenValue());
                return;
            }
            return;
        }
        boolean isFallback = false;
        do {
            int currentToken6 = tokenizer.nextToken();
            if (currentToken6 != 2) {
                reportParseError(2, currentToken6, tokenizer.getCurrentTokenValue());
            }
            String paramName = tokenizer.getCurrentTokenValue().toLowerCase(Locale.ENGLISH);
            currentToken = tokenizer.nextToken();
            if (currentToken != 61 && currentToken != 59 && currentToken != 5) {
                reportParseError(61, 59, 5, currentToken, tokenizer.getCurrentTokenValue());
            }
            if (currentToken == 61) {
                tokenizer.setIsAutoquoting(true);
                int currentToken7 = tokenizer.nextToken();
                tokenizer.setIsAutoquoting(false);
                if (currentToken7 != 2) {
                    reportParseError(2, currentToken7, tokenizer.getCurrentTokenValue());
                }
                String paramValue = tokenizer.getCurrentTokenValue();
                if (paramName.startsWith("x-java-")) {
                    String commandName = paramName.substring(7);
                    if (commandName.equals("fallback-entry") && paramValue.equalsIgnoreCase("true")) {
                        isFallback = true;
                    } else {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("    Command: " + commandName + ", Class: " + paramValue);
                        }
                        List classes = (List) linkedHashMap.get(commandName);
                        if (classes == null) {
                            classes = new ArrayList();
                            linkedHashMap.put(commandName, classes);
                        }
                        if (addReverse) {
                            classes.add(0, paramValue);
                        } else {
                            classes.add(paramValue);
                        }
                    }
                }
                currentToken = tokenizer.nextToken();
            }
        } while (currentToken == 59);
        Map masterHash = isFallback ? this.fallback_hash : this.type_hash;
        Map curcommands = (Map) masterHash.get(mimeType);
        if (curcommands == null) {
            masterHash.put(mimeType, linkedHashMap);
            return;
        }
        if (LogSupport.isLoggable()) {
            LogSupport.log("Merging commands for type " + mimeType);
        }
        for (String cmdName : curcommands.keySet()) {
            List ccv = (List) curcommands.get(cmdName);
            List<String> cv = (List) linkedHashMap.get(cmdName);
            if (cv != null) {
                for (String clazz : cv) {
                    if (!ccv.contains(clazz)) {
                        if (addReverse) {
                            ccv.add(0, clazz);
                        } else {
                            ccv.add(clazz);
                        }
                    }
                }
            }
        }
        for (K cmdName2 : linkedHashMap.keySet()) {
            if (!curcommands.containsKey(cmdName2)) {
                curcommands.put(cmdName2, (List) linkedHashMap.get(cmdName2));
            }
        }
    }

    protected static void reportParseError(int expectedToken, int actualToken, String actualTokenValue) throws MailcapParseException {
        throw new MailcapParseException("Encountered a " + MailcapTokenizer.nameForToken(actualToken) + " token (" + actualTokenValue + ") while expecting a " + MailcapTokenizer.nameForToken(expectedToken) + " token.");
    }

    protected static void reportParseError(int expectedToken, int otherExpectedToken, int actualToken, String actualTokenValue) throws MailcapParseException {
        throw new MailcapParseException("Encountered a " + MailcapTokenizer.nameForToken(actualToken) + " token (" + actualTokenValue + ") while expecting a " + MailcapTokenizer.nameForToken(expectedToken) + " or a " + MailcapTokenizer.nameForToken(otherExpectedToken) + " token.");
    }

    protected static void reportParseError(int expectedToken, int otherExpectedToken, int anotherExpectedToken, int actualToken, String actualTokenValue) throws MailcapParseException {
        if (LogSupport.isLoggable()) {
            LogSupport.log("PARSE ERROR: Encountered a " + MailcapTokenizer.nameForToken(actualToken) + " token (" + actualTokenValue + ") while expecting a " + MailcapTokenizer.nameForToken(expectedToken) + ", a " + MailcapTokenizer.nameForToken(otherExpectedToken) + ", or a " + MailcapTokenizer.nameForToken(anotherExpectedToken) + " token.");
        }
        throw new MailcapParseException("Encountered a " + MailcapTokenizer.nameForToken(actualToken) + " token (" + actualTokenValue + ") while expecting a " + MailcapTokenizer.nameForToken(expectedToken) + ", a " + MailcapTokenizer.nameForToken(otherExpectedToken) + ", or a " + MailcapTokenizer.nameForToken(anotherExpectedToken) + " token.");
    }
}
