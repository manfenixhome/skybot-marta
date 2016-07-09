package model

import java.util

/**
  * Created by cheb on 7/9/16.
  */
class Task(
            hasAns: Boolean,
            ttl: Long,
            delay: Long,
            first: Long, //date
            time: Long,
            msg: String,
            answers: util.ArrayList[String]
          ) {

}
