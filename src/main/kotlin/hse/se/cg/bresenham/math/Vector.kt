package hse.se.cg.bresenham.math

class Vector(data: List<Double>) {
    private val data: List<Double> = ArrayList(data)

    val length
        get() = data.size

    operator fun get(i: Int): Double = data[i]

    operator fun times(v: Vector): Double {
        require(length == v.length)
        return data.zip(v.data).fold(0.0) { sum, (a, b) ->
            sum + a * b
        }
    }

    operator fun component1(): Double {
        require(length > 0)
        return data[0]
    }

    operator fun component2(): Double {
        require(length > 1)
        return data[1]
    }
}

operator fun Matrix.times(v: Vector): Vector {
    require(columns == v.length)
    return Vector(rowsIterable.map { it * v })
}
