package net.blakelee.snackapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_snack.view.*
import net.blakelee.model.Snack

class SnackAdapter(private val vm: SnackViewModel) : RecyclerView.Adapter<SnackAdapter.SnackViewHolder>() {

    private val items = vm.filteredSnacks

    private fun getItemViewId() = R.layout.item_snack

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: SnackViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnackViewHolder {
        return SnackViewHolder(LayoutInflater.from(parent.context).inflate(getItemViewId(), parent, false))
    }

    inner class SnackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val checkbox: CheckBox = view.checkbox
        private val green = ContextCompat.getColor(view.context, R.color.green)
        private val red = ContextCompat.getColor(view.context, R.color.red)

        fun onBind(snack: Snack) {
            val color = when(snack) {
                is Snack.Vegetable -> green
                is Snack.NonVegetable -> red
            }

            checkbox.setTextColor(color)
            checkbox.text = snack.value

            checkbox.setOnCheckedChangeListener(null)
            checkbox.isChecked = vm.isSnackSelected(snack)
            checkbox.setOnCheckedChangeListener { _, checked ->
                vm.toggleSnack(snack, checked)
            }
        }
    }
}