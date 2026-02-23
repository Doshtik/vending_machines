package ru.lizyakin.vending_machines

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class VendingMachineApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(VendingMachineApplication::class.java.getResource("layout-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 1200.0, 800.0)
        stage.title = "Торговые автоматы"
        stage.scene = scene
        stage.show()
    }
}
  
