package com.example.wapp.screen.maps

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.wapp.R
import com.example.wapp.data.Markers
import com.example.wapp.data.NavigationHistory
import com.example.wapp.data.Photo
import com.example.wapp.data.Reviews
import com.example.wapp.data.remote.api.MapboxRetrofitClient
import com.example.wapp.data.remote.api.RetrofitClient
import com.example.wapp.data.remote.api.UploadResponse
import com.example.wapp.data.remote.model.GeocodeResponse
import com.example.wapp.data.remote.model.MapboxSearchResponse
import com.example.wapp.getCurrentTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mapbox.geojson.Point
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.URLEncoder
import java.util.UUID


class MapsViewModel(application: Application): AndroidViewModel(application= application){

    val LOG_TAG = "testMapbox"
    private val MARKER_COLLECTION = "markers"
    private val NAVIGASI_COLLECTION= "navigasi_route_user"
    private val REVIEW_COLLECTION = "reviews"
    private val IMAGE_COLLECTION = "images"

    private val firestore = FirebaseFirestore.getInstance()


    private val _streetName = MutableStateFlow<String?>(null)
    val streetName: StateFlow<String?> get() = _streetName


    private val _markers = MutableStateFlow<List<Markers>?>(null)
    val markers: StateFlow<List<Markers>?> get() = _markers

    private val _marker = MutableStateFlow<Markers?>(null)
    val marker: StateFlow<Markers?> get() = _marker


    private val _images = MutableStateFlow<List<Photo>?>(null)
    val images: StateFlow<List<Photo>?> get() = _images

    private val _reviews = MutableStateFlow<List<Reviews>?>(null)
    val reviews: StateFlow<List<Reviews>?> get () =_reviews

    private val _navigationHistory = MutableStateFlow<NavigationHistory?>(null)
    val navigationHistory: StateFlow<NavigationHistory?> get() = _navigationHistory


    private val _listnavigationHistory = MutableStateFlow<List<NavigationHistory>?>(null)
    val listnavigationHistory: StateFlow<List<NavigationHistory>?> get() = _listnavigationHistory

    private val _photos = MutableStateFlow<List<Photo>?>(null)
    val photos: StateFlow<List<Photo>?> get() = _photos

    private val _currentMarkersSelected = MutableStateFlow<Markers?>(null)
    val currentMarkersSelected: StateFlow<Markers?> get() = _currentMarkersSelected


    private val _listsearchPlace = MutableStateFlow<List<Markers>?>(null)
    val listSearchPlace: StateFlow<List<Markers>?> get () = _listsearchPlace

    private val _searchPlaceSelected = MutableStateFlow<Markers?>(null)
    val searchPlaceSelected: StateFlow<Markers?> get () = _searchPlaceSelected


    private val _isFocusPositionUser = MutableStateFlow<Boolean>(true)
    val isFocusPositionUser: StateFlow<Boolean> get () = _isFocusPositionUser

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean>
        get() = _isError


    val errorHandler = CoroutineExceptionHandler{
            _, error ->
        if (error is Exception){
            _isError.value = true
        }
    }


    private val _destinationPoint = MutableStateFlow<Point?>(null)
    val destinationPoint: StateFlow<Point?> get() = _destinationPoint


