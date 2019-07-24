package de.hswt.fi.web.service.def.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.Map;

@ControllerAdvice
@SuppressWarnings("unused")
public class ExcptionControllerAdvice {

	private ErrorAttributes errorAttributes;

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcptionControllerAdvice.class);

	@Autowired
	public ExcptionControllerAdvice(ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}

	@ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
	private ResponseEntity<Object> handelUnsatisfiedParameterException(HttpServletRequest request) {
		logOriginlError(request);
		return ResponseEntity.badRequest().body(null);
	}
	
	@ExceptionHandler(value = UnsupportedOperationException.class)
	private ResponseEntity<Map<String,Object>> handleUnsupportedOperationException(HttpServletRequest request) {
		logOriginlError(request);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}
	
	@ExceptionHandler(value = InvalidParameterException.class)
	private ResponseEntity<Object> handleInvalidParameterException(HttpServletRequest request) {
		logOriginlError(request);
		return ResponseEntity.badRequest().body(null);
	}
 	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
        	throw e;
        }
        
        logOriginlError(request);
		return ResponseEntity.badRequest().body(null);
    }
	
	private void logOriginlError(HttpServletRequest request) {
		ServletWebRequest servletRequest = new ServletWebRequest(request);
		ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
		final boolean WITHOUT_STACK_TRACE = false;
		Map<String, Object> attributes = errorAttributes.getErrorAttributes(servletRequest, WITHOUT_STACK_TRACE);
		LOGGER.debug("an error occured {}", attributes);

	}
	

}
