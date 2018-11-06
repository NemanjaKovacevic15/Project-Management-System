package com.get.util;

import org.springframework.stereotype.Component;
import com.get.model.StatusType;

@Component
public class HelperUtils {

    public StatusType[] getStatusTypes() {
        return StatusType.values();
    }
}
