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
//    !taskSubscribers.update(DBObject("taskId" -> taskId, "skypeName" -> skypeName), DBObject("taskId" -> taskId, "skypeName" -> skypeName),true,false).isUpdateOfExisting
    //TODO
    true
  }

  def unsubscribeFromTask(taskId: Long, skypeName: String): Boolean = {
//    taskSubscribers.remove(DBObject("taskId" -> taskId, "skypeName" -> skypeName)).getN > 0
    //TODO
    true
  }

  def getTasksByUser(skypeName: String): Seq[Long] = {
    db.withConnection { implicit conn =>
      SQL("SELECT task_id FROM task_subscribers WHERE skype_name = {skypeName}")
        .on("skypeName" -> skypeName).as(SqlParser.long("task_id").*)
    }
  }

}
