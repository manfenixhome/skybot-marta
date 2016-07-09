package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext

/**
  * Created by ekreative on 7/9/2016.
  */
@Singleton
class ReceiveMessageController @Inject()(implicit exec: ExecutionContext) extends Controller {


    def receive = Action {
      request =>
        println(request.body.toString)
        Ok("OK")
    }
}
