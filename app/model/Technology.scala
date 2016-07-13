package model

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import scala.collection.mutable

/**
  * Created by cheb on 7/9/16.
  */
case class Technology(
                       id: Long,
                       name: String,
                       tagType: String,
                       description: Option[String]
                     )

object TechnologyO {

  implicit val technologyReads: Reads[Technology] = (
      (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "tagType").read[String] and
      (JsPath \ "description").readNullable[String]
    ) (Technology.apply _)

  def toSkypeMessage(technology: Technology): String = {
    val buffer = mutable.Buffer[String]()
    buffer.append(technology.name)
    buffer.mkString("\n")
  }
}
