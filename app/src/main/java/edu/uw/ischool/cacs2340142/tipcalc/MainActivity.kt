package edu.uw.ischool.cacs2340142.tipcalc

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.text.*
import android.widget.*
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val tipRates = resources.getStringArray(R.array.tipPercentages)
        val tipRateAdapter = ArrayAdapter(this, R.layout.spinner_text, tipRates)
        tipRateAdapter.setDropDownViewResource(R.layout.spinner_drop_down_text)

        val tipRateSpinner: Spinner = findViewById(R.id.tipRatePercentage)
        tipRateSpinner.adapter = tipRateAdapter
        tipRateSpinner.setSelection(1)

        val tipButton: Button = findViewById(R.id.tipButton)
        tipButton.isEnabled = false

        val tipAmountText: EditText = findViewById(R.id.tipAmount)

        tipAmountText.setOnClickListener{
            if (tipAmountText.text.toString() == "Amount" || tipAmountText.text.toString().isEmpty()) {
                tipAmountText.setText("$0.00")
                tipAmountText.setSelection(tipAmountText.text.length - 3)
            }
        }

        tipAmountText.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    tipAmountText.removeTextChangedListener(this)
                    var numsOnly = s.toString().replace("[$,.]".toRegex(), "")

                    if (numsOnly.isEmpty()) {
                        numsOnly = "000"
                    } else if (numsOnly.length == 1) {
                        numsOnly = "00$numsOnly"
                    } else if (numsOnly.length == 2) {
                        numsOnly = "0$numsOnly"
                    }
                    val setNum = numsOnly.toDouble() / 100
                    val formattedNum = "$" + String.format("%.2f", setNum)

                    current = formattedNum
                    tipAmountText.setText(formattedNum)
                    tipAmountText.setSelection(formattedNum.length)
                    tipButton.isEnabled = setNum != 0.0
                    tipAmountText.addTextChangedListener(this)
                }
            }
        })

        tipButton.setOnClickListener{
            val amountText = tipAmountText.text.toString().replace("$", "")
            val amountNum = amountText.toDouble()
            val tipRate = tipRateSpinner.selectedItem.toString().replace("%","").toDouble() / 100
            val tipAmount = amountNum * tipRate
            val formattedTipAmount = String.format("%.2f", tipAmount)
            val subtotal = String.format("%.2f", (amountNum + tipAmount))

            Toast.makeText(this, "Tip Amount: $$formattedTipAmount\nTotal: $$subtotal", Toast.LENGTH_SHORT).show()
        }
    }
}

