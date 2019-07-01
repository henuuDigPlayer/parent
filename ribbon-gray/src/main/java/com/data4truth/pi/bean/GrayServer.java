package com.data4truth.pi.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author lindj
 * @date 2019/6/26 0026
 * @description
 */
@Data
@NoArgsConstructor
@ToString
public class GrayServer implements Serializable {
    private String currentVersion;
    private String grayVersion;
    private Boolean requestCanGray;
}
