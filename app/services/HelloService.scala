package services

import javax.inject.Singleton

import model.UserMessage

/**
  * Created by ekreative on 7/9/2016.
  */
object HelloService {
  val keywords = Seq("hi")

  def hasKeywords(message: String): Boolean = {
    val tags = message.toUpperCase.split(" ")
    keywords.exists(key => tags.contains(key))
  }

  def doAction(msg: UserMessage, sendService: SendMessageService): Unit = {
    sendService.sendMessage(msg.from, "Hi "+ msg.from)
  }

}
