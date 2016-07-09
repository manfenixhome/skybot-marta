package controllers

import javax.inject.{Inject, Singleton}

import model.UserMessage
import play.api.mvc.{Action, Controller}
import play.libs.Json
import com.codahale.jerkson.{ParsingException, Json => json}

import scala.concurrent.ExecutionContext

/**
  * Created by ekreative on 7/9/2016.
  */
@Singleton
class ReceiveMessageController @Inject()(implicit exec: ExecutionContext) extends Controller {

    def receive = Action(parse.json) {
      request =>
        val message = request.body.as[Seq[UserMessage]]
        println(message)
        Ok("OK")
    }
}
