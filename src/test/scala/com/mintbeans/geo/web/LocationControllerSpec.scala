package com.mintbeans.geo.web

import com.mintbeans.geo.core.{Location, LocationFixtures, LocationRepository}
import org.json4s.jackson.JsonMethods._
import org.json4s.{DefaultFormats, Formats, _}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpecLike
import org.scalatra.test.scalatest.ScalatraSuite

class LocationControllerSpec extends ScalatraSuite with FlatSpecLike with MockFactory with LocationFixtures {

  implicit val jsonFormats: Formats = DefaultFormats

  val repository = mock[LocationRepository]
  val controller = new LocationController(repository)

  addServlet(controller, "/locations/*")

  "LocationController" should "return a sequence of locations" in {
    val fixture = locations("Sopot", "Warsaw")
    (repository.all _).expects().returning(fixture)

    get("/locations") {
      status should equal(200)

      val response = parse(body)
      val locations = response.extract[List[Location]]

      locations should have length (fixture.size)
    }
  }

  it should "return a sequence of locations filtered by a text phrase" in {
    val fixture = locations("New York", "New Orleans", "New Delhi")
    val phrase = "new"
    (repository.byTextPhrase _).expects(phrase).returning(fixture)

    get("/locations", ("phrase", phrase)) {
      status should equal(200)

      val response = parse(body)
      val locations = response.extract[List[Location]]

      locations should have length (fixture.size)
    }
  }


}
