package com.northinrtm.clientsapi.controller;


import com.northinrtm.clientsapi.dto.ErrorResponse;
import com.northinrtm.clientsapi.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.LinkedHashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    GlobalExceptionHandler handler;
    HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void handleNotFound_returns404_errorResponseFilled() {
        var resp = handler.handleNotFound(new NotFoundException("Client not found"), request);

        assertEquals(404, resp.getStatusCode().value());
        ErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(404, body.status());
        assertEquals("Not Found", body.error());
        assertEquals("Client not found", body.message());
        assertEquals("/api/test", body.path());
        assertNotNull(body.timestamp());
    }

    @Test
    void handleBodyValidation_returns400_andFieldErrorsMap() {
        Object target = new Object();
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(target, "req");
        br.addError(new FieldError("req", "name", "must not be blank"));
        br.addError(new FieldError("req", "lastName", "must not be blank"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, br);

        var resp = handler.handleBodyValidation(ex, request);

        assertEquals(400, resp.getStatusCode().value());
        ErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Bad Request", body.error());
        assertEquals("Validation failed", body.message());
        assertEquals("/api/test", body.path());
        assertNotNull(body.fieldErrors());
        assertEquals("must not be blank", body.fieldErrors().get("name"));
        assertEquals("must not be blank", body.fieldErrors().get("lastName"));
    }

    @Test
    void handleConflict_returns409() {
        var resp = handler.handleConflict(new DataIntegrityViolationException("dup"), request);

        assertEquals(409, resp.getStatusCode().value());
        ErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(409, body.status());
        assertEquals("Conflict", body.error());
        assertEquals("email already exists / phone already exists", body.message());
    }

    @Test
    void handleConstraintViolation_returns400() {
        @SuppressWarnings("unchecked")
        ConstraintViolation<Object> v = mock(ConstraintViolation.class);
        when(v.getMessage()).thenReturn("bad");

        Set<ConstraintViolation<?>> set = new LinkedHashSet<>();
        set.add(v);

        var resp = handler.handleConstraintViolation(new ConstraintViolationException(set), request);

        assertEquals(400, resp.getStatusCode().value());
        ErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Bad Request", body.error());
        assertEquals("Validation failed", body.message());
    }

    @Test
    void handleAny_returns500() {
        var resp = handler.handleAny(new RuntimeException("boom"), request);

        assertEquals(500, resp.getStatusCode().value());
        ErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(500, body.status());
        assertEquals("Internal Server Error", body.error());
        assertEquals("Unexpected error", body.message());
    }
}