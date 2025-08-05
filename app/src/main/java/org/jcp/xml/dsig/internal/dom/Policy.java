package org.jcp.xml.dsig.internal.dom;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.Security;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/Policy.class */
public final class Policy {
    private static Set<URI> disallowedAlgs = new HashSet();
    private static int maxTrans = Integer.MAX_VALUE;
    private static int maxRefs = Integer.MAX_VALUE;
    private static Set<String> disallowedRefUriSchemes = new HashSet();
    private static Map<String, Integer> minKeyMap = new HashMap();
    private static boolean noDuplicateIds = false;
    private static boolean noRMLoops = false;

    static {
        try {
            initialize();
        } catch (Exception e2) {
            throw new SecurityException("Cannot initialize the secure validation policy", e2);
        }
    }

    private Policy() {
    }

    private static void initialize() {
        String[] strArrSplit;
        String str = (String) AccessController.doPrivileged(() -> {
            return Security.getProperty("jdk.xml.dsig.secureValidationPolicy");
        });
        if (str == null || str.isEmpty()) {
            return;
        }
        for (String str2 : str.split(",")) {
            strArrSplit = str2.split("\\s");
            switch (strArrSplit[0]) {
                case "disallowAlg":
                    if (strArrSplit.length != 2) {
                        error(str2);
                    }
                    disallowedAlgs.add(URI.create(strArrSplit[1]));
                    break;
                case "maxTransforms":
                    if (strArrSplit.length != 2) {
                        error(str2);
                    }
                    maxTrans = Integer.parseUnsignedInt(strArrSplit[1]);
                    break;
                case "maxReferences":
                    if (strArrSplit.length != 2) {
                        error(str2);
                    }
                    maxRefs = Integer.parseUnsignedInt(strArrSplit[1]);
                    break;
                case "disallowReferenceUriSchemes":
                    if (strArrSplit.length == 1) {
                        error(str2);
                    }
                    for (int i2 = 1; i2 < strArrSplit.length; i2++) {
                        disallowedRefUriSchemes.add(strArrSplit[i2].toLowerCase(Locale.ROOT));
                    }
                    break;
                case "minKeySize":
                    if (strArrSplit.length != 3) {
                        error(str2);
                    }
                    minKeyMap.put(strArrSplit[1], Integer.valueOf(Integer.parseUnsignedInt(strArrSplit[2])));
                    break;
                case "noDuplicateIds":
                    if (strArrSplit.length != 1) {
                        error(str2);
                    }
                    noDuplicateIds = true;
                    break;
                case "noRetrievalMethodLoops":
                    if (strArrSplit.length != 1) {
                        error(str2);
                    }
                    noRMLoops = true;
                    break;
                default:
                    error(str2);
                    break;
            }
        }
    }

    public static boolean restrictAlg(String str) {
        try {
            return disallowedAlgs.contains(new URI(str));
        } catch (URISyntaxException e2) {
            return false;
        }
    }

    public static boolean restrictNumTransforms(int i2) {
        return i2 > maxTrans;
    }

    public static boolean restrictNumReferences(int i2) {
        return i2 > maxRefs;
    }

    public static boolean restrictReferenceUriScheme(String str) {
        String scheme;
        if (str != null && (scheme = URI.create(str).getScheme()) != null) {
            return disallowedRefUriSchemes.contains(scheme.toLowerCase(Locale.ROOT));
        }
        return false;
    }

    public static boolean restrictKey(String str, int i2) {
        return i2 < minKeyMap.getOrDefault(str, 0).intValue();
    }

    public static boolean restrictDuplicateIds() {
        return noDuplicateIds;
    }

    public static boolean restrictRetrievalMethodLoops() {
        return noRMLoops;
    }

    public static Set<URI> disabledAlgs() {
        return Collections.unmodifiableSet(disallowedAlgs);
    }

    public static int maxTransforms() {
        return maxTrans;
    }

    public static int maxReferences() {
        return maxRefs;
    }

    public static Set<String> disabledReferenceUriSchemes() {
        return Collections.unmodifiableSet(disallowedRefUriSchemes);
    }

    public static int minKeySize(String str) {
        return minKeyMap.getOrDefault(str, 0).intValue();
    }

    private static void error(String str) {
        throw new IllegalArgumentException("Invalid jdk.xml.dsig.secureValidationPolicy entry: " + str);
    }
}
