package org.moroz.userprofile.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Service
class FileStorageService {

    private val uploadDir = Path.of("uploads")

    init {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir)
        }
    }

    fun saveFile(file: MultipartFile): String {
        val fileName = file.originalFilename ?: throw IllegalArgumentException("Invalid file name")
        val targetPath = uploadDir.resolve(fileName)
        file.inputStream.use { Files.copy(it, targetPath, StandardCopyOption.REPLACE_EXISTING) }
        return "/uploads/$fileName"
    }
}