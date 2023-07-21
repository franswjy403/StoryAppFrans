package com.example.storyappfrans.utils

import com.example.storyappfrans.data.local.entity.Story

object DataDummy {
    fun GenerateDummyStoriesEntity(): List<Story> {
        val dummyStoryList = listOf(
            Story(
                id = "story-1i9Kjwr9XstJ2zQN",
                userName = "petersen",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687270069667_MtN2ZcMg.jpg",
                description = "hello world",
                lat = null,
                lon = null
            ),
            Story(
                id = "story-BeWwQ6HfOPAKWEw4",
                userName = "you",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687270023462_Bo1fILGe.jpg",
                description = "ddd",
                lat = null,
                lon = null
            ),
            Story(
                id = "story-R6h9B8dAANEGR9oA",
                userName = "Delvin",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687269686976_HufOOKfM.jpg",
                description = "Doggo",
                lat = null,
                lon = null
            ),
            Story(
                id = "story-tqgGb3BHWa044ImB",
                userName = "menjadi",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687268577165_P5dfE4gN.jpg",
                description = "bab",
                lat = 37.4220936,
                lon = -122.083922
            ),
            Story(
                id = "story-lYSk9p02tKEbMHth",
                userName = "ujang",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687268433320_qWymLkcJ.jpg",
                description = "Finally",
                lat = null,
                lon = null
            ),
            Story(
                id = "story-YoeYET1p2zJYNJH4",
                userName = "menjadi",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687267864704_TZq-oIaY.jpg",
                description = "manas",
                lat = null,
                lon = null
            ),
            Story(
                id = "story-vnXh3h1x1QcbObeA",
                userName = "menjadi",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687267712687_gxAaAsN-.jpg",
                description = "Besok",
                lat = 37.422275375790576,
                lon = -122.0842218026519
            ),
            Story(
                id = "story-vfoRy7S0iD7NApiB",
                userName = "anggur75",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687266877390_ef7eHM4V.jpg",
                description = "test cam",
                lat = null,
                lon = null
            ),
            Story(
                id = "story-XPXBcmZqwvzPfvDF",
                userName = "anggur75",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687266865305_kgzvyzxP.jpg",
                description = "test",
                lat = null,
                lon = null
            ),
            Story(
                id = "story-YrcwU2oj6Q6kBceZ",
                userName = "fajar1",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687266814031_LCEwId3f.jpg",
                description = "Ini adalah deskripsi gambar",
                lat = null,
                lon = null
            ),
            Story(
                id = "story-QNVBdrsdsXxaiHDz",
                userName = "rahmat",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687266608409_VTTpl6VO.jpg",
                description = "testt",
                lat = 1.0,
                lon = 2.0
            ),
            Story(
                id = "story-kyXXE93oDIARX71H",
                userName = "rahmat",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1687266594502_G_kSfl0M.jpg",
                description = "testttt",
                lat = 1.0,
                lon = 2.0
            )
        )
        return dummyStoryList
    }
}