package com.example.wapp.data.remote.model


data class GeocodeResponse(
    val type: String,
    val features: List<Feature>
)



data class Context(
    val country: Country?,
    val region: Region?,
    val postcode: Postcode?,
    val district: District?,
    val place: Place?,
    val locality: Locality?,
    val neighborhood: Neighborhood?,
    val address: Address?,
    val street: Street?
)

data class Country(val id: String, val name: String, val country_code: String)
data class Region(val id: String, val name: String)
data class Postcode(val id: String, val name: String)
data class District(val id: String, val name: String)
data class Place(val id: String, val name: String)
data class Locality(val id: String, val name: String)
data class Neighborhood(val id: String, val name: String)
data class Address(val id: String, val name: String, val address_number: String, val street_name: String)
data class Street(val id: String, val name: String)

// Data model for the "coordinates" information
data class Coordinates(
    val latitude: Double?,
    val longitude: Double?,
    val routable_points: List<RoutablePoint>?
)

data class RoutablePoint(
    val name: String?,
    val latitude: Double?,
    val longitude: Double?
)

// Data model for the "properties" section
data class Properties(
    val name: String?,
    val mapbox_id: String?,
    val feature_type: String?,
    val address: String?,
    val full_address: String?,
    val place_formatted: String?,
    val context: Context?,
    val coordinates: Coordinates?,
    val maki: String?,
    val poi_category: List<String>?,
    val poi_category_ids: List<String>?,
    val external_ids: ExternalIds?,
    val metadata: Map<String, Any>?
)

// Data model for external ids
data class ExternalIds(
    val safegraph: String?,
    val foursquare: String?
)

// Feature data model
data class Feature(
    val type: String?,
    val geometry: Geometry?,
    val properties: Properties?
)

data class Geometry(
    val coordinates: List<Double>?,
    val type: String?
)

