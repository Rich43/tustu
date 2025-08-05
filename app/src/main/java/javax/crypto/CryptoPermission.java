package javax.crypto;

import java.security.Permission;
import java.security.PermissionCollection;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;

/* loaded from: jce.jar:javax/crypto/CryptoPermission.class */
class CryptoPermission extends Permission {
    private static final long serialVersionUID = 8987399626114087514L;
    private String alg;
    private int maxKeySize;
    private String exemptionMechanism;
    private AlgorithmParameterSpec algParamSpec;
    private boolean checkParam;
    static final String ALG_NAME_WILDCARD = "*";

    CryptoPermission(String str) {
        super(null);
        this.maxKeySize = Integer.MAX_VALUE;
        this.exemptionMechanism = null;
        this.algParamSpec = null;
        this.checkParam = false;
        this.alg = str;
    }

    CryptoPermission(String str, int i2) {
        super(null);
        this.maxKeySize = Integer.MAX_VALUE;
        this.exemptionMechanism = null;
        this.algParamSpec = null;
        this.checkParam = false;
        this.alg = str;
        this.maxKeySize = i2;
    }

    CryptoPermission(String str, int i2, AlgorithmParameterSpec algorithmParameterSpec) {
        super(null);
        this.maxKeySize = Integer.MAX_VALUE;
        this.exemptionMechanism = null;
        this.algParamSpec = null;
        this.checkParam = false;
        this.alg = str;
        this.maxKeySize = i2;
        this.checkParam = true;
        this.algParamSpec = algorithmParameterSpec;
    }

    CryptoPermission(String str, String str2) {
        super(null);
        this.maxKeySize = Integer.MAX_VALUE;
        this.exemptionMechanism = null;
        this.algParamSpec = null;
        this.checkParam = false;
        this.alg = str;
        this.exemptionMechanism = str2;
    }

    CryptoPermission(String str, int i2, String str2) {
        super(null);
        this.maxKeySize = Integer.MAX_VALUE;
        this.exemptionMechanism = null;
        this.algParamSpec = null;
        this.checkParam = false;
        this.alg = str;
        this.exemptionMechanism = str2;
        this.maxKeySize = i2;
    }

    CryptoPermission(String str, int i2, AlgorithmParameterSpec algorithmParameterSpec, String str2) {
        super(null);
        this.maxKeySize = Integer.MAX_VALUE;
        this.exemptionMechanism = null;
        this.algParamSpec = null;
        this.checkParam = false;
        this.alg = str;
        this.exemptionMechanism = str2;
        this.maxKeySize = i2;
        this.checkParam = true;
        this.algParamSpec = algorithmParameterSpec;
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof CryptoPermission)) {
            return false;
        }
        CryptoPermission cryptoPermission = (CryptoPermission) permission;
        if ((this.alg.equalsIgnoreCase(cryptoPermission.alg) || this.alg.equalsIgnoreCase("*")) && cryptoPermission.maxKeySize <= this.maxKeySize && impliesParameterSpec(cryptoPermission.checkParam, cryptoPermission.algParamSpec) && impliesExemptionMechanism(cryptoPermission.exemptionMechanism)) {
            return true;
        }
        return false;
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CryptoPermission)) {
            return false;
        }
        CryptoPermission cryptoPermission = (CryptoPermission) obj;
        return this.alg.equalsIgnoreCase(cryptoPermission.alg) && this.maxKeySize == cryptoPermission.maxKeySize && this.checkParam == cryptoPermission.checkParam && equalObjects(this.exemptionMechanism, cryptoPermission.exemptionMechanism) && equalObjects(this.algParamSpec, cryptoPermission.algParamSpec);
    }

    @Override // java.security.Permission
    public int hashCode() {
        int iHashCode = this.alg.hashCode() ^ this.maxKeySize;
        if (this.exemptionMechanism != null) {
            iHashCode ^= this.exemptionMechanism.hashCode();
        }
        if (this.checkParam) {
            iHashCode ^= 100;
        }
        if (this.algParamSpec != null) {
            iHashCode ^= this.algParamSpec.hashCode();
        }
        return iHashCode;
    }

    @Override // java.security.Permission
    public String getActions() {
        return null;
    }

    @Override // java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new CryptoPermissionCollection();
    }

    final String getAlgorithm() {
        return this.alg;
    }

    final String getExemptionMechanism() {
        return this.exemptionMechanism;
    }

    final int getMaxKeySize() {
        return this.maxKeySize;
    }

    final boolean getCheckParam() {
        return this.checkParam;
    }

    final AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return this.algParamSpec;
    }

    @Override // java.security.Permission
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("(CryptoPermission " + this.alg + " " + this.maxKeySize);
        if (this.algParamSpec != null) {
            if (this.algParamSpec instanceof RC2ParameterSpec) {
                sb.append(" , effective " + ((RC2ParameterSpec) this.algParamSpec).getEffectiveKeyBits());
            } else if (this.algParamSpec instanceof RC5ParameterSpec) {
                sb.append(" , rounds " + ((RC5ParameterSpec) this.algParamSpec).getRounds());
            }
        }
        if (this.exemptionMechanism != null) {
            sb.append(" " + this.exemptionMechanism);
        }
        sb.append(")");
        return sb.toString();
    }

    private boolean impliesExemptionMechanism(String str) {
        if (this.exemptionMechanism == null) {
            return true;
        }
        if (str != null && this.exemptionMechanism.equals(str)) {
            return true;
        }
        return false;
    }

    private boolean impliesParameterSpec(boolean z2, AlgorithmParameterSpec algorithmParameterSpec) {
        if (this.checkParam && z2) {
            if (algorithmParameterSpec == null) {
                return true;
            }
            if (this.algParamSpec == null || this.algParamSpec.getClass() != algorithmParameterSpec.getClass()) {
                return false;
            }
            if ((algorithmParameterSpec instanceof RC2ParameterSpec) && ((RC2ParameterSpec) algorithmParameterSpec).getEffectiveKeyBits() <= ((RC2ParameterSpec) this.algParamSpec).getEffectiveKeyBits()) {
                return true;
            }
            if ((algorithmParameterSpec instanceof RC5ParameterSpec) && ((RC5ParameterSpec) algorithmParameterSpec).getRounds() <= ((RC5ParameterSpec) this.algParamSpec).getRounds()) {
                return true;
            }
            if (((algorithmParameterSpec instanceof PBEParameterSpec) && ((PBEParameterSpec) algorithmParameterSpec).getIterationCount() <= ((PBEParameterSpec) this.algParamSpec).getIterationCount()) || this.algParamSpec.equals(algorithmParameterSpec)) {
                return true;
            }
            return false;
        }
        if (this.checkParam) {
            return false;
        }
        return true;
    }

    private boolean equalObjects(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }
}
