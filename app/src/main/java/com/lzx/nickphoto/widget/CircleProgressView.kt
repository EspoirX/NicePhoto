package com.lzx.nickphoto.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.os.SystemClock
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.lzx.nickphoto.R

/**
 * Created by lzx on 2017/7/5.
 */
class CircleProgressView : View {

    private val TAG = CircleProgressView::class.java.simpleName

    private var circleRadius = 28

    private var barWidth = 4

    private var rimWidth = 4

    private val barLength = 16

    private val barMaxLength = 270

    private var fillRadius = false

    private var timeStartGrowing = 0.0

    private var barSpinCycleTime = 460.0

    private var barExtraLength = 0f

    private var barGrowingFromFront = true

    private var pausedTimeWithoutGrowing: Long = 0

    private val pauseGrowingTime: Long = 200

    private var barColor = 0xAA000000.toInt()

    private var rimColor = 0x00FFFFFF

    private val barPaint = Paint()

    private val rimPaint = Paint()

    private var circleBounds = RectF()

    private var spinSpeed = 230.0f

    private var lastTimeAnimated: Long = 0

    private var linearProgress: Boolean = false

    private var mProgress = 0.0f

    private var mTargetProgress = 0.0f

    private var isSpinning = false

    private var callback: ProgressCallback? = null


    /**
     * The constructor for the ProgressWheel
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView))
    }


    /**
     * The constructor for the ProgressWheel
     */
    constructor(context: Context) : super(context) {

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val viewWidth = circleRadius + this.paddingLeft + this.paddingRight
        val viewHeight = circleRadius + this.paddingTop + this.paddingBottom

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        val width: Int
        val height: Int

        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            width = Math.min(viewWidth, widthSize)
        } else {
            width = viewWidth
        }

        if (heightMode == View.MeasureSpec.EXACTLY || widthMode == View.MeasureSpec.EXACTLY) {
            height = heightSize
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            height = Math.min(viewHeight, heightSize)
        } else {
            height = viewHeight
        }

