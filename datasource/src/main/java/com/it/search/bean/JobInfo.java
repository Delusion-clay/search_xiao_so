package com.it.search.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: Delusion
 * @date: 2021-05-24 16:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInfo {
    private String id;
    private String type;
    private String title;
    private String url;
    private String infomessage;
}
