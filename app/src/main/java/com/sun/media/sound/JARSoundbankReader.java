package com.sun.media.sound;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.spi.SoundbankReader;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/media/sound/JARSoundbankReader.class */
public final class JARSoundbankReader extends SoundbankReader {
    private static final String JAR_SOUNDBANK_ENABLED = "jdk.sound.jarsoundbank";

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0040  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean isZIP(java.net.URL r3) {
        /*
            r0 = 0
            r4 = r0
            r0 = r3
            java.io.InputStream r0 = r0.openStream()     // Catch: java.io.IOException -> L55
            r5 = r0
            r0 = 4
            byte[] r0 = new byte[r0]     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L55
            r6 = r0
            r0 = r5
            r1 = r6
            int r0 = r0.read(r1)     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L55
            r1 = 4
            if (r0 != r1) goto L18
            r0 = 1
            goto L19
        L18:
            r0 = 0
        L19:
            r4 = r0
            r0 = r4
            if (r0 == 0) goto L42
            r0 = r6
            r1 = 0
            r0 = r0[r1]     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L55
            r1 = 80
            if (r0 != r1) goto L40
            r0 = r6
            r1 = 1
            r0 = r0[r1]     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L55
            r1 = 75
            if (r0 != r1) goto L40
            r0 = r6
            r1 = 2
            r0 = r0[r1]     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L55
            r1 = 3
            if (r0 != r1) goto L40
            r0 = r6
            r1 = 3
            r0 = r0[r1]     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L55
            r1 = 4
            if (r0 != r1) goto L40
            r0 = 1
            goto L41
        L40:
            r0 = 0
        L41:
            r4 = r0
        L42:
            r0 = r5
            r0.close()     // Catch: java.io.IOException -> L55
            goto L52
        L49:
            r7 = move-exception
            r0 = r5
            r0.close()     // Catch: java.io.IOException -> L55
            r0 = r7
            throw r0     // Catch: java.io.IOException -> L55
        L52:
            goto L56
        L55:
            r5 = move-exception
        L56:
            r0 = r4
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.media.sound.JARSoundbankReader.isZIP(java.net.URL):boolean");
    }

    @Override // javax.sound.midi.spi.SoundbankReader
    public Soundbank getSoundbank(URL url) throws InvalidMidiDataException, IOException {
        Objects.requireNonNull(url);
        if (!Boolean.getBoolean(JAR_SOUNDBANK_ENABLED) || !isZIP(url)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        URLClassLoader uRLClassLoaderNewInstance = URLClassLoader.newInstance(new URL[]{url});
        InputStream resourceAsStream = uRLClassLoaderNewInstance.getResourceAsStream("META-INF/services/javax.sound.midi.Soundbank");
        if (resourceAsStream == null) {
            return null;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                if (!line.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                    try {
                        Class<?> cls = Class.forName(line.trim(), false, uRLClassLoaderNewInstance);
                        if (Soundbank.class.isAssignableFrom(cls)) {
                            arrayList.add((Soundbank) ReflectUtil.newInstance(cls));
                        }
                    } catch (ClassNotFoundException e2) {
                    } catch (IllegalAccessException e3) {
                    } catch (InstantiationException e4) {
                    }
                }
            }
            if (arrayList.size() == 0) {
                return null;
            }
            if (arrayList.size() == 1) {
                return (Soundbank) arrayList.get(0);
            }
            SimpleSoundbank simpleSoundbank = new SimpleSoundbank();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                simpleSoundbank.addAllInstruments((Soundbank) it.next());
            }
            return simpleSoundbank;
        } finally {
            resourceAsStream.close();
        }
    }

    @Override // javax.sound.midi.spi.SoundbankReader
    public Soundbank getSoundbank(InputStream inputStream) throws InvalidMidiDataException, IOException {
        return null;
    }

    @Override // javax.sound.midi.spi.SoundbankReader
    public Soundbank getSoundbank(File file) throws InvalidMidiDataException, IOException {
        Objects.requireNonNull(file);
        return getSoundbank(file.toURI().toURL());
    }
}
