package hse.se.cg.bresenham.math

class Matrix(data: List<List<Double>>) {
    val rows = data.size.also { require(it > 0) }
    val columns = data[0].size.also { require(it > 0) }

    private val data = data.map {
        require(it.size == columns)
        it
    }

    operator fun get(row: Int, column: Int): Double {
        require(row in 0 until rows)
        require(column in 0 until columns)

        return data[row][column]
    }

    val rowsIterable: Iterable<Vector>
    get() = data.asSequence().map { Vector(it) }.asIterable()

    val transposed: Matrix
        get() = Matrix(
            (0 until columns).map { column ->
                data.map { it[column] }
            }
        )
}
