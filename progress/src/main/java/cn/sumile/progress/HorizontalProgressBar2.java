package cn.sumile.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Created by Administrator on 2016/6/22.
 */
public class HorizontalProgressBar2 extends ProgressBar {
    private static final int DEFAULT_TEXT_SIZE = 10;
    private static final int DEFAULT_TEXT_COLOR = 0xffFC00D1;
    private static final int DEFAULT_Color_UNREACH = 0xFFD3D6DA;
    private static final int DEFAULT_HEIGHT_UNREACH = 2;
    private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_HEIGHT_REACH = 2;
    private static final int DEFAULT_TEXT_OFFSET = 10;

    private int mTextSize = spToPx(DEFAULT_TEXT_SIZE);

    private int mTextColor = DEFAULT_TEXT_COLOR;

    private int mUnreachHeight = dpToPx(DEFAULT_HEIGHT_UNREACH);
    private int mUnreachColor = DEFAULT_Color_UNREACH;
    private int mReachHeight = dpToPx(DEFAULT_HEIGHT_REACH);
    private int mReachColor = DEFAULT_COLOR_REACH;
    private int mTextOffeset = dpToPx(DEFAULT_TEXT_OFFSET);

    private Paint mPaint = new Paint();

    private int mRealWidth = 0;

    public HorizontalProgressBar2(Context context) {
        this(context, null);
    }

    public HorizontalProgressBar2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBar2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttributeValues(attrs);
        mPaint.setTextSize(mTextSize);
    }


    private int dpToPx(int defaultDp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultDp, getResources().getDisplayMetrics());
    }

    private void obtainAttributeValues(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.Ho2);
        mTextColor = ta.getColor(R.styleable.Ho2_progress_text_color, mTextColor);
        mTextSize = (int) ta.getDimension(R.styleable.Ho2_progress_text_size, mTextSize);

        mReachColor = ta.getColor(R.styleable.Ho2_progress_reach_color, mReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.Ho2_progress_reach_height, mReachHeight);

        mUnreachColor = ta.getColor(R.styleable.Ho2_progress_unreach_color, mUnreachColor);
        mUnreachHeight = (int) ta.getDimension(R.styleable.Ho2_progress_unreach_height, mUnreachHeight);

        mTextOffeset = (int) ta.getDimension(R.styleable.Ho2_progress_text_offset, mTextOffeset);
        ta.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int resultHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthSize, resultHeight);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //init paint and canvas
        canvas.save();
        canvas.translate(getPaddingLeft(), getMeasuredHeight() / 2);//这里我使用了getMeasureHeight
        boolean needToDrawUnreach = true;
        //draw reach bar
        float radio = getProgress() * 1.0f / getMax();
        float progressX = radio * mRealWidth;
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        if (progressX + textWidth >= mRealWidth) {
            progressX = mRealWidth - textWidth;
            needToDrawUnreach = false;
        }
        int endX = (int) (progressX - mTextOffeset / 2);
        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }

        //draw text
        mPaint.setColor(mTextColor);
        mPaint.setStrokeWidth(mTextSize);
        int y = (int) -((mPaint.descent() + mPaint.ascent())/2);
        canvas.drawText(text, progressX, y, mPaint);
        //draw unreach bar
        if (needToDrawUnreach) {
            mPaint.setColor(mUnreachColor);
            mPaint.setStrokeWidth(mUnreachHeight);
            canvas.drawLine(endX + textWidth + mTextOffeset, 0, mRealWidth, 0, mPaint);
        }
        //restore canvas
        canvas.restore();
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            int maxHeight = getPaddingTop() + getPaddingBottom() + Math.max(Math.max(mUnreachHeight, mReachHeight), Math.abs(textHeight));
            result = maxHeight;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, maxHeight);
            }
        }
        return result;
    }

    private int spToPx(int defaultSp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, defaultSp, getResources().getDisplayMetrics());
    }
}
