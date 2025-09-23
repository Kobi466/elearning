package com.kobi.courseservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
    GET_COURSE_SUCCESS(200, "Get course success", HttpStatus.OK),
	UPLOAD_SUCCESS(200, "Upload success", HttpStatus.OK),
    CREATED_COURSE(201, "Created course", HttpStatus.CREATED),
    UPDATED_COURSE(202, "Updated course", HttpStatus.OK),
    DELETED_COURSE(203, "Deleted course", HttpStatus.OK),
    CREATED_SECTION(204, "Created section", HttpStatus.CREATED),
    UPDATED_SECTION(205, "Updated section", HttpStatus.OK),
    DELETED_SECTION(206, "Deleted section", HttpStatus.OK),
    CREATED_LESSON(207, "Created lesson", HttpStatus.CREATED),
    UPDATED_LESSON(208, "Updated lesson", HttpStatus.OK),
    DELETED_LESSON(209, "Deleted lesson", HttpStatus.OK),
	;

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;
}
