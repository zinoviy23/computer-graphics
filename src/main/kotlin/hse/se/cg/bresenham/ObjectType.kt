package hse.se.cg.bresenham

import java.awt.Point

enum class ObjectType(
    private val representation: String,
    val needTwoPoints: Boolean,
    private val objectFactory: (a: Point, b: Point) -> Drawable
) {
    Line(
        "линия",
        true,
        { a, b -> Line(a, b) }
    ),
    Circle(
        "окружность",
        true,
        { a, b -> Circle(a, b) }
    ),
    Ellipsis(
        "овал",
        true,
        { a, b -> Ellipsis(a, b) }
    ),
    LineFloodFill(
        "заливка линиями",
        false,
        { a, _ -> LineFloodFill(a) }
    );

    override fun toString(): String {
        return representation
    }

    fun createObject(a: Point, b: Point): Drawable {
        return objectFactory(a, b)
    }
}
