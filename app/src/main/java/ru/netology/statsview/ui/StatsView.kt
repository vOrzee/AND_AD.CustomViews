package ru.netology.statsview.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import ru.netology.statsview.R
import ru.netology.statsview.utils.AndroidUtils
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random

class StatsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(
    context,
    attributeSet,
    defStyleAttr,
    defStyleRes,
) {

    var data: List<Float> = emptyList()
        set(value) {
            field = value
            update()
        }

    private var radius = 0F
    private var center = PointF()
    private var oval = RectF()
    private var textSize = AndroidUtils.dp(context, 40).toFloat()
    private var lineWidth = AndroidUtils.dp(context, 16).toFloat()
    private var colors = emptyList<Int>()


    private var progress = 0F
    private var valueAnimator: ValueAnimator? = null


    private val paint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        strokeWidth = lineWidth
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        textSize = this@StatsView.textSize
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    init {
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            textSize = getDimension(R.styleable.StatsView_textSize, textSize)
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth)
            colors = listOf(
                getColor(R.styleable.StatsView_color1, randomColor()),
                getColor(R.styleable.StatsView_color2, randomColor()),
                getColor(R.styleable.StatsView_color3, randomColor()),
                getColor(R.styleable.StatsView_color4, randomColor()),
                getColor(R.styleable.StatsView_color5, randomColor()),
            )
        }
    }

    private fun randomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())

    private fun smartStatsViewDivider(sum: Float): Float =
        if (sum < 1) 1F else sum.pow(-1)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - lineWidth / 2
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

    private val durationAnimation = 3000L

    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty()) {
            return
        }
        canvas.drawText(
            "%.2f%%".format(data.sum() * progress * 100 * smartStatsViewDivider(data.sum())),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint,
        )
        var startFrom = -90F
        val currentProgress = progress*360F
        var lastProgress = 0F
        data.forEachIndexed { index, datum ->
            val angle = datum * 360F * smartStatsViewDivider(data.sum())
            paint.color = colors.getOrElse(index) { randomColor() }
            canvas.drawArc(oval, startFrom, currentProgress-lastProgress, false, paint)
            startFrom += angle
            lastProgress += angle
            if (lastProgress>currentProgress) return
        }.apply {
            if (progress == 1F) {
                paint.color = colors[0]
                canvas.drawPoint(center.x, center.y - radius, paint)
            }
        }
    }

    private fun update() {
        valueAnimator?.let {
            it.removeAllListeners()
            it.cancel()
        }
        progress = 0F

        valueAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
            addUpdateListener { anim ->
                progress = anim.animatedValue as Float
                invalidate()
            }
            duration = durationAnimation
            interpolator = LinearInterpolator()
        }.also {
            it.start()
        }
    }
}