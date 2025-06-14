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
        } catch (e: Exception) {
            throw
                if (e is NoResultException || e is NullPointerException) EntityNotFoundException("Запись не найдена!", e)
                else e
        }
    }

    /** Удаление */
    override fun delete(entityManager: EntityManager, model: TransportModel): Boolean {
        val modelNotDetached =
            if (!entityManager.contains(model)) entityManager.find(model::class.java, model.id)
            else model
        entityManager.remove(modelNotDetached)
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