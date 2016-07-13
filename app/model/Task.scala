package model

import java.util
import java.util.Date

import org.joda.time.{DateTime, DateTimeConstants}

/**
  * Created by cheb on 7/9/16.
  */
case class Task(id: Long,
                title: String,
                message: String,
                ttl: Long,
                delay: Long, // in millis
                first: DateTime,
                answers: Seq[String]
               ) {
}

object Task {
  val tasks = Seq(
    //    Task(1, "You will working from home tomorrow?", 10, 15, new DateTime(), Seq("-Yes", "-No")),
    //      Task(2, "Sorry for disturb you, could you please write your lunch for tomorrow? \nhttps://goo.gl/DhwCDt\nThanks", 0 , 30, new DateTime(), Seq())
    Task(1, "Working from home", "You will working tomorrow from home?", 10, 60 * 60 * 24 * 7 /*7 days*/, calcFirstDay(DateTimeConstants.WEDNESDAY, 12,0, 7), Seq("-Yes", "-No")),
    Task(2, "Food hacker lunch", "Sorry for disturb you, could you please write your lunch for tomorrow? \nhttps://goo.gl/DhwCDt\nThanks", 0 , 60 * 60 * 24 /*every day*/, calcFirstDay(new DateTime().getDayOfWeek, 14,0, 1), Seq()),
    Task(3, "Razvozka", "Sorry for disturb you, only want to reminding you don't forget booking seats for your journey to work or home? \nhttps://goo.gl/VXDzFk\nThanks", 0 , 60 * 60 * 24 /*every day*/, calcFirstDay(new DateTime().getDayOfWeek, 13,30, 1), Seq()),
    Task(4, "Track your time", "I know you going to go home, only want to reminding you don't forget track your time in Redmine.\nThanks and have a lovely evening (hug) ", 0 , 60 * 60 * 24 /*every day*/, calcFirstDay(new DateTime().getDayOfWeek, 14,55, 1), Seq())
  )

  def calcFirstDay(dayOfWeek: Int, hour: Int, minutes: Int, delayInDays: Int): DateTime = {
    var d = new DateTime()
    d = d.withDayOfWeek(dayOfWeek)
      .withHourOfDay(hour)
      .withMinuteOfHour(minutes)
      .withSecondOfMinute(0)
    d.isAfterNow match {
      case true => d
      case false => d.plusDays(delayInDays)
    }
  }
}
