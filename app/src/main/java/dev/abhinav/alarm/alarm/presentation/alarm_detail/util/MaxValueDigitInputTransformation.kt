package dev.abhinav.alarm.alarm.presentation.alarm_detail.util

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.forEachChange
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.foundation.text.input.then
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.KeyboardType

@Stable
fun InputTransformation.limitedInput(maxValue: Int): InputTransformation =
    this.then(MaxValueDigitInputTransformation(maxValue))
        .then(InputTransformation.maxLength(2))


@Stable
fun InputTransformation.hourInput(is24h: Boolean = true): InputTransformation {
    val maxValue = if (is24h) 23 else 11
    return limitedInput(maxValue)
}

@Stable
fun InputTransformation.minuteInput(): InputTransformation = limitedInput(59)


data class MaxValueDigitInputTransformation(val maxValue: Int) : InputTransformation {
    init {
        require(maxValue in 0..99) { "maxValue must have at most 2 digits" }
    }

    override val keyboardOptions: KeyboardOptions?
        get() = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)

    @OptIn(ExperimentalFoundationApi::class)
    override fun TextFieldBuffer.transformInput() {
        placeCursorAtEnd()
        changes.forEachChange { range, _ ->
            Log.d("DigitsOnlyTransformation", changes.toString())
            if (!range.collapsed) {
                val charInput = asCharSequence()[range.min]
                if (!charInput.isDigit()) {
                    replace(range.min, range.max, "")
                }
            }
            val inputText = asCharSequence().toString()
            if (inputText.isNotEmpty() && inputText.toInt() > maxValue) {
                revertAllChanges()
            }
        }
    }
}