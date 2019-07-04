package prateek.artmetio.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import prateek.artmetio.utils.CommonUtils;
import prateek.artmetio.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView ivBack;
    private Intent intent;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private View viewUserEmail,viewUserPassword;
    private TextView tvSignUpUser;
    private EditText etUserEmail;
    private EditText etUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context=SignUpActivity.this;
        mAuth = FirebaseAuth.getInstance();
        getIds();
        setListeners();
    }

    private void getIds() {
        TextView tvHeader = (TextView) findViewById(R.id.tvHeader);
        ivBack= (ImageView) findViewById(R.id.ivBack);
        tvSignUpUser= (TextView) findViewById(R.id.tvSignUpUser);
        etUserEmail=(EditText)findViewById(R.id.etUserEmail);
        etUserPassword=(EditText)findViewById(R.id.etUserPassword);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(context.getResources().getString(R.string.header_sign_up));
        viewUserEmail = (View) findViewById(R.id.viewUserEmail);
        viewUserPassword = (View) findViewById(R.id.viewUserPassword);


        etUserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etUserEmail.setTextColor(getResources().getColor(R.color.colorPrimary));
                viewUserEmail.setBackground(getResources().getDrawable(R.color.colorPrimary));
                etUserEmail.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.messtwo,0,0,0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etUserPassword.setTextColor(getResources().getColor(R.color.colorPrimary));
                viewUserPassword.setBackground(getResources().getDrawable(R.color.colorPrimary));
                etUserPassword.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.locktwo,0,0,0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setListeners() {
        ivBack.setOnClickListener(this);
        tvSignUpUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.tvSignUpUser:
                if(etUserEmail.getText().toString().trim().length()==0||etUserPassword.getText().toString().trim().length()==0)
                {
                    CommonUtils.showToast(context,"Please Enter email or password!");
                }
                else
                {
                    checkInter();
                }

                break;

            default:
                break;
        }
    }

    private void checkInter() {
        if (CommonUtils.isOnline(context))
        {
            SignUpUser();
        }
        else
        {
            CommonUtils.showToast(context,getString(R.string.please_check));
        }
    }

    private void SignUpUser() {
        String email = etUserEmail.getText().toString().trim();
        final String password = etUserPassword.getText().toString().trim();

        progressDialog = new ProgressDialog(context);

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        CommonUtils.showToast(context,"User Created Successfully!");
                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            CommonUtils.showToast(context,"Authentication failed." + task.getException());
                        } else {

                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
