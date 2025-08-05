package sun.security.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.AlgorithmParameters;
import java.security.CryptoPrimitive;
import java.security.Key;
import java.security.cert.CertPathValidatorException;
import java.security.interfaces.ECKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.icepdf.core.util.PdfOps;
import sun.security.validator.Validator;

/* loaded from: rt.jar:sun/security/util/DisabledAlgorithmConstraints.class */
public class DisabledAlgorithmConstraints extends AbstractAlgorithmConstraints {
    private static final Debug debug = Debug.getInstance("certpath");
    public static final String PROPERTY_CERTPATH_DISABLED_ALGS = "jdk.certpath.disabledAlgorithms";
    public static final String PROPERTY_SECURITY_LEGACY_ALGS = "jdk.security.legacyAlgorithms";
    public static final String PROPERTY_TLS_DISABLED_ALGS = "jdk.tls.disabledAlgorithms";
    public static final String PROPERTY_JAR_DISABLED_ALGS = "jdk.jar.disabledAlgorithms";
    private static final String PROPERTY_DISABLED_EC_CURVES = "jdk.disabled.namedCurves";
    private final List<String> disabledAlgorithms;
    private final Constraints algorithmConstraints;

    /* loaded from: rt.jar:sun/security/util/DisabledAlgorithmConstraints$CertPathHolder.class */
    private static class CertPathHolder {
        static final DisabledAlgorithmConstraints CONSTRAINTS = new DisabledAlgorithmConstraints(DisabledAlgorithmConstraints.PROPERTY_CERTPATH_DISABLED_ALGS);

        private CertPathHolder() {
        }
    }

    /* loaded from: rt.jar:sun/security/util/DisabledAlgorithmConstraints$JarHolder.class */
    private static class JarHolder {
        static final DisabledAlgorithmConstraints CONSTRAINTS = new DisabledAlgorithmConstraints(DisabledAlgorithmConstraints.PROPERTY_JAR_DISABLED_ALGS);

        private JarHolder() {
        }
    }

    public static DisabledAlgorithmConstraints certPathConstraints() {
        return CertPathHolder.CONSTRAINTS;
    }

    public static DisabledAlgorithmConstraints jarConstraints() {
        return JarHolder.CONSTRAINTS;
    }

    public DisabledAlgorithmConstraints(String str) {
        this(str, new AlgorithmDecomposer());
    }

