package com.seniorzhai.gank.model;

import java.util.List;

/**
 * Created by zhai on 16/6/24.
 */

public class BenefitModel extends BaseModel {
    public List<Benefit> results;

    public class Benefit {
        public String desc;
        public String publishedAt;
        public String type;
        public String url;
        public String who;
    }

}
