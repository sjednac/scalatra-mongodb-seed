package com.mintbeans.geo.core

trait LocationRepository {
  def all(): Seq[Location]
  def byTextPhrase(phrase: String): Seq[Location]
}
