package com.mintbeans.geo.web

import com.mintbeans.geo.core.{Location, LocationRepository}
import org.json4s._
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{BadRequest, NotFound, Ok, ScalatraServlet}
import org.scalatra.swagger._

class LocationController(locationRepo: LocationRepository, implicit val swagger: Swagger) extends ScalatraServlet with JacksonJsonSupport with SwaggerSupport {

  protected val applicationDescription = "Exposes operations for browsing and searching lists of locations, and for retrieving single locations."

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

  val list = (
    apiOperation[List[Location]]("list")
    summary "List all locations"
    parameter queryParam[Option[String]]("name").description("Filter by a name fragment")
    parameter queryParam[Option[String]]("phrase").description("Filter by a text phrase")
    parameter queryParam[Option[Int]]("limit").description(s"Maximum result set size (up to ${maxAllowedLimit})")
  )

  val byId = (
    apiOperation[Location]("byId")
    summary "Get a location by ID"
    parameters (
      pathParam[String]("id").description("Location ID")
    )
  )

  before() {
    contentType = formats("json")
  }

  get("/", operation(list)) {
    if(params.isDefinedAt("name"))
      locationRepo.byNameFragment(params("name"), limit(params))
    else if (params.isDefinedAt("phrase"))
      locationRepo.byTextPhrase(params("phrase"), limit(params))
    else
      locationRepo.all(limit(params))
  }

  get("/:id", operation(byId)) {
    locationRepo.byId(new org.bson.types.ObjectId(params("id"))) match {
      case Some(l) => Ok(l)
      case None => NotFound(s"Location not found: ${params("id")}")
    }
  }

  error {
    case e: LimitValidationException => BadRequest(e.getMessage)
  }
}
