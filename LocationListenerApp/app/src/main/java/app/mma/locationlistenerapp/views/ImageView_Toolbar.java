package app.mma.locationlistenerapp.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


public class ImageView_Toolbar extends AppCompatImageView {

    public ImageView_Toolbar(Context context) {
        super(context);
    }

    public ImageView_Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView_Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight() / 2;
        setMeasuredDimension(height, height);
    }
}
