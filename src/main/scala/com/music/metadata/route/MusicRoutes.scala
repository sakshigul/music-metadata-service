package com.music.metadata.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Route
import com.music.metadata.models.{Artist, Track}
import com.music.metadata.service.MusicService
import spray.json.DefaultJsonProtocol

import scala.util.{Failure, Success}

class MusicRoutes(musicService: MusicService)(implicit val system: ActorSystem) extends SprayJsonSupport with DefaultJsonProtocol {

  // JSON formats
  implicit val trackFormat = jsonFormat5(Track)
  implicit val artistFormat = jsonFormat3(Artist)

  val routes: Route =
    pathPrefix("artists") {
      pathEnd {
        post {
          entity(as[Map[String, String]]) { body =>
            val artistId = body("artistId")
            val name = body("name")
            musicService.addArtist(artistId, name) match {
              case Success(_) => complete("Artist added successfully")
              case Failure(ex) => complete(StatusCodes.BadRequest, ex.getMessage)
            }
          }
        }
      } ~
        path(Segment / "tracks") { artistId =>
          post {
            entity(as[Track]) { track =>
              musicService.addTrack(artistId, track) match {
                case Success(trackId) => complete(trackId)
                case Failure(ex) => complete(StatusCodes.BadRequest, ex.getMessage)
              }
            }
          } ~
            get {
              parameters("page".as[Int].?, "pageSize".as[Int].?) { (page, pageSize) =>
                musicService.getArtistTracks(artistId, page, pageSize) match {
                  case Success(tracks) => complete(tracks)
                  case Failure(ex) => complete(StatusCodes.NotFound, ex.getMessage)
                }
              }
            }
        } ~
        path(Segment) { artistId =>
          put {
            entity(as[Map[String, String]]) { body =>
              val newName = body("name")
              musicService.editArtistName(artistId, newName) match {
                case Success(_) => complete("Artist name updated")
                case Failure(ex) => complete(StatusCodes.BadRequest, ex.getMessage)
              }
            }
          } ~
            get {
              musicService.getArtist(artistId) match {
                case Success(artist) => complete(artist)
                case Failure(ex) => complete(StatusCodes.NotFound, ex.getMessage)
              }
            }
        }
    } ~
      path("artist-of-the-day") {
        get {
          musicService.getArtistOfTheDay match {
            case Success(artist) => complete(artist)
            case Failure(ex) => complete(StatusCodes.NotFound, ex.getMessage)
          }
        }
      } ~
      path("search") {
        get {
          parameters("query") { query =>
            complete(musicService.searchArtists(query))
          }
        }
      }
}