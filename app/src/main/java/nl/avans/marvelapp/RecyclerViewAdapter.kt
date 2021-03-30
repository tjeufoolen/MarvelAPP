package nl.avans.marvelapp
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nl.avans.marvelapp.fragments.interfaces.IRowObject

/*
 * source: https://www.journaldev.com/24041/android-recyclerview-load-more-endless-scrolling
 */
abstract class RecyclerViewAdapter(private var itemList: List<IRowObject?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected val VIEW_TYPE_ITEM = 0
    protected val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
            ItemViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.items_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ItemViewHolder) {
            populateItemRows(viewHolder, position)
        } else if (viewHolder is LoadingViewHolder) {
            showLoadingView(viewHolder, position)
        }
    }

    override fun getItemCount(): Int {
        return if (itemList == null) 0 else itemList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList!![position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    protected inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItem: TextView = itemView.findViewById(R.id.tvItem)
    }

    protected inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    protected fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) {
        //ProgressBar would be displayed
    }

    protected fun populateItemRows(viewHolder: ItemViewHolder, position: Int) {
        val item = itemList!![position]
        if (item != null) {
            viewHolder.tvItem.text = item.getRowTitle()
        }
    }
}