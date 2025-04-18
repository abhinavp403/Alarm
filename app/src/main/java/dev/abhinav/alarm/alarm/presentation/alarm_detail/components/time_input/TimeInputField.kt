package dev.abhinav.alarm.alarm.presentation.alarm_detail.components.time_input

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimeInputField(
    state: TextFieldState,
    inputTransformation: InputTransformation,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
    textStyle: TextStyle = TimeInputFieldDefaults.textStyle.copy(fontFamily = MaterialTheme.typography.displayLarge.fontFamily),
    focusedBorderColor: Color = TimeInputFieldDefaults.focusedBorderColor,
    unfocusedBorderColor: Color = TimeInputFieldDefaults.unfocusedBorderColor,
    focusedBorderWidth: Dp = TimeInputFieldDefaults.focusedBorderWidth,
    unfocusedBorderWidth: Dp? = TimeInputFieldDefaults.unfocusedBorderWidth,
    shape: Shape = TimeInputFieldDefaults.shape
) {
    val isImeVisible = WindowInsets.isImeVisible
    val focus = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val textSize by animateFloatAsState(targetValue = if (isFocused) textStyle.fontSize.value + 4 else textStyle.fontSize.value)

    LaunchedEffect(isFocused) {
        calibrateTextField(state, isFocused)
    }

    LaunchedEffect(isImeVisible) {
        if (isImeVisible.not()) {
            focus.clearFocus()
        }
    }

    BasicTextField(
        state = state,
        modifier = modifier,
        interactionSource = interactionSource,
        inputTransformation = inputTransformation,
        textStyle = textStyle.copy(fontSize = textSize.sp),
        cursorBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF4664FF),
                Color(0xFF4664FF),
            )
        ),
        keyboardOptions = keyboardOptions,
        onKeyboardAction = KeyboardActionHandler { performDefaultAction ->
            handleKeyboardAction(state, keyboardOptions, focus, performDefaultAction)
        },
        lineLimits = TextFieldLineLimits.SingleLine,
        decorator = { innerTextField ->
            Card(
                border = if (isFocused) BorderStroke(
                    focusedBorderWidth,
                    focusedBorderColor
                ) else unfocusedBorderWidth?.let { BorderStroke(it, unfocusedBorderColor) },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F6F6)),
                shape = shape,
                modifier = Modifier.width((textSize * 2.5).dp)
            ) {
                Box(
                    Modifier
                        .padding(horizontal = 29.dp, vertical = 16.dp)
                        .fillMaxWidth()
                ) {
                    if (state.text.isBlank()) {
                        Text(
                            text = "00",
                            style = textStyle.copy(
                                fontSize = textSize.sp,
                                color = Color(0xFF858585)
                            ),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else innerTextField()
                }
            }
        }
    )
}

private fun calibrateTextField(state: TextFieldState, isFocused: Boolean) {
    when {
        isFocused && state.text == "00" -> {
            state.clearText()
        }

        !isFocused && state.text.length == 1 -> {
            state.edit {
                replace(0, 1, "0${state.text}")
            }
        }

        !isFocused && state.text == "0" -> {
            state.edit {
                replace(0, 1, "00")
            }
        }
    }
}

private fun handleKeyboardAction(
    state: TextFieldState,
    keyboardOptions: KeyboardOptions,
    focus: FocusManager,
    performDefaultAction: () -> Unit
) {
    if (state.text.isEmpty()) {
        state.edit {
            append("00")
        }
    }
    when (keyboardOptions.imeAction) {
        ImeAction.Next -> {
            focus.moveFocus(FocusDirection.Next)
        }

        ImeAction.Done -> {
            focus.clearFocus()
        }

        else -> performDefaultAction()
    }
}

object TimeInputFieldDefaults {
    val textStyle: TextStyle = TextStyle(
        fontSize = 52.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        color = Color(0xFF4664FF)
    )
    val focusedBorderColor: Color = Color.Blue
    val unfocusedBorderColor: Color = Color.Gray
    val focusedBorderWidth: Dp = 2.dp
    val unfocusedBorderWidth: Dp? = null
    val shape: Shape = RoundedCornerShape(10.dp)
}