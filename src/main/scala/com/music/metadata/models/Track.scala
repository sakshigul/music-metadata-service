package com.music.metadata.models

case class Track(id: String, title: String, genre: String, length: Int, popularity: Int = 0)
