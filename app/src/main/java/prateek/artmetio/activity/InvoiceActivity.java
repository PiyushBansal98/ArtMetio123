package prateek.artmetio.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import prateek.artmetio.utils.CommonUtils;
import prateek.artmetio.R;
import prateek.artmetio.models.Upload;

public class InvoiceActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView ivBack;
    private ImageView ivRight;
    private Context context;
    private int position;
    private String quantity;
    private List<Upload> uploads;
    private TextView invName;
    private TextView invQuant;
    private TextView invPrice;
    private TextView invSubTotal;
    private TextView invSubTotalT;
    private TextView invGst;
    private TextView invTotal;
    private ImageView ivInvoiceItems;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        context=InvoiceActivity.this;
        getIds();
        setListeners();

        if(CommonUtils.getPreferences(context,CommonUtils.FB_USER_NAME)!=null && !CommonUtils.getPreferences(context,CommonUtils.FB_USER_NAME).equalsIgnoreCase(""))
        {
            invName.setText(CommonUtils.getPreferences(context,CommonUtils.FB_USER_NAME).toString().trim());
        }
        else if(CommonUtils.getPreferences(context,CommonUtils.G_USER_NAME)!=null && !CommonUtils.getPreferences(context,CommonUtils.G_USER_NAME).equalsIgnoreCase(""))
        {
            invName.setText(CommonUtils.getPreferences(context,CommonUtils.G_USER_NAME).toString().trim());
        }
        else if(CommonUtils.getPreferences(context,CommonUtils.USER_NAME)!=null && !CommonUtils.getPreferences(context,CommonUtils.USER_NAME).equalsIgnoreCase(""))
        {
            invName.setText(CommonUtils.getPreferences(context,CommonUtils.USER_NAME).toString().trim());
        }
        else
        {
            invName.setText("Name Error");
        }

    }

    private void getIds() {
        TextView tvHeader = (TextView) findViewById(R.id.tvHeader);
        ivBack= (ImageView) findViewById(R.id.ivBack);
        ivRight= (ImageView) findViewById(R.id.ivRight);
        ivInvoiceItems= (ImageView) findViewById(R.id.ivInvoiceItems);

        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.mipmap.download);
        tvHeader.setText(context.getResources().getString(R.string.header_invoice));

        invName=(TextView)findViewById(R.id.invName);
        invQuant=(TextView)findViewById(R.id.invQuant);
        invPrice=(TextView)findViewById(R.id.invPrice);
        invSubTotal=(TextView)findViewById(R.id.invSubTotal);
        invSubTotalT=(TextView)findViewById(R.id.invSubTotalT);
        invGst=(TextView)findViewById(R.id.invGst);
        invTotal=(TextView)findViewById(R.id.invTotal);

        uploads = (List<Upload>) getIntent().getSerializableExtra("uploads");
        position = getIntent().getIntExtra("position", 0);
        quantity = getIntent().getStringExtra("quantity");

        invQuant.setText(quantity);
        invPrice.setText(String.valueOf(uploads.get(position).getName()));
        invSubTotal.setText(String.valueOf(Integer.parseInt(quantity)* Integer.parseInt(String.valueOf(uploads.get(position).getName()))));
        invSubTotalT.setText(String.valueOf(Integer.parseInt(quantity)* Integer.parseInt(String.valueOf(uploads.get(position).getName()))));

        //GST
        double amount = Double.parseDouble(String.valueOf((18*(Integer.parseInt(quantity)* Integer.parseInt(String.valueOf(uploads.get(position).getName()))))/100));
        double res = (amount / 100.0f) * 100;
        invGst.setText(String.valueOf(res));

        int total=(Integer.parseInt(quantity)* Integer.parseInt(String.valueOf(uploads.get(position).getName())))+(int)res ;
        StringBuilder str = new StringBuilder("Rs. ");
        str.append(String.valueOf(total));
        invTotal.setText(str);
        Picasso.with(context).load(String.valueOf(uploads.get(position).getUrl().toString().trim()))
                .placeholder(R.mipmap.pic)
                .error(R.mipmap.pic)
                .into(ivInvoiceItems);

    }

    private void setListeners() {
        ivBack.setOnClickListener(this);
        ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ivRight:
                if(CommonUtils.isOnline(context)) {
                    new PdfGenerationTask().execute();
                    intent =new Intent(context,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    CommonUtils.showToast(context,getString(R.string.please_check));
                }
                break;
        }
    }


    private class PdfGenerationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            PdfDocument document = new PdfDocument();

            // repaint the user's text into the page
            View content =(LinearLayout)findViewById(R.id.lay_inv);

            // crate a page description
            int pageNumber = 1;
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getWidth(),
                    content.getHeight() - 20, pageNumber).create();

            // create a new page from the PageInfo
            PdfDocument.Page page = document.startPage(pageInfo);

            content.draw(page.getCanvas());

            // do final processing of the page
            document.finishPage(page);

            SimpleDateFormat sdf = new SimpleDateFormat(" d MMM yyyy hh:mm aaa");
            String pdfName = "ArtMetio" + sdf.format(Calendar.getInstance().getTime()) + ".pdf";


            File outputFile = new File(context.getExternalFilesDir("Invoices"), pdfName);

            try {
                outputFile.createNewFile();
                OutputStream out = new FileOutputStream(outputFile);
                document.writeTo(out);
                document.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return outputFile.getPath();
        }

        @Override
        protected void onPostExecute(String filePath) {
            if (filePath != null) {
                CommonUtils.showToast(context,"Pdf saved at " + filePath);

            } else {
                CommonUtils.showToast(context,"Error in Pdf creation" + filePath);
            }

        }

    }
}
