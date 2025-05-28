
import liquibase.Contexts
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.database.Database
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.hibernate.cfg.Configuration
import org.junit.jupiter.api.*
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import ru.tbank.entityManagerFactory
import ru.tbank.model.Client
import ru.tbank.model.Transport
import ru.tbank.model.TransportBrand
import ru.tbank.model.TransportModel
import ru.tbank.persistenceUnitName
import java.sql.Connection
import java.sql.DriverManager

@Order(1)
open class TestMain : ClassOrderer {
    companion object {
        /** Ищет причину ошибки [causeName] вглубь
         * @return если причина не найдена, то вернет последнюю причину, иначе вернет совпадение по [causeName] */
        tailrec fun <T:Throwable> Throwable.findErrCause(causeName: Class<T>) : Throwable =
            if (cause == null) this
            else if (cause!!::class == causeName) cause!! else cause!!.findErrCause(causeName)
        
        /** Test DB */
        private val postgres: PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
            .apply {
                this.withDatabaseName("amp")
                    .withUsername("testPostgres")
                    .withPassword("testPostgres")
                    .withExposedPorts(5432)
                    .waitingFor(Wait.forListeningPort())
            }
        
        @JvmStatic
        @BeforeAll
        internal fun setUp() {
            postgres.start()
            
            persistenceUnitName = "Unit10_Test"
            entityManagerFactory = with(Configuration()) {
                properties["hibernate.connection.driver_class"] = "org.postgresql.Driver"
                properties["hibernate.connection.url"] = postgres.jdbcUrl
                properties["hibernate.connection.username"] = postgres.username
                properties["hibernate.connection.password"] = postgres.password
                properties["hibernate.default_schema"] = "unit8"
                properties["hibernate.dialect"] = "org.hibernate.dialect.PostgreSQL81Dialect"
                addAnnotatedClass(Client::class.java)
                addAnnotatedClass(Transport::class.java)
                addAnnotatedClass(TransportModel::class.java)
                addAnnotatedClass(TransportBrand::class.java)
            }.buildSessionFactory()
            
            dbLiquibase()
        }
        
        /** запускает sql под Liquibase */
        private fun dbLiquibase() {
            val connection: Connection = DriverManager.getConnection(
                postgres.jdbcUrl as String,
                postgres.username as String,
                postgres.password as String
            )
            val database: Database =
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(connection))
            
            Liquibase("db/changelog/db.changelog-master.yaml", ClassLoaderResourceAccessor(), database).use { liquibase ->
                liquibase.update(Contexts(), LabelExpression())
            }
        }
    }
    
    /** Задает порядок запуска тест классов - TestMain будет всегда 1ым */
    override fun orderClasses(context: ClassOrdererContext) {
        context.classDescriptors.sortWith(
            Comparator.comparingInt { t ->
                t.testClass.getAnnotation(Order::class.java).value
            }
        )
    }
    
    @Test
    fun test() {
        println("postgres.jdbcUrl: " + postgres.jdbcUrl)
    }
}