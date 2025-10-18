package com.KayraAtalay.base_code.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.KayraAtalay.dto.DtoAuthor;
import com.KayraAtalay.dto.DtoAuthorIU;
import com.KayraAtalay.exception.BaseException;
import com.KayraAtalay.exception.MessageType;
import com.KayraAtalay.model.Author;
import com.KayraAtalay.repository.AuthorRepository;
import com.KayraAtalay.service.impl.AuthorServiceImpl;

// Enables Mockito integration with JUnit 5
@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

    // @Mock: Tells Mockito to create a mock (fake) instance of AuthorRepository.
    // This mock will never interact with the actual database.
    @Mock
    private AuthorRepository authorRepository;

    // @InjectMocks: Tells Mockito to create a real instance of AuthorServiceImpl
    // and inject the mocks created above (authorRepository) into it.
    @InjectMocks
    private AuthorServiceImpl authorService; // This is the actual class we are testing.

    // @Test: Tells JUnit 5 that this method is a test case.
    @Test
    void whenSaveAuthor_withValidDto_shouldReturnSavedAuthorDto() {
        // GIVEN: Set up the test conditions and mock behavior.
        // 1. Prepare Input Data: The DTO that will be passed to the service method.
        DtoAuthorIU requestDto = new DtoAuthorIU();
        requestDto.setName("George Orwell");
        requestDto.setBirthYear(1903);
        requestDto.setCountry("United Kingdom");

        // 2. Prepare Mock Output Data: The Author entity we expect the mock repository's save method to return.
        Author savedAuthorEntity = new Author();
        savedAuthorEntity.setId(1L); // Simulate the database assigning an ID.
        savedAuthorEntity.setName("George Orwell");
        savedAuthorEntity.setBirthYear(1903);
        savedAuthorEntity.setCountry("United Kingdom");

        // 3. Define Mock Behavior (Stubbing):
        // Tell Mockito: "When the authorRepository's save method is called with ANY object of type Author,
        // then return the 'savedAuthorEntity' object we prepared above."
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthorEntity);

        // WHEN: Execute the method under test.
        // Call the actual method we want to test.
        DtoAuthor actualResultDto = authorService.saveAuthor(requestDto);

        // THEN: Verify the results.
        // Use JUnit 5 Assertions to check if the result matches our expectations.
        assertNotNull(actualResultDto, "The returned DTO should not be null.");
        assertEquals(1L, actualResultDto.getId(), "The ID should match.");
        assertEquals("George Orwell", actualResultDto.getName(), "The name should match.");
        assertEquals(1903, actualResultDto.getBirthYear(), "The birth year should match.");
        assertEquals("United Kingdom", actualResultDto.getCountry(), "The country should match.");
    }



    @Test
    void whenFindAuthorById_withValidId_shouldReturnAuthorDto() {
        // GIVEN
        Long validId = 5L;
        Author foundAuthorEntity = new Author();
        foundAuthorEntity.setId(validId);
        foundAuthorEntity.setName("Agatha Christie");
        foundAuthorEntity.setBirthYear(1890);
        foundAuthorEntity.setCountry("United Kingdom");

        when(authorRepository.findById(validId)).thenReturn(Optional.of(foundAuthorEntity));

        // WHEN
        DtoAuthor actualResultDto = authorService.findAuthorById(validId);

        // THEN
        assertNotNull(actualResultDto);
        assertEquals(validId, actualResultDto.getId());
        assertEquals("Agatha Christie", actualResultDto.getName());
        assertEquals(1890, actualResultDto.getBirthYear());
        assertEquals("United Kingdom", actualResultDto.getCountry());
    }




    @Test
    void whenFindAuthorById_withInvalidId_shouldThrowNotFoundException() {
        // GIVEN
        Long invalidId = 999L;
        when(authorRepository.findById(invalidId)).thenReturn(Optional.empty());

        // WHEN & THEN
        BaseException thrownException = assertThrows(BaseException.class, () -> {
            authorService.findAuthorById(invalidId);
        });

        // Optional extra check for the exception type
        assertNotNull(thrownException.getErrorMessage());
        assertEquals(MessageType.AUTHOR_NOT_FOUND, thrownException.getErrorMessage().getMessageType());
    }




    @Test
    void whenUpdateAuthor_withValidIdAndDto_shouldReturnUpdatedAuthorDto() {
        // --- GIVEN (Setup) ---
        // 1. ID of the author to update and the DTO containing the new information.
        Long authorIdToUpdate = 1L;
        DtoAuthorIU updateRequestDto = new DtoAuthorIU();
        updateRequestDto.setName("George Orwell Updated");
        updateRequestDto.setBirthYear(1904); // Change the information
        updateRequestDto.setCountry("UK");

        // 2. The *existing* state of the author that we expect findById to return.
        Author existingAuthorEntity = new Author();
        existingAuthorEntity.setId(authorIdToUpdate);
        existingAuthorEntity.setName("George Orwell");
        existingAuthorEntity.setBirthYear(1903);
        existingAuthorEntity.setCountry("United Kingdom");

        // 3. The expected *updated* state after save is called.
        // In Mockito, the save method usually returns the same object it received as input,
        // so we'll capture the argument passed to save instead of creating a new object here.

        // 4. Mockito Behaviors:
        // "If findById is called with 'authorIdToUpdate', return an Optional containing 'existingAuthorEntity'."
        when(authorRepository.findById(authorIdToUpdate)).thenReturn(Optional.of(existingAuthorEntity));

        // "If save is called with ANY Author object, return that SAME Author object back."
        // This simulates the typical behavior of JpaRepository's save method.
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));


        // --- WHEN (Execution) ---
        DtoAuthor actualResultDto = authorService.updateAuthor(authorIdToUpdate, updateRequestDto);


        // --- THEN (Verification) ---
        // 1. Is the content of the returned DTO correct?
        assertNotNull(actualResultDto);
        assertEquals(authorIdToUpdate, actualResultDto.getId());
        assertEquals("George Orwell Updated", actualResultDto.getName()); // Updated name
        assertEquals(1904, actualResultDto.getBirthYear()); // Updated year
        assertEquals("UK", actualResultDto.getCountry()); // Updated country

        // 2. Extra Check (Verify): Were the repository methods called correctly?
        // ArgumentCaptor allows us to capture the Author object sent to the save method.
        ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);

        // "Verify that the save method of authorRepository was called exactly 1 time,
        // and capture the Author object that was passed to it."
        verify(authorRepository, times(1)).save(authorCaptor.capture());

        // Let's ensure the captured Author object contains the updated values.
        Author savedAuthor = authorCaptor.getValue();
        assertEquals("George Orwell Updated", savedAuthor.getName());
        assertEquals(1904, savedAuthor.getBirthYear());
        assertEquals("UK", savedAuthor.getCountry());

        // "Verify that the findById method of authorRepository was called exactly 1 time."
        verify(authorRepository, times(1)).findById(authorIdToUpdate);
    }


    @Test
    void whenUpdateAuthor_withInvalidId_shouldThrowNotFoundException() {
        // --- GIVEN (Setup) ---
        Long invalidId = 999L;
        DtoAuthorIU updateRequestDto = new DtoAuthorIU(); // Content doesn't matter, exception should be thrown before saving.
        updateRequestDto.setName("Doesn't Matter");

        // Mockito Behavior: "If findById is called with 'invalidId', return an empty Optional."
        when(authorRepository.findById(invalidId)).thenReturn(Optional.empty());

        // --- WHEN & THEN (Execution and Verification Combined) ---
        // We expect a 'BaseException' to be thrown.
        BaseException thrownException = assertThrows(BaseException.class, () -> {
            authorService.updateAuthor(invalidId, updateRequestDto);
        });

        // Ensure the type of the thrown exception is correct.
        assertNotNull(thrownException.getErrorMessage());
        assertEquals(MessageType.AUTHOR_NOT_FOUND, thrownException.getErrorMessage().getMessageType());
    }

}