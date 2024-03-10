package com.club.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2024年03月10日
 */
@RestController
@RequestMapping("/api/exciting")
@Tag(name = "精彩瞬间接口", description = "精彩瞬间接口")
public class ExcitingMomentsController extends BaseController {

}
