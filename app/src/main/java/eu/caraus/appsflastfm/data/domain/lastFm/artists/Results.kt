package eu.caraus.appsflastfm.data.domain.lastFm.artists

import com.google.gson.annotations.SerializedName

data class Results(

        @field:SerializedName("opensearch:Query")
	val opensearchQuery: OpensearchQuery? = null,

        @field:SerializedName("@attr")
	val attr: Attr? = null,

        @field:SerializedName("opensearch:itemsPerPage")
	val opensearchItemsPerPage: String? = null,

        @field:SerializedName("artistmatches")
	val artistmatches: ArtistMatches? = null,

        @field:SerializedName("opensearch:startIndex")
	val opensearchStartIndex: String? = null,

        @field:SerializedName("opensearch:totalResults")
	val opensearchTotalResults: String? = null
)