package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.models.Bed

class HigherOrderPredicate(private val thisBed: Bed) : Predicate<Bed> {
    override fun invoke(otherBed: Bed): Boolean {
        return otherBed.order > thisBed.order
    }

}