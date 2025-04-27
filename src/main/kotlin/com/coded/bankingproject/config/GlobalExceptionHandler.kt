package com.coded.bankingproject.config

import com.coded.bankingproject.errors.APIBaseException
import com.coded.bankingproject.errors.ErrorCode
import com.coded.bankingproject.errors.ErrorResponse
import com.coded.bankingproject.errors.ValidationError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.Instant

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(APIBaseException::class)
    fun handleAPIBaseException(
        ex: APIBaseException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            timestamp = Instant.now().toString(),
            status = ex.httpStatus.value(),
            error = ex.httpStatus.reasonPhrase,
            message = ex.message,
            code = ex.code.name,
            path = request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity.status(ex.httpStatus).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val errorResponse = ErrorResponse(
            timestamp = Instant.now().toString(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message ?: "Unexpected error occurred",
            code = ErrorCode.INTERNAL_SERVER_ERROR.name,
            path = request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity.status(status).body(errorResponse)
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(
        ex: UsernameNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.UNAUTHORIZED
        val errorResponse = ErrorResponse(
            timestamp = Instant.now().toString(),
            status = status.value(),
            error = status.reasonPhrase,
            message = "Invalid Credentials",
            code = ErrorCode.INVALID_CREDENTIALS.name,
            path = request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity.status(status).body(errorResponse)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(
        ex: BadCredentialsException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.UNAUTHORIZED
        val errorResponse = ErrorResponse(
            timestamp = Instant.now().toString(),
            status = status.value(),
            error = status.reasonPhrase,
            message = "Invalid Credentials",
            code = ErrorCode.INVALID_CREDENTIALS.name,
            path = request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity.status(status).body(errorResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val fieldErrors = ex.bindingResult.fieldErrors.map {
            ValidationError(it.field, it.defaultMessage ?: "Invalid value")
        }

        val status = HttpStatus.BAD_REQUEST

        val errorResponse = ErrorResponse(
            timestamp = Instant.now().toString(),
            status = status.value(),
            error = status.reasonPhrase,
            message = "Validation failed",
            code = ErrorCode.INVALID_INPUT.name,
            path = request.getDescription(false).removePrefix("uri="),
            fieldErrors = fieldErrors
        )

        return ResponseEntity.status(status).body(errorResponse)
    }

}
