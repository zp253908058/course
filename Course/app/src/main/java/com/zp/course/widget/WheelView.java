package com.zp.course.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.zp.course.R;
import com.zp.course.util.DensityUtils;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Class description:
 * 自定义滚轮视图，参考https://github.com/ycuwq/DatePicker/blob/master/datepicker/src/main/java/com/ycuwq/datepicker/WheelPicker.java
 *
 * @author ZP
 * @version 1.0
 * @see WheelView
 * @since 2019/3/25
 */
public class WheelView extends View {

    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXT_SIZE = 1 << 4;
    private static final int DEFAULT_SELECT_TEXT_SIZE = 18;
    private static final int DEFAULT_SELECT_COLOR = Color.parseColor("#FF33AAFF");
    private static final int DEFAULT_CURTAIN_COLOR = Color.parseColor("#303D3D3D");
    private static final int DEFAULT_CURTAIN_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_ITEM_SPACE = 12;

    /**
     * 数据集合
     */
    private List<String> mDataList;

    /**
     * 未选中文本颜色
     */
    @ColorInt
    private int mTextColor = DEFAULT_TEXT_COLOR;

    /**
     * 未选中文本大小
     */
    private int mTextSize = DensityUtils.spToPx(getContext(), DEFAULT_TEXT_SIZE);

    /**
     * 未选中文本画笔
     */
    private Paint mTextPaint;

    /**
     * 字体渐变，开启后越靠近边缘，字体越模糊
     */
    private boolean mIsTextGradual = true;

    /**
     * 选中的Item的Text颜色
     */
    @ColorInt
    private int mSelectedTextColor = DEFAULT_SELECT_COLOR;

    /**
     * 选中的Item的Text大小
     */
    private int mSelectedTextSize = DensityUtils.spToPx(getContext(), DEFAULT_SELECT_TEXT_SIZE);

    /**
     * 选中Item的文本画笔
     */
    private Paint mSelectedPaint;
    /**
     * 指示器文字
     * 会在中心文字后边多绘制一个文字。
     */
    private String mIndicatorText;

    /**
     * 指示器文字颜色
     */
    @ColorInt
    private int mIndicatorTextColor = DEFAULT_SELECT_COLOR;

    /**
     * 指示器文字大小
     */
    private int mIndicatorTextSize = DensityUtils.spToPx(getContext(), DEFAULT_TEXT_SIZE);

    /**
     * 指示器文本画笔
     */
    private Paint mIndicatorPaint;

    private Paint mPaint;

    /**
     * 最大的一个Item的文本的宽高
     */
    private int mTextMaxWidth, mTextMaxHeight;
    /**
     * 输入的一段文字，可以用来测量 mTextMaxWidth
     */
    private String mMaximumWidthText;

    /**
     * 显示的Item一半的数量（中心Item上下两边分别的数量）
     * 总显示的数量为 mHalfVisibleCount * 2 + 1
     */
    private int mHalfVisibleCount = 2;

    /**
     * 两个Item之间的高度间隔
     */
    private int mHeightSpace = DensityUtils.dipToPx(getContext(), DEFAULT_ITEM_SPACE);

    private int mWidthSpace = DensityUtils.dipToPx(getContext(), DEFAULT_ITEM_SPACE);

    private int mHeight;

    /**
     * 当前的Item的位置
     */
    private int mCurrentPosition = 0;

    /**
     * 是否将中间的Item放大
     */
    private boolean mIsZoomInSelected = true;

    /**
     * 是否显示幕布，中央Item会遮盖一个颜色颜色
     */
    private boolean mIsShowCurtain = true;

    /**
     * 幕布颜色
     */
    @ColorInt
    private int mCurtainColor = DEFAULT_CURTAIN_COLOR;

    /**
     * 是否显示幕布的边框
     */
    private boolean mIsShowCurtainBorder = true;

    /**
     * 幕布边框的颜色
     */
    @ColorInt
    private int mCurtainBorderColor = DEFAULT_CURTAIN_BORDER_COLOR;

    /**
     * 整个控件的可绘制面积
     */
    private Rect mDrawnRect;

    /**
     * 中心被选中的Item的坐标矩形
     */
    private Rect mSelectedRect;

    /**
     * 第一个Item的绘制Text的坐标
     */
    private int mFirstDrawX, mFirstDrawY;

