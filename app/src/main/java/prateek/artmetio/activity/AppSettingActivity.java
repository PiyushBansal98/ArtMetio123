package prateek.artmetio.activity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import prateek.artmetio.R;

public class AppSettingActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvTermsCondition;
    private TextView tvPrivacy;
    private TextView tvPrivacyPolicy;
    private TextView tvHeader;
    private ImageView ivBack;
    private LinearLayout llTerms;
    private LinearLayout llPrivacy;
    private ImageView ivTCArrow;
    private ImageView ivPPArrow;
    private boolean flag_terms=false;
    private boolean flag_privacy=false;
    private Context context;
    private TextView tvTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);
        context=AppSettingActivity.this;
        getIdOfView();
        setListenerToView();
    }

    private void getIdOfView() {
        tvTerms= (TextView) findViewById(R.id.tvTerms);
        tvTermsCondition= (TextView) findViewById(R.id.tvTermsCondition);
        tvPrivacy= (TextView) findViewById(R.id.tvPrivacy);
        tvPrivacyPolicy= (TextView) findViewById(R.id.tvPrivacyPolicy);
        llTerms= (LinearLayout) findViewById(R.id.llTerms);
        llPrivacy= (LinearLayout) findViewById(R.id.llPrivacy);
        ivTCArrow= (ImageView) findViewById(R.id.ivTCArrow);
        ivPPArrow= (ImageView) findViewById(R.id.ivPPArrow);
        tvHeader= (TextView) findViewById(R.id.tvHeader);
        ivBack= (ImageView) findViewById(R.id.ivBack);
        assert ivBack != null;
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setVisibility(View.VISIBLE);
        tvHeader.setText(AppSettingActivity.this.getResources().getString(R.string.header_app_settings));

    }

    private void setListenerToView() {
        llTerms.setOnClickListener(this);
        llPrivacy.setOnClickListener(this);
        ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId())
        {

            case R.id.ivBack:
                onBackPressed();

                break;

            case R.id.llTerms:

                flag_terms=!flag_terms;

                if(flag_terms)
                {

                    tvTermsCondition.setVisibility(View.VISIBLE);
                    tvTermsCondition.setText(getResources().getString(R.string.terms));
                    ivTCArrow.setBackground(ContextCompat.getDrawable(AppSettingActivity.this,R.mipmap.down_arrow));

                }
                else {

                    tvTermsCondition.setVisibility(View.GONE);
                    ivTCArrow.setBackground(ContextCompat.getDrawable(AppSettingActivity.this,R.mipmap.right_arrow));
                }

                break;

            case R.id.llPrivacy:

                flag_privacy=!flag_privacy;

                if(flag_privacy)
                {
                    tvPrivacyPolicy.setVisibility(View.VISIBLE);
                    tvPrivacyPolicy.setText(getResources().getString(R.string.privacy));
                    ivPPArrow.setBackground(ContextCompat.getDrawable(AppSettingActivity.this,R.mipmap.down_arrow));
                }
                else {

                    tvPrivacyPolicy.setVisibility(View.GONE);
                    ivPPArrow.setBackground(ContextCompat.getDrawable(AppSettingActivity.this,R.mipmap.right_arrow));
                }

                break;

            default:

                break;
        }
    }
}
