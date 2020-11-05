package hse.se.cg.bresenham

import hse.se.cg.bresenham.math.fixPointToBeLine
import java.awt.Point

enum class ObjectType(
    private val representation: String,
    val addPointOnRelease: Boolean,
    val fixedNumberOfPoints: Boolean,
    private val objectFactory: (points: List<Point>) -> Pair<Drawable?, Int>
) {
    Line(
        "линия",
        true,
        true,
        factory@ { points ->
            if (points.size < 2) return@factory null to points.size
            val (a, b) = points
            Line(a, b) to 0
        }
    ),
    Circle(
        "окружность",
        true,
        true,
        factory@ { points ->
            if (points.size < 2) return@factory null to points.size
            val (a, b) = points
            Circle(a, b) to 0
        }
    ),
    Ellipsis(
        "овал",
        true,
        true,
        factory@ { points ->
            if (points.size < 2) return@factory null to points.size
            val (a, b) = points
            Line(a, b) to 0
        }
    ),
    LineFloodFill(
        "заливка линиями",
        false,
        true,
        { points ->
            points.firstOrNull()?.let { LineFloodFill(it) to 0 } ?: null to points.size
        }
    ),
    Bezier3Curve(
        "кривая Безье 3го порядка",
        false,
        true,
        { points ->
            if (points.size < 4) null to points.size
            else {
                val (p0, p1, p2, p3) = points
                Bezier3Curve(p0, p1, p2, p3) to 0
            }
        }
    ),
    BezierCurve(
        "кривая Безье любого порядка",
        false,
        false,
        { points ->
            if (points.size < 2) null to points.size
            else {
                BezierCurve(ArrayList(points)) to 0
            }
        }
    ), CompoundBezierCurve3(
        "составная кривая Безье (3)",
        false,
        false,
        { points ->
            if (points.size < 4) null to points.size
            else {
                val validPoints = points.size - ((points.size - 4) % 3)
                val curves = makeOpenCompoundCurve(points, validPoints)
                CompoundBezierCurve3(curves) to (points.size - validPoints)
            }
        }
    ), ClosedCompoundBezierCurve3(
        "составная замкнутая кривая Безье (3)",
        false,
        false,
        { points ->
            if (points.size < 6) {
                if (points.size >= 4) {
                    Bezier3Curve(points[0], points[1], points[2], points[3]) to points.size - 4
                } else {
                    null to points.size
                }
            } else {
                val validPoints = points.size - ((points.size - 4) % 3)
                val curves =makeOpenCompoundCurve(points, validPoints)

                if ((points.size - 4) % 3 == 2) {
                    val p0 = points[points.lastIndex - 2]
                    val p1 = fixPointToBeLine(points[points.lastIndex - 3], p0, points[points.lastIndex - 1])
                    val p2 = fixPointToBeLine(points[1], points[0], points.last())
                    val p3 = points[0]

                    curves += Bezier3Curve(p0, p1, p2, p3)
                    CompoundBezierCurve3(curves) to 0
                } else {
                    CompoundBezierCurve3(curves) to (points.size - 4) % 3
                }
            }
        }
    );

    override fun toString(): String {
        return representation
    }

    fun createObject(points: List<Point>): Pair<Drawable?, Int> {
        return objectFactory(points)
    }
}

private fun makeOpenCompoundCurve(
    points: List<Point>,
    validPoints: Int
): MutableList<Bezier3Curve> {
    val curves = mutableListOf(Bezier3Curve(points[0], points[1], points[2], points[3]))
    for (additionalCurveBegin in 4 until validPoints step 3) {
        val p0 = points[additionalCurveBegin - 1]
        val p1 = fixPointToBeLine(points[additionalCurveBegin - 2], p0, points[additionalCurveBegin])
        val p2 = points[additionalCurveBegin + 1]
        val p3 = points[additionalCurveBegin + 2]
        curves += Bezier3Curve(p0, p1, p2, p3)
    }
    return curves
}
