package com.mintbeans.geo.core

trait LocationRepository {
  def all(): Seq[Location]
  def byId(id: String): Option[Location]
  def byNameFragment(name: String): Seq[Location]
  def byTextPhrase(phrase: String): Seq[Location]
}
