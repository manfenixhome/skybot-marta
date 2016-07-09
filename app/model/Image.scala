package model


import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
/**
  * Created by cheb on 7/9/16.
  */
case class Image(
             id: Long,
             name: String,
             src: String
           )
object Image{
  implicit val imageReads: Reads[Image] = (
      (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "src").read[String]
    ) (Image.apply _)
}
