package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.viewmodels.SeasonViewModel

class CurrentSeasonPredicate(private val seasonViewModel: SeasonViewModel): Predicate<Bed> {

    override fun invoke(bed: Bed): Boolean {
        return seasonViewModel.currentSeason == bed.season
    }
}