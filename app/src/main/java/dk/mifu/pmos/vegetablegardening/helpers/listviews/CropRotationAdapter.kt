package dk.mifu.pmos.vegetablegardening.helpers.listviews

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.CropRotationHistoryItem
import kotlin.math.max

class CropRotationAdapter(private val context: Context, private val beds : List<Bed>, private val bedsLookup : Map<Bed, List<CropRotationHistoryItem>>) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return beds.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val bed = beds[groupPosition]
        return bedsLookup[bed]!!.size
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
        placement.text = "Position: ${bed.order + 1}"
        years.text = "${bedsLookup[bed]!![0].seasons} år"
        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val historyItem = getChild(groupPosition, childPosition) as CropRotationHistoryItem
        var view = convertView
        if (view == null) {
            view = View.inflate(context, R.layout.list_item_crop_rotation, null)
        }

        view!!
        val placement : TextView = view.findViewById(R.id.placement_crop_rotation)
        val years : TextView = view.findViewById(R.id.seasons_crop_rotation)

        placement.text = "Position: ${historyItem.order + 1}"
        years.text = "${historyItem.seasons} år tilbage"

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }
}