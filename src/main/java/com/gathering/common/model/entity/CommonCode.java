package com.gathering.common.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Comment;

@Entity
public class CommonCode {

    @Id
    @Column(name = "CODE")
    @Comment("코드")
    private String code;
    @NotNull
    @Comment("코드명")
    private String codeNm;
    @NotNull
    @Comment("코드 그룹")
    private String codeGrp;
    @Comment("설명")
    private String description;

}
