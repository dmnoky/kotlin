
import liquibase.Contexts
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.database.Database
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.junit.jupiter.api.ClassOrderer
import org.junit.jupiter.api.ClassOrdererContext
import ru.tbank.PASSWORD
import ru.tbank.URL
import ru.tbank.USER_NAME
import java.sql.Connection
import java.sql.DriverManager
import kotlin.test.Test

class TestMain : ClassOrderer {
    companion object {
        /** Ищет причину ошибки [causeName] вглубь
         * @return если причина не найдена, то вернет последнюю причину, иначе вернет совпадение по [causeName] */
        tailrec fun <T:Throwable> Throwable.findErrCause(causeName: Class<T>) : Throwable =
            if (cause == null) this
            else if (cause!!::class == causeName) cause!! else cause!!.findErrCause(causeName)
    }

    /** Задает порядок запуска тест классов - TestMain будет всегда 1ым */
    override fun orderClasses(context: ClassOrdererContext) {
        context.classDescriptors.sortBy { t -> t.testClass != TestMain::class.java }
    }

    /** запускает sql под Liquibase */
    @Test
    fun dbLiquibase() {
        val connection: Connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)
        val database: Database =
            DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(connection))
        Liquibase("db/changelog/db.changelog-master.yaml", ClassLoaderResourceAccessor(), database).use { liquibase ->
            liquibase.update(Contexts(), LabelExpression())
        }
    }

}