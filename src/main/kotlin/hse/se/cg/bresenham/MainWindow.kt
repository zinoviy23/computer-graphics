package hse.se.cg.bresenham

import java.awt.BorderLayout
import java.awt.Color
import java.awt.event.ActionEvent
import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.EmptyBorder

class MainWindow : JFrame("Алгоритм Брезенхема") {
    init {
        defaultCloseOperation = EXIT_ON_CLOSE

        val panel = JPanel(BorderLayout())
        val toolbar = JToolBar()
//        toolbar.border = EmptyBorder(1, 1, 1, 1)
        panel.add(DrawingCanvas(), BorderLayout.CENTER)
        panel.add(toolbar, BorderLayout.NORTH)

        add(panel)
        pack()

        setupMenu()

        isVisible = true

        toolbar.add(object : AbstractAction("Очистить") {
            override fun actionPerformed(e: ActionEvent?) {
                GraphicsObjectsModel.clean()
            }
        })
        val testCheckbox = JCheckBox("Тестовый режим")
        testCheckbox.isSelected = GraphicsObjectsModel.Settings.isTestingMode
        toolbar.add(testCheckbox)
        testCheckbox.addActionListener {
            GraphicsObjectsModel.Settings.isTestingMode = testCheckbox.isSelected
        }
    }

    private fun setupMenu() {
        val menu = JMenuBar()
        val helpMenu = JMenu("Помощь")
        val aboutItem = JMenuItem("О программе")
        aboutItem.addActionListener {
            AboutWindow()
        }
        helpMenu.add(aboutItem)
        menu.add(helpMenu)
        jMenuBar = menu
    }
}
