package com.home.iot.exceptions.mapper;

import java.util.Date;

/**
 * This defines the generic exception response structure for handling
 * all sorts of exceptions across all resources
 */
public class ExceptionResponse {
	  private Date timestamp;
	  private String message;
	  private String details;

	  public ExceptionResponse(Date timestamp, String message, String details) {
	    super();
	    this.timestamp = timestamp;
	    this.message = message;
	    this.details = details;
	  }

	  public Date getTimestamp() {
	    return timestamp;
	  }

	  public String getMessage() {
	    return message;
	  }

	  public String getDetails() {
	    return details;
	  }

	}