    private val _showDialog = MutableStateFlow<Boolean>(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog



    private val _isStart = MutableStateFlow<Boolean>(false)
    val isStart: StateFlow<Boolean> get() = _isStart

    private val _markerIsAdded = MutableStateFlow<Boolean>(false)
    val markerIsAdded: StateFlow<Boolean> get() = _markerIsAdded


    lateinit var navigation: MapboxNavigation



    fun updateFocusUserCamera(status: Boolean){
        _isFocusPositionUser.value = status
    }


    fun updateStatusmarkerIsAdded(status: Boolean){
        _markerIsAdded.value = status
    }


    fun  updateSearchPlaceSelected(selectedMarkers: Markers?){
        _searchPlaceSelected.value = selectedMarkers
    }

    fun updateStartNavigation(status: Boolean){
        _isStart.value = status
    }

    fun updateShowDialog(status: Boolean){
        _showDialog.value = status
    }
    fun selectedMarkers(marker: Markers?){
        _currentMarkersSelected.value = marker
    }
    fun updateStreetName(streetName: String?){
        _streetName.value = streetName
    }

    fun updateDestinationPosition(point: Point?) {
        _destinationPoint.value = point
    }
    fun setMapboxNavigation(mapboxNav: MapboxNavigation) {
        navigation = mapboxNav
    }

    fun getFeatureDetails( accessToken: String, id: String) {
         val sessionToken = UUID.randomUUID().toString() // Generate a new session token
         viewModelScope.launch {
            try {
                val response = MapboxRetrofitClient.geocodingService.getFeatureDetails(
                    id = id,
                    accessToken = accessToken,
                    sessionToken = sessionToken
                )
                if (response.features != null) {
                    val feature = response.features[0]
                    val cordinate = feature.geometry?.coordinates!!
                    val newMarker = Markers(
                        markerId = feature.properties?.mapbox_id ?: "No Namme",
                        locationName = feature.properties?.name ?: "No Name" ,
                        streetName = feature.properties?.full_address ?: "No Address",
                        type = "",
                        description = "No description available",
                        latitude =cordinate[1],
                        longitude =cordinate[0],
                        categoryId = "",
                        addedBy = "",
                        createdAt = "2024-12-20T00:00:00Z",
                        updatedAt = "2024-12-20T00:00:00Z"
                    )

                    updateSearchPlaceSelected(newMarker)
                } else {
                    Log.d("Response-Q", "No features found")
                }

        } catch (e: Exception) {
            null
        } }
    }




    fun search(query: String, accessToken: String) {
        val sessionToken = UUID.randomUUID().toString() // Generate a new session token

        viewModelScope.launch {
            try {

                val response = MapboxRetrofitClient.geocodingService.getSuggestedResults(
                    query = query,
                    limit = 5,
                    types = "poi",
                    country = "ID",
                    accessToken = accessToken,
                    sessionToken =sessionToken
                )


                if (response.suggestions.isNotEmpty()) {
                    val updatedList = _listsearchPlace.value?.toMutableList() ?: mutableListOf()
                    response.suggestions.forEach { feature ->

                        val newMarker = Markers(
                            markerId = feature.mapbox_id,
                            locationName = feature.name,
                            streetName = feature.full_address ?: "",
                            type = "",
                            description = "No description available",
                            latitude = 0.0,
                            longitude = 0.0,
                            categoryId = "",
                            addedBy = "",
                            createdAt = "2024-12-20T00:00:00Z",
                            updatedAt = "2024-12-20T00:00:00Z"
                        )


                        if (updatedList.none { it.streetName == newMarker.streetName }) {
                            updatedList.add(newMarker)
                        }
                    }

                    // Memperbarui state dengan daftar marker baru
                    _listsearchPlace.value = updatedList
                } else {
                    Log.d("Response-Q", "No features found")
                }
            } catch (e: Exception) {
                Log.e("SuggestError", "Error fetching suggestions: ${e.message}")
            }
        }
    }

    fun clearListsearchPlace(){
        _listsearchPlace.value = null
    }


    fun getListOfMarkerWisata() {
        viewModelScope.launch {
            try {
                val markersList = withContext(Dispatchers.IO) {
                    getMarkersByType("wisata")
                }
                _markers.value = markersList
            } catch (e: Exception) {
                _markers.value = null
            }
        }
    }

    fun  getListOfReviewByIdMarker(id: String){
        viewModelScope.launch {
            try {
                val listreviews = withContext(Dispatchers.IO +  errorHandler){
                    getAllReviewByIdMarker(id)
                }
                _reviews.value = listreviews

            }catch (e: Exception){
                _reviews.value = null
            }
        }
    }


    fun getListOfMarkerKuliner() {
        viewModelScope.launch {
            try {
                val markersList = withContext(Dispatchers.IO + errorHandler) {
                    getMarkersByType("kuliner")
                }
                _markers.value = markersList
            } catch (e: Exception) {
                _markers.value = null
            }
        }
    }
    fun getMarkersById(id: String) {
        viewModelScope.launch {
            try {
                val markersList = withContext(Dispatchers.IO + errorHandler) {
                    getMarkerId(id)
                }
                _marker.value = markersList
            } catch (e: Exception) {
                _marker.value = null
            }
        }
    }
    fun getPhoto(id: String) {
        viewModelScope.launch {
            try {
                val photos = withContext(Dispatchers.IO + errorHandler) {
                    getPhotosByMarkerId(id)
                }
                _photos.value = photos
            } catch (e: Exception) {
                _photos.value = null
            }
        }
    }

    fun getAllMarkers() {
        viewModelScope.launch {
            try {
                val markersList = withContext(Dispatchers.IO + errorHandler) {
                    AllMarker()
                }
                _markers.value = markersList
            } catch (e: Exception) {
                _markers.value = null
            }
        }
    }

    fun getAllImage() {
        viewModelScope.launch {
            try {
                val images = withContext(Dispatchers.IO + errorHandler) {
                    AllImage()
                }
                _images.value = images
            } catch (e: Exception) {
                _markers.value = null
            }
        }
    }
    fun createNavigation(userId:String, idMarker:String, jarak: String, durationRemaining:String, startStreetName:String, context: Context, locationPoint: Point){
        val key = context.getString(R.string.mapbox_access_token)
       viewModelScope.launch {
           try {
               withContext(Dispatchers.IO + errorHandler){
                   startNavigation(
                       userId= userId,
                       idMarker = idMarker,
                       startStreetName = startStreetName,
                       context = context,
                       durationRemaining = durationRemaining,
                       jarak = jarak
                   ){ nav ->
                       _navigationHistory.value = nav
                   }
               }
           }catch (e: Exception){
               _navigationHistory.value = null
           }
       }
    }
    fun getStreetMarkersByGeometry(longitude: Double, latitude: Double, accessToken: String) {
        viewModelScope.launch {
            try {
                val geocodeResponse = withContext(Dispatchers.IO + errorHandler) {
                    getReverseGeocode(latitude = latitude, longitude = longitude, accessToken = accessToken)
                }
                val geo = geocodeResponse!!.features[0].properties
                Log.d("geocodeResponse", "${geocodeResponse.features}")

                geo?.let {
                    val address = it.address ?: it.full_address
                        updateStreetName(address)
                }

            } catch (e: Exception) {
            }
        }
    }

    suspend fun getReverseGeocode(longitude: Double, latitude: Double, accessToken: String): GeocodeResponse? {
        return try {
            val response = MapboxRetrofitClient.geocodingService.reverseGeocode(longitude, latitude, accessToken)
            if (response.isSuccessful) {
                response.body() // Return the response body if successful
            } else {
                null // Handle error cases
            }
        } catch (e: Exception) {
            null // Handle network or other exceptions
        }
    }


    fun getAllNavigationHistoryUser(id:String){
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO + errorHandler){
                    val history = getNavigationHistoryByUserID(id)
                    _listnavigationHistory.value = history
                }
            }catch (e: Exception){
                _listnavigationHistory.value = null
            }
        }
    }
    fun stopUserNavigation(navigationHistory: NavigationHistory,context: Context){
        val currentTimestamp = getCurrentTimestamp()

        val data = navigationHistory.copy(
            navId = navigationHistory.navId,
            endTime = currentTimestamp
        )
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO + errorHandler){
                    navigationHistory.navId?.let {
                        updateNavigationData(context =context, documentId = navigationHistory.navId, updatedData =  data.toMap()){
                            updateStartNavigation(false)
                        }
                    }


                }
            }catch (e: Exception){
                _navigationHistory.value = null
            }
        }

    }
    private fun startNavigation(userId:String, idMarker:String, jarak: String, durationRemaining:String, startStreetName:String, context: Context, onSuccess: (NavigationHistory) -> Unit){
         val currentTimestamp = getCurrentTimestamp()
         val navigationData = NavigationHistory(
             createdAt = currentTimestamp,
             startTime = currentTimestamp,
             startStreetName = startStreetName,
             userId = userId,
             destinationId = idMarker,
             totalJarak = jarak,
             totalWaktu = durationRemaining
         )
         val  navData = navigationData.toMap()
         firestore.collection(NAVIGASI_COLLECTION)
            .add(navData)
            .addOnSuccessListener { documentReference->
                Toast.makeText(context, "Memulai Navigasi", Toast.LENGTH_SHORT).show()
                updateStatusmarkerIsAdded(true)
                val navDataCopy = navigationData.copy(
                    navId =  documentReference.id
                )
                onSuccess(navDataCopy)
            }.addOnFailureListener{
                Toast.makeText(context, "Gagal Menyimpan Data navigasi ", Toast.LENGTH_SHORT).show()
            }
    }


    fun updateNavigationData(documentId: String, updatedData: Map<String, Any>, context: Context, onSuccess: () -> Unit) {
        firestore.collection(NAVIGASI_COLLECTION)
            .document(documentId)
            .update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(context, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Gagal memperbarui data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    suspend private fun AllImage(): List<Photo>? {
        return try {
            val querySnapshot = firestore.collection(IMAGE_COLLECTION)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                querySnapshot.documents.map { document ->
                  val id = document.id
                    val markerId = document.getString("markerId") ?: ""
                    val uploadedBy = document.getString("uploadedBy") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val createdAt = document.getString("createdAt") ?: ""
                    val updatedAt = document.getString("updatedAt") ?: ""

                    Photo(
                         id= id,
                        markerId=markerId,
                        uploadedBy = uploadedBy,
                        imageUrl =imageUrl,
                        createdAt =createdAt,
                        updatedAt = updatedAt,
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    suspend private fun AllMarker(): List<Markers>? {
        return try {
            val querySnapshot = firestore.collection(MARKER_COLLECTION)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                querySnapshot.documents.map { document ->
                    val idMarker = document.id
                    val locationName = document.getString("locationName") ?: ""
                    val latitude = document.getDouble("latitude") ?: 0.0
                    val longitude = document.getDouble("longitude") ?: 0.0
                    val type = document.getString("type") ?: ""
                    val categoryId = document.getString("categoryId") ?: "Tidak Ditentukan"
                    val createdAt = document.getString("createdAt") ?: "Tidak Ditentukan"
                    val updatedAt = document.getString("updatedAt") ?: "Tidak Ditentukan"
                    val description = document.getString("description") ?: ""
                    val streetName = document.getString("streetName") ?: "Lokasi Tidak Mendukung"
                    // Return the Markers object for each document
                    Markers(
                        id = idMarker,
                        locationName = locationName,
                        categoryId = categoryId,
                        type = type,
                        longitude = longitude,
                        latitude = latitude,
                        addedBy = "Admin",
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        description = description,
                        streetName = streetName
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getNavigationById(id: String): NavigationHistory?{
        return try {
            val querySnapShot = firestore.collection(NAVIGASI_COLLECTION).document(id)
                .get()
                .await()
            if (querySnapShot != null){
                val navID = querySnapShot.id
                val userId = querySnapShot.getString("userId") ?: ""
                val destinationId = querySnapShot.getString("destinationId") ?: ""
                val totalJarak = querySnapShot.getString("totalJarak") ?: ""
                val startStreetName = querySnapShot.getString("startStreetName") ?: ""
                val totalWaktu = querySnapShot.getString("totalWaktu") ?: ""
                val startTime = querySnapShot.getString("startTime") ?: ""
                val createdAt = querySnapShot.getString("createdAt") ?: ""
                val endTime = querySnapShot.getString("endTime") ?: ""

                NavigationHistory(
                    endTime = endTime,
                    userId = userId,
                    navId = navID,
                    startTime = startTime,
                    createdAt = createdAt,
                    destinationId = destinationId,
                    totalWaktu = totalWaktu, totalJarak = totalJarak,
                    startStreetName = startStreetName
                )
            }else{
                null
            }
        }catch (e:Exception){
            null
        }
    }


    suspend fun getNavigationHistoryByUserID(userId:String): List<NavigationHistory>? {
        return try {
            val querySnapshot = firestore.collection(NAVIGASI_COLLECTION)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                querySnapshot.documents.map { document ->
                    val navID = document.id
                    val user = document.getString("userId") ?: ""
                    val destinationId = document.getString("destinationId") ?: ""
                    val totalJarak = document.getString("totalJarak") ?: ""
                    val startStreetName = document.getString("startStreetName") ?: "lokasi tidak didukung"
                    val totalWaktu = document.getString("totalWaktu") ?: ""
                    val startTime = document.getString("startTime") ?: ""
                    val createdAt = document.getString("createdAt") ?: ""
                    val endTime = document.getString("endTime") ?: ""

                    val street = if(startStreetName.isNotEmpty())  startStreetName else "lokasi tidak didukung"

                    NavigationHistory(
                        endTime = endTime,
                        userId = user,
                        navId = navID,
                        startTime = startTime,
                        createdAt = createdAt,
                        destinationId = destinationId,
                        totalWaktu = totalWaktu, totalJarak = totalJarak,
                        startStreetName = street
                    )

                }
            } else {
                null
            }
        } catch (e: Exception) {
            // Handle any exceptions (e.g., network failure)
            null
        }
    }

    suspend fun getAllReviewByIdMarker(id: String):List<Reviews>?{
        return  try {
            val querySnapShot = firestore.collection(REVIEW_COLLECTION)
                .whereEqualTo("markerId", id)
                .get()
                .await()
            if (querySnapShot != null && !querySnapShot.isEmpty){
                querySnapShot.documents.map { document ->
                    val idreview = document.id
                    val idMarker = document.getString("markerId") ?: ""
                    val userid = document.getString("userId") ?: ""
                    val rating = document.getLong("rating") ?: 0.0
                    val createdAt = document.getString("createdAt") ?: ""
                    val review = document.getString("review") ?: ""
                    Reviews(
                        idReview = idreview,
                        markerId = idMarker,
                        userId= userid,
                        rating= rating,
                        createdAt = createdAt,
                        review =  review,

                    )
                }
            }else{
                null
            }
        }catch (e: Exception){
            null
        }
    }

    suspend fun getMarkersByType(type: String): List<Markers>? {
        return try {
            val querySnapshot = firestore.collection(MARKER_COLLECTION)
                .whereEqualTo("type", type)
                .get()
                .await()
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                querySnapshot.documents.map { document ->
                    val idMarker = document.id
                    val locationName = document.getString("locationName") ?: ""
                    val latitude = document.getDouble("latitude") ?: 0.0
                    val longitude = document.getDouble("longitude") ?: 0.0
                    val types = document.getString("type") ?: ""
                    val categoryId = document.getString("categoryId") ?: "Tidak Ditentukan"
                    val createdAt = document.getString("createdAt") ?: "Tidak Ditentukan"
                    val updatedAt = document.getString("updatedAt") ?: "Tidak Ditentukan"
                    val description = document.getString("description") ?: ""
                    val streetName = document.getString("streetName") ?: ""

                    Markers(
                        id = idMarker,
                        locationName = locationName,
                        categoryId = categoryId,
                        type = types,
                        longitude = longitude,
                        latitude = latitude,
                        addedBy = "Admin",
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        description = description,
                        streetName = streetName
                    )
                }
            } else {

                null
            }
        } catch (e: Exception) {
            null
        }
    }
    suspend fun getPhotosByMarkerId(markId: String): List<Photo>? {
        return try {
            val querySnapshot = firestore.collection(IMAGE_COLLECTION)
                .whereEqualTo("markerId", markId)
                .get()
                .await()
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                querySnapshot.documents.map { document ->
                    val id = document.id
                    val markerId = document.getString("markerId")?:""
                    val imageUrl = document.getString("imageUrl")?:""
                    val uploadBy = document.getString("uploadedBy")?:""
                    val createdAt = document.getString("createdAt")?:""
                    val updatedAt = document.getString("updatedAt")?:""
                    Photo(
                        id = id,
                        markerId = markerId,
                        imageUrl = imageUrl,
                        uploadedBy = uploadBy,
                        createdAt = createdAt,
                        updatedAt = updatedAt

                    )

                }
            } else {

                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun sendReview(reviews: Reviews, context: Context){
        val currentTimestamp = getCurrentTimestamp()

        val review = reviews.copy(
            createdAt = currentTimestamp
        )
        val reviewData = review.toMap()

        firestore.collection(REVIEW_COLLECTION)
            .add(reviewData)
            .addOnSuccessListener {
                Toast.makeText(context, "Berhasil Menambahkan Review", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(context, "Gagal Menambahkan Review", Toast.LENGTH_SHORT).show()
            }

    }

    fun saveMarkerToDatabase(markers: Markers, context: Context){
        val currentTimestamp = getCurrentTimestamp()

        val marker = markers.copy(
            locationName = markers.locationName,
            type =markers.type,
            description = markers.description,
            createdAt = currentTimestamp,
            latitude = markers.latitude,
            longitude = markers.longitude,
            updatedAt = currentTimestamp,
            addedBy = "admin",
            categoryId = "",
        )
        val markerData = marker.toMap()

        firestore.collection(MARKER_COLLECTION)
            .add(markerData)
            .addOnSuccessListener {
                Toast.makeText(context, "Suksess Menambahkan Marker.kt", Toast.LENGTH_SHORT).show()
                updateStatusmarkerIsAdded(true)
            }.addOnFailureListener{
                Toast.makeText(context, "Gagal Menambahkan Marker.kt", Toast.LENGTH_SHORT).show()
            }
    }

    fun deleteMarkerFromDatabase(markerId: String, context: Context) {
        firestore.collection(MARKER_COLLECTION)
            .document(markerId)  // Menggunakan ID marker yang ingin dihapus
            .delete()  // Menghapus dokumen
            .addOnSuccessListener {
                deleteHistoryByMarkerId(markerId)
                deleteImageByMarkerId(markerId)
                deleteReviewByMarkerId(markerId)
                Toast.makeText(context, "Sukses Menghapus Marker", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Gagal Menghapus Marker", Toast.LENGTH_SHORT).show()
            }
    }
    fun deleteHistoryByMarkerId(markerid: String){
        firestore.collection(NAVIGASI_COLLECTION)
            .whereEqualTo("markerId", markerid) // Mencari dokumen berdasarkan nama
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    // Menghapus dokumen berdasarkan ID-nya
                    firestore.collection(IMAGE_COLLECTION).document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d("Firestore", "Dokumen dengan nama John Doe berhasil dihapus.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error saat menghapus dokumen: ", e)
                        }
                }

                if (querySnapshot.isEmpty) {
                    Log.d("Firestore", "Tidak ada dokumen dengan nama John Doe.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error saat mencari dokumen: ", e)
            }
    }

    fun deleteReviewByMarkerId(markerid: String){
        firestore.collection(REVIEW_COLLECTION)
            .whereEqualTo("markerId", markerid) // Mencari dokumen berdasarkan nama
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    // Menghapus dokumen berdasarkan ID-nya
                    firestore.collection(IMAGE_COLLECTION).document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d("Firestore", "Dokumen dengan nama John Doe berhasil dihapus.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error saat menghapus dokumen: ", e)
                        }
                }

                if (querySnapshot.isEmpty) {
                    Log.d("Firestore", "Tidak ada dokumen dengan nama John Doe.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error saat mencari dokumen: ", e)
            }
    }

    fun  deleteImageByMarkerId(markerid:String){
        firestore.collection(IMAGE_COLLECTION)
            .whereEqualTo("markerId", markerid) // Mencari dokumen berdasarkan nama
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    // Menghapus dokumen berdasarkan ID-nya
                    firestore.collection(IMAGE_COLLECTION).document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d("Firestore", "Dokumen dengan nama John Doe berhasil dihapus.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error saat menghapus dokumen: ", e)
                        }
                }

                if (querySnapshot.isEmpty) {
                    Log.d("Firestore", "Tidak ada dokumen dengan nama John Doe.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error saat mencari dokumen: ", e)
            }
    }


    fun updateMarkerToDatabase(markerId: String, markers: Markers, context: Context) {
        val currentTimestamp = getCurrentTimestamp()

        val marker = markers.copy(
            locationName = markers.locationName,
            type = markers.type,
            description = markers.description,
            createdAt = currentTimestamp,
            latitude = markers.latitude,
            longitude = markers.longitude,
            updatedAt = currentTimestamp,
            addedBy = "admin",
            categoryId = "",
        )
        val markerData = marker.toMap()


        firestore.collection(MARKER_COLLECTION)
            .document(markerId)
            .update(markerData)
            .addOnSuccessListener {
                Toast.makeText(context, "Sukses Memperbarui Marker", Toast.LENGTH_SHORT).show()
                updateStatusmarkerIsAdded(true)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Gagal Memperbarui Marker", Toast.LENGTH_SHORT).show()
            }
    }



    suspend fun getMarkerId(id: String): Markers? {
        return try {

            val documentSnapshot = firestore.collection(MARKER_COLLECTION)
                .document(id)
                .get()
                .await()


            if (documentSnapshot.exists()) {
                val idMarker = documentSnapshot.id
                val locationName = documentSnapshot.getString("locationName") ?: ""
                val latitude = documentSnapshot.getDouble("latitude") ?: 0.0
                val longitude = documentSnapshot.getDouble("longitude") ?: 0.0
                val type = documentSnapshot.getString("type") ?: ""
                val categoryId = documentSnapshot.getString("categoryId") ?: "Tidak Ditentukan"
                val createdAt = documentSnapshot.getString("createdAt") ?: "Tidak Ditentukan"
                val updatedAt = documentSnapshot.getString("updatedAt") ?: "Tidak Ditentukan"
                val description = documentSnapshot.getString("description") ?: ""
                val streetName = documentSnapshot.getString("streetName") ?: ""
                // Return the Markers object
                Markers(
                    id = idMarker,
                    locationName = locationName,
                    categoryId = categoryId,
                    type = type,
                    longitude = longitude,
                    latitude = latitude,
                    addedBy = "Admin",
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    description = description,
                    streetName = streetName
                )
            } else {
                // Return null if the document doesn't exist
                null
            }
        } catch (e: Exception) {
            // Handle any exceptions (e.g., network failure)
            null
        }
    }

    fun uploadImage(file: File, id: String, context: Context) {
        val filePart = prepareFileForUpload(file)
        uploadFileToServer(filePart, id= id, context  = context)
    }

    private fun updateImage(id: String, fileUrl: String, context: Context){
        val currentTimestamp = getCurrentTimestamp()

        val photo = Photo(
            markerId = id,
            updatedAt = currentTimestamp,
            imageUrl = fileUrl,
            uploadedBy = "admin",
            createdAt = currentTimestamp
        )

        val photoData =photo.toMap()

        firestore.collection(IMAGE_COLLECTION)
            .add(photoData)
            .addOnSuccessListener {
                Toast.makeText(context, "Suksess Menambahkan Foto", Toast.LENGTH_SHORT).show()
                updateStatusmarkerIsAdded(true)
            }.addOnFailureListener{
                Toast.makeText(context, "Gagal Menambahkan Foto", Toast.LENGTH_SHORT).show()
            }
    }

    fun  Photo.toMap():Map<String, Any>{
        return mapOf(
            "markerId" to this.markerId,
            "imageUrl" to this.imageUrl,
            "uploadedBy" to this.uploadedBy,
            "createdAt" to this.createdAt,
            "updatedAt" to this.updatedAt
        )
    }

    fun NavigationHistory.toMap():Map<String, Any>{

        return  mapOf(
            "navId" to this.navId!!,
            "userId" to this.userId,
            "destinationId" to this.destinationId,
            "startTime" to this.startTime,
            "startStreetName" to this.startStreetName,
            "totalWaktu" to this.totalWaktu,
            "totalJarak" to this.totalJarak,
            "createdAt" to this.createdAt,
            "endTime" to this.endTime!!

        )
    }

//    data class Reviews(
//        val markerId: String,
//        val userId: String,
//        val rating: Number,
//        val review: String,
//        val createdAt: String? = null
//    )

    fun Reviews.toMap(): Map<String, Any>{
        return  mapOf(
            "markerId" to this.markerId,
            "userId" to this.userId,
            "rating" to this.rating,
            "review" to this.review,
            "createdAt" to  this.createdAt!!
        )
    }

    fun Markers.toMap():Map<String, Any>{
        return mapOf(
            "locationName" to  this.locationName,
            "type" to this.type,
            "description" to this.description,
            "longitude" to this.longitude,
            "latitude" to this.latitude,
            "addedBy" to this.addedBy,
            "createdAt" to this.createdAt,
            "updatedAt" to this.updatedAt,
            "categoryId" to  this.categoryId,
            "streetName" to this.streetName
        )
    }


   private fun prepareFileForUpload(file: File): MultipartBody.Part {
        val mimeType = when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            "gif" -> "image/gif"
            else -> "application/octet-stream"
        }
        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    // Function to upload the file to the server
    private fun uploadFileToServer(filePart: MultipartBody.Part, id: String, context: Context) {

        RetrofitClient.uploadService.uploadFile(filePart).enqueue(object :
            Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    // Get the file URL from the response
                    val fileUrl = response.body()?.fileUrl
                    if (fileUrl != null) {
                        updateImage(id, fileUrl, context)
                    }
                } else {
                    Log.e("Upload", "Upload failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.e("Upload", "Upload failed: ${t.message}")
            }
        })
    }


}