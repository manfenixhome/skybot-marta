package model

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import scala.collection.mutable

/**
  * Created by cheb on 7/9/16.
  */
case class User(
                 id: Long,
                 redmineId: String,
                 whereIsSitting: String,
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
//                 images: Seq[Map[String, String]],
//                 technology: List[String],
                 caste: Option[Seq[Caste]],
                 language: Option[Seq[Language]]
               )

object User {

  import model.Caste._
  import model.Language._

//  implicit val technologyReads: Reads[Technology] = (
//      (JsPath \ "id").read[Long] and
//      (JsPath \ "name").read[String] and
//      (JsPath \ "tagType").read[String] and
//      (JsPath \ "description").readNullable[String]
//    ) (Technology.apply _)

  implicit val userReads = (
    (__ \ "id").read[Long] and
      (__ \ "redmineId").read[String] and
      (__ \ "whereIsSitting").read[String] and
      (__ \ "about").read[String] and
      (__ \ "name").read[String] and
      (__ \ "address").read[String] and
      (__ \ "birthday").read[String] and
      (__ \ "closePersonPhoneNumber").read[String] and
      (__ \ "hobby").read[String] and
      (__ \ "homeEmail").read[String] and
      (__ \ "workingEmail").read[String] and
      (__ \ "loginToComputer").read[String] and
      (__ \ "phoneNumber").read[String] and
      (__ \ "title").read[String] and
      (__ \ "skype").read[String] and
      (__ \ "startWorking").read[String] and
      (__ \ "securityKeyToComputer").read[String] and
//      (JsPath \ "images").read[Seq[Map[String, String]]] and
//      (__ \ "technology").read[List[Map[String, String]]].map(_.map(_("name"))) and
      (__ \ "caste").lazyReadNullable(Reads.list[Caste](casteReads)) and
      (__ \ "language").lazyReadNullable(Reads.list[Language](languageReads))
    )(User.apply _)

  def toSkypeMessage(user: User): String = {
    val buffer = mutable.Buffer[String]()
    buffer.append("Full Name: " + user.name)
    buffer.append("Working Skype: " + user.skype)
    buffer.append("Working Email: " + user.workingEmail)
    buffer.append("Start working at eKreative: " + user.startWorking)
    buffer.append("Birthday: " + user.birthday)
//    if (user.technology.nonEmpty){
//      buffer.append("Technologies:")
//      buffer.append(user.technology.mkString(","))
//      buffer.append("\ntechnologies: " + user.technology.get.toString())
//    }
    /*if (item.technology.isDefined && item.technology.get.nonEmpty){
      sb.append(" technologies: \n" )
      sb.append(item.technology.get.map(t => t.name).mkString("\n"))
    }*/
    /*if (item.images.isDefined && item.images.nonEmpty) {
      val image =  item.images.get.head
      sb.append(" image: " + image.src)
    }*/
    buffer.mkString("\n")
  }
}
