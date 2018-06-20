package com.rupinderjeet.textswitchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TextSwitchLayout extends FrameLayout {

    private static final String TAG = TextSwitchLayout.class.getSimpleName();
    private Context context;

    private TextView onTextView;
    private TextView offTextView;
    private SwitchCompat switchCompat;

    private String onText = "ON";
    private String offText = "OFF";

    private int switchOnTrackColor;
    private int switchOffTrackColor;

    private CompoundButton.OnCheckedChangeListener checkedChangeListener;

    public TextSwitchLayout(Context context) {
        super(context);
        init(context, null);
    }

    public TextSwitchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextSwitchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /*
     * init
     */

    private void init (final Context context, AttributeSet attrs) {

        this.context = context;

        // default colors
        switchOnTrackColor = ContextCompat.getColor(context, R.color.ts_track);
        switchOffTrackColor = ContextCompat.getColor(context, R.color.ts_track_off);

        consumeAttributes(attrs);

        inflate(context, R.layout.text_switch_layout, this);

        onTextView = findViewById(R.id.ts_on_view);
        offTextView = findViewById(R.id.ts_off_view);
        switchCompat = findViewById(R.id.ts_switch);

        int thumbWidth = switchCompat.getThumbDrawable().getIntrinsicWidth();
        onTextView.setPadding(0, 0, thumbWidth, 0);
        offTextView.setPadding(thumbWidth, 0, 0, 0);

        onTextView.setText(onText);
        offTextView.setText(offText);

        updateTextVisibility(switchCompat.isChecked());
        updateTrackColor(switchCompat.isChecked());
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                updateTextVisibility(isChecked);
                updateTrackColor(isChecked);

                if (checkedChangeListener != null) {
                    checkedChangeListener.onCheckedChanged(buttonView, isChecked);
                }
            }
        });
    }

    private void consumeAttributes (AttributeSet attrs) {

        if (attrs == null) {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextSwitchLayout);

        onText = typedArray.getString(R.styleable.TextSwitchLayout_ts_text_on);
        offText = typedArray.getString(R.styleable.TextSwitchLayout_ts_text_off);

        switchOnTrackColor = typedArray.getColor(
                R.styleable.TextSwitchLayout_ts_on_track_color,
                ContextCompat.getColor(context, R.color.ts_track)
        );

        switchOffTrackColor = typedArray.getColor(
                R.styleable.TextSwitchLayout_ts_off_track_color,
                ContextCompat.getColor(context, R.color.ts_track_off)
        );

        typedArray.recycle();
    }

    private void updateTextVisibility (boolean isSwitchOn) {

        onTextView.setVisibility(isSwitchOn ? View.VISIBLE : View.GONE);
        offTextView.setVisibility(isSwitchOn ? View.GONE : View.VISIBLE);
    }

    private void updateTrackColor (boolean isSwitchOn) {

        switchCompat.getTrackDrawable()
                .setColorFilter(
                        isSwitchOn ? switchOnTrackColor : switchOffTrackColor,
                        PorterDuff.Mode.SRC_ATOP
                );
    }

    public TextSwitchLayout setCheckedChangeListener(
            CompoundButton.OnCheckedChangeListener checkedChangeListener) {

        this.checkedChangeListener = checkedChangeListener;
        return this;
    }
}
