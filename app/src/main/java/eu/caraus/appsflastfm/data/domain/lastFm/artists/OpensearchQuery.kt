package eu.caraus.appsflastfm.data.domain.lastFm.artists

import com.google.gson.annotations.SerializedName

data class OpensearchQuery(

	@field:SerializedName("startPage")
	val startPage: String? = null,

	@field:SerializedName("#text")
	val text: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("searchTerms")
	val searchTerms: String? = null
)