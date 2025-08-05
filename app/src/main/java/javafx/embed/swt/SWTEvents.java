package javafx.embed.swt;

/* loaded from: jfxswt.jar:javafx/embed/swt/SWTEvents.class */
class SWTEvents {
    static final int[][] KeyTable = {new int[]{0, 0}, new int[]{10, 13}, new int[]{10, 10}, new int[]{8, 8}, new int[]{9, 9}, new int[]{27, 27}, new int[]{32, 32}, new int[]{127, 127}, new int[]{155, 16777225}, new int[]{156, 16777297}, new int[]{16, 131072}, new int[]{17, 262144}, new int[]{18, 65536}, new int[]{524, 4194304}, new int[]{20, 16777298}, new int[]{144, 16777299}, new int[]{145, 16777300}, new int[]{33, 16777221}, new int[]{34, 16777222}, new int[]{35, 16777224}, new int[]{36, 16777223}, new int[]{37, 16777219}, new int[]{38, 16777217}, new int[]{39, 16777220}, new int[]{40, 16777218}, new int[]{44, 44}, new int[]{45, 45}, new int[]{46, 46}, new int[]{47, 47}, new int[]{59, 59}, new int[]{61, 61}, new int[]{91, 91}, new int[]{92, 92}, new int[]{93, 93}, new int[]{106, 16777258}, new int[]{107, 16777259}, new int[]{109, 16777261}, new int[]{110, 16777262}, new int[]{111, 16777263}, new int[]{150, 64}, new int[]{151, 42}, new int[]{152, 34}, new int[]{153, 60}, new int[]{160, 62}, new int[]{161, 123}, new int[]{162, 125}, new int[]{192, 96}, new int[]{222, 39}, new int[]{512, 64}, new int[]{513, 58}, new int[]{514, 94}, new int[]{515, 36}, new int[]{517, 33}, new int[]{519, 40}, new int[]{520, 35}, new int[]{521, 43}, new int[]{522, 41}, new int[]{523, 95}, new int[]{48, 48}, new int[]{49, 49}, new int[]{50, 50}, new int[]{51, 51}, new int[]{52, 52}, new int[]{53, 53}, new int[]{54, 54}, new int[]{55, 55}, new int[]{56, 56}, new int[]{57, 57}, new int[]{65, 97}, new int[]{66, 98}, new int[]{67, 99}, new int[]{68, 100}, new int[]{69, 101}, new int[]{70, 102}, new int[]{71, 103}, new int[]{72, 104}, new int[]{73, 105}, new int[]{74, 106}, new int[]{75, 107}, new int[]{76, 108}, new int[]{77, 109}, new int[]{78, 110}, new int[]{79, 111}, new int[]{80, 112}, new int[]{81, 113}, new int[]{82, 114}, new int[]{83, 115}, new int[]{84, 116}, new int[]{85, 117}, new int[]{86, 118}, new int[]{87, 119}, new int[]{88, 120}, new int[]{89, 121}, new int[]{90, 122}, new int[]{96, 16777264}, new int[]{97, 16777265}, new int[]{98, 16777266}, new int[]{99, 16777267}, new int[]{100, 16777268}, new int[]{101, 16777269}, new int[]{102, 16777270}, new int[]{103, 16777271}, new int[]{104, 16777272}, new int[]{105, 16777273}, new int[]{112, 16777226}, new int[]{113, 16777227}, new int[]{114, 16777228}, new int[]{115, 16777229}, new int[]{116, 16777230}, new int[]{117, 16777231}, new int[]{118, 16777232}, new int[]{119, 16777233}, new int[]{120, 16777234}, new int[]{121, 16777235}, new int[]{122, 16777236}, new int[]{123, 16777237}};

    SWTEvents() {
    }

    static int mouseButtonToEmbedMouseButton(int button, int extModifiers) {
        switch (button) {
            case 1:
                return 1;
            case 2:
                return 4;
            case 3:
                return 2;
            default:
                return 0;
        }
    }

