package org.icepdf.core.pobjects;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/OptionalContentMembership.class */
public class OptionalContentMembership extends Dictionary implements OptionalContents {
    public static final Name TYPE = new Name("OCMD");
    public static final Name OCGs_KEY = new Name("OCGs");
    public static final Name P_KEY = new Name(Constants._TAG_P);
    public static final Name VE_KEY = new Name("VE");
    public static final Name ALL_ON_KEY = new Name("AllOn");
    public static final Name ALL_OFF_KEY = new Name("AllOff");
    public static final Name ANY_ON_KEY = new Name("AnyOn");
    public static final Name ANY_OFF_KEY = new Name("AnyOff");
    private VisibilityPolicy policy;
    private List<OptionalContentGroup> ocgs;
    private List visibilityExpression;

    public OptionalContentMembership(Library library, HashMap entries) {
        super(library, entries);
        this.ocgs = new ArrayList();
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public void init() {
        if (this.inited) {
            return;
        }
        Object ocgObj = this.library.getObject(this.entries, OCGs_KEY);
        if (ocgObj instanceof OptionalContentGroup) {
            this.ocgs.add((OptionalContentGroup) ocgObj);
        } else if (ocgObj instanceof List) {
            List ocgList = (List) ocgObj;
            for (Object object : ocgList) {
                Object ocg = this.library.getObject(object);
                if (ocg instanceof OptionalContentGroup) {
                    this.ocgs.add((OptionalContentGroup) ocg);
                }
            }
        }
        this.policy = VisibilityPolicy.getPolicy(this.library.getName(this.entries, P_KEY));
        this.inited = true;
    }

    @Override // org.icepdf.core.pobjects.OptionalContents
    public boolean isOCG() {
        return true;
    }

    public VisibilityPolicy getPolicy() {
        return this.policy;
    }

    public List<OptionalContentGroup> getOcgs() {
        return this.ocgs;
    }

    @Override // org.icepdf.core.pobjects.OptionalContents
    public boolean isVisible() {
        return this.policy.isVisible(this.ocgs);
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/OptionalContentMembership$VisibilityPolicy.class */
    public enum VisibilityPolicy {
        ALL_ON { // from class: org.icepdf.core.pobjects.OptionalContentMembership.VisibilityPolicy.1
            @Override // org.icepdf.core.pobjects.OptionalContentMembership.VisibilityPolicy
            boolean isVisible(List<OptionalContentGroup> ocgs) {
                for (OptionalContentGroup ocg : ocgs) {
                    if (!ocg.isVisible()) {
                        return false;
                    }
                }
                return true;
            }
        },
        ANY_ON { // from class: org.icepdf.core.pobjects.OptionalContentMembership.VisibilityPolicy.2
            @Override // org.icepdf.core.pobjects.OptionalContentMembership.VisibilityPolicy
            boolean isVisible(List<OptionalContentGroup> ocgs) {
                for (OptionalContentGroup ocg : ocgs) {
                    if (ocg.isVisible()) {
                        return true;
                    }
                }
                return false;
            }
        },
        ANY_OFF { // from class: org.icepdf.core.pobjects.OptionalContentMembership.VisibilityPolicy.3
            @Override // org.icepdf.core.pobjects.OptionalContentMembership.VisibilityPolicy
            boolean isVisible(List<OptionalContentGroup> ocgs) {
                for (OptionalContentGroup ocg : ocgs) {
                    if (!ocg.isVisible()) {
                        return true;
                    }
                }
                return false;
            }
        },
        ALL_OFF { // from class: org.icepdf.core.pobjects.OptionalContentMembership.VisibilityPolicy.4
            @Override // org.icepdf.core.pobjects.OptionalContentMembership.VisibilityPolicy
            boolean isVisible(List<OptionalContentGroup> ocgs) {
                for (OptionalContentGroup ocg : ocgs) {
                    if (ocg.isVisible()) {
                        return false;
                    }
                }
                return true;
            }
        };

        abstract boolean isVisible(List<OptionalContentGroup> list);

        public static VisibilityPolicy getPolicy(Name p2) {
            if (OptionalContentMembership.ALL_ON_KEY.equals(p2)) {
                return ALL_ON;
            }
            if (OptionalContentMembership.ALL_OFF_KEY.equals(p2)) {
                return ALL_OFF;
            }
            if (OptionalContentMembership.ANY_ON_KEY.equals(p2)) {
                return ANY_ON;
            }
            if (OptionalContentMembership.ANY_OFF_KEY.equals(p2)) {
                return ANY_OFF;
            }
            if (OptionalContentMembership.ANY_OFF_KEY.equals(p2)) {
                return ALL_OFF;
            }
            return ANY_ON;
        }
    }
}
