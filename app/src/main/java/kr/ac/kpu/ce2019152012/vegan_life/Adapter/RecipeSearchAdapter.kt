package kr.ac.kpu.ce2019152012.vegan_life.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.CalendarFoodListDataVo
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.RecipeDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R
import java.nio.file.Files.size

class RecipeSearchAdapter(var searchs: ArrayList<RecipeDataVo>) :
    RecyclerView.Adapter<RecipeSearchAdapter.ViewHolder>(), Filterable {

    var searchList = ArrayList<RecipeDataVo>()

    var itemFilter = ItemFilter()

    init {
        searchList.addAll(searchs)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: RecipeDataVo, post:Int)
    }

    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_listview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = searchList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val photo = itemView.findViewById<ImageView>(R.id.rv_photo)
        private val name = itemView.findViewById<TextView>(R.id.rv_name)

        fun bind(item: RecipeDataVo){
            name.text = item.recipename
            Glide.with(itemView).load(item.recipephoto).into(photo)

            val pos = adapterPosition
            if(pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener { listener?.onItemClick(itemView, item, pos) }
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
            val filteredList: ArrayList<RecipeDataVo> = ArrayList<RecipeDataVo>()
            //공백제외 아무런 값이 없을 경우 -> 원본 배열
            if (filterString.trim { it <= ' ' }.isEmpty()) {
                results.values = searchs
                results.count = searchs.size

                return results
                // 공백제외 2글자 이하인 경우 -> 이름으로만 검색
            } else if (filterString.trim { it <= ' ' }.length <= 2) {
                for (data in searchs){
                    if (data.recipename!!.contains(filterString)){
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
            searchList.addAll(filterResults?.values as ArrayList<RecipeDataVo>)
            notifyDataSetChanged()
        }
    }
}