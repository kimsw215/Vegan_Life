package kr.ac.kpu.ce2019152012.vegan_life.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.CalendarFoodListDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R

class CalendarLineaAdapter:
    RecyclerView.Adapter<CalendarLineaAdapter.ViewHolder>() {

    var dataList = mutableListOf<CalendarFoodListDataVo>()

    interface OnItemClickListener {
        fun onItemClick(v: View, data: CalendarFoodListDataVo, pos: Int)
//        val onClickDelte: (data : CalendarFoodListDataVo) -> Unit
    }

    private var listener : OnItemClickListener?= null
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_listview,parent,false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val foodName = itemView.findViewById<TextView>(R.id.FoodName)
        private val foodkcal = itemView.findViewById<TextView>(R.id.FoodKcal)
        private val delete = itemView.findViewById<TextView>(R.id.FoodDelete)

        fun bind(item: CalendarFoodListDataVo){
            foodName.text = item.foodname
            foodkcal.text = item.kcal
            delete.text = item.delete

            val pos = adapterPosition
            if(pos != RecyclerView.NO_POSITION){
                delete.setOnClickListener { listener?.onItemClick(itemView, item, pos) }
            }
        }
    }


}