package org.moroz.userprofile.service

import org.moroz.userprofile.model.UserProfile
import org.moroz.userprofile.repository.UserProfileRepository
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus

@Service
class UserProfileService(private val repository: UserProfileRepository) {

    fun getProfile(): UserProfile {
        return repository.findById(1).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found")
        }
    }

    fun updateProfile(profile: UserProfile): UserProfile {
        if (profile.id == 0L) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is required")
        return repository.save(profile)
    }
}