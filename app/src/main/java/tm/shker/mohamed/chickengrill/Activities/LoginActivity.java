package tm.shker.mohamed.chickengrill.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import tm.shker.mohamed.chickengrill.Managers.AppManager;
import tm.shker.mohamed.chickengrill.Managers.Constants;
import tm.shker.mohamed.chickengrill.Objects.User;
import tm.shker.mohamed.chickengrill.R;

import com.facebook.FacebookSdk;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private ImageView logo;
    private ProgressDialog dialog;
    public SignInButton btnGoogleLogin;
    public LoginButton btnFacebookLogin;
    static GoogleApiClient mGoogleApiClient;
    private CallbackManager callBackManager;
    private TextInputLayout emailWrapper;
    private TextInputLayout passWrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        logo = (ImageView) findViewById(R.id.logo);
        emailWrapper = (TextInputLayout) findViewById(R.id.TilEmailWrapper);
        passWrapper = (TextInputLayout) findViewById(R.id.TilPassWrapper);
        emailWrapper.setHint("כתובת דואר אלקטרוני:");
        passWrapper.setHint("סיסמה:");
        passWrapper.setPasswordVisibilityToggleDrawable(R.drawable.ic_action_eye_open);
        passWrapper.setPasswordVisibilityToggleTintList(ColorStateList.valueOf(Color.rgb(245,94,24)));
        prepareFacebookBTN();
        prepareGoogleBTN();
        Picasso.with(this).load(R.drawable.chicken_gril_logo).error(R.mipmap.ic_launcher).into(logo);

    }

    private void prepareFacebookBTN() {
        btnFacebookLogin = (LoginButton) findViewById(R.id.btnFacebookLogin);
        assert btnFacebookLogin != null;
        btnFacebookLogin.setReadPermissions("email", "public_profile");
        callBackManager = CallbackManager.Factory.create();
        btnFacebookLogin.registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showProgress("Logging you in","Please wait");
                String token = loginResult.getAccessToken().getToken();
                AuthCredential credential = FacebookAuthProvider.getCredential(token);

                FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        saveUser(authResult);
                        gotoMain();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError(e, btnFacebookLogin);
                    }
                });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                showError(error, btnFacebookLogin);
            }
        });
    }

    private void prepareGoogleBTN() {
        btnGoogleLogin = (SignInButton) findViewById(R.id.btnGoogleLogin);
        assert btnGoogleLogin != null;
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithGoogle();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, null /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void loginWithGoogle() {
        showProgress("Logging you in","Please wait");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Constants.RC_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_GOOGLE_SIGN_IN && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();
            assert account != null;
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    saveUser(authResult);
                    gotoMain();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showError(e, btnGoogleLogin);
                }
            });
        } else {
            callBackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void login(final View view) {
        if (!validate()) {
            return;
        }
        showProgress("Logging you in","Please wait");
        FirebaseAuth.getInstance().
                signInWithEmailAndPassword(getEmail(), getPassword())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        gotoMain();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError(e, view);
                    }
                });

    }

    public void register(final View view) {
        if (!validate()) {
            return;
        }
        showProgress("Logging you in","Please wait");
        FirebaseAuth.getInstance().
                createUserWithEmailAndPassword(getEmail(), getPassword()).
                addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        saveUser(authResult);
                        gotoMain();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError(e, view);
                    }
                });
    }

    private void saveUser(AuthResult authResult) {
        String uid = authResult.getUser().getUid();
        User u = new User(authResult.getUser().getEmail());
        FirebaseDatabase.getInstance().getReference().
                child("Users").child(uid).setValue(u);
    }

    private boolean validate() {
        boolean isValid = true;
        if (getEmail() == null || getEmail().isEmpty()) {
            etEmail.setError("אנא הכנס כתובת מייל לבחירתך");
            isValid = false;
        } else if (getEmail() != null && !getEmail().contains("@")) {
            etEmail.setError("אנא הכנס מייל בתבנית מתאימה");
        }
        if (getPassword() == null || getPassword().length() < 6) {
            etPassword.setError("הסיסמא אמורה להכיל לפחות 6 תווים");
            isValid = false;
        }
        return isValid;
    }

    private void showProgress(String title, String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    private void hideProgress() {
        if (dialog != null)
            dialog.dismiss();
    }

    private void showError(Exception e, View view) {
        hideProgress();
        Snackbar.make(view,
                e.getLocalizedMessage(),
                Snackbar.LENGTH_INDEFINITE
        ).setAction("dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }

    private void gotoMain() {
        hideProgress();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);
    }

    public String getEmail() {
        return etEmail.getText().toString();
    }

    public String getPassword() {
        return etPassword.getText().toString();
    }

    public void resetPass(View view) {
        String email = getEmail();
        if(email != null && !email.isEmpty()) {
            showProgress("Please wait", " ");
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    hideProgress();
                    Toast.makeText(getApplicationContext(), "נשלח מייל שחזור סיסמה לכתובת המייל שהכנסת", Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(this, "אנא הכנס כתובת דואר אלקטרוני לצורך שחזור הסיסמה...", Toast.LENGTH_LONG).show();
        }
    }
}