package it.gmaglio.sportsmanage.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import it.gmaglio.sportsmanage.R;
import it.gmaglio.sportsmanage.dialog.SignInDialog;

public class LoginActivity extends AbstractSportsManagerActivity implements FacebookCallback<LoginResult>, OnCompleteListener<AuthResult>, View.OnClickListener {


    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN_GOOGLE = 45;
    private final int RC_SIGN_IN_FACEBOOK = 44;
    private FirebaseAuth firebaseAuth;
    private SignInButton signInButtonGoogle;
    private Intent secureActivity;
    private CallbackManager facebookCallBackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebookInit();
        googleInit();

        findViewById(R.id.signInWithEmail).setOnClickListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        secureActivity = new Intent(this,MainActivity.class);

       // checkUserAuthenticated();

    }

    private void checkUserAuthenticated () {


        if(firebaseAuth.getCurrentUser() != null ){
             Toast.makeText(this, "Utente già loggato", Toast.LENGTH_SHORT).show();
           secureActivity.putExtra("authUser",firebaseAuth.getCurrentUser());
           startActivity(secureActivity);
         }else {
           Toast.makeText(this, "Utente non loggato", Toast.LENGTH_SHORT).show();
         }
    }

    private void facebookInit(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        facebookCallBackManager = CallbackManager.Factory.create();
        LoginButton facebookLoginButton =  findViewById(R.id.login_facebook_button);
        facebookLoginButton.setReadPermissions("email","public_profile");
        facebookLoginButton.registerCallback(facebookCallBackManager, this);

    }

    private void googleInit(){
        GoogleSignInOptions  gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient  = GoogleSignIn.getClient(this, gso);
        signInButtonGoogle = findViewById(R.id.signInGoogle);
        signInButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicSignInWithGoogle();
            }
        });
    }



    public void publicSignInWithGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallBackManager.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case RC_SIGN_IN_GOOGLE:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResultGoogle(task);
                break;

        }
    }

    private void handleSignInResultGoogle(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(this, "Login OK", Toast.LENGTH_SHORT).show();
            firebaseAuthWithGoogle(account);

        } catch (ApiException e) {
            Log.w(CLASS_TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }





    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(LoginActivity.class.getCanonicalName(), "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, this);
    }

    //GOOGLE LISTENER

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Log.d(CLASS_TAG, "signInWithCredential:success");
            FirebaseUser user = firebaseAuth.getCurrentUser();
            secureActivity.putExtra("authUser",firebaseAuth.getCurrentUser());
            startActivity(secureActivity);
        } else {
            Log.w(CLASS_TAG, "signInWithCredential:failure", task.getException());
        }

    }



 //facebook listner
    @Override
    public void onSuccess(LoginResult loginResult) {
        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, this);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.signInWithEmail :
                SignInDialog.showSignInDialog(getSupportFragmentManager());
                break;
        }
    }
}

