package com.efiAnalytics.apps.ts.dashboard;

import G.C0113cs;
import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;
import javafx.fxml.FXMLLoader;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/Z.class */
public class Z implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private String f9426a = "";

    /* renamed from: b, reason: collision with root package name */
    private Color f9427b = null;

    /* renamed from: c, reason: collision with root package name */
    private Color f9428c = null;

    /* renamed from: d, reason: collision with root package name */
    private String f9429d = "";

    /* renamed from: e, reason: collision with root package name */
    private String f9430e = "";

    /* renamed from: f, reason: collision with root package name */
    private Component[] f9431f = null;

    /* renamed from: g, reason: collision with root package name */
    private boolean f9432g = true;

    /* renamed from: h, reason: collision with root package name */
    private boolean f9433h = false;

    /* renamed from: i, reason: collision with root package name */
    private double f9434i = 16.0d;

    /* renamed from: j, reason: collision with root package name */
    private double f9435j = 9.0d;

    public Color a() {
        return this.f9427b;
    }

    public void a(Color color) {
        this.f9427b = color;
    }

    public String b() {
        return this.f9429d;
    }

    public void a(String str) {
        if (str == null || str.equals("") || str.equals(FXMLLoader.NULL_KEYWORD)) {
            return;
        }
        this.f9429d = str;
    }

    public Component[] c() {
        return this.f9431f;
    }

    public void a(Component[] componentArr) {
        this.f9431f = componentArr;
    }

    public String d() {
        return this.f9426a;
    }

    public void b(String str) {
        this.f9426a = str;
    }

    public String e() {
        return this.f9430e;
    }

    public void c(String str) {
        this.f9430e = str;
    }

    public void d(String str) {
        if (this.f9431f != null) {
            for (int i2 = 0; i2 < this.f9431f.length; i2++) {
                if (this.f9431f[i2] instanceof AbstractC1420s) {
                    AbstractC1420s abstractC1420s = (AbstractC1420s) this.f9431f[i2];
                    if (abstractC1420s.getEcuConfigurationName() != null && !abstractC1420s.getEcuConfigurationName().equals(C0113cs.f1154a)) {
                        abstractC1420s.setEcuConfigurationName(str);
                    }
                }
            }
        }
    }

    public boolean f() {
        return this.f9432g;
    }

    public void a(boolean z2) {
        this.f9432g = z2;
    }

    public Color g() {
        return this.f9428c;
    }

    public void b(Color color) {
        this.f9428c = color;
    }

    public boolean h() {
        return this.f9433h;
    }

    public void b(boolean z2) {
        this.f9433h = z2;
    }

    public double i() {
        return this.f9434i;
    }

    public void a(double d2) {
        this.f9434i = d2;
    }

    public double j() {
        return this.f9435j;
    }

    public void b(double d2) {
        this.f9435j = d2;
    }
}
