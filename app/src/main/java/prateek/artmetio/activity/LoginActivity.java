package prateek.artmetio.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import prateek.artmetio.utils.CommonUtils;
import prateek.artmetio.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvSignUp;
    private TextView tvLogin;
    private EditText etEmail;
    private EditText etPassword;
    private Context context;
    private View viewEmail,viewPassword;
    private TextView tvLoginFacebook;
    private CallbackManager callbackManager;

    private AccessToken accessToken;
    private Intent intent;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private com.google.android.gms.common.SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    String personName;
    String personPhoto;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=LoginActivity.this;
        FacebookSdk.sdkInitialize(context);
        AppEventsLogger.activateApp(context);
        getIds();
        setListener();

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etEmail.setTextColor(getResources().getColor(R.color.colorPrimary));
                viewEmail.setBackground(getResources().getDrawable(R.color.colorPrimary));
                etEmail.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.messtwo,0,0,0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etPassword.setTextColor(getResources().getColor(R.color.colorPrimary));
                viewPassword.setBackground(getResources().getDrawable(R.color.colorPrimary));
                etPassword.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.locktwo,0,0,0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //popup sign in   // this is for the gmail integration
        gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }/**OnConnectionFailedListener*/)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)  // google signin options
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {

                    //User signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                }
                else
                {   //user is signed out
                    Log.d(TAG, "OnAuthStateChanged:sign:signed_out");
                }
            }
        };

        //Hash Key
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "prateek.artmetio",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void getIds() {
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.etPassword);
        tvLogin=(TextView)findViewById(R.id.tvLogin);
        tvSignUp=(TextView)findViewById(R.id.tvSignUp);
        tvLoginFacebook=(TextView)findViewById(R.id.tvLoginFacebook);
        viewEmail = (View) findViewById(R.id.viewEmail);
        viewPassword = (View) findViewById(R.id.viewPassword);
        signInButton = (com.google.android.gms.common.SignInButton)findViewById(R.id.signInButton);

    }

    private void setListener() {
        tvLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvLoginFacebook.setOnClickListener(this);
        signInButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId())
        {
            case R.id.tvLogin:

                if(etEmail.getText().toString().trim().length()==0||etPassword.getText().toString().trim().length()==0)
                {
                    CommonUtils.showToast(context,"Please Enter email or password!");
                }
                else
                {
                    checkInter();
                }

                break;

            case R.id.tvLoginFacebook:
                if (CommonUtils.isOnline(context))
                {
                    loginWithFacebook();
                }
                else
                {
                    CommonUtils.showToast(context,getString(R.string.please_check));
                }
                break;

            case R.id.signInButton:
                if (CommonUtils.isOnline(context))
                {
                    signIn();
                }
                else
                {
                    CommonUtils.showToast(context,getString(R.string.please_check));
                }
                break;

            case R.id.tvSignUp:
                intent = new Intent(context, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }
    }

    private void checkInter() {
        if (CommonUtils.isOnline(context))
        {
            loginWithFireBase();
        }
        else
        {
            CommonUtils.showToast(context,getString(R.string.please_check));
        }
    }

    private void loginWithFireBase() {
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        progressDialog = new ProgressDialog(context);

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Logging You In...");
        progressDialog.show();

        //authenticate user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            CommonUtils.showToast(context,"Please Fill Correct Details!");

                        }
                        else
                        {    // there was an error
                            CommonUtils.savePreferencesBoolean(context, CommonUtils.IS_LOGIN,true);
                            CommonUtils.saveStringPreferences(context,CommonUtils.USER_NAME,etEmail.getText().toString().trim());
                            CommonUtils.saveStringPreferences(context,CommonUtils.USER_IMAGE, String.valueOf((R.mipmap.user_img)).toString().trim());
                            intent = new Intent(context, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);  //sign in popup
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void loginWithFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("loginActivity", "LoginManager FacebookCallback onSuccess");
                accessToken = AccessToken.getCurrentAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        String facebookuserID = response.getJSONObject().optString("id");
                        String fbUserEmail = response.getJSONObject().optString("email");
                        String fbUserName = response.getJSONObject().optString("name");
//                        AppConstants.fbUserGender = response.getJSONObject().optString("gender");
                        String fbUserImage  = ("https://graph.facebook.com/" + facebookuserID + "/picture?type=large").toString();
                        String birthday = response.getJSONObject().optString("birthday");
                       /* PreferencesUtils.saveStringPreferences(context,AppConstants.FACEBOOK_ID,facebookuserID);
                        PreferencesUtils.saveStringPreferences(context,AppConstants.EMAIL_ID,fbUserEmail);*/
                        CommonUtils.saveStringPreferences(context,CommonUtils.FB_USER_NAME,fbUserName);
                        CommonUtils.saveStringPreferences(context,CommonUtils.FB_IMAGE,fbUserImage);
                        CommonUtils.savePreferencesBoolean(context, CommonUtils.IS_LOGIN,true);

                        CommonUtils.showToast(context,"Logging you In....");
                        intent=new Intent(context,HomeActivity.class);
                        startActivity(intent);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,name,gender,email,picture.width(200),birthday,friends{name}");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                Toast.makeText(getApplication(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplication(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //Result returned from launching the Intent from GoogleSignApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {   mGoogleApiClient.clearDefaultAccountAndReconnect();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            if (result.isSuccess())
            {   //Google Sign In Was successful, Authenticate with firebase
                GoogleSignInAccount account = result.getSignInAccount();
                CommonUtils.showToast(context,"Logging you In....");
                CommonUtils.savePreferencesBoolean(context, CommonUtils.IS_LOGIN,true);
                userData(result);
                firebaseAuthWithGoogle(account);
            }
            else
            {
                CommonUtils.showToast(context,"Unable to connect!");
                //Google Sign Failed, update UI appropriately
            }
        }
        else
        { if(requestCode!= 0)
           {
            callbackManager.onActivityResult(requestCode, resultCode, data);
           }
        }
    }

    private void userData(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            if (acct != null) {
                personName = acct.getDisplayName().toString();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                personPhoto = acct.getPhotoUrl().toString();
                CommonUtils.saveStringPreferences(context,CommonUtils.G_USER_NAME,personName);
                CommonUtils.saveStringPreferences(context,CommonUtils.G_IMAGE,personPhoto);

            }
        }
        else
        {
            // Signed out, show unauthenticated UI.
        }


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle: " + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());



                        intent=new Intent(context,HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        //If sign in fails, display a message to the user.

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "SignInWithCredential" + task.getException());
                            CommonUtils.showToast(context,"Authentication Failed.");
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
