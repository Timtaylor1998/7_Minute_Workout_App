package eu.tutorials.a7minuteworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import eu.tutorials.a7minuteworkoutapp.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"
    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW

    private var binding: ActivityBmiBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener{ _, checkedId: Int ->

            if (checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUSUnitsView()
            }

        }

        binding?.btnCalculateUnits?.setOnClickListener{

            calculateUnits()

        }

    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilUSUnitWeight?.visibility = View.GONE
        binding?.tilUSUnitHeightFeet?.visibility = View.GONE
        binding?.tilUSUnitHeightInch?.visibility = View.GONE

        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE
        binding?.tilUSUnitWeight?.visibility = View.VISIBLE
        binding?.tilUSUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilUSUnitHeightInch?.visibility = View.VISIBLE

        binding?.etUSUnitHeightFeet?.text!!.clear()
        binding?.etUSUnitHeightInch?.text!!.clear()
        binding?.etUSUnitWeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun displayBMIResults(bmi: Float){

        val bmiLabel : String
        val bmiDescription : String

        if(bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "OOps! You really need to take better care of yourself! Eat more"

        }else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "OOps! You really need to take better care of yourself! Eat more"

        }else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "OOps! You really need to take better care of yourself! Eat more"

        }else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations You are in good shape!"

        }else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "OOps! You really need to take better care of yourself! Workout more"

        }else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "OOps! You really need to take better care of yourself! Workout more"

        }else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now"

        }else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now"

        }

        val bmiValue = BigDecimal(bmi.toDouble())
            .setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription

    }

    private fun validateMetricUnits():Boolean{
        var isValid = true

        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if(binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }

        return isValid
    }

    private fun calculateUnits(){
        if(currentVisibleView == METRIC_UNITS_VIEW){
            if(validateMetricUnits()){
                val heightValue : Float = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100

                val weightValue : Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                val bmi = weightValue / (heightValue * heightValue)

                displayBMIResults(bmi)
            }else{
                Toast.makeText(this@BMIActivity,
                    "Please enter valid values.",
                    Toast.LENGTH_SHORT)
                    .show()
                }
            }else{
                if (validateUSUnits()){
                    val usUnitHeightValueFeet: String =
                        binding?.etUSUnitHeightFeet?.text.toString()
                    val usUnitHeightValueInch: String =
                        binding?.etUSUnitHeightInch?.text.toString()
                    val usUnitValueWeight: Float =
                        binding?.etUSUnitWeight?.text.toString().toFloat()

                    val heightValue =
                        usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                    val bmi = 703 * (usUnitValueWeight / (heightValue * heightValue))

                    displayBMIResults(bmi)

                }else {
                    Toast.makeText(
                        this@BMIActivity,
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
        }


    private fun validateUSUnits():Boolean {
        var isValid = true

        when {
            binding?.etUSUnitWeight?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUSUnitHeightFeet?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUSUnitHeightInch?.text.toString().isEmpty() -> {
                isValid = false
            }
        }

            return isValid
        }



}