package prateek.artmetio.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import prateek.artmetio.utils.CircleTransformation;
import prateek.artmetio.utils.CommonUtils;
import prateek.artmetio.R;
import prateek.artmetio.fragment.FragmentDrawer;
import prateek.artmetio.fragment.HomeFragment;

public class HomeActivity extends AppCompatActivity  implements FragmentDrawer.FragmentDrawerListener,View.OnClickListener{
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private Fragment fragment;
    private Context context;
    private TextView tvAppHeader;
    private Intent intent;
    private ImageView ivProfileImage;
    private TextView tvProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getIdOfView();
        setListenerToView();
        drawerSettings();
        context=HomeActivity.this;
        try
        {
            EventBus.getDefault().register(this);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(CommonUtils.isOnline(context))
        {
            displayView(0);
            Picasso.with(context).load(R.mipmap.user_img).error(R.mipmap.user_img).transform(new CircleTransformation()).into(ivProfileImage);

        }
        else
        {
            CommonUtils.showToast(context,context.getString(R.string.please_check));
        }


        //Set Names and Image on Drawer
        if(CommonUtils.getPreferences(context,CommonUtils.USER_NAME)!=null && !CommonUtils.getPreferences(context,CommonUtils.USER_NAME).equalsIgnoreCase(""))
        {
            tvProfileName.setText(CommonUtils.getPreferences(context,CommonUtils.USER_NAME).toString().trim());
            Picasso.with(context).load(CommonUtils.getPreferences(context,CommonUtils.USER_IMAGE).toString().trim())
                    .error(R.mipmap.user_img)
                    .transform(new CircleTransformation())
                    .into(ivProfileImage);
        }
        else if(CommonUtils.getPreferences(context,CommonUtils.FB_USER_NAME)!=null && !CommonUtils.getPreferences(context,CommonUtils.FB_USER_NAME).equalsIgnoreCase(""))
        {
            tvProfileName.setText(CommonUtils.getPreferences(context,CommonUtils.FB_USER_NAME).toString().trim());
            Picasso.with(context).load(CommonUtils.getPreferences(context,CommonUtils.FB_IMAGE).toString().trim())
                    .error(R.mipmap.pic)
                    .transform(new CircleTransformation())
                    .into(ivProfileImage);
        }
        else if(CommonUtils.getPreferences(context,CommonUtils.G_USER_NAME)!=null && !CommonUtils.getPreferences(context,CommonUtils.G_USER_NAME).equalsIgnoreCase(""))
        {
            tvProfileName.setText(CommonUtils.getPreferences(context,CommonUtils.G_USER_NAME).toString().trim());
            Picasso.with(context).load(CommonUtils.getPreferences(context,CommonUtils.G_IMAGE).toString().trim())
                    .error(R.mipmap.pic)
                    .transform(new CircleTransformation())
                    .into(ivProfileImage);
        }
        else
        {
            tvProfileName.setText("Name Error");
            Picasso.with(context).load(R.mipmap.pic)
                    .error(R.mipmap.pic)
                    .transform(new CircleTransformation())
                    .into(ivProfileImage);

        }

    }

    private void getIdOfView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbars);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tvAppHeader = (TextView) findViewById(R.id.tvAppHeader);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvProfileName = (TextView) findViewById(R.id.tvProfileName);

    }

    private void setListenerToView() {

    }


    private void drawerSettings() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FragmentDrawer drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, mToolbar);
        drawerFragment.setDrawerListener(this);
    }
    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {

        Log.e("Position","Position   "+position);
        switch (position)
        {
            case 0:
                fragment = new HomeFragment();
                CommonUtils.setFragment(fragment, true, (FragmentActivity) context,R.id.container_body);
                tvAppHeader.setText(context.getResources().getString(R.string.app_name));
                break;

            case 1:
                intent=new Intent(HomeActivity.this,AddItemsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case 2:
                intent=new Intent(HomeActivity.this,RateUsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case 3:
                intent=new Intent(HomeActivity.this,AppSettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case 4:
                CommonUtils.showDialogLogout((Activity) context);
                break;

            default:
                onBackPressed();
                break;

        }

    }

    @Override
    public void onBackPressed() {
        CommonUtils.exitDialog((Activity) context);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
