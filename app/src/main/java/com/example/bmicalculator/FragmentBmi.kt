package com.example.bmicalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.bmicalculator.databinding.FragmentBmiBinding
import kotlinx.coroutines.NonDisposableHandle.parent
import java.math.RoundingMode
import java.text.DecimalFormat


class FragmentBmi : Fragment() {

    private var _binding: FragmentBmiBinding? = null
    private val binding get() = _binding!!

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

//        binding.metricSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                val selectedItem = parent.getItemAtPosition(position).toString()
//
//                val meszSystem = when (selectedItem) {
//                    "Metric System" ->
//                }
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//
//        }

        binding.calculateBtn.setOnClickListener {
            calculateBmi()
        }

    }

    private fun bmi(height: Double, weight: Double): Double {

        return (weight / (height * height))

    }

    private fun calculateBmi() {
        val height = binding.heightEt.text.toString().toDouble()
        val weight = binding.weightEt.text.toString().toDouble()

        val measurementSystem = binding.metricSpinner.selectedItem.toString()

        val result = if (measurementSystem == "US Customary System") {
            bmi(height, weight) * 703
        } else {
            bmi(height, weight)
        }

        // Rounding up the result to 2 d.p
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_UP

        val roundedResult = df.format((result))

//        binding.outputTv.text = "Your BMI is: $roundedResult"
        binding.numResultTv.text = "$roundedResult"
    }

//    private fun metricSystem() {
//
//    }

    // TODO
    // Have height options for metres and cm and Inches and Feet
    // Have weight options for kgs and Pounds
    // Conversion operations


    /* Conversions half-done - Issue:
    * Height is converted to inches - what if I want in feet and inches?
    * Need to change height to match conversion
    * use sharedPreferences to retain conversion system
    * Based on conversion system, let it reflect on hint
    */


    // Describe BMI result with different colours(blue, green, yellow, red)
    // Create a status bar showing the result and color



}