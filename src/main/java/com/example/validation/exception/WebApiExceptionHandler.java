package com.example.validation.exception;

import com.example.validation.rule.RuleFailedException;
import lombok.AllArgsConstructor;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@ControllerAdvice
@AllArgsConstructor
public class WebApiExceptionHandler extends ResponseEntityExceptionHandler {
    private WebApiExceptionConverter converter;



    private ResponseEntity<ApiError> wrap(WebRequest request, ResponseEntity resp, Exception ex, BindingResult bindingResult){
        try{
            return converter.convert(request, resp, ex, bindingResult);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    @ExceptionHandler(RuleFailedException.class)
    public ResponseEntity<ApiError> handleRuleFailedException(RuleFailedException ex, WebRequest request) {
        return wrap(request, new ResponseEntity("Something went wrong.", new HttpHeaders(), HttpStatus.BAD_REQUEST), ex, ex.getBindingResult());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, WebRequest request){
        ApiError err = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "An error has occurred.");
        return new ResponseEntity(err, new HttpHeaders(), err.getStatus());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        return wrap(request, new ResponseEntity("The item you requested could not be found.", new HttpHeaders(), HttpStatus.NOT_FOUND), ex, null);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleRuleFailedException(ConstraintViolationException ex, WebRequest request) {
        return wrap(request, new ResponseEntity("Something went wrong, constraint.", new HttpHeaders(), HttpStatus.BAD_REQUEST), ex, null);
    }

    @Override
    protected ResponseEntity handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleAsyncRequestTimeoutException(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }

    @Override
    protected ResponseEntity handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleBindException(ex, headers, status, request);
        return wrap(request, resp, ex, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleConversionNotSupported(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }

    @Override
    protected ResponseEntity handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleExceptionInternal(ex, body, headers, status, request);
        return wrap(request, resp, ex, null);
    }

    @Override
    protected ResponseEntity handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }

    @Override
    protected ResponseEntity handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }

    @Override
    protected ResponseEntity handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleHttpMessageNotReadable(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }

    @Override
    protected ResponseEntity handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleHttpMessageNotWritable(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }

    @Override
    protected ResponseEntity handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }


    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleMethodArgumentNotValid(ex, headers, status, request);
        return wrap(request, resp, ex, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleMissingPathVariable(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }

    @Override
    protected ResponseEntity handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleMissingServletRequestParameter(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }

    @Override
    protected ResponseEntity handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleMissingServletRequestPart(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }

    @Override
    protected ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleNoHandlerFoundException(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }

    @Override
    protected ResponseEntity handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleServletRequestBindingException(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }

    @Override
    protected ResponseEntity handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity resp = super.handleTypeMismatch(ex, headers, status, request);
        return wrap(request, resp, ex, null);
    }

}
