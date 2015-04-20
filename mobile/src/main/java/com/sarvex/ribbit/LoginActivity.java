package com.sarvex.ribbit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;


public class LoginActivity extends Activity {

    @InjectView(R.id.loginButton)
    Button loginButton;

    @InjectView(R.id.signupButton)
    Button signupButton;

    @InjectView(R.id.userNameField)
    EditText usernameField;

    @InjectView(R.id.passwordField)
    EditText passwordField;

    @InjectView(R.id.emailField)
    EditText emailField;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.reset(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        loginButton.setEnabled(false);
        signupButton.setEnabled(false);
    }

    @OnEditorAction(R.id.userNameField)
    public boolean enableLogin() {
        loginButton.setEnabled(true);
        return true;
    }

    @OnEditorAction(R.id.emailField)
    public boolean enableSignup() {
        boolean status = (emailField.getText().toString().trim().length() > 0);
        loginButton.setEnabled(!status);
        signupButton.setEnabled(status);
        return status;
    }

    @OnClick(R.id.loginButton)
    public void login() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setMessage(R.string.login_error_message)
                    .setTitle(R.string.login_error_title)
                    .setPositiveButton(android.R.string.ok, null).create();
        } else {
            setProgressBarIndeterminateVisibility(true);
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException exception) {
                    setProgressBarIndeterminateVisibility(false);
                    if (user != null) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    } else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setMessage(exception.getMessage())
                                .setTitle(R.string.login_error_title)
                                .setPositiveButton(android.R.string.ok, null).create();
                    }
                }
            });
        }
    }

    @OnClick(R.id.signupButton)
    public void signup() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String email = emailField.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {

            new AlertDialog.Builder(LoginActivity.this)
                    .setMessage(R.string.signup_error_message)
                    .setTitle(R.string.signup_error_title)
                    .setPositiveButton(android.R.string.ok, null).create();

        } else {
            // create a new user
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException exception) {
                    if (exception == null) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    } else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setMessage(exception.getMessage())
                                .setTitle(R.string.signup_error_title)
                                .setPositiveButton(android.R.string.ok, null).create();
                    }
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
