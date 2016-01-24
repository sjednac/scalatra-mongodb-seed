package com.mintbeans.geo

import com.mintbeans.geo.data.DataModule
import com.mintbeans.geo.web.WebModule
import com.typesafe.config.ConfigFactory
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.webapp.WebAppContext
import org.slf4j.{Logger, LoggerFactory}

object LocationProvider extends App with WebModule with DataModule {
  val logger = LoggerFactory.getLogger(getClass)
  val config = ConfigFactory.load()
  val server = new Server(config.getInt("http.port"))
  val webCtx = new WebAppContext()
  webCtx.setContextPath(config.getString("http.path"))
  webCtx.setResourceBase("/WEB-INF")
  webCtx.addServlet(new ServletHolder("locations", locationController), "/locations/*")
  webCtx.addServlet(new ServletHolder(documentationController), "/api-docs/*")

  server.setHandler(webCtx)
  server.start
  logger.info("Server started.")
  server.join
}
