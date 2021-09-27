package xyz.teamgravity.musicknob

import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import kotlin.math.PI
import kotlin.math.atan2

@ExperimentalComposeUiApi
@Composable
fun MusicKnob(
    modifier: Modifier = Modifier,
    limitAngle: Float = 25f, // to determine where to start position knob
    onValueChange: (Float) -> Unit
) {

    var rotation by remember {
        mutableStateOf(limitAngle)
    }

    var touchX by remember {
        mutableStateOf(0f)
    }

    var touchY by remember {
        mutableStateOf(0f)
    }

    var centerX by remember {
        mutableStateOf(0f)
    }

    var centerY by remember {
        mutableStateOf(0f)
    }

    Image(
        painter = painterResource(id = R.drawable.music_knob),
        contentDescription = "music_knob",
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                val boundsWindow = it.boundsInWindow().size
                centerX = boundsWindow.width / 2f
                centerY = boundsWindow.height / 2f
            }
            .pointerInteropFilter { motion ->
                touchX = motion.x
                touchY = motion.y

                val angle = -atan2(centerX - touchX, centerY - touchY) * (180f / PI).toFloat()

                when (motion.action) {
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_MOVE -> {
                        if (angle !in -limitAngle..limitAngle) {
                            val fixedAngle = if (angle in -180f..-limitAngle) 360f + angle else angle
                            rotation = fixedAngle

                            val percent = (fixedAngle - limitAngle) / (360f - 2 * limitAngle)
                            onValueChange(percent)
                            true
                        } else false
                    }
                    else -> false
                }
            }
            .rotate(rotation)
    )

}