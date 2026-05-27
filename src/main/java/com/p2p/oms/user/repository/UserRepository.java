package com.p2p.oms.user.repository;

import com.p2p.oms.user.entity.User;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@NullMarked
public interface UserRepository extends JpaRepository<User, UUID> {
}
