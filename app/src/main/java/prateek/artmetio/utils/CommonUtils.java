package prateek.artmetio.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import prateek.artmetio.R;
import prateek.artmetio.activity.LoginActivity;

public class CommonUtils {

    public static final int PERMISSION_REQUEST_CODE=100;

    public final static String IS_LOGIN="is_login";

    public final static String USER_NAME="user_name";
    public final static String USER_IMAGE="user_image";

    public final static String FB_USER_NAME="user_name";
    public final static String FB_IMAGE="user_image";

    public final static String G_USER_NAME="user_name";
    public final static String G_IMAGE="user_image";


    public static void savePreferencesBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public static boolean getPreferencesBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, false);
    }


    public static String getPreferences(Context context, String key) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");

    }

    public static void saveStringPreferences(Context context, String key, String value) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {

            return false;
        }
        return true;
    }

    public static   void requestPermissionStorage( Activity activity){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)|| ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }

    public static  boolean checkPermissionStorage(Activity context){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result  == PackageManager.PERMISSION_GRANTED){

            if(result1 == PackageManager.PERMISSION_GRANTED){
                return true;
            }else {
                return false;
            }


        } else {
            return false;

        }
    }

    public static void showDialogLogout(final Activity context) {


        final String TAG="dialogSorryCall_dialog";

        TextView tvMessage,tvYes,tvNo;

        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog mDialog = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.75f;
        mDialog.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow();
        View dialoglayout = inflater.inflate(R.layout.dialog_logout, null);
        mDialog.setContentView(dialoglayout);
        tvMessage = (TextView) mDialog.findViewById(R.id.tvMessage);
        tvYes = (TextView) mDialog.findViewById(R.id.tvYes);
        tvNo = (TextView) mDialog.findViewById(R.id.tvNo);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                CommonUtils.savePreferencesBoolean(context, CommonUtils.IS_LOGIN,false);
                Intent  intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                context.finish();

            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.dismiss();

            }
        });

        mDialog.show();
    }



    public static void exitDialog(final Activity context) {
        TextView tvYes,tvNo,tvMsg;
        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog mDialog = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);
//        mDialog.getWindow().getAttributes().windowAnimations = R.anim;
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.75f;
        mDialog.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow();

        View dialoglayout = inflater.inflate(R.layout.dialog_logout, null);
        mDialog.setContentView(dialoglayout);


        tvMsg = (TextView) mDialog.findViewById(R.id.tvMessage);
        tvYes = (TextView) mDialog.findViewById(R.id.tvYes);
        tvNo = (TextView) mDialog.findViewById(R.id.tvNo);
        tvMsg.setText(R.string.exit_from_app);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.dismiss();
                context.moveTaskToBack(true);

            }
        });
        mDialog.show();
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    public static void setFragment(Fragment fragment, boolean removeStack, FragmentActivity activity, int mContainer) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();
        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ftTransaction.replace(mContainer, fragment);
        } else {
            ftTransaction.replace(mContainer, fragment);
            ftTransaction.addToBackStack(null);
        }
        ftTransaction.commit();
    }

    public static void setFragmentChild(Fragment fragment, boolean removeStack, FragmentActivity activity, int mContainer, FragmentManager fragmentManagerr) {

        FragmentManager fragmentManager = fragmentManagerr;
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();

        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ftTransaction.replace(mContainer, fragment);
        } else {
            ftTransaction.replace(mContainer, fragment);
            ftTransaction.addToBackStack(null);
        }

        ftTransaction.commit();

    }


}
