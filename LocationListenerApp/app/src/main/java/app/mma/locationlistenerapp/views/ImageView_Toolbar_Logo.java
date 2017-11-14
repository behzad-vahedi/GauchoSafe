package app.mma.locationlistenerapp.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;


public class ImageView_Toolbar_Logo extends AppCompatImageView {

    TypedValue typedValue = new TypedValue();
    public ImageView_Toolbar_Logo(Context context) {
        super(context);
    }

    public ImageView_Toolbar_Logo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView_Toolbar_Logo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
//        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true))
//        {
//            height = TypedValue.complexToDimensionPixelSize(
//                    typedValue.data, getResources().getDisplayMetrics());
//        }
        setMeasuredDimension(height, height);
    }

}
