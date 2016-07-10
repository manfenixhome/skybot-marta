package model

import java.util
import java.util.Date

import org.joda.time.{DateTime, DateTimeConstants}

/**
  * Created by cheb on 7/9/16.
  */
case class Task(id: Long,
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
    Task(1, "You will working tomorrow from home?", 10, 60 * 60 * 24 * 7 /*7 days*/, calcFirstDay(DateTimeConstants.WEDNESDAY, 15, 7), Seq("-Yes", "-No")),
    Task(2, "Sorry for disturb you, could you please write your lunch for tomorrow? \nhttps://goo.gl/DhwCDt\nThanks", 0 , 60 * 60 * 24, calcFirstDay(new DateTime().getDayOfWeek, 17, 1), Seq())
  )

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
