package com.efiAnalytics.ui;

import java.awt.Color;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fz.class */
public class fz {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f11716a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    private Color f11717b = Color.WHITE;

    public Color a(double d2) {
        Color colorA = a();
        Color colorB = null;
        double dA = 0.5d;
        int i2 = 0;
        while (true) {
            if (i2 >= this.f11716a.size()) {
                break;
            }
            if (d2 < ((fA) this.f11716a.get(i2)).f11619b) {
                colorB = ((fA) this.f11716a.get(i2)).b();
                if (i2 > 0) {
                    colorA = ((fA) this.f11716a.get(i2 - 1)).b();
                    dA = (d2 - ((fA) this.f11716a.get(i2 - 1)).a()) / (((fA) this.f11716a.get(i2)).a() - ((fA) this.f11716a.get(i2 - 1)).a());
                }
            } else {
                i2++;
            }
        }
        return colorB == null ? ((fA) this.f11716a.get(this.f11716a.size() - 1)).b() : new Color((int) ((colorB.getRed() * dA) + (colorA.getRed() * (1.0d - dA))), (int) ((colorB.getGreen() * dA) + (colorA.getGreen() * (1.0d - dA))), (int) ((colorB.getBlue() * dA) + (colorA.getBlue() * (1.0d - dA))));
    }

    public void a(double d2, Color color) {
        fA fAVar = new fA(this);
        fAVar.a(d2);
        fAVar.a(color);
        if (this.f11716a.size() <= 0) {
            this.f11716a.add(fAVar);
            return;
        }
        for (int size = this.f11716a.size(); size > 0; size++) {
            if (d2 > ((fA) this.f11716a.get(size - 1)).f11619b) {
                this.f11716a.add(size, fAVar);
                return;
            }
        }
    }

    public Color a() {
        return this.f11717b;
    }

    public static fz a(Color color) {
        fz fzVar = new fz();
        fzVar.a(0.0d, color);
        fzVar.a(8.0d, Color.yellow);
        fzVar.a(50.0d, Color.GREEN);
        fzVar.a(200.0d, Color.GREEN.darker());
        return fzVar;
    }
}
