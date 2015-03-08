package com.mintbeans.geo.web

import com.mintbeans.geo.core.LocationRepository
import org.json4s._
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{BadRequest, NotFound, Ok, ScalatraServlet}

class LocationController(locationRepo: LocationRepository) extends ScalatraServlet with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats + Serializers.objectId

  val maxAllowedLimit = 100

  private class LimitValidationException(msg: String) extends IllegalArgumentException(msg)

  private def limit(params: org.scalatra.Params): Option[Int] = {
    val l = params.getAsOrElse[Int]("limit", maxAllowedLimit)
    if (l <= 0)
      throw new LimitValidationException("Limit must be a positive integer.")
    if (l > maxAllowedLimit)
      throw new LimitValidationException(s"Maximum allowed limit: ${maxAllowedLimit}")

    Some(l)
  }

  before() {
    contentType = formats("json")
  }

  get("/") {
    if(params.isDefinedAt("name"))
      locationRepo.byNameFragment(params("name"), limit(params))
    else if (params.isDefinedAt("phrase"))
      locationRepo.byTextPhrase(params("phrase"), limit(params))
    else
      locationRepo.all(limit(params))
  }

  get("/:id") {
    locationRepo.byId(new org.bson.types.ObjectId(params("id"))) match {
      case Some(l) => Ok(l)
      case None => NotFound(s"Location not found: ${params("id")}")
    }
  }

  error {
    case e: LimitValidationException => BadRequest(e.getMessage)
  }
}
