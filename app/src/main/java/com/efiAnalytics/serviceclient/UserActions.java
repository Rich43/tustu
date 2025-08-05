package com.efianalytics.serviceclient;

import com.efianalytics.userprofile.AuthenticationResponse;
import com.efianalytics.userprofile.RoleToken;
import com.efianalytics.userprofile.UserChangeResult;
import com.efianalytics.userprofile.UserProfile;
import com.efianalytics.userprofile.UserServices;
import com.efianalytics.userprofile.UserServicesService;
import java.util.List;

/* loaded from: efiaServicesClient.jar:com/efianalytics/serviceclient/UserActions.class */
public class UserActions {
    public static UserProfile getUserProfile(String userId, String password) {
        UserServicesService service = new UserServicesService();
        UserServices port = service.getUserServicesPort();
        return port.getUserProfile(userId, password);
    }

    public static AuthenticationResponse authenticate(String userId, String password) {
        UserServicesService service = new UserServicesService();
        UserServices port = service.getUserServicesPort();
        return port.authenticate(userId, password);
    }

    public static UserChangeResult createUser(String userId, String email, String firstName, String lastName, String middleInitial) {
        UserServicesService service = new UserServicesService();
        UserServices port = service.getUserServicesPort();
        return port.createUser(userId, email, firstName, lastName, middleInitial);
    }

    public static List<RoleToken> getAccessTokens(String userId, String password) {
        UserServicesService service = new UserServicesService();
        UserServices port = service.getUserServicesPort();
        return port.getAccessTokens(userId, password);
    }

    public static UserChangeResult updateUserProfile(String userId, String password, String newPassword, String email, boolean sendEmail) {
        UserServicesService service = new UserServicesService();
        UserServices port = service.getUserServicesPort();
        return port.updateUserProfile(userId, password, newPassword, email, sendEmail);
    }

    public static AuthenticationResponse authenticateSession(String sessionId) {
        UserServicesService service = new UserServicesService();
        UserServices port = service.getUserServicesPort();
        return port.authenticateSession(sessionId);
    }

    public static UserChangeResult resetPassword(String userIdOrEmail) {
        UserServicesService service = new UserServicesService();
        UserServices port = service.getUserServicesPort();
        return port.resetPassword(userIdOrEmail);
    }

    public static Boolean isUserIdInUse(String userId) {
        UserServicesService service = new UserServicesService();
        UserServices port = service.getUserServicesPort();
        return port.isUserIdInUse(userId);
    }
}
