package com.efiAnalytics.tunerStudio.search;

import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/z.class */
class z extends DefaultTableModel {

    /* renamed from: a, reason: collision with root package name */
    Map f10232a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ q f10233b;

    z(q qVar) {
        this.f10233b = qVar;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public boolean isCellEditable(int i2, int i3) {
        return i3 == 2;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public int getColumnCount() {
        return 3;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public int getRowCount() {
        if (this.f10233b.f10209b.isEmpty()) {
            return 1;
        }
        return this.f10233b.f10209b.size();
    }

    @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public Class getColumnClass(int i2) {
        return i2 == 0 ? ImageIcon.class : i2 == 2 ? v.class : Object.class;
    }

    @Override // javax.swing.table.DefaultTableModel, javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        String str;
        if (i2 < 0 || i2 >= this.f10233b.f10209b.size()) {
            if (i3 == 0) {
                try {
                    ImageIcon imageIcon = (ImageIcon) this.f10232a.get("Searching");
                    if (imageIcon == null) {
                        imageIcon = new ImageIcon(cO.a().a(cO.f11105u, this.f10233b.f10211d, this.f10233b.f10214i));
                        this.f10232a.put("Searching", imageIcon);
                    }
                    return imageIcon;
                } catch (V.a e2) {
                    Logger.getLogger(q.class.getName()).log(Level.WARNING, "Falied to load searching ICON", (Throwable) e2);
                    return "";
                }
            }
            if (i3 == 1) {
                return "<html><center><font size=+1>No " + C1798a.f13272f + " found.</font><br>Connect to the same network as your " + C1798a.f13272f + " and power it on, or select \"Work offline\"</center></html>";
            }
            if (i3 != 2) {
                return "";
            }
            String str2 = q.f10221p;
            this.f10233b.f10212e.b(str2);
            this.f10233b.f10212e.a(str2);
            this.f10233b.f10213f.b(str2);
            this.f10233b.f10213f.a(str2);
            this.f10233b.f10212e.a(true);
            return "";
        }
        f fVar = (f) this.f10233b.f10209b.get(i2);
        if (i3 == 0) {
            try {
                ImageIcon imageIcon2 = (ImageIcon) this.f10232a.get(fVar.b());
                if (imageIcon2 == null) {
                    imageIcon2 = new ImageIcon((fVar.b().contains("Gen 4") || fVar.b().contains("Gen4")) ? cO.a().a(cO.f11139ac, this.f10233b.f10211d, 50) : cO.a().a(cO.f11140ad, this.f10233b.f10211d, 50));
                    this.f10232a.put(fVar.b(), imageIcon2);
                }
                return imageIcon2;
            } catch (V.a e3) {
                Logger.getLogger(q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                return fVar.b();
            }
        }
        if (i3 != 1) {
            return i3 == 2 ? this.f10233b.f10212e.b() : "Huh?";
        }
        int iA = eJ.a(5);
        if (fVar.b().equals(bT.o.f7604a)) {
            str = (fVar.f().j() == null || fVar.f().j().isEmpty()) ? (fVar.a() == null || fVar.a().isEmpty()) ? (fVar.f().f() == null || fVar.f().f().isEmpty()) ? (fVar.f().b() == null || fVar.f().b().isEmpty()) ? "<html><center><font size=" + iA + ">" + fVar.b() + ", Serial# " + fVar.a() + "</font><br>Host: " + fVar.f().c() : "<html><center><font size=" + iA + ">" + fVar.f().b() + ", " + fVar.f().f() + "</font><br>Host: " + fVar.f().c() : "<html><center><font size=" + iA + ">" + fVar.b() + " Host: " + fVar.f().c() + "</font><br>" + fVar.f().f() : "<html><center><font size=" + iA + ">" + fVar.b() + ", Serial# " + fVar.a() + "</font><br>Host: " + fVar.f().c() : "<html><center><font size=" + iA + ">" + fVar.b() + " - " + fVar.f().j() + "</font><br> " + fVar.f().f() + "<br>Host: " + fVar.f().c();
        } else {
            str = (fVar.a() == null || fVar.a().isEmpty()) ? (fVar.f().j() == null || fVar.f().j().isEmpty()) ? (fVar.f().f() == null || fVar.f().f().isEmpty()) ? (fVar.f().b() == null || fVar.f().b().isEmpty()) ? "<html><center><font size=" + iA + ">" + fVar.b() + ", Serial# " + fVar.a() + "</font><br>Host: " + fVar.f().c() : "<html><center><font size=" + iA + ">" + fVar.f().b() + ", " + fVar.f().f() + "</font><br>Host: " + fVar.f().c() : "<html><center><font size=" + iA + ">" + fVar.b() + " Host: " + fVar.f().c() + "</font><br>" + fVar.f().f() : "<html><center><font size=" + iA + ">" + fVar.b() + " - " + fVar.f().j() + "</font><br> " + fVar.f().f() + "<br>Host: " + fVar.f().c() : "<html><center><font size=" + iA + ">" + fVar.b() + ", Serial# " + fVar.a() + "</font><br>Host: " + fVar.f().c();
        }
        D.a aVarG = fVar.g();
        if (aVarG != null && aVarG.e() != null && !aVarG.e().isEmpty()) {
            str = str + " Registered to: " + aVarG.e() + " " + aVarG.f();
        }
        if (fVar.f().h()) {
            str = str + "<br><font color=red>Currently Busy. To connect, another session must end.</font>";
        } else if (fVar.h() != null) {
            str = str + "<br>Local Project: " + fVar.h().a();
        }
        return str + "</center></html>";
    }
}
