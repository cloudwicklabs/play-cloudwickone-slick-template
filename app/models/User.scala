package models

import java.util.Date

import slick.driver.PostgresDriver.api._
import slick.lifted.Tag

case class User(
  id: Option[Long],
  firstName: String,
  lastName: String,
  fullName: String,
  age: Int,
  active: Boolean
)

class Users(tag: Tag) extends Table[User](tag, "user") {
  implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def firstName = column[String]("firstName")
  def lastName = column[String]("lastName")
  def fullName = column[String]("fullName")
  def age = column[Int]("age")
  def active = column[Boolean]("active")

  override def * = (id.?, firstName, lastName, fullName, age, active) <> (User.tupled, User.unapply)
}
