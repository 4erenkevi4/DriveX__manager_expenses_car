package com.example.drivex.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R

class ServiceAdapter(private val click: (String,View) -> Unit):
    RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        val onClick: (String,View) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val root: View = view.findViewById(R.id.item_root)
        private val nameService = root.findViewById<TextView>(R.id.name_service)
        private val iconService = root.findViewById<View>(R.id.icon_Service)

        fun bind(service: String) {
            nameService.text = service
        }
        fun oClick(string: String,view: View) {
            root.setOnClickListener {
                onClick(string,view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_service,
            parent,
            false
        )
        return ViewHolder(view,click)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listService[position]
        holder.bind(item)
        holder.oClick(item,holder.itemView)
    }

    override fun getItemCount(): Int {
        return listService.size
    }

    var listService:List<String> = listOf(
        "Шиномонтаж",
        "Мойка автомобиля",
        "Замена масла двигателя",
        "Замена масла в КПП",
        "Замена воздушного фильтра",
        "Замена маслянного фильтра",
        "Замена салонного фильтра",
        "Замена топливного фильтра",
        "Замена тормозной жидкости",
        "Замена передних тормозных колодок",
        "Замена задних тормозных колодок",
        "Замена передних тормозных дисков",
        "Замена задних тормозных дисков",
        "Замена свечей зажигания",
        "Замена высоковольтных проводов",
        "Замена катушек зажигания,",
        "Заправка кондиционера",
        "Обслуживание кондиционера",
        "Замена охлаждающей жидкости",
        "Развал-схождение",
        "Обслуживание подвески",
        "Замена ремня ГРМ",
        "Замена АКБ",
        "Замена жидкости ГУР",
        "Обслуживание КПП",
        "Обслуживание ДВС",
        "Обслуживание топливной системы",
        "Обслуживание Рулевого управлления",
        "Прочее"
    )
}