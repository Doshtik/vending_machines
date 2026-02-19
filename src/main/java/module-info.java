module ru.lizyakin.vending_machines {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens ru.lizyakin.vending_machines to javafx.fxml;
    exports ru.lizyakin.vending_machines;
}