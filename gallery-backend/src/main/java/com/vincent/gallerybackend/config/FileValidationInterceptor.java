//package com.vincent.gallerybackend.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import java.util.List;
//
//@Component
//public class FileValidationInterceptor implements HandlerInterceptor {
//
//    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png");
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (request.getContentType() != null && request.getContentType().contains("multipart/form-data")) {
//            String fileType = request.getHeader("Content-Type");
//            if (!isAllowedFileType(fileType)) {
//                response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
//                response.getWriter().write("Unsupported file type: " + fileType);
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private boolean isAllowedFileType(String contentType) {
//        return ALLOWED_CONTENT_TYPES.contains(contentType);
//    }
//
//}
//
//
//
