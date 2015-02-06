package com.mintbeans.geo.web

import com.mintbeans.geo.core.LocationRepository
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

class LocationController(locationRepo: LocationRepository) extends ScalatraServlet with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    if (params.isDefinedAt("phrase"))
      locationRepo.byTextPhrase(params("phrase"))
    else
      locationRepo.all()
  }

  get("/:id") {
    locationRepo.byId(params("id"))
  }

}
