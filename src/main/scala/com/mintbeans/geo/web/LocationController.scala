package com.mintbeans.geo.web

import com.mintbeans.geo.core.LocationRepository
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.{NotFound, Ok, ScalatraServlet}
import org.scalatra.json.JacksonJsonSupport

class LocationController(locationRepo: LocationRepository) extends ScalatraServlet with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

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
    locationRepo.byId(params("id")) match {
      case Some(l) => Ok(l)
      case None => NotFound(s"Location not found: ${params("id")}")
    }
  }

}
