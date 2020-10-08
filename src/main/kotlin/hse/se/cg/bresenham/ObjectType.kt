package hse.se.cg.bresenham

import java.awt.Point

enum class ObjectType(
    private val representation: String,
    private val objectFactory: (a: Point, b: Point) -> Drawable
) {
    Line(
        "линия",
        { a, b -> Line(a, b) }
    ),
    Circle(
        "окружность",
        { a, b -> Circle(a, b) }
    );

    override fun toString(): String {
        return representation
    }

    fun createObject(a: Point, b: Point): Drawable {
        return objectFactory(a, b)
    }
}
