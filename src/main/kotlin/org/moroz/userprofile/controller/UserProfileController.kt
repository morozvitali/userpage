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

    @GetMapping
    fun getProfile(): ResponseEntity<UserProfile> {
        val profile = repository.findById(1).orElse(null)
        return if (profile != null) ResponseEntity.ok(profile) else ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @PutMapping
    fun updateProfile(@Validated @RequestBody profile: UserProfile): ResponseEntity<UserProfile> {
        val savedProfile = repository.save(profile)
        return ResponseEntity.ok(savedProfile)
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