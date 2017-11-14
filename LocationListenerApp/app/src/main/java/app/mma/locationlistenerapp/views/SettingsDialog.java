package app.mma.locationlistenerapp.views;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.beardedhen.androidbootstrap.BootstrapButton;

import app.mma.locationlistenerapp.R;
import app.mma.locationlistenerapp.config.Config;

public class SettingsDialog extends Dialog {

    IntervalValueSelector intervalSelector;
    BootstrapButton btnDone;
    SharedPreferences prefs;

    public SettingsDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_settings);
        intervalSelector = (IntervalValueSelector) findViewById(R.id.interval_value_selector);
        btnDone = (BootstrapButton) findViewById(R.id.btn_submit);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        intervalSelector.setCallbacks(new ValueSelector.Callbacks() {
            @Override
            public void onValueChanged(ValueSelector selector, int value) {
                prefs.edit().putInt(Config.INTERVAL_SECONDS, value).apply();
            }
        });
        intervalSelector.setValue(prefs.getInt(Config.INTERVAL_SECONDS, Config.DEFAULT_INT));
    }


    @Override
    public void show() {
        changeSize();
        intervalSelector.setValue(prefs.getInt(Config.INTERVAL_SECONDS, Config.DEFAULT_INT));
        super.show();
    }



    private void changeSize(){
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        Window window = getWindow();
        if(window != null){
            window.setLayout(
                    metrics.widthPixels * 95 / 100,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}
