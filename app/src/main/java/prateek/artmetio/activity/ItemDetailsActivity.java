package prateek.artmetio.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import prateek.artmetio.utils.CommonUtils;
import prateek.artmetio.R;
import prateek.artmetio.models.Upload;

public class ItemDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView ivBack;
    private Context context;
    private int position;
    private List<Upload> uploads;
    private ImageView ivItem;
    private TextView tvItemPrice;
    private TextView tvminus;
    private TextView tvNumber;
    private TextView tvplus;
    private TextView tvBuy;
    private int number;
    private int number1;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        context=ItemDetailsActivity.this;
        getIds();
        setListeners();

    }

    private void getIds() {
        TextView tvHeader = (TextView) findViewById(R.id.tvHeader);
        ivBack= (ImageView) findViewById(R.id.ivBack);
        ivItem= (ImageView) findViewById(R.id.ivItem);
        tvItemPrice= (TextView) findViewById(R.id.tvItemPrice);
        tvminus= (TextView) findViewById(R.id.tvminus);
        tvminus.setClickable(false);
        tvNumber= (TextView) findViewById(R.id.tvNumber);
        tvplus= (TextView) findViewById(R.id.tvplus);
        tvBuy= (TextView) findViewById(R.id.tvBuy);

        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(context.getResources().getString(R.string.header_item_details));
        uploads = (List<Upload>) getIntent().getSerializableExtra("uploads");
        position = getIntent().getIntExtra("position", 0);

        // CommonUtils.showToast(context,String.valueOf(uploads.get(position).getUrl().toString()));
        tvItemPrice.setText(String.valueOf(uploads.get(position).getName()));
        Picasso.with(context).load(String.valueOf(uploads.get(position).getUrl().toString().trim()))
                .placeholder(R.mipmap.pic)
                .error(R.mipmap.pic)
                .into(ivItem);

    }

    private void setListeners() {
        ivBack.setOnClickListener(this);
        tvminus.setOnClickListener(this);
        tvplus.setOnClickListener(this);
        tvBuy.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.tvminus:
                View tempview = (View) tvminus.getTag(R.integer.btn_minus_view);
                TextView tv = (TextView)findViewById(R.id.tvNumber);
                number = Integer.parseInt(tv.getText().toString()) - 1;
                setTextNumber(number);
                tv.setText(String.valueOf(number));
                break;

            case R.id.tvplus:
                View tempview1 = (View) tvplus.getTag(R.integer.btn_plus_view);
                TextView tv1 = (TextView)findViewById(R.id.tvNumber);
                number1 = Integer.parseInt(tv1.getText().toString()) + 1;
                setTextNo(number1);
                tv1.setText(String.valueOf(number1));
                break;

            case R.id.tvBuy:
                if(CommonUtils.isOnline(context))
                {
                    intent=new Intent(context, InvoiceActivity.class);
                    intent.putExtra("uploads", (Serializable) uploads);
                    intent.putExtra("position", position);
                    intent.putExtra("quantity",tvNumber.getText().toString());
                    startActivity(intent);
                    finish();
                }
                else
                {
                    CommonUtils.showToast(context,context.getString(R.string.please_check));
                }
                break;
        }
    }

    private void setTextNo(int number1) {
        if(number1>=0)
        {   tvminus.setClickable(true);
        }
        else
        {
            tvminus.setClickable(false);
        }

    }

    private void setTextNumber(int number) {

        if (number==0)
        {
            tvminus.setClickable(false);
        }
        else
        {   tvminus.setClickable(true);
            //CommonUtils.showToast(context,"abcd");
        }

    }
}
