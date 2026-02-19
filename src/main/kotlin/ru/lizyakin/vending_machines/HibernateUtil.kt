package ru.lizyakin.vending_machines

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

object HibernateUtil {
    val sessionFactory: SessionFactory = try {
        Configuration().configure().buildSessionFactory() as SessionFactory
    } catch (ex: Throwable) {
        println("Initial SessionFactory creation failed. $ex")
        throw ExceptionInInitializerError(ex)
    }

    fun shutdown() = sessionFactory.close()
}