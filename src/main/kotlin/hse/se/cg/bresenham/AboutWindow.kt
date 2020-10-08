package hse.se.cg.bresenham

import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextArea

class AboutWindow : JFrame("О программе") {
    init {
        val panel = JPanel(BorderLayout())
        panel.add(
            JTextArea(ABOUT_PROGRAM).apply {
                isEditable = false
            },
            BorderLayout.CENTER
        )
        add(panel)
        pack()
        isVisible = true
    }

    companion object {
        val ABOUT_PROGRAM = """
            Программа для рисования окружностей и эллипсов алгоритмом Брезенхема. Семинар 2.
            Выполнил студент БПИ172 Измайлов Александр Александрович.
            
            Выполнено в среде Intellij IDEA на языке прогрммирования Kotlin,
            с использование библиотеки Swing и системой сборки Gradle.
            
            Для запуска нужна предустановленная Java машина версии 1.8 или выше.
            
            На выбор есть три примитива, все задаются 2 точками
                (для круга и эллипса по ним считаются центры и радиусы).
            С помощью мыши можно нарисовать выбранный объект выбранным цветом.
            При выборе "режим тестирования" рядом нарисуется объект нарисованный
            стандартным методом из Graphics.
            
            Все объекты хранятся в списке, поэтому есть возможность отменить рисование
        """.trimIndent()
    }
}
