package com.app.koran.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.app.koran.realm.table.PostRealm;
import io.realm.RealmList;

public class Post implements Serializable {

    public long id = -1;
    public String type = "";
    public String slug = "";
    public String url = "";
    public String status = "";
    public String title = "";
    public String title_plain = "";
    public String content = "";
    public String excerpt = "";
    public String date = "";
    public String modified = "";
    public String thumbnail = "";
    public long comment_count = -1;

    public List<Category> categories = new ArrayList<>();
    public Author author = null;
    public List<Comment> comments = new ArrayList<>();
    public List<Attachment> attachments = new ArrayList<>();
    public Thumbnails thumbnail_images = null;

    public PostRealm getObjectRealm() {
        PostRealm p = new PostRealm();
        p.id = id;
        p.type = type;
        p.slug = slug;
        p.url = url;
        p.status = status;
        p.title = title;
        p.title_plain = title_plain;
        p.content = content;
        p.excerpt = excerpt;
        p.date = date;
        p.modified = modified;
        p.thumbnail = thumbnail;
        p.comment_count = comment_count;

        p.categories = new RealmList<>();
        for (Category c : categories) { p.categories.add(c.getObjectRealm()); }

        p.comments = new RealmList<>();
        for (Comment c : comments) { p.comments.add(c.getObjectRealm()); }

        p.author = ( author != null ? author.getObjectRealm() : null );
        p.thumbnail_images = ( thumbnail_images != null ? thumbnail_images.getObjectRealm() : null );

        p.attachments = new RealmList<>();
        for (Attachment a : attachments) { p.attachments.add(a.getObjectRealm()); }

        return p;
    }

    public boolean isDraft(){
        return !(content != null && !content.trim().equals(""));
    }

}
