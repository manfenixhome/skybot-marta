package services

import model.UserMessage

/**
  * Created by ekreative on 7/9/2016.
  */
object HelpService {
  val keywords = Seq("hlp", "help", "хелп", "помоги", "помощь", "допомога", "допоможи")

  def hasKeywords(message: String): Boolean = {
    val tags = message.toLowerCase.split(" ")
    keywords.exists(key => tags.contains(key))
  }

  def showHelp(msg: UserMessage, sendService: SendMessageService): Unit = {
    sendService.sendMessage(msg.from,
      "list - show all coworkers [-a] - more info\n" +
      "whois [skype ID] - show user info\n" +
      "opendoor - open garden gate\n" +
      "help - show help\n" +
      "tasks - show all tasks to subscribe\n" +
      "my tasks - show all my subscribed tasks\n" +
      "subscribe [task ID] - subscribe on the task\n" +
      "unsubscribe [task ID] - unsubscribe from the task\n" +
      "book - comming soon"
    )
  }
}
