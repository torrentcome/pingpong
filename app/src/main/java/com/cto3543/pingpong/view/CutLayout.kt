package com.cto3543.pingpong.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by cto3543 on 04/05/2017.
 */
class CutLayout : FrameLayout {

    var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mode: Xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    var path: Path = Path()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun dispatchDraw(canvas: Canvas?) {
        var saveCount = canvas?.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG) as Int
        super.dispatchDraw(canvas)

        paint.xfermode = mode
        path.reset()
        path.moveTo(0F, height.toFloat())
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(width.toFloat(), height.toFloat() - (height.toFloat() / 2))
        path.close()
        canvas.drawPath(path, paint)

        canvas.restoreToCount(saveCount)
        paint.xfermode = null
    }
}