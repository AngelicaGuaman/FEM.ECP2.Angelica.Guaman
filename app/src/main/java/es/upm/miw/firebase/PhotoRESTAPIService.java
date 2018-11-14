package es.upm.miw.firebase;

import java.util.List;

import es.upm.miw.firebase.models.Photo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@SuppressWarnings("Unused")
interface PhotoRESTAPIService {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("/photos/{id}")
    Call<Photo> getPhotoById(@Path("id") String id);

    @GET("/photos")
    Call<List<Photo>> getPhotos();

}
