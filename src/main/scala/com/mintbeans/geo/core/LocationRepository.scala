package com.mintbeans.geo.core

trait LocationRepository {
  def all(): Seq[Location]
}
