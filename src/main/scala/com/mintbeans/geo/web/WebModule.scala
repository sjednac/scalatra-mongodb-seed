package com.mintbeans.geo.web

import com.mintbeans.geo.core.LocationRepository
import com.softwaremill.macwire.MacwireMacros._

trait WebModule {

  def locationRepository: LocationRepository

  lazy val locationController = wire[LocationController]

}
