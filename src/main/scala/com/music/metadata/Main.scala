package com.music.metadata

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.music.metadata.route.MusicRoutes
import com.music.metadata.service.MusicService

import scala.util.{Failure, Success}

object Main extends App {
  // Initialize ActorSystem
  implicit val system: ActorSystem = ActorSystem("music-metadata-service")

  // Initialize MusicService and Routes
  val musicService = new MusicService
  val routes: Route = new MusicRoutes(musicService).routes

  // Start the HTTP server
  val serverBinding = Http().newServerAt("localhost", 8080).bind(routes)

  // Handle server binding result
  serverBinding.onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      println(s"Server online at http://${address.getHostString}:${address.getPort}/")
    case Failure(ex) =>
      println(s"Failed to bind HTTP server: ${ex.getMessage}")
      system.terminate()
  }(system.dispatcher)
}