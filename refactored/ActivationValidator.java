package az;

import java.util.Date;

/**
 * Validates activation responses returned by the activation server. The
 * original application performed extensive hardware checks; this
 * implementation mirrors the observed behaviour from the obfuscated
 * code sufficiently for offline activation.
 */
public class ActivationValidator {
    private static final ActivationValidator INSTANCE = new ActivationValidator();

    /** Local identifiers taken from the activation request. */
    private ActivationRequest localRequest;

    public static ActivationValidator getInstance() {
        return INSTANCE;
    }

    private ActivationValidator() {
    }

    /**
     * Set the identifiers used when validating activation data.
     */
    public void setLocalRequest(ActivationRequest request) {
        this.localRequest = request;
    }

    /**
     * Validate the provided activation data.
     */
    public ActivationResult validate(ActivationData data) {
        ActivationResult result = new ActivationResult();
        if (data == null) {
            return result; // defaults to invalid activation data
        }

        int errorCode = data.getErrorCode();
        if (errorCode == 0 || errorCode == 1) {
            if (localRequest == null) {
                result.setCode(4);
                result.setMessage("No identifiers available.");
                return result;
            }

            boolean haveId = false;
            boolean match = false;

            String hw = localRequest.getHardwareId();
            if (hw != null && !hw.isEmpty()) {
                haveId = true;
                match = hw.equals(data.getHardwareId());
            }

            String mb = localRequest.getMotherboardId();
            if (!match && mb != null && !mb.isEmpty()) {
                haveId = true;
                match = mb.equals(data.getMotherboardId());
            }

            String dev = localRequest.getDeviceId();
            if (!match && dev != null && !dev.isEmpty()) {
                haveId = true;
                match = dev.equals(data.getDeviceId());
            }

            if (!haveId) {
                result.setCode(4);
                result.setMessage("No identifiers available.");
                return result;
            }
            if (!match) {
                result.setCode(2);
                result.setMessage("Invalid Activation.");
                return result;
            }

            if (data.getRenewalDate().before(new Date())) {
                result.setCode(1);
            } else {
                result.setCode(0);
            }
            result.setMessage("Valid Activation.");
            result.setData(data);
        } else if (errorCode == 5) {
            result.setCode(5);
            result.setMessage("Current Activation Count: " + data.getActivationCount());
            result.setData(data);
        } else if (errorCode == 6) {
            result.setCode(6);
            result.setMessage(data.getMessage());
        } else {
            result.setCode(2);
            result.setMessage("Invalid activation data.");
        }
        return result;
    }
}
