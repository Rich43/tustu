package R;

import bH.C;
import com.efianalytics.serviceclient.ReviewActions;

/* loaded from: TunerStudioMS.jar:R/e.class */
class e implements c {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f1794a;

    e(d dVar) {
        this.f1794a = dVar;
    }

    @Override // R.c
    public boolean a(l lVar) {
        try {
            a aVar = (a) lVar;
            String strSubmitUserReview = ReviewActions.submitUserReview(aVar.b(), aVar.c(), aVar.d(), aVar.e(), aVar.f(), aVar.g(), aVar.h(), aVar.i());
            C.c("Submit Message result:" + strSubmitUserReview);
            if (strSubmitUserReview != null) {
                if (strSubmitUserReview.equals("Thank you for your review")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e2) {
            C.d("unable to process UserReview Message. Exception:" + e2.getMessage());
            return false;
        }
    }
}
