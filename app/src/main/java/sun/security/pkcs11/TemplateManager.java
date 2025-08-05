package sun.security.pkcs11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.Functions;
import sun.security.pkcs11.wrapper.PKCS11Constants;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/TemplateManager.class */
final class TemplateManager {
    private static final boolean DEBUG = false;
    static final String O_ANY = "*";
    static final String O_IMPORT = "import";
    static final String O_GENERATE = "generate";
    private final List<KeyAndTemplate> primitiveTemplates = new ArrayList();
    private final Map<TemplateKey, Template> compositeTemplates = new ConcurrentHashMap();

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/TemplateManager$KeyAndTemplate.class */
    private static class KeyAndTemplate {
        final TemplateKey key;
        final Template template;

        KeyAndTemplate(TemplateKey templateKey, Template template) {
            this.key = templateKey;
            this.template = template;
        }
    }

    TemplateManager() {
    }

    void addTemplate(String str, long j2, long j3, CK_ATTRIBUTE[] ck_attributeArr) {
        this.primitiveTemplates.add(new KeyAndTemplate(new TemplateKey(str, j2, j3), new Template(ck_attributeArr)));
    }

    private Template getTemplate(TemplateKey templateKey) {
        Template templateBuildCompositeTemplate = this.compositeTemplates.get(templateKey);
        if (templateBuildCompositeTemplate == null) {
            templateBuildCompositeTemplate = buildCompositeTemplate(templateKey);
            this.compositeTemplates.put(templateKey, templateBuildCompositeTemplate);
        }
        return templateBuildCompositeTemplate;
    }

    CK_ATTRIBUTE[] getAttributes(String str, long j2, long j3, CK_ATTRIBUTE[] ck_attributeArr) {
        return getTemplate(new TemplateKey(str, j2, j3)).getAttributes(ck_attributeArr);
    }

    private Template buildCompositeTemplate(TemplateKey templateKey) {
        Template template = new Template();
        for (KeyAndTemplate keyAndTemplate : this.primitiveTemplates) {
            if (keyAndTemplate.key.appliesTo(templateKey)) {
                template.add(keyAndTemplate.template);
            }
        }
        return template;
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/TemplateManager$TemplateKey.class */
    private static final class TemplateKey {
        final String operation;
        final long keyType;
        final long keyAlgorithm;

        TemplateKey(String str, long j2, long j3) {
            this.operation = str;
            this.keyType = j2;
            this.keyAlgorithm = j3;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TemplateKey)) {
                return false;
            }
            TemplateKey templateKey = (TemplateKey) obj;
            return this.operation.equals(templateKey.operation) && this.keyType == templateKey.keyType && this.keyAlgorithm == templateKey.keyAlgorithm;
        }

        public int hashCode() {
            return this.operation.hashCode() + ((int) this.keyType) + ((int) this.keyAlgorithm);
        }

        boolean appliesTo(TemplateKey templateKey) {
            if (!this.operation.equals("*") && !this.operation.equals(templateKey.operation)) {
                return false;
            }
            if (this.keyType != 2147483427 && this.keyType != templateKey.keyType) {
                return false;
            }
            if (this.keyAlgorithm == PKCS11Constants.PCKK_ANY || this.keyAlgorithm == templateKey.keyAlgorithm) {
                return true;
            }
            return false;
        }

        public String toString() {
            return "(" + this.operation + "," + Functions.getObjectClassName(this.keyType) + "," + Functions.getKeyName(this.keyAlgorithm) + ")";
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/TemplateManager$Template.class */
    private static final class Template {
        private static final CK_ATTRIBUTE[] A0 = new CK_ATTRIBUTE[0];
        private CK_ATTRIBUTE[] attributes;

        Template() {
            this.attributes = A0;
        }

        Template(CK_ATTRIBUTE[] ck_attributeArr) {
            this.attributes = ck_attributeArr;
        }

        void add(Template template) {
            this.attributes = getAttributes(template.attributes);
        }

        CK_ATTRIBUTE[] getAttributes(CK_ATTRIBUTE[] ck_attributeArr) {
            return combine(this.attributes, ck_attributeArr);
        }

        private static CK_ATTRIBUTE[] combine(CK_ATTRIBUTE[] ck_attributeArr, CK_ATTRIBUTE[] ck_attributeArr2) {
            ArrayList arrayList = new ArrayList();
            for (CK_ATTRIBUTE ck_attribute : ck_attributeArr) {
                if (ck_attribute.pValue != null) {
                    arrayList.add(ck_attribute);
                }
            }
            for (CK_ATTRIBUTE ck_attribute2 : ck_attributeArr2) {
                long j2 = ck_attribute2.type;
                for (CK_ATTRIBUTE ck_attribute3 : ck_attributeArr) {
                    if (ck_attribute3.type == j2) {
                        arrayList.remove(ck_attribute3);
                    }
                }
                if (ck_attribute2.pValue != null) {
                    arrayList.add(ck_attribute2);
                }
            }
            return (CK_ATTRIBUTE[]) arrayList.toArray(A0);
        }

        public String toString() {
            return Arrays.asList(this.attributes).toString();
        }
    }
}
