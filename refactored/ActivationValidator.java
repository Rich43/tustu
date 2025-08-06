package az;

import java.util.Date;

/**
 * Minimal activation validator used by the refactored dialog.  The original
 * project performed extensive hardware checks.  Here we simply ensure the
 * activation has not expired.
 */
public class ActivationValidator {
    private static final ActivationValidator INSTANCE = new ActivationValidator();

    public static ActivationValidator getInstance() {
        return INSTANCE;
    }

    private ActivationValidator() {
    }

    /**
     * Validate the provided activation data.
     */
    public ActivationResult validate(ActivationData data) {
        ActivationResult result = new ActivationResult();
        if (data.getRenewalDate().before(new Date())) {
            result.setCode(2);
            result.setMessage("Invalid activation data.");
        } else {
            result.setCode(0);
            result.setMessage("Valid Activation.");
            result.setData(data);
        }
        return result;
    }
}
