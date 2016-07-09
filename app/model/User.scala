package model

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.libs.json
import com.codahale.jerkson.{ParsingException, Json => json}

/**
  * Created by cheb on 7/9/16.
  */
case class User(
                 id: Long,
                 about: String,
                 name: String,
                 address: String,
                 birthday: String,
                 closePersonPhoneNumber: String,
                 hobby: String,
                 homeEmail: String,
                 workingEmail: String,
                 loginToComputer: String,
                 phoneNumber: String,
                 title: String,
                 skype: String,
                 startWorking: String,
                 securityKeyToComputer: String,
                 images: Option[Seq[Image]],
                 technology: Option[Seq[Technology]],
                 caste: Option[Seq[Caste]],
                 language: Option[Seq[Language]]
               )

object User {

  import model.Technology._
  import model.Image._
  import model.Caste._
  import model.Language._

  implicit val userReads: Reads[User] = (
      (JsPath \ "id").read[Long] and
      (JsPath \ "about").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "address").read[String] and
      (JsPath \ "birthday").read[String] and
      (JsPath \ "closePersonPhoneNumber").read[String] and
      (JsPath \ "hobby").read[String] and
      (JsPath \ "homeEmail").read[String] and
      (JsPath \ "workingEmail").read[String] and
      (JsPath \ "loginToComputer").read[String] and
      (JsPath \ "phoneNumber").read[String] and
      (JsPath \ "title").read[String] and
      (JsPath \ "skype").read[String] and
      (JsPath \ "startWorking").read[String] and
      (JsPath \ "securityKeyToComputer").read[String] and
      (JsPath \ "images").lazyReadNullable(Reads.list[Image](imageReads)) and
      (JsPath \ "technology").lazyReadNullable(Reads.list[Technology](technologyReads)) and
      (JsPath \ "caste").lazyReadNullable(Reads.list[Caste](casteReads)) and
      (JsPath \ "language").lazyReadNullable(Reads.list[Language](languageReads))
    ) (User.apply _)
}
