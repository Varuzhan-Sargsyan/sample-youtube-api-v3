package com.api.youtube.data

class SearchResponse (
    var kind: String?,
    var etag: String?,
    var nextPageToken: String?,
    var prevPageToken: String?,
    var regionCode: String?,
    var pageInfo: PageInfo?,
    var items: List<SearchItem>?
)

data class Thumbnail(var url: String?, var width: Int?, var height: Int?)

data class Thumbnails(var default: Thumbnail?, var medium: Thumbnail?, var high: Thumbnail?)

data class Snippet(
    var publishedAt: String?,
    var channelId: String?,
    var title: String?,
    var description: String?,
    var thumbnails: Thumbnails?,
    var channelTitle: String?,
    var liveBroadcastContent: String?,
    var publishTime: String?
)

data class Id(var kind: String?, var videoId: String?)

data class PageInfo(var totalResults: Int?, var resultsPerPage: Int?)

data class SearchItem(
    var kind: String?,
    var etag: String?,
    var id: Id?,
    var snippet: Snippet?
)