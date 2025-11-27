package com.raqaf.ecom.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dotmdrgex",
                "api_key", "647644367474549",
                "api_secret", "HUEjEPXHvsp_lnRxOQhx0KuJrTo",
                "secure", true
        ));
    }
}
