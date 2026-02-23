package ru.lizyakin.vending_machines.controllers

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.SubScene
import javafx.scene.layout.StackPane
import ru.lizyakin.vending_machines.VendingMachineApplication
import javafx.scene.control.Label

class LayoutController {
    private var currentRoot: Parent? = null

    @FXML
    lateinit var mainBody: StackPane

    @FXML
    lateinit var navSectionName: Label

    @FXML
    fun initialize() {
        navToMain()
    }

    private fun nav(viewName: String, sectionName: String) {
        val loader = FXMLLoader(VendingMachineApplication::class.java.getResource(viewName))
        val view: Parent = loader.load()

        currentRoot = view
        mainBody.children.setAll(view)
        navSectionName.text = sectionName
    }

    @FXML
    fun navToMain() {
        nav("main-view.fxml", "Главная")
    }

    @FXML
   fun navToMonitorVM() {
        nav("main-monitor-vm-view.fxml", "Главная / Монитор ТА")
    }

    @FXML
    fun navToReportVM() {
        //Нет примера
    }

    @FXML
    fun navToReportUsers() {
        //Нет примера
    }

    @FXML
    fun navToAccountingVM() {
        //Нет примера
    }

    @FXML
    fun navToAccountingUsers() {
        //Нет примера
    }

    @FXML
    fun navToAdminVM() {
        nav("admin-vm-view.fxml", "Администрирование / Торговые автоматы")
    }

    @FXML
    fun navToAdminCompanies() {

    }

    @FXML
    fun navToAdminUsers() {

    }

    @FXML
    fun navToAdminModems() {

    }

    @FXML
    fun navToAdminAdditionally() {

    }
}