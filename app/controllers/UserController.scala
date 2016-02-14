package controllers

import javax.inject.Inject

import dao.UserDao
import models.User
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class UserController @Inject() (val messagesApi: MessagesApi, userDao: UserDao) extends Controller with I18nSupport {

  private val logger = Logger(this.getClass)

  /** Create user form */
  val createUserForm = Form(
    tuple(
      "firstName" -> nonEmptyText(maxLength = 12),
      "lastName" -> nonEmptyText(maxLength = 12),
      "age" -> number(min = 18, max = 99),
      "active" -> boolean
    )
  )

  def users = Action.async { implicit request =>
    userDao.list() map { x =>
      Ok(views.html.users(x, createUserForm))
    }
  }

  def createUser = Action.async { implicit request =>
    val formValidationResult = createUserForm.bindFromRequest
    formValidationResult.fold({ formWithErrors =>
      // form has validation errors
      userDao.list() map { users =>
        BadRequest(views.html.users(users,formWithErrors))
      }
    }, { u => // binding successful
      val user = User(None, firstName = u._1, lastName = u._2, fullName = s"${u._1} ${u._2}", age = u._3, active = u._4)
      userDao.insert(user) map { uid =>
        logger.info(s"User with id $uid got created.")
        Redirect(routes.UserController.users()).flashing("state" -> "success", "message" -> "User saved!")
      }
    })
  }

}
