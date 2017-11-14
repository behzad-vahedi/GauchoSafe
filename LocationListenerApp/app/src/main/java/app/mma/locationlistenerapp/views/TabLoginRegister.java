package app.mma.locationlistenerapp.views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import app.mma.locationlistenerapp.R;


public class TabLoginRegister extends LinearLayout implements View.OnClickListener {

    private static final int BG_ACTIVE = R.drawable.tab_btn_bg_selected;
    private static final int BG_INACTIVE = R.drawable.tab_layout_bg;
    private static final int TEXT_COLOR_ACTIVE = Color.WHITE;
    private static final int TEXT_COLOR_INACTIVE = Color.LTGRAY;

    Button btnLogin, btnRegister;
    View rootView;

    TabListener listener;
    private int activeIndex = 0;
    public TabLoginRegister(Context context) {
        super(context);
        init(context);
    }

    public TabLoginRegister(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        rootView = inflate(context, R.layout.tab_login_register, this);
        btnLogin = (Button) rootView.findViewById(R.id.btn_login);
        btnRegister = (Button) rootView.findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        setActiveButton(0);
    }


    @Override
    public void onClick(View v) {
        if(v.equals(btnLogin)){
            setActiveButton(0);
        } else if(v.equals(btnRegister)){
            setActiveButton(1);
        }
    }

    public void setActiveButton(int index){
        // 0 : login
        // 1 : register
        if(index == 0){
            btnLogin.setBackgroundResource(BG_ACTIVE);
            btnLogin.setTextColor(TEXT_COLOR_ACTIVE);
            btnRegister.setBackgroundResource(BG_INACTIVE);
            btnRegister.setTextColor(TEXT_COLOR_INACTIVE);
        } else if(index == 1) {
            btnLogin.setBackgroundResource(BG_INACTIVE);
            btnLogin.setTextColor(TEXT_COLOR_INACTIVE);
            btnRegister.setBackgroundResource(BG_ACTIVE);
            btnRegister.setTextColor(TEXT_COLOR_ACTIVE);
        } else{
            throw new RuntimeException("wrong index ");
        }

        activeIndex = index;
        if(listener != null){
            listener.onTabSelected(index, (index == 0) ? btnLogin : btnRegister);
        }
    }

    public int getActiveTabIndex(){
        return activeIndex;
    }

    public void setTabListener(TabListener listener){
        this.listener = listener;
    }

    public interface TabListener {
        public void onTabSelected(int index, View tab);
    }

}
