package com.example.data.mapper

import com.example.data.model.GenreDto
import com.example.domain.model.Genre
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieMappersTest {

    @Test
    fun `when GenreDto mapped to domain, should return correct Genre model`() {
        // Arrange
        val dto = GenreDto(id = 28, name = "Action")

        // Act
        val domain = dto.toDomain()

        // Assert
        assertEquals(28, domain.id)
        assertEquals("Action", domain.name)
    }
}
