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
    var fillingTypeField = ""

    private var radius = 0F
    private var center = PointF()
    private var oval = RectF()
    private var textSize = AndroidUtils.dp(context, 56).toFloat()
    private var lineWidth = AndroidUtils.dp(context, 24).toFloat()
    private var colors = emptyList<Int>()

    private var progress = 0F
    private var valueAnimator: ValueAnimator? = null
    private var fillingType: Int = 0
    private var durationAnimation = 300L

    private val paint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        strokeWidth = lineWidth
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.BEVEL
        strokeCap = Paint.Cap.SQUARE
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
            fillingType = getInt(R.styleable.StatsView_fillingType, fillingType)
            durationAnimation = getInt(R.styleable.StatsView_duration, 300).toLong()
        }
    }

    private fun randomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())

    private fun smartStatsViewDivider(sum: Float): Float =
        if (sum < 1) 1F else sum.pow(-1)

    private fun checkedUserChoice() {
        when(fillingTypeField) {
            "rotation" -> fillingType = 1
            "sequential" -> fillingType = 2
            "bidirectional" -> fillingType = 3
            else -> {}
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - lineWidth - 1 / 2
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

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
        checkedUserChoice()
        when (fillingType) {
            1 -> { // fillingType "rotation"
                data.forEachIndexed { index, datum ->
                    val angle = datum * 360F * smartStatsViewDivider(data.sum())
                    paint.color = colors.getOrElse(index) { randomColor() }
                    canvas.drawArc(
                        oval,
                        startFrom + 360F * progress,
                        angle * progress,
                        false,
                        paint
                    )
                    startFrom += angle
                }
            }
            2 -> { // fillingType "sequential"
                data.forEachIndexed { index, datum ->
                    val angle = datum * 360F * smartStatsViewDivider(data.sum())
                    paint.color = colors.getOrElse(index) { randomColor() }
                    canvas.drawArc(
                        oval,
                        startFrom,
                        progress * 360F - (startFrom + 90F),
                        false,
                        paint
                    )
                    startFrom += angle
                    if ((startFrom + 90F) > progress * 360F) return
                }
            }
            3 -> { // fillingType "bidirectional"
                data.forEachIndexed { index, datum ->
                    val angle = datum * 360F * smartStatsViewDivider(data.sum())
                    paint.color = colors.getOrElse(index) { randomColor() }
                    canvas.drawArc(oval, startFrom, angle / 2 * progress, false, paint)
                    canvas.drawArc(oval, startFrom, -angle / 2 * progress, false, paint)
                    startFrom += angle
                }
            }
            else -> { // fillingType do nothing or "standard"
                data.forEachIndexed { index, datum ->
                    val angle = datum * 360F * smartStatsViewDivider(data.sum())
                    paint.color = colors.getOrElse(index) { randomColor() }
                    canvas.drawArc(oval, startFrom, angle * progress, false, paint)
                    startFrom += angle
                }
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