    /*  JADX ERROR: NoSuchElementException in pass: ReplaceNewArray
        java.util.NoSuchElementException
        	at java.base/java.util.TreeMap.key(Unknown Source)
        	at java.base/java.util.TreeMap.lastKey(Unknown Source)
        	at jadx.core.dex.visitors.ReplaceNewArray.processNewArray(ReplaceNewArray.java:171)
        	at jadx.core.dex.visitors.ReplaceNewArray.processInsn(ReplaceNewArray.java:72)
        	at jadx.core.dex.visitors.ReplaceNewArray.visit(ReplaceNewArray.java:53)
        */
    static int getWheelRotation(org.eclipse.swt.events.MouseEvent r7, int r8) {
        /*
            r0 = 1
            r9 = r0
            r0 = r8
            r1 = 7
            if (r0 != r1) goto La3
            java.lang.String r0 = "win32"
            java.lang.String r1 = org.eclipse.swt.SWT.getPlatform()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L8a
            r0 = 1
            int[] r0 = new int[r0]
            r10 = r0
            java.lang.String r0 = "org.eclipse.swt.internal.win32.OS"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r11 = r0
            r0 = r11
            java.lang.String r1 = "SystemParametersInfo"
            r2 = 4
            java.lang.Class[] r2 = new java.lang.Class[r2]     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3 = r2
            r4 = 0
            java.lang.Class<java.lang.Integer> r5 = java.lang.Integer.TYPE     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3[r4] = r5     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3 = r2
            r4 = 1
            java.lang.Class<java.lang.Integer> r5 = java.lang.Integer.TYPE     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3[r4] = r5     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3 = r2
            r4 = 2
            java.lang.Class<int[]> r5 = int[].class
            r3[r4] = r5     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3 = r2
            r4 = 3
            java.lang.Class<java.lang.Integer> r5 = java.lang.Integer.TYPE     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3[r4] = r5     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            java.lang.reflect.Method r0 = r0.getDeclaredMethod(r1, r2)     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r12 = r0
            r0 = r12
            r1 = r11
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3 = r2
            r4 = 0
            r5 = 104(0x68, float:1.46E-43)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3[r4] = r5     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3 = r2
            r4 = 1
            r5 = 0
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3[r4] = r5     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3 = r2
            r4 = 2
            r5 = r10
            r3[r4] = r5     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3 = r2
            r4 = 3
            r5 = 0
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            r3[r4] = r5     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            java.lang.Object r0 = r0.invoke(r1, r2)     // Catch: java.lang.IllegalAccessException -> L6b java.lang.reflect.InvocationTargetException -> L70 java.lang.NoSuchMethodException -> L75 java.lang.ClassNotFoundException -> L7a
            goto L7c
        L6b:
            r11 = move-exception
            goto L7c
        L70:
            r11 = move-exception
            goto L7c
        L75:
            r11 = move-exception
            goto L7c
        L7a:
            r11 = move-exception
        L7c:
            r0 = r10
            r1 = 0
            r0 = r0[r1]
            r1 = -1
            if (r0 == r1) goto L87
            r0 = r10
            r1 = 0
            r0 = r0[r1]
            r9 = r0
        L87:
            goto L97
        L8a:
            java.lang.String r0 = "gtk"
            java.lang.String r1 = org.eclipse.swt.SWT.getPlatform()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L97
            r0 = 3
            r9 = r0
        L97:
            r0 = r7
            int r0 = r0.count
            int r0 = -r0
            r1 = 1
            r2 = r9
            int r1 = java.lang.Math.max(r1, r2)
            int r0 = r0 / r1
            return r0
        La3:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.embed.swt.SWTEvents.getWheelRotation(org.eclipse.swt.events.MouseEvent, int):int");
    }

    static int keyIDToEmbedKeyType(int id) {
        switch (id) {
            case 1:
                return 0;
            case 2:
                return 1;
            default:
                return 0;
        }
    }

    static int keyCodeToEmbedKeyCode(int keyCode) {
        for (int i2 = 0; i2 < KeyTable.length; i2++) {
            if (KeyTable[i2][1] == keyCode) {
                return KeyTable[i2][0];
            }
        }
        return 0;
    }

    static int keyModifiersToEmbedKeyModifiers(int extModifiers) {
        int embedModifiers = 0;
        if ((extModifiers & 131072) != 0) {
            embedModifiers = 0 | 1;
        }
        if ((extModifiers & 262144) != 0) {
            embedModifiers |= 2;
        }
        if ((extModifiers & 65536) != 0) {
            embedModifiers |= 4;
        }
        if ((extModifiers & 4194304) != 0) {
            embedModifiers |= 8;
        }
        return embedModifiers;
    }
}
