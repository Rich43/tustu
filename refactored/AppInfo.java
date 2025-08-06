package az;

import java.awt.Window;

/**
 * Describes information about the running application.
 */
public interface AppInfo {
    /**
     * Edition of the application (for example "Lite!").
     */
    String getEdition();

    /**
     * Path where user data is stored.
     */
    String getDataDirectory();

    /**
     * Current registration key.
     */
    String getRegistrationKey();

    /**
     * Persist a new registration key.
     */
    void setRegistrationKey(String key);

    /**
     * Version information.
     */
    String getVersion();

    /**
     * Product name.
     */
    String getProductName();

    /**
     * Optional company name.
     */
    String getCompany();

    /**
     * Email address used for the license.
     */
    String getLicenseEmail();

    /**
     * Main application window.
     */
    Window getMainWindow();

    /**
     * Indicates whether the application is already activated.
     */
    boolean isRegistered();
}
