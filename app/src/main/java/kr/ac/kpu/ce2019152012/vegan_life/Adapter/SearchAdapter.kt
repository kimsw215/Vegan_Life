package kr.ac.kpu.ce2019152012.vegan_life.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.CalendarFoodListDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R
import java.nio.file.Files.size

class SearchAdapter(var searchs: ArrayList<CalendarFoodListDataVo>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>(), Filterable {

    var searchList = ArrayList<CalendarFoodListDataVo>()

    var itemFilter = ItemFilter()

    init {
        searchList.addAll(searchs)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: CalendarFoodListDataVo, post:Int)
    }

    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.calendar_listview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = searchList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val foodname = itemView.findViewById<TextView>(R.id.FoodName)
        private val foodkcal = itemView.findViewById<TextView>(R.id.FoodKcal)

        fun bind(item: CalendarFoodListDataVo) {
            foodname.text = item.foodname
            foodkcal.text = item.kcal
            val pos = adapterPosition
            if( pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,item,pos) }
            }
        }
    }

    // -- filter
    override fun getFilter(): Filter {
        return itemFilter
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()

            //검색이 필요없을 경우를 위해 원본 배열을 복제
            val filteredList: ArrayList<CalendarFoodListDataVo> = ArrayList<CalendarFoodListDataVo>()
            //공백제외 아무런 값이 없을 경우 -> 원본 배열
            if (filterString.trim { it <= ' ' }.isEmpty()) {
                results.values = searchs
                results.count = searchs.size

                return results
                // 공백제외 6글자 이하인 경우 -> 이름으로만 검색
            } else if (filterString.trim { it <= ' ' }.length <= 6) {
                for (data in searchs){
                    if (data.foodname!!.contains(filterString)){
                        filteredList.add(data)
                    }
                }
            }
            results.values = filteredList
            results.count = filteredList.size

            return results
        }
        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            searchList.clear()
            searchList.addAll(filterResults?.values as ArrayList<CalendarFoodListDataVo>)
            notifyDataSetChanged()
        }
    }
}