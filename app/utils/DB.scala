package utils

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import play.api.Play

import scala.collection.mutable
import scala.collection.JavaConverters._
/**
  * Created by ekreative on 7/9/2016.
  */
object DB {

  val mongoClient = MongoClient(Play.current.configuration.getStringSeq("mongodb.servers.path").get.head)
  val martaDb = mongoClient.getDB("marta-db")
  val taskSubscribers = martaDb.getCollection("task-subscribers")

  def getUsersByTaskId(taskId: Long): Seq[String] = {
    val buffer = mutable.Buffer[String]()

    val pipeline: java.util.List[DBObject] = List(
      DBObject("$match" -> MongoDBObject("taskId" -> taskId))
    ).asJava

    val iterator = taskSubscribers.aggregate(pipeline).results().iterator()
    while (iterator.hasNext) {
      buffer.append(iterator.next().getAs[String]("skypeName").getOrElse(""))
    }
    println(buffer)
    buffer.filterNot(user => user.isEmpty)
  }

  def subscribeOnTask(taskId: Long, skypeName: String): Boolean = {
    !taskSubscribers.update(DBObject("taskId" -> taskId, "skypeName" -> skypeName), DBObject("taskId" -> taskId, "skypeName" -> skypeName),true,false).isUpdateOfExisting
  }

  def unsubscribeFromTask(taskId: Long, skypeName: String): Boolean = {
    taskSubscribers.remove(DBObject("taskId" -> taskId, "skypeName" -> skypeName)).getN > 0
  }

  def getTasksByUser(skypeName: String): Seq[Long] = {
    val buffer = mutable.Buffer[Long]()

    val pipeline: java.util.List[DBObject] = List(
      DBObject("$match" -> MongoDBObject("skypeName" -> skypeName))
    ).asJava

    val iterator = taskSubscribers.aggregate(pipeline).results().iterator()
    while (iterator.hasNext) {
      buffer.append(iterator.next().getAs[Long]("taskId").getOrElse(0))
    }
    buffer.filterNot(taskId => taskId == 0)
  }

}
