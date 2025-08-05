package sun.security.util;

import java.security.AccessController;
import java.security.AlgorithmConstraints;
import java.security.PrivilegedAction;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: rt.jar:sun/security/util/AbstractAlgorithmConstraints.class */
public abstract class AbstractAlgorithmConstraints implements AlgorithmConstraints {
    protected final AlgorithmDecomposer decomposer;

    protected AbstractAlgorithmConstraints(AlgorithmDecomposer algorithmDecomposer) {
        this.decomposer = algorithmDecomposer;
    }

    static List<String> getAlgorithms(final String str) {
        String strSubstring = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.util.AbstractAlgorithmConstraints.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                return Security.getProperty(str);
            }
        });
        String[] strArrSplit = null;
        if (strSubstring != null && !strSubstring.isEmpty()) {
            if (strSubstring.length() >= 2 && strSubstring.charAt(0) == '\"' && strSubstring.charAt(strSubstring.length() - 1) == '\"') {
                strSubstring = strSubstring.substring(1, strSubstring.length() - 1);
            }
            strArrSplit = strSubstring.split(",");
            for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                strArrSplit[i2] = strArrSplit[i2].trim();
            }
        }
        if (strArrSplit == null) {
            return Collections.emptyList();
        }
        return new ArrayList(Arrays.asList(strArrSplit));
    }

    static boolean checkAlgorithm(List<String> list, String str, AlgorithmDecomposer algorithmDecomposer) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("No algorithm name specified");
        }
        Set<String> setDecompose = null;
        for (String str2 : list) {
            if (str2 != null && !str2.isEmpty()) {
                if (str2.equalsIgnoreCase(str)) {
                    return false;
                }
                if (setDecompose == null) {
                    setDecompose = algorithmDecomposer.decompose(str);
                }
                Iterator<String> it = setDecompose.iterator();
                while (it.hasNext()) {
                    if (str2.equalsIgnoreCase(it.next())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
