package ru.tbank.dao.transport

import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.model.TransportModel
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.NoResultException

class TransportModelSimpleDao private constructor() : TransportModelDao {
    companion object {
        val singleton: TransportModelSimpleDao = lazy { TransportModelSimpleDao() }.value
    }

    override fun findAll(entityManager: EntityManager): LinkedHashSet<TransportModel> {
        return entityManager.createQuery("FROM TransportModel", TransportModel::class.java)
            .resultList.toCollection(LinkedHashSet<TransportModel>())
    }

    /** Получение
     * @throws EntityNotFoundException */
    override fun findById(entityManager: EntityManager, modelId: UUID): TransportModel {
        try {
            return entityManager.find(TransportModel::class.java, modelId)
        } catch (e: NullPointerException) {
            throw EntityNotFoundException("Запись не найдена!", e)
        }
    }

    /** Получение
     * @throws EntityNotFoundException */
    override fun findByName(entityManager: EntityManager, model: String): TransportModel {
        try {
            return entityManager.createQuery("SELECT m FROM TransportModel m WHERE model=:model", TransportModel::class.java)
                .setParameter("model", model).singleResult
        } catch (e: NoResultException) {
            throw EntityNotFoundException("Запись не найдена!", e)
        }
    }

    /** Удаление */
    override fun delete(entityManager: EntityManager, model: TransportModel): Boolean {
        println(model)
        println(entityManager.contains(model))
        var entity = findById(entityManager, model.id)//entityManager.find(model::class.java, model.id)
        entity = entityManager.createQuery("FROM TransportModel WHERE id=:id", TransportModel::class.java)
            .setParameter("id", model.id).singleResult
        entityManager.merge(entity)
        println(entity)
        println(entityManager.contains(model))
        entityManager.remove(model)
        return true
    }

    /** Добавление */
    override fun add(entityManager: EntityManager, model: TransportModel): Boolean {
        entityManager.persist(model)
        return true
    }

    /** Обновление
     * @throws javax.persistence.OptimisticLockException */
    override fun update(entityManager: EntityManager, model: TransportModel): Boolean {
        entityManager.merge(model)
        return true
    }
}