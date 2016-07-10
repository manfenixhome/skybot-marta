package services

import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import model.Task
import org.joda.time.{DateTime, DateTimeConstants, LocalDateTime, Seconds}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

/**
  * Created by ekreative on 7/9/2016.
  */
@Singleton
class TaskScheduleService @Inject()(actorSystem: ActorSystem, sendService: SendMessage)(implicit exec: ExecutionContext) {

  val tasks = Seq(
//    Task(1, "You will working from home tomorrow?", 10, 15, new DateTime(), Seq("-Yes", "-No")),
//      Task(2, "Sorry for disturb you, could you please write your lunch for tomorrow? \nhttps://goo.gl/DhwCDt\nThanks", 0 , 30, new DateTime(), Seq())
    Task(1, "You will working tomorrow from home?", 10, 60 * 60 * 24 * 7 /*7 days*/, calcFirstDay(DateTimeConstants.WEDNESDAY, 15, 7), Seq("-Yes", "-No")),
    Task(2, "Sorry for disturb you, could you please write your lunch for tomorrow? \nhttps://goo.gl/DhwCDt\nThanks", 0 , 60 * 60 * 24, calcFirstDay(new DateTime().getDayOfWeek, 17, 1), Seq())
  )

  def startPlanning(): Unit = {
    //1+1
    //razvozka
//    val workingFromHome = Task(1, "You will working from home tomorrow?", 10, 15, new DateTime(), Seq("-Yes", "-No"))

    //var развозка https://goo.gl/VXDzFk
    tasks.foreach(task => planningTask(task))
  }

  def planningTask(task: Task): Unit = {
    actorSystem.scheduler.schedule(
      Duration.create(nextExecutionInSeconds(task.first), TimeUnit.SECONDS),
      Duration.create(task.delay, TimeUnit.SECONDS)) {
      //TODO getList of users for task
      val today = new DateTime().getDayOfWeek
//      if (today != DateTimeConstants.SATURDAY && today != DateTimeConstants.SUNDAY) {
        val users = getUsersByTaskId(task.id)
        users.foreach(user => sendService.sendMessage(user, "%d) %s \n%s".format(task.id, task.message, task.answers.mkString("\n"))))
//      }
    }
  }

  def getUsersByTaskId(taskId: Long): Seq[String] = {
    Seq("8:antonekreative", "8:cheb.ekreative")
  }

  def nextExecutionInSeconds(launch: DateTime): Int = {
    Seconds.secondsBetween(DateTime.now(), launch).getSeconds
  }

  def calcFirstDay(dayOfWeek: Int, hour: Int, delayInDays: Int): DateTime = {
    val d = new DateTime()
    d.withDayOfWeek(dayOfWeek)
      .withHourOfDay(hour)
      .withMillisOfDay(0)
      .withSecondOfMinute(0)
      .withMillisOfDay(0)
    d.isBeforeNow match {
      case true => d
      case false => d.plusDays(delayInDays)
    }
  }



}
