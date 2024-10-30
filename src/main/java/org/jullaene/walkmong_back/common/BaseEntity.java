package org.jullaene.walkmong_back.common;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@DynamicUpdate
public abstract class BaseEntity {
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "del_yn", columnDefinition = "VARCHAR(1) default 'N'")
    protected String delYn;

    @PrePersist
    public void prePersist() {
        this.delYn = this.delYn == null ? "N" : this.delYn;
    }

    public void delete() {
        this.delYn = "Y";
    }
}
