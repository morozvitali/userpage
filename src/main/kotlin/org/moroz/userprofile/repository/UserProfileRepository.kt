package org.moroz.userprofile.repository

import org.moroz.userprofile.model.UserProfile
import org.springframework.data.jpa.repository.JpaRepository

interface UserProfileRepository : JpaRepository<UserProfile, Long>