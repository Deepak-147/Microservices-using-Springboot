package com.eazybytes.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Author: ldeepak
 *
 * BaseEntity class serves as a mapped superclass for other entity classes in the application.
 * It contains common fields that are shared across multiple entities, such as createdAt, createdBy.
 * Mainly used for auditing purposes to track when an entity was created or last modified, and by whom.
 */

/**
 * The @MappedSuperclass annotation is used to indicate that the class is a superclass whose properties should be mapped to the database table of its subclasses.
 * It allows the fields defined in the superclass to be inherited by entity classes that extend it, without the superclass itself being an entity.
 * @Getter, @Setter, and @ToString are Lombok annotations that automatically generate getter and setter methods, and a toString method for the class, reducing boilerplate code.
 * @EntityListeners annotation in JPA enables automatic auditing (like setting created/modified dates and users) by listening to entity lifecycle events and populating the annotated fields accordingly. This helps automate and centralize auditing logic for your entities.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @ToString
public class BaseEntity {

	/**
	 *  updatable=false, attribute means the column value cannot be changed in an update operation (it is only set when the row is first inserted).
	 *  It makes sense for fields like createdAt and createdBy which should not change after the initial insert.
	 *  insertable=false, attribute means the column value cannot be set during an insert operation (it is only modified during updates).
	 *  It makes sense for fields like updatedAt and updatedBy which should not have values when the row is first created.
	 *
	 *  We can specify name attribute in @Column annotation if the database column name is different from the entity field name.
	 *  If you do not specify name, the field name (createdAt) is used as the column name (with default naming strategy, it may be converted to created_at in the DB depending on your JPA settings).
	 *
	 *  @CreatedDate: Automatically sets the field with the timestamp when the entity is first persisted (created).
	 *  @CreatedBy: Automatically sets the field with the user who created the entity.
	 *  @LastModifiedDate: Automatically updates the field with the timestamp whenever the entity is updated.
	 *  @LastModifiedBy: Automatically updates the field with the user who last modified the entity.
	 */
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@CreatedBy
	@Column(updatable = false)
	private String createdBy;

	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime updatedAt;

	@LastModifiedBy
	@Column(insertable = false)
	private String updatedBy;
}
