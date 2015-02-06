package com.mintbeans.geo.core

trait LocationFixtures {

  def locations(names: String*) = names.zipWithIndex.map({case (name,i) => Location(i.toString, name, Point(0.0, 0.0))})

  def locationWithId(id: String) = Location(id, "Test", Point(0.0, 0.0))
}
