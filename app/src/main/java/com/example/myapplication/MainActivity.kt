package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Cyan
            ) {
                BirthdayGreeting(
                    message = "Happy Birthday Sahar!",
                    from = "From Sepehr"
                )
            }
        }
    }
}

@Composable
fun BirthdayGreeting(message: String, from: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Balloons Animation
        BalloonsAnimation()

        // Confetti Animation
        ConfettiAnimation()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                fontSize = 50.sp
            )
            Text(
                text = from,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}

@Composable
fun BalloonsAnimation() {
    val infiniteTransition = rememberInfiniteTransition()

    val xOffset = infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val yOffset = infiniteTransition.animateFloat(
        initialValue = 800f,
        targetValue = -100f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val balloonRadius = 40f
        val stringLength = 150f

        // Balloon positions
        val balloons = listOf(
            Offset(size.width / 2 + xOffset.value, yOffset.value),
            Offset(size.width / 3 + xOffset.value, yOffset.value + 100),
            Offset(size.width * 2 / 3 + xOffset.value, yOffset.value + 200)
        )

        val colors = listOf(Color.Red, Color.Blue, Color.Yellow)

        balloons.forEachIndexed { index, balloonCenter ->
            val balloonColor = colors[index]

            // Draw the wavy string
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(balloonCenter.x, balloonCenter.y + balloonRadius)

                val waveWidth = 20f  // Controls the waviness of the string
                val waveHeight = 30f // Controls the height of the waves

                for (i in 1..4) { // Create 4 waves
                    val x1 = balloonCenter.x + if (i % 2 == 0) -waveWidth else waveWidth
                    val y1 = balloonCenter.y + balloonRadius + (i * waveHeight) - waveHeight / 2
                    val x2 = balloonCenter.x
                    val y2 = balloonCenter.y + balloonRadius + (i * waveHeight)
                    quadraticBezierTo(x1, y1, x2, y2)
                }

                lineTo(balloonCenter.x, balloonCenter.y + balloonRadius + stringLength)
            }

            drawPath(
                path = path,
                color = Color.Black,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
            )

            // Draw the balloon (circle)
            drawCircle(
                color = balloonColor,
                radius = balloonRadius,
                center = balloonCenter
            )
        }
    }
}


@Composable
fun ConfettiAnimation() {
    val confettiList = remember { List(100) { ConfettiParticle() } }

    LaunchedEffect(Unit) {
        while (true) {
            confettiList.forEach { it.update() }
            delay(16L) // 60fps animation
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        confettiList.forEach { particle ->
            drawCircle(particle.color, radius = 5f, center = Offset(particle.x, particle.y))
        }
    }
}

class ConfettiParticle {
    var x = Random.nextFloat() * 1080f
    var y = Random.nextFloat() * 1920f
    var color = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta).random()
    var speed = Random.nextFloat() * 5 + 2

    fun update() {
        y += speed
        if (y > 1920f) {
            y = 0f
            x = Random.nextFloat() * 1080f
        }
    }
}