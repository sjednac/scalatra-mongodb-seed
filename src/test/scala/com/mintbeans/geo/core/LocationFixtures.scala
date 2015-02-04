package com.mintbeans.geo.core

trait LocationFixtures {

  def locations(names: String*) = names.map(name => Location(name, Point(0.0, 0.0)))

}
