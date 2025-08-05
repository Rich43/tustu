package R;

import bH.C;
import com.efianalytics.serviceclient.ReviewActions;
import com.efianalytics.serviceclient.TranslationActions;
import com.efianalytics.translation.MutationResult;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:R/m.class */
public class m {
    public boolean a(String str, String str2, String str3, String str4, String str5, String str6, int i2, String str7) throws k {
        try {
            b(str, str2, str3, str4, str5, str6, i2, str7);
            return true;
        } catch (k e2) {
            Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            try {
                g.a().a(new a(str, str2, str3, str4, str5, str6, i2, str7));
                return false;
            } catch (i e3) {
                Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                throw new k("Failed to submit service call or store for later.");
            }
        }
    }

    public boolean a(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        try {
            b(str, str2, str3, str4, str5, str6, str7, str8, str9);
            return true;
        } catch (k e2) {
            Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            try {
                g.a().a(new j(str, str2, str3, str4, str5, str6, str7, str8, str9));
                return false;
            } catch (i e3) {
                Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                throw new k("Failed to submit service call, store for later failed as well.");
            }
        }
    }

    protected void b(String str, String str2, String str3, String str4, String str5, String str6, int i2, String str7) throws k {
        try {
            ReviewActions.submitUserReview(str, str2, str3, str4, str5, str6, i2, str7);
        } catch (Exception e2) {
            throw new k("BasicUserReview Service un available." + e2.getMessage());
        }
    }

    protected void b(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) throws k {
        try {
            MutationResult mutationResultAddUpdateProposedStaticTranslation = TranslationActions.addUpdateProposedStaticTranslation(str, str2, str3, str4, str5, str6, str7, str8, str9);
            if (mutationResultAddUpdateProposedStaticTranslation.getReturnCode() != 0) {
                C.b("Proposed Translation call failed. Message: " + mutationResultAddUpdateProposedStaticTranslation.getLongMessage());
                throw new k("Proposed Translation call failed. Short Message: " + mutationResultAddUpdateProposedStaticTranslation.getShortMessage() + "\nLong Message:" + mutationResultAddUpdateProposedStaticTranslation.getLongMessage());
            }
        } catch (Exception e2) {
            throw new k("Translation Service unavailable." + e2.getMessage());
        }
    }
}
