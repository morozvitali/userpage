package org.moroz.userprofile.controller

import org.moroz.userprofile.model.UserProfile
import org.moroz.userprofile.repository.UserProfileRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@RestController
@RequestMapping("/profile")
class UserProfileController(private val repository: UserProfileRepository) {

    @GetMapping("/{id}")
    fun getProfile(@PathVariable id: Long): ResponseEntity<UserProfile> {
        val profile = repository.findById(id).orElse(null)
        return if (profile != null) ResponseEntity.ok(profile) else ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @PutMapping("/{id}/update")
    fun updateProfile(
        @PathVariable id: Long,
        @Validated @RequestBody profile: UserProfile
    ): ResponseEntity<UserProfile> {
        val existingProfile = repository.findById(id).orElse(null)

        return if (existingProfile != null) {
            val updatedProfile = existingProfile.copy(
                name = profile.name.takeIf { it.isNotBlank() } ?: existingProfile.name,
                surname = profile.surname.takeIf { it.isNotBlank() } ?: existingProfile.surname,
                phone = profile.phone.takeIf { it.isNotBlank() } ?: existingProfile.phone,
                jobTitle = profile.jobTitle ?: existingProfile.jobTitle,
                address = profile.address ?: existingProfile.address,
                interests = if (profile.interests.isNotEmpty()) profile.interests else existingProfile.interests,
                profileLink = profile.profileLink ?: existingProfile.profileLink,
                isPublic = profile.isPublic ?: existingProfile.isPublic,
                avatarUrl = profile.avatarUrl ?: existingProfile.avatarUrl
            )

            ResponseEntity.ok(repository.save(updatedProfile))
        } else {
            val newProfile = UserProfile(
                id = id,
                name = profile.name,
                surname = profile.surname,
                phone = profile.phone,
                jobTitle = profile.jobTitle,
                address = profile.address,
                interests = profile.interests,
                profileLink = profile.profileLink,
                isPublic = profile.isPublic,
                avatarUrl = profile.avatarUrl
            )
            ResponseEntity.status(HttpStatus.CREATED).body(repository.save(newProfile))
        }
    }

    @PostMapping("/upload-avatar")
    fun uploadAvatar(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        if (file.isEmpty || !listOf("image/jpeg", "image/png").contains(file.contentType)) {
            return ResponseEntity.badRequest().body("Invalid file type")
        }

        val uploadsDir = Path.of("uploads")
        if (!Files.exists(uploadsDir)) Files.createDirectories(uploadsDir)

        val targetPath = uploadsDir.resolve(file.originalFilename!!)
        file.inputStream.use { Files.copy(it, targetPath, StandardCopyOption.REPLACE_EXISTING) }

        val avatarUrl = "/uploads/${file.originalFilename}"
        return ResponseEntity.ok(avatarUrl)
    }
}