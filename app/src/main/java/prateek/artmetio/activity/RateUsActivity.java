package prateek.artmetio.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import prateek.artmetio.utils.CommonUtils;
import prateek.artmetio.R;

public class RateUsActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView ivBack;
    private Context context;
    private TextView tvSatisfiedItem;
    private TextView tvSubmit;
    private TextView tvRateItem;
    private RadioGroup rgSatisfiedUnsatisfied;
    private RadioButton rbSatisfied;
    private RadioButton rbUnsatisfied;
    private RatingBar rbSatisfaction;
    private String satisfied="";
    private String unSatisfied="";
    private Intent intent;
    private String owneritemID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
        context=RateUsActivity.this;
        getIdOfView();
        setListenerToView();
        rgSatisfiedUnsatisfied.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==rbSatisfied.getId()){

                    satisfied="Satisfied";

                } else if(checkedId==rbUnsatisfied.getId()){

                    unSatisfied="Unsatisfied";
                }
            }
        });
    }

    private void getIdOfView() {
        TextView tvHeader = (TextView) findViewById(R.id.tvHeader);
        ivBack= (ImageView) findViewById(R.id.ivBack);

        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(context.getResources().getString(R.string.header_rate_us));

        tvSatisfiedItem=(TextView)findViewById(R.id.tvSatisfiedItem);
        tvRateItem=(TextView)findViewById(R.id.tvRateItem);
        tvSubmit=(TextView)findViewById(R.id.tvSubmit);

        rgSatisfiedUnsatisfied=(RadioGroup) findViewById(R.id.rgSatisfiedUnsatisfied);

        rbSatisfied=(RadioButton) findViewById(R.id.rbSatisfied);
        rbUnsatisfied=(RadioButton) findViewById(R.id.rbUnsatisfied);

        rbSatisfaction=(RatingBar)findViewById(R.id.rbSatisfaction);

    }

    private void setListenerToView() {
        ivBack.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvSubmit:
                CommonUtils.showToast(context,"Rating Submitted!");
                intent=new Intent(context,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
