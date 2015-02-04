package com.mintbeans.geo.web

import com.mintbeans.geo.core.{Location, LocationRepository, Point}
import org.json4s.jackson.JsonMethods._
import org.json4s.{DefaultFormats, Formats, _}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpecLike
import org.scalatra.test.scalatest.ScalatraSuite

class LocationControllerSpec extends ScalatraSuite with FlatSpecLike with MockFactory {

  sealed trait Test {
    implicit val jsonFormats: Formats = DefaultFormats

    val repository = mock[LocationRepository]
    val controller = new LocationController(repository)

    addServlet(controller, "/locations/*")
  }

  "LocationController" should "return a sequence of locations" in new Test {
    val fixture = List(Location("Sopot", Point(54.45, 18.56)), Location("Warsaw", Point(52.23, 21.01)))
    (repository.all _).expects().returning(fixture)

    get("/locations") {
      status should equal(200)

      val response = parse(body)
      val locations = response.extract[List[Location]]

      locations should have length (fixture.size)
    }
  }

}
