package com.app.koran.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page implements Serializable {

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

    public Author author = null;
    public List<Attachment> attachments = new ArrayList<>();
    public Thumbnails thumbnail_images = null;

    public boolean isDraft() {
        return !(content != null && !content.trim().equals(""));
    }

}
