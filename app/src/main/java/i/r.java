package I;

import G.C0113cs;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:I/r.class */
class r extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ q f1412a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    r(q qVar) {
        super("ClockPublisher_" + Math.random());
        this.f1412a = qVar;
        super.setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Calendar calendar = Calendar.getInstance();
        while (true) {
            int i2 = calendar.get(11);
            int i3 = calendar.get(12);
            int i4 = calendar.get(13);
            C0113cs.a().a("hourOfDay", i2);
            C0113cs.a().a("minuteOfHour", i3);
            C0113cs.a().a("secondOfMinute", i4);
            try {
                sleep((int) (1000 - (System.currentTimeMillis() - calendar.getTimeInMillis())));
            } catch (InterruptedException e2) {
                Logger.getLogger(q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
    }
}
