/*package com.cadence.upload.core;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cadence.upload.exception.CustomError;

//http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-error-handling
@ControllerAdvice
public class RestGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //@ExceptionHandler(MultipartException.class)
	@ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {

        HttpStatus status = getStatus(request);
        CustomError ce = new CustomError(ex.getClass().getSimpleName()
        		, ex.getMessage());
        return new ResponseEntity<CustomError>(ce, status);

        //return new ResponseEntity("Attachment size exceeds the allowable limit! (10MB)", status);

        // example
        //return new ResponseEntity(ex.getMessage(), status);
        //return new ResponseEntity("success", responseHeaders, HttpStatus.OK);

    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}
*/