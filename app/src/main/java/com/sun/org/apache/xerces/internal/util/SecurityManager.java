package com.sun.org.apache.xerces.internal.util;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/SecurityManager.class */
public final class SecurityManager {
    private static final int DEFAULT_ENTITY_EXPANSION_LIMIT = 64000;
    private static final int DEFAULT_MAX_OCCUR_NODE_LIMIT = 5000;
    private static final int DEFAULT_ELEMENT_ATTRIBUTE_LIMIT = 10000;
    private int entityExpansionLimit = DEFAULT_ENTITY_EXPANSION_LIMIT;
    private int maxOccurLimit = 5000;
    private int fElementAttributeLimit = 10000;

    public SecurityManager() {
        readSystemProperties();
    }

    public void setEntityExpansionLimit(int limit) {
        this.entityExpansionLimit = limit;
    }

    public int getEntityExpansionLimit() {
        return this.entityExpansionLimit;
    }

    public void setMaxOccurNodeLimit(int limit) {
        this.maxOccurLimit = limit;
    }

    public int getMaxOccurNodeLimit() {
        return this.maxOccurLimit;
    }

    public int getElementAttrLimit() {
        return this.fElementAttributeLimit;
    }

    public void setElementAttrLimit(int limit) {
        this.fElementAttributeLimit = limit;
    }

    private void readSystemProperties() {
        try {
            String value = System.getProperty("entityExpansionLimit");
            if (value != null && !value.equals("")) {
                this.entityExpansionLimit = Integer.parseInt(value);
                if (this.entityExpansionLimit < 0) {
                    this.entityExpansionLimit = DEFAULT_ENTITY_EXPANSION_LIMIT;
                }
            } else {
                this.entityExpansionLimit = DEFAULT_ENTITY_EXPANSION_LIMIT;
            }
        } catch (Exception e2) {
        }
        try {
            String value2 = System.getProperty("maxOccurLimit");
            if (value2 != null && !value2.equals("")) {
                this.maxOccurLimit = Integer.parseInt(value2);
                if (this.maxOccurLimit < 0) {
                    this.maxOccurLimit = 5000;
                }
            } else {
                this.maxOccurLimit = 5000;
            }
        } catch (Exception e3) {
        }
        try {
            String value3 = System.getProperty("elementAttributeLimit");
            if (value3 != null && !value3.equals("")) {
                this.fElementAttributeLimit = Integer.parseInt(value3);
                if (this.fElementAttributeLimit < 0) {
                    this.fElementAttributeLimit = 10000;
                }
            } else {
                this.fElementAttributeLimit = 10000;
            }
        } catch (Exception e4) {
        }
    }
}
