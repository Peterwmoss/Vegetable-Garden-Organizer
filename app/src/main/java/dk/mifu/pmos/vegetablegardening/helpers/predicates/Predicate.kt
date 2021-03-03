package dk.mifu.pmos.vegetablegardening.helpers.predicates

import java.io.Serializable

interface Predicate<T>: (T) -> Boolean, Serializable