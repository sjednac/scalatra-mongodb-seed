package com.mintbeans.geo.web

import com.mintbeans.geo.core.LocationRepository
import com.softwaremill.macwire._
import org.scalatra.swagger.Swagger

trait WebModule {

  implicit val swagger = new Swagger(Swagger.SpecVersion, "1.0.0", ApiInfo)

  def locationRepository: LocationRepository

  lazy val documentationController = wire[DocumentationController]

  lazy val locationController = wire[LocationController]
}