        setMeasuredDimension(width, height)
    }


    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of
     * the view, because this method is called after measuring the dimensions of
     * MATCH_PARENT & WRAP_CONTENT. Use this dimensions to setup the bounds and
     * paints.
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        super.onSizeChanged(w, h, oldw, oldh)

        setupBounds(w, h)
        setupPaints()
        invalidate()
    }


    /**
     * Set the properties of the paints we're using to draw the progress wheel
     */
    private fun setupPaints() {

        barPaint.color = barColor
        barPaint.isAntiAlias = true
        barPaint.style = Paint.Style.STROKE
        barPaint.strokeWidth = barWidth.toFloat()

        rimPaint.color = rimColor
        rimPaint.isAntiAlias = true
        rimPaint.style = Paint.Style.STROKE
        rimPaint.strokeWidth = rimWidth.toFloat()
    }


    /**
     * Set the bounds of the component
     */
    private fun setupBounds(layout_width: Int, layout_height: Int) {

        val paddingTop = paddingTop
        val paddingBottom = paddingBottom
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight

        if (!fillRadius) {
            val minValue = Math.min(layout_width - paddingLeft - paddingRight,
                    layout_height - paddingBottom - paddingTop)

            val circleDiameter = Math.min(minValue, circleRadius * 2 - barWidth * 2)

            val xOffset = (layout_width - paddingLeft - paddingRight - circleDiameter) / 2 + paddingLeft
            val yOffset = (layout_height - paddingTop - paddingBottom - circleDiameter) / 2 + paddingTop

            circleBounds = RectF((xOffset + barWidth).toFloat(), (yOffset + barWidth).toFloat(),
                    (xOffset + circleDiameter - barWidth).toFloat(), (yOffset + circleDiameter - barWidth).toFloat())
        } else {
            circleBounds = RectF((paddingLeft + barWidth).toFloat(), (paddingTop + barWidth).toFloat(),
                    (layout_width - paddingRight - barWidth).toFloat(), (layout_height - paddingBottom - barWidth).toFloat())
        }
    }


    /**
     * Parse the attributes passed to the view from the XML

     * @param a the attributes to parse
     */
    private fun parseAttributes(a: TypedArray) {

        val metrics = context.resources.displayMetrics
        barWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, barWidth.toFloat(), metrics).toInt()
        rimWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rimWidth.toFloat(), metrics).toInt()
        circleRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, circleRadius.toFloat(),
                metrics).toInt()

        circleRadius = a.getDimension(R.styleable.CircleProgressView_matProg_circleRadius,
                circleRadius.toFloat()).toInt()

        fillRadius = a.getBoolean(R.styleable.CircleProgressView_matProg_fillRadius, false)

        barWidth = a.getDimension(R.styleable.CircleProgressView_matProg_barWidth, barWidth.toFloat()).toInt()

        rimWidth = a.getDimension(R.styleable.CircleProgressView_matProg_rimWidth, rimWidth.toFloat()).toInt()

        val baseSpinSpeed = a.getFloat(R.styleable.CircleProgressView_matProg_spinSpeed,
                spinSpeed / 360.0f)
        spinSpeed = baseSpinSpeed * 360

        barSpinCycleTime = a.getInt(R.styleable.CircleProgressView_matProg_barSpinCycleTime,
                barSpinCycleTime.toInt()).toDouble()

        barColor = a.getColor(R.styleable.CircleProgressView_matProg_barColor, barColor)

        rimColor = a.getColor(R.styleable.CircleProgressView_matProg_rimColor, rimColor)

        linearProgress = a.getBoolean(R.styleable.CircleProgressView_matProg_linearProgress, false)

        if (a.getBoolean(R.styleable.CircleProgressView_matProg_progressIndeterminate, false)) {
            spin()
        }

        a.recycle()
    }


    fun setCallback(progressCallback: ProgressCallback) {

        callback = progressCallback

        if (!isSpinning) {
            runCallback()
        }
    }


    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        canvas.drawArc(circleBounds, 360f, 360f, false, rimPaint)

        var mustInvalidate = false

        if (isSpinning) {
            mustInvalidate = true

            val deltaTime = SystemClock.uptimeMillis() - lastTimeAnimated
            val deltaNormalized = deltaTime * spinSpeed / 1000.0f

            updateBarLength(deltaTime)

            mProgress += deltaNormalized
            if (mProgress > 360) {
                mProgress -= 360f

                runCallback(-1.0f)
            }
            lastTimeAnimated = SystemClock.uptimeMillis()

            var from = mProgress - 90
            var length = barLength + barExtraLength

            if (isInEditMode) {
                from = 0f
                length = 135f
            }

            canvas.drawArc(circleBounds, from, length, false, barPaint)
        } else {
            val oldProgress = mProgress

            if (mProgress != mTargetProgress) {
                mustInvalidate = true

                val deltaTime = (SystemClock.uptimeMillis() - lastTimeAnimated).toFloat() / 1000
                val deltaNormalized = deltaTime * spinSpeed

                mProgress = Math.min(mProgress + deltaNormalized, mTargetProgress)
                lastTimeAnimated = SystemClock.uptimeMillis()
            }

            if (oldProgress != mProgress) {
                runCallback()
            }

            var offset = 0.0f
            var progress = mProgress
            if (!linearProgress) {
                val factor = 2.0f
                offset = (1.0f - Math.pow((1.0f - mProgress / 360.0f).toDouble(), (2.0f * factor).toDouble())).toFloat() * 360.0f
                progress = (1.0f - Math.pow((1.0f - mProgress / 360.0f).toDouble(), factor.toDouble())).toFloat() * 360.0f
            }

            if (isInEditMode) {
                progress = 360f
            }

            canvas.drawArc(circleBounds, offset - 90, progress, false, barPaint)
        }

        if (mustInvalidate) {
            invalidate()
        }
    }


    override fun onVisibilityChanged(changedView: View, visibility: Int) {

        super.onVisibilityChanged(changedView, visibility)

        if (visibility == View.VISIBLE) {
            lastTimeAnimated = SystemClock.uptimeMillis()
        }
    }


    private fun updateBarLength(deltaTimeInMilliSeconds: Long) {

        if (pausedTimeWithoutGrowing >= pauseGrowingTime) {
            timeStartGrowing += deltaTimeInMilliSeconds.toDouble()

            if (timeStartGrowing > barSpinCycleTime) {
                timeStartGrowing -= barSpinCycleTime
                pausedTimeWithoutGrowing = 0
                barGrowingFromFront = !barGrowingFromFront
            }

            val distance = Math.cos((timeStartGrowing / barSpinCycleTime + 1) * Math.PI).toFloat() / 2 + 0.5f
            val destLength = (barMaxLength - barLength).toFloat()

            if (barGrowingFromFront) {
                barExtraLength = distance * destLength
            } else {
                val newLength = destLength * (1 - distance)
                mProgress += barExtraLength - newLength
                barExtraLength = newLength
            }
        } else {
            pausedTimeWithoutGrowing += deltaTimeInMilliSeconds
        }
    }


    /**
     * Check if the wheel is currently spinning
     */

    fun isSpinning(): Boolean {

        return isSpinning
    }


    /**
     * Reset the count (in increment mode)
     */
    fun resetCount() {

        mProgress = 0.0f
        mTargetProgress = 0.0f
        invalidate()
    }


    /**
     * Turn off spin mode
     */
    fun stopSpinning() {

        isSpinning = false
        mProgress = 0.0f
        mTargetProgress = 0.0f
        invalidate()
    }


    /**
     * Puts the view on spin mode
     */
    fun spin() {

        lastTimeAnimated = SystemClock.uptimeMillis()
        isSpinning = true
        invalidate()
    }


    private fun runCallback(value: Float) {

        if (callback != null) {
            callback!!.onProgressUpdate(value)
        }
    }


    private fun runCallback() {

        if (callback != null) {
            val normalizedProgress = Math.round(mProgress * 100 / 360.0f).toFloat() / 100
            callback!!.onProgressUpdate(normalizedProgress)
        }
    }


    /**
     * Set the progress to a specific value, the bar will smoothly animate until
     * that value

     * @param progress the progress between 0 and 1
     */
    fun setProgress(progress: Float) {
        var progress = progress

        if (isSpinning) {
            mProgress = 0.0f
            isSpinning = false

            runCallback()
        }

        if (progress > 1.0f) {
            progress -= 1.0f
        } else if (progress < 0) {
            progress = 0f
        }

        if (progress == mTargetProgress) {
            return
        }

        if (mProgress == mTargetProgress) {
            lastTimeAnimated = SystemClock.uptimeMillis()
        }

        mTargetProgress = Math.min(progress * 360.0f, 360.0f)

        invalidate()
    }


    /**
     * Set the progress to a specific value, the bar will be set instantly to
     * that value

     * @param progress the progress between 0 and 1
     */
    fun setInstantProgress(progress: Float) {
        var progress = progress

        if (isSpinning) {
            mProgress = 0.0f
            isSpinning = false
        }

        if (progress > 1.0f) {
            progress -= 1.0f
        } else if (progress < 0) {
            progress = 0f
        }

        if (progress == mTargetProgress) {
            return
        }

        mTargetProgress = Math.min(progress * 360.0f, 360.0f)
        mProgress = mTargetProgress
        lastTimeAnimated = SystemClock.uptimeMillis()
        invalidate()
    }


    public override fun onSaveInstanceState(): Parcelable {

        val superState = super.onSaveInstanceState()

        val ss = WheelSavedState(superState)

        ss.mProgress = this.mProgress
        ss.mTargetProgress = this.mTargetProgress
        ss.isSpinning = this.isSpinning
        ss.spinSpeed = this.spinSpeed
        ss.barWidth = this.barWidth
        ss.barColor = this.barColor
        ss.rimWidth = this.rimWidth
        ss.rimColor = this.rimColor
        ss.circleRadius = this.circleRadius
        ss.linearProgress = this.linearProgress
        ss.fillRadius = this.fillRadius

        return ss
    }


    public override fun onRestoreInstanceState(state: Parcelable) {

        if (state !is WheelSavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        val ss = state
        super.onRestoreInstanceState(ss.superState)

        this.mProgress = ss.mProgress
        this.mTargetProgress = ss.mTargetProgress
        this.isSpinning = ss.isSpinning
        this.spinSpeed = ss.spinSpeed
        this.barWidth = ss.barWidth
        this.barColor = ss.barColor
        this.rimWidth = ss.rimWidth
        this.rimColor = ss.rimColor
        this.circleRadius = ss.circleRadius
        this.linearProgress = ss.linearProgress
        this.fillRadius = ss.fillRadius

        this.lastTimeAnimated = SystemClock.uptimeMillis()
    }


    /**
     * @return the current progress between 0.0 and 1.0, if the wheel is
     * * indeterminate, then the result is -1
     */
    fun getProgress(): Float {

        return if (isSpinning) -1f else mProgress / 360.0f
    }


    /**
     * Sets the determinate progress mode

     * @param isLinear if the progress should increase linearly
     */
    fun setLinearProgress(isLinear: Boolean) {

        linearProgress = isLinear
        if (!isSpinning) {
            invalidate()
        }
    }


    /**
     * @return the radius of the wheel in pixels
     */
    fun getCircleRadius(): Int {

        return circleRadius
    }


    /**
     * Sets the radius of the wheel

     * @param circleRadius the expected radius, in pixels
     */
    fun setCircleRadius(circleRadius: Int) {

        this.circleRadius = circleRadius
        if (!isSpinning) {
            invalidate()
        }
    }


    /**
     * @return the width of the spinning bar
     */
    fun getBarWidth(): Int {

        return barWidth
    }


    /**
     * Sets the width of the spinning bar

     * @param barWidth the spinning bar width in pixels
     */
    fun setBarWidth(barWidth: Int) {

        this.barWidth = barWidth
        if (!isSpinning) {
            invalidate()
        }
    }


    /**
     * @return the color of the spinning bar
     */
    fun getBarColor(): Int {

        return barColor
    }


    /**
     * Sets the color of the spinning bar

     * @param barColor The spinning bar color
     */
    fun setBarColor(barColor: Int) {

        this.barColor = barColor
        setupPaints()
        if (!isSpinning) {
            invalidate()
        }
    }


    /**
     * @return the color of the wheel's contour
     */
    fun getRimColor(): Int {

        return rimColor
    }


    /**
     * Sets the color of the wheel's contour

     * @param rimColor the color for the wheel
     */
    fun setRimColor(rimColor: Int) {

        this.rimColor = rimColor
        setupPaints()
        if (!isSpinning) {
            invalidate()
        }
    }


    /**
     * @return the base spinning speed, in full circle turns per second (1.0
     * * equals on full turn in one second), this value also is applied
     * * for the smoothness when setting a progress
     */
    fun getSpinSpeed(): Float {

        return spinSpeed / 360.0f
    }


    /**
     * Sets the base spinning speed, in full circle turns per second (1.0 equals
     * on full turn in one second), this value also is applied for the
     * smoothness when setting a progress

     * @param spinSpeed the desired base speed in full turns per second
     */
    fun setSpinSpeed(spinSpeed: Float) {

        this.spinSpeed = spinSpeed * 360.0f
    }


    /**
     * @return the width of the wheel's contour in pixels
     */
    fun getRimWidth(): Int {

        return rimWidth
    }


    /**
     * Sets the width of the wheel's contour

     * @param rimWidth the width in pixels
     */
    fun setRimWidth(rimWidth: Int) {

        this.rimWidth = rimWidth
        if (!isSpinning) {
            invalidate()
        }
    }


    internal class WheelSavedState : View.BaseSavedState {

        var mProgress: Float = 0.toFloat()

        var mTargetProgress: Float = 0.toFloat()

        var isSpinning: Boolean = false

        var spinSpeed: Float = 0.toFloat()

        var barWidth: Int = 0

        var barColor: Int = 0

        var rimWidth: Int = 0

        var rimColor: Int = 0

        var circleRadius: Int = 0

        var linearProgress: Boolean = false

        var fillRadius: Boolean = false


        constructor(superState: Parcelable) : super(superState) {}


        private constructor(`in`: Parcel) : super(`in`) {
            this.mProgress = `in`.readFloat()
            this.mTargetProgress = `in`.readFloat()
            this.isSpinning = `in`.readByte().toInt() != 0
            this.spinSpeed = `in`.readFloat()
            this.barWidth = `in`.readInt()
            this.barColor = `in`.readInt()
            this.rimWidth = `in`.readInt()
            this.rimColor = `in`.readInt()
            this.circleRadius = `in`.readInt()
            this.linearProgress = `in`.readByte().toInt() != 0
            this.fillRadius = `in`.readByte().toInt() != 0
        }


        override fun writeToParcel(out: Parcel, flags: Int) {

            super.writeToParcel(out, flags)
            out.writeFloat(this.mProgress)
            out.writeFloat(this.mTargetProgress)
            out.writeByte((if (isSpinning) 1 else 0).toByte())
            out.writeFloat(this.spinSpeed)
            out.writeInt(this.barWidth)
            out.writeInt(this.barColor)
            out.writeInt(this.rimWidth)
            out.writeInt(this.rimColor)
            out.writeInt(this.circleRadius)
            out.writeByte((if (linearProgress) 1 else 0).toByte())
            out.writeByte((if (fillRadius) 1 else 0).toByte())
        }

        companion object {


            val CREATOR: Parcelable.Creator<WheelSavedState> = object : Parcelable.Creator<WheelSavedState> {

                override fun createFromParcel(`in`: Parcel): WheelSavedState {

                    return WheelSavedState(`in`)
                }


                override fun newArray(size: Int): Array<WheelSavedState?> {

                    return arrayOfNulls(size)
                }
            }
        }
    }

    interface ProgressCallback {

        /**
         * Method to call when the progress reaches a value in order to avoid
         * float precision issues, the progress is rounded to a float with two
         * decimals.
         *
         *
         * In indeterminate mode, the callback is called each time the wheel
         * completes an animation cycle, with, the progress value is -1.0f

         * @param progress a double value between 0.00 and 1.00 both included
         */
        fun onProgressUpdate(progress: Float)
    }
}