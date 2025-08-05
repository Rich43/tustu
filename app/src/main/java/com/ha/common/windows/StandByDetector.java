package com.ha.common.windows;

import javax.swing.JFrame;
import javax.swing.JLabel;

/* loaded from: TunerStudioMS.jar:com/ha/common/windows/StandByDetector.class */
public class StandByDetector {

    /* renamed from: a, reason: collision with root package name */
    private b f11797a;

    public StandByDetector(b bVar) {
        this.f11797a = bVar;
        init();
    }

    private native boolean init();

    public native void setAllowStandby(boolean z2);

    public static void main(String[] strArr) {
        new StandByDetector(new a()).setAllowStandby(false);
        JFrame jFrame = new JFrame();
        jFrame.getContentPane().add(new JLabel("close to end test"));
        jFrame.setSize(300, 100);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(3);
    }

    static {
        System.loadLibrary("StandByDetector");
    }
}
