package pt.demo.calculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pt.demo.calculator.databinding.ActivityMainBinding
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val TAG = "MainActivity"

    private var currentNumber: Double? = null
    private var previousNumber: Double? = null
    private var operation: String? = null //What does the '?' do here? It allows the variable to be null

    private var stringResult: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        val numberButtons = listOf(binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
                                   binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9);

        for (button in numberButtons) {
            button.setOnClickListener (::onNumberClick);
        }

        val operationButtons = listOf(binding.btnPlus, binding.btnMinus, binding.btnMultiply, binding.btnDivision, binding.btnEquals);

        for (button in operationButtons) {
            button.setOnClickListener (::onOperationClick);
        }

        binding.btnEquals.setOnClickListener(::onEqualsClick)
        binding.btnAC.setOnClickListener (::onACClick)
        binding.btnDot.setOnClickListener(::onDotClick)
        binding.btnPerCent.setOnClickListener(::onPercentClick)
        binding.btnSignalChange.setOnClickListener(::onSignChangeClick)

    }

    fun onNumberClick(view: View) {
        val button = view as? Button
        button?.let {
            stringResult += it.text               // append digit to the displayed string
            currentNumber = stringResult.toDoubleOrNull()  // parse it as Double
            binding.resultBox.text = stringResult
            Log.d(
                TAG,
                "onNumberClick: currentNumber=$currentNumber\npreviousNumber=$previousNumber\noperation=$operation\nStringResult=$stringResult"
            )
        }
    }



    fun onOperationClick(view: View) {
        val button = view as? Button
        button?.let {
            if (currentNumber != null) {
                if (previousNumber == null) {
                    previousNumber = currentNumber
                } else {
                    previousNumber = when (operation) {
                        "+" -> previousNumber!! + currentNumber!!
                        "-" -> previousNumber!! - currentNumber!!
                        "*" -> previousNumber!! * currentNumber!!
                        "/" -> if (currentNumber != 0.0) previousNumber!! / currentNumber!! else previousNumber
                        else -> previousNumber
                    }
                }
            }
            operation = it.text.toString()
            stringResult = ""   // reset here so the next number starts fresh
            binding.resultBox.text = operation
            currentNumber = null
        }
        Log.d(TAG, "onOperationClick: currentNumber=$currentNumber\npreviousNumber=$previousNumber\noperation=$operation")
    }


    fun onEqualsClick(view: View){
        if (operation != null && previousNumber != null && currentNumber != null) {
            previousNumber = when(operation){
                "+" -> previousNumber!! + currentNumber!!
                "-" -> previousNumber!! - currentNumber!!
                "*" -> previousNumber!! * currentNumber!!
                "/" -> if (currentNumber != 0.0) previousNumber!! / currentNumber!! else previousNumber
                else -> previousNumber
            }
            stringResult = "$previousNumber"
            binding.resultBox.text = stringResult
            Log.d(TAG, "onEqualsClick: currentNumber=$currentNumber\npreviousNumber=$previousNumber\noperation=$operation")
            currentNumber = null
            operation = null
        }
    }

    fun onACClick(view: View){
        currentNumber = null
        previousNumber = null
        operation = null
        stringResult = ""
        binding.resultBox.text = stringResult
        Log.d(TAG, "onACClick: currentNumber=$currentNumber\npreviousNumber=$previousNumber\noperation=$operation")
    }

    fun onDotClick(view: View) {
        if (!stringResult.contains(".")) {
            if (stringResult.isEmpty()) {
                stringResult = "0."
            } else {
                stringResult += "."
            }
            binding.resultBox.text = stringResult
        }
        Log.d(TAG, "onDotClick: stringResult=$stringResult")
    }


    fun onPercentClick(view: View) {
        val value = stringResult.toDoubleOrNull()
        if (value != null) {
            val percent = value / 100.0
            stringResult = percent.toString()
            currentNumber = percent
            binding.resultBox.text = stringResult
        }
        Log.d(TAG, "onPercentClick: currentNumber=$currentNumber\npreviousNumber=$previousNumber\noperation=$operation")
    }

    fun onSignChangeClick(view: View) {
        val value = stringResult.toDoubleOrNull()
        if (value != null) {
            val negated = -value
            stringResult = if (negated % 1 == 0.0) negated.toInt().toString() else negated.toString()
            currentNumber = negated
            binding.resultBox.text = stringResult
        }
        Log.d(TAG, "onSignChangeClick: currentNumber=$currentNumber\npreviousNumber=$previousNumber\noperation=$operation")
    }
}