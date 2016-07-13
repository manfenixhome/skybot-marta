package services

import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import model.Task
import org.joda.time.{DateTime, DateTimeConstants, LocalDateTime, Seconds}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import utils.DB

/**
  * Created by ekreative on 7/9/2016.
  */
@Singleton
class TaskScheduleService @Inject()(actorSystem: ActorSystem, sendService: SendMessage, db: DB)(implicit exec: ExecutionContext) {

  var isStarted = false

  def startPlanning: Unit = {
    //1+1

    if (!isStarted) {
      Task.tasks.foreach(task => planningTask(task))
      isStarted = true
      sendService.sendMessage("8:antonekreative", "Tasks started")
    } else {
      sendService.sendMessage("8:antonekreative", "Tasks already started")
    }
  }

  def planningTask(task: Task): Unit = {
    actorSystem.scheduler.schedule(
      Duration.create(nextExecutionInSeconds(task.first), TimeUnit.SECONDS),
      Duration.create(task.delay, TimeUnit.SECONDS)) {
      //TODO getList of users for task
      val today = new DateTime().getDayOfWeek
      if (today != DateTimeConstants.SATURDAY && today != DateTimeConstants.SUNDAY) {
        val users = db.getUsersByTaskId(task.id)
        users.foreach(user => sendService.sendMessage(user, "%s \n%s".format(task.message, task.answers.mkString("\n"))))
      }
    }
  }

  def launchTask(task: Task): Unit = {
    println("admin started task=" + task.title)
    val users = db.getUsersByTaskId(task.id)
    println("users=" + users)
    sendService.sendMessage("8:antonekreative", "%d) %s \n%s".format(task.id, task.message, task.answers.mkString("\n")))
  }

  def nextExecutionInSeconds(launch: DateTime): Int = {
    Seconds.secondsBetween(DateTime.now(), launch).getSeconds
  }

}
