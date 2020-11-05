package hse.se.cg.bresenham

import java.awt.Point

enum class ObjectType(
    private val representation: String,
    val addPointOnRelease: Boolean,
    val fixedNumberOfPoints: Boolean,
    private val objectFactory: (points: List<Point>) -> Drawable?
) {
    Line(
        "линия",
        true,
        true,
        factory@ { points ->
            if (points.size < 2) return@factory null
            val (a, b) = points
            Line(a, b)
        }
    ),
    Circle(
        "окружность",
        true,
        true,
        factory@ { points ->
            if (points.size < 2) return@factory null
            val (a, b) = points
            Circle(a, b)
        }
    ),
    Ellipsis(
        "овал",
        true,
        true,
        factory@ { points ->
            if (points.size < 2) return@factory null
            val (a, b) = points
            Line(a, b)
        }
    ),
    LineFloodFill(
        "заливка линиями",
        false,
        true,
        { points ->
            points.firstOrNull()?.let { LineFloodFill(it) }
        }
    ),
    Bezier3Curve(
        "кривая Безье 3го порядка",
        false,
        true,
        { points ->
            if (points.size < 4) null
            else {
                val (p0, p1, p2, p3) = points
                Bezier3Curve(p0, p1, p2, p3)
            }
        }
    ),
    BezierCurve(
        "кривая Безье любого порядка",
        false,
        false,
        { points ->
            if (points.size < 2) null
            else {
                BezierCurve(ArrayList(points))
            }
        }
    );

    override fun toString(): String {
        return representation
    }

    fun createObject(points: List<Point>): Drawable? {
        return objectFactory(points)
    }
}
