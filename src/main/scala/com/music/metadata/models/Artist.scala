package com.music.metadata.models

case class Artist(id: String, name: String, tracks: List[Track] = List.empty)

