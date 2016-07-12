package services

import model.UserMessage
import model.Task
import utils.DB

/**
  * Created by ekreative on 7/9/2016.
  */
object SubscribeService {
  val keywords = Seq("subscribe", "unsubscribe")

  def hasKeywords(message: String): Boolean = {
    message.toLowerCase.matches("^(subscribe|unsubscribe).*")
  }

  def doAction(msg: UserMessage, sendService: SendMessageService, db: DB): Unit = {
    println("do some")
    if (msg.content.toLowerCase.matches("^(subscribe|unsubscribe)\\s+\\d+\\s*$")) {
      val taskId = msg.content.toLowerCase.replaceAll("[^0-9]+", "").toInt
      if (taskId > 0 && taskId <= Task.tasks.size) {
        if (msg.content.toLowerCase.matches("^subscribe.*")) {
          if (db.subscribeOnTask(taskId, msg.from)) {
            sendService.sendMessage(msg.from, "Subscribed")
          } else {
            sendService.sendMessage(msg.from, "hm... (movember) seems you already subscribed on this task")
          }
        } else {
          if (db.unsubscribeFromTask(taskId, msg.from)) {
            sendService.sendMessage(msg.from, "Unsubscribed")
          } else {
            sendService.sendMessage(msg.from, "I'm sorry, but seems you aren't subscribed on this task")
          }
        }
      } else {
        sendService.sendMessage(msg.from, "I'm sorry, but I can't find this task ;(. Are you sure that task with provided ID exists?")
      }
      println(msg.content.toLowerCase.replaceAll("[^0-9]+", ""))
    } else {
      sendService.sendMessage(msg.from, "subscribe [task ID] - subscribe on the task\nunsubscribe [task ID] - unsubscribe from the task")
    }
  }
}
