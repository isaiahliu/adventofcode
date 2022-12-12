fun main() {
    var startRow = 0
    var startColumn = 0

    var endRow = 0
    var endColumn = 0
    val heightmap = input.mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, c ->
            when (c) {
                'S' -> {
                    startRow = rowIndex
                    startColumn = columnIndex

                    'a' - 'a'
                }

                'E' -> {
                    endRow = rowIndex
                    endColumn = columnIndex

                    'z' - 'a'
                }

                else -> {
                    c - 'a'
                }
            }
        }.toIntArray()
    }.toTypedArray()

    fun process(startIndex: Int, endIndex: Int, heightCheck: (Int) -> Boolean, arrivedCheck: (Pair<Int, Int>) -> Boolean): Int {
        val arrived = hashSetOf<String>()
        fun walked(pair: Pair<Int, Int>): Boolean {
            return !arrived.add("${pair.first}_${pair.second}")
        }

        val tasks = arrayListOf(startIndex to endIndex)

        var step = 0
        while (true) {
            val currentTasks = tasks.toList()
            tasks.clear()

            for (task in currentTasks) {
                if (arrivedCheck(task)) {
                    return step
                }

                if (walked(task)) {
                    continue
                }

                val currentHeight = heightmap[task.first][task.second]

                heightmap.getOrNull(task.first - 1)?.getOrNull(task.second)?.takeIf { heightCheck(it - currentHeight) }?.also { tasks += task.first - 1 to task.second }
                heightmap.getOrNull(task.first + 1)?.getOrNull(task.second)?.takeIf { heightCheck(it - currentHeight) }?.also { tasks += task.first + 1 to task.second }
                heightmap.getOrNull(task.first)?.getOrNull(task.second - 1)?.takeIf { heightCheck(it - currentHeight) }?.also { tasks += task.first to task.second - 1 }
                heightmap.getOrNull(task.first)?.getOrNull(task.second + 1)?.takeIf { heightCheck(it - currentHeight) }?.also { tasks += task.first to task.second + 1 }
            }

            step++
        }
    }
    println(process(startRow, startColumn, { it <= 1 }) { (r, c) ->
        r == endRow && c == endColumn
    })

    println(process(endRow, endColumn, { it >= -1 }) { (r, c) ->
        heightmap[r][c] == 0
    })
}