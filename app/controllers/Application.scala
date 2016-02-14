package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index("Your new application is ready."))
  }

  def redirectToIndex = Action { implicit request =>
    Redirect("/").flashing("state" -> "error", "message" -> "Success message.")
  }

}
