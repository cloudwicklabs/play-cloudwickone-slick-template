package dao

import javax.inject.Inject

import models.{Page, User, Users}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserDao @Inject() (val dbConfig: DatabaseConfig[JdbcProfile]) extends Dao[User] {

  private lazy val users = TableQuery[Users]

  /** Filter user with id */
  private def filterQuery(id: Long): Query[Users, User, Seq] = users.filter(_.id === id)

  /** Count employees with a filter */
  private def count(filter: String): Future[Int] = {
    dbConfig.db.run(users.filter(_.fullName.toLowerCase like filter.toLowerCase()).length.result)
  }

  override def insert(user: User): Future[Int] = {
    dbConfig.db.run(users += user)
  }

  override def count: Future[Int] = {
    dbConfig.db.run(users.length.result)
  }

  override def update(id: Long, user: User): Future[Int] = {
    dbConfig.db.run(filterQuery(id).update(user))
  }

  override def findById(id: Long): Future[User] = {
    dbConfig.db.run(filterQuery(id).result.head)
  }

  override def delete(id: Long): Future[Int] = {
    dbConfig.db.run(filterQuery(id).delete)
  }

  override def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Future[Page[User]] = {
    val offset = pageSize * page
    val query =
      (for {
        user <- users if user.fullName.toLowerCase like filter.toLowerCase
      } yield user).drop(offset).take(pageSize)
    val totalRows = count(filter)
    val result = dbConfig.db.run(query.result)
    result flatMap (employees => totalRows map (rows => Page(employees, page, offset, rows)))
  }
}
