package utils

import javax.inject.{Inject, Singleton}

import anorm._
import play.api.db.{Database, _}

/**
  * Created by ekreative on 7/9/2016.
  */
@Singleton
class DB @Inject() (@NamedDatabase("marta") db: Database){

  def getUsersByTaskId(taskId: Long): Seq[String] = {
    db.withConnection { implicit conn =>
      SQL("SELECT skype_name FROM task_subscribers WHERE task_id = {taskId}")
        .on("taskId" -> taskId).as(SqlParser.str("skype_name").*)
    }
  }

  def subscribeOnTask(taskId: Long, skypeName: String): Boolean = {
    db.withConnection { implicit conn =>
      if (!getTasksByUser(skypeName).contains(taskId)) {
        val id: Option[Long] = SQL("INSERT into task_subscribers(task_id, skype_name) values ({taskId}, {skypeName})")
          .on("taskId" -> taskId, "skypeName" -> skypeName).executeInsert()
        id.isDefined
      } else {
        false
      }
    }
  }

  def unsubscribeFromTask(taskId: Long, skypeName: String): Boolean = {
    db.withConnection { implicit conn =>
      if (getTasksByUser(skypeName).contains(taskId)) {
        val count: Int = SQL("DELETE FROM task_subscribers WHERE task_id = {taskId} AND skype_name = {skypeName}")
          .on("taskId" -> taskId, "skypeName" -> skypeName).executeUpdate()
        count > 0
      } else {
        false
      }
    }
  }

  def getTasksByUser(skypeName: String): Seq[Long] = {
    db.withConnection { implicit conn =>
      SQL("SELECT task_id FROM task_subscribers WHERE skype_name = {skypeName}")
        .on("skypeName" -> skypeName).as(SqlParser.long("task_id").*)
    }
  }

}