    /**
     * 中心的Item绘制text的Y轴坐标
     */
    private int mCenterDrawnY;

    private Scroller mScroller;

    private int mTouchSlop;

    /**
     * 该标记的作用是，令mTouchSlop仅在一个滑动过程中生效一次。
     */
    private boolean mTouchSlopFlag;

    private VelocityTracker mTracker;

    private int mTouchDownY;
    /**
     * Y轴Scroll滚动的位移
     */
    private int mScrollOffsetY;

    /**
     * 最后手指Down事件的Y轴坐标，用于计算拖动距离
     */
    private int mLastDownY;

    /**
     * 是否循环读取
     */
    private boolean mIsCyclic = false;


    /**
     * 最大可以Fling的距离
     */
    private int mMaxFlingY, mMinFlingY;

    /**
     * 滚轮滑动时的最小/最大速度
     */
    private int mMinimumVelocity = 50, mMaximumVelocity = 12000;

    /**
     * 是否是手动停止滚动
     */
    private boolean mIsAbortScroller;

    private LinearGradient mLinearGradient;

    private Handler mHandler = new Handler();

    private OnWheelChangeListener mOnWheelChangeListener;

    private Runnable mScrollerRunnable = new Runnable() {
        @Override
        public void run() {

            if (mScroller.computeScrollOffset()) {

                mScrollOffsetY = mScroller.getCurrY();
                postInvalidate();
                mHandler.postDelayed(this, 16);
            }
            if (mScroller.isFinished() || (mScroller.getFinalY() == mScroller.getCurrY()
                    && mScroller.getFinalX() == mScroller.getCurrX())) {

                if (mHeight == 0) {
                    return;
                }
                int position = -mScrollOffsetY / mHeight;
                position = fixItemPosition(position);
                if (mCurrentPosition != position) {
                    mCurrentPosition = position;
                    if (mOnWheelChangeListener == null) {
                        return;
                    }
                    mOnWheelChangeListener.onWheelSelected(mDataList.get(position), position);
                }
            }
        }
    };


    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
        mLinearGradient = new LinearGradient(mTextColor, mSelectedTextColor);
        mDrawnRect = new Rect();
        mSelectedRect = new Rect();
        mScroller = new Scroller(context);

        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelView);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.WheelView_android_textColor:
                    mTextColor = a.getColor(attr, mTextColor);
                    break;
                case R.styleable.WheelView_android_textSize:
                    mTextSize = (a.getDimensionPixelSize(attr, mTextSize));
                    break;
                case R.styleable.WheelView_textGradual:
                    mIsTextGradual = (a.getBoolean(attr, mIsTextGradual));
                    break;
                case R.styleable.WheelView_cyclic:
                    mIsCyclic = (a.getBoolean(attr, mIsCyclic));
                    break;
                case R.styleable.WheelView_halfVisibleCount:
                    mHalfVisibleCount = (a.getInteger(attr, mHalfVisibleCount));
                    break;
                case R.styleable.WheelView_maximumWidthText:
                    mMaximumWidthText = (a.getString(attr));
                    break;
                case R.styleable.WheelView_selectedTextColor:
                    mSelectedTextColor = (a.getColor(attr, mSelectedTextColor));
                    break;
                case R.styleable.WheelView_selectedTextSize:
                    mSelectedTextSize = (a.getDimensionPixelSize(attr, mSelectedTextSize));
                    break;
                case R.styleable.WheelView_currentPosition:
                    mCurrentPosition = (a.getInteger(attr, mCurrentPosition));
                    break;
                case R.styleable.WheelView_widthSpace:
                    mWidthSpace = (a.getDimensionPixelSize(attr, mWidthSpace));
                    break;
                case R.styleable.WheelView_heightSpace:
                    mHeightSpace = (a.getDimensionPixelSize(attr, mHeightSpace));
                    break;
                case R.styleable.WheelView_zoomInSelected:
                    mIsZoomInSelected = (a.getBoolean(attr, mIsZoomInSelected));
                    break;
                case R.styleable.WheelView_showCurtain:
                    mIsShowCurtain = (a.getBoolean(attr, mIsShowCurtain));
                    break;
                case R.styleable.WheelView_curtainColor:
                    mCurtainColor = (a.getColor(attr, mCurtainColor));
                    break;
                case R.styleable.WheelView_showCurtainBorder:
                    mIsShowCurtainBorder = (a.getBoolean(attr, mIsShowCurtainBorder));
                    break;
                case R.styleable.WheelView_curtainBorderColor:
                    mCurtainBorderColor = (a.getColor(attr, mCurtainBorderColor));
                    break;
                case R.styleable.WheelView_indicatorText:
                    mIndicatorText = (a.getString(attr));
                    break;
                case R.styleable.WheelView_indicatorTextColor:
                    mIndicatorTextColor = (a.getColor(attr, mIndicatorTextColor));
                    break;
                case R.styleable.WheelView_indicatorTextSize:
                    mIndicatorTextSize = (a.getDimensionPixelSize(attr, mIndicatorTextSize));
                    break;
                case R.styleable.WheelView_dataRes:
                    mDataList = Arrays.asList(getResources().getStringArray(a.getResourceId(attr, 0)));
                    break;
            }
        }
        a.recycle();
    }

    public void computeTextSize() {
        mTextMaxWidth = mTextMaxHeight = 0;
        if (mDataList.size() == 0) {
            return;
        }

        //这里使用最大的,防止文字大小超过布局大小。
        mPaint.setTextSize(mSelectedTextSize > mTextSize ? mSelectedTextSize : mTextSize);

        if (!TextUtils.isEmpty(mMaximumWidthText)) {
            mTextMaxWidth = (int) mPaint.measureText(mMaximumWidthText);
        } else {
            mTextMaxWidth = (int) mPaint.measureText(mDataList.get(0).toString());
        }
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        mTextMaxHeight = (int) (metrics.bottom - metrics.top);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setTextAlign(Paint.Align.CENTER);
        mSelectedPaint.setColor(mSelectedTextColor);
        mSelectedPaint.setTextSize(mSelectedTextSize);
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setTextAlign(Paint.Align.LEFT);
        mIndicatorPaint.setColor(mIndicatorTextColor);
        mIndicatorPaint.setTextSize(mIndicatorTextSize);
    }

    /**
     * 计算实际的大小
     *
     * @param specMode 测量模式
     * @param specSize 测量的大小
     * @param size     需要的大小
     * @return 返回的数值
     */
    private int measureSize(int specMode, int specSize, int size) {
        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        } else {
            return Math.min(specSize, size);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = mTextMaxWidth + mWidthSpace;
        int height = (mTextMaxHeight + mHeightSpace) * getVisibleItemCount();

        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(measureSize(specWidthMode, specWidthSize, width),
                measureSize(specHeightMode, specHeightSize, height));
    }

    /**
     * 计算Fling极限
     * 如果为Cyclic模式则为Integer的极限值，如果正常模式，则为一整个数据集的上下限。
     */
    private void computeFlingLimitY() {
        mMinFlingY = mIsCyclic ? Integer.MIN_VALUE : -mHeight * (mDataList.size() - 1);
        mMaxFlingY = mIsCyclic ? Integer.MAX_VALUE : 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawnRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        mHeight = mDrawnRect.height() / getVisibleItemCount();
        mFirstDrawX = mDrawnRect.centerX();
        mFirstDrawY = (int) ((mHeight - (mSelectedPaint.ascent() + mSelectedPaint.descent())) / 2);
        //中间的Item边框
        mSelectedRect.set(getPaddingLeft(), mHeight * mHalfVisibleCount, getWidth() - getPaddingRight(), mHeight + mHeight * mHalfVisibleCount);
        computeFlingLimitY();
        mCenterDrawnY = mFirstDrawY + mHeight * mHalfVisibleCount;

        mScrollOffsetY = -mHeight * mCurrentPosition;
    }

    /**
     * 修正坐标值，让其回到dateList的范围内
     *
     * @param position 修正前的值
     * @return 修正后的值
     */
    private int fixItemPosition(int position) {
        if (position < 0) {
            //将数据集限定在0 ~ mDataList.size()-1之间
            position = mDataList.size() + (position % mDataList.size());

        }
        if (position >= mDataList.size()) {
            //将数据集限定在0 ~ mDataList.size()-1之间
            position = position % mDataList.size();
        }
        return position;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextAlign(Paint.Align.CENTER);
        if (mIsShowCurtain) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mCurtainColor);
            canvas.drawRect(mSelectedRect, mPaint);
        }
        if (mIsShowCurtainBorder) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mCurtainBorderColor);
            canvas.drawRect(mSelectedRect, mPaint);
            canvas.drawRect(mDrawnRect, mPaint);
        }
        int drawnSelectedPos = -mScrollOffsetY / mHeight;
        mPaint.setStyle(Paint.Style.FILL);
        //首尾各多绘制一个用于缓冲
        for (int drawDataPos = drawnSelectedPos - mHalfVisibleCount - 1;
             drawDataPos <= drawnSelectedPos + mHalfVisibleCount + 1; drawDataPos++) {
            int position = drawDataPos;
            if (mIsCyclic) {
                position = fixItemPosition(position);
            } else {
                if (position < 0 || position > mDataList.size() - 1) {
                    continue;
                }
            }

            String data = mDataList.get(position);
            int itemDrawY = mFirstDrawY + (drawDataPos + mHalfVisibleCount) * mHeight + mScrollOffsetY;
            //距离中心的Y轴距离
            int distanceY = Math.abs(mCenterDrawnY - itemDrawY);

            if (mIsTextGradual) {
                //文字颜色渐变要在设置透明度上边，否则会被覆盖
                //计算文字颜色渐变
                if (distanceY < mHeight) {  //距离中心的高度小于一个ItemHeight才会开启渐变
                    float colorRatio = 1 - (distanceY / (float) mHeight);
                    mSelectedPaint.setColor(mLinearGradient.getColor(colorRatio));
                    mTextPaint.setColor(mLinearGradient.getColor(colorRatio));
                } else {
                    mSelectedPaint.setColor(mSelectedTextColor);
                    mTextPaint.setColor(mTextColor);
                }
                //计算透明度渐变
                float alphaRatio;
                if (itemDrawY > mCenterDrawnY) {
                    alphaRatio = (mDrawnRect.height() - itemDrawY) /
                            (float) (mDrawnRect.height() - (mCenterDrawnY));
                } else {
                    alphaRatio = itemDrawY / (float) mCenterDrawnY;
                }

                alphaRatio = alphaRatio < 0 ? 0 : alphaRatio;
                mSelectedPaint.setAlpha((int) (alphaRatio * 255));
                mTextPaint.setAlpha((int) (alphaRatio * 255));
            }

            //开启此选项,会将越靠近中心的Item字体放大
            if (mIsZoomInSelected) {
                if (distanceY < mHeight) {
                    float addedSize = (mHeight - distanceY) / (float) mHeight * (mSelectedTextSize - mTextSize);
                    mSelectedPaint.setTextSize(mTextSize + addedSize);
                    mTextPaint.setTextSize(mTextSize + addedSize);
                } else {
                    mSelectedPaint.setTextSize(mTextSize);
                    mTextPaint.setTextSize(mTextSize);
                }
            } else {
                mSelectedPaint.setTextSize(mTextSize);
                mTextPaint.setTextSize(mTextSize);
            }
            //在中间位置的Item作为被选中的。
            if (distanceY < mHeight / 2) {
                canvas.drawText(data, mFirstDrawX, itemDrawY, mSelectedPaint);
            } else {
                canvas.drawText(data, mFirstDrawX, itemDrawY, mTextPaint);
            }
        }
        if (!TextUtils.isEmpty(mIndicatorText)) {
            canvas.drawText(mIndicatorText, mFirstDrawX + mTextMaxWidth >> 1, mCenterDrawnY, mIndicatorPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTracker == null) {
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    mIsAbortScroller = true;
                } else {
                    mIsAbortScroller = false;
                }
                mTracker.clear();
                mTouchDownY = mLastDownY = (int) event.getY();
                mTouchSlopFlag = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mTouchSlopFlag && Math.abs(mTouchDownY - event.getY()) < mTouchSlop) {
                    break;
                }
                mTouchSlopFlag = false;
                float move = event.getY() - mLastDownY;
                mScrollOffsetY += move;
                mLastDownY = (int) event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (!mIsAbortScroller && mTouchDownY == mLastDownY) {
                    performClick();
                    if (event.getY() > mSelectedRect.bottom) {
                        int scrollItem = (int) (event.getY() - mSelectedRect.bottom) / mHeight + 1;
                        mScroller.startScroll(0, mScrollOffsetY, 0,
                                -scrollItem * mHeight);

                    } else if (event.getY() < mSelectedRect.top) {
                        int scrollItem = (int) (mSelectedRect.top - event.getY()) / mHeight + 1;
                        mScroller.startScroll(0, mScrollOffsetY, 0,
                                scrollItem * mHeight);
                    }
                } else {
                    mTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int velocity = (int) mTracker.getYVelocity();
                    if (Math.abs(velocity) > mMinimumVelocity) {
                        mScroller.fling(0, mScrollOffsetY, 0, velocity,
                                0, 0, mMinFlingY, mMaxFlingY);
                        mScroller.setFinalY(mScroller.getFinalY() +
                                computeDistanceToEndPoint(mScroller.getFinalY() % mHeight));
                    } else {
                        mScroller.startScroll(0, mScrollOffsetY, 0,
                                computeDistanceToEndPoint(mScrollOffsetY % mHeight));
                    }
                }
                if (!mIsCyclic) {
                    if (mScroller.getFinalY() > mMaxFlingY) {
                        mScroller.setFinalY(mMaxFlingY);
                    } else if (mScroller.getFinalY() < mMinFlingY) {
                        mScroller.setFinalY(mMinFlingY);
                    }
                }
                mHandler.post(mScrollerRunnable);
                mTracker.recycle();
                mTracker = null;
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int computeDistanceToEndPoint(int remainder) {
        if (Math.abs(remainder) > mHeight / 2) {
            if (mScrollOffsetY < 0) {
                return -mHeight - remainder;
            } else {
                return mHeight - remainder;
            }
        } else {
            return -remainder;
        }
    }


    public void setOnWheelChangeListener(OnWheelChangeListener onWheelChangeListener) {
        mOnWheelChangeListener = onWheelChangeListener;
    }

    public Paint getTextPaint() {
        return mTextPaint;
    }

    public Paint getSelectedPaint() {
        return mSelectedPaint;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public Paint getIndicatorPaint() {
        return mIndicatorPaint;
    }

    public List getDataList() {
        return mDataList;
    }

    public void setDataList(@NonNull List<String> dataList) {
        mDataList = dataList;
        if (dataList.size() == 0) {
            return;
        }
        computeTextSize();
        computeFlingLimitY();
        requestLayout();
        postInvalidate();
    }

    public int getTextColor() {
        return mTextColor;
    }

    /**
     * 一般列表的文本颜色
     *
     * @param textColor 文本颜色
     */
    public void setTextColor(@ColorInt int textColor) {
        if (mTextColor == textColor) {
            return;
        }
        mTextPaint.setColor(textColor);
        mTextColor = textColor;
        mLinearGradient.setStartColor(textColor);
        postInvalidate();
    }

    public int getTextSize() {
        return mTextSize;
    }

    /**
     * 一般列表的文本大小
     *
     * @param textSize 文字大小
     */
    public void setTextSize(int textSize) {
        if (mTextSize == textSize) {
            return;
        }
        mTextSize = textSize;
        mTextPaint.setTextSize(textSize);
        computeTextSize();
        postInvalidate();
    }

    public int getSelectedTextColor() {
        return mSelectedTextColor;
    }

    /**
     * 设置被选中时候的文本颜色
     *
     * @param selectedTextColor 文本颜色
     */
    public void setSelectedTextColor(@ColorInt int selectedTextColor) {
        if (mSelectedTextColor == selectedTextColor) {
            return;
        }
        mSelectedPaint.setColor(selectedTextColor);
        mSelectedTextColor = selectedTextColor;
        mLinearGradient.setEndColor(selectedTextColor);
        postInvalidate();
    }

    public int getSelectedTextSize() {
        return mSelectedTextSize;
    }

    /**
     * 设置被选中时候的文本大小
     *
     * @param selectedTextSize 文字大小
     */
    public void setSelectedTextSize(int selectedTextSize) {
        if (mSelectedTextSize == selectedTextSize) {
            return;
        }
        mSelectedPaint.setTextSize(selectedTextSize);
        mSelectedTextSize = selectedTextSize;
        computeTextSize();
        postInvalidate();
    }


    public String getMaximumWidthText() {
        return mMaximumWidthText;
    }

    /**
     * 设置输入的一段文字，用来测量 mTextMaxWidth
     *
     * @param maximumWidthText 文本内容
     */
    public void setMaximumWidthText(String maximumWidthText) {
        mMaximumWidthText = maximumWidthText;
        requestLayout();
        postInvalidate();
    }

    public int getHalfVisibleCount() {
        return mHalfVisibleCount;
    }

    /**
     * 显示的个数等于上下两边Item的个数+ 中间的Item
     *
     * @return 总显示的数量
     */
    public int getVisibleItemCount() {
        return mHalfVisibleCount * 2 + 1;
    }

    /**
     * 设置显示数据量的个数的一半。
     * 为保证总显示个数为奇数,这里将总数拆分，总数为 mHalfVisibleCount * 2 + 1
     *
     * @param halfVisibleCount 总数量的一半
     */
    public void setHalfVisibleCount(int halfVisibleCount) {
        if (mHalfVisibleCount == halfVisibleCount) {
            return;
        }
        mHalfVisibleCount = halfVisibleCount;
        requestLayout();
    }

    public int getWidthSpace() {
        return mWidthSpace;
    }

    public void setWidthSpace(int widthSpace) {
        if (mWidthSpace == widthSpace) {
            return;
        }
        mWidthSpace = widthSpace;
        requestLayout();
    }

    public int getHeightSpace() {
        return mHeightSpace;
    }

    /**
     * 设置两个Item之间的间隔
     *
     * @param heightSpace 间隔值
     */
    public void setHeightSpace(int heightSpace) {
        if (mHeightSpace == heightSpace) {
            return;
        }
        mHeightSpace = heightSpace;
        requestLayout();
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    /**
     * 设置当前选中的列表项,将滚动到所选位置
     *
     * @param currentPosition 设置的当前位置
     */
    public void setCurrentPosition(int currentPosition) {
        setCurrentPosition(currentPosition, true);
    }

    /**
     * 设置当前选中的列表位置
     *
     * @param currentPosition 设置的当前位置
     * @param smoothScroll    是否平滑滚动
     */
    public synchronized void setCurrentPosition(int currentPosition, boolean smoothScroll) {
        if (currentPosition > mDataList.size() - 1) {
            currentPosition = mDataList.size() - 1;
        }
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        if (mCurrentPosition == currentPosition) {
            return;
        }
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        //如果mItemHeight=0代表还没有绘制完成，这时平滑滚动没有意义
        if (smoothScroll && mHeight > 0) {
            mScroller.startScroll(0, mScrollOffsetY, 0, (mCurrentPosition - currentPosition) * mHeight);
//            mScroller.setFinalY(mScroller.getFinalY() +
//                    computeDistanceToEndPoint(mScroller.getFinalY() % mHeight));
            int finalY = -currentPosition * mHeight;
            mScroller.setFinalY(finalY);
            mHandler.post(mScrollerRunnable);
        } else {
            mCurrentPosition = currentPosition;
            mScrollOffsetY = -mHeight * mCurrentPosition;
            postInvalidate();
            if (mOnWheelChangeListener != null) {
                mOnWheelChangeListener.onWheelSelected(mDataList.get(currentPosition), currentPosition);
            }
        }
    }

    public boolean isZoomInSelected() {
        return mIsZoomInSelected;
    }

    public void setZoomInSelected(boolean zoomInSelected) {
        if (mIsZoomInSelected == zoomInSelected) {
            return;
        }
        mIsZoomInSelected = zoomInSelected;
        postInvalidate();
    }

    public boolean isCyclic() {
        return mIsCyclic;
    }

    /**
     * 设置是否循环滚动。
     *
     * @param cyclic 上下边界是否相邻
     */
    public void setCyclic(boolean cyclic) {
        if (mIsCyclic == cyclic) {
            return;
        }
        mIsCyclic = cyclic;
        computeFlingLimitY();
        requestLayout();
    }

    public int getMinimumVelocity() {
        return mMinimumVelocity;
    }

    /**
     * 设置最小滚动速度,如果实际速度小于此速度，将不会触发滚动。
     *
     * @param minimumVelocity 最小速度
     */
    public void setMinimumVelocity(int minimumVelocity) {
        mMinimumVelocity = minimumVelocity;
    }

    public int getMaximumVelocity() {
        return mMaximumVelocity;
    }

    /**
     * 设置最大滚动的速度,实际滚动速度的上限
     *
     * @param maximumVelocity 最大滚动速度
     */
    public void setMaximumVelocity(int maximumVelocity) {
        mMaximumVelocity = maximumVelocity;
    }

    public boolean isTextGradual() {
        return mIsTextGradual;
    }

    /**
     * 设置文字渐变，离中心越远越淡。
     *
     * @param textGradual 是否渐变
     */
    public void setTextGradual(boolean textGradual) {
        if (mIsTextGradual == textGradual) {
            return;
        }
        mIsTextGradual = textGradual;
        postInvalidate();
    }

    public boolean isShowCurtain() {
        return mIsShowCurtain;
    }

    /**
     * 设置中心Item是否有幕布遮盖
     *
     * @param showCurtain 是否有幕布
     */
    public void setShowCurtain(boolean showCurtain) {
        if (mIsShowCurtain == showCurtain) {
            return;
        }
        mIsShowCurtain = showCurtain;
        postInvalidate();
    }

    public int getCurtainColor() {
        return mCurtainColor;
    }

    /**
     * 设置幕布颜色
     *
     * @param curtainColor 幕布颜色
     */
    public void setCurtainColor(@ColorInt int curtainColor) {
        if (mCurtainColor == curtainColor) {
            return;
        }
        mCurtainColor = curtainColor;
        postInvalidate();
    }

    public boolean isShowCurtainBorder() {
        return mIsShowCurtainBorder;
    }

    /**
     * 设置幕布是否显示边框
     *
     * @param showCurtainBorder 是否有幕布边框
     */
    public void setShowCurtainBorder(boolean showCurtainBorder) {
        if (mIsShowCurtainBorder == showCurtainBorder) {
            return;
        }
        mIsShowCurtainBorder = showCurtainBorder;
        postInvalidate();
    }

    public int getCurtainBorderColor() {
        return mCurtainBorderColor;
    }

    /**
     * 幕布边框的颜色
     *
     * @param curtainBorderColor 幕布边框颜色
     */
    public void setCurtainBorderColor(@ColorInt int curtainBorderColor) {
        if (mCurtainBorderColor == curtainBorderColor) {
            return;
        }
        mCurtainBorderColor = curtainBorderColor;
        postInvalidate();
    }

    public void setIndicatorText(String indicatorText) {
        mIndicatorText = indicatorText;
        postInvalidate();
    }

    public void setIndicatorTextColor(int indicatorTextColor) {
        mIndicatorTextColor = indicatorTextColor;
        mIndicatorPaint.setColor(mIndicatorTextColor);
        postInvalidate();
    }

    public void setIndicatorTextSize(int indicatorTextSize) {
        mIndicatorTextSize = indicatorTextSize;
        mIndicatorPaint.setTextSize(mIndicatorTextSize);
        postInvalidate();
    }

    public interface OnWheelChangeListener {
        void onWheelSelected(String item, int position);
    }


    private static class LinearGradient {

        private int mStartColor;
        private int mEndColor;
        private int mRedStart;
        private int mBlueStart;
        private int mGreenStart;
        private int mRedEnd;
        private int mBlueEnd;
        private int mGreenEnd;

        public LinearGradient(@ColorInt int startColor, @ColorInt int endColor) {
            mStartColor = startColor;
            mEndColor = endColor;
            updateColor();
        }


        public void setStartColor(@ColorInt int startColor) {
            mStartColor = startColor;
            updateColor();
        }

        public void setEndColor(@ColorInt int endColor) {
            mEndColor = endColor;
            updateColor();
        }

        private void updateColor() {
            mRedStart = Color.red(mStartColor);
            mBlueStart = Color.blue(mStartColor);
            mGreenStart = Color.green(mStartColor);
            mRedEnd = Color.red(mEndColor);
            mBlueEnd = Color.blue(mEndColor);
            mGreenEnd = Color.green(mEndColor);
        }

        public int getColor(float ratio) {
            int red = (int) (mRedStart + ((mRedEnd - mRedStart) * ratio + 0.5));
            int greed = (int) (mGreenStart + ((mGreenEnd - mGreenStart) * ratio + 0.5));
            int blue = (int) (mBlueStart + ((mBlueEnd - mBlueStart) * ratio + 0.5));
            return Color.rgb(red, greed, blue);
        }
    }
}
