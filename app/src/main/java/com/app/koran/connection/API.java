package com.app.koran.connection;


import com.app.koran.connection.callbacks.CallbackCategories;
import com.app.koran.connection.callbacks.CallbackCategoryDetails;
import com.app.koran.connection.callbacks.CallbackComment;
import com.app.koran.connection.callbacks.CallbackDetailsPage;
import com.app.koran.connection.callbacks.CallbackDetailsPost;
import com.app.koran.connection.callbacks.CallbackDevice;
import com.app.koran.connection.callbacks.CallbackInfo;
import com.app.koran.connection.callbacks.CallbackListPage;
import com.app.koran.connection.callbacks.CallbackListPost;
import com.app.koran.model.DeviceInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    /* your wordPress url */
    String BASE_URL = "https://bpbd.wajokab.go.id";


    // minimize field for list of post
    String EXCLUDE_FIELD = "&exclude=content,categories,tags,comments,custom_fields";
    String USER_AGENT = "Koran";

    /* info API transaction ------------------------------- */

    @GET("?json=info")
    Call<CallbackInfo> getInfo();


    /* Post API transaction ------------------------------- */

    @Headers({"Cache-Control: max-age=0", "User-Agent: "+USER_AGENT})
    @GET("?json=get_posts" + EXCLUDE_FIELD)
    Call<CallbackListPost> getPostByPage(
            @Query("page") int page,
            @Query("count") int count
    );

    @Headers({"Cache-Control: max-age=0", "User-Agent: "+USER_AGENT})
    @GET("?json=get_post")
    Call<CallbackDetailsPost> getPostDetailsById(
            @Query("id") long id
    );

    @Headers({"Cache-Control: max-age=0", "User-Agent: "+USER_AGENT})
    @GET("?json=get_search_results" + EXCLUDE_FIELD)
    Call<CallbackListPost> getSearchPosts(
            @Query("search") String search,
            @Query("count") int count
    );


    /* Category API transaction --------------------------- */

    @Headers({"Cache-Control: max-age=0", "User-Agent: "+USER_AGENT})
    @GET("?json=get_category_index")
    Call<CallbackCategories> getAllCategories();

    @Headers({"Cache-Control: max-age=0", "User-Agent: "+USER_AGENT})
    @GET("?json=get_category_posts" + EXCLUDE_FIELD)
    Call<CallbackCategoryDetails> getCategoryDetailsByPage(
            @Query("id") long id,
            @Query("page") long page,
            @Query("count") long count
    );

    @Headers({"Cache-Control: max-age=0", "User-Agent: "+USER_AGENT})
    @GET("?json=respond/submit_comment")
    Call<CallbackComment> sendComment(
            @Query("post_id") long post_id,
            @Query("name") String name,
            @Query("email") String email,
            @Query("content") String content
    );

    /* Page API transaction --------------------------- */

    @Headers({"Cache-Control: max-age=0", "User-Agent: "+USER_AGENT})
    @GET("?json=get_page_index" + EXCLUDE_FIELD)
    Call<CallbackListPage> getPagesByPage();

    @Headers({"Cache-Control: max-age=0", "User-Agent: "+USER_AGENT})
    @GET("?json=get_page")
    Call<CallbackDetailsPage> getPageDetailsById(
            @Query("id") long id
    );

    /* FCM notification API transaction --------------------------- */

    @Headers({"Cache-Control: max-age=0", "User-Agent: "+USER_AGENT})
    @POST("?api-fcm=register")
    Call<CallbackDevice> registerDevice(@Body DeviceInfo deviceInfo);


}
