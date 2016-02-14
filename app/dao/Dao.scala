package dao

import models.Page

import scala.concurrent.Future

trait Dao[T] {
  def insert(model: T): Future[Int]
  def update(id: Long, model: T): Future[Int]
  def delete(id: Long): Future[Int]
  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Future[Page[T]]
  def findById(id: Long): Future[T]
  def count: Future[Int]
}
