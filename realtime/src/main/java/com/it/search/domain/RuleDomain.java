package com.it.search.domain;

/**
 * @description:
 * @author: Delusion
 * @date: 2021-05-31 16:08
 */
public class RuleDomain {
    private Integer id;
    private String words;

    public RuleDomain() {
    }

    public RuleDomain(Integer id, String words) {
        this.id = id;
        this.words = words;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
