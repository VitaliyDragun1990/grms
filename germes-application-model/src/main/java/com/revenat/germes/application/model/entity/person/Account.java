package com.revenat.germes.application.model.entity.person;

import com.revenat.germes.application.model.entity.base.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity that encapsulates user of the application
 *
 * @author Vitaliy Dragun
 */
@Table(name = "ACCOUNT")
@Entity
public class Account extends AbstractEntity {
}
