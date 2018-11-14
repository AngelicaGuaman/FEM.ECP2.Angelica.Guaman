package es.upm.miw.firebase;

import java.util.List;

import es.upm.miw.firebase.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@SuppressWarnings("Unused")
interface UserRESTAPIService {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("/users/{id}")
    Call<User> getUserById(@Path("id") String id);

    @GET("/users/{email}")
    Call<User> getUserByEmail(@Path("email") String email);

    @GET("/users")
    Call<List<User>> getUsers();
}
