package cn.adlin.ad.tools;

import cn.adlin.ad.changewopic.R;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

public class EditTextWithAdd extends EditText {
    private final static String TAG = "EditTextWithAdd";
    private Drawable addIcon;
    private Drawable delIcon;
    private Context mContext;
    private EditTextWithAddInterFace baseEditTextWithAddInterFace;

    public EditTextWithAdd(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public void setActivity(
            EditTextWithAddInterFace baseEditTextWithAddInterFace) {
        this.baseEditTextWithAddInterFace = baseEditTextWithAddInterFace;
    }

    public EditTextWithAdd(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public EditTextWithAdd(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        addIcon = mContext.getResources().getDrawable(R.mipmap.add);
        addIcon.setBounds(-11, 0, 70, 70);
        delIcon = mContext.getResources().getDrawable(R.mipmap.delete_gray);
        delIcon.setBounds(-11, 0, 70, 70);
        // setDrawable();
        // setCompoundDrawables(null, null, addIcon, null);
        // setCompoundDrawablesWithIntrinsicBounds(null, null, dateIcon, null);
        // this.setEnabled(false);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    // ����ɾ��ͼƬ
    private void setDrawable() {
        if (length() < 1)
            setCompoundDrawablesWithIntrinsicBounds(null, null, addIcon, null);
        else
            setCompoundDrawablesWithIntrinsicBounds(null, null, delIcon, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (addIcon != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 81;
            if (rect.contains(eventX, eventY)) {
                if (baseEditTextWithAddInterFace != null) {
                    baseEditTextWithAddInterFace.addButtonListener();
                }
            }
        }
        return super.onTouchEvent(event);
    }

}
