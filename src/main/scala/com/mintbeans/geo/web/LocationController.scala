package com.mintbeans.geo.web

import com.mintbeans.geo.core.LocationRepository
import org.json4s._
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{NotFound, Ok, ScalatraServlet}

class LocationController(locationRepo: LocationRepository) extends ScalatraServlet with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats + Serializers.objectId

  before() {
    contentType = formats("json")
  }

  get("/") {
    if(params.isDefinedAt("name"))
      locationRepo.byNameFragment(params("name"))
    else if (params.isDefinedAt("phrase"))
      locationRepo.byTextPhrase(params("phrase"))
    else
      locationRepo.all()
  }

  get("/:id") {
    locationRepo.byId(new org.bson.types.ObjectId(params("id"))) match {
      case Some(l) => Ok(l)
      case None => NotFound(s"Location not found: ${params("id")}")
    }
  }

}
