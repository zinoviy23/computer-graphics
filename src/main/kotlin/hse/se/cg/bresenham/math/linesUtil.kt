package hse.se.cg.bresenham.math

import java.awt.Point
import kotlin.math.abs

sealed class NumberOrInfinity {
    interface NonZero

    object Infinity : NumberOrInfinity(), NonZero
    sealed class Number : NumberOrInfinity() {
        abstract val number: Double

        operator fun times(double: Double) = number * double

        object Zero : Number() {
            override val number: Double = 0.0
        }

        class NonZeroNumber(override val number: Double) : Number(), NonZero
    }
}

operator fun Double.div(number: NumberOrInfinity.NonZero) = when (number) {
    is NumberOrInfinity.Infinity -> 0.0
    is NumberOrInfinity.Number.NonZeroNumber -> this / number.number
    else -> error("cannot be here")
}

fun incline(a: Point, b: Point): NumberOrInfinity {
    if (a.x - b.x == 0) return NumberOrInfinity.Infinity
    if ((a.y - b.y) == 0) return NumberOrInfinity.Number.Zero
    return NumberOrInfinity.Number.NonZeroNumber((a.y.toDouble() - b.y) / (a.x.toDouble() - b.x))
}

fun fixPointToBeLine(begin: Point, end: Point, point: Point): Point {
    val incline = incline(begin, end)
    if (incline is NumberOrInfinity.Infinity) {
        return if (sign(begin.y - end.y) != sign(end.y - point.y) && point.y != end.y) {
            val y = end.y + sign(begin.y - end.y) * abs(begin.y - point.y)
            Point(begin.x, y)
        } else {
            Point(begin.x, point.y)
        }
    }
    if (incline !is NumberOrInfinity.Number) throw AssertionError()

    val b = begin.y - incline * begin.x.toDouble()

    val x = if (sign(begin.x - end.x) != sign(end.x - point.x) && point.x != end.x) {
        end.x - sign(begin.x - end.x) * abs(begin.x - point.x)
    } else {
        point.x
    }

    val y = (incline * x.toDouble() + b).toInt()

    return Point(x, y)
}

fun sign(n: Int) = when {
    n == 0 -> 0
    n < 0 -> -1
    else -> 1
}
