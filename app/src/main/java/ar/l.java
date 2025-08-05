package aR;

import d.InterfaceC1711c;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/* loaded from: TunerStudioMS.jar:aR/l.class */
public class l implements InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    public static String f3869a = "Sound_File_Path";

    /* renamed from: b, reason: collision with root package name */
    public static String f3870b = "playSoundFile";

    /* renamed from: c, reason: collision with root package name */
    d.k f3871c = null;

    @Override // d.InterfaceC1711c
    public String a() {
        return f3870b;
    }

    @Override // d.InterfaceC1711c
    public String b() {
        return "Play Sound File";
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Other Actions";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) throws d.e {
        String property = properties.getProperty(f3869a, null);
        if (property == null) {
            throw new d.e(f3869a + " is required");
        }
        File file = new File(property);
        if (!file.exists()) {
            throw new d.e("Sound File not found.");
        }
        if (property.toLowerCase().endsWith("mp3")) {
            try {
                new MediaPlayer(new Media(file.toURI().toString())).play();
                return;
            } catch (Error e2) {
                bH.C.b("No JavaFX, Can't play mp3.");
                throw new d.e("JavaFX Libraries not found, JavaFX is required for mp3 playback.");
            }
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (IOException e3) {
            bH.C.a("Unable to read sound file: " + file.getAbsolutePath());
        } catch (LineUnavailableException e4) {
            bH.C.a("Unable to open player line.");
        } catch (UnsupportedAudioFileException e5) {
            throw new d.e("Unsupported audio format:\n" + e5.getLocalizedMessage());
        }
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) throws d.e {
        String property = properties.getProperty(f3869a, null);
        if (property == null) {
            throw new d.e(f3869a + " is required");
        }
        File file = new File(property);
        if (!file.exists()) {
            throw new d.e("Sound File not found.");
        }
        if (property.toLowerCase().endsWith("mp3")) {
            try {
                new Media(file.toURI().toString());
            } catch (Error e2) {
                bH.C.b("No JavaFX, Can't play mp3.");
                throw new d.e("JavaFX Libraries not found, JavaFX is required for mp3 playback.");
            }
        }
    }

    @Override // d.InterfaceC1711c
    public d.k e() {
        if (this.f3871c == null) {
            this.f3871c = new d.k();
            d.i iVar = new d.i(f3869a, "");
            iVar.a(7);
            iVar.c("Set the full Path to the file you would like play when this action is triggered. Supportde formats are: wav, aiff, au and mp3. For MP3, you must have JavaFX libraries. Windows and OS X installers always include these.");
            this.f3871c.add(iVar);
        }
        return this.f3871c;
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return false;
    }
}