    public DisabledAlgorithmConstraints(String str, AlgorithmDecomposer algorithmDecomposer) {
        super(algorithmDecomposer);
        this.disabledAlgorithms = getAlgorithms(str);
        int i2 = -1;
        int i3 = 0;
        Iterator<String> it = this.disabledAlgorithms.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String next = it.next();
            if (next.regionMatches(true, 0, "include ", 0, 8) && next.regionMatches(true, 8, PROPERTY_DISABLED_EC_CURVES, 0, PROPERTY_DISABLED_EC_CURVES.length())) {
                i2 = i3;
                break;
            }
            i3++;
        }
        if (i2 > -1) {
            this.disabledAlgorithms.remove(i2);
            this.disabledAlgorithms.addAll(i2, getAlgorithms(PROPERTY_DISABLED_EC_CURVES));
        }
        this.algorithmConstraints = new Constraints(str, this.disabledAlgorithms);
    }

    @Override // java.security.AlgorithmConstraints
    public final boolean permits(Set<CryptoPrimitive> set, String str, AlgorithmParameters algorithmParameters) {
        if (!checkAlgorithm(this.disabledAlgorithms, str, this.decomposer)) {
            return false;
        }
        if (algorithmParameters != null) {
            return this.algorithmConstraints.permits(str, algorithmParameters);
        }
        return true;
    }

    @Override // java.security.AlgorithmConstraints
    public final boolean permits(Set<CryptoPrimitive> set, Key key) {
        return checkConstraints(set, "", key, null);
    }

    @Override // java.security.AlgorithmConstraints
    public final boolean permits(Set<CryptoPrimitive> set, String str, Key key, AlgorithmParameters algorithmParameters) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("No algorithm name specified");
        }
        return checkConstraints(set, str, key, algorithmParameters);
    }

    public final void permits(String str, AlgorithmParameters algorithmParameters, ConstraintsParameters constraintsParameters, boolean z2) throws CertPathValidatorException {
        permits(str, constraintsParameters, z2);
        if (algorithmParameters != null) {
            permits(algorithmParameters, constraintsParameters);
        }
    }

    private void permits(AlgorithmParameters algorithmParameters, ConstraintsParameters constraintsParameters) throws CertPathValidatorException {
        switch (algorithmParameters.getAlgorithm().toUpperCase(Locale.ENGLISH)) {
            case "RSASSA-PSS":
                permitsPSSParams(algorithmParameters, constraintsParameters);
                break;
        }
    }

    private void permitsPSSParams(AlgorithmParameters algorithmParameters, ConstraintsParameters constraintsParameters) throws CertPathValidatorException {
        try {
            PSSParameterSpec pSSParameterSpec = (PSSParameterSpec) algorithmParameters.getParameterSpec(PSSParameterSpec.class);
            String digestAlgorithm = pSSParameterSpec.getDigestAlgorithm();
            permits(digestAlgorithm, constraintsParameters, false);
            AlgorithmParameterSpec mGFParameters = pSSParameterSpec.getMGFParameters();
            if (mGFParameters instanceof MGF1ParameterSpec) {
                String digestAlgorithm2 = ((MGF1ParameterSpec) mGFParameters).getDigestAlgorithm();
                if (!digestAlgorithm2.equalsIgnoreCase(digestAlgorithm)) {
                    permits(digestAlgorithm2, constraintsParameters, false);
                }
            }
        } catch (InvalidParameterSpecException e2) {
        }
    }

    public final void permits(String str, ConstraintsParameters constraintsParameters, boolean z2) throws CertPathValidatorException {
        if (z2) {
            Iterator<Key> it = constraintsParameters.getKeys().iterator();
            while (it.hasNext()) {
                for (String str2 : getNamedCurveFromKey(it.next())) {
                    if (!checkAlgorithm(this.disabledAlgorithms, str2, this.decomposer)) {
                        throw new CertPathValidatorException("Algorithm constraints check failed on disabled algorithm: " + str2, null, null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
                    }
                }
            }
        }
        this.algorithmConstraints.permits(str, constraintsParameters, z2);
    }

    private static List<String> getNamedCurveFromKey(Key key) {
        if (key instanceof ECKey) {
            NamedCurve namedCurveLookup = CurveDB.lookup(((ECKey) key).getParams());
            return namedCurveLookup == null ? Collections.emptyList() : Arrays.asList(CurveDB.getNamesByOID(namedCurveLookup.getObjectId()));
        }
        return Collections.emptyList();
    }

    private boolean checkConstraints(Set<CryptoPrimitive> set, String str, Key key, AlgorithmParameters algorithmParameters) {
        if (key == null) {
            throw new IllegalArgumentException("The key cannot be null");
        }
        if ((str != null && str.length() != 0 && !permits(set, str, algorithmParameters)) || !permits(set, key.getAlgorithm(), (AlgorithmParameters) null)) {
            return false;
        }
        Iterator<String> it = getNamedCurveFromKey(key).iterator();
        while (it.hasNext()) {
            if (!permits(set, it.next(), (AlgorithmParameters) null)) {
                return false;
            }
        }
        return this.algorithmConstraints.permits(key);
    }

    /* loaded from: rt.jar:sun/security/util/DisabledAlgorithmConstraints$Constraints.class */
    private static class Constraints {
        private Map<String, List<Constraint>> constraintsMap = new HashMap();

        /* loaded from: rt.jar:sun/security/util/DisabledAlgorithmConstraints$Constraints$Holder.class */
        private static class Holder {
            private static final Pattern DENY_AFTER_PATTERN = Pattern.compile("denyAfter\\s+(\\d{4})-(\\d{2})-(\\d{2})");

            private Holder() {
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:61:0x027b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public Constraints(java.lang.String r8, java.util.List<java.lang.String> r9) {
            /*
                Method dump skipped, instructions count: 777
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.security.util.DisabledAlgorithmConstraints.Constraints.<init>(java.lang.String, java.util.List):void");
        }

        private List<Constraint> getConstraints(String str) {
            return this.constraintsMap.get(str.toUpperCase(Locale.ENGLISH));
        }

        public boolean permits(Key key) {
            List<Constraint> constraints = getConstraints(key.getAlgorithm());
            if (constraints == null) {
                return true;
            }
            Iterator<Constraint> it = constraints.iterator();
            while (it.hasNext()) {
                if (!it.next().permits(key)) {
                    if (DisabledAlgorithmConstraints.debug != null) {
                        DisabledAlgorithmConstraints.debug.println("Constraints: failed key sizeconstraint check " + KeyUtil.getKeySize(key));
                        return false;
                    }
                    return false;
                }
            }
            return true;
        }

        public boolean permits(String str, AlgorithmParameters algorithmParameters) {
            List<Constraint> constraints = getConstraints(str);
            if (constraints == null) {
                return true;
            }
            Iterator<Constraint> it = constraints.iterator();
            while (it.hasNext()) {
                if (!it.next().permits(algorithmParameters)) {
                    if (DisabledAlgorithmConstraints.debug != null) {
                        DisabledAlgorithmConstraints.debug.println("Constraints: failed algorithm parameters constraint check " + ((Object) algorithmParameters));
                        return false;
                    }
                    return false;
                }
            }
            return true;
        }

        public void permits(String str, ConstraintsParameters constraintsParameters, boolean z2) throws CertPathValidatorException {
            if (DisabledAlgorithmConstraints.debug != null) {
                DisabledAlgorithmConstraints.debug.println("Constraints.permits(): " + str + ", " + constraintsParameters.toString());
            }
            HashSet hashSet = new HashSet();
            if (str != null) {
                hashSet.addAll(AlgorithmDecomposer.decomposeOneHash(str));
                hashSet.add(str);
            }
            if (z2) {
                Iterator<Key> it = constraintsParameters.getKeys().iterator();
                while (it.hasNext()) {
                    hashSet.add(it.next().getAlgorithm());
                }
            }
            Iterator<E> it2 = hashSet.iterator();
            while (it2.hasNext()) {
                List<Constraint> constraints = getConstraints((String) it2.next());
                if (constraints != null) {
                    for (Constraint constraint : constraints) {
                        if (z2 || !(constraint instanceof KeySizeConstraint)) {
                            constraint.permits(constraintsParameters);
                        }
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:sun/security/util/DisabledAlgorithmConstraints$Constraint.class */
    private static abstract class Constraint {
        String algorithm;
        Constraint nextConstraint;

        public abstract void permits(ConstraintsParameters constraintsParameters) throws CertPathValidatorException;

        private Constraint() {
            this.nextConstraint = null;
        }

        /* loaded from: rt.jar:sun/security/util/DisabledAlgorithmConstraints$Constraint$Operator.class */
        enum Operator {
            EQ,
            NE,
            LT,
            LE,
            GT,
            GE;

            static Operator of(String str) {
                switch (str) {
                    case "==":
                        return EQ;
                    case "!=":
                        return NE;
                    case "<":
                        return LT;
                    case "<=":
                        return LE;
                    case ">":
                        return GT;
                    case ">=":
                        return GE;
                    default:
                        throw new IllegalArgumentException("Error in security property. " + str + " is not a legal Operator");
                }
            }
        }

        public boolean permits(Key key) {
            return true;
        }

        public boolean permits(AlgorithmParameters algorithmParameters) {
            return true;
        }

        boolean next(ConstraintsParameters constraintsParameters) throws CertPathValidatorException {
            if (this.nextConstraint != null) {
                this.nextConstraint.permits(constraintsParameters);
                return true;
            }
            return false;
        }

        boolean next(Key key) {
            return this.nextConstraint != null && this.nextConstraint.permits(key);
        }
    }

    /* loaded from: rt.jar:sun/security/util/DisabledAlgorithmConstraints$jdkCAConstraint.class */
    private static class jdkCAConstraint extends Constraint {
        jdkCAConstraint(String str) {
            super();
            this.algorithm = str;
        }

        @Override // sun.security.util.DisabledAlgorithmConstraints.Constraint
        public void permits(ConstraintsParameters constraintsParameters) throws CertPathValidatorException {
            if (DisabledAlgorithmConstraints.debug != null) {
                DisabledAlgorithmConstraints.debug.println("jdkCAConstraints.permits(): " + this.algorithm);
            }
            if (!constraintsParameters.anchorIsJdkCA() || next(constraintsParameters)) {
            } else {
                throw new CertPathValidatorException("Algorithm constraints check failed on certificate anchor limits. " + this.algorithm + constraintsParameters.extendedExceptionMsg(), null, null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
            }
        }
    }

    /* loaded from: rt.jar:sun/security/util/DisabledAlgorithmConstraints$DenyAfterConstraint.class */
    private static class DenyAfterConstraint extends Constraint {
        private ZonedDateTime zdt;
        private Instant denyAfterDate;

        DenyAfterConstraint(String str, int i2, int i3, int i4) {
            super();
            this.algorithm = str;
            if (DisabledAlgorithmConstraints.debug != null) {
                DisabledAlgorithmConstraints.debug.println("DenyAfterConstraint read in as: year " + i2 + ", month = " + i3 + ", day = " + i4);
            }
            try {
                this.zdt = ZonedDateTime.of(i2, i3, i4, 0, 0, 0, 0, ZoneId.of("GMT"));
                this.denyAfterDate = this.zdt.toInstant();
                if (DisabledAlgorithmConstraints.debug != null) {
                    DisabledAlgorithmConstraints.debug.println("DenyAfterConstraint date set to: " + ((Object) this.zdt.toLocalDate()));
                }
            } catch (DateTimeException e2) {
                throw new IllegalArgumentException("Invalid denyAfter date", e2);
            }
        }

        @Override // sun.security.util.DisabledAlgorithmConstraints.Constraint
        public void permits(ConstraintsParameters constraintsParameters) throws CertPathValidatorException {
            Instant instantNow;
            if (constraintsParameters.getDate() != null) {
                instantNow = constraintsParameters.getDate().toInstant();
            } else {
                instantNow = Instant.now();
            }
            if (this.denyAfterDate.isAfter(instantNow) || next(constraintsParameters)) {
            } else {
                throw new CertPathValidatorException("denyAfter constraint check failed: " + this.algorithm + " used with Constraint date: " + ((Object) this.zdt.toLocalDate()) + "; params date: " + ((Object) instantNow) + constraintsParameters.extendedExceptionMsg(), null, null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
            }
        }

        @Override // sun.security.util.DisabledAlgorithmConstraints.Constraint
        public boolean permits(Key key) {
            if (!next(key)) {
                if (DisabledAlgorithmConstraints.debug != null) {
                    DisabledAlgorithmConstraints.debug.println("DenyAfterConstraints.permits(): " + this.algorithm);
                }
                return this.denyAfterDate.isAfter(Instant.now());
            }
            return true;
        }
    }

    /* loaded from: rt.jar:sun/security/util/DisabledAlgorithmConstraints$UsageConstraint.class */
    private static class UsageConstraint extends Constraint {
        String[] usages;

        UsageConstraint(String str, String[] strArr) {
            super();
            this.algorithm = str;
            this.usages = strArr;
        }

        @Override // sun.security.util.DisabledAlgorithmConstraints.Constraint
        public void permits(ConstraintsParameters constraintsParameters) throws CertPathValidatorException {
            boolean zEquals;
            String variant = constraintsParameters.getVariant();
            for (String str : this.usages) {
                zEquals = false;
                switch (str.toLowerCase()) {
                    case "tlsserver":
                        zEquals = variant.equals(Validator.VAR_TLS_SERVER);
                        break;
                    case "tlsclient":
                        zEquals = variant.equals(Validator.VAR_TLS_CLIENT);
                        break;
                    case "signedjar":
                        zEquals = variant.equals(Validator.VAR_PLUGIN_CODE_SIGNING) || variant.equals(Validator.VAR_CODE_SIGNING) || variant.equals(Validator.VAR_TSA_SERVER);
                        break;
                }
                if (DisabledAlgorithmConstraints.debug != null) {
                    DisabledAlgorithmConstraints.debug.println("Checking if usage constraint \"" + str + "\" matches \"" + constraintsParameters.getVariant() + PdfOps.DOUBLE_QUOTE__TOKEN);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    new Exception().printStackTrace(new PrintStream(byteArrayOutputStream));
                    DisabledAlgorithmConstraints.debug.println(byteArrayOutputStream.toString());
                }
                if (zEquals) {
                    if (next(constraintsParameters)) {
                        return;
                    } else {
                        throw new CertPathValidatorException("Usage constraint " + str + " check failed: " + this.algorithm + constraintsParameters.extendedExceptionMsg(), null, null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:sun/security/util/DisabledAlgorithmConstraints$KeySizeConstraint.class */
    private static class KeySizeConstraint extends Constraint {
        private int minSize;
        private int maxSize;
        private int prohibitedSize;
        private int size;

        public KeySizeConstraint(String str, Constraint.Operator operator, int i2) {
            super();
            this.prohibitedSize = -1;
            this.algorithm = str;
            switch (operator) {
                case EQ:
                    this.minSize = 0;
                    this.maxSize = Integer.MAX_VALUE;
                    this.prohibitedSize = i2;
                    break;
                case NE:
                    this.minSize = i2;
                    this.maxSize = i2;
                    break;
                case LT:
                    this.minSize = i2;
                    this.maxSize = Integer.MAX_VALUE;
                    break;
                case LE:
                    this.minSize = i2 + 1;
                    this.maxSize = Integer.MAX_VALUE;
                    break;
                case GT:
                    this.minSize = 0;
                    this.maxSize = i2;
                    break;
                case GE:
                    this.minSize = 0;
                    this.maxSize = i2 > 1 ? i2 - 1 : 0;
                    break;
                default:
                    this.minSize = Integer.MAX_VALUE;
                    this.maxSize = -1;
                    break;
            }
        }

        @Override // sun.security.util.DisabledAlgorithmConstraints.Constraint
        public void permits(ConstraintsParameters constraintsParameters) throws CertPathValidatorException {
            for (Key key : constraintsParameters.getKeys()) {
                if (!permitsImpl(key)) {
                    if (this.nextConstraint != null) {
                        this.nextConstraint.permits(constraintsParameters);
                    } else {
                        throw new CertPathValidatorException("Algorithm constraints check failed on keysize limits: " + this.algorithm + " " + KeyUtil.getKeySize(key) + " bit key" + constraintsParameters.extendedExceptionMsg(), null, null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
                    }
                }
            }
        }

        @Override // sun.security.util.DisabledAlgorithmConstraints.Constraint
        public boolean permits(Key key) {
            if (this.nextConstraint == null || !this.nextConstraint.permits(key)) {
                if (DisabledAlgorithmConstraints.debug != null) {
                    DisabledAlgorithmConstraints.debug.println("KeySizeConstraints.permits(): " + this.algorithm);
                }
                return permitsImpl(key);
            }
            return true;
        }

        @Override // sun.security.util.DisabledAlgorithmConstraints.Constraint
        public boolean permits(AlgorithmParameters algorithmParameters) {
            String algorithm = algorithmParameters.getAlgorithm();
            if (!this.algorithm.equalsIgnoreCase(algorithmParameters.getAlgorithm()) && !AlgorithmDecomposer.getAliases(this.algorithm).contains(algorithm)) {
                return true;
            }
            int keySize = KeyUtil.getKeySize(algorithmParameters);
            if (keySize == 0) {
                return false;
            }
            if (keySize > 0) {
                return keySize >= this.minSize && keySize <= this.maxSize && this.prohibitedSize != keySize;
            }
            return true;
        }

        private boolean permitsImpl(Key key) {
            if (this.algorithm.compareToIgnoreCase(key.getAlgorithm()) != 0) {
                return true;
            }
            this.size = KeyUtil.getKeySize(key);
            if (this.size == 0) {
                return false;
            }
            if (this.size > 0) {
                return this.size >= this.minSize && this.size <= this.maxSize && this.prohibitedSize != this.size;
            }
            return true;
        }
    }

    /* loaded from: rt.jar:sun/security/util/DisabledAlgorithmConstraints$DisabledConstraint.class */
    private static class DisabledConstraint extends Constraint {
        DisabledConstraint(String str) {
            super();
            this.algorithm = str;
        }

        @Override // sun.security.util.DisabledAlgorithmConstraints.Constraint
        public void permits(ConstraintsParameters constraintsParameters) throws CertPathValidatorException {
            throw new CertPathValidatorException("Algorithm constraints check failed on disabled algorithm: " + this.algorithm + constraintsParameters.extendedExceptionMsg(), null, null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
        }

        @Override // sun.security.util.DisabledAlgorithmConstraints.Constraint
        public boolean permits(Key key) {
            return false;
        }
    }
}
