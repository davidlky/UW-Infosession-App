package co.mrdavidliu.uwinfosession;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * TODO: document your custom view class.
 */
public class FlatCalendar extends LinearLayout {
//    private String mExampleString; // TODO: use a default from R.string...
//    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
//    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
//    private Drawable mExampleDrawable;

//    private TextPaint mTextPaint;
//    private float mTextWidth;
//    private float mTextHeight;
    GridView gridView;

    public FlatCalendar(Context context) {
        super(context);
        init(null, 0);
    }

    public FlatCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FlatCalendar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LinearLayout main = new LinearLayout(this.getContext());
        main.setOrientation(VERTICAL);
        main.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout b = new LinearLayout(this.getContext());
        b.setOrientation(HORIZONTAL);
        b.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        String[] days = new String[]{
                "Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"
        };
        for(String day : days){
            TextView textView = new TextView(getContext());
            textView.setText(day.substring(0,3));
            textView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            textView.setPadding(5,10,5,10);
            b.addView(textView);
        }
        TextView title = new TextView(this.getContext());
        title.setText("Month");
        title.setTextSize(18f);
        title.setPadding(0, 20, 0, 20);
        main.addView(title);
        main.addView(b);
        this.addView(main);
        gridView.setNumColumns(7);
        gridView.setLayoutParams(new GridLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 240)));
        gridView.setAdapter(new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });
        this.addView(gridView);
        updateCalendar();
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.FlatCalendar, defStyle, 0);

//        mExampleString = a.getString(
//                R.styleable.FlatCalendar_exampleString);
//        mExampleColor = a.getColor(
//                R.styleable.FlatCalendar_exampleColor,
//                mExampleColor);
//        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
//        // values that should fall on pixel boundaries.
//        mExampleDimension = a.getDimension(
//                R.styleable.FlatCalendar_exampleDimension,
//                mExampleDimension);
//
//        if (a.hasValue(R.styleable.FlatCalendar_exampleDrawable)) {
//            mExampleDrawable = a.getDrawable(
//                    R.styleable.FlatCalendar_exampleDrawable);
//            mExampleDrawable.setCallback(this);
//        }
//
//        a.recycle();
//
//        // Set up a default TextPaint object
//        mTextPaint = new TextPaint();
//        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
//        mTextPaint.setTextSize(mExampleDimension);
//        mTextPaint.setColor(mExampleColor);
//        mTextWidth = mTextPaint.measureText(mExampleString);
//
//        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
//        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
//        canvas.drawText(mExampleString,
//                paddingLeft + (contentWidth - mTextWidth) / 2,
//                paddingTop + (contentHeight + mTextHeight) / 2,
//                mTextPaint);
//
//        // Draw the example drawable on top of the text.
//        if (mExampleDrawable != null) {
//            mExampleDrawable.setBounds(paddingLeft, paddingTop,
//                    paddingLeft + contentWidth, paddingTop + contentHeight);
//            mExampleDrawable.draw(canvas);
//        }
    }

    private void updateCalendar()
    {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = new GregorianCalendar(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH);

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells (42 days calendar as per our business logic)
        while (cells.size() < calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
//
//    /**
//     * Gets the example string attribute value.
//     *
//     * @return The example string attribute value.
//     */
//    public String getExampleString() {
//        return mExampleString;
//    }
//
//    /**
//     * Sets the view's example string attribute value. In the example view, this string
//     * is the text to draw.
//     *
//     * @param exampleString The example string attribute value to use.
//     */
//    public void setExampleString(String exampleString) {
//        mExampleString = exampleString;
//        invalidateTextPaintAndMeasurements();
//    }
//
//    /**
//     * Gets the example color attribute value.
//     *
//     * @return The example color attribute value.
//     */
//    public int getExampleColor() {
//        return mExampleColor;
//    }
//
//    /**
//     * Sets the view's example color attribute value. In the example view, this color
//     * is the font color.
//     *
//     * @param exampleColor The example color attribute value to use.
//     */
//    public void setExampleColor(int exampleColor) {
//        mExampleColor = exampleColor;
//        invalidateTextPaintAndMeasurements();
//    }
//
//    /**
//     * Gets the example dimension attribute value.
//     *
//     * @return The example dimension attribute value.
//     */
//    public float getExampleDimension() {
//        return mExampleDimension;
//    }
//
//    /**
//     * Sets the view's example dimension attribute value. In the example view, this dimension
//     * is the font size.
//     *
//     * @param exampleDimension The example dimension attribute value to use.
//     */
//    public void setExampleDimension(float exampleDimension) {
//        mExampleDimension = exampleDimension;
//        invalidateTextPaintAndMeasurements();
//    }
//
//    /**
//     * Gets the example drawable attribute value.
//     *
//     * @return The example drawable attribute value.
//     */
//    public Drawable getExampleDrawable() {
//        return mExampleDrawable;
//    }
//
//    /**
//     * Sets the view's example drawable attribute value. In the example view, this drawable is
//     * drawn above the text.
//     *
//     * @param exampleDrawable The example drawable attribute value to use.
//     */
//    public void setExampleDrawable(Drawable exampleDrawable) {
//        mExampleDrawable = exampleDrawable;
//    }
}
