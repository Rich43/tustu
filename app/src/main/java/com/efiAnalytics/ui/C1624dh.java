package com.efiAnalytics.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;

/* renamed from: com.efiAnalytics.ui.dh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dh.class */
class C1624dh extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    long f11418a = 0;

    /* renamed from: b, reason: collision with root package name */
    StringBuilder f11419b = new StringBuilder(20);

    /* renamed from: c, reason: collision with root package name */
    String[] f11420c = null;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ C1621de f11421d;

    C1624dh(C1621de c1621de) {
        this.f11421d = c1621de;
    }

    private int a() {
        if (this.f11420c == null) {
            this.f11420c = new String[this.f11421d.getModel().getSize()];
            for (int i2 = 0; i2 < this.f11420c.length; i2++) {
                this.f11420c[i2] = this.f11421d.getModel().getElementAt(i2).toString().toLowerCase();
            }
        }
        for (int i3 = 0; i3 < this.f11420c.length; i3++) {
            if (this.f11420c[i3].startsWith(this.f11419b.toString())) {
                return i3;
            }
        }
        return -1;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        int iSelectionForKey;
        int i2 = 0;
        switch (keyEvent.getKeyCode()) {
            case 10:
                return;
            case 33:
                i2 = -5;
                break;
            case 34:
                i2 = 5;
                break;
            case 38:
                i2 = -1;
                break;
            case 40:
                i2 = 1;
                break;
            default:
                long jCurrentTimeMillis = System.currentTimeMillis();
                char lowerCase = Character.toLowerCase(keyEvent.getKeyChar());
                if (jCurrentTimeMillis - this.f11418a <= 1500) {
                    this.f11419b.append(lowerCase);
                    int iA = a();
                    if (iA != -1) {
                        this.f11421d.setSelectedIndex(iA);
                        break;
                    }
                } else {
                    this.f11419b.setLength(0);
                    this.f11419b.append(lowerCase);
                    this.f11418a = jCurrentTimeMillis;
                    break;
                }
                break;
        }
        int selectedIndex = this.f11421d.getSelectedIndex();
        if (i2 != 0) {
            keyEvent.consume();
            iSelectionForKey = selectedIndex + i2;
            if (iSelectionForKey < 0) {
                iSelectionForKey = 0;
            } else if (iSelectionForKey >= this.f11421d.getItemCount()) {
                iSelectionForKey = this.f11421d.getItemCount() - 1;
            }
            this.f11421d.setSelectedIndex(iSelectionForKey);
        } else {
            iSelectionForKey = this.f11421d.getKeySelectionManager().selectionForKey(keyEvent.getKeyChar(), this.f11421d.getModel());
        }
        if (iSelectionForKey == -1 || iSelectionForKey != selectedIndex) {
            SwingUtilities.invokeLater(new RunnableC1625di(this));
        }
    }
}
