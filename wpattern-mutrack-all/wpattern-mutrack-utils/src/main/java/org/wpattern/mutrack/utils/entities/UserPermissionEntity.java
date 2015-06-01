package org.wpattern.mutrack.utils.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.wpattern.mutrack.utils.BaseEntity;
import org.wpattern.mutrack.utils.entities.keys.UserPermissionKey;

@Entity
@Table(name = "tb_user_permission")
public class UserPermissionEntity extends BaseEntity<UserPermissionKey> {

	private static final long serialVersionUID = 201505091629L;

	@Id
	private UserPermissionKey id;

	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name = "permission_id", insertable = false, updatable = false)
	private PermissionEntity permission;

	public UserPermissionEntity() {
	}

	public UserPermissionEntity(UserEntity user, PermissionEntity permission) {
		this.user = user;
		this.permission = permission;
	}

	@Override
	public UserPermissionKey getId() {
		return this.id;
	}

	@Override
	public void setId(UserPermissionKey id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return this.user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public PermissionEntity getPermission() {
		return this.permission;
	}

	public void setPermission(PermissionEntity permission) {
		this.permission = permission;
	}

}
