package app.mma.locationlistenerapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import app.mma.locationlistenerapp.R;
import app.mma.locationlistenerapp.views.TabLoginRegister;
import app.mma.locationlistenerapp.fragments.LoginFragment;
import app.mma.locationlistenerapp.fragments.RegisterFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginRegisterActivity extends AppCompatActivity implements TabLoginRegister.TabListener, RegisterFragment.RegisterCallback, LoginFragment.LoginCallback {

    TabLoginRegister tabs;
    ViewPager viewpager;

    LoginFragment fLogin;
    RegisterFragment fRegister;
    MyPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabs = (TabLoginRegister) findViewById(R.id.tabs);
        tabs.setTabListener(this);
        fLogin = new LoginFragment();
        fRegister = new RegisterFragment();
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fLogin, fRegister);
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabs.setActiveButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        changeStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



    @Override
    public void onTabSelected(int index, View tab) {
        if(index < viewpager.getChildCount()){
            viewpager.setCurrentItem(index);
        }
    }

    private void changeStatusBarColor(int color){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    @Override
    public void onRegister() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        Fragment[] fragments;
        public MyPagerAdapter(FragmentManager fm, Fragment frag0, Fragment frag1){
            super(fm);
            fragments = new Fragment[]{frag0, frag1};
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 2;
        }
    }



}
