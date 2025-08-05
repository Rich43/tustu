package com.sun.webkit.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.IDN;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/network/PublicSuffixes.class */
final class PublicSuffixes {
    private static final Logger logger = Logger.getLogger(PublicSuffixes.class.getName());
    private static final Map<String, Rule> RULES = loadRules("effective_tld_names.dat");

    /* loaded from: jfxrt.jar:com/sun/webkit/network/PublicSuffixes$Rule.class */
    private enum Rule {
        SIMPLE_RULE,
        WILDCARD_RULE,
        EXCEPTION_RULE
    }

    private PublicSuffixes() {
        throw new AssertionError();
    }

    static boolean isPublicSuffix(String domain) {
        Rule rule;
        if (domain.length() == 0 || (rule = RULES.get(domain)) == Rule.EXCEPTION_RULE) {
            return false;
        }
        if (rule == Rule.SIMPLE_RULE || rule == Rule.WILDCARD_RULE) {
            return true;
        }
        int pos = domain.indexOf(46) + 1;
        if (pos == 0) {
            pos = domain.length();
        }
        String parent = domain.substring(pos);
        return RULES.get(parent) == Rule.WILDCARD_RULE;
    }

    private static Map<String, Rule> loadRules(String resourceName) {
        Map<String, Rule> mapEmptyMap;
        logger.log(Level.FINEST, "resourceName: [{0}]", resourceName);
        Map<String, Rule> result = null;
        InputStream is = PublicSuffixes.class.getResourceAsStream(resourceName);
        if (is != null) {
            BufferedReader reader = null;
            try {
                try {
                    reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    result = loadRules(reader);
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException ex) {
                            logger.log(Level.WARNING, "Unexpected error", (Throwable) ex);
                        }
                    }
                } catch (IOException ex2) {
                    logger.log(Level.WARNING, "Unexpected error", (Throwable) ex2);
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException ex3) {
                            logger.log(Level.WARNING, "Unexpected error", (Throwable) ex3);
                        }
                    }
                }
            } catch (Throwable th) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex4) {
                        logger.log(Level.WARNING, "Unexpected error", (Throwable) ex4);
                        throw th;
                    }
                }
                throw th;
            }
        } else {
            logger.log(Level.WARNING, "Resource not found: [{0}]", resourceName);
        }
        if (result != null) {
            mapEmptyMap = Collections.unmodifiableMap(result);
        } else {
            mapEmptyMap = Collections.emptyMap();
        }
        Map<String, Rule> result2 = mapEmptyMap;
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "result: {0}", toLogString(result2));
        }
        return result2;
    }

    private static Map<String, Rule> loadRules(BufferedReader reader) throws IOException {
        Rule rule;
        Map<String, Rule> result = new LinkedHashMap<>();
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                String line2 = line.split("\\s+", 2)[0];
                if (line2.length() != 0 && !line2.startsWith("//")) {
                    if (line2.startsWith("!")) {
                        line2 = line2.substring(1);
                        rule = Rule.EXCEPTION_RULE;
                    } else if (line2.startsWith("*.")) {
                        line2 = line2.substring(2);
                        rule = Rule.WILDCARD_RULE;
                    } else {
                        rule = Rule.SIMPLE_RULE;
                    }
                    try {
                        line2 = IDN.toASCII(line2, 1);
                        result.put(line2, rule);
                    } catch (Exception ex) {
                        logger.log(Level.WARNING, String.format("Error parsing rule: [%s]", line2), (Throwable) ex);
                    }
                }
            } else {
                return result;
            }
        }
    }

    private static String toLogString(Map<String, Rule> rules) {
        if (rules.isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Rule> entry : rules.entrySet()) {
            sb.append(String.format("%n    ", new Object[0]));
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append((Object) entry.getValue());
        }
        return sb.toString();
    }
}
