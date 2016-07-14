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

  def startPlanning: Unit = {
    //1+1
      Task.tasks.foreach(task => planningTask(task))
      sendService.sendMessage("8:antonekreative", "Tasks started")
  }

  def planningTask(task: Task): Unit = {
    actorSystem.scheduler.schedule(
      Duration.create(nextExecutionInSeconds(task.first), TimeUnit.SECONDS),
      Duration.create(task.delay, TimeUnit.SECONDS)) {
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
    users.foreach(user => sendService.sendMessage(user, "%s \n%s".format(task.message, task.answers.mkString("\n"))))
    sendService.sendMessage("8:antonekreative", "Sent")
  }

  def nextExecutionInSeconds(launch: DateTime): Int = {
    Seconds.secondsBetween(DateTime.now(), launch).getSeconds
  }

}
