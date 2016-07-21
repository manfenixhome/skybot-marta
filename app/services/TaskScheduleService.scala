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
        launchTask(task, users, today)
      }
    }
  }

  private def launchTask(task: Task, users: Seq[String], today: Int): Unit = {
    if (task.id == 2) {
      val targetDay = today match {
        case DateTimeConstants.THURSDAY => "Monday"
        case DateTimeConstants.FRIDAY => "Tuesday"
        case DateTimeConstants.MONDAY => "Wednesday"
        case DateTimeConstants.TUESDAY => "Thursday"
        case DateTimeConstants.WEDNESDAY => "Friday"
        case _ => ""
      }
      users.foreach(user => sendService.sendMessage(user, "%s \n%s".format(task.message.format(targetDay), task.answers.mkString("\n"))))
    } else {
      users.foreach(user => sendService.sendMessage(user, "%s \n%s".format(task.message, task.answers.mkString("\n"))))
    }
  }

  def launchTask(task: Task): Unit = {
    println("admin started task=" + task.title)
    launchTask(task, Seq("8:antonekreative"), DateTimeConstants.FRIDAY)
    sendService.sendMessage("8:antonekreative", "Sent")
  }

  def nextExecutionInSeconds(launch: DateTime): Int = {
    Seconds.secondsBetween(DateTime.now(), launch).getSeconds
  }

}
