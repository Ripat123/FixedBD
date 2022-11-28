package com.sbitbd.fixed_bd.ui.login_page.data;

import android.content.Context;

import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.ui.login_page.data.model.LoggedInUser;
import com.sbitbd.fixed_bd.ui.login_page.ui.login.LoginActivity;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    LoginActivity loginActivity = new LoginActivity();
    DoConfig config = new DoConfig();
    private static Result<LoggedInUser> loggedInUserResult;

    public Result<LoggedInUser> login(String username, String password, Context context) {


        try {
            // TODO: handle loggedInUser authentication

//            loginActivity.login(username,password,context);



//            LoggedInUser fakeUser =
//                    new LoggedInUser(
//                            java.util.UUID.randomUUID().toString(),
//                            "");
            return loggedInUserResult;

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
    public void check() {
        try {
            // TODO: handle loggedInUser authentication

            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "");
            loggedInUserResult = new Result.Success<>(fakeUser);

        } catch (Exception e) {
            loggedInUserResult = new Result.Error(new IOException("Error logging in", e));
        }
    }
    public void checkE() {
        try {
            // TODO: handle loggedInUser authentication

            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "failed");
            loggedInUserResult = new Result.Success<>(fakeUser);

        } catch (Exception e) {
            loggedInUserResult = new Result.Error(new IOException("Error logging in", e));
        }
    }

}