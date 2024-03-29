package dk.mifu.pmos.vegetablegardening.helpers.listviews

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.CropRotationHistoryItem

class CropRotationAdapter(private val context: Context, private val beds : List<Bed>, private val bedsLookup : Map<Bed, List<CropRotationHistoryItem>>) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return beds.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val bed = beds[groupPosition]
        return bedsLookup[bed]!!.size - 1
    }

    override fun getGroup(groupPosition: Int): Any {
        return beds[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        val bed = beds[groupPosition]
        return bedsLookup[bed]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val bed = getGroup(groupPosition) as Bed
        var view = convertView
        if (view == null) {
            view = View.inflate(context, R.layout.list_group_crop_rotation, null)
        }
        view!!
        val name : TextView = view.findViewById(R.id.bed_name_crop_rotation)
        val placement : TextView = view.findViewById(R.id.placement_crop_rotation)
        val years : TextView = view.findViewById(R.id.seasons_crop_rotation)

        name.text = bed.name
        placement.text = context.getString(R.string.position, bed.order + 1)
        years.text = context.getString(R.string.current_year, bedsLookup[bed]!![0].seasons)
        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val bed = getGroup(groupPosition) as Bed
        val historyItem = getChild(groupPosition, childPosition+1) as CropRotationHistoryItem
        var view = convertView
        if (view == null) {
            view = View.inflate(context, R.layout.list_item_crop_rotation, null)
        }

        view!!
        val placement : TextView = view.findViewById(R.id.placement_crop_rotation)
        val years : TextView = view.findViewById(R.id.seasons_crop_rotation)

        placement.text = context.getString(R.string.position, historyItem.order + 1)
        years.text =
                when {
                    bed.plants.isNullOrEmpty() -> context.getString(R.string.seasons_empty_bed)
                    historyItem.seasons <= 0 -> context.getString(R.string.plantable_again)
                    else -> context.getString(R.string.years_left, historyItem.seasons)
                }

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }
}