package com.example.yin.model.reponse;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @date 2023/4/11
 */
@Data
public class SongIntroductionTreeResponse implements Serializable {

    private String value;

    private String label;

    private List<SongIntroductionTreeResponse> children;
}
