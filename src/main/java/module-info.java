module ru.lizyakin.vending_machines {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    // Стандартные модули Java
    requires java.naming; // ВОТ ЭТО РЕШАЕТ ПРОБЛЕМУ REFERENCEABLE
    requires java.sql;

    // Hibernate и JPA
    requires org.hibernate.orm.core;
    requires jakarta.persistence;

    // PostgreSQL
    requires org.postgresql.jdbc;

    // Разрешаем Hibernate доступ к сущностям (замени путь на свой пакет с Entity)
    opens ru.lizyakin.vending_machines.models.entites to org.hibernate.orm.core;
    // Разрешаем JavaFX доступ к контроллерам
    opens ru.lizyakin.vending_machines to javafx.fxml;
    exports ru.lizyakin.vending_machines;
}