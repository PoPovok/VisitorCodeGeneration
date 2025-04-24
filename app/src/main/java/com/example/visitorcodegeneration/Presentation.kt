package com.example.visitorcodegeneration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.codegenerator.annotation.Visitor
import com.example.visitorcodegeneration.ui.theme.VisitorCodeGenerationTheme

@Visitor
class ThemeVisitor

@Composable
fun Presentation() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Light", "Dark")

    Box(Modifier.background(Color.White)) {
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
                    label = { Text(label) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PresentationPreview() {
    VisitorCodeGenerationTheme {
        Presentation()
    }
}