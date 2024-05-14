package com.example.mtgfamiliar

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var healthCounter: Int = 40
    var stormCounter: Int = 0
    var poisonCounter: Int = 0
    var commanderDmgp1: Int = 0
    var commanderDmgp2: Int = 0
    var commanderDmgp3: Int = 0
}