package dk.mifu.pmos.vegetablegardening.helpers

class ListExtension {
    companion object {
        val <T> List<T>.tail: List<T>
            get() = drop(1)

        val <T> List<T>.head: T?
            get() = firstOrNull()
    }

}