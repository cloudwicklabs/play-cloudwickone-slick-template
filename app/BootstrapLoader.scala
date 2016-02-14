import controllers.UserController
import dao.UserDao
import play.api.ApplicationLoader.Context
import play.api.db.evolutions.{DynamicEvolutions, EvolutionsComponents}
import play.api.db.slick.{DbName, DatabaseConfigProvider, SlickComponents}
import play.api.db.slick.evolutions.SlickEvolutionsComponents
import play.api.i18n.{DefaultLangs, DefaultMessagesApi, MessagesApi}
import play.api.routing.Router
import play.api._
import router._
import slick.driver.JdbcProfile

class BootstrapLoader extends ApplicationLoader {
  override def load(context: Context): Application = {
    Logger.configure(context.environment)
    new ApplicationComponents(context).application
  }
}

class ApplicationComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with SlickComponents with SlickEvolutionsComponents with EvolutionsComponents {

  lazy val messagesApi: MessagesApi = new DefaultMessagesApi(environment, configuration, new DefaultLangs(configuration))
  /**  */
  lazy val dbConfig = api.dbConfig[JdbcProfile](DbName("default"))
  lazy val userDao = new UserDao(dbConfig)

  lazy val applicationController = new controllers.Application()
  lazy val assets = new controllers.Assets(httpErrorHandler)
  lazy val webAssets = new controllers.WebJarAssets(httpErrorHandler, configuration, environment)
  lazy val userController = new UserController(messagesApi, userDao = userDao)

  override def router: Router = new Routes(
    httpErrorHandler,
    applicationController,
    userController,
    assets,
    webAssets
  )

  // This is required by EvolutionsComponents
  lazy val dynamicEvolutions = new DynamicEvolutions

  def onStart() = {
    // applicationEvolutions is a val and requires evolution
    applicationEvolutions
  }

  onStart()
}

