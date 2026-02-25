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
@Table(name = "vending_machines")
class VendingMachine (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,

    @Column(name = "inventory_id")
    var inventoryId: Int?,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_status")
    var status: Status,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_manufacturer")
    var manufacturer: Manufacturer,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_country")
    var country: Country,

    @Column(name = "model_name")
    var modelName: String,

    @Column(name = "created_at")
    var createdAt: Date,

    @Column(name = "commissioned_at")
    var commissionedAt: Date,

    @Column(name = "last_time_checked_at")
    var lastTimeCheckedAt: Date,

    @Column(name = "interchecking_interval")
    var intercheckingInterval: Short,

    @Column(name = "resource_of_machine")
    var resourceOfMachine: Short,

    @Column(name = "next_check_at")
    var nextCheckAt: Date,

    @Column(name = "check_time")
    var checkTime: Short,

    @Column(name = "inventory_at")
    var inventoryAt: Date,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_last_checker")
    var lastChecker: User,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_modem")
    var modem: Modem,

    @Column(name = "address")
    var address: String
) {
    // Эти методы JavaFX будет использовать для связки с таблицей.
    // Аннотация @Transient говорит Hibernate игнорировать эти функции.

    @Transient
    fun idProperty(): ObservableValue<Int> = ReadOnlyObjectWrapper(id)

    @Transient
    fun inventoryIdProperty(): ObservableValue<Int> = ReadOnlyObjectWrapper(inventoryId)

    @Transient
    fun statusProperty(): StringProperty = SimpleStringProperty(status?.statusName)

    @Transient
    fun manufacturerProperty(): StringProperty = SimpleStringProperty(manufacturer?.name)

    @Transient
    fun countryProperty(): StringProperty = SimpleStringProperty(country?.name)

    @Transient
    fun modelNameProperty(): StringProperty = SimpleStringProperty(modelName)

    @Transient
    fun createdAtProperty(): ObservableValue<Date> = ReadOnlyObjectWrapper(createdAt)

    @Transient
    fun commissionedAtProperty(): ObservableValue<Date> = ReadOnlyObjectWrapper(commissionedAt)

    @Transient
    fun lastTimeCheckedAtProperty(): ObservableValue<Date> = ReadOnlyObjectWrapper(lastTimeCheckedAt)

    @Transient
    fun resourceOfMachineIntervalProperty(): ObservableValue<Short> = ReadOnlyObjectWrapper(resourceOfMachine)

    @Transient
    fun intercheckingIntervalProperty(): ObservableValue<Number> = ReadOnlyObjectWrapper(intercheckingInterval)

    @Transient
    fun nextCheckAtProperty(): ObservableValue<Date> = ReadOnlyObjectWrapper(nextCheckAt)

    @Transient
    fun checkTimeProperty(): ObservableValue<Short> = ReadOnlyObjectWrapper(checkTime)

    @Transient
    fun inventoryAtProperty(): ObservableValue<Date> = ReadOnlyObjectWrapper(inventoryAt)

    @Transient
    fun lastCheckerProperty(): StringProperty = SimpleStringProperty(lastChecker.lastname + " " + lastChecker.firstname)

    @Transient
    fun modemProperty(): StringProperty = SimpleStringProperty(modem?.modemNumber)

    @Transient
    fun addressProperty(): StringProperty = SimpleStringProperty(address)
}