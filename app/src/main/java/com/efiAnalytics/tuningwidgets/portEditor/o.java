package com.efiAnalytics.tuningwidgets.portEditor;

import G.R;
import G.aM;
import bH.W;
import org.icepdf.core.util.PdfOps;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/o.class */
class o {

    /* renamed from: d, reason: collision with root package name */
    private String f10580d;

    /* renamed from: a, reason: collision with root package name */
    R f10581a;

    /* renamed from: b, reason: collision with root package name */
    aM f10582b;

    /* renamed from: e, reason: collision with root package name */
    private int f10583e;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ OutputPortEditor f10584c;

    public o(OutputPortEditor outputPortEditor, R r2, String str, String str2, int i2) {
        this.f10584c = outputPortEditor;
        this.f10580d = "";
        this.f10581a = null;
        this.f10582b = null;
        this.f10583e = 0;
        this.f10580d = str2;
        this.f10581a = r2;
        this.f10582b = r2.c(str);
        this.f10583e = i2;
    }

    public String a() {
        return this.f10580d;
    }

    public boolean equals(Object obj) {
        return obj instanceof String ? a().equals(obj) : super.equals(obj);
    }

    public boolean b() {
        try {
            return this.f10582b.i(this.f10581a.h())[this.f10583e][0] != 0.0d;
        } catch (V.g e2) {
            bH.C.a("Failed to getValue for " + this.f10582b.aJ() + ", index = " + this.f10583e, e2, null);
            return false;
        }
    }

    public String toString() {
        return C1818g.b(W.b(this.f10580d, PdfOps.DOUBLE_QUOTE__TOKEN, "")) + " ";
    }
}
