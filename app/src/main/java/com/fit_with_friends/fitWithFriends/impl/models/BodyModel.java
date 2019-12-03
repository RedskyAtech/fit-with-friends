package com.fit_with_friends.fitWithFriends.impl.models;

import java.util.ArrayList;
import java.util.List;

public class BodyModel<TModel> {

    public Long total = 0L;
    public List<TModel> data;
    private Long current_page;
    private String first_page_url;
    private Long from;
    private Long last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private Long per_page;
    private String prev_page_url;
    private Long to;

    public BodyModel() {
        data = new ArrayList<>();
    }

}
