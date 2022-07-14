package com.example.retrofit2.retrofit;

import com.example.retrofit2.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Eldor Turgunov on 14.07.2022.
 * Retrofit 2
 * eldorturgunov777@gmail.com
 */
public interface ServiceApi {
    //get
    @GET("posts")
    Call<List<Note>> getNotes();

    //get one note
    @GET("posts/{id}")
    Call<Note> getNote(@Path("id") int id);

    //insert
    @POST("posts")
    Call<Note> createPost(@Body Note note);

    //update
    @PUT("posts/{id}")
    Call<Note> updatePost(@Path("id") int id, @Body Note note);

    //delete
    @DELETE("posts/{id}")
    Call<Note> deletePost(@Path("id") int id);
}
