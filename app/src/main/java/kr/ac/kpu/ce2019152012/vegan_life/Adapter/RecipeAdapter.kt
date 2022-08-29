package kr.ac.kpu.ce2019152012.vegan_life.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.RecipeDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R

class RecipeAdapter:
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    var dataList = mutableListOf<RecipeDataVo>()

    interface OnItemClickListener {
        fun onItemClick(v: View, data: RecipeDataVo, pos: Int)
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_listview,parent,false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

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


}