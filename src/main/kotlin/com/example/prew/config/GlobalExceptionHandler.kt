package com.example.prew.config

import com.example.prew.errors.ErrorResponse
import com.example.prew.errors.Error
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleJsonParseError(
        exception: HttpMessageNotReadableException,
        request: WebRequest): ResponseEntity<ErrorResponse> {

        val message = exception.message

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(
                message = message,
                path = request.getDescription(false).removePrefix("uri=")
            ))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationError(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.joinToString(", ") {
            "${it.field}: ${it.defaultMessage}"
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(
                message = "Validation failed: $errors",
                path = request.getDescription(false).removePrefix("uri=")
            ))
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(ErrorResponse(
                message = "Method ${ex.method} not supported for this endpoint",
                path = request.getDescription(false).removePrefix("uri=")
            ))
    }

    @ExceptionHandler(Error::class)
    fun handleCustomError(ex: Error, request: WebRequest): ResponseEntity<ErrorResponse> {
        return ex.toHttpResponse(request.getDescription(false).removePrefix("uri="))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericError(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(
                message = "An unexpected error occurred",
                path = request.getDescription(false).removePrefix("uri=")
            ))
    }
}