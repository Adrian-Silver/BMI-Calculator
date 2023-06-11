package com.example.bmicalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bmicalculator.databinding.FragmentBmiBinding
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

        val result = bmi(height, weight)

        // Rounding up the result to 2 d.p
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_UP

        val roundedResult = df.format((result))

        binding.outputTv.text = "Your BMI is: $roundedResult"
    }

    // TODO
    // Have height options for metres and cm and Inches and Feet
    // Have weight options for kgs and Pounds
    // Conversion operations

    // Describe BMI result with different colours(blue, green, yellow, red)


}