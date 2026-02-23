package ru.lizyakin.vending_machines.repositories

import org.hibernate.Session
import ru.lizyakin.vending_machines.HibernateUtil
import ru.lizyakin.vending_machines.models.User
import ru.lizyakin.vending_machines.models.VendingMachine

object VMRepository {

    // Helper для автоматического закрытия сессии и управления транзакциями
    fun <T> execute(action: (Session) -> T): T {
        val session = HibernateUtil.sessionFactory.openSession()
        val transaction = session.beginTransaction()
        return try {
            val result = action(session)
            transaction.commit()
            result
        } catch (e: Exception) {
            transaction.rollback()
            throw e
        } finally {
            session.close()
        }
    }

    fun findAll(): List<VendingMachine> = execute {
        it.createQuery("FROM VendingMachine", VendingMachine::class.java).list()
    }

    fun findById(id: Int): VendingMachine? = execute { it.get(VendingMachine::class.java, id) }

    fun save(vm: VendingMachine) = execute { it.persist(vm) }

    fun update(vm: VendingMachine) = execute { it.merge(vm) }

    fun delete(vm: VendingMachine) = execute { it.remove(vm) }
}