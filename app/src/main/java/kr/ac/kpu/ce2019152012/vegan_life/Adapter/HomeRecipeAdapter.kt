package kr.ac.kpu.ce2019152012.vegan_life.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.HomeRecipeDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R

class HomeRecipeAdapter:
    RecyclerView.Adapter<HomeRecipeAdapter.ViewHolder>() {

    var dataList = mutableListOf<HomeRecipeDataVo>()

    interface OnItemClickListener {
        fun onItemClick(v: View, data: HomeRecipeDataVo, pos: Int)
    }

    private var listener : OnItemClickListener?= null
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    // viewType은 변수명 그대로 viewType에 의해 구분되어 들어오는 값
    // onCreateViewHolder가 호출 전 getItemViewType(position: Int):Int 함수를 먼저 호출하며
    // 리턴 값이 넘겨지는 것이다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_listview,parent,false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val photo = itemView.findViewById<ImageView>(R.id.rv_photo)
        private val name = itemView.findViewById<TextView>(R.id.rv_name)

        fun bind(item: HomeRecipeDataVo){
            name.text = item.recipename
            Glide.with(itemView).load(item.recipephoto).into(photo)

            val pos = adapterPosition
            if(pos != RecyclerView.NO_POSITION){
                itemView.setOnClickListener { listener?.onItemClick(itemView, item, pos) }
            }
        }
    }

}