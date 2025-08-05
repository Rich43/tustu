package java.time.zone;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.StreamCorruptedException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: rt.jar:java/time/zone/TzdbZoneRulesProvider.class */
final class TzdbZoneRulesProvider extends ZoneRulesProvider {
    private List<String> regionIds;
    private String versionId;
    private final Map<String, Object> regionToRules = new ConcurrentHashMap();

    public TzdbZoneRulesProvider() {
        try {
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(System.getProperty("java.home") + File.separator + "lib", "tzdb.dat"))));
            Throwable th = null;
            try {
                try {
                    load(dataInputStream);
                    if (dataInputStream != null) {
                        if (0 != 0) {
                            try {
                                dataInputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            dataInputStream.close();
                        }
                    }
                } finally {
                }
            } finally {
            }
        } catch (Exception e2) {
            throw new ZoneRulesException("Unable to load TZDB time-zone rules", e2);
        }
    }

    @Override // java.time.zone.ZoneRulesProvider
    protected Set<String> provideZoneIds() {
        return new HashSet(this.regionIds);
    }

    @Override // java.time.zone.ZoneRulesProvider
    protected ZoneRules provideRules(String str, boolean z2) {
        Object obj = this.regionToRules.get(str);
        if (obj == null) {
            throw new ZoneRulesException("Unknown time-zone ID: " + str);
        }
        try {
            if (obj instanceof byte[]) {
                obj = Ser.read(new DataInputStream(new ByteArrayInputStream((byte[]) obj)));
                this.regionToRules.put(str, obj);
            }
            return (ZoneRules) obj;
        } catch (Exception e2) {
            throw new ZoneRulesException("Invalid binary time-zone data: TZDB:" + str + ", version: " + this.versionId, e2);
        }
    }

    @Override // java.time.zone.ZoneRulesProvider
    protected NavigableMap<String, ZoneRules> provideVersions(String str) {
        TreeMap treeMap = new TreeMap();
        ZoneRules rules = getRules(str, false);
        if (rules != null) {
            treeMap.put(this.versionId, rules);
        }
        return treeMap;
    }

    private void load(DataInputStream dataInputStream) throws Exception {
        if (dataInputStream.readByte() != 1) {
            throw new StreamCorruptedException("File format not recognised");
        }
        if (!"TZDB".equals(dataInputStream.readUTF())) {
            throw new StreamCorruptedException("File format not recognised");
        }
        int i2 = dataInputStream.readShort();
        for (int i3 = 0; i3 < i2; i3++) {
            this.versionId = dataInputStream.readUTF();
        }
        int i4 = dataInputStream.readShort();
        String[] strArr = new String[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            strArr[i5] = dataInputStream.readUTF();
        }
        this.regionIds = Arrays.asList(strArr);
        int i6 = dataInputStream.readShort();
        Object[] objArr = new Object[i6];
        for (int i7 = 0; i7 < i6; i7++) {
            byte[] bArr = new byte[dataInputStream.readShort()];
            dataInputStream.readFully(bArr);
            objArr[i7] = bArr;
        }
        for (int i8 = 0; i8 < i2; i8++) {
            int i9 = dataInputStream.readShort();
            this.regionToRules.clear();
            for (int i10 = 0; i10 < i9; i10++) {
                this.regionToRules.put(strArr[dataInputStream.readShort()], objArr[dataInputStream.readShort() & 65535]);
            }
        }
    }

    public String toString() {
        return "TZDB[" + this.versionId + "]";
    }
}
