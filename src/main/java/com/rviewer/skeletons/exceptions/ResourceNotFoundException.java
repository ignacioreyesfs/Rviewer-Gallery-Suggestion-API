package com.rviewer.skeletons.exceptions;

public class ResourceNotFoundException extends AppException{
	public ResourceNotFoundException(APIError error) {
		super(error.getHttpStatus(), error.getMessage(), null);
	}
}
