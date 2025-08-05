package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/javafx/css/StringStore.class */
public class StringStore {
    private final Map<String, Integer> stringMap = new HashMap();
    public final List<String> strings = new ArrayList();

    public int addString(String s2) {
        Integer index = this.stringMap.get(s2);
        if (index == null) {
            index = Integer.valueOf(this.strings.size());
            this.strings.add(s2);
            this.stringMap.put(s2, index);
        }
        return index.intValue();
    }

    public void writeBinary(DataOutputStream os) throws IOException {
        os.writeShort(this.strings.size());
        if (this.stringMap.containsKey(null)) {
            Integer index = this.stringMap.get(null);
            os.writeShort(index.intValue());
        } else {
            os.writeShort(-1);
        }
        for (int n2 = 0; n2 < this.strings.size(); n2++) {
            String s2 = this.strings.get(n2);
            if (s2 != null) {
                os.writeUTF(s2);
            }
        }
    }

    static String[] readBinary(DataInputStream is) throws IOException {
        int nStrings = is.readShort();
        int nullIndex = is.readShort();
        String[] strings = new String[nStrings];
        Arrays.fill(strings, (Object) null);
        for (int n2 = 0; n2 < nStrings; n2++) {
            if (n2 != nullIndex) {
                strings[n2] = is.readUTF();
            }
        }
        return strings;
    }
}
