import com.music.metadata.models.{Artist, Track}
import com.music.metadata.service.MusicService
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

class MusicServiceSpec extends AnyFlatSpec with Matchers {

  // Initialize the MusicService
  val musicService = new MusicService

  // Test data
  val artistId = "artist123"
  val artistName = "Artist Name"
  val track1: Track = Track("track1", "Song 1", "Pop", 240)
  val track2: Track = Track("track2", "Song 2", "Rock", 300)

  // Test 1: Add a new artist
  "MusicService" should "add a new artist" in {
    musicService.addArtist(artistId, artistName) shouldBe Success(())
    musicService.getArtist(artistId) shouldBe Success(Artist(artistId, artistName, List.empty))
  }

  // Test 2: Add a new track to an artist's catalog
  it should "add a new track to an artist's catalog" in {
    musicService.addTrack(artistId, track1) shouldBe Success(track1.id)
    musicService.getArtist(artistId) shouldBe Success(Artist(artistId, artistName, List(track1)))
  }

  // Test 3: Edit an artist's name
  it should "edit an artist's name" in {
    val newName = "New Artist Name"
    musicService.editArtistName(artistId, newName) shouldBe Success(())
    musicService.getArtist(artistId) shouldBe Success(Artist(artistId, newName, List(track1)))
  }

  // Test 4: Fetch all tracks for an artist
  it should "fetch all tracks for an artist" in {
    musicService.addTrack(artistId, track2) shouldBe Success(track2.id)
    musicService.getArtistTracks(artistId, None, None) shouldBe Success(List(track1, track2))
  }

  // Test 5: Fetch paginated tracks for an artist
  it should "fetch paginated tracks for an artist" in {
    musicService.getArtistTracks(artistId, Some(1), Some(1)) shouldBe Success(List(track1))
    musicService.getArtistTracks(artistId, Some(2), Some(1)) shouldBe Success(List(track2))
  }

  // Test 6: Fetch artist details
  it should "fetch artist details" in {
    musicService.getArtist(artistId) shouldBe Success(Artist(artistId, "New Artist Name", List(track1, track2)))
  }

  // Test 7: Get the 'Artist of the Day' in a cyclical manner
  it should "return the 'Artist of the Day' in a cyclical manner" in {
    // Add a second artist
    val artistId2 = "artist456"
    val artistName2 = "Artist Name 2"
    musicService.addArtist(artistId2, artistName2) shouldBe Success(())

    // Test the cycle
    val artist1 = musicService.getArtistOfTheDay
    val artist2 = musicService.getArtistOfTheDay
    val artist3 = musicService.getArtistOfTheDay

    // Since there are 2 artists, the third call should cycle back to the first artist
    artist3 shouldEqual artist1
  }

  // Test 8: Handle non-existent artist
  it should "throw an exception when trying to add a track to a non-existent artist" in {
    val nonExistentArtistId = "nonExistentArtist"
    val track = Track("track3", "Song 3", "Classical", 360)
    val result = musicService.addTrack(nonExistentArtistId, track)
    result match {
      case Failure(exception) =>
        exception.getMessage shouldBe "Artist not found"
      case _ => fail("Expected Failure, but got success")
    }
  }


  // Test 9: Handle duplicate artist ID
  it should "throw an exception when trying to add an artist with a duplicate ID" in {
    val result = musicService.addArtist(artistId, "Another Artist Name")
    result match {
      case Failure(exception) =>
        exception.getMessage shouldBe "Artist ID already exists"
      case _ => fail("Expected Failure, but got success")
    }
  }

  // Test 10: Handle invalid track data
  it should "throw an exception when trying to add a track with invalid data" in {
    val invalidTrack = Track("", "", "", -1)
    val result = musicService.addTrack(artistId, invalidTrack)
    result match {
      case Failure(exception) =>
        exception.getMessage shouldBe "Invalid track data"
      case _ => fail("Expected Failure, but got success")
    }
  }
}