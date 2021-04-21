package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.models.Bed

class CurrentSeasonPredicate(private val year: Int): Predicate<Bed> {

    override fun invoke(bed: Bed): Boolean {
        return year == bed.season
    }
}