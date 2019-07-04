package prateek.artmetio.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import prateek.artmetio.utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import prateek.artmetio.utils.CommonUtils;
import prateek.artmetio.utils.Constants;
import prateek.artmetio.R;
import prateek.artmetio.models.Upload;

public class AddItemsActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_IMAGE_REQUEST = 100;
    private ImageView ivBack;
    private Context context;
    private TextView tvChoose;
    private TextView tvUpload;
    private EditText etAdminPrice;
    private ImageView ivAdmin;
    private Uri filePath;
    private Intent intent;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        context=AddItemsActivity.this;
        getIdOfView();
        setListenerToView();
    }

    private void getIdOfView() {
        TextView tvHeader = (TextView) findViewById(R.id.tvHeader);
        ivBack= (ImageView) findViewById(R.id.ivBack);

        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(context.getResources().getString(R.string.header_add_item));

        tvChoose=(TextView)findViewById(R.id.tvChoose);
        tvUpload=(TextView)findViewById(R.id.tvUpload);
        etAdminPrice=(EditText)findViewById(R.id.etAdminPrice);
        ivAdmin=(ImageView)findViewById(R.id.ivAdmin);
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

    }

    private void setListenerToView() {
        ivBack.setOnClickListener(this);
        tvChoose.setOnClickListener(this);
        tvUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.tvChoose:
                if(CommonUtils.checkPermissionStorage((Activity) context))
                {
                    showFileChooser();
                }
                else
                {
                    CommonUtils.requestPermissionStorage((Activity) context);
                }
                break;

            case R.id.tvUpload:
                if (CommonUtils.isOnline(context))
                {
                    if(etAdminPrice.getText().toString().trim().length()==0)
                    {
                        CommonUtils.showToast(context,"Please Enter Price");
                    }
                    else
                    {
                        uploadFile();
                    }
                }
                else
                {
                    CommonUtils.showToast(context,getString(R.string.please_check));
                }

                break;
        }
    }

    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Adding Item");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //creating the upload object to store uploaded image details
                            Upload upload = new Upload(etAdminPrice.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());

                            //adding an upload to firebase database
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);

                            //displaying success toast
                            CommonUtils.showToast(context,"Item Added Successfully!");
                            intent=new Intent(context,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Adding Item " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivAdmin.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
