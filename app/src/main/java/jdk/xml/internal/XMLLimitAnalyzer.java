package jdk.xml.internal;

import com.sun.javafx.fxml.BeanAdapter;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import jdk.xml.internal.XMLSecurityManager;

/* loaded from: rt.jar:jdk/xml/internal/XMLLimitAnalyzer.class */
public final class XMLLimitAnalyzer {
    private String entityStart;
    private String entityEnd;
    private final int[] values = new int[XMLSecurityManager.Limit.values().length];
    private final int[] totalValue = new int[XMLSecurityManager.Limit.values().length];
    private final String[] names = new String[XMLSecurityManager.Limit.values().length];
    private final Map<String, Integer>[] caches = new Map[XMLSecurityManager.Limit.values().length];

    /* loaded from: rt.jar:jdk/xml/internal/XMLLimitAnalyzer$NameMap.class */
    public enum NameMap {
        ENTITY_EXPANSION_LIMIT("jdk.xml.entityExpansionLimit", "entityExpansionLimit"),
        MAX_OCCUR_NODE_LIMIT("jdk.xml.maxOccurLimit", "maxOccurLimit"),
        ELEMENT_ATTRIBUTE_LIMIT("jdk.xml.elementAttributeLimit", "elementAttributeLimit");

        final String newName;
        final String oldName;

        NameMap(String newName, String oldName) {
            this.newName = newName;
            this.oldName = oldName;
        }

        String getOldName(String newName) {
            if (newName.equals(this.newName)) {
                return this.oldName;
            }
            return null;
        }
    }

    public void addValue(XMLSecurityManager.Limit limit, String entityName, int value) {
        addValue(limit.ordinal(), entityName, value);
    }

    public void addValue(int index, String entityName, int value) {
        Map<String, Integer> cache;
        if (index == XMLSecurityManager.Limit.ENTITY_EXPANSION_LIMIT.ordinal() || index == XMLSecurityManager.Limit.MAX_OCCUR_NODE_LIMIT.ordinal() || index == XMLSecurityManager.Limit.ELEMENT_ATTRIBUTE_LIMIT.ordinal() || index == XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT.ordinal() || index == XMLSecurityManager.Limit.ENTITY_REPLACEMENT_LIMIT.ordinal()) {
            int[] iArr = this.totalValue;
            iArr[index] = iArr[index] + value;
            return;
        }
        if (index == XMLSecurityManager.Limit.MAX_ELEMENT_DEPTH_LIMIT.ordinal() || index == XMLSecurityManager.Limit.MAX_NAME_LIMIT.ordinal()) {
            this.values[index] = value;
            this.totalValue[index] = value;
            return;
        }
        if (this.caches[index] == null) {
            cache = new HashMap(10);
            this.caches[index] = cache;
        } else {
            cache = this.caches[index];
        }
        int accumulatedValue = value;
        if (cache.containsKey(entityName)) {
            accumulatedValue += cache.get(entityName).intValue();
            cache.put(entityName, Integer.valueOf(accumulatedValue));
        } else {
            cache.put(entityName, Integer.valueOf(value));
        }
        if (accumulatedValue > this.values[index]) {
            this.values[index] = accumulatedValue;
            this.names[index] = entityName;
        }
        if (index == XMLSecurityManager.Limit.GENERAL_ENTITY_SIZE_LIMIT.ordinal() || index == XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT.ordinal()) {
            int[] iArr2 = this.totalValue;
            int iOrdinal = XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT.ordinal();
            iArr2[iOrdinal] = iArr2[iOrdinal] + value;
        }
    }

    public int getValue(XMLSecurityManager.Limit limit) {
        return getValue(limit.ordinal());
    }

    public int getValue(int index) {
        if (index == XMLSecurityManager.Limit.ENTITY_REPLACEMENT_LIMIT.ordinal()) {
            return this.totalValue[index];
        }
        return this.values[index];
    }

    public int getTotalValue(XMLSecurityManager.Limit limit) {
        return this.totalValue[limit.ordinal()];
    }

    public int getTotalValue(int index) {
        return this.totalValue[index];
    }

    public int getValueByIndex(int index) {
        return this.values[index];
    }

    public void startEntity(String name) {
        this.entityStart = name;
    }

    public boolean isTracking(String name) {
        if (this.entityStart == null) {
            return false;
        }
        return this.entityStart.equals(name);
    }

    public void endEntity(XMLSecurityManager.Limit limit, String name) {
        this.entityStart = "";
        Map<String, Integer> cache = this.caches[limit.ordinal()];
        if (cache != null) {
            cache.remove(name);
        }
    }

    public void reset(XMLSecurityManager.Limit limit) {
        if (limit.ordinal() == XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT.ordinal()) {
            this.totalValue[limit.ordinal()] = 0;
        } else if (limit.ordinal() == XMLSecurityManager.Limit.GENERAL_ENTITY_SIZE_LIMIT.ordinal()) {
            this.names[limit.ordinal()] = null;
            this.values[limit.ordinal()] = 0;
            this.caches[limit.ordinal()] = null;
            this.totalValue[limit.ordinal()] = 0;
        }
    }

    public void debugPrint(XMLSecurityManager securityManager) {
        Formatter formatter = new Formatter();
        System.out.println(formatter.format("%30s %15s %15s %15s %30s", BeanAdapter.PROPERTY_SUFFIX, "Limit", "Total size", "Size", "Entity Name"));
        for (XMLSecurityManager.Limit limit : XMLSecurityManager.Limit.values()) {
            Formatter formatter2 = new Formatter();
            System.out.println(formatter2.format("%30s %15d %15d %15d %30s", limit.name(), Integer.valueOf(securityManager.getLimit(limit)), Integer.valueOf(this.totalValue[limit.ordinal()]), Integer.valueOf(this.values[limit.ordinal()]), this.names[limit.ordinal()]));
        }
    }
}
