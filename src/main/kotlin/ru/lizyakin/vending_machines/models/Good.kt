package ru.lizyakin.vending_machines.models

import jakarta.persistence.*
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue

@Entity
@Table(name = "goods")
class Good(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @Column(name = "name")
    var name: String,

    @Column(name = "description")
    var description: String,

    @Column(name = "price")
    var price: Float,

    @Column(name = "amount_in_stock")
    var amountInStock: Int,

    @Column(name = "propensity")
    var propensity: String
) {
    // Эти методы JavaFX будет использовать для связки с таблицей.
    // Аннотация @Transient говорит Hibernate игнорировать эти функции.

    @Transient
    fun idProperty(): ObservableValue<Number> = ReadOnlyObjectWrapper(id)

    @Transient
    fun nameProperty(): StringProperty = SimpleStringProperty(name)

    @Transient
    fun descriptionProperty(): StringProperty = SimpleStringProperty(description)

    @Transient
    fun priceProperty(): ObservableValue<Number> = ReadOnlyObjectWrapper(price)

    @Transient
    fun amountInStockProperty(): ObservableValue<Number> = ReadOnlyObjectWrapper(amountInStock)

    @Transient
    fun propensityProperty(): StringProperty = SimpleStringProperty(propensity)
}