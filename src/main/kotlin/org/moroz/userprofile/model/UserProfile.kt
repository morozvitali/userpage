package org.moroz.userprofile.model

import jakarta.persistence.*
import jakarta.validation.constraints.*

@Entity
data class UserProfile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @field:NotBlank
    @field:Size(min = 2, max = 50)
    val name: String,

    @field:NotBlank
    @field:Size(min = 2, max = 50)
    val surname: String,

    @field:Size(max = 100)
    val jobTitle: String? = null,

    @field:NotBlank
    @field:Pattern(regexp = "\\+\\d{10,15}")
    val phone: String,

    @field:Size(max = 200)
    val address: String? = null,

    @ElementCollection
    @CollectionTable(name = "interests", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "interest")
    val interests: List<String> = listOf(),

    @field:Size(max = 200)
    @field:Pattern(regexp = "https?://.*")
    val profileLink: String? = null,

    val isPublic: Boolean = false,

    val avatarUrl: String? = null
)