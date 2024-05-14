package com.example.mtgfamiliar

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mtgfamiliar.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    // Handler for managing repeated function execution
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 100 //milliseconds

    //track if button is held down
    private var isButtonHeldDown = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
    override fun onResume() {
        super.onResume()
    }
    */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.HealthDisplay.text = sharedViewModel.healthCounter.toString()
        binding.StormDisplay.text = sharedViewModel.stormCounter.toString()
        binding.PoisonDisplay.text = sharedViewModel.poisonCounter.toString()
        binding.Commanderdmgp1.text = sharedViewModel.commanderDmgp1.toString()
        binding.Commanderdmgp2.text = sharedViewModel.commanderDmgp2.toString()
        binding.Commanderdmgp3.text = sharedViewModel.commanderDmgp3.toString()

        //define and set touch listeners for buttons
        setUpTouchListener(binding.Gain1Health, ::healthIncrease, binding.HealthDisplay)
        setUpTouchListener(binding.Lose1Health, ::healthDecrease, binding.HealthDisplay)
        setUpTouchListener(binding.GainStorm, ::stormIncrease, binding.StormDisplay)
        setUpTouchListener(binding.LoseStorm, ::stormDecrease, binding.StormDisplay)
        setUpTouchListener(binding.Gain1Poison, ::poisonIncrease, binding.PoisonDisplay)
        setUpTouchListener(binding.Lose1Poison, ::poisonDecrease, binding.PoisonDisplay)
        setUpTouchListener(binding.addp1, ::commanderDmgIncrease1, binding.Commanderdmgp1)
        setUpTouchListener(binding.Subtractp1, ::commanderDmgDecrease1, binding.Commanderdmgp1)
        setUpTouchListener(binding.addp2, ::commanderDmgIncrease2, binding.Commanderdmgp2)
        setUpTouchListener(binding.subtractp2, ::commanderDmgDecrease2, binding.Commanderdmgp2)
        setUpTouchListener(binding.addp3, ::commanderDmgIncrease3, binding.Commanderdmgp3)
        setUpTouchListener(binding.subtractp3, ::commanderDmgDecrease3, binding.Commanderdmgp3)


        binding.Gain10Health.setOnClickListener {
            sharedViewModel.healthCounter+= 10
            binding.HealthDisplay.text = sharedViewModel.healthCounter.toString()
        }

        binding.Lose10Health.setOnClickListener {
            sharedViewModel.healthCounter-= 10
            binding.HealthDisplay.text = sharedViewModel.healthCounter.toString()
        }

        // Add click listener for Reset Buttons
        binding.ResetHealth.setOnClickListener {
            sharedViewModel.healthCounter = 40
            sharedViewModel.commanderDmgp1 = 0
            sharedViewModel.commanderDmgp2 = 0
            sharedViewModel.commanderDmgp3 = 0

            binding.HealthDisplay.text = sharedViewModel.healthCounter.toString()
            binding.Commanderdmgp1.text = sharedViewModel.commanderDmgp1.toString()
            binding.Commanderdmgp2.text = sharedViewModel.commanderDmgp2.toString()
            binding.Commanderdmgp3.text = sharedViewModel.commanderDmgp1.toString()

        }

        binding.ResetStorm.setOnClickListener {
            sharedViewModel.stormCounter = 0
            binding.StormDisplay.text = sharedViewModel.stormCounter.toString()
        }

        binding.ResetPoison.setOnClickListener {
            sharedViewModel.poisonCounter = 0
            binding.PoisonDisplay.text = sharedViewModel.poisonCounter.toString()
        }

        // Navigation button
        binding.BackButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private fun setUpTouchListener(button: View, function: () -> Int, textView: TextView) {
        val defaultAlpha = button.alpha //store the default value of the button

        val runnable = object : Runnable {
            override fun run() {
                //Execute function and update textview
                val newValue = function()
                textView.text = newValue.toString()
                //Update ViewModel
                when (textView) {
                    binding.HealthDisplay -> sharedViewModel.healthCounter = newValue
                    binding.StormDisplay -> sharedViewModel.stormCounter = newValue
                    binding.PoisonDisplay -> sharedViewModel.poisonCounter = newValue
                    binding.Commanderdmgp1 -> sharedViewModel.commanderDmgp1 = newValue
                    binding.Commanderdmgp2 -> sharedViewModel.commanderDmgp2 = newValue
                    binding.Commanderdmgp3 -> sharedViewModel.commanderDmgp3 = newValue
                }
                //if button is held do it again
                if (isButtonHeldDown) {
                    handler.postDelayed(this, delayMillis)
                }
            }
        }

        //set touchlistener on button
        button.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isButtonHeldDown = true
                    view.alpha = defaultAlpha * 0.6f
                    handler.post(runnable)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    isButtonHeldDown = false
                    view.alpha = defaultAlpha
                    handler.removeCallbacks(runnable)
                    view.performClick()
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    isButtonHeldDown = false
                    view.alpha = defaultAlpha
                    handler.removeCallbacks(runnable)
                    true
                }
                else -> false
            }
        }
    }

    private fun healthIncrease(): Int {
        sharedViewModel.healthCounter++
        return sharedViewModel.healthCounter
    }
    private fun healthDecrease(): Int {
        sharedViewModel.healthCounter--
        return sharedViewModel.healthCounter
    }
    private fun stormIncrease(): Int {
        sharedViewModel.stormCounter++
        return sharedViewModel.stormCounter
    }
    private fun stormDecrease(): Int {
        sharedViewModel.stormCounter = maxOf(sharedViewModel.stormCounter - 1, 0)
        return sharedViewModel.stormCounter
    }
    private fun poisonIncrease(): Int {
        sharedViewModel.poisonCounter++
        return sharedViewModel.poisonCounter
    }
    private fun poisonDecrease(): Int {
        sharedViewModel.poisonCounter = maxOf(sharedViewModel.poisonCounter - 1, 0)
        return sharedViewModel.poisonCounter
    }
    private fun commanderDmgIncrease1(): Int {
        sharedViewModel.commanderDmgp1++
        return sharedViewModel.commanderDmgp1
    }
    private fun commanderDmgDecrease1(): Int {
        sharedViewModel.commanderDmgp1 = maxOf(sharedViewModel.commanderDmgp1 - 1, 0)
        return sharedViewModel.commanderDmgp1
    }
    private fun commanderDmgIncrease2(): Int {
        sharedViewModel.commanderDmgp2++
        return sharedViewModel.commanderDmgp2
    }
    private fun commanderDmgDecrease2(): Int {
        sharedViewModel.commanderDmgp2 = maxOf(sharedViewModel.commanderDmgp2 - 1, 0)
        return sharedViewModel.commanderDmgp2
    }
    private fun commanderDmgIncrease3(): Int {
        sharedViewModel.commanderDmgp3++
        return sharedViewModel.commanderDmgp3
    }
    private fun commanderDmgDecrease3(): Int {
        sharedViewModel.commanderDmgp3 = maxOf(sharedViewModel.commanderDmgp3 - 1, 0)
        return sharedViewModel.commanderDmgp3
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}