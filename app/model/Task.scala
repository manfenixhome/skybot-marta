package model

import java.util
import java.util.Date

import org.joda.time.{DateTime}

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
