package com.sun.xml.internal.bind.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.lang.model.SourceVersion;

/* loaded from: rt.jar:com/sun/xml/internal/bind/api/impl/NameConverter.class */
public interface NameConverter {
    public static final NameConverter standard = new Standard();
    public static final NameConverter jaxrpcCompatible = new Standard() { // from class: com.sun.xml.internal.bind.api.impl.NameConverter.1
        @Override // com.sun.xml.internal.bind.api.impl.NameUtil
        protected boolean isPunct(char c2) {
            return c2 == '.' || c2 == '-' || c2 == ';' || c2 == 183 || c2 == 903 || c2 == 1757 || c2 == 1758;
        }

        @Override // com.sun.xml.internal.bind.api.impl.NameUtil
        protected boolean isLetter(char c2) {
            return super.isLetter(c2) || c2 == '_';
        }

        @Override // com.sun.xml.internal.bind.api.impl.NameUtil
        protected int classify(char c0) {
            if (c0 == '_') {
                return 2;
            }
            return super.classify(c0);
        }
    };
    public static final NameConverter smart = new Standard() { // from class: com.sun.xml.internal.bind.api.impl.NameConverter.2
        @Override // com.sun.xml.internal.bind.api.impl.NameConverter.Standard, com.sun.xml.internal.bind.api.impl.NameUtil, com.sun.xml.internal.bind.api.impl.NameConverter
        public String toConstantName(String token) {
            String name = super.toConstantName(token);
            if (!SourceVersion.isKeyword(name)) {
                return name;
            }
            return '_' + name;
        }
    };

    String toClassName(String str);

    String toInterfaceName(String str);

    String toPropertyName(String str);

    String toConstantName(String str);

    String toVariableName(String str);

    String toPackageName(String str);

    /* loaded from: rt.jar:com/sun/xml/internal/bind/api/impl/NameConverter$Standard.class */
    public static class Standard extends NameUtil implements NameConverter {
        @Override // com.sun.xml.internal.bind.api.impl.NameUtil
        public /* bridge */ /* synthetic */ String toConstantName(List list) {
            return super.toConstantName((List<String>) list);
        }

        @Override // com.sun.xml.internal.bind.api.impl.NameUtil
        public /* bridge */ /* synthetic */ List toWordList(String str) {
            return super.toWordList(str);
        }

        @Override // com.sun.xml.internal.bind.api.impl.NameUtil
        public /* bridge */ /* synthetic */ String capitalize(String str) {
            return super.capitalize(str);
        }

        @Override // com.sun.xml.internal.bind.api.impl.NameConverter
        public String toClassName(String s2) {
            return toMixedCaseName(toWordList(s2), true);
        }

        @Override // com.sun.xml.internal.bind.api.impl.NameConverter
        public String toVariableName(String s2) {
            return toMixedCaseName(toWordList(s2), false);
        }

        @Override // com.sun.xml.internal.bind.api.impl.NameConverter
        public String toInterfaceName(String token) {
            return toClassName(token);
        }

        @Override // com.sun.xml.internal.bind.api.impl.NameConverter
        public String toPropertyName(String s2) {
            String prop = toClassName(s2);
            if (prop.equals("Class")) {
                prop = "Clazz";
            }
            return prop;
        }

        @Override // com.sun.xml.internal.bind.api.impl.NameUtil, com.sun.xml.internal.bind.api.impl.NameConverter
        public String toConstantName(String token) {
            return super.toConstantName(token);
        }

        @Override // com.sun.xml.internal.bind.api.impl.NameConverter
        public String toPackageName(String nsUri) {
            String lastToken;
            int idx;
            int idx2 = nsUri.indexOf(58);
            String scheme = "";
            if (idx2 >= 0) {
                scheme = nsUri.substring(0, idx2);
                if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("urn")) {
                    nsUri = nsUri.substring(idx2 + 1);
                }
            }
            ArrayList<String> tokens = tokenize(nsUri, "/: ");
            if (tokens.size() == 0) {
                return null;
            }
            if (tokens.size() > 1 && (idx = (lastToken = tokens.get(tokens.size() - 1)).lastIndexOf(46)) > 0) {
                tokens.set(tokens.size() - 1, lastToken.substring(0, idx));
            }
            String domain = tokens.get(0);
            int idx3 = domain.indexOf(58);
            if (idx3 >= 0) {
                domain = domain.substring(0, idx3);
            }
            ArrayList<String> r2 = reverse(tokenize(domain, scheme.equals("urn") ? ".-" : "."));
            if (r2.get(r2.size() - 1).equalsIgnoreCase("www")) {
                r2.remove(r2.size() - 1);
            }
            tokens.addAll(1, r2);
            tokens.remove(0);
            for (int i2 = 0; i2 < tokens.size(); i2++) {
                String token = removeIllegalIdentifierChars(tokens.get(i2));
                if (SourceVersion.isKeyword(token.toLowerCase())) {
                    token = '_' + token;
                }
                tokens.set(i2, token.toLowerCase());
            }
            return combine(tokens, '.');
        }

        private static String removeIllegalIdentifierChars(String token) {
            StringBuilder newToken = new StringBuilder(token.length() + 1);
            for (int i2 = 0; i2 < token.length(); i2++) {
                char c2 = token.charAt(i2);
                if (i2 == 0 && !Character.isJavaIdentifierStart(c2)) {
                    newToken.append('_');
                }
                if (!Character.isJavaIdentifierPart(c2)) {
                    newToken.append('_');
                } else {
                    newToken.append(c2);
                }
            }
            return newToken.toString();
        }

        private static ArrayList<String> tokenize(String str, String sep) {
            StringTokenizer tokens = new StringTokenizer(str, sep);
            ArrayList<String> r2 = new ArrayList<>();
            while (tokens.hasMoreTokens()) {
                r2.add(tokens.nextToken());
            }
            return r2;
        }

        private static <T> ArrayList<T> reverse(List<T> a2) {
            ArrayList<T> r2 = new ArrayList<>();
            for (int i2 = a2.size() - 1; i2 >= 0; i2--) {
                r2.add(a2.get(i2));
            }
            return r2;
        }

        private static String combine(List r2, char sep) {
            StringBuilder buf = new StringBuilder(r2.get(0).toString());
            for (int i2 = 1; i2 < r2.size(); i2++) {
                buf.append(sep);
                buf.append(r2.get(i2));
            }
            return buf.toString();
        }
    }
}
