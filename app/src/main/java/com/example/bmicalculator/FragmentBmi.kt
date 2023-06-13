package com.example.bmicalculator

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.bmicalculator.databinding.FragmentBmiBinding
import com.google.android.material.textview.MaterialTextView
import java.lang.Double.NaN
import java.math.RoundingMode
import java.text.DecimalFormat


class FragmentBmi : Fragment() {

    private var _binding: FragmentBmiBinding? = null
    private val binding get() = _binding!!

    private var previousSelectedMetricSystem: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentBmiBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val measurementSystems = arrayOf("Metric System", "US Customary System")

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, measurementSystems)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.metricSpinner.adapter = spinnerAdapter

        binding.metricSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val measurementSystem = parent.getItemAtPosition(position).toString()

                if (measurementSystem != previousSelectedMetricSystem) {
                    binding.heightEt.text?.clear()
                    binding.heightCmEt.text?.clear()
                    binding.weightEt.text?.clear()
                    binding.numResultTv.text = "0.0"
                    binding.commentTv.visibility = View.GONE
                    binding.resultCommentTv.text = ""

                }

                previousSelectedMetricSystem = measurementSystem

                if (measurementSystem == "US Customary System") {
                    binding.heightEtL.hint = getString(R.string.hint_us_customary)
                    binding.heightCmEtL.hint = getString(R.string.hint_us_customary_inches)
                    binding.weightEtL.hint = getString(R.string.hint_us_customary_pounds)
                } else {
                    binding.heightEtL.hint = getString(R.string.hint_metric)
                    binding.heightCmEtL.hint = getString(R.string.hint_metric_cm)
                    binding.weightEtL.hint = getString(R.string.hint_metric_weight)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case where no item is selected
            }
        }

        binding.calculateBtn.setOnClickListener {

            // clear textViews first before performing the calculation
            binding.numResultTv.text = ""
            binding.resultCommentTv.text = ""
            binding.commentTv.visibility = View.VISIBLE
            calculateBmi()
        }

    }

    private fun bmi(height: Double, weight: Double): Double {

        return (weight / (height * height))
    }

    private fun calculateBmi() {
//        val height1 = binding.heightEt.text.toString().toDouble()
//        val height2 = binding.heightCmEt.text.toString().toDouble()

        // NB: Handle null values!! -> use toDoubleOrNull() ?: 0.0
        val height1 = binding.heightEt.text.toString().toDoubleOrNull() ?: 0.0
        val height2 = binding.heightCmEt.text.toString().toDoubleOrNull() ?: 0.0


        val weight = binding.weightEt.text.toString().toDoubleOrNull() ?: 0.0

        val measurementSystem = binding.metricSpinner.selectedItem.toString()

        var result = if (measurementSystem == "US Customary System") {

            // set Toast showing that if inches value is set to greater than 11.0,
            // it will be set by default to 11.0, also update value on editText to show max value
//            if (height2 > 11.0) {
//                Toast.makeText(requireContext(), "Inches is invalid,automatically converted to  11.0", Toast.LENGTH_SHORT).show()
//            }
            val height = (height1 * 12) + height2.coerceIn(0.0, 11.0)
            // assumes height is in inchesresult >= 30.00
            bmi(height, weight) * 703
        } else {

            // set Toast showing that if cm value is set to greater than 99.9,
            // it will be set by default to 99.9
//            if (height2 > 99.9) {
//            Toast.makeText(requireContext(), "Inches is invalid,automatically converted to  99.9", Toast.LENGTH_SHORT).show()

//            }
            val height = height1 + (height2.coerceIn(0.0, 99.9) / 100)
            // height is in metres
            bmi(height, weight)
        }

        // Rounding up the result to 2 d.p
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_UP

        val roundedResult = df.format((result))

//        binding.outputTv.text = "Your BMI is: $roundedResult"
        binding.numResultTv.text = roundedResult

        // set rules for color of textView based on result.

        val comment = binding.resultCommentTv.text.toString()

        if (result.isNaN()) {
            Toast.makeText(requireContext(), "Calculation is invalid (NaN). Check your inputs", Toast.LENGTH_SHORT).show()
//            result = 0.0
            binding.numResultTv.text = "0.0"
            binding.commentTv.visibility = View.GONE

        } else if (result <= 18.50) {
//            val status: String = getString(R.string.underweight)
            binding.resultCommentTv.setTextColor(Color.BLUE)
//            binding.resultCommentTv.text = "$comment " + getString(R.string.healthy)
            binding.resultCommentTv.text = buildString {
                append(comment)
                append(" ")
                append(getString(R.string.underweight))
            }
        }
        else if(result in (18.5..24.99)) {
//            val status: String = getString(R.string.healthy)
            binding.resultCommentTv.setTextColor(Color.GREEN)
//            binding.resultCommentTv.text = "$comment " + getString(R.string.healthy)
            binding.resultCommentTv.text = buildString {
                append(comment)
                append(" ")
                append(getString(R.string.healthy))
            }

        }
        else if (result in (25.0..29.99)) {
//            val status: String = getString(R.string.overweight)
            binding.resultCommentTv.setTextColor(Color.rgb(226, 107, 10))
//            binding.resultCommentTv.text = "$comment " + getString(R.string.overweight)
            binding.resultCommentTv.text = buildString {
                append(comment)
                append(" ")
                append(getString(R.string.overweight))
            }

        }
        else if (result >= 30.00) {
//            val status: String = getString(R.string.obese)
            binding.resultCommentTv.setTextColor(Color.RED)
//            binding.resultCommentTv.text = "$comment " + getString(R.string.obese)
            binding.resultCommentTv.text = buildString {
                append(comment)
                append(" ")
                append(getString(R.string.obese))
            }

        }

    }


    // TODO
    /*
    handle case for null values
     use sharedPreferences to retain conversion system
     Describe BMI result with different colours(blue, green, yellow, red)
     Create a status bar showing the result and color - color;Success || status; Buggy

     Make keyboard disappear/ drop automatically after calculateBtn is clicked

     */




}

