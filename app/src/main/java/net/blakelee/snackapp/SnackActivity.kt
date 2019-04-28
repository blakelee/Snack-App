package net.blakelee.snackapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_snack.*
import kotlinx.android.synthetic.main.dialog_add_item.view.*
import net.blakelee.model.DummySnackModel

class SnackActivity : AppCompatActivity() {

    private lateinit var viewModel: SnackViewModel
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: SnackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snack)

        viewModel = SnackViewModel(DummySnackModel())

        recycler = findViewById(R.id.recycler)
        adapter = SnackAdapter(viewModel)
        recycler.adapter = adapter

        setupListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_add -> {
                showAddItemDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupListeners() {
        veggieCb.setOnCheckedChangeListener { _, checked ->
            viewModel.toggleVeggieFilter(checked)
            adapter.notifyDataSetChanged()
        }

        nonVeggieCb.setOnCheckedChangeListener { _, checked ->
            viewModel.toggleNonVeggieFilter(checked)
            adapter.notifyDataSetChanged()
        }

        summary.setOnClickListener {
            showSummaryDialog()
        }
    }

    private fun showAddItemDialog() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_add_item, null)

        val snackEditText: EditText = view.snackEditText
        val veggie = view.veggieRadioButton

        snackEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                snackEditText.error = null
            }
        })

        val dialog = builder.setView(view)
            .setPositiveButton(R.string.save, null)
            .create()

        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val snackText = snackEditText.text.toString()
                if (snackText.isEmpty()) {
                    snackEditText.error = getString(R.string.add_snack_error)
                } else {
                    viewModel.addSnack(snackText, veggie.isChecked, veggieCb.isChecked, nonVeggieCb.isChecked)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun showSummaryDialog() {
        val builder = AlertDialog.Builder(this)

        viewModel.placeOrder()

        val selected = viewModel.snacksSelected.sortedBy { it.value.toLowerCase() }.joinToString(", ") { it.value }
        val text = if (selected.isEmpty()) {
            getString(R.string.summary_empty)
        } else {
            selected
        }

        builder.setTitle(R.string.summary)
            .setMessage(text)
            .setPositiveButton(android.R.string.ok, null)
            .setOnDismissListener {
                viewModel.resetSnacksSelected()
                veggieCb.isChecked = true
                nonVeggieCb.isChecked = true
                adapter.notifyDataSetChanged()
            }
            .show()
    }
}
