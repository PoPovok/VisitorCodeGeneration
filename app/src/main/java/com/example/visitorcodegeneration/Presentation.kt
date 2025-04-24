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
import com.example.codegenerator.ThemeVisitorBase
import com.example.codegenerator.ThemeVisitorHolder
import com.example.codegenerator.annotation.Visitor
import com.example.codegenerator.annotation.VisitorFor
import com.example.visitorcodegeneration.ui.theme.VisitorCodeGenerationTheme

sealed class Theme {
    data object Background : Theme()
    data object Active : Theme()
    data object Content : Theme()
}

@Visitor(Theme::class)
class ThemeVisitor

@VisitorFor(ThemeVisitor::class)
class LightThemeVisitor : ThemeVisitorBase() {
    override fun getTheme(active: Theme.Active) = Color.Red
    override fun getTheme(background: Theme.Background) = Color.White
    override fun getTheme(content: Theme.Content) = Color.Black
}

@VisitorFor(ThemeVisitor::class)
class DarkThemeVisitor : ThemeVisitorBase() {
    override fun getTheme(active: Theme.Active) = Color.Blue
    override fun getTheme(background: Theme.Background) = Color.Black
    override fun getTheme(content: Theme.Content) = Color.White
}

@Composable
fun Presentation() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val visitors = ThemeVisitorHolder.visitor
    val activeVisitor = visitors[selectedIndex]

    Box(Modifier.background(activeVisitor.getTheme(Theme.Background))) {
        SingleChoiceSegmentedButtonRow {
            visitors.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = visitors.size
                    ),
                    colors = SegmentedButtonDefaults.colors().copy(
                        activeContainerColor = activeVisitor.getTheme(Theme.Active),
                        inactiveContentColor = activeVisitor.getTheme(Theme.Content),
                        inactiveContainerColor = activeVisitor.getTheme(Theme.Background)
                    ),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
                    label = { Text(visitors[index].javaClass.simpleName) }
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