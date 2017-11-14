package app.mma.locationlistenerapp.views;


import android.content.Context;
import android.util.AttributeSet;

import app.mma.locationlistenerapp.config.Config;
import app.mma.locationlistenerapp.views.ValueSelector;

public class IntervalValueSelector extends ValueSelector{


    public IntervalValueSelector(Context context) {
        super(context);
        init();
    }

    public IntervalValueSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setMinValue(Config.DEFAULT_INT);
    }


    @Override
    protected void incrementValue() {
        int value = getValue();
        value += 10;
        value -= (value % 10);
        if(value == 0){
            value = Config.DEFAULT_INT;
        }
        setValue(value);
    }

    @Override
    protected void decrementValue() {
        int value = getValue();
        value -= 10;
        value -= (value % 10);
        if(value == 0){
            value = Config.DEFAULT_INT;
        }
        setValue(value);
    }



}
