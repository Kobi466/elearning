package com.kobi.courseservice.controller;

import com.kobi.courseservice.dto.ApiResponse;
import com.kobi.courseservice.dto.request.CoursePriceResponse;
import com.kobi.courseservice.dto.response.CourseResponse;
import com.kobi.courseservice.exception.SuccessCode;
import com.kobi.courseservice.service.CourseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalController {
    CourseService courseService;

    @GetMapping("/{courseId}")
    ApiResponse<CourseResponse> getCourse(@PathVariable String courseId){
        return ApiResponse.ok(courseService.getCourse(courseId), SuccessCode.GET_COURSE_SUCCESS);
    }
    @GetMapping("/price/{courseId}")
    ApiResponse<CoursePriceResponse> getCoursePrice(@PathVariable String courseId){
        return ApiResponse.ok(courseService.getCoursePrice(courseId), SuccessCode.GET_COURSE_SUCCESS);
    }
    @PostMapping("/get-by-ids")
    ApiResponse<List<CourseResponse>> getCourseByIds(@RequestBody List<String> courseIds){
        return ApiResponse.ok(courseService.getCourseByIds(courseIds), SuccessCode.GET_COURSE_SUCCESS);
    }
}
