package ru.lizyakin.vending_machines.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Transient
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue
import java.util.Date

@Entity
@Table(name = "sales")
class Sale (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vending_machine")
    var vendingMachine: VendingMachine,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_good")
    var good: Good,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type_payment")
    var typePayment: TypePayment,

    @Column(name = "amount")
    var amount: Int,

    @Column(name = "sold_at")
    var soldAt: Date
) {
    // Эти методы JavaFX будет использовать для связки с таблицей.
    // Аннотация @Transient говорит Hibernate игнорировать эти функции.

    @Transient
    fun idProperty(): ObservableValue<Number> = ReadOnlyObjectWrapper(id ?: 0)

    @Transient
    fun goodProperty(): StringProperty = SimpleStringProperty(good?.name)

    @Transient
    fun typePaymentProperty(): StringProperty = SimpleStringProperty(typePayment?.typeName)

    @Transient
    fun amountProperty(): ObservableValue<Number> = ReadOnlyObjectWrapper(amount)
}