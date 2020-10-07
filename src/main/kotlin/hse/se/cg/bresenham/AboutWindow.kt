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
            Программа для рисования отрезков алгоритмом Брезенхема. Семинар 1.
            Выполнил студент БПИ172 Измайлов Александр Александрович.
            
            Выполнено в среде Intellij IDEA на языке прогрммирования Kotlin,
            с использование библиотеки Swing и системой сборки Gradle.
            
            Для запуска нужна предустановленная Java машина версии 1.8 или выше.
            
            Синим цветом рисуются линии с помощью стандартного метода drawLine.
            Красным цветом рисуются линии с помощью реализованного алгоритма Брезенхема.
            Черным кругом обозначается начало текущей линии.
            
            Краткое описание работы программы:
                1) Сначала рисуется 8 рандомных линий во всех 8 направлениях
                2) При клике мышью на панель рисования позиция мыши добавляется в LinesModel
                3) LinesModel определяет нужно ли добавить новую линию, или нужно ждать еще одну точку
                4) Перерисовываются все линии. На каждую линию рисуется еще одна линия сдвинутая на (10, 10)
                    реализованная при помощи алгоритма Брезенхема
        """.trimIndent()
    }
}
