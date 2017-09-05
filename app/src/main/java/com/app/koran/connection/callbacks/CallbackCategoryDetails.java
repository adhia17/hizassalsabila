package com.app.koran.connection.callbacks;

import java.util.ArrayList;
import java.util.List;

import com.app.koran.model.Category;
import com.app.koran.model.Post;

public class CallbackCategoryDetails {

    public String status = "";
    public int count = -1;
    public int pages = -1;
    public Category category = null;
    public List<Post> posts = new ArrayList<>();
}
