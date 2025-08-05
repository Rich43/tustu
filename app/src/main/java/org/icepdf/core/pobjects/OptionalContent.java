package org.icepdf.core.pobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/OptionalContent.class */
public class OptionalContent extends Dictionary {
    private Map<Reference, OptionalContentGroup> groups;
    public static final Name OCGs_KEY = new Name("OCGs");
    public static final Name OC_KEY = new Name("OC");
    public static final Name D_KEY = new Name(PdfOps.D_TOKEN);
    public static final Name BASE_STATE_KEY = new Name("BaseState");
    public static final Name INTENT_KEY = new Name("Intent");
    public static final Name AS_KEY = new Name("AS");
    public static final Name ORDER_KEY = new Name("Order");
    public static final Name LIST_MODE_KEY = new Name("ListMode");
    public static final Name RBGROUPS_KEY = new Name("RBGroups");
    public static final Name LOCKED_KEY = new Name("Locked");
    public static final Name OFF_VALUE = new Name("OFF");
    public static final Name ON_vALUE = new Name("ON");
    public static final Name UNCHANGED_KEY = new Name("Unchanged");
    public static final Name VIEW_VALUE = new Name("View");
    public static final Name DESIGN_VALUE = new Name("Design");
    public static final Name NONE_OC_FLAG = new Name("marked");
    private Name baseState;
    private List<Name> intent;
    private List<Object> order;
    private List<Object> rbGroups;

    public OptionalContent(Library l2, HashMap h2) {
        super(l2, h2);
        this.baseState = ON_vALUE;
        this.intent = Arrays.asList(VIEW_VALUE);
        this.groups = new HashMap();
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public void init() {
        List toggle;
        if (this.inited) {
            return;
        }
        Object ogcs = this.library.getObject(this.entries, OCGs_KEY);
        if (ogcs instanceof List) {
            List ogcList = (List) ogcs;
            for (Object object : ogcList) {
                if (object instanceof Reference) {
                    Reference ref = (Reference) object;
                    Object ocgObj = this.library.getObject(ref);
                    if (ocgObj instanceof OptionalContentGroup) {
                        OptionalContentGroup ogc = (OptionalContentGroup) ocgObj;
                        this.groups.put(ref, ogc);
                    }
                }
            }
        }
        Object dObj = this.library.getObject(this.entries, D_KEY);
        if (dObj instanceof HashMap) {
            HashMap configurationDictionary = (HashMap) dObj;
            Object tmp = this.library.getName(configurationDictionary, BASE_STATE_KEY);
            if (tmp != null) {
                this.baseState = (Name) tmp;
            }
            boolean isBaseOn = this.baseState.equals(ON_vALUE);
            if (isBaseOn) {
                toggle = this.library.getArray(configurationDictionary, OFF_VALUE);
            } else {
                toggle = this.library.getArray(configurationDictionary, ON_vALUE);
            }
            if (toggle != null) {
                for (Object obj : toggle) {
                    OptionalContentGroup ocg = this.groups.get(obj);
                    if (ocg != null) {
                        if (isBaseOn) {
                            ocg.setVisible(false);
                        } else {
                            ocg.setVisible(true);
                        }
                    }
                }
            }
            Object tmp2 = this.library.getName(configurationDictionary, INTENT_KEY);
            if (tmp2 != null) {
                this.intent = Arrays.asList((Name) tmp2);
            }
            Object tmp3 = this.library.getObject(configurationDictionary, ORDER_KEY);
            if (tmp3 != null && (tmp3 instanceof List)) {
                List orderedOCs = (List) tmp3;
                if (orderedOCs.size() > 0) {
                    this.order = new ArrayList(orderedOCs.size());
                    this.order = parseOrderArray(orderedOCs, null);
                }
            }
            Object tmp4 = this.library.getObject(configurationDictionary, RBGROUPS_KEY);
            if (tmp4 != null && (tmp4 instanceof List)) {
                List orderedOCs2 = (List) tmp4;
                if (orderedOCs2.size() > 0) {
                    this.rbGroups = new ArrayList(orderedOCs2.size());
                    this.rbGroups = parseOrderArray(orderedOCs2, null);
                }
            }
        }
        this.inited = true;
    }

    private List<Object> parseOrderArray(List<Object> rawOrder, OptionalContentGroup parent) {
        List<Object> order = new ArrayList<>(5);
        OptionalContentGroup group = null;
        Iterator i$ = rawOrder.iterator();
        while (i$.hasNext()) {
            Object obj = i$.next();
            if (obj instanceof Reference) {
                Object refObject = getOCGs((Reference) obj);
                if (refObject != null) {
                    group = (OptionalContentGroup) refObject;
                    if (parent != null && !parent.isVisible()) {
                        group.setVisible(false);
                    }
                    order.add(group);
                } else {
                    obj = this.library.getObject((Reference) obj);
                }
            }
            if (obj instanceof List) {
                parent = group;
                order.add(parseOrderArray((List) obj, parent));
            } else if (obj instanceof StringObject) {
                order.add(Utils.convertStringObject(this.library, (StringObject) obj));
            }
        }
        return order;
    }

    public boolean isVisible(Reference ocgRef) {
        Object object = this.library.getObject(ocgRef);
        if (object instanceof OptionalContentGroup) {
            return isVisible((OptionalContentGroup) object);
        }
        if (object instanceof OptionalContentMembership) {
            return isVisible((OptionalContentMembership) object);
        }
        return false;
    }

    public boolean isVisible(OptionalContentGroup ocg) {
        return this.groups.containsKey(ocg.getPObjectReference());
    }

    public boolean isVisible(OptionalContentMembership ocmd) {
        ocmd.init();
        return ocmd.isVisible();
    }

    public boolean isVisible(Object object) {
        if (object instanceof Reference) {
            return isVisible((Reference) object);
        }
        if (object instanceof OptionalContentGroup) {
            return isVisible((OptionalContentGroup) object);
        }
        if (object instanceof OptionalContentMembership) {
            return isVisible((OptionalContentMembership) object);
        }
        return true;
    }

    public List<Object> getOrder() {
        return this.order;
    }

    public List<Name> getIntent() {
        return this.intent;
    }

    public int getGroupsSize() {
        return this.groups.size();
    }

    public List<Object> getRbGroups() {
        return this.rbGroups;
    }

    public OptionalContentGroup getOCGs(Reference reference) {
        return this.groups.get(reference);
    }
}
