package com.sun.org.apache.xml.internal.resolver;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/CatalogEntry.class */
public class CatalogEntry {
    protected static AtomicInteger nextEntry = new AtomicInteger(0);
    protected static final Map<String, Integer> entryTypes = new ConcurrentHashMap();
    protected static Vector entryArgs = new Vector();
    protected int entryType;
    protected Vector args;

    static int addEntryType(String name, int numArgs) {
        int index = nextEntry.getAndIncrement();
        entryTypes.put(name, Integer.valueOf(index));
        entryArgs.add(index, Integer.valueOf(numArgs));
        return index;
    }

    public static int getEntryType(String name) throws CatalogException {
        if (!entryTypes.containsKey(name)) {
            throw new CatalogException(3);
        }
        Integer iType = entryTypes.get(name);
        if (iType == null) {
            throw new CatalogException(3);
        }
        return iType.intValue();
    }

    public static int getEntryArgCount(String name) throws CatalogException {
        return getEntryArgCount(getEntryType(name));
    }

    public static int getEntryArgCount(int type) throws CatalogException {
        try {
            Integer iArgs = (Integer) entryArgs.get(type);
            return iArgs.intValue();
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new CatalogException(3);
        }
    }

    public CatalogEntry() {
        this.entryType = 0;
        this.args = null;
    }

    public CatalogEntry(String name, Vector args) throws CatalogException {
        this.entryType = 0;
        this.args = null;
        Integer iType = entryTypes.get(name);
        if (iType == null) {
            throw new CatalogException(3);
        }
        int type = iType.intValue();
        try {
            Integer iArgs = (Integer) entryArgs.get(type);
            if (iArgs.intValue() != args.size()) {
                throw new CatalogException(2);
            }
            this.entryType = type;
            this.args = args;
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new CatalogException(3);
        }
    }

    public CatalogEntry(int type, Vector args) throws CatalogException {
        this.entryType = 0;
        this.args = null;
        try {
            Integer iArgs = (Integer) entryArgs.get(type);
            if (iArgs.intValue() != args.size()) {
                throw new CatalogException(2);
            }
            this.entryType = type;
            this.args = args;
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new CatalogException(3);
        }
    }

    public int getEntryType() {
        return this.entryType;
    }

    public String getEntryArg(int argNum) {
        try {
            String arg = (String) this.args.get(argNum);
            return arg;
        } catch (ArrayIndexOutOfBoundsException e2) {
            return null;
        }
    }

    public void setEntryArg(int argNum, String newspec) throws ArrayIndexOutOfBoundsException {
        this.args.set(argNum, newspec);
    }
}
