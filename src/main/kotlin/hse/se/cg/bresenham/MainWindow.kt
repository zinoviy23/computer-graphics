package hse.se.cg.bresenham

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.*

class MainWindow : JFrame("Алгоритм Брезенхема") {
    init {
        defaultCloseOperation = EXIT_ON_CLOSE

        val panel = JPanel(BorderLayout())
        panel.add(DrawingCanvas(), BorderLayout.CENTER)
        panel.add(setupToolbar(), BorderLayout.NORTH)

        add(panel)
        pack()

        setupMenu()

        isVisible = true
    }

    private fun setupToolbar(): JToolBar {
        val toolbar = JToolBar()
        toolbar.layout = FlowLayout(FlowLayout.LEFT)

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

        val colorPanel = JPanel()
        colorPanel.preferredSize = Dimension(20, 20)
        colorPanel.background = GraphicsObjectsModel.Settings.color
        val colorButton = JButton("Выбрать цвет")
        toolbar.add(colorButton)
        colorButton.addActionListener {
            val newColor = JColorChooser.showDialog(
                this,
                "Цвет",
                GraphicsObjectsModel.Settings.color
            )
            GraphicsObjectsModel.Settings.color = newColor
            colorPanel.background = GraphicsObjectsModel.Settings.color
        }
        toolbar.add(JPanel(FlowLayout()).apply {
            add(colorPanel)
            add(colorButton)
        })

        val instrumentPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        instrumentPanel.add(JLabel("Инструмент:"))
        val instrumentTypeComboBox = JComboBox(
            arrayOf(
                ObjectType.Line,
                ObjectType.Circle
            )
        )
        instrumentTypeComboBox.selectedItem = GraphicsObjectsModel.Settings.currentInstrument
        instrumentTypeComboBox.addActionListener {
            GraphicsObjectsModel.Settings.currentInstrument =
                instrumentTypeComboBox.selectedItem as ObjectType
        }
        instrumentPanel.add(instrumentTypeComboBox)
        toolbar.add(instrumentPanel)

        return toolbar
    }

    private fun setupMenu() {
        val menu = JMenuBar()

        val editMenu = JMenu("Правка")
        val undoItem = JMenuItem("Отменить")
        editMenu.add(undoItem)
        undoItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK)
        undoItem.addActionListener {
            GraphicsObjectsModel.undo()
        }

        val helpMenu = JMenu("Помощь")
        val aboutItem = JMenuItem("О программе")
        aboutItem.addActionListener {
            AboutWindow()
        }
        helpMenu.add(aboutItem)
        menu.add(editMenu)
        menu.add(helpMenu)
        jMenuBar = menu
    }
}
