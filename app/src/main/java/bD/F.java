package bD;

import com.efiAnalytics.remotefileaccess.RemoteFileDescriptor;
import com.efiAnalytics.ui.eJ;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/* loaded from: TunerStudioMS.jar:bD/F.class */
public class F extends AbstractTableModel {

    /* renamed from: a, reason: collision with root package name */
    public static int f6614a = 0;

    /* renamed from: b, reason: collision with root package name */
    public static int f6615b = 1;

    /* renamed from: c, reason: collision with root package name */
    public static int f6616c = 2;

    /* renamed from: d, reason: collision with root package name */
    public static int f6617d = 3;

    /* renamed from: e, reason: collision with root package name */
    public static int f6618e = 4;

    /* renamed from: f, reason: collision with root package name */
    String[] f6619f = {"", "Name", "Date", Constants._ATT_TYPE, "Size"};

    /* renamed from: g, reason: collision with root package name */
    Icon f6620g = null;

    /* renamed from: h, reason: collision with root package name */
    List f6621h = new ArrayList();

    public void a(List list) {
        this.f6621h.addAll(list);
        super.fireTableDataChanged();
    }

    public void a(RemoteFileDescriptor remoteFileDescriptor) {
        this.f6621h.remove(remoteFileDescriptor);
    }

    public RemoteFileDescriptor a(int i2) {
        return (RemoteFileDescriptor) this.f6621h.get(i2);
    }

    @Override // javax.swing.table.TableModel
    public int getRowCount() {
        return this.f6621h.size();
    }

    @Override // javax.swing.table.TableModel
    public int getColumnCount() {
        return this.f6619f.length;
    }

    @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public String getColumnName(int i2) {
        return this.f6619f[i2];
    }

    @Override // javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        if (i2 >= this.f6621h.size()) {
            return null;
        }
        RemoteFileDescriptor remoteFileDescriptor = (RemoteFileDescriptor) this.f6621h.get(i2);
        switch (i3) {
            case 0:
                return b(remoteFileDescriptor);
            case 1:
                return remoteFileDescriptor.getName();
            case 2:
                return new Date(remoteFileDescriptor.getLastModified());
            case 3:
                return a(remoteFileDescriptor.getName());
            case 4:
                return Long.valueOf(remoteFileDescriptor.getSize());
            default:
                return "";
        }
    }

    private String a(String str) {
        return str == null ? "" : str.toUpperCase().endsWith(".MS3") ? "MS3 SD Log" : str.toUpperCase().endsWith(".FRD") ? "Formatted Raw Data" : str.toUpperCase().endsWith(".MSL") ? "MegaSquirt Log" : str.toUpperCase().endsWith(".MLG") ? "MLG Data Log" : str.toUpperCase().endsWith(".CSV") ? "CSV Data Log" : str.lastIndexOf(".") != -1 ? str.substring(str.lastIndexOf(".") + 1) : "Other";
    }

    private Icon b(RemoteFileDescriptor remoteFileDescriptor) {
        if (this.f6620g == null) {
            this.f6620g = new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logIcon.gif"))));
        }
        return this.f6620g;
    }

    @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public void setValueAt(Object obj, int i2, int i3) {
    }

    public void a() {
        this.f6621h.clear();
        super.fireTableDataChanged();
    }
}
