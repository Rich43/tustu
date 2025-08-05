package g;

import java.io.File;
import java.util.ArrayList;
import javax.swing.filechooser.FileFilter;

/* renamed from: g.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:g/f.class */
public class C1728f extends FileFilter {

    /* renamed from: a, reason: collision with root package name */
    String f12193a = "";

    /* renamed from: b, reason: collision with root package name */
    ArrayList f12194b = new ArrayList();

    public C1728f(String str) {
        a(str);
    }

    public void a(String str) {
        this.f12193a = str;
    }

    public void b(String str) {
        this.f12194b.add(str.toLowerCase());
    }

    @Override // javax.swing.filechooser.FileFilter
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String name = file.getName();
        if (name.lastIndexOf(46) <= -1) {
            return false;
        }
        return this.f12194b.contains(name.substring(name.lastIndexOf(46) + 1).toLowerCase());
    }

    @Override // javax.swing.filechooser.FileFilter
    public String getDescription() {
        return this.f12193a;
    }
}
