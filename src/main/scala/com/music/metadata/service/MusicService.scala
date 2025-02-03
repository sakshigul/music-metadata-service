package com.music.metadata.service

import com.music.metadata.models.{Artist, Track}

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

class MusicService {
  private val artists: mutable.Map[String, Artist] = mutable.Map.empty
  private var artistOfTheDayIndex: Int = 0

  // Add a new artist
  def addArtist(artistId: String, name: String): Try[Unit] = {
    if (artists.contains(artistId)) {
      Failure(new IllegalArgumentException("Artist ID already exists"))
    } else if (name.isEmpty) {
      Failure(new IllegalArgumentException("Artist name cannot be empty"))
    } else {
      artists += (artistId -> Artist(artistId, name))
      Success(())
    }
  }

  // Add a new track to an artist's catalog
  def addTrack(artistId: String, track: Track): Try[String] = {
    if (!validateTrack(track)) {
      Failure(new IllegalArgumentException("Invalid track data"))
    } else {
      artists.get(artistId) match {
        case Some(artist) =>
          val updatedArtist = artist.copy(tracks = artist.tracks :+ track)
          artists += (artistId -> updatedArtist)
          Success(track.id)
        case None =>
          Failure(new IllegalArgumentException("Artist not found"))
      }
    }
  }

  // Edit an artist's name
  def editArtistName(artistId: String, newName: String): Try[Unit] = {
    if (newName.isEmpty) {
      Failure(new IllegalArgumentException("Artist name cannot be empty"))
    } else {
      artists.get(artistId) match {
        case Some(artist) =>
          val updatedArtist = artist.copy(name = newName)
          artists += (artistId -> updatedArtist)
          Success(())
        case None =>
          Failure(new IllegalArgumentException("Artist not found"))
      }
    }
  }

  // Fetch all tracks for an artist with pagination optional
  def getArtistTracks(artistId: String, page: Option[Int] = None, pageSize: Option[Int] = None): Try[List[Track]] = {
    artists.get(artistId) match {
      case Some(artist) =>
        val tracks = artist.tracks
        val paginatedTracks = (page, pageSize) match {
          case (Some(p), Some(ps)) => tracks.slice((p - 1) * ps, p * ps)
          case _ => tracks // Return all tracks if pagination parameters are missing
        }
        Success(paginatedTracks)
      case None =>
        Failure(new IllegalArgumentException("Artist not found"))
    }
  }

  // Fetch artist details
  def getArtist(artistId: String): Try[Artist] = {
    artists.get(artistId) match {
      case Some(artist) => Success(artist)
      case None => Failure(new IllegalArgumentException("Artist not found"))
    }
  }

  // Search artists by name
  def searchArtists(query: String): List[Artist] = {
    artists.values.filter(_.name.toLowerCase.contains(query.toLowerCase)).toList
  }

  // Get the "Artist of the Day"
  def getArtistOfTheDay: Try[Artist] = {
    val artistList = artists.values.toList
    if (artistList.isEmpty) {
      Failure(new IllegalStateException("No artists available"))
    } else {
      // Use modulo to cycle back to the beginning
      val artist = artistList(artistOfTheDayIndex % artistList.size)
      artistOfTheDayIndex += 1 // Increment index for the next call
      Success(artist)
    }
  }

  // Validate track data
  private def validateTrack(track: Track): Boolean = {
    track.title.nonEmpty && track.genre.nonEmpty && track.length > 0
  }
}